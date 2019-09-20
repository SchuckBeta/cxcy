package com.oseasy.com.pcore.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public class CorePnprovince implements ICorePn {
    private static CorePnprovince corePn = new CorePnprovince();
    public static CorePnprovince init(){
        return corePn;
    }

    @Override
    public String pn() {
        return Sval.EmPn.NPROVINCE.getPrefix();
    }

    @Override
    public String pt(Sval.EmPt pt) {
        return pt.key();
    }

    @Override
    public String loginPage(Sval.EmPt pt) {
        System.out.println("pt = " + pt.getRemark());
        System.out.println("((Sval.EmPt.TM_ADMIN).equals(pt)) = " + ((Sval.EmPt.TM_ADMIN).equals(pt)));
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceLogin";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceLogin";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String loginSuccessPage(Sval.EmPt pt) {
        System.out.println("loginSuccessPage-pt = " + pt.getRemark());
        System.out.println("loginSuccessPage-((Sval.EmPt.TM_ADMIN).equals(pt)) = " + ((Sval.EmPt.TM_ADMIN).equals(pt)));
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceMenuIndex";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceMenuIndex";
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String indexPage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            System.out.println("indexPage--->TM_ADMIN");
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceIndex";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            System.out.println("indexPage--->TM_FRONT");
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceIndex";
        }
        System.out.println("indexPage--->ERROR_404");
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public String framePage(Sval.EmPt pt) {
        if((Sval.EmPt.TM_ADMIN).equals(pt)){
            System.out.println("framePage--->TM_ADMIN");
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceIndexBackV4";
        }else if((Sval.EmPt.TM_FRONT).equals(pt)){
            System.out.println("framePage--->TM_FRONT");
            return CoreSval.path.vms(CoreSval.CoreEmskey.SYS.k()) + "sysNprovinceIndexBackV4";
        }
        System.out.println("framePage--->ERROR_404");
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
