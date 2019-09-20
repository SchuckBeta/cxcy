package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 固定资产编号规则Entity.
 *
 * @author pw
 * @version 2017-12-05
 */
public class PwFassetsnoRule extends DataEntity<PwFassetsnoRule> {

    private static final long serialVersionUID = 1L;
    private String fcid;       // 资产类型id
    private String prefix;        // 前缀
    private String format;        // 格式
    private String startNumber;        // 开始数值
    private String numberLen;        // 最小位数
    private int maxValue;       // 当前末位最大值
    private int version;          //版本号，用于实现乐观锁

    public PwFassetsnoRule() {
        super();
    }

    public PwFassetsnoRule(String id) {
        super(id);
    }

    public String getFcid() {
        return fcid;
    }

    public void setFcid(String fcid) {
        this.fcid = fcid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Length(min = 0, max = 255, message = "格式长度必须介于 0 和 255 之间")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Length(min = 0, max = 11, message = "层级长度必须介于 0 和 11 之间")
    public String getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    @Length(min = 0, max = 11, message = "格式长度必须介于 0 和 11 之间")
    public String getNumberLen() {
        return numberLen;
    }

    public void setNumberLen(String numberLen) {
        this.numberLen = numberLen;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}