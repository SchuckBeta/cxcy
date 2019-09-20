/**
 * .
 */

package com.oseasy.dr.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * [DEMO]系统模块路径常量.
 * @author chenhao
 */
public class DrPath extends SupPath implements IPath{
    public DrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.DR;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return DrSval.DrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return DrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
