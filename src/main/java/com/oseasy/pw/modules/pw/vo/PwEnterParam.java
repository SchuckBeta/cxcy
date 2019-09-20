package com.oseasy.pw.modules.pw.vo;

public class PwEnterParam {
  private Boolean isView;
  private String applyId;

  public PwEnterParam() {
    super();
    this.isView = false;
  }
  public PwEnterParam(Boolean isView, String applyId) {
    super();
    this.isView = isView;
    this.applyId = applyId;
  }
  public Boolean getIsView() {
    return isView;
  }
  public void setIsView(Boolean isView) {
    this.isView = isView;
  }
  public String getApplyId() {
    return applyId;
  }
  public void setApplyId(String applyId) {
    this.applyId = applyId;
  }
}
