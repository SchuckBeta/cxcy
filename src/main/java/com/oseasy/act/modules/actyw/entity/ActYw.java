package com.oseasy.act.modules.actyw.entity;

import java.io.Serializable;
import java.util.List;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.config.CoreSval;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.act.modules.actyw.tool.IeYw;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IConfig;
import com.oseasy.act.modules.actyw.tool.apply.IGroup;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程关联Entity.
 *
 * @author chenhao
 * @version 2017-05-23
 */
public class ActYw extends DataEntity<ActYw> implements IActYw, IeYw , Serializable {
    public static final String KEY_SHOW_TIME = "isShowTime";
    private static final long serialVersionUID = 1L;
    private String relId; // 项目ID
    private String groupId; // 自定义流程ID
    private String flowId; // 流程部署ProcessDefinition ID
    private String deploymentId; // 流程部署 Deployment ID
    private Boolean isPreRelease; // 是否预发布
    private Boolean isDeploy; // 是否发布
    private Boolean isShowAxis; // 是否显示到时间轴
    private String isCurr; // 是否当前流程
    private String isNrule; // 是否自定义编号规则

    private String isSend; // 是否能够推送
//    private ActYw modelActYw;

    private Boolean isUpdateYw;
    @Transient
    private Boolean isNtype;//范围类型:0、本地流程；1、全局流程
    @Transient
    private Boolean isPush;//是否为下发流程

    private ActYwGroup group; // 自定义流程
    private ActYwGroup rgroup; // 关联流程
//    private ProjectDeclare projectDeclare; // 项目
    private ProProject proProject;
    private List<ActYwYear> years;
    private String status; // 消息是否发布(0未发布、1发布)
    private String showTime; // 是否显示时间（0否、1是）
    private String keyType; // 类别 民大
    private String numberRuleName; // 编号规则清除
    private String numberRuleText; // 编号规则示例
    private List<SysAttachment> fileInfo;

    private String tenantId;

    List<ActYwGtime> actYwGtimeList;//父节点审核时间

    List<ActYwGtime> childActYwGtimeList;//子节点审核时间

    ActYwYear ActYwYear;//当前时间年份

    public List<ActYwYear> getYears() {
        return years;
    }

    public void setYears(List<ActYwYear> years) {
        this.years = years;
    }

    /**
     * 获取表单主题.
     *
     * @return FormTheme
     */
    @JsonIgnore
    public FormTheme getFtheme() {
        if ((this.group != null) && (this.group.getTheme() != null)) {
            return FormTheme.getById(this.group.getTheme());
        }
        return null;
    }

    /**
     * 获取项目类型.
     *
     * @return FlowProjectType
     */
    @JsonIgnore
    public FlowProjectType getFptype() {
        if ((this.proProject != null) && StringUtil.isNotEmpty(this.proProject.getProType())) {
            String key = StringUtil.replace(this.proProject.getProType(), StringUtil.DOTH, StringUtil.EMPTY);
            return FlowProjectType.getByKey(key);
        }
        return null;
    }

    public List<ActYwGtime> getActYwGtimeList() {
        return actYwGtimeList;
    }

