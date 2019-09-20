package com.oseasy.com.fileserver.common.vsftp.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class PoolConfig extends GenericObjectPoolConfig {

  public PoolConfig() {
    setTestWhileIdle(true);
    setTimeBetweenEvictionRunsMillis(60000);
    setMinEvictableIdleTimeMillis(180000L);
    //参数testOnBorrow设定在借出对象时是否进行有效性检查
    setTestOnBorrow(true);
    //参数testOnBorrow设定在还回对象时是否进行有效性检查
    setTestOnReturn(false);
    //    参数testWhileIdle，则设定在进行后台对象清理时，是否还对没有过期的池内对象进行有效性检查。不能通过有效性检查的对象也将被回收
    setTestWhileIdle(true);
    setSoftMinEvictableIdleTimeMillis(-1);
    setLifo(false);
    setBlockWhenExhausted(true); // 连接耗尽时是否阻塞,默认为true
  }

}
