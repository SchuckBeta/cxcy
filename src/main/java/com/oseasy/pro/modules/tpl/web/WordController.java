package com.oseasy.pro.modules.tpl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.tpl.service.WordService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.WtplType;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.tpl.vo.impl.WparamApply;
import com.oseasy.pro.modules.tpl.vo.impl.WparamReport;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.file.FileType;

@Controller
@RequestMapping(value = "${adminPath}/word")
public class WordController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(WordController.class);

  @Autowired
  private WordService wordService;


  /**
   * 下载Word模板，指定vsn参数,支持不同Office兼容，
   * 默认下载WPS模板.
   * @param type 下载模板类型
   * @param vsn 兼容Office版本
   * @param request HTTP请求
   * @param response HTTP响应
   * @throws Exception 异常
   */
  @RequestMapping(value = "/downtpl")
  public void downtpl(String type, String vsn, HttpServletRequest request, HttpServletResponse response) throws Exception {
    IWparam wordParam = null;
    /**
     * 根据类型生成文件名(下载文件名、模板文件名、模拟数据文件名).
     * 默认下载申报模板.
     */
    Wtype wtype = null;
    if (StringUtil.isNotEmpty(type)) {
      wtype = Wtype.getByKey(type);
      if (wtype == null) {
        logger.info("Word 模板类型未定义！[type = "+type+"]");
      }
    }

    /**
     * 如果模板参数未定义，则使用系统默认模拟参数生成模板.
     */
    String fileName = null;
    String tplFileName = null;
    if ((wordParam == null) && (wtype != null)) {
      tplFileName = wtype.getName();
      fileName = IWparam.getFileTplPreFix() + wtype.getName();
      if ((wtype.getTpl()).equals(WtplType.TT_APPLY)) {
        wordParam = WparamApply.genWparam(fileName + FileType.FT_JSON.getSuffer());
      }else if ((wtype.getTpl()).equals(WtplType.TT_REPORT_JX) || (wtype.getTpl()).equals(WtplType.TT_REPORT_ZQ)) {
        wordParam = WparamReport.genWparam(fileName + FileType.FT_JSON.getSuffer());
      }
    }else{
      tplFileName = IWparam.FILE_TPL_PREFIX + Wtype.T_CXXL_APPLY.getName();
      fileName = IWparam.getFileTplPreFix() + Wtype.T_CXXL_APPLY.getName();
      wordParam = WparamApply.genWparam(fileName + FileType.FT_JSON.getSuffer());
    }

    if ((wordParam != null)) {
      wordParam.setFileName(fileName);
      wordParam.setTplFileName(tplFileName);
      wordService.exeDownload(vsn, wordParam, request, response);
      logger.info("下载成功");
    }else{
      logger.info("下载失败");
    }
  }
}
