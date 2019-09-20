/**
 * .
 */

package com.oseasy.act.modules.actyw.tool.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

/**
 * 表单页面参数加载类.
 * @author chenhao
 *
 */
public interface IpageParam {
    /**
     * 核心：加载表单，初始化多种参数.
     * @param model 模型
     * @param request 请求
     * @param response 响应
     * @param objs 参数对象（注意强转和异常抛出）
     * @return Boolean
     */
    public Boolean init(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs);

    /**
     * 初始化附件参数，根据页面需要，在表单页面的init方法中选择性调用.
     * @param model 模型
     * @param request 请求
     * @param response 响应
     * @param objs 参数对象（注意强转和异常抛出）
     * @return Boolean
     */
    public Boolean initSysAttachment(Model model, HttpServletRequest request, HttpServletResponse response, Object... objs);
}
