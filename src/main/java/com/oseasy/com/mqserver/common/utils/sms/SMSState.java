package com.oseasy.com.mqserver.common.utils.sms;

/**
 * Created by victor on 2017/3/11.
 */
public enum SMSState {
    SUCCESS(100, "发送成功"), FAIL101(101, "验证失败"), FAIL201(201, "未请求,参数为空"), FAIL202(202, "未请求,参数不合法");

    private int state;
    private String message;

    // 普通方法
    public static String getName(int state) {
        for (SMSState c : SMSState.values()) {
            if (c.getState() == state) {
                return c.message;
            }
        }
        return null;
    }


    SMSState(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
