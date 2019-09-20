package com.oseasy.dr.modules.dr.vo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.dr.modules.dr.entity.DrCardreGtime;
import com.oseasy.dr.modules.dr.manager.IdrVo;
import com.oseasy.pw.modules.pw.vo.PwEnterInfo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;


public class DrCardRecordWarnVo extends DataEntity<DrCardRecordWarnVo> implements IdrVo{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private String sortid;
    private String id;
	private String qryType;
	private String qryStartTime;
	private String qryEndTime;
	private String qryOffice;
	private String qryStr;
    private String queryWarn;
    private String cardNo;
	private String cardStatus;
    private String uno;
	private String uid;
    private String uname;
    private String umobile;
    private String enterType;
    private String office;
    private String enterTimeStr;
    private String exitTimeStr;
    private Date enterTime;
    private Date exitTime;
    private String timestr;
    private String psname;
    private String prname;
    private String msg;
    private String gid;
    private String gname;
    private String rspaceId;
    private String erspaceId;
    private String rspaceName;
    private String warnOver;
    private String warnCurr;
    private String warnH24;

    private Date pcTime;        // 打卡时间
	private Date lastEnterDate;  //最后一次进入时间
	private Date lastExitDate;  //最后一次退出时间
	private String enterAllTime; //进入总时长
    private String fullSpace;
    private String warnName;
	private String isEnter;
	private String enterDay;
	private String isOut;
	private Date outBeginDate;
	private Date outEndDate;

	private PwEnterInfo pwEnterInfo;
	@Transient
    private boolean isExportAll;        // 是否导出所有
    @Transient
    private List<String> ids;  //卡ID，查询多种状态
    @Transient
    private List<String> gids;  //规则组ID，查询多种状态
    @Transient
    private List<String> sortids;  //预警排序ID，查询多种状态
    @Transient
    private List<String> officeIds;  //卡ID，查询多种状态
    @Transient
    private List<String> rspaceIds;  //场地ID，查询多种状态
    @Transient
    private List<DrCardreGtime> drGtimes;  //预警忽略时间段

