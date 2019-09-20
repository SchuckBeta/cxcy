package com.oseasy.com.fileserver.common.vsftp;

import com.oseasy.com.fileserver.common.vsftp.core.Vsftp;
import com.oseasy.com.fileserver.common.vsftp.vo.VsFile;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FileVo;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VsftpUtils {
	private static Logger logger = LoggerFactory.getLogger(VsftpUtils.class);
	private static Vsftp vsftp =SpringContextHolder.getBean(Vsftp.class);

	public static FTPFile[] getFiles(String filepath)  {
		return vsftp.getFiles(filepath);
	}

	public static int getFileCount(String filepath)   {
		return vsftp.getFileCount(filepath);
	}

	public static boolean isFileExist(String filepath)   {
		return vsftp.isFileExist(filepath);
	}

	public static void uploadFile(String remotePath, String filename, File file)   {
		try {
			remotePath = "/"+ TenantConfig.getCacheTenant()+remotePath;
			vsftp.uploadFile(remotePath, filename, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException异常!", e);
		}
	}

	public static Vsftp getResource() {
		return vsftp;
	}


	public static int getNumIdle() {
		return vsftp.getFtpClientPool().getNumIdle();
	}

/*	public static void returnResource(Vsftp vsftp,FTPClient client) {
		vsftp.getFtpClientPool().returnObject(client);
	}*/

	public static void uploadFile(String remotePath, String filename, InputStream input)  {
		remotePath = "/"+ TenantConfig.getCacheTenant()+remotePath;
		vsftp.uploadFile(remotePath, filename, input);
	}

	public static void downFile(String filepath,String localPath)   {
		vsftp.downFile(filepath, localPath);
	}



	public static void downFile(String remotePath, String fileName, String localPath)   {
		vsftp.downFile(remotePath, fileName, localPath);
	}

	public static void downFilesPlus(Vsftp vss,List<VsFile> vsFiles, String expInfoId,String key) {
		vss = vsftp;
		batchDownFilesPlus(vsFiles,expInfoId,key);
	}

	public static void batchDownFilesPlus(List<VsFile> vsFiles, String expInfoId,String key) {
		OutputStream is = null;
		FTPClient ftpclient = null;
		try {
			int suc = 0;
			ftpclient = vsftp.getFtpclient();
			if (vsFiles != null && vsFiles.size() > 0) {
				for (VsFile vsFile : vsFiles) {
					ftpclient.changeWorkingDirectory(vsFile.getRemotePath());// 转移到FTP服务器目录
					FTPFile[] fs = ftpclient.listFiles();
					for (FTPFile ff : fs) {
						String remotFileName = new String(ff.getName().getBytes("iso-8859-1"), "utf-8");
						if (remotFileName.equals(vsFile.getRfileName())) {
							FileUtil.createDirectory(vsFile.getLocalPath());
							File localFile = new File(vsFile.getLocalPath() + "/" + vsFile.getLfileName());
							is = new FileOutputStream(localFile);
							if (ftpclient.retrieveFile(ff.getName(), is)) {
								suc++;
								CacheUtils.put(CacheUtils.EXP_NUM_CACHE + expInfoId, key, suc);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("下载文件异常!", e);
		} finally {
			CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + expInfoId, key, "1");//该线程执行完毕
			IOUtils.closeQuietly(is);
			vsftp.getFtpClientPool().returnObject(ftpclient);

		}
	}


	/**
	 * FTP批量下载文件.
	 * @param vsFiles
	 *   文件路径列表.
	 */
	public static FileVo downFiles(List<VsFile> vsFiles)   {
		return  vsftp.downFiles(vsFiles);
	}

	public static boolean downFileWithName(HttpServletRequest request, HttpServletResponse response,
			String name,	String realName, String path)   {
		boolean  	ret = vsftp.downFileWithName(request, response, name, realName, path);
		return ret;
	}

	/**
	 * @param remotePath  要删除的文件所在ftp的路径名不包含ftp地址
	 * @param fileName 要删除的文件名
	 * @throws IOException
	 */
	public static boolean removeFile(String remotePath, String fileName)   {
		boolean ret;
		ret = vsftp.removeFile(remotePath, fileName);
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(FileUtil.SUFFIX_DOC.equals(suffix)||FileUtil.SUFFIX_DOCX.equals(suffix)){
			String pdffilename = fileName.substring(0, fileName.lastIndexOf("."))+"."+FileUtil.SUFFIX_PDF; // 文件名
			String pdfpath=remotePath+pdffilename;
			if(vsftp.isFileExist(pdfpath)){
				vsftp.removeFile(pdfpath);
			}
		}
		return ret;
	}

	public static boolean rename(String from, String to) {
		boolean ret = vsftp.rename(from, to);
		return ret;
	}

	// 移动文件从临时目录到正式目录
	public static String moveFile(String tmpPath)   {
		String ret = vsftp.moveFile(tmpPath);
		return ret;
	}
	// 移动文件
	public static String moveFile(String tmpPath,String newPath)   {
		String ret = vsftp.moveFile(tmpPath,newPath);
		return ret;
	}

	// 复制文件从来源目录到其他目录
	public static boolean copyFile(String sourceFileName, String sourceDir, String targetFileName,String targetDir)   {
		boolean ret= vsftp.copyFile(sourceFileName,sourceDir,targetFileName,targetDir);
		return ret;
	}

	/**
	 * 异步
	 * 出错不抛出
	 * @param filepath
	 */
	public static void word2PDF(final String filepath){
		try {
			if(StringUtil.isEmpty(filepath)){
				return;
			}
			String suffix = filepath.substring(filepath.lastIndexOf(".") + 1).toLowerCase();
			if(!FileUtil.SUFFIX_DOC.equals(suffix)&&!FileUtil.SUFFIX_DOCX.equals(suffix)){
				return;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		new Thread(){
			@Override
			public void run() {
				try {
					String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
					File tempPathDir = new File(tempPath + File.separator);
					if (!tempPathDir.exists()) {
						tempPathDir.mkdirs();
					}
					String realName = filepath.substring(filepath.lastIndexOf("/") + 1);
					String path = filepath.substring(0, filepath.lastIndexOf("/") + 1);
					VsftpUtils.downFile(path, realName, tempPath);
					FileUtil.word2PDF(tempPathDir.getCanonicalPath(),realName);
					String pdffilename = realName.substring(0, realName.lastIndexOf("."))+"."+FileUtil.SUFFIX_PDF; // 文件名
					String pdfpath=tempPath+File.separator+pdffilename;
					File pdf=new File(pdfpath);
					if(pdf.exists()){
						VsftpUtils.uploadFile(path,pdffilename, pdf);
					}
					FileUtil.deleteFileOrDir(new File(tempPath));
				} catch (Exception e) {
					logger.error(ExceptionUtil.getStackTrace(e));
				}
			}

		}.start();
	}



/*
	public static void downFilesPlus(Vsftp vsftp,List<VsFile> vsFiles, String expInfoId,String key) {
		try {
			OutputStream is = null;
			int suc = 0;
			if(vsFiles!=null&&vsFiles.size()>0){
				for (VsFile vsFile : vsFiles) {
					try {
						vsftp.ftpclient.changeWorkingDirectory(vsFile.getRemotePath());// 转移到FTP服务器目录
						FTPFile[] fs = vsftp.ftpclient.listFiles();
						for (FTPFile ff : fs) {
							String remotFileName = new String(ff.getName().getBytes("iso-8859-1"), "utf-8");
							if (remotFileName.equals(vsFile.getRfileName())) {
								FileUtil.createDirectory(vsFile.getLocalPath());
								File localFile = new File(vsFile.getLocalPath() + "/" + vsFile.getLfileName());
								is = new FileOutputStream(localFile);
								if (vsftp.ftpclient.retrieveFile(ff.getName(), is)) {
									suc++;
									CacheUtils.put(CacheUtils.EXP_NUM_CACHE+expInfoId, key, suc);
								}
							}
						}
					} catch (Exception e) {
						logger.error(ExceptionUtil.getStackTrace(e));
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (Exception e) {
								logger.error(ExceptionUtil.getStackTrace(e));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
		} finally {
			CacheUtils.put(CacheUtils.EXP_STATUS_CACHE+expInfoId, key, "1");//该线程执行完毕
			vsFtpPool.returnResource(vsftp);
		}
	}*/


}
