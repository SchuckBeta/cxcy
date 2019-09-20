package com.oseasy.cms.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 评论Entity.
 * @author chenh
 * @version 2018-09-04
 */
public class CmsConmment extends DataEntity<CmsConmment> {

	private static final long serialVersionUID = 1L;
	private Category category;		// 栏目编号
	private CmsArticle cnt;		// 栏目内容的编号
	private User user;		// 评论人
	private Integer likes;		// 点赞量
	private String content;		// 评论内容
    private User auUser;        // 审核人
	private Date auditDate;		// 审核时间
	private String reply;		// 回复评论
	private User reUser;		// 回复人
	private Date reDate;		// 回复时间
	private String auditstatus;		// 审核状态（待审核，审核通过，审核不通过）
	private String isRecommend;		// 是否推荐（0否1是）
	private String queryStr;		// 查询字符串
	private List<String> ids;		// 查询Ids
	private List<CmsConmmentLikes> ctLikes;		//点赞人列表
	private List<String> ctLikeIds;		//点赞人ID列表


	public CmsConmment() {
		super();
	}
	public CmsConmment(List<String> ids) {
		super();
		this.ids = ids;
	}

	public CmsConmment(String id){
		super(id);
	}

	public Date getReDate() {
        return reDate;
    }
    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }
    public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public CmsArticle getCnt() {
		return cnt;
	}

	public void setCnt(CmsArticle cnt) {
		this.cnt = cnt;
	}

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuUser() {
        return auUser;
    }

    public List<String> getCtLikeIds() {
        if(StringUtil.checkNotEmpty(this.getCtLikes())){
            this.ctLikeIds = Lists.newArrayList();
            for (CmsConmmentLikes ctLike : this.ctLikes) {
                if((ctLike.getUser() == null) || StringUtil.isEmpty(ctLike.getUser().getId())){
                    continue;
                }
                this.ctLikeIds.add(ctLike.getUser().getId());
            }
        }
        return this.ctLikeIds;
    }
    public void setAuUser(User auUser) {
        this.auUser = auUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public User getReUser() {
        return reUser;
    }
    public void setReUser(User reUser) {
        this.reUser = reUser;
    }
    @Length(min=0, max=255, message="回复评论长度必须介于 0 和 255 之间")
	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	@Length(min=0, max=10, message="审核状态（待审核，审核通过，审核不通过）长度必须介于 0 和 10 之间")
	public String getAuditstatus() {
		return auditstatus;
	}

	public void setAuditstatus(String auditstatus) {
		this.auditstatus = auditstatus;
	}

	@Length(min=0, max=10, message="是否推荐（0否1是）长度必须介于 0 和 10 之间")
	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

    public List<CmsConmmentLikes> getCtLikes() {
        return ctLikes;
    }

    public void setCtLikes(List<CmsConmmentLikes> ctLikes) {
        this.ctLikes = ctLikes;
    }

    @JsonIgnore
    @XmlTransient
	public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    @JsonIgnore
	@XmlTransient
    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}