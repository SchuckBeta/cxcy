package com.oseasy.cms.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttVo;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 留言Entity.
 * @author chenh
 * @version 2018-09-04
 */
public class CmsGuestbook extends DataEntity<CmsGuestbook> {

	private static final long serialVersionUID = 1L;
	private String type;		// 留言分类（0咨询，1问题反馈）
	private String content;		// 留言内容
	private String name;		// 姓名
	private String email;		// 邮箱
	private String phone;		// 手机号
	private String qq;		// QQ
	private String auditstatus;		// 审核状态（0待审核，1审核通过，2审核不通过）
	private String isRecommend;		// 是否推荐（0否1是）
	private User createUser;		// 创建人
	private User reUser;		// 回复人
	private Date reDate;		// 回复时间
	private String reContent;		// 回复内容
    private List<String> ids;       // 查询Ids
    private List<SysAttVo> files;       //附件保存参数
    private List<SysAttachment> filess;       //附件

	public CmsGuestbook() {
		super();
	}

	public CmsGuestbook(String id){
		super(id);
	}
	public CmsGuestbook(List<String> ids){
		super();
		this.ids = ids;
	}

	@Length(min=0, max=1, message="留言分类（咨询，问题反馈）长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=255, message="留言内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=100, message="姓名长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=100, message="邮箱长度必须介于 0 和 100 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min=0, max=100, message="手机号长度必须介于 0 和 100 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=50, message="QQ长度必须介于 0 和 50 之间")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Length(min=0, max=1, message="审核状态（待审核，审核通过，审核不通过）长度必须介于 0 和 1 之间")
	public String getAuditstatus() {
		return auditstatus;
	}

	public void setAuditstatus(String auditstatus) {
		this.auditstatus = auditstatus;
	}

	@Length(min=0, max=1, message="是否推荐（0否1是）长度必须介于 0 和 1 之间")
	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public User getReUser() {
		return reUser;
	}

	public void setReUser(User reUser) {
		this.reUser = reUser;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReDate() {
		return reDate;
	}

	public void setReDate(Date reDate) {
		this.reDate = reDate;
	}

	@Length(min=0, max=64, message="回复内容长度必须介于 0 和 64 之间")
	public String getReContent() {
		return reContent;
	}

	public void setReContent(String reContent) {
		this.reContent = reContent;
	}

    @JsonIgnore
    @XmlTransient
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

	public User getCreateUser() {
		if(this.createBy != null){
			this.createUser = this.createBy;
		}
		return createUser;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

    public List<SysAttVo> getFiles() {
        return files;
    }

    public void setFiles(List<SysAttVo> files) {
        this.files = files;
    }

    public List<SysAttachment> getFiless() {
        return filess;
    }

    public void setFiless(List<SysAttachment> filess) {
        this.filess = filess;
    }
}