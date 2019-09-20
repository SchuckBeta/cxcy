package com.oseasy.pw.modules.pw.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 耗材使用记录Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwCosMaterialUhistory extends DataEntity<PwCosMaterialUhistory> {

	private static final long serialVersionUID = 1L;
	private String cmid;		// 耗材编号
	private String ayname;		// 申领人姓名
	private Date time;		// 申领日期
	private String phone;		// 电话
	private String mobile;		// 手机
	private String num;		// 数量
	private String beginAyname;		// 开始 申领人姓名
	private String endAyname;		// 结束 申领人姓名

	public PwCosMaterialUhistory() {
		super();
	}

	public PwCosMaterialUhistory(String id){
		super(id);
	}

	@Length(min=1, max=64, message="耗材编号长度必须介于 1 和 64 之间")
	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	@Length(min=0, max=255, message="申领人姓名长度必须介于 0 和 255 之间")
	public String getAyname() {
		return ayname;
	}

	public void setAyname(String ayname) {
		this.ayname = ayname;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Length(min=0, max=200, message="电话长度必须介于 0 和 200 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 0 和 200 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=11, message="数量长度必须介于 0 和 11 之间")
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getBeginAyname() {
		return beginAyname;
	}

	public void setBeginAyname(String beginAyname) {
		this.beginAyname = beginAyname;
	}

	public String getEndAyname() {
		return endAyname;
	}

	public void setEndAyname(String endAyname) {
		this.endAyname = endAyname;
	}

}