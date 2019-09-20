/**
 * .
 */

package com.oseasy.util.common.utils.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.io.File;
import java.text.AttributedString;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.oseasy.util.common.exception.UtilRunException;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
public class ImageUtil {
    public final static Logger logger = Logger.getLogger(ImageUtil.class);
    /** 默认透明度*/
    public static final float ALPHA = 1.0f;
    public static final Color FONT_COLOR = Color.BLACK;
    public static final Integer FONT_SIZE = 14;
    public static final Integer FONT_STYLE = Font.PLAIN;
    public static final String FONT_NAME = "宋体";
    /** 图片格式：jpg */
    public static final String PICTRUE_FORMATE_JPG = "jpg";
    /** 默认旋转角度*/
    public static final float ROTERATE = 0.0f;
    public static final String bold = "bold";
    public static final String underline = "underline";
    /*前台字体样式值*/
    public static final String italic = "italic";

    /**
     * @param g Graphics2D
     * @param width 底图宽度
     * @param height 底图高度
     * @param img 被加入的图片路径
     * @param x 被加入的图片x
     * @param y 被加入的图片y
     * @param alpha 透明
     * @param roteRate 旋转
     */
    public final static void pressImage(Graphics2D g,int width,int height,String img,int x, int y, Float alpha, Float roteRate) throws Exception {
        if (roteRate == null) {
            roteRate = ImageUtil.ROTERATE;
        }
        if (alpha == null) {
        	alpha = ImageUtil.ALPHA;
        }
        Image wrImage=null;;
    	try {
    		wrImage = ImageIO.read(new File(img));
    	} catch (Exception e) {
    		throw new UtilRunException("保存失败，存在默认图片，请双击上传图片");
    	}
        if(wrImage==null){
        	throw new UtilRunException("保存失败，存在默认图片，请双击上传图片");
        }
        int wrW = wrImage.getWidth(null);
        int wrH = wrImage.getHeight(null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        g.rotate(Math.toRadians(roteRate));

        int widthDiff = width - wrW;
        int heightDiff = height - wrH;
        if (x <= 0) {
            x = widthDiff / 2;
        } else if (x > widthDiff) {
            x = widthDiff;
        }
        if (y <= 0) {
            y = heightDiff / 2;
        } else if (y > heightDiff) {
            y = heightDiff;
        }

        int wpX = 0, wpY = 0;
        wpX = x;
        wpY = y;
        g.drawImage(wrImage, wpX, wpY, wrW, wrH, null);
    }

    public static void pressText(Graphics2D g,int width,int height, String pressText, String fontName, Integer fontStyle, Integer fontSize, Color color, int x, int y, Float roteRate, Float alpha,boolean underline) {
            if(StringUtil.isEmpty(pressText)){
            	return;
            }
        	if (roteRate == null) {
                roteRate = ImageUtil.ROTERATE;
            }
            if (alpha == null) {
            	alpha = ImageUtil.ALPHA;
            }
            if (fontName == null) {
            	fontName = ImageUtil.FONT_NAME;
            }
            if (fontStyle == null) {
            	fontStyle = ImageUtil.FONT_STYLE;
            }
            if (fontSize == null) {
            	fontSize = ImageUtil.FONT_SIZE;
            }
            if (color == null) {
            	color = ImageUtil.FONT_COLOR;
            }
            Font ft=new Font(fontName, fontStyle, fontSize);
            g.setFont(ft);
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.rotate(Math.toRadians(roteRate));

            int ptW = fontSize * ImageUtil.getLength(pressText);
            int ptH = fontSize;
            int widthDiff = width - ptW;
            int heightDiff = height - ptH;
    //        if (x <= 0) {
    //            x = widthDiff / 2;
    //        } else if (x > widthDiff) {
    //            x = widthDiff;
    //        }
    //        if (y <= 0) {
    //            y = heightDiff / 2;
    //        } else if (y > heightDiff) {
    //            y = heightDiff;
    //        }

            int wpX = 0, wpY = 0;
            wpX = x;
            wpY = y;
            AttributedString as = new AttributedString(pressText);
            as.addAttribute(TextAttribute.FONT, ft);
            if(underline){
            	as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            }
            g.drawString(as.getIterator(), x, y + fontSize * 85 / 100);
        }

    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    public static Color getColorByStyle(String c){
    	try {
    		String color="";
    		if(StringUtil.isEmpty(c)){
    			return FONT_COLOR;
    		}
    		Pattern p = Pattern.compile("(?<=\\()(.+?)(?=\\))");
    		Matcher m = p.matcher(c);
    		if(m.find()){
    			color=m.group();
    		}
    		if(StringUtil.isEmpty(color)){
    			return FONT_COLOR;
    		}
    		String[] cs=color.split(",");
    		return new Color(Integer.parseInt(cs[0]),Integer.parseInt(cs[1]),Integer.parseInt(cs[2]));
    	} catch (Exception e) {
    		logger.error("证书字体颜色转换失败:"+c);
    		return FONT_COLOR;
    	}
    }

    public static Color getColorByHex(String c){
        try {
            Color color = ColorUtil.hex2Color(c);
            if(color == null){
                return FONT_COLOR;
            }
            return color;
        } catch (Exception e) {
            logger.error("证书字体颜色转换失败:"+c);
            return FONT_COLOR;
        }
    }

}
