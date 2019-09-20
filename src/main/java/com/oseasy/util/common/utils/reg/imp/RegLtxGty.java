/**
 * .
 */

package com.oseasy.util.common.utils.reg.imp;

import com.oseasy.util.common.utils.reg.RegType;
import com.oseasy.util.common.utils.reg.RegVo;

/**
 * k < x || k > y.
 * @author chenhao
 *
 */
public class RegLtxGty extends SupReg{
    @Override
    public RegType key() {
        return RegType.OR_LTX_GTY;
    }

    @Override
    public boolean validate(RegVo regVo, float val) {
        return val < regVo.getDx() || val > regVo.getDy();
    }
}
