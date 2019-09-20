package com.oseasy.util.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialUtils {
    private static final String PFIX = "OS";
    public static final String FORMAT_YMDH = "yyyyMMddhh";
	private static final String FORMAT_ALL = "yyyyMMddhhmmssS";
	private static long orderNum = 0;
	private static String count = "000";
	private static String dateValue = "20000000";

	/****************************************************************************************
	 * 产生序号
	 */
	public synchronized static String getSerialNo() {
		return getSerialNo(PFIX, FORMAT_ALL);
	}
	public synchronized static String getSerialNo(String pfix) {
		return getSerialNo(pfix, FORMAT_ALL);
	}
	public synchronized static String getSerialNo(String pfix, String FORMAT_ALL) {
		long No = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_ALL);
		String nowdate = sdf.format(new Date());
		No = Long.parseLong(nowdate);
		if (!(String.valueOf(No)).equals(dateValue)) {
			count = "000";
			dateValue = String.valueOf(No);
		}
		String num = String.valueOf(No);
		num += getNo(count);
		num = pfix + num;
		return num;
	}

	/**
	 * 返回当天的订单数+1
	 */
	public static String getNo(String s) {
		String rs = s;
		int i = Integer.parseInt(rs);
		i += 1;
		rs = "" + i;
		for (int j = rs.length(); j < 3; j++) {
			rs = "0" + rs;
		}
		count = rs;
		return rs;
	}

    /****************************************************************************************
     * 生成订单编号
     * @return
     */
    public static synchronized String getOrderNo() {
		return getOrderNo(PFIX);
    }
	public static synchronized String getOrderNo(String pfix) {
		return getOrderNo(pfix, FORMAT_ALL);
	}
	public static synchronized String getOrderNo(String pfix, String FORMAT_ALL) {
        String str = new SimpleDateFormat(FORMAT_ALL).format(new Date());
        long orderNo = Long.parseLong((str)) * 100;
        orderNo += orderNum;
        orderNum ++;
        if (orderNum>99) {
        	orderNum  = 0;
        }
        return pfix+(orderNo+"");
    }

	public static void main(String[] args) {
		System.out.println(SerialUtils.getSerialNo(StringUtil.upperCase("Product"), "yyyMMddhh"));
//		System.out.println(getSerialNo());
//		System.out.println(getSerialNo());
//		System.out.println(getOrderNo());
		for(int i=0;i<1200;i++) {
//			System.out.println(getOrderNo());
			System.out.println(getOrderNo("ORDERNO"));
		}
	}
}
