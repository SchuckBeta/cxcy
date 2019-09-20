/**
 * .
 */

package com.oseasy.act.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.common.utils.*;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.pcore.common.config.CorePncenter;
import com.oseasy.com.pcore.common.config.CorePnprovince;
import com.oseasy.com.pcore.common.config.CorePnschool;

/**
 * Act工作流系统模块常量类.
 * @author chenhao
 */
public class ActSval extends Sval{
    public static ActPath path = new ActPath();
    public static ActCkey ck = new ActCkey();
    public static List<IActPn> actpns = Lists.newArrayList();

    static{
        actpns.add(ActPncenter.init());
        actpns.add(ActPnprovince.init());
        actpns.add(ActPnschool.init());
    }

    public static final String ASD_HOME = "/auditstandard/index/home";//YWID标识
    public static final String ASD_INDX = "/auditstandard/index";//YWID标识
    public static final String ASD_INDEX = ASD_INDX + "?" + ActYwGroup.JK_ACTYW_ID + "=";//YWID标识

    public enum ActEmskey implements IEu {
        ACT("act", "Activit"),
        ACTYW("actyw", "自定义工作流"),
        ACTYWYEAR("actywyear", "工作流年份"),
        PRO("pro", "流程配置、拓展");

        private String key;//url
        private String remark;
        private ActEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (ActEmskey entity : ActEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (ActEmskey entity : ActEmskey.values()) {
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
}
