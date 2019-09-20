package com.oseasy.cms.modules.cms.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.oseasy.com.pcore.common.persistence.DataEntity;

/**
 * 首页资源Entity
 * @author daichanggeng
 * @version 2017-04-14
 */
public class CmsIndexResource extends DataEntity<CmsIndexResource> {

	private static final long serialVersionUID = 1L;
	private CmsIndexRegion cmsIndexRegion;		// 区域
	private String regionType;		// 区域类型
	private String resName;		// 资源名称
	private String resState;		// 状态：0：无效 1：有效
	private String resType;		// 类型 ：图片 2：页面 3视频 4音频 5其他
	private String resSort;		// 排序
	private String title;		// 标题
	private String resModel;   //资源模式：0-参数模式，1-模板模式,2-标准模式
	//模板模式资源
	private String tpl;			// 模板模式内容
	private String tplJson;		// 模板模式内容参数Json
	private String tplUrl;		// 模板模式文件地址

	//标准模式资源
	private String genId;		// 标准模式模板ID
	private String genJson;		// 标准模式内容参数Json

	//参数模式资源
	private String content;		// 内容
	private String botton1Name;		// 按钮1名称
	private String resUrl1;		// 资源地址1
	private String jumpUrl1;		// 跳转地址1
	private String botton2Name;		// 按钮2名称
	private String resUrl2;		// 资源地址2
	private String jumpUrl2;		// 跳转地址2
	private String botton3Name;		// 按钮3名称
	private String resUrl3;		// 资源地址3
	private String jumpUrl3;		// 跳转地址3
	private String reserve1;		// 预留字段1
	private String reserve2;		// 预留字段2
	private String reserve3;		// 预留字段3

	public CmsIndexResource() {
		super();
	}

	public CmsIndexResource(String id) {
		super(id);
	}

	public CmsIndexResource(CmsIndexRegion cmsIndexRegion) {
		super();
		this.cmsIndexRegion = cmsIndexRegion;
	}

	public CmsIndexResource(String siteId, String categoryId, String regionId, String id) {
		super();
		this.id = id;
		this.cmsIndexRegion = new CmsIndexRegion(regionId, new Category(categoryId, new Site(siteId)));
	}


	public String getResModel() {
		return resModel;
	}

	public void setResModel(String resModel) {
		this.resModel = resModel;
	}

	public CmsIndexRegion getCmsIndexRegion() {
		return cmsIndexRegion;
	}

	public void setCmsIndexRegion(CmsIndexRegion cmsIndexRegion) {
		this.cmsIndexRegion = cmsIndexRegion;
	}

	@Length(min=1, max=64, message="区域类型长度必须介于 1 和 64 之间")
	public String getRegionType() {
		return regionType;
	}

	public void setRegionType(String regionType) {
		this.regionType = regionType;
	}

	@Length(min=1, max=64, message="资源名称长度必须介于 1 和 64 之间")
	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	@Length(min=1, max=64, message="状态：0：无效 1：有效长度必须介于 1 和 64 之间")
	public String getResState() {
		return resState;
	}

	public void setResState(String resState) {
		this.resState = resState;
	}

	@Length(min=1, max=64, message="类型 1：图片 2：页面 3视频 4音频 5其他长度必须介于 1 和 64 之间")
	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	@Length(min=1, max=64, message="排序长度必须介于 1 和 64 之间")
	public String getResSort() {
		return resSort;
	}

	public void setResSort(String resSort) {
		this.resSort = resSort;
	}

	@Length(min=0, max=64, message="标题长度必须介于 0 和 64 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=64, message="按钮1名称长度必须介于 0 和 64 之间")
	public String getBotton1Name() {
		return botton1Name;
	}

	public void setBotton1Name(String botton1Name) {
		this.botton1Name = botton1Name;
	}

	@Length(min=0, max=255, message="资源地址1长度必须介于 0 和 255 之间")
	public String getResUrl1() {
		return resUrl1;
	}

	public void setResUrl1(String resUrl1) {
		this.resUrl1 = resUrl1;
	}

	@Length(min=0, max=255, message="跳转地址1长度必须介于 0 和 255 之间")
	public String getJumpUrl1() {
		return jumpUrl1;
	}

	public void setJumpUrl1(String jumpUrl1) {
		this.jumpUrl1 = jumpUrl1;
	}

	@Length(min=0, max=64, message="按钮2名称长度必须介于 0 和 64 之间")
	public String getBotton2Name() {
		return botton2Name;
	}

	public void setBotton2Name(String botton2Name) {
		this.botton2Name = botton2Name;
	}

	@Length(min=0, max=255, message="资源地址2长度必须介于 0 和 255 之间")
	public String getResUrl2() {
		return resUrl2;
	}

	public void setResUrl2(String resUrl2) {
		this.resUrl2 = resUrl2;
	}

	@Length(min=0, max=255, message="跳转地址2长度必须介于 0 和 255 之间")
	public String getJumpUrl2() {
		return jumpUrl2;
	}

	public void setJumpUrl2(String jumpUrl2) {
		this.jumpUrl2 = jumpUrl2;
	}

	@Length(min=0, max=64, message="按钮3名称长度必须介于 0 和 64 之间")
	public String getBotton3Name() {
		return botton3Name;
	}

	public void setBotton3Name(String botton3Name) {
		this.botton3Name = botton3Name;
	}

	@Length(min=0, max=255, message="资源地址3长度必须介于 0 和 255 之间")
	public String getResUrl3() {
		return resUrl3;
	}

	public void setResUrl3(String resUrl3) {
		this.resUrl3 = resUrl3;
	}

	@Length(min=0, max=255, message="跳转地址3长度必须介于 0 和 255 之间")
	public String getJumpUrl3() {
		return jumpUrl3;
	}

	public void setJumpUrl3(String jumpUrl3) {
		this.jumpUrl3 = jumpUrl3;
	}

	@Length(min=0, max=255, message="预留字段1长度必须介于 0 和 255 之间")
	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	@Length(min=0, max=255, message="预留字段2长度必须介于 0 和 255 之间")
	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	@Length(min=0, max=255, message="预留字段3长度必须介于 0 和 255 之间")
	public String getReserve3() {
		return reserve3;
	}

	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}

	public String getTpl() {
		return tpl;
	}

	public void setTpl(String tpl) {
		this.tpl = tpl;
	}

	public String getTplJson() {
		return tplJson;
	}

	public void setTplJson(String tplJson) {
		this.tplJson = tplJson;
	}

	public String getTplUrl() {
		return tplUrl;
	}

	public void setTplUrl(String tplUrl) {
		this.tplUrl = tplUrl;
	}

	public String getGenId() {
		return genId;
	}

	public void setGenId(String genId) {
		this.genId = genId;
	}

	public String getGenJson() {
		return genJson;
	}

	public void setGenJson(String genJson) {
		this.genJson = genJson;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}