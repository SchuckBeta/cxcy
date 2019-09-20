/**
 * .
 */

package com.oseasy.com.pcore.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

/**
 * 公共系统模块路径常量.
 * @author chenhao
 */
public class CorePath extends SupPath implements IPath{
    public CorePath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_PCORE;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return CoreSval.CoreEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return CoreSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
