/**
 * .
 */

package com.oseasy.sys.modules.sys.vo;

/**
 * .
 * @author chenhao
 *
 */
public enum TeacherType {
    TY_XY("1", "校园导师"),
    TY_QY("2", "企业导师");

  private String key;
  private String name;

  public static final String TEACHER_TYPES = "teacherTypes";
  private TeacherType(String key, String name) {
    this.key = key;
    this.name = name;
  }

  /**
   * 根据key获取枚举 .
   *
   * @author chenhao
   * @param key 枚举标识
   * @return TeacherType
   */
  public static TeacherType getByKey(String key) {
    if ((key != null)) {
      TeacherType[] entitys = TeacherType.values();
      for (TeacherType entity : entitys) {
        if ((key).equals(entity.getKey())) {
          return entity;
        }
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

@Override
  public String toString() {
      return "{\"key\":\"" + this.key  + "\",\"name\":\"" + this.name + "\"}";
  }
}
