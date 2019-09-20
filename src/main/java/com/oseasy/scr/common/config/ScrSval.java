/**
 * .
 */

package com.oseasy.scr.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;

/**
 * 学分管理系统模块常量类.
 * @author chenhao
 */
public class ScrSval extends Sval{
    public static ScrPath path = new ScrPath();
    public static ScrCkey ck = new ScrCkey();

    public enum ScrEmskey implements IEu {
        SCO("sco", "课程学分模块"),
        SCR("scr", "自定义学分模块");

        private String key;//url
        private String remark;
        private ScrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (ScrEmskey entity : ScrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (ScrEmskey entity : ScrEmskey.values()) {
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
