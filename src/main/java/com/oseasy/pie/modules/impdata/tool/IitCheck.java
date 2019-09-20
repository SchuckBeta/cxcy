/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

/**
 * 导入参数校验.
 * @author chenhao
 *
 */
public interface IitCheck<T extends IitCheckParam> {
    /**
     * 获取校验匹配标识（列名）.
     */
    String key();

    /**
     * 检验列明是否匹配（列名）.
     */
    boolean validateKey(T t);

    /**
     * 执行校验方法.
     */
    T validate(T t, IitCheckEetyExt pe, IitCheckEetyExt pev);
}
