package com.oseasy.util.common.utils.image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
/**
 * 图片压缩工具类
 *
 * @author wuyu
 *
 */
public class ImageBufferUtil	{
//将图按比例缩小。
	public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		//这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
		//则将下面的if else语句注释即可
		if (sx>sy)
		{
			sx = sy;
			targetW = (int)(sx * source.getWidth());
		}else{
			sy = sx;
			targetH = (int)(sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) {//handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else  {
			target = new BufferedImage(targetW, targetH, type);
			Graphics2D g = target.createGraphics();
		//smoother than exlax:
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
		}
		return target;
	}

	public static void saveImageAsJpg(String fromFileStr,String saveToFileStr,int width,int hight)
			throws Exception {
		BufferedImage srcImage;
		 String ex = fromFileStr.substring(fromFileStr.indexOf("."),fromFileStr.length());
		String imgType = "JPEG";
		if (fromFileStr.toLowerCase().endsWith(".png")) {
			imgType = "PNG";
		}
		imgType=ex;
		// System.out.println(ex);
		File saveFile=new File(saveToFileStr);
		File fromFile=new File(fromFileStr);
		srcImage = ImageIO.read(fromFile);
		if (width > 0 || hight > 0)         {
			srcImage = resize(srcImage, width, hight);
		}
		ImageIO.write(srcImage, imgType, saveFile);
	}

	public static void main (String argv[]) {
		try{
			//参数1(from),参数2(to),参数3(宽),参数4(高)
			saveImageAsJpg("C:/Users/1/Desktop/icon_040.gif",
					"C:/Users/1/Desktop/icon_040__.gif",120,120);
			String url = "C:/Users/1/Desktop/icon_040.gif";
			String ol = "C:/Users/1/Desktop/icon_040__.gif";

			File file = new File(url);

//			FileInputStream is = new FileInputStream(file);
			BufferedImage bi = ImageIO.read(file);
				bi = resize(bi, 1200, 1200);
			ImageIO.write(bi, "gif", new File(ol));
//
//			BufferedOutputStream bos = new BufferedOutputStream(out)



//			ByteArrayOutputStream bs =new ByteArrayOutputStream();
//			ImageOutputStream imOut =ImageIO.createImageOutputStream(bs);
//			ImageIO.write(scaledImage1,"jpg",imOut); //scaledImage1为BufferedImage，jpg为图像的类型
//			InputStream is =new ByteArrayInputStream(bs.toByteArray());

//		   JPEGImageDecoder decoderFile = JPEGCodec.createJPEGDecoder(in);
//		   BufferedImage image = decoderFile.decodeAsBufferedImage();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}