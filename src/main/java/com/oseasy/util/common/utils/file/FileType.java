package com.oseasy.util.common.utils.file;

public enum FileType {
  FT_DOC("doc", ".doc", "WORD文件")
  ,FT_DOCX("docx", ".docx", "WORD文件")
  ,FT_CVS("cvs", ".cvs", "CVS文件")
  ,FT_XLS("xls", ".xls", "XLS文件")
  ,FT_PDF("docx", ".docx", "PDF文件")
  ,FT_FTL("ftl", ".ftl", "FTL文件")
  ,FT_JSON("json", ".json", "JSON文件")
  ;

 private String key;
 private String suffer;
 private String name;

 private FileType(String key, String suffer, String name) {
   this.key = key;
   this.suffer = suffer;
   this.name = name;
 }

 /**
  * 根据key获取枚举 .
  * @author chenhao
  * @param key 枚举标识
  * @return FileType
  */
 public static FileType getByKey(String key) {
   if ((key != null)) {
     FileType[] entitys = FileType.values();
     for (FileType entity : entitys) {
       if ((key).equals(entity.getKey())) {
         return entity;
       }
     }
   }
   return null;
 }

  public String getSuffer() {
  return suffer;
}

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }
}
