package com.oseasy.auy.modules.cms.entity;

import java.beans.Transient;

import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.cms.modules.cms.entity.Category;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 创建项目Entity.
 * @author zhangyao
 * @version 2017-06-15
 */
public class AuyProProject extends ProProject{
    private static final long serialVersionUID = 1L;
    private Category category;        //前台栏目
    public AuyProProject() {
        super();
    }

    public AuyProProject(String id) {
        super(id);
    }

    public String getCategoryRid() {
        if (StringUtil.isEmpty(this.categoryRid) && (this.category != null)) {
            this.categoryRid = this.category.getId();
        }
        return categoryRid;
    }

    public void setCategoryRid(String categoryRid) {
        this.categoryRid = categoryRid;
    }

    @Transient
    public Category getCategory() {
        return category;
    }

    @Transient
    public void setCategory(Category category) {
        if ((this.category != null) && StringUtil.isNotEmpty(this.category.getId())) {
            this.categoryRid = this.category.getId();
        }
        this.category = category;
    }
}