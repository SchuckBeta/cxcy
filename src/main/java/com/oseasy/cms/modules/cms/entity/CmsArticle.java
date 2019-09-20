package com.oseasy.cms.modules.cms.entity;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.IidEntity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 一般内容管理Entity.
 * @author liangjie
 * @version 2018-09-04
 */
public class CmsArticle extends DataEntity<CmsArticle> implements IidEntity {

	private static final long serialVersionUID = 1L;
	private Category cmsCategory;
	private CmsArticleData cmsArticleData;
	private String categoryId;		// 归属栏目
	private String module;		// 栏目模块
	private String prId;		// pr_id
	private String prType;		// 项目类型(1项目 7大赛)
	private String keywords;		// 关键词
	private Integer views; //点击数
	private String video;		// 视频展示
	private String image;		// 图片展示
	private String title;		// 简短标题
	private String isLink;		// 是否为超链接
	private String link;		// 超链接地址
	private String isshowlink;		// 是否展示超链接地址
	private String description;		// 摘要
	private String isshowdescription;		// 是否展示摘要
	private String thumbnail;		// 缩略图
	private String writer;		// 作者
	private String isshowwriter;		// 是否展示作者
	private Date articlepulishDate;		// 文章发表时间
	private String isshowpublishdate;		// 是否展示文章发表时间
	private String publishStatus;		// 发布状态（发布，未发布）
	private Date publishStartdate;		// 发布有效期起始日期
	private Date publishEnddate;		// 发布有效期截止日期
	private String publishhforever;		// 发布有效期是否永久
	private String sort;		// 排序
	private String top;		// 置顶
	private Date deadline;		// 过期时间
	private String posid;		// 推荐位
	private String posidweight;		// 推荐位权重
	private String nowDate;
	private String projectName;		// 大赛或者项目名称
    private String ids;       // 查询Ids
	private String snames;  //项目组成员
	private String tnames;  //指导老师
	private String sourceName;     //来源名称
	private String leaderName;     //负责人名称
	private String collgeName;     //院系
	private String projectIntroduction;     //项目简介

	private Date articlepulishStartDate;
	private Date articlepulishEndDate;

	private String queryStr;//查询关键字

	public CmsArticle() {
		super();
	}

	public CmsArticle(String id){
		super(id);
	}
	public CmsArticle(Category category) {
		this();
		this.cmsCategory = category;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getProjectIntroduction() {
		return projectIntroduction;
	}

	public void setProjectIntroduction(String projectIntroduction) {
		this.projectIntroduction = projectIntroduction;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getCollgeName() {
		return collgeName;
	}

	public void setCollgeName(String collgeName) {
		this.collgeName = collgeName;
	}

	public Date getArticlepulishStartDate() {
		return articlepulishStartDate;
	}

	public void setArticlepulishStartDate(Date articlepulishStartDate) {
		this.articlepulishStartDate = articlepulishStartDate;
	}

	public Date getArticlepulishEndDate() {
		return articlepulishEndDate;
	}

	public void setArticlepulishEndDate(Date articlepulishEndDate) {
		this.articlepulishEndDate = articlepulishEndDate;
	}

	public String getSnames() {
		return snames;
	}

	public void setSnames(String snames) {
		this.snames = snames;
	}

	public String getTnames() {
		return tnames;
	}

	public void setTnames(String tnames) {
		this.tnames = tnames;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Length(min=0, max=11, message="归属栏目长度必须介于 0 和 11 之间")
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Length(min=0, max=20, message="栏目模块长度必须介于 0 和 20 之间")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Length(min=0, max=11, message="pr_id长度必须介于 0 和 11 之间")
	public String getPrId() {
		return prId;
	}

	public void setPrId(String prId) {
		this.prId = prId;
	}

	@Length(min=0, max=11, message="项目类型(0大创  1自定义  3互联网大赛  )长度必须介于 0 和 11 之间")
	public String getPrType() {
		return prType;
	}

	public void setPrType(String prType) {
		this.prType = prType;
	}

	@Length(min=0, max=255, message="关键词长度必须介于 0 和 255 之间")
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=255, message="视频展示长度必须介于 0 和 255 之间")
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	@Length(min=0, max=255, message="图片展示长度必须介于 0 和 255 之间")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Length(min=0, max=30, message="简短标题长度必须介于 0 和 30 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min=0, max=11, message="是否为超链接长度必须介于 0 和 11 之间")
	public String getIsLink() {
		return isLink;
	}

	public void setIsLink(String isLink) {
		this.isLink = isLink;
	}

	@Length(min=0, max=30, message="超链接地址长度必须介于 0 和 30 之间")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Length(min=0, max=1, message="是否展示超链接地址长度必须介于 0 和 1 之间")
	public String getIsshowlink() {
		return isshowlink;
	}

	public void setIsshowlink(String isshowlink) {
		this.isshowlink = isshowlink;
	}

	@Length(min=0, max=255, message="摘要长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Length(min=0, max=1, message="是否展示摘要长度必须介于 0 和 1 之间")
	public String getIsshowdescription() {
		return isshowdescription;
	}

	public void setIsshowdescription(String isshowdescription) {
		this.isshowdescription = isshowdescription;
	}

	@Length(min=0, max=255, message="缩略图长度必须介于 0 和 255 之间")
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Length(min=0, max=20, message="作者长度必须介于 0 和 20 之间")
	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	@Length(min=0, max=1, message="是否展示作者长度必须介于 0 和 1 之间")
	public String getIsshowwriter() {
		return isshowwriter;
	}

	public void setIsshowwriter(String isshowwriter) {
		this.isshowwriter = isshowwriter;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getArticlepulishDate() {
		return articlepulishDate;
	}

	public void setArticlepulishDate(Date articlepulishDate) {
		this.articlepulishDate = articlepulishDate;
	}

	@Length(min=0, max=1, message="是否展示文章发表时间长度必须介于 0 和 1 之间")
	public String getIsshowpublishdate() {
		return isshowpublishdate;
	}

	public void setIsshowpublishdate(String isshowpublishdate) {
		this.isshowpublishdate = isshowpublishdate;
	}

	@Length(min=0, max=11, message="发布状态（发布，未发布）长度必须介于 0 和 11 之间")
	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPublishStartdate() {
		return publishStartdate;
	}

	public void setPublishStartdate(Date publishStartdate) {
		this.publishStartdate = publishStartdate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPublishEnddate() {
		return publishEnddate;
	}

	public void setPublishEnddate(Date publishEnddate) {
		this.publishEnddate = publishEnddate;
	}

	@Length(min=0, max=1, message="发布有效期是否永久长度必须介于 0 和 1 之间")
	public String getPublishhforever() {
		return publishhforever;
	}

	public void setPublishhforever(String publishhforever) {
		this.publishhforever = publishhforever;
	}

	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Length(min=0, max=11, message="置顶长度必须介于 0 和 11 之间")
	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Length(min=0, max=11, message="推荐位长度必须介于 0 和 11 之间")
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	@Length(min=0, max=11, message="推荐位权重长度必须介于 0 和 11 之间")
	public String getPosidweight() {
		return posidweight;
	}

	public void setPosidweight(String posidweight) {
		this.posidweight = posidweight;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Category getCmsCategory() {
		return cmsCategory;
	}

	public void setCmsCategory(Category cmsCategory) {
		this.cmsCategory = cmsCategory;
	}

	public CmsArticleData getCmsArticleData() {
		return cmsArticleData;
	}

	public void setCmsArticleData(CmsArticleData cmsArticleData) {
		this.cmsArticleData = cmsArticleData;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
}