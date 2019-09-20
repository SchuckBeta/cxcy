package com.oseasy.com.fileserver.common.vsftp.exceptions;

public class FtpException extends Exception {
  private static final long serialVersionUID = -2946266495682282677L;

  public FtpException(String message) {
    super(message);
  }

  public FtpException(Throwable e) {
    super(e);
  }

  public FtpException(String message, Throwable cause) {
    super(message, cause);
  }
}
