/**
 * .
 */

package com.oseasy.pro.modules.workflow;

/**
 * IWork 导出Excel结果集（导出Excel的Vo类可能与查询数据的Vo类不一致）.
 * @author chenhao
 *
 */
public interface IWorkRes {
    public String getOfficeName();//获取当前结果集中的机构（导出Excel使用，根据机构名分组附件）
}
