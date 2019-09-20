package com.oseasy.sys.common.config;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.oseasy.com.common.utils.CkeyMsvo;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.oseasy.com.common.utils.PathMsvo;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.com.common.utils.IEu;

/**
 * 用户拓展信息系统模块常量
 * @author chenhao
 *
 */
public class SysSval {
    public static SysPath path = new SysPath();
    public static SysCkey ck = new SysCkey();

    public enum SysEmskey implements IEu {
        SYS("sys", "用户拓展信息模块"),
        SEQ("seq", "系列号模块"),
        TEAM("team", "团队管理模块");

        private String key;//url
        private String remark;
        private SysEmskey(String key, String remark) {
            this.key = key;
            this.remark = remark;
        }

        public static List<PathMsvo> toPmsvos() {
            List<PathMsvo> entitys = Lists.newArrayList();
            for (SysEmskey entity : SysEmskey.values()) {
                entitys.add(new PathMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }

        public static List<CkeyMsvo> toCmsvos() {
            List<CkeyMsvo> entitys = Lists.newArrayList();
            for (SysEmskey entity : SysEmskey.values()) {
                entitys.add(new CkeyMsvo(entity.k(), entity.getRemark()));
            }
            return entitys;
        }
        public String k() {
            return key;
        }

        public String getRemark() {
            return remark;
        }
    }

    public static class SysCertSval{
      public static final String SYS_CERTIFICATE_TYPE = "sys_certificate_type";//证书类型
    }

    public static final String DICT_MASTER_HELP = "master_help";//服务意向
    public static final String DICT_MASTER_TYPE = "master_type";
    public static final String DICT_PROFESSIONAL_TYPE = "professional_type";//学科门类
    public static final String DICT_XUEKE = "0000000111";//学科门类
    public static final String DICT_CURRENT_SATE = "current_sate";
    public static final String DICT_DEGREE_TYPE = "degree_type";//学位、在读学位
    public static final String DICT_ENDUCATION_DEGREE = "enducation_degree";//学位、在读学位
    public static final String DICT_ENDUCATION_LEVEL = "enducation_level";//学历
    public static final String DICT_ENDUCATION_TYPE = "enducation_type";//学历类别
    public static final String DICT_ID_TYPE = "id_type";
    public static final String DICT_SEX = "sex";
    public static final String DICT_TECHNOLOGY_FIELD = "technology_field";//技术领域
    /**
     * 团队负责人和组员.
     */
    public static final String FZR = "0";
    public static final String ZY = "1";
    /**
     * 团队学生和导师
     */
    public static final String XS = "1";
    /**
     * 团队学生和导师
     */
    public static final String DS = "2";
    /**
     * 获取URL后缀.
     */
    public static String getDomainlt(String domain, String html) {
    	String[] domains = StringUtils.split(domain, StringUtil.DOTH);
    	String returnString = "";
    	for (int i = 0; i < domains.length; i++) {
    		returnString += "<" + html + ">" + domains[i] + "</" + html + ">";
    	}
    	return returnString;
    }
    /**
     * 获取URL后缀. public static String getAge(Date birthday) { String
     * returnString=""; return returnString; }
     */
    public static int getAge(Date birthDay) {
    	Calendar cal = Calendar.getInstance();
    	if (cal.before(birthDay)) {
    		throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
    	}

    	int yearNow = cal.get(Calendar.YEAR);
    	int monthNow = cal.get(Calendar.MONTH);
    	int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
    	cal.setTime(birthDay);

    	int yearBirth = cal.get(Calendar.YEAR);
    	int monthBirth = cal.get(Calendar.MONTH);
    	int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

    	int age = yearNow - yearBirth;
    	if (monthNow <= monthBirth) {
    		if (monthNow == monthBirth) {
    			// monthNow==monthBirth
    			if (dayOfMonthNow < dayOfMonthBirth) {
    				age--;
    			} else {
    				// do nothing
    			}
    		} else {
    			// monthNow>monthBirth
    			age--;
    		}
    	} else {
    		// monthNow<monthBirth
    		// donothing
    	}
    	return age;
    }

    /**
     * 团队状态
     */
    public static final String YJR= "0";
    public static final String SQJR = "1";
    public static final String FZYQ = "2";
    public static final String BTY = "3";
    public static final String HL = "4";
    public static final String SB = "5";
}
