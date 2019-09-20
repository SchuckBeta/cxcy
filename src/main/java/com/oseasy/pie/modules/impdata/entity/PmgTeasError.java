package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 自定义大赛导师信息错误数据Entity.
 * @author 自定义大赛导师信息错误数据
 * @version 2018-05-14
 */
public class PmgTeasError extends DataEntity<PmgTeasError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String pmgeId;		// pro_model_gcontest_error表主键
	private String name;		// 姓名
	private String no;		// 工号
	private String office;		// 学院
	private String enter;		// 入学年份
	private String mobile;		// 手机号码
	private String email;		// 邮箱
	private String zhicheng;		// 职称
	private String sort;		// 排序

	public PmgTeasError() {
		super();
	}

	public PmgTeasError(String id){
		super(id);
	}

	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=1, max=64, message="pro_model_gcontest_error表主键长度必须介于 1 和 64 之间")
	public String getPmgeId() {
		return pmgeId;
	}

	public void setPmgeId(String pmgeId) {
		this.pmgeId = pmgeId;
	}

	@Length(min=0, max=128, message="姓名长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=128, message="工号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="学院长度必须介于 0 和 128 之间")
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=128, message="入学年份长度必须介于 0 和 128 之间")
	public String getEnter() {
		return enter;
	}

	public void setEnter(String enter) {
		this.enter = enter;
	}

	@Length(min=0, max=128, message="手机号码长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=128, message="邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=128, message="职称长度必须介于 0 和 128 之间")
	public String getZhicheng() {
		return zhicheng;
	}

	public void setZhicheng(String zhicheng) {
		this.zhicheng = zhicheng;
	}

	@Length(min=0, max=3, message="排序长度必须介于 0 和 3 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}