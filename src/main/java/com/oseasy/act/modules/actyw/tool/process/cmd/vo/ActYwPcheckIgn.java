/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.vo;

/**
 * 忽略检查ID,原因：当执行事务处理时，查询无法查到PreGnode, PreFunGnode的情况.
 * @author chenhao
 *
 */
public class ActYwPcheckIgn {
    private Boolean ignCheckPreId; //是否忽略ID检查(处理新增是检查Pre)
    private Boolean ignCheckPreFunId; //是否忽略ID检查(处理新增是检查PreFun)

    public ActYwPcheckIgn() {
        super();
        this.ignCheckPreId = false;
        this.ignCheckPreFunId = false;
    }

    public ActYwPcheckIgn(Boolean ignCheckPreId, Boolean ignCheckPreFunId) {
        super();
        this.ignCheckPreId = ignCheckPreId;
        this.ignCheckPreFunId = ignCheckPreFunId;
    }

    public Boolean getIgnCheckPreId() {
        return ignCheckPreId;
    }
    public void setIgnCheckPreId(Boolean ignCheckPreId) {
        this.ignCheckPreId = ignCheckPreId;
    }
    public Boolean getIgnCheckPreFunId() {
        return ignCheckPreFunId;
    }
    public void setIgnCheckPreFunId(Boolean ignCheckPreFunId) {
        this.ignCheckPreFunId = ignCheckPreFunId;
    }
}
