/**
 * .
 */

package com.oseasy.pro.modules.workflow;

/**
 * 工作流拓展实体必须实现的公共方法接口.
 * @author chenhao
 *
 */
public interface IWorkFetyExt {
    /**
     * 获取流程项目ID.
     * @return
     */
    public String getActYwId();

    /**
     * 获取流程项目模型ID.
     * @return
     */
    public String getModelId();
}
