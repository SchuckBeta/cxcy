package com.oseasy.pie.modules.impdata.exception;

public class ImpDataException extends RuntimeException {    //或者继承任何标准异常类
    /**
	 * 
	 */
	private static final long serialVersionUID = 8585570632367609589L;
	public ImpDataException()  {}                //用来创建无参数对象
    public ImpDataException(String message) {        //用来创建指定参数对象
        super(message);                             //调用超类构造器
    }
}
