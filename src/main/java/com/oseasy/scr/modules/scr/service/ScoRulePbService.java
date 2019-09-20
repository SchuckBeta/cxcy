package com.oseasy.scr.modules.scr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.scr.modules.scr.dao.ScoRulePbDao;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRulePb;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分规则配比Service.
 * @author chenh
 * @version 2018-12-26
 */
@Service
@Transactional(readOnly = true)
public class ScoRulePbService extends CrudService<ScoRulePbDao, ScoRulePb> {


	public ScoRulePb get(String id) {
		ScoRulePb entity = super.get(id);
		return entity;
	}

	public List<ScoRulePb> findList(ScoRulePb entity) {
		return super.findList(entity);
	}

	public Page<ScoRulePb> findPage(Page<ScoRulePb> page, ScoRulePb entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRulePb entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}
	@Transactional(readOnly = false)
	public void insertPL(List<ScoRulePb> entitys){
		dao.insertPL(entitys);
	}

	@Transactional(readOnly = false)
	public void updatePL(List<ScoRulePb> entitys){
		dao.updatePL(entitys);
	}
	@Transactional(readOnly = false)
	public void deleteByRid(ScoRulePb entity){
		dao.deleteByRid(entity);
	}

	@Transactional(readOnly = false)
	public void delete(ScoRulePb entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRulePb entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRulePb entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRulePb entity) {
  	  dao.deleteWLPL(entity);
  	}
}