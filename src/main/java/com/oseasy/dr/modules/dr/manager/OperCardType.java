/**
 * .
 */

package com.oseasy.dr.modules.dr.manager;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * .
 * @author chenhao
 *
 */
public enum OperCardType {
    READ(1,"Net.PC15.FC8800.Command.Card.ReadCardDetail"),
    ADD(2,"Net.PC15.FC8800.Command.Card.WriteCardListBySort_Result"),
    DELETE(3,"Net.PC15.FC8800.Command.Card.DeleteCard"),
    VERSION(4,"Net.PC15.FC8800.Command.System.ReadVersion"),
    ADDAll(5,"Net.PC15.FC8800.Command.Card.WriteCardListBySequence"),
    READNEW(6,"Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabaseByIndex")
    ;
    private int key;
    private String value;

    private OperCardType(int key,String value) {
        this.key = key;
        this.value=value;
    }


    /**
     * 获取卡状态.
     * @return List
     */
    public static List<OperCardType> getAll(Boolean isShow) {
        if(isShow == null){
            isShow = true;
        }

        List<OperCardType> enty = Lists.newArrayList();
        OperCardType[] entitys = OperCardType.values();
        for (OperCardType entity : entitys) {
            if (entity.getKey() != null) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<OperCardType> getAll() {
        return getAll(true);
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\"}";
    }
}
