package com.oseasy.pro.modules.cert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pro.modules.cert.dao.CertMakeInfoDao;
import com.oseasy.pro.modules.cert.entity.CertMakeInfo;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.entity.SysCert;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 下发证书进度信息Service.
 * @author 奔波儿灞
 * @version 2018-03-02
 */
@Service
@Transactional(readOnly = true)
public class CertMakeInfoService extends CrudService<CertMakeInfoDao, CertMakeInfo> {
	@Autowired
	private SysCertService sysCertService;
	public CertMakeInfo get(String id) {
		return super.get(id);
	}
	public List<CertMakeInfo> findList(CertMakeInfo certMakeInfo) {
		return super.findList(certMakeInfo);
	}
	public Page<CertMakeInfo> findPage(Page<CertMakeInfo> page, CertMakeInfo certMakeInfo) {
		return super.findPage(page, certMakeInfo);
	}

	@Transactional(readOnly = false)
	public void save(CertMakeInfo certMakeInfo) {
		super.save(certMakeInfo);
	}

	@Transactional(readOnly = false)
	public void delete(CertMakeInfo certMakeInfo) {
		super.delete(certMakeInfo);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(CertMakeInfo certMakeInfo) {
  	  dao.deleteWL(certMakeInfo);
  	}
  	public CertMakeInfo getCertMakeInfo(String id) {
  		CertMakeInfo ii=(CertMakeInfo)CacheUtils.get(CacheUtils.CERTMAKE_INFO_CACHE, id);
		if (ii==null) {
			ii=super.get(id);
		}
		return ii;
	}
  	public Integer getCertMakeingNum(String actywId,String certid){
  		return dao.getCertMakeingNum(actywId, certid);
  	}
  	public JSONObject checkCertMake(String actywId,String certid){
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
		Integer cc=getCertMakeingNum(actywId, certid);
		if(cc!=null&&cc>0){
			js.put("msg", "该证书的下发还未结束");
			return js;
		}
		Map<String,List<String>> map= sysCertService.getMdCertFlows(actywId, certid);
		if(map==null||map.isEmpty()){
			js.put("msg", "没有需要下发该证书的项目");
			return js;
		}
		js.put("ret", 1);
		return js;
  	}
}