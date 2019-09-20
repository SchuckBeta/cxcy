/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.cmd.vo
 * @Description [[_ActYwPgroot_]]文件
 * @date 2017年6月18日 下午2:25:33
 *
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.vo;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwPtpl;
import com.oseasy.act.modules.actyw.tool.process.impl.ActYwEngineImpl;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 业务流程参数-业务根节点.
 * 1、根据自定义流程判断是否需要生成基本节点（开始和结束）
 * 2、
 * @author chenhao
 * @date 2017年6月18日 下午2:25:33
 *
 */
public class ActYwPgnode extends ApiTstatus<ActYwGnode> implements ActYwPtpl<ActYwEngineImpl>{
    private ActYwPcheckIgn checkIgn; //是否忽略ID检查
    private ActYwGnode gnode; // 当前新增的业务节点(node 为 StenType.ST_TASK_USER)
    private ActYwGnode startGnode; // 流程前一节点
    private ActYwGnode endGnode; // 流程后一节点
    private ActYwGnode preFunGnode; // 流程前一业务节点

  public ActYwPgnode() {
    super();
    this.checkIgn = new ActYwPcheckIgn();
  }

  public ActYwPgnode(ActYwGnode gnode) {
    super();
    this.gnode = gnode;
    this.checkIgn = new ActYwPcheckIgn();
  }

  public ActYwPgnode(ActYwGnode gnode, ActYwGnode startGnode, ActYwGnode endGnode) {
    super();
    this.gnode = gnode;
    this.startGnode = startGnode;
    this.endGnode = endGnode;
    this.checkIgn = new ActYwPcheckIgn();
  }

  public ActYwPgnode(ActYwGnode gnode, ActYwGnode startGnode, ActYwGnode endGnode, ActYwGnode preFunGnode) {
    super();
    this.gnode = gnode;
    this.startGnode = startGnode;
    this.endGnode = endGnode;
    this.preFunGnode = preFunGnode;
    this.checkIgn = new ActYwPcheckIgn();
  }


  public ActYwGnode getGnode() {
    return gnode;
  }

  public void setGnode(ActYwGnode gnode) {
    this.gnode = gnode;
  }

  public ActYwGnode getStartGnode() {
    return startGnode;
  }

  public void setStartGnode(ActYwGnode startGnode) {
    this.startGnode = startGnode;
  }

  public ActYwGnode getEndGnode() {
    return endGnode;
  }

  public void setEndGnode(ActYwGnode endGnode) {
    this.endGnode = endGnode;
  }

  public ActYwGnode getPreFunGnode() {
    return preFunGnode;
  }

  public void setPreFunGnode(ActYwGnode preFunGnode) {
    this.preFunGnode = preFunGnode;
  }

  @Override
  public ActYwPgnode check(ActYwEngineImpl engine, String key, ActYwPgnode tpl, Logger logger) {
      tpl.setStatus(true);
      return tpl;
  }

