/**
 * .
 */

package com.oseasy.com.common.utils;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目模块获取路径基类.
 * @author chenhao
 */
public abstract class SupPath {
    public SupPath() {
        super();
        if(StringUtil.checkEmpty(curmkey().getPmsvos())){
            curmkey().setPmsvos(mskeys());
        }
    }

    public abstract Sval.Emkey emkey();
    public abstract List<PathMsvo> mskeys();
    public abstract PathMvo curmkey();
    public abstract IPath path();

    public List<PathMvo> mkeys() {
        return Sval.pmvos;
    }

    public String mkey() {
        return emkey().getKey();
    }

    public String subkey() {
        return emkey().getSub();
    }

    public PathMvo mkey(String mkey) {
        return Emkey.getPathMkey(mkey);
    }

    public String packge(){
        return PathUtil.packge(path());
    }

    public String mappings(){
        return PathUtil.mappings(path());
    }

    public String packge(String mskey) {
        return PathUtil.packge(path(), mskey);
    }

    public String mappings(String mskey) {
        return PathUtil.mappings(path(), mskey);
    }

    public String vms(){
        return PathUtil.vmodules(path());
    }

    public String vms(String mskey) {
        return PathUtil.vmodules(path(), mskey);
    }

    public String vss(String app) {
        return PathUtil.vsites(path(), app);
    }

    public String vss(String mskey, String app) {
        return PathUtil.vsites(path(), mskey, app);
    }
}
