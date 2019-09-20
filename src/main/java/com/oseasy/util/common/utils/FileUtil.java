/**
 *
 */
package com.oseasy.util.common.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.file.BrowserType;

/**
 * 文件操作工具类. 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 *
 * @version 2015-3-16
 */
public class FileUtil extends FileUtils {
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();

	public static final String SUFFIX_PDF="pdf";
	public static final String SUFFIX_ZIP="zip";
	public static final String SUFFIX_EXCEL_XLS="xls";
	public static final String SUFFIX_EXCEL_XLSX="xlsx";
	public static final String SUFFIX_DOCX="docx";
	public static final String SUFFIX_DOC="doc";
	private static final String USER_AGENT = "User-Agent";
	public static final String ISO8859_1 = "ISO8859-1";
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String DOT = ".";
	public static final String LINE = "/";
	public static final String LINE_M = "-";
	public static final String LINE_D = "_";
	public static final String FILE_NAME = "fileName";
	public static final String WEB_INF_CLASSES = "/WEB-INF/classes";
	public static final String TempFileDir = "tempFileDir";
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	static {
		allFtypes(); // 初始化文件类型信息
	}

	/**
	 * 常见文件头信息.
	 */
	private static void allFtypes() {
		FILE_TYPE_MAP.put("jpg", "FFD8FF"); // JPEG (jpg)
		FILE_TYPE_MAP.put("png", "89504E47"); // PNG (png)
		FILE_TYPE_MAP.put("gif", "47494638"); // GIF (gif)
		FILE_TYPE_MAP.put("tif", "49492A00"); // TIFF (tif)
		FILE_TYPE_MAP.put("bmp", "424D"); // Windows Bitmap (bmp)
		FILE_TYPE_MAP.put("dwg", "41433130"); // CAD (dwg)
		FILE_TYPE_MAP.put("html", "68746D6C3E"); // HTML (html)
		FILE_TYPE_MAP.put("rtf", "7B5C727466"); // Rich Text Format (rtf)
		FILE_TYPE_MAP.put("xml", "3C3F786D6C");
		FILE_TYPE_MAP.put("zip", "504B0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		FILE_TYPE_MAP.put("psd", "38425053"); // Photoshop (psd)
		FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A"); // Email  [thorough only] (eml)
		FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F"); // Outlook Express (dbx)
		FILE_TYPE_MAP.put("pst", "2142444E"); // Outlook (pst)
		FILE_TYPE_MAP.put("xls", "D0CF11E0"); // MS Word
		FILE_TYPE_MAP.put("doc", "D0CF11E0"); // MS Excel 注意：word 和 excel的文件头一样
		FILE_TYPE_MAP.put("mdb", "5374616E64617264204A"); // MS Access (mdb)
		FILE_TYPE_MAP.put("wpd", "FF575043"); // WordPerfect (wpd)
		FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("pdf", "255044462D312E"); // Adobe Acrobat (pdf)
		FILE_TYPE_MAP.put("qdf", "AC9EBD8F"); // Quicken (qdf)
		FILE_TYPE_MAP.put("pwl", "E3828596"); // Windows Password (pwl)
		FILE_TYPE_MAP.put("wav", "57415645"); // Wave (wav)
		FILE_TYPE_MAP.put("avi", "41564920");
		FILE_TYPE_MAP.put("ram", "2E7261FD"); // Real Audio (ram)
		FILE_TYPE_MAP.put("rm", "2E524D46"); // Real Media (rm)
		FILE_TYPE_MAP.put("mpg", "000001BA"); //
		FILE_TYPE_MAP.put("mov", "6D6F6F76"); // Quicktime (mov)
		FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); // Windows Media (asf)
		FILE_TYPE_MAP.put("mid", "4D546864"); // MIDI (mid)
	}

	public  static void word2PDF(String filedir,String fielname) throws Exception{
		synchronized(FileUtil.class){
			File dir=new File(filedir);
			dir.setExecutable(true, false);
			dir.setReadable(true, false);
			dir.setWritable(true, false);
			File doc=new File(filedir+File.separator+fielname);
			doc.setExecutable(true, false);
			doc.setReadable(true, false);
			doc.setWritable(true, false);
			String comd="soffice --invisible --convert-to pdf:writer_pdf_Export "+fielname;
			Process p=Runtime.getRuntime().exec(comd,null,new File(filedir));
			p.waitFor();
		}
	}
	public static void deleteFileOrDir(File dir) {
		if(dir!=null){
			if (dir.isDirectory()&&dir.list().length>0) {
	            for (String fps:dir.list()) {
	            	deleteFileOrDir(new File(dir,fps));
	            }
	        }
	        dir.delete();
		}
	}

