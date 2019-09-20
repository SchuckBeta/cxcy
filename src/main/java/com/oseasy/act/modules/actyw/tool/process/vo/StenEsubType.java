/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_StenEsubType_]]文件
 * @date 2017年6月13日 下午6:09:38
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import com.oseasy.util.common.utils.StringUtil;

/**
 * 流程图节点元素子类型.
 * @author chenhao
 * @date 2017年6月13日 下午6:09:38
 *
 */
public enum StenEsubType {
    SES_CORE("0", "核心"),
    SES_EVENT_START("10", "开始事件"),
    SES_EVENT_END("20", "结束事件"),
    SES_TASK("30", "任务"),
    SES_JG("40", "结构"),
    SES_GATEWAY("50", "网关"),
    SES_FLOW("60", "链接对象"),
    SES_CATCH_EVENT("70", "中间捕捉事件"),
    SES_CTROW_EVENT("80", "中间抛出事件"),
    SES_BOUNDARY_EVENT("90", "边界事件"),
    SES_ZJ("95", "组件"),
    SES_YD("98", "泳道");

  /**
   * "1、核心|2、开始事件|3、任务|4、结构|5、网关|6、边界事件|7、中间捕捉事件|8、中间抛出事件|9、结束事件|10、泳道|11、组件|12、链接对象".
   */
  private String type;
  private String remark;

  private StenEsubType(String type, String remark) {
    this.type = type;
    this.remark = remark;
  }

  /**
   * 根据类型获取枚举 .
   *
   * @author chenhao
   * @param type
   *          类型
   * @return StenEsubType
   */
  public static StenEsubType getByType(String type) {
    StenEsubType[] entitys = StenEsubType.values();
    for (StenEsubType entity : entitys) {
      if ((StringUtil.isNotEmpty(type)) && (type).equals(entity.getType())) {
        return entity;
      }
    }
    return null;
  }

  public String getType() {
    return type;
  }

  public String getRemark() {
    return remark;
  }
}
