package com.oseasy.pro.modules.cert.service;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.common.vsftp.config.Global;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.common.utils.SendMailUtil;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.modules.cert.dao.SysCertDao;
import com.oseasy.pro.modules.cert.dao.SysCertFlowDao;
import com.oseasy.pro.modules.cert.entity.CertElement;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.pro.modules.cert.entity.SysCertElement;
import com.oseasy.pro.modules.cert.entity.SysCertFlow;
import com.oseasy.pro.modules.cert.entity.SysCertIns;
import com.oseasy.pro.modules.cert.entity.SysCertPage;
import com.oseasy.pro.modules.cert.entity.SysCertPageIns;
import com.oseasy.pro.modules.cert.enums.ColEnum;
import com.oseasy.pro.modules.cert.utils.SysCertParam;
import com.oseasy.pro.modules.cert.utils.SysCertUtil;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;
import com.oseasy.pro.modules.cert.vo.SysCertPageInsVo;
import com.oseasy.pro.modules.cert.vo.SysCertPageVo;
import com.oseasy.pro.modules.cert.vo.SysCertVo;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.util.common.exception.UtilRunException;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.image.ImageUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.mail.EmailAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 证书模板Service.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class SysCertService extends CrudService<SysCertDao, SysCert> {
    @Autowired
	private SysCertPageService sysCertPageService;
	@Autowired
	private SysCertElementService sysCertElementService;
	@Autowired
	private ProSysAttachmentService proSysAttachmentService;
	@Autowired
	private SysCertFlowDao sysCertFlowDao;
	@Autowired
	private ProModelService proModelService;
	@Autowired
	private SysCertInsService sysCertInsService;
	@Autowired
	private SysCertPageInsService sysCertPageInsService;
	@Autowired
	private OaNotifyService oaNotifyService;
    public List<Map<String,String>> getCertListWithFlow(String flow){
    	return dao.getCertListWithFlow(flow);
    }
    public List<SysCertPageInsVo> getCertIns(String certinsid){
    	return sysCertPageInsService.getSysCertPageIns(certinsid);
    }
	public SysCert get(String id) {
		return super.get(id);
	}
	/**查询需要生成证书的数据
	 * @param flow
	 * @param node
	 * @return key-证书模板关联关系id，项目id 的List
	 */
	public Map<String,List<String>> getMdCertFlowsForJob(String flow,String node){
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		//查询关联了证书的节点，并且当前时间是节点结束的第二天
		List<SysCertFlow> list=sysCertFlowDao.getCertFlows(flow, node);
		if(list!=null&&list.size()>0){
			for(SysCertFlow scf:list){
				List<String> ids=proModelService.findIdsPassGnode(scf.getFlow(), scf.getNode(), "1");
				if(ids!=null&&ids.size()>0){
					List<String> pids =sysCertInsService.getPidsByFlowNode(scf.getFlow(), scf.getNode());
					if(ids!=null&&ids.size()>0){
						ids.removeAll(pids);//去除已生成过证书的
					}
					if(ids!=null&&ids.size()>0){
						map.put(scf.getId(), ids);
					}
				}
			}
		}
		return map;
	}
	/**查询需要生成证书的数据
	 * @param flow
	 * @param certid
	 * @return key-证书模板关联关系id，项目id 的List
	 */
	public Map<String,List<String>> getMdCertFlows(String flow,String certid){
		Map<String,List<String>> map=new HashMap<String,List<String>>();
		List<SysCertFlow> list=sysCertFlowDao.getCertFlowsWithCid(flow, certid);
		if(list!=null&&list.size()>0){
			for(SysCertFlow scf:list){
				List<String> ids=proModelService.findIdsPassGnode(scf.getFlow(), scf.getNode(), "1");
				if(ids!=null&&ids.size()>0){
					List<String> pids =sysCertInsService.getPidsByFlowNode(scf.getFlow(), scf.getNode());
					if(ids!=null&&ids.size()>0){
						ids.removeAll(pids);//去除已生成过证书的
					}
					if(ids!=null&&ids.size()>0){
						map.put(scf.getId(), ids);
					}
				}
			}
		}
		return map;
	}
	public Map<String, String> getProjectData(String pid){
		Date nowd=new Date();
		Map<String, String> map=dao.getProjectData(pid);
		String sub=map.get("subtime");
		String now=DateUtil.formatDate(nowd, "yyyy年MM月");
		if(StringUtil.isNotEmpty(sub)){
			map.put(ColEnum.C9.getValue(), sub+"-"+now);
		}
		map.put(ColEnum.C10.getValue(), StringUtil.getUpperYearAndMonth(DateUtil.formatDate(nowd, "yyyy"),DateUtil.formatDate(nowd, "M")));
		map.put(ColEnum.C11.getValue(), StringUtil.getUpperYear(DateUtil.formatDate(nowd, "yyyy")));
		map.put(ColEnum.C12.getValue(), StringUtil.getUpperMonth(DateUtil.formatDate(nowd, "M")));
		map.put(ColEnum.C13.getValue(), DateUtil.formatDate(nowd, "yyyy"));
		map.put(ColEnum.C14.getValue(), DateUtil.formatDate(nowd, "MM"));
		return map;
	}
	public Map<String, String> getCertViewData(){
		Date nowd=new Date();
		Map<String, String> map=new HashMap<String,String>();
		for(ColEnum c:ColEnum.values()){
			map.put(c.getValue(), c.getName());
		}
		map.put(ColEnum.C10.getValue(), StringUtil.getUpperYearAndMonth(DateUtil.formatDate(nowd, "yyyy"),DateUtil.formatDate(nowd, "M")));
		map.put(ColEnum.C11.getValue(), StringUtil.getUpperYear(DateUtil.formatDate(nowd, "yyyy")));
		map.put(ColEnum.C12.getValue(), StringUtil.getUpperMonth(DateUtil.formatDate(nowd, "M")));
		map.put(ColEnum.C13.getValue(), DateUtil.formatDate(nowd, "yyyy"));
		map.put(ColEnum.C14.getValue(), DateUtil.formatDate(nowd, "MM"));
		return map;
	}
	//生成证书
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void createMdCert(User send,List<CertPage> cps,SysCert cert,SysCertFlowVo scf,String pid,String tempRootPath) throws Exception{
		//保存主表信息
		SysCertIns sysCertIns =new SysCertIns();
		sysCertIns.setCertId(cert.getId());
		sysCertIns.setCertName(cert.getName());
		sysCertIns.setFlow(scf.getFlow());
		sysCertIns.setGnode(scf.getNode());
		sysCertIns.setProid(pid);
		try {
			sysCertInsService.save(sysCertIns);
		} catch (DuplicateKeyException e) {
			throw new UtilRunException("证书生成失败，该节点已有证书");
		}
		//保存子表信息
		List<String> cpi_ids=new ArrayList<String>();
		for(int i=0;i<cps.size();i++){
			SysCertPageIns scpi=new SysCertPageIns();
			scpi.setCertInsId(sysCertIns.getId());
			scpi.setName(cps.get(i).getCertpagename());
			scpi.setSort(i+"");
			sysCertPageInsService.save(scpi);
			cpi_ids.add(scpi.getId());
		}
		Map<String, String> map=getProjectData(pid);//变量对应值
		List<EmailAttachment> eas=new ArrayList<EmailAttachment>();//发邮件附件
		String tempPath = tempRootPath + File.separator + IdGen.uuid();// 生成的文件所在目录
		for(int i=0;i<cps.size();i++){
			CertPage cp=cps.get(i);
			//生成证书的图片
			String outImg=tempPath+File.separator+IdGen.uuid()+"."+ImageUtil.PICTRUE_FORMATE_JPG;
			SysCertUtil.createCertInsPic(map,cp,tempRootPath,tempPath,outImg);
			File outImgFile=new File(outImg);
			if(outImgFile.exists()){
				//邮件附件
				EmailAttachment attachment = new EmailAttachment();//创建附件
		        attachment.setPath(outImg);//本地附件，绝对路径
		        attachment.setDisposition(EmailAttachment.ATTACHMENT);
		        attachment.setName(cp.getCertpagename()+"."+ImageUtil.PICTRUE_FORMATE_JPG);//附件名称
		        eas.add(attachment);

		        //上传ftp并保存SysAttachment
				String ftpPath = Global.REMOTEPATH+ SysCertParam.FTP_FOLDER + "/" + DateUtil.getDate("yyyy-MM-dd");
				String realName = outImg.substring(outImg.lastIndexOf(File.separator) + 1);
				SysAttachment sa = new SysAttachment();
				sa.setUid(cpi_ids.get(i));
				sa.setName(cp.getCertpagename());
				sa.setGnodeId(null);
				sa.setSuffix(ImageUtil.PICTRUE_FORMATE_JPG);
				sa.setSize(outImgFile.length()+"");
				sa.setType(FileTypeEnum.S_SYS_CERT);
				sa.setFileStep(FileStepEnum.S10003);
				sa.setUrl("/"+ TenantConfig.getCacheTenant()+ftpPath+"/"+realName);
				proSysAttachmentService.save(sa);
				VsftpUtils.uploadFile(ftpPath,realName, outImgFile);
			}else{
				throw new UtilRunException("证书生成失败");
			}
		}
		//发送邮件
		if(Const.YES.equals(SysCertParam.Email_isSend)){
			try {
				String email=map.get("email");
				if(StringUtil.isNotEmpty(email)){
					Map<String, Object> emap = new HashMap<String, Object>();
					emap.put("projectname", map.get(ColEnum.C1.getValue()));
					emap.put("certname", cert.getName());
					SendMailUtil.sendFtlMail(email, "恭喜你获得"+cert.getName(), SysCertParam.TemplatePath, emap,eas);
				}
			} catch (Exception e) {
				logger.error("证书下发邮件发送失败",e);
			}
		}
		//发送系统消息
		try {
			if(send!=null){
				User rec_User=UserUtils.get(map.get("leader"));
				oaNotifyService.sendOaNotifyByType(send,rec_User,"证书颁发",
					"你的项目"+map.get(ColEnum.C1.getValue())+"获得"+cert.getName(), OaNotify.Type_Enum.TYPE16.getValue(),cert.getId());
			}
		} catch (Exception e) {
			logger.error("证书下发系统消息发送失败",e);
		}
		deleteFileOrDir(new File(tempPath));
	}
	public List<SysCert> findList(SysCert sysCert) {
		return super.findList(sysCert);
	}

	public Page<SysCert> findPage(Page<SysCert> page, SysCert sysCert) {
		return super.findPage(page, sysCert);
	}
	public List<CertPage> getCertPages(String certid) {
		List<CertPage> ret=new ArrayList<CertPage>();
		List<SysCertPage> list=sysCertPageService.getSysCertPages(certid);
		if(list!=null&&list.size()>0){
			for(SysCertPage scp:list){
				ret.add(getCertPage(scp.getId()));
			}
		}
		return ret;
	}
	public CertPage getCertPage(String pageid) {
		CertPage cp=dao.getCertPage(pageid);
		List<SysCertElement> list=sysCertElementService.getCertPage(pageid);
		if(list!=null&&list.size()>0){
			List<CertElement> l=new ArrayList<CertElement>();
			for(SysCertElement s:list){
				String content=s.getContent();
				if(StringUtil.isNotEmpty(content)){
					CertElement ce=(CertElement)JSONObject.toBean(JSONObject.fromObject(content), CertElement.class);
					ce.setUrl(FtpUtil.ftpImgUrl(ce.getUrl()));
					l.add(ce);
				}
			}
			cp.setList(l);
		}
		return cp;
	}
	public List<SysCertVo> findAllList(){
		List<SysCertVo> list=dao.getMyPageList(new SysCertVo());
		if(list!=null&&list.size()>0){
			List<String> ids = new ArrayList<String>();
			for (SysCertVo v : list) {
				ids.add(v.getId());
			}
			Map<String,List<SysCertPageVo>> pmap=new HashMap<String,List<SysCertPageVo>>();
			List<SysCertPageVo> listp=dao.getCertPageVoList(ids);
			if(listp!=null&&listp.size()>0){
				for(SysCertPageVo v:listp){
					List<SysCertPageVo> tem=pmap.get(v.getCertid());
					if(tem==null){
						tem=new ArrayList<SysCertPageVo>();
						pmap.put(v.getCertid(), tem);
					}
					if(StringUtil.isNotEmpty(v.getImgUrl())){
						v.setImgUrl(FtpUtil.ftpImgUrl(v.getImgUrl()));
					}
					tem.add(v);
				}
			}

			Map<String,List<SysCertFlowVo>> fmap=new HashMap<String,List<SysCertFlowVo>>();
			List<SysCertFlowVo> listf=dao.getCertFlowVoList(ids);
			if(listf!=null&&listf.size()>0){
				for(SysCertFlowVo v:listf){
					List<SysCertFlowVo> tem=fmap.get(v.getCertId());
					if(tem==null){
						tem=new ArrayList<SysCertFlowVo>();
						fmap.put(v.getCertId(), tem);
					}
					tem.add(v);
				}
			}
			for (SysCertVo v : list) {
				v.setScp(pmap.get(v.getId()));
				v.setScf(fmap.get(v.getId()));
			}
		}
		return list;
	}
	public Page<SysCertVo> findPageVo(Page<SysCertVo> page, SysCertVo vo) {
		vo.setPage(page);
		page.setList(dao.getMyPageList(vo));
		if(page.getList()!=null&&page.getList().size()>0){
			List<String> ids = new ArrayList<String>();
			for (SysCertVo v : page.getList()) {
				ids.add(v.getId());
			}
			Map<String,List<SysCertPageVo>> pmap=new HashMap<String,List<SysCertPageVo>>();
			List<SysCertPageVo> listp=dao.getCertPageVoList(ids);
			if(listp!=null&&listp.size()>0){
				for(SysCertPageVo v:listp){
					List<SysCertPageVo> tem=pmap.get(v.getCertid());
					if(tem==null){
						tem=new ArrayList<SysCertPageVo>();
						pmap.put(v.getCertid(), tem);
					}
					if(StringUtil.isNotEmpty(v.getImgUrl())){
						v.setImgUrl(FtpUtil.ftpImgUrl(v.getImgUrl()));
					}
					tem.add(v);
				}
			}

			Map<String,List<SysCertFlowVo>> fmap=new HashMap<String,List<SysCertFlowVo>>();
			List<SysCertFlowVo> listf=dao.getCertFlowVoList(ids);
			if(listf!=null&&listf.size()>0){
				for(SysCertFlowVo v:listf){
					List<SysCertFlowVo> tem=fmap.get(v.getCertId());
					if(tem==null){
						tem=new ArrayList<SysCertFlowVo>();
						fmap.put(v.getCertId(), tem);
					}
					tem.add(v);
				}
			}
			for (SysCertVo v : page.getList()) {
				v.setScp(pmap.get(v.getId()));
				v.setScf(fmap.get(v.getId()));
			}
		}
		return page;
	}




	@Transactional(readOnly = false)
	public void save(SysCert sysCert) {
		super.save(sysCert);
	}

	@Transactional(readOnly = false)
	public void delete(SysCert sysCert) {
		super.delete(sysCert);
	}
	@Transactional(readOnly = false)
	public void unrelease(String id){
		dao.unrelease(id);
	}
	@Transactional(readOnly = false)
	public void release(String id){
		dao.release(id);
	}
	@Transactional(readOnly = false)
	public void deleteCertPage(String id){
		SysAttachment s=new SysAttachment();
		s.setUid(id);
		proSysAttachmentService.deleteByCdn(s);
		sysCertPageService.deleteWL(new SysCertPage(id));
	}
	@Transactional(readOnly = false)
	public int deleteAll(String ids){
		return dao.deleteAll(ids);
	}
	@SuppressWarnings("rawtypes")
	public void preview(@RequestBody JSONObject requestBody,HttpServletResponse response)throws Exception{
		if(requestBody==null){
			throw new UtilRunException("预览失败，无效的数据");
		}
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("list", CertElement.class);
		CertPage cp=(CertPage)JSONObject.toBean(requestBody, CertPage.class,classMap);//前台json参数转换为bean

		for(CertElement el:cp.getList()){
			String url=el.getUrl();
			if(StringUtil.isNotEmpty(url)){
				url=url.split("\\?")[0];
				el.setUrl(FtpUtil.getFtpPath(url));
			}
		}
		//生成模板的图片
		String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
		String outImg=tempPath+File.separator+IdGen.uuid()+"."+ImageUtil.PICTRUE_FORMATE_JPG;
		SysCertUtil.createCertViewPic(getCertViewData(),cp, tempPath,outImg);
		File outImgFile=new File(outImg);
		OutputStream out=null;
		FileInputStream inputStream=null;
		try {
			//读取本地图片输入流
			inputStream = new FileInputStream(outImgFile);
			int i = inputStream.available();
			//byte数组用于存放图片字节数据
			byte[] buff = new byte[i];
			inputStream.read(buff);
			//记得关闭输入流
			inputStream.close();
			//设置发送到客户端的响应内容类型
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			out.write(buff);
		}finally {
			//关闭响应输出流
			if(out!=null){
				out.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
		}
		deleteFileOrDir(new File(tempPath));
	}
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void savePageName(String pageid,String pagename){
		sysCertPageService.savePageName(pageid, pagename);
	}
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public Map<String,String> saveCert(JSONObject requestBody) throws Exception {
		if(requestBody==null){
			throw new UtilRunException("保存失败，无效的数据");
		}
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("list", CertElement.class);
		CertPage cp=(CertPage)JSONObject.toBean(requestBody, CertPage.class,classMap);//前台json参数转换为bean
		//保存证书模板主表
		SysCert cert=null;
		String certid=cp.getCertid();
		if(StringUtil.isEmpty(certid)){//没有证书id，说明是新增
			cert=new SysCert();
			cert.setReleases(Const.NO);
		}else{
			cert=get(certid);
			if(cert==null||Const.YES.equals(cert.getDelFlag())){
				throw new UtilRunException("保存失败，证书已被删除");
			}
		}
		cert.setName(cp.getCertname());
		save(cert);

		//保存证书模板页面
		SysCertPage certPage=null;
		String certpageid=cp.getCertpageid();
		if(StringUtil.isEmpty(certpageid)){//没有证书页面id，说明是新增
			certPage=new SysCertPage();
			Integer maxsort=sysCertPageService.getMaxSort(cert.getId());
			if(maxsort==null){
				maxsort=0;
			}else{
				maxsort=maxsort+1;
			}
			certPage.setCertId(cert.getId());
			certPage.setSort(maxsort);
		}else{
			certPage=sysCertPageService.get(certpageid);
			if(certPage==null||Const.YES.equals(certPage.getDelFlag())){
				throw new UtilRunException("保存失败，证书页已被删除");
			}
		}
		certPage.setName(cp.getCertpagename());
		certPage.setHtml(cp.getUiHtml());
		certPage.setWidth(cp.getWidth());
		certPage.setHeight(cp.getHeight());
		certPage.setUiJson(cp.getUiJson());
		sysCertPageService.save(certPage);

		Map<String,String> retm=new HashMap<String,String>();
		retm.put("certid", cert.getId());
		retm.put("certpageid", certPage.getId());

		//保存证书模板页面元素
		Map<String,String> map=saveElement(certPage,cp);
		if(map!=null&&!map.isEmpty()){//有temp url需要替换
			String html=certPage.getHtml();
			for(String k:map.keySet()){
				html=html.replaceAll(SysAttachmentService.escapeExprSpecialWord(k),map.get(k));
			}
			certPage.setHtml(html);
			sysCertPageService.save(certPage);//更新
		}
		//生成模板的图片
		String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
		try{
			//生成预览图
			String outImg=tempPath+File.separator+IdGen.uuid()+"."+ImageUtil.PICTRUE_FORMATE_JPG;
			SysCertUtil.createCertViewPic(getCertViewData(),cp, tempPath,outImg);
			File outImgFile=new File(outImg);
			if(outImgFile.exists()){
				//删除上次生成的图片
				SysAttachment s=new SysAttachment();
				s.setUid(certPage.getId());
				s.setType(FileTypeEnum.S_SYS_CERT);
				s.setFileStep(FileStepEnum.S10002);
				proSysAttachmentService.deleteByCdn(s);
				//生成新图片
				String ftpPath = Global.REMOTEPATH+ SysCertParam.FTP_FOLDER + "/" + DateUtil.getDate("yyyy-MM-dd");
				String realName = outImg.substring(outImg.lastIndexOf(File.separator) + 1);
				SysAttachment sa = new SysAttachment();
				sa.setUid(certPage.getId());
				sa.setName(certPage.getName());
				sa.setGnodeId(null);
				sa.setSuffix(ImageUtil.PICTRUE_FORMATE_JPG);
				sa.setSize(outImgFile.length()+"");
				sa.setType(FileTypeEnum.S_SYS_CERT);
				sa.setFileStep(FileStepEnum.S10002);
				sa.setUrl("/"+ TenantConfig.getCacheTenant()+ftpPath+"/"+realName);
				proSysAttachmentService.save(sa);
				VsftpUtils.uploadFile(ftpPath,realName, outImgFile);
			}
			//生成证书页下发图
			String outImgTempl=tempPath+File.separator+IdGen.uuid()+"."+ImageUtil.PICTRUE_FORMATE_JPG;
			SysCertUtil.createCertPic(cp, tempPath,outImgTempl);
			File outImgFileTempl=new File(outImgTempl);
			if(outImgFileTempl.exists()){
				//删除上次生成的图片
				SysAttachment s=new SysAttachment();
				s.setUid(certPage.getId());
				s.setType(FileTypeEnum.S_SYS_CERT);
				s.setFileStep(FileStepEnum.S10004);
				proSysAttachmentService.deleteByCdn(s);
				//生成新图片
				String ftpPath = Global.REMOTEPATH+ SysCertParam.FTP_FOLDER + "/" + DateUtil.getDate("yyyy-MM-dd");
				String realName = outImgTempl.substring(outImgTempl.lastIndexOf(File.separator) + 1);
				SysAttachment sa = new SysAttachment();
				sa.setUid(certPage.getId());
				sa.setName(certPage.getName());
				sa.setGnodeId(null);
				sa.setSuffix(ImageUtil.PICTRUE_FORMATE_JPG);
				sa.setSize(outImgFileTempl.length()+"");
				sa.setType(FileTypeEnum.S_SYS_CERT);
				sa.setFileStep(FileStepEnum.S10004);
				sa.setUrl("/"+ TenantConfig.getCacheTenant()+ftpPath+"/"+realName);
				proSysAttachmentService.save(sa);
				VsftpUtils.uploadFile(ftpPath,realName, outImgFileTempl);
			}
		}finally{
			deleteFileOrDir(new File(tempPath));
		}
		return retm;
	}
	private void deleteFileOrDir(File dir) {
		if(dir!=null){
			if (dir.isDirectory()&&dir.list().length>0) {
	            for (String fps:dir.list()) {
	            	deleteFileOrDir(new File(dir,fps));
	            }
	        }
	        dir.delete();
		}
	}
	//保存证书模板页面元素
	private Map<String,String> saveElement(SysCertPage certPage,CertPage cp) throws Exception{
		sysCertElementService.deleteByPageid(certPage.getId());
		List<CertElement> els=cp.getList();
		if(els==null||els.size()==0){//无页面元素
			SysAttachment s=new SysAttachment();
			s.setUid(certPage.getId());
			proSysAttachmentService.deleteByCdn(s);
			return null;
		}else{
			Map<String,String> map = proSysAttachmentService.moveAndSaveTempFile(els, certPage.getId(), FileTypeEnum.S_SYS_CERT, FileStepEnum.S10001);//移动每个元素中temp url
			for(CertElement el:els){
				String url=el.getUrl();
				if(StringUtil.isNotEmpty(url)&&map!=null&&map.get(url)!=null){//map里能找到对应的替换url
					el.setUrl(map.get(url));
				}else{
					el.setUrl(FtpUtil.getFtpPath(el.getUrl()));
				}
				SysCertElement sce=new SysCertElement();
				sce.setCertPageId(certPage.getId());
				JSONObject json = JSONObject.fromObject(el);//将java对象转换为json对象
				sce.setContent(json.toString());
				sce.setSort(el.getSort());
				sce.setElementType(el.getElementType());
				sysCertElementService.save(sce);
			}
			return map;
		}
	}
  	@Transactional(readOnly = false)
  	public void deleteWL(SysCert sysCert) {
  	  dao.deleteWL(sysCert);
  	}

	@Transactional(readOnly = false)
	public void editSysCertName(SysCertVo sysCert) {
		dao.editSysCertName(sysCert);
	}
}