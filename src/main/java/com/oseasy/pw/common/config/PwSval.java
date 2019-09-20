/**
 * .
 */

package com.oseasy.pw.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;

/**
 * 入驻管理系统模块常量类.
 * @author chenhao
 */
public class PwSval extends Sval{
    public static PwPath path = new PwPath();
    public static PwCkey ck = new PwCkey();

    public enum PwEmskey implements IEu {
        PW("pw", "入驻管理模块");

        private String key;//url
        private String remark;
        private PwEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (PwEmskey entity : PwEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (PwEmskey entity : PwEmskey.values()) {
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
