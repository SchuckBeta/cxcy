package com.oseasy.act.modules.actyw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwClazzDao;
import com.oseasy.act.modules.actyw.entity.ActYwClazz;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 监听Service.
 * @author chenh
 * @version 2018-03-01
 */
@Service
@Transactional(readOnly = true)
public class ActYwClazzService extends CrudService<ActYwClazzDao, ActYwClazz> {

	public ActYwClazz get(String id) {
		return super.get(id);
	}

	public List<ActYwClazz> findList(ActYwClazz actYwClazz) {
		return super.findList(actYwClazz);
	}

	public Page<ActYwClazz> findPage(Page<ActYwClazz> page, ActYwClazz actYwClazz) {
		return super.findPage(page, actYwClazz);
	}

	@Transactional(readOnly = false)
	public void save(ActYwClazz actYwClazz) {
		super.save(actYwClazz);
	}

	@Transactional(readOnly = false)
	public void delete(ActYwClazz actYwClazz) {
		super.delete(actYwClazz);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwClazz actYwClazz) {
  	  dao.deleteWL(actYwClazz);
  	}
}