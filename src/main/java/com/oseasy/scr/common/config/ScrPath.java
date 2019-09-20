/**
 * .
 */

package com.oseasy.scr.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 学分管理系统模块路径常量.
 * @author chenhao
 */
public class ScrPath extends SupPath implements IPath{
    public ScrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.SCR;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return ScrSval.ScrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return ScrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
