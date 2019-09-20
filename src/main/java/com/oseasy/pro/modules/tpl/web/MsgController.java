package com.oseasy.pro.modules.tpl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pro.modules.tpl.service.MsgService;
import com.oseasy.pro.modules.tpl.vo.IMparam;
import com.oseasy.pro.modules.tpl.vo.MsgType;
import com.oseasy.pro.modules.tpl.vo.impl.Mteam;
import com.oseasy.util.common.utils.StringUtil;

@Controller
@RequestMapping(value = "${adminPath}/msg")
public class MsgController extends BaseController {
  private Logger logger = LoggerFactory.getLogger(MsgController.class);

  @Autowired
  private MsgService msgService;

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
  public String render(String type, String vsn, HttpServletRequest request, HttpServletResponse response) throws Exception {
    IMparam param = null;
    /**
     * 根据类型生成文件名(下载文件名、模板文件名、模拟数据文件名).
     * 默认下载申报模板.
     */
    MsgType wtype = null;
    if (StringUtil.isNotEmpty(type)) {
      wtype = MsgType.getByKey(type);
      if (wtype == null) {
        logger.info("Msg 模板类型未定义！[type = "+type+"]");
      }
    }

    /**
     * 如果模板参数未定义，则使用系统默认模拟参数生成模板.
     */
    String tplFileName = null;
    if ((param == null) && (wtype != null)) {
      tplFileName = wtype.getName();
      if ((wtype).equals(MsgType.M_TEAM_YQ)) {
        param = Mteam.init(new Mteam());
      }else if ((wtype).equals(MsgType.M_TEAM_YQ)) {
        param = Mteam.init(new Mteam());
      }
    }

    if ((param != null)) {
      param.setTplFileName(tplFileName);
      logger.info("渲染成功");
      return msgService.render(param);
    }else{
      logger.info("渲染失败");
    }
    return null;
  }
}
