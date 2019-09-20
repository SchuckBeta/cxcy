package com.oseasy.pw.modules.pw.vo;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻变更说明.
 * @author chenhao
 */
public enum PwEnterBgremarks {
    R1("10","入驻团队申请")
    ,R2("20","入驻企业申请")
    ,R3("30","续期申请")
    ,R4("40","退孵申请")
    ,R5("50","场地变更申请")
    ,R6("60","企业变更申请")
    ,R7("70","项目变更申请")
    ,R8("80","团队变更申请")
    ,R9("90","管理员审核")
    ,R10("100","管理员变更")
    ,R110("110","管理员场地修改")
    ,R120("120","管理员退孵");

    public static final String PW_EBGREMARKS = "pwEnterBgremarks";
    private String key;
    private String name;

    public static String getTypeStringByType(String type) {
        List<String> typeList= Arrays.asList(type.split(StringUtil.DOTH));
        StringBuffer typeString=new StringBuffer();
        PwEnterBgremarks pwEnterBgremarks = null;
        for(int i=0;i<typeList.size();i++){
            String key=typeList.get(i);
            pwEnterBgremarks = PwEnterBgremarks.getByKey(key);
            typeString.append(pwEnterBgremarks.getName());
            typeString.append(StringUtil.DOTH);
        }
        return StringUtil.removeLastDotH(typeString.toString());
    }


    public static String getFrontTypeStringByType(String type) {
            List<String> typeList= Arrays.asList(type.split(StringUtil.DOTH));
            StringBuffer typeString=new StringBuffer();
            PwEnterBgremarks pwEnterBgremarks = null;
    //        for(int i=0;i<typeList.size();i++){
    //            String key=typeList.get(i);
    //            pwEnterBgremarks = PwEnterBgremarks.getByKey(key);
    //            typeString.append(pwEnterBgremarks.getName());
    //            typeString.append(StringUtil.DOTH);
    //        }
            if(StringUtil.checkEmpty(typeList)){
                return "";
            }
            if(typeList.size()>1){
                typeString.append("变更申请");
            }else{
                String key=typeList.get(0);
                pwEnterBgremarks = PwEnterBgremarks.getByKey(key);
                typeString.append(pwEnterBgremarks.getName());
            }
            return StringUtil.removeLastDotH(typeString.toString());
        }

    private PwEnterBgremarks(String key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return PwEnterType
     */
    public static PwEnterBgremarks getByKey(String key) {
        if ((key != null)) {
            PwEnterBgremarks[] entitys = PwEnterBgremarks.values();
            for (PwEnterBgremarks entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param keys
     *            枚举标识
     * @return PwEnterType
     */
    public static List<PwEnterBgremarks> getByKeys(String keys) {
        if ((keys != null)) {
            PwEnterBgremarks[] entitys = PwEnterBgremarks.values();
            List<PwEnterBgremarks> pwEnterStatuss = Lists.newArrayList();
            List<String> keyss = Arrays.asList(StringUtil.split(keys, StringUtil.DOTH));
            for (PwEnterBgremarks entity : entitys) {
                for (String key : keyss) {
                    if (StringUtil.isNotEmpty(key) && (key).equals(entity.getKey())) {
                        pwEnterStatuss.add(entity);
                    }
                }
            }
            return pwEnterStatuss;
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
        return "{\"key\":\"" + this.key + "\",\"name\":\"" + this.name + "\"}";
    }


}