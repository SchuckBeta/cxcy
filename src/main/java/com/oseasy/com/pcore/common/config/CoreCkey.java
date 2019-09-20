/**
 * .
 */

package com.oseasy.com.pcore.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

/**
 * 公共系统模块缓存标识.
 * @author chenhao
 */
public class CoreCkey extends SupCkey implements ICkey{
    public CoreCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_PCORE;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return CoreSval.CoreEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return CoreSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
