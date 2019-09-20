/**
 *
 */
package com.oseasy.com.pcore.modules.sys.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.IdSnow;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.excel.fieldtype.RoleListType;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.vo.IAuser;
import com.oseasy.util.common.utils.Collections3;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.excel.annotation.ExcelField;

/**
 * 用户Entity
 */
public class User extends UserEntity<User> implements IidEntity, IAuser, Serializable {
    public static final String DICT_TECHNOLOGY_FIELD = "technology_field";
    private static final long serialVersionUID = 1L;
	public static final String PSW_MW = "psw_mw";
	public static final String PSW_MD5 = "psw_md5";
	public static final String TENANT_ID = "tenant.curid";
	private List<String> ids;
	private Office company; // 归属公司
	private Office office; // 归属 学院
	private String officeId; //学院id
	private String loginName;// 登录名
	private String password;// 密码
	private String no; // 工号 学号
	private String name; // 姓名
	private String email; // 邮箱
	private String phone; // 电话
	private String mobile; // 手机
	private String userType;// 用户类型 1学生 2导师
	private String loginIp; // 最后登陆IP
	private Date loginDate; // 最后登陆日期
	private String loginFlag; // 是否允许登陆
	private String photo; // 头像

	private String oldLoginName;// 原登录名
	private String newPassword; // 新密码

	private String oldLoginIp; // 上次登陆IP
	private Date oldLoginDate; // 上次登陆日期

	private String systemType; // 允许登陆的系统类型 0 前台 1后台

	private Role role; // 根据角色查询用户条件
	private List<String> roleBizTypes; // 根据角色类型查询用户条件

	private String degree;// 学位； 1：学士2：硕士3：博士
	private String education;// 学历
	private String area;// 地区
	private String sex; // 0 女 1 男
	private Date birthday;// 生日
	private String professional;// 专业
	private String school;// 高校
	private String professionalName;// 专业
	private Office subject;// 专业
	private String domain;// 擅长/技术领域
	private String idType;// 证件
	private String national;// 民族
	private String political;// 政治面貌
	private String country;// 国家
	private String idNumber;// 身份证
	private String qq; // qq
	private String source;//来源 1-导入
	private String passc;//密码状态 1-需要修改
	private List<String> domainIdList;//
	private String domainlt;// 擅长/技术领域,显示列表时用
	private String residence;// 户籍
	private String introduction;// 简介
	private String roleId;// 根据角色查询时用
	private String postCode;// 邮编

	private String teacherType;// 导师来源
	private String stringIds;
	private String age;
	private String likes; // 点赞量
	private String views; // 浏览量
	private String curJoin;// 当前在研，显示
	private ArrayList<HashMap<String,String>> curJoinParam;//当前在研查询条件
	private List<String> curJoinStr;//当前在研查询条件,接收页面传值
	private String currState;//现状 查询条件
	private String currStateStr;//列表显示

	private String queryStr;//关键字

	private List<String> dictList;//查询条件 领域

	private String auth;//用户最高权限
	private String regval;// 初始化规则表达式(名称|登录名|默认密码)

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public List<String> getDictList() {
		return dictList;
	}

	public void setDictList(List<String> dictList) {
		this.dictList = dictList;
	}

	public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

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

	public ArrayList<HashMap<String,String>> getCurJoinParam() {
		if (curJoinParam!=null) {
			return curJoinParam;
		}
		if (curJoinStr==null||curJoinStr.size()==0) {
			return null;
		}
		ArrayList<HashMap<String,String>> l=new ArrayList<HashMap<String,String>>();
		for(String s:curJoinStr) {
			if (StringUtil.isNotEmpty(s)) {
				HashMap<String,String> map=new HashMap<String,String>();
				String[] ss=s.split("-");
				map.put("pType", ss[0]);
				map.put("psType", ss[1]);
				l.add(map);
			}
		}
		if (l.size()==0) {
			return null;
		}
		return l;
	}

	public String getStringIds() {
		return stringIds;
	}

	public void setStringIds(String stringIds) {
		this.stringIds = stringIds;
	}

	public void setCurJoinParam(ArrayList<HashMap<String,String>> curJoinParam) {
		this.curJoinParam = curJoinParam;
	}

	public List<String> getCurJoinStr() {
		return curJoinStr;
	}

