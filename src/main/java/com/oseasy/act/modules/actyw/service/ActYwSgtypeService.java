package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwSgtypeDao;
import com.oseasy.act.modules.actyw.entity.ActYwSgtype;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 状态条件Service.
 * @author zy
 * @version 2018-02-01
 */
@Service
@Transactional(readOnly = true)
public class ActYwSgtypeService extends CrudService<ActYwSgtypeDao, ActYwSgtype> {
	@Autowired
	private ActYwStatusService actYwStatusService;
	public ActYwSgtype get(String id) {
		return super.get(id);
	}

	public List<ActYwSgtype> findList(ActYwSgtype actYwSgtype) {
		return super.findList(actYwSgtype);
	}

	public Page<ActYwSgtype> findPage(Page<ActYwSgtype> page, ActYwSgtype actYwSgtype) {
		return super.findPage(page, actYwSgtype);
	}

	@Transactional(readOnly = false)
	public void save(ActYwSgtype actYwSgtype) {
		super.save(actYwSgtype);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwSgtype actYwSgtype) {
		super.delete(actYwSgtype);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwSgtype actYwSgtype) {
  	  dao.deleteWL(actYwSgtype);
  	}

	public boolean checkName(ActYwSgtype actYwSgtype) {
		boolean result=true;
		List<ActYwSgtype> list=dao.checkName(actYwSgtype);
		if(list!=null &&list.size()>0){
			result=false;
		}
		return result;
	}

	public boolean getInuseActYwSgtype(ActYwSgtype actYwSgtype) {
		boolean result=false;
		List<ActYwSgtype> list=dao.getInuseActYwSgtype(actYwSgtype.getId());
		if(list!=null &&list.size()>0){
			result=true;
		}
		return result;
	}

	@Transactional(readOnly = false)
	public void deleteAll(ActYwSgtype actYwSgtype) {
		delete(actYwSgtype);
		//同时删除条件
		actYwStatusService.deleteWLBySgtypeId(actYwSgtype.getId());
	}
}