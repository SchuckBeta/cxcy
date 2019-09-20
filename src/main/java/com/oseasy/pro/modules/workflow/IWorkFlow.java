package com.oseasy.pro.modules.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.pro.modules.promodel.entity.ProRole;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.pro.common.utils.EasyPoiUtil;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import net.sf.json.JSONObject;

/**
 * 新增一套全新的自定义流程需要实现的方法.
 * @author chenhao
 *
 * @param <T>
 * @param <R>
 */
public interface IWorkFlow<T extends IWorkRes, PM extends IWorkFety, R extends IWorkRes> {
    public final static Logger logger = Logger.getLogger(IWorkFlow.class);

    /**
     * 工作流后台列表查询方法（查询流程具体节点）
     * @param page 查询参数
     * @param actywId
     * @param gnodeId
     * @param actYw
     * @param act
     * @param model
     * @return
     */
    Page<T> findDataPage(Page<T> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, T t);

    /**
     * 项目查询（查询所有）
     * @param page
     * @param actywId
     * @param actYw
     * @return
     */
    Page<T> findQueryPage(Page<T> page, Model model, String actywId, ActYw actYw, T t);

    /**
     *
     * @param page
     * @param actywId
     * @param actYw
     * @return
     */
    Page<T> findAssignPage(Page<T> page, Model model, String actywId, ActYw actYw, T t);

