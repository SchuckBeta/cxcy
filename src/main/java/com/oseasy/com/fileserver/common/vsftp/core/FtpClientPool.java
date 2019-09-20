package com.oseasy.com.fileserver.common.vsftp.core;

import com.oseasy.com.fileserver.common.vsftp.config.PoolConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class FtpClientPool extends GenericObjectPool<FTPClient> {
  public   FtpClientPool(FtpClientFactory factory, PoolConfig poolConfig) {
      super(factory,poolConfig);
  }

}
