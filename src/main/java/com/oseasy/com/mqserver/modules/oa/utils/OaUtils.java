/**
 *
 */
package com.oseasy.com.mqserver.modules.oa.utils;

/**
 * 内容管理工具类


 */
public class OaUtils {
	public static final String OA_CACHE = "oaCache";
	/**
	 * 通知显示最大记录数：5
	 */
	public static final Integer OA_CACHE_NOTIFYS_MAXNUM = 5;
    private static final String DOWN_FILE_F = "f/ftp/ueditorUpload/downFile";
    private static final String DOWN_FILE_A = "a/ftp/ueditorUpload/downFile";

    public static String convertFront(String content) {
        if (((content).indexOf(DOWN_FILE_A) != -1)) {
            content = content.replaceAll(DOWN_FILE_A, DOWN_FILE_F);
        }
        return content;
    }
}