	public void setCurJoinStr(List<String> curJoinStr) {
		this.curJoinStr = curJoinStr;
	}

	public Office getSubject() {
		return subject;
	}

	public String getCurJoin() {
		return curJoin;
	}

	public String getRegval() {
		return regval;
	}

	public void setRegval(String regval) {
		this.regval = regval;
	}

	public void setCurJoin(String curJoin) {
		this.curJoin = curJoin;
	}

	public List<String> getRoleBizTypes() {
		return roleBizTypes;
	}

	public void setRoleBizTypes(List<String> roleBizTypes) {
		this.roleBizTypes = roleBizTypes;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public void setSubject(Office subject) {
		this.subject = subject;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPassc() {
		return passc;
	}

	public void setPassc(String passc) {
		this.passc = passc;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getAge() {
		if (birthday != null) {
			Date date = new Date();
			long day = (date.getTime() - birthday.getTime()) / (24 * 60 * 60 * 1000) + 1;
			String year = new DecimalFormat("#").format(day / 365f);
			return year;
		}

		return "";
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}

	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getProfessional() {
		return professional;
	}

	public void setProfessional(String professional) {
		this.professional = professional;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainlt() {
		if (StringUtils.isNotBlank(domain)) {
			String domainNames = DictUtils.getDictLabels(domain, DICT_TECHNOLOGY_FIELD, "未知");
			return domainNames;
		}
		return domainlt;
	}

	public void setDomainlt(String domainlt) {
		this.domainlt = domainlt;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}


	public User() {
		super();
		this.loginFlag = Const.YES;
	}

	public User(String id) {
		super(id);
	}

	public User(String id, String loginName) {
		super(id);
		this.loginName = loginName;
	}

	public User(Role role) {
		super();
		this.role = role;
	}

	public User(String id, Role role) {
    super();
    this.id = id;
    this.role = role;
  }

  public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	// @SupCol(isUnique="true", isHide="true")
	// @ExcelField(title="ID", type=1, align=2, sort=1)
	// public String getId() {
	// return id;
	// }

	@JsonIgnore
	@NotNull(message = "归属公司不能为空")
	@ExcelField(title = "归属公司", align = 2, sort = 20)
	public Office getCompany() {
		return company;
	}

	@Transient
    public String getCompanyName() {
        if((company != null) && StringUtil.isNotEmpty(company.getName())){
            return company.getName();
        }
        return null;
    }

    @Transient
    public String getCompanyId() {
        if((company != null) && StringUtil.isNotEmpty(company.getId())){
            return company.getId();
        }
        return null;
    }

	public void setCompany(Office company) {
		this.company = company;
	}

	@JsonIgnore
	@NotNull(message = "归属部门不能为空")
	@ExcelField(title = "归属部门", align = 2, sort = 25)
	public Office getOffice() {
		return office;
	}

	@Transient
	public String getOfficeName() {
	    if((office != null) && StringUtil.isNotEmpty(office.getName())){
	        return office.getName();
	    }
	    return null;
	}

	@Transient
	public String getOfficeId() {
	    if((office != null) && StringUtil.isNotEmpty(office.getId())){
	        return office.getId();
	    }
		if(StringUtil.isNotEmpty(officeId)){
			return officeId;
		}
	    return null;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min = 1, max = 100, message = "登录名长度必须介于 1 和 100 之间")
	@ExcelField(title = "登录名", align = 2, sort = 30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min = 1, max = 100, message = "密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min = 1, max = 100, message = "姓名长度必须介于 1 和 100 之间")
	@ExcelField(title = "姓名", align = 2, sort = 40)
	public String getName() {
		return name;
	}

	@Length(min = 1, max = 100, message = "工号长度必须介于 1 和 100 之间")
	@ExcelField(title = "工号", align = 2, sort = 45)
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Email(message = "邮箱格式不正确")
	@Length(min = 0, max = 200, message = "邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title = "邮箱", align = 1, sort = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min = 0, max = 200, message = "电话长度必须介于 1 和 200 之间")
	@ExcelField(title = "电话", align = 2, sort = 60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min = 0, max = 200, message = "手机长度必须介于 1 和 200 之间")
	@ExcelField(title = "手机", align = 2, sort = 70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@ExcelField(title = "备注", align = 1, sort = 900)
	public String getRemarks() {
		return remarks;
	}

	@Length(min = 0, max = 100, message = "用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title = "用户类型", align = 2, sort = 80, dictType = "sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@ExcelField(title = "创建时间", type = 0, align = 1, sort = 90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title = "最后登录IP", type = 1, align = 1, sort = 100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title = "最后登录日期", type = 1, align = 1, sort = 110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null) {
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null) {
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = Lists.newArrayList();
		if(StringUtil.checkEmpty(this.roleList)){
			this.roleList = Lists.newArrayList();
		}
		for (Role role : this.roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = Lists.newArrayList();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}

	public List<String> getDomainIdList() {
		if (StringUtils.isNotBlank(domain)) {
			String[] domainArray = StringUtils.split(domain, StringUtil.DOTH);
			domainIdList = Lists.newArrayList();
			for (String id : domainArray) {
				domainIdList.add(id);
			}
		}

		return domainIdList;
	}

	public void setDomainIdList(List<String> domainIdList) {
		if (domainIdList != null && domainIdList.size() > 0) {
			StringBuffer strbuff = new StringBuffer();
			for (String domainId : domainIdList) {
				strbuff.append(domainId);
				strbuff.append(StringUtil.DOTH);
			}
			setDomain(strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH)));
		}
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		if(StringUtil.checkEmpty(this.roleList)){
			this.roleList = Lists.newArrayList();
		}
		return Collections3.extractToString(roleList, "name", ",");
	}

	/**
	 * 处理登陆名\限制登陆名的长度
	 * @param user User
	 */
	public static User dealLoginName(User user) {
		if((user != null) && StringUtil.isNotEmpty(user.getLoginName())){
			if(StringUtil.isChinese(user.getLoginName()) && (StringUtil.length(user.getLoginName()) > 10)){
				user.setLoginName(StringUtil.substring(user.getLoginName(), 0, 10));
			}else if(StringUtil.length(user.getLoginName()) > 20){
				user.setLoginName(StringUtil.substring(user.getLoginName(), 0, 20));
			}
		}
		return user;
	}

	/**
	 * 处理用户组织结构
	 * @param user User
	 */
	public static User dealOffice(User user) {
		if((user != null) && (StringUtil.isEmpty(user.getCompanyId()) || StringUtil.isEmpty(user.getOfficeId()))){
			String tenantId = TenantConfig.getCacheTenant();
			if(StringUtil.isEmpty(user.getCompanyId())){
				user.setCompany(OfficeUtils.getRootByTenant(TenantConfig.getCacheTenant()));
			}

			if(StringUtil.isEmpty(user.getOfficeId()) && StringUtil.isNotEmpty(user.getCompanyId())){
				user.setOffice(user.getCompany());
			}
		}
		return user;
	}


	public static User dealLoginNameAnNo(User user) {
		if(StringUtil.isEmpty(user.getId())) {
			if (StringUtil.isNotEmpty(user.getLoginName()) && StringUtil.isEmpty(user.getNo())) {
				user.setNo(user.getLoginName());
			}

			if (StringUtil.isNotEmpty(user.getMobile()) && StringUtil.isEmpty(user.getNo()) && StringUtil.isEmpty(user.getLoginName())) {
				user.setLoginName(StringUtil.EMPTY + new IdSnow(1).nextId());
			}

			if (StringUtil.isNotEmpty(user.getNo()) && StringUtil.isEmpty(user.getLoginName())) {
				user.setLoginName(user.getNo());
			}
		}
		return user;
	}

	/*****************************************************************************************
	 * 判断用户是否为管理员.
	 ****************************************************************************************/
	 /**
	 * 是否为超级管理员. 1、user.id 为 1; 2、role.id 为 1;
	 * @return boolean
	 */
	public boolean getAdmin() {
		return isSuper(this);
	}

	/**
	 * 是否为系统管理员.
	 * 1、role.id 为 10;
	 * @return boolean
	 */
	public boolean getSysAdmin() {
		return isAdmin(this);
	}

	/**
	 * 是否为系统运营管理员.
	 * 1、role.id 为 10;
	 * @return boolean
	 */
	public boolean getSysAdmyw() {
		return isAdmyw(this);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}