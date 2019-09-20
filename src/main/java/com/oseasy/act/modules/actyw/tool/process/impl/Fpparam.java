/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

import com.oseasy.act.modules.actyw.tool.process.IpageParam;

/**
 * 公共参数传递.
 * @author chenhao
 *
 */
public class Fpparam implements IpageParam{
    @Override
    public Boolean init(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        return true;
    }

    @Override
    public Boolean initSysAttachment(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs) {
        //什么也不做，表示页面不支持表单
        return true;
    }
}
