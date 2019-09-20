/**
 * .
 */

package com.oseasy.cms.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * CMS系统模块路径常量.
 * @author chenhao
 */
public class CmsPath extends SupPath implements IPath{
    public CmsPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.CMS;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return CmsSval.CmsEmskey.toPmsvos();
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
