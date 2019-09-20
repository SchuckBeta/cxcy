package com.oseasy.act.modules.actyw.entity;

import java.util.List;

import com.oseasy.util.common.utils.IidEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oseasy.act.modules.actyw.tool.apply.IGroup;
import com.oseasy.act.modules.actyw.tool.process.cmd.ActYwRtpl;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 自定义流程Entity.
 * @author chenhao
 * @version 2017-05-23
 */
public class ActYwGroup extends DataEntity<ActYwGroup> implements ActYwRtpl, IGroup, IidEntity {
  private static final long serialVersionUID = 1L;
  public static final String GROUP_DEPLOY_0 = "0";
  public static final String GROUP_DEPLOY_1 = "1";
  public static final String GROUP_VERSION = "1.0.0";
  public static final String JK_ACTYW_ID = "actywId";//YWID标识
  public static final String ACT_YW_ID = "actYwId";
  public static final String JK_GROUP = "group";
  public static final String JK_GROUP_ID = "groupId";
  public static final String JK_GNODE = "group";
  public static final String JK_GNODE_ID = "gnodeId";

  private String name; // 流程名称
  private String status; // 状态:0、未启用；1、启用
  private String flowId; // 流程模型ID
  private String flowType; // 流程类型
    private String type; // 项目类型
    private String ntype; // 范围类型:0、本地流程；1、全局流程
    private Boolean isPush; // 是否下发流程:0、否；1、是
  private String keyss; // 流程唯一标识
  private Integer theme; //表单组类型
  private Boolean temp; //是否临时数据
  private String author; // 流程作者
  private String version; // 流程版本
  private Integer sort;   // 排序
  private String uiHtml;// 设计前端HTML
  private String uiJson;// 设计前端JSON
    @JsonIgnore
  private List<ActYw> actYws; // 项目流程

    private String relationName; //关联校流程名称

    private String relationId; //关联校流程id

    private String actYwStepId; //流程Id

    private String step; //流程步骤

  @JsonIgnore
  private Long ywsize;   // actYws记录数
  @JsonIgnore
  private ApiTstatus<ActYwGnode> rstatus;   // 状态

    @JsonIgnore
    private List<ActYwGnode> gnodeList;   // 流程下节点
  @Transient
  private Boolean showZp;//是否显示指派功能
    @Transient
    private Boolean showWp;//是否显示委派功能
    @Transient
    private Boolean isnprfalil;//是否省关联流程且已校验失败（流程、项目已发布）

    private String tenantId;

    public String getNtype() {
        return ntype;
    }

    public void setNtype(String ntype) {
        this.ntype = ntype;
    }

    public Boolean getIsPush() {
        if(this.isPush == null){
            return isPush;
        }
        return isPush;
    }

    public void setIsPush(Boolean isPush) {
        this.isPush = isPush;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getActYwStepId() {
        return actYwStepId;
    }

    public void setActYwStepId(String actYwStepId) {
        this.actYwStepId = actYwStepId;
    }

    public ActYwGroup() {
    super();
  }

    public ActYwGroup(String id) {
    super(id);
  }

    public List<ActYwGnode> getGnodeList() {
        return gnodeList;
    }

    public void setGnodeList(List<ActYwGnode> gnodeList) {
        this.gnodeList = gnodeList;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public List<ActYw> getActYws() {
    return actYws;
  }

  public void setActYw(List<ActYw> actYws) {
    this.actYws = actYws;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFlowType() {
    return flowType;
  }

  public void setFlowType(String flowType) {
    this.flowType = flowType;
  }

    public Boolean getIsnprfalil() {
        if(this.isnprfalil == null){
            this.isnprfalil = false;
        }
        return isnprfalil;
    }

    public void setIsnprfalil(Boolean isnprfalil) {
        this.isnprfalil = isnprfalil;
    }

    @Transient
  public Boolean getShowZp() {
      if((this.showZp == null) && StringUtil.isNotEmpty(this.type)){
          this.showZp = (FlowProjectType.showZp()).contains(this.type);
      }
      return this.showZp;
  }

  @Transient
  public Boolean getShowWp() {
      if((this.showWp == null) && (this.theme != null)){
          this.showWp = (FormTheme.showWp()).contains(this.theme.toString());
      }
      return this.showWp;
  }

@JsonIgnore
  @Transient
  public FlowType flowType() {
      if(StringUtil.isNotEmpty(this.flowType)){
          return FlowType.getByKey(this.flowType);
      }
      return null;
  }

  @JsonIgnore
  @Transient
  public FlowType flowType(FlowType flowType) {
      this.flowType = flowType.getKey();
      return flowType;
  }

  @JsonIgnore
  @Transient
  @Override
  public FormTheme theme() {
      if((this.theme != null)){
          return FormTheme.getById(this.theme);
      }
      return null;
  }

  @Length(min = 0, max = 1, message = "状态:0、未启用；1、启用长度必须介于 0 和 1 之间")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getYwsize() {
    return ywsize;
}

public String getType() {
    return type;
  }

public Boolean getTemp() {
    return temp;
}

public void setTemp(Boolean temp) {
    this.temp = temp;
}

public Integer getTheme() {
    return theme;
}

public void setTheme(Integer theme) {
    this.theme = theme;
}

public String[] getTypes() {
    String[] types = null;
    if (StringUtil.isNotEmpty(this.type) && (this.type.length() > 1)) {
      types = this.type.substring(0, (this.type.length()-1)).split(",");
    }
    return types;
  }

  public void setType(String type) {
    this.type = type;
  }

  @JsonIgnore
  public void setType(FlowProjectType[] flowProjectTypes) {
    StringBuffer proType = new StringBuffer();
    for (FlowProjectType flowProjectType : flowProjectTypes) {
      proType.append(flowProjectType.getKey());
      proType.append(",");
    }
    this.type = proType.toString();
  }

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getFlowId() {
    return flowId;
  }

  public void setFlowId(String flowId) {
    this.flowId = flowId;
  }

  public String getKeyss() {
    return keyss;
  }

  public void setKeyss(String keyss) {
    this.keyss = keyss;
  }

  public ApiTstatus<ActYwGnode> getRstatus() {
    if (this.rstatus == null) {
      this.rstatus = new ApiTstatus<ActYwGnode>();
    }
    return rstatus;
  }

  public void setRstatus(ApiTstatus<ActYwGnode> rstatus) {
    this.rstatus = rstatus;
  }

public String getUiHtml() {
    return uiHtml;
}

public void setUiHtml(String uiHtml) {
    this.uiHtml = uiHtml;
}

public String getUiJson() {
    return uiJson;
}

public void setUiJson(String uiJson) {
    this.uiJson = uiJson;
}

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public String keyss() {
        return this.keyss;
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