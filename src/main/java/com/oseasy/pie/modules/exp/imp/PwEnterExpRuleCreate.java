/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pie.modules.exp.AbsExpRcreate;
import com.oseasy.pie.modules.exp.ExpRule;

/**
 * .
 * @author chenhao
 */
public class PwEnterExpRuleCreate extends AbsExpRcreate{
    public PwEnterExpRuleCreate() {
        super();
        expHandler = new PwEnterExpHandler();
        expHandler.setPreHandler(new PwEnterExpPreHandler());
        expHandler.setBeforeHandler(new PwEnterExpBeforeHandler());
    }

    public void run(ExpRule rule) {
        expHandler.handle(rule);
    }
}
