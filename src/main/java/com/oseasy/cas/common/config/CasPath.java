/**
 * .
 */

package com.oseasy.cas.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * CAS系统模块路径常量.
 * @author chenhao
 */
public class CasPath extends SupPath implements IPath{
    public CasPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.CAS;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return CasSval.CasEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return CasSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
