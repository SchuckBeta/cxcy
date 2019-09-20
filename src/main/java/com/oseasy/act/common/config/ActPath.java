/**
 * .
 */

package com.oseasy.act.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * ACT系统模块路径常量.
 * @author chenhao
 */
public class ActPath extends SupPath implements IPath{
    public ActPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.ACT;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return ActSval.ActEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return ActSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
