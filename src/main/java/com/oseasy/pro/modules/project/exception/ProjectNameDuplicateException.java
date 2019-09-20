package com.oseasy.pro.modules.project.exception;

public class ProjectNameDuplicateException extends Exception {    //或者继承任何标准异常类
    /**
	 * 
	 */
	private static final long serialVersionUID = 8585570632367609589L;
	public ProjectNameDuplicateException()  {}                //用来创建无参数对象
    public ProjectNameDuplicateException(String message) {        //用来创建指定参数对象
        super(message);                             //调用超类构造器
    }
}
