package com.oseasy.com.pcore.modules.sys.vo;

public class SysNodeFresult {
  public static final Integer FP_MAXNO = 5;
  public static final String FP_PREFIX = "${";
  public static final String FP_POSTFIX = "}";

  private Integer idxPrefix;
  private Integer idxPostfix;
  private Integer maxPrefix;
  private Boolean isEnable;
  private String param;
  private String result;

  public SysNodeFresult() {
    super();
    this.isEnable = false;
  }

  public SysNodeFresult(String result) {
    super();
    this.isEnable = false;
    this.result = result;
  }

  public SysNodeFresult(Boolean isEnable, String result) {
    super();
    this.isEnable = isEnable;
    this.result = result;
  }

  public Boolean getIsEnable() {
    return isEnable;
  }

  public void setIsEnable(Boolean isEnable) {
    this.isEnable = isEnable;
  }

  public Integer getMaxPrefix() {
    return maxPrefix;
  }

  public void setMaxPrefix(Integer maxPrefix) {
    this.maxPrefix = maxPrefix;
  }

  public Integer getIdxPrefix() {
    return idxPrefix;
  }
  public void setIdxPrefix(Integer idxPrefix) {
    this.idxPrefix = idxPrefix;
  }
  public Integer getIdxPostfix() {
    return idxPostfix;
  }
  public void setIdxPostfix(Integer idxPostfix) {
    this.idxPostfix = idxPostfix;
  }
  public String getParam() {
    return param;
  }
  public void setParam(String param) {
    this.param = param;
  }

  public String getResult() {
    return result;
  }

  public SysNodeFresult setResult(String result) {
    this.result = result;
    return this;
  }
}
