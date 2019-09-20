/**
 * .
 */

package com.oseasy.util.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

/**
 * antlr-2.7.4.jar, chardet-1.0.jar, jargs-1.0.jar, cpdetector_1.0.10.jar.
 *
 * @author chenhao
 *
 */
public class EncodingUtil {
    private static Logger logger = LoggerFactory.getLogger(EncodingUtil.class);
    /** 探测器代理 **/
    private CodepageDetectorProxy detector;

    public static void main(String[] args) throws Exception {
        String filePath ="E:/workspace-sts3/ROOT";
//        System.out.println(getFileEncode(filePath+"/导入附件GBK.zip"));
//        System.out.println(getFileEncode(filePath+"/批量下载_计划书utf8.zip"));
//        System.out.println(getFileEncode(filePath+"/新建文本文档utf-8.txt"));
//        ImpDataService.unZipFiles("E:/workspace-sts3/ROOT/导入附件GBK.zip", "E:/workspace-sts3/ROOT/999_AA");
//        ImpDataService.unZipFiles("E:/workspace-sts3/ROOT/批量下载_计划书utf8.zip", "E:/workspace-sts3/ROOT/999_BB");

//        FileUtil.unZipFiles(filePath+"/附件汇总391.zip", filePath+"/zip/999_AA");
        FileUtil.unZipFiles(filePath+"/批量下载_计划书utf8.zip", filePath+"/zip/999_BB");
        FileUtil.unZipFiles(filePath+"/导入附件GBK.zip", filePath+"/zip/999_CC");


//        String charset = EncodingUtil.checkEncoding(new File(filePath+"/A1中utf-8.rar"));
//        EncodingUtil.convertMessy(charset, "乱码字符串！");
    }

    /**
     * 构造方法， detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
     *
     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法
     *
     * 加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。
     *
     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
     *
     * 字符集编码。
     */
    public EncodingUtil() {
        detector = CodepageDetectorProxy.getInstance();
        // JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码测定。
        // 所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以再多加几个探测器，
        // 比如下面的ASCIIDetector、UnicodeDetector等。
        detector.add(JChardetFacade.getInstance());
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());

        // ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
        // 指示是否显示探测过程的详细信息，为false不显示
        // 如果不希望判断xml的encoding，而是要判断该xml文件的编码，则可以注释掉
        detector.add(new ParsingDetector(false));
    }

    /**
     * 利用第三方开源包cpdetector获取文件编码格式
     * @param path
     *            要判断文件编码格式的源文件的路径
     * @author 冯琪琪
     * @version 2012-7-12 14:05
     */
    public static String getFileEncode(String path) {
      /*
       * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
       * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
       * JChardetFacade、ASCIIDetector、UnicodeDetector。
       * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
       * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
       * cpDetector是基于统计学原理的，不保证完全正确。
       */
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
      /*
       * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
       * 指示是否显示探测过程的详细信息，为false不显示。
       */
        detector.add(new ParsingDetector(false));
      /*
       * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
       * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
       * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
       */
        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
        java.nio.charset.Charset charset = null;
        File f = new File(path);
        try {
            charset = detector.detectCodepage(f.toURI().toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (charset != null)
            return charset.name();
        else
            return null;
    }



    /**
     * 检查文件的编码格式.
     */
    public static String checkEncoding(File file) {
        String charset;
        try {
            charset = StringUtil.upperCase(new EncodingUtil().detector(file).name());
            logger.info("当前文件检查编码集为：["+charset+"]");
            if((StringUtil.contains(charset, "UTF"))){
                return FileUtil.UTF_8;
            }else if((StringUtil.contains(charset, "GBK"))){
                return FileUtil.GBK;
            }else{
                return charset;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 探测.
     *
     * @param file
     * @return charset
     * @throws MalformedURLException
     * @throws IOException
     */
    public Charset detector(File file) throws MalformedURLException, IOException {
        return detector.detectCodepage(file.toURI().toURL());
    }

    /**
     * 探测.
     * @param url
     * @return charset
     * @throws IOException
     */
    public Charset detector(URL url) throws IOException {
        return detector.detectCodepage(url);
    }

    /**
     * 探测.
     * @param inputStream
     * @param length
     *            长度应小于流的可用长度
     * @return charset
     * @throws IOException
     */
    public Charset detector(InputStream inputStream, int length) throws IOException {
        return detector.detectCodepage(inputStream, length);
    }

    /**
     * 判断是否有中文编码.
     * @param
     * @return
     */
    public static boolean checkChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
//            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
//                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
//                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
//                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
//                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
//                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
//                    || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检查当前字符是否为乱码.
     * @param strName
     * @return
     */
    public static boolean checkMessy(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!checkChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 检测转换字符串编码，默认设置为Utf-8.
     * @param charset 元素字符集
     * @param srcStr 原始数据
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String convertMessy(String charset,String srcStr) throws UnsupportedEncodingException {
        String tarStr = srcStr;
        if(checkMessy(tarStr)){
            tarStr =  new String((srcStr).getBytes(charset), StringUtil.GBK);

            if(checkMessy(tarStr)){
                tarStr =  new String((srcStr).getBytes(charset), StringUtil.UTF_8);
            }

            if(checkMessy(tarStr)){
                tarStr =  new String((srcStr).getBytes(charset), charset);
            }
        }else{
            tarStr =  new String((srcStr).getBytes(charset), StringUtil.UTF_8);

            if(checkMessy(tarStr)){
                tarStr =  new String((srcStr).getBytes(charset), StringUtil.GBK);
            }

            if(checkMessy(tarStr)){
                tarStr =  new String((srcStr).getBytes(charset), charset);
            }
        }
        return tarStr;
    }
}
