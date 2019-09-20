/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.exception;

/**
 * 没有初始化异常.
 * @author chenhao
 */
public class NoInitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码.
     **/
    private String code;

    public NoInitException(String code) {
      super();
      this.code = code;
    }

    public NoInitException() {
      super();
    }

    public NoInitException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
      super(arg0, arg1, arg2, arg3);
    }

    public NoInitException(String arg0, Throwable arg1) {
      super(arg0, arg1);
    }

    public NoInitException(Throwable arg0) {
      super(arg0);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
  }