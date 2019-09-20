package com.oseasy.dr.modules.dr.vo;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardErspace;
import com.oseasy.dr.modules.dr.manager.IdrVo;
import com.oseasy.util.common.utils.StringUtil;


public class DrCardRecordShowVo  extends DataEntity<DrCardRecordShowVo> implements IdrVo{
    private static final long serialVersionUID = 1L;
    private String id;
	private String qryStartTime;
	private String qryEndTime;
	private String qryStr;
	private String qryOffice;

	private User user;        // 用户
	private DrCard card;        // 卡信息
	private DrCardErspace cerspace;        // 设备空间信息
	private String isEnter;        // 是否进门打卡
	private String type;        // 打卡类型  正常打打卡， 未注册打卡 ， 黑名单打卡
	private Date pcTime;        // 打卡时间
	private String dispose;//是否已被处理，0-未处理，1-已处理
	private String msg;//消息

	private String cardNo;//卡号
	private String eptSn;//设备SN
	private String eptId;//设备id
	private String rspType;//场地类型
	private String rspaceId;//场地id
	private String rspaceName;//场地名称
	private String drNo;//设备端口号
	private String name;//场地门名称
	private String uno;//学号
	private String uname;
	private String umobile;
	private String office;
	private String space;//房间
	private String psname;//场地
	private String pcTimeStr;
	@Transient
    private boolean isExportAll;        // 是否导出所有
    @Transient
    private List<String> ids;  //卡ID，查询多种状态
    @Transient
    private List<String> officeIds;  //卡ID，查询多种状态

	public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQryEndTime(String qryEndTime) {
		this.qryEndTime = qryEndTime;
	}

	public String getQryStr() {
		return qryStr;
	}

	public void setQryStr(String qryStr) {
		this.qryStr = qryStr;
	}

	public String getQryOffice() {
		return qryOffice;
	}

	public void setQryOffice(String qryOffice) {
		this.qryOffice = qryOffice;
	}

	public List<String> getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(List<String> officeIds) {
        this.officeIds = officeIds;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DrCard getCard() {
		return card;
	}

	public String getPsname() {
        return psname;
    }

    public void setPsname(String psname) {
        this.psname = psname;
    }

    public String getUmobile() {
        if(StringUtil.isEmpty(this.umobile) && ((this.card != null) && StringUtil.isNotEmpty(this.card.getTmpTel()))){
            return this.card.getTmpTel();
        }
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public void setCard(DrCard card) {
		this.card = card;
	}

	public DrCardErspace getCerspace() {
		return cerspace;
	}

	public void setCerspace(DrCardErspace cerspace) {
		this.cerspace = cerspace;
	}

	public String getRspaceName() {
        return rspaceName;
    }

    public void setRspaceName(String rspaceName) {
        this.rspaceName = rspaceName;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getEptSn() {
		return eptSn;
	}

	public void setEptSn(String eptSn) {
		this.eptSn = eptSn;
	}

	public String getEptId() {
		return eptId;
	}

	public void setEptId(String eptId) {
		this.eptId = eptId;
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

	public String getDrNo() {
		return drNo;
	}

	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}

	public String getName() {
	    if(StringUtil.isEmpty(this.name) && ((this.card != null) && StringUtil.isNotEmpty(this.card.getTmpName()))){
	        return this.card.getTmpName();
	    }
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUno() {
	    if(StringUtil.isEmpty(this.uno) && ((this.card != null) && StringUtil.isNotEmpty(this.card.getTmpNo()))){
	        return this.card.getTmpNo();
	    }
		return uno;
	}

	public void setUno(String uno) {
		this.uno = uno;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getSpace() {
        if(StringUtil.isEmpty(this.space)){
            if(StringUtil.isNotEmpty(this.psname)){
                return this.psname;
            }
            if(StringUtil.isNotEmpty(this.rspaceName)){
                return this.rspaceName;
            }
        }
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getUname() {
	    if(StringUtil.isEmpty(this.uname) && ((this.card != null) && StringUtil.isNotEmpty(this.card.getTmpName()))){
            return this.card.getTmpName();
        }
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPcTimeStr() {
		return pcTimeStr;
	}

	public void setPcTimeStr(String pcTimeStr) {
		this.pcTimeStr = pcTimeStr;
	}
}