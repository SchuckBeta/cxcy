/**
 * .
 */

package com.oseasy.util.common.utils.reg;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.reg.imp.RegGt;
import com.oseasy.util.common.utils.reg.imp.RegLtexGty;
import com.oseasy.util.common.utils.reg.imp.RegLtxGty;

/**
 * 正则范围类型.
 * @author chenhao
 *
 */
public enum RegType {
    EQ("10", RegOper.EQ, "x = k", "[x]", "等于x", "等于", true)
    ,EQ_OR("20", RegOper.EOR, "x = k", "[x]||[x]||[x]", "等于x", "等于", false)

    ,GTX_MAX("110", RegOper.DEF, "x < k", "(x,}", "大于x", "大于", true)
    ,GTEX_MAX("120", RegOper.DEF, "x <= k", "[x,}", "大于等于x", "大于等于", true)
    ,LTX_MIN("210", RegOper.DEF, "k < x", "{,x)", "小于x", "小于", true)
    ,LTEX_MIN("220", RegOper.DEF, "k <= x", "{,x]", "小于等于x", "小于等于", true)

    ,AD_GTX_LTY("130", RegOper.AND, "x < k < y", "(x,y)", "大于x小于y", "大于x小于y", false)
    ,AD_GTX_LTEY("140", RegOper.AND, "x < k <= y", "(x,y]", "大于x小于等于y", "大于x小于等于y", false)
    ,AD_GTEX_LTY("150", RegOper.AND, "x <= k < y", "[x,y)", "大于等于x小于y", "大于等于x小于y", false)
    ,AD_GTEX_LTEY("160", RegOper.AND, "x <= k <= y", "[x,y]", "大于等于x小于y", "大于等于x小于y", false)

    ,OR_LTX_GTY("230", RegOper.OR, "k < x || k > y", "{,x)||(y,}", "小于x或大于y", "小于x或大于y", false)
    ,OR_LTEX_GTY("240", RegOper.OR, "k <= x || k > y", "{,x]||(y,}", "小于等于x或大于y", "小于等于x或大于y", false)
    ,OR_LTX_GTEY("250", RegOper.OR, "k < x || k >= y", "{,x)||[y,}", "小于x或大于等于y", "小于x或大于等于y", false)
    ,OR_LTEX_GTEY("260", RegOper.OR, "k <= x || k >= y", "{,x]||[y,}", "小于等于x或大于等于y", "小于等于x或大于等于y", false)
    ;

    private String key;
    private String sname;
    private RegOper oper;// 匹配操作类型
    private String ruexp;// 匹配规则样例
    private String name;//
    private String rulename;//规则name
    private boolean enable;

    public static final String REG_K = "k";
    public static final String REG_X = "x";
    public static final String REG_Y = "y";
    public static final String REG_TYPES = "regTypes";
    /**
     * 日志对象.
     */
    protected static Logger logger = LoggerFactory.getLogger(RegType.class);

    private RegType(String key, RegOper oper, String sname, String ruexp, String name, String rulename, boolean enable) {
        this.key = key;
        this.name = name;
        this.rulename = rulename;
        this.oper = oper;
        this.ruexp = ruexp;
        this.sname = sname;
        this.enable = enable;
    }

    /**
     * 校验数据是否符合规则.
     * @param reg
     * @param val
     * @return boolean
     */
    public static boolean validate(RegVo reg, float val) {
        if(reg.getType() == null){
            return false;
        }

        if((RegType.GTX_MAX).equals(reg.getType())){
            return new RegGt().validate(reg, val);
        }else if((RegType.OR_LTX_GTY).equals(reg.getType())){
            return new RegLtxGty().validate(reg, val);
        }else if((RegType.OR_LTEX_GTY).equals(reg.getType())){
            return new RegLtexGty().validate(reg, val);
        }else{
            //TODO
            logger.warn("RegVo.type类型未定义！");
        }
        return false;
    }


    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return RegexType
     */
    public static RegType getByKey(String key) {
        if ((key != null)) {
            RegType[] entitys = RegType.values();
            for (RegType entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 获取主题 .
     *
     * @return List
     */
    public static List<RegType> getAll(Boolean enable) {
        if (enable == null) {
            enable = true;
        }

        List<RegType> enty = Lists.newArrayList();
        RegType[] entitys = RegType.values();
        for (RegType entity : entitys) {
            if ((entity.getKey() != null) && (entity.isEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }

    public static List<RegType> getAll() {
        return getAll(true);
    }

    /**
     * 转换Sname属性.
     */
    public String convertSname(RegType regType, String x, String y) {
        if (regType == null || StringUtil.isEmpty(regType.getRuexp())) {
            return null;
        }

        String buffer = regType.getRuexp();
        if (StringUtil.isNotEmpty(x)) {
            buffer = buffer.replaceAll(REG_X, x);
        }
        if (StringUtil.isNotEmpty(x)) {
            buffer = buffer.replaceAll(REG_Y, y);
        }
        return buffer;
    }

    /**
     * 转换Ruexp属性.
     */
    public String convertRuexp(RegType regType, String x, String y) {
        if (regType == null || StringUtil.isEmpty(regType.getRuexp())) {
            return null;
        }

        String buffer = regType.getRuexp();
        if (StringUtil.isNotEmpty(x)) {
            buffer = buffer.replaceAll(REG_X, x);
        }
        if (StringUtil.isNotEmpty(x)) {
            buffer = buffer.replaceAll(REG_Y, y);
        }

        return buffer;
    }

    public RegOper getOper() {
        return oper;
    }

    public String getSname() {
        return sname;
    }

    public String getRuexp() {
        return ruexp;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getRulename() {
        return rulename;
    }

    public void setRulename(String rulename) {
        this.rulename = rulename;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\",\"rulename\":\"" + this.rulename + "\",\"name\":\"" + this.name + "\",\"sname\":\"" + this.sname
                + "\",\"ruexp\":\"" + this.ruexp + "\",\"enable\":\"" + this.enable + "\"}";
    }
}