package com.oseasy.pw.modules.pw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwEnterRoomRecordDao;
import com.oseasy.pw.modules.pw.entity.PwEnterRoomRecord;

/**
 * 场地分配记录Service.
 * @author chenh
 * @version 2018-12-10
 */
@Service
@Transactional(readOnly = true)
public class PwEnterRoomRecordService extends CrudService<PwEnterRoomRecordDao, PwEnterRoomRecord> {

	public PwEnterRoomRecord get(String id) {
		return super.get(id);
	}

	public List<PwEnterRoomRecord> findList(PwEnterRoomRecord entity) {
		return super.findList(entity);
	}

	public Page<PwEnterRoomRecord> findPage(Page<PwEnterRoomRecord> page, PwEnterRoomRecord entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(PwEnterRoomRecord entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<PwEnterRoomRecord> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<PwEnterRoomRecord> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(PwEnterRoomRecord entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(PwEnterRoomRecord entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(PwEnterRoomRecord entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(PwEnterRoomRecord entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}
}