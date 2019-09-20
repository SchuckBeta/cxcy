package com.oseasy.pro.modules.course.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.oseasy.cms.modules.cms.entity.CmsArticle;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.Collections3;
import com.oseasy.util.common.utils.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * 课程主表Entity.
 * @author 张正
 * @version 2017-06-28
 */
public class Course extends DataEntity<Course> {

	private static final long serialVersionUID = 1L;
	private String name;		// 课程名称
	private String coverImg;		// 封面图片
	private String video;		// 课程视频ftp地址
	private String description;		// 课程描述
	private String type;		// 课程类型分类
	private String status;		// 状态分类
	private String courseSummary;		// 课程介绍
	private String teacherSummary;		// 教师简介
	private String publishFlag;		// 是否发布：0-否，1-是
	private String topFlag;		// 是否置顶：0-否，1-是
	private String commentFlag;		// 是否可评论：0-否，1-是
	private String recommendFlag;    // 是否推荐：0否，1是
	private int downloads;		// 下载量
	private int views;		// 浏览量
	private int likes;		// 点赞量
	private int comments;		// 评论量
	private Date publishDate;		// 发布时间
	private String teasInfo;//授课教师信息
	private String categoryId;  //根据专业课程分类查询用
	private CmsArticle cmsArticle; // 内容文章冗余字段
	private String cmsArtileId;

	private List<CourseCategory> categoryList = Lists.newArrayList();  //拥有的课程专业分类
	private List<CourseTeacher>  teacherList =  Lists.newArrayList();  //拥有的授课教师
	private List<SysAttachment> attachmentList = Lists.newArrayList(); //拥有的课件

	private List<String> categoryValueList = Lists.newArrayList();
	private List<String> typeList = Lists.newArrayList();
	private List<String> statusList = Lists.newArrayList();

	private AttachMentEntity attachMentEntity;


	//授课教师姓名
	public String getTeasNames() {
		if(StringUtil.isNotEmpty(teasInfo)){
			String[] ss=teasInfo.split("-");
			if(ss!=null&&ss.length==2){
				return ss[0];
			}
		}
		return null;
	}
	//授课教师职称
	public String getTeasTitles() {
		if(StringUtil.isNotEmpty(teasInfo)){
			String[] ss=teasInfo.split("-");
			if(ss!=null&&ss.length==2){
				return ss[1];
			}
		}
		return null;
	}
	public String getTeasInfo() {
		return teasInfo;
	}

	public void setTeasInfo(String teasInfo) {
		this.teasInfo = teasInfo;
	}

	public Course() {
		super();
	}

	public Course(String id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CmsArticle getCmsArticle() {
		return cmsArticle;
	}

	public void setCmsArticle(CmsArticle cmsArticle) {
		this.cmsArticle = cmsArticle;
	}

	public String getCmsArtileId() {
		return cmsArtileId;
	}

	public void setCmsArtileId(String cmsArtileId) {
		this.cmsArtileId = cmsArtileId;
	}

	public String getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(String coverImg) {
		this.coverImg = coverImg;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCourseSummary() {
		return courseSummary;
	}

	public void setCourseSummary(String courseSummary) {
		this.courseSummary = courseSummary;
	}

	public String getTeacherSummary() {
		return teacherSummary;
	}

	public void setTeacherSummary(String teacherSummary) {
		this.teacherSummary = teacherSummary;
	}

	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public String getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(String topFlag) {
		this.topFlag = topFlag;
	}

	public String getCommentFlag() {
		return commentFlag;
	}

	public void setCommentFlag(String commentFlag) {
		this.commentFlag = commentFlag;
	}

	public String getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(String recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public List<CourseCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CourseCategory> categoryList) {
		this.categoryList = categoryList;
	}

	public List<CourseTeacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<CourseTeacher> teacherList) {
		this.teacherList = teacherList;
	}

	public List<SysAttachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<SysAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	//课程拥有的专业课程分类字符串, 多个专业课程分类用','分隔.
	public String getCategoryNames() {
		return Collections3.extractToString(categoryList, "label", ",");
	}

	//课程拥有的授课老师姓名字符串, 多个授课老师姓名用','分隔.
   public String getTeacherNames() {
	   return Collections3.extractToString(teacherList, "teacherName", ",");
   }

	public List<String> getCategoryValueList() {
		for(CourseCategory courseCategory:categoryList) {
			categoryValueList.add(courseCategory.getValue());
		}
		return categoryValueList;
	}

	public void setCategoryValueList(List<String> categoryValueList) {
		this.categoryValueList = categoryValueList;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public AttachMentEntity getAttachMentEntity() {
		return attachMentEntity;
	}

	public void setAttachMentEntity(AttachMentEntity attachMentEntity) {
		this.attachMentEntity = attachMentEntity;
	}
}