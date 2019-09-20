package com.oseasy.pro.modules.promodel.service;

import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.dao.ActYwGnodeDao;
import com.oseasy.act.modules.actyw.dao.ActYwGroupDao;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.tool.apply.IAgnservice;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pro.modules.gcontest.enums.GContestStatusEnum;
import com.oseasy.pro.modules.gcontest.vo.GContestNodeVo;
import com.oseasy.pro.modules.project.enums.ProjectStatusEnum;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.pro.modules.promodel.tool.process.vo.GnodeView;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 流程节点Service.
 *
 * @author chenh
 * @version 2018-01-15
 */
@Service
@Transactional(readOnly = true)
public class ProActYwGnodeService extends CrudService<ActYwGnodeDao, ActYwGnode> implements IAgnservice {
    @Autowired
    ActYwGroupDao actYwGroupDao;
    @Autowired
    ActYwGnodeService actYwGnodeService;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private ActYwGassignService actYwGassignService;
    @Autowired
    private CoreService coreService;
    /****************************************************************************************************************
     * 新修改的接口.
     ***************************************************************************************************************/
    /**
     * 根据状态和流程ID 定位流程跟踪(旧的项目、大赛使用). 根据固定流程的状态获取gnode节点.
     *
     * @param idx
     *            状态
     * @param groupId
     *            流程
     * @param model
     * @return ActYwGnode
     */
    public ActYwGnode getGnodeByStatus(String idx, String groupId, Model model) {
        ActYwGnode gnode = null;
        ActYwGroup group = null;
        boolean isnodef = false;
        String gnodeId = null;
        if (StringUtil.isNotEmpty(groupId)) {
            group = actYwGroupDao.get(groupId);
            if ((groupId).equals(FlowYwId.FY_P.getGid())) {
                ProjectStatusEnum pstatus = ProjectStatusEnum.getByValue(idx);
                if ((pstatus != null)) {
                    isnodef = true;
                    gnodeId = ProjectNodeVo.getGNodeIdByNodeId(pstatus.getGnodeId());
                    model.addAttribute(ActYwGroup.JK_GNODE_ID, pstatus.getGnodeId());
                }
            } else if ((groupId).equals(FlowYwId.FY_G.getGid())) {
                GContestStatusEnum gstatus = GContestStatusEnum.getByValue(idx);
                if ((gstatus != null)) {
                    isnodef = true;
                    gnodeId = GContestNodeVo.getGNodeIdByNodeId(gstatus.getGnodeId());
                    model.addAttribute(ActYwGroup.JK_GNODE_ID, gstatus.getGnodeId());
                }
            }
        }

        if ((!isnodef) || StringUtil.isEmpty(gnodeId)) {
            logger.warn("状态类型未定义，或数据不存在！");
        } else {
            gnode = get(gnodeId);
        }
        model.addAttribute("group", group);
        model.addAttribute("groupId", groupId);
        return gnode;
    }

    /**
     * 验证流程合法性. 验证流程是否符合发布条件，过滤特定流程
     *
     * @param groupId
     *            流程标识
     * @return boolean
     */
    public boolean validateProcess(String groupId) {
        List<ActYwGnode> processGnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(new ActYwGroup(groupId)));
        Boolean hasSubProcess = true;
        Boolean hasSubChilds = true;

        /** 忽略列表 */
        if (validateIgnoProcess(groupId)) {
            return true;
        }

