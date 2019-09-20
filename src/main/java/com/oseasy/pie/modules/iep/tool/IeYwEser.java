/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import org.springframework.web.multipart.MultipartFile;

import com.oseasy.pie.modules.iep.entity.IepTpl;

/**
 * 流程数据导入服务.
 * @author chenhao
 *
 */
public interface IeYwEser {
//    /**
//     * 下载导入模板.
//     */
//    public void downTpl(ActYw actyw, HttpServletResponse response, String rootpath, String key);
//    /**
//     * 删除导入信息和错误数据.
//     */
//    public Rtstatus<?> deleteImpInfo(ImpInfo impInfo);
//    /**
//     * 下载导入错误数据附件.
//     */
//    public void expErrorData(ImpInfo impInfo, String tplName, String fileName, HttpServletRequest request, HttpServletResponse response);
//    /**
//     * 下载导入错误数据附件.
//     */
//    public void getExpErrorDataFile(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response);
//    /**
//     * 设置下载导出附件的头.
//     */
//    public void setExcelHead(XSSFSheet sheet);
//
//    /**
//     * 获取下载导出附件所有列名索引.
//     */
//    public void initItIdxVos();
//
//    /**
//     * Excel文件说明部分行数.
//     */
//    public int getExcelHeadRow();
//
//    /**
//     * 获取模板导入模板.
//     */
//    public IitAbsTpl getTpl(IitAbsTpl tpl, String key, String tplName, String fileName);
//
    /**
     * 检查模板.
     */
    public void checkTpl(IepTpl iepTpl, IeAbsYw yw);

    /**
     * 导入数据.
     * @throws Exception
     */
    public void impData(IepTpl iepTpl, IeAbsYw yw, MultipartFile mpFile) throws Exception;
//
//    /**
//     * 上传附件数据.
//     */
//    void uploadFtp(IePparam param, MultipartFile mpFile);
}
