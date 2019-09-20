package com.oseasy.pw.modules.pw.entity;

import java.math.BigDecimal;
import java.util.List;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻企业Entity.
 * @author chenh
 * @version 2017-11-26
 */
public class PwCompany extends DataEntity<PwCompany> {

	private static final long serialVersionUID = 1L;
	private String no;		// 执照编号
	private String name;		// 名称
	private String phone;		// 电话
	private String mobile;		// 手机
	private String address;		// 地址
	private BigDecimal regMoney;		// 注册资金:万元
	private String regMtype;		// 资金来源 pw_reg_mtype
    private List<String> regMtypes;//资金来源
	private String regPerson;		// 法人
	private String remarks;		// 公司主营业务
	private String eid;		// 入驻申请表id
	private AttachMentEntity attachMentEntity; //附件
	private List<SysAttachment> fileInfo;
	private List<SysAttachment> sysAttachmentList;

	public List<SysAttachment> getSysAttachmentList() {
		return sysAttachmentList;
	}

	public void setSysAttachmentList(List<SysAttachment> sysAttachmentList) {
		this.sysAttachmentList = sysAttachmentList;
	}

	public PwCompany() {
		super();
	}

	public PwCompany(String id){
		super(id);
	}

	@Length(min=1, max=64, message="执照编号长度必须介于 1 和 64 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Length(min=0, max=255, message="名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=200, message="电话长度必须介于 0 和 200 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 0 和 200 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Length(min=0, max=200, message="地址长度必须介于 0 和 200 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getRegMoney() {
		return regMoney;
	}

	public void setRegMoney(BigDecimal regMoney) {
		this.regMoney = regMoney;
	}

	public String getEid() { return eid; }

	public void setEid(String eid) { this.eid = eid; }

	@Override
	public String getRemarks() { return remarks; }

	@Override
	public void setRemarks(String remarks) { this.remarks = remarks; }

	public String getRegMtype() {
    return regMtype;
  }

  	public void setRegMtype(String regMtype) {
    this.regMtype = regMtype;
  }

  	public String getRegPerson() {
		return regPerson;
	}

	public void setRegPerson(String regPerson) {
		this.regPerson = regPerson;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}

	public List<SysAttachment> getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(List<SysAttachment> fileInfo) {
		this.fileInfo = fileInfo;
	}

	public List<String> getRegMtypes() {
		if (StringUtils.isNotBlank(regMtype)) {
		  	String[] regMtypeArray = StringUtils.split(regMtype, StringUtil.DOTH);
		  	regMtypes = Lists.newArrayList();
		  	for (String rMtype : regMtypeArray) {
				regMtypes.add(rMtype);
		  	}
		}
    	return regMtypes;
  	}

	public void setRegMtypes(List<String> regMtypes) {
		if ((regMtypes != null) && (regMtypes.size() > 0)) {
			StringBuffer strbuff = new StringBuffer();
			for (String rMtype : regMtypes) {
			  	strbuff.append(rMtype);
			  	strbuff.append(StringUtil.DOTH);
			}
			String currMtype = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
			setRegMtype(currMtype);
		}
		this.regMtypes = regMtypes;
	}
	@Override
    public boolean equals(Object obj) {
        if (obj instanceof PwCompany) {
			PwCompany pwCompany= (PwCompany) obj;
            return name.equalsIgnoreCase(pwCompany.getName().trim())
					&& no.equalsIgnoreCase(pwCompany.getNo().trim())
					&& address.equalsIgnoreCase(pwCompany.getAddress().trim())
					&& remarks.equalsIgnoreCase(pwCompany.getRemarks().trim())
					&& regMoney.compareTo(pwCompany.getRegMoney()) == 0 ;
        }
        return false;
    }
}