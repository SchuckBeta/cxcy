/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

/**
 * 流程表单实体接口.
 * @author chenhao
 */
public interface IForm {
    public static final String IFORM_ID = "formId";
    public static final String ILFORM_ID = "lformId";//列表表单ID
    /**********************************************************************************
     * 获取流程表单类型.
     * FormStyleType
     * @return String
     */
    String styleType();

    /**
     * 获取文件模板路径.
     * @return String
     */
    String ipath();
}
