package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.dr.modules.dr.dao.DrCardreGitemDao;
import com.oseasy.dr.modules.dr.entity.DrCardreGitem;
import com.oseasy.dr.modules.dr.vo.GitemEstatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡记录规则明细Service.
 * @author chenh
 * @version 2018-05-16
 */
@Service
@Transactional(readOnly = true)
public class DrCardreGitemService extends CrudService<DrCardreGitemDao, DrCardreGitem> {

	public DrCardreGitem get(String id) {
		return super.get(id);
	}

	public List<DrCardreGitem> findList(DrCardreGitem drCardreGitem) {
		return super.findList(drCardreGitem);
	}

	public Page<DrCardreGitem> findPage(Page<DrCardreGitem> page, DrCardreGitem drCardreGitem) {
		return super.findPage(page, drCardreGitem);
	}

	@Transactional(readOnly = false)
	public void save(DrCardreGitem drCardreGitem) {
	    if(drCardreGitem.getIsNewRecord()){
            if(StringUtil.isEmpty(drCardreGitem.getEstatus())){
                drCardreGitem.setEstatus(GitemEstatus.GES_NORMAL.getKey());
            }
        }
		super.save(drCardreGitem);
	}

    @Transactional(readOnly = false)
    public void savePl(List<DrCardreGitem> entitys) {
        for (DrCardreGitem ety : entitys) {
            if(StringUtil.isEmpty(ety.getEstatus())){
                ety.setEstatus(GitemEstatus.GES_NORMAL.getKey());
            }
        }
        dao.savePl(entitys);
    }

	@Transactional(readOnly = false)
	public void delete(DrCardreGitem drCardreGitem) {
		super.delete(drCardreGitem);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardreGitem drCardreGitem) {
  	  dao.deleteWL(drCardreGitem);
  	}
  	@Transactional(readOnly = false)
  	public void deleteWLPLByGid(String gid) {
  	    dao.deleteWLPLByGid(gid);
  	}

    /**
     * 根据ids批量删除数据
     * @param asList
     */
    public void deleteWLPLByIds(List<String> ids) {
        dao.deleteWLPLByIds(ids);
    }
}