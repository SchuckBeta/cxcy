/**
 * .
 */

package com.oseasy.pw.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 入驻管理系统模块路径常量.
 * @author chenhao
 */
public class PwPath extends SupPath implements IPath{
    public PwPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PW;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return PwSval.PwEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return PwSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
