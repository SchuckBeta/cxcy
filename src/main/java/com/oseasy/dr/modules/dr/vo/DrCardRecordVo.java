package com.oseasy.dr.modules.dr.vo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.oseasy.util.common.utils.DateUtil;


public class DrCardRecordVo {
	private String id;
    private String gid;
    private String uid;
    private String cardId;
    private String cerspaceId;
    private String erspaceId;
    private String isEnter;
    private String type;
    private Date pcTime;
    private String pcTimeStr;
    private String dispose;
    private String msg;
    private String cardNo;
    private String eptId;
    private String rspType;
    private String rspaceId;
    private String drNo;
    private String name;

    protected String tmpNo;        //学号/工号
    protected String tmpName;        //临时卡姓名
    protected String tmpTel;        //临时卡电话

    @Transient
    private List<String> rspaceIds;  //场地ID，查询多种状态

	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getDrNo() {
		return drNo;
	}
	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEptId() {
		return eptId;
	}
	public void setEptId(String eptId) {
		this.eptId = eptId;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPcTimeStr() {
		if(pcTime!=null){
			return DateUtil.formatDate(pcTime, "yyyy-MM-dd HH:mm:ss");
		}
		return pcTimeStr;
	}
	public void setPcTimeStr(String pcTimeStr) {
		this.pcTimeStr = pcTimeStr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getErspaceId() {
		return erspaceId;
	}
	public void setErspaceId(String erspaceId) {
		this.erspaceId = erspaceId;
	}
	public List<String> getRspaceIds() {
        return rspaceIds;
    }
    public void setRspaceIds(List<String> rspaceIds) {
        this.rspaceIds = rspaceIds;
    }
    public String getIsEnter() {
		return isEnter;
	}
	public void setIsEnter(String isEnter) {
		this.isEnter = isEnter;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getPcTime() {
		return pcTime;
	}
	public void setPcTime(Date pcTime) {
		this.pcTime = pcTime;
	}
	public String getDispose() {
		return dispose;
	}
	public void setDispose(String dispose) {
		this.dispose = dispose;
	}
	public String getRspType() {
		return rspType;
	}
	public void setRspType(String rspType) {
		this.rspType = rspType;
	}
	public String getRspaceId() {
		return rspaceId;
	}
	public void setRspaceId(String rspaceId) {
		this.rspaceId = rspaceId;
	}
    public String getTmpNo() {
        return tmpNo;
    }
    public void setTmpNo(String tmpNo) {
        this.tmpNo = tmpNo;
    }
    public String getTmpName() {
        return tmpName;
    }
    public void setTmpName(String tmpName) {
        this.tmpName = tmpName;
    }
    public String getTmpTel() {
        return tmpTel;
    }
    public void setTmpTel(String tmpTel) {
        this.tmpTel = tmpTel;
    }
    public String getCerspaceId() {
        return cerspaceId;
    }
    public void setCerspaceId(String cerspaceId) {
        this.cerspaceId = cerspaceId;
    }
    public String getGid() {
        return gid;
    }
    public void setGid(String gid) {
        this.gid = gid;
    }
}