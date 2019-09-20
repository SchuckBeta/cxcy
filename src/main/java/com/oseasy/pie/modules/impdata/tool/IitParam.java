/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import com.oseasy.pro.modules.promodel.tool.IitOper;

/**
 * 导入参数定义.
 * @author chenhao
 *
 */
public interface IitParam<T extends IitOper> {
    /**
     * 获取操作参数.
     */
    T itOper();
}
