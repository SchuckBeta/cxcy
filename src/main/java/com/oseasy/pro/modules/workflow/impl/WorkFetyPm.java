package com.oseasy.pro.modules.workflow.impl;

import java.util.List;

import com.oseasy.pro.modules.promodel.entity.ProModel;

public class WorkFetyPm<T> extends SupWorkFety<T, ProModel> {
    private static final long serialVersionUID = 1L;
    public WorkFetyPm() {
    }

    public WorkFetyPm(String id) {
        super(id);
    }

    public WorkFetyPm(List<String> ids) {
        super();
        this.ids = ids;
    }

    public WorkFetyPm(List<String> ids, String actYwId) {
        super();
        this.ids = ids;
        this.actYwId = actYwId;
    }
}
