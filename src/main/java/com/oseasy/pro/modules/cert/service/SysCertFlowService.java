package com.oseasy.pro.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.cert.dao.SysCertFlowDao;
import com.oseasy.pro.modules.cert.entity.SysCertFlow;
import com.oseasy.pro.modules.cert.vo.SysCertFlowVo;

/**
 * 证书模板-流程节点关系Service.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class SysCertFlowService extends CrudService<SysCertFlowDao, SysCertFlow> {
	public SysCertFlowVo getCertFlowVo(String id) {
		return dao.getCertFlowVo(id);
	}
	public SysCertFlow get(String id) {
		return super.get(id);
	}
	public int findByCdn(String flow,String node) {
		return dao.findByCdn( flow, node);
	}
	public List<SysCertFlow> findList(SysCertFlow sysCertFlow) {
		return super.findList(sysCertFlow);
	}

	public Page<SysCertFlow> findPage(Page<SysCertFlow> page, SysCertFlow sysCertFlow) {
		return super.findPage(page, sysCertFlow);
	}

	@Transactional(readOnly = false)
	public void save(SysCertFlow sysCertFlow) {
		super.save(sysCertFlow);
	}

	@Transactional(readOnly = false)
	public void delete(SysCertFlow sysCertFlow) {
		super.delete(sysCertFlow);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysCertFlow sysCertFlow) {
  	  dao.deleteWL(sysCertFlow);
  	}
	@Transactional(readOnly = false)
	public void deleteByFlow(SysCertFlow sysCertFlow){
		dao.deleteByFlow(sysCertFlow);
	}
}