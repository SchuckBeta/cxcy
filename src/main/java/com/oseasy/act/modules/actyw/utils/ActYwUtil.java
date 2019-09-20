/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process
 * @Description [[_ActYwUtil_]]文件
 * @date 2017年6月16日 下午6:31:46
 *
 */

package com.oseasy.act.modules.actyw.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.oseasy.act.modules.actyw.tool.process.vo.StenEsubType;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;

/**
 * 业务流程工具类.
 * @author chenhao
 * @date 2017年6月16日 下午6:31:46
 *
 */
public class ActYwUtil {
  public static String getSestRemark(String value, String defaultValue) {
    if (StringUtils.isNotBlank(value)) {
      return StenEsubType.getByType(value).getRemark();
    }
    return defaultValue;
  }

  public static String getSestType(String value, String defaultValue) {
    if (StringUtils.isNotBlank(value)) {
      return StenEsubType.getByType(value).getType();
    }
    return defaultValue;
  }

  public static List<StenEsubType> getSestAll() {
    return Arrays.asList(StenEsubType.values());
  }

  public static List<StenType> getStenTypeAll() {
    return Arrays.asList(StenType.values());
  }

  public static List<StenType> getStenTypeList(String subtype) {
    return StenType.getBySubType(subtype);
  }
}
