package com.oseasy.sys.modules.sys.vo;

import com.oseasy.util.common.utils.UrlUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by Administrator on 2019/7/2 0002.
 */
public class Urvo implements Serializable {
    private String type;
    private String referer;
    private String url;
    private String html;
    private boolean showVcode;
    private Usvo usvo;

    public Urvo() {
        this.type = Urtype.URL.getKey();
        this.showVcode = false;
    }

    public Urvo(HttpServletRequest request) {
        this.type = Urtype.URL.getKey();
        this.referer = UrlUtil.referer(request);
        this.showVcode = false;
    }

    public Urvo(HttpServletRequest request, Usvo usvo) {
        this.type = Urtype.URL.getKey();
        this.referer = UrlUtil.referer(request);
        this.usvo = usvo;
        this.showVcode = false;
    }

    public boolean isShowVcode() {
        return showVcode;
    }

    public void setShowVcode(boolean showVcode) {
        this.showVcode = showVcode;
    }

    public Usvo getUsvo() {
        return usvo;
    }

    public void setUsvo(Usvo usvo) {
        this.usvo = usvo;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public enum Urtype{
        URL("1", "地址"),
        HTML("2", "HTML");

        private String key;//主题
        private String remark;//

        private Urtype(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        /**
         * 根据key获取枚举 .
         * @author chenhao
         * @param key
         *            页面标识
         * @return Urtype
         */
        public static Urtype getByKey(String key) {
            if ((key != null)) {
                Urtype[] entitys = Urtype.values();
                for (Urtype entity : entitys) {
                    if ((key).equals(entity.getKey())) {
                        return entity;
                    }
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }
}
