package com.oseasy.com.pcore.modules.sys.utils;

import java.util.regex.Pattern;

public class RegexUtils {
	public static  String mobileRegex = "^0?(13[0-9]|14[0-9]|15[012356789]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$";
	public static  String emailRegex = "^[a-zA-Z0-9_-][\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
	public static  String grantRegex="^0|0.0|0.00|[1-9][0-9]*(\\.[0-9]{1,2})?$";
	public static  String idNumRegex="(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

	public static void main(String[] args) {
	    String[] argsss = new String[]{"14785619314", "19947122560", "17762355089", "16602731346"};
	    for (String str : argsss) {
	        System.out.println(Pattern.matches(RegexUtils.mobileRegex, str));
        }
    }
}
