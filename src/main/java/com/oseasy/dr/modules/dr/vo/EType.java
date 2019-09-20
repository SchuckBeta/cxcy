//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.oseasy.dr.modules.dr.vo;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;

public enum EType {
    FC8900(0,"FC8900"),
    FC8800(1,"FC8800"),
    MC5800(2,"MC5800");

    private int key;
    private String value;

    public static EType getByValue(String val) {
        for(EType e: EType.values()) {
            if ((e.getValue() == val)) {
                return e;
            }
        }
        return null;
    }

    private EType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    public void setKey(int key) {
            this.key = key;
        }

    public void setValue(String value) {
               this.value = value;
           }

    public static List<Object> getETypes(){
        List<Object> list = Lists.newArrayList();
        for (EType eType: EType.values()){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("value", eType.getValue());
            hashMap.put("label", eType.getKey());
            list.add(hashMap);
        }
        return list;
    }
}
