package com.oseasy.util.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipUtil {

    private static final int buffer = 2048;

    public static final String ISO8859_1 = "ISO8859-1";
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";

    public static void main(String[] args) {
        String filePath ="D:/work/work1/trunk0420/v2.0.0.100/zip";
        unZip(filePath+"/批量下载_计划书utf81.zip");
//        unZip(filePath+"/导入附件GBK.zip");
    }

    /**
     * 解压Zip文件
     *
     * @param path 文件目录
     */
    public static void unZip(String path) {
        System.out.println("-----unZip---------------");
        int count = -1;
        String savepath = "";

        File file = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        savepath = path.substring(0, path.lastIndexOf(".")) ; //保存解压文件目录
        System.out.println("保存解压文件目录:::"+savepath);
        new File(savepath).mkdir(); //创建保存目录
        // 检测当前文件编码集.
        ZipFile zipFile = null;
        try {
            String charset = EncodingUtil.checkEncoding(new File(savepath));
//            zipFile = new ZipFile(path, "gbk"); //解决中文乱码问题
            // 根据ZIP文件创建ZipFile对象
            if((GBK).equals(charset)){
                zipFile = new ZipFile(path, GBK);
            }else if((UTF_8).equals(charset)){
                zipFile = new ZipFile(path, UTF_8);
            }else{
                zipFile = new ZipFile(path, charset);
            }
            Enumeration<?> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {

                byte buf[] = new byte[buffer];
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                filename = EncodingUtil.convertMessy(charset, entry.getName());
                System.out.println("处理后：" + filename  );


                boolean ismkdir = false;
                if (filename.lastIndexOf("/") != -1) { //检查此文件是否带有文件夹
                    ismkdir = true;
                }
                filename = savepath + filename;

                if (entry.isDirectory()) { //如果是文件夹先创建
                    file = new File(filename);
                    file.mkdirs();
                    continue;
                }
                file = new File(filename);
                if (!file.exists()) { //如果是目录先创建
                    if (ismkdir) {
                        new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                    }
                }
                file.createNewFile(); //创建文件

                is = zipFile.getInputStream(entry);
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos, buffer);

                while ((count = is.read(buf)) > -1) {
                    bos.write(buf, 0, count);
                }
                bos.flush();
                bos.close();
                fos.close();

                is.close();
            }

            zipFile.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}