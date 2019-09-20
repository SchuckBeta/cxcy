package com.oseasy.test.ftpPool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.oseasy.com.fileserver.common.vsftp.core.Vsftp;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-context-ftp.xml"})
public class FtpPoolTest {

  @Autowired
  private Vsftp vsftp;

  @Before
  public  void initTest() {
    /*  System.out.println("开始上传...");
    if (ObjectUtils.isEmpty(ftpClientPool)) {
    GenericObjectPool<FTPClient>   ftpClientPool = vsftp.getFtpClientPool();
      AbandonedConfig abandonedConfig = new AbandonedConfig();
      abandonedConfig.setRemoveAbandonedOnMaintenance(true); //在Maintenance的时候检查是否有泄漏
      abandonedConfig.setRemoveAbandonedOnBorrow(true); //borrow 的时候检查泄漏
      abandonedConfig.setRemoveAbandonedTimeout(10); //如果一个对象borrow之后10秒还没有返还给pool，认为是泄漏的对象
      ftpClientPool.setAbandonedConfig(abandonedConfig);
      ftpClientPool.setTimeBetweenEvictionRunsMillis(5000); //5秒运行一次维护任务
      System.out.println("----------------------------------" + ftpClientPool.hashCode());
    }
    */
  }

  @Test
    public void testFtpPool5() {
    String tname = Thread.currentThread().getName();
    vsftp.uploadFile(new File("E:\\test\\-a.txt"), tname + "-" + System.nanoTime() + ".txt", "/test/");
  }

  @Test
  public void testFtpPool2() {
    try {
    //    线程命名
//          ThreadFactory threadFactory = new ThreadFactoryBuilder() .setNameFormat("fixed.ftppool.thread-%d").setDaemon(true)   .build();
//          ExecutorService fixedthreadpool = Executors.newFixedThreadPool(300, threadFactory);
          ExecutorService fixedthreadpool = newFixedThreadPool();
          for (int i = 0; i < 100; i++) {
            fixedthreadpool.execute(new Runnable() {
              @Override
              public void run() {
                String tname = Thread.currentThread().getName();
                vsftp.uploadFile(new File("E:\\test\\-a.txt"), tname+"-"+System.nanoTime() + ".txt", "/test/");
              }
            });
          }
//          Thread.sleep(5000);
          fixedthreadpool.shutdown();
          //等待一分钟，直到这些任务执行完
          final boolean done = fixedthreadpool.awaitTermination(10, TimeUnit.SECONDS);
          if (!done){
            fixedthreadpool.shutdownNow();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
  }

  @Test
     public void testFtpPool3() {

     for (int i = 0; i < 20; i++) {
      final int index =i;
       new Thread(new Runnable() {
         @Override
         public void run() {
           System.out.println("-----------------------");
           try {
             String tname = Thread.currentThread().getName();
             boolean uploadResult = vsftp.uploadFile(new File("E:\\test\\-a.txt"), tname+"-"+System.nanoTime() + ".txt", "/test/");
             System.out.printf(tname +"上传结果:%d ,%s",index, uploadResult );
           } catch (Exception e) {
             e.printStackTrace();
           }
         }
       }).start();
     }
   }



  public static ExecutorService newFixedThreadPool() {
      ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-act-%d")
              .build();
      return new ThreadPoolExecutor(10, 10,
              0L, TimeUnit.MILLISECONDS,
              new LinkedBlockingQueue<Runnable>(4048), namedThreadFactory,
              new ThreadPoolExecutor.CallerRunsPolicy());
  }

}
