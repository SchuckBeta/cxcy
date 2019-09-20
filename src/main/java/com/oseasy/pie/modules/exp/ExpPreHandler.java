/**
 * .
 */

package com.oseasy.pie.modules.exp;

/**
 * .
 * @author chenhao
 *
 */
public interface ExpPreHandler {
    public Boolean handle(ExpRule rule);

    /**
     * 检查模板.
     * @param rule
     * @return
     */
    public Boolean check(ExpRule rule);
}