    public static String getClassPath(String path) {
        URL url = FileUtils.class.getClass().getResource(FileUtil.LINE);
        if(url == null){
            url = Thread.currentThread().getContextClassLoader().getResource(FileUtil.LINE);
        }

        if(url == null){
            return null;
        }
        return url.getPath() + path;
    }

    public String getWebPath(String path) {
        String clzzPath = getClassPath(path);
        if(clzzPath == null){
            return null;
        }
        return clzzPath.replace(FileUtil.WEB_INF_CLASSES, "") + path;
    }

	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 *
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return FileUtil.copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 *
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param coverlay
	 *            如果目标文件已存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			logger.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			logger.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				logger.debug("目标文件已存在，准备删除!");
				if (!FileUtil.delFile(descFileName)) {
					logger.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				logger.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				logger.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					logger.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			logger.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
			return true;
		} catch (Exception e) {
			logger.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制整个目录的内容，如果目标目录存在，则不覆盖
	 *
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return FileUtil.copyDirectoryCover(srcDirName, descDirName, false);
	}

	/**
	 * 复制整个目录的内容
	 *
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @param coverlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			logger.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			logger.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				logger.debug("目标目录已存在，准备删除!");
				if (!FileUtil.delFile(descDirNames)) {
					logger.debug("删除目录 " + descDirNames + " 失败!");
					return false;
				}
			} else {
				logger.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			logger.debug("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				logger.debug("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		if (files == null) {
			logger.debug("文件不存在!");
			return false;
		}
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = FileUtil.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = FileUtil.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			logger.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
			return false;
		}
		logger.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
		return true;

	}

	/**
	 *
	 * 删除文件，可以删除单个文件或文件夹
	 *
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			logger.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileUtil.deleteFile(fileName);
			} else {
				return FileUtil.deleteDirectory(fileName);
			}
		}
	}

	/**
	 *
	 * 删除单个文件
	 *
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		return deleteFile(new File(fileName));
	}

	public static boolean deleteFile(File file) {
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				logger.debug("删除文件 " + file.getName() + " 成功!");
				return true;
			} else {
				logger.debug("删除文件 " + file.getName() + " 失败!");
				return false;
			}
		} else {
			logger.debug(file.getName() + " 文件不存在!");
			return true;
		}
	}

	/**
	 * 批量删除文件.
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static FileVo deleteFiles(List<File> files) {
		FileVo fileVo = null;
		List<File> suFiles = Lists.newArrayList();
		List<File> falFiles = Lists.newArrayList();
		for (File file : files) {
			if (deleteFile(file)) {
				suFiles.add(file);
			} else {
				falFiles.add(file);
			}
		}

		if (falFiles == null) {
			fileVo = new FileVo(FileVo.SUCCESS, suFiles);
		} else if (suFiles == null) {
			fileVo = new FileVo(FileVo.FAIL, falFiles);
		} else {
			fileVo = new FileVo(suFiles, falFiles);
		}
		return fileVo;
	}

	/**
	 *
	 * 删除目录及目录下的文件
	 *
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			logger.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		if (files == null) {
			logger.debug("文件不存在!");
			return false;
		}
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileUtil.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			logger.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			logger.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			logger.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 创建单个文件
	 *
	 * @param descFileName
	 *            文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			logger.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			logger.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				logger.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				logger.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				logger.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}

	/**
	 * 创建多个文件
	 *
	 * @param descFileNames
	 *            文件名，包含路径
	 * @return 返回FileVo
	 */
	public static FileVo createFiles(List<String> descFileNames) {
		List<File> suFiles = Lists.newArrayList();
		List<File> falFiles = Lists.newArrayList();
		for (String descFileName : descFileNames) {
			boolean isTrue = createFile(descFileName);
			if (isTrue) {
				suFiles.add(new File(descFileName));
			} else {
				falFiles.add(new File(descFileName));
			}
		}

		if (falFiles == null) {
			return new FileVo(FileVo.SUCCESS, suFiles);
		}

		if (suFiles == null) {
			return new FileVo(FileVo.FAIL, falFiles);
		}

		return new FileVo(suFiles, falFiles);
	}

	/**
	 * 创建目录
	 *
	 * @param descDirName
	 *            目录名,包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			logger.debug("目录 " + descDirNames + " 已存在!");
			return false;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			logger.debug("目录 " + descDirNames + " 创建成功!");
			return true;
		} else {
			logger.debug("目录 " + descDirNames + " 创建失败!");
			return false;
		}

	}

	/**
	 * 写入文件
	 *
	 *            要写入的文件
	 */
	public static void writeToFile(String fileName, String content, boolean append) {
		try {
			FileUtil.write(new File(fileName), content, "utf-8", append);
			logger.debug("文件 " + fileName + " 写入成功!");
		} catch (IOException e) {
			logger.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
		}
	}

