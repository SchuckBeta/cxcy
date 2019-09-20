package com.oseasy.act.modules.actyw.tool.process.vo;
/**
 * 自定义流程.
 * 流程表单根目录：webapp\WEB-INF\views\template\form
 * @author chenhao
 */
public enum FlowPropertyType {
  FPT_DASAI(FlowProjectType.PMT_DASAI.getValue(), new FlowProjectType[]{FlowProjectType.PMT_DASAI})
  ,FPT_TECHNOLOGY(FlowProjectType.PMT_TECHNOLOGY.getValue(), new FlowProjectType[]{FlowProjectType.PMT_TECHNOLOGY})
  ,FPT_XM(FlowProjectType.PMT_XM.getValue(), new FlowProjectType[]{FlowProjectType.PMT_XM})
  ,FPT_SCORE(FlowProjectType.PMT_SCORE.getValue(), new FlowProjectType[]{FlowProjectType.PMT_SCORE})
  ,FPT_APPOINTMENT(FlowProjectType.PMT_APPOINTMENT.getValue(), new FlowProjectType[]{FlowProjectType.PMT_APPOINTMENT})
  ,FPT_ENTER(FlowProjectType.PMT_ENTER.getValue(), new FlowProjectType[]{FlowProjectType.PMT_ENTER})
  ,FPT_ALL(FlowProjectType.PMT_ALL.getValue(), new FlowProjectType[]{FlowProjectType.PMT_ALL})
  ;

  private String key;//惟一标识,表单模板目录
  private FlowProjectType[] types;

  private FlowPropertyType(String key, FlowProjectType[] types) {
    this.key = key;
    this.types = types;
  }

  public String getKey() {
    return key;
  }

  public FlowProjectType[] getTypes() {
    return types;
  }

  @Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"types\":\"" + this.types + "\"}";
  }
}
