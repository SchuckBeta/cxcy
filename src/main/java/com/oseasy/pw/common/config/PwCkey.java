/**
 * .
 */

package com.oseasy.pw.common.config;

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
public class PwCkey extends SupCkey implements ICkey{
    public PwCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PW;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return PwSval.PwEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return PwSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
