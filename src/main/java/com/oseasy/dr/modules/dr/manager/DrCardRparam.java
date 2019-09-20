/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * .
 * @author chenhao
 *
 */
public class DrCardRparam {
    protected Integer type;//类型，硬件操作类型1add 2get 3 del 4query
    protected Integer operType;//操作类型，标识执行的操作
    protected Long operVersion;//操作标识，标识操作的惟一ID，为了区分不同操作
    protected Long operCeVersion;//操作设备标识，标识操作的惟一ID，为了区分不同操作

    protected String etpId;//设备ID
    protected String etpSn;//设备编号
    protected String cardId;//卡ID
    protected String cardNo;//卡编号

    private Boolean success;//处理结果
    private Boolean isRelease;//是否最后一个设备
    private Boolean isDeal;//是否处理完成
    private List<Integer> doors; //设备下的门列表
    private String commandType; // 请求命令
    public DrCardRparam() {
        super();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DrCardRparam(DrCardParam param) {
        super();
        if(param != null){
            this.etpId = param.getEtpId();
            this.etpSn = param.getEtpSn();
            this.operVersion = param.getDealVersion();
            this.operCeVersion = param.getCeVersion();

            this.isRelease = param.getIsRelease();//是否最后一个设备
            this.isDeal = param.getIsDeal();//是否处理完成
            if(param.getCard() != null){
                this.cardId = param.getCard().getId();
                this.cardNo = param.getCard().getNo();
            }
            if (!CollectionUtils.isEmpty(param.getDoors())) {
                this.doors = param.getDoors();
            }

            if (StringUtils.isEmpty(param.getType())) {
                this.commandType = param.getType();
            }
        }
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Long getOperVersion() {
        return operVersion;
    }

    public void setOperVersion(Long operVersion) {
        this.operVersion = operVersion;
    }

    public String getEtpId() {
        return etpId;
    }

    public void setEtpId(String etpId) {
        this.etpId = etpId;
    }

    public String getEtpSn() {
        return etpSn;
    }

    public void setEtpSn(String etpSn) {
        this.etpSn = etpSn;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Boolean getSuccess() {
        if(this.success == null){
            this.success = false;
        }
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getOperCeVersion() {
        return operCeVersion;
    }

    public void setOperCeVersion(Long operCeVersion) {
        this.operCeVersion = operCeVersion;
    }

	public Boolean getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(Boolean isRelease) {
		this.isRelease = isRelease;
	}

	public Boolean getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(Boolean isDeal) {
		this.isDeal = isDeal;
	}

    public List<Integer> getDoors() {
        return doors;
    }

    public void setDoors(List<Integer> doors) {
        this.doors = doors;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }
}
