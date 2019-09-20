/**
 * 源代码版权归[[os-easy]]公司所有.
 * @Project: ROOT
 * @Package com.oseasy.act.modules.actyw.tool.process.vo
 * @Description [[_GnodeType_]]文件
 * @date 2017年6月27日 下午4:02:08
 *
 */

package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;


/**
 * 业务流程任务节点执行类型.
 *
 * @author chenhao
 * @date 2017年6月27日 下午4:02:08
 *
 */
public enum GnodeTaskType {
    GTT_NONE("None", true, "签收（适用审核/打分）"), GTT_PARALLEL("Parallel", true, "并行（适用打分）"), GTT_SEQUENTIAL("Sequential", false, "顺序")
    ;

    private String key;
    private Boolean isShow;
    private String remark;

    private GnodeTaskType(String key, Boolean isShow, String remark) {
        this.key = key;
        this.isShow = isShow;
        this.remark = remark;
    }

    /**
     * 根据id获取GnodeTaskType .
     * @author chenhao
     * @param key 惟一标识
     * @return GnodeType
     */
    public static GnodeTaskType getByKey(String key) {
        GnodeTaskType[] entitys = GnodeTaskType.values();
        for (GnodeTaskType entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 获取所有可用GnodeTaskType .
     * @author chenhao
     * @param isShow 是否可用
     * @return List
     */
    public static List<GnodeTaskType> getAll(Boolean isShow) {
        GnodeTaskType[] entitys = GnodeTaskType.values();
        if(isShow == null){
            return Arrays.asList(entitys);
        }

        List<GnodeTaskType> gttypes = Lists.newArrayList();
        for (GnodeTaskType entity : entitys) {
            if ((isShow).equals(entity.getIsShow())) {
                gttypes.add(entity);
            }
        }
        return gttypes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"isShow\":\""  + this.isShow + "\",\"remark\":\"" + this.remark + "\"}";
    }
}
