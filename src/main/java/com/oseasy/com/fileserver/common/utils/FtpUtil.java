package com.oseasy.com.fileserver.common.utils;

import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.LoggerFactory;

/**
 * Created by victor on 2019/6/10
 * 过时的api 建议不要使用 请使用vsftpUtils
 */
@Deprecated
public class FtpUtil {
	   private static org.slf4j.Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    public static final String FTP_HTTPURL = CoreSval.getConfig("ftp.httpUrl");
    public static final String FTP_MARKER ="@5c319144e8474f329d29ac90a85a44c6";//html代码中ftp路径占位符
    public static final String FTP_DOWNURL="/ftp/ueditorUpload/downFile";//FTP下载链接
    static FTPClient ftpClient=null;

    static {
        Long startTime =  System.currentTimeMillis();
        try {
            ftpClient=    VsftpUtils.getResource().getFtpclient();
        } catch (Exception e) {
            logger.error( "FTP连接异常 %s \n" ,e );
        }
        logger.info("打开ftp连接时间："+(System.currentTimeMillis()-startTime)+"ms");
    }

    public static FTPClient   getftpClient() {
        try {
            if (ftpClient == null) {
                ftpClient = VsftpUtils.getResource().getFtpclient();
            }
        } catch (Exception e) {
            logger.error("FTP连接异常 %s \n", e);
        }
        return ftpClient;
    }

    public static String getFtpPath(String url) {
        if (StringUtil.isNotEmpty(url)&&StringUtil.contains(url,FTP_HTTPURL)) {
            url=url.replace(FTP_HTTPURL, "");
            url="/tool"+url;
        }
        return url;
    }

