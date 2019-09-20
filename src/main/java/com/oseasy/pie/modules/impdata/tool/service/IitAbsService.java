/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.pie.modules.impdata.tool.IitService;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.pie.modules.impdata.tool.param.ItSupparam;

/**
 * 通用流程导入.
 * @author chenhao
 *
 */
public abstract class IitAbsService implements IitService<ItSupparam> {
    public static ImpDataService impservice = SpringContextHolder.getBean(ImpDataService.class);
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
