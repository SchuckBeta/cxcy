/**
 * .
 */

package com.oseasy.pro.modules.promodel.vo;

import java.io.File;

import com.oseasy.pro.modules.workflow.IExpGfile;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.excel.entity.ExportParams;

/**
 * 附件导入导出参数实体.
 * @author chenhao
 *
 */
public class ExpGnodeFile extends ExpSfile implements IExpGfile{
    private Boolean hasTimeid;//是否有时间戳
    private String rpath;//基础路径
    private String oname;//机构名称
    private boolean hideTitle;//隐藏头
    private String fileName;//文件名称
    private String sheetName;//Sheet名称
    private String fileType;//默认xlsx
    private ItReqParam reqParam;//导入导出请求参数
    private Class<?> clazz;//默认xlsx

    public ExpGnodeFile() {
        super();
        this.hasTimeid = false;
        this.hideTitle = false;
        this.reqParam = new ItReqParam();
        this.fileType = StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
    }

    public ExpGnodeFile(ItReqParam param) {
        super();
        this.reqParam = param;
        this.hasTimeid = false;
        this.hideTitle = false;
        this.fileType = StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
    }

    public ExpGnodeFile(String rpath, String oname, String fileName) {
        super();
        this.hasTimeid = false;
        this.hideTitle = false;
        this.rpath = rpath;
        this.oname = oname;
        this.fileName = fileName;
        this.reqParam = new ItReqParam();
        this.fileType = StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
    }

    public ExpGnodeFile(ItReqParam param, String rpath, String oname, String fileName) {
        super();
        this.hasTimeid = false;
        this.hideTitle = false;
        this.rpath = rpath;
        this.oname = oname;
        this.reqParam = param;
        this.fileName = fileName;
        this.fileType = StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
    }

    @Override
    public String getPath() {
        return this.getRpath() + File.separator + this.getFileName() + StringUtil.LINE_D + this.getOname() + this.getFileType();
    }

    public boolean getHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
    }

    public ItReqParam getReqParam() {
        return reqParam;
    }

    public void setReqParam(ItReqParam reqParam) {
        this.reqParam = reqParam;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    @Override
    public Class<?> getClazz() {
        return this.clazz;
    }

    public Boolean getHasTimeid() {
        return hasTimeid;
    }

    public void setHasTimeid(Boolean hasTimeid) {
        this.hasTimeid = hasTimeid;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getRpath() {
        return rpath;
    }

    public void setRpath(String rpath) {
        this.rpath = rpath;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
