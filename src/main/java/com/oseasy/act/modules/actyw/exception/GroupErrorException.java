package com.oseasy.act.modules.actyw.exception;

/**
 * 申报异常.
 * @author chenhao
 *
 */
public class GroupErrorException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public GroupErrorException(String code) {
    super();
    this.code = code;
  }

  public GroupErrorException() {
    super();
  }

  public GroupErrorException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public GroupErrorException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public GroupErrorException(Throwable arg0) {
    super(arg0);
  }
}