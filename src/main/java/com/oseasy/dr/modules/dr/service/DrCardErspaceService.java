package com.oseasy.dr.modules.dr.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.dr.modules.dr.dao.DrCardErspaceDao;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.entity.DrEmentNo;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡设备Service.
 * @author chenh
 * @version 2018-04-03
 */
@Service
@Transactional(readOnly = true)
public class DrCardErspaceService extends CrudService<DrCardErspaceDao, DrCardErspace> {

	public DrCardErspace get(String id) {
		return super.get(id);
	}

	public List<DrCardErspace> findList(DrCardErspace drCardErspace) {
		return super.findList(drCardErspace);
	}

	public Page<DrCardErspace> findPage(Page<DrCardErspace> page, DrCardErspace drCardErspace) {
		return super.findPage(page, drCardErspace);
	}

    /**
     * 根据卡id查询所有数据列表
     * 查询所有数据列表
     * @param cids 卡ID
     * @return List
     */
    public List<DrEmentNo> findDrEmentNosByCid(String cid){
    	return dao.findDrEmentNosByCid(cid);
    }

    /**
     * 根据卡ids查询所有数据列表
     * @param cids 卡IDs
     * @return List
     */
    public List<DrEmentNo> findDrEmentNosByCids( List<String> cids){
    	return dao.findDrEmentNosByCids(cids);
    }

	@Transactional(readOnly = false)
	public void save(DrCardErspace drCardErspace) {
	    if (drCardErspace.getIsNewRecord()) {
            if(StringUtil.isEmpty(drCardErspace.getId())){
                drCardErspace.setId(IdGen.uuid());
            }
        }
		super.save(drCardErspace);
	}

	@Transactional(readOnly = false)
	public void delete(DrCardErspace drCardErspace) {
		super.delete(drCardErspace);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(DrCardErspace drCardErspace) {
  	  dao.deleteWL(drCardErspace);
  	}

	public List<DrCardErspace> findListByg(DrCardErspace entity){
		return dao.findListByg(entity);
	}

	@Transactional(readOnly = false)
	public void savePl(List<DrCardErspace> entitys) {
	    dao.savePl(entitys);
	}


	@Transactional(readOnly = false)
	public void updateByPl(List<DrCardErspace> entitys) {
	    dao.updateByPl(entitys);
	}

	@Transactional(readOnly = false)
	public void updateStatusByCid(List<String> cids, Integer status) {
		dao.updateStatusByCid(cids, status);
	}

  	@Transactional(readOnly = false)
  	public void deletePlwl(List<String> ids) {
  	    dao.deletePlwl(ids);
  	}

    /**
     * 根据申报ID物理删除.
     * @param entity
     */
  	@Transactional(readOnly = false)
  	public void deletePlwlByCard(String cardId) {
  	    dao.deletePlwlByCard(cardId);
  	}
}