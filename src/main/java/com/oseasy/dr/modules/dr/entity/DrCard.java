package com.oseasy.dr.modules.dr.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.thread.IThreadPvo;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.dr.modules.dr.vo.CardDetailEvo;
import com.oseasy.dr.modules.dr.vo.DrAuth;
import com.oseasy.dr.modules.dr.vo.DrCdealStatus;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.pw.modules.pw.vo.PwEnterInfo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

import Net.PC15.FC8800.Command.Data.CardDetail;

/**
 * 门禁卡Entity.
 * @author chenh
 * @version 2018-03-30
 */
public class DrCard extends DataEntity<DrCard> implements Serializable, IidEntity, IThreadPvo{
    private static final String EXPIRY_FORMAT = "yyyy-MM-dd hh:mm";
    private static final long serialVersionUID = 1L;
    public static final String JK_DRCARD_ID = "drCardId";
    public static final String[] RM_ZERO = new String[]{"000", "00", "0"};
	protected User user;		// 用户ID
	private List<DrCardErspace> erspaces;		// 设备场地ID
	protected String no;		// 门禁卡卡号
	protected String password;		// 密码
	protected Date expiry;		// 有效期
	protected Integer status;		// 状态 @see DrCstatus 枚举类
	protected Integer dealStatus;		// 状态 @see DrCdealStatus 枚举类
	protected Long dealVersion;		// 版本标识
	protected Integer openTimes;		// 开门次数,有效次数  65535无限制
	protected Integer privilege;		// 特权:0无1首卡特权卡2常开特权卡3巡更卡4防盗设置卡  @see MyCard.Privilege
	protected Boolean holidayUse;		// 节假日限制
	protected String warnning;		// 是否
	protected String isCancel;		// 取消预警
    protected Boolean isImp;       // 节假日限制
    protected String isExpiry;      // 是否有效：0，否，1.是

    protected String cardType;        //卡类型：1，正式卡，0.临时卡
    protected String tmpNo;        //学号/工号
    protected String tmpName;        //临时卡姓名
    protected String tmpTel;        //临时卡电话
    protected String tmpSex;        //性别：1:男0:女
    protected String tmpOffice;        //零时卡部门学院专业班级
    protected String tmpOfficeBm;        //零时卡部门
    protected String tmpOfficeXyId;        //零时卡部门学院
    protected String tmpOfficeZyId;        //零时卡部门专业
    protected String tmpOfficeXy;        //零时卡部门学院
    protected String tmpOfficeZy;        //零时卡部门专业
    protected String tmpOfficeBj;        //零时卡部门班级

    @Transient
    protected String drNo;      // 门控制信息
    @Transient
	private Boolean[] drNoStatus;		// 设备状态

    @Transient
    private List<String> ids;  //卡ID，查询多种状态
    @Transient
    private PwEnterInfo peInfo;

    @Transient
    private List<String> params;  //卡ID，查询多种状态
    @Transient
    private boolean canTozs;  //是否可转正式卡

	public DrCard() {
		super();
	}

	public DrCard(Boolean isinit) {
	    super();
	    if(isinit){
	        this.privilege = DrAuth.DA_NONE.getKey();
	        this.status = DrCstatus.DC_NORMAL.getKey();
	        this.dealStatus = DrCdealStatus.DCD_NORMAL.getKey();
	        this.password = DrConfig.DET_PSW;
	        this.openTimes = DrConfig.DET_CARD_EXPIRE_MAX;
	        this.holidayUse = false;
            this.canTozs = false;
	        this.warnning = Const.NO;
	        this.isCancel = Const.YES;
	    }
	}

	public DrCard(String id){
		super(id);
	}

	public DrCard(Boolean isinit, String id){
	    super(id);
	    if(isinit){
    	    this.privilege = DrAuth.DA_NONE.getKey();
    	    this.status = DrCstatus.DC_NORMAL.getKey();
            this.dealStatus = DrCdealStatus.DCD_NORMAL.getKey();
    	    this.password = DrConfig.DET_PSW;
    	    this.openTimes = DrConfig.DET_CARD_EXPIRE_MAX;
    	    this.holidayUse = false;
    	    this.warnning = Const.NO;
    	    this.canTozs = false;
    	    this.isCancel = Const.YES;
    	}
	}

