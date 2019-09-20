/**
 * .
 */

package com.oseasy.cms.modules.cms.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 审核状态.
 * @author chenhao
 *
 */
public enum CmsAuditstatus {
    CA_DSH("0", false, "待审核"),
    CA_SHTG("1", false, "审核通过"),
    CA_SHSB("2", false, "审核不通过")
    ;

    private CmsAuditstatus(String id, Boolean enable, String name) {
        this.id = id;
        this.enable = enable;
        this.name = name;
    }

    private String id;
    private Boolean enable;
    private String name;

    /**
     * 获取状态 .
     * @return List
     */
    public static List<CmsAuditstatus> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<CmsAuditstatus> enty = Lists.newArrayList();
        CmsAuditstatus[] entitys = CmsAuditstatus.values();
        for (CmsAuditstatus entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<CmsAuditstatus> getAll() {
        return getAll(true);
    }

    /**
     * 获取Ids .
     * @return List
     */
    public static List<String> getAllIds(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<String> ids = Lists.newArrayList();
        CmsAuditstatus[] entitys = CmsAuditstatus.values();
        for (CmsAuditstatus entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                ids.add(entity.getId());
            }
        }
        return ids;
    }
    public static List<String> getAllIds() {
        return getAllIds(true);
    }

    /**
     * 根据id获取CmsAuditstatus .
     * @author chenhao
     * @param id id惟一标识
     * @return CmsAuditstatus
     */
    public static CmsAuditstatus getById(String id) {
        if (id == null) {
            return null;
        }

        CmsAuditstatus[] entitys = CmsAuditstatus.values();
        for (CmsAuditstatus entity : entitys) {
            if ((entity.getId()).equals(id)) {
                return entity;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
    public Boolean getEnable() {
        return enable;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"enable\":\"" + this.enable + "\",\"name\":" + this.name + "\"}";
    }
}
