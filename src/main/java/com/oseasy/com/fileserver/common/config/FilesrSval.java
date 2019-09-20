/**
 * .
 */

package com.oseasy.com.fileserver.common.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.common.utils.PathMsvo;
/**
 * 文件管理系统模块常量类.
 * @author chenhao
 */
public class FilesrSval extends Sval{
    protected static final Logger logger = LoggerFactory.getLogger(FilesrSval.class);
    public static FilesrPath path = new FilesrPath();
    public static FilesrCkey ck = new FilesrCkey();

    public enum FilesrEmskey implements IEu {
        ATTACHMENT("attachment", "文件管理核心模块"),
        VSFTP("vsftp", "基于FTP文件管理模块"),
        FASTDFS("fastDFS", "基于fastDFS文件管理模块");

        private String key;//url
        private String remark;
        private FilesrEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (FilesrEmskey entity : FilesrEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (FilesrEmskey entity : FilesrEmskey.values()) {
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