  public ActYwPgnode check2(ActYwEngineImpl engine, String key, ActYwPgnode tpl, Logger logger) {
      if (tpl == null) {
          tpl = new ActYwPgnode();
          tpl.setMsg("参数不能为空！");
          logger.warn(key + " 命令[tpl]不能为空！");
          tpl.setStatus(false);
          return tpl;
      }

      if (tpl.getGnode() == null) {
          tpl.setMsg("存在空节点！");
          logger.warn(key + " 命令[gnode]不能为空！");
          tpl.setStatus(false);
          return tpl;
      }

      ActYwGnode curGnode = tpl.getGnode();

      /**
       * 验证流程参数.
       */
      if ((GnodeType.getIdByGroupId()).contains(curGnode.getType())) {
          if ((curGnode.getGroup() == null) || StringUtil.isEmpty(curGnode.getGroup().getId())) {
              tpl.setMsg(curGnode.getName() + "，不能为空！");
              logger.warn(key + " 命令[group.id]-["+curGnode.getId()+"]不能为空！");
              tpl.setStatus(false);
              return tpl;
          }
      }

      /**
       * 验证节点参数.
       */
      if ((curGnode.getNode() == null) || StringUtil.isEmpty(curGnode.getNode().getId())) {
          tpl.setMsg(curGnode.getName() + ".节点名，不能为空！");
          logger.warn(key + " 命令[gnode.node.id]-["+curGnode.getId()+"]不能为空！");
          tpl.setStatus(false);
          return tpl;
      }

      /**
       * 验证子流程参数.
       */
      if ((curGnode.getParent() == null) || StringUtil.isEmpty(curGnode.getParent().getId())) {
          tpl.setMsg(curGnode.getName() + ".父节点名，不能为空！");
          logger.warn(key + " 命令[gnode.parent.id]-["+curGnode.getId()+"]不能为空！");
          tpl.setStatus(false);
          return tpl;
      }

      /**
       * 验证节点类型参数.
       */
      if (StringUtil.isEmpty(curGnode.getType())) {
          tpl.setMsg(curGnode.getName() + ".类型，不能为空！");
          logger.warn(key + " 命令[gnode.type]-["+curGnode.getId()+"]不能为空！");
          tpl.setStatus(false);
          return tpl;
      }

      /**
       * 验证节点名称参数.
       */
      if ((GnodeType.getIdByName()).contains(curGnode.getType())) {
          if (StringUtil.isEmpty(curGnode.getName())) {
              tpl.setMsg("名称，不能为空！");
              logger.warn(key + " 命令[gnode.name]-["+curGnode.getId()+"]不能为空！");
              tpl.setStatus(false);
              return tpl;
          }
      }else{
          //curGnode.setName(null);
      }

      /**
       * 验证节点执行任务类型参数.
       */
      if ((GnodeType.getIdByTask()).contains(curGnode.getType())) {
          if (StringUtil.isEmpty(curGnode.getTaskType())) {
              curGnode.setTaskType(GnodeTaskType.GTT_NONE.getKey());
          }
      }else{
          curGnode.setTaskType(null);
      }

      /**
       * 验证节点 前置节点参数.
       */
      if ((GnodeType.getIdByPre()).contains(curGnode.getType())) {
          if (StringUtil.isEmpty(curGnode.getPreId())) {
              tpl.setMsg(curGnode.getName() + ".前节点，不能为空！");
              logger.warn(key + " 命令[gnode.preId]-["+curGnode.getId()+"]不能为空！");
              tpl.setStatus(false);
              return tpl;
          }else{
              //tpl.getCheckIgn().setIgnCheckPreId(ActYwRunner.containsId(curGnode.getPreGnode().getId()));
          }
//          if (StringUtil.checkEmpty(curGnode.getPreGnode())) {
//              tpl.setMsg(key + " 命令[gnode.preGnode]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }else{
//              //tpl.getCheckIgn().setIgnCheckPreId(ActYwRunner.containsId(curGnode.getPreGnode().getId()));
//          }
      }else{
          curGnode.setPreId(null);
//          curGnode.setPreGnode(null);
      }

      /**
       * 验证节点 前置节点参数.
       */
//      if ((GnodeType.getIdByPreFun()).contains(curGnode.getType())) {
//          if (StringUtil.checkNotEmpty(curGnode.getPreFunGnode())) {
//              tpl.setMsg(key + " 命令[gnode.preFunGnode]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }else{
//              //tpl.getCheckIgn().setIgnCheckPreFunId(ActYwRunner.containsId(curGnode.getPreFunGnode().getId()));
//          }
//      }else{
//          curGnode.setPreFunId(null);
//          curGnode.setPreFunGnode(null);
//      }

      /**
       * 验证节点 删除状态.
       */
      if ((GnodeType.getIdByDelFlag()).contains(curGnode.getType())) {
          curGnode.setDelFlag(Const.YES);
      }else{
          curGnode.setDelFlag(Const.NO);
      }

      /**
       * 验证节点 表单参数.
       */
      if ((GnodeType.getIdByForms()).contains(curGnode.getType())) {
          curGnode.setIsForm(true);
      }else{
          curGnode.setIsForm(false);
          curGnode.setGforms(null);
      }
      if (curGnode.getIsForm()) {
//          if ((curGnode.getForm() == null) || StringUtil.isEmpty(curGnode.getForm().getId())) {
//              tpl.setMsg(key + " 命令[gnode.form.id]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }
      }

      /**
       * 验证节点 角色参数.
       */
      if ((GnodeType.getIdByRoles()).contains(curGnode.getType())) {
//          curGnode.setGroles(engine.grole().findList(new ActYwGrole(curGnode.getGroup().getId(), curGnode.getId())));
//          if (StringUtil.checkEmpty(curGnode.getGroles())) {
//              tpl.setMsg(key + " 命令[gnode.groles]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }
      }else{
          curGnode.setGroles(null);
      }

      /**
       * 验证节点 用户参数.
       */
      if ((GnodeType.getIdByUsers()).contains(curGnode.getType())) {
//          curGnode.setGusers(engine.guser().findList(new ActYwGuser(curGnode.getGroup().getId(), curGnode.getId())));
//          if (StringUtil.checkEmpty(curGnode.getGusers())) {
//              tpl.setMsg(key + " 命令[gnode.gusers]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }
      }else{
          curGnode.setGusers(null);
      }

      /**
       * 验证节点 状态参数.
       */
      if ((GnodeType.getIdByGstatuss()).contains(curGnode.getType())) {
//          curGnode.setGstatuss(engine.gstatus().findList(new ActYwGstatus(curGnode.getGroup().getId(), curGnode.getId())));
//          if (StringUtil.checkEmpty(curGnode.getGstatuss())) {
//              tpl.setMsg(key + " 命令[gnode.gstatuss]-["+curGnode.getId()+"]不能为空！");
//              logger.warn(tpl.getMsg());
//              tpl.setStatus(false);
//              return tpl;
//          }
      }else{
          curGnode.setGstatuss(null);
      }

      /**
       * 验证节点显示参数.
       */
      if ((GnodeType.getIdByShow()).contains(curGnode.getType())) {
          curGnode.setIsShow(true);
      }else{
          curGnode.setIsShow(false);
      }

      /**
       * 验证图标参数.
       */
      if ((GnodeType.getIdByIconUrl()).contains(curGnode.getType())) {
          if (StringUtil.isEmpty(curGnode.getIconUrl())) {
              tpl.setMsg(curGnode.getName() + ".图标，不能为空！");
              logger.warn(key + " 命令[gnode.iconUrl]-["+curGnode.getId()+"]不能为空！");
              tpl.setStatus(false);
              return tpl;
          }
      }else{
          curGnode.setIconUrl(null);
      }
      return tpl;
  }

    public ActYwPcheckIgn getCheckIgn() {
        return checkIgn;
    }

    public void setCheckIgn(ActYwPcheckIgn checkIgn) {
        this.checkIgn = checkIgn;
    }
}