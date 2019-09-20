/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.io.Serializable;

/**
 * 流程申请实体提交参数接口.
 * @author chenhao
 */
public class IAsup implements Serializable{
    private static final long serialVersionUID = 1L;
    private String igrade;//审核结果
    private Double iscore;//审核分值
    private String iremarks;

    /**
     * 获取流程审核结果.
     * @return String
     */
    public String igrade() {
        return igrade;
    }

    /**
     * 设置流程审核结果.
     * @return String
     */
    public String igrade(String igrade) {
        this.igrade = igrade;
        return igrade;
    }

    /**
     * 获取流程申请备注.
     * @return String
     */
    public String iremarks() {
        return iremarks;
    }

    /**
     * 设置流程审核结果备注.
     * @return String
     */
    public String iremarks(String iremarks) {
        this.iremarks = iremarks;
        return iremarks;
    }

    /**
     * 获取流程审核结果分值.
     * @return Double
     */
    public Double iscore() {
        return iscore;
    }

    /**
     * 设置流程审核结果分值.
     * @return Double
     */
    public Double iscore(Double iscore) {
        this.iscore = iscore;
        return iscore;
    }
}
