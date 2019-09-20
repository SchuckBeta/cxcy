package com.oseasy.com.pcore.modules.sys.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SysDao {
  /**
   * 根据数据库获取时间.
   * type=0 :获取时间戳 就是日期时间
   * type=1 :获取当前 日期
   * type=2 :获取当前 时间
   * @param type
   * @return
   */
  public Date getDBCurDate(@Param("type") String type);
  /**
   * 根据数据库获取时间时间戳.
   * @return Long
   */
  public Long getDbCurLong();
}
