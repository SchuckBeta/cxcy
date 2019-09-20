package com.oseasy.act.modules.actyw.service;

import com.oseasy.act.modules.actyw.dao.ActYwScstepDao;
import com.oseasy.act.modules.actyw.entity.ActYwScstep;
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
public class ActYwScstepService extends CrudService<ActYwScstepDao, ActYwScstep> {

	public ActYwScstep get(String id) {
		return super.get(id);
	}

	public List<ActYwScstep> findList(ActYwScstep actYwGroupRelation) {
		return super.findList(actYwGroupRelation);
	}

	public Page<ActYwScstep> findPage(Page<ActYwScstep> page, ActYwScstep actYwGroupRelation) {
		return super.findPage(page, actYwGroupRelation);
	}



	@Transactional(readOnly = false)
	public void save(ActYwScstep actYwScstep) {
			super.save(actYwScstep);
		}

	//批量保存节点数据
	@Transactional(readOnly = false)
	public boolean saveAll(List<ActYwScstep> actYwScstepList) {
		boolean res=true;
		try{
			dao.saveAll(actYwScstepList);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	@Transactional(readOnly = false)
	public void delete(ActYwScstep actYwScstep) {
		super.delete(actYwScstep);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwScstep actYwScstep) {
  	  dao.deleteWL(actYwScstep);
  	}

    /**
     * 批量保存.
     * @param actYwScstepList
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwScstep> actYwScstepList) {
        dao.savePl(actYwScstepList);
    }

	public List<ActYwScstep> stepList() {
		ActYwScstep actYwScstep=new ActYwScstep();
		actYwScstep.setSchoolTenantId(TenantConfig.getCacheTenant());
		List<ActYwScstep>  stepList=dao.getStepList(actYwScstep);
		return stepList;
	}
}