package com.oseasy.com.fileserver.common.vsftp.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.LoggerFactory;

/**
 * 实现文件上传下载
 */
@Deprecated
public class FtpTemplateBase2 {

  protected static org.slf4j.Logger log = LoggerFactory.getLogger(FtpTemplateBase2.class);

  protected FtpClientPool2 ftpClientPool;


  public FtpTemplateBase2(FtpClientPool2 ftpClientPool) {
    this.ftpClientPool = ftpClientPool;
  }

  protected boolean existDirectory(String path, FTPClient ftpClient) {
    try {
      return ftpClient.changeWorkingDirectory(path);
    } catch (IOException e) {
      log.error("existDirectory异常!", e);
    }
    return false;
  }


  protected void mkDirectory(String path, FTPClient ftpClient) {
    String[] ss = path.split("/");
    try {
      ftpClient.changeWorkingDirectory("/");
      for (String s : ss) {
        ftpClient.mkd(s);
        ftpClient.changeWorkingDirectory(s);
      }
    } catch (IOException e) {
      log.error("mkDirectory异常!", e);
    }
  }

  public boolean uploadFile(InputStream inStream, String fileName, String remotePath) {
    FTPClient ftpClient = null;
    try {
      //从池中获取对象
      ftpClient = ftpClientPool.borrowObject();
      boolean isExist = existDirectory(remotePath, ftpClient);
      if (!isExist) {
        mkDirectory(remotePath, ftpClient);
      }
      // 改变工作路径
      ftpClient.changeWorkingDirectory(remotePath);

      final int retryTimes = 3;
      for (int j = 0; j <= retryTimes; j++) {
        boolean success = ftpClient.storeFile(fileName, inStream);
        if (success) {
          log.info(fileName + "文件上传成功!");
          return true;
        }
        log.warn("文件上传失败!试图重新上传... 尝试{}次", j);
      }
//      log.info("池中所有在用实例pool.getNumActive()："+ftpClientPool.getNumActive());
//      log.info("池中处于闲置状态的实例pool.getNumIdle()："+ftpClientPool.getNumIdle());
    } catch (Exception e) {
      log.error("上传文件异常!", e);
    } finally {
      IOUtils.closeQuietly(inStream);
      //将对象放回池中
      ftpClientPool.returnObject(ftpClient);
//      log.info("returnObject后 池中所有在用实例pool.getNumActive()："+ftpClientPool.getNumActive());
//       log.info("returnObject后 池中处于闲置状态的实例pool.getNumIdle()："+ftpClientPool.getNumIdle());
    }
    return false;

  }


  /***
   * 上传Ftp文件
   * @param localFile 当地文件
   * @param remotePath 上传服务器路径 - 应该以/结束
   * @return true or false
   */
  public boolean uploadFile(File localFile, String fileName, String remotePath) {
    InputStream inStream = null;
    try {
      inStream = new BufferedInputStream(new FileInputStream(localFile));
    } catch (FileNotFoundException e) {
      log.error("系统找不到指定的文件!{}", localFile);
    }
    return uploadFile(inStream, fileName, remotePath);
  }


  /**
   * 下载文件
   *
   * @param remotePath FTP服务器文件目录
   * @param fileName 需要下载的文件名称
   * @param localPath 下载后的文件路径
   * @return true or false
   */
  public boolean downloadFile(String remotePath, String fileName, String localPath) {
    FTPClient ftpClient = null;
    OutputStream outputStream = null;
    try {
      ftpClient = ftpClientPool.borrowObject();
      // 验证FTP服务器是否登录成功
           /* if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                return false;
            }*/
//            ftpClient = ftpClientPool.borrowObject();
      // 切换FTP目录
      ftpClient.changeWorkingDirectory(remotePath);
      FTPFile[] ftpFiles = ftpClient.listFiles();
      for (FTPFile file : ftpFiles) {
        if (fileName.equalsIgnoreCase(file.getName())) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(localPath).append(File.separator).append(file.getName());
          File localFile = new File(stringBuilder.toString());
          outputStream = new FileOutputStream(localFile);
          ftpClient.retrieveFile(file.getName(), outputStream);
        }
      }
      return true;
    } catch (Exception e) {
      log.error("下载文件异常", e);
    } finally {
      IOUtils.closeQuietly(outputStream);
      ftpClientPool.returnObject(ftpClient);
    }
    return false;
  }


  /**
   * 删除文件
   *
   * @param remotePath FTP服务器保存目录
   * @param fileName 要删除的文件名称
   * @return true or false
   */
  public boolean deleteFile(String remotePath, String fileName) {
    FTPClient ftpClient = null;
    try {
      ftpClient = ftpClientPool.borrowObject();
      // 验证FTP服务器是否登录成功
//            int replyCode = ftpClient.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(replyCode)) {
//                return false;
//            }
      // 切换FTP目录
      ftpClient.changeWorkingDirectory(remotePath);
      int delCode = ftpClient.dele(fileName);
      log.debug("删除文件:服务器返回的code为:{}", delCode);
      return true;
    } catch (Exception e) {
      log.error("文件删除失败!", e);
    } finally {
      ftpClientPool.returnObject(ftpClient);
    }
    return false;
  }


  public boolean isFileExist(String filepath) {
    FTPClient ftpClient = null;
    try {
      ftpClient = ftpClientPool.borrowObject();
      FTPFile[] fs = ftpClient.listFiles(filepath);
      if (fs != null && fs.length > 0) {
        return true;
      }
    } catch (Exception e) {
      log.error("文件删除失败!", e);
    } finally {
      ftpClientPool.returnObject(ftpClient);
    }
    return false;
  }


  public FTPClient getFtpclient() throws Exception {
    return ftpClientPool.borrowObject();
  }

//  public GenericObjectPool<FTPClient> getFtpClientPool() {
//    return ftpClientPool;
//  }
  public FtpClientPool2 getFtpClientPool() {
     return ftpClientPool;
   }


}
