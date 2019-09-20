package com.oseasy.pro.modules.tpl.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wversion;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.ObjectUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.file.FileType;

@Service
public class WordService {
  public static final Logger log = LoggerFactory.getLogger(WordService.class);

  public String getClassPath(String path) {
    String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
    return clzzPath+path;
  }

  public String getWebPath(String path) {
    String clzzPath = this.getClass().getResource(FileUtil.LINE).getPath();
    return clzzPath.replace(FileUtil.WEB_INF_CLASSES, "") + path;
  }

  /**
   * 模板渲染.
   * 只支持FTL模板，文件名不带后缀.
   * @param path 模板文件路径
   * @param param 参数
   */
  public String render(String path, IWparam param) {
    if (StringUtil.isNotEmpty(param.getFileName())) {
      Map<String, Object> model = getDataModel(param);
      File file = new File(getWebPath(TplService.ROOT_WORD) + path + FileUtil.LINE + param.getTplFileName() + FileType.FT_FTL.getSuffer());
      try {
        String cotent = FileUtil.readFileToString(file);
        return FreeMarkers.renderString(cotent, model);
      } catch (IOException e) {
        log.warn("Word 模板渲染失败！", e);
      }
    }
    return null;
  }

  /**
   * 对模板参数特殊处理方法
   * @param param 模板参数
   * @return Map
   */
  private Map<String, Object> getDataModel(IWparam param) {
    return ObjectUtil.obj2Map(param);
  }

  /**
   * 模板文件下载.
   * 只支持DOC，文件名不带后缀.
   * @param path 模板文件根路径
   * @param content 内容
   * @param fileName 文件名
   * @param request Http请求
   * @param response Http响应
   * @throws Exception
   */
  public void download(String path, String content, String fileName, HttpServletRequest request, HttpServletResponse response) {
    String targetFile = fileName + FileUtil.DOT + FileType.FT_DOC.getKey();
    String temPath = getWebPath(path) + FileUtil.LINE + targetFile;
    FileUtil.createFile(temPath);
    File tem = new File(temPath);
    try {
      FileUtil.write(tem, content);
    } catch (IOException e) {
      log.warn("Word 模板下载失败:", e);
    }
    FileUtil.downFile(tem, request, response, targetFile);
    FileUtil.deleteFile(temPath);
  }

  /**
   * 执行Word下载.
   * @param vsn 兼容版本（默认Office）
   * @param wordParam Word参数
   * @param fileName 下载文件名
   * @param request HttpRequest
   * @param response HttpResponse
   * @throws Exception 异常
   */
  public void exeDownload(String vsn, IWparam wordParam, HttpServletRequest request, HttpServletResponse response) {
    /**
     * 渲染模板并执行下载.
     */
    Wversion wv = Wversion.getByKey(vsn);
    if (wv == null) {
      wv = Wversion.V_OFFICE;
    }
    String path = FileUtil.LINE + wv.getKey() + FileUtil.LINE + FlowProjectType.PMT_XM.getValue();
    download(path, render(path, wordParam), wordParam.getFileName(), request, response);
  }
}
