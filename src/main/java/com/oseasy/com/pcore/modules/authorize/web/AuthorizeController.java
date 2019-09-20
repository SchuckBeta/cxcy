package com.oseasy.com.pcore.modules.authorize.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.utils.license.License;
import com.oseasy.com.pcore.common.utils.license.MachineCacheUtils;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.authorize.service.AuthorizeService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.rsa.Base64;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class AuthorizeController extends BaseController {
	public static final String GET_LICENSE_URL = CoreSval.getConfig("getLicenseUrl");
	private static Logger logger = LoggerFactory.getLogger(AuthorizeController.class);
	@Autowired
	private AuthorizeService authorizeService;

	@Autowired
	private SysTenantService sysTenantService;
	/**根据编号判断授权信息
	 * @param num MenuPlusEnum 枚举值序号从0开始
	 * @return
	 */
	@RequestMapping(value = {"${adminPath}/authorize/checkMenuByNum"})
	@ResponseBody
	public boolean adminCheckMenuByNum(Integer num) {
		return authorizeService.checkMenuByNum(num);
	}
	@RequestMapping(value = {"${frontPath}/authorize/checkMenuByNum"})
	@ResponseBody
	public boolean frontCheckMenuByNum(Integer num) {
		return authorizeService.checkMenuByNum(num);
	}
	private void cmodel(Model model,HttpServletRequest request) {
		authorizeService.putMachineInfo();
		String tenantId = null;
		String domain = request.getServerName();
		if(CoreSval.getTenantIsopen()){
			//按租户存储
			try {
				tenantId = (String) JedisUtils.hashGet("tenant",domain);
				if (tenantId == null){
					tenantId =sysTenantService.getDomainByName(domain);
				}
				if (tenantId != null){
					JedisUtils.hashSetKey("tenant",domain,tenantId);
				}
			} catch (Exception e) {
				logger.error("解析域名失败",e);
				throw new RuntimeException("获取域名失败");
			}
		}
		License license=authorizeService.getLicenseInfo(tenantId);
		model.addAttribute("valid","0");
		if (license!=null) {
			try {
				Date expiredDate=DateUtil.parseDate(license.getExpiredDate(),"yyyy-MM-dd HH:mm:ss");
				Date exp=DateUtil.addMonth(expiredDate, Integer.parseInt(license.getMonth()));

				if ("0".equals(license.getMonth())&&"1".equals(license.getValid())) {
					model.addAttribute("exp", "无期限");
				}else{
					if ("0".equals(license.getValid())) {
						model.addAttribute("exp", "");
					}else{
						model.addAttribute("exp", DateUtil.formatDate(exp , "yyyy-MM-dd HH:mm:ss"));
					}
				}
				model.addAttribute("valid",license.getValid());
			} catch (Exception e) {
				logger.error("错误:",e.getMessage());
			}
		}
		Cache<String, Object> cs=MachineCacheUtils.getCache();
		if (cs!=null&&cs.values()!=null) {
			model.addAttribute("count", cs.values().size());
		}
		model.addAttribute("getLicenseUrl",GET_LICENSE_URL);
		model.addAttribute("version", CoreSval.getConfig("version"));
	}
	@RequestMapping(value = {"${adminPath}/authorize/aboat"})
    public String aboat(Model model,HttpServletRequest request) {
		cmodel(model,request);
		return CoreSval.path.vms(CoreEmskey.AUTHORIZE.k()) + "/aboat";
	}
	@RequestMapping(value = "${adminPath}/authorize")
    public String accredit(Model model,HttpServletRequest request) {
		cmodel(model,request);
        return CoreSval.path.vms(CoreEmskey.AUTHORIZE.k()) + "/accredit";
    }
	@RequestMapping(value = "/notOpenTenant")
	public String notOpenTenant(){

		return CorePages.ERROR_NOTOPENTENANT.getIdxUrl();
	}

	@RequestMapping(value = "${adminPath}/authorize/inner")
    public String accreditInner(Model model,HttpServletRequest request) {
		cmodel(model,request);
        return CoreSval.path.vms(CoreEmskey.AUTHORIZE.k()) + "/accreditInner";
    }
	@RequestMapping(value = "${adminPath}/authorize/uploadLicense")
	@ResponseBody
	public JSONObject uploadLicense(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=authorizeService.uploadFile(request);
		return js;
	}
	@RequestMapping(value = "${adminPath}/authorize/donwLoadMachineInfo")
	public void donwLoadMachineInfo(HttpServletRequest request, HttpServletResponse response) {
		File file=null;
		BufferedWriter bw = null;
		InputStream in=null;
		OutputStream out=null;
		try {
		    file= new File(File.separator+FileUtil.TempFileDir+File.separator+UUID.randomUUID().toString().replace("-", "")+".mcd");
			if (!file.getParentFile().exists()) {
	            file.getParentFile().mkdirs();
			}
			if (file!=null && !file.exists()) {
					file.createNewFile();
	        }
			file.setWritable(true, false);
		    bw = new BufferedWriter(new FileWriter(file));
		    Cache<String, Object> cs=MachineCacheUtils.getCache();
		    JSONArray ja=new JSONArray();
		    for(Object o:cs.values()) {
		    	ja.add(o);
		    }
		    bw.write(Base64.encode(ja.toString().getBytes()));
		    bw.flush();

		    response.setContentType("multipart/form-data;charset=UTF-8");
	        response.setHeader("Content-Disposition", "attachment;filename="+file.getName());
	        //读取文件
	        in = new FileInputStream(file);
	        out = response.getOutputStream();

	        //写文件
	        int b;
	        while((b=in.read())!= -1) {
	            out.write(b);
	        }
	        out.flush();
		} catch (Exception e) {
			logger.error("下载机器信息出错",e);
		}finally {
			try {
				if (bw!=null) {
					bw.close();
				}
				if (in!=null) {
					in.close();
				}
				if (out!=null) {
					out.close();
				}
				if (file!=null)file.delete();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
	    String ss = "W3siY3B1IjoiYmZlYmZiZmYwMDA1MDZlMyIsIm1hYyI6IjFjMWIwZDc5YTZjOCJ9XQ==";
        byte[] ssDecodeBytes = Base64.decode(ss);
        String ssDecode = new String(ssDecodeBytes);
        System.out.println(ssDecode);
        System.out.println(JSONArray.fromObject(ssDecode));
//        System.out.println(new Date().getTime());
        }
}
