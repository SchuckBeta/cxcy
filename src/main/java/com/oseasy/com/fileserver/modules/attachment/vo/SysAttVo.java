/**
 * .
 */

package com.oseasy.com.fileserver.modules.attachment.vo;

import java.io.Serializable;

/**
 * 附件.
 * @author chenhao
 *
 */
public class SysAttVo implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String fielSize;
    protected String fielTitle;
    protected String fielType;
    protected String fielFtpUrl;
    protected String fileTypeEnum;
    protected String fileStepEnum;

    public String getFielSize() {
        return fielSize;
    }
    public void setFielSize(String fielSize) {
        this.fielSize = fielSize;
    }
    public String getFielTitle() {
        return fielTitle;
    }
    public void setFielTitle(String fielTitle) {
        this.fielTitle = fielTitle;
    }
    public String getFielType() {
        return fielType;
    }
    public void setFielType(String fielType) {
        this.fielType = fielType;
    }
    public String getFielFtpUrl() {
        return fielFtpUrl;
    }
    public void setFielFtpUrl(String fielFtpUrl) {
        this.fielFtpUrl = fielFtpUrl;
    }
    public String getFileTypeEnum() {
        return fileTypeEnum;
    }
    public void setFileTypeEnum(String fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }
    public String getFileStepEnum() {
        return fileStepEnum;
    }
    public void setFileStepEnum(String fileStepEnum) {
        this.fileStepEnum = fileStepEnum;
    }
}
