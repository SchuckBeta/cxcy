package com.oseasy.pw.modules.pw.exception;

public class EnterException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public EnterException(String code) {
    super();
    this.code = code;
  }

  public EnterException() {
    super();
  }

  public EnterException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public EnterException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public EnterException(Throwable arg0) {
    super(arg0);
  }
}