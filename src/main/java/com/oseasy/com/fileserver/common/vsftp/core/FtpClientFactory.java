package com.oseasy.com.fileserver.common.vsftp.core;

import com.oseasy.com.fileserver.common.vsftp.config.FtpProperties;
import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.LoggerFactory;

/**
 * FTPClient工厂类，通过FTPClient工厂提供FTPClient实例的创建和销毁
 *
 */
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(FtpClientFactory.class);
    private FtpProperties config;

    public FtpClientFactory(FtpProperties config) {
        log.info("ftp配置："+config.toString());
        this.config = config;
    }

    /**
     * 创建FtpClient对象
     */
    @Override
    public FTPClient create() {
        log.info(Thread.currentThread().getName() +" 创建FtpClient对象 create");
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(config.getEncoding());//iso-8859-1
        ftpClient.setConnectTimeout(config.getConnectTimeout());
        ftpClient.setControlKeepAliveTimeout(config.getKeepAliveTimeout());
        ftpClient.setAutodetectUTF8(true);
        try {
            ftpClient.connect(config.getHost(), config.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                log.warn("FTPServer refused connection,replyCode:{}", replyCode);
                return null;
            }
            if (!ftpClient.login(config.getUsername(), config.getPassword())) {
                log.warn("ftpClient login failed... username is {}; password: {}", config.getUsername(), config.getPassword());
            }
            ftpClient.setBufferSize(config.getBufferSize());
            ftpClient.setFileType(config.getTransferFileType()); //config.getTransferFileType()
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();
            }

        } catch (IOException e) {
            log.error("create ftp connection failed...{}", e);
        }
        return ftpClient;
    }

    /**
     * 用PooledObject封装对象放入池中
     */
    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        log.info(Thread.currentThread().getName() +"用PooledObject封装对象放入池中  wrap");
        return new DefaultPooledObject<>(ftpClient);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) {
        log.info(Thread.currentThread().getName() +"销毁FtpClient对象  destroyObject");
        if (ftpPooled == null) {
            return;
        }
        FTPClient ftpClient = ftpPooled.getObject();
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException io) {
            log.error("ftp client logout failed...{}", io);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException io) {
                log.error("close ftp client failed...{}", io);
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        log.info(Thread.currentThread().getName() +"验证FtpClient对象  validateObject");
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("Failed to validate client: {}", e);
        }
        return false;
    }


}
