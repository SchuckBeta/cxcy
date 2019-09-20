package com.oseasy.com.fileserver.common.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.util.common.utils.file.FileNameUtils;
import com.oseasy.util.common.utils.file.FileWrap;
import com.oseasy.util.common.utils.file.FileWrap.FileComparator;
import com.oseasy.util.common.utils.file.ResourceMng;

public class FileResManager implements ResourceMng {
  // 插件权限配置key
  private static final String PLUG_PERMS = "plug.perms";
  // 插件配置文件前缀
  public static final String FORM_ROOT = "/WEB-INF/views/template/form";
  public static final Logger log = LoggerFactory.getLogger(FileResManager.class);

  public String getClassPath(String path) {
    String clzzPath = this.getClass().getResource("/").getPath();
    return clzzPath+path;
  }

  public String getRealPath(String path) {
    String clzzPath = this.getClass().getResource("/").getPath();
    return clzzPath.replace("/WEB-INF/classes", "")+"/"+path;
  }

  public List<FileWrap> listFile(String path, boolean dirAndEditable) {
      File parent = new File(getRealPath(path));
      if (parent.exists()) {
        File[] files;
        if (dirAndEditable) {
          files = parent.listFiles(filter);
        } else {
          files = parent.listFiles();
        }
        if (files != null) {
          Arrays.sort(files, new FileComparator());
          List<FileWrap> list = new ArrayList<FileWrap>(files.length);
          for (File f : files) {
            list.add(new FileWrap(f, ""));
          }
          return list;
        }
      }
      return new ArrayList<FileWrap>(0);
    }

  public boolean createDir(String path, String dirName) {
    File parent = new File(getRealPath(path));
    parent.mkdirs();
    File dir = new File(parent, dirName);
    return dir.mkdir();
  }

  public String readFile(String name) throws IOException {
    File file = new File(getRealPath(name));
    return FileUtils.readFileToString(file, FileNameUtils.UTF8);
  }

  public void updateFile(String name, String data) throws IOException {
    File file = new File(getRealPath(name));
    FileUtils.writeStringToFile(file, data, FileNameUtils.UTF8);
  }

  public int delete(String[] names) {
    int count = 0;
    File f;
    for (String name : names) {
      f = new File(getRealPath(name));
      if (FileUtils.deleteQuietly(f)) {
        count++;
      }
    }
    return count;
  }

  public void rename(String origName, String destName) {
    File orig = new File(getRealPath(origName));
    File dest = new File(getRealPath(destName));
    orig.renameTo(dest);
  }

  public String[] getSolutions(String path) {
    File tpl = new File(getRealPath(path));
    return tpl.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return dir.isDirectory();
      }
    });
  }

  public void unZipFile(File file) throws IOException {
    // 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
    // 解压之前要坚持是否冲突
    ZipFile zip = new ZipFile(file, "GBK");
    ZipEntry entry;
    String name;
    String filename;
    File outFile;
    File pfile;
    byte[] buf = new byte[1024];
    int len;
    InputStream is = null;
    OutputStream os = null;
    Enumeration<ZipEntry> en = zip.getEntries();
    while (en.hasMoreElements()) {
      entry = en.nextElement();
      name = entry.getName();
      if (!entry.isDirectory()) {
        name = entry.getName();
        filename = name;
        outFile = new File(getRealPath(filename));
        if (outFile.exists()) {
          break;
        }
        pfile = outFile.getParentFile();
        if (!pfile.exists()) {
          // pfile.mkdirs();
          createFolder(outFile);
        }
        try {
          is = zip.getInputStream(entry);
          os = new FileOutputStream(outFile);
          while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
          }
        } finally {
          if (is != null) {
            is.close();
            is = null;
          }
          if (os != null) {
            os.close();
            os = null;
          }
        }
      }
    }
    zip.close();
  }

  private String getPlugPerms(File file) throws IOException {
    ZipFile zip = new ZipFile(file, "GBK");
    ZipEntry entry;
    String name, filename;
    File propertyFile;
    String plugPerms = "";
    Enumeration<ZipEntry> en = zip.getEntries();
    while (en.hasMoreElements()) {
      entry = en.nextElement();
      name = entry.getName();
      if (!entry.isDirectory()) {
        name = entry.getName();
        filename = name;
        // 读取属性文件的plug.mark属性
        if (filename.startsWith(FORM_ROOT) && filename.endsWith(".properties")) {
          propertyFile = new File(getRealPath(filename));
          Properties p = new Properties();
          p.load(new FileInputStream(propertyFile));
          plugPerms = p.getProperty(PLUG_PERMS);
        }
      }
    }
    zip.close();
    return plugPerms;
  }

  public void deleteZipFile(File file) throws IOException {
    // 根据压缩包删除解压后的文件
    // 用默认编码或UTF-8编码解压会乱码！windows7的原因吗
    ZipFile zip = new ZipFile(file, "GBK");
    ZipEntry entry;
    String name;
    String filename;
    File directory;
    // 删除文件
    Enumeration<ZipEntry> en = zip.getEntries();
    while (en.hasMoreElements()) {
      entry = en.nextElement();
      if (!entry.isDirectory()) {
        name = entry.getName();
        filename = name;
        directory = new File(getRealPath(filename));
        if (directory.exists()) {
          directory.delete();
        }
      }
    }
    // 删除空文件夹
    en = zip.getEntries();
    while (en.hasMoreElements()) {
      entry = en.nextElement();
      if (entry.isDirectory()) {
        name = entry.getName();
        filename = name;
        directory = new File(getRealPath(filename));
        if (!directoryHasFile(directory)) {
          directory.delete();
        }
      }
    }
    zip.close();
  }

  public String readFileFromZip(File file, String readFileName) throws IOException {
    // 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
    // 解压之前要坚持是否冲突
    ZipFile zip = new ZipFile(file, "GBK");
    ZipEntry entry;
    String name;
    String filename;
    InputStream is = null;
    InputStreamReader reader = null;
    BufferedReader in = null;
    Enumeration<ZipEntry> en = zip.getEntries();
    StringBuffer buff = new StringBuffer();
    String line;
    while (en.hasMoreElements()) {
      entry = en.nextElement();
      name = entry.getName();
      if (!entry.isDirectory()) {
        name = entry.getName();
        filename = name;
        if (filename.endsWith(readFileName)) {
          try {
            is = zip.getInputStream(entry);
            reader = new InputStreamReader(is);
            in = new BufferedReader(reader);
            line = in.readLine();
            while (StringUtils.isNotBlank(line)) {
              buff.append(line);
              line = in.readLine();
            }
          } finally {
            if (is != null) {
              is.close();
              is = null;
            }
            if (reader != null) {
              reader.close();
              reader = null;
            }
            if (in != null) {
              in.close();
              in = null;
            }
          }
          break;
        }
      }
    }
    zip.close();
    return buff.toString();
  }

  private void createFolder(File f) {
    File parent = f.getParentFile();
    if (!parent.exists()) {
      parent.mkdirs();
      createFolder(parent);
    }
  }

  // 文件夹判断是否有文件
  private boolean directoryHasFile(File directory) {
    File[] files = directory.listFiles();
    if (files != null && files.length > 0) {
      for (File f : files) {
        if (directoryHasFile(f)) {
          return true;
        } else {
          continue;
        }
      }
      return false;
    } else {
      return false;
    }
  }

  // 文件夹和可编辑文件则显示
  private FileFilter filter = new FileFilter() {
    public boolean accept(File file) {
      return file.isDirectory() || FileWrap.editableExt(FilenameUtils.getExtension(file.getName()));
    }
  };
}
