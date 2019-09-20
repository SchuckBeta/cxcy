/**
 * .
 */

package com.oseasy.util.common.utils;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 特殊标识符枚举(字典：sp_steel).
 * @author chenhao
 */
public enum SpSteel {
  ST_CNMAOH(5, "：", "：", false),
  ST_CNJUH(5, "。", "。", false),
  ST_CNWENH(5, "？", "？", false),
  ST_CNSLH(5, "……", "……", false),
  ST_CNJIANKHS(5, "^", "^", false),
  ST_CNDIANHS(5, "`", "`", false),
  ST_CNBLX(5, "~", "~", false),
  ST_CNGTH(5, "!", "!", false),
  ST_CNEMAIL(5, "@", "@", false),
  ST_CNJINH(5, "#", "#", false),
  ST_CNRENMB(5, "￥", "￥", false),
  ST_CNBFH(5, "%", "%", false),
  ST_CNAND(5, "&", "&", false),
  ST_CNDENGYUH(5, "=", "=", false),
  ST_CNKUOHL(5, "（", "（", false),
  ST_CNKUOHR(5, "）", "）", false),
  ST_CNFKUOHL(5, "【", "【", false),
  ST_CNFKUOHR(5, "】", "】", false),
  ST_CNFENH(5, "；", "；", false),
  SSL_DHS(1, "'", "'", false),
  SSL_DYHL(1, "‘", "‘", false),
  ST_DYHR(4, "’", "’", false),
  SSL_SYHL(1, "“", "“", false),
  ST_SYHR(4, "”", "”", false),
  ST_CNSKILL(5, "‘", "‘", false),
  ST_CNDUNH(3, "、", "、", false),
  ST_CNDOTH(5, "，", "，", false),
  ST_FXGN(5, "\n", "\n", false),
  ST_FXGT(5, "\t", "\t", false),
  ST_FXGR(5, "\r", "\r", false),
  ST_XGN(5, "/n", "/n", false),
  ST_XGT(5, "/t", "/t", false),
  ST_XGR(5, "/r", "/r", false),
  ST_KONGGE(5, " ", " ", false),
  ST_DOTH(5, ",", ",", false),
  ST_MAOH(5, ":", ":", false),
  ST_FENH(5, ";", ";", false),
  ST_JIANKHL(5, "<", "<", false),
  ST_JIANKHR(5, ">", ">", false),

  ST_MEIYUAN(5, "$", "[$]", true),
  ST_JIANKHS(5, "^", "\\^", true),
  ST_KUOHL(5, "(", "[(]", true),
  ST_KUOHR(5, ")", "[)]", true),
  ST_DAKHL(5, "{", "[{]", true),
  ST_DAKHR(5, "}", "[}]", true),
  ST_ZKHL(5, "[", "\\[", true),
  ST_ZKHR(5, "]", "\\]", true),
  ST_SHUX(5, "|", "[|]", true),
  ST_JIAH(5, "+", "[+]", true),
  ST_DIANH(5, ".", "[.]", true),
  ST_XINGH(5, "*", "[*]", true),
  ST_WENH(5, "?", "[?]", true),
  ST_LINE(5, "/", "[/]", false),
  ST_FXG(5, "\\", "\\\\", true),
  ST_NULL(5, "null", "null", false)
  ;

    public static void main(String[] args) {
//        String a ="a|:/na*+aa/nna##axm[x]*adCVs*34_a _09_b5*[/435^*&城池()^$$&*).{}+.|.)%%*(*.中国}34{45[]12.fd'*&999下面是中文的字符￥……{}【】。，；’“‘”？";
//        List<String> filters = Lists.newArrayList();
//        filters.add("？");
//        filters.add("“");
//        filters.add("}");
//        System.out.println(SpSteel.replaceAll(a, filters));

        System.out.println(SpSteel.getFrontSpSteel());
        System.out.println(SpSteel.checkNotFronts("123）412"));
        System.out.println(SpSteel.checkNotFronts("231'421"));
}


  private Integer key;
  private String remarks;//字符
  private boolean isTsfer;//转义
  private String tsfer;//转义

  public final static String SP_STEEL_DKEY = "sp_steel";

  private SpSteel(Integer key, String remarks, String tsfer, boolean isTsfer) {
    this.key = key;
    this.remarks = remarks;
    this.tsfer = tsfer;
    this.isTsfer = isTsfer;
  }

  /**
   * 根据关键字获取枚举 .
   *
   * @author chenhao
   * @param key
   *          关键字
   * @return SpSteel
   */
  public static SpSteel getByKey(Integer key) {
    SpSteel[] entitys = SpSteel.values();
    for (SpSteel entity : entitys) {
      if ((key).equals(entity.getKey())) {
        return entity;
      }
    }
    return null;
  }

