package com.oseasy.act.modules.actyw.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RtOutgoing;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.TreeEntity;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程节点Entity.
 *
 * @author chenh
 * @version 2018-01-15
 */
public class ActYwGnode extends TreeEntity<ActYwGnode> implements IidEntity, IGnode{
    private static final long serialVersionUID = 1L;
    public static final Integer L_ALL = 1;//所有节点
    public static final Integer L_YW = 2;//所有业务节点(level == (1/2) type = (19/70))
    public static final Integer L_PROCESS = 3;//所有子流程节点(level == 2 type = 19)
    @JsonIgnore
    private ActYwGnode parent;        // 子流程节点
    private String parentIds;        // 所有父级编号
    private ActYwGroup group;        // 流程组编号
    private String name;        // 名称
    private String rid;        // 模板节点ID
    private String type;        // 流程节点类型 GnodeType;
    private ActYwNode node;        // 流程节点编号-转换成json
    private String preId;        // 前一个流程节点编号(多个值)
    private Boolean isShow;        // 显示:1、默认（显示）；0、隐藏
    private Boolean isGval;        // 全局变量:0、默认（否）；1、是
    private Boolean isAssign;        // 指派:0、默认（否）；1、是
    private Boolean isForm;     // 是否为表单节点:0、默认（否）；1、是
    private Boolean isDelegate;     //委派:0、默认（否）；1、是
    private String taskType;     // 执行任务类型：
    private String iconUrl;        // 图标地址；
    private String outgoing; //Json字符串
    private Integer level; //层级

    @Transient
    private List<ActYwGform> gforms;        // 表单标识
    @Transient
    private List<ActYwGform> gdforms;        // 委派表单标识
    @Transient
    private List<ActYwGrole> groles; //审核角色
    @Transient
    private List<ActYwGrole> gdroles; //委派角色
    @Transient
    private List<ActYwGuser> gusers; // 审核用户
    @Transient
    private List<ActYwGstatus> gstatuss; // 前置节点为网关的时候，才不为，对应act_yw_status的id
    @Transient
    private ActYwGtime actYwGtime;
    @Transient
    private List<ActYwGnode> subs; //子流程节点列表(parent相同)
    @Transient
    private  List<List<ActYwGnode>> gateways; //网关的后节点列表(parent相同)
    @Transient
    private List<ActYwGnode> nexts; //下一个节点列表(preGnode相同)
    @Transient
    private  Boolean isvalid =false;   //标记该节点是否已经使用
    @Transient
    private boolean suspended; //是否挂起

    @Transient
    private List<String> types;  //状态，查询多种状态

    public ActYwGnode() {
        super();
    }

    public ActYwGnode(String id) {
        super(id);
    }

    public ActYwGnode(ActYwGroup group) {
        super();
        this.group = group;
    }

