/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.cmd.exception;

/**
 * 没有数据异常.
 * @author chenhao
 */
public class NoDateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码.
     **/
    private String code;

    public NoDateException(String code) {
      super();
      this.code = code;
    }

    public NoDateException() {
      super();
    }

    public NoDateException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
      super(arg0, arg1, arg2, arg3);
    }

    public NoDateException(String arg0, Throwable arg1) {
      super(arg0, arg1);
    }

    public NoDateException(Throwable arg0) {
      super(arg0);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
  }