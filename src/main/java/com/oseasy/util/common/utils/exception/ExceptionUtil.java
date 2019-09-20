package com.oseasy.util.common.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionUtil {
    /**
     * 取得异常的stacktrace字符串。
     * 
     * @param throwable
     *            异常
     * 
     * @return stacktrace字符串
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter buffer = new StringWriter();
        PrintWriter out =null;
        try {
			out = new PrintWriter(buffer);
			throwable.printStackTrace(out);
			out.flush();
		} finally{
			if (out!=null) {
				out.close();
			}
		}
        return buffer.toString();
    }
    
}
