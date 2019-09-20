package com.oseasy.com.fileserver.common.vsftp.config;

import org.apache.commons.net.ftp.FTP;


public class FtpProperties {

//    public static final String FTP_USERNAME = CoreSval.getConfig("ftp.username");

    /**
     * ftp地址
     */
//    @Value("#{APP_PROP['ftp.client.host']}")
//    @Value("${ftp.client.host}")
    private String host;

    /**
     * 端口号
     */
    private Integer port = 21;

    /**
     * 登录用户
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 被动模式
     */
    private boolean passiveMode = false;

    /**
     * 编码
     */
    private String encoding = "UTF-8";

    /**
     * 连接超时时间(秒)
     */
    private Integer connectTimeout;

    /**
     * 缓冲大小
     */
    private Integer bufferSize = 1024;

    /**
     * 设置keepAlive
     * 单位:秒  0禁用
     */
    private Integer keepAliveTimeout = 0;

    /**
     * 传输文件类型
     */
//    private Integer transferFileType = FTP.LOCAL_FILE_TYPE;
    private Integer transferFileType = FTP.BINARY_FILE_TYPE;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(Integer keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public Integer getTransferFileType() {
        return transferFileType;
    }

    public void setTransferFileType(Integer transferFileType) {
        this.transferFileType = transferFileType;
    }

    @Override
    public String toString() {
        return "FtpProperties{" +
            "host='" + host + '\'' +
            ", port=" + port +
            ", username='" + username + '\'' +
            ", passiveMode=" + passiveMode +
            ", encoding='" + encoding + '\'' +
            ", connectTimeout=" + connectTimeout +
            ", bufferSize=" + bufferSize +
            ", keepAliveTimeout=" + keepAliveTimeout +
            ", transferFileType=" + transferFileType +
            '}';
    }
}
