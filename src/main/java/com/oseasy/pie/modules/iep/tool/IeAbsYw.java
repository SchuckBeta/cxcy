/**
 * .
 */

package com.oseasy.pie.modules.iep.tool;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.IeYw;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IGnode;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.impl.IeRpm;
import com.oseasy.pie.modules.iep.tool.impl.IeRpmFlow;
import com.oseasy.pie.modules.iep.tool.impl.IeTpl;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.pie.modules.iep.vo.TplOperType;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 抽象业务.
 * @author chenhao
 */
public abstract class IeAbsYw {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected IeYw ieYw;//业务实体
    protected IeAbsTpl tpl;//模板
    protected IePparam rpparam;//请求参数
    protected IeQparam rqparam;//响应参数
    public IeAbsYw() {
        super();
    }

    public IeAbsYw(IepTpl iepTpl, HttpServletRequest request, HttpServletResponse response) {
        super();
        this.request = request;
        this.response = response;
        if((TplType.MR.getKey()).equals(iepTpl.getType())){
            IeRpmFlow rpmFlow = new IeRpmFlow();
            rpmFlow.setId(request.getParameter(rpmFlow.getkey()));
            rpmFlow.setOperType(request.getParameter(TplOperType.TPL_OPERTYPE));
            rpmFlow.setActywId(request.getParameter(IActYw.IACTYW_ID));
            rpmFlow.setGnodeId(request.getParameter(IGnode.IGNODE_ID));
            rpmFlow.setIepId(iepTpl.getId());
            this.rpparam = rpmFlow;
        }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
            IeRpmFlow rpmFlow = new IeRpmFlow();
            rpmFlow.setId(request.getParameter(rpmFlow.getkey()));
            rpmFlow.setOperType(request.getParameter(TplOperType.TPL_OPERTYPE));
            rpmFlow.setActywId(request.getParameter(IActYw.IACTYW_ID));
            rpmFlow.setGnodeId(request.getParameter(IGnode.IGNODE_ID));
            rpmFlow.setIepId(iepTpl.getId());
            this.rpparam = rpmFlow;
        }else{
            IeRpm rpmFlow = new IeRpm();
            rpmFlow.setId(request.getParameter(rpmFlow.getkey()));
            rpmFlow.setOperType(request.getParameter(TplOperType.TPL_OPERTYPE));
            rpmFlow.setIepId(iepTpl.getId());
            this.rpparam = rpmFlow;
        }
    }
    /************************************************************************************
     *检查导入导出信息
     ***********************************************************************************/
    /**
     * 检查上传附件是否正确.
     * @param iepTpl 模板配置对象
     * @param IeAbsYw 业务对象
     * @return Rtstatus
     */
    public static ApiTstatus<Object> checkUpload(IepTpl iepTpl, IeAbsYw yw) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) yw.getRequest();
        // 读取上传的文件内容
        List<MultipartFile> imgFiles = multipartRequest.getFiles(FileUtil.FILE_NAME);

        if (StringUtil.checkEmpty(imgFiles)) {
            return new ApiTstatus<Object>(false, "上传失败", imgFiles);
        }

        List<String> filters = Lists.newArrayList();
        filters.add(TplFType.ZIP.getPostfix());
        if(!IeAbsYw.checkHasFtype(iepTpl, imgFiles, filters)){
            return new ApiTstatus<Object>(false, "校验失败，没有对应格式的文件");
        }
        return new ApiTstatus<Object>(true, "成功", imgFiles);
    }

    /**
     * 检查上传附件的文件类型是否与模板一致.
     * @param iepTpl 模板配置对象
     * @param imgFiles 附件
     * @return boolean
     */
    public static boolean checkHasFtype(IepTpl iepTpl, List<MultipartFile> imgFiles, List<String> filters) {
        if(filters == null){
            filters = Lists.newArrayList();
        }

        TplFType ftype = TplFType.getByKey(iepTpl.getFtype());
        if(ftype == null){
            return false;
        }
        filters.add(ftype.getPostfix());
        if(StringUtil.checkNotEmpty(iepTpl.filters())){
            filters.addAll(iepTpl.filters());
        }

        boolean hasFile = false;
        for (MultipartFile imgFile : imgFiles) {
            String fname=imgFile.getOriginalFilename().toLowerCase();

            for (String filter : filters) {
                if(fname.endsWith(filter)){
                    hasFile = true;
                    break;
                }
            }

            if(hasFile){
                break;
            }
        }
        return hasFile;
    }

    /**
     * 初始化IeTpl模板属性.
     * @param iepTpl IepTpl
     * @param yw IeAbsYw
     * @param ii 信息
     * @param wb 文档
     * @return
     */
    public static IeAbsYw initTpl(IepTpl iepTpl, IeAbsYw yw, ImpInfo ii, XSSFWorkbook wb) {
        IeTpl absTpl = new IeTpl(iepTpl, SpringContextHolder.getWebPath(yw.getRequest()));
        if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype())){
            absTpl.setWb(wb);
            absTpl.setCur(wb.getSheetAt(0));
            //ii.setTotal((((XSSFSheet)absTpl.getWb()).getLastRowNum() - iepTpl.getDstartRow()) + "");
            yw.setTpl(absTpl);
            ii.setIepType(iepTpl.getType());
            ii.setImpTpye(iepTpl.getId());
            yw.getRpparam().setIi(ii);
         }else if((TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
             absTpl.setWb(wb);
             absTpl.setCur(wb.getSheetAt(0));
//           absTpl.setCur(wb);
//           ii.setTotal(((((XSSFWorkbook) absTpl.getCur()).getSheetAt(0)).getLastRowNum() - iepTpl.getDstartRow()) + "");
             yw.setTpl(absTpl);
             ii.setIepType(iepTpl.getType());
             ii.setImpTpye(iepTpl.getId());
             yw.getRpparam().setIi(ii);
         }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
         }else {
         }
         return yw;
    }
    /************************************************************************************
     *GET/SET
     ***********************************************************************************/
    public IeYw getIeYw() {
        return ieYw;
    }
    public IeAbsTpl getTpl() {
        return tpl;
    }
    public void setTpl(IeAbsTpl tpl) {
        this.tpl = tpl;
    }
    public void setIeYw(IeYw ieYw) {
        this.ieYw = ieYw;
    }
    public IePparam getRpparam() {
        return rpparam;
    }
    public void setRpparam(IePparam rpparam) {
        this.rpparam = rpparam;
    }
    public IeQparam getRqparam() {
        return rqparam;
    }
    public void setRqparam(IeQparam rqparam) {
        this.rqparam = rqparam;
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