	public String getSortid() {
        return sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getQueryWarn() {
        return queryWarn;
    }

    public void setQueryWarn(String queryWarn) {
        this.queryWarn = queryWarn;
    }

    public String getWarnH24() {
        return warnH24;
    }

    public void setWarnH24(String warnH24) {
        this.warnH24 = warnH24;
    }

    public String getEnterAllTime() {
		return enterAllTime;
	}

	public void setEnterAllTime(String enterAllTime) {
		this.enterAllTime = enterAllTime;
	}
    public Date getPcTime() {
        return pcTime;
    }

    public void setPcTime(Date pcTime) {
        this.pcTime = pcTime;
    }

    public List<String> getGids() {
        return gids;
    }

    public void setGids(List<String> gids) {
        this.gids = gids;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getLastExitDate() {
        return lastExitDate;
    }

    public void setLastExitDate(Date lastExitDate) {
        this.lastExitDate = lastExitDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastEnterDate() {
		return lastEnterDate;
	}

	public void setLastEnterDate(Date lastEnterDate) {
		this.lastEnterDate = lastEnterDate;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean getIsExportAll() {
        return isExportAll;
    }

    public void setIsExportAll(boolean isExportAll) {
        this.isExportAll = isExportAll;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public PwEnterInfo getPwEnterInfo() {
		return pwEnterInfo;
	}

	public void setPwEnterInfo(PwEnterInfo pwEnterInfo) {
		this.pwEnterInfo = pwEnterInfo;
	}

	public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getWarnOver() {
        return warnOver;
    }

    public void setWarnOver(String warnOver) {
        this.warnOver = warnOver;
    }

    public String getWarnCurr() {
        return warnCurr;
    }

    public void setWarnCurr(String warnCurr) {
        this.warnCurr = warnCurr;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getRspaceId() {
        return rspaceId;
    }

    public void setRspaceId(String rspaceId) {
        this.rspaceId = rspaceId;
    }

    public String getErspaceId() {
        return erspaceId;
    }

    public void setErspaceId(String erspaceId) {
        this.erspaceId = erspaceId;
    }

    public String getRspaceName() {
        return rspaceName;
    }

    public void setRspaceName(String rspaceName) {
        this.rspaceName = rspaceName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getWarnName() {
		return warnName;
	}

	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}

	public String getIsEnter() {
		return isEnter;
	}

	public void setIsEnter(String isEnter) {
		this.isEnter = isEnter;
	}

	public String getEnterDay() {
		return enterDay;
	}

	public void setEnterDay(String enterDay) {
		this.enterDay = enterDay;
	}

	public String getIsOut() {
		return isOut;
	}

	public void setIsOut(String isOut) {
		this.isOut = isOut;
	}

	public Date getOutBeginDate() {
		return outBeginDate;
	}

	public void setOutBeginDate(Date outBeginDate) {
		this.outBeginDate = outBeginDate;
	}

	public Date getOutEndDate() {
		return outEndDate;
	}

	public void setOutEndDate(Date outEndDate) {
		this.outEndDate = outEndDate;
	}

	public String getPsname() {
		return psname;
	}
	public void setPsname(String psname) {
		this.psname = psname;
	}
	public String getPrname() {
		return prname;
	}
	public void setPrname(String prname) {
		this.prname = prname;
	}
	public String getFullSpace() {
		if(StringUtil.isNotEmpty(psname)){
			return psname;
		}
		if(StringUtil.isNotEmpty(prname)){
			return prname;
		}
		return fullSpace;
	}
	public void setFullSpace(String fullSpace) {
		this.fullSpace = fullSpace;
	}
	public String getEnterTimeStr() {
		return enterTimeStr;
	}
	public void setEnterTimeStr(String enterTimeStr) {
		this.enterTimeStr = enterTimeStr;
	}
	public String getExitTimeStr() {
		return exitTimeStr;
	}
	public void setExitTimeStr(String exitTimeStr) {
		this.exitTimeStr = exitTimeStr;
	}
	public String getQryType() {
		return qryType;
	}
	public void setQryType(String qryType) {
		this.qryType = qryType;
	}
	public String getQryStartTime() {
		return qryStartTime;
	}
	public void setQryStartTime(String qryStartTime) {
		this.qryStartTime = qryStartTime;
	}
	public String getQryEndTime() {
		return qryEndTime;
	}
	public void setQryEndTime(String qryEndTime) {
		this.qryEndTime = qryEndTime;
	}
	public String getQryOffice() {
		return qryOffice;
	}
	public void setQryOffice(String qryOffice) {
		this.qryOffice = qryOffice;
	}
	public String getQryStr() {
		return qryStr;
	}
	public void setQryStr(String qryStr) {
		this.qryStr = qryStr;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getUno() {
		return uno;
	}
	public void setUno(String uno) {
		this.uno = uno;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public String getEnterType() {
		return enterType;
	}
	public void setEnterType(String enterType) {
		this.enterType = enterType;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public Date getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}
	public Date getExitTime() {
		return exitTime;
	}
	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}
	public String getTimestr() {
		if(enterTime!=null&&exitTime!=null){
			return DateUtil.formatDateTime3(exitTime.getTime()- enterTime.getTime());
		}
		return timestr;
	}
	public void setTimestr(String timestr) {
		this.timestr = timestr;
	}

    public List<String> getRspaceIds() {
        return rspaceIds;
    }

    public void setRspaceIds(List<String> rspaceIds) {
        this.rspaceIds = rspaceIds;
    }

    public List<DrCardreGtime> getDrGtimes() {
        return drGtimes;
    }

    public void setDrGtimes(List<DrCardreGtime> drGtimes) {
        this.drGtimes = drGtimes;
    }

    public List<String> getSortids() {
        return sortids;
    }

    public void setSortids(List<String> sortids) {
        this.sortids = sortids;
    }
}