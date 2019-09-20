/**
 * .
 */

package com.oseasy.cas.common.config;

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
public class CasCkey extends SupCkey implements ICkey{
    public CasCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.CAS;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return CasSval.CasEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return CasSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
