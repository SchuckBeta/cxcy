/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

/**
 * 抽象模板.
 * @author chenhao
 */
public abstract class IeAbsTpl {
    protected String rootPath;// 模板文件根路径
    protected String path;// 模板文件全路径
    protected String fname;// 模板文件下载后的显示名称
    protected String tplName;// 模板文件名称
    protected Object wb;// 当前WB对象
    protected Object cur;// 当前对象

    public Object getCur() {
        return cur;
    }
    public void setCur(Object cur) {
        this.cur = cur;
    }
    public Object getWb() {
        return wb;
    }
    public void setWb(Object wb) {
        this.wb = wb;
    }
    public String getRootPath() {
        return rootPath;
    }
    public String getFname() {
        return fname;
    }
    public String getTplName() {
        return tplName;
    }
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public void setTplName(String tplName) {
        this.tplName = tplName;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
