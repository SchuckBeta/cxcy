package com.oseasy.com.pcore.common.aop.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{
	public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	private final static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
	@Override
	protected Object determineCurrentLookupKey() {
		String dataSource = getDataSource();
		logger.info("当前操作使用的数据源：{}", dataSource);
		return dataSource;
	}

	/**
	 * 设置数据源.
	 * @param dataSource
	 */
	public static void setDataSource(String dataSource) {
		contextHolder.set(dataSource);
	}

	/**
	 * 获取数据源.
	 * @return
	 */
	public static String getDataSource() {
		String dataSource = contextHolder.get();
		// 如果没有指定数据源，使用默认数据源
		if (null == dataSource) {
			DynamicDataSource.setDataSource(DataStype.MASTER.getDefault());
		}
		return contextHolder.get();
	}

	/**
	 * 清除数据源.
	 */
	public static void clearDataSource() {
		contextHolder.remove();
	}

}
