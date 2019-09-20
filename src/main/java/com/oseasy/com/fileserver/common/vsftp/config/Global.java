package com.oseasy.com.fileserver.common.vsftp.config;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Global {

  private static Logger logger = LoggerFactory.getLogger( Global.class);
  /**
   * 未区分各高校的 文件存放路径 上传ftp路径规则 -1
   * /租户id/tool/oseasy/
   **/
  public static String REMOTEPATH = "/tool/oseasy";
  public static final String FTP_DOWNURL = "/ftp/ueditorUpload/downFile";//FTP下载链接
  public static final String FTP_HTTPURL = CoreSval.getConfig("ftp.httpUrl");
  public static final String FTP_MARKER = "@5c319144e8474f329d29ac90a85a44c6";//html代码中ftp路径占位符




  public enum FileDIREnum {
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
      this.name = name;
    }
  }


  public static String ftpImgUrl(String part) {
    String fileUrl = null;
    if (StringUtil.isNotEmpty(part) && StringUtil.contains(part, "/tool")) {
      fileUrl = part;
      fileUrl = fileUrl.replace("/tool", "");
      fileUrl = FTP_HTTPURL + fileUrl;
    }
    return fileUrl;
  }

  public static String getFtpPath(String url) {
    if (StringUtil.isNotEmpty(url)&&StringUtil.contains(url,FTP_HTTPURL)) {
      url=url.replace(FTP_HTTPURL, "");
      url="/tool"+url;
    }
    return url;
  }


  public static void main(String[] args) {
    System.out.println(    FileDIREnum.TEMP.name());
  }
}
