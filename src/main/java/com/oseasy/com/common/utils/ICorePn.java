package com.oseasy.com.common.utils;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CorePncenter;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public interface ICorePn {
    public static ICorePn curPt(){
        ICorePn corepn = null;

        String currpn = CoreSval.getTenantCurrpn();
        if(StringUtil.checkEmpty(Sval.cpns) || StringUtil.isEmpty(currpn)){
            return new CorePncenter();
        }

        for (ICorePn cur: Sval.cpns) {
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
     * 登录页面地址
     * @param pt
     * @return String
     */
    public String loginPage(Sval.EmPt pt);

    /**
     * 登录成功页面地址
     * @param pt
     * @return String
     */
    public String loginSuccessPage(Sval.EmPt pt);

    /**
     * 首页页面地址
     * @param pt
     * @return String
     */
    public String indexPage(Sval.EmPt pt);

    /**
     * 框架页面地址
     * @param pt
     * @return String
     */
    public String framePage(Sval.EmPt pt);

    /**
     * 公共页面根目录
     * @param pt
     * @return String
     */
    public String rootPage(Sval.EmPt pt);
}
