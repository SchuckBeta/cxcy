package com.oseasy.scr.modules.scr.service;

import java.util.List;

import com.oseasy.scr.modules.scr.dao.ScoRapplyMemberDao;
import com.oseasy.scr.modules.scr.entity.ScoRapplyMember;
import com.oseasy.scr.modules.scr.entity.ScoRapplyValid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 学分申请成员Service.
 * @author chenhao
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class ScoRapplyMemberService extends CrudService<ScoRapplyMemberDao, ScoRapplyMember> {

	public ScoRapplyMember get(String id) {
		return super.get(id);
	}

	public List<ScoRapplyMember> findList(ScoRapplyMember entity) {
		return super.findList(entity);
	}

	public List<ScoRapplyMember> findScoMemberList(ScoRapplyMember entity) {
		return dao.findScoMemberList(entity);
	}

	public List<ScoRapplyValid> ajaxValidScoMemberList(ScoRapplyValid entity){
		return dao.ajaxValidScoMemberList(entity);
	}
	public List<ScoRapplyValid> ajaxValidScoRapplyList(ScoRapplyValid entity){
		return  dao.ajaxValidScoRapplyList(entity);
	}

	public Page<ScoRapplyMember> findPage(Page<ScoRapplyMember> page, ScoRapplyMember entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ScoRapplyMember entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ScoRapplyMember> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ScoRapplyMember> entitys) {
        dao.updatePL(entitys);
    }


	@Transactional(readOnly = false)
	public void delete(ScoRapplyMember entity) {
		super.delete(entity);
	}


	@Transactional(readOnly = false)
	public void deleteRapplyMembers(ScoRapplyMember entity) {
		dao.deleteRapplyMembers(entity);
	}


	@Transactional(readOnly = false)
	public void deletePL(ScoRapplyMember entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ScoRapplyMember entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ScoRapplyMember entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
  	    dao.deleteWLAll();
  	}
}