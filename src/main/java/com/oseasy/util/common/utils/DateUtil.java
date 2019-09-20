/**
 *
 */
package com.oseasy.util.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;


/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类

 * @version 2014-4-15
 */
public class DateUtil extends DateUtils {
  	public static final String FMT_HMS000 = "00:00:00";
  	public static final String FMT_HMS999 = "23:59:59";
  	public static final String FMT_YYYYMMDD = "yyyy年MM月dd日";
	public static final String FMT_YYYYMM_ZG = "yyyy-MM";
  	public static final String FMT_YYYYMMDD_ZG = "yyyy-MM-dd";
  	public static final String FMT_YYYYMMDD_HHmmss_ZG = "yyyy-MM-dd HH:mm:ss";
  	public static final String FMT_YYYY_MM_DD = "yyyyMMdd";
  	public static final String FMT_YYYY = "yyyy";
  	public static final String FMT_MM = "MM";
	public static final String FMT_YYYY_MM_DD_HH = "yyyyMMddHH";
	public static final String FMT_YYYY_MM_DD_HH12 = "yyyyMMddhh";
  	public static final String FMT_YYYY_MM_DD_HHmmss = "yyyyMMddHHmmss";
  	public static final String FMT_YYYYMMDD_HHmmss = "yyyy年MM月dd日 HHmmss";
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 转换格式数组
	 */
	private static SimpleDateFormat[] simpleDateFormat = new SimpleDateFormat[10];

