package com.oseasy.act.modules.actyw.exception;

/**
 * 申报异常.
 * @author chenhao
 *
 */
public class ApplyException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public ApplyException(String code) {
    super();
    this.code = code;
  }

  public ApplyException() {
    super();
  }

  public ApplyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public ApplyException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ApplyException(Throwable arg0) {
    super(arg0);
  }
}