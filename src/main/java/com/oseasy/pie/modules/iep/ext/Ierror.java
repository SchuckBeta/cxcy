/**
 * .
 */

package com.oseasy.pie.modules.iep.ext;

/**
 * .
 * @author chenhao
 *
 */
public interface Ierror<T> {
    /**
     * 获取错误信息类.
     */
    T getErr();

    /**
     * 获取错误信息验证类.
     */
    T getVerr();
}
