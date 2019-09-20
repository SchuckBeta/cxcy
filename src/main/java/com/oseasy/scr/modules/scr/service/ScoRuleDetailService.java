package com.oseasy.scr.modules.scr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.scr.modules.scr.dao.ScoRuleDetailDao;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetail;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分规则详情Service.
 * @author chenh
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRuleDetailService extends CrudService<ScoRuleDetailDao, ScoRuleDetail> {

	public ScoRuleDetail get(String id) {
		return super.get(id);
	}

	public List<ScoRuleDetail> findList(ScoRuleDetail entity) {
		return super.findList(entity);
	}

	public Page<ScoRuleDetail> findPage(Page<ScoRuleDetail> page, ScoRuleDetail entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRuleDetail entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRuleDetail> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRuleDetail> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRuleDetail entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRuleDetail entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRuleDetail entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRuleDetail entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	@Transactional(readOnly = false)
	public void updateSort(ScoRuleDetail entity) {
		dao.updateSort(entity);
	}

	@Transactional(readOnly = false)
	public void deleteByRid(ScoRuleDetail entity){
		dao.deleteByRid(entity);
	}

	@Transactional(readOnly = false)
	public void updateMaxOrSumByRid(ScoRuleDetail entity){
		dao.updateMaxOrSumByRid(entity);
	}


	public List<ScoRuleDetail> ajaxValiScrRuleDetailName(ScoRuleDetail entity){
		return  dao.ajaxValiScrRuleDetailName(entity);
	}
}