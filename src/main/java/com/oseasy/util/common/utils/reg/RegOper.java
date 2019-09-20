/**
 * .
 */

package com.oseasy.util.common.utils.reg;

import java.util.List;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.reg.imp.RegGt;

/**
 * 匹配操作类型.
 * @author chenhao
 *
 */
public enum RegOper {
    DEF("0"), EQ("10"), EOR("20"), OR("30"), AND("40");

    private String key;

    public static final String REG_OPERS = "regOper";

    private RegOper(String key) {
        this.key = key;
    }

    /**
     * 根据key获取枚举 .
     *
     * @author chenhao
     * @param key
     *            枚举标识
     * @return RegOper
     */
    public static RegOper getByKey(String key) {
        if ((key != null)) {
            RegOper[] entitys = RegOper.values();
            for (RegOper entity : entitys) {
                if ((entity.getKey() != null) && (key).equals(entity.getKey())) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * 初始化 RegVo 的X\Y值.
     * @param regvo
     * @return
     */
    public static RegVo gen(RegVo regvo) {
        RegType regType = RegType.getByKey(regvo.getKey());
        if(regType == null){
            return null;
        }

        regvo.setType(regType);
        if ((RegOper.DEF).equals(regType.getOper())) {
            return RegOper.def(regvo);
        } else if ((RegOper.OR).equals(regType.getOper())) {
            return RegOper.or(regvo);
        } else if ((RegOper.AND).equals(regType.getOper())) {
            return RegOper.and(regvo);
        } else {
            return null;
        }
    }

    /**
     * Default处理参数X\Y.
     */
    public static RegVo def(RegVo regvo) {
        String exp = (regvo.getExp()).replaceAll(StringUtil.KGE, StringUtil.EMPTY);
        List<Integer> idxDHs = StringUtil.splitIdxs(RegMtype.DOTH.getKey(), exp);
        regvo.setX(exp.substring(1, idxDHs.get(0)));
        regvo.setY(exp.substring(idxDHs.get(0), idxDHs.get(0)));
        return regvo;
    }

    /**
     * EQ处理参数X\Y.
     */
    public static RegVo eq(RegVo regvo) {
        String exp = (regvo.getExp()).replaceAll(StringUtil.KGE, StringUtil.EMPTY);
        regvo.setX(exp.substring(1, exp.length() - 1));
        return regvo;
    }

    /**
     * Or处理参数X\Y.
     */
    public static RegVo eor(RegVo regvo) {
        String exp = (regvo.getExp()).replaceAll(StringUtil.KGE, StringUtil.EMPTY);
        List<Integer> idxORs = StringUtil.splitIdxs(RegMtype.OR.getKey(), exp);
        Integer pre = null;
        StringBuffer xbuffer = new StringBuffer();
        for (int i = 0; i < idxORs.size(); i++) {
            Integer iv = idxORs.get(i);
            if (i == 0) {
                pre = 1;
                xbuffer.append(exp.substring(pre, iv - 1));
                pre = (iv + 3);
            } else {
                xbuffer.append(StringUtil.DOTH);
                xbuffer.append(exp.substring(pre, iv - 1));
                pre = (iv + 3);
            }

            if (i == (idxORs.size() - 1)) {
                xbuffer.append(StringUtil.DOTH);
                xbuffer.append(exp.substring(iv + 3, exp.length() - 1));
            }
        }
        regvo.setX(xbuffer.toString());
        return regvo;
    }

    /**
     * And处理参数X\Y.
     */
    public static RegVo and(RegVo regvo) {
        String exp = (regvo.getExp()).replaceAll(StringUtil.KGE, StringUtil.EMPTY);
        List<Integer> idxDHs = StringUtil.splitIdxs(RegMtype.DOTH.getKey(), exp);
        // List<Integer> idxORs = StringUtil.splitIdxs(RegMtype.OR.getKey(),
        // exp);
        regvo.setX(exp.substring(1, idxDHs.get(0)));
        regvo.setY(exp.substring(idxDHs.get(0) + 1, exp.length() - 1));
        return regvo;
    }

    /**
     * Or处理参数X\Y.
     */
    public static RegVo or(RegVo regvo) {
        String exp = (regvo.getExp()).replaceAll(StringUtil.KGE, StringUtil.EMPTY);
        List<Integer> idxDHs = StringUtil.splitIdxs(RegMtype.DOTH.getKey(), exp);
        List<Integer> idxORs = StringUtil.splitIdxs(RegMtype.OR.getKey(), exp);
        regvo.setX(exp.substring(idxDHs.get(0) + 1, idxORs.get(0) - 1));
        regvo.setY(exp.substring(idxORs.get(0) + 3, idxDHs.get(1)));
        return regvo;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "{\"key\":\"" + this.key + "\"}";
    }

    public static void main(String[] args) {
//        String str1 = "[1]";
//        RegVo regvo1 = new RegVo("10");
//        regvo1.setExp(str1);
//        System.out.println(RegOper.eq(regvo1).toString());
//
//        String str = "(11,}";
//        RegVo regvo = new RegVo("110");
//        regvo.setExp(str);
//        System.out.println(RegOper.def(regvo).toString());
//        System.out.println(RegType.validate(RegOper.gen(regvo), 10));
//
//        String str2 = "[11,123)";
//        RegVo regvo2 = new RegVo("130");
//        regvo2.setExp(str2);
//        System.out.println(RegOper.and(regvo2).toString());

//        String str4 = "{,111]||(2222,}";
//        String str4 = "{,111)||(222,}";
//        RegVo regvo4 = new RegVo("230");
//        regvo4.setExp(str4);
//        System.out.println(RegOper.or(regvo4).toString());
//        System.out.println(RegType.validate(RegOper.gen(regvo4), 110));
//
//        String str41 = "{,111]||(2222,}";
//        RegVo regvo41 = new RegVo("240");
//        regvo41.setExp(str41);
//        System.out.println(RegOper.or(regvo41).toString());
//        System.out.println(RegType.validate(RegOper.gen(regvo41), 111));
//
//        String str3 = "[1]||[2]||[3]";
//        RegVo regvo3 = new RegVo("20");
//        regvo3.setExp(str3);
//        System.out.println(RegOper.eor(regvo3).toString());
    }
}