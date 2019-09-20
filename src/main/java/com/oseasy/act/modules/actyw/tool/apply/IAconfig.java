/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.apply.impl.Aconfig;
import com.oseasy.act.modules.actyw.tool.process.vo.*;
import com.oseasy.act.modules.pro.service.ProProjectService;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;

import java.util.List;

/**
 * .
 * @author chenhao
 *
 */
public interface IAconfig {
    public final static String ACONFIG = "aconfig";

    /**
     * 生成配置信息.
     * @param flowType
     */
    public static Aconfig genAconfig(FlowType flowType){
        return genAconfig(flowType, null);
    }
    public static Aconfig genAconfig(FlowType flowType, ProProjectService proProjectService){
        Aconfig config = new Aconfig(flowType.getKey());
        if ((flowType).equals(FlowType.FWT_XM)) {
            config.setIsShowTime(true);
            config.setHasUpProp(true);

            config.setHasPtype(true);
            config.setDictPtype(DictUtils.getProDict(FlowPtype.PTT_XM.getKey()));
//            config.setDictPtypes(DictUtils.getProDicts(FlowPtype.PTT_XM.getKey(), proProjectService.getByProType(FlowProjectType.PMT_XM.key())));
            config.setDictPtypes(DictUtils.getProDicts(FlowPtype.PTT_XM.getKey(), null));
            config.setDictPtypeKey(FlowPtype.PTT_XM.getName());
            config.setDictPtypeks(FlowPtype.PTT_XM.getKey());

            config.setHasPctype(true);
            config.setDictPctype(DictUtils.getProDict(FlowPcategoryType.PCT_XM.getKey()));
            config.setDictPctypes(DictUtils.getDictListByType(FlowPcategoryType.PCT_XM.getKey()));
            config.setDictPctypeKey(FlowPcategoryType.PCT_XM.getName());
            config.setDictPctypeks(FlowPcategoryType.PCT_XM.getKey());
        } else if ((flowType).equals(FlowType.FWT_DASAI)) {
            config.setIsShowTime(true);
            config.setHasUpProp(true);

            config.setHasPtype(true);
            config.setDictPtype(DictUtils.getProDict(FlowPtype.PTT_DASAI.getKey()));
//            config.setDictPtypes(DictUtils.getProDicts(FlowPtype.PTT_DASAI.getKey(), proProjectService.getByProType(FlowProjectType.PMT_DASAI.key())));
            config.setDictPtypes(DictUtils.getProDicts(FlowPtype.PTT_DASAI.getKey(), null));
            config.setDictPtypeKey(FlowPtype.PTT_DASAI.getName());
            config.setDictPtypeks(FlowPtype.PTT_DASAI.getKey());

            config.setHasPctype(true);
            config.setDictPctype(DictUtils.getProDict(FlowPcategoryType.PCT_DASAI.getKey()));
            config.setDictPctypes(DictUtils.getDictListByType(FlowPcategoryType.PCT_DASAI.getKey()));
            config.setDictPctypeKey(FlowPcategoryType.PCT_DASAI.getName());
            config.setDictPctypeks(FlowPcategoryType.PCT_DASAI.getKey());
        } else if ((flowType).equals(FlowType.FWT_SCORE)) {
            config.setIsShowTime(true);
            config.setHasUpProp(false);

            config.setHasPtype(true);
            config.setDictPtype(DictUtils.getProDict(FlowPtype.PTT_SCORE.getKey()));
            config.setDictPtypes(DictUtils.getProDicts(FlowPtype.PTT_SCORE.getKey(), proProjectService.getByProType(FlowProjectType.PMT_SCORE.key())));
            config.setDictPtypeKey(FlowPtype.PTT_SCORE.getName());
            config.setDictPtypeks(FlowPtype.PTT_SCORE.getKey());
            config.setDefPtypeks("0000000164");//创新创业

            config.setHasPctype(false);
            config.setDictPctype(DictUtils.getProDict(FlowPcategoryType.PCT_SCORE.getKey()));
            config.setDictPctypes(DictUtils.getDictListByType(FlowPcategoryType.PCT_SCORE.getKey()));
            config.setDictPctypeKey(FlowPcategoryType.PCT_SCORE.getName());
            config.setDictPctypeks(FlowPcategoryType.PCT_SCORE.getKey());
        }else{
            config.setIsShowTime(true);
            config.setHasUpProp(false);
            config.setHasPtype(true);
            config.setHasPctype(false);
        }
        return config;
    }

    /**
     * 生成配置信息.
     * @param flowType FlowType
     * @param actyw ActYw
     * @return List
     */
    public static ActYw dicts(ActYw actyw){
        if ((FlowProjectType.PMT_XM).equals(actyw.getFptype())) {
            actyw.getProProject().setProCategoryds(DictUtils.getProDicts(FlowPcategoryType.PCT_XM.getKey(), actyw.getProProject().getProCategorys(), false));
            actyw.getProProject().setLevelds(DictUtils.getProDicts(FlowPlevelType.PLT_XM.getKey(), actyw.getProProject().getLevels(), false));
        } else if ((FlowProjectType.PMT_DASAI).equals(actyw.getFptype())) {
            actyw.getProProject().setProCategoryds(DictUtils.getProDicts(FlowPcategoryType.PCT_DASAI.getKey(), actyw.getProProject().getProCategorys(), false));
            actyw.getProProject().setLevelds(DictUtils.getProDicts(FlowPlevelType.PLT_DASAI.getKey(), actyw.getProProject().getLevels(), false));
        } else if ((FlowProjectType.PMT_SCORE).equals(actyw.getFptype())) {
            actyw.getProProject().setProCategoryds(DictUtils.getProDicts(FlowPcategoryType.PCT_SCORE.getKey(), actyw.getProProject().getProCategorys(), false));
            actyw.getProProject().setLevelds(DictUtils.getProDicts(FlowPlevelType.PLT_SCORE.getKey(), actyw.getProProject().getLevels(), false));
        }
        return actyw;
    }
}
