package com.oseasy.com.fileserver.modules.attachment.config;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Global {

    private static Logger logger = LoggerFactory.getLogger(Global.class);
    /**
    * 未区分各高校的 文件存放路径
    * 上传ftp路径规则   /tool/oseasy/租户id/  【】
    *  公共的文件   存放  /tool/oseasy/pub/
    *
    **/
    private static String REMOTEPATH = File.separator +"tool/oseasy/";




    public enum FileDIREnum{
        //前台样式
        CMS("cms"),
        //首页展示图片
        CMSINDEXRESOURCE("cmsIndexResource"),
        //菜单图片
        MENUIMG("menuImg"),
        //项目
        PROJECT("project"),
        //系统
        SYS("sys"),
        //临时目录
        TEMP("temp"),
        //ue编辑器里
        UEDITOR("ueditor"),
        //ue编辑器里 旧
        UEEDIT("ueEdit"),
      //用户
        USER("user");

     	private String name;

     	public String getName() {
     		return name;
     	}

     	public void setName(String name) {
     		this.name = name;
     	}

     	private FileDIREnum(String name) {
     		this.name=name;
     	}
    }


}
