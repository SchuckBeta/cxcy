package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 节点用户Entity.
 * @author chenh
 * @version 2018-01-15
 */
public class ActYwGuser extends DataEntity<ActYwGuser> {

	private static final long serialVersionUID = 1L;
    private ActYwGroup group;       // 流程编号
	private ActYwGnode gnode;		// 流程节点编号
	private User user;		// 用户编号

	public ActYwGuser() {
		super();
	}

	public ActYwGuser(ActYwGnode gnode, User user) {
        super();
        this.gnode = gnode;
        this.user = user;
    }

    public ActYwGuser(ActYwGroup group, ActYwGnode gnode) {
        super();
        this.group = group;
        this.gnode = gnode;
    }

    public ActYwGuser(String groupId, String gnodeId, String userId) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.user = new User(userId);
    }

    public ActYwGuser(String id, String groupId, String gnodeId, String userId) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.user = new User(userId);
    }

    public ActYwGuser(String id, ActYwGroup group, String gnodeId, String userId) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.user = new User(userId);
    }

    public ActYwGuser(String groupId, String gnodeId, User user) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.user = user;
    }
    public ActYwGuser(String id, String groupId, String gnodeId, User user) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.user = user;
    }

    public ActYwGuser(String id, ActYwGroup group, String gnodeId, User user) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.user = user;
    }

    public ActYwGuser(String id, ActYwGroup group, ActYwGnode gnode, User user) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = gnode;
        this.user = user;
    }

    public ActYwGuser(ActYwGroup group, ActYwGnode gnode, User user) {
        super();
        this.group = group;
        this.gnode = gnode;
        this.user = user;
    }

    public ActYwGuser(String id){
		super(id);
	}

    public ActYwGuser(String group, String gnode) {
        super();
        this.group = new ActYwGroup(group);
        this.gnode = new ActYwGnode(gnode);
    }

    public ActYwGnode getGnode() {
        return gnode;
    }

    public void setGnode(ActYwGnode gnode) {
        this.gnode = gnode;
    }

    public ActYwGroup getGroup() {
        return group;
    }

    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    @NotNull(message="用户编号不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    /**
     * IDS转换为用户对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param pids 用户
     * @return List
     */
    public static List<ActYwGuser> converts(ActYwGroup group, String gnodeId, List<User> pids) {
        List<ActYwGuser> users = Lists.newArrayList();
        for (User pid : pids) {
            users.add(new ActYwGuser(IdGen.uuid(), group, gnodeId, pid));
        }
        return users;
    }

    /**
     * IDS转换为用户对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGuser> convert(String groupId, String gnodeId, List<String> ids) {
        List<ActYwGuser> users = Lists.newArrayList();
        for (String id : ids) {
            users.add(new ActYwGuser(IdGen.uuid(), groupId, gnodeId, id));
        }
        return users;
    }

}