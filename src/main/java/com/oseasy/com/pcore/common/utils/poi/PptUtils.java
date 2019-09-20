package com.oseasy.com.pcore.common.utils.poi;

/**
 *
 */
import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * @author u1
 *
 */
public class PptUtils {

  //创建pptx
  public static void writeDataPptx(String filePath, String titlePptx,
      String imagePath) {
    // TODO Auto-generated method stub
    OutputStream ostream=null;
    try {
      ostream = new FileOutputStream(filePath);
      XMLSlideShow ppt=new XMLSlideShow();
      //ppt.setPageSize(new Dimension(500,400));
      //创建第一块ppt 放置图片
      XSLFSlide slide=ppt.createSlide();

      XSLFGroupShape shape=slide.createGroup();

      //添加图片
      byte[] picData=IOUtils.toByteArray(new FileInputStream(imagePath));
      shape.createPicture(ppt.addPicture(picData, PictureData.PictureType.JPEG));

      //第二张ppt 放置文本
      //创建文本框
      slide=ppt.createSlide();
      XSLFTextShape textShapeOnly=slide.createTextBox();
      textShapeOnly.setAnchor(new Rectangle(100, 100, 302, 302));
      textShapeOnly.setPlaceholder(Placeholder.TITLE);
      textShapeOnly.setText(titlePptx);

      //第三张ppt  放置标题和文本
      XSLFSlideMaster master=ppt.getSlideMasters().get(0);
      XSLFSlideLayout layout=master.getLayout(SlideLayout.TITLE_AND_CONTENT);
      slide=ppt.createSlide(layout);
      XSLFTextShape[] textShape=slide.getPlaceholders();
      XSLFTextShape textShape2=textShape[0];
      textShape2.setText(titlePptx);
      textShape2=textShape[1];
      //清除掉母版文本
      textShape2.clearText();
      XSLFTextParagraph paragraph=textShape2.addNewTextParagraph();
      XSLFTextRun run=paragraph.addNewTextRun();
      run.setText("华尔街是纽约市曼哈顿区南部从百老汇路延伸到东河的一条大街道的名字");
      run.setFontColor(Color.RED);
      run.setFontSize(20d);
      paragraph=textShape2.addNewTextParagraph();
      run=paragraph.addNewTextRun();
      run.setText("“华尔街”一词现已超越这条街道本身，成为附近区域的代称，亦可指对整个美国经济具有影响力的金融市场和金融机构。");
      run.setFontColor(Color.RED);
      run.setFontSize(20d);

      ppt.write(ostream);

      System.out.println("创建pptx成功");
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      if (ostream!=null)
        try {
          ostream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }


  //读取pptx的内容
    public static String readDataPptx(String filePath) {
      String content="";
      InputStream istream=null;
      try {
        istream = new FileInputStream(filePath);
        XMLSlideShow ppt=new XMLSlideShow(istream);
          for(XSLFSlide slide:ppt.getSlides()) { //遍历每一页ppt
            //content+=slide.getTitle()+"\t";
            for(XSLFShape shape:slide.getShapes()) {
              if (shape instanceof XSLFTextShape) { //获取到ppt的文本信息
                for(Iterator iterator=((XSLFTextShape) shape).iterator();iterator.hasNext();) {
                //获取到每一段的文本信息
                  XSLFTextParagraph paragraph=(XSLFTextParagraph) iterator.next();
                  for (XSLFTextRun xslfTextRun : paragraph) {
                  content+=xslfTextRun.getRawText()+"\t";
                }
                }
              }
            }
            //获取一张ppt的内容后 换行
            content+="\n";
          }
      } catch (Exception e) {
        e.printStackTrace();
      }finally{
        if (istream!=null)
          try {
            istream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
      return content;
    }
}
