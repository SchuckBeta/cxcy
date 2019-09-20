package com.oseasy.pw.modules.pw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oseasy.com.pcore.common.persistence.TreeEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotNull;

/**
 * 资源类别Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwCategory extends TreeEntity<PwCategory> {

    private static final long serialVersionUID = 1L;
    private PwCategory parent;        // 父级编号
    private String parentIds;        // 所有父级编号
    private String name;        // 名称

    private PwFassetsnoRule pwFassetsnoRule;  //资产编号规则

    @Transient
    private String parentPrefix;

    @Transient
    private String level;

    public PwCategory() {
        super();
    }

    public PwCategory(String id) {
        super(id);
    }

    @JsonBackReference
    @NotNull(message = "父级编号不能为空")
    public PwCategory getParent() {
        return parent;
    }

    public void setParent(PwCategory parent) {
        this.parent = parent;
    }

    @Length(min = 1, max = 2000, message = "所有父级编号长度必须介于 1 和 2000 之间")
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Length(min = 1, max = 100, message = "名称长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }

    public PwFassetsnoRule getPwFassetsnoRule() {
        return pwFassetsnoRule;
    }

    public void setPwFassetsnoRule(PwFassetsnoRule pwFassetsnoRule) {
        this.pwFassetsnoRule = pwFassetsnoRule;
    }

    public String getParentPrefix() {
        return (parent != null && parent.getPwFassetsnoRule() != null) ? parent.getPwFassetsnoRule().getPrefix() : null;
    }

    public void setParentPrefix(String parentPrefix) {
        this.parentPrefix = parentPrefix;
    }

    public String getLevel() {
        if(StringUtils.isBlank(parentIds)){
            return "";
        }
        return (parentIds.split(",").length - 1) + "";
    }

}