	public DrCard(List<String> ids) {
        super();
        this.ids = ids;
    }

    @Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public boolean isCanTozs() {
        return canTozs;
    }

    public void setCanTozs(boolean canTozs) {
        this.canTozs = canTozs;
    }

    public List<DrCardErspace> getErspaces() {
        return erspaces;
    }

    public void setErspaces(List<DrCardErspace> erspaces) {
        this.erspaces = erspaces;
    }

    @Length(min=0, max=64, message="门禁卡卡号长度必须介于 0 和 64 之间")
	public String getNo() {
    	if(StringUtil.isNotEmpty(this.no)){
    	    this.no = StringUtil.rmstart(this.no, RM_ZERO);
    	}
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

    @Length(min=0, max=16, message="密码长度必须介于 0 和 16 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


    public String getIsExpiry() {
        if(this.expiry == null){
            this.isExpiry = Const.NO;
        }
        return isExpiry;
    }

    public void setIsExpiry(String isExpiry) {
        this.isExpiry = isExpiry;
    }

    /**
     * 根据DrEmentNo获取所有设备ID
     * @param drEmentNos 设备编号
     * @return List
     */
    public static List<String> getDrEids(List<DrEmentNo> drEmentNos) {
        if(StringUtil.checkNotEmpty(drEmentNos)){
            List<String> drEnoIds = Lists.newArrayList();
            for (DrEmentNo drEno : drEmentNos) {
                if(StringUtil.checkNotEmpty(drEnoIds) && (drEnoIds).contains(drEno.getEtId())){
                    continue;
                }
                drEnoIds.add(drEno.getEtId());
            }
            return drEnoIds;
        }
        return Lists.newArrayList();
    }

	/**
	 * 根据DrEmentNo获取设备编号
	 * @param drEmentNos 设备编号
	 * @param etpId 设备ID
	 * @return List
	 */
	public static List<String> getDrEnos(List<DrEmentNo> drEmentNos, String etpId) {
		if(StringUtil.checkNotEmpty(drEmentNos)){
		    List<String> drEnoIds = Lists.newArrayList();
		    for (DrEmentNo drEno : drEmentNos) {
		    	if(StringUtil.isNotEmpty(etpId) && (drEno != null) && (etpId).equals(drEno.getEtId())){
		    		drEnoIds.add(drEno.getDrNo());
		    	}
		    }
		    return drEnoIds;
		}
		return null;
	}

	/**
	 * 获取设备硬件状态
	 * @return Boolean
	 */
	public Boolean[] getDrNoStatus() {
		return drNoStatus;
	}

	public void setDrNoStatus(Boolean[] drNoStatus) {
		this.drNoStatus = drNoStatus;
	}

	public Date getExpiry() {
		return expiry;
	}

    public PwEnterInfo getPeInfo() {
        return peInfo;
    }

    public void setPeInfo(PwEnterInfo peInfo) {
        this.peInfo = peInfo;
    }

    public Calendar getExpiryCalender(String format) throws  Exception {
		if(this.expiry == null){
			return null;
		}

		format = format != null ? format : EXPIRY_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(DateUtil.format(sdf, this.expiry)));
		return calendar;
	}

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTmpName() {
        if(StringUtil.isEmpty(this.tmpName) && (this.user != null) && StringUtil.isNotEmpty(this.user.getName())){
            this.tmpName = this.user.getName();
        }
        return tmpName;
    }

    public void setTmpName(String tmpName) {
        this.tmpName = tmpName;
    }

    public String getTmpTel() {
        if(StringUtil.isEmpty(this.tmpTel) && (this.user != null) && StringUtil.isNotEmpty(this.user.getMobile())){
            this.tmpTel = this.user.getMobile();
        }
        return tmpTel;
    }

    public void setTmpTel(String tmpTel) {
        this.tmpTel = tmpTel;
    }

    @Transient
	public List<String> getIds() {
        return ids;
    }

    @Transient
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    @JsonIgnore
	public void setStatusBytes(int status) {
	    this.status = status;
	}

	public Integer getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(Integer openTimes) {
        this.openTimes = openTimes;
    }

	public Integer getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Integer privilege) {
		this.privilege = privilege;
	}

