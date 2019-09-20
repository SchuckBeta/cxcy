package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrCardreGtimeDao;
import com.oseasy.dr.modules.dr.entity.DrCardreGtime;

/**
 * 卡记录规则时间Service.
 * @author chenh
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class DrCardreGtimeService extends CrudService<DrCardreGtimeDao, DrCardreGtime> {

	public DrCardreGtime get(String id) {
		return super.get(id);
	}

    /**
     * 查询所有记录.
     * @return
     */
    public List<DrCardreGtime> findAllList() {
        return dao.findAllList();
    }

	public List<DrCardreGtime> findList(DrCardreGtime drCardreGtime) {
		return super.findList(drCardreGtime);
	}

	public Page<DrCardreGtime> findPage(Page<DrCardreGtime> page, DrCardreGtime drCardreGtime) {
		return super.findPage(page, drCardreGtime);
	}

	@Transactional(readOnly = false)
	public void save(DrCardreGtime drCardreGtime) {
		super.save(drCardreGtime);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardreGtime drCardreGtime) {
		super.delete(drCardreGtime);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardreGtime drCardreGtime) {
  	  dao.deleteWL(drCardreGtime);
  	}

  	/**
     * 根据gid批量删除数据
     * @param gid
     * @return
     */
    @Transactional(readOnly = false)
    public void deleteWLPLByGid(String gid) {
        dao.deleteWLPLByGid(gid);
    }

    /**
     * 批量更新数据
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public void savePl(List<DrCardreGtime> entitys) {
        dao.savePl(entitys);
    }

    /**
     * 根据ids批量删除数据
     * @param asList
     */
    @Transactional(readOnly = false)
    public void deleteWLPLByIds(List<String> ids) {
        dao.deleteWLPLByIds(ids);
    }
}