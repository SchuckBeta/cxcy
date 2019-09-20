package com.oseasy.pro.modules.tpl.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

@Service
public class TplService {
  public static final Logger logger = LoggerFactory.getLogger(TplService.class);
  // 短信文件根路径
  public static final String ROOT_MSG = "/WEB-INF/views/template/msg";
  // WORD文件根路径
  public static final String ROOT_WORD = "/WEB-INF/views/template/word";
  // Help文件根路径
  public static final String ROOT_HELP = "/WEB-INF/views/template/help";

  public String getClassPath(String path) {
    String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
    return clzzPath+path;
  }

  public String getWebPath(String path) {
    String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
    return clzzPath.replace(FileUtil.WEB_INF_CLASSES, "") + path;
  }

  /**
   * 下载模板文件.
   * @param root 根路径
   * @param fileName 文件名
   * @param request Http请求
   * @param response Http响应
   */
  @SuppressWarnings("deprecation")
  public String downtpl(String root, String fileName, HttpServletRequest request, HttpServletResponse response) {
    if (StringUtil.isNotEmpty(root) && StringUtil.isNotEmpty(fileName)) {
      try {
        fileName = URLDecoder.decode(URLDecoder.decode((String)request.getParameter(FileUtil.FILE_NAME), FileUtil.UTF_8));
        String path = root + FileUtil.LINE + fileName;
        return FileUtil.downFile(new File(getWebPath(path)), request, response);
      } catch (UnsupportedEncodingException e) {
        logger.warn("文件名转码异常", e);
        return "文件名转码异常";
      }
    }else{
      logger.info("下载失败");
      return "下载失败";
    }
  }
}
