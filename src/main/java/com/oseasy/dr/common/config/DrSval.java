/**
 * .
 */

package com.oseasy.dr.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;

/**
 * 门禁系统模块常量类.
 * @author chenhao
 */
public class DrSval extends Sval{
    public static DrPath path = new DrPath();
    public static DrCkey ck = new DrCkey();

    public enum DrEmskey implements IEu {
        DR("dr", "门禁模块");

        private String key;//url
        private String remark;
        private DrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (DrEmskey entity : DrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (DrEmskey entity : DrEmskey.values()) {
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
