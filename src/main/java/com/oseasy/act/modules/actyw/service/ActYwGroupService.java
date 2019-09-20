package com.oseasy.act.modules.actyw.service;

import java.util.List;

import com.oseasy.act.modules.actyw.dao.ActYwGroupRelationDao;
import com.oseasy.act.modules.actyw.entity.ActYwGroupRelation;
import com.oseasy.act.modules.actyw.entity.ActYwStep;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.dao.ActYwGroupDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.apply.IAgservice;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义流程Service.
 * @author chenhao
 * @version 2017-05-23
 */
@Service
@Transactional(readOnly = true)
public class ActYwGroupService extends CrudService<ActYwGroupDao, ActYwGroup> implements IAgservice{
@Autowired
  private ActYwService actYwService;
  @Autowired
  private ActYwGnodeService actYwGnodeService;
    @Autowired
    private ActYwStepService actYwStepService;
    @Autowired
    private  SysTenantService sysTenantService;
    @Autowired
    private ActYwGroupRelationDao actYwGroupRelationDao;


  public ActYwGroup get(String id) {
    return super.get(id);
  }

  /**
   * 根据唯一标识获取对象.
   * @param keyss 标识值
   * @return List
   */
  public List<ActYwGroup> getByKeyss(String keyss) {
    return dao.getByKeyss(keyss);
  }

  /**
   * 验证唯一标识是否存在.
   * @param keyss 标识值
   * @param isNew 是否新增
   * @return Boolean
   */
  public Boolean validKeyss(String keyss, Boolean isNew) {
    List<ActYwGroup> actYwGroups = getByKeyss(keyss);
    if ((actYwGroups == null) || (actYwGroups.size() <= 0)) {
      return true;
    }

    int size = actYwGroups.size();
    if (!isNew && (size == 1)) {
        return true;
    }
    return false;
  }

  /**
   * 执行findList时调用  findCount 获取总记录数.
   * @param actYwGroup
   * @return
   */
  public Long findCount(ActYwGroup actYwGroup) {
    return dao.findCount(actYwGroup);
  }

  public List<ActYwGroup> findList(ActYwGroup actYwGroup) {
      return super.findList(actYwGroup);
  }

  public Page<ActYwGroup> findPage(Page<ActYwGroup> page, ActYwGroup actYwGroup) {
    return super.findPage(page, actYwGroup);
  }

  public List<ActYwGroup> findListByCount(ActYwGroup entity) {
      return dao.findListByCount(entity);
  }

  public Page<ActYwGroup> findPageByCount(Page<ActYwGroup> page, ActYwGroup entity) {
      entity.setPage(page);
      page.setList(findListByCount(entity));
      return page;
  }

