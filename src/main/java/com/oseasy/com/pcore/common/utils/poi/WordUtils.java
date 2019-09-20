package com.oseasy.com.pcore.common.utils.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordUtils {
  //创建.doc后缀的word
  public static void createWord(String path,String fileName) {
    //判断目录是否存在
    File file=new File(path);
    if (!file.exists()) file.mkdirs();
    //因为HWPFDocument并没有提供公共的构造方法 所以没有办法构造word
    //这里使用word2007及以上的XWPFDocument来进行构造word
    XWPFDocument document=new XWPFDocument();
    OutputStream stream=null;
    try {
      stream = new FileOutputStream(new File(file, fileName));
      document.write(stream);
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if (stream!=null)
        try {
          stream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  //向word中写入数据
  public static void writeDataDocx(String path,String data) {
    InputStream istream=null;
    OutputStream ostream=null;
    try {
      ostream = new FileOutputStream(path);
      XWPFDocument document=new XWPFDocument();
      //添加一个段落
      XWPFParagraph p1=document.createParagraph();
      p1.setAlignment(ParagraphAlignment.CENTER);
      XWPFRun r1=p1.createRun();
      r1.setText(data);
      r1.setStrike(true);
      document.write(ostream);
      System.out.println("创建word成功");
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if (istream!=null)
        try {
          istream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      if (ostream!=null)
        try {
          ostream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

  }

  //向word中写入数据
  public static void writeDataDoc(String path,String data) {
    OutputStream ostream=null;
    try {
      ostream = new FileOutputStream(path);
      ostream.write(data.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if (ostream!=null)
        try {
          ostream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  //读取数据 docx
  public static String readDataDocx(String filePath) {
    String content = "";
    InputStream istream=null;
    try {
      istream = new FileInputStream(filePath);
         XWPFDocument document=new XWPFDocument(istream);
         content=document.getLastParagraph().getText();
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if (istream!=null)
        try {
          istream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return content;
  }

}
