package com.oseasy.scr.modules.scr.entity;

import java.io.Serializable;

/**
 * Created by PW on 2019/1/23.
 */
public class ScoRapplySum implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private double rDetailVal;
    private double sumVal;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getrDetailVal() {
        return rDetailVal;
    }

    public void setrDetailVal(double rDetailVal) {
        this.rDetailVal = rDetailVal;
    }

    public double getSumVal() {
        return sumVal;
    }

    public void setSumVal(double sumVal) {
        this.sumVal = sumVal;
    }
}
