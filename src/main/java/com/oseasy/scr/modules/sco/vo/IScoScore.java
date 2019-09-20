package com.oseasy.scr.modules.sco.vo;

public interface IScoScore<T> {
  /**
   * 计算最终学分.
   */
  public Double calculate(T entity);

  /**
   * 学分对应类型ID.
   */
  public String getId(T entity);
}
