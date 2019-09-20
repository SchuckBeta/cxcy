/**
 * .
 */

package com.oseasy.pro.modules.workflow.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.FormPageType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.pro.modules.project.dao.ProjectDeclareDao;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.WorkFlowService;
import com.oseasy.pro.modules.workflow.handler.DataExpPmTlxyVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.vo.ExpProModelTlxyVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import net.sf.json.JSONObject;

/**
 * .
 * @author chenhao
 */
//@Service
@Transactional(readOnly = true)
public class ProDeclareService implements IWorkFlow<ProjectDeclare, ProjectDeclare, ExpProModelVo> {
    public final static Logger logger = Logger.getLogger(ProDeclareService.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static ProjectDeclareDao projectDeclareDao = SpringContextHolder.getBean(ProjectDeclareDao.class);
    private static ProjectDeclareService projectDeclareService = SpringContextHolder.getBean(ProjectDeclareService.class);
    @Override
    public Page<ProjectDeclare> findDataPage(Page<ProjectDeclare> page, Model model, String actywId, String gnodeId,
            ActYw actYw, Act act, ProjectDeclare t) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Page<ProjectDeclare> findQueryPage(Page<ProjectDeclare> page, Model model, String actywId, ActYw actYw,
            ProjectDeclare t) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Page<ProjectDeclare> findAssignPage(Page<ProjectDeclare> page, Model model, String actywId, ActYw actYw,
            ProjectDeclare t) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request,
            HttpServletResponse response, ProjectDeclare pm, ProProject proProject, ActYw actYw) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request,
            HttpServletResponse response, ProjectDeclare pm, ActYw actYw) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void audit(String gnodeId, String proModelId, Model model) {
        // TODO Auto-generated method stub

    }
    @Override
    public void audit(ProjectDeclare t, Model model, SysAttachment attachment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveAddPro(ProjectDeclare pm) {
        // TODO Auto-generated method stub

    }
    @Override
    public void auditByGateWay(ProjectDeclare pm, String gnodeId, HttpServletRequest request) {
        // TODO Auto-generated method stub

    }
    @Override
    public void gcontestEdit(ProjectDeclare pm, HttpServletRequest request, Model model) {
        // TODO Auto-generated method stub

    }
    @Override
    public void projectEdit(ProjectDeclare pm, HttpServletRequest request, Model model) {
        // TODO Auto-generated method stub

    }
    @Override
    public void saveFormData(ProjectDeclare pm, HttpServletRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public JSONObject submit(ProjectDeclare pm, JSONObject js) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public JSONObject saveProjectEdit(ProModel proModel, HttpServletRequest request) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {

    }

    @Override
    public void addFrontParam(ProjectDeclareListVo v) {

    }

    @Override
    public void indexTime(Map<String, String> lastpro) {

    }


    @Override
    public Class<? extends IWorkRes> getTClass() {
        return ProjectDeclare.class;
    }

    @Override
    public List<ExpProModelVo> exportData(Page<ProjectDeclare> page, ProjectDeclare projectDeclare) {
        projectDeclare.setPage(page);
        try {
            return projectDeclareDao.export(projectDeclare);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw,
            ActYwGnode gnode, List<String> pids, ProjectDeclare projectDeclare) {
        projectDeclare.setIds(pids);
        projectDeclare.setActywId(actyw.getId());
        //大创项目
        Page<ProjectDeclare> page = new Page<ProjectDeclare>(request, response, Page.MAX_PAGE_SIZE);
        Map<String, List<IWorkRes>> map = IWorkFlow.exportDataMap(exportData(page, projectDeclare));
        if(map == null){
            logger.warn("exportDataMap 方法处理异常，查询结果不能为空！");
            return;
        }
        String fileName = "";
        if((actyw.getProProject() != null) && StringUtil.isNotEmpty(actyw.getProProject().getProjectName())){
            fileName = actyw.getProProject().getProjectName();
        }
        if((gnode != null) && StringUtil.isNotEmpty(gnode.getName())){
            fileName += StringUtil.LINE_D + gnode.getName();
        }
        for (String key : map.keySet()) {
            // 按学院名称生成项目审核信息
            ExpGnodeFile expGfile = new ExpGnodeFile(tempPath + File.separator + gnode.getName() + File.separator + key, key, fileName);
            expGfile.setFileName(fileName + StringUtil.LINE_D + "大赛汇总表");
            expGfile.setClazz(ExpProModelTlxyVo.class);
            expGfile.setReqParam(new ItReqParam(request));
            ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛汇总", expGfile.getFileType());
            eparams.setDictHandler(new DictHandler());
            eparams.setDataHandler(new DataExpPmTlxyVoHandler(expGfile.getReqParam()));
            expGfile.setParam(eparams);
            IWorkFlow.expExcelByOs(expGfile, map.get(key));
        }
    }

    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProjectDeclare t) {
        // TODO Auto-generated method stub
    }
}