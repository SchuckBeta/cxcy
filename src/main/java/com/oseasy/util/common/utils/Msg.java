package com.oseasy.util.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午9:59:27
 */
public class Msg extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Msg() {
        put("code", 0);
    }

    public static Msg error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Msg error(String msg) {
        return error(500, msg);
    }

    public static Msg error(int code, String message) {
        Msg msg = new Msg();
        msg.put("code", code);
        msg.put("msg", message);
        return msg;
    }

    public static Msg ok(String message) {
        Msg msg = new Msg();
        msg.put("msg", message);
        return msg;
    }

    public static Msg ok(Map<String, Object> map) {
        Msg msg = new Msg();
        msg.putAll(map);
        return msg;
    }

    public static Msg ok() {
        return new Msg();
    }

    public Msg put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
