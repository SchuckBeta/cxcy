/**
 * .
 */

package com.oseasy.cms.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cms.modules.cms.service.CategoryService;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;
import com.oseasy.com.pcore.common.config.CoreCkey;
import com.oseasy.com.pcore.modules.authorize.vo.IAuthCheck;
import com.oseasy.com.pcore.modules.sys.service.SystemService;

/**
 * CMS系统模块常量类.
 * @author chenhao
 */
public class CmsSval extends Sval{
    public static CmsPath path = new CmsPath();
    public static CmsCkey ck = new CmsCkey();

    public enum CmsEmskey implements IEu {
        WEBSITE("website", "公共页面管理"),
        CMS("cms", "内容管理"),
        SC("sc", "站点静态页管理"),
        SI("si", "站点管理"),
        ST("st", "站点模板管理");

        private String key;//url
        private String remark;
        private CmsEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (CmsEmskey entity : CmsEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (CmsEmskey entity : CmsEmskey.values()) {
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

    public enum StEmskey implements IEu {
        SC("sc", "站点静态页管理"),
        SI("si", "站点管理"),
        ST("st", "站点模板管理");

        private String key;//url
        private String remark;
        private StEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (StEmskey entity : StEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (StEmskey entity : StEmskey.values()) {
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
