package com.oseasy.com.pcore.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public class CorePnschool implements ICorePn {
    private static CorePnschool corePn = new CorePnschool();
    public static CorePnschool init(){
        return corePn;
    }

    @Override
    public String pn() {
        return Sval.EmPn.NSCHOOL.getPrefix();
    }

    @Override
    public String pt(Sval.EmPt pt) {
        return pt.key();
    }

    @Override
    public String loginPage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysLogin";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysLogin";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String loginSuccessPage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNschoolMenuIndex";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysMenuIndex";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String indexPage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysIndex";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysIndex";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String framePage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CorePages.IDX_BACK_V4.getIdxUrl();
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CorePages.IDX_BACK_V4.getIdxUrl();
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String rootPage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return "";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return "";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }
}
