package com.oseasy.act.modules.actyw.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.oseasy.act.modules.act.utils.ThreadUtils;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.dao.ActYwDao;
import com.oseasy.act.modules.actyw.dao.ActYwGnodeDao;
import com.oseasy.act.modules.actyw.dao.ActYwGroupDao;
import com.oseasy.act.modules.actyw.tool.apply.IAgnservice;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.GnodeGsep;
import com.oseasy.act.modules.actyw.tool.process.GnodeGstree;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwEcoper;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRunner;
import com.oseasy.act.modules.actyw.tool.process.cmd.exception.NoDateException;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwGparam;
import com.oseasy.act.modules.actyw.tool.process.cmd.vo.ActYwPgnode;
import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.act.modules.actyw.tool.process.rest.GnodeMargeVo;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.NodeEtype;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程节点Service.
 * @author chenh
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ActYwGnodeService extends CrudService<ActYwGnodeDao, ActYwGnode> implements IAgnservice{
    public static final String NO_PASS = "1";//审核不通过
    @Autowired
    ActYwGroupDao actYwGroupDao;
    @Autowired
    private ActYwDao actYwDao;
    @Autowired
    private ActYwStepService actYwStepService;
    @Autowired
    private ActYwGroleService actYwGroleService;
    @Autowired
    private ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private SysTenantService sysTenantService;
    //查询指派人员
    public List<Map<String,String>> getAssignUserNames(List<String> pids,String gnodeid){
    	return dao.getAssignUserNames(pids, gnodeid);
    }
    //查询指派人员
    public List<Map<String,String>> getAssignUsers(String  promodelId,String gnodeId){
        return dao.getAssignUsers(promodelId,gnodeId);
    }

    //查询有指派属性的节点
    public List<ActYwGnode> getAssignNodes(String actywid){
    	return dao.getAssignNodes(actywid);
    }
    /**
     * 查询有委派属性的节点.
     * @param actywid 流程ID
     * @return List
     */
    public List<ActYwGnode> findGnodesByDelegate(String actywid){
        return dao.findGnodesByDelegate(actywid);
    }

    //查询有审核属性的节点
    public List<ActYwGnode> getAuditNodes(String actywid){
        	return dao.getAuditNodes(actywid);
        }
    //  public List<ActYwGnode> findListByYwProcess(ActYwGnode actYwGnode) {
    //  // TODO Auto-generated method stub
    //  return null;
    //}


    /**
     * 获取第一个任务节点，如果第一个节点为子流程，则获取子流程第一个任务节点.
     * @param ywid
     * @return ActYwGnode
     */
    public ActYwGnode getFirstTaskByYwid(String ywid){
        return getFirstTask(ywid, null);
    }
    public ActYwGnode getFirstTaskByGid(String groupId){
        return getFirstTask(null, groupId);
    }
    public ActYwGnode getFirstTask(String ywid, String groupId){
        if(StringUtil.isEmpty(ywid) && StringUtil.isEmpty(groupId)){
            return null;
        }
        CompletableFuture<ActYwGnode> task150Future = CompletableFuture.supplyAsync(() -> dao.getFirstTask(ywid, groupId, GnodeType.GT_ROOT_TASK.getId()), ThreadUtils.newFixedThreadPool());
        CompletableFuture<ActYwGnode> task250Future = CompletableFuture.supplyAsync(() -> dao.getFirstTask(ywid, groupId, GnodeType.GT_PROCESS_TASK.getId()),ThreadUtils.newFixedThreadPool());
        CompletableFuture.allOf(task150Future,task250Future).join();
        ActYwGnode task150 = null;
        ActYwGnode task250 = null;
        try {
            task150 = task150Future.get();
            task250 = task250Future.get();
        } catch (InterruptedException e) {
            logger.info("查询线程中断,queryRecordIds");
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.info("执行异常,queryRecordIds");
            e.printStackTrace();
        }finally {
            ThreadUtils.shutdown();
        }

       /* ActYwGnode task150 = dao.getFirstTask(ywid, groupId, GnodeType.GT_ROOT_TASK.getId());
        ActYwGnode task250 = dao.getFirstTask(ywid, groupId, GnodeType.GT_PROCESS_TASK.getId());*/
        if((task150 != null) && (task250 == null)){
            return task150;
        }else if((task150 == null) && (task250 != null)){
            return task250;
        }else if((task150 != null) && (task250 != null)){
            if((task250.getParent().getLevel() > task150.getLevel())){
                return task150;
            }
            return task250;
        }
        return null;
    }

    /**
     * 获取最后一个任务节点，如果最后一个节点为子流程，则获取子流程最后一个任务节点.
     * @param ywid
     * @return ActYwGnode
     */
    public ActYwGnode getLastTaskByYwid(String ywid){
        return getLastTask(ywid, null);
    }
    public ActYwGnode getLastTaskByGid(String groupId){
        return getLastTask(null, groupId);
    }
    public ActYwGnode getLastTask(String ywid, String groupId){
        if(StringUtil.isEmpty(ywid) && StringUtil.isEmpty(groupId)){
            return null;
        }
        CompletableFuture<ActYwGnode> task150Future = CompletableFuture.supplyAsync(() -> dao.getLastTask(ywid, groupId, GnodeType.GT_ROOT_TASK.getId()), ThreadUtils.newFixedThreadPool());
        CompletableFuture<ActYwGnode> task250Future = CompletableFuture.supplyAsync(() -> dao.getLastTask(ywid, groupId, GnodeType.GT_PROCESS_TASK.getId()),ThreadUtils.newFixedThreadPool());

        CompletableFuture.allOf(task150Future,task250Future).join();
        ActYwGnode task150 = null;
        ActYwGnode task250 = null;
        try {
            task150 = task150Future.get();
            task250 = task250Future.get();
        } catch (InterruptedException e) {
            logger.info("查询线程中断,queryRecordIds");
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.info("执行异常,queryRecordIds");
            e.printStackTrace();
        }finally {
            ThreadUtils.shutdown();
        }
//        ActYwGnode task150 = dao.getLastTask(ywid, groupId, GnodeType.GT_ROOT_TASK.getId());
//        ActYwGnode task250 = dao.getLastTask(ywid, groupId, GnodeType.GT_PROCESS_TASK.getId());
        if(task250.getParent().getLevel() > task150.getLevel()){
            return task250;
        }
        return task150;
    }

    /****************************************************************************************************************
     * 被弃用或重写的接口.
     ***************************************************************************************************************/
    /**
     * 时间轴.
     * @param extId
     * @param ywId
     * @param level
     * @param isDetail
     * @param isYw
     * @param isAll
     * @return
     */
    public List<Map<String, Object>> treeDataForTimeIndexByYwId(String promdelId,String yearId,String extId, String ywId,  String level,  Boolean isDetail, Integer isYw,Boolean isAll) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
      	    if (StringUtil.isNotEmpty(ywId)) {
      	      ActYw actYw = actYwDao.get(ywId);
      	      if ((actYw != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
      	        mapList = treeDataForTimeIndexByGroup(promdelId,yearId,actYw.getProProject().getId(),ywId,extId, actYw.getGroupId(), level,  isDetail, isYw, isAll);
      	      }
      	    }
      	    return mapList;
    }

    /****************************************************************************************************************
    * 被弃用或重写的接口.
    ***************************************************************************************************************/
   /**
    * 时间轴.
    * @param extId
    * @param ywId
    * @param level
    * @param isDetail
    * @param isYw
    * @param isAll
    * @return
    */
   public List<Map<String, Object>> treeDataForTimeIndexByYwId(String yearId,String extId, String ywId,  String level,  Boolean isDetail, Integer isYw,Boolean isAll) {
       List<Map<String, Object>> mapList = Lists.newArrayList();
            if (StringUtil.isNotEmpty(ywId)) {
              ActYw actYw = actYwDao.get(ywId);
              if ((actYw != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
                mapList = treeDataForTimeIndexByGroup(yearId,actYw.getProProject().getId(),ywId,extId, actYw.getGroupId(), level,  isDetail, isYw, isAll);
              }
            }
            return mapList;
   }


    /**
       * 时间轴.
       * @param extId
       * @param ywId
       * @param level
       * @param isDetail
       * @param isYw
       * @param isAll
       * @return
       */
      public List<Map<String, Object>> treeDataForTimeIndexByYwId(String extId, String ywId,  String level,  Boolean isDetail, Integer isYw,Boolean isAll) {
          List<Map<String, Object>> mapList = Lists.newArrayList();
        	    if (StringUtil.isNotEmpty(ywId)) {
        	      ActYw actYw = actYwDao.get(ywId);
        	      if ((actYw != null) && StringUtil.isNotEmpty(actYw.getGroupId())) {
        	        mapList = treeDataForTimeIndexByGroup(actYw.getProProject().getId(),ywId,extId, actYw.getGroupId(), level,  isDetail, isYw, isAll);
        	      }
        	    }
        	    return mapList;
      }

      /**
       * 查询已审核的节点.
       * @param yearId 年份ID
       * @param ppid 项目ID
       * @param groupId 流程ID
       * @param promdelId 模型ID
       * @return List
       */
    public List<ActYwGnode> queryGnodeByAuditList(String yearId,String ppid,String groupId,String promdelId){
        List<ActYwGnode> actYwGnodeList =new ArrayList<ActYwGnode>();
        ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(promdelId);
       // List<ActYwAuditInfo> actYwAuditInfoList=actYwAuditInfoService.findSubStringList(promdelId);
        List<String> stringList=actYwAuditInfoService.findSubStringList(promdelId);

        ActYwGnode actYwGnodeiIndex=new ActYwGnode();
        actYwGnodeiIndex.setGroup(new ActYwGroup(groupId));
        ActYwGtime actyg=new ActYwGtime();
        actyg.setProjectId(ppid);
        actyg.setYearId(yearId);
        actYwGnodeiIndex.setActYwGtime(actyg);

        actYwGnodeList= findListBygTimeInIds(actYwGnodeiIndex,stringList);

        return actYwGnodeList;
    }

    /**
     * 废弃掉没有调用,如果调用，请更改 queryGnodeByAuditList方法.
     */
    @Deprecated
    public List<Map<String, Object>> treeDataForTimeIndexByGroup(String promdelId,String yearId,String ppid,String actywId,String extId, String groupId, String level,Boolean isDetail,Integer isYw, Boolean isAll) {
  	  List<Map<String, Object>> mapList = Lists.newArrayList();
      List<ActYwGnode> list = queryGnodeByAuditList(yearId,ppid,groupId,promdelId);
      //更改这里传递proModel.getProcInsId()参数
      //List<ActYwGnode> list = queryGnodeByAuditList(yearId,ppid,groupId,promdelId, proModel.getProcInsId());

              //queryForTimeIndexByYw(yearId,ppid,groupId, level, isYw);
      for (int i=0; i<list.size(); i++) {
        ActYwGnode e = list.get(i);
        if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
          Map<String, Object> map = Maps.newHashMap();
          map.put("id", e.getId());
          map.put("pId", e.getParentId());
          map.put("name", e.getName());
          if(e.getGforms()!=null && e.getGforms().size()>0 && e.getGforms().get(0)!=null
                  && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null){
              map.put("formId", (e.getGforms()!=null && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null?e.getGforms().get(0).getForm().getId():"javascript:void(0);"));
              map.put("formName", (e.getGforms().get(0).getForm()==null?"":e.getGforms().get(0).getForm().getName()));
          }
          map.put("beginDate", (e.getActYwGtime()==null?null:e.getActYwGtime().getBeginDate()));
          map.put("endDate",  (e.getActYwGtime()==null?null:e.getActYwGtime().getEndDate()));
          if ((isDetail != null) && isDetail) {
            map.put("gnode", e);
          }
          mapList.add(map);
        }
      }
      return mapList;
    }

    public List<Map<String, Object>> treeDataForTimeIndexByGroup(String yearId,String ppid,String actywId,String extId, String groupId, String level,Boolean isDetail,Integer isYw, Boolean isAll) {
    	  List<Map<String, Object>> mapList = Lists.newArrayList();

        List<ActYwGnode> list = queryForTimeIndexByYw(yearId,ppid,groupId, level, isYw);
        for (int i=0; i<list.size(); i++) {
          ActYwGnode e = list.get(i);
          if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("name", e.getName());
            if(e.getGforms()!=null && e.getGforms().size()>0 && e.getGforms().get(0)!=null
                    && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null){
                map.put("formId", (e.getGforms()!=null && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null?e.getGforms().get(0).getForm().getId():"javascript:void(0);"));
                map.put("formName", (e.getGforms().get(0).getForm()==null?"":e.getGforms().get(0).getForm().getName()));
            }
            map.put("beginDate", (e.getActYwGtime()==null?null:e.getActYwGtime().getBeginDate()));
            map.put("endDate",  (e.getActYwGtime()==null?null:e.getActYwGtime().getEndDate()));
            if ((isDetail != null) && isDetail) {
              map.put("gnode", e);
            }
            mapList.add(map);
          }
        }
        return mapList;
      }

    public List<Map<String, Object>> treeDataForTimeIndexByGroup(String ppid,String actywId,String extId, String groupId, String level,Boolean isDetail,Integer isYw, Boolean isAll) {
      	  List<Map<String, Object>> mapList = Lists.newArrayList();

          List<ActYwGnode> list = queryForTimeIndexByYw(ppid,groupId, level, isYw);
          for (int i=0; i<list.size(); i++) {
            ActYwGnode e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
              Map<String, Object> map = Maps.newHashMap();
              map.put("id", e.getId());
              map.put("pId", e.getParentId());
              map.put("name", e.getName());
              if(e.getGforms()!=null && e.getGforms().size()>0 && e.getGforms().get(0)!=null
                      && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null){
                  map.put("formId", (e.getGforms()!=null && e.getGforms().get(0).getForm()!=null&&e.getGforms().get(0).getForm().getId()!=null?e.getGforms().get(0).getForm().getId():"javascript:void(0);"));
                  map.put("formName", (e.getGforms().get(0).getForm()==null?"":e.getGforms().get(0).getForm().getName()));
              }
              map.put("beginDate", (e.getActYwGtime()==null?null:e.getActYwGtime().getBeginDate()));
              map.put("endDate",  (e.getActYwGtime()==null?null:e.getActYwGtime().getEndDate()));
              if ((isDetail != null) && isDetail) {
                map.put("gnode", e);
              }
              mapList.add(map);
            }
          }
          return mapList;
        }

    public List<ActYwGnode> queryForTimeIndexByYw(String yearId,String ppid,String groupId, String level, Integer isYw) {
   	    ActYwGnode pactYwGnode = new ActYwGnode();
        List<ActYwGnode> list = null;
   	    if (isYw == null) {
            isYw = ActYwGnode.L_YW;
            if ((isYw).equals(ActYwGnode.L_PROCESS)) {
                list = findListByYwProcess(pactYwGnode, groupId);
            } else if ((isYw).equals(ActYwGnode.L_YW)) {
                //list = findListForTimeIndexByYwGroup(pactYwGnode);
                pactYwGnode.setGroup(new ActYwGroup(groupId));
                ActYwGtime actyg=new ActYwGtime();
                actyg.setProjectId(ppid);
                actyg.setYearId(yearId);
                pactYwGnode.setActYwGtime(actyg);
                list = findListTimeByg(pactYwGnode);
            } else if ((isYw).equals(ActYwGnode.L_ALL)) {
                list = findListByGroup(pactYwGnode);
            } else {
                logger.warn("业务类型未定义！！");
                return Lists.newArrayList();
            }
        }
   	    return list;
   	  }

    public List<ActYwGnode> queryForTimeIndexByYw(String ppid,String groupId, String level, Integer isYw) {
       	    ActYwGnode pactYwGnode = new ActYwGnode();
            List<ActYwGnode> list = null;
       	    if (isYw == null) {
                isYw = ActYwGnode.L_YW;
                if ((isYw).equals(ActYwGnode.L_PROCESS)) {
                    list = findListByYwProcess(pactYwGnode, groupId);
                } else if ((isYw).equals(ActYwGnode.L_YW)) {
                    //list = findListForTimeIndexByYwGroup(pactYwGnode);
                    pactYwGnode.setGroup(new ActYwGroup(groupId));
                    ActYwGtime actyg=new ActYwGtime();
                    actyg.setProjectId(ppid);
                    pactYwGnode.setActYwGtime(actyg);
                    list = findListTimeByg(pactYwGnode);
                } else if ((isYw).equals(ActYwGnode.L_ALL)) {
                    list = findListByGroup(pactYwGnode);
                } else {
                    logger.warn("业务类型未定义！！");
                    return Lists.newArrayList();
                }
            }
       	    return list;
       	  }

