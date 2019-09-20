package com.oseasy.dr.modules.dr.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 门禁预警Entity.
 * @author zy
 * @version 2018-04-13
 */
public class DrCardRule extends DataEntity<DrCardRule> {

	private static final long serialVersionUID = 1L;
	private String isWarm;		// 入驻时间到期预警 1：是，0不是
	private String warmTime;		// 距离入驻有效期多少天自动预警（天）
	private String isEnter;		// 多长时间未进入基地预警 1：是，0不是
	private String enterTime;		// 未进入基地多长时间（天）
	private String isOut;		// 多长时间未出预警 1：是，0不是
	private String outTime;		// 进入基地多长时间未出自动预警（小时）
	private String beginTime;		// 记录门禁进入开始（开始时间）
	private String endTime;		// 记录门禁进入结束（结束时间）
	private String isOpen;      // 记录门禁设备全开/关(1:是，0:不是)
    private String isTimeLimit;     // 是否时间限制

	public DrCardRule() {
		super();
	}

	public DrCardRule(String id){
		super(id);
	}

	@Length(min=0, max=2, message="入驻时间到期预警 1：是，0不是长度必须介于 0 和 2 之间")
	public String getIsWarm() {
		return isWarm;
	}

	public void setIsWarm(String isWarm) {
		this.isWarm = isWarm;
	}

	@Length(min=0, max=64, message="距离入驻有效期多少天自动预警（天）长度必须介于 0 和 64 之间")
	public String getWarmTime() {
		return warmTime;
	}

	public void setWarmTime(String warmTime) {
		this.warmTime = warmTime;
	}

	public String getIsTimeLimit() {
        return isTimeLimit;
    }

    public void setIsTimeLimit(String isTimeLimit) {
        this.isTimeLimit = isTimeLimit;
    }

    @Length(min=0, max=2, message="多长时间未进入基地预警 1：是，0不是长度必须介于 0 和 2 之间")
	public String getIsEnter() {
		return isEnter;
	}

	public void setIsEnter(String isEnter) {
		this.isEnter = isEnter;
	}

	@Length(min=0, max=64, message="未进入基地多长时间（天）长度必须介于 0 和 64 之间")
	public String getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}

	@Length(min=0, max=2, message="多长时间未出预警 1：是，0不是长度必须介于 0 和 2 之间")
	public String getIsOut() {
		return isOut;
	}

	public void setIsOut(String isOut) {
		this.isOut = isOut;
	}

	@Length(min=0, max=64, message="进入基地多长时间未出自动预警（小时）长度必须介于 0 和 64 之间")
	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	@Length(min=0, max=10, message="记录门禁进入开始（开始时间）长度必须介于 0 和 10 之间")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	@Length(min=0, max=10, message="记录门禁进入结束（结束时间）长度必须介于 0 和 10 之间")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Length(min=0, max=2, message="门禁设备开启状态长度必须介于 0 和 2 之间")
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
}