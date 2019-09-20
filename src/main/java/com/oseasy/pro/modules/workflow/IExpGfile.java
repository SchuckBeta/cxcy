/**
 * .
 */

package com.oseasy.pro.modules.workflow;

/**
 * Excel导出附件生成汇总文档类接口.
 * @author chenhao
 *
 */
public interface IExpGfile {
    /**
     * 生成文件路径.
     */
    String getPath();

    /**
     * 获取Excel导出模板类.
     * @return
     */
    Class<?> getClazz();
}
