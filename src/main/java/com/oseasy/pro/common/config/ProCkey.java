/**
 * .
 */

package com.oseasy.pro.common.config;

import com.oseasy.cms.common.config.CmsSval;
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
public class ProCkey extends SupCkey implements ICkey{
    public ProCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PRO;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return ProSval.ProEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return ProSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
