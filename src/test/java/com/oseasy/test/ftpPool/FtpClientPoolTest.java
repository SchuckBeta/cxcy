package com.oseasy.test.ftpPool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oseasy.com.fileserver.common.vsftp.core.Vsftp;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BaseObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 测试ftp 上传.下载.删除
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-context-ftp.xml"})
public class FtpClientPoolTest {

  @Autowired
  private Vsftp vsftp;

  @Test
    public void testFTPConnect() {
     GenericObjectPool<FTPClient> ftpClientPool2 = vsftp.getFtpClientPool();
//    BaseObjectPool<FTPClient> ftpClientPool2 = vsftp.getFtpClientPool();
    for (int i = 0; i < 20; i++) {
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            System.err.println("-----------------------------------");
            FTPClient ftpClient = ftpClientPool2.borrowObject();
            System.out.println(Thread.currentThread().getName() + "   " + ftpClient.isConnected());
            System.out.println("borrowObject--------------------------");
            System.out.println("getNumIdle " + ftpClientPool2.getNumIdle() + "  getNumActive() :" + ftpClientPool2.getNumActive());
//            System.out.println("getMaxIdle " + ftpClientPool2.getMaxIdle() + "  getNumWaiters() :" + ftpClientPool2.getNumWaiters());
//            System.out.println("getMinIdle " + ftpClientPool2.getMinIdle() + "  maxTotal() :" + ftpClientPool2.getMaxTotal());
            Thread.sleep(2000);
            ftpClientPool2.returnObject(ftpClient);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }
  @Test
  public void testFTPConnect_Pool()  throws  Exception {
    GenericObjectPool<FTPClient> ftpClientPool2 = vsftp.getFtpClientPool();
//    BaseObjectPool<FTPClient> ftpClientPool2 = vsftp.getFtpClientPool();
    ExecutorService fixedthreadpool = newFixedThreadPool();
    for (int i = 0; i < 5; i++) {
      final int index = i;
      fixedthreadpool.execute(new Runnable() {
        @Override
        public void run() {
          try {
            FTPClient ftpClient = ftpClientPool2.borrowObject();
            System.out.println(Thread.currentThread().getName() + "   " + ftpClient.isConnected());
            System.out.println("borrowObject--------------------------"+index);
            System.out.println(index +"getNumIdle " + ftpClientPool2.getNumIdle() + "  getNumActive() :" + ftpClientPool2.getNumActive());
//            System.out.println(index+"getMaxIdle " + ftpClientPool2.getMaxIdle() + "  getNumWaiters() :" + ftpClientPool2.getNumWaiters());
//            System.out.println(index+"getMinIdle " + ftpClientPool2.getMinIdle() + "  maxTotal() :" + ftpClientPool2.getMaxTotal());
            System.out.println("borrowObject--------------------------end "+index);
            Thread.sleep(2000);
            ftpClientPool2.returnObject(ftpClient);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
    Thread.sleep(5000);
    fixedthreadpool.shutdown();


  }


  @Test
  public void uploadFile(){
    boolean uploadResult = vsftp.uploadFile(new File("d:\\电商高并发方案及Redis案例分享.zip"), "电商高并发方案及Redis案例分享.zip", "/test/");
    Assert.assertTrue(uploadResult);
  }

  @Test
  public void downloadFile() {
    boolean downloadResult = vsftp.downloadFile("/test/", "电商高并发方案及Redis案例分享.zip", "E:\\");
    Assert.assertTrue(downloadResult);
  }

  @Test
  public void deleteFile() {
    boolean deleteResult = vsftp.deleteFile("/test/", "电商高并发方案及Redis案例分享.zip");
    Assert.assertTrue(deleteResult);
  }



  public static ExecutorService newFixedThreadPool() {
      ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-act-%d") .build();
      return new ThreadPoolExecutor(10, 10,0L,
               TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory,
              new ThreadPoolExecutor.CallerRunsPolicy());
  }

}