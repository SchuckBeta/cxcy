package com.oseasy.act.modules.actyw.service;

import com.oseasy.act.modules.actyw.dao.ActYwGroupRelationDao;
import com.oseasy.act.modules.actyw.dao.ActYwGstatusDao;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ActYwGroupRelationService extends CrudService<ActYwGroupRelationDao, ActYwGroupRelation> {
	@Autowired
	private ActYwStepService actYwStepService;
	public ActYwGroupRelation get(String id) {
		return super.get(id);
	}

	public List<ActYwGroupRelation> findList(ActYwGroupRelation actYwGroupRelation) {
		return super.findList(actYwGroupRelation);
	}

	public Page<ActYwGroupRelation> findPage(Page<ActYwGroupRelation> page, ActYwGroupRelation actYwGroupRelation) {
		return super.findPage(page, actYwGroupRelation);
	}



	@Transactional(readOnly = false)
	public void save(ActYwGroupRelation actYwGroupRelation) {
			super.save(actYwGroupRelation);
		}

	//批量保存节点数据
	@Transactional(readOnly = false)
	public boolean saveAll(List<ActYwGroupRelation> actYwGroupRelationList) {
		boolean res=true;
		try{
			dao.saveAll(actYwGroupRelationList);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	@Transactional(readOnly = false)
	public void delete(ActYwGroupRelation actYwGroupRelation) {
		super.delete(actYwGroupRelation);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwGroupRelation actYwGroupRelation) {
  	  dao.deleteWL(actYwGroupRelation);
  	}

    /**
     * 批量保存.
     * @param actYwGroupRelationList
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwGroupRelation> actYwGroupRelationList) {
        dao.savePl(actYwGroupRelationList);
    }

	@Transactional(readOnly = false)
	public void saveByRelation(String provGroupId, String modelGroupId) {
		ActYwGroupRelation actYwGroupRelation=new ActYwGroupRelation();
		actYwGroupRelation.setProvGroupId(provGroupId);
		List<ActYwGroupRelation> list=findList(actYwGroupRelation);
		if(StringUtil.checkNotEmpty(list)){
			//处理已经关联过的流程。
			ActYwGroupRelation actYwGroupRelationOld=list.get(0);
			actYwGroupRelationOld.setModelGroupId(modelGroupId);
			save(actYwGroupRelationOld);
		}else{
			actYwGroupRelation.setModelGroupId(modelGroupId);
			save(actYwGroupRelation);
			ActYwStep actYwStep =actYwStepService.getActYwStepByGroupId(provGroupId);
			actYwStep.setStep(ActYwStep.StepEnmu.STEP4.getValue());
			actYwStep.setModelGroupId(modelGroupId);
			actYwStepService.save(actYwStep);
		}
	}

	public String getModelActYwGroupIdByProv(String groupId) {
		return dao.getModelActYwGroupByProv(groupId);
	}

	public ActYwGroup getNscActYwGroupByProv(String groupId) {
		ActYwGroupRelation entity = new ActYwGroupRelation();
		entity.setProvGroupId(groupId);
		return dao.getNscActYwGroupByProv(entity);
	}

	/**
	 * 查询所有已关联的流程.
	 * @param entity ActYwGroupRelation
	 * @return
	 */
	public List<ActYwGroup> findListHasGrel(ActYwGroupRelation entity) {
		return dao.findListHasGrel(entity);
	}

	/**
	 * 校验当前流程是否关联.
	 * @param entity ActYwGroupRelation
	 * @return
	 */
	public Boolean checkHasGrel(ActYwGroupRelation entity) {
		return StringUtil.checkNotEmpty(dao.findListHasGrel(entity));
	}

	/**
	 * 查询所有已关联的业务.
	 * @param entity ActYwGroupRelation
	 * @return
	 */
	public List<ActYw> findListHasYrel(ActYwGroupRelation entity) {
		return dao.findListHasYrel(entity);
	}

	/**
	 * 校验当前业务是否关联.
	 * @param entity ActYwGroupRelation
	 * @return
	 */
	public Boolean checkHasYrel(ActYwGroupRelation entity) {
		return StringUtil.checkNotEmpty(dao.findListHasYrel(entity));
	}
}