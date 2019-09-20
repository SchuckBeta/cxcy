package com.oseasy.pro.modules.workflow.utils;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.IWorkFety;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelHsxm;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.service.ProDeclareService;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.pro.modules.workflow.service.ProModelHsxmService;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.util.common.utils.StringUtil;

public class WorkFlowUtil {
    public final static Logger logger = Logger.getLogger(WorkFlowUtil.class);

    @SuppressWarnings("rawtypes")
    private static Map<String, IWorkFlow> workFlowMap = SpringContextHolder.getApplicationContext().getBeansOfType(IWorkFlow.class);

    @SuppressWarnings("unchecked")
    public static IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> getWorkFlowService(String serviceName) {
        return workFlowMap.get(serviceName);
    }

    public static IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> getWorkFlowService(ActYw actYw) {
        if(actYw == null){
            return null;
        }
        String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
        return getWorkFlowService(serviceName);
    }


    /**
     * 根据不同的服务生成对应的Zip文件目录结构和数据汇总Excel.
     * @param request
     * @param response
     * @param actYw
     * @param tempPath
     * @param pids
     * @param gnode
     */
    public static boolean genZipByService(HttpServletRequest request, HttpServletResponse response, ActYw actYw, String tempPath,
            List<String> pids, ActYwGnode gnode) {
        IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> workFlow = WorkFlowUtil.getWorkFlowService(actYw);
        if (workFlow == null) {
            logger.error("当前服务workFlow为空！");
            return false;
        }

        /**
         * 按学院生成附件.
         */
        if((workFlow.getClass().getSimpleName()).startsWith(ProModelService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelService.class.getSimpleName()))){
            logger.error("当前服务：ProModelService");
            ((ProModelService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModel)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProModelMdService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelMdService.class.getSimpleName()))){
            logger.error("当前服务：ProModelMdService");
            ((ProModelMdService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModelMd)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProModelGzsmxxService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelGzsmxxService.class.getSimpleName()))){
            logger.error("当前服务：ProModelGzsmxxService");
            ((ProModelGzsmxxService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModelGzsmxx)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProModelMdGcService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelMdGcService.class.getSimpleName()))){
            logger.error("当前服务：ProModelMdGcService");
            ((ProModelMdGcService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModelMdGc)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProModelTlxyService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelTlxyService.class.getSimpleName()))){
            logger.error("当前服务：ProModelTlxyService");
            ((ProModelTlxyService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModelTlxy)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProModelHsxmService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProModelHsxmService.class.getSimpleName()))){
            logger.error("当前服务：ProModelHsxmService");
            ((ProModelHsxmService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProModelHsxm)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else if((workFlow.getClass().getSimpleName()).startsWith(ProDeclareService.class.getSimpleName() + StringUtil.DDL) || ((workFlow.getClass().getSimpleName()).equals(ProDeclareService.class.getSimpleName()))){
            logger.error("当前服务：ProDeclareService");
            ((ProDeclareService)workFlow).exportData(request, response, tempPath, actYw, gnode, pids, (ProjectDeclare)WorkFlowUtil.getParameters(request.getParameterMap(), workFlow));
        }else{
            logger.info("当前服务未定义！");
            return false;
        }
        return true;
    }

    public static IWorkRes getParameters(Map<?, ?> parameters, IWorkFlow<? extends IWorkRes, ? extends IWorkFety, ? extends IWorkRes> workFlow) {
        JSONObject gzs = new JSONObject();
        parameters.forEach((key, value) -> {
            String keyStr = key.toString();
            String[] Keys = keyStr.split("\\.");
            if (Keys.length > 1) {
                JSONObject j = gzs;
                for (int i = 0; i< Keys.length; i++) {
                    if (j.containsKey(Keys[i])){
                        if (i == Keys.length -1) {
                            j.put(Keys[i], ((String[])value)[0]);
                        } else if(i == 0) {
                            j = gzs.getJSONObject(Keys[i]);
                        } else {
                            j = gzs.getJSONObject(Keys[i]);
                            j.put(Keys[i], new JSONObject());
                        }
                    } else {
                        if (i == Keys.length -1) {
                            j.put(Keys[i], ((String[])value)[0]);
                        } else if(i == 0) {
                            j.put(Keys[i], new JSONObject());
                            j = j.getJSONObject(Keys[i]);
                        } else {
                            j.put(Keys[i], new JSONObject());
                            j = j.getJSONObject(Keys[i]);
                        }
                    }
                }
            } else {
                gzs.put(keyStr, ((String[])value)[0]);
            }
        });
        if (StringUtils.isEmpty(workFlow)) {
            return JSONObject.toJavaObject(gzs, ProModel.class);
        }
        Class<?> clazz = workFlow.getTClass();
        return (IWorkRes) JSONObject.toJavaObject(gzs, clazz);
    }
}
