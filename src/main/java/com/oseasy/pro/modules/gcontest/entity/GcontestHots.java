package com.oseasy.pro.modules.gcontest.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 大赛热点Entity.
 * @author 9527
 * @version 2017-07-12
 */
public class GcontestHots extends DataEntity<GcontestHots> {

	private static final long serialVersionUID = 1L;
	private String coverImg;		// 封面图片
	private String content;		// 页面内容
	private String isRelease;		// 是否发布：0-否，1-是
	private String isTop;		// 是否置顶：0-否，1-是
	private String isComment;		// 是否可评论：0-否，1-是
	private String views;		// 浏览量
	private String likes;		// 点赞量
	private String comments;		// 评论量
	private Date releaseDate;		// 发布时间
	private String source;//来源
	private String title;//标题
	private List<String> keywords;
	
	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public GcontestHots() {
		super();
	}

	public GcontestHots(String id) {
		super(id);
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Length(min=0, max=128, message="封面图片长度必须介于 0 和 128 之间")
	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

}