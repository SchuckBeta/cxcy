/**
 * .
 */

package com.oseasy.pie.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;
import com.oseasy.pro.common.config.ProSval;

import java.util.List;

/**
 * 公共系统模块缓存标识.
 * @author chenhao
 */
public class PieCkey extends SupCkey implements ICkey{
    public PieCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PIE;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return PieSval.PieEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return PieSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