	public Boolean getHolidayUse() {
		return holidayUse;
	}

	public void setHolidayUse(Boolean holidayUse) {
		this.holidayUse = holidayUse;
	}

	public Long getDealVersion() {
        return dealVersion;
    }

    public void setDealVersion(Long dealVersion) {
        this.dealVersion = dealVersion;
    }

    @Transient
	public String getDrNo() {
        if(StringUtil.checkNotEmpty(this.erspaces)){
//        if(StringUtil.isEmpty(this.drNo) && StringUtil.checkNotEmpty(this.erspaces)){
            StringBuffer buffer = null;
            for (DrCardErspace drCerspace : erspaces) {
                if((drCerspace.getErspace() != null) && StringUtil.isNotEmpty(drCerspace.getErspace().getDrNo())){
                    if(buffer == null){
                        buffer = new StringBuffer();
                    }
                    buffer.append(drCerspace.getErspace().getDrNo());
                    buffer.append(StringUtil.DOTH);
                }
            }

            if(buffer != null){
                this.drNo = buffer.toString();
            }
        }
        return this.drNo;
    }

    @Deprecated
	@Transient
    public void setDrNo(String drNo) {
        this.drNo = drNo;
    }

    @Length(min=0, max=1, message="是否长度必须介于 0 和 1 之间")
	public String getWarnning() {
		return warnning;
	}

	public void setWarnning(String warnning) {
		this.warnning = warnning;
	}

	@Length(min=0, max=1, message="取消预警长度必须介于 0 和 1 之间")
	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public String getTmpNo() {
	    if(StringUtil.isEmpty(this.tmpNo) && (this.user != null) && StringUtil.isNotEmpty(this.user.getNo())){
            this.tmpNo = this.user.getNo();
        }
        return tmpNo;
    }

    public void setTmpNo(String tmpNo) {
        this.tmpNo = tmpNo;
    }

    public String getTmpSex() {
        if(StringUtil.isEmpty(this.tmpSex) && (this.user != null) && StringUtil.isNotEmpty(this.user.getSex())){
            this.tmpSex = this.user.getSex();
        }
        return tmpSex;
    }

    public void setTmpSex(String tmpSex) {
        this.tmpSex = tmpSex;
    }

    public String getTmpOffice() {
        if(StringUtil.isEmpty(this.tmpOffice) && StringUtil.isNotEmpty(this.tmpOfficeXy)){
            this.tmpOffice = this.tmpOfficeXy;
        }
        return tmpOffice;
    }

    public void setTmpOffice(String tmpOffice) {
        this.tmpOffice = tmpOffice;
    }

    public String getTmpOfficeBm() {
        return tmpOfficeBm;
    }

    public void setTmpOfficeBm(String tmpOfficeBm) {
        this.tmpOfficeBm = tmpOfficeBm;
    }

    public String getTmpOfficeXy() {
        if(StringUtil.isEmpty(this.tmpOfficeXy) && (this.user != null) && StringUtil.isNotEmpty(this.user.getOfficeName())){
            this.tmpOfficeXy = this.user.getOfficeName();
        }
        return tmpOfficeXy;
    }

    public void setTmpOfficeXy(String tmpOfficeXy) {
        this.tmpOfficeXy = tmpOfficeXy;
    }

    public String getTmpOfficeXyId() {
        if(StringUtil.isEmpty(this.tmpOfficeXy) && (this.user != null) && StringUtil.isNotEmpty(this.user.getOfficeId())){
            this.tmpOfficeXy = this.user.getOfficeId();
        }
        return tmpOfficeXyId;
    }

    public void setTmpOfficeXyId(String tmpOfficeXyId) {
        this.tmpOfficeXyId = tmpOfficeXyId;
    }

    public String getTmpOfficeZyId() {
        return tmpOfficeZyId;
    }

    public void setTmpOfficeZyId(String tmpOfficeZyId) {
        this.tmpOfficeZyId = tmpOfficeZyId;
    }

