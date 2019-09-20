/**
 * .
 */

package com.oseasy.pie.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.utils.PathUtil;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.pro.common.config.ProCkey;

/**
 * 导入导出.
 * @author chenhao
 */
public class PieSval {
    public static PiePath path = new PiePath();
    public static PieCkey ck = new PieCkey();
    public static final String VIEWS_IE = "/pie";
    public static final String VIEWS_MD_IEIEP = PathUtil.vmodules() + VIEWS_IE + "/iep";
    public static final String ROOT_IMP = PathUtil.view() + PathUtil.vtemplate() + "/imp";
    public static final String VIEWS_TEMPLATE_IMP = PathUtil.view() + PathUtil.vtemplate() + "/iep";

    public enum PieEmskey implements IEu {
        IEP("iep", "统一导入导出模块（目前民大互联网+大赛导入应用）"),
        EXP("exp", "导出模块"),
        EXPDATA("expdata", "导出数据模块"),
        IMPDATA("impdata", "导入数据模块");

        private String key;//url
        private String remark;
        private PieEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (PieEmskey entity : PieEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (PieEmskey entity : PieEmskey.values()) {
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
