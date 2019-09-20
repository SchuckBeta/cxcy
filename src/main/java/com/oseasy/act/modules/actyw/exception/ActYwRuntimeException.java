package com.oseasy.act.modules.actyw.exception;

/**
 * 申报异常.
 * 
 * @author chenhao
 *
 */
public class ActYwRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ActYwRuntimeException() {
		super();
	}

	public ActYwRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ActYwRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActYwRuntimeException(String message) {
		super(message);
	}

	public ActYwRuntimeException(Throwable cause) {
		super(cause);
	}
	
	
}