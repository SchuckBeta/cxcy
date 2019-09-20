package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.apply.IForm;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormType;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 项目流程表单Entity.
 *
 * @author chenhao
 * @version 2017-05-23
 */
public class ActYwForm extends DataEntity<ActYwForm> implements IForm {

    private static final long serialVersionUID = 1L;
    protected String name; // 业务模块（1-立项审核，2-中期检查，3-结项审核）
    protected String proType; // 项目类别
    private List<String> flowTypes;//
    protected String flowType; // 流程类别 FlowType
    protected Integer theme; // 主题
    protected ActYwGtheme gtheme; // 主题
    protected String type; // 表单类别 FormType
    protected String sgtype; // 审核类别 RegType
    protected String styleType; // 样式类型
    protected String clientType; // 客户端类型
    protected String model; // 模式：（0-文件模板，1-地址模板，2-html模板）
    protected String params; // 表单模板文件参数
    protected String path; // 表单模板文件路径
    protected String content; // 表单模板内容
    protected Office office; // 节点所属机构:1、默认（系统全局）；
    protected String listId; // 列表ID
    protected ActYwForm listForm; // 列表Form
    protected String fname; // 模板文件名称（不含前缀）
    @Transient
    private List<Integer> themes; // 查询多种主题
    private String queryStr; // 查询字符串

    public ActYwForm() {
        super();
    }

    public ActYwForm(String id) {
        super(id);
    }

    public String getProType() {
        return proType;
    }

    public ActYwForm getListForm() {
        return listForm;
    }

    public void setListForm(ActYwForm listForm) {
        this.listForm = listForm;
    }

    public ActYwGtheme getGtheme() {
        return gtheme;
    }

    public void setGtheme(ActYwGtheme gtheme) {
        this.gtheme = gtheme;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public List<Integer> getThemes() {
        return themes;
    }

    public void setThemes(List<Integer> themes) {
        this.themes = themes;
    }

    public String[] getProTypes() {
        String[] proTypes = null;
        if (StringUtil.isNotEmpty(this.proType) && (this.proType.length() > 1)) {
            proTypes = this.proType.substring(0, (this.proType.length() - 1)).split(",");
        }
        return proTypes;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public void setProType(FlowProjectType[] flowProjectTypes) {
        StringBuffer proType = new StringBuffer();
        for (FlowProjectType flowProjectType : flowProjectTypes) {
            proType.append(flowProjectType.getKey());
            proType.append(StringUtil.DOTH);
        }
        this.proType = proType.toString();
    }

    public String getType() {
        return type;
    }

    public String getStyleType() {
        return styleType;
    }

    public String getSgtype() {
        return sgtype;
    }

    public void setSgtype(String sgtype) {
        this.sgtype = sgtype;
    }

    public String getEstyleType() {
        if (StringUtil.isEmpty(this.styleType) && StringUtil.isNotEmpty(this.type)) {
            FormType formType = FormType.getByKey(this.type);
            if (formType != null) {
                this.styleType = formType.getStyle().getKey();
            }
        }
        return styleType;
    }

    public String getClientType() {
        return this.clientType;
    }

    public String getEclientType() {
        if (StringUtil.isEmpty(this.clientType) && StringUtil.isNotEmpty(this.type)) {
            FormType formType = FormType.getByKey(this.type);
            if (formType != null) {
                this.clientType = formType.getClient().getKey();
            }
        }
        return this.clientType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getFlowTypes() {
        if (StringUtils.isNotBlank(flowType)) {
            String[] flowTypeArray = StringUtil.split(flowType, StringUtil.DOTH);
            flowTypes = Lists.newArrayList();
            for (String fType : flowTypeArray) {
                flowTypes.add(fType);
            }
        }
        return flowTypes;
    }

    public void setFlowTypes(List<String> flowTypes) {
        if ((flowTypes != null) && (flowTypes.size() > 0)) {
            StringBuffer strbuff = new StringBuffer();
            for (String fType : flowTypes) {
                strbuff.append(fType);
                strbuff.append(StringUtil.DOTH);
            }
            String curfType = strbuff.substring(0, strbuff.lastIndexOf(StringUtil.DOTH));
            setFlowType(curfType);
        }
        this.flowTypes = flowTypes;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setStyleType(String styleType) {
        this.styleType = styleType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Length(min = 0, max = 64, message = "表单模板文件参数长度必须介于 0 和 64 之间")
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Length(min = 0, max = 64, message = "表单模板文件路径长度必须介于 0 和 64 之间")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return 获取content属性值.
     */
    public String getContent() {
        return content;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 设置content属性值.
     */
    public void setContent(String content) {
        this.content = content;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String styleType() {
        return this.styleType;
    }

    @Override
    public String ipath() {
        return this.path;
    }
}