/**
 * .
 */

package com.oseasy.com.rediserver.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;

/**
 * 缓存管理系统模块常量类.
 * @author chenhao
 */
public class RedisrSval extends Sval{
    public static RedisrPath path = new RedisrPath();
    public static RedisrCkeys ck = new RedisrCkeys();

    public static String dota = ":";

    public enum RedisrEmskey implements IEu {
        REDIS("redis", "Redis缓存模块");

        private String key;//url
        private String remark;
        private RedisrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (RedisrEmskey entity : RedisrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (RedisrEmskey entity : RedisrEmskey.values()) {
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
