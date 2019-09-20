/**
 * .
 */

package com.oseasy.auy.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 辅助模块路径常量.
 * @author chenhao
 */
public class AuyPath extends SupPath implements IPath{
    public AuyPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.AUY;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return AuySval.AuyEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return AuySval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
