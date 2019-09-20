package com.oseasy.util.common.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 模板资源管理接口
 */
public interface ResourceMng {
	/**
	 * 获得子文件列表
	 *
	 * @param path
	 *            父文件夹路径
	 * @param dirAndEditable
	 *            是否只获取文件夹和可编辑文件
	 * @return
	 */
	public List<FileWrap> listFile(String path, boolean dirAndEditable);

	/**
	 * 创建文件夹
	 *
	 * @param path
	 *            当前目录
	 * @param dirName
	 *            文件夹名
	 * @return
	 */
	public boolean createDir(String path, String dirName);

	/**
	 * 读取文件
	 *
	 * @param name
	 *            文件名称
	 * @return 文件内容
	 * @throws IOException
	 */
	public String readFile(String name) throws IOException;

	/**
	 * 更新文件
	 *
	 * @param name
	 *            文件名称
	 * @param data
	 *            文件内容
	 * @throws IOException
	 */
	public void updateFile(String name, String data) throws IOException;

	/**
	 * 文件重命名
	 *
	 * @param origName
	 *            原文件名
	 * @param destName
	 *            目标文件名
	 */
	public void rename(String origName, String destName);

	/**
	 * 删除文件
	 *
	 * @param names
	 * @return
	 */
	public int delete(String[] names);

	/**
	 * 列出所有模板方案
	 *
	 * @param path
	 *            模板方案路径
	 * @return
	 */
	public String[] getSolutions(String path);

	/**
	 * 解压文件（插件）
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public void unZipFile(File file) throws IOException;

	/**
	 * 删除解压文件以及zip包本身
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public void deleteZipFile(File file) throws IOException;
	/**
	 * 从zip文件中读取文件内容
	 * @param file
	 * @param readFileName
	 * @return
	 * @throws IOException
	 */
	public String readFileFromZip(File file,String readFileName) throws IOException;
}
