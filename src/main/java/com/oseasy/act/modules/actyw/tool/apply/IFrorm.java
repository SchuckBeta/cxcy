/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.apply;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYwGform;
import com.oseasy.act.modules.actyw.tool.process.vo.FormStyleType;

/**
 * 流程表单实体接口.
 * @author chenhao
 */
public interface IFrorm {
    IGnode ignode();
    IForm iform();


    /**
     * 获取表单.
     * @param gforms 节点表单数据.
     * @param fstype 表单类型
     * @return ActYwGform
     */
    public static IFrorm gformFst(List<? extends IFrorm> gforms, FormStyleType fstype) {
        for (IFrorm gform : gforms) {
            if((fstype.getKey()).equals(gform.iform().styleType())){
                return gform;
            }
        }
        return null;
    }

    /**
     * 获取表单列表.
     * @param gforms 节点表单数据.
     * @param fstype 表单类型
     * @return List
     */
    public static List<IFrorm> gforms(List<IFrorm> gforms, FormStyleType fstype) {
        List<IFrorm> ngforms = Lists.newArrayList();
        for (IFrorm gform : gforms) {
            if((fstype.getKey()).equals(gform.iform().styleType())){
                ngforms.add(gform);
            }
        }
        return ngforms;
    }
}
