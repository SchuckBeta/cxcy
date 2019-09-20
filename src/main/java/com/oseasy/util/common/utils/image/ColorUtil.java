/**
 * .
 */

package com.oseasy.util.common.utils.image;

import java.awt.Color;

import org.apache.log4j.Logger;

import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.reg.RegexUtil;

/**
 * <strong>颜色工具类</strong><br>
 * <ul>
 * <li>颜色进制转换</li>
 * <li>颜色合法性校验</li>
 * </ul>
 * <br>
 */
public class ColorUtil {
    /**
     * .
     */
    private static final String RGB = "RGB";
    private static Logger logger = Logger.getLogger(ColorUtil.class.getName());
    private static String msg = "";

    private static String regHex = "^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$";
    private static String regRgb = "^(RGB\\(|rgb\\()([0-9]{1,3},){2}[0-9]{1,3}\\)$";
    private static String regRepRgb = "(rgb|\\(|\\)|RGB)*";

    public ColorUtil() {
    }

    /**
     * <strong>颜色十六进制转颜色RGB</strong><br>
     * <ul>
     * <li>颜色十六进制参数不合法时，返回null</li>
     * </ul>
     * <br>
     *
     * @param hex 颜色十六进制
     * @return 颜色RGB
     */
    public static String hex2Rgb(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtil.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";
            logger.error(msg);
            return null;
        }

