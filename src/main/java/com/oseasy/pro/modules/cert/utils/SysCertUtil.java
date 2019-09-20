package com.oseasy.pro.modules.cert.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.pro.modules.cert.entity.CertElement;
import com.oseasy.pro.modules.cert.entity.CertPage;
import com.oseasy.pro.modules.cert.enums.ElementTypeEnum;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.image.ImageUtil;

public final class SysCertUtil {
	public final static Logger logger = Logger.getLogger(SysCertUtil.class);

    private static int getFontStyle(CertElement ce){
    	int fs=ImageUtil.FONT_STYLE;
    	if(ImageUtil.bold.equals(ce.getFontWeight())){
    		fs=fs+Font.BOLD;
    	}
    	if(ImageUtil.italic.equals(ce.getFontStyle())){
    		fs=fs+Font.ITALIC;
    	}
    	return fs;
    }
    private static boolean getUnderline(CertElement ce){
    	if(ImageUtil.underline.equals(ce.getTextDecoration())){
    		return true;
    	}else{
    		return false;
    	}
    }
  //保存证书模板页时生成一下发使用的图片
    public static void createCertPic(CertPage cp,String tempPath,String outImg) throws Exception{
    	if(cp==null||cp.getList()==null||cp.getList().size()==0){
    		return;
    	}
		File tempPathDir = new File(tempPath + File.separator);
		if (!tempPathDir.exists()) {
			tempPathDir.mkdirs();
		}
    	ImageOutputStream outputStream=null;
    	BufferedImage bufferedImage = new BufferedImage(cp.getWidth(), cp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,cp.getWidth(), cp.getHeight());

        Collections.sort(cp.getList());
        for(CertElement ce:cp.getList()){
        	if(!ElementTypeEnum.VAR.getValue().equals(ce.getElementType())){//不处理变量
        		if(StringUtil.isNotEmpty(ce.getUrl())){//图片
        			String filepath=FtpUtil.getFtpPath(ce.getUrl());
        			String realName = filepath.substring(filepath.lastIndexOf("/") + 1);
					String path = filepath.substring(0, filepath.lastIndexOf("/") + 1);
        			VsftpUtils.downFile(path, realName, tempPath);
        			ImageUtil.pressImage(g, cp.getWidth(), cp.getHeight(), tempPath+File.separator+realName, ce.getX(), ce.getY(), ce.getFillOpacity(), null);
        		}else{//文字
        			ImageUtil.pressText(g, cp.getWidth(), cp.getHeight(), ce.getText(),ce.getFontFamily(), getFontStyle(ce), ce.getFontSize(), ImageUtil.getColorByHex(ce.getColor()), ce.getX(), ce.getY(), null,ce.getFillOpacity(),getUnderline(ce));
        		}
        	}
        }

        g.dispose();
        //输出图片
		ImageWriter writer = null;
		ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(bufferedImage);
		Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, ImageUtil.PICTRUE_FORMATE_JPG);
		writer = iter.next();

