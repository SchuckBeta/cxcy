package com.oseasy.pie.modules.impdata.vo;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.vo.DrCardType;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 卡导入错误信息Entity.
 * @author chenh
 * @version 2018-07-19
 */
public class DrCardError extends DataEntity<DrCardError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String no;		// 门禁卡卡号
	private Date expiry;		// 有效期
	private Date startDate;		// 开卡日期
	private String tmpNo;		// 零时卡使用者 学号
	private String tmpName;		// 零时卡使用者 姓名
	private String tmpTel;		// 零时卡使用电话
	private String tmpSex;		// 零时卡性别：1:男0:女
	private String tmpOffice;		// 零时卡学院专业
  private String name;        // 姓名
  private String mobile;      // 手机
  private String sex;     // 1:男0:女

	//学生信息
    private String companyId;       // 归属公司
    private String loginName;       // 登录名
    private String password;        // 密码
    private String email;       // 邮箱
    private String phone;       // 电话
    private String userType;        // 用户类型
    private String loginFlag;       // 是否可登录
    private String idType;      // 证件
    private String national;        // 民族
    private String political;       // 政治面貌
    private String postCode;        // 邮编
    private String birthday;        // 出生年月
    private String country;     // 国家
    private String area;        // 地区
    private String domain;      // 擅长/技术领域
    private String degree;      // 学位
    private String education;       // 学历
    private String idNo;        // 证件号
    private String projectExperience;       // 项目经历
    private String contestExperience;       // 大赛经历
    private String award;       // 获奖作品
    private String isOpen;      // 是否公开
    private String graduation;      // 毕业时间
    private String enterDate;       // 入学时间
    private String instudy;     // 在读学位
    private String temporaryDate;       // 休学时间
    private String address;     // 联系地址
    private String currState;       // 现状

    //机构信息
    private String office;      // 学院名称
    private String professional;        // 专业名称
    private String tClass;      // 班级

    private User curUser;      // 用户
    private Office curOffice;      // 学院名称
    private Office curProfessional;        // 专业名称

    public User getCurUser() {
        return curUser;
    }

    public void setCurUser(User curUser) {
        this.curUser = curUser;
    }

    public Office getCurOffice() {
        return curOffice;
    }

    public void setCurOffice(Office curOffice) {
        this.curOffice = curOffice;
    }

    public Office getCurProfessional() {
        return curProfessional;
    }

    public void setCurProfessional(Office curProfessional) {
        this.curProfessional = curProfessional;
    }

    @Length(min=0, max=128, message="归属公司长度必须介于 0 和 128 之间")
    public String getCompanyId() {
        return companyId;
    }

    public String getName() {
        if(StringUtil.isEmpty(this.name) && StringUtil.isNotEmpty(this.tmpName)){
            this.name = this.tmpName;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        if(StringUtil.isEmpty(this.mobile) && StringUtil.isNotEmpty(this.tmpTel)){
            this.mobile = this.tmpTel;
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        if(StringUtil.isEmpty(this.sex) && StringUtil.isNotEmpty(this.tmpSex)){
            this.sex = this.tmpSex;
        }
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    @Length(min=1, max=128, message="登录名长度必须介于 1 和 128 之间")
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Length(min=1, max=128, message="密码长度必须介于 1 和 128 之间")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Length(min=0, max=128, message="用户类型长度必须介于 0 和 128 之间")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Length(min=0, max=64, message="是否可登录长度必须介于 0 和 64 之间")
    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @Length(min=0, max=128, message="证件长度必须介于 0 和 128 之间")
    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    @Length(min=0, max=128, message="民族长度必须介于 0 和 128 之间")
    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    @Length(min=0, max=128, message="政治面貌长度必须介于 0 和 128 之间")
    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    @Length(min=0, max=128, message="邮编长度必须介于 0 和 128 之间")
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Length(min=0, max=128, message="出生年月长度必须介于 0 和 128 之间")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Length(min=0, max=128, message="国家长度必须介于 0 和 128 之间")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Length(min=0, max=128, message="地区长度必须介于 0 和 128 之间")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Length(min=0, max=1024, message="擅长/技术领域长度必须介于 0 和 1024 之间")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Length(min=0, max=128, message="学位长度必须介于 0 和 128 之间")
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Length(min=0, max=128, message="学历长度必须介于 0 和 128 之间")
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Length(min=0, max=128, message="证件号长度必须介于 0 和 128 之间")
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getProjectExperience() {
        return projectExperience;
    }

    public void setProjectExperience(String projectExperience) {
        this.projectExperience = projectExperience;
    }

    public String getContestExperience() {
        return contestExperience;
    }

    public void setContestExperience(String contestExperience) {
        this.contestExperience = contestExperience;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    @Length(min=0, max=128, message="是否公开长度必须介于 0 和 128 之间")
    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    @Length(min=0, max=128, message="毕业时间长度必须介于 0 和 128 之间")
    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    @Length(min=0, max=128, message="入学时间长度必须介于 0 和 128 之间")
    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    @Length(min=0, max=128, message="班级长度必须介于 0 和 128 之间")
    public String getTClass() {
        return tClass;
    }

    public void setTClass(String tClass) {
        this.tClass = tClass;
    }

    @Length(min=0, max=128, message="在读学位长度必须介于 0 和 128 之间")
    public String getInstudy() {
        return instudy;
    }

    public void setInstudy(String instudy) {
        this.instudy = instudy;
    }

    @Length(min=0, max=128, message="休学时间长度必须介于 0 和 128 之间")
    public String getTemporaryDate() {
        return temporaryDate;
    }

    public void setTemporaryDate(String temporaryDate) {
        this.temporaryDate = temporaryDate;
    }

    @Length(min=0, max=512, message="联系地址长度必须介于 0 和 512 之间")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Length(min=0, max=128, message="现状长度必须介于 0 和 128 之间")
    public String getCurrState() {
        return currState;
    }

    public void setCurrState(String currState) {
        this.currState = currState;
    }

	public DrCardError() {
		super();
	}

	public DrCardError(String id){
		super(id);
	}
	public DrCardError(String id, String impId){
	    super(id);
	    this.impId = impId;
	}


	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=64, message="门禁卡卡号长度必须介于 0 和 64 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Length(min=0, max=64, message="零时卡使用者 学号长度必须介于 0 和 64 之间")
	public String getTmpNo() {
		return tmpNo;
	}

	public void setTmpNo(String tmpNo) {
		this.tmpNo = tmpNo;
	}

	@Length(min=0, max=64, message="零时卡使用者 姓名长度必须介于 0 和 64 之间")
	public String getTmpName() {
	    if(StringUtil.isEmpty(this.tmpName) && StringUtil.isNotEmpty(this.name)){
            this.tmpName = this.name;
        }
		return tmpName;
	}

	public void setTmpName(String tmpName) {
		this.tmpName = tmpName;
	}

	@Length(min=0, max=64, message="零时卡使用电话长度必须介于 0 和 64 之间")
	public String getTmpTel() {
	    if(StringUtil.isEmpty(this.tmpTel) && StringUtil.isNotEmpty(this.mobile)){
            this.tmpTel = this.mobile;
        }
		return tmpTel;
	}

	public void setTmpTel(String tmpTel) {
		this.tmpTel = tmpTel;
	}

	@Length(min=0, max=64, message="零时卡性别：1:男0:女长度必须介于 0 和 64 之间")
	public String getTmpSex() {
	    if(StringUtil.isEmpty(this.tmpSex) && StringUtil.isNotEmpty(this.sex)){
            this.tmpSex = this.sex;
        }
		return tmpSex;
	}

	public void setTmpSex(String tmpSex) {
		this.tmpSex = tmpSex;
	}

	@Length(min=0, max=64, message="零时卡学院专业长度必须介于 0 和 64 之间")
	public String getTmpOffice() {
		return tmpOffice;
	}

	public void setTmpOffice(String tmpOffice) {
		this.tmpOffice = tmpOffice;
	}

	/**
     * 转换为卡对象.
     * @param cerror
     * @param parent
     * @return
     */
    public static DrCard convertToDrCard(DrCardError cerror, DrCard card) {
        if(card == null){
            card = new DrCard();
        }
        card.setCardType(DrCardType.TEMP.getKey());
        card.setNo(cerror.getNo());
        card.setPassword(cerror.getPassword());
        card.setExpiry(cerror.getExpiry());
        card.setStatus(DrCstatus.DC_TEMP.getKey());
        card.setDealStatus(DrCdealStatus.DCD_NORMAL.getKey());
        if(cerror.getCurUser() != null){
            card.setUser(cerror.getCurUser());
            card.setTmpName(cerror.getCurUser().getName());
            card.setTmpNo(cerror.getCurUser().getNo());
            card.setTmpSex(cerror.getCurUser().getSex());
            card.setTmpTel(cerror.getCurUser().getMobile());
            if(cerror.getCurUser().getOffice() != null){
                card.setTmpOfficeXy(cerror.getCurUser().getOffice().getName());
            }
            if(cerror.getCurUser().getOffice() != null){
                card.setTmpOfficeXyId(cerror.getCurUser().getOffice().getId());
            }
            if(cerror.getCurProfessional() != null){
                card.setTmpOfficeZy(cerror.getCurProfessional().getName());
            }
            if(cerror.getCurProfessional() != null){
                card.setTmpOfficeZyId(cerror.getCurProfessional().getId());
            }
            if((card.getTmpOfficeXy() != null) && (card.getTmpOfficeZy() != null)){
                card.setTmpOffice(card.getTmpOfficeXy() + StringUtil.LINE + card.getTmpOfficeZy());
            }
        }
        return card;
    }

    /**
     * 转换为用户.
     * @param cerror
     * @param parent
     * @return
     */
    public static User convertToUser(DrCardError cerror, User user) {
        if(user == null){
            user = new User();
        }
        if(cerror.getCurOffice() != null){
            user.setOffice(cerror.getCurOffice());
        }
        if(cerror.getCurProfessional() != null){
        	user.setProfessional(cerror.getCurProfessional().getId());
        }
        user.setNo(cerror.getTmpNo());
        user.setSex((("男").equals(cerror.getTmpSex()) || (("1").equals(cerror.getTmpSex())))?"1":"0");
        user.setName(cerror.getTmpName());
        user.setMobile(cerror.getTmpTel());
        user.setEmail(cerror.getEmail());
        return user;
    }
    /**
     * 转换为学院.
     * @param cerror
     * @param parent
     * @return
     */
    public static Office convertToOfficeXY(DrCardError cerror, Office office) {
        if(office == null){
            office = new Office();
        }
        office.setParent(new Office(CoreIds.NCE_SYS_OFFICE_TOP.getId()));
        office.setName(cerror.getOffice());
        office.setType("4");
        office.setGrade("2");
        return office;
    }

    /**
     * 转换为专业.
     * @param cerror
     * @param parent
     * @return
     */
    public static Office convertToOfficeZY(DrCardError cerror, Office parent, Office office) {
        if(parent == null){
            return null;
        }

        if(office == null){
            office = new Office();
        }
        office.setParent(parent);
        office.setName(cerror.getProfessional());
        office.setType("5");
        office.setGrade("3");
        return office;
    }
}