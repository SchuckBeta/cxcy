/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import java.util.List;

import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public interface IeIdxVos {
    public List<ItIdxVo> initIdxVos();

    /**
     * 获取下载导出附件列名索引.
     */
    public static Integer getIdx(List<ItIdxVo> idxVos, String key) {
        return getIdx(idxVos, key, null);
    }
    public static Integer getIdx(List<ItIdxVo> idxVos, String key, List<String> filter) {
        if(idxVos == null){
            return null;
        }

        for (ItIdxVo itIdxVo : idxVos) {
            if(StringUtil.checkNotEmpty(filter) && (filter).contains(itIdxVo.getKey())){
               continue;
            }
            if((itIdxVo.getKey()).equals(key)){
                return itIdxVo.getIdx();
            }
        }
        return null;
    }
}
