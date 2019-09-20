package com.oseasy.act.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public interface IActPn {
    public static IActPn curPt(){
        IActPn corepn = null;

        String currpn = CoreSval.getTenantCurrpn();
        if(StringUtil.checkEmpty(Sval.cpns) || StringUtil.isEmpty(currpn)){
            return new ActPncenter();
        }

        String curtid = TenantConfig.getCacheTenant();
        if((CoreIds.NCE_SYS_TENANT.getId()).equals(curtid)){
            currpn =  Sval.EmPn.NCENTER.getPrefix();
        }else if((CoreIds.NPR_SYS_TENANT.getId()).equals(curtid)){
            currpn =  Sval.EmPn.NPROVINCE.getPrefix();
        }else if((CoreIds.NSC_SYS_TENANT.getId()).equals(curtid)){
            currpn =  Sval.EmPn.NSCHOOL.getPrefix();
        }

        for (IActPn cur: ActSval.actpns) {
            System.out.println("currpn="+currpn + "=?" + "cur.pn()="+cur.pn());
            if(((currpn).equals(cur.pn()))){
                corepn = cur;
                break;
            }
        }
        System.out.println("corepn="+corepn);
        return corepn;
    }

    /**
     * 平台标识.
     * @return
     */
    public String pn();

    /**
     * 平台端标识.
     * @return
     */
    public String pt(Sval.EmPt pt);

    /**
     * 流程列表页面
     * @param pt
     * @return String
     */
    public String groupList(Sval.EmPt pt);

    /**
     * 流程表单页面
     * @param pt
     * @return String
     */
    public String groupForm(Sval.EmPt pt);

    /**
     * 项目列表页面
     * @param pt
     * @return String
     */
    public String actywList(Sval.EmPt pt);

    /**
     * 项目表单页面
     * @param pt
     * @return String
     */
    public String actywForm(Sval.EmPt pt);

    /**
     * 项目修改属性页面
     * @param pt
     * @return String
     */
    public String actywProp(Sval.EmPt pt);
}
