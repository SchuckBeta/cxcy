package com.oseasy.com.pcore.modules.authorize.service;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.license.License;
import com.oseasy.com.pcore.common.utils.license.MachineCacheUtils;
import com.oseasy.com.pcore.common.utils.machine.HardWareUtils;
import com.oseasy.com.pcore.common.utils.machine.MacUtil;
import com.oseasy.com.pcore.modules.authorize.entity.SysLicense;
import com.oseasy.com.pcore.modules.authorize.enums.MenuEnum;
import com.oseasy.com.pcore.modules.authorize.enums.MenuPlusEnum;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.authorize.vo.IAuthCheck;
import com.oseasy.com.pcore.modules.authorize.vo.Minfo;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.service.SysService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.rsa.Base64;
import com.oseasy.util.common.utils.rsa.RSAEncrypt;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class AuthorizeService {
	public static String cpu = null;
	public static String mac = null;
	public static final String product_key = "kaichuangla";
	public static final String pub_product_key = "pub_kaichuangla";
	public static final String LICENSE_CACHE = "licenseCache";

	public static final String OPERATION_TENANTID = "10";

	public static final String PROVINCE_TENANTID = "20";

	public static final String AUTHORIZATION_YES = "1";
	public static final String AUTHORIZATION_NO = "0";
	public static final char AUTHORIZATION_CODE = '1';

	public final static Logger logger = Logger.getLogger(AuthorizeService.class);
	@Autowired
	private SysLicenseService sysLicenseService;
	@Autowired
	private SysService sysService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private SysTenantService sysTenantService;


	@Transactional(readOnly = false)
	public void initInfo() {
//		initSysLicenseInfo();
		putMachineInfo();
	}

	/*@Transactional(readOnly = false)
	public void initSysLicenseInfo() {
		try {
			SysLicense s = sysLicenseService.getLicense();
//			List<SysLicense> sysLicenseList = sysLicenseService.getAllSysLicenses();
			if (s == null) {
				s = new SysLicense();
				s.setId(SysLicenseService.KEY);
				sysLicenseService.insertWithId(s);
			}
		} catch (Exception e1) {
			logger.error("初始化SysLicense出错", e1);
		}
	}*/

	public void putMachineInfo() {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return ;
		}
*/
		Minfo minfo = getMachineInfo();
		JSONObject js = JSONObject.fromObject(minfo);
		try {
			String cpu = minfo.getCpu();
			String mac = minfo.getMac();
			if (StringUtil.isEmpty(cpu)) {
				logger.error("无法获取CPU信息！系统将终止启动或无法正常使用");
				SysLicenseService.unValid = true;
				System.exit(0);
			}
			if (StringUtil.isEmpty(mac)) {
				logger.error("无法获取MAC信息！系统将终止启动或无法正常使用");
				SysLicenseService.unValid = true;
				System.exit(0);
			}
			if (!SysLicenseService.unValid) {
				MachineCacheUtils.put(cpu + mac, js.toString());
			}
		} catch (Exception e) {
			logger.error("获取机器信息出错！系统将终止启动或无法正常使用", e);
			SysLicenseService.unValid = true;
			System.exit(0);
		}
	}

	public License getLicenseInfo(String tenantId) {
		String key = LICENSE_CACHE;
		if (tenantId != null) {
			key = LICENSE_CACHE + ":" + tenantId;
		}
		String linfo = null;
		SysLicense o = (SysLicense) JedisUtils.hashGet(LICENSE_CACHE, key);
		Date sysDate = sysService.getDbCurDateYmdHms();
		if (o == null) {
			SysLicense sl = sysLicenseService.getLicense(tenantId);
			if (sl == null) {
				return null;
			} else {
				JedisUtils.hashSetKey(LICENSE_CACHE, key, sl);
				linfo = sl.getLicense();
			}
		} else {
			linfo = o.getLicense();
		}
		if (!StringUtil.isEmpty(linfo)) {
			//解码证书字段
			linfo = decrypt(linfo);
			if (linfo != null) {
				JSONObject jp = JSONObject.fromObject(linfo);
				//对解码后的信息验证 0-无效，1-有效，2-已过期
				jp.put("valid", validateLicense(linfo, sysDate));
				//获取机器码
				jp.put("machineCode", JSONArray.fromObject(jp.get("machineCode")));
				License license = (License) JSONObject.toBean(jp, License.class);
				return license;
			}
		}
		return null;
	}

	public static Minfo getMachineInfo() {
		Minfo minfo = new Minfo();
		if (cpu == null) {
			cpu = HardWareUtils.getCPUSerial();
			cpu = (cpu == null) ? "" : cpu;
		}
		if (mac == null) {
			mac = MacUtil.getMACAddress();
			mac = (mac == null) ? "" : mac;
		}
		minfo.setCpu(cpu);
		minfo.setMac(mac);
		logger.info("cpu:" + minfo.getCpu());
		logger.info("mac:" + minfo.getMac());
		return minfo;
	}

	@Transactional(readOnly = false)
	public JSONObject uploadFile(HttpServletRequest request) {
		JSONObject obj = new JSONObject();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		//读取上传的文件内容
		MultipartFile imgFile1 = multipartRequest.getFile("fileName");
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = imgFile1.getInputStream();

			reader = new BufferedReader(new InputStreamReader(is));
			String tempString = null;
			StringBuffer sb = new StringBuffer();
			Date sysDate = sysService.getDbCurDateYmdHms();
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			String decrypt_lic = decrypt(sb.toString());//解码文件内容
			String vaild = validateLicense(decrypt_lic, sysDate);
			if ("0".equals(vaild)) {
				obj.put("ret", "0");
				obj.put("msg", "无效的授权文件");
				return obj;
			} else if ("2".equals(vaild)) {
				obj.put("ret", "0");
				obj.put("msg", "授权文件已过期");
				return obj;
			} else if ("1".equals(vaild)) {
				String tenantId = null;
				String domain = request.getServerName();
				if (CoreSval.getTenantIsopen()) {
					//按租户存储
					try {
						tenantId = (String) JedisUtils.hashGet("tenant", domain);
						if (tenantId == null) {
							tenantId = sysTenantService.getDomainByName(domain);
						}
						if (tenantId != null) {
							JedisUtils.hashSetKey("tenant", domain, tenantId);
						}
					} catch (Exception e) {
						logger.error("解析域名失败", e);
						throw new RuntimeException("获取域名失败");
					}
				}
				String key = LICENSE_CACHE;
				if (tenantId != null) {
					key = LICENSE_CACHE + ":" + tenantId;
				}
				SysLicense sl = new SysLicense();
				sl.setLicense(sb.toString());
				sl.setTenantId(tenantId);
				sysLicenseService.saveLicense(sl);//保存未解码的文件内容
				JedisUtils.hashSetKey(LICENSE_CACHE, key, sl);
				obj.put("ret", "1");
				obj.put("msg", "授权成功");

				License license = getLicenseInfo(tenantId);
				if (license != null) {
					try {
						Date expiredDate = DateUtil.parseDate(license.getExpiredDate(), "yyyy-MM-dd HH:mm:ss");
						Date exp = DateUtil.addMonth(expiredDate, Integer.parseInt(license.getMonth()));
						obj.put("exp", DateUtil.formatDate(exp, "yyyy-MM-dd HH:mm:ss"));
					} catch (Exception e) {
						logger.error("错误:", e);
					}
				}

			}
			return obj;
		} catch (Exception e) {
			logger.error("无效的授权文件", e);
			obj.put("ret", "0");
			obj.put("msg", "无效的授权文件");
			return obj;
		} finally {
			try {
				is.close();
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*对解码后的信息验证 0-无效，1-有效，2-已过期*/
	public static String validateLicense(String decrypt_lic, Date sysDate) {
	/*	//现阶段屏蔽证书
        if(License.isOpen()){
          return "1";
        }*/

		try {
			if (decrypt_lic != null) {
				JSONObject jp = JSONObject.fromObject(decrypt_lic);
				jp.put("machineCode", JSONArray.fromObject(jp.get("machineCode")));
				License license = (License) JSONObject.toBean(jp, License.class);
				//验证产品key是否一致,如果开启了租户，则pub
				if (CoreSval.getTenantIsopen()) {
					if (!license.getProduct_key().equals(pub_product_key)) {
						return "0";
					}
				}

				//试用授权，不验证机器码
				if ("0".equals(license.getProductType())) {

				} else {
					//验证机器信息是否一致
					Minfo minfo = getMachineInfo();
					JSONObject m = JSONObject.fromObject(minfo);
					String cpu = minfo.getCpu();
					String mac = minfo.getMac();
					int tag = 0;
					JSONArray ja = license.getMachineCode();
					for (int i = 0; i < ja.size(); i++) {
						JSONObject mi = ja.getJSONObject(i);
						if (cpu.equals(mi.getString("cpu")) && mac.equals(mi.getString("mac"))) {
							tag = 1;
							break;
						}
					}
					if (tag == 0) {
						return "0";
					}
				}
				//验证modules值是否正确
				String regex = "[01]+";
				if (StringUtil.isEmpty(license.getModules()) || !Pattern.matches(regex, license.getModules())) {
					return "0";
				}
				//验证日期是否有效
				logger.info("expoirtedate:" + DateUtil.addMonth(DateUtil.parseDate(license.getExpiredDate(), "yyyy-MM-dd HH:mm:ss"), Integer.parseInt(license.getMonth())));
				logger.info("sysDate:" + sysDate);

				if ("0".equals(license.getMonth())) {//0无期限
				} else if (DateUtil.addMonth(DateUtil.parseDate(license.getExpiredDate(), "yyyy-MM-dd HH:mm:ss"), Integer.parseInt(license.getMonth())).before(sysDate)) {
					return "2";
				}
				return "1";
			}
		} catch (Exception e) {
			return "0";
		}
		return "0";
	}

	/*解码*/
	public String decrypt(String lic) {
		try {
			byte[] res;
			res = RSAEncrypt.decryptPlus(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(this.getClass().getResource("/key/publicKey.keystore").getFile())), Base64.decode(lic));
			String restr = new String(res, "UTF-8");
			return restr;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean checkMenu(String id) {
	/*	//现阶段屏蔽证书
        if(License.isOpen()){
            return true;
        }*/
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		if (license.getModules().length() == 9) {
			//这里要改为动态获取菜单，前5个位基础版菜单，后3个位扩展版
			int index = MenuEnum.getIndexById(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 0 && index <= 4)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		} else {
			int index = MenuPlusEnum.getIndexById(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 50 && index <= 54)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		}

	}

	/**
	 * 租户版校验菜单
	 *
	 * @param id
	 * @return
	 */
	public boolean checkMenuOfTenant(String id) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		Menu menu = systemService.getMenuById(id);
		if (menu == null) {
			return false;
		}
		Integer ltype = menu.getLtype();
		if (ltype == null) {
			return false;
		}
		TenantMenu tenantMenu = TenantMenu.getById(ltype);
		if (tenantMenu == null) {
			return false;
		}
		if (tenantId.equals(OPERATION_TENANTID)) {
			return true;
		}
		if (ltype < 0 || license.getModules().length() <= ltype) {
			return false;
		}
		if ((ltype >= 51 && ltype <= 55)) {
			return true;
		}
		if (tenantId.equals(PROVINCE_TENANTID)) {
			if ((ltype >= 60 && ltype <= 63)) {
				return true;
			}
		}
		if (AUTHORIZATION_YES.equals(license.getValid()) && license.getModules().charAt(ltype) == AUTHORIZATION_CODE) {
			return true;
		}
		return false;


	}


	public boolean checkMenuOfTenant(TenantMenu tenantMenu) {
	/*	//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || AUTHORIZATION_NO.equals(license.getValid())) {
			return false;
		}
		//这里要改为动态获取菜单，前5个位基础版菜单，后3个位扩展版
		int index = tenantMenu.getId();
		if (index == -1 || license.getModules().length() <= index) {
			return false;
		}
		if (getAuthorizeResult(tenantId, license, index)) {
			return true;
		}
		return false;
	}

	public boolean getAuthorizeResult(String tenantId, License license, int index) {
		if (tenantId.equals(OPERATION_TENANTID)) {
			return true;
		}
		if ((index >= 51 && index <= 55)) {
			return true;
		}
		if (tenantId.equals(PROVINCE_TENANTID)) {
			if ((index >= 60 && index <= 63)) {
				return true;
			}
		}
		if (AUTHORIZATION_YES.equals(license.getValid()) && license.getModules().charAt(index) == AUTHORIZATION_CODE) {
			return true;
		}
		return false;
	}

	//校验子菜单
	public boolean checkChildMenu(String id) {
       /* if(License.isOpen()){
            return true;
        }*/
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		if (license.getModules().length() == 9) {
			int index = MenuEnum.getIndexByChildMenuId(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 0 && index <= 4)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		} else {
			int index = MenuPlusEnum.getIndexByChildMenuId(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 50 && index <= 54)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		}

	}


	/**
	 * 新版本用59位
	 *
	 * @param id 子菜单Id
	 * @return
	 */
	public boolean checkChildMenuOfTenant(String id) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		Menu menu = systemService.getMenuById(id);
		if (menu == null) {
			return false;
		}
		Integer ltype = menu.getLtype();
		if (ltype == null) {
			return false;
		}
		TenantMenu tenantMenu = TenantMenu.getById(ltype);
		if (tenantMenu == null) {
			return false;
		}
		if (ltype < 0 || license.getModules().length() <= ltype) {
			return false;
		}
		if (getAuthorizeResult(tenantId, license, ltype)) {
			return true;
		}
		return false;


	}

	public boolean checkCategory(String id) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/


		if (!License.isOpen()) {
			return true;
		}
		String tenantId = TenantConfig.getCacheTenant();
		License license = getLicenseInfo(tenantId);
		if (license == null || "0".equals(license.getValid())) {
			return false;
		}
		//核对栏目
		if (license.getModules().length() == 9) {
			int index = MenuEnum.getIndexByCategoryId(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 0 && index <= 4)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		} else {
			//核对栏目
			int index = MenuPlusEnum.getIndexByCategoryId(id);
			if (index == -1 || license.getModules().length() <= index) {
				return false;
			}
			if ((index >= 50 && index <= 54)) {
				return true;
			}
			if ("1".equals(license.getValid()) && license.getModules().charAt(index) == '1') {
				return true;
			}
			return false;
		}
	}

	/**
	 * 校验栏目
	 *
	 * @return
	 */
	public boolean checkCategoryOfTenant(String id) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		if ((CoreIds.NCE_SYS_TREE_ROOT.getId()).equals(id)) {
			return true;
		}
		Boolean flag = false;
		try {
			flag = IAuthCheck.runcheck(IAuthCheck.AuthCtype.CTEGROY, id);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 根据编号判断授权信息
	 *
	 * @param num MenuPlusEnum 枚举值序号从0开始
	 * @return
	 */
	public boolean checkMenuByNum(Integer num) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		if (num != null) {
			String id = null;
			if (num == 0) {
				id = MenuPlusEnum.S0.getId();
			} else if (num == 1) {
				id = MenuPlusEnum.S1.getId();
			} else if (num == 2) {
				id = MenuPlusEnum.S2.getId();
			} else if (num == 3) {
				id = MenuPlusEnum.S3.getId();
			} else if (num == 4) {
				id = MenuPlusEnum.S4.getId();
			} else if (num == 5) {
				id = MenuPlusEnum.S5.getId();
			} else if (num == 6) {
				id = MenuPlusEnum.S6.getId();
			} else if (num == 7) {
				id = MenuPlusEnum.S7.getId();
			} else if (num == 8) {
				id = MenuPlusEnum.S8.getId();
			}
			return checkMenu(id);
		} else {
			return false;
		}
	}


	public boolean checkMenuByNumOfTenant(Integer num) {
		/*//现阶段屏蔽证书
		if(License.isOpen()){
			return true;
		}*/
		if (num != null) {
			Integer ltype = null;
			switch (num) {
				case 0:
					ltype = getMenu(TenantMenu.S0).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S0);
					}
				case 1:
					ltype = getMenu(TenantMenu.S1).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S1);
					}
				case 2:
					ltype = getMenu(TenantMenu.S2).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S2);
					}
				case 3:
					ltype = getMenu(TenantMenu.S3).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S3);
					}
				case 4:
					ltype = getMenu(TenantMenu.S4).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S4);
					}
				case 5:
					ltype = getMenu(TenantMenu.S5).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S5);
					}
				case 6:
					ltype = getMenu(TenantMenu.S6).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S6);
					}
				case 7:
					ltype = getMenu(TenantMenu.S7).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S7);
					}
				case 8:
					ltype = getMenu(TenantMenu.S8).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S8);
					}
				case 9:
					ltype = getMenu(TenantMenu.S9).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S9);
					}
				case 10:
					ltype = getMenu(TenantMenu.S10).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S10);
					}
				case 11:
					ltype = getMenu(TenantMenu.S11).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S11);
					}
				case 12:
					ltype = getMenu(TenantMenu.S12).getLtype();
					if (ltype != null) {
						return checkMenuOfTenant(TenantMenu.S12);
					}
				default:
					return false;
			}

		} else {
			return false;
		}
	}

	public Menu getMenu(TenantMenu tenantMenu) {
		Menu m = new Menu();
		m.setTenantId(CoreSval.getCurrpntplTenantByType(CoreUtils.getUser()));
		if (StringUtil.isEmpty(m.getTenantId())) {
			return null;
		}
		//获取模板菜单
		m.setLtype(tenantMenu.getId());
		Menu tpl = systemService.getByLtype(m);
		if (CoreIds.checkTpl(m.getTenantId())) {
			return tpl;
		}
		// 获取当前租户对应的发布菜单.要改
		Menu cur = new Menu();
		cur.setLtype(tpl.getLtype());
		return systemService.getByLtype(cur);

	}

}