/**
 * .
 */

package com.oseasy.auy.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActCkey;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.pie.common.config.PieSval;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pw.common.config.PwSval;
import com.oseasy.scr.common.config.ScrSval;
import com.oseasy.sys.common.config.SysSval;

/**
 * 辅助模块常量类.
 * @author chenhao
 */
public class AuySval extends Sval{
    public static AuyPath path = new AuyPath();
    public static AuyCkey ck = new AuyCkey();

    public enum AuyEmskey implements IEu {
        MENU("menu", "自定义流程菜单模块");

        private String key;//url
        private String remark;
        private AuyEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (AuyEmskey entity : AuyEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (AuyEmskey entity : AuyEmskey.values()) {
                entitys.add(new CkeyMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }
        
        public String k() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }


    public static void main(String[] args) {
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS));
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS, "租户1"));
        System.out.println(CoreSval.ck.cks(CoreSval.CoreEmskey.SYS, "租户1", "业务A1"));

        System.out.println(ActSval.ck.cks(ActSval.ActEmskey.ACTYW, "租户1"));
        System.out.println(ActSval.ck.cks(ActSval.ActEmskey.ACTYW, "租户1", "业务B1"));
        System.out.println(ActSval.ck.cks(ActSval.ActEmskey.PRO, "租户21"));
        System.out.println(ActSval.ck.cks(ActSval.ActEmskey.PRO, "租户1", "业务B1"));

        System.out.println(CmsSval.ck.cks(CmsSval.CmsEmskey.CMS, "租户1"));
        System.out.println(CmsSval.ck.cks(CmsSval.CmsEmskey.WEBSITE, "租户1", "业务1"));

        System.out.println(AuySval.ck.cks(AuySval.AuyEmskey.MENU, "租户1"));
        System.out.println(AuySval.ck.cks(AuySval.AuyEmskey.MENU, "租户1", "业务1"));

        System.out.println(ProSval.ck.cks(ProSval.ProEmskey.PROJECT, "租户1"));
        System.out.println(ProSval.ck.cks(ProSval.ProEmskey.GCONTEST, "租户1", "业务1"));

        System.out.println(PieSval.ck.cks(PieSval.PieEmskey.EXP, "租户1"));
        System.out.println(PieSval.ck.cks(PieSval.PieEmskey.IEP, "租户1", "业务1"));

        System.out.println(CasSval.ck.cks(CasSval.CasEmskey.CAS, "租户1"));
        System.out.println(CasSval.ck.cks(CasSval.CasEmskey.CAS, "租户1", "业务1"));

        System.out.println(DrSval.ck.cks(DrSval.DrEmskey.DR, "租户1"));
        System.out.println(DrSval.ck.cks(DrSval.DrEmskey.DR, "租户1", "业务1"));

        System.out.println(PwSval.ck.cks(PwSval.PwEmskey.PW, "租户1"));
        System.out.println(PwSval.ck.cks(PwSval.PwEmskey.PW, "租户1", "业务1"));

        System.out.println(ScrSval.ck.cks(ScrSval.ScrEmskey.SCR, "租户1"));
        System.out.println(ScrSval.ck.cks(ScrSval.ScrEmskey.SCO, "租户1", "业务1"));

        System.out.println(SysSval.ck.cks(SysSval.SysEmskey.SYS, "租户1"));
        System.out.println(SysSval.ck.cks(SysSval.SysEmskey.SYS, "租户1", "业务1"));
    }
}
