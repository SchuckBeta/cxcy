package com.oseasy.act.modules.actyw.web;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.common.config.ActSval.ActEmskey;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.act.utils.ActUtils;
import com.oseasy.act.modules.act.utils.ProcessMapUtil;
import com.oseasy.act.modules.act.vo.ProcessMapVo;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * Created by zhangzheng on 2017/3/23.
 */
@Controller
@RequestMapping(value = "${frontPath}/act/task")
public class FrontActTaskContoller extends BaseController {
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;

    //获取跟踪信息
    @RequestMapping(value = "processMapByType")
    public String processMap(String proInsId, String type, String status, Model model) {
      ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInsId, type, status);
      model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
      model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
      model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());
      return ActSval.path.vms(ActEmskey.ACT.k()) + "frontActTaskMap";
    }

    //获取跟踪信息
    @RequestMapping(value = "processMap")
    public String processMap(String proInsId, Model model) {
      ProcessMapVo vo = ProcessMapUtil.processMap(repositoryService, actTaskService, runtimeService, proInsId, null, null);
      model.addAttribute(ProcessMapVo.PM_PROC_DEF_ID, vo.getProcDefId());
      model.addAttribute(ProcessMapVo.PM_PRO_INST_ID, vo.getProInstId());
      model.addAttribute(ProcessMapVo.PM_ACT_IMPLS, vo.getActImpls());
        return ActSval.path.vms(ActEmskey.ACT.k()) + "frontActTaskMap";
    }

    @RequestMapping(value = "processPic")
    public void processPic(String procDefId, HttpServletResponse response) throws Exception {
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
        String diagramResourceName = procDef.getDiagramResourceName();
        InputStream imageStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), diagramResourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    @RequestMapping(value = "auditform")
    public String auditform(Act act, HttpServletRequest request, Model model) {
        // 获取流程XML上的表单KEY
        String formKey= "/promodel/proModel/auditForm";
//		String	urlPath=request.getParameter("path");
        String	pathUrl=request.getParameter("pathUrl");
        String	gnodeId=request.getParameter(ActYwGroup.JK_GNODE_ID);
        String	taskName=request.getParameter("taskName");
        if (StringUtils.isNotBlank(taskName)) {
            try {
                taskName = URLEncoder.encode(taskName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //DO nothing
            }
        }
        String	proModelId=request.getParameter("proModelId");
        // 获取流程实例对象
        if (act.getProcInsId() != null) {
            act.setProcIns(actTaskService.getProcIns(act.getProcInsId()));
        }
        String url="";
        if (proModelId!=null) {
            StringBuilder formUrl = new StringBuilder();

            String formServerUrl = CoreSval.getConfig("activiti.form.server.url");
            if (StringUtil.isBlank(formServerUrl)) {
                formUrl.append(CoreSval.getAdminPath());
            }else{
                formUrl.append(formServerUrl);
            }

            url=formUrl.toString()+formKey+"?actionPath="+pathUrl+"&gnodeId="+gnodeId+"&proModelId="+proModelId + "&taskName=" + taskName;
        }else{
            url= ActUtils.getFormUrl(formKey, act);
            url=url+"&actionPath="+pathUrl+"&gnodeId="+gnodeId + "&taskName=" + taskName;
        }
        return CoreSval.REDIRECT + url;
    }

}
