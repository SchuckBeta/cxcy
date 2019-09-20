/**
 * .
 */

package com.oseasy.com.rediserver.common.config;

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
public class RedisrCkeys extends SupCkey implements ICkey{
    public RedisrCkeys() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_REDISERVER;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return RedisrSval.RedisrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return RedisrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
