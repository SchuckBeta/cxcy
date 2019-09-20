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
public class IdxDr implements IeIdxVos{
    @Override
    public List<ItIdxVo> initIdxVos() {
        List<ItIdxVo> idxVos = Lists.newArrayList();
        idxVos.add(new ItIdxVo(0, "login_name"));
        idxVos.add(new ItIdxVo(1, "name"));
        idxVos.add(new ItIdxVo(2, "no"));
        idxVos.add(new ItIdxVo(3, "mobile"));
        idxVos.add(new ItIdxVo(4, "email"));
        idxVos.add(new ItIdxVo(5, "remarks"));
        idxVos.add(new ItIdxVo(6, "birthday"));
        idxVos.add(new ItIdxVo(7, "id_type"));
        idxVos.add(new ItIdxVo(8, "id_no"));
        idxVos.add(new ItIdxVo(9, "tmp_sex"));
        idxVos.add(new ItIdxVo(10, "domain"));
        idxVos.add(new ItIdxVo(11, "degree"));
        idxVos.add(new ItIdxVo(12, "education"));
        idxVos.add(new ItIdxVo(13, "tmp_office"));
        idxVos.add(new ItIdxVo(14, "professional"));
        idxVos.add(new ItIdxVo(15, "t_class"));
        idxVos.add(new ItIdxVo(16, "country"));
        idxVos.add(new ItIdxVo(17, "area"));
        idxVos.add(new ItIdxVo(18, "national"));
        idxVos.add(new ItIdxVo(19, "political"));
        idxVos.add(new ItIdxVo(20, "projectExperience"));
        idxVos.add(new ItIdxVo(21, "contestExperience"));
        idxVos.add(new ItIdxVo(22, "award"));
        idxVos.add(new ItIdxVo(23, "enterDate"));
        idxVos.add(new ItIdxVo(24, "temporary_date"));
        idxVos.add(new ItIdxVo(25, "graduation"));
        idxVos.add(new ItIdxVo(26, "address"));
        idxVos.add(new ItIdxVo(27, "instudy"));
        idxVos.add(new ItIdxVo(28, "curr_state"));
        idxVos.add(new ItIdxVo(29, "tmp_no"));
        idxVos.add(new ItIdxVo(30, "expiry"));
        return idxVos;
    }
}
