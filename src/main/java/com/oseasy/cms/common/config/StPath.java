/**
 * .
 */

package com.oseasy.cms.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

import java.util.List;

/**
 * SIte 模块路径常量.
 * @author chenhao
 */
public class StPath extends SupPath implements IPath{
    public StPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.CMS;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return CmsSval.StEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return CmsSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
