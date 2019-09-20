package com.oseasy.pw.modules.pw.vo;

public class Msg {

    private boolean success;

    private String id;

    private String msg;

    public Msg(boolean success, String id) {
        this.success = success;
        this.id = id;
    }

    public Msg(String msg) {
        this.success = false;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toJson(){
        if(success){
            return "{\"success\":true,\"id\":\"" + id + "\"}";
        }else{
            return "{\"success\":false,\"msg\":\"" + msg + "\"}";
        }
    }
}
