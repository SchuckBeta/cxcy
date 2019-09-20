package com.oseasy.util.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转换为拼音工具类.
 * Created by Administrator on 2019/5/9 0009.
 */
public class PinyinUtil {
    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     * @param chines 汉字
     * @return
     */
    public static String pingYin(String chines) {
        return pingYin(chines, true);
    }

    /**
     * 将字符串中的中文转化为拼音,英文字符不变
     * @param chines 汉字
     * @param firstUpper 是否首字母大写
     * @return
     */
    public static String pingYin(String chines, boolean firstUpper) {
        chines = cleanChar(chines);
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuffer output = new StringBuffer();
        if (chines != null && chines.length() > 0 && !("null".equals(chines))) {
            char[] input = chines.trim().toCharArray();
            try {
                boolean flag = true;
                for (int i = 0; i < input.length; i++) {
                    if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                        if(firstUpper){
                            output.append(temp[0].substring(0, 1).toUpperCase());
                            output.append(temp[0].substring(1));
                        }else{
                            output.append(temp[0]);
                        }
                        flag = true;
                    } else{
                        String curen = java.lang.Character.toString(input[i]);
                        if(firstUpper && flag){
                            output.append(curen.substring(0, 1).toUpperCase());
                            output.append(curen.substring(1));
                        }else{
                            output.append(curen);
                        }
                        flag = false;
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        } else {
            return "*";
        }
        return output.toString();
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变,获取拼音大写首字母
     * @param chines 汉字
     * @return 拼音
     */
    public static String firstChar(String chines) {
        return firstChar(chines, false, false);
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变,获取拼音大写首字母
     * @param chines 汉字
     * @param isupper 是否大写
     * @param enall 英文全拼
     * @return 拼音
     */
    public static String firstChar(String chines, boolean isupper, boolean enall) {
        chines = cleanChar(chines);
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        boolean flag = true;//判断是否为首字母
        for (int i = 0; i < nameChar.length; i++) {
            String cur;
            if (nameChar[i] > 128) {
                try {
                    cur = java.lang.Character.toString(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0));
                    if(isupper){
                        cur = cur.toUpperCase();
                    }else{
                        cur = cur.toLowerCase();
                    }
                    pinyinName.append(cur);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
                flag = true;
            } else {
                cur = java.lang.Character.toString(nameChar[i]);
                if(flag){
                    if(isupper){
                        cur = cur.toUpperCase();
                    }else{
                        cur = cur.toLowerCase();
                    }
                    pinyinName.append(cur);
                    flag = false;
                }else if(enall){
                    cur = java.lang.Character.toString(nameChar[i]).toLowerCase();
                    pinyinName.append(cur);
                }
            }
        }
        return pinyinName.toString();
    }

    /**
     * 清理特殊字符.
     * @param chines String
     * @return String
     */
    public static String cleanChar(String chines) {
        chines = chines.replaceAll("[\\p{Punct}\\p{Space}]+", ""); // 正则去掉所有字符操作
        // 正则表达式去掉所有中文的特殊符号
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}<>《》【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(chines);
        chines = matcher.replaceAll("").trim();
        return chines;
    }

    public static void main(String[] args) {
        String ss = "haha我爱你love中国";
        System.out.println(PinyinUtil.pingYin(ss));
        System.out.println(PinyinUtil.pingYin(ss, false));
        System.out.println(PinyinUtil.pingYin(ss, true));
        System.out.println(PinyinUtil.firstChar(ss));
        System.out.println(PinyinUtil.firstChar(ss, false, false));
        System.out.println(PinyinUtil.firstChar(ss, false, true));
        System.out.println(PinyinUtil.firstChar(ss, true, false));
        System.out.println(PinyinUtil.firstChar(ss, true, true));
    }
}