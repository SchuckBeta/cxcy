/**
 * .
 */

package com.oseasy.pie.modules.exp;

/**
 * .
 *
 * @author chenhao
 *
 */
public abstract class AbsExpHandler implements ExpHandler {
    private ExpPreHandler preHandler;
    private ExpBeforeHandler beforeHandler;

    public Boolean doPreHandle(ExpRule rule) {
        Boolean isres = false;
        try {
            if (null != preHandler) {
                isres = preHandler.handle(rule);
            }
        } catch (Exception e) {
            // 业务代码执行失败主动回滚
            // rollBack(rule);
            return false;
        }
        return isres;
    }

    public Boolean doBeforeHandle(ExpRule rule) {
        Boolean isres = false;
        // 业务代码执行成功主动调用下一个处理器处理
        if (null != beforeHandler) {
            isres = beforeHandler.handle(rule);
        }
        return isres;
    }

    public void setPreHandler(ExpPreHandler preHandler) {
        this.preHandler = preHandler;
    }

    public void setBeforeHandler(ExpBeforeHandler beforeHandler) {
        this.beforeHandler = beforeHandler;
    }
}
