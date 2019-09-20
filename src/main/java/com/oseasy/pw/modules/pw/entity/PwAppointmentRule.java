package com.oseasy.pw.modules.pw.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Arrays;
import java.util.List;

/**
 * 预约规则Entity.
 *
 * @author chenh
 * @version 2017-11-26
 */
public class PwAppointmentRule extends DataEntity<PwAppointmentRule> {

    private static final long serialVersionUID = 1L;
    private String isAuto;        // 是否自动审核
    private String autoTime;        // 预约自动审核时间（分钟）
    private String afterDays;        // 提前多少天可以预约

    private String isTimeAuto;       // 是否开放预约时间
    private String beginTime;        // 预约开始时间
    private String endTime;            // 预约结束

    private String isAppDay;            // 预约时间
    private List<String> isAppDayList;            // 预约时间

    public String getIsAppDay() {
        return isAppDay;
    }

    public void setIsAppDay(String isAppDay) {
        this.isAppDay = isAppDay;
    }

    public void setIsAppDayList(List<String> isAppDayList) {
        if (isAppDayList != null && isAppDayList.size() > 0) {
            StringBuffer strbuff = new StringBuffer();
            for (String domainId : isAppDayList) {
                strbuff.append(domainId);
                strbuff.append(StringUtil.DOTH);
            }
            String domainIds = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
            setIsAppDay(domainIds);
        }
    }

    public List<String> getIsAppDayList() {
        if (StringUtils.isNotBlank(isAppDay)) {
            isAppDayList = Arrays.asList(StringUtils.split(isAppDay, StringUtil.DOTH));
        }
        return isAppDayList;
    }

    public String getIsTimeAuto() {
        return isTimeAuto;
    }

    public void setIsTimeAuto(String isTimeAuto) {
        this.isTimeAuto = isTimeAuto;
    }

    public PwAppointmentRule() {
        super();
    }

    public PwAppointmentRule(String id) {
        super(id);
    }

    @Length(min = 0, max = 1, message = "是否自动审核长度必须介于 0 和 1 之间")
    public String getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto;
    }

    public String getAutoTime() {
        return autoTime;
    }

    public void setAutoTime(String autoTime) {
        this.autoTime = autoTime;
    }

    public String getAfterDays() {
        return afterDays;
    }

    public void setAfterDays(String afterDays) {
        this.afterDays = afterDays;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}