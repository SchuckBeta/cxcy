package com.oseasy.dr.modules.dr.entity;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataExtEntity;
import com.oseasy.dr.modules.dr.manager.DrConfig;
import com.oseasy.util.common.utils.IidEntity;
import com.oseasy.util.common.utils.StringUtil;

import Net.PC15.Connector.E_ControllerType;

/**
 * 门禁设备Entity.
 * @author chenh
 * @version 2018-03-30
 */
public class DrEquipment extends DataExtEntity<DrEquipment> implements IidEntity{

	private static final long serialVersionUID = 1L;
	private String name;		// 设备名称
    private String no;      // 设备号
    private String psw;      // 设备密码
	private String ip;		// IP地址
	private Integer type;		// 设备类型
	private int port;		// 端口号
	private String drNo;		// 设备出口编号，门1  门2 门3 门4
    protected Integer dealStatus;       // 状态 @see DrCdealStatus 枚举类
	private List<String> drNoList;		// 设备出口编号，门1  门2 门3 门4
    @Transient
    private List<String> ids;      // 设备ID
    @Transient
    private List<String> delFlags;      //删除状态

    private Integer tindex;        // 读取的索引位置
	private Integer tsize;  //每次读取的长度。
    private Boolean removeAll;        //删除设备是否清除数据

	public List<String> getDrNoList() {
		List<String> drNoList = null;
		if (StringUtils.isNotBlank(this.drNo)) {
			drNoList = Arrays.asList(StringUtils.split(this.drNo, StringUtil.DOTH));
		}

		if (drNoList == null) {
			drNoList = Lists.newArrayList();
		}
		return drNoList;
//		return drNoList;
	}

	public void setDrNoList(List<String> drNoList) {
		if (drNoList != null && drNoList.size() > 0) {
			StringBuffer strbuff = new StringBuffer();
			for (String domainId : drNoList) {
				strbuff.append(domainId);
				strbuff.append(StringUtil.DOTH);
			}
			String domainIds = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
			setDrNo(domainIds);
		}
		this.drNoList = drNoList;
	}

	public DrEquipment() {
		super();
	}

	public DrEquipment(Boolean isInit) {
	    super();
	    if(isInit){
	        this.psw = DrConfig.DET_PSW;
	        this.port = DrConfig.DET_PROT;
	        this.drNo = DrConfig.DET_DR_NO;
	        this.type = DrConfig.DET_TYPE;
			this.tindex=DrConfig.DET_INDEX;
			this.tsize=DrConfig.DET_SIZE;

	    }
	}


	public DrEquipment(String id){
		super(id);
	}

	public DrEquipment(Boolean isInit, String id){
	    super(id);
	    if(isInit){
	        this.psw = DrConfig.DET_PSW;
	        this.port = DrConfig.DET_PROT;
	        this.drNo = DrConfig.DET_DR_NO;
	        this.type = DrConfig.DET_TYPE;
			this.tsize=DrConfig.DET_SIZE;
        }
	}

	public DrEquipment(String no, String ip) {
        super();
        this.no = no;
        this.ip = ip;
    }

	public DrEquipment(Boolean isInit, String no, String ip) {
	    super();
	    this.no = no;
	    this.ip = ip;
	    if(isInit){
    	    this.psw = DrConfig.DET_PSW;
    	    this.port = DrConfig.DET_PROT;
    	    this.drNo = DrConfig.DET_DR_NO;
    	    this.type = DrConfig.DET_TYPE;
			this.tsize=DrConfig.DET_SIZE;
	    }
	}

	public DrEquipment(String no, String name, String ip) {
	    super();
	    this.no = no;
	    this.name = name;
	    this.ip = ip;

	}

	public DrEquipment(Boolean isInit, String no, String name, String ip) {
	    super();
	    this.no = no;
	    this.name = name;
	    this.ip = ip;
	    if(isInit){
    	    this.psw = DrConfig.DET_PSW;
    	    this.port = DrConfig.DET_PROT;
    	    this.drNo = DrConfig.DET_DR_NO;
    	    this.type = DrConfig.DET_TYPE;
	    }
	}

    public DrEquipment(String name, String no, String psw, String ip, Integer type, int port, String drNo,
            int tindex) {
        super();
        this.name = name;
        this.no = no;
        this.psw = psw;
        this.ip = ip;
        this.type = type;
        this.port = port;
        this.drNo = drNo;
        this.tindex = tindex;
    }

    @Length(min=0, max=64, message="设备号长度必须介于 0 和 64 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
    public List<String> getDelFlags() {
        return delFlags;
    }

    public void setDelFlags(List<String> delFlags) {
        this.delFlags = delFlags;
    }

    public Boolean getRemoveAll() {
        return removeAll;
    }

    public void setRemoveAll(Boolean removeAll) {
        this.removeAll = removeAll;
    }

    @Length(min=0, max=64, message="设备名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Length(min=0, max=64, message="IP地址长度必须介于 0 和 64 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Integer dealStatus) {
        this.dealStatus = dealStatus;
    }

    public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTindex() {
	    if(tindex == null){
	        tindex = 0;
	    }
        return tindex;
    }

    public void setTindex(int tindex) {
        this.tindex = tindex;
    }

	public Integer getTsize() {
		return tsize;
	}

	public void setTsize(Integer tsize) {
		this.tsize = tsize;
	}

	@Length(min=0, max=32, message="设备出口编号，门1  门2 门3 门4长度必须介于 0 和 32 之间")
	public String getDrNo() {
		return drNo;
	}

	public void setDrNo(String drNo) {
		this.drNo = drNo;
	}

    public E_ControllerType retType() {
        if(this.type == null){
            return null;
        }
        return E_ControllerType.getByValue(this.type);
    }

    public List<String> getDrNos() {
        List<String> drNos = null;
        if (StringUtils.isNotBlank(this.drNo)) {
            drNos = Arrays.asList(StringUtils.split(this.drNo, StringUtil.DOTH));
        }

        if (drNos == null) {
            drNos = Lists.newArrayList();
        }
        return drNos;
    }



}