        if ((processGnodes == null) || (processGnodes.size() <= 0)) {
            hasSubProcess = false;
        } else {
            for (ActYwGnode process : processGnodes) {
                List<ActYwGnode> subs = Lists.newArrayList();
                for (ActYwGnode subgnode : processGnodes) {
                    if ((process.getId()).equals(subgnode.getParent().getId())) {
                        subs.add(subgnode);
                    }
                }

                if (subs.size() <= 0) {
                    hasSubChilds = false;
                }
            }
        }
        return (hasSubProcess && hasSubChilds);
    }

    /**
     * 验证流程是否符合发布条件，过滤特定流程.
     *
     * @param groupId
     *            流程标识
     * @return
     */
    public boolean validateIgnoProcess(String groupId) {
        String[] ignorFilter = new String[] { ProjectNodeVo.YW_FID, GContestNodeVo.YW_FID };
        Boolean isTrue = false;
        for (String id : ignorFilter) {
            if (((groupId).equals(id))) {
                isTrue = true;
            }
        }
        return isTrue;
    }

    /**
     * 处理节点选中状态.
     *
     * @param groupId
     *            流程ID
     * @param selectId
     *            选中节点ID
     * @param gnodeViews
     *            视图节点集合
     * @param curRunningGnode
     *            当前节点
     * @return ActYwRstatus
     */
    private ApiTstatus<List<GnodeView>> dealGnodeViews(String groupId, String selectId, List<GnodeView> gnodeViews,
            ActYwGnode curRunningGnode) {
        ApiTstatus<List<GnodeView>> rstatus = new ApiTstatus<List<GnodeView>>();
        if ((curRunningGnode == null) || StringUtil.isEmpty(curRunningGnode.getId())) {
            rstatus.setMsg("流程当前结点数据不存在[curId= " + selectId + "]！");
            rstatus.setDatas(gnodeViews);
            return rstatus;
        } else {
            /**
             * 查询流程历史获取当前执行的流程. 根据流程执行状态，获取节点ID(将参数传递的ID改为流程获取,参数去掉)
             */
            if (!(groupId).equals(curRunningGnode.getGroup().getId())) {
                rstatus.setStatus(false);
                rstatus.setMsg("groupId[" + groupId + "]与id存在[groupId=" + curRunningGnode.getGroup().getId() + "]冲突!");
                return rstatus;
            }

            /**
             * 流程节点更新用户信息(指派、委派、审核).
             */
            List<String> roleIds = Lists.newArrayList();
            for (GnodeView gview : gnodeViews) {
                if (StringUtil.checkNotEmpty(gview.getGroles())) {
                    for (ActYwGrole grole : gview.getGroles()) {
                        // 排除学生角色
                        Role role  = coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey());
                        if (role != null){
                            if ((role.getId()).equals(grole.getRole().getId())) {
                                continue;
                            }
                        }
                        roleIds.add(grole.getRole().getId());
                    }
                }
            }

            if (StringUtil.checkNotEmpty(roleIds)) {
                List<Role> roles = roleDao.findListByIds(roleIds);
                if (StringUtil.checkNotEmpty(roles)) {
                    for (GnodeView gview : gnodeViews) {
                        List<User> curUsers = Lists.newArrayList();
                        if (StringUtil.checkEmpty(gview.getGroles())) {
                            gview.setUsers(curUsers);
                            continue;
                        }

                        for (Role role : roles) {
                            Role role1 = coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey());
                            if (role1 != null){
                                if ((role1.getId()).equals(role.getId()) || (role.getUser() == null)) {
                                    continue;
                                }
                            }

                            if ((gview.getRoleIds()).contains(role.getId())) {
                                curUsers.add(role.getUser());
                            }
                        }
                        gview.setUsers(curUsers);
                    }
                }
            }
        }
        rstatus.setDatas(gnodeViews);
        return rstatus;
    }


    public ApiTstatus<List<GnodeView>> queryStatusTreeByGnode(String groupId, String gnodeId, String grade, String fteacher) {
      ApiTstatus<List<GnodeView>> rstatus = new ApiTstatus<List<GnodeView>>();
      List<GnodeView> gviews = Lists.newArrayList();

      ActYwGnode curGnode = null;
      if (StringUtil.isNotEmpty(grade) && (grade).equals(ActYwGnodeService.NO_PASS)) {
          List<ActYwGnode> ends = actYwGnodeService.getEndBygRoot(groupId);
          curGnode = ((StringUtil.checkNotEmpty(ends)) ? ends.get(0) : null);
      }else{
        /**
         * 若流程节点ID为空，当前节点定位开始节点.
         */
        if (StringUtil.isNotEmpty(gnodeId)) {
            curGnode = get(gnodeId);
        }else{
            curGnode = actYwGnodeService.getStart(groupId);
        }
      }

      ActYwGroup pactYwGroup = new ActYwGroup(groupId);
      List<ActYwGnode> gnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(pactYwGroup));
      if (gnodes == null) {
          rstatus.setMsg("流程节点数据不存在[groupId= "+groupId+"]！");
          rstatus.setDatas(gviews);
          return rstatus;
      }
      gviews = GnodeView.convertsGview(gnodes, curGnode, null, null);

      if (StringUtil.isEmpty(gnodeId)) {
          gviews = GnodeView.convertsGview(gnodes, null, null, null);
          rstatus.setMsg("流程当前结点数据不存在[proInsId= "+gnodeId+"]！");
          rstatus.setDatas(gviews);
          return rstatus;
      }

      return dealGnodeViews(groupId, gnodeId, gviews, curGnode);
    }

    /**
     * 固定流程定位流程审核状态.
     * @param groupId 流程ID
     * @param gnodeId 流程节点ID
     * @return ActYwRstatus
     */
    public ApiTstatus<List<GnodeView>> queryStatusTreeByGnode(String groupId, String gnodeId, String grade) {
        return queryStatusTreeByGnode(groupId, gnodeId, grade, null);
    }


    /**
     * 自定义流程定位流程审核状态.
     * @param groupId 流程ID
     * @param proInsId 流程实例ID
     * @param actTaskService
     * @return ActYwRstatus
     */
    public ApiTstatus<List<GnodeView>> queryStatusTree(String groupId, String proInsId, String grade, ActTaskService actTaskService) {
        ApiTstatus<List<GnodeView>> rstatus = new ApiTstatus<List<GnodeView>>();
      List<GnodeView> gviews = Lists.newArrayList();

      String fteacher = null;
      ActYwGnode curGnode = null;
      List<ActYwGassign> gassigns = null;

      /**
       * 获取委派数据, 判断项目是否已经指派.
       */
      ProModel pm = proModelService.checkByProInsId(proInsId);
      if(pm != null){
          fteacher = pm.getTeam().getFirstTeacher();
          ActYwGassign gassign = new ActYwGassign();
          gassign.setPromodelId(pm.getId());
          gassigns = actYwGassignService.findListByPro(gassign);
      }

      if (StringUtil.isNotEmpty(grade) && (grade).equals(ActYwGnodeService.NO_PASS)) {
          List<ActYwGnode> ends = actYwGnodeService.getEndByRoot(groupId);
          curGnode = ((StringUtil.checkNotEmpty(ends)) ? ends.get(0) : null);
      }else{
        /**
         * 若流程节点ID为空，当前节点定位开始节点.
         */
        if (StringUtil.isNotEmpty(proInsId)) {
            curGnode = proActTaskService.getNodeByProInsIdByGroupId(groupId, proInsId);
        }else{
            curGnode = actYwGnodeService.getStart(groupId);
        }
      }

      ActYwGroup pactYwGroup = new ActYwGroup(groupId);
      List<ActYwGnode> gnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(pactYwGroup));
      if (gnodes == null) {
          rstatus.setMsg("流程节点数据不存在[groupId= "+groupId+"]！");
          rstatus.setDatas(gviews);
          return rstatus;
      }
      //gviews = GnodeView.convertsGview(gnodes, curGnode);
      gviews = GnodeView.convertsGview(gnodes, curGnode, gassigns, fteacher);

      if (StringUtil.isEmpty(proInsId)) {
          gviews = GnodeView.convertsGview(gnodes, null, null, fteacher);
          rstatus.setMsg("流程当前结点数据不存在[proInsId= "+proInsId+"]！");
          rstatus.setDatas(gviews);
          return rstatus;
      }

      return dealGnodeViews(groupId, proInsId, gviews, curGnode);
    }


    /**
     * 查询已审核的节点(加上当前结点).
     * @param yearId 年份ID
     * @param ppid 项目ID
     * @param groupId 流程ID
     * @param promdelId 模型ID
     * @param proInsId 实例ID
     * @return List
     */
    public List<ActYwGnode> queryGnodeByAuditList(String yearId, String ppid, String groupId, String promdelId, String proInsId){
        List<ActYwGnode> gnodes = actYwGnodeService.queryGnodeByAuditList(yearId, ppid, groupId, promdelId);
        ActYwGnode curGnode = proActTaskService.getNodeByProInsIdByGroupId(groupId, proInsId);
        if(curGnode == null){
            return gnodes;
        }

        if((GnodeType.getIdByProcess()).contains(curGnode.getType())){
            curGnode = actYwGnodeService.getBygTime(curGnode.getParentId(), ppid, yearId);
        }else{
            curGnode = actYwGnodeService.getBygTime(curGnode.getId(), ppid, yearId);
        }

        if(curGnode == null){
            return gnodes;
        }

        Boolean notInList = true;
        for (ActYwGnode cg : gnodes) {
           if((cg.getId()).equals(curGnode.getId())){
               notInList = false;
           }
        }

        if(notInList){
            gnodes.add(curGnode);
        }
        return gnodes;
    }


    /**
     * 得到流程通过和正在进行的节点
     * @param groupId 流程ID
     * @param proInsId 流程实例ID
     * @return ActYwRstatus
     */
    public List<GnodeView> queryGnodeByPass(String groupId, String projectId,String proInsId,String endGnodeId, String fteacher) {
        List<GnodeView> gviews = Lists.newArrayList();
        ActYwGnode curGnode = null;
        /**
         * 若流程节点ID为空，当前节点定位开始节点.
         */

        //根据gnodeId得到最后一个结束任务节点
        if(StringUtil.isNotEmpty(endGnodeId)){
            curGnode = get(endGnodeId);
        }else {
            if (StringUtil.isNotEmpty(proInsId)) {
                curGnode = proActTaskService.getNodeByProInsIdByGroupId(groupId, proInsId);
            } else {
                curGnode = actYwGnodeService.getStart(groupId);
            }
        }
        ActYwGroup pactYwGroup = new ActYwGroup(groupId);
        ActYwGnode actYwGnode=new ActYwGnode(pactYwGroup);
        actYwGnode.setActYwGtime(new ActYwGtime());
        actYwGnode.getActYwGtime().setProjectId(projectId);
        List<ActYwGnode> gnodes = actYwGnodeService.findListTimeBygGroup(actYwGnode);
        gviews = GnodeView.convertsGview(gnodes, curGnode, null, fteacher);
        List<GnodeView> newGview = Lists.newArrayList();
        for(int i=0;i<gviews.size();i++){
            GnodeView gnodeView = gviews.get(i);
            //判断 子流程或节点 并且 状态为结果或者正在进行
            if((GnodeType.GT_PROCESS.getId().equals(gnodeView.getType())||GnodeType.GT_ROOT_TASK.getId().equals(gnodeView.getType())) && ((gnodeView.getRstatus()==GnodeView.GV_END)||(gnodeView.getRstatus()==GnodeView.GV_RUNNING))){
                newGview.add(gnodeView);
            }
        }
        return newGview;
    }

    /**
    * 自定义流程定位流程审核状态.
    * @param groupId 流程ID
    * @param proInsId 流程实例ID
    * @param actTaskService
    * @return ActYwRstatus
    */
    public JSONObject queryGnodeUser(String groupId, String proInsId, String grade, ActTaskService actTaskService) { ApiTstatus<List<GnodeView>> rstatus = new ApiTstatus<List<GnodeView>>();
        JSONObject js=new JSONObject();
        ActYwGnode curGnode = null;
        if (StringUtil.isNotEmpty(grade) && (grade).equals(ActYwGnodeService.NO_PASS)) {
            List<ActYwGnode> ends = actYwGnodeService.getEndByRoot(groupId);
            curGnode = ((StringUtil.checkNotEmpty(ends)) ? ends.get(0) : null);
        }else{
           /**
            * 若流程节点ID为空，当前节点定位开始节点.
            */
            if (StringUtil.isNotEmpty(proInsId)) {
                curGnode = proActTaskService.getNodeByProInsIdByGroupId(groupId, proInsId);
            }else{
                curGnode = actYwGnodeService.getStart(groupId);
            }
        }
        ActYwGroup pactYwGroup = new ActYwGroup(groupId);
        List<ActYwGnode> gnodes = actYwGnodeService.findListBygGroup(new ActYwGnode(pactYwGroup));
        if (gnodes == null) {
            js.put("msg","流程节点数据不存在[groupId= "+groupId+"]！");
            return js;
        }
        if (StringUtil.isEmpty(proInsId)) {
            js.put("ret",1);
            js.put("msg","流程当前结点数据不存在[proInsId= "+proInsId+"]！");
            return js;
        }
        if(curGnode!=null){
            js = proModelService.getJsByProInsId(curGnode,proInsId);
        }
        return js;
    }
}