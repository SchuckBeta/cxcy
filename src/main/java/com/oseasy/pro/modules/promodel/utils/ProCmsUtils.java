/**
 *
 */
package com.oseasy.pro.modules.promodel.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.cms.modules.cms.entity.CmsTemplate;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.pro.modules.excellent.enums.ExcTemplateEnum;

/**
 * 内容管理工具类
 */
public class ProCmsUtils {
    /**获取优秀展示模板列表*/
    public static List<CmsTemplate> getExcTemplateList() {
        String path="/templates/modules/excellent/";
        List<CmsTemplate> list = Lists.newArrayList();
        for(ExcTemplateEnum rte:ExcTemplateEnum.values()) {
            CmsTemplate ct = CmsUtils.fileToObject(path,rte.getTempName());
            ct.setName(rte.getName());
            ct.setValue(rte.getValue());
            list.add(ct);
        }
        return list;
    }
}