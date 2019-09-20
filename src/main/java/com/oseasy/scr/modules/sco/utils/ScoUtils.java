/**
 * .
 */

package com.oseasy.scr.modules.sco.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oseasy.act.modules.actyw.tool.process.vo.FlowPtype;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.scr.modules.sco.service.ScoAffirmConfService;

/**
 * 学分模块工具类.
 * @author chenhao
 */
public class ScoUtils {
    private static ScoAffirmConfService scoAffirmConfService = SpringContextHolder.getBean(ScoAffirmConfService.class);
    public static List<Dict> getPublishDictList() {
        List<Dict> l=new ArrayList<Dict>();
        Set<String> pset=scoAffirmConfService.getTypeSetData("1,");
        Set<String> gset=scoAffirmConfService.getTypeSetData("7,");
        if (pset!=null&&pset.size()>0) {
            List<Dict> l2=scoAffirmConfService.getDictForScoAffirm(pset,FlowPtype.PTT_XM.getKey());
            for(Dict d:l2) {
                d.setValue("1-"+d.getValue());
                l.add(d);
            }
        }
        if (gset!=null&&gset.size()>0) {
            List<Dict> l2=scoAffirmConfService.getDictForScoAffirm(gset,FlowPtype.PTT_DASAI.getKey());
            for(Dict d:l2) {
                d.setValue("7-"+d.getValue());
                l.add(d);
            }
        }
        return l;
    }
}
