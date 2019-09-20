package com.oseasy.act.modules.actyw.entity;

public class ActYwFormVo extends ActYwForm{
    private static final long serialVersionUID = 1L;
    private Boolean inStyleList;

  public ActYwFormVo() {
    super();
    this.inStyleList = true;
  }

  public ActYwFormVo(Boolean inStyleList) {
    super();
    this.inStyleList = inStyleList;
  }

  public ActYwFormVo(Boolean inStyleList, ActYwForm actYwForm) {
    super();
    this.inStyleList = inStyleList;
    if (actYwForm != null) {
      this.name = actYwForm.getName();
      this.proType = actYwForm.getProType();
      this.flowType = actYwForm.getFlowType();
      this.type = actYwForm.getType();
      this.theme = actYwForm.getTheme();
      this.styleType = actYwForm.getStyleType();
      this.clientType = actYwForm.getClientType();
      this.model = actYwForm.getModel();
      this.params = actYwForm.getParams();
      this.path = actYwForm.getPath();
      this.content = actYwForm.getContent();
      this.office = actYwForm.getOffice();
    }
  }

  public Boolean getInStyleList() {
    return inStyleList;
  }

  public void setInStyleList(Boolean inStyleList) {
    this.inStyleList = inStyleList;
  }
}
