package com.oseasy.com.pcore.common.utils.poi;

/**
* 实现java用poi实现对word读取和修改操作
* @author chenh
*
*/
public class PoiUtil {

  //创建word
  public static void createWord(String path, String fileName) {
    WordUtils.createWord(path, fileName);
  }

  //向word中写入数据
  public static void writeDataDocx(String path,String data) {
    WordUtils.writeDataDocx(path,data);
  }

  //读取数据 docx
  public static String readDataDocx(String filePath) {
    return WordUtils.readDataDocx(filePath);
  }

  //创建xlsx
  public static void writeDataExcelx(String filePath, String[] dataExcel, String title,String[] unitTitle, String[] itemTitle) {
    ExcelUtils.writeDataExcelx(filePath,dataExcel,title,unitTitle,itemTitle);
  }

  //读取数据 xlsx
  public static String readDataExcelx(String filePath) {
    return ExcelUtils.readDataExcelx(filePath);
  }

  //创建pptx
  public static void writeDataPptx(String filePath, String titlePptx, String imagePath) {
    PptUtils.writeDataPptx(filePath,titlePptx,imagePath);
  }

  //读取pptx的内容
  public static String readDataPptx(String filePath) {
    return PptUtils.readDataPptx(filePath);
  }
}