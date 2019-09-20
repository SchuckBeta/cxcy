package com.oseasy.com.common.config;

import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;

/**
 * cms接口返回结果类
 * Created by liangjie on 2018/8/30.
 */
public class ApiResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ApiResult.class);

    /**
        调用状态
    */
    private Integer status;
    /**
        错误编码
     */
    private Integer code ;
    /**
        错误信息
     */
    private String msg ="";
    /**
        业务数据
     */
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    /**
     * 提供成功的静态方法，方便使用
     * @return
     */
    public static ApiResult success(){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_SUCCESS);
        result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
        if(logger.isDebugEnabled()){
            logger.debug("输出响应："+result);
        }
        return result;
    }

    /**
     * 提供成功的静态方法，方便使用
     * @param data 数据
     * @return
     */
    public static ApiResult success(Object data){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_SUCCESS);
        result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
        result.setData(data);
        if(logger.isDebugEnabled()){
            logger.debug("输出响应："+result);
        }
        return result;
    }

    /**
     * 提供成功的静态方法，方便使用
     * @param data 数据
     * @return
     */
    public static ApiResult success(Object data,String msg){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_SUCCESS);
        result.setCode(ApiConst.CODE_REQUEST_SUCCESS);
        result.setMsg(msg);
        result.setData(data);
        if(logger.isDebugEnabled()){
            logger.debug("输出响应："+result);
        }
        return result;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param code  错误码
     * @param msg   错误消息
     * @return
     */
    public static ApiResult failed(Integer code,String msg){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_FAIL);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param code  错误码
     * @return
     */
    public static ApiResult failed(Integer code){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_FAIL);
        result.setCode(code);
        //输出错误格式：[code]msg,例如：[1003]内部错误
        String msg = StringUtil.KUOHZL+code+StringUtil.KUOHZR+ApiConst.getErrMsg(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param code  错误码
     * @param msg   错误消息
     * @return
     */
    public static ApiResult failed(Object data,Integer code,String msg){
        ApiResult result = new ApiResult();
        result.setStatus(ApiConst.STATUS_FAIL);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
