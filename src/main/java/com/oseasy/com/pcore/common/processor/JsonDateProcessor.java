/**
 * .
 */

package com.oseasy.com.pcore.common.processor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.oseasy.util.common.utils.DateUtil;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * Json日期处理.
 * @author chenhao
 */
public class JsonDateProcessor implements JsonValueProcessor {
    private String format = DateUtil.FMT_YYYYMMDD_HHmmss_ZG;

   public JsonDateProcessor() {
       super();
   }

   public JsonDateProcessor(String format) {
       super();
       this.format = format;
   }

    @Override
    public Object processArrayValue(Object paramObject, JsonConfig paramJsonConfig) {
        return process(paramObject);
    }

    @Override
    public Object processObjectValue(String paramString, Object paramObject, JsonConfig paramJsonConfig) {
        return process(paramObject);
    }

    private Object process(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }
}
