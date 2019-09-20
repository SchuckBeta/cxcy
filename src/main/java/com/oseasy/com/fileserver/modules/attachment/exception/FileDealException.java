package com.oseasy.com.fileserver.modules.attachment.exception;

/**
 * 附件处理异常.
 * @author chenhao
 */
public class FileDealException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 错误编码.
   **/
  private String code;

  public FileDealException(String code) {
    super();
    this.code = code;
  }

  public FileDealException() {
    super();
  }

  public FileDealException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public FileDealException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public FileDealException(Throwable arg0) {
    super(arg0);
  }
}