package com.oseasy.act.modules.actyw.exception;

/**
 * 项目时间异常.
 * @author chenhao
 *
 */
public class ProTimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public ProTimeException(String code) {
    super();
    this.code = code;
  }

  public ProTimeException() {
    super();
  }

  public ProTimeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public ProTimeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ProTimeException(Throwable arg0) {
    super(arg0);
  }
}