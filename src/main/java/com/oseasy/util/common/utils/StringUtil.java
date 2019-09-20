/**
 *
 */
package com.oseasy.util.common.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.reg.RegMtype;


/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类


 */
public class StringUtil extends StringUtils {

    public static final String JIN = "#";
    public static final String PREF = "#PREF";
    public static final String POST = "#POST";
    public static final String EMPTY = "";
    public static final String KGE = " ";
    public static final String MAOH = ":";
    public static final String WENH = "?";
    public static final String DOTH = ",";
	public static final String DANYH = "'";
	public static final String SHUANGYH = "\"";
    public static final String JSP_VAL_PREFIX = "${";
    public static final String JSP_VAL_POSTFIX = "}";
    public static final String KUOHDLR = "{}";
    public static final String KUOHDL = "{";
    public static final String KUOHDR = "}";
    public static final String KUOHZLR = "[]";
    public static final String KUOHZL = "[";
    public static final String KUOHZR = "]";
    public static final String KUOHL = "(";
    public static final String KUOHR = ")";
    public static final String SHUX = "|";
    public static final String OR = "||";
    public static final String AD = "&";
    public static final String AND = "&&";
    public static final char SEPARATOR = '_';
    public static final String ISO8859_1 = "ISO8859-1";
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String JIANT = "->";
    public static final String Z_DUN = "、";
    public static final String DOT = ".";
    public static final String LINE = "/";
	public static final String DL = "$";
    public static final String DDL = "$$";
    public static final String LOG = ".log";
    public static final String JSON = ".json";
    public static final String POSTFIX_JSP = ".jsp";
    public static final String POSTFIX_JSON = ".json";
    public static final String POSTFIX_SQL = ".sql";
    public static final String POSTFIX_TEXT = ".text";
    public static final String LINE_M = "-";
    public static final String LINE_D = "_";
    public static final String DENGH = "=";
    public static final String DENGH_DB = "==";
    public static final String FMT_N = "\n";
    public static final String FMT_RN = "\r\n";
    public static final String CHARSET_NAME = "UTF-8";

    public final static Map<String,String> datemap = new HashMap<String,String>();
	static {
		datemap.put("1", "一");
		datemap.put("2", "二");
		datemap.put("3", "三");
		datemap.put("4", "四");
		datemap.put("5", "五");
		datemap.put("6", "六");
		datemap.put("7", "七");
		datemap.put("8", "八");
		datemap.put("9", "九");
		datemap.put("0", "〇");
	}
	public final static Map<String,String> monthmap = new HashMap<String,String>();
	static {
		monthmap.put("1", "一");
		monthmap.put("2", "二");
		monthmap.put("3", "三");
		monthmap.put("4", "四");
		monthmap.put("5", "五");
		monthmap.put("6", "六");
		monthmap.put("7", "七");
		monthmap.put("8", "八");
		monthmap.put("9", "九");
		monthmap.put("10", "十");
		monthmap.put("11", "十一");
		monthmap.put("12", "十二");
	}
    /**
     * 根据小写数字格式的日期转换成大写格式的日期yyyy-M格式
     * @param date
     * @return
     */
    public static String getUpperYearAndMonth(String year,String month) {
    	if(StringUtil.isEmpty(year)||StringUtil.isEmpty(month)){
    		return "";
    	}
    	for(char c:year.toCharArray()){
    		String sc=String.valueOf(c);
    		String rep=datemap.get(sc);
    		if(rep!=null){
    			year=year.replaceAll(sc, rep);
    		}
    	}
    	String rep=monthmap.get(month);
		if(rep!=null){
			month=rep;
		}
    	return year+"年"+month+"月";
    }
    /**
     * 根据小写数字格式的日期转换成大写格式的日期yyyy格式
     * @param date
     * @return
     */
    public static String getUpperYear(String date) {
    	if(StringUtil.isEmpty(date)){
    		return date;
    	}
    	for(char c:date.toCharArray()){
    		String sc=String.valueOf(c);
    		String rep=datemap.get(sc);
    		if(rep!=null){
    			date=date.replaceAll(sc, rep);
    		}
    	}
    	return date;
    }
    /**
     * 根据小写数字格式的日期转换成大写格式的日期M格式
     * @param date
     * @return
     */
    public static String getUpperMonth(String date) {
    	if(StringUtil.isEmpty(date)){
    		return date;
    	}
		String rep=monthmap.get(date);
		if(rep!=null){
			date=rep;
		}
    	return date;
    }

