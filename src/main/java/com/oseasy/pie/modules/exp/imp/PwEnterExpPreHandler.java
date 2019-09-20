/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import com.oseasy.pie.modules.exp.ExpPreHandler;
import com.oseasy.pie.modules.exp.ExpRule;

/**
 * .
 * @author chenhao
 *
 */
public class PwEnterExpPreHandler implements ExpPreHandler{
    @Override
    public Boolean handle(ExpRule rule) {
        return check(rule);
    }

    @Override
    public Boolean check(ExpRule rule) {
        return true;
    }

}
