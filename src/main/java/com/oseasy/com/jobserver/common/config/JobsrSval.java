/**
 * .
 */

package com.oseasy.com.jobserver.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.mqserver.common.config.MqsrCkey;

/**
 * 定时任务系统模块常量类.
 * @author chenhao
 */
public class JobsrSval extends Sval{
    public static JobsrPath path = new JobsrPath();
    public static MqsrCkey ck = new MqsrCkey();

    public enum JobsrEmskey implements IEu {
        QUARTZ("quartz", "Quartz定时任务模块"),
        TASK("task", "Spring Quartz定时任务模块");

        private String key;//url
        private String remark;
        private JobsrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (JobsrEmskey entity : JobsrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (JobsrEmskey entity : JobsrEmskey.values()) {
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
