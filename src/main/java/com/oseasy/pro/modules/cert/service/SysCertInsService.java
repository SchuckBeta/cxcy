package com.oseasy.pro.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.entity.SysCertIns;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 证书信息记录Service.
 * @author 奔波儿灞
 * @version 2018-02-06
 */
@Service
@Transactional(readOnly = true)
public class SysCertInsService extends CrudService<SysCertInsDao, SysCertIns> {
	public List<String> getPidsByFlowNode(String flow,String node) {
		return dao.getPidsByFlowNode(flow, node);
	}
	public SysCertIns get(String id) {
		return super.get(id);
	}

	public List<SysCertIns> findList(SysCertIns sysCertIns) {
		return super.findList(sysCertIns);
	}

	public Page<SysCertIns> findPage(Page<SysCertIns> page, SysCertIns sysCertIns) {
		return super.findPage(page, sysCertIns);
	}

	@Transactional(readOnly = false)
	public void save(SysCertIns sysCertIns) {
		super.save(sysCertIns);
	}

	@Transactional(readOnly = false)
	public void delete(SysCertIns sysCertIns) {
		super.delete(sysCertIns);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysCertIns sysCertIns) {
  	  dao.deleteWL(sysCertIns);
  	}
}