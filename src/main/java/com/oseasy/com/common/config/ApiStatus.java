package com.oseasy.com.common.config;

import net.sf.json.JSONObject;

public class ApiStatus {
  private Boolean status;
  private String msg;
  private JSONObject datas;

  public ApiStatus() {
    super();
    this.status = true;
    this.msg = "执行成功";
  }

  public ApiStatus(Boolean status, String msg) {
    super();
    this.status = status;
    this.msg = msg;
  }

  public ApiStatus(Boolean status, String msg, JSONObject datas) {
    super();
    this.status = status;
    this.msg = msg;
    this.datas = datas;
  }

  public Boolean getStatus() {
    return status;
  }
  public void setStatus(Boolean status) {
    this.status = status;
  }
  public String getMsg() {
    return msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void setDatas(JSONObject datas) {
    this.datas = datas;
  }

  public JSONObject getDatas() {
    return datas;
  }
}
