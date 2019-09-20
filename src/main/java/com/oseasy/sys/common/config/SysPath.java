/**
 * .
 */

package com.oseasy.sys.common.config;

import java.util.List;

import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;
import com.oseasy.com.common.config.Sval;

/**
 * 用户拓展信息管理系统模块路径常量.
 * @author chenhao
 */
public class SysPath extends SupPath implements IPath{
    public SysPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.SYS;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return SysSval.SysEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return SysSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
