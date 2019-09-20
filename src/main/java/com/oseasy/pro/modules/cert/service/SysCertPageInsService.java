package com.oseasy.pro.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.cert.dao.SysCertPageInsDao;
import com.oseasy.pro.modules.cert.entity.SysCertPageIns;
import com.oseasy.pro.modules.cert.vo.SysCertPageInsVo;

/**
 * 证书颁发记录页面表Service.
 * @author 奔波儿灞
 * @version 2018-02-09
 */
@Service
@Transactional(readOnly = true)
public class SysCertPageInsService extends CrudService<SysCertPageInsDao, SysCertPageIns> {
	public List<SysCertPageInsVo> getSysCertPageIns(String sci){
		return dao.getSysCertPageIns(sci);
	}
	public SysCertPageIns get(String id) {
		return super.get(id);
	}

	public List<SysCertPageIns> findList(SysCertPageIns sysCertPageIns) {
		return super.findList(sysCertPageIns);
	}

	public Page<SysCertPageIns> findPage(Page<SysCertPageIns> page, SysCertPageIns sysCertPageIns) {
		return super.findPage(page, sysCertPageIns);
	}

	@Transactional(readOnly = false)
	public void save(SysCertPageIns sysCertPageIns) {
		super.save(sysCertPageIns);
	}

	@Transactional(readOnly = false)
	public void delete(SysCertPageIns sysCertPageIns) {
		super.delete(sysCertPageIns);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(SysCertPageIns sysCertPageIns) {
  	  dao.deleteWL(sysCertPageIns);
  	}
}