	/**
	 * 写入文件
	 *
	 *            要写入的文件
	 */
	public static void writeToFile(String fileName, String content, String encoding, boolean append) {
		try {
			FileUtil.write(new File(fileName), content, encoding, append);
			logger.debug("文件 " + fileName + " 写入成功!");
		} catch (IOException e) {
			logger.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
		}
	}

	/**
	 * 压缩文件或目录
	 *
	 * @param srcDirName
	 *            压缩的根目录
	 * @param fileName
	 *            根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
	 * @param descFileName
	 *            目标zip文件
	 */
	public static void zipFiles(String srcDirName, String fileName, String descFileName) {
		// 判断目录是否存在
		if (srcDirName == null) {
			logger.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
			return;
		}
		File fileDir = new File(srcDirName);
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			logger.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
			return;
		}
		String dirPath = fileDir.getAbsolutePath();
		File descFile = new File(descFileName);
		try {
			ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(descFile));
			if ("*".equals(fileName) || "".equals(fileName)) {
				FileUtil.zipDirectoryToZipFile(dirPath, fileDir, zouts);
			} else {
				File file = new File(fileDir, fileName);
				if (file.isFile()) {
					FileUtil.zipFilesToZipFile(dirPath, file, zouts);
				} else {
					FileUtil.zipDirectoryToZipFile(dirPath, file, zouts);
				}
			}
			zouts.close();
			logger.debug(descFileName + " 文件压缩成功!");
		} catch (Exception e) {
			logger.debug("文件压缩失败：" + e.getMessage());
			e.printStackTrace();
		}

	}
	private static boolean cclm(String source) {
		boolean flag = true;
		char ws[] = new char[] { '"', '?', '&' };
//		char ws[] = new char[] { '"', '?', ' ', '\'', '&' };
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			for (int j = 0; j < ws.length; j++) {
				char v = ws[j];
				if (c == v) {
					flag = false;
				}
			}
			if ((int) c == 0xfffd) {
				flag = false;
			}
		}
		return flag;
	}

	public static boolean readyUnZipFile(String zipFilePath, String unZipDirectory) throws IOException{
	    return readyUnZipFile(zipFilePath, unZipDirectory, GBK);
	}
	public static boolean readyUnZipFile(String zipFilePath, String unZipDirectory, String encode)
			throws IOException {
		ZipFile zipFile =null;
		// 检测当前文件编码集.
		//String charset = EncodingUtil.checkEncoding(new File(zipFilePath));
		zipFile = new ZipFile(zipFilePath, encode);
		Enumeration<?> entries = zipFile.getEntries();
		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry) entries.nextElement();
			//System.out.println("处理前：" + zipEntry.getName()  /*+"  编码："+getEncoding(zipEntry.getName())*/ + "   是否乱码：" + cclm(zipEntry.getName()));
			String entryName = zipEntry.getName();
			//entryName = new String(zipEntry.getName().getBytes(GBK), GBK);
			if ((EncodingUtil.checkMessy(entryName)) || (!cclm(entryName))){
				return true;//乱码
			}
		}
		zipFile.close();
		return  false;
	}

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下.
     * @param zipFileName  需要解压的ZIP文件
     * @param descFileName 目标文件
     * @throws Exception
     */
    public static void unZipFiles(String zipFileName, String descFileName) throws Exception {
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }

        ZipFile zipFile = null;
        try {
            //当压缩文件内没有乱码文件时，默认UTF-8
            String encode = UTF_8;
            boolean readyRes = readyUnZipFile(zipFileName,descFileName, encode);
            if(!readyRes){
                zipFile = new ZipFile(zipFileName, encode);
            }

            if(readyRes){
                encode = GBK;
                readyRes = readyUnZipFile(zipFileName,descFileName, encode);
                if(!readyRes){
                    zipFile = new ZipFile(zipFileName, encode);
                }
            }

            if(readyRes){
                encode = EncodingUtil.checkEncoding(new File(zipFileName));
                readyRes = readyUnZipFile(zipFileName,descFileName, encode);
                if(!readyRes){
                    zipFile = new ZipFile(zipFileName, encode);
                }
            }

            if(zipFile == null){
                zipFile = new ZipFile(zipFileName, encode);
            }
//            zipFile = new ZipFile(zipFileName, GBK);
            // 根据ZIP文件创建ZipFile对象
//			if(readyRes){
//				zipFile = new ZipFile(zipFileName, UTF_8);
//			}else{
//				zipFile = new ZipFile(zipFileName, GBK);
//			}
            logger.info("当前文件转换编码集为：[" + zipFile.getEncoding() + "]");
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.getEntries();
            // 遍历所有entry
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
				entryName= entry.getName();
				// 获得entry的名字
//                entryName = EncodingUtil.convertMessy(zipFile.getEncoding(), entry.getName());
//				if (!cclm(entry.getName())){
//					entryName  = new String (entry.getName().getBytes("gbk"),"gbk");
//				}else{
//					entryName  = new String (entry.getName().getBytes("utf-8"),"utf-8");
//				}
                System.out.println("处理后：" + entryName );
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    entry.setUnixMode(755);// 解决linux乱码 文件设置644 目录设置755
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    entry.setUnixMode(644);// 解决linux乱码 文件设置644 目录设置755
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                // 打开文件输出流
                OutputStream os = null;
                // 从ZipFile对象中打开entry的输入流
                InputStream is = null;
                try {
                    os = new FileOutputStream(file);
                    is = zipFile.getInputStream(entry);
                    while ((readByte = is.read(buf)) != -1) {
                        os.write(buf, 0, readByte);
                    }
                } finally {
                    if (os != null) os.close();
                    if (is != null) is.close();
                }
            }
        } finally {
            if (zipFile != null) zipFile.close();
        }
    }

	/**
	 * 将目录压缩到ZIP输出流
	 *
	 * @param dirPath
	 *            目录路径
	 * @param fileDir
	 *            文件信息
	 * @param zouts
	 *            输出流
	 */
	public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			// 空的文件夹
			if (files != null) {
				if (files.length == 0) {
					// 目录信息
					ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
					try {
						zouts.putNextEntry(entry);
						zouts.closeEntry();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}

				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile()) {
						// 如果是文件，则调用文件压缩方法
						FileUtil.zipFilesToZipFile(dirPath, files[i], zouts);
					} else {
						// 如果是目录，则递归调用
						FileUtil.zipDirectoryToZipFile(dirPath, files[i], zouts);
					}
				}
			}
		}
	}

	/**
	 * 将文件压缩到ZIP输出流
	 *
	 * @param dirPath
	 *            目录路径
	 * @param file
	 *            文件
	 * @param zouts
	 *            输出流
	 */
	public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
		FileInputStream fin = null;
		ZipEntry entry = null;
		// 创建复制缓冲区
		byte[] buf = new byte[4096];
		int readByte = 0;
		if (file.isFile()) {
			try {
				// 创建一个文件输入流
				fin = new FileInputStream(file);
				// 创建一个ZipEntry
				entry = new ZipEntry(getEntryName(dirPath, file));
				// 存储信息到压缩文件
				zouts.putNextEntry(entry);
				// 复制字节到压缩文件
				while ((readByte = fin.read(buf)) != -1) {
					zouts.write(buf, 0, readByte);
				}
				zouts.closeEntry();
				fin.close();
				System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取待压缩文件在ZIP文件中entry的名字，即相对于跟目录的相对路径名
	 *            目录名
	 * @param file
	 *            entry文件名
	 * @return
	 */
	private static String getEntryName(String dirPath, File file) {
		String dirPaths = dirPath;
		if (!dirPaths.endsWith(File.separator)) {
			dirPaths = dirPaths + File.separator;
		}
		String filePath = file.getAbsolutePath();
		// 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
		if (file.isDirectory()) {
			filePath += "/";
		}
		int index = filePath.indexOf(dirPaths);

		return filePath.substring(index + dirPaths.length());
	}

	/**
	 * 根据“文件名的后缀”获取文件内容类型（而非根据File.getContentType()读取的文件类型）
	 *
	 * @param returnFileName
	 *            带验证的文件名
	 * @return 返回文件类型
	 */
	public static String getContentType(String returnFileName) {
		String contentType = "application/octet-stream";
		if (returnFileName.lastIndexOf(".") < 0)
			return contentType;
		returnFileName = returnFileName.toLowerCase();
		returnFileName = returnFileName.substring(returnFileName.lastIndexOf(".") + 1);
		if (returnFileName.equals("html") || returnFileName.equals("htm") || returnFileName.equals("shtml")) {
			contentType = "text/html";
		} else if (returnFileName.equals("apk")) {
			contentType = "application/vnd.android.package-archive";
		} else if (returnFileName.equals("sis")) {
			contentType = "application/vnd.symbian.install";
		} else if (returnFileName.equals("sisx")) {
			contentType = "application/vnd.symbian.install";
		} else if (returnFileName.equals("exe")) {
			contentType = "application/x-msdownload";
		} else if (returnFileName.equals("msi")) {
			contentType = "application/x-msdownload";
		} else if (returnFileName.equals("css")) {
			contentType = "text/css";
		} else if (returnFileName.equals("xml")) {
			contentType = "text/xml";
		} else if (returnFileName.equals("gif")) {
			contentType = "image/gif";
		} else if (returnFileName.equals("jpeg") || returnFileName.equals("jpg")) {
			contentType = "image/jpeg";
		} else if (returnFileName.equals("js")) {
			contentType = "application/x-javascript";
		} else if (returnFileName.equals("atom")) {
			contentType = "application/atom+xml";
		} else if (returnFileName.equals("rss")) {
			contentType = "application/rss+xml";
		} else if (returnFileName.equals("mml")) {
			contentType = "text/mathml";
		} else if (returnFileName.equals("txt")) {
			contentType = "text/plain";
		} else if (returnFileName.equals("jad")) {
			contentType = "text/vnd.sun.j2me.app-descriptor";
		} else if (returnFileName.equals("wml")) {
			contentType = "text/vnd.wap.wml";
		} else if (returnFileName.equals("htc")) {
			contentType = "text/x-component";
		} else if (returnFileName.equals("png")) {
			contentType = "image/png";
		} else if (returnFileName.equals("tif") || returnFileName.equals("tiff")) {
			contentType = "image/tiff";
		} else if (returnFileName.equals("wbmp")) {
			contentType = "image/vnd.wap.wbmp";
		} else if (returnFileName.equals("ico")) {
			contentType = "image/x-icon";
		} else if (returnFileName.equals("jng")) {
			contentType = "image/x-jng";
		} else if (returnFileName.equals("bmp")) {
			contentType = "image/x-ms-bmp";
		} else if (returnFileName.equals("svg")) {
			contentType = "image/svg+xml";
		} else if (returnFileName.equals("jar") || returnFileName.equals("var") || returnFileName.equals("ear")) {
			contentType = "application/java-archive";
		} else if (returnFileName.equals("doc") || returnFileName.equals("docx")) {
			contentType = "application/msword";
		} else if (returnFileName.equals("pdf")) {
			contentType = "application/pdf";
		} else if (returnFileName.equals("rtf")) {
			contentType = "application/rtf";
		} else if (returnFileName.equals("xls")) {
			contentType = "application/vnd.ms-excel";
		} else if (returnFileName.equals("ppt")) {
			contentType = "application/vnd.ms-powerpoint";
		} else if (returnFileName.equals("7z")) {
			contentType = "application/x-7z-compressed";
		} else if (returnFileName.equals("rar")) {
			contentType = "application/x-rar-compressed";
		} else if (returnFileName.equals("swf")) {
			contentType = "application/x-shockwave-flash";
		} else if (returnFileName.equals("rpm")) {
			contentType = "application/x-redhat-package-manager";
		} else if (returnFileName.equals("der") || returnFileName.equals("pem") || returnFileName.equals("crt")) {
			contentType = "application/x-x509-ca-cert";
		} else if (returnFileName.equals("xhtml")) {
			contentType = "application/xhtml+xml";
		} else if (returnFileName.equals("zip")) {
			contentType = "application/zip";
		} else if (returnFileName.equals("mid") || returnFileName.equals("midi") || returnFileName.equals("kar")) {
			contentType = "audio/midi";
		} else if (returnFileName.equals("mp3")) {
			contentType = "audio/mpeg";
		} else if (returnFileName.equals("ogg")) {
			contentType = "audio/ogg";
		} else if (returnFileName.equals("m4a")) {
			contentType = "audio/x-m4a";
		} else if (returnFileName.equals("ra")) {
			contentType = "audio/x-realaudio";
		} else if (returnFileName.equals("3gpp") || returnFileName.equals("3gp")) {
			contentType = "video/3gpp";
		} else if (returnFileName.equals("mp4")) {
			contentType = "video/mp4";
		} else if (returnFileName.equals("mpeg") || returnFileName.equals("mpg")) {
			contentType = "video/mpeg";
		} else if (returnFileName.equals("mov")) {
			contentType = "video/quicktime";
		} else if (returnFileName.equals("flv")) {
			contentType = "video/x-flv";
		} else if (returnFileName.equals("m4v")) {
			contentType = "video/x-m4v";
		} else if (returnFileName.equals("mng")) {
			contentType = "video/x-mng";
		} else if (returnFileName.equals("asx") || returnFileName.equals("asf")) {
			contentType = "video/x-ms-asf";
		} else if (returnFileName.equals("wmv")) {
			contentType = "video/x-ms-wmv";
		} else if (returnFileName.equals("avi")) {
			contentType = "video/x-msvideo";
		}
		return contentType;
	}

	/**
	 * 向浏览器发送文件下载，支持断点续传
	 *
	 * @param file
	 *            要下载的文件
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return 返回错误信息，无错误信息返回null
	 */
	public static String downFile(File file, HttpServletRequest request, HttpServletResponse response) {
		return downFile(file, request, response, null);
	}

	/**
	 * 向浏览器发送文件下载，支持断点续传
	 *
	 * @param file
	 *            要下载的文件
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param fileName
	 *            指定下载的文件名
	 * @return 返回错误信息，无错误信息返回null
	 */
	public static String downFile(File file, HttpServletRequest request, HttpServletResponse response,
			String fileName) {
		String error = null;
		if (file != null && file.exists()) {
			if (file.isFile()) {
				if (file.length() <= 0) {
					error = "该文件是一个空文件。";
				}
				if (!file.canRead()) {
					error = "该文件没有读取权限。";
				}
			} else {
				error = "该文件是一个文件夹。";
			}
		} else {
			error = "文件已丢失或不存在！";
		}
		if (error != null) {
			logger.debug("---------------" + file + " " + error);
			return error;
		}

		long fileLength = file.length(); // 记录文件大小
		long pastLength = 0; // 记录已下载文件大小
		int rangeSwitch = 0; // 0：从头开始的全文下载；1：从某字节开始的下载（bytes=27000-）；2：从某字节开始到某字节结束的下载（bytes=27000-39000）
		long toLength = 0; // 记录客户端需要下载的字节段的最后一个字节偏移量（比如bytes=27000-39000，则这个值是为39000）
		long contentLength = 0; // 客户端请求的字节总量
		String rangeBytes = ""; // 记录客户端传来的形如“bytes=27000-”或者“bytes=27000-39000”的内容
		RandomAccessFile raf = null; // 负责读取数据
		OutputStream os = null; // 写出数据
		OutputStream out = null; // 缓冲
		byte b[] = new byte[1024]; // 暂存容器

		if (request.getHeader("Range") != null) { // 客户端请求的下载的文件块的开始字节
			response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
			logger.debug("request.getHeader(\"Range\") = " + request.getHeader("Range"));
			rangeBytes = request.getHeader("Range").replaceAll("bytes=", "");
			if (rangeBytes.indexOf('-') == rangeBytes.length() - 1) {// bytes=969998336-
				rangeSwitch = 1;
				rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				pastLength = Long.parseLong(rangeBytes.trim());
				contentLength = fileLength - pastLength; // 客户端请求的是 969998336
															// 之后的字节
			} else { // bytes=1275856879-1275877358
				rangeSwitch = 2;
				String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
				String temp2 = rangeBytes.substring(rangeBytes.indexOf('-') + 1, rangeBytes.length());
				pastLength = Long.parseLong(temp0.trim()); // bytes=1275856879-1275877358，从第
															// 1275856879
															// 个字节开始下载
				toLength = Long.parseLong(temp2); // bytes=1275856879-1275877358，到第
													// 1275877358 个字节结束
				contentLength = toLength - pastLength; // 客户端请求的是
														// 1275856879-1275877358
														// 之间的字节
			}
		} else { // 从开始进行下载
			contentLength = fileLength; // 客户端要求全文下载
		}

		// 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。 响应的格式是:
		// Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
		// ServletActionContext.getResponse().setHeader("Content- Length", new
		// Long(file.length() - p).toString());
		response.reset(); // 告诉客户端允许断点续传多线程连接下载,响应的格式是:Accept-Ranges: bytes
		if (pastLength != 0) {
			response.setHeader("Accept-Ranges", "bytes");// 如果是第一次下,还没有断点续传,状态是默认的
															// 200,无需显式设置;响应的格式是:HTTP/1.1
															// 200 OK
			// 不是从最开始下载, 响应的格式是: Content-Range: bytes [文件块的开始字节]-[文件的总大小 -
			// 1]/[文件的总大小]
			logger.debug("---------------不是从开始进行下载！服务器即将开始断点续传...");
			switch (rangeSwitch) {
			case 1: { // 针对 bytes=27000- 的请求
				String contentRange = new StringBuffer("bytes ").append(new Long(pastLength).toString()).append("-")
						.append(new Long(fileLength - 1).toString()).append("/").append(new Long(fileLength).toString())
						.toString();
				response.setHeader("Content-Range", contentRange);
				break;
			}
			case 2: { // 针对 bytes=27000-39000 的请求
				String contentRange = rangeBytes + "/" + new Long(fileLength).toString();
				response.setHeader("Content-Range", contentRange);
				break;
			}
			default: {
				break;
			}
			}
		} else {
			// 是从开始下载
			logger.debug("---------------是从开始进行下载！");
		}

		try {
			String filename = dealBrowserChina(request, (StringUtil.isBlank(fileName) ? file.getName() : fileName));
			response = dealFileHeader(response, getContentType(file.getName()), contentLength, filename);
			os = response.getOutputStream();
			out = new BufferedOutputStream(os);
			raf = new RandomAccessFile(file, "r");
			try {
				switch (rangeSwitch) {
				case 0: { // 普通下载，或者从头开始的下载 同1
				}
				case 1: { // 针对 bytes=27000- 的请求
					raf.seek(pastLength); // 形如 bytes=969998336- 的客户端请求，跳过
											// 969998336 个字节
					int n = 0;
					while ((n = raf.read(b, 0, 1024)) != -1) {
						out.write(b, 0, n);
					}
					break;
				}
				case 2: { // 针对 bytes=27000-39000 的请求
					raf.seek(pastLength); // 形如 bytes=1275856879-1275877358
											// 的客户端请求，找到第 1275856879 个字节
					int n = 0;
					long readLength = 0; // 记录已读字节数
					while (readLength <= contentLength - 1024) {// 大部分字节在这里读取
						n = raf.read(b, 0, 1024);
						readLength += 1024;
						out.write(b, 0, n);
					}
					if (readLength <= contentLength) { // 余下的不足 1024 个字节在这里读取
						n = raf.read(b, 0, (int) (contentLength - readLength));
						out.write(b, 0, n);
					}
					break;
				}
				default: {
					break;
				}
				}
				out.flush();
				logger.debug("---------------下载完成！");
			} catch (IOException ie) {
				/**
				 * 在写数据的时候， 对于 ClientAbortException 之类的异常，
				 * 是因为客户端取消了下载，而服务器端继续向浏览器写入数据时， 抛出这个异常，这个是正常的。
				 * 尤其是对于迅雷这种吸血的客户端软件， 明明已经有一个线程在读取 bytes=1275856879-1275877358，
				 * 如果短时间内没有读取完毕，迅雷会再启第二个、第三个。。。线程来读取相同的字节段， 直到有一个线程读取完毕，迅雷会 KILL
				 * 掉其他正在下载同一字节段的线程， 强行中止字节读出，造成服务器抛 ClientAbortException。
				 * 所以，我们忽略这种异常
				 */
				logger.debug("提醒：向客户端传输时出现IO异常，但此异常是允许的，有可能客户端取消了下载，导致此异常，不用关心！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}

	public static HttpServletResponse dealFileHeader(HttpServletResponse response, String fileType, Long contentLength,
			String filename) {
		response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		if (fileType != null) {
			response.setContentType(fileType); // set the MIME type.
		}
		if (contentLength != null) {
			response.addHeader("Content-Length", String.valueOf(contentLength));
		}
		return response;
	}

	/**
	 * 处理不同浏览器中文乱码.
	 *
	 * @param request
	 *            请求
	 * @param filename
	 *            文件名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String dealBrowserChina(HttpServletRequest request, String filename)
			throws UnsupportedEncodingException {
		String USERAGENT = request.getHeader(USER_AGENT).toUpperCase();
		if ((USERAGENT).indexOf(BrowserType.BT_IE.getKey()) > 0) {
			filename = URLEncoder.encode(Encodes.urlEncode(filename), UTF_8);
		} else if (((USERAGENT).indexOf(BrowserType.BT_OPERA.getKey()) > 0)) {
			filename = new String(Encodes.urlEncode(filename).getBytes(UTF_8), ISO8859_1);
		} else if (((USERAGENT).indexOf(BrowserType.BT_CHROME.getKey()) > 0)) {
			filename = new String(Encodes.urlEncode(filename).getBytes(UTF_8), ISO8859_1);
		} else if ((USERAGENT).indexOf(BrowserType.BT_FIREFOX.getKey()) > 0) {
			filename = new String((filename).getBytes(UTF_8), ISO8859_1);
		}
		return filename;
	}

	/**
	 * 修正路径，将 \\ 或 / 等替换为 File.separator
	 *
	 * @param path
	 *            待修正的路径
	 * @return 修正后的路径
	 */
	public static String path(String path) {
		String p = StringUtil.replace(path, "\\", "/");
		p = StringUtil.join(StringUtil.split(p, "/"), "/");
		if (!StringUtil.startsWithAny(p, "/") && StringUtil.startsWithAny(path, "\\", "/")) {
			p += "/";
		}
		if (!StringUtil.endsWithAny(p, "/") && StringUtil.endsWithAny(path, "\\", "/")) {
			p = p + "/";
		}
		if (path != null && path.startsWith("/")) {
			p = "/" + p; // linux下路径
		}
		return p;
	}

	/**
	 * 获目录下的文件列表
	 *
	 * @param dir
	 *            搜索目录
	 * @param searchDirs
	 *            是否是搜索目录
	 * @return 文件列表
	 */
	public static List<String> findChildrenList(File dir, boolean searchDirs) {
		if (dir == null) {
			return null;
		}
		String[] dirs = dir.list();

		if (dirs == null) {
			return null;
		}

		List<String> files = Lists.newArrayList();
		for (String subFiles : dirs) {
			File file = new File(dir + "/" + subFiles);
			if (((searchDirs) && (file.isDirectory())) || ((!searchDirs) && (!file.isDirectory()))) {
				files.add(file.getName());
			}
		}
		return files;
	}

	/**
	 * 获取文件扩展名(返回小写)
	 *
	 * @param fileName
	 *            文件名
	 * @return 例如：test.jpg 返回： jpg
	 */
	public static String getFileExtension(String fileName) {
		if ((fileName == null) || (fileName.lastIndexOf(".") == -1)
				|| (fileName.lastIndexOf(".") == fileName.length() - 1)) {
			return null;
		}
		return StringUtil.lowerCase(fileName.substring(fileName.lastIndexOf(".") + 1));
	}

	/**
	 * 获取文件名
	 * @param path 文件路径
	 * @return 例如：xsss/test.jpg 返回： test.jpg
	 */
	public static String getFileName(String path) {
	    if ((path == null)
	            || (path.lastIndexOf("/") == -1)
	            || (path.lastIndexOf(".") == -1)
	            || (path.lastIndexOf(".") == path.length() - 1)) {
	        return null;
	    }
	    return path.substring(path.lastIndexOf("/") + 1);
	}

	/**
	 * 获取文件名，不包含扩展名
	 *
	 * @param fileName
	 *            文件名
	 * @return 例如：d:\files\test.jpg 返回：d:\files\test
	 */
	public static String getFileNameWithoutExtension(String fileName) {
		if ((fileName == null) || (fileName.lastIndexOf(".") == -1)) {
			return null;
		}
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

    /**
     * 检查当前文件类型.
     * @param imgFile 文件对象
     * @param flieType 文件后缀
     * @return Boolean
     */
	public static Boolean checkFileType(MultipartFile imgFile, String flieType) {
        String fname = imgFile.getOriginalFilename().toLowerCase();
        if(StringUtil.isEmpty(fname)){
            return false;
        }
        if (!fname.endsWith(flieType)) {
            logger.warn("文件类型不匹配("+flieType+")不等于("+fname+")");
            return false;
        }
        return true;
    }

	/**
	 * 获取图片文件类型.
	 * @param f  File
	 * @return fileType
	 */
	public final static String getFtypeImg(File f) {
		if (isImage(f)) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(f);
				Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
				if (!iter.hasNext()) {
					return null;
				}
				ImageReader reader = iter.next();
				iis.close();
				return reader.getFormatName();
			} catch (IOException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 根据文件判断文件类型.
	 * Discription:[getFileByFile,获取文件类型,包括图片,若格式不是已配置的,则返回null]
	 * @param file
	 * @return fileType
	 */
	public final static String getFtype(File file) {
		String filetype = null;
		byte[] b = new byte[50];
		try {
			InputStream is = new FileInputStream(file);
			is.read(b);
			filetype = getFtype(b);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filetype;
	}

	/**
	 * 根据流判断文件类型.
	 * @param b
	 * @return fileType
	 * @author:[shixing_11@sina.com]
	 */
	public final static String getFtype(byte[] b) {
		String filetypeHex = String.valueOf(getFileHex(b));
		Iterator<Map.Entry<String, String>> entryiterator = FILE_TYPE_MAP.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Map.Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * 根据流判断文件是否为图片.
	 * @param file
	 * @return boolean true 是 | false 否
	 */
	public static final boolean isImage(File file) {
		boolean flag = false;
		try {
			BufferedImage bufreader = ImageIO.read(file);
			int width = bufreader.getWidth();
			int height = bufreader.getHeight();
			if (width == 0 || height == 0) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (IOException e) {
			flag = false;
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 根据流获取文件Hash
	 * @param b
	 * @return String
	 */
	public final static String getFileHex(byte[] b) {
		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
