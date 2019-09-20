/**
 * .
 */

package com.oseasy.pie.modules.exp;

/**
 * .
 * @author chenhao
 *
 */
public interface ExpBeforeHandler {
    public Boolean handle(ExpRule rule);
    /**
     * 处理消息和日志记录.
     * @param rule
     */
    public Boolean dealMsg(ExpRule rule);
}
