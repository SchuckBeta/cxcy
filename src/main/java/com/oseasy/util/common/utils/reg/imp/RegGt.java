/**
 * .
 */

package com.oseasy.util.common.utils.reg.imp;

import com.oseasy.util.common.utils.reg.RegType;
import com.oseasy.util.common.utils.reg.RegVo;

/**
 * .
 * @author chenhao
 *
 */
public class RegGt extends SupReg{
    @Override
    public RegType key() {
        return RegType.GTX_MAX;
    }

    @Override
    public boolean validate(RegVo regVo, float val) {
        return regVo.getDx() < val;
    }
}
