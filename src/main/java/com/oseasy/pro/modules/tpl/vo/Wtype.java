package com.oseasy.pro.modules.tpl.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.ObjectUtil;

import net.sf.json.JSONArray;

/**
 * Word 项目模板名.
 * @author chenhao
 */
public enum Wtype {
  T_CXXL_APPLY(WproType.PT_CXXL.getKey() + WtplType.TT_APPLY.getKey(), WproType.PT_CXXL, WtplType.TT_APPLY, WproType.PT_CXXL.getName() + WtplType.TT_APPLY.getName())
   ,T_CXXL_REPORT_ZQ(WproType.PT_CXXL.getKey() + WtplType.TT_REPORT_ZQ.getKey(), WproType.PT_CXXL, WtplType.TT_REPORT_ZQ, WproType.PT_CXXL.getName() + WtplType.TT_REPORT_ZQ.getName())
   ,T_CXXL_REPORT_JX(WproType.PT_CXXL.getKey() + WtplType.TT_REPORT_JX.getKey(), WproType.PT_CXXL, WtplType.TT_REPORT_JX, WproType.PT_CXXL.getName() + WtplType.TT_REPORT_JX.getName())
   ,T_CYSJ_APPLY(WproType.PT_CYSJ.getKey() + WtplType.TT_APPLY.getKey(), WproType.PT_CYSJ, WtplType.TT_APPLY, WproType.PT_CYSJ.getName() + WtplType.TT_APPLY.getName())
   ,T_CYSJ_REPORT_ZQ(WproType.PT_CYSJ.getKey() + WtplType.TT_REPORT_ZQ.getKey(), WproType.PT_CYSJ, WtplType.TT_REPORT_ZQ, WproType.PT_CYSJ.getName() + WtplType.TT_REPORT_ZQ.getName())
   ,T_CYSJ_REPORT_JX(WproType.PT_CYSJ.getKey() + WtplType.TT_REPORT_JX.getKey(), WproType.PT_CYSJ, WtplType.TT_REPORT_JX, WproType.PT_CYSJ.getName() + WtplType.TT_REPORT_JX.getName())
   ,T_CYXL_APPLY(WproType.PT_CYXL.getKey() + WtplType.TT_APPLY.getKey(), WproType.PT_CYXL, WtplType.TT_APPLY, WproType.PT_CYXL.getName() + WtplType.TT_APPLY.getName())
   ,T_CYXL_REPORT_ZQ(WproType.PT_CYXL.getKey() + WtplType.TT_REPORT_ZQ.getKey(), WproType.PT_CYXL, WtplType.TT_REPORT_ZQ, WproType.PT_CYXL.getName() + WtplType.TT_REPORT_ZQ.getName())
   ,T_CYXL_REPORT_JX(WproType.PT_CYXL.getKey() + WtplType.TT_REPORT_JX.getKey(), WproType.PT_CYXL, WtplType.TT_REPORT_JX, WproType.PT_CYXL.getName() + WtplType.TT_REPORT_JX.getName());

   private String key;
   private WproType type;
   private WtplType tpl;
   private String name;

   private Wtype(String key, WproType type, WtplType tpl, String name) {
     this.key = key;
     this.type = type;
     this.tpl = tpl;
     this.name=name;
   }

   /**
    * 根据key获取枚举 .
    *
    * @author chenhao
    * @param key 枚举标识
    * @return Wtype
    */
   public static Wtype getByKey(String key) {
     if ((key != null)) {
       Wtype[] entitys = Wtype.values();
       for (Wtype entity : entitys) {
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

    public WproType getType() {
      return type;
    }

    public WtplType getTpl() {
      return tpl;
    }

    public String getName() {
      return name;
    }

  public static String toJson() {
    List<Map<String, Object>> jos = Lists.newArrayList();
    Wtype[] wtypes = Wtype.values();
    for (Wtype wtype : wtypes) {
      Map<String, Object> jo = new HashMap<String, Object>();
      jo.put("key", wtype.getKey());
      jo.put("name", wtype.getName());
      Map<String, Object> jotype = new HashMap<String, Object>();
      jotype.put("key", wtype.getType().getKey());
      jotype.put("name", wtype.getType().getName());
      jo.put("type", jotype);
      Map<String, Object> jotpl = new HashMap<String, Object>();
      jotpl.put("key", wtype.getTpl().getKey());
      jotpl.put("name", wtype.getTpl().getName());
      jo.put("tpl", jotpl);
      jos.add(jo);
    }
    return JSONArray.fromObject(jos).toString();
  }
}
