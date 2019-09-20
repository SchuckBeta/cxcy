/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_RtOutgoing_]]文件
 * @date 2017年6月2日 下午2:22:56
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程节点关联关系.
 *
 * @author chenhao
 * @date 2017年6月2日 下午2:22:56
 *
 */
public class RtOutgoing {
  /**
   * resourceId : sid-EF1E73EA-D33F-4970-8BB4-059B7202490D.
   */

  private String resourceId;

  public RtOutgoing() {
    super();
  }

  public RtOutgoing(String resourceId) {
    super();
    this.resourceId = resourceId;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public static String listIdToStr(List<RtOutgoing> entitys, String split, Boolean hasEnd) {
      StringBuffer sb = new StringBuffer();
      if(hasEnd == null){
          hasEnd = false;
      }
      if(StringUtil.isEmpty(split)){
          return null;
      }

      boolean isFirst = true;
      for (RtOutgoing entity : entitys) {
          if (isFirst) {
              sb.append(entity.getResourceId());
              isFirst = false;
          }else{
              sb.append(split);
              sb.append(entity.getResourceId());
          }
          if(hasEnd){
              sb.append(split);
          }
      }
      return sb.toString();
  }
  public static String listIdToStr(List<RtOutgoing> entitys, String split) {
      return listIdToStr(entitys, split, false);
  }
  public static String listIdToStr(List<RtOutgoing> entitys) {
      return listIdToStr(entitys, StringUtil.DOTH);
  }
}
