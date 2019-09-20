package com.oseasy.dr.modules.dr.vo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.dr.modules.dr.manager.IdrVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;


public class DrInoutRecordVo extends DataEntity<DrInoutRecordVo> implements IdrVo{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String qryType; //1 进  2出
	private String qryStartTime;
	private String qryEndTime;
	private String qryOffice;
	private String qryStr;
    private String cardNo;
    private String uno;
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
    private String fullSpace;

    @Transient
    private boolean isExportAll;        // 是否导出所有
    @Transient
    private List<String> ids;  //卡ID，查询多种状态
    @Transient
    private List<String> officeIds;  //卡ID，查询多种状态


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
	public String getUmobile() {
        return umobile;
    }
    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }
    public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
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
	public boolean getIsExportAll() {
        return isExportAll;
    }
    public void setIsExportAll(boolean isExportAll) {
        this.isExportAll = isExportAll;
    }
    public List<String> getIds() {
        return ids;
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getOfficeIds() {
        return officeIds;
    }
    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
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


}