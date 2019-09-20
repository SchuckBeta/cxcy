/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

/**
 * 流程节点状态接口.
 * @author chenhao
 */
public interface IAstatus {
    /**
     * 节点状态标识.
     * @return String
     */
    public String getIkey();

    /**
     * 节点状态说明.
     * @return String
     */
    public String getIremark();

    /**
     * 节点正则类型.
     * @return String
     */
    public String getIregType();

    /**
     * 节点状态值.
     * @return
     */
    public String getIstatus();

    /**
     * 别名.
     * @return
     */
    public String getIalias();

    /**
     * 审核说明.
     * @return
     */
    public String getIstate();
}
