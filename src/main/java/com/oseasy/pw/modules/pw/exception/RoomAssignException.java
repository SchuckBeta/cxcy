package com.oseasy.pw.modules.pw.exception;

public class RoomAssignException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public RoomAssignException(String code) {
    super();
    this.code = code;
  }

  public RoomAssignException() {
    super();
  }

  public RoomAssignException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public RoomAssignException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public RoomAssignException(Throwable arg0) {
    super(arg0);
  }
}