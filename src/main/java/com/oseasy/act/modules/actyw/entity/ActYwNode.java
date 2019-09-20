package com.oseasy.act.modules.actyw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 项目流程节点Entity.
 *
 * @author chenhao
 * @version 2017-05-23
 */
public class ActYwNode extends DataEntity<ActYwNode> {

    private static final long serialVersionUID = 1L;
    private String name; // 节点名称
    private String type; // 业务模块（0-流程节点，1-立项审核，2-中期检查，3-结项审核）
    private Boolean isForm; // 是否为表单节点:0、默认（否）；1、是
    private Boolean isVisible; // 是否可见:0、默认（否）；1、是
    private String iconUrl; // 图标地址

    private String uiJson; // UI结构
    private String uiOperate; // UI可执行的操作
    private String nodeType; // 流程节点类型
    private String nodeKey; // 流程节点标识
    private String nodeIcon; // 元素默认图标
    private String nodeRoles; // 元素允许执行的操作
    private String nodeXml; // 元素Xml结构

    public ActYwNode() {
        super();
    }

    public ActYwNode(String id) {
        super(id);
    }

    @Length(min = 1, max = 255, message = "节点名称长度必须介于 1 和 255 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 1, max = 255, message = "业务模块（1-立项审核，2-中期检查，3-结项审核）长度必须介于 1 和 255 之间")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsForm() {
        return isForm;
    }

    public void setIsForm(Boolean isForm) {
        this.isForm = isForm;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNodeIcon() {
        return nodeIcon;
    }

    public void setNodeIcon(String nodeIcon) {
        this.nodeIcon = nodeIcon;
    }

    public String getNodeRoles() {
        return nodeRoles;
    }

    public void setNodeRoles(String nodeRoles) {
        this.nodeRoles = nodeRoles;
    }

    public String getNodeXml() {
        return nodeXml;
    }

    public void setNodeXml(String nodeXml) {
        this.nodeXml = nodeXml;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getUiJson() {
        return uiJson;
    }

    public void setUiJson(String uiJson) {
        this.uiJson = uiJson;
    }

    public String getUiOperate() {
        return uiOperate;
    }

    public void setUiOperate(String uiOperate) {
        this.uiOperate = uiOperate;
    }

}