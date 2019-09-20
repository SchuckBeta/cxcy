package com.oseasy.pro.modules.analysis.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/12.
 */
public class BarVo {
    private String name;
    private List<Integer> value;
    private List<String>  categories;

    public BarVo(String name) {
        this.name = name;
        this.value = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getValue() {
        return value;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
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

    public void addValue(int val) {
        this.value.add(val);
    }


}
