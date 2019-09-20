/**
 * .
 */

package com.oseasy.com.mqserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

/**
 * 消息服务管理系统模块路径常量.
 * @author chenhao
 */
public class MqsrPath extends SupPath implements IPath{
    public MqsrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Emkey.COM_MQSERVER;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return MqsrSval.MqsrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return MqsrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
