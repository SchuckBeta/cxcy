/**
 * .
 */

package com.oseasy.pie.modules.impdata.vo;

/**
 * .
 * @author chenhao
 *
 */
public class ImpDataVo {
    private String type;
    private String actywid;
    private String gnodeId;
    private String referer;
    private String referrer;
    private String encodereferrer;

    public static class Builder {
        private ImpDataVo impDataVo;

        public Builder() {
            impDataVo = new ImpDataVo();
        }

        public Builder type(String type) {
            impDataVo.type = type;
            return this;
        }
        public Builder referer(String referer) {
            impDataVo.referer = referer;
            return this;
        }
        public Builder referrer(String referrer) {
            impDataVo.referrer = referrer;
            return this;
        }
        public String getReferrer() {
            return impDataVo.referrer;
        }
        public Builder encodereferrer(String encodereferrer) {
            impDataVo.encodereferrer = encodereferrer;
            return this;
        }
        public Builder actywid(String actywid) {
            impDataVo.actywid = actywid;
            return this;
        }
        public Builder gnodeId(String gnodeId) {
            impDataVo.gnodeId = gnodeId;
            return this;
        }

        public ImpDataVo build() {
            return this.impDataVo;
        }
    }

    public String getGnodeId() {
        return gnodeId;
    }

    public void setGnodeId(String gnodeId) {
        this.gnodeId = gnodeId;
    }

    public String getActywid() {
        return actywid;
    }

    public void setActywid(String actywid) {
        this.actywid = actywid;
    }

    public String getType() {
        return type;
    }

    public String getReferer() {
        return referer;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getEncodereferrer() {
        return encodereferrer;
    }
}
