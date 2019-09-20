/**
 * .
 */

package com.oseasy.util.common.utils.reg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 *
 * @author chenhao
 *
 */
public class RegexUtil {
    private static final String REG_START = "^";//转义字符
    private static final String REG_END = "$";//转义字符
    public static final String REG_ZY = "\\";//转义字符

    /**
     * 匹配以XXX开头.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return
     */
    public static boolean startWith(String rule, String str) {
        Matcher m = Pattern.compile(REG_START + rule).matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 匹配以XXX开头.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return Matcher
     */
    public static Matcher startWithRt(String rule, String str) {
        Matcher m = Pattern.compile(REG_START + rule).matcher(str);
        while (m.find()) {
            return m;
        }
        return null;
    }


    /**
     * 匹配以XXX结尾.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return
     */
    public static boolean endWith(String rule, String str) {
        Matcher m = Pattern.compile(rule + REG_END).matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 匹配以XXX结尾.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return Matcher
     */
    public static Matcher endWithRt(String rule, String str) {
        Matcher m = Pattern.compile(rule + REG_END).matcher(str);
        while (m.find()) {
            return m;
        }
        return null;
    }

    /**
     * 匹配包含XXX字符.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return Matcher
     */
    public static Boolean contains(String rule, String str) {
        Matcher m = Pattern.compile(rule).matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 匹配包含XXX字符.
     * @param rule 匹配规则字符
     * @param str 匹配参数字符
     * @return Matcher
     */
    public static Matcher containsRt(String rule, String str) {
        Matcher m = Pattern.compile(rule).matcher(str);
        while (m.find()) {
            return m;
        }
        return null;
    }

    /**
     * 验证Email
     *
     * @param email
     *            email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard
     *            居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile
     *            移动、联通、电信运营商的号码段
     *            <p>
     *            移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *            、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *            </p>
     *            <p>
     *            联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *            </p>
     *            <p>
     *            电信的号段：133、153、180（未启用）、189
     *            </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone
     *            电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *            <p>
     *            <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
     *            的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
     *            </p>
     *            <p>
     *            <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *            对不使用地区或城市代码的国家（地区），则省略该组件。
     *            </p>
     *            <p>
     *            <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
     *            </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit
     *            一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals
     *            一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace
     *            空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese
     *            中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday
     *            日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url
     *            格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     *            http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域
     * </pre>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",
                Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern
        // p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)",
        // Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode
     *            邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress
     *            IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * integer (-MAX, MAX)
     */
    public final static String REGEX_INTEGER = "^[-\\+]?\\d+$"; //$NON-NLS-1$
    /**
     * integer [1, MAX)
     */
    public final static String REGEX_POSITIVE_INTEGER = "^\\+?[1-9]\\d*$"; //$NON-NLS-1$
    /**
     * integer (-MAX, -1]
     */
    public final static String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$"; //$NON-NLS-1$
    /**
     * integer [0, MAX), only numeric
     */
    public final static String REGEX_NUMERIC = "^\\d+$"; //$NON-NLS-1$
    /**
     * decimal (-MAX, MAX)
     */
    public final static String REGEX_DECIMAL = "^[-\\+]?\\d+\\.\\d+$"; //$NON-NLS-1$
    /**
     * decimal (0.0, MAX)
     */
    public final static String REGEX_POSITIVE_DECIMAL = "^\\+?([1-9]+\\.\\d+|0\\.\\d*[1-9])$"; //$NON-NLS-1$
    /**
     * decimal (-MAX, -0.0)
     */
    public final static String REGEX_NEGATIVE_DECIMAL = "^-([1-9]+\\.\\d+|0\\.\\d*[1-9])$"; //$NON-NLS-1$
    /**
     * decimal + integer (-MAX, MAX)
     */
    public final static String REGEX_REAL_NUMBER = "^[-\\+]?(\\d+|\\d+\\.\\d+)$"; //$NON-NLS-1$
    /**
     * decimal + integer [0, MAX)
     */
    public final static String REGEX_NON_NEGATIVE_REAL_NUMBER = "^\\+?(\\d+|\\d+\\.\\d+)$"; //$NON-NLS-1$

    public static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) { //$NON-NLS-1$
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 非负整数[0,MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isNumeric(String orginal) {
        return isMatch(REGEX_NUMERIC, orginal);
    }

    /**
     * 正整数[1,MAX)
     *
     * @param orginal
     * @Description: 是否为正整数
     * @return boolean
     */
    public static boolean isPositiveInteger(String orginal) {
        return isMatch(REGEX_POSITIVE_INTEGER, orginal);
    }

    /**
     * 负整数 (-MAX,-1]
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isNegativeInteger(String orginal) {
        return isMatch(REGEX_NEGATIVE_INTEGER, orginal);
    }

    /**
     * 整数 (-MAX,MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isInteger(String orginal) {
        return isMatch(REGEX_INTEGER, orginal);
    }

    /**
     * 正小数 (0.0, MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isPositiveDecimal(String orginal) {
        return isMatch(REGEX_POSITIVE_DECIMAL, orginal);
    }

    /**
     * 负小数 (-MAX, -0.0)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isNegativeDecimal(String orginal) {
        return isMatch(REGEX_NEGATIVE_DECIMAL, orginal);
    }

    /**
     * 小数 (-MAX, MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isDecimal(String orginal) {
        return isMatch(REGEX_DECIMAL, orginal);
    }

    /**
     * 实数，包括所有的整数与小数 (-MAX, MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isRealNumber(String orginal) {
        return isMatch(REGEX_REAL_NUMBER, orginal);
    }

    /**
     * 非负实数 [0, MAX)
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isNonNegativeRealNumber(String orginal) {
        return isMatch(REGEX_NON_NEGATIVE_REAL_NUMBER, orginal);
    }

    /**
     * 正实数
     *
     * @param orginal
     * @return boolean
     */
    public static boolean isPositiveRealNumber(String orginal) {
        return isPositiveDecimal(orginal) || isPositiveInteger(orginal);
    }

    /**
     * 正则表达式验证
     *
     * @param regex
     *            正则表达式
     * @param input
     *            字符串
     * @return 返回是否符合正则表达式
     */
    public static boolean regex(String regex, CharSequence input) {
        if (null == input) {
            return false;
        }
        boolean flag = true;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (!m.find()) {
            flag = false;
        }
        return flag;
    }
}