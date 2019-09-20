/**
 *
 */
package com.oseasy.com.pcore.common.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * 数据Entity类
 * 接收前台附件数据
 * @version 2014-05-16
 */
public final class AttachMentEntity implements Serializable {
	protected List<String> fielSize;
	protected List<String> fielTitle;
	protected List<String> fielType;
	protected List<String> fielFtpUrl;
	protected List<String> fileTypeEnum;
	protected List<String> fileStepEnum;
	protected String gnodeId;

	public String getGnodeId() {
		return gnodeId;
	}

	public void setGnodeId(String gnodeId) {
		this.gnodeId = gnodeId;
	}

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

	public List<String> getFileTypeEnum() {
		return fileTypeEnum;
	}

	public void setFileTypeEnum(List<String> fileTypeEnum) {
		this.fileTypeEnum = fileTypeEnum;
	}

	public List<String> getFileStepEnum() {
		return fileStepEnum;
	}

	public void setFileStepEnum(List<String> fileStepEnum) {
		this.fileStepEnum = fileStepEnum;
	}

}
