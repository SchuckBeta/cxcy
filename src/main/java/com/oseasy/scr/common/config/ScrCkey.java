/**
 * .
 */

package com.oseasy.scr.common.config;

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
public class ScrCkey extends SupCkey implements ICkey{
    public ScrCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.SCR;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return ScrSval.ScrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return ScrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
