package com.oseasy.pw.modules.pw.exception;

public class AuditFailException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public AuditFailException(String code) {
    super();
    this.code = code;
  }

  public AuditFailException() {
    super();
  }

  public AuditFailException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public AuditFailException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public AuditFailException(Throwable arg0) {
    super(arg0);
  }
}