        String c = StringUtil.replace(hex.toUpperCase(), StringUtil.JIN, StringUtil.EMPTY);
        String r = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16) + "";
        String g = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16) + "";
        String b = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16) + "";

        sb.append(RGB+StringUtil.KUOHL + r + StringUtil.DOTH + g + StringUtil.DOTH + b + StringUtil.KUOHR);

        return sb.toString();
    }
    public static Color hex2Color(String hex) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtil.isHex(hex)) {
            msg = "颜色十六进制格式 【" + hex + "】 不合法，请确认！";
            logger.error(msg);
            return null;
        }

        String c = StringUtil.replace(hex.toUpperCase(), StringUtil.JIN, StringUtil.EMPTY);
        Integer r = Integer.parseInt((c.length() == 3 ? c.substring(0, 1) + c.substring(0, 1) : c.substring(0, 2)), 16);
        Integer g = Integer.parseInt((c.length() == 3 ? c.substring(1, 2) + c.substring(1, 2) : c.substring(2, 4)), 16);
        Integer b = Integer.parseInt((c.length() == 3 ? c.substring(2, 3) + c.substring(2, 3) : c.substring(4, 6)), 16);
        return new Color(r, g, b);
    }

    /**
     * <strong>颜色RGB转十六进制</strong><br>
     * <ul>
     * <li>颜色RGB不合法，则返回null</li>
     * </ul>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 合法时返回颜色十六进制
     */
    public static String rgb2Hex(String rgb) {
        StringBuilder sb = new StringBuilder();

        if (!ColorUtil.isRgb(rgb)) {
            msg = "颜色 RGB 格式【" + rgb + "】 不合法，请确认！";
            logger.error(msg);
            return null;
        }

        String r = Integer.toHexString(ColorUtil.getRed(rgb)).toUpperCase();
        String g = Integer.toHexString(ColorUtil.getGreen(rgb)).toUpperCase();
        String b = Integer.toHexString(ColorUtil.getBlue(rgb)).toUpperCase();

        sb.append(StringUtil.JIN);
        sb.append(r.length() == 1 ? "0" + r : r);
        sb.append(g.length() == 1 ? "0" + g : g);
        sb.append(b.length() == 1 ? "0" + b : b);

        return sb.toString();
    }

    /**
     * <strong>获取颜色RGB红色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 红色值
     */
    public static int getRed(String rgb){
        return Integer.valueOf(ColorUtil.getRGB(rgb)[0]);
    }

    /**
     * <strong>获取颜色RGB绿色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 绿色值
     */
    public static int getGreen(String rgb){
        return Integer.valueOf(ColorUtil.getRGB(rgb)[1]);
    }

    /**
     * <strong>获取颜色RGB蓝色值</strong><br>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 蓝色数值
     */
    public static int getBlue(String rgb){
        return Integer.valueOf(ColorUtil.getRGB(rgb)[2]);
    }

    /**
     * <strong>获取颜色RGB数组</strong><br>
     * <br>
     * @author Aaron.ffp
     *
     * @param rgb 颜色RGB
     * @return 颜色数组[红，绿，蓝]
     */
    public static String[] getRGB(String rgb){
        rgb = StringUtil.replace(rgb, StringUtil.KGE, StringUtil.EMPTY);
        rgb = StringUtil.replace(rgb, RGB+StringUtil.KUOHL, StringUtil.EMPTY);
        rgb = StringUtil.replace(rgb, StringUtil.KUOHR, StringUtil.EMPTY);
        return rgb.split(",");
    }

    /**
     * <strong>验证颜色十六进制是否合法</strong><br>
     * <ul>
     * <li>规则：#号开头，三位或六位字母数字组成的字符串</li>
     * </ul>
     * <br>
     * @author Aaron.ffp
     *
     * @param hex 十六进制颜色
     * @return 合法则返回true
     */
    public static boolean isHex(String hex) {
        return RegexUtil.isMatch(regHex, hex);
    }

    /**
     * <strong>验证颜色RGB是否合法</strong><br>
     * <ul>
     * <li>1、RGB符合正则表达式</li>
     * <li>2、颜色值在0-255之间</li>
     * </ul>
     * <br>
     * @param rgb 颜色RGB
     * @return 是否合法，合法返回true
     */
    public static boolean isRgb(String rgb) {
        boolean r = ColorUtil.getRed(rgb) >= 0 && ColorUtil.getRed(rgb) <= 255;
        boolean g = ColorUtil.getGreen(rgb) >= 0 && ColorUtil.getGreen(rgb) <= 255;
        boolean b = ColorUtil.getBlue(rgb) >= 0 && ColorUtil.getBlue(rgb) <= 255;

        return ColorUtil.isRgbFormat(rgb) && r && g && b;
    }

    /**
     * <strong>验证颜色RGB是否匹配正则表达式</strong><br>
     * <ul>
     * <li>匹配则返回true</li>
     * </ul>
     * <br>
     *
     * @param rgb 颜色RGB
     * @return 是否匹配
     */
    public static boolean isRgbFormat(String rgb) {
        return RegexUtil.isMatch(regRgb, rgb);
    }

    /*********************************************************************
     *测试
     *********************************************************************/
    public static void main(String[] args) {
        beforeClass();
    }

    public static void beforeClass() {
        String[][] rgb = new String[8][2];
        String[][] hex = new String[11][2];
        rgb[0][0] = "RGB(0,0,0)";
        rgb[1][0] = "RGB(0,128,255)";
        rgb[2][0] = "RGB(128,0,75)";
        rgb[3][0] = "RGB(128,0,0)";
        rgb[4][0] = "rgb(128,23,200)";
        rgb[5][0] = "rgb(128, 128, 128)";
        rgb[6][0] = "rgb(255, 255, 255)";
        rgb[7][0] = "rgb(128, 128, 300)";

        rgb[0][1] = "#000000";
        rgb[1][1] = "#0080FF";
        rgb[2][1] = "#80004B";
        rgb[3][1] = "#800000";
        rgb[4][1] = "#8017C8";
        rgb[5][1] = "#808080";
        rgb[6][1] = "#FFFFFF";
        rgb[7][1] = null;

        hex[0][0] = "#000000";
        hex[1][0] = "#000";
        hex[2][0] = "#FAFAFA";
        hex[3][0] = "#000080";
        hex[4][0] = "#008080";
        hex[5][0] = "#F0FF80";
        hex[6][0] = "#EA00FA";
        hex[7][0] = "#00EAEA";
        hex[8][0] = "#876";
        hex[9][0] = "#000xEA";
        hex[10][0] = "#ghi";

        hex[0][1] = "RGB(0,0,0)";
        hex[1][1] = "RGB(0,0,0)";
        hex[2][1] = "RGB(250,250,250)";
        hex[3][1] = "RGB(0,0,128)";
        hex[4][1] = "RGB(0,128,128)";
        hex[5][1] = "RGB(240,255,128)";
        hex[6][1] = "RGB(234,0,250)";
        hex[7][1] = "RGB(136,119,102)";
        hex[8][1] = "RGB(0,0,0)";
//        hex[9][1] = null;
//        hex[10][1] = null;
//        getBlue(rgb);
//        hex2Rgb(hex);
        hex2Color(hex);
//        rgb2Hex(rgb);
    }

    public static void getBlue(String[][] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            System.out.println("获取蓝色：\t" + rgb[i][0] + "\t" + ColorUtil.getBlue(rgb[i][0]));
        }
    }

    public void getGreen(String[][] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            System.out.println("获取绿色：\t" + rgb[i][0] + "\t" + ColorUtil.getGreen(rgb[i][0]));
        }
    }

    public void getRed(String[][] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            System.out.println("获取红色：\t" + rgb[i][0] + "\t" + ColorUtil.getRed(rgb[i][0]));
        }
    }

    public static void hex2Rgb(String[][] hex) {
        for (int i = 0; i < hex.length; i++) {
            System.out.println("十六进制转 RGB：\t" + hex[i][0] + "\t期望结果：" + hex[i][1] + "\t实际结果：" + ColorUtil.hex2Rgb(hex[i][0]));
        }
    }
    public static void hex2Color(String[][] hex) {
        for (int i = 0; i < hex.length; i++) {
            System.out.println("十六进制转 RGB：\t" + hex[i][0] + "\t期望结果：" + hex[i][1] + "\t实际结果：" + ColorUtil.hex2Color(hex[i][0]).toString());
        }
    }

    public void isHex(String[][] hex) {
        for (int i = 0; i < hex.length; i++) {
            System.out.println(hex[i][0] + "\t\t" + ColorUtil.isHex(hex[i][0]));
        }
    }

    public void isRgb(String[][] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            System.out.println(rgb[i][0] + "\t\t" + ColorUtil.isRgb(rgb[i][0]));
        }
    }

    public static void rgb2Hex(String[][] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            System.out.println("RGB 转十六进制：\t" + rgb[i][0] + "\t期望结果：" + rgb[i][1] + "\t实际结果：" + ColorUtil.rgb2Hex(rgb[i][0]));
        }
    }

}