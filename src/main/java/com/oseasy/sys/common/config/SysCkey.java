/**
 * .
 */

package com.oseasy.sys.common.config;

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
public class SysCkey extends SupCkey implements ICkey{
    public SysCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.SYS;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return SysSval.SysEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return SysSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
