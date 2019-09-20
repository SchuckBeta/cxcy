package com.oseasy.com.fileserver.common.vsftp.core;

import com.google.common.collect.Lists;
import com.oseasy.com.fileserver.common.vsftp.config.Global.FileDIREnum;
import com.oseasy.com.fileserver.common.vsftp.config.PoolConfig;
import com.oseasy.com.fileserver.common.vsftp.exceptions.FtpException;
import com.oseasy.com.fileserver.common.vsftp.vo.VsFile;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FileVo;
import com.oseasy.util.common.utils.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Vsftp extends FtpTemplateBase {

	public Vsftp(FtpClientPool ftpClientPool) {
		super(ftpClientPool);
	}

/*	public Vsftp(FtpClientFactory ftpFactory,PoolConfig poolConfig) {
		super(ftpFactory, poolConfig);
	}*/


	public  FTPFile[]  getFiles(String remotePath)  {
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			boolean isExist = existDirectory(remotePath, ftpclient);
			if (!isExist) {
				return null;
			}
			ftpclient.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftpclient.listFiles();
			if (fs != null && fs.length > 0) {
				return fs;
			}
		} catch (Exception e) {
			log.error("获取文件异常!", e);
		} finally {
			release(null, ftpclient);
		}
		return null;
	}

	public  int  getFileCount(String remotePath) {
		FTPFile[] fs =getFiles(remotePath);
		if(fs==null||fs.length==0){
			return 0;
		}else{
			int c=0;
			for(FTPFile f:fs){
				if(f.isFile()){
					c++;
				}
			}
			return c;
		}
	}

	public void uploadFile(String remotePath, String filename, InputStream inStream)  {
		FTPClient	ftpclient =null;
			try {
				ftpclient = ftpClientPool.borrowObject();
			boolean isExist = existDirectory(remotePath, ftpclient);
			if (!isExist) {
				mkDirectory(remotePath, ftpclient);
			}
			ftpclient.changeWorkingDirectory(remotePath);
			// 设置文件名上传的编码格式为 utf-8
			ftpclient.setBufferSize(1024);
			ftpclient.storeFile(filename, inStream);
		} catch (Exception e){
			 log.error("上传文件异常!", e);
		} finally{
			IOUtils.closeQuietly(inStream);
			//将对象放回池中
			ftpClientPool.returnObject(ftpclient);
		}

	}
	public void downFile(String filePath, String localPath) {
		OutputStream is=null;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			if(StringUtil.isNotEmpty(filePath)&&StringUtil.isNotEmpty(localPath)){
				String realName = filePath.substring(filePath.lastIndexOf("/") + 1);
				String path = filePath.substring(0, filePath.lastIndexOf("/") + 1);
				ftpclient.changeWorkingDirectory(path);// 转移到FTP服务器目录
				FileUtil.createDirectory(localPath);
				File localFile = new File(localPath + "/" + realName);
				is = new FileOutputStream(localFile);
				ftpclient.retrieveFile(realName, is);
			}
		} catch (Exception e){
			log.error("downFile异常!", e);
		} finally {
			release(is, ftpclient);
		}
	}



	public void downFile(String remotePath, String fileName, String localPath) {
		OutputStream is = null;
		FTPClient ftpclient = null;
		try {
			ftpclient =ftpClientPool.borrowObject();
			if (StringUtil.isNotEmpty(remotePath) && StringUtil.isNotEmpty(fileName) && StringUtil
					.isNotEmpty(localPath)) {
				ftpclient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
				FileUtil.createDirectory(localPath);
				File localFile = new File(localPath + "/" + fileName);
				is = new FileOutputStream(localFile);
				ftpclient.retrieveFile(fileName, is);
			}
		}catch (Exception e){
			log.error("downFile文件异常!", e);
		} finally {
			release(is, ftpclient);
		}
	}

	/**
	 * 用完连接后释放连接，关闭流
	 * @param is
	 * @param ftpclient
	 */
	private void release(OutputStream is, FTPClient ftpclient) {
		IOUtils.closeQuietly(is); //关闭流
		//将对象放回池中
		ftpClientPool.returnObject(ftpclient);
	}
	/**
	 * FTP批量下载文件.
	 *
	 * @param vsFiles
	 *            文件路径列表.
	 * @throws FtpException
	 */
	public FileVo downFiles(List<VsFile> vsFiles) {
		FileVo fileVo = null;
		OutputStream is = null;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			List<File> suFiles = Lists.newArrayList();
			List<File> falFiles = Lists.newArrayList();
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
							suFiles.add(localFile);
						} else {
							falFiles.add(localFile);
						}
						if (is != null) {
							is.close();
						}
					}
				}
			}
			if ((falFiles == null) || (falFiles.size() <= 0)) {
				fileVo = new FileVo(FileVo.SUCCESS, suFiles);
			} else if ((suFiles == null) || (suFiles.size() <= 0)) {
				fileVo = new FileVo(FileVo.FAIL, falFiles);
			} else {
				fileVo = new FileVo(suFiles, falFiles);
			}
		} catch (Exception e) {
			fileVo = new FileVo(FileVo.FAIL);
			log.error("downFile文件异常!", e);
		} finally {
			release(is, ftpclient);
		}
		return fileVo;
	}

	public boolean downFileWithName(HttpServletRequest request, HttpServletResponse response, String name,
			String realName, String path) {
		OutputStream os = null;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			ftpclient.changeWorkingDirectory(path);
			name = FileUtil.dealBrowserChina(request, name);
			response = FileUtil.dealFileHeader(response, null, null, name);
			os = response.getOutputStream();
			ftpclient.retrieveFile(realName, os);
			os.flush();
		} catch (Exception e) {
			log.error("downFileWithName文件异常!", e);
		} finally {
			ftpClientPool.returnObject(ftpclient);
		}
		return true;
	}


	public void removeFile(String filePath)  {
		FTPClient ftpclient = null;
		try {
			ftpclient =ftpClientPool.borrowObject();
			ftpclient.deleteFile(filePath);
		} catch (Exception e) {
			log.error("removeFile文件异常!", e);
		}finally {
			release(null, ftpclient);
		}
	}
	/**
	 * @param remotePath
	 *            要删除的文件所在ftp的路径名不包含ftp地址
	 * @param fileName
	 *            要删除的文件名
	 * @return
	 * @throws FtpException
	 * @throws IOException
	 * TODO 中文名的文件 无法删除
	 */
	public boolean removeFile(String remotePath, String fileName)  {
		boolean result = false;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			// ftpclient.enterLocalPassiveMode();//设置ftp为被动模式
			ftpclient.changeWorkingDirectory(remotePath);
			result = ftpclient.deleteFile(fileName);// 删除远程文件
		} catch (Exception e) {
			result = false;
			log.error("removeFile文件异常!", e);
		} finally {
			release(null, ftpclient);
		}
		return result;
	}

	/*
	 * private boolean cd(String dir) throws IOException { if
	 * (ftpclient.changeWorkingDirectory(dir)) { return true; } else { return
	 * false; } }
	 */



	public boolean rename(String from, String to) {
		boolean renameResult = false;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			renameResult = ftpclient.rename(from, to);
		} catch (Exception e) {
			log.error("重新命名失败",e);
		}finally {
			release(null, ftpclient);
		}
		return renameResult;
	}

	// 移动文件从临时目录到正式目录
	public String moveFile(String tmpPath)  {
		String realPath = null;
		FTPClient ftpclient = null;
		try {
			ftpclient = ftpClientPool.borrowObject();
			realPath = tmpPath.replace(FileDIREnum.TEMP.name(), "");
			if (tmpPath.equals(realPath)) {
				return realPath;
			}
			// 得到文件名后缀，用id到名称保存。
			String realDirectory = realPath.substring(0, realPath.lastIndexOf("/"));
			// 如果没有最终的文件夹目录则创建最终的文件夹目录
			boolean isExist = existDirectory(realDirectory, ftpclient);
			if (!isExist) {
				mkDirectory(realDirectory, ftpclient);
			}
			// 移出该文件
			ftpclient.rename(tmpPath, realPath);
		} catch (Exception e) {
			log.error("moveFile文件失败",e);
		} finally {
			release(null, ftpclient);
		}
		return realPath;
	}

	public String moveFile(String tmpPath,String newPath) {
		FTPClient  ftpclient =null;
		try {
			// 得到文件名后缀，用id到名称保存。
			String realDirectory = newPath.substring(0, newPath.lastIndexOf("/"));
			ftpclient = ftpClientPool.borrowObject();
			// 如果没有最终的文件夹目录则创建最终的文件夹目录
			boolean isExist = existDirectory(realDirectory,ftpclient);
			if (!isExist) {
        mkDirectory(realDirectory,ftpclient);
      }
			// 移出该文件
			ftpclient.rename(tmpPath, newPath);
		} catch (Exception e) {
			log.error("移出文件失败",e);
		} finally {
			release(null, ftpclient);
		}
		return newPath;
	}

	/**
	 * 复制文件.
	 * @param sourceFileName 来源文件名称
	 * @param sourceDir 来源文件夹
	 * @param targetDir 存放文件夹
	 * @throws IOException
	 */
	public boolean copyFile(String sourceFileName, String sourceDir, String targetFileName, String targetDir)  {
		ByteArrayInputStream in = null;
		FTPClient  ftpclient =null;
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		boolean renameResult = false;
		try {
			ftpclient = ftpClientPool.borrowObject();
			if (!existDirectory(targetDir,ftpclient)) {
				FileUtil.createDirectory(targetDir);
			}
			ftpclient.setBufferSize(1024);
			ftpclient.changeWorkingDirectory(sourceDir);
//			// 设置以二进制流的方式传输
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
			// 将文件读到内存中
			String inName=new String(sourceFileName.getBytes("utf-8"), "iso-8859-1");
			ftpclient.retrieveFile(inName, fos);

			in = new ByteArrayInputStream(fos.toByteArray());
			if (in != null) {
				ftpclient.changeWorkingDirectory(targetDir);
				renameResult=ftpclient.storeFile(new String(targetFileName.getBytes("utf-8"), "iso-8859-1"), in);
			}
		} catch (Exception e) {
			log.error("copyFile失败",e);
		} finally {
			IOUtils.closeQuietly(in); //关闭流
			IOUtils.closeQuietly(fos); //关闭流
			ftpClientPool.returnObject(ftpclient);//将对象放回池中

		}
		return renameResult;
	}

	//判断文件是否重名
	public static boolean checkName(FTPClient ftpClient,String ftpUrl, String newName) throws Exception {
		FTPFile[] files = listFiles(ftpClient,ftpUrl+"/"+newName);
		if (files.length > 0) {
			return false;
		} else {
			return true;
		}
	}

	private static FTPFile[] listFiles(FTPClient ftpClient,String fileName) throws IOException {
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
}
