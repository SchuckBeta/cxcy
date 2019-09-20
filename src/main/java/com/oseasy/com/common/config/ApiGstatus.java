package com.oseasy.com.common.config;

import com.google.common.collect.Lists;

import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/10/26 0026.
 */
public class ApiGstatus {
    private Boolean status;
    private String msg;
    private JSONObject datas;
    private List<ApiStatus> successs;
    private List<ApiStatus> fails;

    public ApiGstatus() {
      super();
      this.status = true;
      this.msg = "执行成功";
    }

    public ApiGstatus(Boolean status, String msg) {
      super();
      this.status = status;
      this.msg = msg;
    }

    public ApiGstatus(Boolean status, String msg, JSONObject datas) {
      super();
      this.status = status;
      this.msg = msg;
      this.datas = datas;
    }

    public ApiGstatus(Boolean status, String msg, JSONObject datas, List<ApiStatus> successs, List<ApiStatus> fails) {
      super();
      this.status = status;
      this.msg = msg;
        this.datas = datas;
        this.successs = successs;
        this.fails = fails;
    }

    public JSONObject getDatas() {
        return datas;
    }

    public void setDatas(JSONObject datas) {
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

    public List<ApiStatus> getSuccesss() {
      if (successs == null) {
        successs = Lists.newArrayList();
      }
        return successs;
    }

    public void setSuccesss(List<ApiStatus> successs) {
        this.successs = successs;
    }

    public List<ApiStatus> getFails() {
      if (fails == null) {
        fails = Lists.newArrayList();
      }
        return fails;
    }

    public void setFails(List<ApiStatus> fails) {
        this.fails = fails;
    }

    /**
     * 结果状态和消息.
     */
    public ApiGstatus deal() {
        if ((this.fails == null) || (this.successs == null)) {
            this.status = true;
            this.msg = "执行成功!";
            this.fails = Lists.newArrayList();
            this.successs = Lists.newArrayList();
            return this;
        }

        this.status = (this.fails.size() > 0);
        if (this.status) {
            this.msg = "执行成功!";
            return this;
        }

        StringBuffer msgBuffer = new StringBuffer("执行失败!失败记录如下：");
        for(ApiStatus rs : this.fails) {
            msgBuffer.append(rs.getMsg());
            msgBuffer.append("|");
        }
        this.msg = msgBuffer.toString();
        return this;
    }

    /**
     * 获取执行成功数量.
     * @return Integer
     */
    public Integer getNumFails() {
        return (this.fails == null)? 0 : this.fails.size();
    }

    /**
     * 获取执行失败数量.
     * @return Integer
     */
    public Integer getNumSuccesss() {
        return (this.successs == null)? 0 : this.successs.size();
    }
}