    /**
     * closeServer
     * 归还连接至连接池
     */
    public static void closeServer() {
        try {
            VsftpUtils.getResource().getFtpClientPool().returnObject(ftpClient);
        } catch (Exception e) {
            logger.error("closeServer error：{}" + e);
        }
    }
    /**
     * ftp上传单个文件
     */
    public static boolean uploadInputSteam(InputStream fis,String directory, String destName) throws IOException {

        boolean result = false;
        try {
            // 设置上传目录
            boolean isExist = existDirectory( directory);
            logger.debug("file upload  path is：==============" + destName);
            if (!isExist) {
                mkDirectory(directory);
            }
            ftpClient.changeWorkingDirectory(directory);
            // 设置文件类型（二进制）
            result = ftpClient.storeFile(destName, fis);
            return result;
        }  catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            IOUtils.closeQuietly(fis);
            closeServer();
        }
        return result;
    }

    /**
     * ftp上传单个文件
     *
     * @param directory   上传至ftp的路径名不包括ftp地址
     * @param srcFileName 要上传的文件全路径名
     * @param destName    上传至ftp后存储的文件名
     * @throws IOException
     */
    public static boolean upload( String directory, String srcFileName, String destName) throws IOException {
        FileInputStream fis = null;
        boolean result = false;
        try {
            File srcFile = new File(srcFileName);
            fis = new FileInputStream(srcFile);
            // 设置上传目录
            logger.info("[upload]设置上传目录::::"+directory);
            ftpClient.changeWorkingDirectory(directory);
            // 设置文件类型（二进制）
            destName = new String(destName.getBytes( System.getProperty("file.encoding")), "iso-8859-1");
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            logger.info("[ftpClient.storeFile  destName]::::"+destName);
            result = ftpClient.storeFile(destName, fis);
        }  catch (Exception e) {
            logger.error("ftp上传文件失败!\n  {}" ,e);
        } finally {
            IOUtils.closeQuietly(fis);
            closeServer();
        }
        return result;
    }


    /**
     * FTP单个文件下载
     *
     * @param directory    要下载的文件所在ftp的路径名不包含ftp地址
     * @param destFileName 要下载的文件名
     * @param downloadName 下载后锁存储的文件名全路径
     */
    public static boolean download(String directory, String destFileName, String downloadName) throws IOException {
        boolean result = false;
        try {
            // 设置文件类型（二进制）
            ftpClient.changeWorkingDirectory(directory);
            logger.debug("download file  path：" + "destFileName:" + destFileName + ",downloadName:"
                + downloadName);
            result = ftpClient.retrieveFile(destFileName, new FileOutputStream(downloadName));
        } catch (IOException e) {
            logger.error("ftp下载文件失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
    * FTP单个文件下载
    * fileName 文件名
    * path 文件ftp上地址
    */
    public static boolean downloadFile(HttpServletResponse response, String fileName, String path) {
        try {
        	ftpClient.changeWorkingDirectory(path);
            FTPFile[] fs = ftpClient.listFiles();
            logger.debug("下载文件路径："+"destFileName:" + path + ",downloadName:" + fileName);
            for(FTPFile ff: fs) {
                if (ff.getName().equals(fileName)) {
                    response.setHeader("Content-Disposition", "attachment;fileName="+
                    		new String( filenameUrlDecode(ff.getName()).getBytes("utf-8"), "ISO8859-1" )
                    		);
                    OutputStream os = response.getOutputStream();
                    ftpClient.retrieveFile(ff.getName(), os);
                    os.flush();
                    os.close();
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ftp下载文件失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return true;
    }

    /**
     * FTP单个文件下载
     * name 下载的文件名
     * fileName 文件名
     * path 文件ftp上地址
     */
     public static boolean downloadUrlFile(HttpServletResponse response, String name,String realName, String path) {
         long startTime = System.currentTimeMillis();
         try {
         	ftpClient.changeWorkingDirectory(path);
             logger.debug("下载文件路径："+"destFileName:" + path + ",downloadName:" + realName+"。文件名："+name);
             response.setHeader("Content-Disposition", "attachment;fileName=\""+//name
                     new String(name.getBytes("utf-8"), "ISO8859-1")+"\""
             );
             OutputStream os = response.getOutputStream();
             ftpClient.retrieveFile(realName, os);
             os.flush();
             os.close();
         } catch (Exception e) {
             logger.error("ftp下载文件失败!\n  %s", e);
         } finally {
             closeServer();
         }
         logger.info("下载时间："+(System.currentTimeMillis()-startTime)+"ms");
         return true;
     }


    /**
     *判断远程文件是否重命名成功，如果成功返回true，否则返回false
     * @param directory   要重命名的文件所在ftp的路径名不包含ftp地址
     * @param oldFileName 要重命名的文件名
     * @param newFileName 重命名后的文件名
     * @throws Exception
     */
    public static boolean rename( String directory, String oldFileName, String newFileName) throws IOException {
        boolean result = false;
        try {
            result = ftpClient.rename(oldFileName, newFileName);//重命名远程文件
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
     * 判断远程文件是否移除成功，如果成功返回true，否则返回false
     * @param directory 要删除的文件所在ftp的路径名不包含ftp地址
     * @param fileName  要删除的文件名
     * @return
     * @throws IOException
     */
    public static boolean remove(String directory, String fileName) throws IOException {
        boolean result = false;
        try {
            ftpClient.changeWorkingDirectory(directory);
            result = ftpClient.deleteFile(fileName);//删除远程文件
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
     * 判断远程文件是否移除成功，如果成功返回true，否则返回false
     * @param directory    要创建的目录所在ftp的路径名不包含ftp地址
     * @param newDirectory 要创建的新目录名
     * @return
     * @throws IOException
     */
    public static boolean makeDirecotory( String directory, String newDirectory) throws IOException {
        boolean result = false;
        try {
            ftpClient.changeWorkingDirectory(directory);
            result = ftpClient.makeDirectory(newDirectory);//创建新目录
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
     * 判断远程文件是否移除成功，如果成功返回true，否则返回false
     * @param directory    要重命名的目录所在ftp的路径名不包含ftp地址
     * @param oldDirectory 要重命名的旧目录名
     * @param newDirectory 重命名后的新目录
     * @return
     * @throws IOException
     */
    public static boolean renameDirecotory( String directory, String oldDirectory, String newDirectory) throws IOException {
        boolean result = false;
        try {
            ftpClient.changeWorkingDirectory(directory);
            result = ftpClient.rename(oldDirectory, newDirectory);//重命名目录
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
     * 判断远程文件是否移除成功，如果成功返回true，否则返回false
     * @param directory    要重命名的目录所在ftp的路径名不包含ftp地址
     * @param deldirectory 要删除的目录名
     * @return
     * @throws IOException
     */
    public static boolean removeDirecotory( String directory, String deldirectory) throws IOException {
        boolean result = false;
        try {
            ftpClient.changeWorkingDirectory(directory);
            result = ftpClient.removeDirectory(deldirectory);//删除目录
            return result;
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return result;
    }

    /**
     * @param directory
     * @return
     * @throws IOException
     */
    public static String[] list( String directory) throws IOException {
        String[] list = null;
        try {
            ftpClient.setControlEncoding("gbk");
            ftpClient.changeWorkingDirectory(directory);
//            ftpClient.enterLocalPassiveMode();
            list = ftpClient.listNames();//删除目录
        } catch (Exception e) {
            logger.error("连接ftp服务器失败!\n  %s", e);
        } finally {
            closeServer();
        }
        return list;
    }

    private static boolean existDirectory(String path) {
        boolean flag = false;
        FTPFile[] listFiles;
        try {
            String Localpath = ftpClient.printWorkingDirectory();
            listFiles = listFiles(null);
        } catch (Exception e) {

            return false;
        }
        if (listFiles == null){
            return false;
        }
        for (FTPFile ffile : listFiles) {
            boolean isFile = ffile.isFile();
            if (!isFile) {
                if (ffile.getName().equalsIgnoreCase(path)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean rename(String from, String to) {
        boolean renameResult = false;
        try {
            renameResult = ftpClient.rename(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return renameResult;
    }
    public static String moveFile(String sogouFtpPath) throws IOException {
        String to = sogouFtpPath.replace("/temp", "");
        //得到文件名后缀，用id到名称保存。
        mkDirectory(to.substring(0, to.lastIndexOf("/")));//新建目录
        logger.debug("ftp更改路径：" + sogouFtpPath + "更改为：" + to);
        rename(sogouFtpPath, to);//下载完成后 移出该文件
        return to;
    }

    public static String moveFileToFile(String sogouFtpPath,String to) throws IOException {

     	//String to=sogouFtpPath.replace("/temp", "");
     	//得到文件名后缀，用id到名称保存。
        mkDirectory(to.substring(0,to.lastIndexOf("/")));//新建目录
    	logger.debug("ftp更改路径："+sogouFtpPath+"更改为："+to);
        rename(sogouFtpPath, to);//下载完成后 移出该文件
        return to;
    }

    private String addSeparatorEnd(String str) {
        return StringUtil.endsWith(str, "/") ? str : str + "/";
    }

    private String filenameUrlEncode(String str) {
    	String[] index=str.split("\\.");
		String filename=index[0];
		String suffix=index[1];
		filename=Encodes.urlEncode(filename);
		//编码后名称
		String saveFileName=filename+"."+suffix;
        return saveFileName;
    }
    private static String filenameUrlDecode(String str) {
    	String filename=str.substring(0,str.lastIndexOf("."));
		String suffix=str.substring(str.lastIndexOf(".")+1);
		filename=Encodes.urlDecode(filename);
		//编码后名称
		String saveFileName=filename+"."+suffix;
        return saveFileName;
    }
    /**
     */
    private static FTPFile[] listFiles(String fileName) throws IOException {
        FTPFile[] files = null;
        if (StringUtil.isNotEmpty(fileName)) {
            files = ftpClient.listFiles(fileName);
            if (files != null && files.length != 1) {
                // 如果没有不能下载文件，再用被动模式试一次（lidahu）
                ftpClient.enterLocalPassiveMode();
                files = ftpClient.listFiles(fileName);
            }
        } else {
            files = ftpClient.listFiles();
            if (files == null || files.length == 0) {        // 以被动模式再试一次
                ftpClient.enterLocalPassiveMode();
                files = ftpClient.listFiles();
            }
        }
        return files;
    }

    private static void mkDirectory(String path) {
        String[] ss = path.split("/");
        try {
            ftpClient.changeWorkingDirectory("/");

            for (String s : ss) {

                int res = ftpClient.mkd(s);
//                    System.out.println(s+"   mkd:"+res);
                ftpClient.changeWorkingDirectory(s);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断文件是否重名
    public static boolean checkName(String ftpUrl, String newName) throws Exception {
        FTPFile[] files = listFiles(ftpUrl+"/"+newName);
        if (files.length > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String ftpHttpUrl() {
        return FTP_HTTPURL;
    }

    public static String ftpImgUrl(String part) {
    	String fileUrl = null;
    	if (StringUtil.isNotEmpty(part)&&StringUtil.contains(part,"/tool")) {
    		fileUrl=part;
        	//System.out.println(fileName.substring(fileName.lastIndexOf("/")+1));
        	//String fileName=fileUrl.substring(0,fileUrl.lastIndexOf("/")+1);
//        	fileUrl=filenameUrlDecode(fileUrl);
        	//fileUrl=fileUrl.replace("/tool", "");
        	fileUrl=FTP_HTTPURL+fileUrl;
    	}
      return fileUrl;
    }

    public static String getFileUrl(String url) {
        String fileUrl="";
        if (StringUtil.isNotEmpty(url)) {
            fileUrl=url.replace("/tool", "");
            fileUrl=FTP_HTTPURL+fileUrl;
        }
        return fileUrl;
    }



}
