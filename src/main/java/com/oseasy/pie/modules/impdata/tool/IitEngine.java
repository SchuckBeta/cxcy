/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * 导入模板引擎.
 * @author chenhao
 *
 */
public interface IitEngine{
    /**
     * 检查模板.
     */
    public void checkTpl(IitTpl<?> tpl, HttpServletRequest request);

    /**
     * 导入多个附件数据.
     */
    public void impDatas(IitParam<?> param, List<MultipartFile> mpFiles, HttpServletRequest request);

    /**
     * 导入数据单个附件.
     */
    public void impData(IitParam<?> param, MultipartFile mpFile, HttpServletRequest request);

    /**
     * 执行导入.
     */
    public void run(IitParam<?> param, List<MultipartFile> mpFiles, HttpServletRequest request);
}
