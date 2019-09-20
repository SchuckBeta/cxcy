package com.oseasy.act.modules.actyw.service;

import com.oseasy.act.modules.actyw.dao.ActYwGroupRelationDao;
import com.oseasy.act.modules.actyw.dao.ActYwPscrelDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroupRelation;
import com.oseasy.act.modules.actyw.entity.ActYwPscrel;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 节点状态中间表Service.
 * @author zy
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwPscrelService extends CrudService<ActYwPscrelDao, ActYwPscrel> {

	public ActYwPscrel get(String id) {
		return super.get(id);
	}

	public List<ActYwPscrel> findList(ActYwPscrel actYwPscrel) {
		return super.findList(actYwPscrel);
	}

	public List<ActYwPscrel> findListByActywId(ActYwPscrel actYwPscrel) {
		return dao.findListByActywId(actYwPscrel);
	}

	public Page<ActYwPscrel> findPage(Page<ActYwPscrel> page, ActYwPscrel actYwPscrel) {
		return super.findPage(page, actYwPscrel);
	}



	@Transactional(readOnly = false)
	public void save(ActYwPscrel actYwPscrel) {
			super.save(actYwPscrel);
		}

	//批量保存节点数据
	@Transactional(readOnly = false)
	public boolean saveAll(List<ActYwPscrel> actYwPscrelList) {
		boolean res=true;
		try{
			dao.saveAll(actYwPscrelList);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	@Transactional(readOnly = false)
	public void delete(ActYwPscrel actYwGroupRelation) {
		super.delete(actYwGroupRelation);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwPscrel actYwPscrel) {
  	  dao.deleteWL(actYwPscrel);
  	}

    /**
     * 批量保存.
     * @param actYwPscrelList
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwPscrel> actYwPscrelList) {
        dao.saveAll(actYwPscrelList);
    }


	public List<ActYwPscrel> getSchoolByActYw(String id) {
		return dao.getSchoolByActYw(id);
	}

	public String findProvActYwId(String schoolActYwId){
    	return dao.findProvActYwId(schoolActYwId, TenantConfig.getCacheTenant());
	}

}