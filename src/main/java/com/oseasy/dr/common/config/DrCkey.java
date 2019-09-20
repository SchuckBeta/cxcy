/**
 * .
 */

package com.oseasy.dr.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

import java.util.List;

/**
 * 公共系统模块缓存标识.
 * @author chenhao
 */
public class DrCkey extends SupCkey implements ICkey{
    public DrCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.DR;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return DrSval.DrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return DrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