    public ActYwGnode(String id, String groupId) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
    }

    public ActYwGnode(String groupId, String id, String name) {
        super();
        this.group = new ActYwGroup(groupId);
        this.id = id;
        this.name = name;
    }

    public ActYwGnode(ActYwGroup group, String proId) {
        super();
        this.group = group;
        this.actYwGtime = new ActYwGtime();
        this.actYwGtime.setProjectId(proId);
    }

    public ActYwGnode(ActYwGroup group, String proId, String yearId) {
        super();
        this.group = group;
        this.actYwGtime = new ActYwGtime(proId, yearId);
        this.actYwGtime.setProjectId(proId);
    }

    public ActYwGnode(ActYwGtime actYwGtime) {
        super();
        this.actYwGtime = actYwGtime;
    }


    public ActYwGnode(ActYwGroup group, ActYwGtime actYwGtime) {
        super();
        this.group = group;
        this.actYwGtime = actYwGtime;
    }

    public ActYwGnode(String groupId, List<String> types) {
        super();
        this.types = types;
        this.group = new ActYwGroup(groupId);
    }

    public ActYwGnode(ActYwGnode gnode) {
        super();
        this.id = gnode.getId();
        this.rid = gnode.getRid();
        this.parent = gnode.getParent();
        this.parentIds = gnode.getParentIds();
        this.group = gnode.getGroup();
        this.name = gnode.getName();
        this.type = gnode.getType();
        this.node = gnode.getNode();
        this.preId = gnode.getPreId();
        this.isShow = gnode.getIsShow();
        this.isForm = gnode.getIsForm();
        this.taskType = gnode.getTaskType();
        this.iconUrl = gnode.getIconUrl();
        this.outgoing = gnode.getOutgoing();
        this.level = gnode.getLevel();
    }

    public ActYwGnode(ActYwGnode gnode, Boolean isAll) {
        super();
        this.id = gnode.getId();
        this.rid = gnode.getRid();
        this.parent = gnode.getParent();
        this.parentIds = gnode.getParentIds();
        this.group = gnode.getGroup();
        this.name = gnode.getName();
        this.isAssign = gnode.getIsAssign();
        this.isDelegate = gnode.getIsDelegate();
        this.type = gnode.getType();
        this.node = gnode.getNode();
        this.preId = gnode.getPreId();
        this.isShow = gnode.getIsShow();
        this.isForm = gnode.getIsForm();
        this.taskType = gnode.getTaskType();
        this.iconUrl = gnode.getIconUrl();
        this.outgoing = gnode.getOutgoing();
        this.level = gnode.getLevel();
        if(isAll){
           this.gforms = gnode.getGforms();
           this.gdforms = gnode.getGdforms();
           this.groles = gnode.getGroles();
           this.gdroles = gnode.getGdroles();
           this.gusers = gnode.getGusers();
           this.gstatuss = gnode.getGstatuss();
           this.actYwGtime = gnode.getActYwGtime();
           this.subs = gnode.getSubs();
           this.gateways = gnode.getGateways();
           this.nexts = gnode.getNexts();
           this.suspended = gnode.isSuspended();
           this.types = gnode.getTypes();
           this.isvalid = gnode.getIsvalid();
        }
    }

    public Boolean getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(Boolean isDelegate) {
        this.isDelegate = isDelegate;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public ActYwGnode(ActYwGroup group, ActYwGnode parent) {
        super();
        this.group = group;
        this.parent = parent;
    }

    public Boolean getIsAssign() {
        if(this.isAssign == null){
            this.isAssign = false;
        }
        return isAssign;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setIsAssign(Boolean isAssign) {
        this.isAssign = isAssign;
    }

    public Boolean getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(Boolean isvalid) {
        this.isvalid = isvalid;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    public List<ActYwGform> getGdforms() {
        return gdforms;
    }

    public void setGdforms(List<ActYwGform> gdforms) {
        this.gdforms = gdforms;
    }

    @JsonBackReference
    @NotNull(message = "子流程节点不能为空")
    public ActYwGnode getParent() {
        return parent;
    }

    public void setParent(ActYwGnode parent) {
        this.parent = parent;
    }

    public String getParentIds() {
        if(StringUtil.isEmpty(this.parentIds)){
            if((this.parent != null) && StringUtil.isNotEmpty(this.parent.getId()) && StringUtil.isNotEmpty(this.parent.getParentIds())){
                this.setParentIds(this.parent.getParentIds() + this.parent.getId() + ",");
            }
        }
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public ActYwGroup getGroup() {
        return group;
    }

    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    public List<ActYwGnode> getSubs() {
        return subs;
    }

    public void setSubs(List<ActYwGnode> subs) {
        this.subs = subs;
    }

    public List<List<ActYwGnode>> getGateways() {
        return gateways;
    }

    public void setGateways(List<List<ActYwGnode>> gateways) {
        this.gateways = gateways;
    }

    public String getType() {
        if (StringUtil.isEmpty(this.type)) {
            if ((this.node != null) && StringUtil.isNotEmpty(this.node.getType())) {
                GnodeType gtype = GnodeType.getByGnode(this);
                if (gtype != null) {
                    this.type = gtype.getId();
                }
            }
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ActYwNode getNode() {
        return node;
    }

    public void setNode(ActYwNode node) {
        this.node = node;
    }

    public List<ActYwGstatus> getGstatuss() {
        return gstatuss;
    }

    public void setGstatuss(List<ActYwGstatus> gstatuss) {
        this.gstatuss = gstatuss;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Boolean getIsForm() {
        return isForm;
    }

    public void setIsForm(Boolean isForm) {
        this.isForm = isForm;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<ActYwGrole> getGroles() {
        return groles;
    }

    public void setGroles(List<ActYwGrole> groles) {
        this.groles = groles;
    }

    public List<ActYwGrole> getGdroles() {
        return gdroles;
    }

    public void setGdroles(List<ActYwGrole> gdroles) {
        this.gdroles = gdroles;
    }

    public List<ActYwGuser> getGusers() {
        return gusers;
    }

    public void setGusers(List<ActYwGuser> gusers) {
        this.gusers = gusers;
    }

    public List<ActYwGform> getGforms() {
        return gforms;
    }

    public void setGforms(List<ActYwGform> gforms) {
        this.gforms = gforms;
    }

    public List<ActYwGnode> getNexts() {
        return nexts;
    }

    public void setNexts(List<ActYwGnode> nexts) {
        this.nexts = nexts;
    }

    public ActYwGtime getActYwGtime() {
        return actYwGtime;
    }

    public void setActYwGtime(ActYwGtime actYwGtime) {
        this.actYwGtime = actYwGtime;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Boolean getIsGval() {
        return isGval;
    }

    public void setIsGval(Boolean isGval) {
        this.isGval = isGval;
    }

    /**************************************************
     * 节点内置方法.
     **************************************************
     * 根据节点名称.
     * @return String
     */
    public String getName() {
        if (StringUtil.isEmpty(this.name) && ((this.node != null) && StringUtil.isNotEmpty(this.node.getName()))) {
            return this.node.getName();
        }
        return name;
    }

    /**
     * 设置节点名称.
     *
     * @param name 名称
     * @return String
     */
    public void setName(String name) {
        if (StringUtil.isEmpty(this.name) && ((this.node != null) && StringUtil.isNotEmpty(this.node.getName()))) {
            this.name = this.node.getName();
        } else {
            this.name = name;
        }
    }

    /**
     * 获取节点角色.
     *
     * @return List
     */
    @Transient
    public List<Role> getRoles() {
        List<Role> roles = Lists.newArrayList();
        if (this.groles != null) {
            for (int i=0;i<groles.size();i++) {
                if ((groles.get(i).getRole() != null)) {
                    roles.add(groles.get(i).getRole());
                }
            }
        }
        Collections.sort(roles, new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return o1.compareTo(o2);
            }
        });
        return roles;
    }

    /**
     * 获取节点委派角色.
     *
     * @return List
     */
    @Transient
    public List<Role> getDroles() {
        List<Role> roles = Lists.newArrayList();
        if (this.gdroles != null) {
            for (int i=0;i<gdroles.size();i++) {
                if ((gdroles.get(i).getRole() != null)) {
                    roles.add(gdroles.get(i).getRole());
                }
            }
        }
        Collections.sort(roles, new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return o1.compareTo(o2);
            }
        });
        return roles;
    }

    /**
     * 获取节点用户.
     *
     * @return List
     */
    @Transient
    public List<User> getUsers() {
        List<User> users = Lists.newArrayList();
        if (this.gusers != null) {
            for (ActYwGuser guser : gusers) {
                if ((guser.getUser() != null)) {
                    users.add(guser.getUser());
                }
            }
        }
        return users;
    }

    /**
     * 根据角色ID获取节点角色.
     *
     * @param roleId 角色ID
     * @return Role
     */
    @Transient
    public ActYwGrole getRoleByRid(String roleId) {
        return getRoleByRid(this.groles, roleId);
    }
    /**
     * 根据角色ID获取节点委派角色.
     *
     * @param roleId 角色ID
     * @return Role
     */
    @Transient
    public ActYwGrole getDroleByRid(String roleId) {
        return getRoleByRid(this.gdroles, roleId);
    }

    /**
     * 获取节点角色字符串，逗号分隔.
     * @return String
     */
    @Transient
    public String getRoleIds() {
        return getRoleIds(this.groles);
    }
    /**
     * 获取节点委派角色字符串，逗号分隔.
     * @return String
     */
    @Transient
    public String getDroleIds() {
        return getRoleIds(this.gdroles);
    }

    /**
     * 检查角色ID是否存在于节点.
     *
     * @param roleId 角色ID
     * @return Role
     */
    public Boolean checkRoleByRid(String roleId) {
        if (getRoleByRid(roleId) != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取父节点ID(子流程).
     *
     * @return String
     */
    public String getParentId() {
        return parent != null && StringUtil.isNotEmpty(parent.getId()) ? parent.getId()
                : CoreIds.NCE_SYS_TREE_PROOT.getId();
    }

    /**
     * 是否存在监听.
     * @return Boolean
     */
    public Boolean hasListeners() {
        return this.isGval;
    }

    /**************************************************
     * 静态方法.
     **************************************************
     * 根据角色ID获取角色.
     * @param proles 角色列表
     * @param roleId 角色ID
     * @return Role
     */
    public static ActYwGrole getRoleByRid(List<ActYwGrole> proles, String roleId) {
        if ((proles != null) && (proles.size() > 0)) {
            for (ActYwGrole grole : proles) {
                if ((grole.getRole() != null) && (roleId).equals(grole.getRole().getId())) {
                    return grole;
                }
            }
        }
        return null;
    }

    /**
     * 获取节点角色字符串，逗号分隔.
     *
     * @param proles 角色列表
     * @return String
     */
    public static String getRoleIds(List<ActYwGrole> proles) {
        if ((proles != null) && (proles.size() > 0)) {
            StringBuffer sb = new StringBuffer();
            boolean isFirst = true;
            for (ActYwGrole grole : proles) {
                Role prole = grole.getRole();
                if ((prole == null) || StringUtil.isEmpty(prole.getId())) {
                    continue;
                }
                if (isFirst) {
                    sb.append(prole.getId());
                    isFirst = false;
                } else {
                    sb.append(StringUtil.DOTH);
                    sb.append(prole.getId());
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 节点排序.
     *
     * @return String
     */
    @JsonIgnore
    public static void sortList(List<ActYwGnode> list, List<ActYwGnode> sourcelist, String parentId,
                                boolean cascade) {
        if(sourcelist != null){
            for (int i = 0; i < sourcelist.size(); i++) {
                ActYwGnode e = sourcelist.get(i);
                if (e.getParent() != null && e.getParent().getId() != null && e.getParent().getId().equals(parentId)) {
                    list.add(e);
                    if (cascade) {
                        // 判断是否还有子节点, 有则继续获取子节点
                        for (int j = 0; j < sourcelist.size(); j++) {
                            ActYwGnode child = sourcelist.get(j);
                            if (child.getParent() != null && child.getParent().getId() != null && child.getParent().getId().equals(e.getId())) {
                                sortList(list, sourcelist, e.getId(), true);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 节点倒序.
     */
    @JsonIgnore
    public static void revertList(List<ActYwGnode> list, List<ActYwGnode> sourcelist) {
        if(sourcelist != null){
            for (int i = (sourcelist.size() - 1); i >= 0; i--) {
                list.add(sourcelist.get(i));
            }
        }
    }

    /**
     * 流程根节点.
     *
     * @return String
     */
    @JsonIgnore
    public static String getRootId() {
        return CoreIds.NCE_SYS_TREE_ROOT.getId();
    }

    /**
     * 流程根节点的父节点.
     * @return String
     */
    @JsonIgnore
    public static String getRootPid() {
        return CoreIds.NCE_SYS_TREE_PROOT.getId();
    }

//    @Override
//    public String toString() {
//        会报出空指针异常
//        String preId = this.getPreId() != null ? this.getPreId() : "";
//        return "ActYwGnode{" +
//                "id=" + id +
//                ", parent=" + parent.getId() +
//                ", pre_id='" + preId +
//                ", name='" + name + '\'' +
//                ", node.type='" + this.getNode().getType() + '\'' +
//                ", node.name='" + this.getNode().getName() + '\'' +
//                '}';
//    }


    /**
     * IDS转换为节点对象.
     * @param groupId 流程ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGnode> convert(String groupId, List<String> ids) {
        List<ActYwGnode> gnodes = Lists.newArrayList();
        for (String id : ids) {
            gnodes.add(new ActYwGnode(id, groupId));
        }
        return gnodes;
    }

    /**
     * IDS转换为节点对象.
     * @param groupId 流程ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGnode> converts(String groupId, List<RtOutgoing> ids) {
        List<ActYwGnode> gnodes = Lists.newArrayList();
        for (RtOutgoing pid : ids) {
            gnodes.add(new ActYwGnode(pid.getResourceId(), groupId));
        }
        return gnodes;
    }

    /**
     * Nexts转换为RtOut
     * going对象.
     * @param nexts 后一个节点列表
     * @return List
     */
    public static List<RtOutgoing> convertsOutgoing(String preFix, List<ActYwGnode> nexts) {
        List<RtOutgoing> ogs = Lists.newArrayList();
        for (ActYwGnode next : nexts) {
            ogs.add(new RtOutgoing(preFix + next.getId()));
        }
        return ogs;
    }

    /**
     * Nexts转换为target对象.
     * @param nexts 后一个节点列表
     * @return String
     */
    public static String convertsTarget(String preFix, List<ActYwGnode> nexts) {
        StringBuffer target = new StringBuffer();
        if (StringUtil.checkNotEmpty(nexts)) {
            target.append(preFix);
            target.append(StringUtil.listIdToStr(nexts));
        }
        return target.toString();
    }

    public String getGformNames() {
        List<ActYwGform> gforms = getGforms();
        if (StringUtil.checkNotEmpty(gforms)) {
            List<String> list = gforms.stream().map(e -> {
                if(e.getForm() != null){
                    return e.getForm().getName();
                }
                return StringUtil.EMPTY;
            }).collect(Collectors.toList());
            return StringUtil.join(list, StringUtil.DOTH);
        }
        return "";
    }

    public String getGdformNames() {
        List<ActYwGform> gdforms = getGforms();
            if (StringUtil.checkNotEmpty(gdforms)) {
            List<String> list = gdforms.stream().map(e -> {
                if(e.getForm() != null){
                    return e.getForm().getName();
                }
                return StringUtil.EMPTY;
            }).collect(Collectors.toList());
            return StringUtil.join(list, StringUtil.DOTH);
        }
        return "";
    }

    public String getGroleNames() {
        List<ActYwGrole> groles = getGroles();
        if (StringUtil.checkNotEmpty(groles)) {
            List<String> list = groles.stream().map(e -> {
                if(e.getRole() != null){
                    return e.getRole().getName();
                }
                return StringUtil.EMPTY;
            }).collect(Collectors.toList());
            return StringUtil.join(list, StringUtil.DOTH);
        }
        return "";
    }

    public String getGdroleNames() {
        List<ActYwGrole> gdroles = getGdroles();
        if (StringUtil.checkNotEmpty(gdroles)) {
            List<String> list = gdroles.stream().map(e -> {
                if(e.getRole() != null){
                    return e.getRole().getName();
                }
                return StringUtil.EMPTY;
            }).collect(Collectors.toList());
            return StringUtil.join(list, StringUtil.DOTH);
        }
        return "";
    }

    public String getGstatusNames() {
        List<ActYwGstatus> gstatuss = getGstatuss();
        if (StringUtil.checkNotEmpty(gstatuss)) {
            List<String> list = gstatuss.stream().map(e -> {
                if(e.getStatus() != null){
                    return e.getStatus().getName();
                }
                return StringUtil.EMPTY;
            }).collect(Collectors.toList());
            return StringUtil.join(list, StringUtil.DOTH);
        }
        return "";
    }

    /**
     * 根据 ActYwGform获取指定类型的 表单.
     * @param gforms 表单列表
     * @param ftype 表单类型
     * @return ActYwGform
     */
    public ActYwGform filterForm(List<ActYwGform> gforms, FormType ftype) {
        if(StringUtil.checkEmpty(gforms) || (ftype == null)){
            return null;
        }

        for (ActYwGform gform : gforms) {
            if ((gform.getForm() != null) && (ftype.getKey()).equals(gform.getForm().getType())) {
                return gform;
            }
        }
        return null;
    }

    /**
     * 根据 ActYwGform获取指定类型的 表单列表（同一个类型有多个的时候只取第一个匹配的）.
     * @param gforms 表单列表
     * @param ftypes 表单列表
     * @param style 表单样式类型
     * @param client 表单客户端类型
     * @return ActYwGform
     */
    public ActYwGform filterForm(List<ActYwGform> gforms, List<FormType> ftypes, FormStyleType style, FormClientType client) {
        if(StringUtil.checkEmpty(gforms) || StringUtil.checkEmpty(ftypes)){
            return null;
        }

        for (ActYwGform gform : gforms) {
            ActYwForm form = gform.getForm();
            if (form == null) {
                continue;
            }

            if ((style != null) && !(style.getKey()).equals(form.getStyleType())) {
                continue;
            }

            if ((client != null) && !(client.getKey()).equals(form.getClientType())) {
                continue;
            }

            for (FormType ftype : ftypes) {
                if ((ftype.getKey()).equals(gform.getForm().getType())) {
                    return gform;
                }
            }
        }
        return null;
    }

    public ActYwGform filterFormByApply(List<ActYwGform> gforms, List<FormType> ftypes, FormStyleType style) {
        return filterForm(gforms, ftypes, style, FormClientType.FST_FRONT);
    }

    public ActYwGform filterFormByFrontApply(List<ActYwGform> gforms, List<FormType> ftypes, FormStyleType style) {
        return filterForm(gforms, ftypes, style, FormClientType.FST_FRONT);
    }

    /**
     * 根据 ActYwGform获取指定类型的 表单列表.
     * @param gforms 表单列表
     * @param ftypes 表单列表
     * @param styles 表单样式类型
     * @param clients 表单客户端类型
     * @return ActYwGform
     */
    public List<ActYwGform> filterForms(List<ActYwGform> gforms, List<FormType> ftypes, List<FormStyleType> styles, List<FormClientType> clients) {
        if(StringUtil.checkEmpty(gforms) || StringUtil.checkEmpty(ftypes)){
            return null;
        }

        List<ActYwGform> gformss = Lists.newArrayList();
        for (ActYwGform gform : gforms) {
            ActYwForm form = gform.getForm();
            if (form == null) {
                continue;
            }

            if (StringUtil.checkNotEmpty(styles) && !FormStyleType.checkHas(styles, form.getStyleType())) {
                continue;
            }

            if (StringUtil.checkNotEmpty(clients) && !FormClientType.checkHas(clients, form.getClientType())) {
                continue;
            }

            for (FormType ftype : ftypes) {
                if (!(ftype.getKey()).equals(gform.getForm().getType())) {
                    continue;
                }
                gformss.add(gform);
            }
        }
        return gformss;
    }


    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String id(String id) {
        this.id = id;
        return this.id;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Transient
    @Override
    public List<ActYwGform> igforms() {
        return this.gforms;
    }

    @JsonIgnore
    @Transient
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<ActYwGform> igforms(List igforms) {
        this.gforms = igforms;
        return this.gforms;
    }

    public static List<ActYwGtime> convertToGtimes(List<ActYwGnode> gnodes) {
        List<ActYwGtime> gtimes = Lists.newArrayList();
        if(StringUtil.checkEmpty(gnodes)){
            return gtimes;
        }

        for (ActYwGnode gnode : gnodes) {
            ActYwGtime gtime = new ActYwGtime();
            gtime.setGnode(gnode);
            gtime.setGrounpId(gnode.getGroup().getId());
            gtimes.add(gtime);
        }
        return gtimes;
    }
}