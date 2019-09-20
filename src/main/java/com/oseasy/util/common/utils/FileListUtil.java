package com.oseasy.util.common.utils;

import java.io.File;
import java.util.Vector;

public class FileListUtil {
  public Vector<String> getList(String dirName) throws Exception {
    Vector<String> ver = new Vector<String>();// 用做堆栈
    ver.add(dirName);

    while (ver.size() > 0) {
      File[] files = new File(ver.get(0).toString()).listFiles(); // 获取该文件夹下所有的文件(夹)名
      ver.remove(0);
      if (files != null) {
        int len = files.length;
        for (int i = 0; i < len; i++) {
          String tmp = files[i].getAbsolutePath();
          if (files[i].isDirectory()) {// 如果是目录，则加入队列。以便进行后续处理
            ver.add(tmp);
          }
        }
      }
    }
    return ver;
  }
}
