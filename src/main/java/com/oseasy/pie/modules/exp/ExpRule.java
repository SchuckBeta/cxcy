/**
 * .
 */

package com.oseasy.pie.modules.exp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.pro.modules.promodel.vo.ExpRparam;
import com.oseasy.pro.modules.promodel.vo.ExpSfile;
import com.oseasy.pro.modules.workflow.IExpGfile;

/**
 * .
 * @author chenhao
 *
 */
public class ExpRule<T extends ExpRparam> extends ExpSfile implements IExpGfile{
    private Class<?> clazz;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private List datas;
    private String fileName;//文件名称
    private String sheetName;//Sheet名称
    private T rparam;//导入导出请求参数

    public ExpRule() {
        super();
    }

    public ExpRule(T rparam) {
        super();
        this.rparam = rparam;
    }

    @Override
    public String getPath() {
        return null;
    }

    public List getDatas() {
        return datas;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public T getRparam() {
        return rparam;
    }

    public void setRparam(T rparam) {
        this.rparam = rparam;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    public HttpServletResponse getResponse() {
        return response;
    }
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
