/**
 * .
 */

package com.oseasy.cms.modules.cms.vo;

import java.util.List;

/**
 * 栏目类型 0000000282.
 * @author chenhao
 *
 */
public enum CategoryType {
    CT_SCORE("0000000280", "学分认定"),
    CT_MSJT("0000000175", "名师讲堂"),
    CT_DASAI("0000000284", "双创大赛"),
    CT_DASAI_RD("0000000285", "大赛热点"),
    CT_DASAI_YXDSZS("0000000287", "优秀大赛展示"),
    CT_PRO_APP("0000000283", "项目申报"),
    CT_PRO_YXXMZS("0000000286", "优秀项目展示");

    private String key;
    private String name;

    private CategoryType(String key, String name) {
      this.key = key;
      this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return CategoryType
     */
    public static CategoryType getByKey(String key) {
      if ((key != null)) {
        CategoryType[] entitys = CategoryType.values();
        for (CategoryType entity : entitys) {
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
     * @return CategoryType
     */
    public static Boolean checkHas(List<CategoryType> fstypes, String key) {
        if (checkRet(fstypes, key) != null) {
            return true;
        }
        return false;
    }

    /**
     * 检查是否存在，并返回 .
     * @author chenhao
     * @param key 枚举标识
     * @return CategoryType
     */
    public static CategoryType checkRet(List<CategoryType> fstypes, String key) {
        for (CategoryType fstype : fstypes) {
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