  /**
   * 查询当前所有已发布的流程.
   * @return List
   */
  public List<ActYwGroup> findListByDeploy() {
    ActYwGroup pactYwGroup = new ActYwGroup();
    pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_1);
    pactYwGroup.setDelFlag(Const.NO);
    return super.findList(pactYwGroup);
  }


  /**
   * 根据类型获取已发布流程.
   * 如果类型为空，返回所有已发布流程.
   * @param ftype 排除的ID
   * @return
   */
  public List<ActYwGroup> listData(String ftype) {
    List<ActYwGroup> actYwGroups = Lists.newArrayList();
    if (StringUtil.isEmpty(ftype)) {
      actYwGroups = findListByDeploy();
    }else{
      FlowType flowType = FlowType.getByKey(ftype);
      if (flowType == null) {
        logger.warn("类型参数未定义!");
        return actYwGroups;
      }
      actYwGroups = findListByDeploy(flowType);
    }
    return actYwGroups;
  }
  /**
   * 查询当前所有未发布的流程.
   * @return List
   */
  public List<ActYwGroup> findListByNoDeploy() {
    ActYwGroup pactYwGroup = new ActYwGroup();
    pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_0);
    pactYwGroup.setDelFlag(Const.NO);
    return super.findList(pactYwGroup);
  }

  /**
   * 根据流程类型查询当前已发布的流程.
   * @param flowType 流程类型.
   * @return List
   */
  public List<ActYwGroup> findListByDeploy(FlowType flowType) {
    if (flowType == null) {
      return null;
    }

    ActYwGroup pactYwGroup = new ActYwGroup();
    pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_1);
    pactYwGroup.setDelFlag(Const.NO);
    pactYwGroup.setFlowType(flowType.getKey());
    return super.findList(pactYwGroup);
  }

  /**
   * 根据流程类型查询当前未发布的流程.
   * @param flowType 流程类型.
   * @return List
   */
  public List<ActYwGroup> findListByNoDeploy(FlowType flowType) {
    if (flowType == null) {
      return null;
    }

    ActYwGroup pactYwGroup = new ActYwGroup();
    pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_0);
    pactYwGroup.setDelFlag(Const.NO);
    pactYwGroup.setFlowType(flowType.getKey());
    return super.findList(pactYwGroup);
  }

  @Transactional(readOnly = false)
  public void save(ActYwGroup actYwGroup) {
    if (actYwGroup.getIsNewRecord()) {
      if (actYwGroup.getSort() == null) {
        actYwGroup.setSort(1);
      }
      if (actYwGroup.getAuthor() == null) {
        actYwGroup.setAuthor(UserUtils.getUser().getId() + StringUtil.LINE_M + UserUtils.getUser().getName());
      }
      if (StringUtil.isEmpty(actYwGroup.getStatus())) {
        actYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_0);
      }
        if ((actYwGroup.getTemp() == null)) {
            actYwGroup.setTemp(true);
        }
        if ((actYwGroup.getIsPush() == null)) {
            actYwGroup.setIsPush(false);
        }

        if (StringUtil.isEmpty(actYwGroup.getVersion())) {
            actYwGroup.setVersion(ActYwGroup.GROUP_VERSION);
        }

        if (StringUtil.isEmpty(actYwGroup.getNtype())) {
            actYwGroup.setNtype(CoreSval.Const.NO);
        }

      if (StringUtil.isEmpty(actYwGroup.getKeyss())) {
        actYwGroup.setKeyss(SysNodeTool.genByKeyss(SysNoType.NO_FLOW));
      }
    }
    if (StringUtil.isNotEmpty(actYwGroup.getFlowType())) {
      actYwGroup.setType(FlowType.getByKey(actYwGroup.getFlowType()).getType().getTypes());
    }
    if (validKeyss(actYwGroup.getKeyss(), actYwGroup.getIsNewRecord())) {
        Boolean isFirst=actYwGroup.getIsNewRecord();
        super.save(actYwGroup);

        //如果是省平台保存流程步骤
        SysTenant sysTenant=sysTenantService.get(TenantConfig.getCacheTenant());
        if (sysTenant!=null && (Sval.EmPn.NPROVINCE.getPrefix()).equals(sysTenant.getType())) {
            if(isFirst) {
                actYwStepService.saveNew(actYwGroup.getId());
            }else{
                ActYwStep actYwStep = actYwStepService.getActYwStepByGroupId(actYwGroup.getId());
                if((Const.NO).equals(actYwGroup.getStatus())) {
                    actYwStep.setStep(ActYwStep.StepEnmu.STEP2.getValue());
                    actYwStepService.save(actYwStep);
                }else{
                    ActYwGroupRelation actYwGroupRelation = new ActYwGroupRelation();
                    actYwGroupRelation.setProvGroupId(actYwGroup.getId());
                    if(StringUtil.checkNotEmpty(actYwGroupRelationDao.findList(actYwGroupRelation))){
                        actYwStep.setStep(ActYwStep.StepEnmu.STEP4.getValue());
                        actYwStepService.save(actYwStep);
                    }else{
                        actYwStep.setStep(ActYwStep.StepEnmu.STEP3.getValue());
                        actYwStepService.save(actYwStep);
                    }
                }
            }
        }
    }
  }

  /**
   * 删除流程且做依赖校验.
   * 删除自定义流程约束性校验
   * 1,没关联项目，可以删除
   * 2，有关联项目，无法删除
   * @param actYwGroup
   * @return ActYwRstatus
   */
    @Transactional(readOnly = false)
    public ApiTstatus<ActYwGroup> deleteCheck(ActYwGroup actYwGroup) {
        ApiTstatus<ActYwGroup> rstats = new ApiTstatus<ActYwGroup>();
        try{
            ActYw actYw = new ActYw();
            actYw.setGroupId(actYwGroup.getId());
            List<ActYw> list = actYwService.findList(actYw);
            if(!list.isEmpty()){
                rstats.setStatus(false);
                rstats.setMsg("流程关联了项目，无法删除");
            }
            actYwGroup = get(actYwGroup);
            if((actYwGroup == null) || (Const.YES).equals(actYwGroup.getStatus())){
                rstats.setStatus(false);
                rstats.setMsg("流程已发布，无法删除");
            }
            actYwGnodeService.deletePlwlByGroup(actYwGroup.getId());
            delete(actYwGroup);
            rstats.setMsg("删除成功！");
        }catch (Exception e){
            logger.error(e.getMessage());
            rstats.setStatus(false);
            rstats.setMsg(e.getMessage());
        }
        return rstats;
    }

    @Transactional(readOnly = false)
    public void delete(ActYwGroup actYwGroup) {
      super.delete(actYwGroup);
    }

    public List<ActYwGroup> getByTanentId() {
        return null;
    }

    /**
     * 根据类型查询可关联的流程.
     * @param proType 流程类型
     * @return List
     */
    public List<ActYwGroup> getActYwGroupByModel(String proType) {
        ActYwGroup pactYwGroup = new ActYwGroup();
        pactYwGroup.setType(proType);
        pactYwGroup.setDelFlag(Const.NO);
        pactYwGroup.setStatus(ActYwGroup.GROUP_DEPLOY_1);
        return dao.findModelList(pactYwGroup);
    }
}