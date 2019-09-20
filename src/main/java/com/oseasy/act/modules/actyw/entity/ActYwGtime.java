package com.oseasy.act.modules.actyw.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 时间和组件关联关系Entity.
 * @author zy
 * @version 2017-06-27
 */
public class ActYwGtime extends DataEntity<ActYwGtime> {

	private static final long serialVersionUID = 1L;
	private String grounpId;		// 节点组id
	private String gnodeId;		// 节点id
	private String projectId;		// 项目id
	private String yearId;			// 年份id
	private String gnodeName;		// 节点名称
	@JsonIgnore
	private ActYwGnode gnode ;     //
	private Date beginDate;		// 节点开始时间
    private Date endDate;   // 节点结束时间
    private String status;    // 状态：1：生效 0：失效
    private Float rate;   // 通过率
    private String rateStatus;    //  通过率开关状态：1：生效 0：失效
    private Boolean hasTpl;    // Excel模板路径
    private String excelTplPath;    // Excel模板路径
    private String excelTplClazz;    // Excel模板类

	public ActYwGtime() {
		super();
	}

	public ActYwGtime(String id) {
		super(id);
	}


	public ActYwGtime(String projectId, String yearId) {
        super();
        this.projectId = projectId;
        this.yearId = yearId;
    }

	public String getGnodeName() {
		if(StringUtil.isEmpty(this.gnodeName) && (this.gnode != null)){
			this.gnodeName = this.gnode.getName();
		}
		return gnodeName;
	}

	public void setGnodeName(String gnodeName) {
		this.gnodeName = gnodeName;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public ActYwGnode getGnode() {
		return gnode;
	}

	public void setGnode(ActYwGnode gnode) {
		this.gnode = gnode;
	}

	@Length(min=1, max=64, message="节点组id长度必须介于 1 和 64 之间")
	public String getGrounpId() {
		return grounpId;
	}

	public void setGrounpId(String grounpId) {
		this.grounpId = grounpId;
	}

	@Length(min=1, max=64, message="节点id长度必须介于 1 和 64 之间")
	public String getGnodeId() {
	    if(StringUtil.isEmpty(this.gnodeId) && (this.gnode != null)){
	        this.gnodeId = this.gnode.getId();
	    }
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Float getRate() {
    return rate;
  }

  public void setRate(Float rate) {
    this.rate = rate;
  }

  public String getRateStatus() {
    return rateStatus;
  }

  public void setRateStatus(String rateStatus) {
    this.rateStatus = rateStatus;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Length(min=1, max=2, message="状态：1：生效 0：失效长度必须介于 1 和 2 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public Boolean getHasTpl() {
        return hasTpl;
    }

    public void setHasTpl(Boolean hasTpl) {
        this.hasTpl = hasTpl;
    }

    public String getExcelTplPath() {
        return excelTplPath;
    }

    public void setExcelTplPath(String excelTplPath) {
        this.excelTplPath = excelTplPath;
    }

    public String getExcelTplClazz() {
        return excelTplClazz;
    }

    public void setExcelTplClazz(String excelTplClazz) {
        this.excelTplClazz = excelTplClazz;
    }
}