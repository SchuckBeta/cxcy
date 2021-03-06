/**
 * .
 */

package com.oseasy.com.fileserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICkey;
import com.oseasy.com.common.utils.SupCkey;

/**
 * 公共系统模块缓存标识.
 * @author chenhao
 */
public class FilesrCkey extends SupCkey implements ICkey{
    public FilesrCkey() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Sval.Emkey.COM_FILESERVER;
    }

    @Override
    public List<CkeyMsvo> mskeys() {
        return FilesrSval.FilesrEmskey.toCmsvos();
    }

    @Override
    public ICkey path() {
        return FilesrSval.ck;
    }

    @Override
    public CkeyMvo curmkey() {
        return mkey(mkey());
    }
}
