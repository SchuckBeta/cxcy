package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrCardreGroupDao;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡记录规则组Service.
 * @author chenh
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class DrCardreGroupService extends CrudService<DrCardreGroupDao, DrCardreGroup> {

	public DrCardreGroup get(String id) {
		return super.get(id);
	}

	public DrCardreGroup getByg(String id) {
	    return dao.getByg(id);
	}

	public List<DrCardreGroup> findList(DrCardreGroup drCardreGroup) {
		return super.findList(drCardreGroup);
	}

	public List<DrCardreGroup> findListByg(DrCardreGroup drCardreGroup) {
	    return dao.findListByg(drCardreGroup);
	}

	public Page<DrCardreGroup> findPage(Page<DrCardreGroup> page, DrCardreGroup drCardreGroup) {
		return super.findPage(page, drCardreGroup);
	}

	public Page<DrCardreGroup> findPageByg(Page<DrCardreGroup> page, DrCardreGroup drCardreGroup) {
	    drCardreGroup.setPage(page);
        page.setList(dao.findListByg(drCardreGroup));
        return page;
	}

	public Page<DrCardreGroup> findAllPageByg(Page<DrCardreGroup> page, DrCardreGroup drCardreGroup) {
	    drCardreGroup.setPage(page);
	    page.setList(dao.findAllListByg());
	    return page;
	}

	@Transactional(readOnly = false)
	public void save(DrCardreGroup drCardreGroup) {
	    if(drCardreGroup.getIsNewRecord()){
	        if(StringUtil.isEmpty(drCardreGroup.getIsTimeLimit())){
                drCardreGroup.setIsTimeLimit(Const.NO);
            }
	        if((drCardreGroup.getIsShow() == null)){
                drCardreGroup.setIsShow(true);
            }
	    }
		super.save(drCardreGroup);
	}

	@Transactional(readOnly = false)
	public void updateIsShow(DrCardreGroup drCardreGroup) {
		dao.updateIsShow(drCardreGroup);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardreGroup drCardreGroup) {
	    super.delete(drCardreGroup);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardreGroup drCardreGroup) {
  	  dao.deleteWL(drCardreGroup);
  	}
}