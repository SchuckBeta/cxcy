package com.oseasy.pie.modules.impdata.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;

import org.hibernate.validator.constraints.Length;

/**
 * 导入学生错误数据表Entity
 * @author 9527
 * @version 2017-05-16
 */
public class ConStuError extends DataEntity<ConStuError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String companyId;		// 归属公司
	private String office;		// 归属机构
	private String professional;		// 专业

	private String password;		// 密码
	private String no;		// 工号
	private String name;		// 姓名
	private String email;		// 邮箱
	private String phone;		// 电话
	private String mobile;		// 手机

	private String graduation;		// 毕业时间
	private String enterdate;		// 入学时间


	public ConStuError() {
		super();
	}

	public ConStuError(String id) {
		super(id);
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="归属公司长度必须介于 0 和 128 之间")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Length(min=0, max=128, message="专业长度必须介于 0 和 128 之间")
	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}


	@Length(min=1, max=128, message="密码长度必须介于 1 和 128 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=0, max=128, message="工号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=1, max=128, message="姓名长度必须介于 1 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=128, message="邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=128, message="电话长度必须介于 0 和 128 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=128, message="手机长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEnterdate() {
		return enterdate;
	}

	public void setEnterdate(String enterdate) {
		this.enterdate = enterdate;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}
}