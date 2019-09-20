package com.oseasy.util.common.utils.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片工具类， 图片水印，文字水印，缩放，补白等
 *
 * @author ChenHao
 */
public final class ImagePutils {
    /** 默认水印旋转角度*/
    private static final float ROTE_RATE_DEFAULT = 30.0f;
    /** 图片格式：JPG */
    private static final String PICTRUE_FORMATE_JPG = "jpg";

    private ImagePutils() {
    }

    /**
     * 添加图片水印.
     * @param targetImg
     *            目标图片路径，如：C://myPictrue//1.jpg
     * @param outImg
     *            输出图片路径，如：C://myPictrue//out1.jpg
     * @param waterImg
     *            水印图片路径，如：C://myPictrue//logo.png
     * @param x
     *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y
     *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param roteRate 水印旋转角度
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param hasLoop 是否循环水印
     */
    public final static void pressImage(String targetImg, String waterImg,
                                        String outImg, int x, int y, float alpha, Float roteRate, Boolean hasLoop) {
        if (roteRate == null) {
            roteRate = ROTE_RATE_DEFAULT;
        }
        try {
            File file = new File(targetImg);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
            g.drawImage(image, 0, 0, width, height, null);

            Image wrImage = ImageIO.read(new File(waterImg)); // 水印文件
            int wrW = wrImage.getWidth(null);
            int wrH = wrImage.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.rotate(Math.toRadians(roteRate), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

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

            float wrate = 1.5f;
            int wpX = 0, wpY = 0;
            if (hasLoop && ((x > 0) && (y > 0))) {
                wpX = -width / 2;
                while (wpX < (width * wrate)) {
                    wpY = -height / 2;
                    while (wpY < (height * wrate)) {
                        g.drawImage(wrImage, wpX, wpY, wrW, wrH, null); // 水印文件结束
                        wpY += y;
                    }
                    wpX += x;
                }
            } else {
                wpX = x;
                wpY = y;
                g.drawImage(wrImage, wpX, wpY, wrW, wrH, null); // 水印文件结束
            }
            g.dispose();
//            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, new File(outImg));
          //图片质量
            ImageWriter writer = null;
            ImageTypeSpecifier type =ImageTypeSpecifier.createFromRenderedImage(bufferedImage);
            Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, "jpg");
            if (iter.hasNext()) {
            	writer = iter.next();
            }
            if (writer == null) {
            	return;
            } 
            
            IIOImage iioImage = new IIOImage(bufferedImage, null, null);
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1f);
            ImageOutputStream outputStream = ImageIO.createImageOutputStream(new File(outImg)); 
            writer.setOutput(outputStream);
            writer.write(null, iioImage, param); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void pressImage(String targetImg, String waterImg, int x, int y, float alpha, Float roteRate) {
        pressImage(targetImg, waterImg, targetImg, x, y, alpha, roteRate, true);
    }

    public final static void pressImage(String targetImg, String waterImg,
                                        int x, int y, float alpha, Float roteRate, boolean hasLoop) {
        pressImage(targetImg, waterImg, targetImg, x, y, alpha, roteRate, hasLoop);
    }

    public final static void pressImage(String targetImg, String waterImg,
                                        String outImg, int x, int y, Float roteRate, float alpha) {
        pressImage(targetImg, waterImg, outImg, x, y, alpha, roteRate, false);
    }

    /**
     * 添加文字水印.
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param outImg 输出图片路径，如：C://myPictrue//out1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param fontName 字体名称， 如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize 字体大小，单位为像素
     * @param color 字体颜色
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param roteRate 水印旋转角度
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param hasLoop 是否循环水印
     */
    public static void pressText(String targetImg, String outImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, Float roteRate, float alpha, boolean hasLoop) {
        if (roteRate == null) {
            roteRate = ROTE_RATE_DEFAULT;
        }
        try {
            File file = new File(targetImg);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  

//            g.drawImage(image, 0, 0, width, height, null);
            g.drawImage(image.getScaledInstance(width,height, Image.SCALE_SMOOTH),0, 0, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.rotate(Math.toRadians(roteRate), bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);

            int ptW = fontSize * getLength(pressText);
            int ptH = fontSize;
            int widthDiff = width - ptW;
            int heightDiff = height - ptH;
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

            float wrate = 1.5f;
            int wpX = 0, wpY = 0;
            if (hasLoop && ((x > 0) && (y > 0))) {
                wpX = - width / 2;
                while (wpX < (width * wrate)) {
                    wpY = - height / 2;
                    while (wpY < (height * wrate)) {
                        g.drawString(pressText, wpX, wpY + ptH);//水印文件结束
                        wpY += y;
                    }
                    wpX += x;
                }
            } else {
                wpX = x;
                wpY = y;
                g.drawString(pressText, wpX, wpY + ptH);//水印文件结束
            }

            g.dispose();
//            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, new File(outImg));
            //图片质量
            ImageWriter writer = null;
            ImageTypeSpecifier type =ImageTypeSpecifier.createFromRenderedImage(bufferedImage);
            Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, "jpg");
            if (iter.hasNext()) {
            	writer = iter.next();
            }
            if (writer == null) {
            	return;
            } 
            
            IIOImage iioImage = new IIOImage(bufferedImage, null, null);
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1f);
            ImageOutputStream outputStream = ImageIO.createImageOutputStream(new File(outImg)); 
            writer.setOutput(outputStream);
            writer.write(null, iioImage, param); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, Float roteRate, float alpha) {
        pressText(targetImg, pressText, fontName, fontStyle, fontSize, color, x, y, roteRate, alpha, true);
    }
    public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, Float roteRate, float alpha, boolean hasLoop) {
        pressText(targetImg, targetImg, pressText, fontName, fontStyle, fontSize, color, x, y, roteRate, alpha, hasLoop);
    }
    public static void pressText(String targetImg, String outImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, Float roteRate, float alpha) {
        pressText(targetImg, outImg, pressText, fontName, fontStyle, fontSize, color, x, y, roteRate, alpha, false);
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

    /**
     * 图片缩放.
     * @param filePath
     *            图片路径
     * @param outPath
     *            输出图片路径，如：C://myPictrue//out1.jpg
     * @param height
     *            高度
     * @param width
     *            宽度
     * @param bb
     *            比例不对时是否需要补白
     */
    public static void resize(String filePath, String outPath, int height, int width, boolean bb) {
        try {
            double ratio = 0; // 缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height,
                    BufferedImage.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }else{
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, PICTRUE_FORMATE_JPG, new File(outPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //
        pressImage("d://Uwork//image//srcimg.jpg", "d://Uwork//image//ipree.png", "d://Uwork//image//srcimgXX1.jpg", 200, 250, null, 0.5f);
//        pressImage("C://Uwork//image//srcimg.png", "C://Uwork//image//ipree.png", "C://Uwork//image//srcimgXX2.png", 100, 90, null, 0.5f);
//
//        pressText("C://Uwork//image//srcimg.png", "C://Uwork//image//srcimgTT1.png", "测试水印", "宋体", Font.BOLD|Font.ITALIC, 20, Color.WHITE, 150, 80, 60.0f, 0.8f);
//
//        pressText("d://Uwork//image//srcimg.jpg", "d://Uwork//image//srcimgTT2.jpg", "测试水印2", "宋体", Font.BOLD|Font.ITALIC, 20, Color.BLACK, 1, 1, 0f, 0.8f);
//
//        resize("C://Uwork//image//srcimg.png", "C://Uwork//image//srcimgRRR.png", 300, 200, true);
    }
}