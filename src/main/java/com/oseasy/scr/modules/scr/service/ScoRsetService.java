package com.oseasy.scr.modules.scr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.scr.modules.scr.dao.ScoRsetDao;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 设置学分规则Service.
 * @author liangjie
 * @version 2018-12-27
 */
@Service
@Transactional(readOnly = true)
public class ScoRsetService extends CrudService<ScoRsetDao, ScoRset> {

	public ScoRset get(String id) {
		return super.get(id);
	}

	public List<ScoRset> findList(ScoRset entity) {
		return super.findList(entity);
	}

	public Page<ScoRset> findPage(Page<ScoRset> page, ScoRset entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRset entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRset> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRset> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRset entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRset entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRset entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRset entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}
}