package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;

/**
 * 节点状态中间表Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwGstatus extends DataEntity<ActYwGstatus> {

	private static final long serialVersionUID = 1L;
	private ActYwGroup group;		// 流程ID
	private ActYwGnode gnode;		// 节点id
	private ActYwStatus status;		// 状态id

	public ActYwGstatus() {
		super();
	}

	public ActYwGstatus(ActYwGroup group, ActYwGnode gnode) {
        super();
        this.group = group;
        this.gnode = gnode;
    }

    public ActYwGstatus(String id, String groupId, String gnodeId, String statusId) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.status = new ActYwStatus(statusId);
    }

    public ActYwGstatus(String groupId, String gnodeId, ActYwStatus status) {
        super();
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.status = status;
    }

    public ActYwGstatus(String id, String groupId, String gnodeId, ActYwStatus status) {
        super();
        this.id = id;
        this.group = new ActYwGroup(groupId);
        this.gnode = new ActYwGnode(gnodeId);
        this.status = status;
    }

    public ActYwGstatus(String id, ActYwGroup group, String gnodeId, ActYwStatus status) {
        super();
        this.id = id;
        this.group = group;
        this.gnode = new ActYwGnode(gnodeId);
        this.status = status;
    }

    public ActYwGstatus(String group, String gnode) {
        super();
        this.group = new ActYwGroup(group);
        this.gnode = new ActYwGnode(gnode);
    }

    public ActYwGstatus(String id){
		super(id);
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

    public ActYwStatus getStatus() {
        return status;
    }

    public void setStatus(ActYwStatus status) {
        this.status = status;
    }

    /**
     * IDS转换为节点状态对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGstatus> converts(ActYwGroup group, String gnodeId, List<ActYwStatus> pids) {
        List<ActYwGstatus> statuss = Lists.newArrayList();
        for (ActYwStatus pid : pids) {
            statuss.add(new ActYwGstatus(IdGen.uuid(), group, gnodeId, pid));
        }
        return statuss;
    }

    /**
     * IDS转换为节点状态对象.
     * @param groupId 流程ID
     * @param gnodeId 节点ID
     * @param ids ID串
     * @return List
     */
    public static List<ActYwGstatus> convert(String groupId, String gnodeId, List<String> ids) {
        List<ActYwGstatus> statuss = Lists.newArrayList();
        for (String id : ids) {
            statuss.add(new ActYwGstatus(IdGen.uuid(), groupId, gnodeId, id));
        }
        return statuss;
    }

    /**
     * 检查ID是否存在.
     * @param gstatuss 节点状态列表
     * @param id ID
     * @return Boolean
     */
    public static Boolean checkEqId(List<ActYwGstatus> gstatuss, ActYwStatus status) {
        if ((gstatuss == null) || (status == null)) {
            return false;
        }

        for (ActYwGstatus gstatus : gstatuss) {
            if((gstatus != null) && (gstatus.getStatus() != null) && (gstatus.getStatus().getId()).equals(status.getId())){
                return true;
            }
        }
        return false;
    }
}