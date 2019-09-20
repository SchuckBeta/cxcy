package com.oseasy.aop.common.config;

/**
 * @author: QM
 * @date: 2019/3/24 17:30
 * @description:
 */
public enum ScanMode {
	// 只执行扫描到接口名或者类名上的注解后的处理
	FOR_CLASS_ANNOTATION_ONLY,
	// 只执行扫描到接口或者类方法上的注解后的处理
	FOR_METHOD_ANNOTATION_ONLY,
	// 上述两者都执行
	FOR_CLASS_OR_METHOD_ANNOTATION
}
