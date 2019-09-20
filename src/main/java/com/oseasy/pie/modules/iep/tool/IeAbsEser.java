/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pie.modules.iep.service.IepService;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.pie.modules.impdata.service.ImpInfoService;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;

/**
 * 通用流程导入.
 * @author chenhao
 *
 */
public abstract class IeAbsEser implements IeYwEser {
    public static IepService entityService = SpringContextHolder.getBean(IepService.class);
    public static ImpDataService impservice = SpringContextHolder.getBean(ImpDataService.class);
    public static ImpInfoService impIService = SpringContextHolder.getBean(ImpInfoService.class);
    public static ImpInfoErrmsgService impemsgService = SpringContextHolder.getBean(ImpInfoErrmsgService.class);
    public static final List<ItIdxVo> idxVos = Lists.newArrayList();

    /**
     * 获取下载导出附件列名索引.
     */
    public static Integer getIdx(String key) {
        if(idxVos == null){
            return null;
        }

        for (ItIdxVo itIdxVo : idxVos) {
            if((itIdxVo.getKey()).equals(key)){
                return itIdxVo.getIdx();
            }
        }
        return null;
    }
}
