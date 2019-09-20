/**
 * .
 */

package com.oseasy.cms.modules.cms.vo;

import java.util.List;

/**
 * 留言类型.
 * @author chenhao
 *
 */
public enum GbookType {
    GT_PT("1", "普通"), GT_YJ("2", "意见"), GT_TS("3", "投诉");

    private String key;
    private String name;

    private GbookType(String key, String name) {
      this.key = key;
      this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return GbookType
     */
    public static GbookType getByKey(String key) {
      if ((key != null)) {
        GbookType[] entitys = GbookType.values();
        for (GbookType entity : entitys) {
          if ((key).equals(entity.getKey())) {
            return entity;
          }
        }
      }
      return null;
    }

    /**
     * 检查是否存在 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return GbookType
     */
    public static Boolean checkHas(List<GbookType> fstypes, String key) {
        if (checkRet(fstypes, key) != null) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否存在，并返回 .
     * @author chenhao
     * @param key 枚举标识
     * @return GbookType
     */
    public static GbookType checkRet(List<GbookType> fstypes, String key) {
        for (GbookType fstype : fstypes) {
            if ((fstype.getKey()).equals(key)) {
                return fstype;
            }
        }
        return null;
    }

    public String getKey() {
      return key;
    }

    public String getName() {
      return name;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }
}