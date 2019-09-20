/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pie.modules.exp.ExpBeforeHandler;
import com.oseasy.pie.modules.exp.ExpRule;

/**
 * .
 * @author chenhao
 *
 */
public class PwEnterExpBeforeHandler implements ExpBeforeHandler{

    @Override
    public Boolean handle(ExpRule rule) {
        return dealMsg(rule);
    }

    @Override
    public Boolean dealMsg(ExpRule rule) {
        return true;
    }
}
