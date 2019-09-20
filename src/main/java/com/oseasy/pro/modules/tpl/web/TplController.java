package com.oseasy.pro.modules.tpl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.tpl.service.TplService;

@Controller
public class TplController extends BaseController {
  @Autowired
  private TplService tplService;

  /**
   * 下载帮助文档，
   * @param fileName 文件名
   * @param request HTTP请求
   * @param response HTTP响应
   */
  @RequestMapping(value = "/downHelp")
  public String downHelp(String fileName, HttpServletRequest request, HttpServletResponse response) {
    return tplService.downtpl(TplService.ROOT_HELP, fileName, request, response);
  }
}
