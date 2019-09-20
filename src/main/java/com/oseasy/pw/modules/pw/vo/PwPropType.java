/**
 * .
 */

package com.oseasy.pw.modules.pw.vo;

import java.util.Map;

import com.google.common.collect.Maps;
import com.oseasy.com.pcore.modules.sys.vo.CorePropType;
import com.oseasy.pw.modules.pw.service.PwRenewalRuleService;

/**
 * Pw属性.
 * @author chenhao
 */
public class PwPropType {
    /**
         * 获取配置Map.
         */
      public static Map<String, Object> getMaps(PwRenewalRuleService pwrrService) {
        Map<String, Object> maps = Maps.newHashMap();
        maps.put(CorePropType.SPT_ENTER.getKey(), pwrrService.get(CorePropType.SPT_ENTER.getId()));
        return maps;
      }

}
