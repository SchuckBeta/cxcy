/**
 * .
 */

package com.oseasy.auy.common.config;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

import java.util.List;

/**
 * 流程系统模块缓存标识.
 * @author chenhao
 */
public class AuyCkey extends SupCkey implements ICkey{
    public AuyCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.AUY;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return AuySval.AuyEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return AuySval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
