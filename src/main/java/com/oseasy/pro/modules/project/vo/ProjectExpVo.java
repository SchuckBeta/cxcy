package com.oseasy.pro.modules.project.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * Created by zhangzheng on 2017/4/13.
 * 项目经历
 */
public class ProjectExpVo implements Serializable{
	private static final long serialVersionUID = 1L;
    private String id; //项目id
    private String name; //项目名称
    private String year; //年份
    private String introduction; //介绍
    private Date startDate; //项目开始时间
    private Date endDate; //项目结束时间
    private String roleName; //担任角色
    private String level;  //项目评级
    private String result; //项目结果
    private String proName; //项目类别
    private String finish;//0-进行中，1已结束
    private String leaderId;
    private String userType;

	private String actywId;
	private SysAttachment logo;
	private Date beginDate;//项目周期开始时间


	private List<User> userList = Lists.newArrayList();;

	public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public SysAttachment getLogo() {
        return logo;
    }

    public void setLogo(SysAttachment logo) {
        this.logo = logo;
    }

    public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getFinish() {
		return finish;
	}
	public void setFinish(String finish) {
		this.finish = finish;
	}
    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

	public String getActywId() {
		return actywId;
	}

	public void setActywId(String actywId) {
		this.actywId = actywId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
}
