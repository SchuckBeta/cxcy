/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtStencilset_]]文件
 * @date 2017年6月2日 下午2:18:13
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;

/**
 * 流程BPMNDi命名空间信息.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:18:13
 *
 */
public class RtStencilset {
    private static Logger logger = LoggerFactory.getLogger(RtStencilset.class);
  /**
   * url : stencilsets/bpmn2.0/bpmn2.0.json namespace : http://b3mn.org/stencilset/bpmn2.0#
   */

  private String url;
  private String namespace;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  /**
   * 初始化边界.
   * @param gnode 流程节点
   * @return RtStencilset
   */
  public static RtStencilset init(ActYwGroup group) {
      RtStencilset rtb = null;
      String file = FileUtil.getClassPath(ActYwTool.FLOW_JSON_PATH) + RtStencilset.class.getSimpleName() + StringUtil.JSON;
      try {
          rtb = JsonAliUtils.readBeano(file, RtStencilset.class);
      } catch (Exception e) {
          logger.warn("RtStencilset:文件处理失败，使用默认数据生成，路径：" + file);
      }
      return rtb;
  }
}
