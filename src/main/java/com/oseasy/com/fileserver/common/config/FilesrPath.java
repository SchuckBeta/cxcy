/**
 * .
 */

package com.oseasy.com.fileserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

/**
 * 文件管理系统模块路径常量.
 * @author chenhao
 */
public class FilesrPath extends SupPath implements IPath{
    public FilesrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Emkey.COM_FILESERVER;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return FilesrSval.FilesrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return FilesrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
