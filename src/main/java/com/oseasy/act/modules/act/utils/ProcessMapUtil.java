/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.act.utils
 * @Description [[_ProcessMapUtil_]]文件
 * @date 2017年6月9日 上午11:38:56
 *
 */

package com.oseasy.act.modules.act.utils;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.vo.ProcessMapVo;
import com.oseasy.act.modules.act.vo.ProjectEnd;
import com.oseasy.util.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.log4j.Logger;

/**
 * 获取流程图工具类.
 *
 * @author chenhao
 * @date 2017年6月9日 上午11:38:56
 *
 */
public class ProcessMapUtil {
  protected static final Logger LOGGER = Logger.getLogger(ProcessMapUtil.class);

  /**
   * 生成项目流程图Model .
   *
   * @author chenhao
   * @param proInsId
   *          流程实例ID
   * @param type
   *          类型
   * @param status
   *          状态
   */
  public static ProcessMapVo processMap(RepositoryService repositoryService,
      ActTaskService actTaskService, RuntimeService runtimeService, String proInsId, String type,
      String status) {
    String procDefId = actTaskService.getProcessDefinitionIdByProInstId(proInsId);
    List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        .processDefinitionId(procDefId).singleResult();
    ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
    if (pdImpl!=null) {
      String processDefinitionId = pdImpl.getId();// 流程标识
         ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
             .getDeployedProcessDefinition(processDefinitionId);
         List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点

         actImpls = dealActiveNode(runtimeService, proInsId, actImpls, activitiList, type, status);

         return new ProcessMapVo(procDefId, proInsId, actImpls);
    }else{
      return null;
    }

  }


    /**
     * 根据nodeId得到节点
     *
     * @author chenhao
     * @param proInsId
     *          流程实例ID
     */
    public static String getNodeByProInsId(RepositoryService repositoryService,ActTaskService actTaskService,
      RuntimeService runtimeService, String proInsId) {
      String procDefId = actTaskService.getProcessDefinitionIdByProInstId(proInsId);
      List<ActivityImpl> actImpls = new ArrayList<ActivityImpl>();

      ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
          .processDefinitionId(procDefId).singleResult();
      ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
      String processDefinitionId = pdImpl.getId();// 流程标识
      ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
          .getDeployedProcessDefinition(processDefinitionId);
      List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
      activitiList.get(0).getId();
      List<String> activeActivityIds = runtimeService.getActiveActivityIds(proInsId);
      String gnodeId="";
        for (String activeId : activeActivityIds) {
            for (ActivityImpl activityImpl : activitiList) {
                String id = activityImpl.getId();
                if (activityImpl.isScope()) {
                    if (activityImpl.getActivities().size() > 1) {
                        List<ActivityImpl> subAcList = activityImpl.getActivities();
                        for (ActivityImpl subActImpl : subAcList) {
                            String subid = subActImpl.getId();
                            System.out.println("subImpl:" + subid);
                            if (activeId.equals(subid)) {// 获得执行到那个节点
                                gnodeId=subid;
                                break;
                            }
                        }
                    }
                }
                if (activeId.equals(id)) {
                  // 获得执行到那个节点
                    actImpls.add(activityImpl);
                    gnodeId=id;
                    break;
                    //System.out.println(id);
                }
            }
            if (StringUtil.isNotEmpty(gnodeId)) {
              break;
            }
        }
      return gnodeId;
    }



  /**
   * 处理获取ActiveNode异常问题 .
   *
   * @author chenhao
   * @param proInsId
   *          流程实例
   * @param actImpls
   *          高亮节点
   * @param activitiList
   *          节点列表
   */
  private static List<ActivityImpl> dealActiveNode(RuntimeService runtimeService, String proInsId,
      List<ActivityImpl> actImpls, List<ActivityImpl> activitiList, String type, String status) {

    ProjectEnd pend = ProjectEnd.getByKeyVal(type, status);
    if (pend != null) {
      if (pend.equals(ProjectEnd.PE_DS_8) || (pend.equals(ProjectEnd.PE_GC_8))
          || (pend.equals(ProjectEnd.PE_GC_9))) {
        actImpls.add(activitiList.get(activitiList.size() - 1));
      } else if (pend.equals(ProjectEnd.PE_DS_0)) {
        /**
         * 定位到开始节点下一个节点.
         */
        actImpls.add(activitiList.get(0));
      } else if (pend.equals(ProjectEnd.PE_DS_9)) {
        actImpls.add(activitiList.get(activitiList.size() - 2));
      } else if (pend.equals(ProjectEnd.PE_DS_7)) {
        actImpls.add(activitiList.get(activitiList.size() - 3));
      }
      return actImpls;
    }

    if (pend == null) {
      List<String> activeActivityIds = Lists.newArrayList();
      try {
        activeActivityIds = runtimeService.getActiveActivityIds(proInsId);
      } catch (ActivitiObjectNotFoundException e) {
        LOGGER.error("Activiti Object Not Found [" + proInsId + "] ", e);
      } catch (ActivitiIllegalArgumentException e) {
        LOGGER.error("Activiti Illegal Argument [proInsId=" + proInsId + "] ", e);
      }

      for (String activeId : activeActivityIds) {
        for (ActivityImpl activityImpl : activitiList) {
          String id = activityImpl.getId();
          if (activityImpl.isScope()) {
            if (activityImpl.getActivities().size() > 1) {
              List<ActivityImpl> subAcList = activityImpl.getActivities();
              for (ActivityImpl subActImpl : subAcList) {
                String subid = subActImpl.getId();
                if (activeId.equals(subid)) {
                  /**
                   * 获得执行到那个节点.
                   */
                  actImpls.add(subActImpl);
                  break;
                }
              }
            }
          }
          if (activeId.equals(id)) {
            /**
             * 获得执行到那个节点.
             */
            actImpls.add(activityImpl);
          }
        }
      }
    }
    return actImpls;
  }
}
