/**
 * .
 */

package com.oseasy.pro.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cms.common.config.CmsCkey;
import com.oseasy.com.common.utils.CkeyMsvo;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.IEu;

/**
 * 项目管理系统模块常量类.
 * @author chenhao
 */
public class ProSval extends Sval{
    public static final String DICT_COMPETITION_NET_TYPE = "competition_net_type";//互联网+参赛类别
    public static final String DICT_GCONTEST_LEVEL = "gcontest_level";//互联网+大赛参赛组别
    public static final String DICT_COMPETITION_COLLEGE_PRISE = "competition_college_prise";//互联网+奖项
    public static ProPath path = new ProPath();
    public static ProCkey ck = new ProCkey();
    public enum ProEmskey implements IEu {
        ANALYSIS("analysis", "分析模块"),
        AUDITSTAN("auditstandard", "审核标准模块"),
        CERT("cert", "证书模块"),
        COURSE("course", "课程模块"),
        DASAI("dasai", "大赛模块"),
        EXCELLENT("excellent", "优秀项目模块"),
        GCONTEST("gcontest", "大赛模块"),
        PROJECT("project", "项目模块"),
        PROMODEL("promodel", "自定义项目模块"),
        PROPROJECTMD("proprojectmd", "民大自定义项目模块"),
        STATE("state", "变更模块"),
        TPL("tpl", "模板模块"),
        WORKFLOW("workflow", "自定义工作流模块"),
        INTERACTIVE("interactive", "文章评论点赞");

        private String key;//url
        private String remark;
        private ProEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (ProEmskey entity : ProEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (ProEmskey entity : ProEmskey.values()) {
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
    /**
     * 双创大赛类型字典值
     */
    public static final String PRO_TYPE_GCONTEST = "7,";
    /**
     * 双创项目类型字典值
     */
    public static final String PRO_TYPE_PROJECT = "1,";
    public static final String VIEW = "view";
    /**
     * 表单类型
     */
    public static final String EDIT = "edit";
}
