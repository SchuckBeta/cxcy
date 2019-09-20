package com.oseasy.pie.modules.impdata.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义大赛信息错误数据Entity.
 * @author 自定义大赛信息错误数据
 * @version 2018-05-14
 */
public class ProModelGcontestError extends DataEntity<ProModelGcontestError> implements IitCheckEetyExt{

	private static final long serialVersionUID = 1L;
	private String impId;		// 导入信息表主键
	private String name;		// 项目名称
	private String year;		// 项目年份
	private String stage;//项目阶段
	private String type;		// 参赛类别
	private String groups;		// 参赛组别
	private String track;		// 赛道
	private String introduction;		// 项目简介
	private String hasfile;		// 是否有附件
	private String leader;		// 负责人姓名
	private String no;		// 负责人学号
	private String profes;		// 负责人专业
	private String enter;		// 负责人入学年份
	private String outy;		// 负责人毕业年份
	private String xueli;		// 负责人学历
	private String idnum;		// 负责人身份证号码
	private String mobile;		// 负责人手机号码
	private String email;		// 负责人邮箱
	private List<PmgMemsError> pmes=new ArrayList<PmgMemsError>();//组成员
	private List<PmgTeasError> ptes=new ArrayList<PmgTeasError>();//导师
	private boolean validName;//标识name是否有效
    private String domain;     // 团队成员及学号
    private String province;     // 团队成员及学号
    private String lschool;     // 团队成员及学号
    private String lyear;        // 入学时间
    private String gyear;        // 毕业时间
    private String members;     // 团队成员及学号
    private String teachers;        // 指导教师
    private String businfos;        // 工商信息

	public ProModelGcontestError() {
		super();
	}

	public String getLyear() {
        return lyear;
    }

    public void setLyear(String lyear) {
        this.lyear = lyear;
    }

    public String getBusinfos() {
        return businfos;
    }

    public void setBusinfos(String businfos) {
        this.businfos = businfos;
    }

    public String getGyear() {
        return gyear;
    }

    public void setGyear(String gyear) {
        this.gyear = gyear;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isValidName() {
		return validName;
	}

	public void setValidName(boolean validName) {
		this.validName = validName;
	}
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLschool() {
        return lschool;
    }

    public void setLschool(String lschool) {
        this.lschool = lschool;
    }

    public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public ProModelGcontestError(String id){
		super(id);
	}

	public List<PmgMemsError> getPmes() {
		return pmes;
	}

	public void setPmes(List<PmgMemsError> pmes) {
		this.pmes = pmes;
	}

	public List<PmgTeasError> getPtes() {
		return ptes;
	}

	public void setPtes(List<PmgTeasError> ptes) {
		this.ptes = ptes;
	}

	@Length(min=1, max=64, message="导入信息表主键长度必须介于 1 和 64 之间")
	public String getImpId() {
		return impId;
	}

	public void setImpId(String impId) {
		this.impId = impId;
	}

	@Length(min=0, max=128, message="项目名称长度必须介于 0 和 128 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=4, message="项目年份长度必须介于 0 和 4 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Length(min=0, max=128, message="参赛类别长度必须介于 0 和 128 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=128, message="参赛组别长度必须介于 0 和 128 之间")
	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	@Length(min=0, max=2000, message="项目简介长度必须介于 0 和 2000 之间")
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	@Length(min=0, max=1, message="是否有附件长度必须介于 0 和 1 之间")
	public String getHasfile() {
		return hasfile;
	}

	public void setHasfile(String hasfile) {
		this.hasfile = hasfile;
	}

	@Length(min=0, max=128, message="负责人姓名长度必须介于 0 和 128 之间")
	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	@Length(min=0, max=128, message="负责人学号长度必须介于 0 和 128 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=128, message="负责人专业长度必须介于 0 和 128 之间")
	public String getProfes() {
		return profes;
	}

	public void setProfes(String profes) {
		this.profes = profes;
	}

	@Length(min=0, max=128, message="负责人入学年份长度必须介于 0 和 128 之间")
	public String getEnter() {
		return enter;
	}

	public void setEnter(String enter) {
		this.enter = enter;
	}

	@Length(min=0, max=128, message="负责人毕业年份长度必须介于 0 和 128 之间")
	public String getOut() {
		return outy;
	}

	public void setOut(String out) {
		this.outy = out;
	}

	@Length(min=0, max=128, message="负责人学历长度必须介于 0 和 128 之间")
	public String getXueli() {
		return xueli;
	}

	public void setXueli(String xueli) {
		this.xueli = xueli;
	}

	@Length(min=0, max=50, message="负责人身份证号码长度必须介于 0 和 50 之间")
	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	@Length(min=0, max=128, message="负责人手机号码长度必须介于 0 和 128 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    @Length(min=0, max=128, message="负责人邮箱长度必须介于 0 和 128 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public static ProModel convertToProModel(ProModel pm, ProModelGcontestError pmer) {
        if(StringUtil.isNotEmpty(pmer.getType())){
            //pm.setType(pmer.getType());
        }
        if(StringUtil.isNotEmpty(pmer.getGroups())){
            pm.setLevel(pmer.getGroups());
        }
        if(StringUtil.isNotEmpty(pmer.getStage())){
            pm.setStage(pmer.getStage());
        }
        if(StringUtil.isNotEmpty(pmer.getIntroduction())){
            pm.setIntroduction(pmer.getIntroduction());
        }
        return pm;
    }

    public static User convertToUser(User leader, ProModelGcontestError pmer) {
        if(StringUtil.isNotEmpty(pmer.getNo())){
            leader.setNo(pmer.getNo());
        }
        if(StringUtil.isNotEmpty(pmer.getEmail())){
            leader.setEmail(pmer.getEmail());
        }
        if(StringUtil.isNotEmpty(pmer.getMobile())){
            leader.setMobile(pmer.getMobile());
        }
        if(StringUtil.isNotEmpty(pmer.getXueli())){
            leader.setEducation(pmer.getXueli());
        }
        //入学时间、毕业时间
        return leader;
    }
}