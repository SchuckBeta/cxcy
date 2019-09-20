package com.oseasy.com.mqserver.common.utils.sms;

public class SmsAlidayuRstate {
  private String tels; //手机号(多个号码以,分隔)
  private SMSState state;

  public SmsAlidayuRstate() {
    super();
  }

  public SmsAlidayuRstate(String tels, SMSState state) {
    super();
    this.tels = tels;
    this.state = state;
  }

  public String getTels() {
    return tels;
  }
  public void setTels(String tels) {
    this.tels = tels;
  }
  public SMSState getState() {
    return state;
  }
  public void setState(SMSState state) {
    this.state = state;
  }
}
