package com.oseasy.pro.modules.cert.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.cert.entity.CertMakeInfo;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 下发证书进度信息Service.
 * @author 奔波儿灞
 * @version 2018-03-02
 */
@Service
public class CertMakeService{
	public final static Logger logger = Logger.getLogger(CertMakeService.class);
	@Autowired
	private SysCertService sysCertService;
	@Autowired
	private CertMakeInfoService certMakeInfoService;
	@Autowired
	private SysCertFlowService sysCertFlowService;
	public JSONObject doCertMake(String actywId,String certid){
		JSONObject js=new JSONObject();
		js.put("ret", 0);
		if(StringUtil.isEmpty(actywId)||StringUtil.isEmpty(certid)){
			js.put("msg", "参数错误");
			return js;
		}
		SysCert sc=sysCertService.get(certid);
		if(sc==null){
			js.put("msg", "证书下发出错,证书模板已被删除");
			return js;
		}
		List<CertPage> cps=sysCertService.getCertPages(certid);
		if(cps==null||cps.size()==0){
			js.put("msg", "证书下发出错,证书模板页已被删除");
			return js;
		}
		for(CertPage cp:cps){
			try {
				if(!VsftpUtils.isFileExist(cp.getImgPath())){
					js.put("msg", "证书下发出错,证书模板页图片已被删除,请重新保存模板以便生成图片");
					return js;
				}
			} catch (Exception e) {
				logger.error("检查证书模板页面图片出错",e);
			}
		}
		Integer cc=certMakeInfoService.getCertMakeingNum(actywId, certid);
		if(cc!=null&&cc>0){
			js.put("msg", "该证书的下发还未结束");
			return js;
		}
		Map<String,List<String>> map= sysCertService.getMdCertFlows(actywId, certid);
		if(map==null||map.isEmpty()){
			js.put("msg", "没有需要下发该证书的项目");
			return js;
		}
		
		CertMakeInfo ci=new CertMakeInfo();
		ci.setActywid(actywId);
		ci.setCertid(certid);
		ci.setCertname(sc.getName());
		ci.setTotal(getTotal(map));
		ci.setFail("0");
		ci.setSuccess("0");
		ci.setIsComplete(Const.NO);
		certMakeInfoService.save(ci);
		
		User user=UserUtils.getUser();
		try {
			ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
				@Override
				public void run() {
					try{
						certMakeMd(map,sc,user,cps,ci);
					} catch (Exception e) {
						ci.setIsComplete(Const.YES);
						certMakeInfoService.save(ci);
						CacheUtils.remove(CacheUtils.CERTMAKE_INFO_CACHE, ci.getId());
						logger.error("证书下发出错",e);
					}
				}
			});
		} catch (Exception e) {
			ci.setIsComplete(Const.YES);
			ci.setErrmsg("证书下发出错");
			certMakeInfoService.save(ci);
			logger.error("证书下发出错", e);
			js.put("msg", "证书下发出错,系统异常");
			js.put("ret", 0);
		}
		js.put("ret", 1);
		return js;
	}
	private void certMakeMd(Map<String,List<String>> map,SysCert cert,User user,List<CertPage> cps,CertMakeInfo ci) throws Exception{
		String tempRootPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
		for(CertPage cp:cps){
			String templatePath=cp.getImgPath();
			String realName = templatePath.substring(templatePath.lastIndexOf("/") + 1);
			String path = templatePath.substring(0, templatePath.lastIndexOf("/") + 1);
			VsftpUtils.downFile(path, realName, tempRootPath);
		}
		int fail=0;//失败数
		int success=0;//成功数
		for(String cid:map.keySet()){
			SysCertFlowVo scfv=sysCertFlowService.getCertFlowVo(cid);
			if(scfv==null){
				logger.error("证书下发出错,证书模板关联已被删除");
				continue;
			}
			for(String pid:map.get(cid)){
				try {
					sysCertService.createMdCert(user, cps, cert, scfv, pid, tempRootPath);
					success++;
				} catch (Exception e) {
					logger.error("证书下发出错",e);
					fail++;
				}
				ci.setFail(fail+"");
				ci.setSuccess(success+"");
				ci.setTotal((fail+success)+"");
				CacheUtils.put(CacheUtils.CERTMAKE_INFO_CACHE, ci.getId(), ci);
			}
		}
		ci.setIsComplete(Const.YES);
		certMakeInfoService.save(ci);
		CacheUtils.remove(CacheUtils.CERTMAKE_INFO_CACHE, ci.getId());
		deleteFileOrDir(new File(tempRootPath));
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
	private String getTotal(Map<String,List<String>> map){
		int num=0;
		for(String cid:map.keySet()){
			num=num+map.get(cid).size();
		}
		return num+"";
	}
}