	/**
	 * 静态初始化
	 */
	static {
		simpleDateFormat[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		simpleDateFormat[1] = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateFormat[2] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		simpleDateFormat[3] = new SimpleDateFormat("yyyy/MM/dd");
		simpleDateFormat[4] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		simpleDateFormat[5] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		simpleDateFormat[6] = new SimpleDateFormat("yyyy年/MM月/dd日");
		simpleDateFormat[7] = new SimpleDateFormat("yyyy年MM月dd日");
		simpleDateFormat[8] = new SimpleDateFormat("yyyy年-MM月-dd日");
		simpleDateFormat[9] = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");
	}

	/**
	 * 时间类型.
	 */
	public enum Dtype{
	  	YEAR(365, "年"),YEAR_A_HALF(183, "半年"), QUARTER(92, "季度"), MONTH(31, "月"), DAY(1, "天");
	  	private Integer num;
	  	private String remarks;
    private Dtype(Integer num, String remarks) {
      	this.num = num;
      	this.remarks = remarks;
    }
    public Integer getNum() {
      return num;
    }
    public String getRemarks() {
      return remarks;
    }

    /**
     * 根据num获取枚举 .
     * @author chenhao
     * @param num 枚举标识
     * @return Dtype
     */
    public static Dtype getByNum(Integer num) {
     	if ((num != null)) {
        	Dtype[] entitys = Dtype.values();
        	for (Dtype entity : entitys) {
          		if ((num).equals(entity.getNum())) {
            	return entity;
          		}
        	}
      	}
      	return null;
   	 	}
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期000 格式（yyyy-MM-dd 00:00:00:000）
	 */
	public static Date getCurDateYMD000() {
    return getCurDateYMD000(new Date());
	}
	public static Date getCurDateYMD000(Date curDate) {
	   if (curDate == null) {
	      curDate = new Date();
	    }
	  curDate = setHours(curDate, 0);
	  curDate = setMinutes(curDate, 0);
	  curDate = setSeconds(curDate, 0);
	  curDate = setMilliseconds(curDate, 0);
	  return curDate;
	}

	  public static Date newStartDate() {
	    try {
	      return getStartDate(new Date());
	    } catch (ParseException e) {
	      e.printStackTrace();
	    }
	    return null;
	  }
	  public static Date newEndDate() {
	      try {
	          return getEndDate(new Date());
	      } catch (ParseException e) {
	          e.printStackTrace();
	      }
	      return null;
	  }
	/**
	 * 得到当前日期999 格式（yyyy-MM-dd 23:59:59:999）
	 */
	public static Date getCurDateYMD999() {
	  return getCurDateYMD999(new Date());
	}
	public static Date getCurDateYMD999(Date curDate) {
	  if (curDate == null) {
	    curDate = new Date();
	  }
	  curDate = setHours(curDate, 23);
	  curDate = setMinutes(curDate, 59);
	  curDate = setSeconds(curDate, 59);
	  curDate = setMilliseconds(curDate, 999);
	  return curDate;
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}


	public static boolean betweenDay(Date startDate, Date endDate) {
			boolean result=false;
	        if (startDate != null && endDate != null) {
	          	Date to=new Date();
				if (to.after(startDate) && to.before(endDate)) {
					 result=true;
				}
	            return result;
	        } else {
	            throw new IllegalArgumentException("The date must not be null");
	        }
	 }
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 将HH:mm:ss格式转换为Date.
	 */
	@SuppressWarnings("deprecation")
  public static Date formatHmsToDate(String dateHms) {
	    Date curDate = new Date();
      String[] hms = (dateHms).split(StringUtil.MAOH);
      if (hms.length != 3) {
        return null;
      }
      curDate.setHours(Integer.parseInt(hms[0]));
      curDate.setMinutes(Integer.parseInt(hms[1]));
      curDate.setSeconds(Integer.parseInt(hms[2]));
      return curDate;
	}

	public static String formatToDstr(String year, String month) {
	    String rdate = DateUtil.FMT_YYYYMM_ZG;
	    rdate = rdate.replace(DateUtil.FMT_YYYY, year);
	    rdate = rdate.replace(DateUtil.FMT_MM, month);
	    return rdate;
	}
    public static Date formatToDate(String format, String dateYMdHms) {
        if(StringUtil.isNotEmpty(format) && StringUtil.isNotEmpty(dateYMdHms)){
            try {
                SimpleDateFormat fm = new SimpleDateFormat(format);
                return fm.parse(dateYMdHms);
            } catch (ParseException e1) {
                return null;
            }
        }
	    return null;
	}

    public static String formatToDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (StringUtils.isBlank(pattern)) {
            pattern = FMT_YYYYMMDD_HHmmss_ZG;
        }
        sdf.applyPattern(pattern);
        return sdf.format(date);
    }

    public static String formatDate(Date date) {
        return DateUtil.formatToDate(date, null);
    }

	public static Date formatToDate(SimpleDateFormat format, String date) {
	    if((format != null) && StringUtil.isNotEmpty(date)){
	        try {
	            return format.parse(date);
	        } catch (ParseException e1) {
	            return null;
	        }
	    }
	    return null;
	}


	public static String format(SimpleDateFormat format, Date date) {
	 return (date == null) ? "" : (format).format(date);
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	public static Date parseDate(String str,String pattern) throws ParseException {
		if (str == null) {
			return null;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.parse(str);
	}
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
    return pastDays(new Date(), date);
	}

	public static long pastDays(Date curDate, Date date) {
	  return pastDays(curDate, date, false);
	}

  public static long pastDays(Date curDate, Date date, Boolean isGtZero) {
    if (isGtZero == null) {
      isGtZero = false;
    }
	  long t = curDate.getTime()-date.getTime();
	  if (t > 0) {
	    return t/(24*60*60*1000);
	  }else if (t == 0) {
	    return 0;
	  }else{
	    if (isGtZero) {
        return (-t)/(24*60*60*1000);
	    }else{
        return t/(24*60*60*1000);
	    }
	  }
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		if (t > 0) {
      return t/(60*60*1000);
    }else if (t == 0) {
      return 0;
    }else{
      return (-t)/(60*60*1000);
    }
	}

	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		if (t > 0) {
      return t/(60*1000);
    }else if (t == 0) {
      return 0;
    }else{
      return (-t)/(60*1000);
    }
	}
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime3(long timeMillis) {
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		if(day==0L&&hour==0L&&min==0L){
			return "1分钟";
		}
		return (day>0?day+"天":"")+(hour>0?hour+"小时":"")+(min>0?min+"分钟":"");
    }
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis) {
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	/**
	 * 转换为时间（x天）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime2(long timeMillis) {
		long day = timeMillis/(24*60*60*1000);
		if (day==0l) {
			long hour = (timeMillis/(60*60*1000)-day*24);
			long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
			long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
			return (hour>0?hour+"小时":"")+(min>0?min+"分":"")+s+"秒";
		}
		if (timeMillis%(24*60*60*1000)!=0) {
			day++;
		}
		return day+"天";
    }
	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	public static Date addDay(Date s, int day) {
		Calendar c=Calendar.getInstance();
		c.setTime(s);
		c.add(Calendar.DAY_OF_MONTH, day);
		return c.getTime();
	}
	public static Date addMonth(Date s, int month) {
		Calendar c=Calendar.getInstance();
		c.setTime(s);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}
	public static Date addYear(Date s,int year) {
		Calendar c=Calendar.getInstance();
		c.setTime(s);
		c.add(Calendar.YEAR, year);
		return c.getTime();
	}
	public static Date getStartDate(Date s) throws ParseException{
		if (s==null) {
			return s;
		}
		return parseDate(formatDate(s,"yyyy-MM-dd")+" 00:00:00","yyyy-MM-dd HH:mm:ss");
	}
	public static Date getEndDate(Date s) throws ParseException{
		if (s==null) {
			return s;
		}
		return parseDate(formatDate(s,"yyyy-MM-dd")+" 23:59:59","yyyy-MM-dd HH:mm:ss");
	}

  public static Date newDate() {
      return new Date();
  }

  /**
   * 根据类型添加天数（忽略负值）.
   * @param type Dtype年、半年、月、日、季度
   * @param num 添加数量
   * @param date 时间
   * @return Integer
   */
  public static Integer addDayByType(Dtype type, Integer num, Date date) {
    return addDayByType(type, num, date, false, true);
  }

  /**
   * 根据类型添加天数.
   * @param type Dtype年、半年、月、日、季度
   * @param num 添加数量
   * @param date 时间
   * @param ignFval 是否忽略负值
   * @return Integer
   */
  public static Integer addDayByType(Dtype type, Integer num, Date date, Boolean ignFval) {
    return addDayByType(type, num, date, false, ignFval);
  }

  /**
   * 根据类型添加天数.
   * @param type Dtype年、半年、月、日、季度
   * @param num 添加数量
   * @param date 时间
   * @param useDef 是否使用默认
   * @param ignFval 是否忽略负值
   * @return Integer
   */
  public static Integer addDayByType(Dtype type, Integer num, Date date, Boolean useDef, Boolean ignFval) {
    if ((type == null) || (num == null) || (date == null)) {
      return null;
    }

    if (useDef == null) {
      useDef = false;
    }

    if (ignFval == null) {
      ignFval = false;
    }

    Integer days = null;
    if (useDef) {
      switch (type) {
        case YEAR:
          days = Dtype.YEAR.getNum();
          break;
        case YEAR_A_HALF:
          days = Dtype.YEAR_A_HALF.getNum();
          break;
        case MONTH:
          days = Dtype.MONTH.getNum();
          break;
        case QUARTER:
          days = Dtype.QUARTER.getNum();
          break;
        case DAY:
          days = Dtype.DAY.getNum();
          break;
        default:
          break;
      }
    }else{
      switch (type) {
        case YEAR:
          days = (int) DateUtil.pastDays(addYear(date, num), date, ignFval);
          break;
        case YEAR_A_HALF:
          days = (int) DateUtil.pastDays(addMonth(date, 6 * num), date, ignFval);
          break;
        case QUARTER:
          days = (int) DateUtil.pastDays(addMonth(date, 3 * num), date, ignFval);
          break;
        case MONTH:
          days = (int) DateUtil.pastDays(addMonth(date, num), date, ignFval);
          break;
        case DAY:
          days = num;
          break;
        default:
          break;
      }
    }
    return days;
  }

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(DateUtil.dateToStamp("2019-02-02"));
//		System.out.println(DateUtil.stampToDateString("20190202"));
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
//		String s="yyyyMMddHHss";
//		System.out.println(formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		System.out.println(DateUtil.formatDate(new Date(), s));
//		System.out.println(DateUtil.formatHmsToDate("21:12:11"));


//	  Date date = new Date();
//    DateUtil.setMonths(date, 1);
//    date = DateUtil.setYears(date, 2019);
//    long days = DateUtil.pastDays(date);
//    System.out.println(days);

//	  int days = DateUtil.addDayByType(Dtype.YEAR, 1, date, false);
//	  int days = DateUtil.addDayByType(Dtype.YEAR_A_HALF, 1, date, false);
//	  int days = DateUtil.addDayByType(Dtype.MONTH, 1, date, false);
//	  int days = DateUtil.addDayByType(Dtype.QUARTER, 1, date, false);
//	  int days = DateUtil.addDayByType(Dtype.DAY, 1, date, false);
//	  System.out.println(days);
//	  System.out.println(formatDate(date, "yyyy-MM-dd HH:mm:ss"));
//    System.out.println(formatDate(DateUtil.addDay(date, days), "yyyy-MM-dd HH:mm:ss"));
//    System.out.println(formatDate(getCurDateYMD(), "yyyy-MM-dd HH:mm:ss-SSS"));
	}