//    /**
//      * 根据流程组获取流程(type=19).
//      *
//      * @param actYwGnode
//      *          流程节点
//      * @return List
//      */
//     public List<ActYwGnode> findListByYwProcessGroup(ActYwGnode actYwGnode,String groupId) {
//
//       return findListByYwProcess(actYwGnode,groupId);
//     }


    public List<ActYwGnode> findListByYwProcess(ActYwGnode actYwGnode,String groupId) {
        List<ActYwGnode> actYwGnodes = new   ArrayList<ActYwGnode>();
        if(GnodeType.GT_PROCESS.getId().equals(actYwGnode.getType())){
            actYwGnodes = findListBygYwGprocess(groupId,actYwGnode.getId());
        }else{
            actYwGnodes.add(actYwGnode);
        }
        return actYwGnodes;
    }

    /****************************************************************************************************************
     * 民大版本-新修改的接口.
     ***************************************************************************************************************/
    /**
     * 查找结束节点的前一个业务节点，添加网关后会存在多个结束节点（大赛、大创专用）.
     * @param groupId
     * @return
     */
    public ActYwGnode getEndPreFun(String groupId) {
        List<ActYwGnode> ends = getEndByRoot(groupId);
        if(StringUtil.checkEmpty(ends) || (ends.size() != 1)){
            logger.error("不符合业务逻辑");
            return null;
        }
        return ends.get(0);
    }

    /**
     * 获取节点名称
     * @param groupId
     * @return
     */
    public ActYwGnode getEndPreFunOfName(String groupId) {
        List<ActYwGnode> ends = getEndNamePreFun(groupId);
        if(StringUtil.checkEmpty(ends) || (ends.size() != 1)){
            logger.error("不符合业务逻辑");
            return null;
        }
        return ends.get(0);
    }

    /**
     * 获取结束节点.
     * @param groupId 流程ID
     * @param preFunId 前置业务节点
     * @param status 结束状态
     * @return ActYwGnode
     */
    public ActYwGnode getEndByPreFun(String groupId, String preFunId, String status) {
        List<ActYwGnode> ends = getEndByRoot(groupId);
        if(StringUtil.checkEmpty(ends) || (ends.size() != 1)){
            logger.error("不符合业务逻辑");
            return null;
        }
        ActYwGnode end = ends.get(0);
        //GnodeType.GT_ROOT_TASK.getId();
        return end;
    }


    /**
     * 根据流程ID和ParentId查找业务节点.
     * @param pactYwGnode
     * @return List
     */
    public List<ActYwGnode> findListByYw(ActYwGnode pactYwGnode) {
        if(pactYwGnode == null){
            pactYwGnode = new ActYwGnode();
        }

        if(StringUtil.checkEmpty(pactYwGnode.getTypes())){
            List<String> types = Lists.newArrayList();
            types.add(GnodeType.GT_ROOT_TASK.getId());
            types.add(GnodeType.GT_PROCESS.getId());
            types.add(GnodeType.GT_PROCESS_TASK.getId());
            pactYwGnode.setTypes(types);
        }
        return findListByGparent(pactYwGnode);
    }

    //判断流程中是否含有一级网关
    public boolean isLevelGate(String  groupId){
        boolean isLevelGate=false;
        List<ActYwGnode> actYwGnodeList =findGateWayByGroupId(groupId);
        if(StringUtil.checkNotEmpty(actYwGnodeList)){
            isLevelGate=true;
        }
        return isLevelGate;
    }
    /**
     * 根据流程ID和ParentId查找一级网关节点.
     * @param groupId
     * @return List
     */
    public List<ActYwGnode> findGateWayByGroupId(String  groupId) {
        ActYwGnode pactYwGnode=new ActYwGnode();
        pactYwGnode.setGroup(new ActYwGroup(groupId));
        pactYwGnode.setParent(new ActYwGnode(CoreIds.NCE_SYS_TREE_ROOT.getId()));
        //找一级网关节点
        List<String> types = Lists.newArrayList();
        types.add(GnodeType.GT_ROOT_GATEWAY.getId());
        pactYwGnode.setTypes(types);

        return findListByGparent(pactYwGnode);
    }

    /**
     * 根据流程ID和父级ID获取节点.
     * @param extId
     * @param ywId
     * @param parentId
     * @param isAll
     * @return List
     */
    public List<Map<String, Object>> treeDataByYwId(String extId, String ywId, String parentId, Boolean isAll) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        if(StringUtil.isEmpty(parentId)){
            parentId = ActYwGnode.getRootId();
        }
        if(StringUtil.isNotEmpty(ywId)){
          ActYw actYw = actYwDao.get(ywId);
          if((actYw != null) && StringUtil.isNotEmpty(actYw.getGroupId())){
            mapList = treeDataByGroup(extId, actYw.getGroupId(), parentId, true, isAll);
          }
        }
        return mapList;
    }

    /**
     * 根据流程ID和父级ID获取节点.
     * @param extId
     * @param groupId
     * @param parentId
     * @param isDetail
     * @param isAll
     * @return List
     */
    public List<Map<String, Object>> treeDataByGroup(String extId, String groupId, String parentId, Boolean isDetail,Boolean isAll) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        ActYwGnode pactYwGnode = new ActYwGnode();
        pactYwGnode.setGroup(new ActYwGroup(groupId));
        pactYwGnode.setParent(new ActYwGnode(parentId));
        List<ActYwGnode> list = findListByYw(pactYwGnode);
        if(StringUtil.checkEmpty(list)){
            return mapList;
        }

        for (int i=0; i<list.size(); i++) {
            ActYwGnode e = list.get(i);
            if (StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)) {
              Map<String, Object> map = Maps.newHashMap();
              map.put("id", e.getId());
              map.put("pId", e.getParentId());
              map.put("name", e.getName());
              if((isDetail != null) && isDetail){
                map.put("gnode", e);
              }
              mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 根据父节点模糊查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.

     * @param gnode 过滤的节点类型
     * @return List
     */
    public List<ActYwGnode> findByYwParentIdsLike(ActYwGnode gnode) {
        List<String> types = Lists.newArrayList();
        types.add(GnodeType.GT_ROOT_START.getId());
        types.add(GnodeType.GT_ROOT_TASK.getId());
        types.add(GnodeType.GT_ROOT_END.getId());
        types.add(GnodeType.GT_ROOT_GATEWAY.getId());
        types.add(GnodeType.GT_PROCESS.getId());

        types.add(GnodeType.GT_PROCESS_START.getId());
        types.add(GnodeType.GT_PROCESS_TASK.getId());
        types.add(GnodeType.GT_PROCESS_END.getId());
        types.add(GnodeType.GT_PROCESS_GATEWAY.getId());
        return findListBygParentIdsLike(gnode, types);
    }

    /**
     * 根据父节点模糊查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.
     * @param entity 父节点IDS
     * @param types 过滤的节点类型
     * @return List
     */
    public List<ActYwGnode> findListBygParentIdsLike(ActYwGnode entity, List<String> types) {
        if((entity.getParent() != null) && StringUtil.isNotEmpty(entity.getParent().getId())){
            // 设置新的父节点串
            if((ActYwGnode.getRootPid()).equals(entity.getParent().getId())){
                entity.setParentIds(ActYwGnode.getRootPid() + ",");
            }else{
                entity.setParent(this.get(entity.getParent().getId()));
                entity.setParentIds(entity.getParent().getParentIds() + entity.getParent().getId() + ",");
            }
        }
        if((entity == null) || StringUtil.isEmpty(entity.getParentIds())){
            return new ArrayList<>(0);
        }

        if(StringUtil.checkNotEmpty(types)){
            entity.setTypes(types);
        }
        return findAllListByg(dao.findListBygParentIdsLike(entity), entity);
    }

    /**
      * 根据流程获取节点时间.
      * @param gnode 流程节点,groupId必填
      * @return List
      */
     public List<ActYwGnode> findAllListTimeByg(ActYwGnode gnode) {
       return dao.findAllListTimeByg(gnode);
     }

    /**
          * 根据流程获取节点时间,关联所有属性.
          * @param gnode 流程节点,groupId必填
          * @return List
          */
        public List<ActYwGnode> findListTimeByg(ActYwGnode gnode) {
           return findAllListByg(dao.findAllListTimeByg(gnode), gnode);
       }

    public List<ActYwGnode> findListYwTimeByg(ActYwGnode gnode) {
           return findAllListByg(dao.findAllListTimeByg(gnode), gnode);
       }
    /**
     * 查询所有流程节点.
     * @return List
     */
    @SuppressWarnings("deprecation")
    public List<ActYwGnode> findAllList() {
        return dao.findAllList();
    }

    @Transactional(readOnly = false)
    public void save(ActYwGnode actYwGnode) {
        if((actYwGnode.getParent() != null) && StringUtil.isNotEmpty(actYwGnode.getParent().getId())){
            // 设置新的父节点串
            if((ActYwGnode.getRootPid()).equals(actYwGnode.getParent().getId())){
                actYwGnode.setParentIds(ActYwGnode.getRootPid() + ",");
            }else{
                actYwGnode.setParent(this.get(actYwGnode.getParent().getId()));
                actYwGnode.setParentIds(actYwGnode.getParent().getParentIds() + actYwGnode.getParent().getId() + ",");
            }
        }
        super.save(actYwGnode);
    }

    @Transactional(readOnly = false)
    public void delete(ActYwGnode actYwGnode) {
        super.delete(actYwGnode);
    }

    @Transactional(readOnly = false)
    public void deleteWL(ActYwGnode actYwGnode) {
      dao.deleteWL(actYwGnode);
    }

    public ActYwGnode get(String id) {
        return getByg(id);
    }

    public ActYwGnode getByg(String id) {
        ActYwGnode entity = dao.getByg(id);
        if(entity == null){
            return null;
        }
        return findAllListByg(entity, new ActYwGnode(entity.getGroup()));
    }

    public ActYwGnode getBygTime(String id, String projectId, String yearId) {
        ActYwGnode entity = dao.getByg(id);
        if(entity == null){
            return null;
        }
        return findAllListByg(entity, new ActYwGnode(entity.getGroup(), new ActYwGtime(projectId, yearId)), true);
    }

    public ActYwGnode getBygGclazz(String id) {
        ActYwGnode entity = dao.getByg(id);
        if(entity == null){
            return null;
        }
        return findAllListBygGclazz(entity, new ActYwGnode(entity.getGroup()));
    }

    public List<ActYwGnode> findList(ActYwGnode actYwGnode) {
        return super.findList(actYwGnode);
    }

    public Page<ActYwGnode> findPage(Page<ActYwGnode> page, ActYwGnode actYwGnode) {
        return super.findPage(page, actYwGnode);
    }

    public List<ActYwGnode> findListByg(ActYwGnode entity) {
        if(entity == null){
            return null;
        }
        return findAllListByg(dao.findListByg(entity), new ActYwGnode(entity.getGroup()));
    }

    public Page<ActYwGnode> findPageByg(Page<ActYwGnode> page, ActYwGnode entity) {
        entity.setPage(page);
        page.setList(findListByg(entity));
        return page;
    }

    public List<ActYwGnode> getALLByGroup(ActYwGnode actYwGnode) {
        return dao.getALLByGroup(actYwGnode);
    }

    /**
     * 关联查询所有.
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListByg(ActYwGnode gnode){
        return findAllListByg(gnode, true, true, true, true, false, false);
    }
    public List<ActYwGnode> findAllListBygTime(ActYwGnode gnode, Boolean addTime){
        return findAllListByg(gnode, true, true, true, true, false, addTime);
    }
    public List<ActYwGnode> findAllListBygClazz(ActYwGnode gnode, Boolean addClazz){
        return findAllListByg(gnode, true, true, true, true, addClazz, false);
    }
    public List<ActYwGnode> findAllListByg(ActYwGnode gnode, Boolean addClazz, Boolean addTime){
        return findAllListByg(gnode, true, true, true, true, addClazz, addTime);
    }
    public List<ActYwGnode> findAllListByg(ActYwGnode gnode, Boolean addUser, Boolean addRole, Boolean addStatus, Boolean addForm, Boolean addClazz, Boolean addTime){
        return findAllListByg(dao.findList(gnode), gnode, addUser, addRole, addStatus, addForm, addClazz, addTime);
    }

    /**
     * 关联查询所有(根据节点初始化所有数据).
     * @param entitys 查询结果集
     * @param entity groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListByg(List<ActYwGnode> entitys, ActYwGnode entity){
        return findAllListByg(entitys, entity, true, true, true, true, false, false);
    }
    public List<ActYwGnode> findAllListBygClazz(List<ActYwGnode> entitys, ActYwGnode entity, Boolean addClazz){
        return findAllListByg(entitys, entity, true, true, true, true, addClazz, false);
    }
    public List<ActYwGnode> findAllListBygTime(List<ActYwGnode> entitys, ActYwGnode entity, Boolean addTime){
        return findAllListByg(entitys, entity, true, true, true, true, false, addTime);
    }
    public List<ActYwGnode> findAllListBygOnlyTime(List<ActYwGnode> entitys, ActYwGnode entity){
       return findAllListByg(entitys, entity, false, false, false, false, false, true);
   }
    public List<ActYwGnode> findAllListByg(List<ActYwGnode> entitys, ActYwGnode entity, Boolean addClazz, Boolean addTime){
        return findAllListByg(entitys, entity, true, true, true, true, addClazz, addTime);
    }
    public List<ActYwGnode> findAllListByg(List<ActYwGnode> entitys, ActYwGnode gnode, Boolean addUser, Boolean addRole, Boolean addStatus, Boolean addForm, Boolean addClazz, Boolean addTime){
        if(StringUtil.checkEmpty(entitys)){
            return entitys;
        }

        List<ActYwGnode> entityUsers = null;
        List<ActYwGnode> entityRoles = null;
        List<ActYwGnode> entityForms = null;
        List<ActYwGnode> entityDroles = null;
        List<ActYwGnode> entityDforms = null;
        List<ActYwGnode> entityStatuss = null;
        List<ActYwGnode> entityClazzs = null;
        List<ActYwGnode> entityTimes = null;
        Boolean addTim = (addTime && (gnode.getActYwGtime() != null) && StringUtil.isNotEmpty(gnode.getActYwGtime().getProjectId()));//时间
        if(addUser){
            entityUsers = dao.findAllListUserByg(gnode);
        }
        if(addRole){
            entityRoles = dao.findAllListRoleByg(gnode);
            entityDroles = dao.findAllListDroleByg(gnode);
        }
        if(addStatus){
            entityStatuss = dao.findAllListStatusByg(gnode);
        }
        if(addForm){
            entityForms = dao.findAllListFormByg(gnode);
            entityDforms = dao.findAllListDformByg(gnode);
        }
        if(addClazz){
            entityClazzs = dao.findAllListClazzByg(gnode);
        }
        if(addTim){
            entityTimes = dao.findAllListTimeByg(gnode);
        }

        for (ActYwGnode cur : entitys) {
            if(addUser && StringUtil.checkNotEmpty(entityUsers)){
                for (ActYwGnode curu : entityUsers) {
                    if((cur.getId()).equals(curu.getId())){
                        cur.setGusers(curu.getGusers());
                        break;
                    }
                }
            }

            if(addRole){
                if(StringUtil.checkNotEmpty(entityRoles)){
                    for (ActYwGnode curr : entityRoles) {
                        if((cur.getId()).equals(curr.getId())){
                            cur.setGroles(curr.getGroles());
                            break;
                        }
                    }
                }

                if(StringUtil.checkNotEmpty(entityDroles)){
                    for (ActYwGnode curr : entityDroles) {
                        if((cur.getId()).equals(curr.getId())){
                            cur.setGdroles(curr.getGdroles());
                            break;
                        }
                    }
                }
            }

            if(addStatus && StringUtil.checkNotEmpty(entityStatuss)){
                for (ActYwGnode curs : entityStatuss) {
                    if((cur.getId()).equals(curs.getId())){
                        cur.setGstatuss(curs.getGstatuss());
                        break;
                    }
                }
            }

            if(addForm){
                if(StringUtil.checkNotEmpty(entityForms)){
                    for (ActYwGnode curf : entityForms) {
                        if((cur.getId()).equals(curf.getId())){
                            cur.setGforms(curf.getGforms());
                            break;
                        }
                    }
                }
                if(StringUtil.checkNotEmpty(entityDforms)){
                    for (ActYwGnode curf : entityDforms) {
                        if((cur.getId()).equals(curf.getId())){
                            cur.setGdforms(curf.getGdforms());
                            break;
                        }
                    }
                }
            }

            if(addTim && StringUtil.checkNotEmpty(entityTimes)){
                for (ActYwGnode curt : entityTimes) {
                    if((cur.getId()).equals(curt.getId())){
                        cur.setActYwGtime(curt.getActYwGtime());
                        break;
                    }
                }
            }
        }
        return entitys;
    }


    /**
     * 根据groupId查询所有节点涉及到的角色id列表（去重）
     * @param groupId
     * @return
     */
    public Set<String> getRolesByGroup(String groupId){
        ActYwGroup actYwGroup = new ActYwGroup(groupId);
        ActYwGrole actYwGrole = new ActYwGrole();
        actYwGrole.setGroup(actYwGroup);
        List<ActYwGrole> roles = actYwGroleService.findList(actYwGrole);
        //这个地方可能会报错（角色空指针-原因：系统内没有固定角色的专家存在）
        return roles.stream().map(e -> e.getRole().getId()).collect(Collectors.toSet());
    }

    /**
     * 关联查询所有(根据节点初始化所有数据).
     * @param enty 查询结果集
     * @param entity groupId必填
     * @return List
     */
    public ActYwGnode findAllListByg(ActYwGnode enty, ActYwGnode entity){
        return findAllListByg(enty, entity, false);
    }
    public ActYwGnode findAllListByg(ActYwGnode enty, ActYwGnode entity, Boolean addTime){
        if((enty == null) || StringUtil.isEmpty(enty.getId())){
            return enty;
        }

        List<ActYwGnode> etys = findAllListByg(entity, true, true, true, true, false, addTime);
        for (ActYwGnode curety : etys) {
            if((enty.getId()).equals(curety.getId())){
                enty.setGdforms(curety.getGdforms());
                enty.setGdroles(curety.getGdroles());
                enty.setGforms(curety.getGforms());
                enty.setGroles(curety.getGroles());
                enty.setGstatuss(curety.getGstatuss());
                enty.setGusers(curety.getGusers());
                enty.setActYwGtime(curety.getActYwGtime());
                break;
            }
        }
        return enty;
    }


    /**
     * 关联查询所有(根据节点初始化所有数据)-Gclass.
     * @param enty 查询结果集
     * @param entity groupId必填
     * @return List
     */
    public ActYwGnode findAllListBygGclazz(ActYwGnode enty, ActYwGnode entity){
        if((enty == null) || StringUtil.isEmpty(enty.getId())){
            return enty;
        }
        List<ActYwGnode> etys = findAllListByg(entity, true, true, true, true, true, false);
        for (ActYwGnode curety : etys) {
            if((enty.getId()).equals(curety.getId())){
                enty.setGdforms(curety.getGdforms());
                enty.setGdroles(curety.getGdroles());
                enty.setGforms(curety.getGforms());
                enty.setGroles(curety.getGroles());
                enty.setGstatuss(curety.getGstatuss());
                enty.setGusers(curety.getGusers());
                enty.setActYwGtime(curety.getActYwGtime());
                break;
            }
        }
        return enty;
    }


    /**
     * 根据流程查询流程节点.
     * @param gnode
     * @return
     */
    public List<ActYwGnode> findListByGroup(ActYwGnode gnode) {
        return dao.findListByGroup(gnode);
    }

    /**
     * 根据流程查询流程节点（关联表单、角色、用户、状态）.
     * @param entity
     * @return List
     */
    public List<ActYwGnode> findListBygGroup(ActYwGnode entity) {
        return findAllListByg(dao.findListBygGroup(entity), entity);
    }

    /**
    * 根据流程查询流程节点（关联表单、角色、用户、状态）.
    * @param entity
    * @return List
    */
   public List<ActYwGnode> findListTimeBygGroup(ActYwGnode entity) {
       return findAllListBygOnlyTime(dao.findListBygGroup(entity), entity);
   }

    /**
     * 获取排序链表.
     * @param groupId 流程ID
     * @return List
     */
    public List<GnodeGsep> findSortBygGroup(String groupId, Boolean hasSubs) {
        if(StringUtil.isEmpty(groupId)){
            return null;
        }

        if(hasSubs == null){
            hasSubs = false;
        }

        String[] param = new String[]{GnodeType.GT_ROOT_END.getId(), GnodeType.GT_PROCESS_END.getId(),
                GnodeType.GT_ROOT_GATEWAY.getId(), GnodeType.GT_PROCESS_GATEWAY.getId()
                /*,GnodeType.GT_ROOT_START.getId(), GnodeType.GT_PROCESS_START.getId()*/
                };
        List<ActYwGnode> rgnodes = findListBygGroup(new ActYwGnode(new ActYwGroup(groupId)));
        List<ActYwGnode> sgends = findListBygGroup(new ActYwGnode(groupId, Arrays.asList(param)));
        rgnodes = ActYwTool.initProps(rgnodes);
        if(hasSubs){
            logger.warn("子流程排序暂未实现！");
            return null;
        }else{
            return ActYwTool.initLinks(sgends, rgnodes);
        }
    }

    public List<GnodeGsep> findSortBygGroup(String groupId) {
        return findSortBygGroup(groupId, false);
    }

    /**
     * 获取树结构排序链表.
     * @param groupId 流程ID
     * @return List
     */
    public GnodeGstree findStreeBygGroup(String groupId, Boolean hasSubs) {
        if(StringUtil.isEmpty(groupId)){
            return null;
        }

        if(hasSubs == null){
            hasSubs = false;
        }

        List<ActYwGnode> rgnodes = findListBygGroup(new ActYwGnode(new ActYwGroup(groupId)));
        ActYwGnode start = ActYwTool.initStartProps(getStart(groupId), rgnodes);
        rgnodes = ActYwTool.initProps(rgnodes);
        if(hasSubs){
            logger.warn("子流程树排序暂未实现！");
            return null;
        }else{
            return ActYwTool.initTrees(start, rgnodes);
        }
    }

    public GnodeGstree findStreeBygGroup(String groupId) {
        return findStreeBygGroup(groupId, false);
    }

    /**
     * 根据流程ID和子流程ID查询流程节点.
     * @param groupId 流程ID
     * @param pid 子流程ID
     * @return List
     */
    public List<ActYwGnode> findListByGprocess(String groupId, String pid) {
        if(StringUtil.isEmpty(groupId) || StringUtil.isEmpty(pid)){
            return null;
        }
        ActYwGnode pentity = new ActYwGnode();
        pentity.setGroup(new ActYwGroup(groupId));
        pentity.setParent(new ActYwGnode(pid));
        return dao.findListByGroup(pentity);
    }

    /**
     * 根据IDS查询流程节点.
     * @param ids 节点ID
     * @return List
     */
    public List<ActYwGnode> findListByInIds(String ids) {
        if (StringUtil.isEmpty(ids)) {
          return Lists.newArrayList();
        }
        return findListByInIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)));
    }

    public List<ActYwGnode> findListByInIds(List<String> ids) {
        if ((ids != null) && (ids.size() > 0)) {
          return dao.findListByInIds(ids);
        }
        return Lists.newArrayList();
    }

    public List<ActYwGnode> findListBygTimeInIds(ActYwGnode entity, List<String> ids) {
        return findAllListBygTime(findListByInIds(ids), entity, true);
    }

    /**
     * 根据IDS查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.
     * @param ids 节点ID
     * @return List
     */
    public List<ActYwGnode> findListBygInIds(String ids, String groupId) {
        if (StringUtil.isEmpty(ids) || StringUtil.isEmpty(groupId)) {
            return Lists.newArrayList();
        }
        return findListBygInIds(Arrays.asList(StringUtil.split(ids, StringUtil.DOTH)), groupId);
    }

    public List<ActYwGnode> findListBygInIds(List<String> ids, String groupId) {
        if ((ids != null) && (ids.size() > 0)) {
          return findAllListByg(dao.findListBygInIds(ids), new ActYwGnode(new ActYwGroup(groupId)));
        }
        return Lists.newArrayList();
    }

    /**
     * 删除节点及节点属性.
     * @param engine 执行引擎
     * @param gnode 流程节点
     * @return GnodeMargeVo
     */
    @Transactional(readOnly = false)
    public GnodeMargeVo margeDeleteWl(ActYwEngineImpl engine, ActYwGnode gnode) throws Exception{
        GnodeMargeVo margeVo = new GnodeMargeVo(gnode.getGroup(), false, "执行失败");
        try {
            if ((gnode.getGroup() == null) || StringUtil.isEmpty(gnode.getGroup().getId())
                    || StringUtil.isEmpty(gnode.getGroup().getId())) {
                return margeVo;
            }
            engine.gform().deletePlwl(gnode.getGroup().getId(), gnode.getId());
            engine.grole().deletePlwl(gnode.getGroup().getId(), gnode.getId());
            engine.gstatus().deletePlwl(gnode.getGroup().getId(), gnode.getId());
            engine.guser().deletePlwl(gnode.getGroup().getId(), gnode.getId());
            engine.gtime().deletePlwl(gnode.getGroup().getId(), gnode.getId());
            deleteWL(gnode);
            margeVo.setGnode(gnode);
            margeVo.setStatus(true);
            margeVo.setMsg("执行成功");
            return margeVo;
        } catch (Exception e) {
            margeVo.setStatus(false);
            margeVo.setMsg("节点删除失败");
            logger.error("节点保存失败, id = " + gnode.getId(), e.getMessage());
            throw new Exception("节点删除失败, id = "+gnode.getId());
        }
    }

    /**
     * 保存节点及节点属性.
     * @param engine 执行引擎
     * @param gnode 流程节点
     * @return GnodeMargeVo
     */
    @Transactional(readOnly = false)
    public GnodeMargeVo margeList(ActYwEngineImpl engine, ActYwGnode gnode) throws Exception{
        GnodeMargeVo margeVo = new GnodeMargeVo(gnode.getGroup(), false, "执行失败");
        try {
            if(gnode.getIsForm() == null){
                gnode.setIsForm(false);
            }
            save(gnode);

            /**
             * 保存表单参数.
             */
            if ((GnodeType.getIdByForms()).contains(gnode.getType())) {
                if (!StringUtil.checkEmpty(gnode.getGforms())) {
                    engine.gform().savePl(gnode.getGforms());
                }

                if (!StringUtil.checkEmpty(gnode.getGdforms())) {
                    engine.gform().savePl(gnode.getGdforms());
                }
            }

            /**
             * 保存角色参数.
             */
            if ((GnodeType.getIdByRoles()).contains(gnode.getType())) {
                if (!StringUtil.checkEmpty(gnode.getGroles())) {
                    engine.grole().savePl(gnode.getGroles());
                }

                if (!StringUtil.checkEmpty(gnode.getGdroles())) {
                    engine.grole().savePl(gnode.getGdroles());
                }
            }

            /**
             * 保存用户参数.
             */
            if ((GnodeType.getIdByUsers()).contains(gnode.getType())) {
                if (!StringUtil.checkEmpty(gnode.getGusers())) {
                    engine.guser().savePl(gnode.getGusers());
                }
            }

            /**
             * 验证节点 状态参数.
             */
            if ((GnodeType.getIdByGstatuss()).contains(gnode.getType())) {
                if (!StringUtil.checkEmpty(gnode.getGstatuss())) {
                    engine.gstatus().savePl(gnode.getGstatuss());
                }
            }

            margeVo.setStatus(true);
            margeVo.setMsg("保存成功");
        } catch (Exception e) {
            margeVo.setStatus(false);
            margeVo.setMsg("节点保存失败");
            logger.error("节点保存失败, id = " + gnode.getId(), e.getMessage());
            throw new Exception("节点保存失败, id = "+gnode.getId()+"-->"+e.getMessage());
        }
        return margeVo;
    }

    /**
     * 流程节点保存.
     * @param runner 执行器
     * @param gparam 参数
     * @param hasBakVersion 是否开启版本备份
     * @return ActYwRstatus
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<ActYwGnode>> savePl(ActYwRunner runner, ActYwGparam gparam, Boolean hasBakVersion) throws Exception {
        ActYwGroup group = actYwGroupDao.get(gparam.getGroupId());
        group.setTemp(gparam.getTemp());
        group.setUiHtml(gparam.getUiHtml());
        group.setUiJson(gparam.getUiJson().toString());
        if(hasBakVersion){
            return saveBakAll(runner, group, ActYwGparam.convert(runner, group, gparam));
        }else{
            deletePlwlByGroup(runner, gparam.getGroupId());//清空流程数据
            return savePl(runner, group, ActYwGparam.convert(runner, group, gparam));
        }
    }

    @Transactional(readOnly = false)
    public ApiTstatus<List<ActYwGnode>> savePl(ActYwRunner runner, ActYwGparam gparam) {
        try {
            return savePl(runner, gparam, false);
        } catch (Exception e) {
            logger.error("命令执行出错：", e.getMessage());
        }
        return null;
    }

    /**
     * 流程节点保存(考虑数据备份、还原).
     * @param runner 执行器
     * @param group 参数
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<ActYwGnode>> saveBakAll(ActYwRunner runner, ActYwGroup group, List<ActYwGnode> curgnodes) {
        String version = IdGen.uuid();
        List<ActYwGnode> oldGnodes = null;
        if((group != null) && StringUtil.isNotEmpty(group.getId())){
            oldGnodes = runner.getEngine().gnode().findListByGroup(new ActYwGnode(new ActYwGroup(group.getId())));
            if(StringUtil.checkNotEmpty(oldGnodes)){
                savePlVersion(version, oldGnodes);//保存到备份表
                deletePlwlByGroup(group.getId());
            }
        }

        ApiTstatus<List<ActYwGnode>> rstatus = savePl(runner, group, curgnodes);

        if(!rstatus.getStatus() && StringUtil.checkNotEmpty(oldGnodes)){
            savePl(oldGnodes);
        }
        return rstatus;
    }

    /**
     * 流程节点保存.
     * @param runner 执行器
     * @param gnodes 节点列表
     * @return ActYwRstatus
     */
    @Transactional(readOnly = false)
    public ApiTstatus<List<ActYwGnode>> savePl(ActYwRunner runner, ActYwGroup group, List<ActYwGnode> gnodes) {
        List<ActYwGnode> rerrors = Lists.newArrayList();
        //List<ActYwGnode> rgnodes = Lists.newArrayList();
        for (ActYwGnode gnode : gnodes) {
            gnode.setIsNewRecord(true);
            ApiTstatus<ActYwGnode> rstatus = runner.callExecute(ActYwEcoper.ECR_ADD, new ActYwPgnode(gnode));
            if(rstatus.getStatus()){

                //rgnodes.add(gnode);
            }else{
                rerrors.add(gnode);
                return new ApiTstatus<List<ActYwGnode>>(false, rstatus.getMsg(), rerrors);
            }
        }
        /**
         *
         * 更新group的uiJson和uiHtml属性.
         */
//        Date date11 = new Date();
//        System.out.println("saveAll 开始GroupDao.update，时间：[" + DateUtil.formatDate(date11, DateUtil.FMT_YYYY_MM_DD_HHmmss) + "]");
        actYwGroupDao.update(group);

        SysTenant sysTenant=sysTenantService.get(TenantConfig.getCacheTenant());
        if (sysTenant!=null && (Sval.EmPn.NPROVINCE.getPrefix()).equals(sysTenant.getType())) {
            actYwStepService.saveStep(ActYwStep.StepEnmu.STEP2.getValue(), group.getId());
        }

//        Date date12 = new Date();
//        System.out.println("saveAll 结束GroupDao.update，时间：[" + DateUtil.formatDate(date12, DateUtil.FMT_YYYY_MM_DD_HHmmss) + "]总耗时->" + (date12.getTime() - date11.getTime()));
//        return new ActYwRstatus<List<ActYwGnode>>(true, "保存成功！", rgnodes);
        return new ApiTstatus<List<ActYwGnode>>(true, "保存成功！");
    }

    /**
     * 流程设计数据备份-快照恢复接口.
     * 注意事项：版本恢复要考虑恢复的范围（表单、角色、用户、状态）
     * @param groupId 指定流程
     * @param version 恢复版本
     */
    @Transactional(readOnly = false)
    public Boolean revertAllByGroup(String version, String groupId) {
        if(StringUtil.isNotEmpty(groupId) && StringUtil.isNotEmpty(version)){
            String newVersion = IdGen.uuid();
            List<ActYwGnode> oldGnodes = null;
            try {
                oldGnodes = findListByGroup(new ActYwGnode(groupId));
                savePlVersion(newVersion, oldGnodes);

                List<ActYwGnode> entitys = getALLVersionByGroup(version, groupId);
                if(entitys == null){
                    throw new NoDateException("没有版本数据异常");
                }
                deletePlwlByGroup(groupId);
                savePl(entitys);
                return true;
            } catch (Exception e) {
                List<ActYwGnode> entitys = getALLVersionByGroup(newVersion, groupId);
                deletePlwlByGroup(groupId);
                savePl(entitys);
                deleteVersionPlwlByGroup(newVersion, groupId);
                logger.error("流程恢复版本失败, groupId = " + groupId + " | newVersion="+newVersion);
            }
        }
        return false;
    }

    /**
     * 根据流程批量删除.
     * @param runner 执行器
     * @param groupId 流程ID
     * @throws Exception
     */
    @Transactional(readOnly = false)
    public void deletePlwlByGroup(ActYwRunner runner, String groupId) {
        try{
            runner.getEngine().gform().deletePlwlByGroup(groupId);
            runner.getEngine().grole().deletePlwlByGroup(groupId);
            runner.getEngine().gstatus().deletePlwlByGroup(groupId);
            runner.getEngine().guser().deletePlwlByGroup(groupId);
            runner.getEngine().gtime().deletePlwlByGroup(groupId);
            dao.deletePlwlByGroup(groupId);
        } catch (Exception e) {
            logger.error("流程删除失败, groupId = " + groupId);
        }
    }

    /**
     * 批量保存.
     * @param entitys 节点数据
     */
    @Transactional(readOnly = false)
    public int savePl(List<ActYwGnode> entitys) {
        return dao.savePl(entitys);
    }

    /**
     * 根据流程批量删除.
     * @param groupId 流程ID
     */
    @Transactional(readOnly = false)
    public void deletePlwlByGroup(String groupId) {
        dao.deletePlwlByGroup(groupId);
    }

    /**
     * 查询当前版本所有数据.
     * @param version 版本
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getALLVersionByGroup(String version, String groupId) {
        return dao.getALLVersionByGroup(version, groupId);
    }

    /**
     * 流程设计数据备份-批量快照保存.
     * @param entitys 节点数据
     */
    @Transactional(readOnly = false)
    public int savePlVersion(String version, List<ActYwGnode> entitys) {
        return dao.savePlVersion(version, entitys);
    }

    /**
     * 根据流程批量爆发版本删除.
     */
    @Transactional(readOnly = false)
    public void deleteVersionPlwlByGroup(String version, String groupId) {
        dao.deleteVersionPlwlByGroup(version, groupId);
    }

    /**
     * 查找符合生成菜单条件的节点.
     * @param entity
     * @return List
     */
    public List<ActYwGnode> findListByMenu(ActYwGnode entity) {
        return dao.findListBygMenu(entity);
    }
    public List<ActYwGnode> findListBygMenu(ActYwGnode entity) {
        return findAllListBygTime(dao.findListBygMenu(entity), entity, true);
    }

    /**
     * 查找当前流程parent节点的所有子节点.
     * group.id和parent.id必填
     * type可选（GnodeType）
     * @param gnode 节点
     * @return List
     */
    public List<ActYwGnode> findListByGparent(ActYwGnode gnode) {
        return dao.findListByGparent(gnode);
    }

    /**
     * 查找当前流程parent节点的所有子节点（关联表单、角色、用户、状态）.
     * group.id和parent.id必填
     * type可选（GnodeType）
     * @param gnode 节点
     * @return List
     */
    public List<ActYwGnode> findListBygGparent(ActYwGnode gnode) {
        return findAllListByg(dao.findListByGparent(gnode), gnode);
    }


    /**
     * 根据流程ID和子流程ID查询流程节点(所有)（关联表单、角色、用户、状态）.
     * @param groupId 流程ID
     * @param pid 子流程ID
     * @return List
     */
    public List<ActYwGnode> findListBygGprocess(String groupId, String pid) {
        if(StringUtil.isEmpty(groupId) || StringUtil.isEmpty(pid)){
            return null;
        }
        ActYwGnode pentity = new ActYwGnode();
        pentity.setGroup(new ActYwGroup(groupId));
        pentity.setParent(new ActYwGnode(pid));
        return findAllListByg(dao.findListBygGroup(pentity), pentity);
    }

    /**
     * 根据流程ID和子流程ID查询流程节点(TASK)（关联表单、角色、用户、状态）.
     * @param groupId 流程ID
     * @param pid 子流程ID
     * @return List
     */
    public List<ActYwGnode> findListBygYwGprocess(String groupId, String pid) {
        if(StringUtil.isEmpty(groupId) || StringUtil.isEmpty(pid)){
            return null;
        }
        ActYwGnode pentity = new ActYwGnode();
        pentity.setGroup(new ActYwGroup(groupId));
        pentity.setParent(new ActYwGnode(pid));
        pentity.setTypes(Arrays.asList(new String[]{GnodeType.GT_ROOT_TASK.getId(), GnodeType.GT_PROCESS_TASK.getId()}));
        Page<ActYwGnode> page = new Page<>();
        page.setOrderBy("level");
        page.setOrderByType("ASC");
        pentity.setPage(page);
        return findAllListByg(dao.findListBygGroup(pentity), pentity);
    }



    /**
     * 根据流程ID获取根开始节点.
     * @param groupId 流程ID
     * @return ActYwGnode
     */
    public ActYwGnode getStart(String groupId) {
        return dao.getStart(groupId);
    }
    public ActYwGnode getStartByg(String groupId) {
        return findAllListByg(dao.getStartByg(groupId), new ActYwGnode(new ActYwGroup(groupId)));
    }

    /**
     * 根据流程ID获取根结束节点.
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getEndByRoot(String groupId) {
        return dao.getEndByRoot(groupId);
    }

    /**
     * 仅获取节点名称
     * @param groupId
     * @return
     */
    public List<ActYwGnode> getEndNamePreFun(String groupId) {
        return dao.getEndNameByRoot(groupId);
    }

    public List<ActYwGnode> getEndBygRoot(String groupId) {
        return findAllListByg(dao.getEndBygRoot(groupId), new ActYwGnode(new ActYwGroup(groupId)));
    }

    /**
     * 根据流程ID获取子流程结束节点.
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getEndBySub(String groupId, List<String> nodeIds) {
        return dao.getEndBySub(groupId, nodeIds);
    }

    public List<ActYwGnode> getEndBySub(String groupId) {
        return dao.getEndBySub(groupId, Arrays.asList(new String[]{StenType.ST_END_EVENT_TERMINATE.getId()}));
    }


    /**
     * 根据流程ID和前置业务节点获取结束节点或子流程结束（事件结束）.
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getEnds(String groupId, List<String> preIds, List<String> nodeIds) {
        return dao.getEnds(groupId, preIds, nodeIds);
    }

    public List<ActYwGnode> getEnds(String groupId, List<String> preIds) {
        return getEnds(groupId, preIds, Arrays.asList(new String[]{StenType.ST_END_EVENT_TERMINATE.getId()}));
    }

    public List<ActYwGnode> getEnd(String groupId) {
        return getEnds(groupId, null);
    }

    public List<ActYwGnode> getEnd(String groupId, String preId) {
        return getEnds(groupId, Arrays.asList(new String[]{preId}), Arrays.asList(new String[]{StenType.ST_END_EVENT_TERMINATE.getId()}));
    }

    public List<ActYwGnode> getEndsByPre(String groupId, String preId) {
        return getEndsByPre(groupId, Arrays.asList(new String[]{preId}));
    }
    public List<ActYwGnode> getEndsByPre(String groupId, List<String> preIds) {
        return dao.getEndsByPre(groupId, preIds, Arrays.asList(new String[]{StenType.ST_END_EVENT_TERMINATE.getId()}));
    }

    public List<ActYwGnode> getEndsByPpre(String groupId, String preId) {
        return getEndsByPpre(groupId, Arrays.asList(new String[]{preId}));
    }
    public List<ActYwGnode> getEndsByPpre(String groupId, List<String> preIds) {
        return dao.getEndsByPpre(groupId, preIds, Arrays.asList(new String[]{StenType.ST_END_EVENT_TERMINATE.getId()}));
    }

    /**
     * 检查是否为结束节点.
     * @param groupId 流程ID
     * @param preId 结束节点前节点
     * @return Boolean
     */
    public Boolean checkEnd(String groupId, String preId) {
        return StringUtil.checkEmpty(getEnd(groupId, preId));
    }
    public Boolean checkEnds(String groupId, List<String> preIds) {
        return StringUtil.checkEmpty(getEnds(groupId, preIds));
    }

    /**根据当前获取下个节点列表*/
    public List<ActYwGnode> findListByPre(String preId){
        return dao.findListByPre(preId);
    }

    /**根据当前获取下个节点*/
    public List<ActYwGnode> getNextNode(ActYwGnode fnode){
        return dao.getNextNode(fnode);
    }

    /**根据当前获取下个节点-关联*/
    public List<ActYwGnode> getBygNextNode(ActYwGnode fnode){
        return findAllListByg(dao.getBygNextNode(fnode), fnode);
    }

    /**根据当前获取下下个节点*/
    public List<ActYwGnode> getNextNextNode(ActYwGnode fnode){
       return dao.getNextNextNode(fnode);
    }
    /**根据当前获取下下个节点-关联*/
    public List<ActYwGnode> getBygNextNextNode(ActYwGnode fnode){
        return findAllListByg(dao.getNextNextNode(fnode), fnode);
    }

    /**根据网关节点获取 网关下面的分支*/
    public List<List<ActYwGnode>> getGatewayList(ActYwGnode gatewayNode){
        List<List<ActYwGnode>> res = new ArrayList<List<ActYwGnode>>();
        List<ActYwGnode> list = this.getNextNode(gatewayNode);
        for (ActYwGnode temp:list) {
            List<ActYwGnode> list1 =new ArrayList<ActYwGnode>();
            list1.add(temp);
            list1.addAll(this.getNextNode(temp));
            res.add(list1);
        }
      return  res;
    }

    public  List<ActYwGnode> getChildFlowAllNode(ActYwGnode flowNode){
        return dao.getChildFlowAllNode(flowNode);
    };

    public List<ActYwGnode> getChildFlowsSecondNode(ActYwGnode node) {
        List<ActYwGnode> res = dao.getChildFlowAllNode(node);
//        System.out.println("====" + res.size());
        for (ActYwGnode  tt:res ) {
            if (ifStartNode(tt)){

                List<ActYwGnode>  t1=this.getNextNextNode(tt);
//                List<ActYwGnode> t1=this.getNextNode(tt);
//                List<ActYwGnode> t2=this.getNextNode(t1.get(0));
                return t1;
            }
        }
        return null;
    }

    private boolean ifStartNode(ActYwGnode node) {
        if (NodeEtype.NET_START.getId().equals(node.getNode().getType())
                && (node.getPreId() == null || "".equals(node.getPreId()))) {//放入开始节点
            return true;
        }
        return false;
    }

    /**
     * 查找当前流程parent节点的所有业务节点. group.id和parent.id必填 type可选（GnodeType）
     * @param gnode 节点
     * @return List
     */
    public List<ActYwGnode> findListByYwGparent(ActYwGnode gnode) {
        gnode.setTypes( Arrays.asList(new String[] { GnodeType.GT_ROOT_TASK.getId(), GnodeType.GT_PROCESS.getId(), GnodeType.GT_PROCESS_TASK.getId() }));
        return dao.findListByGparent(gnode);
    }

    public List<ActYwGnode> getDelegateNode(String actywId) {
        return dao.getDelegateNode(actywId);
    }
}