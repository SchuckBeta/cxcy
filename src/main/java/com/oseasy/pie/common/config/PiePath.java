/**
 * .
 */

package com.oseasy.pie.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 导入导出系统模块路径常量.
 * @author chenhao
 */
public class PiePath extends SupPath implements IPath{
    public PiePath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.PIE;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return PieSval.PieEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return PieSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
