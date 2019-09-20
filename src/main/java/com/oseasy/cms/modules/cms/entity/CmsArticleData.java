package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 文章详情表Entity.
 * @author liangjie
 * @version 2018-09-04
 */
public class CmsArticleData extends DataEntity<CmsArticleData> {

	private static final long serialVersionUID = 1L;
	private String contentId;		// 内容id
	private String content;		// 文章内容
	private String copyfrom;		// 文章来源
	private String isshowcopyfrom;		// 是否展示文章来源
	private String relation;		// 相关文章
	private String allowComment;		// 是否允许评论（0否1是或者空）
	private Integer likes;            //点赞

	public CmsArticleData() {
		super();
	}

	public CmsArticleData(String id){
		super(id);
	}

	@Length(min=0, max=11, message="内容id长度必须介于 0 和 11 之间")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	@Length(min=0, max=255, message="文章来源长度必须介于 0 和 255 之间")
	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	@Length(min=0, max=1, message="是否展示文章来源长度必须介于 0 和 1 之间")
	public String getIsshowcopyfrom() {
		return isshowcopyfrom;
	}

	public void setIsshowcopyfrom(String isshowcopyfrom) {
		this.isshowcopyfrom = isshowcopyfrom;
	}

	@Length(min=0, max=255, message="相关文章长度必须介于 0 和 255 之间")
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Length(min=0, max=1, message="是否允许评论（0否1是或者空）长度必须介于 0 和 1 之间")
	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}