/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;

/**
 * 流程实体接口.
 * @author chenhao
 *
 */
public interface IGroup {
    /**
     * 自定义流程对象KEY.
     */
    public final static String IGROUP_ID = "groupId";
    public final static String IFLOW_TYPE = "flowType";
    /**
     * 流程ID.
     * @return String
     */
    String id();

    /**
     * 流程标识字符.
     * @return
     */
    String keyss();

    /**
     * 流程类型.
     * @return
     */
    FlowType flowType();

    /**
     * 设置流程类型.
     * @return
     */
    FlowType flowType(FlowType flowType);

    /**
     * 流程主题.
     * @return
     */
    FormTheme theme();
}
