/**
 * .
 */

package com.oseasy.com.common.config;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.CkeyMvo;
import com.oseasy.com.common.utils.ICorePn;
import com.oseasy.com.common.utils.PathMvo;
import com.oseasy.com.pcore.common.config.CorePncenter;
import com.oseasy.com.pcore.common.config.CorePnprovince;
import com.oseasy.com.pcore.common.config.CorePnschool;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 模块常量类接口.
 * @author chenhao
 */
public class Sval {
    public static final String WEB_INF_VIEWS = "/WEB-INF/views/";
    public static final String VIEWS_MODULES = "modules";
    public static final String VIEWS_SITES = "sites";
    public static final String VIEWS_TEMPLATE = "template";
    public static final String BASE_PACKAGE = "com.oseasy";
    public static final String MAPPINGS = "mappings";
    public static List<PathMvo> pmvos = Lists.newArrayList();
    public static List<CkeyMvo> cmvos = Lists.newArrayList();
    public static List<ICorePn> cpns = Lists.newArrayList();

    static{
        pmvos.addAll(Emkey.toPmvos());
        cmvos.addAll(Emkey.toCmvos());

        cpns.add(CorePncenter.init());
        cpns.add(CorePnprovince.init());
        cpns.add(CorePnschool.init());
    }

    public enum Emkey {
        COM_PCORE("com", "pcore", "公共授权和中间件系统"),
        COM_FILESERVER("com", "fileserver", "公共文件管理系统"),
        COM_JOBSERVER("com", "jobserver", "公共定时任务系统"),
        COM_MQSERVER("com", "mqserver", "公共消息推送系统"),
        COM_REDISERVER("com", "rediserver", "公共缓存管理系统"),
        DEMO("demo", "", "样例系统"),
        DEMOA("demoa", "demoa", "样例系统"),
        ACT("act", "", "工作流系统"),
        AUY("auy", "", "业务辅助系统"),
        CAS("cas", "", "单点登录系统"),
        CMS("cms", "", "内容管理系统"),
        DR("dr", "", "门禁系统"),
        PIE("pie", "", "导入导出系统"),
        PRO("pro", "", "项目管理系统"),
        PW("pw", "", "基地入驻管理系统"),
        SCR("scr", "", "学分管理系统"),
        SYS("sys", "", "用户拓展信息管理系统"),
        UTIL("util", "", "工具系统"),

        WEB_COM("", "", "UI公共依赖系统"),
        WEB_CENTER("center", "", "运营中心系统"),
        WEB_NPROVINCE("nprovince", "", "省级节点系统"),
        WEB_NSCHOOL("nschool", "", "省级节点系统");

        private String key;//系统
        private String sub;//子系统
        private String remark;
        private Emkey(String key, String sub, String remark) {
            this.key = key;
            this.sub = sub;
            this.remark = remark;
        }

        public static List<PathMvo> toPmvos() {
            List<PathMvo> entitys = Lists.newArrayList();
            for (Emkey entity : Emkey.values()) {
                entitys.add(new PathMvo(entity));
            }
            return entitys;
        }

        /**
         * 增加模块到系统常量中.
         */
        public static PathMvo getPathMkey(String mkey) {
            for (PathMvo cur : Sval.pmvos) {
                if((cur.getKey()).equals(mkey)){
                    return cur;
                }
            }
            return null;
        }

        public static List<CkeyMvo> toCmvos() {
            List<CkeyMvo> entitys = Lists.newArrayList();
            for (Emkey entity : Emkey.values()) {
                entitys.add(new CkeyMvo(entity));
            }
            return entitys;
        }

        /**
         * 增加模块到系统常量中.
         */
        public static CkeyMvo getCacheMkey(String mkey) {
            for (CkeyMvo cur : Sval.cmvos) {
                if((cur.getKey()).equals(mkey)){
                    return cur;
                }
            }
            return null;
        }
        public String getKey() {
            return key;
        }

        public String getSub() {
            return sub;
        }

        public String getRemark() {
            return remark;
        }
    }

    /**
     * 系统平台节点.
     */
    public enum EmPn {
        NCENTER("1", "ncenter", "运营中心平台"),
        NPROVINCE("2", "nprovince", "省中心平台"),
        NSCHOOL("3", "nschool", "校节点平台");

        private String prefix;//前缀标识
        private String key;//系统
        private String remark;
        EmPn(String prefix, String key, String remark) {
            this.key = key;
            this.prefix = prefix;
            this.remark = remark;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }


        /**
         * 根据key获取枚举 .
         * @author chenhao
         * @param prefix 标识
         * @return EmPn
         */
        public static EmPn getByPrefix(String prefix) {
            if ((prefix != null)) {
                EmPn[] entitys = EmPn.values();
                for (EmPn entity : entitys) {
                    if ((entity.getPrefix()).equals(prefix)) {
                        return entity;
                    }
                }
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("{\"key\":\"" + this.key +"\"");
            buffer.append(",\"prefix\":" + "\""+ this.prefix +"\"");
            if(StringUtil.isNotEmpty(this.remark)){
                buffer.append(",\"remark\":\"" + this.remark + "\"}");
            }else{
                buffer.append(",\"remark\":\"\"}");
            }
            return buffer.toString();
        }
    }

    /**
     * 系统平台访问端类型.
     */
    public enum EmPt {
        TW_FRONT("web", "1", "front", "前台"),
        TW_ADMIN("web", "2", "admin", "后台"),
        TM_FRONT("mobile", "1", "front", "前台"),
        TM_ADMIN("mobile", "2", "admin", "后台");

        private String terminal;//终端标识
        private String key;//系统
        private String prefix;//前缀标识
        private String remark;
        EmPt(String terminal, String key, String prefix, String remark) {
            this.terminal = terminal;
            this.key = key;
            this.prefix = prefix;
            this.remark = remark;
        }

        public String getTerminal() {
            return terminal;
        }

        public String getPrefix() {
            return prefix;
        }

        public String key() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }
}
