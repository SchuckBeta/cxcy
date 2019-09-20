package com.oseasy.scr.modules.sco.vo;

/**
 * Created by zhangzheng on 2017/7/21.
 * 根据 type（学分类型)、item（学分项）、
 *      category（课程、项目、大赛、技能大类）、
 *      subdivision（课程、项目、大赛小类）、
 *      number(人数)获得学分配比
 *      的查询实体
 */
public class ScoRatioVo {

    private String type;		// 学分类型
    private String item;		// 学分项
    private String category;		// 课程、项目、大赛、技能大类
    private String subdivision;		// 课程、项目、大赛小类
    private int number;		// 组人数
    private String ratio;		// 分配比例

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