    /**
     * 跳转申报表单
     * @param fpageType
     * @param model
     * @param request
     * @param response
     * @param proModel
     * @param proProject
     * @param actYw
     * @return
     */
    String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, PM pm, ProProject proProject, ActYw actYw);

    /**
     * 跳转查看详情页面
     * @param fpageType
     * @param model
     * @param request
     * @param response
     * @param proModel
     * @param actYw
     * @return
     */
    String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, PM pm, ActYw actYw);

    /**
     * 审核
     * @param proModelId
     * @param model
     */
    void audit(String gnodeId,String proModelId, Model model);

    void audit(T t, Model model, SysAttachment attachment);

    /**
     * 获取泛型类
     * @return
     */
    Class<? extends IWorkRes> getTClass();

    void saveAddPro(PM pm);

    void auditByGateWay(PM pm, String gnodeId, HttpServletRequest request);

    void gcontestEdit(PM pm, HttpServletRequest request,Model model);

    void projectEdit(PM pm, HttpServletRequest request,Model model);

    void saveFormData(PM pm, HttpServletRequest request);

    /**
     * 获取导出数据.
     * @param page
     * @param t
     * @return List
     */
    List<R> exportData(Page<T> page, T t);

    /**
     * 根据数据生成导出文件.
     * @param request
     * @param response
     * @param tempPath
     * @param gnode
     * @param pids
     * @param t
     */
    void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, T t);

    /**
     * 根据数据生成导出文件_查询(查询列表专用).
     * @param request
     * @param response
     * @param t
     */
    void exportDataQuery(HttpServletRequest request, HttpServletResponse response, T t);


    /**
     * 自定义流程导出数据及附件的处理规则.
     * @param wres
     * @return
     */
    public static Map<String, List<IWorkRes>> exportDataMap(List<? extends IWorkRes> wres) {
        logger.info("开始：exportDataMap(List<? extends IWorkRes> wres)！");
        Map<String, List<IWorkRes>> map = new HashMap<>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
        if (StringUtil.checkNotEmpty(wres)) {
            for (IWorkRes pm : wres) {
                String officeName = pm.getOfficeName();
                if (map.containsKey(officeName)) {
                    List<IWorkRes> pmvos = map.get(officeName);
                    pmvos.add(pm);
                    map.put(officeName, pmvos);
                } else {
                    List<IWorkRes> pmvos = new ArrayList<>();
                    pmvos.add(pm);
                    map.put(officeName, pmvos);
                }
            }
        }
        logger.info("完成：exportDataMap(List<? extends IWorkRes> wres)！");
        return map;
    }

    /**
     * 自定义流程导出Excel学院申报汇总表的方法.
     * @param expGfile 导出Excel信息
     */
    public static boolean expExcelByOs(ExpGnodeFile expGfile, List<IWorkRes> wres) {
        if((expGfile == null) || (expGfile == null)){
            logger.error("导出模板参数未定义");
            return false;
        }

        File dirFile = new File(expGfile.getRpath());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        Map<String, Object> map = new HashMap<>();
        try {
            if((expGfile == null) || (expGfile.getParam() == null)){
                return false;
            }
            ExportParams params = expGfile.getParam();
            if(StringUtil.isEmpty(params.getSecondTitle())){
                logger.warn("Excel 的当前子标题为空！");
            }
            System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
            logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

            File file = new File(expGfile.getPath());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            if(StringUtil.isEmpty(expGfile.getOname())){
                map.put(NormalExcelConstants.FILE_NAME, expGfile.getFileName() + expGfile.getFileType());
            }else{
                map.put(NormalExcelConstants.FILE_NAME, expGfile.getFileName() + expGfile.getOname() + expGfile.getFileType());
            }
            map.put(NormalExcelConstants.PARAMS, params);
            map.put(NormalExcelConstants.DATA_LIST, wres);
            map.put(NormalExcelConstants.CLASS, expGfile.getClazz());
            if((StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS).equals(expGfile.getFileType())){
                params.setType(ExcelType.HSSF);
            }else{
               params.setType(ExcelType.XSSF);
            }
            ExcelExportUtil.exportExcel(params, expGfile.getClazz(), wres).write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 自定义流程查询列表导出Excel汇总表的方法.
     * @param expGfile 导出Excel信息
     */
    public static boolean expRenderView(HttpServletRequest request, HttpServletResponse response, ExpGnodeFile expGfile, List<? extends IWorkRes> wres) {
        if((expGfile == null) || (expGfile == null)){
            logger.error("导出模板参数未定义");
            return false;
        }

        Map<String, Object> map = new HashMap<>();
        if((expGfile == null) || (expGfile.getParam() == null)){
            return false;
        }
        ExportParams params = expGfile.getParam();
        if(StringUtil.isEmpty(params.getSecondTitle())){
            logger.warn("Excel 的当前子标题为空！");
        }
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

        if(StringUtil.isEmpty(expGfile.getOname())){
            map.put(NormalExcelConstants.FILE_NAME, expGfile.getFileName());
        }else{
            map.put(NormalExcelConstants.FILE_NAME, expGfile.getFileName() + expGfile.getOname());
        }

        params.setType(ExcelType.XSSF);
        params.setStyle(EasyPoiUtil.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.DATA_LIST, wres);
        map.put(NormalExcelConstants.CLASS, expGfile.getClazz());
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(expGfile.getFileName()));
//        response.setContentType("application/vnd.ms-excel");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
        return true;
    }

//    public void expRenderViewXxx(HttpServletResponse response, ExpGnodeFile expGfile, List<? extends IWorkRes> wres) {
//        ServletOutputStream out = null;
//        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
//        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));
//        try {
//            String fileName = actYw.getProProject().getProjectName();
//            String titleName=new String (fileName);
//            if(StringUtil.isNotBlank(gnodeId)){
//                ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
//                titleName=titleName+actYwGnode.getName();
//                fileName += "_" + actYwGnode.getName();
//            }else{
//                fileName += "_项目查询";
//                titleName=titleName+"信息表";
//            }
//            fileName += "_" + DateUtil.getDate("yyyyMMddHHmmss") + ".xlsx";
//            String header = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", header);
//            out = response.getOutputStream();
//            WriteExcel(list, titleName).write(out);
//        } catch (IOException e) {
//            logger.error(ExceptionUtil.getStackTrace(e));
//        } finally {
//            IOUtils.closeQuietly(out);
//        }
//    }

   /**
    * 默认导出模板命名规则.
    * @param request
    * @param response
    * @param actYw
    * @param actYwGnode
    */
   public static boolean expRenderFile(HttpServletRequest request, HttpServletResponse response, List<? extends IWorkRes> wres, ActYw actYw, ActYwGnode actYwGnode) {
       return expRenderFile(request, response, wres, actYw, actYwGnode, null);
   }
   /**
    * 使用默认模板ProModelMdGcVo生成Excel.
    * @param request
    * @param response
    * @param wres
    * @param actYw
    * @param actYwGnode
    * @param expGfile
    */
   public static boolean expRenderFile(HttpServletRequest request, HttpServletResponse response, List<? extends IWorkRes> wres, ActYw actYw, ActYwGnode actYwGnode, ExpGnodeFile expGfile) {
       if(expGfile == null){
           expGfile = new ExpGnodeFile();
           expGfile.setClazz(ExpProModelVo.class);
       }
       if(expGfile.getClazz() == null){
           logger.warn("导出模板或模板类不能为空！");
           return false;
       }
       expGfile.setFileName(actYw.getProProject().getProjectName());
       String titleName = new String (expGfile.getFileName());
       if(actYwGnode != null){
           titleName=titleName+actYwGnode.getName();
           expGfile.setFileName(expGfile.getFileName() + StringUtil.LINE_D + actYwGnode.getName());
       }else{
           expGfile.setFileName(expGfile.getFileName() + StringUtil.LINE_D + "项目查询");
           titleName = titleName + "信息表";
       }
       if(StringUtil.isEmpty(expGfile.getSheetName())){
           expGfile.setSheetName("项目信息-v1");
       }
       if(expGfile.getHasTimeid()){
           expGfile.setFileName(expGfile.getFileName() + StringUtil.LINE_D + DateUtil.getDate(DateUtil.FMT_YYYY_MM_DD_HHmmss));
       }

       ExportParams eparams = null;
       if(expGfile.getParam() != null){
           eparams = expGfile.getParam();
           eparams.setTitle(expGfile.getFileName());
           eparams.setSecondTitle(titleName);
           eparams.setSheetName(expGfile.getSheetName());
       }else{
           eparams = new ExportParams(expGfile.getFileName(), titleName, expGfile.getSheetName());
       }
       if(expGfile.getParam().getDictHandler() == null){
           eparams.setDictHandler(new DictHandler());
       }else{
           eparams.setDictHandler(expGfile.getParam().getDictHandler());
       }

       if(expGfile.getParam().getDataHandler() == null){
           eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
       }else{
           eparams.setDataHandler(expGfile.getParam().getDataHandler());
       }

       if(expGfile.getHideTitle()){
           eparams.setTitle(null);
           eparams.setSecondTitle(null);
       }
       expGfile.setParam(eparams);
       expRenderView(request, response, expGfile, wres);
       return true;
    }

   public static void toExcel(HttpServletResponse response, ExpGnodeFile expGfile, List<IWorkRes> wres){
        ServletOutputStream out = null;
        try {
            String fileName = expGfile.getFileName();
            String titleName= expGfile.getParam().getSecondTitle();
            fileName += "_" + DateUtil.getDate("yyyyMMddHHmmss") + ".xlsx";
            String header = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", header);
            out = response.getOutputStream();
            //WriteExcel(wres, titleName).write(out);
        } catch (IOException e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
    JSONObject submit(PM pm, JSONObject js);
    @Transactional(readOnly = false)
    JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) throws Exception;

    void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId);

    void addFrontParam(ProjectDeclareListVo v);

    void indexTime(Map<String, String> lastpro);

    public static ProModel initRnames(List<ProRole> proRoles, ProModel model) {
        StringBuffer currname = null;
        for (ProRole cur: proRoles) {
            if(!(cur.getPmId()).equals(model.getId())){
                continue;
            }
            if(currname == null){
                currname = new StringBuffer();
                currname.append(cur.getName());
            }else{
                currname.append(StringUtil.DOTH);
                currname.append(cur.getName());
            }
        }
        if(currname != null){
            model.setRnames(currname.toString());
        }
        return model;
    }
}
