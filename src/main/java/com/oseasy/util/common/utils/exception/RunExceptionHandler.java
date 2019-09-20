package com.oseasy.util.common.utils.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.oseasy.util.common.utils.Msg;

@ControllerAdvice(basePackages = {"com.oseasy"})
public class RunExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(RunException.class)
    public Msg handleRunException(RunException e){
        Msg r = new Msg();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());

        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Msg handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return Msg.error("数据库中已存在该记录");
    }



//    @ExceptionHandler(Exception.class)
//    public Msg handleException(Exception e){
//        logger.error(e.getMessage(), e);
//        return Msg.error();
//    }
}