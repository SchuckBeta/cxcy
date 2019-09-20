package com.oseasy.pro.modules.project.vo;

/**
 * Created by zhangzheng on 2017/8/4.
 */
public class ProjectStandardDetailVo {
    private String checkPoint;		// 检查要点
    private String checkElement;		// 审核元素
    private String viewScore;		// 参考分值
    private String sort;		// 排序
    private String isEescoreNodes;		// 评分节点

    public String getIsEescoreNodes() {
        return isEescoreNodes;
    }

    public void setIsEescoreNodes(String isEescoreNodes) {
        this.isEescoreNodes = isEescoreNodes;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public String getCheckElement() {
        return checkElement;
    }

    public void setCheckElement(String checkElement) {
        this.checkElement = checkElement;
    }

    public String getViewScore() {
        return viewScore;
    }

    public void setViewScore(String viewScore) {
        this.viewScore = viewScore;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
