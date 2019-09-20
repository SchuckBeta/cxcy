/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;

/**
 * 流程数据导入服务.
 * @author chenhao
 *
 */
public interface IitService<T extends IitParam<?>> {
    /**
     * 下载导入模板.
     */
    public void downTpl(ActYw actyw, HttpServletResponse response, String rootpath, String key);
    /**
     * 删除导入信息和错误数据.
     */
    public ApiTstatus<?> deleteImpInfo(ImpInfo impInfo);
    /**
     * 下载导入错误数据附件.
     */
    public void expErrorData(ImpInfo impInfo, String tplName, String fileName, HttpServletRequest request, HttpServletResponse response);
    /**
     * 下载导入错误数据附件.
     */
    public void getExpErrorDataFile(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response);
    /**
     * 设置下载导出附件的头.
     */
    public void setExcelHead(XSSFSheet sheet);

    /**
     * 获取下载导出附件所有列名索引.
     */
    public void initItIdxVos();

    /**
     * Excel文件说明部分行数.
     */
    public int getExcelHeadRow();

    /**
     * 获取模板导入模板.
     */
    public IitAbsTpl getTpl(IitAbsTpl tpl, String key, String tplName, String fileName);

    /**
     * 检查模板.
     */
    public void checkTpl(IitTpl<?> tpl, HttpServletRequest request);

    /**
     * 导入数据.
     * @throws Exception
     */
    void impData(T param, MultipartFile mpFile, HttpServletRequest request) throws Exception;

    /**
     * 上传附件数据.
     */
    void uploadFtp(T param, MultipartFile mpFile);
}
