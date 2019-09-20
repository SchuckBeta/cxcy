package com.oseasy.com.fileserver.modules.attachment.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 附件信息表Entity
 *
 * @author zy
 * @version 2017-03-23
 */
public class SysAttachment extends DataEntity<SysAttachment> {

    private static final long serialVersionUID = 1L;
    private String ftype; // 类型：0项目，1 项目通告，2 大赛，3大赛通告
    private String ffileStep; // 标识文件是中期检查报告或者阶段报告等,枚举
    private FileTypeEnum type; // 类型：0项目，1 项目通告，2 大赛，3大赛通告
    private FileStepEnum fileStep; // 标识文件是中期检查报告或者阶段报告等,枚举
    private List<String> fileSteps; // 标识文件是中期检查报告或者阶段报告等,枚举
    private String uid; // 对应表的id：如项目表ID
    private List<String> uids; // 对应表的id：如项目表ID
    private String url; // url
    private String name; // 名称
    private String userName; // 上传人
    private String size; // 附件大小
    private String suffix; // 后缀
    @SuppressWarnings("unused")
    private String imgType;// 小图标
    private String viewUrl;//word、图片预览链接
    private String ftpUrl;

    private String tempUrl;
    private String tempFtpUrl;

    public String getTempFtpUrl() {
        if(StringUtil.isEmpty(tempFtpUrl)){
            return ftpUrl;
        }
        return tempFtpUrl;
    }

    public void setTempFtpUrl(String tempFtpUrl) {
        this.tempFtpUrl = tempFtpUrl;
    }

    public String getTempUrl() {
        if(StringUtil.isEmpty(tempUrl)){
            return url;
        }
        return tempUrl;
    }

    public void setTempUrl(String tempUrl) {
        this.tempUrl = tempUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    private String gnodeId;

    public String getGnodeId() {
        return gnodeId;
    }

    public void setGnodeId(String gnodeId) {
        this.gnodeId = gnodeId;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFfileStep() {
        return ffileStep;
    }

    public void setFfileStep(String ffileStep) {
        this.ffileStep = ffileStep;
    }

    public String getFtpUrl() {
        return FtpUtil.ftpImgUrl(url);
    }

    public void setFtpUrl(String ftpUrl) {
        this.ftpUrl = ftpUrl;
    }

    public SysAttachment() {
        super();
    }

    public List<String> getFileSteps() {
        return fileSteps;
    }

    public void setFileSteps(List<String> fileSteps) {
        this.fileSteps = fileSteps;
    }

    public List<String> getUids() {
        return uids;
    }

    public void setUids(List<String> uids) {
        this.uids = uids;
    }

    public String getImgType() {
        if (suffix == null) {
            return "unknow";
        }
        String extname = suffix.toLowerCase();
        switch (extname) {
            case "xls":
            case "xlsx":
                extname = "excel";
                break;
            case "doc":
            case "docx":
                extname = "word";
                break;
            case "ppt":
            case "pptx":
                extname = "ppt";
                break;
            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
            case "bmp":
                extname = "image";
                break;
            case "rar":
            case "zip":
            case "txt":
            case "project":
                break;
            default:
                extname = "unknow";
        }
        return extname;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public SysAttachment(String id) {
        super(id);
    }

    public SysAttachment(String uid, FileTypeEnum type) {
        super();
        this.type = type;
        this.uid = uid;
    }

    public SysAttachment(String uid, FileTypeEnum type, String ftype) {
        super();
        this.ftype = ftype;
        this.type = type;
        this.uid = uid;
    }

    public FileTypeEnum getType() {
        if ((this.type == null) && StringUtil.isNotEmpty(this.ftype)) {
            type = FileTypeEnum.getByValue(this.ftype);
        }
        return type;
    }

    public void setType(FileTypeEnum type) {
        this.type = type;
    }

    public FileStepEnum getFileStep() {
        if ((this.fileStep == null) && StringUtil.isNotEmpty(this.ffileStep)) {
            fileStep = FileStepEnum.getByValue(this.ffileStep);
        }
        return fileStep;
    }

    public void setFileStep(FileStepEnum fileStep) {
        this.fileStep = fileStep;
    }

    @Length(min = 1, max = 64, message = "对应表的id：如项目表ID长度必须介于 1 和 64 之间")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Length(min = 0, max = 128, message = "url长度必须介于 0 和 128 之间")
    public String getUrl() {
        return url;
    }

    public String getFileName() {
        if (StringUtil.isNotEmpty(url)) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }

    public String getRemotePath() {
        if (StringUtil.isNotEmpty(url)) {
            return url.substring(0, url.lastIndexOf("/") + 1);
        }
        return null;
    }

    public String getName(String suffix) {
        if (StringUtil.isNotEmpty(name)) {
            int idx = name.lastIndexOf(FileUtil.DOT);
            if (idx != -1) {
                return name.substring(0, idx) + FileUtil.LINE_M + suffix + name.substring(idx, name.length());
            }
        }
        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Length(min = 1, max = 128, message = "名称长度必须介于 1 和 128 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 32, message = "附件大小长度必须介于 0 和 32 之间")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Length(min = 1, max = 12, message = "后缀长度必须介于 1 和 12 之间")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "SysAttachment{" +
                "ftype='" + ftype + '\'' +
                ", ffileStep='" + ffileStep + '\'' +
                ", type=" + type +
                ", fileStep=" + fileStep +
                ", fileSteps=" + fileSteps +
                ", uid='" + uid + '\'' +
                ", uids=" + uids +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", size='" + size + '\'' +
                ", suffix='" + suffix + '\'' +
                ", imgType='" + imgType + '\'' +
                ", viewUrl='" + viewUrl + '\'' +
                ", ftpUrl='" + ftpUrl + '\'' +
                ", gnodeId='" + gnodeId + '\'' +
                '}';
    }
}