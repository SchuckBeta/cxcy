package com.oseasy.pw.modules.pw.vo;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.DateUtil.Dtype;

public enum DtypeTerm {
  YEAR_10(3653, Dtype.YEAR, 10, "十年", false),
  YEAR_5(1826, Dtype.YEAR, 5, "五年", false),
  YEAR_4(1461, Dtype.YEAR, 4, "四年", false),
  YEAR_3(1095, Dtype.YEAR, 3, "三年", false),
  YEAR_2(730, Dtype.YEAR, 2, "二年", true),
  YEAR_1(365, Dtype.YEAR, 1, "一年", true),
  YEAR_A_HALF(183, Dtype.MONTH, 6, "半年", true),
  QUARTER(92, Dtype.MONTH, 3, "季度", false),
  MONTH(31, Dtype.MONTH, 1, "月", false),
  DAY(1, Dtype.DAY, 1, "天", false),
  Zero(0, Dtype.DAY, 0, "天", false);

  private Integer num;
  private Dtype type;
  private Integer tnum;
  private boolean enable;
  private String remarks;
  private DtypeTerm(Integer num, Dtype type, Integer tnum, String remarks, boolean enable) {
    this.num = num;
    this.tnum = tnum;
    this.type = type;
    this.remarks = remarks;
    this.enable = enable;
  }

  public boolean isEnable() {
    return enable;
}

public Integer getTnum() {
    return tnum;
  }

  public void setTnum(Integer tnum) {
    this.tnum = tnum;
  }

  public Integer getNum() {
    return num;
  }
  public Dtype getType() {
    return type;
  }
  public void setType(Dtype type) {
    this.type = type;
  }
  public String getRemarks() {
    return remarks;
  }

  /**
   * 根据num获取枚举 .
   * @author chenhao
   * @param num 枚举标识
   * @return DtypeTerm
   */
  public static DtypeTerm getByNum(Integer num) {
    if ((num != null)) {
      DtypeTerm[] entitys = DtypeTerm.values();
      for (DtypeTerm entity : entitys) {
        if ((entity.getNum() != null) && ((num).equals(entity.getNum()))) {
          return entity;
        }
      }
    }
    return null;
  }

  /**
   * 获取主题 .
   * @return List
   */
  public static List<DtypeTerm> getAll(Boolean enable) {
      if(enable == null){
          enable = true;
      }

      List<DtypeTerm> enty = Lists.newArrayList();
      DtypeTerm[] entitys = DtypeTerm.values();
      for (DtypeTerm entity : entitys) {
          if ((entity.getNum() != null) && (entity.isEnable())) {
              enty.add(entity);
          }
      }
      return enty;
  }

  public static List<DtypeTerm> getAll() {
      return getAll(true);
  }

  /**
   * 根据类型添加天数.
   * @param type Dtype年、半年、月、日、季度
   * @param num 添加数量
   * @param date
   * @return Integer
   */
  public static Integer addDayByType(Integer num, Date date) {
//      DtypeTerm dtypeTerm = getByNum(num);
//    return (dtypeTerm == null) ? num : addDayByType(dtypeTerm, date);
    return addDayByType(getByNum(num), date);
  }

  public static Integer addDayByType(DtypeTerm dtypeTerm, Date date) {
    if(dtypeTerm == null){
      return null;
    }
    return DateUtil.addDayByType(dtypeTerm.getType(), dtypeTerm.getTnum(), date, false);
  }

  @Override
  public String toString() {
      return "{\"num\":\"" + this.num + "\",\"type\":\"" + this.type+ "\",\"tnum\":\"" + this.tnum+ "\",\"remarks\":\"" + this.remarks + "\",\"enable\":\"" + this.enable + "\"}";
  }
}
