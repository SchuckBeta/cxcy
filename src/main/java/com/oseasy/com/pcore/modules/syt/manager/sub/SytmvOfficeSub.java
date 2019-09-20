package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.syt.manager.ISytMvo;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytmvo;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化子机构参数.
 */
public class SytmvOfficeSub extends SupSytmvo implements ISytMvo{
    /****************************************************************
     * 参数属性：用于执行当前操作所需要的参数.
     ****************************************************************/
    /**
     * 父机构.
     */
    private SytmvOffice sytmvOffice;

    /**
     * 所有子机构.
     */
    private List<Office> subs;

    /****************************************************************
     * 传递属性：用于提供给后续操作使用的参数.
     ****************************************************************/

    public SytmvOfficeSub(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public SytmvOffice getSytmvOffice() {
        return sytmvOffice;
    }

    public void setSytmvOffice(SytmvOffice sytmvOffice) {
        this.sytmvOffice = sytmvOffice;
    }

    public List<Office> getSubs() {
        if(this.subs == null){
            this.subs = Lists.newArrayList();
        }
        return subs;
    }

    public void setSubs(List<Office> subs) {
        this.subs = subs;
    }

    public static Office nprovince(SytmvOfficeSub sytmvOfficeSub){
        if(StringUtil.checkEmpty(sytmvOfficeSub.subs)){
            return null;
        }

        for (Office cur: sytmvOfficeSub.getSubs()) {
            if(isOffice(Sval.EmPn.NPROVINCE, cur)){
                return cur;
            }
        }
        return null;
    }

    public static Office nschool(SytmvOfficeSub sytmvOfficeSub){
        if(StringUtil.checkEmpty(sytmvOfficeSub.subs)){
            return null;
        }

        for (Office cur: sytmvOfficeSub.getSubs()) {
            if(isOffice(Sval.EmPn.NSCHOOL, cur)){
                return cur;
            }
        }
        return null;
    }

    public static Office ncenter(SytmvOfficeSub sytmvOfficeSub){
        if(StringUtil.checkEmpty(sytmvOfficeSub.subs)){
            return null;
        }

        for (Office cur: sytmvOfficeSub.getSubs()) {
            if(isOffice(Sval.EmPn.NCENTER, cur)){
                return cur;
            }
        }
        return null;
    }

    public static boolean isOffice(Sval.EmPn empn, Office cur){
        if((Sval.EmPn.NCENTER.getPrefix()).equals(empn)){
            if(("运营机构").equals(cur.getName())){
                return true;
            }
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(empn)){
            if(("省教育部").equals(cur.getName())){
                return true;
            }
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(empn)){
            if(("校演示大学").equals(cur.getName())){
                return true;
            }
        }
        return false;
    }

}
