package com.oseasy.com.pcore.common.persistence;

/**
 * Created by Administrator on 2019/6/20 0020.
 * 树层级枚举.
 */
public enum TreeLver {
    L0(0),L1(1),L2(2),L3(3),L4(4),L5(5),L6(6),L7(7),L8(8),L9(9),L10(10),L11(11),L12(12),L13(13),L14(14);

    private Integer key;

    private TreeLver(Integer key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key 枚举标识
     * @return TreeLver
     */
    public static TreeLver getByKey(Integer key) {
        if ((key != null)) {
            TreeLver[] entitys = TreeLver.values();
            for (TreeLver entity : entitys) {
                if ((key == entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }
}