    /**
     * 指定日期，往前或往后推多少天，取那天0时0分0秒
     * @param date
     * @param delay
     * @return Date
     */
    public static Date getDayFromBegin(Date date, int delay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.DATE, delay);
        return c.getTime();
    }

    /**
     * 从当前天开始，往前或往后推多少天，取那天0时0分0秒
     * @param delay
     * @return Date
     */
    public static Date getDayFromBegin(int delay) {
        Calendar c = Calendar.getInstance();
        return getDayFromBegin(c.getTime(), delay);
    }

    /**
     * 指定日期，往前或往后推多少天，取那天23时59分59秒
     * @param date
     * @param delay
     * @return Date
     */
    public static Date getDayToEnd(Date date, int delay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.add(Calendar.DATE, delay);
        return c.getTime();
    }

    /**
     * 从当前天开始，往前或往后推多少天，取那天23时59分59秒
     * @param delay
     * @return
     */
    public static Date getDayToEnd(int delay) {
        Calendar c = Calendar.getInstance();
        return getDayToEnd(c.getTime(), delay);
    }

	/**
	 * 获得当天0点时间
	 *
	 * @return Date
	 */
	public static Date getTimesmorning() {
		// 获得今天时间
		Calendar c = Calendar.getInstance();

		// 将时，分，秒，毫秒设置为0
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		// 此处返回的为今天的零点的毫秒数
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 获得当天时间加上自定义天数(Integer day)
	 *
	 * @param day
	 *            自定义天数
	 * @return Date
	 */
	public static Date getTimeByDay(Integer day) {
		if (null == day) {
			return null;
		}
		// 获得今天时间
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);
		// 此处返回的为今天的零点的毫秒数
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 获得当天0点时间加上自定义天数(Integer day)
	 *
	 * @param day
	 *            自定义天数
	 * @return Date
	 */
	public static Date getTimesByDay(Integer day) {
		if (null == day) {
			return null;
		}
		// 获得今天时间
		Calendar c = Calendar.getInstance();

		// 将时，分，秒，毫秒设置为0
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DATE, day);
		// 此处返回的为今天的零点的毫秒数
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 将时间转换为时间戳
	 *
	 * @param s
	 *            时间
	 * @return 时间戳
	 * @throws ParseException
	 */
	public static long dateToStamp(String s) throws ParseException {
		Date date = new Date();
		for (SimpleDateFormat flag : simpleDateFormat) {
			try {
				date = flag.parse(s);
				break;
			} catch (Exception e) {
			}
		}
		long ts = date.getTime() / 1000;
		return ts;
	}

	/**
	 * 将时间戳转换为时间
	 *
	 * @param s
	 *            时间戳
	 * @return 时间
	 */
	public static String stampToDateString(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
