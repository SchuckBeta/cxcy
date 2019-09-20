package com.oseasy.dr.modules.dr.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.utils.thread.IThreadPvo;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.dr.modules.dr.manager.CardManager;
import com.oseasy.dr.modules.dr.vo.DrKey;
import com.oseasy.util.common.utils.StringUtil;

import Net.PC15.FC8800.Command.Data.CardTransaction;

/**
 * 门禁卡记录Entity.
 *
 * @author chenh
 * @version 2018-03-30
 */
public class DrCardRecord extends DataEntity<DrCardRecord> implements Comparable<DrCardRecord>, IThreadPvo {
    private static final long serialVersionUID = 1L;
    private User user;        // 用户
    private DrCard card;        // 卡信息
    private DrCardErspace cerspace;        // 设备空间信息
    private boolean isEnter;        // 是否进门打卡0-进，1-出 GitemEstatus.GES_ENTER
    private String type;        // 打卡类型  正常打打卡， 未注册打卡 ， 黑名单打卡
    private Date pcTime;        // 打卡时间
    private Integer time;//被处理次数，超过3次标记为已处理
    private String dispose;//是否已被处理，0-未处理，1-已处理
    private String disposeMsg;//是否已被处理，0-未处理，1-已处理
    private String msg;//信息

    private String cardNo;//卡号
    private String eptSn;//设备SN
    private String eptId;//设备id
    private String rspType;//场地类型
    private String rspaceId;//场地id
    protected String rspaceName;        //场地名
    private String drNo;//设备端口号
    private String name;//场地门名称

    protected String tmpNo;        //学号/工号
    protected String tmpName;        //临时卡姓名
    protected String tmpTel;        //临时卡电话

    public String getCardNo() {
        if(StringUtil.isNotEmpty(this.cardNo)){
            this.cardNo = StringUtil.rmstart(this.cardNo, DrCard.RM_ZERO);
        }
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDisposeMsg() {
        return disposeMsg;
    }

    public void setDisposeMsg(String disposeMsg) {
        this.disposeMsg = disposeMsg;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DrCardRecord() {
        super();
    }

    public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public DrCardRecord(User user, DrCard card, DrCardErspace cerspace) {
        super();
        this.user = user;
        this.card = card;
        this.cerspace = cerspace;
    }

    public DrCardRecord(User user) {
        super();
        this.user = user;
    }

    public DrCardRecord(DrCard card) {
        super();
        this.card = card;
    }

    public DrCardRecord(DrCardErspace cerspace) {
        super();
        this.cerspace = cerspace;
    }

    public DrCardRecord(User user, DrCard card, DrCardErspace cerspace, boolean isEnter, String type,
                        Date pcTime) {
        super();
        this.user = user;
        this.card = card;
        this.cerspace = cerspace;
        this.isEnter = isEnter;
        this.type = type;
        this.pcTime = pcTime;
    }

    public String getDispose() {
        return dispose;
    }

    public void setDispose(String dispose) {
        this.dispose = dispose;
    }

    public DrCardRecord(String id) {
        super(id);
    }

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean getIsEnter() {
        return isEnter;
    }

    public String getRspaceName() {
        return rspaceName;
    }

    public void setRspaceName(String rspaceName) {
        this.rspaceName = rspaceName;
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

    public void setEnter(boolean isEnter) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DrCard getCard() {
        return card;
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

    public static DrCardRecord fromCardTransaction(String sn, String recordType, CardTransaction record) {
        DrCardRecord cardRecord = new DrCardRecord(new DrCard());
        cardRecord.setEptSn(sn);
        cardRecord.setDispose(Const.NO);
        cardRecord.setDisposeMsg(Const.NO);
        cardRecord.setTime(0);
        //处理DrCardRecord 中 cardNo为0开头的卡会缺失0，而授权时为0必须有。统一为8位
        if(StringUtil.isNotEmpty(record.CardData + "")){
            cardRecord.setCardNo(record.CardData + "");
            //处理DrCardRecord 中 cardNo为0开头的卡会缺失0，而授权时为0必须有。统一为8位
//            if(cardRecord.getCardNo().length() < 8){
//            	cardRecord.setCardNo(StringUtil.autoGenZero(8, cardRecord.getCardNo()));
//            }
            cardRecord.getCard().setNo(cardRecord.getCardNo());
        }
        //处理DrCardRecord 中 drNo为1、2、3、4，而授权时为0、1、2、3不匹配问题。统一为0、1、2、3
        if(StringUtil.isNotEmpty(record.DoorNum() + "")){
        	DrKey drKey = DrKey.getByIdxStr(record.DoorNum() + "");
            if(drKey != null){
            	cardRecord.setDrNo(drKey.getKeyStr());
                cardRecord.getCard().setDrNo(cardRecord.getDrNo());
                cardRecord.setName(drKey.getName());
            }else{
                cardRecord.setName("");
            }
        }
        cardRecord.setEnter(record.IsEnter());//GitemEstatus 0进1出
        cardRecord.setPcTime(record.TransactionDate().getTime());
        cardRecord.setType(recordType);
        short code = record.TransactionCode();
        if (code < 255) {
            String sCode = CardManager.mCardTransactionList[code];
            if (StringUtil.isNotEmpty(sCode)) {
                cardRecord.setType(CardManager.UNKOWN_TYPE);
            }
            cardRecord.setType(sCode);
        } else {
            cardRecord.setType(CardManager.UNKOWN_TYPE);
        }
        return cardRecord;
    }

    @Override
    public int compareTo(DrCardRecord o) {
        if (o == null) {
            return -1;
        }
        return o.getPcTime().compareTo(this.getPcTime());
    }


    @Override
    public String toString() {
        String card1 = card != null ? card.getNo() : "";
        String doorNo = card != null ? card.getDrNo() : "";
        String erspace_id = cerspace != null ? cerspace.getId() : "";
        String userid = user != null ? user.getId() : "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String pcTime1 = sdf.format(pcTime.getTime());
        return "DrCardRecord{" +
                "isEnter=" + isEnter +
                ", type='" + type + '\'' +
                ", userid='" + userid + '\'' +
                ", cardNo='" + card1 + '\'' +
                ", eptSn='" + eptSn + '\'' +
                ", eptId='" + eptId + '\'' +
                ", erspace_id='" + erspace_id + '\'' +
                ", rspType='" + rspType + '\'' +
                ", rspaceId='" + rspaceId + '\'' +
                ", drNo='" + drNo + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}