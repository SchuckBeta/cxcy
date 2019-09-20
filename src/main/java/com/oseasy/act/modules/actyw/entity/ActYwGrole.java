package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.util.common.utils.IidEntity;

/**
 * 节点角色Entity.
 * @author chenh
 * @version 2018-01-15
 */
public class ActYwGrole extends DataEntity<ActYwGrole> implements IidEntity{

	private static final long serialVersionUID = 1L;
	private ActYwGroup group;		// 流程编号
	private ActYwGnode gnode;		// 流程节点编号
	private Role role;		// 角色编号
	private Boolean isDelegate;		//委派:0、默认（否）；1、是

	public ActYwGrole() {
		super();
	}

    public ActYwGrole(ActYwGnode gnode) {
        super();
        this.gnode = gnode;
    }

    public ActYwGrole(String group, String gnode) {
        super();
        this.group = new ActYwGroup(group);
        this.gnode = new ActYwGnode(gnode);
    }

    public ActYwGrole(ActYwGroup group, ActYwGnode gnode) {
        super();
        this.group = group;
        this.gnode = gnode;
    }

    public ActYwGrole(String groupId, String gnodeId, String roleId) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.role = new Role(roleId);
    }

    public ActYwGrole(String id, String groupId, String gnodeId, String roleId) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.role = new Role(roleId);
    }

    public ActYwGrole(String id, ActYwGroup group, String gnodeId, String roleId) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.role = new Role(roleId);
        this.isDelegate = false;
    }

    public ActYwGrole(String id, ActYwGroup group, String gnodeId, String roleId, Boolean isDelegate) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.role = new Role(roleId);
        this.isDelegate = isDelegate;
    }

    public ActYwGrole(String groupId, String gnodeId, Role role) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.role = role;
        this.isDelegate = false;
    }

    public ActYwGrole(String id, String groupId, String gnodeId, Role role) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.role = role;
    }

    public ActYwGrole(String id, ActYwGroup group, String gnodeId, Role role) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.role = role;
        this.isDelegate = false;
    }

    public ActYwGrole(ActYwGroup group, ActYwGnode gnode, Role role) {
        super();
        this.group = group;
        this.gnode = gnode;
        this.role = role;
        this.isDelegate = false;
    }

    public ActYwGroup getGroup() {
        return group;
    }

    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    public ActYwGnode getGnode() {
        return gnode;
    }

    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(Boolean isDelegate) {
        this.isDelegate = isDelegate;
    }

    /**
     * IDS转换为角色对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids 状态
     * @return List
     */
    public static List<ActYwGrole> converts(ActYwGroup group, String gnodeId, List<Role> pids) {
        List<ActYwGrole> statuss = Lists.newArrayList();
        for (Role pid : pids) {
            statuss.add(new ActYwGrole(IdGen.uuid(), group, gnodeId, pid));
        }
        return statuss;
    }
    public static List<ActYwGrole> convertDrs(ActYwGroup group, String gnodeId, List<String> pids) {
        List<ActYwGrole> statuss = Lists.newArrayList();
        for (String pid : pids) {
            statuss.add(new ActYwGrole(IdGen.uuid(), group, gnodeId, pid, true));
        }
        return statuss;
    }

    /**
     * IDS转换为角色对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGrole> convert(String groupId, String gnodeId, List<String> ids) {
        List<ActYwGrole> statuss = Lists.newArrayList();
        for (String id : ids) {
            statuss.add(new ActYwGrole(IdGen.uuid(), groupId, gnodeId, id));
        }
        return statuss;
    }
}