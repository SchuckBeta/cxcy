package com.oseasy.act.modules.act.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author: QM
 * @date: 2019/4/24 14:31
 * @description: 线程池设置
 */
public class ThreadUtils {

	public static ExecutorService newFixedThreadPool() {
		ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-act-%d").build();
		return new ThreadPoolExecutor(10, 10,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
	}


	public static void shutdown() {
		ExecutorService executorService = newFixedThreadPool();
		executorService.shutdown();
		executorService.shutdownNow();
	}
}