    public String getTmpOfficeZy() {
        return tmpOfficeZy;
    }

    public void setTmpOfficeZy(String tmpOfficeZy) {
        this.tmpOfficeZy = tmpOfficeZy;
    }

    public String getTmpOfficeBj() {
        return tmpOfficeBj;
    }

    public void setTmpOfficeBj(String tmpOfficeBj) {
        this.tmpOfficeBj = tmpOfficeBj;
    }

    /**
	 * 处理CardDetail基本信息.
	 * @param drCard
	 * @return CardDetail
	 */
    private static CardDetail initCardDetail(DrCard drCard) {
        CardDetail cardDetail = new CardDetail(Long.valueOf(drCard.getNo()));//设置卡号
        cardDetail.Password = drCard.password ;
        if(drCard.expiry != null){
            Calendar cdar = Calendar.getInstance();
            cdar.setTime(drCard.expiry);
            cardDetail.Expiry = cdar;
        }

        if(drCard.openTimes == null){
            drCard.setOpenTimes(DrConfig.DET_CARD_EXPIRE_MAX);
        }
        cardDetail.OpenTimes = drCard.openTimes.intValue();

		if(drCard.getStatus()!=null){
			cardDetail.CardStatus = drCard.getStatus().byteValue();
		}

        if(drCard.getPrivilege() == null){
            drCard.setPrivilege(DrAuth.DA_NONE.getKey());
        }
        DrAuth.updateCardDetail(drCard, cardDetail);

        if(drCard.holidayUse == null){
            drCard.setHolidayUse(true);
        }
        cardDetail.HolidayUse = drCard.holidayUse.booleanValue();

        if(drCard.expiry == null){
            cardDetail.Expiry = DrConfig.getExpiry();
            drCard.setExpiry(DrConfig.getExpiry().getTime());
        }
        return cardDetail;
    }

    /**
     * 处理CardDetail的授权信息.
     * @param drCard
     * @param cardDetail
     * @return CardDetail
     */
    private static CardDetail initCardDetailAuth(DrCard drCard, CardDetail cardDetail) {
        cardDetail.SetTimeGroup(DrKey.DK_1.getIndex(), 1);//每个门都设定为1门
        cardDetail.SetTimeGroup(DrKey.DK_2.getIndex(), 1);//每个门都设定为1门
        cardDetail.SetTimeGroup(DrKey.DK_3.getIndex(), 1);//每个门都设定为1门
        cardDetail.SetTimeGroup(DrKey.DK_4.getIndex(), 1);//每个门都设定为1门

    	if((drCard.getDrNoStatus() != null) && (drCard.getDrNoStatus().length == 4)){
            cardDetail.SetDoor(DrKey.DK_1.getIndex(), drCard.getDrNoStatus()[0]);
            cardDetail.SetDoor(DrKey.DK_2.getIndex(), drCard.getDrNoStatus()[1]);
            cardDetail.SetDoor(DrKey.DK_3.getIndex(), drCard.getDrNoStatus()[2]);
            cardDetail.SetDoor(DrKey.DK_4.getIndex(), drCard.getDrNoStatus()[3]);
        }
//        if((drCard != null) && StringUtil.isNotEmpty(drCard.getDrNo())){//4门控制限制
            //TODO SetTimeGroup方法的作用是什么????
//        	cardDetail.SetTimeGroup(DrKey.DK_1.getIndex(), 1);//每个门都设定为1门
//            cardDetail.SetTimeGroup(DrKey.DK_2.getIndex(), 1);//每个门都设定为1门
//            cardDetail.SetTimeGroup(DrKey.DK_3.getIndex(), 1);//每个门都设定为1门
//            cardDetail.SetTimeGroup(DrKey.DK_4.getIndex(), 1);//每个门都设定为1门

//            cardDetail.SetDoor(DrKey.DK_1.getIndex(), StringUtil.contains(drCard.getDrNo(), DrKey.DK_1.getKey() + ""));
//            cardDetail.SetDoor(DrKey.DK_2.getIndex(), StringUtil.contains(drCard.getDrNo(), DrKey.DK_2.getKey() + ""));
//            cardDetail.SetDoor(DrKey.DK_3.getIndex(), StringUtil.contains(drCard.getDrNo(), DrKey.DK_3.getKey() + ""));
//            cardDetail.SetDoor(DrKey.DK_4.getIndex(), StringUtil.contains(drCard.getDrNo(), DrKey.DK_4.getKey() + ""));
//        }
        return cardDetail;
    }

