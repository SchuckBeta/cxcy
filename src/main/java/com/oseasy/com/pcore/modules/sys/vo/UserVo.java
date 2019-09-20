/**
 *
 */
package com.oseasy.com.pcore.modules.sys.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 用户Entity
 *
 *
 */
public class UserVo extends DataEntity<UserVo> {

	private static final long serialVersionUID = 1L;
	private String userId;
	private String ids;
	private String[] idsArr;
	private String domain;
	private String photo;
	private String office; // 归属 学院
	private String no; // 工号 学号
	private String name; // 姓名
	private String email; // 邮箱
	private String phone; // 电话
	private String mobile; // 手机
	private String userType;// 用户类型 1学生 2导师 3 教学秘书 4院级专家 5校级专家 6校级管理员
	private String degree;// 学位； 1：学士2：硕士3：博士
	private String education;// 学历
	private String instudy;
	private String postTitle;
	private String professional;// 专业
	private String teacherType;// 导师来源
	private String curJoin;// 当前在研，显示
	private ArrayList<HashMap<String, String>> curJoinParam;// 当前在研查询条件
	private List<String> curJoinStr;// 当前在研查询条件,接收页面传值
	private String currState;// 现状 查询条件
	private String currStateStr;// 列表显示

	public String getCurrStateStr() {
		return currStateStr;
	}

	public void setCurrStateStr(String currStateStr) {
		this.currStateStr = currStateStr;
	}

	public String getCurrState() {
		return currState;
	}

	public void setCurrState(String currState) {
		this.currState = currState;
	}

	public ArrayList<HashMap<String, String>> getCurJoinParam() {
		if (curJoinParam != null) {
			return curJoinParam;
		}
		if (curJoinStr == null || curJoinStr.size() == 0) {
			return null;
		}
		ArrayList<HashMap<String, String>> l = new ArrayList<HashMap<String, String>>();
		for (String s : curJoinStr) {
			if (StringUtil.isNotEmpty(s)) {
				HashMap<String, String> map = new HashMap<String, String>();
				String[] ss = s.split("-");
				map.put("pType", ss[0]);
				map.put("psType", ss[1]);
				l.add(map);
			}
		}
		if (l.size() == 0) {
			return null;
		}
		return l;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setCurJoinParam(ArrayList<HashMap<String, String>> curJoinParam) {
		this.curJoinParam = curJoinParam;
	}

	public List<String> getCurJoinStr() {
		return curJoinStr;
	}

	public void setCurJoinStr(List<String> curJoinStr) {
		this.curJoinStr = curJoinStr;
	}

	public String getCurJoin() {
		return curJoin;
	}

	public void setCurJoin(String curJoin) {
		this.curJoin = curJoin;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getInstudy() {
		return instudy;
	}

	public void setInstudy(String instudy) {
		this.instudy = instudy;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String[] getIdsArr() {
		if (StringUtil.isNotEmpty(ids)) {
			return ids.split(",");
		}
		return idsArr;
	}
	public void setIdsArr(String[] idsArr) {
		this.idsArr = idsArr;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Override
	public String getId() {
		if(StringUtil.isEmpty(this.id) && StringUtil.isNotEmpty(this.userId)){
			this.id = this.userId;
		}
		return this.id;
	}

	@Override
	public void setId(String id) {
		super.setId(id);
	}
}