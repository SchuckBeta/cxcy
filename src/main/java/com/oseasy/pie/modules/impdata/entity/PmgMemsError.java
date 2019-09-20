package com.oseasy.pie.modules.impdata.entity;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 自定义大赛成员信息错误数据Entity.
 * @author 自定义大赛成员信息错误数据
 * @version 2018-05-14
 */
public class PmgMemsError extends DataEntity<PmgMemsError> {

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String pmgeId;		// pro_model_gcontest_error表主键
	private String name;		// 姓名
	private String no;		// 学号
	private String profes;		// 专业
	private String enter;		// 入学年份
	private String outy;		// 毕业年份
	private String xueli;		// 学历
	private String idnum;		// 身份证号码
	private String mobile;		// 手机号码
	private String email;		// 邮箱
	private String sort;		// 排序

	public PmgMemsError() {
		super();
	}

	public PmgMemsError(String id){
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

	@Length(min=0, max=128, message="学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="专业长度必须介于 0 和 128 之间")
	public String getProfes() {
		return profes;
	}

	public void setProfes(String profes) {
		this.profes = profes;
	}

	@Length(min=0, max=128, message="入学年份长度必须介于 0 和 128 之间")
	public String getEnter() {
		return enter;
	}

	public void setEnter(String enter) {
		this.enter = enter;
	}

	@Length(min=0, max=128, message="毕业年份长度必须介于 0 和 128 之间")
	public String getOut() {
		return outy;
	}

	public void setOut(String out) {
		this.outy = out;
	}

	@Length(min=0, max=128, message="学历长度必须介于 0 和 128 之间")
	public String getXueli() {
		return xueli;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	@Length(min=0, max=50, message="身份证号码长度必须介于 0 和 50 之间")
	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
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

	@Length(min=0, max=3, message="排序长度必须介于 0 和 3 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}