    public void setActYwGtimeList(List<ActYwGtime> actYwGtimeList) {
        this.actYwGtimeList = actYwGtimeList;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    /**
     * 获取项目类型.
     *
     * @return FlowProjectType
     */
    @JsonIgnore
    public String projectTname() {
        if ((this.proProject != null)) {
            return this.proProject.getProjectName();
        }
        return "";
    }

    public List<SysAttachment> getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(List<SysAttachment> fileInfo) {
        this.fileInfo = fileInfo;
    }

    public ActYwYear getActYwYear() {
        return ActYwYear;
    }

    public void setActYwYear(ActYwYear actYwYear) {
        ActYwYear = actYwYear;
    }

//    public ActYw getModelActYw() {
//        return modelActYw;
//    }
//
//    public void setModelActYw(ActYw modelActYw) {
//        this.modelActYw = modelActYw;
//    }


    public ActYwGroup getRgroup() {
        return rgroup;
    }

    public void setRgroup(ActYwGroup rgroup) {
        this.rgroup = rgroup;
    }

    public List<ActYwGtime> getChildActYwGtimeList() {
        return childActYwGtimeList;
    }

    public void setChildActYwGtimeList(List<ActYwGtime> childActYwGtimeList) {
        this.childActYwGtimeList = childActYwGtimeList;
    }

    public void setNtype(Boolean ntype) {
        isNtype = ntype;
    }

    public Boolean getNtype() {
        if((this.isNtype == null)){
            if((this.getGroup() != null)){
                this.isNtype = (Const.YES).equals(this.getGroup().getNtype());
            }else{
                this.isNtype = false;
            }
        }
        return isNtype;
    }

    public void setIsPush(Boolean isPush) {
        isPush = isPush;
    }

    public Boolean getIsPush() {
        if((this.isPush == null)){
            if((this.getGroup() != null)){
                this.isPush = this.getGroup().getIsPush();
            }else{
                this.isPush = false;
            }
        }
        return isPush;
    }


    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public ActYw() {
        super();
    }

    public ActYw(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "项目长度必须介于 1 和 64 之间")
    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    @Length(min = 1, max = 64, message = "自定义流程长度必须介于 1 和 64 之间")
    public String getGroupId() {
        if (StringUtil.isEmpty(this.groupId) && (this.group != null) && StringUtil.isNotEmpty(this.group.getId())) {
            this.groupId = this.group.getId();
        }
        return groupId;
    }

    public Boolean getIsShowAxis() {
        return isShowAxis;
    }

    public void setIsShowAxis(Boolean showAxis) {
        isShowAxis = showAxis;
    }

    public String getIsNrule() {
        if (this.isNrule == null) {
            //this.isNrule = CoreSval.NO;
        }
        return isNrule;
    }

    public void setIsNrule(String isNrule) {
        this.isNrule = isNrule;
    }

    public String getIsCurr() {
        if (this.isCurr == null) {
            //this.isCurr = CoreSval.YES;
        }
        return isCurr;
    }

    public void setIsCurr(String isCurr) {
        this.isCurr = isCurr;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsPreRelease() {
        return isPreRelease;
    }

    public void setIsPreRelease(Boolean isPreRelease) {
        this.isPreRelease = isPreRelease;
    }

    /**
     * @return 获取isDeploy属性值.
     */
    public Boolean getIsDeploy() {
        return isDeploy;
    }

    /**
     * 设置isDeploy属性值.
     */
    public void setIsDeploy(Boolean isDeploy) {
        this.isDeploy = isDeploy;
    }

//    /**
//     * @return 获取projectDeclare属性值.
//     */
//    public ProjectDeclare getProjectDeclare() {
//        return projectDeclare;
//    }
//
//    /**
//     * 设置projectDeclare属性值.
//     */
//    public void setProjectDeclare(ProjectDeclare projectDeclare) {
//        this.projectDeclare = projectDeclare;
//    }

    /**
     * @return 获取group属性值.
     */
    public ActYwGroup getGroup() {
        return group;
    }

    /**
     * 设置group属性值.
     */
    public void setGroup(ActYwGroup group) {
        this.group = group;
    }

    public ProProject getProProject() {
        return proProject;
    }

    public void setProProject(ProProject proProject) {
        this.proProject = proProject;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @JsonIgnore
    @Transient
    public static String genBusinessKey(IActYw actYw, String id) {
        if (actYw == null) {
            return null;
        }
        return actYw.flowType() + StringUtil.MAOH + id;
    }

    @JsonIgnore
    @Transient
    public static String getPkey(IActYw actYw) {
        if (actYw == null) {
            return StringUtil.EMPTY;
        }

        if ((actYw.group() == null) || (StringUtil.isEmpty(actYw.group().keyss()))) {
            return StringUtil.EMPTY;
        }

        if ((actYw.config() == null) || (StringUtil.isEmpty(actYw.config().keyss()))) {
            return StringUtil.EMPTY;
        }
        return IActYw.KEY_SEPTOR_PREFIX + actYw.config().keyss() + IActYw.KEY_SEPTOR + actYw.group().keyss();
    }

    /**
     * 获取pkey中的keyss.
     *
     * @param pkey
     *            流程模型唯一标识=流程标识+项目标识
     * @return String
     */
    @JsonIgnore
    @Transient
    public static String pkeySplitKeyss(String pkey) {
        if ((pkey == null) || (StringUtil.isEmpty(pkey))) {
            return null;
        }
        return pkey.substring(pkey.lastIndexOf(IActYw.KEY_SEPTOR) + IActYw.KEY_SEPTOR.length());
    }

    public String getNumberRuleName() {
        return numberRuleName;
    }

    public void setNumberRuleName(String numberRuleName) {
        this.numberRuleName = numberRuleName;
    }

    public String getNumberRuleText() {
        return numberRuleText;
    }

    public void setNumberRuleText(String numberRuleText) {
        this.numberRuleText = numberRuleText;
    }

    /**
     * 获取pkey中的keyss.
     *
     * @param pkey
     *            流程模型唯一标识=流程标识+项目标识
     * @return String
     */
    @JsonIgnore
    @Transient
    public static String pkeySplitProjectMark(String pkey) {
        if ((pkey == null) || (StringUtil.isEmpty(pkey))) {
            return null;
        }
        return pkey.substring(pkey.indexOf(IActYw.KEY_SEPTOR_PREFIX) + IActYw.KEY_SEPTOR_PREFIX.length(),
                pkey.lastIndexOf(IActYw.KEY_SEPTOR));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowTime() {
        if (showTime == null) {
            return Const.SHOW;
        }
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public Boolean getIsUpdateYw() {
        return isUpdateYw;
    }

    public void setIsUpdateYw(Boolean updateYw) {
        isUpdateYw = updateYw;
    }

    @JsonIgnore
    @Transient
    @Override
    public String id() {
        return this.id;
    }
    @JsonIgnore
    @Transient
    @Override
    public String id(String id) {
        this.id = id;
        return this.id;
    }

    @JsonIgnore
    @Transient
    @Override
    public ActYwGroup group() {
        if (this.group == null) {
            this.group = new ActYwGroup();
        }
        return this.group;
    }

    @JsonIgnore
    @Transient
    @Override
    public ActYwGroup group(IGroup group) {
        this.group = (ActYwGroup) group;
        return this.group;
    }

    @JsonIgnore
    @Transient
    @Override
    public ProProject config() {
        if(this.proProject == null){
            this.proProject = new ProProject();
        }
        return this.proProject;
    }

    @JsonIgnore
    @Transient
    @Override
    public ProProject config(IConfig config) {
        this.proProject = (ProProject) config;
        return this.proProject;
    }

    @JsonIgnore
    @Transient
    @Override
    public FlowType flowType() {
        if (group() == null) {
            return null;
        }
        return group().flowType();
    }

    @JsonIgnore
    @Transient
    @Override
    public String ptype() {
        if (config() == null) {
            return null;
        }
        return config().ptype();
    }

    @JsonIgnore
    @Transient
    @Override
    public FormTheme theme() {
        if (group() == null) {
            return null;
        }
        return group().theme();
    }

    /**
     * 检查是否需要生成编号规则.
     * @param actYw
     * @return boolean
     */
    public static boolean checkNeedNum(ActYw actYw) {
        return (Const.YES).equals(actYw.getIsNrule());
    }

    /**
     * 检查是否需要生成编号规则..
     */
    public static Boolean needNum(ActYw actYw) {
       return (FlowProjectType.PMT_XM).equals(actYw.getFptype()) ||  (FlowProjectType.PMT_DASAI).equals(actYw.getFptype());
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}