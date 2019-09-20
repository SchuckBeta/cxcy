package com.oseasy.com.fileserver.common.vsftp.core;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BaseObjectPool;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

/**
 * FTP连接池
 * 不建议自己实现连接池
 *
 * @see "https://commons.apache.org/proper/commons-pool/apidocs/org/apache/commons/pool2/BaseObjectPool.html"
 */

@Deprecated
public class FtpClientPool2 extends BaseObjectPool<FTPClient> {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(FtpClientPool2.class);
    private static final int DEFAULT_POOL_SIZE = 8;

    private final BlockingQueue<FTPClient> ftpBlockingQueue;
    private final FtpClientFactory ftpClientFactory;


    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param ftpClientFactory ftp工厂
     */
    public FtpClientPool2(FtpClientFactory ftpClientFactory) throws Exception {
        this(DEFAULT_POOL_SIZE, ftpClientFactory);
    }

    public FtpClientPool2(int poolSize, FtpClientFactory factory) throws Exception {
        this.ftpClientFactory = factory;
        ftpBlockingQueue = new ArrayBlockingQueue<>(poolSize*2);
        initPool(poolSize);
    }

    /**
     * 初始化连接池，需要注入一个工厂来提供FTPClient实例
     *
     * @param maxPoolSize 最大连接数
     */
    private void initPool(int maxPoolSize) throws Exception {
        for (int i = 0; i < maxPoolSize; i++) {
            // 往池中添加对象
            addObject();
        }

    }

    /**
     * 从连接池中获取对象
     */
    @Override
    public FTPClient borrowObject() throws Exception {
        FTPClient client = ftpBlockingQueue.take();

        if (ObjectUtils.isEmpty(client)) {
            client = ftpClientFactory.create();
            returnObject(client);
            // 验证对象是否有效
        } else if (!ftpClientFactory.validateObject(ftpClientFactory.wrap(client))) {
            // 对无效的对象进行处理
            invalidateObject(client);
            // 创建新的对象
            client = ftpClientFactory.create();
            // 将新的对象放入连接池
            returnObject(client);
        }
        return client;

    }

    /**
     * 返还对象到连接池中
     */
    @Override
    public void returnObject(FTPClient client) {
        try {
            long timeout = 3L;
            if (client != null && !ftpBlockingQueue.offer(client, timeout, TimeUnit.SECONDS)) {
                ftpClientFactory.destroyObject(ftpClientFactory.wrap(client));
            }
        } catch (InterruptedException e) {
            log.error("return ftp client interrupted ...{}", e);
        }
    }

    /**
     * 移除无效的对象
     */
    @Override
    public void invalidateObject(FTPClient client) {
        try {
            client.changeWorkingDirectory("/");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ftpBlockingQueue.remove(client);
        }
    }

    /**
     * 增加一个新的链接，超时失效
     */
    @Override
    public void addObject() throws Exception {
        // 插入对象到队列
        ftpBlockingQueue.offer(ftpClientFactory.create(), 3, TimeUnit.SECONDS);
    }

    /**
     * 关闭连接池
     */
    @Override
    public void close() {
        try {
            while (ftpBlockingQueue.iterator().hasNext()) {
                FTPClient client = ftpBlockingQueue.take();
                ftpClientFactory.destroyObject(ftpClientFactory.wrap(client));
            }
        } catch (Exception e) {
            log.error("close ftp client ftpBlockingQueue failed...{}", e);
        }
    }

}
