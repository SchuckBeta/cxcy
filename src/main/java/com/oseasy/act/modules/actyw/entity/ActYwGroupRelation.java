package com.oseasy.act.modules.actyw.entity;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.IdGen;

import java.util.List;

/**
 * 节点状态中间表Entity.
 * @author zy
 * @version 2018-01-15
 */
public class ActYwGroupRelation extends DataEntity<ActYwGroupRelation> {

	private static final long serialVersionUID = 1L;
	private String provGroupId;		// 省模板流程id
	private String modelGroupId;		// 模板流程id

	public ActYwGroupRelation() {
		super();
	}

    public String getProvGroupId() {
        return provGroupId;
    }

    public void setProvGroupId(String provGroupId) {
        this.provGroupId = provGroupId;
    }

    public String getModelGroupId() {
        return modelGroupId;
    }

    public void setModelGroupId(String modelGroupId) {
        this.modelGroupId = modelGroupId;
    }
}