/**
 * .
 */

package com.oseasy.com.jobserver.common.config;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.com.common.utils.IPath;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.common.utils.SupPath;

/**
 * 定时任务系统模块路径常量.
 * @author chenhao
 */
public class JobsrPath extends SupPath implements IPath{
    public JobsrPath() {
        super();
    }

    @Override
    public Sval.Emkey emkey() {
        return Emkey.COM_JOBSERVER;
    }

    @Override
    public List<PathMsvo> mskeys() {
        return JobsrSval.JobsrEmskey.toPmsvos();
    }

    @Override
    public IPath path() {
        return JobsrSval.path;
    }

    @Override
    public PathMvo curmkey() {
        return mkey(mkey());
    }
}
