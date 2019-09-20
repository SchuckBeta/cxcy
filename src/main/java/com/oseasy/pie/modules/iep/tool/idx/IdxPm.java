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
 *
 */
public class IdxPm implements IeIdxVos{
    @Override
    public List<ItIdxVo> initIdxVos() {
        List<ItIdxVo> idxVos = Lists.newArrayList();
        idxVos.add(new ItIdxVo(0, "office"));
        idxVos.add(new ItIdxVo(1, "name"));
        idxVos.add(new ItIdxVo(2, "number"));
        idxVos.add(new ItIdxVo(3, "type"));
        idxVos.add(new ItIdxVo(4, "leader"));
        idxVos.add(new ItIdxVo(5, "no"));
        idxVos.add(new ItIdxVo(6, "team_id"));
        idxVos.add(new ItIdxVo(7, "team_name"));
        idxVos.add(new ItIdxVo(8, "mobile"));
        idxVos.add(new ItIdxVo(9, "email"));
        idxVos.add(new ItIdxVo(10, "profes"));
        idxVos.add(new ItIdxVo(11, "members"));
        idxVos.add(new ItIdxVo(12, "teachers"));
        idxVos.add(new ItIdxVo(13, "tea_no"));
        idxVos.add(new ItIdxVo(14, "tea_title"));
        idxVos.add(new ItIdxVo(15, "year"));
        idxVos.add(new ItIdxVo(16, "result"));
        idxVos.add(new ItIdxVo(17, "hasfile"));
        idxVos.add(new ItIdxVo(18, "introduction"));
        idxVos.add(new ItIdxVo(19, "level"));
        idxVos.add(new ItIdxVo(20, "short_name"));
        idxVos.add(new ItIdxVo(21, "stage"));
        idxVos.add(new ItIdxVo(22, "source"));

        idxVos.add(new ItIdxVo(23, "resultType"));
        idxVos.add(new ItIdxVo(24, "resultContent"));
        idxVos.add(new ItIdxVo(25, "budgetDollar"));
        idxVos.add(new ItIdxVo(26, "budget"));
        idxVos.add(new ItIdxVo(27, "innovation"));
        idxVos.add(new ItIdxVo(28, "planStep"));
        idxVos.add(new ItIdxVo(29, "planContent"));
        idxVos.add(new ItIdxVo(30, "planStartDate"));
        idxVos.add(new ItIdxVo(31, "planEndDate"));
        return idxVos;
    }
}
