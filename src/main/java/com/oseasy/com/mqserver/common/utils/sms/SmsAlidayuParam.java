package com.oseasy.com.mqserver.common.utils.sms;

public class SmsAlidayuParam<T extends ISendParam> {
  private String tels; // 手机号(多个号码以,分隔)
  private T param;

  public SmsAlidayuParam() {
    super();
  }

  public SmsAlidayuParam(String tels, T param) {
    super();
    this.tels = tels;
    this.param = param;
  }

  public String getTels() {
    return tels;
  }

  public void setTels(String tels) {
    this.tels = tels;
  }

  public T getParam() {
    return param;
  }

  public void setParam(T param) {
    this.param = param;
  }
}
