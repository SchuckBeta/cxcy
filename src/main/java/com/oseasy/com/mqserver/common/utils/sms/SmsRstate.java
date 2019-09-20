package com.oseasy.com.mqserver.common.utils.sms;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

public class SmsRstate{
  private static final String MSG_SUCCESS = "短信发送成功";
  private static final String MSG_FAILE = "短信发送失败";
  private Boolean status;
  private String msg;
  private List<SmsAlidayuRstate> sucstates;
  private List<SmsAlidayuRstate> failstates;

  public SmsRstate() {
    super();
    this.status = true;
    this.msg = MSG_SUCCESS;
  }

  public SmsRstate(Boolean status, String msg) {
    super();
    this.status = status;
    this.msg = msg;
  }

  public SmsRstate(Boolean status, String msg, List<SmsAlidayuRstate> sucstates,
      List<SmsAlidayuRstate> failstates) {
    super();
    this.status = status;
    this.msg = msg;
    this.sucstates = sucstates;
    this.failstates = failstates;
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

  public String setMsgDetail() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(MSG_SUCCESS + StringUtil.KUOHL + getSucstates().size() + StringUtil.KUOHR);
    buffer.append("\n");
    for (SmsAlidayuRstate sarstate : this.sucstates) {
      buffer.append(StringUtil.KUOHL);
      buffer.append(sarstate.getTels());
      buffer.append(StringUtil.SHUX);
      buffer.append(sarstate.getState().getMessage());
      buffer.append(StringUtil.KUOHR);
    }
    buffer.append("\n");
    buffer.append(MSG_FAILE + StringUtil.KUOHL + getFailstates().size() + StringUtil.KUOHR);
    buffer.append("\n");
    for (SmsAlidayuRstate sarstate : this.failstates) {
      buffer.append(StringUtil.KUOHL);
      buffer.append(sarstate.getTels());
      buffer.append(StringUtil.SHUX);
      buffer.append(sarstate.getState().getMessage());
      buffer.append(StringUtil.KUOHR);
    }
    return buffer.toString();
  }


  public List<SmsAlidayuRstate> getSucstates() {
    if (this.sucstates == null) {
      this.sucstates = Lists.newArrayList();
    }
    return sucstates;
  }

  public void setSucstates(List<SmsAlidayuRstate> sucstates) {
    this.sucstates = sucstates;
  }

  public List<SmsAlidayuRstate> getFailstates() {
    if (this.failstates == null) {
      this.failstates = Lists.newArrayList();
    }
    return failstates;
  }

  public void setFailstates(List<SmsAlidayuRstate> failstates) {
    this.failstates = failstates;
  }

  /**
   * 更新执行结果.
   * @param smsRstate 状态
   * @return SmsRstate
   */
  public static SmsRstate validate(SmsRstate smsRstate) {
    if ((smsRstate.getFailstates().size() > 0)) {
      smsRstate.setStatus(false);
      smsRstate.setMsg(MSG_SUCCESS + StringUtil.KUOHL + smsRstate.getSucstates().size() + StringUtil.KUOHR + StringUtil.SHUX + MSG_FAILE + StringUtil.KUOHL + smsRstate.getFailstates().size() + StringUtil.KUOHR);
    }else{
      smsRstate.setStatus(true);
      smsRstate.setMsg(MSG_SUCCESS);
    }
    return smsRstate;
  }
}
