/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.web
 * @Description [[_ActStenController_]]文件
 * @date 2017年6月2日 上午10:00:39
 *
 */

package com.oseasy.act.modules.actyw.web;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.act.modules.actyw.tool.process.ActYwResult;
import com.oseasy.act.modules.actyw.tool.process.ActYwSten;
import com.oseasy.act.modules.actyw.tool.process.vo.RtSvl;
import com.oseasy.act.modules.actyw.tool.process.vo.SnRules;
import com.oseasy.act.modules.actyw.tool.process.vo.SnStencils;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;


/**
 * 获取配置实体.
 * @author chenhao
 * @date 2017年6月2日 上午10:00:39
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/actyw/actSten/")
public class ActYwStenRestResource extends BaseController {
  /**
   * 获取json结构 .
   * @author chenhao
   * @param stencilset json文件名
   * @return ActYwResult
   */
  @ResponseBody
  @RequestMapping(value = "/{stencilset}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public ActYwResult getJson(@PathVariable String stencilset) {
    if (StringUtil.isEmpty(stencilset)) {
      return null;
    }

    try {
      return ActYwResult.genActYwResult(IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(stencilset+".json"), RtSvl.RtModelVal.UTF_8));
    } catch (Exception e) {
      throw new ActivitiException("Error while loading ActYwResult", e);
    }
  }

  /**
   * 获取json结构 .
   * @author chenhao
   * @param stencilset json文件名
   * @return ActYwResult
   */
  @ResponseBody
  @RequestMapping(value = "/str/{stencilset}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public String getJstr(@PathVariable String stencilset) {
      if (StringUtil.isEmpty(stencilset)) {
          return null;
      }

      try {
          return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(stencilset+".json"), RtSvl.RtModelVal.UTF_8);
      } catch (Exception e) {
          throw new ActivitiException("Error while loading ActYwResult", e);
      }
  }

  /**
   * 获取ActYwSten.SnStencils列表.
   * @author chenhao
   * @return List
   */
  @ResponseBody
  @RequestMapping(value = "/stencilset/stencils", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public List<SnStencils> getSnStencilsJson() {
      return ActYwSten.genActYwStenStencils();
  }

  /**
   * 获取ActYwSten.rules列表.
   * @author chenhao
   * @return List
   */
  @ResponseBody
  @RequestMapping(value = "/stencilset/rules", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public SnRules getSnRulesJson() {
    return ActYwSten.genActYwStenRules();
  }

  /**
   * 获取ActYwSten.rules列表.
   * @author chenhao
   * @return List
   */
  @ResponseBody
  @RequestMapping(value = "/stenTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
  public List<StenType> getStenTypes() {
    return Arrays.asList(StenType.values());
  }
}