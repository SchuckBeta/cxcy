package com.oseasy.scr.modules.scr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.scr.modules.scr.dao.ScoRapplyRecordDao;
import com.oseasy.scr.modules.scr.entity.ScoRapplyRecord;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分申请记录Service.
 * @author chenh
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRapplyRecordService extends CrudService<ScoRapplyRecordDao, ScoRapplyRecord> {

	public ScoRapplyRecord get(String id) {
		return super.get(id);
	}

	public List<ScoRapplyRecord> findList(ScoRapplyRecord entity) {
		return super.findList(entity);
	}

	public Page<ScoRapplyRecord> findPage(Page<ScoRapplyRecord> page, ScoRapplyRecord entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRapplyRecord entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRapplyRecord> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRapplyRecord> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRapplyRecord entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRapplyRecord entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRapplyRecord entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRapplyRecord entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}

	public List<ScoRapplyRecord> findCourseAuditList(ScoRapplyRecord entity){
		return dao.findCourseAuditList(entity);
	}
}