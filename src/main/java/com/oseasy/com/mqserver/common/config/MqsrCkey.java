/**
 * .
 */

package com.oseasy.com.mqserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

/**
 * 公共MQ系统模块缓存标识.
 * @author chenhao
 */
public class MqsrCkey extends SupCkey implements ICkey{
    public MqsrCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_MQSERVER;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return MqsrSval.MqsrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return MqsrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
