/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.idx;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.pie.modules.iep.tool.IeIdxVos;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;

/**
 * .
 * @author chenhao
 */
public class IdxPmGcontest implements IeIdxVos{
    @Override
    public List<ItIdxVo> initIdxVos() {
        List<ItIdxVo> idxVos = Lists.newArrayList();
        idxVos.add(new ItIdxVo(0, "name"));
        idxVos.add(new ItIdxVo(1, "leader"));
        idxVos.add(new ItIdxVo(2, "no"));
        idxVos.add(new ItIdxVo(3, "mobile"));
        idxVos.add(new ItIdxVo(4, "email"));
        idxVos.add(new ItIdxVo(5, "xueli"));
        idxVos.add(new ItIdxVo(6, "enter"));
        idxVos.add(new ItIdxVo(7, "outy"));
        idxVos.add(new ItIdxVo(8, "profes"));
        idxVos.add(new ItIdxVo(9, "groups"));
        idxVos.add(new ItIdxVo(10, "type"));
        idxVos.add(new ItIdxVo(11, "stage"));
        idxVos.add(new ItIdxVo(12, "introduction"));
        idxVos.add(new ItIdxVo(13, "hasfile"));
        return idxVos;
    }
}
