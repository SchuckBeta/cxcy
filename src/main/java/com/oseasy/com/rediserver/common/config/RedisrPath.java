/**
 * .
 */

package com.oseasy.com.rediserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

/**
 * 缓存管理系统模块路径常量.
 * @author chenhao
 */
public class RedisrPath extends SupPath implements IPath{
    public RedisrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Emkey.COM_REDISERVER;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return RedisrSval.RedisrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return RedisrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
