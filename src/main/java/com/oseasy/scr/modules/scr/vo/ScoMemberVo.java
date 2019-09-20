/**
 * .
 */

package com.oseasy.scr.modules.scr.vo;

import java.io.Serializable;

/**
 * 学分审核参数实体.
 * @author chenhao
 *
 */
public class ScoMemberVo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String uid;      // 申请人ID
    private Integer rate;       // 配比
    private Double score;       // 学值

    public ScoMemberVo() {
        super();
    }

    public ScoMemberVo(String uid, Integer rate, Double score) {
        super();
        this.uid = uid;
        this.rate = rate;
        this.score = score;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public Integer getRate() {
        return rate;
    }
    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }
}
