package com.oseasy.pro.modules.workflow.impl;

import java.util.List;

import com.oseasy.act.common.persistence.ActEntity;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.pro.modules.workflow.IWorkFety;
import com.oseasy.pro.modules.workflow.IWorkFetyExt;

/**
 * 工作流拓展实体父类.
 * @author chenhao
 *
 * @param <T>
 * @param <PM>
 */
public class SupWorkFety<T, PM extends IWorkFety> extends ActEntity<T> implements IWorkFetyExt {
    private static final long serialVersionUID = 1L;
    protected String modelId;

    protected PM proModel;

    protected String fileUrl;
    protected String fileId;

    protected List<String> ids;
    protected String actYwId;        //业务id

    private AttachMentEntity attachMentEntity; //附件

    public SupWorkFety() {
    }

    public SupWorkFety(String id) {
        super(id);
    }

    public SupWorkFety(List<String> ids) {
        super();
        this.ids = ids;
    }

    public SupWorkFety(List<String> ids, String actYwId) {
        super();
        this.ids = ids;
        this.actYwId = actYwId;
    }

    @Override
    public String getActYwId() {
        return this.actYwId;
    }

    @Override
    public String getModelId() {
        return this.modelId;
    }

    public PM getProModel() {
        return proModel;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public AttachMentEntity getAttachMentEntity() {
        return attachMentEntity;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setProModel(PM proModel) {
        this.proModel = proModel;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public void setActYwId(String actYwId) {
        this.actYwId = actYwId;
    }

    public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
        this.attachMentEntity = attachMentEntity;
    }
}
