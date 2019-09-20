package com.oseasy.aop.common.exception;

/**
 * @author: QM
 * @date: 2019/3/24 12:27
 * @description:
 */
public class ProxyException extends RuntimeException {
	public ProxyException() {
		super();
	}

	public ProxyException(String message) {
		super(message);
	}

	public ProxyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProxyException(Throwable cause) {
		super(cause);
	}
}
