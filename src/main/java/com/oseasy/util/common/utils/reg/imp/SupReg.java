/**
 * .
 */

package com.oseasy.util.common.utils.reg.imp;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.reg.IReg;
import com.oseasy.util.common.utils.reg.RegOper;
import com.oseasy.util.common.utils.reg.RegVo;

/**
 * .
 * @author chenhao
 *
 */
public abstract class SupReg implements IReg{
    @Override
    public Boolean check(RegVo regvo) {
        if(StringUtil.isEmpty(regvo.getKey())){
            return false;
        }

        if(StringUtil.isEmpty(regvo.getExp())){
            return false;
        }

        return true;
    }

    @Override
    public RegVo gen(RegVo regvo) {
        if(!check(regvo)){
            return null;
        }
        return RegOper.gen(regvo);
    }
}
