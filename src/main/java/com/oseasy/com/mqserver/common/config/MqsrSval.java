/**
 * .
 */

package com.oseasy.com.mqserver.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;

/**
 * 消息服务管理系统模块常量类.
 * @author chenhao
 */
public class MqsrSval extends Sval{
    public static MqsrPath path = new MqsrPath();
    public static MqsrCkey ck = new MqsrCkey();

    public enum MqsrEmskey implements IEu {
        OA("oa", "基础消息模块"),
        MQ_REDIS("redis", "基于Redis消息模块"),
        MQ_ACTIVE("active", "基于ActiveMQ消息模块"),
        MQ_RABBIT("rabbit", "基于RabbitMQ消息模块"),
        MQ_KAFKA("kafka", "基于Kafka消息模块");

        private String key;//url
        private String remark;
        private MqsrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (MqsrEmskey entity : MqsrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (MqsrEmskey entity : MqsrEmskey.values()) {
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
