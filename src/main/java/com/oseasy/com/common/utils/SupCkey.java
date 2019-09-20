/**
 * .
 */

package com.oseasy.com.common.utils;

import java.util.List;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.config.Sval.Emkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目模块获取缓存标识基类.
 * @author chenhao
 */
public abstract class SupCkey {
    public SupCkey() {
        super();
        if(StringUtil.checkEmpty(curmkey().getCmsvos())){
            curmkey().setCmsvos(mskeys());
        }
    }

    public abstract Sval.Emkey emkey();
    public abstract List<CkeyMsvo> mskeys();
    public abstract CkeyMvo curmkey();
    public abstract ICkey path();

    public List<CkeyMvo> mkeys() {
        return Sval.cmvos;
    }

    public String mkey() {
        return emkey().getKey();
    }

    public String subkey() {
        return emkey().getSub();
    }

    public CkeyMvo mkey(String mkey) {
        return Emkey.getCacheMkey(mkey);
    }

    public String cks(){
        return CkeyUtil.ckeys(path()) + StringUtil.MAOH + StringUtil.MAOH + StringUtil.MAOH;
    }

    public String cks(IEu mskey) {
        return CkeyUtil.ckeys(path(), mskey.k()) + StringUtil.MAOH;
    }

    public String cks(IEu mskey, String tid) {
        return CkeyUtil.ckeys(tid, path(), mskey.k()) + StringUtil.MAOH;
    }

    public String cks(IEu mskey, String tid, String unionkey) {
        return CkeyUtil.ckeys(tid, path(), mskey.k()) + StringUtil.MAOH + unionkey;
    }

    public static void main(String[] args) {
        System.out.println(CoreSval.ck.cks());
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS));
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS, "1"));
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS, "1", "A1"));
    }
}