		IIOImage iioImage = new IIOImage(bufferedImage, null, null);
		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1f);
		try {
			outputStream = ImageIO.createImageOutputStream(new File(outImg));
			writer.setOutput(outputStream);
			writer.write(null, iioImage, param);
		} finally{
			if(outputStream!=null){
				outputStream.close();
			}
		}
    }
    //保存证书模板页时生成一张浏览图片
    public static void createCertViewPic(Map<String,String> colMap,CertPage cp,String tempPath,String outImg) throws Exception{
    	if(cp==null||cp.getList()==null||cp.getList().size()==0){
    		return;
    	}
		File tempPathDir = new File(tempPath + File.separator);
		if (!tempPathDir.exists()) {
			tempPathDir.mkdirs();
		}
    	ImageOutputStream outputStream=null;
    	BufferedImage bufferedImage = new BufferedImage(cp.getWidth(), cp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,cp.getWidth(), cp.getHeight());

        Collections.sort(cp.getList());
        for(CertElement ce:cp.getList()){
        	if(!ElementTypeEnum.VAR.getValue().equals(ce.getElementType())){//不处理变量
        		if(StringUtil.isNotEmpty(ce.getUrl())){//图片
        			String filepath=FtpUtil.getFtpPath(ce.getUrl());
        			String realName = filepath.substring(filepath.lastIndexOf("/") + 1);
					String path = filepath.substring(0, filepath.lastIndexOf("/") + 1);
        			VsftpUtils.downFile(path, realName, tempPath);
        			ImageUtil.pressImage(g, cp.getWidth(), cp.getHeight(), tempPath+File.separator+realName, ce.getX(), ce.getY(), ce.getFillOpacity(), null);
        		}else{//文字
        			ImageUtil.pressText(g, cp.getWidth(), cp.getHeight(), ce.getText(),ce.getFontFamily(), getFontStyle(ce), ce.getFontSize(), ImageUtil.getColorByHex(ce.getColor()), ce.getX(), ce.getY(), null,ce.getFillOpacity(),getUnderline(ce));
        		}
        	}else{
        		ImageUtil.pressText(g, cp.getWidth(), cp.getHeight(), colMap.get(ce.getVarCol()),ce.getFontFamily(), getFontStyle(ce), ce.getFontSize(), ImageUtil.getColorByStyle(ce.getColor()), ce.getX(), ce.getY(), null,ce.getFillOpacity(),getUnderline(ce));
        	}
        }

        g.dispose();
        //输出图片
		ImageWriter writer = null;
		ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(bufferedImage);
		Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, ImageUtil.PICTRUE_FORMATE_JPG);
		writer = iter.next();

		IIOImage iioImage = new IIOImage(bufferedImage, null, null);
		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1f);
		try {
			outputStream = ImageIO.createImageOutputStream(new File(outImg));
			writer.setOutput(outputStream);
			writer.write(null, iioImage, param);
		} finally{
			if(outputStream!=null){
				outputStream.close();
			}
		}
    }
  //生成证书页图片
    public static void createCertInsPic(Map<String,String> colMap,CertPage cp,String tempRootPath,String tempPath,String outImg) throws Exception{
    	if(cp==null||cp.getList()==null||cp.getList().size()==0){
    		return;
    	}
		File tempPathDir = new File(tempPath + File.separator);
		if (!tempPathDir.exists()) {
			tempPathDir.mkdirs();
		}
    	ImageOutputStream outputStream=null;
    	BufferedImage bufferedImage = new BufferedImage(cp.getWidth(), cp.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,cp.getWidth(), cp.getHeight());

        Collections.sort(cp.getList());

        //写入模板图片
        String templatePath=cp.getImgPath();
		String realName = templatePath.substring(templatePath.lastIndexOf("/") + 1);
        ImageUtil.pressImage(g, cp.getWidth(), cp.getHeight(),  tempRootPath+File.separator+realName, 0, 0, 1f, null);

        //处理变量
        for(CertElement ce:cp.getList()){
        	if(ElementTypeEnum.VAR.getValue().equals(ce.getElementType())){
    			ImageUtil.pressText(g, cp.getWidth(), cp.getHeight(), colMap.get(ce.getVarCol()),ce.getFontFamily(), getFontStyle(ce), ce.getFontSize(), ImageUtil.getColorByHex(ce.getColor()), ce.getX(), ce.getY(), null, ce.getFillOpacity(),getUnderline(ce));
        	}
        }

        g.dispose();
        //输出图片
		ImageWriter writer = null;
		ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(bufferedImage);
		Iterator<ImageWriter> iter = ImageIO.getImageWriters(type, ImageUtil.PICTRUE_FORMATE_JPG);
		writer = iter.next();

		IIOImage iioImage = new IIOImage(bufferedImage, null, null);
		ImageWriteParam param = writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(1f);
		try {
			outputStream = ImageIO.createImageOutputStream(new File(outImg));
			writer.setOutput(outputStream);
			writer.write(null, iioImage, param);
		} finally{
			if(outputStream!=null){
				outputStream.close();
			}
		}
    }
//    public static void main(String[] args) throws IOException {
        //
//        pressImage("d://Uwork//image//srcimg.jpg", "d://Uwork//image//ipree.png", "d://Uwork//image//srcimgXX1.jpg", 200, 250, null, 0.5f);
//        pressImage("C://Uwork//image//srcimg.png", "C://Uwork//image//ipree.png", "C://Uwork//image//srcimgXX2.png", 100, 90, null, 0.5f);
//
//        pressText("C://Uwork//image//srcimg.png", "C://Uwork//image//srcimgTT1.png", "测试水印", "宋体", Font.BOLD|Font.ITALIC, 20, Color.WHITE, 150, 80, 60.0f, 0.8f);
//
//        pressText("d://Uwork//image//srcimg.jpg", "d://Uwork//image//srcimgTT2.jpg", "测试水印2", "宋体", Font.BOLD|Font.ITALIC, 20, Color.BLACK, 1, 1, 0f, 0.8f);
//
//        resize("C://Uwork//image//srcimg.png", "C://Uwork//image//srcimgRRR.png", 300, 200, true);
//    }
}