package com.oseasy.com.mqserver.modules.oa.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * 院系通知过滤Entity.
 * @author chenhao
 * @version 2018-08-16
 */
public class OaNotifyReoffilter extends DataEntity<OaNotifyReoffilter> {

	private static final long serialVersionUID = 1L;
	private OaNotify oaNotify;		// 通知通告ID
	private User user;		// 接受人
	private String readFlag;		// 阅读标记(0未阅读，1已阅读)
	private Date readDate;		// 阅读时间
	private String operateFlag;		// 操作标记（0未操作，1已操作）

	public OaNotifyReoffilter() {
		super();
	}

	public OaNotifyReoffilter(OaNotify oaNotify) {
        super();
        this.oaNotify = oaNotify;
    }

    public OaNotifyReoffilter(String id){
		super(id);
	}

	public OaNotify getOaNotify() {
        return oaNotify;
    }

    public void setOaNotify(OaNotify oaNotify) {
        this.oaNotify = oaNotify;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Length(min=0, max=1, message="阅读标记(0未阅读，1已阅读)长度必须介于 0 和 1 之间")
	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	@Length(min=0, max=1, message="操作标记（0未操作，1已操作）长度必须介于 0 和 1 之间")
	public String getOperateFlag() {
		return operateFlag;
	}

	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
	}

}