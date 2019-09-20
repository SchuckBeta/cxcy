package com.oseasy.pro.modules.tpl.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oseasy.pro.modules.tpl.vo.IMparam;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FreeMarkers;
import com.oseasy.util.common.utils.ObjectUtil;
import com.oseasy.util.common.utils.file.FileType;

@Service
public class MsgService {
  public static final Logger log = LoggerFactory.getLogger(MsgService.class);

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
  public String render(IMparam param) {
      Map<String, Object> model = getDataModel(param);
      File file = new File(getWebPath(TplService.ROOT_MSG) + param.getTplFileName() + FileType.FT_FTL.getSuffer());
      try {
        String cotent = FileUtil.readFileToString(file);
        return FreeMarkers.renderString(cotent, model);
      } catch (IOException e) {
        log.warn("Msg 模板渲染失败！", e);
      }
    return null;
  }

  /**
   * 对模板参数特殊处理方法
   * @param param 模板参数
   * @return Map
   */
  private Map<String, Object> getDataModel(IMparam param) {
    return ObjectUtil.obj2Map(param);
  }
}
