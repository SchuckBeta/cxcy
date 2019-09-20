package com.oseasy.com.fileserver.vsftp.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zhangzheng on 2017/6/26.  百度富文本编辑器初始化controller
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UeditorController  {

    @RequestMapping(value = "upload")
    public void upload(HttpServletRequest request, HttpServletResponse response)throws Exception{
        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        String rootPath =request.getSession().getServletContext().getRealPath("/");
        response.getWriter().write(new MyActionEnter(request, rootPath).exec());
    }







}