    /**
     * 根据Erspaces处理DrCard的DrNo.
     * @param drCard 卡
     * @param epmentId 设备ID
     * @return CardDetail
     */
    public static DrCard initDrNo(DrCard drCard, String epmentId) {
        List<DrCardErspace> erspaces = drCard.getErspaces();
        StringBuffer buffer = new StringBuffer();
        if(StringUtil.checkEmpty(erspaces) || StringUtil.isEmpty(epmentId)){//4门控制限制
            return drCard;
        }

        for (DrCardErspace drCerspace : erspaces) {
            if((drCerspace == null) || (drCerspace.getErspace() == null) || (drCerspace.getErspace().getEpment() == null) || StringUtil.isEmpty(drCerspace.getErspace().getDrNo()) || StringUtil.isEmpty(drCerspace.getErspace().getEpment().getId())){
                continue;
            }

            if(!(epmentId).equals(drCerspace.getErspace().getEpment().getId())){
                continue;
            }

            buffer.append(StringUtil.DOTH);
            buffer.append(drCerspace.getErspace().getDrNo());
        }
        drCard.setDrNo((buffer.toString().startsWith(StringUtil.DOTH)) ? (buffer.toString()).substring(1) : buffer.toString());
        return drCard;
    }


	/**
	 * 如果drCard.erspaces属性为空，则无法控制.
	 * @param drCard
	 * @return
	 */
    public static CardDetail toCardDetail(DrCard drCard) {
        if(drCard == null){
            return null;
        }
        return initCardDetailAuth(drCard, initCardDetail(drCard));
    }

    /**
     * 根据设备ID处理CardDetail如果drCard.erspaces属性为空，则无法控制.
     * @param drCard 卡
     * @param epmentId 设备ID
     * @return CardDetailEvo
     */
    public static CardDetailEvo toCardDetail(DrCard drCard, String epmentId) {
        if(StringUtil.isEmpty(epmentId)){
            return null;
        }
        CardDetail cardDetail = initCardDetail(drCard);

        List<DrCardErspace> erspaces = drCard.getErspaces();
        if(StringUtil.checkEmpty(erspaces) || StringUtil.isEmpty(epmentId)){//4门控制限制
            return new CardDetailEvo(cardDetail, epmentId);
        }

        drCard = initDrNo(drCard, epmentId);
        initCardDetailAuth(drCard, cardDetail);
        return new CardDetailEvo(cardDetail, epmentId);
    }

    /**
     * 根据设备ID处理CardDetail如果drCard.erspaces属性为空，则无法控制.
     * @param drCards 卡(card和epmentId必填)
     * @return List
     */
    public static List<CardDetailEvo> toCardDetails(List<CardDetailEvo> drCards) {
        List<CardDetailEvo> cardDetailEvos = Lists.newArrayList();
        for (CardDetailEvo cardDetailEvo : drCards) {
            cardDetailEvos.add(toCardDetail(cardDetailEvo.getCard(), cardDetailEvo.getEpmentId()));
        }
        return cardDetailEvos;
    }

    public static DrCard fromCardDetail(CardDetail cardDetail){
        DrCard card = new DrCard();
        card.setNo(cardDetail.CardData + "");
        card.setPassword(cardDetail.Password.replaceAll("F", ""));
        card.setExpiry(cardDetail.Expiry.getTime());
        card.setOpenTimes(cardDetail.OpenTimes);
        card.setStatusBytes(cardDetail.CardStatus);

        DrAuth.updateCard(card, cardDetail);
        card.setHolidayUse(cardDetail.HolidayUse);
        card.setDrNo(DrKey.updateDrCard(card, cardDetail));
        return card;
    }
}