package com.oseasy.scr.modules.sco.vo;

/**
 * Created by zhangzheng on 2017/7/24.
 * 根据 type（学分类型)、item（学分项）、
 *      category（课程、项目、大赛、技能大类）、
 *      subdivision（课程、项目、大赛小类）、
 *      category2 (项目级别）
 *      result(项目等级)
 *      获得标准认定学分分值
 *      的查询实体
 */
public class ScoAffrimCriterionVo {

    private String type;		// 学分类型
    private String item;		// 学分项
    private String category;		// 课程、项目、大赛、技能大类
    private String subdivision;		// 课程、项目、大赛小类
    private String category2;   //标准分类（项目级别或者竞赛级别）
    private String result;   //等级（优秀、合格、不合格、延期等）
    private float score;     //分值（学分）

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

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