  /**
   * 根据获取前台校验文本包含特殊字符的枚举 .
   * @param str 关键字
   * @return SpSteel
   */
  public static boolean checkNotFronts(String str) {
      List<SpSteel> es = getFrontSpSteels();
      for (SpSteel spSteel : es) {
          if((str).contains(spSteel.getTsfer())){
              return false;
          }
      }
      return true;
  }
  public static String getFrontSpSteel() {
      return SpSteel.listToStr(SpSteel.getFrontSpSteels());
  }
  public static List<SpSteel> getFrontSpSteels() {
      List<SpSteel> es = Lists.newArrayList();
//      es.add(SpSteel.SSL_DHS);
//      es.add(SpSteel.SSL_DYHL);
//      es.add(SpSteel.ST_DYHR);
//      es.add(SpSteel.SSL_SYHL);
//      es.add(SpSteel.ST_SYHR);
//      es.add(SpSteel.ST_CNSKILL);
//      es.add(SpSteel.ST_CNDUNH);
//      es.add(SpSteel.ST_CNDOTH);
      es.add(SpSteel.ST_FXGN);
      es.add(SpSteel.ST_FXGT);
      es.add(SpSteel.ST_FXGR);
      es.add(SpSteel.ST_XGN);
      es.add(SpSteel.ST_XGT);
      es.add(SpSteel.ST_XGR);
//      es.add(SpSteel.ST_KONGGE);
//      es.add(SpSteel.ST_DOTH);
      //es.add(SpSteel.ST_MAOH);
      es.add(SpSteel.ST_FENH);
      //es.add(SpSteel.ST_JIANKHL);
      //es.add(SpSteel.ST_JIANKHR);
      es.add(SpSteel.ST_MEIYUAN);
      es.add(SpSteel.ST_JIANKHS);
      es.add(SpSteel.ST_KUOHL);
      es.add(SpSteel.ST_KUOHR);
      es.add(SpSteel.ST_DAKHL);
      es.add(SpSteel.ST_DAKHR);
      es.add(SpSteel.ST_ZKHL);
      es.add(SpSteel.ST_ZKHR);
      es.add(SpSteel.ST_SHUX);
      es.add(SpSteel.ST_JIAH);
      es.add(SpSteel.ST_DIANH);
      es.add(SpSteel.ST_XINGH);
      es.add(SpSteel.ST_WENH);
      es.add(SpSteel.ST_LINE);
      es.add(SpSteel.ST_FXG);
      es.add(SpSteel.ST_NULL);
      return es;
  }

  /**
   * 枚举转字符串.
   */
  public static String listToStr(List<SpSteel> entity) {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < entity.size(); i++) {
          SpSteel centity = entity.get(i);
          if(i != 0){
              buffer.append(StringUtil.DOTH);
          }

          if(centity.getIsTsfer()){
              buffer.append(centity.getTsfer());
          }else{
              buffer.append(centity.getRemarks());
          }
      }
      return buffer.toString();
  }

  /**
   * 清除字符中的特殊字符.
   * @param str 原始字符
   * @param filters 字典字符过滤器
   * @return String
   */
  public static String replaceAll(String str, List<String> filters) {
      if(StringUtil.isNotEmpty(str)){
          SpSteel[] entitys = SpSteel.values();
          for (SpSteel entity : entitys) {
              if(entity.getIsTsfer()){
                  str = (str).replaceAll(entity.getTsfer(), StringUtil.EMPTY);
              }else{
                  str = (str).replaceAll(entity.getRemarks(), StringUtil.EMPTY);
              }
          }

          if(StringUtil.checkEmpty(filters)){
              return str;
          }

          for (String filter : filters) {
              for (SpSteel entity : entitys) {
                  if(!entity.getIsTsfer()){
                      continue;
                  }

                  if(((filter).equals(entity.getRemarks()) || (filter).equals(entity.getRemarks()))){
                      filter = entity.getTsfer();
                  }
              }
              str = (str).replaceAll(filter, StringUtil.EMPTY);
          }
      }
      return str;
  }

  public String getTsfer() {
    return tsfer;
  }

  public Integer getKey() {
    return key;
  }

  public String getRemarks() {
    return remarks;
  }

  public boolean getIsTsfer() {
    return isTsfer;
}

@Override
  public String toString() {
      return "{\"key\":\"" + this.key + "\",\"tsfer\":"  + this.tsfer + ",\"remarks\":\"" + this.remarks + "\"}";
  }
}
