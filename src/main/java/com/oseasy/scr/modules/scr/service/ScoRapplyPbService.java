package com.oseasy.scr.modules.scr.service;

import java.util.List;

import com.oseasy.scr.modules.scr.dao.ScoRapplyPbDao;
import com.oseasy.scr.modules.scr.entity.ScoRapply;
import com.oseasy.scr.modules.scr.entity.ScoRapplyPb;
import com.oseasy.scr.modules.scr.entity.ScoRsum;
import com.oseasy.scr.modules.scr.vo.ScoQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分申请配比Service.
 * @author chenh
 * @version 2018-12-26
 */
@Service
@Transactional(readOnly = true)
public class ScoRapplyPbService extends CrudService<ScoRapplyPbDao, ScoRapplyPb> {


	public ScoRapplyPb get(String id) {
		ScoRapplyPb entity = super.get(id);
		return entity;
	}

	public List<ScoRapplyPb> findList(ScoRapplyPb entity) {
		return super.findList(entity);
	}

	public Page<ScoRapplyPb> findPage(Page<ScoRapplyPb> page, ScoRapplyPb entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRapplyPb entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(ScoRapplyPb entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ScoRapplyPb entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRapplyPb entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRapplyPb entity) {
  	  dao.deleteWLPL(entity);
  	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRapplyPb> entitys) {
        dao.insertPL(entitys);
    }

	@Transactional(readOnly = false)
	public void deleteWLByApplyId(ScoQuery scoQuery){
		ScoRapply scoRapply = new ScoRapply(scoQuery.getId());
		ScoRapplyPb scoRapplyPb = new ScoRapplyPb();
		scoRapplyPb.setApply(scoRapply);
		dao.deleteWLByApplyId(scoRapplyPb);
	}

	@Transactional(readOnly = false)
	public void deleteWLByApplyId(String appId){
	    ScoRapply scoRapply = new ScoRapply(appId);
	    ScoRapplyPb scoRapplyPb = new ScoRapplyPb();
	    scoRapplyPb.setApply(scoRapply);
	    dao.deleteWLByApplyId(scoRapplyPb);
	}
	@Transactional(readOnly = false)
	public void updateVal(ScoRsum entity){
		dao.updateVal(entity);
	}
}