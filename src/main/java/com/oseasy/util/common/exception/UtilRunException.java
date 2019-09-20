package com.oseasy.util.common.exception;

public class UtilRunException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;    //或者继承任何标准异常类

	public UtilRunException() {
		super();
	}

	public UtilRunException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtilRunException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilRunException(String message) {
		super(message);
	}

	public UtilRunException(Throwable cause) {
		super(cause);
	}

	
}