package com.oseasy.pw.modules.pw.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.pw.modules.pw.entity.PwEnter;

public class PwEnterEvo {
  private Integer totalNum; //总记录数
  private Integer ignorNum; //忽略记录数
  private Integer succNum; //成功记录数
  private Integer failNum; //失败记录数
  private String logFile; //日志文件路径
  private List<ApiTstatus<PwEnter>> ignorEnters; //忽略记录
  private List<ApiTstatus<PwEnter>> succEnters; //成功记录
  private List<ApiTstatus<PwEnter>> failEnters; //失败记录
  private String failSqlInIds; //日志文件路径


  public PwEnterEvo() {
    super();
    this.ignorEnters = Lists.newArrayList();
    this.succEnters = Lists.newArrayList();
    this.failEnters = Lists.newArrayList();
  }
  public Integer getTotalNum() {
    return totalNum;
  }
  public void setTotalNum(Integer totalNum) {
    this.totalNum = totalNum;
  }
  public Integer getSuccNum() {
    if(this.succNum == null){
      this.succNum = this.succEnters.size();
    }
    return succNum;
  }
  public void setSuccNum(Integer succNum) {
    this.succNum = succNum;
  }
  public Integer getFailNum() {
    if(this.failNum == null){
      this.failNum = this.failEnters.size();
    }
    return failNum;
  }
  public void setFailNum(Integer failNum) {
    this.failNum = failNum;
  }
  public Integer getIgnorNum() {
    if(this.ignorNum == null){
      this.ignorNum = this.ignorEnters.size();
    }
    return ignorNum;
  }
  public void setIgnorNum(Integer ignorNum) {
    this.ignorNum = ignorNum;
  }
  public List<ApiTstatus<PwEnter>> getIgnorEnters() {
    return ignorEnters;
  }
  public void setIgnorEnters(List<ApiTstatus<PwEnter>> ignorEnters) {
    this.ignorEnters = ignorEnters;
  }
  public List<ApiTstatus<PwEnter>> getSuccEnters() {
    return succEnters;
  }
  public void setSuccEnters(List<ApiTstatus<PwEnter>> succEnters) {
    this.succEnters = succEnters;
  }
  public List<ApiTstatus<PwEnter>> getFailEnters() {
    return failEnters;
  }
  public void setFailEnters(List<ApiTstatus<PwEnter>> failEnters) {
    this.failEnters = failEnters;
  }
  public String getLogFile() {
    return logFile;
  }
  public void setLogFile(String logFile) {
    this.logFile = logFile;
  }
  public String getFailSqlInIds() {
    return failSqlInIds;
  }
  public void setFailSqlInIds(String failSqlInIds) {
    this.failSqlInIds = failSqlInIds;
  }
}