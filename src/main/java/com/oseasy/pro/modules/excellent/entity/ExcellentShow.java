package com.oseasy.pro.modules.excellent.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 优秀展示Entity.
 * @author 9527
 * @version 2017-06-23
 */
public class ExcellentShow extends DataEntity<ExcellentShow> {
	public final static String  Type_Project="0000000075";
    public final static String  Type_Gcontest="0000000076";
	private static final long serialVersionUID = 1L;
	private String coverImg;		// 封面图片
	private String type;		// 类别：0000000075-项目，0000000076-大赛
	private String foreignId;		// 项目、大赛、科研成果id
	private String content;		// 页面内容
	private String isRelease;		// 是否发布：0-否，1-是
	private String isTop;		// 是否置顶：0-否，1-是
	private String isComment;		// 是否可评论：0-否，1-是
	private String views;		// 浏览量
	private String likes;		// 点赞量
	private String comments;		// 评论量
	private String subType;//项目、大赛小类型，数据字典，project_style、competition_type
	private String managed;//发布过的需要显示在内容管理下面，0-非，1-是
	private Date releaseDate;//发布时间
	private List<String> keywords;
	private AttachMentEntity attachMentEntity;


	public String getManaged() {
		return managed;
	}

	public void setManaged(String managed) {
		this.managed = managed;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}
	public ExcellentShow() {
		super();
	}

	public ExcellentShow(String id) {
		super(id);
	}

	
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=128, message="封面图片长度必须介于 0 和 128 之间")
	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	@Length(min=0, max=64, message="类别：0-项目，1-大赛，2-科研成果长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=64, message="项目、大赛、科研成果id长度必须介于 0 和 64 之间")
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=64, message="是否发布：0-否，1-是长度必须介于 0 和 64 之间")
	public String getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(String isRelease) {
		this.isRelease = isRelease;
	}

	@Length(min=0, max=64, message="是否置顶：0-否，1-是长度必须介于 0 和 64 之间")
	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	@Length(min=0, max=64, message="是否可评论：0-否，1-是长度必须介于 0 和 64 之间")
	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}

	@Length(min=0, max=11, message="浏览量长度必须介于 0 和 11 之间")
	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	@Length(min=0, max=11, message="点赞量长度必须介于 0 和 11 之间")
	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	@Length(min=0, max=11, message="评论量长度必须介于 0 和 11 之间")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	protected List<String> fielSize;
	protected List<String> fielTitle;
	protected List<String> fielType;
	protected List<String> fielFtpUrl;
	
	public List<String> getFielSize() {
		return fielSize;
	}
	public void setFielSize(List<String> fielSize) {
		this.fielSize = fielSize;
	}
	public List<String> getFielTitle() {
		return fielTitle;
	}
	public void setFielTitle(List<String> fielTitle) {
		this.fielTitle = fielTitle;
	}
	public List<String> getFielType() {
		return fielType;
	}
	public void setFielType(List<String> fielType) {
		this.fielType = fielType;
	}
	public List<String> getFielFtpUrl() {
		return fielFtpUrl;
	}
	public void setFielFtpUrl(List<String> fielFtpUrl) {
		this.fielFtpUrl = fielFtpUrl;
	}
}