/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.io.Serializable;

/**
 * 流程申请实体拓展属性.
 * @author chenhao
 */
public class IAprop implements Serializable{
    private static final long serialVersionUID = 1L;
    private boolean istart;//是否启动成功
    private boolean iend;//是否结束
    private String iendgnid;//结束节点ID
    private String iendgname;//结束节点名称

    /**
     * 获取流程结束节点名称.
     * @return String
     */
    public String iendgname() {
        return iendgname;
    }

    /**
     * 设置流程结束节点名称.
     * @return String
     */
    public String iendgname(String iendgname) {
        this.iendgname = iendgname;
        return iendgname;
    }

    /**
     * 获取流程结束标志.
     * @return boolean
     */
    public boolean iend() {
        return iend;
    }

    /**
     * 设置流程结束标志.
     * @return boolean
     */
    public boolean iend(boolean iend) {
        this.iend = iend;
        return iend;
    }

    public boolean istart() {
        return istart;
    }

    public void istart(boolean istart) {
        this.istart = istart;
    }

    /**
     * 获取流程结束节点ID.
     * @return String
     */
    public String iendgnid() {
        return iendgnid;
    }

    /**
     * 设置流程结束节点ID.
     * @return String
     */
    public String iendgnid(String iendgnid) {
        this.iendgnid = iendgnid;
        return iendgnid;
    }
}