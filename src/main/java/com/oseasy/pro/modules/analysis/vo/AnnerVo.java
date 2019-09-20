package com.oseasy.pro.modules.analysis.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/12.
 */
public class AnnerVo {
    private String name;
    private String type;
    private List<Integer> data;
    private List<String>  categories;

    public AnnerVo(String name) {
        this.name = name;
        this.data = new ArrayList<Integer>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void addCategories(String categoryItem) {
        this.categories.add(categoryItem);
    }

    public void addData(int val) {
        this.data.add(val);
    }


}
