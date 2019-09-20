package com.oseasy.sys.common.utils;

import java.util.List;

public class Tree {

    private String id;
    private String text;
    private String parentId;
    private List<Tree> childList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Tree> getChildList() {
        return childList;
    }

    public void setChildList(List<Tree> childList) {
        this.childList = childList;
    }
}
