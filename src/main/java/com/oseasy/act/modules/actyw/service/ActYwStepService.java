package com.oseasy.act.modules.actyw.service;

import com.oseasy.act.modules.actyw.dao.ActYwStepDao;
import com.oseasy.act.modules.actyw.entity.ActYwStep;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.oseasy.act.modules.actyw.entity.ActYwStep.StepEnmu.STEP1;

/**
 * 节点状态中间表Service.
 * @author zy
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwStepService extends CrudService<ActYwStepDao, ActYwStep> {

	public ActYwStep get(String id) {
		return super.get(id);
	}

	public List<ActYwStep> findList(ActYwStep actYwStep) {
		return super.findList(actYwStep);
	}

	public Page<ActYwStep> findPage(Page<ActYwStep> page, ActYwStep actYwStep) {
		return super.findPage(page, actYwStep);
	}

	@Transactional(readOnly = false)
	public void save(ActYwStep actYwStep) {
			super.save(actYwStep);
		}

	//批量保存节点数据
	@Transactional(readOnly = false)
	public boolean saveAll(List<ActYwStep> actYwScstepList) {
		boolean res=true;
		try{
			dao.saveAll(actYwScstepList);
		}catch (Exception e){
			res=false;
		}
		return res;
	}

	@Transactional(readOnly = false)
	public void delete(ActYwStep actYwStep) {
		super.delete(actYwStep);
	}

  	@Transactional(readOnly = false)
  	public void deleteWL(ActYwStep actYwStep) {
  	  dao.deleteWL(actYwStep);
  	}

    /**
     * 批量保存.
     * @param actYwStepList
     */
    @Transactional(readOnly = false)
    public void savePl(List<ActYwStep> actYwStepList) {
        dao.savePl(actYwStepList);
    }

	public List<ActYwStep> stepList() {
		ActYwStep actYwStep=new ActYwStep();

		List<ActYwStep>  stepList=dao.getStepList(actYwStep);
		return stepList;
	}

	public ActYwStep getActYwStepByGroupId(String groupId) {
		return dao.getActYwStepByGroupId(groupId);
	}
	@Transactional(readOnly = false)
	public void saveStep(String step,String groupId) {
		ActYwStep actYwStep=getActYwStepByGroupId(groupId);
		if(actYwStep!=null){
			actYwStep.setStep(step);
			dao.updateStep(actYwStep);
		}

	}

	@Transactional(readOnly = false)
	public void saveNew(String groupId) {
		ActYwStep actYwStep=new ActYwStep();
		actYwStep.setProvGroupId(groupId);
		actYwStep.setStep(STEP1.getValue());
		save(actYwStep);
	}
}