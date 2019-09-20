/**
 * .
 */

package com.oseasy.pro.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 项目管理系统模块路径常量.
 * @author chenhao
 */
public class ProPath extends SupPath implements IPath{
    public ProPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PRO;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return ProSval.ProEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return ProSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
