/**
 * 节点参数对象.
 */

package com.oseasy.act.modules.actyw.tool.process.cmd;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.oseasy.act.modules.actyw.entity.ActYwClazz;
import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGrole;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.entity.ActYwGuser;
import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RtOutgoing;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 节点参数.
 * @author chenhao
 *
 */
public class Gpnode implements Serializable{
    private static final long serialVersionUID = 1L;
    protected static final Logger logger = Logger.getLogger(Gpnode.class);

    private String id;
    private String name;
    private String type;
    private Integer isAssign;        // 签收:0、默认（否）；1、是
    private Integer isDelegate;        // 是否委派:0、默认（否）；1、是
    private Integer level; //排序层级
    private Boolean isShow;
    private Integer isGval;
    private String taskType;     // 执行任务类型：
    private String iconUrl;        // 图标地址；
    private String remarks;        //备注；

    private String nodeId;
    private String parentId;
    private String preFunId;
    private List<RtOutgoing> preIds;
    private List<Role> roleIds;
    private List<String> droleIds;//委派角色
    private List<User> userIds;
    private List<ActYwForm> formIds;//所有表单
    private List<ActYwForm> formListIds;//列表表单
    private List<ActYwForm> formNlistIds;//非列表表单
    private List<String> dformIds;//委派表单
    private List<ActYwStatus> statusIds;
    private List<ActYwClazz> clazzIds;

    private List<RtOutgoing> outgoing;
    private List<String> childShapes;

    public Integer getIsGval() {
        return isGval;
    }

    public void setIsGval(Integer gval) {
        isGval = gval;
    }

    public static ActYwGnode gen(ActYwRunner runner, Gpnode entity, ActYwGroup group) {
      ActYwGnode cur = new ActYwGnode();
      cur.setIsNewRecord(true);
      cur.setId(entity.getId());
      cur.setName(entity.getName());
      cur.setType(entity.getType());
        if((entity.getIsAssign() != null) && (entity.getIsAssign() == 1)){
            cur.setIsAssign(true);
        }else{
            cur.setIsAssign(false);
        }

        if((entity.getIsGval() != null) && (entity.getIsGval() == 1)){
            cur.setIsGval(true);
        }else{
            cur.setIsGval(false);
        }

      if((entity.getIsDelegate() != null) && (entity.getIsDelegate() == 1)){
          cur.setIsDelegate(true);
      }else{
          cur.setIsDelegate(false);
      }
      cur.setLevel(entity.getLevel());
      cur.setIsShow(entity.getIsShow());
      cur.setTaskType(entity.getTaskType());
      cur.setIconUrl(entity.getIconUrl());
      cur.setRemarks(entity.getRemarks());

      cur.setGroup(group);
      cur.setNode(new ActYwNode(entity.getNodeId()));
      cur.setParent(new ActYwGnode(entity.getParentId()));
      if(StringUtil.checkNotEmpty(entity.getOutgoing())){
          cur.setOutgoing(JsonMapper.toJsonString(entity.getOutgoing()));
      }

      if(StringUtil.checkNotEmpty(entity.getPreIds())){
          cur.setPreId(RtOutgoing.listIdToStr(entity.getPreIds()));
      }

      if(StringUtil.checkNotEmpty(entity.getStatusIds())){
          cur.setGstatuss(ActYwGstatus.converts(group, cur.getId(), entity.getStatusIds()));
      }

      if ((GnodeType.getIdByListForms()).contains(cur.getType())) {
          if(StringUtil.checkEmpty(entity.getFormListIds())){
              logger.warn("Gpnode.formListIds中必须含有列表表单,id = ["+cur.getId()+"]");
          }
      }

      if ((GnodeType.getIdByNlistForms()).contains(cur.getType())) {
          if(StringUtil.checkEmpty(entity.getFormNlistIds())){
              logger.warn("Gpnode.formNlistIds 中必须含有非列表表单,id = ["+cur.getId()+"]");
          }
      }

      if(StringUtil.checkNotEmpty(entity.getFormIds())){
          cur.setGforms(ActYwGform.converts(group, cur.getId(), entity.getFormIds()));
      }

//      if(StringUtil.checkNotEmpty(entity.getDformIds())){
//          cur.setGdforms(ActYwGform.converts(group, cur.getId(), entity.getDformIds()));
//      }
      if(StringUtil.checkNotEmpty(entity.getDformIds())){
          cur.setGdforms(ActYwGform.convertDfs(group, cur.getId(), entity.getDformIds()));
      }

      if(StringUtil.checkNotEmpty(entity.getRoleIds())){
          cur.setGroles(ActYwGrole.converts(group, cur.getId(), entity.getRoleIds()));
      }

//      if(StringUtil.checkNotEmpty(entity.getDroleIds())){
//          cur.setGdroles(ActYwGrole.converts(group, cur.getId(), entity.getDroleIds()));
//      }
      if(StringUtil.checkNotEmpty(entity.getDroleIds())){
          cur.setGdroles(ActYwGrole.convertDrs(group, cur.getId(), entity.getDroleIds()));
      }

      if(StringUtil.checkNotEmpty(entity.getUserIds())){
          cur.setGusers(ActYwGuser.converts(group, cur.getId(), entity.getUserIds()));
      }

      return cur;
    }

    public List<String> getDroleIds() {
        return droleIds;
    }

    public void setDroleIds(List<String> droleIds) {
        this.droleIds = droleIds;
    }

    public List<String> getDformIds() {
        return dformIds;
    }

    public void setDformIds(List<String> dformIds) {
        this.dformIds = dformIds;
    }

    public Integer getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(Integer isDelegate) {
        this.isDelegate = isDelegate;
    }

    public Integer getIsAssign() {
        return isAssign;
    }

    public void setIsAssign(Integer isAssign) {
        this.isAssign = isAssign;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPreFunId() {
        return preFunId;
    }

    public void setPreFunId(String preFunId) {
        this.preFunId = preFunId;
    }

    public List<ActYwClazz> getClazzIds() {
        return clazzIds;
    }

    public void setClazzIds(List<ActYwClazz> clazzIds) {
        this.clazzIds = clazzIds;
    }

    public List<RtOutgoing> getPreIds() {
        return preIds;
    }

    public void setPreIds(List<RtOutgoing> preIds) {
        this.preIds = preIds;
    }

    public List<Role> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Role> roleIds) {
        this.roleIds = roleIds;
    }

    public List<User> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<User> userIds) {
        this.userIds = userIds;
    }

    public List<ActYwForm> getFormIds() {
        return formIds;
    }

    public void setFormIds(List<ActYwForm> formIds) {
        this.formIds = formIds;
    }

    public List<ActYwStatus> getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(List<ActYwStatus> statusIds) {
        this.statusIds = statusIds;
    }

    public List<RtOutgoing> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<RtOutgoing> outgoing) {
        this.outgoing = outgoing;
    }

    public List<String> getChildShapes() {
        return childShapes;
    }

    public List<ActYwForm> getFormListIds() {
        return formListIds;
    }

    public void setFormListIds(List<ActYwForm> formListIds) {
        this.formListIds = formListIds;
    }

    public List<ActYwForm> getFormNlistIds() {
        return formNlistIds;
    }

    public void setFormNlistIds(List<ActYwForm> formNlistIds) {
        this.formNlistIds = formNlistIds;
    }

    public void setChildShapes(List<String> childShapes) {
        this.childShapes = childShapes;
    }
}
