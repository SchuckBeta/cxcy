/**
 * .
 */

package com.oseasy.com.jobserver.common.config;

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
public class JobsrCkey extends SupCkey implements ICkey{
    public JobsrCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_JOBSERVER;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return JobsrSval.JobsrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return JobsrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
