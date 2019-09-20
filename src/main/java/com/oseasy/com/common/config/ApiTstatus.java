/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.com.common.config
 * @Description [[_ActYwRstatus_]]文件
 * @date 2017年6月30日 上午11:07:07
 *
 */

package com.oseasy.com.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 获取命令处理状态信息.
 * @author chenhao
 * @date 2017年6月30日 上午11:07:07
 *
 */
public class ApiTstatus<T> implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(ApiTstatus.class);

  private Boolean status;
  private String msg;
  private T datas;

  public ApiTstatus() {
    super();
    this.status = true;
    this.msg = "执行成功";
  }

  public ApiTstatus(Boolean status, String msg) {
    super();
    this.status = status;
    this.msg = msg;
  }

  public ApiTstatus(Boolean status, String msg, T datas) {
    super();
    this.status = status;
    this.msg = msg;
    this.datas = datas;
  }

  public ApiTstatus(T datas) {
      super();
      this.status = true;
      this.msg = "执行成功";
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

  public void setDatas(T datas) {
    this.datas = datas;
  }

  public T getDatas() {
    return datas;
  }

  /**
   * 提供成功的静态方法，方便使用
   * @return
   */
  public static ApiTstatus ok(){
    return new ApiTstatus();
  }

  /**
   * 提供成功的静态方法，方便使用
   * @return
   */
  public static ApiTstatus ok(String msg){
    ApiTstatus r = new ApiTstatus();
    r.setMsg(msg);
    return r;
  }

  public static ApiTstatus error() {
    return error("未知异常，请联系管理员");
  }

  public static ApiTstatus error(String msg) {
    ApiTstatus r = new ApiTstatus();
    r.setStatus(false);
    r.setMsg(msg);
    return r;
  }
}
