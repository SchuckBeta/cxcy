package com.oseasy.act.modules.actyw.service;

import java.util.List;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.actyw.dao.ActYwGthemeDao;
import com.oseasy.act.modules.actyw.entity.ActYwGtheme;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;

/**
 * 表单组Service.
 * @author chenh
 * @version 2018-09-06
 */
@Service
@Transactional(readOnly = true)
public class ActYwGthemeService extends CrudService<ActYwGthemeDao, ActYwGtheme> {

	public ActYwGtheme get(String id) {
		return super.get(id);
	}

	public List<ActYwGtheme> findList(ActYwGtheme entity) {
		return super.findList(entity);
	}

	public Page<ActYwGtheme> findPage(Page<ActYwGtheme> page, ActYwGtheme entity) {
		return super.findPage(page, entity);
	}

	@Transactional(readOnly = false)
	public void save(ActYwGtheme entity) {
		if(entity.getIsNewRecord()){
	    }
		super.save(entity);
	}

    @Transactional(readOnly = false)
    public void insertPL(List<ActYwGtheme> entitys) {
        dao.insertPL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePL(List<ActYwGtheme> entitys) {
        dao.updatePL(entitys);
    }

    @Transactional(readOnly = false)
    public void updatePLSort(List<ActYwGtheme> entitys) {
        dao.updatePLSort(entitys);
    }

	@Transactional(readOnly = false)
	public void delete(ActYwGtheme entity) {
		super.delete(entity);
	}

	@Transactional(readOnly = false)
	public void deletePL(ActYwGtheme entity) {
		dao.deletePL(entity);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwGtheme entity) {
  	  dao.deleteWL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLPL(ActYwGtheme entity) {
  	  dao.deleteWLPL(entity);
  	}

  	@Transactional(readOnly = false)
  	public void deleteWLAll() {
		String tenantId = TenantConfig.getCacheTenant();
  	    dao.deleteWLAll(tenantId);
  	}
}