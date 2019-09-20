/**
 * .
 */

package com.oseasy.cms.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;
import com.oseasy.com.pcore.common.config.CoreSval;

import java.util.List;

/**
 * 公共系统模块缓存标识.
 * @author chenhao
 */
public class CmsCkey extends SupCkey implements ICkey{
    public CmsCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.CMS;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return CmsSval.CmsEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return CmsSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
