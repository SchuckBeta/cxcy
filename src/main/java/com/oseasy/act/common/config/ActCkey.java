/**
 * .
 */

package com.oseasy.act.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;
import com.oseasy.com.pcore.common.config.CoreSval;

import java.util.List;

/**
 * 流程系统模块缓存标识.
 * @author chenhao
 */
public class ActCkey extends SupCkey implements ICkey{
    public ActCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.ACT;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return ActSval.ActEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return ActSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