    /**
     * 生成一个Double随机数.
     */
    public static double randomDouble(double min, double max) {
        return (min + new Random().nextDouble() * (max - min));
    }

    public static String getString(Object o) {
		if (o==null) {
			return "";
		}else{
			if (o instanceof BigDecimal) {
				return ((BigDecimal)o).intValue()+"";
			}else{
				return o.toString();
			}
		}
	}
    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
    	if (str != null) {
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	}else{
    		return null;
    	}
    }

    /**
	 * 转换为Boolean类型
	 * 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will return true. Otherwise, false is returned.
	 */
	public static Boolean toBoolean(final Object val) {
		if (val == null) {
			return false;
		}
		return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
	}

    /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static String toString(byte[] bytes) {
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
    }

    /**
	 * 如果对象为空，则使用defaultVal值
	 * 	see: ObjectUtils.toString(obj, defaultVal)
	 * @param obj
	 * @param defaultVal
	 * @return
	 */
    public static String toString(final Object obj, final String defaultVal) {
    	 return obj == null ? defaultVal : obj.toString();
    }

    /**
     * 是否包含字符串
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
    	if (str != null) {
        	for (String s : strs) {
        		if (str.equals(trim(s))) {
        			return true;
        		}
        	}
    	}
    	return false;
    }

    /**
     * 去除字符串中所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trimAll(String s){
        String result = "";
        if (StringUtil.isNotEmpty(s)) {
            result = s.replaceAll("[　*| *| *|//s*]*", "");
        }
        return result;
    }

    /**
     * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等）
     * @param s
     * @return
     */
    public static String trim(String s){
        String result = "";
        if (StringUtil.isNotEmpty(s)) {// 去掉所有空格
            result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");
        }
        return result;
    }

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceEscapeHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		html = StringEscapeUtils.unescapeHtml4(html);
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param html
	 * @return
	 */
	public static String replaceMobileHtml(String html) {
		if (html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * @param txt
	 * @return
	 */
	public static String toHtml(String txt) {
		if (txt == null) {
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	public static String unescapeHtml3(String txt) {
		String str1= StringEscapeUtils.unescapeHtml3(txt);
		return replace(replace(str1,"\r",""),"\n","");
	}

	public static Boolean contains(String src, String csrc) {
	  if (StringUtil.isEmpty(src)) {
      return false;
    }
	  if (StringUtil.isEmpty(csrc)) {
      return false;
    }
	  int idx= src.indexOf(csrc);
	  return (idx == -1)?false:true;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String abbr2(String param, int length) {
		if (param == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}
			try {
				if (!isCode && !isHTML) {
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (n <= length - 3) {
				result.append(temp);
			} else {
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
				"$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result
				.replaceAll(
						"</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
						"");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
				"$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while (m.find()) {
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 设置首字母小写
	 * @param s
	 * @return
	 */
	public static String initialToLowCase(String s) {
		char[] chars = s.toCharArray();
		if ((int) chars[0] >= 65 && (int) chars[0] <= 90) {
			chars[0] += 32;
		}
		return new String(chars);
	}

	/**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
	 * 驼峰命名法工具
	 * @return
	 * 		toCamelCase("hello_world") == "helloWorld"
	 * 		toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * 		toUnderScoreCase("helloWorld") = "hello_world"
	 */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * @param objectString 对象串
     *   例如：row.user.id
     *   返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
    	StringBuilder result = new StringBuilder();
    	StringBuilder val = new StringBuilder();
    	String[] vals = split(objectString, ".");
    	for (int i=0; i<vals.length; i++) {
    		val.append("." + vals[i]);
    		result.append("!"+(val.substring(1))+"?'':");
    	}
    	result.append(val.substring(1));
    	return result.toString();
    }

    public static String rmstart(String str, String... regs) {
        if(regs == null){
            return str;
        }
        for (String reg : regs) {
          //卡号首位以0开头
            if((str).startsWith(reg)){
                str = (str).substring(reg.length());
            }
        }
        return str;
    }

	public static String getSuffixValue(String suffix) {
		String extname = "unknow";
		if (equals(suffix, "xls") || equals(suffix, "xlsx")) {
			extname = "excel";
		} else if (equals(suffix, "doc") || equals(suffix, "docx")) {
			extname = "word";
		} else if (equals(suffix, "ppt") || equals(suffix, "pptx")) {
			extname = "ppt";
		} else if (equals(suffix, "jpg") || equals(suffix, "jpeg") || equals(suffix, "gif") || equals(suffix, "png") || equals(suffix, "bmp")) {
			extname = "image";
		} else if (equals(suffix, "rar") || equals(suffix, "zip") || equals(suffix, "txt") || equals(suffix, "zip") || equals(suffix, "project")) {
			extname = suffix;
		}
		return extname;
	}

	/**
	 * 判断能否整除, 可设定误差范围.
	 * @param dividend 被除数
	 * @param divisor 除数
	 * @return Boolean
	 */
  public static Boolean exeDivision(Integer dividend, Integer divisor) {
    return exeDivision(dividend, divisor, null);
  }

  /**
   * 判断能否整除, 可设定误差范围.
   * @param dividend 被除数
   * @param divisor 除数
   * @return Boolean
   */
  public static Boolean exeDivision(String dividend, Integer divisor) {
    return exeDivision(dividend, divisor, null);
  }

  /**
   * 判断能否整除, 可设定误差范围.
   * @param dividend 被除数
   * @param divisor 除数
   * @param deviation 误差+-
   * @return Boolean
   */
  public static Boolean exeDivision(Integer dividend, Integer divisor, Integer deviation) {
    if (deviation == null) {
      return (dividend / divisor >= 1) && (dividend % divisor == 0);
    }else{
      return (dividend / divisor >= 1) && (dividend % divisor >= -deviation) && (dividend % divisor <= deviation);
    }
  }

  /**
   * 判断能否整除, 可设定误差范围.
   * @param dividend 被除数
   * @param divisor 除数
   * @param deviation 误差+-
   * @return Boolean
   */
  public static Boolean exeDivision(String dividend, Integer divisor, Integer deviation) {
    if (dividend != null) {
      int intDividend = Integer.valueOf(dividend);
      return exeDivision(intDividend, divisor, deviation);
    }
    return false;
  }

  /**
   * 移除末尾的符号.
   * @param num 被处理的字符
   * @param ibyte 保留num的长度位数字
   * @return
   */
  public static String removeLastDotH(String str) {
      return removeLastDotH(str, DOTH);
  }
  /**
   * 移除末尾的符号.
   * @param str
   * @return
   */
  public static String removeLastDotH(String str, String dt) {
    if (isNotEmpty(str) && (str).endsWith(dt)) {
      return str.substring(0, str.length()-1);
    }
    return str;
  }

  /**
   * 不够位数的在前面补0，保留num的长度位数字.
   * @param num 被处理的字符
   * @param ibyte 保留num的长度位数字
   * @return
   */
  public static String autoGenZeroLong(int ibyte, String numStr) {
    return autoGenZero(ibyte, Long.parseLong(numStr));
  }

  /**
   * 不够位数的在前面补0，保留num的长度位数字.
   * @param num 被处理的字符
   * @param ibyte 保留num的长度位数字
   * @return
   */
  public static String autoGenZero(int ibyte, Long num) {
	    /**
	     * 保留num的位数
	     * 0 代表前面补充0
	     * num 代表长度为4
	     * d 代表参数为正数型
	     */
	    return String.format("%0" + ibyte + "d", num);
  }

	public static String autoGenZero(int ibyte, String num) {
		if(StringUtil.isEmpty(num)){
			num = "";
		}
		while (num.length() < ibyte){
			num = "0" + num;
		}
		return num;
	}

	/**
	 * 字符分割长度.
	 * @param split 分隔符
	 * @param str 字符
	 * @return Integer
	 * @author Chenh
	 * @date 2016年5月27日 下午2:52:59
	 */
	public static Integer splitLength(String split, String str) {
		if(StringUtil.isEmpty(str) || StringUtil.isEmpty(split)){
			return null;
		}

		if((str).indexOf(split) == -1){
			return 0;
		}
		return (str).split(split).length;
	}

	/**
	 * 字符获取分隔符的位置数组.
	 * @param split 分隔符
	 * @param str 字符
	 * @return
	 * @author Chenh
	 * @date 2016年5月27日 下午2:52:59
	 */
	public static List<Integer> splitIdxs(String split, String str) {
		RegMtype regMtype = RegMtype.getByKey(split);//处理特殊字符不支持
		List<Integer> ids = Lists.newArrayList();
		String[] splitStrs = str.split(split);
		if(regMtype != null){
			splitStrs = str.split(regMtype.getUnicode());
		}else{
			splitStrs = str.split(split);
		}
		if((splitStrs == null) || (splitStrs.length <= 0)){
			return ids;
		}

		int cur = 0;
		for (String spstr : splitStrs) {
			if(cur == 0){
				cur = str.indexOf(split, cur);
			}else{
				cur = str.indexOf(split, cur + 1);
			}
			if(cur == -1){
				break;
			}
			ids.add(cur);
		}
		return ids;
	}

	/**
	 * 切分字符串为列表.
	 * @param strss 字符串
	 * @param prefix 分隔符
     * @return List
     */
	public static List<String> splitStr(String strss, String prefix) {
		List<String> ids = Lists.newArrayList();
		if(StringUtil.isEmpty(strss) || StringUtil.isEmpty(prefix)){
			return ids;
		}
		ids = Arrays.asList((strss).split(prefix));
		return ids;
	}

    /**
     * 解析分隔符.
     * @param strss 字符串
     * @param prefix 数组属性分隔符
     * @param postFix 列表分隔符
     * @return List
     */
    public static List<String[]> splitByPF(String strss, String prefix, String postFix) {
        if (StringUtil.isNotEmpty(strss)) {
            List<String[]> list = Lists.newArrayList();
            String[] curss = strss.split(postFix);
            for (String curs : curss) {
                if (StringUtil.isEmpty(curs)) {
                    continue;
                }

                String[] cur = curs.split(prefix);
                if(cur != null){
                    list.add(cur);
                }
            }
            return list;
        }
        return null;
    }

  /**
   * 列表转sql
   * @param ids sql字符
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String sqlInByList(List<String> ids) {
    StringBuffer sb = new StringBuffer();
    boolean isFirst = true;
    for (String id : ids) {
      if (isFirst) {
        sb.append("'"+id+"'");
        isFirst = false;
      }else{
        sb.append(",'"+id+"'");
      }
    }
    return sb.toString();
  }

	/**
	 * 列表转sql
	 * @param entity 实体
	 * @return
	 * @author Chenh
	 * @date 2016年5月27日 下午2:52:59
	 */
	public static boolean checkNotEmptyId(IidEntity entity) {
		if ((entity != null) && StringUtil.isNotEmpty(entity.getId())) {
			return true;
		}
		return false;
	}

  /**
   * 列表转sql
   * @param entitys sql字符
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String sqlInByListIds(List<? extends IidEntity> entitys) {
    StringBuffer sb = new StringBuffer();
    boolean isFirst = true;
    for (IidEntity ide : entitys) {
      if (isFirst) {
        sb.append("'"+ide.getId()+"'");
        isFirst = false;
      }else{
        sb.append(",'"+ide.getId()+"'");
      }
    }
    return sb.toString();
  }
  /**
   * 列表转sql ids
   * @param entitys sql字符
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static List<String> sqlInByListIdss(List<? extends IidEntity> entitys) {
      List<String> ids = Lists.newArrayList();
      for (IidEntity ide : entitys) {
      	if (ide == null){
      		continue;
		}
          ids.add(ide.getId());
      }
      return ids;
  }

  /**
   * 字符列表ID转字符 .
   * 格式：
   *    hasEnd=true:1,2,3,4,
   *    hasEnd=false:1,2,3,4
   * @param ids 字符串列表.
   * @param split 分隔符.
   * @param hasEnd 结尾是否含有分隔符.
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String listToStr(List<String> ids, String split, Boolean hasEnd) {
      StringBuffer sb = new StringBuffer();
      if(hasEnd == null){
          hasEnd = false;
      }
      if(StringUtil.isEmpty(split)){
          return null;
      }

      boolean isFirst = true;
      for (String id : ids) {
          if (isFirst) {
              sb.append(id);
              isFirst = false;
          }else{
              sb.append(split);
              sb.append(id);
          }
          if(hasEnd){
              sb.append(split);
          }
      }
      return sb.toString();
  }
  public static String listToStr(List<String> ids, String split) {
      return listToStr(ids, split, false);
  }
  public static String listToStr(List<String> ids) {
      return listToStr(ids, StringUtil.DOTH);
  }

  /**
   * 列表ID转字符 .
   * 格式：
   *    hasEnd=true:1,2,3,4,
   *    hasEnd=false:1,2,3,4
   * @param entitys 含有getId()方法的实体.
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static List<String> listIdToList(List<? extends IidEntity> entitys) {
      List<String> ids = Lists.newArrayList();

      for (IidEntity entity : entitys) {
          ids.add(entity.getId());
      }
      return ids;
  }

  /**
   * 列表ID转字符 .
   * 格式：
   *    hasEnd=true:1,2,3,4,
   *    hasEnd=false:1,2,3,4
   * @param entitys 含有getId()方法的实体.
   * @param split 分隔符.
   * @param hasEnd 结尾是否含有分隔符.
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String listIdToStr(List<? extends IidEntity> entitys, String split, Boolean hasEnd) {
      StringBuffer sb = new StringBuffer();
      if(hasEnd == null){
          hasEnd = false;
      }
      if(StringUtil.isEmpty(split)){
          return null;
      }

      boolean isFirst = true;
      for (IidEntity entity : entitys) {
          if (isFirst) {
              sb.append(entity.getId());
              isFirst = false;
          }else{
              sb.append(split);
              sb.append(entity.getId());
          }
          if(hasEnd){
              sb.append(split);
          }
      }
      return sb.toString();
  }
  public static String listIdToStr(List<? extends IidEntity> entitys, String split) {
      return listIdToStr(entitys, split, false);
  }
  public static String listIdToStr(List<? extends IidEntity> entitys) {
      return listIdToStr(entitys, StringUtil.DOTH);
  }

	/**
	 * 隐藏字符串一部分.
	 * @param str  字符串
	 * @param start 从第几位开始替换
	 * @param length 替换长度
	 * @param pfix 后续替换偏移量
     * @return
     */
	public static String hiddenStr(String str, int start, int length, int pfix) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		return str.replaceAll("(\\d{" + start + "})\\d{" + length + "}(\\d{" + pfix + "})", "$1****$2");
	}

  /**
   * 字符串转sql
   * @param ids sql字符
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String sqlInByStr(String ids) {
    return sqlInByStr(ids, StringUtil.DOTH);
  }

  /**
   * 字符串转sql
   * @param ids sql字符
   * @param spilt 分隔符
   * @return
   * @author Chenh
   * @date 2016年5月27日 下午2:52:59
   */
  public static String sqlInByStr(String ids, String spilt) {
    if (isEmpty(spilt)) {
      spilt = StringUtil.DOTH;
    }
    return sqlInByList(Arrays.asList((ids).split(spilt)));
  }

    public static String getStr(String str) {
        return isEmpty(str) ? "" : str;
    }

    public static String getFloatStr(Float flt) {
        return (flt == null) ? "0.0" : flt.toString();
    }

    /**
     * 检查多个字符串是否都不为空.
     * @return Boolean
     * @author Chenh
     * @date 2016年5月27日 下午2:52:59
     */
    public static Boolean isNotEmptys(String... strs) {
        for (int i = 0; i < strs.length; i++) {
            if(isEmpty(strs[i])){
                return false;
            }
        }
        return true;
    }

    /**
     * 检查列表是否为空.
     * @return Boolean
     * @author Chenh
     * @date 2016年5月27日 下午2:52:59
     */
    public static Boolean checkEmpty(List<? extends Object> list) {
        return ((list == null) || (list.size() <= 0));
    }
    public static Boolean checkNotEmpty(List<? extends Object> list) {
        return !checkEmpty(list);
    }
    public static Boolean checkTwo(List<? extends Object> list) {
        return checkNotEmpty(list) && (list.size() == 2);
    }
    public static Boolean checkOne(List<? extends Object> list) {
        return checkNotEmpty(list) && (list.size() == 1);
    }
    public static Boolean checkUnion(List<? extends Object> list) {
        return checkOne(list);
    }
	public static Boolean checkGtZero(List<? extends Object> list) {
		return checkNotEmpty(list) && (list.size() > 0);
	}
	public static Boolean checkGtOne(List<? extends Object> list) {
		return checkNotEmpty(list) && (list.size() > 1);
	}
	public static Boolean checkGt(List<? extends Object> list, int num) {
		return checkNotEmpty(list) && (list.size() > num);
	}
    /**
     * 占位符替换拼接字符串
     * 第{0}行{1}列不等于{2}-{3}.
     * @param tpl 字符模板
     * @param params 参数列表
     * @return String
     */
    public static String format(String tpl, Object ...params){
        if (StringUtil.isEmpty(tpl)) {
            return null;
        }
        return MessageFormat.format(tpl, params);
    }

	public static String mapKeyInList(Set<String> mapKey, List<String> ids) {
		StringBuffer buf = new StringBuffer();
		if(mapKey != null){
			for (String id : ids) {
				if(!(mapKey).contains(id)){
					buf.append(id);
					buf.append(StringUtil.DOTH);
				}
			}
		}
		return buf.toString();
	}

	/**
	 * 列表元素去重.
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static List removeDup(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

	public static String deleteZero(Object obj) {
        String value = String.valueOf(obj);
        if (value.indexOf(".") > 0) {
            value = value.replaceAll("0+?$", "");//去掉多余的0
            value = value.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        if (StringUtils.equals("0",value)||StringUtils.equals("null",value)) {
            value="0";
        }
        return value;
    }

	/**
	 * 随机生成指定长度的字符串.
	 * @param length 长度
	 * @return String
	 */
	public static String genStr(int length) {
		char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
				'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; i++) {
			s.append(String.valueOf(codeSeq[random.nextInt(codeSeq.length)]));
		}
		return s.toString();
	}

	/**
	 * 随机生成指定长度的字符串.
	 * @param length 长度
	 * @return String
	 */
	public static String genNumStr(int length) {
		char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < length; i++) {
			s.append(String.valueOf(codeSeq[random.nextInt(codeSeq.length)]));
		}
		return s.toString();
	}

    public static String saveNum(Object obj, int n) {
        NumberFormat ddf=NumberFormat.getNumberInstance() ;
        ddf.setMaximumFractionDigits(n);
        String s= ddf.format(obj) ;
        return s;
    }

	// 完整的判断中文汉字和符号
	public static boolean isChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 将字符串text中由open和close组成的占位符依次替换为args数组中的值
	 * @param open
	 * @param close
	 * @param text
	 * @param args
	 * @return String
	 */
	public static String parse(String open, String close, String text, Object... args) {
		if (args == null || args.length <= 0) {
			return text;
		}
		int argsIndex = 0;
		if (text == null || text.isEmpty()) {
			return "";
		}
		char[] src = text.toCharArray();
		int offset = 0;
		// search open token
		int start = text.indexOf(open, offset);
		if (start == -1) {
			return text;
		}
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		while (start > -1) {
			if (start > 0 && src[start - 1] == '\\') {
				// this open token is escaped. remove the backslash and continue.
				builder.append(src, offset, start - offset - 1).append(open);
				offset = start + open.length();
			} else {
				// found open token. let's search close token.
				if (expression == null) {
					expression = new StringBuilder();
				} else {
					expression.setLength(0);
				}
				builder.append(src, offset, start - offset);
				offset = start + open.length();
				int end = text.indexOf(close, offset);
				while (end > -1) {
					if (end > offset && src[end - 1] == '\\') {
						// this close token is escaped. remove the backslash and continue.
						expression.append(src, offset, end - offset - 1).append(close);
						offset = end + close.length();
						end = text.indexOf(close, offset);
					} else {
						expression.append(src, offset, end - offset);
						offset = end + close.length();
						break;
					}
				}
				if (end == -1) {
					// close token was not found.
					builder.append(src, start, src.length - start);
					offset = src.length;
				} else {
					///////////////////////////////////////仅仅修改了该else分支下的个别行代码////////////////////////
					String value = (argsIndex <= args.length - 1) ?
							(args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
					builder.append(value);
					offset = end + close.length();
					argsIndex++;
					////////////////////////////////////////////////////////////////////////////////////////////////
				}
			}
			start = text.indexOf(open, offset);
		}
		if (offset < src.length) {
			builder.append(src, offset, src.length - offset);
		}
		return builder.toString();
	}

	public static String parseDh(String text, Object... args) {
		return parse("${", "}", text, args);
	}
	public static String parseKh(String text, Object... args) {
		return parse("{", "}", text, args);
	}

	public static String[] genRegex(String src, String ...params) {
		return genRegex(StringUtil.PREF, src, params);
	}

	public static String[] genRegex(String spilt, String src, String ...params) {
		return String.format(src, params).split(StringUtil.PREF);
	}

	public static void main(String[] args) {
//    JsonAliUtils.writeBean(SvalPw.ENTER_EXPIRE_LOGFILE + StringUtil.LINE + DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD_HH) + StringUtil.LOG,
//        JSONObject.fromObject(new PwEnterEvo()));
//    System.out.println(autoGenZero(5 , "1ss"));

//	    System.out.println("张洲源#PREF201494582083".replaceAll(StringUtil.PREF, "/"));
//	    System.out.println("张洲源#PREF201494582083".replaceAll(StringUtil.PREF, "/").replaceAll(StringUtil.POST, ","));
//	    System.out.println("何鑫成#PREF6201532#POST郝北成#PREF268316".replaceAll(StringUtil.PREF, "/"));
//	    System.out.println("何鑫成#PREF6201532#POST郝北成#PREF268316".replaceAll(StringUtil.POST, ","));

//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("aaa1", "s");
//		map.put("aaa0", "s");
//
//		List<String> ids = Lists.newArrayList();
//		ids.add("aaa0");
//		ids.add("aaa1");
//		ids.add("aaa2");
//		ids.add("aaa3");
//		System.out.println(mapKeyInList(map.keySet(), ids));

//	    String ss = "第{0}行{1}列不等于{2}-{3}";
//	    System.out.println(format(ss, 0, 1, 2, 3));

//		System.out.println(StringUtil.parse("{", "}", "我的名字是\\{},结果是{}，可信度是%{}", "雷锋", true, 100));
//		System.out.println(StringUtil.parseDh("我的名字是${},结果是${}，可信度是%${}", "雷锋", true, 100));
//		System.out.println(StringUtil.parseKh("我的名字是{},结果是{}，可信度是%{}", "雷锋", true, 100));


//		System.out.println("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 0));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 1));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 2));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 3));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 4));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 2, 5));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 3, 5));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 4, 5));
//		System.out.println(hiddenStr("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3, 5, 5));
	}
}
