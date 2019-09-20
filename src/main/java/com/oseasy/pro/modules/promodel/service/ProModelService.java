package com.oseasy.pro.modules.promodel.service;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.google.common.collect.Lists;
import com.oseasy.act.common.config.ActSval;
import com.oseasy.act.modules.act.dao.ActDao;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.exception.ApplyException;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.service.*;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.*;
import com.oseasy.act.modules.actyw.utils.ActYwUtils;
import com.oseasy.act.modules.actyw.vo.ActYwRuntimeException;
import com.oseasy.act.modules.actyw.vo.EarAtype;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiProModel;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.service.SysAttachmentService;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.AttachMentEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.common.service.CommonService;
import com.oseasy.pro.jobs.pro.ProModelTopic;
import com.oseasy.pro.modules.cert.dao.SysCertInsDao;
import com.oseasy.pro.modules.cert.service.ProSysAttachmentService;
import com.oseasy.pro.modules.cert.vo.SysCertInsVo;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProModelNodeVo;
import com.oseasy.pro.modules.project.vo.ProjectDeclareListVo;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.*;
import com.oseasy.pro.modules.promodel.tool.process.vo.FormPage;
import com.oseasy.pro.modules.promodel.utils.ProProcessDefUtils;
import com.oseasy.pro.modules.promodel.vo.*;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.tpl.vo.IWparam;
import com.oseasy.pro.modules.tpl.vo.Wtype;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.entity.ProvinceProModel;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pro.modules.workflow.service.ProvinceProModelService;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.utils.IdUtils;
import com.oseasy.sys.modules.sys.service.BackTeacherExpansionService;
import com.oseasy.sys.modules.sys.utils.NumRuleUtils;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserChangeService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import net.sf.json.JSONObject;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * proModelService.
 *
 * @author zy
 * @version 2017-07-13
 */
@Service
@Transactional(readOnly = false)
public class ProModelService extends CrudService<ProModelDao, ProModel> implements IWorkFlow<ProModel, ProModel, ExpProModelVo> {
    @Override
    public void addFrontParam(ProjectDeclareListVo v) {}

    @Override
    public void indexTime(Map<String, String> lastpro) {}
    @Autowired
    private ProModelDao proModelDao;
    @Autowired
    private SysCertInsDao sysCertInsDao;
    @Autowired
    ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    ProActTaskService proActTaskService;
    @Autowired
    ActYwService actYwService;
    @Autowired
    ActYwGnodeService actYwGnodeService;
    @Autowired
    ProActYwGnodeService proActYwGnodeService;
    @Autowired
    ActYwStatusService actYwStatusService;
    @Autowired
    UserService userService;
    @Autowired
    SystemService systemService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    IdentityService identityService;
    @Autowired
    TeamUserChangeService teamUserChangeService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActDao actDao;
    @Autowired
    private ProSysAttachmentService proSysAttachmentService;
    @Autowired
    TeamUserHistoryService teamUserHistoryService;
    @Autowired
    private TeamUserHistoryDao teamUserHistoryDao;
    @Autowired
    private SysAttachmentService sysAttachmentService;
//    @Autowired
//    ScoAffirmDao scoAffirmDao;
//    @Autowired
//    ScoScoreDao scoScoreDao;
//    @Autowired
//    ScoAllotRatioDao scoAllotRatioDao;
//    @Autowired
//    ScoAffirmCriterionDao scoAffirmCriterionDao;
    @Autowired
    ProjectDeclareService projectDeclareService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ProMidSubmitService proMidSubmitService;
    @Autowired
    private ProReportService proReportService;
    @Autowired
    private ProCloseSubmitService proCloseSubmitService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private ActYwGassignService actYwGassignService;
    @Autowired
    private ProModelMdService proModelMdService;
    @Autowired
    private  ActYwEtAssignRuleService actYwEtAssignRuleService;
    @Autowired
    private  BackTeacherExpansionService backTeacherExpansionService;
    @Autowired
    private  ActYwEtAuditNumService actYwEtAuditNumService;
    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private CoreService coreService;

    public final static Logger logger = Logger.getLogger(ProModelService.class);
    public static final String ExpGcontestTitle = "大学生创新创业大赛报名信息汇总表";

    public boolean existProName(ActYw ay, String name) {
        if (ay == null || StringUtil.isEmpty(name)) {
            return true;
        }
        Integer i = dao.getCountByName(ay.getProProject().getProType(), ay.getProProject().getType(), name);
        if (i != null && i > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = false)
    public void updateSubStatus(String pid, String subStatus) {
        dao.updateSubStatus(pid, subStatus);
    }

    /**
     * 删除自定义项目、大赛预发布所有项目和流程数据 (业务表逻辑删除) (工作流数据api删除)
     *
     * @param actywid
     */
    @Transactional(readOnly = false)
    public void clearPreReleaseData(String actywid) {
        //删除流程数据
        List<String> list = dao.getProcinsidByActywid(actywid);
        if (list != null && list.size() > 0) {
            for (String proins : list) {
                try {
                    runtimeService.deleteProcessInstance(proins, "预发布项目删除");
                } catch (Exception e) {
                    //流程已结束的会删除失败，无需处理
                    logger.error("预发布项目流程运行数据删除失败");
                }
                historyService.deleteHistoricProcessInstance(proins);
            }
        }
        //删除项目、大赛信息表数据
        dao.deleteByActywid(actywid);
        //删除项目、大赛人员信息表数据
        dao.deleteTeamUserHisByActywid(actywid);
        //删除报告信息表数据
        dao.deleteReportByActywid(actywid);
    }

    //大赛信息导出
    public void expGcontestData(ProModel param, HttpServletRequest request, HttpServletResponse response) {
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            // excel模板路径
            ActYw actYw = actYwService.get(param.getActYwId());
            String year = "";
            if (StringUtil.isNotEmpty(param.getYear())) {
                year = param.getYear() + "年";
            }
            String gtype = "";
            if (actYw != null && actYw.getProProject() != null) {
                gtype = DictUtils.getDictLabel(actYw.getProProject().getType(), "competition_type", "");
            }
            String fileName = year + gtype + ExpGcontestTitle + ".xlsx";
            String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + "exp_promodel_gcontest.xlsx");
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            Map<String, GcontestExpData> m1 = new LinkedHashMap<String, GcontestExpData>();
            Map<String, List<GcontestStuExpData>> m2 = new HashMap<String, List<GcontestStuExpData>>();
            Map<String, List<GcontestTeaExpData>> m3 = new HashMap<String, List<GcontestTeaExpData>>();
            getGcontestDataForExp(actYw, param, m1, m2, m3);
            if (!m1.isEmpty()) {
                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                rowStyle.setAlignment(HorizontalAlignment.CENTER);
                rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));
                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + gtype + ExpGcontestTitle);
                int row0 = 4;//当前项目起始行
                int prorows = 1;//每个项目占几行
                for (String pid : m1.keySet()) {
                    //算项目行数
                    List<GcontestStuExpData> l2 = m2.get(pid);
                    if (l2 != null && l2.size() > prorows) {
                        prorows = l2.size();
                    }
                    List<GcontestTeaExpData> l3 = m3.get(pid);
                    if (l3 != null && l3.size() > prorows) {
                        prorows = l3.size();
                    }
                    //创建行
                    ExcelUtils.creatRow(sheet0, row0, prorows);
                    XSSFRow row = sheet0.getRow(row0);
                    GcontestExpData ged = m1.get(pid);
                    row.createCell(0).setCellValue(ged.getIndxnum() + "");
                    row.createCell(1).setCellValue(ged.getName());
                    row.createCell(2).setCellValue(ged.getType());
                    row.createCell(3).setCellValue(ged.getGroupName());
                    row.createCell(4).setCellValue(ged.getLeaderName());
                    row.createCell(5).setCellValue(ged.getLeaderEnter());
                    row.createCell(6).setCellValue(ged.getLeaderPro());
                    row.createCell(7).setCellValue(ged.getLeaderIdNo());
                    row.createCell(8).setCellValue(ged.getLeaderNo());
                    row.createCell(9).setCellValue(ged.getLeaderMobile());
                    row.createCell(10).setCellValue(ged.getTeamNum());
                    row.createCell(21).setCellValue(ged.getOffice());
                    row.createCell(22).setCellValue(ged.getSummary());
                    if (l2 != null && l2.size() > 0) {
                        for (int i = 0; i < l2.size(); i++) {
                            XSSFRow row2 = sheet0.getRow(row0 + i);
                            GcontestStuExpData gse = l2.get(i);
                            row2.createCell(11).setCellValue(gse.getName());
                            row2.createCell(12).setCellValue(gse.getProfess());
                            row2.createCell(13).setCellValue(gse.getEnter());
                            row2.createCell(14).setCellValue(gse.getXueli());
                            row2.createCell(15).setCellValue(gse.getIdNo());
                            row2.createCell(16).setCellValue(gse.getNo());
                            row2.createCell(17).setCellValue(gse.getMobile());
                        }
                    }
                    if (l3 != null && l3.size() > 0) {
                        for (int i = 0; i < l3.size(); i++) {
                            XSSFRow row3 = sheet0.getRow(row0 + i);
                            GcontestTeaExpData gse = l3.get(i);
                            row3.createCell(18).setCellValue(gse.getName());
                            row3.createCell(19).setCellValue(gse.getNo());
                            row3.createCell(20).setCellValue(gse.getZhicheng());
                        }
                    }
                    ExcelUtils.mergedRowCell(sheet0, row0, prorows);
                    row0 = row0 + prorows;
                }
                // 设置样式
                for (int i = 4; i < row0; i++) {
                    XSSFRow row = sheet0.getRow(i);
                    for (int m = 0; m < 23; m++) {
                        XSSFCell ce = row.getCell(m);
                        if (ce == null) {
                            ce = row.createCell(m);
                        }
                        ce.setCellStyle(rowStyle);
                    }
                }
            }
            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }
    }

    private void getGcontestDataForExp(ActYw actYw, ProModel param, Map<String, GcontestExpData> m1, Map<String, List<GcontestStuExpData>> m2, Map<String, List<GcontestTeaExpData>> m3) {
        if (actYw == null) {
            return;
        }
        String key = ActYw.getPkey(actYw);
        if (StringUtil.isEmpty(key)) {
            return;
        }
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> recordIds = actTaskService.queryRecordIds(act, actYw.getId());
        if (recordIds == null || recordIds.size() == 0) {
            return;
        }
        param.setIds(recordIds);
        List<GcontestExpData> l1 = dao.getGcontestExpData(param);
        if (l1 == null || l1.size() == 0) {
            return;
        }
        int index = 1;
        for (GcontestExpData o : l1) {
            o.setIndxnum(index);
            m1.put(o.getPromodelid(), o);
            index++;
        }
        List<GcontestStuExpData> l2 = dao.getGcontestStuExpData(param);
        if (l2 != null && l2.size() != 0) {
            for (GcontestStuExpData o : l2) {
                GcontestExpData tem = m1.get(o.getPromodelid());
                if (tem != null) {
                    tem.setTeamNum(tem.getTeamNum() + 1);
                }
                List<GcontestStuExpData> teml = m2.get(o.getPromodelid());
                if (teml == null) {
                    teml = new ArrayList<GcontestStuExpData>();
                    m2.put(o.getPromodelid(), teml);
                }
                teml.add(o);
            }
        }
        List<GcontestTeaExpData> l3 = dao.getGcontestTeaExpData(param);
        if (l3 != null && l3.size() != 0) {
            for (GcontestTeaExpData o : l3) {
                List<GcontestTeaExpData> teml = m3.get(o.getPromodelid());
                if (teml == null) {
                    teml = new ArrayList<GcontestTeaExpData>();
                    m3.put(o.getPromodelid(), teml);
                }
                teml.add(o);
            }
        }
    }

    // 下发证书 最后一个节点根据状态下发
    public List<String> findIdsPassGnode(String actywId, String gnodeId, String state) {
        List<String> ids = new ArrayList<String>();
        ActYw actyw = actYwService.get(actywId);
        ActYwGnode actYwGnode = new ActYwGnode(StringUtil.EMPTY, actyw.getGroupId());
        List<ActYwGnode> gnodeList = actYwGnodeService.findListBygMenu(actYwGnode);
        Boolean isEnd = false;
        ActYwGnode nextGnode = null;
        if (gnodeList != null && gnodeList.size() > 0) {
            for (int i = 0; i < gnodeList.size(); i++) {
                ActYwGnode gNode = gnodeList.get(i);
                // 找到当前节点 向下找一个节点
                if (gNode.getId().equals(gnodeId)) {
                    if (i < (gnodeList.size() - 1)) {
                        nextGnode = gnodeList.get(i + 1);
                        isEnd = true;
                        break;
                    }
                }
            }
            if (nextGnode == null) {
                nextGnode = gnodeList.get(gnodeList.size() - 1);
            }
        }
        if (nextGnode.getId().equals(gnodeList.get(gnodeList.size() - 1).getId())) {
            if (isEnd) {
                ids = getIdsByGnode(nextGnode, actyw);
            } else {
                // 得到审核过最后一步节点id
                ids = gnodeEndIdsList(nextGnode, actyw);
            }
        } else {
            ids = getIdsByGnode(nextGnode, actyw);
        }
        return ids;
    }

    public List<String> getIdsByGnode(ActYwGnode nextGnode, ActYw actyw) {
        String key = ActYw.getPkey(actyw);
        List<String> ids = new ArrayList<String>();
        if (GnodeType.GT_PROCESS.getId().equals(nextGnode.getType())) {

            ActYwGnode actYwGnode = new ActYwGnode(StringUtil.EMPTY, actyw.getGroupId());
            actYwGnode.setParent(new ActYwGnode(nextGnode.getId()));
            List<ActYwGnode> nextGnodeList = actYwGnodeService.findListByYwGparent(actYwGnode);

            if (nextGnodeList != null && nextGnodeList.size() > 0) {
                ActYwGnode firstGnode = nextGnodeList.get(0);
                ids = actTaskService.gnodeIdsList(key, firstGnode.getId());
            }
        } else {
            ids = actTaskService.gnodeIdsList(key, nextGnode.getId());
        }
        return ids;
    }

    public List<String> gnodeEndIdsList(ActYwGnode nextGnode, ActYw actyw) {
        String key = ActYw.getPkey(actyw);
        List<String> ids = new ArrayList<String>();
        if (GnodeType.GT_PROCESS.getId().equals(nextGnode.getType())) {
            ActYwGnode actYwGnode = new ActYwGnode(StringUtil.EMPTY, actyw.getGroupId());
            actYwGnode.setParent(new ActYwGnode(nextGnode.getId()));
            List<ActYwGnode> nextGnodeList = actYwGnodeService.findListByYwGparent(actYwGnode);

            if (nextGnodeList != null && nextGnodeList.size() > 0) {
                ActYwGnode firstGnode = nextGnodeList.get(nextGnodeList.size() - 1);
                ids = actTaskService.gnodeEndIdsList(key, firstGnode.getId());
            }
        } else {
            ids = actTaskService.gnodeEndIdsList(key, nextGnode.getId());
        }
        return ids;
    }

    public ProModel get(String id) {
        return proModelDao.get(id);
    }

    public ProModel getByName(ActYw actYw, String name) {
        ProModel pm = new ProModel();
        pm.setPName(name);
        pm.setType(actYw.getProProject().getType());
        pm.setProType(actYw.getProProject().getProType());
        pm.setActYw(actYw);
        return dao.getByName(pm);
    }

    public ProModel getByProInsId(String proInsId) {
        return dao.getByProInsId(proInsId);
    }

    public ProModel checkByProInsId(String proInsId) {
        ProModel proModel = dao.getByProInsId(proInsId);
        if((proModel == null) || (proModel.getTeam() == null)){
            return null;
        }
        return proModel;
    }

    public List<ProModel> findList(ProModel proModel) {
        return super.findList(proModel);
    }

    public Page<ProModel> findPage(Page<ProModel> page, ProModel proModel) {
        return super.findPage(page, proModel);
    }

    public Page<ProModel> findDataPage(Page<ProModel> page, Model model, String actywId, String gnodeId, ActYw actYw, Act act, ProModel proModel) {
        List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
        String glist = actYwGnodes.stream().map(e->"sid-"+e.getId()).collect(Collectors.joining(","));
        //根据gnodeId得到下一个节点是否为网关，是否需要网关状态
        List<ActYwStatus> actYwStatusList = getActYwStatus(gnodeId);
        if (actYwStatusList != null) {//批量审核参数
            for(ActYwStatus actYwStatus:actYwStatusList){
                //匹配批量审核通过
                if(actYwStatus.getState().equals("通过")){
                    if(model!=null){
                        model.addAttribute("isGate", "1");
                        model.addAttribute("actYwStatus", actYwStatus);
                    }
                }
            }
        }
        proModel.setGnodeIdList(glist);
        proModel.setPage(page);
        if (StringUtils.isBlank(glist)){
            page.setList(new ArrayList<>(0));
        } else {
            //页面审核条件参数
            if(StringUtil.isNotEmpty(proModel.getProCategory())){
                List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
                proModel.setProCategoryList(proList);
            }
            if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
                List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
                proModel.setProjectLevelList(levelList);
            }
            if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
                    &&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
                List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
                proModel.setOfficeIdList(officeList);
            }
            proModel.setActYwId(actywId);
            getRoleResult(proModel);
            List<ProModel> list = dao.findListByIdsOfSt(proModel);
//            List<ProModel> list = dao.findListByIds(proModel);
            //证书
            Map<String, List<SysCertInsVo>> certMap = getSysCertIns(list);
            for (ProModel proModelIndex : list) {
                if (certMap != null && !certMap.isEmpty()) {
                    proModelIndex.setScis(certMap.get(proModelIndex.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(proModelIndex.getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    proModelIndex.getTeam().setuName(StringUtils.join(names, ","));
                }

                List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if(StringUtil.isNotEmpty(proModelIndex.getDeclareId())){
                    User declareUser= systemService.getUser(proModelIndex.getDeclareId());
                    if(declareUser!=null){
                        if(stunames.contains(declareUser.getName())){
                            stunames.remove(declareUser.getName());
                        }
                    }
                }
                if (!stunames.isEmpty()) {
                    proModelIndex.getTeam().setEntName(StringUtils.join(stunames, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(proModelIndex.getEndGnodeId()) && Const.YES.equals(proModelIndex.getState())) {// 流程已结束
                    getFinalResult(proModelIndex);
                }
                Map<String,String>map= ProProcessDefUtils.getActByPromodelId(proModelIndex.getId());
                proModelIndex.setAuditMap(map);
            }
            page.setList(list);
        }
        return page;
        //return findPage(page, proModel, recordIds);
    }

    //查询指派列表
    public Page<ProModel> findPageUnAudit(Page<ProModel> page, ProModel proModel, List<String> recordIds) {
        proModel.setIds(recordIds);
        proModel.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
            //页面审核条件参数
            if(StringUtil.isNotEmpty(proModel.getProCategory())){
                List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
                proModel.setProCategoryList(proList);
            }
            if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
                List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
                proModel.setProjectLevelList(levelList);
            }
            if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
                    &&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
                List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
                proModel.setOfficeIdList(officeList);
            }

            List<ProModel> list = dao.findListByIdsUnAudit(proModel);
            //证书
            Map<String, String> ass = getTaskAssigns(list, proModel.getGnodeId());
            Map<String, List<SysCertInsVo>> map = getSysCertIns(list);
            for (ProModel model : list) {
                //证书
                if (map != null && !map.isEmpty()) {
                    model.setScis(map.get(model.getId()));
                }
                //指派人
                if (ass != null && !ass.isEmpty()) {
                    model.setTaskAssigns(ass.get(model.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    model.getTeam().setuName(StringUtils.join(names, ","));
                }
                // 查询团队学生的名称，多个以逗号分隔
                List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if(StringUtil.isNotEmpty(model.getDeclareId())){
                    User declareUser= systemService.getUser(model.getDeclareId());
                    if(declareUser!=null){
                        if(stunames.contains(declareUser.getName())){
                            stunames.remove(declareUser.getName());
                        }
                    }
                }
                if (!stunames.isEmpty()) {
                    model.getTeam().setEntName(StringUtils.join(stunames, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getEndGnodeId()) && Const.YES.equals(model.getState())) {// 流程已结束
                    getFinalResult(model);
                }
                //添加审核方法参数
                Map<String,String> mapParam=ProProcessDefUtils.getActByPromodelId(model.getId());
                model.setAuditMap(mapParam);

            }
            page.setList(list);
        }
        return page;
    }

    public Page<ProModel> findAssignPage(Page<ProModel> page, Model model, String actywId, ActYw actYw, ProModel proModel) {
        String gnodeId = null;
        if (proModel != null) {
            gnodeId = proModel.getGnodeId();
        }
        if (StringUtil.isEmpty(gnodeId)) {
            List<ActYwGnode> l = ActYwUtils.getAssignNodes(actywId);
            if (l != null && l.size() > 0) {
                gnodeId = l.get(0).getId();
            }
        }
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> gnodeIdList = new ArrayList<String>();
        gnodeIdList.add(gnodeId);
        List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actywId);
        if (org.springframework.util.StringUtils.isEmpty(proModel)) {
            proModel = new ProModel();
        }
        proModel.setActYwId(actywId);
        proModel.setGnodeId(gnodeId);
        if(model!=null){
            model.addAttribute("proModel", proModel);
        }
        return findPageUnAudit(page, proModel, recordIds);
    }

    public Page<ProModel> findQueryPage(Page<ProModel> page, Model model, String actywId, ActYw actYw, ProModel proModel) {
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
       /* List<String> recordIds = actTaskService.queryRecordIds(act, actywId);
        recordIds=removeDuplicate(recordIds);*/
        //增加导入的数据
        if (org.springframework.util.StringUtils.isEmpty(proModel)) {
            proModel = new ProModel();
        }
        proModel.setActYwId(actywId);
        if(model!=null){
            model.addAttribute("proModel", proModel);
        }
        /*List<ProModel> importList = findImportList(proModel);
        if (!importList.isEmpty()) {
            List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds.addAll(importIds);
        }*/
//        return findPage(page, proModel, recordIds);
        return findPageView(page, proModel);
    }

    public Page<ProModel> findPage(Page<ProModel> page, ProModel proModel, List<String> recordIds) {
        proModel.setIds(recordIds);
        proModel.setPage(page);
        if (recordIds.isEmpty()) {
            page.setList(new ArrayList<>(0));
        } else {
            if(StringUtil.isNotEmpty(proModel.getProCategory())){
                List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
                proModel.setProCategoryList(proList);
            }
            if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
                List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
                proModel.setProjectLevelList(levelList);
            }
            if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
                    &&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
                List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
                proModel.setOfficeIdList(officeList);
            }
            List<ProModel> list = dao.findListByIdsOfBefore(proModel);
            //证书
            Map<String, List<SysCertInsVo>> certMap = getSysCertIns(list);
            for (ProModel model : list) {
                if (certMap != null && !certMap.isEmpty()) {
                    model.setScis(certMap.get(model.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getTeamId());
                List<String> teanames = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!teanames.isEmpty()) {
                    model.getTeam().setuName(StringUtils.join(teanames, ","));
                }

                List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if(StringUtil.isNotEmpty(model.getDeclareId())){
                    User declareUser= systemService.getUser(model.getDeclareId());
                    if(declareUser!=null){
                        if(stunames.contains(declareUser.getName())){
                            stunames.remove(declareUser.getName());
                        }
                    }
                }
                if (!stunames.isEmpty()) {
                    model.getTeam().setEntName(StringUtils.join(stunames, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getEndGnodeId()) && Const.YES.equals(model.getState())) {// 流程已结束
                    getFinalResult(model);
                }
                Map<String,String>map= ProProcessDefUtils.getActByPromodelId(model.getId());
                model.setAuditMap(map);
            }
            page.setList(list);
        }
        return page;
    }

    public Page<ProModel> findPageView(Page<ProModel> page, ProModel proModel) {
        proModel.setPage(page);
            if(StringUtil.isNotEmpty(proModel.getProCategory())){
                List<String> proList=Arrays.asList(proModel.getProCategory().split(","));
                proModel.setProCategoryList(proList);
            }
            if(StringUtil.isNotEmpty(proModel.getFinalStatus())){
                List<String> levelList=Arrays.asList(proModel.getFinalStatus().split(","));
                proModel.setProjectLevelList(levelList);
            }
            if(proModel.getDeuser()!=null && proModel.getDeuser().getOffice()!=null
                    &&StringUtil.isNotEmpty(proModel.getDeuser().getOffice().getId())){
                List<String> officeList=Arrays.asList(proModel.getDeuser().getOffice().getId().split(","));
                proModel.setOfficeIdList(officeList);
            }
//        proModel.setActYwId(actywId);
        List<ProModel> list = null;
        getRoleResult(proModel);
            if (proModel.getRoleFlag().equals("2")){
                //专家
                list = dao.findListByIdsOfProfessor(proModel);
            }else{
                //管理员和秘书
                list = dao.findListByIdsOfBefore(proModel);
            }

            //证书
            Map<String, List<SysCertInsVo>> certMap = getSysCertIns(list);
            for (ProModel model : list) {
                if (certMap != null && !certMap.isEmpty()) {
                    model.setScis(certMap.get(model.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(model.getTeamId());
                List<String> teanames = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!teanames.isEmpty()) {
                    model.getTeam().setuName(StringUtils.join(teanames, ","));
                }

                List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if(StringUtil.isNotEmpty(model.getDeclareId())){
                    User declareUser= systemService.getUser(model.getDeclareId());
                    if(declareUser!=null){
                        if(stunames.contains(declareUser.getName())){
                            stunames.remove(declareUser.getName());
                        }
                    }
                }
                if (!stunames.isEmpty()) {
                    model.getTeam().setEntName(StringUtils.join(stunames, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(model.getEndGnodeId()) && Const.YES.equals(model.getState())) {// 流程已结束
                    getFinalResult(model);
                }
                Map<String,String>map= ProProcessDefUtils.getActByPromodelId(model.getId());
                model.setAuditMap(map);
            }
            page.setList(list);

        return page;
    }

    private Map<String, String> getProvTaskAssigns(List<ProModel> subList, String gnodeid) {
            Map<String, String> map = new HashMap<String, String>();
            List<String> param = new ArrayList<String>();
            if (subList.size() == 0) {
                return map;
            }
            for (ProModel p : subList) {
                ProvinceProModel prov=provinceProModelService.getByProModelId(p.getId());
                param.add(prov.getId());
            }
            List<Map<String, String>> list = actYwGnodeService.getAssignUserNames(param, gnodeid);
            if (list != null && list.size() > 0) {
                for (Map<String, String> s : list) {
                    map.put(s.get("promodel_id"), s.get("anames"));
                }
            }
            return map;
        }

    private Map<String, String> getTaskAssigns(List<ProModel> subList, String gnodeid) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> param = new ArrayList<String>();
        if (subList.size() == 0) {
            return map;
        }
        for (ProModel p : subList) {
            param.add(p.getId());
        }
        List<Map<String, String>> list = actYwGnodeService.getAssignUserNames(param, gnodeid);
        if (list != null && list.size() > 0) {
            for (Map<String, String> s : list) {
                map.put(s.get("promodel_id"), s.get("anames"));
            }
        }
        return map;
    }

    private Map<String, List<SysCertInsVo>> getSysCertIns(List<ProModel> subList) {
        Map<String, List<SysCertInsVo>> map = new HashMap<String, List<SysCertInsVo>>();
        List<String> param = new ArrayList<String>();
        if (subList.size() == 0) {
            return map;
        }
        for (ProModel p : subList) {
            param.add(p.getId());
        }
        List<SysCertInsVo> list = sysCertInsDao.getSysCertIns(param);
        if (list != null && list.size() > 0) {
            for (SysCertInsVo s : list) {
                List<SysCertInsVo> tem = map.get(s.getProid());
                if (tem == null) {
                    tem = new ArrayList<SysCertInsVo>();
                    map.put(s.getProid(), tem);
                }
                tem.add(s);
            }
        }
        return map;
    }

    public List<ProModel> findListByIds(ProModel proModel) {
        List<ProModel> list = dao.findListByIdsOfBefore(proModel);
        for (ProModel model : list) {
            // 项目结果
            if (StringUtils.isNotBlank(model.getEndGnodeId()) && Const.YES.equals(model.getState())) {// 流程已结束
                getFinalResult(model);
            }
        }
        return list;
    }

    public void getFinalResult(ProModel model) {
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setGnodeId(model.getEndGnodeId());
        actYwAuditInfo.setPromodelId(model.getId());
        ActYwGnode endNode = actYwGnodeService.getByg(model.getEndGnodeId());
        model.setFinalResult((endNode != null ? endNode.getName() : "")
                + this.getStateByAuditInfo(endNode, actYwAuditInfoService.findList(actYwAuditInfo)));
    }

    @Transactional(readOnly = false)
    public void save(ProModel proModel) {
        if (proModel.getSubStatus() == null) {
            proModel.setSubStatus(Const.NO);
        }
        super.save(proModel);
    }

    @Transactional(readOnly = false)
    public void delete(ProModel proModel) {
        super.delete(proModel);
    }

    @Transactional(readOnly = false)
    public String startFromProcess(ProModel proModel) {
        return "";
    }

    @Transactional(readOnly = false)
    public JSONObject submit(ProModel proModel, JSONObject js) {
        ActYw actYw = actYwService.get(proModel.getActYwId());
        List<String> roles = new ArrayList<String>();
        // 启动大赛工作流
        if (proModelDao.checkProName(proModel.getPName(), proModel.getId(), proModel.getActYwId()) > 0) {
            js.put("ret", 0);
            js.put("msg", "保存失败，该项目名称已经存在");
            return js;
        }
        if (StringUtil.isEmpty(proModel.getYear())) {
            ActYwYear actYwYear = com.oseasy.pro.jobs.pro.ActYwUtils.findActYwYear(proModel.getActYwId(), TenantConfig.getCacheTenant());
            String year = null;
            if (actYwYear != null){
                year = actYwYear.getYear();
            }
            if (year == null){
                year = commonService.getApplyYear(proModel.getActYwId());
            }
            proModel.setYear(year);
        }
        if (actYw != null) {
            if (actYw.getProProject().getProType().contains(FlowProjectType.PMT_XM.getKey())) {
                js = commonService.checkProjectApplyOnSubmit(proModel.getId(), proModel.getActYwId(), proModel.getProCategory(), proModel.getTeamId(), proModel.getYear());
            } else if (actYw.getProProject().getProType().contains(FlowProjectType.PMT_DASAI.getKey())) {
                js = commonService.checkGcontestApplyOnSubmit(proModel.getId(), proModel.getActYwId(), proModel.getTeamId(), proModel.getYear());
            }
            if ("0".equals(js.getString("ret"))) {
                return js;
            }

            ProProject proProject = actYw.getProProject();
//			String nodeRoleId = actTaskService
//					.getProcessStartRoleName(ActYw.getPkey(actYw.group(), actYw.config())); // 从工作流中查询// 下一步的角色集合
//			String roleId = nodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
            ActYwGnode actYwNextGnode = actTaskService
                    .getStartNextGnode(ActYw.getPkey(actYw));
            List<Role> roleList=actYwNextGnode.getRoles();

            if (roleList != null) {
                // 启动节点
                String nodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
                roles=getUsersByRoleList(proModel,roleList);
                if (roles.size() > 0) {
                    //保存编号根据编号规则来。
                    String num ="";
                    try {
                         num = NumRuleUtils.getNumberText(proModel.getActYwId(),proModel.getYear());
                    }catch(Exception e){
                        js.put("ret", 0);
                        js.put("msg", "未设置正确编号规则，请联系管理员设置编号规则!");
                        return js;
                    }
                    if(StringUtil.isEmpty(num)){
                        js.put("ret", 0);
                        js.put("msg", "未设置编号规则，请联系管理员设置编号规则!");
                        return js;
                    }
                    proModel.setCompetitionNumber(num);
//                    proModel.setCompetitionNumber(IdUtils.getGContestNumberByDb());

                    if (StringUtils.isBlank(proModel.getId())){
                        proModel.setId(IdGen.uuid());
                    }
                    if (proModel.getSubStatus() == null){
                        proModel.setIsNewRecord(true);
                        proModel.setSubStatus(Const.NO);
                        proModel.setCreateDate(new Date());
                    }
                    proModel.setTenantId(TenantConfig.getCacheTenant());
                    JedisUtils.publishMsg(ProModelTopic.TOPIC_ONE,proModel);
                    Map<String, Object> vars = new HashMap<String, Object>();
                    vars = proModel.getVars();

                    if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
                        vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, roles);
                    } else {
                        vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", roles);
                    }
                    if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
                        roles.clear();
                        roles.add("assignUser");
                    }
                    User user=UserUtils.getUser();
                    if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
                        Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
                        roles = userService.findListByRoleId(role.getId());
                        oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "大赛申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，请指派专家审核。",
                               OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
                    }else{
                        oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "大赛申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，请您审核。",
                                           OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
                    }
                    String key = ActYw.getPkey(actYw);
                    String userId = UserUtils.getUser().getId();
                    // UserUtils.getUser().getLoginName();
                    identityService.setAuthenticatedUserId(userId);
                    String tenant=TenantConfig.getCacheTenant();
                    //提交批次号
                    vars.put("gnodeVesion", IdGen.uuid());
                    ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key,
                            "pro_model:" + proModel.getId(), vars, tenant);
                    // 流程id返写业务表
                    if (procIns != null) {
                        Act act = new Act();
                        act.setBusinessTable("pro_model");// 业务表名
                        act.setBusinessId(proModel.getId()); // 业务表ID
                        act.setProcInsId(procIns.getId());
                        actDao.updateProcInsIdByBusinessId(act);
                        proModel.setProcInsId(act.getProcInsId());
                        proModel.setSubTime(new Date());
                        proModel.setSubStatus(Const.YES);
                        proModel.setProType(proProject.getProType());
                        proModel.setType(proProject.getType());
                        proModel.setIsNewRecord(false);
                        proModel.setIsSend(Const.NO);
                        proModel.setState(Const.NO);
                        proModel.setCreateBy(UserUtils.getUser());
                        proModel.setUpdateBy(UserUtils.getUser());
                        //采用消息队列方式:
                        JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,proModel);
                         /*提交时处理TeamUserHistory信息*/
                       if(StringUtil.checkNotEmpty(proModel.getTeamUserHistoryList())){
                            commonService.disposeTeamUserHistoryOnSubmit(proModel.getTeamUserHistoryList(), proModel.getActYwId(), proModel.getTeamId(), proModel.getId(), proModel.getYear());
                        }else {
                            if(StringUtil.checkNotEmpty(proModel.getStudentList()) || StringUtil.checkNotEmpty(proModel.getTeacherList())) {
                                List<TeamUserHistory> studentList = proModel.getStudentList();
                                List<TeamUserHistory> teacherList = proModel.getTeacherList();
                                commonService.disposeTeamUserHistoryOnSubmit(studentList, teacherList, proModel.getActYwId(), proModel.getTeamId(), proModel.getId(), proModel.getYear());
                            }
                        }
                        AttachMentEntity attachMentEntity = proModel.getAttachMentEntity();
                        if(attachMentEntity != null){
                            sysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
                        }else {
                            List<SysAttachment> sysAttachmentList = proModel.getFileInfo();
                            sysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
                        }
                       try {
                           saveProModelLogo(proModel);
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                        /*保存logo*/
//                        SysAttachment sysAtt = null;
//                        if (StringUtil.isNotEmpty(proModel.getLogoUrl())) {// logo有变动
//                            ProModel old=get(proModel.getId());
//                            if (old == null){
//                                old = proModel;
//                            }
//                            String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
//                            String dblogo = sysAttachmentService.getLogoByProModelId(proModel.getId());
//                            if (dblogo == null){
//                                try {
//                                    proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            if (dblogo != null && !dblogo.equals(proModel.getLogoUrl())){
//                                proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
//                                try {
//                                    proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
                          /*  ProModel old=get(proModel.getId());
                            if (old == null){
                                old = proModel;
                            }
                            String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
                            if (old.getLogo() != null) {
//                                sysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
                                proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
                            } else {
                                old.setLogo(new SysAttachment());
                            }
                            sysAtt = new SysAttachment();
                            try {
//                                String sysAttId = sysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
                                String sysAttId = proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
                                if(StringUtil.isNotEmpty(sysAttId)){
                                    sysAtt = proSysAttachmentService.get(sysAttId);
                                }
                                if(sysAtt != null){
									old.setLogoUrl(proModel.getLogoUrl());
                                    old.setLogo(sysAtt);
                                    this.updateLogo(old);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        js.put("ret", 1);
                        js.put("msg", "恭喜您，申报成功！");
                        return js;
                    } else {
                        js.put("ret", 0);
                        js.put("msg", "流程配置故障（审核流程未启动）");
                        return js;
                    }
                }
            }
            js.put("ret", 0);
            js.put("msg", "流程配置故障（流程角色未配置）");
            return js;
        }
        js.put("ret", 0);
        js.put("msg", "流程配置故障（审核流程不存在），请联系管理员!");
        return js;
    }

    @Transactional(readOnly = false)
    public String submitMid(ProModel proModel) {
        ActYw actYw = actYwService.get(proModel.getActYwId());
        if (actYw != null) {
            String key = ActYw.getPkey(actYw);
            // String
            // nextRoleId=actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(),key);
            String nextNodeRoleId = actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(), key); // 从工作流中查询
            if (nextNodeRoleId != null) {
            // 下一步的角色集合
                List<String> roles = new ArrayList<String>();
                roles=getUsersByRoles(proModel.getId(),nextNodeRoleId);
                if (roles.size() > 0) {
                    proModel.setCompetitionNumber(IdUtils.getGContestNumberByDb());
                    super.save(proModel);
                    Map<String, Object> vars = new HashMap<String, Object>();
                    vars = proModel.getVars();
                    vars.put(nextNodeRoleId + "s", roles);

                    super.save(proModel);
                    proSysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S1,
                            FileStepEnum.S102);
                    if (proModel.getAttachMentEntity() != null && proModel.getAttachMentEntity().getGnodeId() != null) {
                        String gnodeId = proModel.getAttachMentEntity().getGnodeId();
                        ProMidSubmit proMidSubmit = new ProMidSubmit();
                        proMidSubmit.setGnodeId(gnodeId);
                        proMidSubmit.setPromodelId(proModel.getId());
                        proMidSubmit.setState("0");
                        proMidSubmitService.save(proMidSubmit);
                    }

                    if ("0".equals(proModel.getGrade())) {
                        // 流程不处理 挂起
                    } else {
                        //提交提交批次号
                        vars.put("gnodeVesion", IdGen.uuid());
                        taskService.complete(proModel.getAct().getTaskId(), vars);
                    }
                    return "恭喜您，提交中期报告成功！";
                }
                return "流程配置故障（流程角色未配置），请联系管理员";
            }
            return "流程配置故障（流程角色未配置），请联系管理员";
        }
        return "流程配置故障（审核流程不存在），请联系管理员";
    }

    @Transactional(readOnly = false)
    public String submitClose(ProModel proModel) {
        ActYw actYw = actYwService.get(proModel.getActYwId());
        if (actYw != null) {
            String key = ActYw.getPkey(actYw);
            String nextRoleId = actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(), key);
            if (nextRoleId != null) {
                List<String> roles = new ArrayList<String>();
                roles=getUsersByRoles(proModel.getId(),nextRoleId);
                if (roles.size() > 0) {
                    proModel.setCompetitionNumber(IdUtils.getGContestNumberByDb());
                    super.save(proModel);
                    Map<String, Object> vars = new HashMap<String, Object>();
                    vars = proModel.getVars();
                    vars.put(nextRoleId + "s", roles);
                    super.save(proModel);
                    proSysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S1,
                            FileStepEnum.S102);

                    if (proModel.getAttachMentEntity() != null && proModel.getAttachMentEntity().getGnodeId() != null) {
                        String gnodeId = proModel.getAttachMentEntity().getGnodeId();
                        ProCloseSubmit proCloseSubmit = new ProCloseSubmit();
                        proCloseSubmit.setGnodeId(gnodeId);
                        proCloseSubmit.setPromodelId(proModel.getId());
                        proCloseSubmit.setState("0");
                        proCloseSubmitService.save(proCloseSubmit);
                    }
                    if ("0".equals(proModel.getGrade())) {
                        // 流程不处理 挂起
                    } else {
                        //提交提交批次号
                        vars.put("gnodeVesion", IdGen.uuid());
                        taskService.complete(proModel.getAct().getTaskId(), vars);
                    }
                    return "恭喜您，提交结项报告成功！";
                }
                return "流程配置故障（流程角色未配置），请联系管理员";
            }
            return "流程配置故障（流程角色未配置），请联系管理员";
        }
        return "流程配置故障（审核流程不存在），请联系管理员";
    }

    public Boolean getMidOrCloseView(String proModelId, String gnodeId) {
        ProMidSubmit proMidSubmit = proMidSubmitService.getByGnodeId(proModelId, gnodeId);
        if (proMidSubmit != null) {
            return true;
        }
        ProCloseSubmit proCloseSubmit = proCloseSubmitService.getByGnodeId(proModelId, gnodeId);
        if (proCloseSubmit != null) {
            return true;
        }
        return false;
    }

    public String getStateByAuditInfo(ActYwGnode actYwGnode, List<ActYwAuditInfo> auditInfos) {
        String ret = "";
        if (GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
            if (auditInfos == null || auditInfos.size() == 0) {
                return ret;
            }
            ActYwAuditInfo auditInfo = auditInfos.get(auditInfos.size() - 1);
            if (auditInfo == null) {
                return ret;
            }

            List<ActYwStatus> actYwStatuss = getActYwStatus(auditInfo.getGnodeId());
            if (actYwStatuss != null && actYwStatuss.size() > 0) {
                if ((RegType.RT_GE.getId()).equals(actYwStatuss.get(0).getRegType())) {
                    ret = String.valueOf(auditInfo.getScore());
                } else {
                    for (int i = 0; i < actYwStatuss.size(); i++) {
                        if (actYwStatuss.get(i).getStatus().equals(auditInfo.getGrade())) {
                            ret = actYwStatuss.get(i).getState();
                            break;
                        }
                    }
                }
            } else {
                //根据固定值判断流程状态
                if(auditInfo.getGrade()!=null){
                    if ("1".equals(auditInfo.getGrade())) {
                        return "通过";
                    }else if("0".equals(auditInfo.getGrade())){
                        return "不通过";
                    }else if ("998".equals(auditInfo.getGrade())) {
                        return "通过";
                    }else if("999".equals(auditInfo.getGrade())){
                        return "不通过";
                    }
                }
                return auditInfo.getScore() + "";
            }
        } else {
            ret = String.valueOf(actYwAuditInfoService.getAuditAvgInfoByList(auditInfos));
            ret =subZeroAndDot(ret);
            // return auditInfos.stream().mapToDouble(e ->
            // e.getScore()).average() + "";
        }
        return ret;
    }

    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }



    public String getStateByAuditInfo(List<ActYwAuditInfo> auditInfos) {
        String ret = "";
        if (auditInfos.size() == 1) {
            ActYwAuditInfo auditInfo = auditInfos.get(0);
            if (auditInfo == null) {
                return ret;
            }
            if (ActYwAuditInfo.repulseCode.equals(auditInfo.getGrade())) {
                return ActYwAuditInfo.repulseStr;
            }
            // ActYwGnode actYwGnode=
            // actYwGnodeService.get(auditInfo.getGnodeId());
            List<ActYwStatus> actYwStatuss = getActYwStatus(auditInfo.getGnodeId());
            //根据网关节点线
            if (actYwStatuss != null && actYwStatuss.size() > 0) {
                if ((RegType.RT_GE.getId()).equals(actYwStatuss.get(0).getRegType())) {
                    ret = String.valueOf(auditInfo.getScore());
                } else {
                    for (int i = 0; i < actYwStatuss.size(); i++) {
                        if (actYwStatuss.get(i).getStatus().equals(auditInfo.getGrade())) {
                            ret = actYwStatuss.get(i).getState();
                            break;
                        }
                    }
                }
            } else {
                //根据固定值判断流程状态
                if(auditInfo.getGrade()!=null){
                    if ("1".equals(auditInfo.getGrade())) {
                        return "通过";
                    }else if("0".equals(auditInfo.getGrade())){
                        return "不通过";
                    }
                    if ("998".equals(auditInfo.getGrade())) {
                        return "通过";
                    }else if("999".equals(auditInfo.getGrade())){
                        return "不通过";
                    }
                }
                String str=new BigDecimal(auditInfo.getScore()).toString();
                return str;
            }
        } else {
            ret = String.valueOf(actYwAuditInfoService.getAuditAvgInfoByList(auditInfos));
            ret =subZeroAndDot(ret);
            // return auditInfos.stream().mapToDouble(e ->
            // e.getScore()).average() + "";
        }
        return ret;
    }

    public String getFrontStateByAuditInfo(List<ActYwAuditInfo> auditInfos) {
        String ret = "";
        if (auditInfos.size() == 1) {
            ActYwAuditInfo auditInfo = auditInfos.get(0);
            if (auditInfo == null) {
                return ret;
            }
            if (ActYwAuditInfo.repulseCode.equals(auditInfo.getGrade())) {
                return ActYwAuditInfo.repulseStr;
            }
            // ActYwGnode actYwGnode=
            // actYwGnodeService.get(auditInfo.getGnodeId());
            List<ActYwStatus> actYwStatuss = getActYwStatus(auditInfo.getGnodeId());
            //根据网关节点线
            if (actYwStatuss != null && actYwStatuss.size() > 0) {
                if ((RegType.RT_GE.getId()).equals(actYwStatuss.get(0).getRegType())) {
                    ret = "";//String.valueOf(auditInfo.getScore());
                } else {
                    for (int i = 0; i < actYwStatuss.size(); i++) {
                        if (actYwStatuss.get(i).getStatus().equals(auditInfo.getGrade())) {
                            ret = actYwStatuss.get(i).getState();
                            break;
                        }
                    }
                }
            } else {
                //根据固定值判断流程状态
                if(auditInfo.getGrade()!=null){
                    if ("1".equals(auditInfo.getGrade())) {
                        return "通过";
                    }else if("0".equals(auditInfo.getGrade())){
                        return "不通过";
                    }
                }
                //String str=new BigDecimal(auditInfo.getScore()).toString();
                return "";//str;
            }
        } else {
            ret = String.valueOf(actYwAuditInfoService.getAuditAvgInfoByList(auditInfos));
            ret =subZeroAndDot(ret);
        }
        return ret;
    }

    @Transactional(readOnly = false)
    public void auditWithGateWay(ProModel proModel, String gnodeId) throws GroupErrorException {
        //首节点打回
        if (proModel.getGrade() != null && proModel.getGrade().equals(ActYwAuditInfo.repulseCode)) {
            ActYw actYw = actYwService.get(proModel.getActYwId());
            if (actYw == null) {
                throw new GroupErrorException("找不到流程配置(actYw)");
            }
            ActYwGnode actYwGnode = actYwGnodeService.getBygGclazz(gnodeId);
            String taskId = "";
            try {
                if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
                    taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
                    taskService.claim(taskId, UserUtils.getUser().getId());
                } else {
                    taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId(), UserUtils.getUser().getId());//UserUtils.getUser().getId()
                }
            }catch (Exception e){
                throw new GroupErrorException("该任务已经不存在");
            }
            if (taskId != null) {
                String gnodeVesion = (String) taskService.getVariable(taskId, "gnodeVesion");
                if (gnodeVesion != null) {
                    saveActYwAuditInfo(proModel, actYwGnode, gnodeVesion);
                } else {
                    saveActYwAuditInfo(proModel, actYwGnode);
                }
            }else{
                throw new GroupErrorException("该任务已经不存在");
            }
            //打回将该任务挂起
            runtimeService.suspendProcessInstanceById(proModel.getProcInsId());
            updateSubStatus(proModel.getId(), Const.NO);
            teamUserHistoryDao.updateFinishAsSave(proModel.getId());
            User apply_User = UserUtils.getUser();
            User rec_User = new User();
            rec_User.setId(proModel.getDeclareId());
            String typeName = "";
            if (actYw.getProProject() != null) {
                typeName = actYw.getProProject().getProjectName();
            }
            oaNotifyService.sendOaNotifyByType(apply_User, rec_User, actYwGnode.getName(),
                    typeName + " " + proModel.getpName() + "项目被打回，请查看该项目审核信息作相应修改后再次提交",
                    OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
        } else {
            auditWithGateWayWithoutBack(proModel, gnodeId);
        }
    }

//    @Transactional(readOnly = false)
//    public void auditWithGateWayWithoutBack(ProModel proModel, String gnodeId) throws GroupErrorException {
//        //走审核节点
//        Map<String, Object> vars = new HashMap<String, Object>();
//        ActYw actYw = actYwService.get(proModel.getActYwId());
//        // 根据当前节点gnodeId和网关判断条件得到下一步审核角色
//        if (actYw != null) {
//            String key = ActYw.getPkey(actYw);
//            // 得到当前节点
//            ActYwGnode actYwGnode = actYwGnodeService.getBygGclazz(gnodeId);
//            //是否是值传递节点
//            Boolean isListen = false;
//            Boolean isGate = getNextIsGate(gnodeId);
//            ActYwGnode nextGnode = null;
//            ActYwGnode nextGate = null;
//            String nextGnodeRoleId = null;
//            if (isGate) {
//                nextGate = getNextGate(gnodeId);
//                nextGate = actYwGnodeService.getBygGclazz(nextGate.getId());
//                isListen = nextGate.hasListeners();
//            } else {
//                nextGnode = getNextGnode(gnodeId);
//                nextGnode = actYwGnodeService.getBygGclazz(nextGnode.getId());
//                if (nextGnode != null && (GnodeType.GT_PROCESS_TASK.getId().equals(nextGnode.getType())
//                        || GnodeType.GT_ROOT_TASK.getId().equals(nextGnode.getType()))) {
//                    List<Role> roleList= nextGnode.getRoles();
//                    if (roleList == null) {
//                        throw new GroupErrorException("审核节点角色配置错误。");
//                    }
//                    //多角色配置人员
//                    nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
//                }
//            }
//            String taskId = "";
//            try{
//                // 当前节点为签收节点 先签收任务
//                if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
//                    //得到当前节点的审核人
//                    List<Role> roleList= actYwGnode.getRoles();
//                    //任务已经被签收就向其他待签收者发消息 该任务已经被签收
//                    List<String> noneUserList=getUsersByRoleList(proModel,roleList);
//                    if(noneUserList!=null &&noneUserList.size()>0){
//                        noneUserList.remove(UserUtils.getUser().getId());
//                        if(noneUserList.size()>0){
//                            //审核完成发消息
//                            User apply_User = UserUtils.getUser();
//                            oaNotifyService.sendOaNotifyByTypeAndUser(apply_User, noneUserList, "任务审核",
//                              proModel.getpName() + "项目已经被其他人签收" ,OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
//                        }
//                    }
//                    taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
//                    taskService.claim(taskId, UserUtils.getUser().getId());
//                } else {
//                    // 铜陵学院代码 因为老师和学生角色未分开 前台提交统一按照项目负责人查找taskId
//                    if (actYwGnode.getGforms() != null && FormClientType.FST_FRONT.getKey().equals(actYwGnode.getGforms().get(0).getForm().getClientType())) {
//                        taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId(), proModel.getDeclareId());//UserUtils.getUser().getId()
//                    } else {
//                        taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId(), UserUtils.getUser().getId());//UserUtils.getUser().getId()
//                    }
//                }
//            }catch (Exception e){
//                throw new GroupErrorException("该任务已经不存在");
//            }
//            if(StringUtil.isEmpty(taskId)){
//                throw new GroupErrorException("该任务已经不存在");
//            }
//            Task task = actTaskService.getTask(taskId);
//            //是否是同一审核节点  每个节点批次号批次号不一样
//            String gnodeVesion = (String) taskService.getVariable(taskId, "gnodeVesion");
//            //判断是否改变审核节点 1是改变了审核节点 0是没有改变审核节点
//            String isChangeNode = (String) taskService.getVariable(taskId, "isChangeNode");
//            //判断审核前后台
//            if (!FormClientType.FST_FRONT.getKey().equals(actYwGnode.getGforms().get(0).getForm().getClientType())) {
//                // 根据批次号 保存后台审核结果
//                if (gnodeVesion != null) {
//                    saveActYwAuditInfo(proModel, actYwGnode, gnodeVesion);
//                } else {
//                    saveActYwAuditInfo(proModel, actYwGnode);
//                }
//            } else {
//                // 保存前台审核结果
//                if (gnodeVesion != null) {
//                    saveFrontActYwAuditInfo(proModel, actYwGnode, gnodeVesion);
//                } else {
//                    saveFrontActYwAuditInfo(proModel, actYwGnode);
//                }
//            }
//            //工作流传递携带参数
//            vars = proModel.getVars();
//            if(StringUtil.isNotEmpty(isChangeNode)&&"1".equals(isChangeNode)){
//                vars.put("isChangeNode","0");
//            }
//            //项目类型 名称
//            String typeName = "";
//            if (actYw.getProProject() != null) {
//                typeName = actYw.getProProject().getProjectName();
//            }
//            if (actYwGnode != null) {
//                // 节点之间有连接线
//                // 根据实例id得到下一个节点
//                // 判断下一个节点是否为网关
//                boolean isEndNode = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(), proModel.getProcInsId());
//                //最后一个审核人 更换批次号
//                if (isEndNode) {
//                    vars.put("gnodeVesion", IdGen.uuid());
//                }
//                //是否是学分审核
//                Boolean isScoreAudit = false;
//                String auditGrade = null;
//                // 判断是网关
//                if (isGate) {
//                    // 判断是否为值传递
//                    if (isListen) {
//                        ActYwGclazzData claszzData = actYwGclazzDataService.getStartData(proModel.getId(),
//                                ClazzThemeListener.CMR_A.getKey());
//                        if (claszzData != null) {
//                            auditGrade = getGradeString(gnodeId, actYwGnode, auditGrade, claszzData);
//                        }
//                    }
//                    // 根据网关节点得到网关后面连接线
//                    List<ActYwStatus> actYwStatusList = getActYwStatus(gnodeId);
//                    if (actYwStatusList != null && actYwStatusList.size() > 0) {
//                        // 判断审核结果 判断网关类型  为评分还是审核 1是审核2是评分
//                        for (ActYwStatus actYwStatus : actYwStatusList) {
//                            if ((RegType.RT_GE.getId()).equals(actYwStatus.getRegType())) {
//                                isScoreAudit = true;
//                                break;
//                            }
//                        }
//                        ActYwStatus actYwStatusNext = null;
//                        if (isScoreAudit) {
//                            // 判断是否走到最后一步
//                            boolean isLast = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(),
//                                    proModel.getProcInsId());
//                            if (isLast) { // 如果当前任务环节完成了
//                                actYwStatusNext = getActYwStatus(proModel, gnodeId, gnodeVesion, auditGrade, actYwStatusList);
//                                if (actYwStatusNext != null) {
//                                    if (auditGrade != null) {
//                                        nextGnode = getNextGnodeByGrade(auditGrade, gnodeId);
//                                    } else {
//                                        nextGnode = getNextGnode(proModel, gnodeId);
//                                    }
//                                    if (nextGnode != null
//                                            && !GnodeType.GT_PROCESS_END.getId().equals(nextGnode.getType())
//                                            && !GnodeType.GT_ROOT_END.getId().equals(nextGnode.getType())) {
//                                        //nextGnodeRoleId = nextGnode.getGroles().get(0).getRole().getId();
//                                        List<Role> roleList= nextGnode.getRoles();
//                                        if (roleList == null) {
//                                           throw new GroupErrorException("审核节点角色配置错误。");
//                                        }
//                                        //多角色配置人员
//                                        nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
//                                        vars = addRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
//                                        //向下一步审核人发送审核消息
//                                        if(StringUtil.isNotEmpty(nextGnodeRoleId)){
//                                            sendMsgToNextGnodeUser(nextGnode,vars,nextGnodeRoleId,proModel);
//                                        }
//                                    }
//                                    //如果是值传递节点  将审核的节点值保存到数据库
//                                    if (isListen && StringUtil.isEmpty(auditGrade)) {
//                                        setListenGrade(nextGate, proModel.getId(), actYwStatusNext.getStatus(),
//                                                ClazzThemeListener.CMR_A.getKey());
//                                    }
//                                    vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getStatus());
//                                    try {
//                                        taskService.complete(taskId, vars);
//                                    } catch (Exception e) {
//                                        logger.error("审核节点配置错误。");
//                                        throw new GroupErrorException("审核节点配置错误。");
//                                    }
//                                    //审核完成发消息
//                                    User apply_User = UserUtils.getUser();
//                                    User rec_User = new User();
//                                    rec_User.setId(proModel.getDeclareId());
//                                    oaNotifyService.sendOaNotifyByType(apply_User, rec_User, "学校管理员审核",
//                                        typeName + " " + proModel.getpName() + "项目，" + actYwGnode.getName()
//                                        + actYwStatusNext.getState(),OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
//                                }
//                            } else {
//                                try {
//                                    taskService.complete(taskId, vars);
//                                } catch (Exception e) {
//                                    logger.error("审核节点配置错误。");
//                                    throw new GroupErrorException("审核节点配置错误。");
//                                }
//                            }
//                        } else {
//                            if (auditGrade != null) {
//                                actYwStatusNext = getGateActYwStatusByGrade(auditGrade, actYwStatusList);
//                                nextGnode = getNextGnodeByGrade(auditGrade, gnodeId);
//                            } else {
//                                actYwStatusNext = getGateActYwStatus(proModel, actYwStatusList);
//                                nextGnode = getNextGnode(proModel, gnodeId);
//                            }
//                            if (nextGnode != null
//                                    && !GnodeType.GT_PROCESS_END.getId().equals(nextGnode.getType())
//                                    && !GnodeType.GT_ROOT_END.getId().equals(nextGnode.getType())) {
//                                List<Role> roleList= nextGnode.getRoles();
//                                if (roleList == null) {
//                                  throw new GroupErrorException("审核节点角色配置错误。");
//                                }
//                                //多角色配置人员
//                                nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
//                                vars = addRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
//                                //向下一步审核人发送审核消息
//                                if(StringUtil.isNotEmpty(nextGnodeRoleId)){
//                                    sendMsgToNextGnodeUser(nextGnode,vars,nextGnodeRoleId,proModel);
//                                }
//                                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getStatus());
//                                taskService.complete(taskId, vars);
//                            } else {
//                                vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getStatus());
//                                try {
//                                    taskService.complete(taskId, vars);
//                                } catch (Exception e) {
//                                    logger.error("审核节点配置错误。");
//                                    throw new GroupErrorException("审核节点配置错误。");
//                                }
//                            }
//
//                            if (isListen && StringUtil.isEmpty(auditGrade)) {
//                                setListenGrade(nextGate, proModel.getId(), actYwStatusNext.getStatus(), ClazzThemeListener.CMR_A.getKey());
//                            }
//                       }
//                    }
//                } else {
//                    if (nextGnodeRoleId == null) {
//                        try {
//                            taskService.complete(taskId, vars);
//                        } catch (Exception e) {
//                            logger.error("审核节点配置错误。");
//                            throw new GroupErrorException("审核节点配置错误。");
//                        }
//                    } else {
//                        // 没有网关
//                        vars = addRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
//                        try {
//                            //向下一步审核人发送审核消息
//                            if(StringUtil.isNotEmpty(nextGnodeRoleId)){
//                                sendMsgToNextGnodeUser(nextGnode,vars,nextGnodeRoleId,proModel);
//                            }
//                            taskService.complete(taskId, vars);
//                        } catch (Exception e) {
//                            logger.error("审核节点配置错误。");
//                            throw new GroupErrorException("审核节点配置错误。");
//                        }
//                    }
//                }
//                // 判断流程走完后是否结束；
//                boolean res = actTaskService.ifOver(proModel.getProcInsId());
//                if (res) {
//                    // 标志流程结束
//                    proModel.setState("1");
//                    //团队完成记录
//                    teamUserHistoryService.updateFinishAsClose(proModel.getId());
//                    //项目完成节点
//                    proModel.setEndGnodeId(gnodeId);
//                    if (StringUtil.isNotEmpty(gnodeVesion)) {
//                        proModel.setEndGnodeVesion(gnodeVesion);
//                    }
//                    actYwGclazzDataService.deleteWLPLByApply(proModel.getId());
//                    super.save(proModel);
//                }
//            }
//        }
//    }


    @Transactional(readOnly = false)
    public void auditWithGateWayWithoutBack(ProModel proModel, String gnodeId) throws GroupErrorException {
        Map<String, Object> vars = new HashMap<String, Object>();
        ActYw actYw = actYwService.get(proModel.getActYwId());
        if (actYw == null) {// 根据当前节点gnodeId和网关判断条件得到下一步审核角色
            return;
        }
        boolean isCurr=checkIsHasReport(gnodeId,proModel.getId());
        if(!isCurr){
            throw new GroupErrorException("该任务已处理。");
        }
        String key = ActYw.getPkey(actYw);
        ActYwGnode actYwGnode = actYwGnodeService.getBygGclazz(gnodeId); // 得到当前节点
        Boolean isListen = false; //是否是值传递节点
        Boolean isGate = getNextIsGate(gnodeId);//是否是网关
        ActYwGnode nextGnode = null;
        ActYwGnode nextGate = null;
        String nextGnodeRoleId = null;
        if (isGate) {
            nextGate = getNextGate(gnodeId);
            nextGate = actYwGnodeService.getBygGclazz(nextGate.getId());
            isListen = nextGate.getIsGval();
        } else {
            nextGnode = getNextGnode(gnodeId);
            nextGnode = actYwGnodeService.getBygGclazz(nextGnode.getId());
            if (nextGnode != null && (GnodeType.GT_PROCESS_TASK.getId().equals(nextGnode.getType())
                    || GnodeType.GT_ROOT_TASK.getId().equals(nextGnode.getType()))) {
                List<Role> roleList= nextGnode.getRoles();
                if (roleList == null) {
                    throw new GroupErrorException("审核节点角色配置错误。");
                }
                nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);//多角色配置人员
            }
        }

        boolean isFirst=FormClientType.FST_FRONT.getKey().equals(actYwGnode.getGforms().get(0).getForm().getClientType());
        //获得下一个节点taskId
        String taskId = getTaskId(proModel, actYwGnode, isFirst);
        String gnodeVesion = (String) taskService.getVariable(taskId, "gnodeVesion");//是否是同一审核节点  每个节点批次号批次号不一样
        saveAuditMsg(proModel, actYwGnode, isFirst, gnodeVesion);//判断审核前后台
        vars = proModel.getVars();//工作流传递携带参数
        String isChangeNode = (String) taskService.getVariable(taskId, "isChangeNode");//判断是否改变审核节点 1是改变了审核节点 0是没有改变审核节点
        if(StringUtil.isNotEmpty(isChangeNode)&&"1".equals(isChangeNode)){
            vars.put("isChangeNode","0");
        }
        ActYwStatus actYwStatusNext = null;
        //值传递
        String auditGrade = null;
        if (actYwGnode != null) {
            // 节点之间有连接线 根据实例id得到下一个节点 判断下一个节点是否为网关
            Task task = actTaskService.getTask(taskId);
            boolean isEndNode = actTaskService.isMultiLast(key, task.getTaskDefinitionKey(), proModel.getProcInsId());
            if (isEndNode) {//最后一个审核人 更换批次号
                vars.put("gnodeVesion", IdGen.uuid());
            }
            if (isGate) {// 判断是网关
                if (isListen) {// 判断是否为值传递
                    Object object=runtimeService.getVariable(proModel.getProcInsId(),"globel");
                    if(object!=null && proModel.getGrade()==null){
                        auditGrade = (String)object;
                    }
                }
                // 根据网关节点得到网关后面连接线
                List<ActYwStatus> actYwStatusList = getActYwStatus(gnodeId);
                if (actYwStatusList != null && actYwStatusList.size() > 0) {
                    // 判断审核结果 判断网关类型  为评分还是审核 1是审核2是评分
                    Boolean isScoreAudit = setScoreAuditStatus(actYwStatusList);
                    if (isScoreAudit) { //是否是评分
                        if (isEndNode) {// 判断是否最后一个审核任务
                            // 如果当前任务环节完成了
                            actYwStatusNext = getActYwStatus(proModel, gnodeId, gnodeVesion, auditGrade, actYwStatusList);
                            if (actYwStatusNext != null) {
                                //配置流程所需参数
                                vars = getVarMap(proModel, gnodeId, vars, auditGrade);
                            }
                        }
                    } else {
                        //配置流程所需参数
                        vars = getVarMap(proModel, gnodeId, vars, auditGrade);
                        if (auditGrade != null) {
                            actYwStatusNext = getGateActYwStatusByGrade(auditGrade, actYwStatusList);
                        } else {
                            actYwStatusNext = getGateActYwStatus(proModel, actYwStatusList);
                        }
                    }
                    if (auditGrade != null) {
                        nextGnode = getNextGnodeByGrade(auditGrade, gnodeId);
                    } else {
                        nextGnode = getNextGnode(proModel, gnodeId);
                    }
                    //添加下一步网关判断条件
                    if(actYwStatusNext!=null){
                        vars.put(ActYwTool.FLOW_PROP_GATEWAY_STATE, actYwStatusNext.getStatus());
                    }
                    //如果是值传递节点  将审核的节点值保存到工作流
                    if (isListen && StringUtil.isEmpty(auditGrade)) {
                        runtimeService.setVariable(proModel.getProcInsId(),"globel",actYwStatusNext.getStatus());
                        //setListenGrade(nextGate, proModel.getId(), actYwStatusNext.getStatus(), ClazzThemeListener.CMR_A.getKey());
                    }
                }
            } else {
                if (nextGnodeRoleId != null) {
                    // 没有网关
                    vars = addRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
                    //向下一步审核人发送审核消息
                    if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
                        sendMsgToNextGnodeUser(nextGnode, vars, nextGnodeRoleId, proModel);
                    }
                }
            }
        }
        //流程走下一步
        try {
            taskService.complete(taskId, vars);
            if(actYwGnode.getIsDelegate()){
                //删除委派。
                ActYwGassign actYwGassign=new ActYwGassign();
                actYwGassign.setPromodelId(proModel.getId());
                actYwGassign.setGnodeId(gnodeId);
                actYwGassignService.deleteTodo(actYwGassign);
            }
            if(nextGnode!=null && nextGnode.getIsDelegate()){
                //删除下个节点委派。
                ActYwGassign actYwGassign=new ActYwGassign();
                actYwGassign.setPromodelId(proModel.getId());
                actYwGassign.setGnodeId(nextGnode.getId());
                actYwGassignService.deleteByAssign(actYwGassign);
           }

            //下一个节点为任务节点 审核完成告诉学生审核完成
            if(nextGnode != null && (GnodeType.GT_PROCESS_TASK.getId().equals(nextGnode.getType())
                                || GnodeType.GT_ROOT_TASK.getId().equals(nextGnode.getType()))){
                User apply_User = UserUtils.getUser();
                User rec_User = new User();
                rec_User.setId(proModel.getDeclareId());
                //项目类型 名称
                String typeName = "";
                if (actYw.getProProject() != null) {
                    typeName = actYw.getProProject().getProjectName();
                }
                //项目结果 名称
                String resultString = "";
                if(actYwStatusNext!=null){
                    resultString=actYwStatusNext.getState();
                }
                if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
                    sendMsgToNextGnodeUser(nextGnode, vars, nextGnodeRoleId, proModel);
                }
//                oaNotifyService.sendOaNotifyByType(apply_User, rec_User, "学校管理员审核",
//                        typeName + " " + proModel.getpName() + "项目，" + actYwGnode.getName()
//                                + resultString, OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
            }
        } catch (Exception e) {
            logger.error("审核节点配置错误。",e);
            throw new GroupErrorException("审核节点配置错误。");
        }
        // 判断流程走完后 项目是否结束；
        boolean res = actTaskService.ifOver(proModel.getProcInsId());
        if (res) {
            // 标志流程结束
            proModel.setState("1");
            //团队完成记录
            teamUserHistoryService.updateFinishAsClose(proModel.getId());
            //项目完成节点
            proModel.setEndGnodeId(gnodeId);
            if (StringUtil.isNotEmpty(gnodeVesion)) {
                proModel.setEndGnodeVesion(gnodeVesion);
            }
//            actYwGclazzDataService.deleteWLPLByApply(proModel.getId());
            super.save(proModel);
        }
    }

    public Boolean setScoreAuditStatus(List<ActYwStatus> actYwStatusList) {
        Boolean isScoreAudit =false ;
        for (ActYwStatus actYwStatus : actYwStatusList) {
			if ((RegType.RT_GE.getId()).equals(actYwStatus.getRegType())) {
				isScoreAudit = true;
				break;
			}
		}
        return isScoreAudit;
    }

    //添加流程所需参数
    public Map<String, Object> getVarMap(ProModel proModel, String gnodeId, Map<String, Object> vars, String auditGrade) {
        ActYwGnode nextGnode;
        String nextGnodeRoleId;
        if (auditGrade != null) {
			nextGnode = getNextGnodeByGrade(auditGrade, gnodeId);
		} else {
			nextGnode = getNextGnode(proModel, gnodeId);
		}
        //判断下一个节点不是结束节点
        if (nextGnode != null
				&& !GnodeType.GT_PROCESS_END.getId().equals(nextGnode.getType())
				&& !GnodeType.GT_ROOT_END.getId().equals(nextGnode.getType())) {
			//nextGnodeRoleId = nextGnode.getGroles().get(0).getRole().getId();
			List<Role> roleList = nextGnode.getRoles();
			if (roleList == null) {
				throw new GroupErrorException("审核节点角色配置错误。");
			}
			//多角色配置人员
			nextGnodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
			vars = addRoleIn(nextGnode, nextGnodeRoleId, vars, proModel);
			//向下一步审核人发送审核消息
			if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
				sendMsgToNextGnodeUser(nextGnode, vars, nextGnodeRoleId, proModel);
			}
		}
        return vars;
    }


    public void saveAuditMsg(ProModel proModel, ActYwGnode actYwGnode, boolean isFirst, String gnodeVesion) {
        if (!isFirst) {
			// 根据批次号 保存后台审核结果
			if (gnodeVesion != null) {
				saveActYwAuditInfo(proModel, actYwGnode, gnodeVesion);
			} else {
				saveActYwAuditInfo(proModel, actYwGnode);
			}
		} else {
			// 保存前台审核结果
			if (gnodeVesion != null) {
				saveFrontActYwAuditInfo(proModel, actYwGnode, gnodeVesion);
			} else {
				saveFrontActYwAuditInfo(proModel, actYwGnode);
			}
		}
    }

    public String getTaskId(ProModel proModel, ActYwGnode actYwGnode, boolean isFirst) {
        String taskId;
        try{
			// 当前节点为签收节点 先签收任务
			if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
				//得到当前节点的审核人
				List<Role> roleList= actYwGnode.getRoles();
				//任务已经被签收就向其他待签收者发消息 该任务已经被签收
				List<String> noneUserList=getUsersByRoleList(proModel,roleList);
				if(noneUserList!=null &&noneUserList.size()>0){
					noneUserList.remove(UserUtils.getUser().getId());
					if(noneUserList.size()>0){
						//审核完成发消息
						User apply_User = UserUtils.getUser();
						oaNotifyService.sendOaNotifyByTypeAndUser(apply_User, noneUserList, "任务审核",
						  proModel.getpName() + "项目已经被其他人签收" , OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
					}
				}
				taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
				taskService.claim(taskId, UserUtils.getUser().getId());
			} else {
				// 铜陵学院代码 因为老师和学生角色未分开 前台提交统一按照项目负责人查找taskId
				if (actYwGnode.getGforms() != null && isFirst) {
					taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId(), proModel.getDeclareId());//UserUtils.getUser().getId()
				} else {
					taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId(), UserUtils.getUser().getId());//UserUtils.getUser().getId()
				}
			}
		}catch (Exception e){
			throw new GroupErrorException("该任务已经不存在");
		}
        if(StringUtil.isEmpty(taskId)){
			throw new GroupErrorException("该任务已经不存在");
		}
        return taskId;
    }

//    public String getGradeString(String gnodeId, ActYwGnode actYwGnode, String auditGrade, ActYwGclazzData claszzData) {
//        String startGnode = claszzData.getGclazz().getGnode().getId();
//        // 如果当前节点为后台节点（审核节点） 删除值重新走审核
//        if (FormClientType.FST_ADMIN.getKey() .equals(actYwGnode.getGforms().get(0).getForm().getClientType())) {
//			actYwGclazzDataService.deleteWL(claszzData);
//		}
//		// 如果当前节点为start节点（审核节点） 删除值重新走审核
//		else if (startGnode.equals(gnodeId)) {
//			actYwGclazzDataService.deleteWL(claszzData);
//		} else {
//			//取之前审核保存的值
//			String stringJs = claszzData.getDatas();
//			if (stringJs != null) {
//				auditGrade = stringJs;
//			}
//		}
//        return auditGrade;
//    }

    public ActYwStatus getActYwStatus(ProModel proModel, String gnodeId, String gnodeVesion, String auditGrade, List<ActYwStatus> actYwStatusList) {
        ActYwStatus actYwStatusNext;
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModel.getId());
        actYwAuditInfo.setGnodeId(gnodeId);
        if (gnodeVesion != null) {
			actYwAuditInfo.setGnodeVesion(gnodeVesion);
		}
        String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
        proModel.setgScore(gscore);
        // 判断 走的那种节点状态
        if (auditGrade != null) {
			actYwStatusNext = getGateActYwStatusByGrade(auditGrade, actYwStatusList);
		} else {
			actYwStatusNext = getGateActYwStatus(proModel, actYwStatusList);
		}
        return actYwStatusNext;
    }

    public ActYwStatus getProvActYwStatus(ProModel proModel,String provId, String gnodeId, String gnodeVesion, String auditGrade, List<ActYwStatus> actYwStatusList) {
           ActYwStatus actYwStatusNext;
           ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
           actYwAuditInfo.setPromodelId(provId);
           actYwAuditInfo.setGnodeId(gnodeId);
           if (gnodeVesion != null) {
   			actYwAuditInfo.setGnodeVesion(gnodeVesion);
   		}
           String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
           proModel.setgScore(gscore);
           // 判断 走的那种节点状态
           if (auditGrade != null) {
   			actYwStatusNext = getGateActYwStatusByGrade(auditGrade, actYwStatusList);
   		} else {
   			actYwStatusNext = getGateActYwStatus(proModel, actYwStatusList);
   		}
           return actYwStatusNext;
       }


    private void sendMsgToNextGnodeUser(ActYwGnode gnode,Map<String, Object> vars, String nextGnodeRoleId,ProModel proModel) {
        List<String> roles = new ArrayList<String>();
        if (gnode.getIsAssign() != null && gnode.getIsAssign()) {
//            Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
            roles = userService.findListByRoleId(role.getId());
            oaNotifyService.sendOaNotifyByTypeAndUser(UserUtils.getUser(), roles, "项目指派", proModel.getPName() + "项目指派专家审核。",
                    OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
        } else {
            roles = getUsersByRoles(proModel.getId(), nextGnodeRoleId);
            if (roles.size() > 0) {
                User apply_User = UserUtils.getUser();
                if (gnode.getGforms() != null && gnode.getGforms().get(0) != null && gnode.getGforms().get(0).getForm() != null
                        && gnode.getGforms().get(0).getForm().getClientType() != null
                        && FormClientType.FST_FRONT.getKey().equals(gnode.getGforms().get(0).getForm().getClientType())) {
                    oaNotifyService.sendOaNotifyByTypeAndUser(apply_User, roles, "任务审核",
                            proModel.getpName() + "项目需要你去继续申报", OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
                } else {
                    oaNotifyService.sendOaNotifyByTypeAndUser(apply_User, roles, "任务审核",
                            proModel.getpName() + "项目需要你去审核", OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
                }
            }
        }
    }

//    public ActYwGclazzData setListenGrade(ActYwGnode actYwGnode, String proId, String grade, String type) {
//        ActYwGclazzData pgclazzData = new ActYwGclazzData();
//        pgclazzData.setGclazz(actYwGnode.getGclazzs().get(0));
//        pgclazzData.setType(ClazzThemeListener.CMR_A.getKey());
//        pgclazzData.setApplyId(proId);
//        pgclazzData.setDatas(grade);
//        ActYwGclazzData gcdataMrA = actYwGclazzDataService.putData(proId, type, pgclazzData);
//        return gcdataMrA;
//    }

    public Map<String, Object> addRoleIn(ActYwGnode gnode, String nextGnodeRoleId, Map<String, Object> vars, ProModel proModel) {
        if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
            List<String> roles = new ArrayList<String>();
            //判断下一个节点是否为自动审核。
            ActYwEtAssignRule actYwEtAssignRule=actYwEtAssignRuleService.getActYwEtAssignRuleByActywIdAndGnodeId((String)vars.get("actYwId"),gnode.getId());
            //设置了自动审核并且持续开启 //计算自动审核人
            if(actYwEtAssignRule!=null && "0".equals(actYwEtAssignRule.getAuditType()) && "1".equals(actYwEtAssignRule.getIsAuto())){
                int gradeSpeEachTotal=Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
                Boolean isAutoAudit=true;
                //删除旧的指派记录
                ActYwGassign actYwGassign = new ActYwGassign();
                actYwGassign.setPromodelId(proModel.getId());
                actYwGassign.setGnodeId(gnode.getId());
                Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
                if(ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())){
                    //0是全部专家
                    List<String> expertList=backTeacherExpansionService.findAllExpertList();
                    //找不到满足条件的专家 自动到下一个节点的处于待指派状态
                    if(expertList.size()<gradeSpeEachTotal){
                        isAutoAudit=false;
                        roles.add("assignUser");
                        //已经被指派过
                        if (isHasAssign) {
                            //删除旧的指派记录
                            actYwGassignService.deleteByAssign(actYwGassign);
                        }
                    }else {
                        roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, proModel.getId(), expertList);
                    }
                }else{//根据专家类型找到的专家
                    List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
                    if(expertList.size()<gradeSpeEachTotal){
                        isAutoAudit=false;
                        roles.add("assignUser");
                        //删除旧的指派记录  //已经被指派过
                        if (isHasAssign) {
                            //删除旧的指派记录
                            actYwGassignService.deleteByAssign(actYwGassign);
                        }
//                        throw new GroupErrorException("自动审核失败，请联系管理员核对自动审核审核参数");
                    }else {
                        roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, proModel.getId(), expertList);
                    }
                }
                if(StringUtil.checkEmpty(roles)){
                    //找不到专家走指派路线
                    isAutoAudit=false;
                    roles.add("assignUser");
                    //已经被指派过
                    if (isHasAssign) {
                        //删除旧的指派记录
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                }
                //如果是自动委派或者自动指派
                if(isAutoAudit){
                    addAssignRule(proModel.getId(), roles, actYwEtAssignRule);
                }
            }else {
                //计算审核人
                if (gnode.getIsAssign() != null && gnode.getIsAssign()) {
                    roles.clear();
                    roles.add("assignUser");
                    //删除旧的指派记录
                    ActYwGassign actYwGassign = new ActYwGassign();
                    actYwGassign.setPromodelId(proModel.getId());
                    actYwGassign.setGnodeId(gnode.getId());
                    Boolean isHasAssign = actYwGassignService.isHasAssign(actYwGassign);
                    //已经被指派过
                    if (isHasAssign) {
                        //删除旧的指派记录
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                } else {
                    roles = getUsersByRoles(proModel.getId(), nextGnodeRoleId);
                }
            }
            if(StringUtil.checkEmpty(roles)){
                throw new GroupErrorException("审核节点角色没有人");
            }
            if (gnode != null && GnodeTaskType.GTT_NONE.getKey().equals(gnode.getTaskType())) {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId, roles);
            } else {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s", roles);
            }
        }
        return vars;
    }

    public void addAssignRule(String proModelId, List<String> roles, ActYwEtAssignRule actYwEtAssignRule) {
        List<ActYwGassign> insertActYwGassignList=new ArrayList<ActYwGassign>();
        ActYwGnode gnode =actYwGnodeService.get(actYwEtAssignRule.getGnodeId());
        for(int j=0;j<roles.size();j++){
			String userId=roles.get(j);
			ActYwGassign actYwGassignIndex = new ActYwGassign();
			actYwGassignIndex.setId(IdGen.uuid());
			actYwGassignIndex.setPromodelId(proModelId);
			actYwGassignIndex.setRevUserId(userId);
			actYwGassignIndex.setGnodeId(actYwEtAssignRule.getGnodeId());
            actYwGassignIndex.setAssignUserId(UserUtils.getUserId());
            if(gnode.getIsDelegate()){
                actYwGassignIndex.setType(EarAtype.WP.getKey());
            }else if(gnode.getIsAssign()){
                actYwGassignIndex.setType(EarAtype.ZP.getKey());
            }
			actYwGassignIndex.setYwId(actYwEtAssignRule.getActywId());
			insertActYwGassignList.add(actYwGassignIndex);
		}
        actYwGassignService.insertPl(insertActYwGassignList);
    }

    // 旧的审核方法
    @Transactional(readOnly = false)
    public void audit(ProModel proModel) {
        Map<String, Object> vars = new HashMap<String, Object>();
        ActYw actYw = actYwService.get(proModel.getActYwId());

        if (actYw != null) {
            String key = ActYw.getPkey(actYw);
            String nextGnodeRoleId = actTaskService.getProcessNextRoleName(proModel.getAct().getTaskDefKey(), key);

            if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
                String nextRoleId = nextGnodeRoleId.substring(ActYwTool.FLOW_ROLE_ID_PREFIX.length());
                Role role = systemService.getNamebyId(nextRoleId);
                // 启动节点
                String roleName = role.getName();
                List<String> roles = new ArrayList<String>();
                if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                    roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
                } else {
                    roles = userService.findListByRoleId(role.getId());
                }
                // 后台学生角色id
                if (nextRoleId.equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                    roles.clear();
                    roles.add(userService.findUserById(proModel.getDeclareId()).getName());
                }
                vars = proModel.getVars();
                // List<String>
                // roles=userService.getCollegeExperts(proModel.getDeclareId());
                vars.put(nextGnodeRoleId + "s", roles);
            } else {
                // 流程没有角色为没有后续流程 将流程表示为已经结束
                proModel.setState("1");
                // 更改完成后团队历史表中的状态
                teamUserHistoryService.updateFinishAsClose(proModel.getId());
            }
            // 保存审核结果
            ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(proModel.getProcInsId());
            saveActYwAuditInfo(proModel, actYwGnode);
            String typeName = "";
            if (actYw.getProProject() != null) {
                typeName = actYw.getProProject().getProjectName();
            }
            // 判断审核结果 结果失败 标记为失败
            if (proModel.getGrade() != null && proModel.getGrade().equals("0")) {
                proModel.setState("1");
                super.save(proModel);
                // 更改完成后团队历史表中的状态
                teamUserHistoryService.updateFinishAsClose(proModel.getId());
                User apply_User = UserUtils.getUser();
                User rec_User = new User();
                rec_User.setId(proModel.getDeclareId());
                oaNotifyService.sendOaNotifyByType(apply_User, rec_User, "学校管理员审核",
                        typeName + " " + proModel.getpName() + "项目，" + actYwGnode.getName() + "审核不合格",
                        OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
            } else {
                if (proModel.getGrade() != null && proModel.getGrade().equals("1")) {
                    User apply_User = UserUtils.getUser();
                    User rec_User = new User();
                    rec_User.setId(proModel.getDeclareId());
                    oaNotifyService.sendOaNotifyByType(apply_User, rec_User, "学校管理员审核",
                            typeName + " " + proModel.getpName() + "项目，" + actYwGnode.getName() + "审核合格",
                            OaNotify.Type_Enum.TYPE14.getValue(), proModel.getId());
                }
                if (proModel.getAct().getTaskId() != null) {
                    taskService.complete(proModel.getAct().getTaskId(), vars);
                    super.save(proModel);
                }
            }
        }
    }

    /**
     * 根据请求获取自定义ProModel查询参数.
     * @param request
     * @param gnodeId
     * @param actywId
     * @return ProModel
     */
    public ProModel gen(HttpServletRequest request, String gnodeId, String actywId) {
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        ProModel proModel = new ProModel();
        proModel.setActYwId(actywId);
        proModel.setGnodeId(gnodeId);
        proModel.setIsAll(request.getParameter("isAll"));
        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setYear(year);
            proModel.setProCategory(request.getParameter("proCategory"));
            if (StringUtil.isNotBlank(officeId)) {
                User user = new User();
                user.setOffice(new Office(officeId));
                proModel.setDeuser(user);
            }
            proModel.setQueryStr(request.getParameter("queryStr"));
        }
        return proModel;
    }

    /**
     * 自定义流程列表根据条件查询数据.
     * @param request
     * @param year
     * @param officeId
     * @param gnodeId
     * @param actYw
     * @return List
     */
    public List<ProModel> findListByQuery(HttpServletRequest request, ActYw actYw, String year, String officeId,
            String gnodeId) {
        if(actYw == null){
            return null;
        }

        List<ProModel> list = new ArrayList<>();
        ProModel proModel = gen(request, gnodeId, actYw.getId());
        proModel.setIsAll(request.getParameter("isAll"));
        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setYear(year);
            proModel.setProCategory(request.getParameter("proCategory"));
            if (StringUtils.isNotBlank(officeId)) {
                User user = new User();
                user.setOffice(new Office(officeId));
                proModel.setDeuser(user);
            }
            proModel.setQueryStr(request.getParameter("queryStr"));
            proModel.setFinalStatus(request.getParameter("finalStatus"));
        }
        Act act = new Act(ActYw.getPkey(actYw));
        List<String> recordIds = null;
        if(StringUtils.isNotBlank(gnodeId)){
            List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
            List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
            recordIds = actTaskService.recordIds(act, gnodeIdList, actYw.getId());
        }else{
            recordIds = actTaskService.queryRecordIds(act, actYw.getId());
            //增加导入的数据
            ProModel imodel = new ProModel();
            imodel.setActYwId(actYw.getId());
            List<ProModel> importList = findImportList(imodel);
            if(!importList.isEmpty()){
                List<String> importIds = importList.stream().map(e -> e.getId()).collect(Collectors.toList());
                recordIds.addAll(importIds);
            }
        }

        if(StringUtil.checkEmpty(recordIds)){
            list = Lists.newArrayList();
            return list;
        }

        if(!(Const.YES).equals(proModel.getIsAll())){
            proModel.setIds(recordIds);
            list = findListByIds(proModel);
        }else{
            proModel = new ProModel();
            proModel.setIds(recordIds);
            list = findListByIds(proModel);
        }

        if(list == null){
            list = Lists.newArrayList();
        }
        return list;
    }

    // 根据值传递grade实际走那个节点
    public ActYwStatus getGateActYwStatusByGrade(String grade, List<ActYwStatus> actYwStatusList) {
        ActYwStatus actYwStatusNext = null;
        for (ActYwStatus actYwStatus : actYwStatusList) {
            if (grade.equals(actYwStatus.getStatus())) {
                actYwStatusNext = actYwStatus;
                break;
            }
        }
        return actYwStatusNext;
    }

    // 判断审核结果走那个节点状态
    public ActYwStatus getGateActYwStatus(ProModel proModel, List<ActYwStatus> actYwStatusList) {
        ActYwStatus actYwStatusNext = null;
        for (ActYwStatus actYwStatus : actYwStatusList) {
            // 判断网关类型为评分还是其他审核 2为评分类型
            if ((RegType.RT_GE.getId()).equals(actYwStatus.getRegType())) {
                String alias = actYwStatus.getAlias();
                String startNum = alias.substring(0, StringUtil.lastIndexOf(alias, "-"));
                String endNum = alias.substring(StringUtil.lastIndexOf(alias, "-") + 1);
                int sNum = Integer.parseInt(startNum);
                int eNum = Integer.parseInt(endNum);
                if (proModel.getgScore() != null) {
                    int num = Integer.parseInt(proModel.getgScore());
                    // 分数为100
                    if (endNum.equals(proModel.getgScore()) && "100".equals(endNum)) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                    if (eNum > num && num >= sNum) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                }
            } else {
                if (proModel.getGrade() != null) {
                    if (proModel.getGrade().equals(actYwStatus.getStatus())) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                }
            }
        }
        return actYwStatusNext;
    }

    // 判断下一个节点是否为网关
    public boolean getNextIsGate(String gnodeId) {
        Boolean res = false;
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (actYwGnode != null) {
            // 如果节点为子流程
            if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
                // 节点之间有连接线
                // 根据节点得到下一个节点
                List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
                // 判断下一个节点是否为网关
                if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                    if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                        res = isGateByList(actYwGnodeList);
                    } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                        ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                        if (subActYwGnode != null) {
                            List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                            // 判断list是否为网关。
                            res = isGateByList(subActYwGnodeList);
                        }
                    }
                }
            } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
                // 如果节点为根流程
                List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
                res = isGateByList(actYwGnodeList);
            }
        }
        return res;
    }

    public boolean isGateByList(List<ActYwGnode> actYwGnodeList) {
        boolean res = false;
        if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
            if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())
                    || GnodeType.GT_ROOT_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                res = true;
            }
        }
        return res;
    }

    // 根据值传递判断出走的网关那个分支
    public ActYwGnode getNextGnodeByValue(List<ActYwGnode> actYwGnodeGate, String grade) {
        ActYwGnode actYwGnodeNext = null;
        for (ActYwGnode gateNextLineGnode : actYwGnodeGate) {
            ActYwGnode gateNextGnode = actYwGnodeService.getByg(gateNextLineGnode.getId());
            List<ActYwGstatus> actywGstatuss = gateNextGnode.getGstatuss();
            if (actywGstatuss != null && actywGstatuss.size() > 0) {
                for (int i = 0; i < actywGstatuss.size(); i++) {
                    ActYwGstatus actYwGstatus = gateNextGnode.getGstatuss().get(i);
                    ActYwStatus actYwStatus = actYwGstatus.getStatus();
                    if (grade.equals(actYwStatus.getStatus())) {
                        actYwGnodeNext = gateNextGnode;
                        break;
                    }
                }
            }
            if (actYwGnodeNext != null) {
                break;
            }
        }
        return actYwGnodeNext;
    }

    // 根据判断条件和填入的条件判断出走的网关那个分支
    public ActYwGnode getNextGnodeByGate(List<ActYwGnode> actYwGnodeGate, ProModel proModel) {
        ActYwGnode actYwGnodeNext = null;
        for (ActYwGnode gateNextLineGnode : actYwGnodeGate) {
            ActYwGnode gateNextGnode = actYwGnodeService.getByg(gateNextLineGnode.getId());
            List<ActYwGstatus> actywGstatuss = gateNextGnode.getGstatuss();
            if (actywGstatuss != null && actywGstatuss.size() > 0 && actYwGnodeNext!=null) {
                return actYwGnodeNext;
            }
            for (int i = 0; i < actywGstatuss.size(); i++) {
                ActYwGstatus actYwGstatus = gateNextGnode.getGstatuss().get(i);
                ActYwStatus actYwStatus = actYwGstatus.getStatus();
                // 打分
                if ((RegType.RT_GE.getId()).equals(actYwStatus.getRegType())) {
                    String alias = actYwStatus.getAlias();
                    String startNum = alias.substring(0, StringUtil.lastIndexOf(alias, "-"));
                    String endNum = alias.substring(StringUtil.lastIndexOf(alias, "-") + 1);
                    int sNum = Integer.parseInt(startNum);
                    int eNum = Integer.parseInt(endNum);
                    if (proModel.getgScore() != null) {
                        int num = Integer.parseInt(proModel.getgScore());
                        // 分数为100
                        if (endNum.equals(proModel.getgScore()) && "100".equals(endNum)) {
                            actYwGnodeNext = gateNextGnode;
                            break;
                        }
                        if (eNum > num && num >= sNum) {
                            actYwGnodeNext = gateNextGnode;
                            break;
                        }
                    }
                } else {
                    if (proModel.getGrade() != null && proModel.getGrade().equals(actYwStatus.getStatus())) {
                        actYwGnodeNext = gateNextGnode;
                        break;
                    }
                }
            }
            if (actYwGnodeNext != null) {
                break;
            }
        }
        return actYwGnodeNext;
    }

    // 网关流程根据当前节点得到网关节点
    public ActYwGnode getNextGate(String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                boolean res = isGateByList(actYwGnodeList);
                if (res) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    return gateGnode;
                    // 网关下的分支线
                }
                if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    } else {
                        ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                        if (subActYwGnode != null) {
                            List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                            // 判断list是否为网关。
                            res = isGateByList(subActYwGnodeList);
                            if (res) {
                                ActYwGnode gateGnode = subActYwGnodeList.get(0);
                                return gateGnode;
                            }
                        }
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            boolean res = isGateByList(actYwGnodeList);
            if (res) {
                ActYwGnode gateGnode = actYwGnodeList.get(0);
                return gateGnode;
            }
        }
        return null;
    }

    // 网关流程根据当前节点和网关状态获得下个节点
    public ActYwGnode getNextGnodeByGrade(String grade, String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        // 网关后的线gnode
        ActYwGnode actYwGnodeNextLine = null;
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    // 网关下的分支线
                    List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                    actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
                } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    // 直接结束节点
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    }
                    ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                    if (subActYwGnode != null) {
                        List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        // 网关下的分支线
                        List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                        actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
                        // actYwGnodeNext=getNextGnodeByGate(subActYwGnodeList,proModel);
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            ActYwGnode gateGnode = actYwGnodeList.get(0);
            // 网关下的分支线
            List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
            actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
            // actYwGnodeNext=getNextGnodeByGate(actYwGnodeList,proModel);
        }
        if (actYwGnodeNextLine != null) {
            ActYwGnode nextGnode = getNextGnodeByline(actYwGnodeNextLine);
            return nextGnode;
        }
        return null;
    }
    // 根据线得到下一个节点
    public ActYwGnode getNextGnodeByline(ActYwGnode actYwGnodeNextLine) {
        List<ActYwGnode> actYwGnodeNextGnodeList = actYwGnodeService.getNextNode(actYwGnodeNextLine);
        if (actYwGnodeNextGnodeList == null) {
            return null;
        }
        // 通过线得到下一个节点
        ActYwGnode actYwGnodeNext = actYwGnodeService.getByg(actYwGnodeNextGnodeList.get(0).getId());
        // 下一个节点为子流程
        if (GnodeType.GT_PROCESS.getId().equals(actYwGnodeNext.getType())) {
            // 得到子流程中第一个任务节点userTask
            ActYwGnode toSubNextGnode = getFirstGnode(actYwGnodeNext);
            toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
            if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                return toSubNextGnode;
            }
            // 下一个节点为子节点结束节点
        } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeNext.getType())) {
            // 直接结束节点
            StenType stenType = StenType.getByKey(actYwGnodeNext.getNode().getNodeKey());
            if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                return actYwGnodeNext;
            }
            // 当前节点父节点
            ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeNext.getParentId());
            if (subActYwGnode != null) {
                List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                ActYwGnode gateGnode = subActYwGnodeList.get(0);
                if (GnodeType.GT_ROOT_END.getId().equals(gateGnode.getType())) {
                    return actYwGnodeNext;
                } else if (GnodeType.GT_PROCESS.getId().equals(gateGnode.getType())) {
                    // 得到子流程中第一个任务节点userTask
                    ActYwGnode toSubNextGnode = getFirstGnode(gateGnode);
                    toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                    if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                        return toSubNextGnode;
                    }
                } else {
                    gateGnode = actYwGnodeService.getByg(gateGnode.getId());
                    return gateGnode;
                }
            }
        } else if (GnodeType.GT_ROOT_END.getId().equals(actYwGnodeNext.getType())) {
            return actYwGnodeNext;
        } else {
            // 下一个节点为userTask
            if (actYwGnodeNext.getGroles() != null && actYwGnodeNext.getGroles().get(0) != null) {
                return actYwGnodeNext;
            }
        }
        return null;
    }

    // 网关流程根据当前节点和网关状态获得下个节点的角色
    public ActYwGnode getNextGnode(ProModel proModel, String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        // 网关后的线gnode
        ActYwGnode actYwGnodeNextLine = null;
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    // 网关下的分支线
                    List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                    actYwGnodeNextLine = getNextGnodeByGate(gatelineList, proModel);
                } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    // 直接结束节点
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    }
                    ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                    if (subActYwGnode != null) {
                        List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        // 网关下的分支线
                        List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                        actYwGnodeNextLine = getNextGnodeByGate(gatelineList, proModel);
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            ActYwGnode gateGnode = actYwGnodeList.get(0);
            // 网关下的分支线
            List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
            actYwGnodeNextLine = getNextGnodeByGate(gatelineList, proModel);
            // actYwGnodeNext=getNextGnodeByGate(actYwGnodeList,proModel);
        }
        if (actYwGnodeNextLine != null) {
            ActYwGnode nextGnode = getNextGnodeByline(actYwGnodeNextLine);
            return nextGnode;
        }
        return null;
    }

    // 网关得到下一个子流程节点的第一个节点
    public ActYwGnode getFirstGnode(ActYwGnode toNextGnode) {
        List<ActYwGnode> childFirstGnode = actYwGnodeService.getChildFlowsSecondNode(toNextGnode);
        ActYwGnode toSubNextGnode = null;
        if (childFirstGnode != null) {
            toSubNextGnode = childFirstGnode.get(0);
        }
        return toSubNextGnode;
    }

    // 得到下一个节点通过gnodeId 无网关
    public ActYwGnode getNextGnode(String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        List<ActYwGnode> nextActYwGnodes = actYwGnodeService.getNextNextNode(actYwGnode);
        ActYwGnode noGate = null;
        if (nextActYwGnodes != null && nextActYwGnodes.size() > 0) {
            noGate = nextActYwGnodes.get(0);
            if (GnodeType.GT_PROCESS_END.getId().equals(noGate.getType())) {
                // 得到父节点
                ActYwGnode subActYwGnode = actYwGnodeService.get(noGate.getParentId());
                if (subActYwGnode != null) {
                    // 得到父节点下一个节点
                    List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                    if (subActYwGnodeList != null && subActYwGnodeList.size() > 0) {
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        if (GnodeType.GT_ROOT_END.getId().equals(gateGnode.getType())) {
                            return gateGnode;
                        } else if (GnodeType.GT_PROCESS.getId().equals(gateGnode.getType())) {
                            // 得到子流程中第一个任务节点userTask
                            ActYwGnode toSubNextGnode = getFirstGnode(gateGnode);
                            toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                            if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                                return toSubNextGnode;
                            }
                        } else {
                            gateGnode = actYwGnodeService.getByg(gateGnode.getId());
                            return gateGnode;
                        }
                    }
                }
            } else if (GnodeType.GT_PROCESS.getId().equals(noGate.getType())) {
                // 得到子流程中第一个任务节点userTask
                ActYwGnode toSubNextGnode = getFirstGnode(noGate);
                toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                    return toSubNextGnode;
                }
            }
            noGate = actYwGnodeService.getByg(noGate.getId());
        }
        return noGate;
    }

    // 根据当前节点获得网关节点的状态
    public List<ActYwStatus> getActYwStatusByActYwGnode(ActYwGnode actYwGnode) {
        List<ActYwStatus> actYwStatusList = null;
        if (actYwGnode != null) {
            // 节点之间有连接线
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 根据实例id得到下一个节点
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())
                        || GnodeType.GT_ROOT_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                    actYwStatusList = actYwStatusService.getAllStateByGnodeId(actYwGnode.getId());
                }
            }
        }
        return actYwStatusList;
    }

    // 获得gnodeId网关节点的状态
    public List<ActYwStatus> getActYwStatus(String gnodeId) {
        List<ActYwStatus> actYwStatusList = null;
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (actYwGnode == null) {
            return actYwStatusList;
        }
        // 节点之间有连接线
        if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 根据实例id得到下一个节点
            if (actYwGnodeList == null ) {
                return actYwStatusList;
            }
            if (actYwGnodeList.size() ==0) {
                return actYwStatusList;
            }
            if (GnodeType.GT_ROOT_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                actYwStatusList = getActYwStatusList(actYwGnodeList.get(0));
            }
        } else {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList == null ) {
                return actYwStatusList;
            }
            if (actYwGnodeList.size() ==0) {
                return actYwStatusList;
            }
            if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                actYwStatusList = getActYwStatusList(actYwGnodeList.get(0));
                // actYwStatusList =
                // actYwStatusService.getAllStateByGnodeId(actYwGnodeList.get(0).getId());
            } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                if (subActYwGnode == null) {
                    return actYwStatusList;
                }
                List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                if (subActYwGnodeList != null && subActYwGnodeList.size() > 0) {
                    if (GnodeType.GT_ROOT_GATEWAY.getId().equals(subActYwGnodeList.get(0).getType())) {
                        actYwStatusList = getActYwStatusList(subActYwGnodeList.get(0));
                    }
                }
            }

        }
        return actYwStatusList;
    }

    // 根据网关节点得到状态
    public List<ActYwStatus> getActYwStatusList(ActYwGnode actYwGnode) {
        List<ActYwStatus> actYwStatusList = new ArrayList<ActYwStatus>();
        List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNode(actYwGnode);
        if (StringUtil.checkNotEmpty(actYwGnodeList)) {
            for (int i = 0; i < actYwGnodeList.size(); i++) {
                List<ActYwStatus> lineActYwStatusList = actYwStatusService
                        .getAllStateByGnodeId(actYwGnodeList.get(i).getId());
                actYwStatusList.addAll(lineActYwStatusList);
            }
        }
        return actYwStatusList;
    }

    @Transactional(readOnly = false)
    public void saveStep3(ProModel proModel) throws Exception {
        AttachMentEntity attachMentEntity = proModel.getAttachMentEntity();
        if(attachMentEntity != null){
            proSysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        }else {
            List<SysAttachment> sysAttachmentList = proModel.getFileInfo();
            proSysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        }
    }

    @Transactional(readOnly = false)
    public void saveActYwAuditInfo(ProModel proModel, ActYwGnode actYwGnode, String gnodeVesion) {
        // 判断审核结果
        if (proModel.getGrade() != null || proModel.getgScore() != null) {
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
            actYwAuditInfo.setPromodelId(proModel.getId());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            actYwAuditInfo.setGnodeVesion(gnodeVesion);
            if (StringUtils.isNotBlank(proModel.getGrade())) {
                actYwAuditInfo.setGrade(proModel.getGrade());
            } else {
                actYwAuditInfo.setScore(Double.parseDouble(proModel.getgScore()));
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfo.setSuggest(proModel.getSource());
            ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
            if (lastAuditActYwAuditInfo != null) {
                actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
            }
            actYwAuditInfoService.save(actYwAuditInfo);
        }
    }

    @Transactional(readOnly = false)
    public void saveActYwAuditInfo(ProModel proModel, ActYwGnode actYwGnode) {
        // 判断审核结果
        if (proModel.getGrade() != null || proModel.getgScore() != null) {
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
            actYwAuditInfo.setPromodelId(proModel.getId());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            if (StringUtils.isNotBlank(proModel.getGrade())) {
                actYwAuditInfo.setGrade(proModel.getGrade());
            } else {
                actYwAuditInfo.setScore(Double.parseDouble(proModel.getgScore()));
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfo.setSuggest(proModel.getSource());
            ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
            if (lastAuditActYwAuditInfo != null) {
                actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
            }
            actYwAuditInfoService.save(actYwAuditInfo);
        }
    }

    @Transactional(readOnly = false)
    public void saveProvActYwAuditInfo(ProvinceProModel provinceProModel,ProModel proModel, ActYwGnode actYwGnode) {
        // 判断审核结果
        if (proModel.getGrade() != null || proModel.getgScore() != null) {
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
            actYwAuditInfo.setPromodelId(provinceProModel.getId());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            if (StringUtils.isNotBlank(proModel.getGrade())) {
                actYwAuditInfo.setGrade(proModel.getGrade());
            } else {
                actYwAuditInfo.setScore(Double.parseDouble(proModel.getgScore()));
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfo.setSuggest(proModel.getSource());
            actYwAuditInfoService.save(actYwAuditInfo);

            //设置平均分



        }
    }


    @Transactional(readOnly = false)
    public void saveFrontActYwAuditInfo(ProModel proModel, ActYwGnode actYwGnode, String gnodeVesion) {
        // 判断审核结果
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
        actYwAuditInfo.setPromodelId(proModel.getId());
        actYwAuditInfo.setGnodeId(actYwGnode.getId());
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        actYwAuditInfo.setGnodeVesion(gnodeVesion);
        ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
        if (lastAuditActYwAuditInfo != null) {
            actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
        }
        actYwAuditInfoService.save(actYwAuditInfo);
    }

    @Transactional(readOnly = false)
    public void saveFrontActYwAuditInfo(ProModel proModel, ActYwGnode actYwGnode) {
        // 判断审核结果
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
        actYwAuditInfo.setPromodelId(proModel.getId());
        actYwAuditInfo.setGnodeId(actYwGnode.getId());
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
        if (lastAuditActYwAuditInfo != null) {
            actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
        }
        actYwAuditInfoService.save(actYwAuditInfo);
    }

    @Transactional(readOnly = false)
    public void submitStep3(ProModel proModel) throws Exception {
        AttachMentEntity attachMentEntity = proModel.getAttachMentEntity();
        if(attachMentEntity != null){
            proSysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        }else {
            List<SysAttachment> sysAttachmentList = proModel.getFileInfo();
            proSysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        }

        teamUserHistoryDao.updateFinishAsStart(proModel.getId());
        if (StringUtil.isNotEmpty(proModel.getProcInsId())) {//被打回
            updateSubStatus(proModel.getId(), Const.YES);
            try {
                runtimeService.activateProcessInstanceById(proModel.getProcInsId());
                //打回后修改流程变量
                String taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
                //设置流程变量
                taskService.setVariable(taskId, "gnodeVesion", IdGen.uuid());
            } catch (Exception e) {
                logger.error(e);
                throw new ActYwRuntimeException("请勿重复提交 !");
            }
        } else {
            ActYw actYw = actYwService.get(proModel.getActYwId());
            if(actYw == null){
                throw new ActYwRuntimeException("流程ID未定义 ["+proModel.getActYwId()+"]!");
            }
//            FlowProjectType fp = actYw.getFptype();
//            if((FlowProjectType.PMT_XM).equals(fp)){
//                excellentShowService.saveExcellentShow(proModel.getIntroduction(), proModel.getTeamId(), ExcellentShow.Type_Project, proModel.getId(), proModel.getActYwId());//保存优秀展示
//            }else if((FlowProjectType.PMT_DASAI).equals(fp)){
//                excellentShowService.saveExcellentShow(proModel.getIntroduction(), proModel.getTeamId(), ExcellentShow.Type_Gcontest, proModel.getId(), proModel.getActYwId());//保存优秀展示
//            }
            proModel.setSubTime(new Date());
            proModel.setSubStatus(Const.YES);
            this.actStart(proModel, actYw);
        }
    }

    @Transactional(readOnly = false)
    public void actStartForImp(ProModel proModel) throws Exception {
        teamUserHistoryDao.updateFinishAsStart(proModel.getId());
        ActYw actYw = actYwService.get(proModel.getActYwId());
        proModel.setSubTime(new Date());
        proModel.setSubStatus(Const.YES);
        this.actStart(proModel, actYw);
    }

    //处理logo
    @Transactional(readOnly = false)
    public void saveProModelLogo(ProModel proModel) throws Exception{
        //logo处理
        SysAttachment sysAttachmentLogo = proModel.getLogo();
        SysAttachment oldLogo = new SysAttachment();
        if (StringUtil.isNotEmpty(sysAttachmentLogo.getUrl())) {// logo有变动
            String logoId = sysAttachmentLogo.getId();
            String logoUrl = StringEscapeUtils.unescapeHtml4(sysAttachmentLogo.getUrl());
            if(StringUtil.isNotEmpty(logoId)){
                oldLogo = sysAttachmentService.get(logoId);
                if(oldLogo != null && !Objects.equals(oldLogo.getUrl(), logoUrl)){
                    proSysAttachmentService.delFile(oldLogo.getId(), oldLogo.getUrl());
                    proSysAttachmentService.moveTempFile(logoUrl, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
                }
            }else {
                proSysAttachmentService.moveTempFile(logoUrl, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
            }
        }else {
            if(StringUtil.isNotEmpty(sysAttachmentLogo.getId())){
                oldLogo = sysAttachmentService.get(sysAttachmentLogo.getId());
                proSysAttachmentService.delFile(oldLogo.getId(), oldLogo.getUrl());
            }
        }
    }


    @Transactional(readOnly = false)
    public void saveStep2(ProModel proModel) throws Exception {
        //这里这个逻辑需要这样改？
        ProModel old = null;
        if (StringUtil.isNotEmpty(proModel.getId())) {
            old = this.get(proModel.getId());
        }
        if (old != null) {
            old.setPName(proModel.getPName());
            old.setProCategory(proModel.getProCategory());
            old.setTeamId(proModel.getTeamId());
            old.setIntroduction(proModel.getIntroduction());
            old.setYear(proModel.getYear());
            old.setShortName(proModel.getShortName());
        } else {
            old = proModel;
        }
        if (StringUtil.isEmpty(old.getYear())) {
            old.setYear(commonService.getApplyYear(old.getActYwId()));
        }
        this.save(old);
//        if (StringUtil.isNotEmpty(proModel.getLogoUrl())) {// logo有变动
//            String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
//            String dblogo = sysAttachmentService.getLogoByProModelId(proModel.getId());
//            if (dblogo == null) {
//                proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
//            }
//            if (dblogo != null && !dblogo.equals(proModel.getLogoUrl())) {
//                proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
//                proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
//            }
//        }
        saveProModelLogo(proModel);
        List<TeamUserHistory> stus = proModel.getStudentList();
        List<TeamUserHistory> teas = proModel.getTeacherList();
        if ((stus != null && stus.size() > 0) || (teas != null && teas.size() > 0)) {
            commonService.disposeTeamUserHistoryOnSave(stus, teas, old.getActYwId(), old.getTeamId(), old.getId(), old.getYear());
        }
    }
    @Transactional(readOnly = false)
    private void updateLogo(ProModel old) {
        dao.updateLogo(old);
    }

    @Transactional(readOnly = false)
    public void actStart(ProModel proModel, ActYw actYw) {
        if (actYw == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程不存在），请联系管理员!");
        }
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        //String nodeRoleId = actYwNextGnode.getGroles().get(0).getRole().getId();
        List<Role> roleList = actYwNextGnode.getRoles();
        if (roleList == null) {
            throw new ActYwRuntimeException("流程配置故障（审核角色不存在），请联系管理员!");
        }
        //多角色配置人员
        String nodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        List<String> roles = new ArrayList<String>();
        if(StringUtil.isEmpty(proModel.getCompetitionNumber())){
            String num ="";
            try {
                num = NumRuleUtils.getNumberText(proModel.getActYwId(),proModel.getYear());
            }catch(Exception e){
                logger.error("未设置正确编号规则，请联系管理员设置编号规则!");
            }
            if(StringUtil.isEmpty(num)){
                num=IdUtils.getProjectNumberByDb();
            }
            proModel.setCompetitionNumber(num);
        }

        if (StringUtil.isNotEmpty(nodeRoleId)) {
            //判断下一个节点是否为自动审核。
            ActYwEtAssignRule actYwEtAssignRule = actYwEtAssignRuleService.getActYwEtAssignRuleByActywIdAndGnodeId(actYw.getId(), actYwNextGnode.getId());
            //设置了自动审核并且持续开启 //计算自动审核人
            if (actYwEtAssignRule != null && "0".equals(actYwEtAssignRule.getAuditType()) && "1".equals(actYwEtAssignRule.getIsAuto())) {
                int gradeSpeEachTotal = Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
                if (ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())) {
                    //0是全部专家
                    List<String> expertList = backTeacherExpansionService.findAllExpertList();
                    if (expertList.size() < gradeSpeEachTotal) {
                        throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                    }
                    roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, proModel.getId(), expertList);
                } else {//1为根据学院找专家
                    //找到的专家
//                    List<String> expertList = backTeacherExpansionService.findCollegeExpertListByPro(proModel.getId());
                    List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
                    if (expertList.size() < gradeSpeEachTotal) {
                        throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                    }
                    roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, proModel.getId(), expertList);
                }
                if (StringUtil.checkEmpty(roles)) {
                    throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                } else {
                    //添加指派记录
                    addAssignRule(proModel.getId(), roles, actYwEtAssignRule);
                    //清除指派中专家项目计数
                    ActYwEtAuditNum actYwEtAuditNum = new ActYwEtAuditNum();
                    actYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
                    actYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
                    actYwEtAuditNum.setProId(proModel.getId());
                    actYwEtAuditNumService.deleteByProId(actYwEtAuditNum);
                }
            }else{
                if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
                    roles.clear();
                    roles.add("assignUser");
                } else {
                    roles=getUsersByRoleList(proModel,roleList);
                }
                if (roles.size() <= 0) {
                    throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
                }
            }
        }
        Map<String, Object> vars = new HashMap<String, Object>();
        vars = proModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, roles);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", roles);
        }
        String key = ActYw.getPkey(actYw);
        User user = UserUtils.getUser();
        identityService.setAuthenticatedUserId(user.getId());
        vars.put("gnodeVesion", IdGen.uuid());
        ProcessInstance procIns = runtimeService.startProcessInstanceByKeyAndTenantId(key, "pro_model:" + proModel.getId(), vars,TenantConfig.getCacheTenant());
        // 流程id返写业务表
        if (procIns == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程未启动），请联系管理员!");
        }
        Act act = new Act();
        act.setBusinessTable("pro_model");// 业务表名
        act.setBusinessId(proModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        proModel.setProcInsId(act.getProcInsId());
        proModel.setTenantId(TenantConfig.getCacheTenant());
        proModel.setState(Const.NO);
        proModel.setIsSend(Const.NO);
        JedisUtils.publishMsg(ProModelTopic.TOPIC_THREE,proModel);
//        super.save(proModel);
        //添加申报消息
        if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
            Role role = coreService.getByRtype(CoreSval.Rtype.EXPORT.getKey());
//            Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            roles = userService.findListByRoleId(role.getId());
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "项目申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，请你指派专家审核。",
                    OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
        }else{
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "项目申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，需要你审核。",
                                OaNotify.Type_Enum.TYPE18.getValue(), proModel.getId());
        }

        String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
        IWorkFlow<?, ?, ?> workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
        if (workFlow != null) {
            if(workFlow instanceof ProModelService){
                ((ProModelService)workFlow).saveAddPro(proModel);
            }else{
                logger.warn("服务名指定的服务类型不匹配["+serviceName+"->ProModelService]");
            }
        }else{
            logger.warn("服务名指定的服务名["+serviceName+"]未定义");
        }
    }

    @Transactional(readOnly = false)
    public void actProvinceStart(ProvinceProModel provinceProModel, ActYw actYw,String tenantId, Date subTime) {
        if (actYw == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程不存在），请联系管理员!");
        }
        ActYwGnode actYwNextGnode = actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
        List<Role> roleList = actYwNextGnode.getRoles();
        if (roleList == null) {
            throw new ActYwRuntimeException("流程配置故障（审核角色不存在），请联系管理员!");
        }
        //多角色配置人员
        String nodeRoleId = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
        List<String> roles = new ArrayList<String>();
        if(StringUtil.isEmpty(provinceProModel.getCompetitionNumber())){
            String num ="";
            try {
                //   所有编号都会存在重复问题
                num = NumRuleUtils.getProvinceNumberText(provinceProModel.getActYwId(),provinceProModel.getYear(),subTime);
            }catch(Exception e){
                logger.error("未设置正确编号规则，请联系管理员设置编号规则!");
                throw new GroupErrorException("未设置正确编号规则，请联系管理员设置编号规则!");
            }
            if(StringUtil.isEmpty(num)){
                num=IdUtils.getProjectNumberByDb();
            }
            provinceProModel.setCompetitionNumber(num);
        }
        //设置推送属性
        String schoolType = Optional.ofNullable(sysTenantService.getTypeByTenantId(tenantId)).orElse("0");
        if (StringUtils.equals("1",schoolType)){
            //部属高校,直接省级
            provinceProModel.setSendState(2);
        }else{
            provinceProModel.setSendState(1);
        }
        provinceProModel.setSchoolTenantId(tenantId);

        //
        try{
            provinceProModelService.save(provinceProModel);
        }catch (Exception e){
            throw new GroupErrorException("推送异常");
        }

        if (StringUtil.isNotEmpty(nodeRoleId)) {
            //判断下一个节点是否为自动审核。
            ActYwEtAssignRule actYwEtAssignRule = actYwEtAssignRuleService.getActYwEtAssignRuleByActywIdAndGnodeId(actYw.getId(), actYwNextGnode.getId());
            //设置了自动审核并且持续开启 //计算自动审核人
            if (actYwEtAssignRule != null && "0".equals(actYwEtAssignRule.getAuditType()) && "1".equals(actYwEtAssignRule.getIsAuto())) {
                int gradeSpeEachTotal = Integer.valueOf(actYwEtAssignRule.getAuditUserNum());
                if (ActYwEtAssignRule.autoRole.equals(actYwEtAssignRule.getAuditRole())) {
                    //0是全部专家
                    List<String> expertList = backTeacherExpansionService.findAllExpertList();
                    if (expertList.size() < gradeSpeEachTotal) {
                        throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                    }
                    roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, provinceProModel.getId(), expertList);
                } else {//1为根据学院找专家
                    //找到的专家
                    List<String> expertList=backTeacherExpansionService.findExpertListByType(actYwEtAssignRule.getAuditRole());
                    if (expertList.size() < gradeSpeEachTotal) {
                        throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                    }
                    roles = actYwEtAssignRuleService.getExpertByRule(actYwEtAssignRule, provinceProModel.getId(), expertList);
                }
                if (StringUtil.checkEmpty(roles)) {
                    throw new GroupErrorException("自动审核失败，请核对自动审核审核参数");
                } else {
                    //添加指派记录
                    addAssignRule(provinceProModel.getId(), roles, actYwEtAssignRule);
                    //清除指派中专家项目计数
                    ActYwEtAuditNum actYwEtAuditNum = new ActYwEtAuditNum();
                    actYwEtAuditNum.setActywId(actYwEtAssignRule.getActywId());
                    actYwEtAuditNum.setGnodeId(actYwEtAssignRule.getGnodeId());
                    actYwEtAuditNum.setProId(provinceProModel.getId());
                    actYwEtAuditNumService.deleteByProId(actYwEtAuditNum);
                }
            }else{
                if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
                    roles.clear();
                    roles.add("assignUser");
                } else {
                    roles=getProvUsersByRoleList(roleList,CoreIds.NPR_SYS_TENANT.getId());
                }
                if (roles.size() <= 0) {
                    throw new ActYwRuntimeException("找不到流程下一步审核人员，请联系管理员!");
                }
            }
        }
        Map<String, Object> vars = new HashMap<String, Object>();
        vars = provinceProModel.getVars();
        if (actYwNextGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwNextGnode.getTaskType())) {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId, roles);
        } else {
            vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nodeRoleId + "s", roles);
        }
        String key = ActYw.getPkey(actYw);
        User user = UserUtils.getUser();
        identityService.setAuthenticatedUserId(user.getId());
        vars.put("gnodeVesion", IdGen.uuid());
        ProcessInstance procIns = runtimeService.
                startProcessInstanceByKeyAndTenantId(key, "province_pro_model:" + provinceProModel.getId(), vars,
                        actYw.getTenantId());
        // 流程id返写业务表
        if (procIns == null) {
            throw new ActYwRuntimeException("流程配置故障（审核流程未启动），请联系管理员!");
        }
        Act act = new Act();
        act.setBusinessTable("province_pro_model");// 业务表名
        act.setBusinessId(provinceProModel.getId()); // 业务表ID
        act.setProcInsId(procIns.getId());
        actDao.updateProcInsIdByBusinessId(act);
        provinceProModel.setProcInsId(act.getProcInsId());
        try{
            provinceProModelService.save(provinceProModel);
        }catch (Exception e){
            throw new GroupErrorException("推送异常");
        }
        //添加申报消息
        if (actYwNextGnode.getIsAssign() != null && actYwNextGnode.getIsAssign()) {
            Role role = coreService.getByRtypeOfProvince(CoreSval.Rtype.ADMIN_PN.getKey(),provinceProModel.getTenantId());
            roles = userService.findListByRoleId(role.getId());
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "项目申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，请你指派专家审核。",
                    OaNotify.Type_Enum.TYPE18.getValue(), provinceProModel.getId());
        }else{
            oaNotifyService.sendOaNotifyByTypeAndUser(user, roles, "项目申报", user.getName() + "申报" + actYw.getProProject().getProjectName()+"，需要你审核。",
                                OaNotify.Type_Enum.TYPE18.getValue(), provinceProModel.getId());
        }
    }
    /**
     * 过滤审核记录 有菜单权限的非管理人员，当前正在进行的节点，只能看到自已的审核记录，已经完成的节点，参与过的，所有人的审核记录均可见；
     * 管理员不受限制
     *
     * @param list
     * @param proModel
     */
    private List<ActYwAuditInfo> filter(List<ActYwAuditInfo> list, ProModel proModel) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        User user = UserUtils.getUser();
        if (UserUtils.isNscAdminOffice(user)) {
            return list;
        }
        if (Const.YES.equals(proModel.getState())) {// 流程已结束，每个节点一定都完成。
            return list;
        }
        ActYwAuditInfo last = list.get(list.size() - 1);
        String lastGnodeId = last.getGnodeId();
        ActYw actYw = actYwService.get(proModel.getActYwId());
        Task task = actTaskService.currentTask(ActYw.getPkey(actYw), proModel.getActYwId(), proModel.getId());// 数据当前流转到的节点task
        if (task != null) {
            if (lastGnodeId.equals(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {// 当前节点未完成
                int index = list.size() - 1;
                for (int i = list.size() - 1; i >= 0; i--) {
                    ActYwAuditInfo info = list.get(i);
                    if (!info.getGnodeId().equals(lastGnodeId)) {
                        index = i;
                        break;
                    }
                }
                if (index < list.size() - 1) {
                    List<ActYwAuditInfo> subList = new ArrayList<>();
                    for (int i = index + 1; i < list.size(); i++) {
                        subList.add(list.get(i));
                    }
                    int tempSize = subList.size();
                    Iterator<ActYwAuditInfo> iterator = subList.iterator();
                    while (iterator.hasNext()) {
                        ActYwAuditInfo entry = iterator.next();
                        if (!user.getId().equals(entry.getAuditId())) {
                            iterator.remove();
                        }
                    }
                    if (subList.size() != tempSize) {
                        List<ActYwAuditInfo> list1 = new ArrayList<>();
                        if (index > 0) {
                            for (int i = 0; i < index; i++) {
                                list1.add(list.get(i));
                            }
                        } else {
                            list1.add(list.get(0));
                        }
                        list1.addAll(list1.size(), subList);
                        return list1;
                    }
                }
            }
        }
        return list;
    }

    private boolean inTeamUserHistoryList(List<TeamUserHistory> l, String uid) {
        if (StringUtil.isEmpty(uid)) {
            return false;
        }
        if (l == null || l.size() == 0) {
            return false;
        }
        for (TeamUserHistory t : l) {
            if (t.getUser()!=null && uid.equals(t.getUser().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 审核记录，带平均分
     *
     * @param proModelId
     * @param filter     学生端查看和管理员后端查看不需要过滤
     * @return
     */
    public List<ActYwAuditInfo> getActYwAuditInfo(String proModelId, boolean filter) {
       ProModel proModel = this.get(proModelId);
        if(proModel==null){
            return null;
        }
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModelId);
        List<ActYwAuditInfo> list = actYwAuditInfoService.findList(actYwAuditInfo);// 审核记录，按时间正序排列
        List<ActYwAuditInfo> newlist=new ArrayList<ActYwAuditInfo>();
        TeamUserHistory teamUserHistory = new TeamUserHistory();
        teamUserHistory.setProId(proModelId);
        teamUserHistory.setUserType("2");
        List<TeamUserHistory> l = teamUserHistoryDao.findList(teamUserHistory);
        // 去掉中期报告、结项报告等记录
        list = list.stream().filter(e -> !proModel.getDeclareId().equals(e.getAuditId())
//                && !inTeamUserHistoryList(l, e.getAuditId())
        ).collect(Collectors.toList());
//        if (filter) {
//            list = this.filter(list, proModel);// 过滤
//        }
        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); i++){
                ActYwAuditInfo info = list.get(i);
                String gnodeId=info.getGnodeId();
                Boolean isFirst =true;
                if(StringUtil.isNotEmpty(gnodeId)){
                    ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
                    if(actYwGnode.getGforms()!=null){
                        List<ActYwGform> curlist=actYwGnode.getGforms();
                        for(ActYwGform actYwGform:curlist){
                            if(FormClientType.FST_FRONT.getKey().equals(actYwGform.getForm().getClientType())){
                                isFirst=false;
                                break;
                            }
                        }
                    }
                }
                if(isFirst){
                    newlist.add(info);
                }
            }
            String lastGondeId = newlist.get(newlist.size() - 1).getGnodeId();
            Map<Integer, Map<String, Object>> temp = new LinkedHashMap<>();
            for (int i = 0; i < newlist.size(); i++) {
                List<ActYwAuditInfo> ywAuditInfos = new ArrayList<>();
                ActYwAuditInfo info = newlist.get(i);
                ywAuditInfos.add(info);
                info.setResult(this.getStateByAuditInfo(ywAuditInfos));
                if (StringUtils.isBlank(info.getGrade())) {
                    // 记录list中需要插入平均值的索引位置和相关的数据
                    if (info.getGnodeId().equals(lastGondeId)) {
                        ActYw actYw = actYwService.get(proModel.getActYwId());
                        Task task = actTaskService.currentTask(ActYw.getPkey(actYw), proModel.getActYwId(),
                                proModel.getId());// 数据当前流转到的节点task
                        if (task != null) {
                            if (lastGondeId.equals(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {// 当前节点未完成
                                continue;
                            }
                        }
                    }
                    Map<String, Object> map = temp.get(i);
                    if (map == null ||
                            !(
                                (info.getGnodeVesion() == null && info.getGnodeId().equals(map.get("gnodeId")))
                                ||
                                (info.getGnodeVesion() != null && info.getGnodeVesion().equals(map.get("gnodeVesion"))
                                )
                            )
                    ) {
                        map = new HashMap<>(4);
                        map.put("num", 1);
                        map.put("gnodeVesion", info.getGnodeVesion());
                        map.put("total", info.getScore());
                    } else {
                        map.put("gnodeVesion", info.getGnodeVesion());
                        map.put("num", Integer.valueOf((int) map.get("num")) + 1);
                        map.put("total", info.getScore() + (double) map.get("total"));
                        temp.remove(i);
                    }
                    map.put("gnodeId", info.getGnodeId());
                    map.put("name", info.getAuditName());
                    temp.put(i + 1, map);
                }
            }
            // 插入平均值记录
            if (!temp.isEmpty()) {
                DecimalFormat df = new DecimalFormat("#.0");
                int count = 0;
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = temp.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Map<String, Object>> entry = iterator.next();
                    Map<String, Object> map = entry.getValue();
                    double total = (double) map.get("total");
                    int num = (int) map.get("num");
                    String average = df.format(total / num);
                    ActYwAuditInfo averageInfo = new ActYwAuditInfo();
                    averageInfo.setAuditName((String)map.get("name"));
                    averageInfo.setResult(average);
                    newlist.add(entry.getKey() + count, averageInfo);
                    count++;
                }
            }
        }
        return newlist;
    }



    public List<ActYwAuditInfo> getProvinceActYwAuditInfo(String proModelId, boolean filter) {
        ProvinceProModel provinceProModel = provinceProModelService.get(proModelId);
        if (provinceProModel == null){
            return null;
        }
        ProModel proModel = provinceProModel.getProModel();
        if(proModel==null){
            return null;
        }
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModelId);
        List<ActYwAuditInfo> list = actYwAuditInfoService.findList(actYwAuditInfo);// 审核记录，按时间正序排列
        List<ActYwAuditInfo> newlist=new ArrayList<ActYwAuditInfo>();
        TeamUserHistory teamUserHistory = new TeamUserHistory();
        teamUserHistory.setProId(proModelId);
        teamUserHistory.setUserType("2");
        List<TeamUserHistory> l = teamUserHistoryDao.findList(teamUserHistory);
        // 去掉中期报告、结项报告等记录
        list = list.stream().filter(e -> !proModel.getDeclareId().equals(e.getAuditId())
//                && !inTeamUserHistoryList(l, e.getAuditId())
        ).collect(Collectors.toList());
//        if (filter) {
//            list = this.filter(list, proModel);// 过滤
//        }
        if (!list.isEmpty()) {
            for(int i = 0; i < list.size(); i++){
                ActYwAuditInfo info = list.get(i);
                String gnodeId=info.getGnodeId();
                Boolean isFirst =true;
                if(StringUtil.isNotEmpty(gnodeId)){
                    ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
                    if(actYwGnode.getGforms()!=null){
                        List<ActYwGform> curlist=actYwGnode.getGforms();
                        for(ActYwGform actYwGform:curlist){
                            if(FormClientType.FST_FRONT.getKey().equals(actYwGform.getForm().getClientType())){
                                isFirst=false;
                                break;
                            }
                        }
                    }
                }
                if(isFirst){
                    newlist.add(info);
                }
            }
            String lastGondeId = newlist.get(newlist.size() - 1).getGnodeId();
            Map<Integer, Map<String, Object>> temp = new LinkedHashMap<>();
            for (int i = 0; i < newlist.size(); i++) {
                List<ActYwAuditInfo> ywAuditInfos = new ArrayList<>();
                ActYwAuditInfo info = newlist.get(i);
                ywAuditInfos.add(info);
                info.setResult(this.getStateByAuditInfo(ywAuditInfos));
                if (StringUtils.isBlank(info.getGrade())) {
                    // 记录list中需要插入平均值的索引位置和相关的数据
                    if (info.getGnodeId().equals(lastGondeId)) {
                        ActYw actYw = actYwService.get(proModel.getActYwId());
                        Task task = actTaskService.currentTask(ActYw.getPkey(actYw), proModel.getActYwId(),
                                proModel.getId());// 数据当前流转到的节点task
                        if (task != null) {
                            if (lastGondeId.equals(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {// 当前节点未完成
                                continue;
                            }
                        }
                    }
                    Map<String, Object> map = temp.get(i);
                    if (map == null || ! (info.getGnodeId().equals(map.get("gnodeId"))
                            && info.getGnodeVesion()!=null
                            && info.getGnodeVesion().equals(map.get("gnodeVesion")))) {
                        map = new HashMap<>(4);
                        map.put("num", 1);
                        map.put("gnodeVesion", info.getGnodeVesion());
                        map.put("total", info.getScore());
                    } else {
                        map.put("gnodeVesion", info.getGnodeVesion());
                        map.put("num", Integer.valueOf((int) map.get("num")) + 1);
                        map.put("total", info.getScore() + (double) map.get("total"));
                        temp.remove(i);
                    }
                    map.put("gnodeId", info.getGnodeId());
                    map.put("name", info.getAuditName());
                    temp.put(i + 1, map);
                }
            }
            // 插入平均值记录
            if (!temp.isEmpty()) {
                DecimalFormat df = new DecimalFormat("#.0");
                int count = 0;
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = temp.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Map<String, Object>> entry = iterator.next();
                    Map<String, Object> map = entry.getValue();
                    double total = (double) map.get("total");
                    int num = (int) map.get("num");
                    String average = df.format(total / num);
                    ActYwAuditInfo averageInfo = new ActYwAuditInfo();
                    averageInfo.setAuditName((String)map.get("name"));
                    averageInfo.setResult(average);
                    newlist.add(entry.getKey() + count, averageInfo);
                    count++;
                }
            }
        }
        return newlist;
    }

    /**
     * 审核记录，带平均分
     *
     * @param proModelId
     * @param filter     学生端查看和管理员后端查看不需要过滤
     * @return
     */
    public List<ActYwAuditInfo> getFrontActYwAuditInfo(String proModelId, boolean filter) {
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModelId);
        List<ActYwAuditInfo> list = actYwAuditInfoService.findList(actYwAuditInfo);// 审核记录，按时间正序排列
        ProModel proModel = this.get(proModelId);
        TeamUserHistory teamUserHistory = new TeamUserHistory();
        teamUserHistory.setProId(proModelId);
        teamUserHistory.setUserType("2");
        List<TeamUserHistory> turList = teamUserHistoryDao.findList(teamUserHistory);
        // 去掉中期报告、结项报告等记录
        list = list.stream().filter(e -> !proModel.getDeclareId().equals(e.getAuditId())
//                && !inTeamUserHistoryList(turList, e.getAuditId())
        ).collect(Collectors.toList());
//        if (filter) {
//            list = this.filter(list, proModel);// 过滤
//        }
        if (!list.isEmpty()) {
            String lastGondeId = list.get(list.size() - 1).getGnodeId();
            Map<Integer, Map<String, Object>> temp = new LinkedHashMap<>();
            for (int i = 0; i < list.size(); i++) {
                List<ActYwAuditInfo> ywAuditInfos = new ArrayList<>();
                ActYwAuditInfo info = list.get(i);
                ywAuditInfos.add(info);
                info.setResult(this.getFrontStateByAuditInfo(ywAuditInfos));

                if (StringUtils.isBlank(info.getGrade())) {// 记录list中需要插入平均值的索引位置和相关的数据
                    if (info.getGnodeId().equals(lastGondeId)) {
                        ActYw actYw = actYwService.get(proModel.getActYwId());
                        Task task = actTaskService.currentTask(ActYw.getPkey(actYw), proModel.getActYwId(),
                                proModel.getId());// 数据当前流转到的节点task
                        if (task != null) {
                            if (lastGondeId.equals(task.getTaskDefinitionKey().replace(ActYwTool.FLOW_ID_PREFIX, ""))) {// 当前节点未完成
                                continue;
                            }
                        }
                    }
                    Map<String, Object> map = temp.get(i);
                    if (map == null || !info.getGnodeId().equals(map.get("gnodeId"))) {
                        map = new HashMap<>(4);
                        map.put("num", 1);
                        map.put("total", info.getScore());
                    } else {
                        map.put("num", Integer.valueOf((int) map.get("num")) + 1);
                        map.put("total", info.getScore() + (double) map.get("total"));
                        temp.remove(i);
                    }
                    map.put("gnodeId", info.getGnodeId());
                    map.put("name", info.getAuditName());
                    temp.put(i + 1, map);
                }
            }

            // 插入平均值记录
            if (!temp.isEmpty()) {
                DecimalFormat df = new DecimalFormat("#.0");
                int count = 0;
                Iterator<Map.Entry<Integer, Map<String, Object>>> iterator = temp.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, Map<String, Object>> entry = iterator.next();
                    Map<String, Object> map = entry.getValue();
                    double total = (double) map.get("total");
                    int num = (int) map.get("num");
                    String average = df.format(total / num);
                    ActYwAuditInfo averageInfo = new ActYwAuditInfo();
                    averageInfo.setAuditName((String)map.get("name"));
                    averageInfo.setResult(average);
                    list.add(entry.getKey() + count, averageInfo);
                    count++;
                }
            }
        }
        return list;
    }

    public List<ProModel> findIsHaveData(String actYwId) {
        // 查询是否有数据的数据
        return dao.findIsHaveData(actYwId);
    }

    public List<ProModel> findIsOverHaveData(String actYwId, String state) {
        // 查询没有完结的数据
        return dao.findIsOverHaveData(actYwId, state);
    }

    @Transactional(readOnly = false)
    public JSONObject reportSubmit(ProReport proReport, String gnodeId) {
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (proReport == null) {
            js.put("msg", "提交失败，该表单为空");
            return js;
        }
        // 回退网关 重新提交报告会涉及
        // if (proReportService.getByGnodeId(proReport.getProModelId(),
        // proReport.getGnodeId()) != null) {
        // js.put("msg", "提交失败，该表单已经提交过");
        // return js;
        // }
        // ProReport report = new ProReport();
        // report.setPromodelId(proModel.getId());
        proReport.setGnodeId(gnodeId);
        proReportService.save(proReport);
        // proModel.getAttachMentEntity().setGnodeId(gnodeId);
        AttachMentEntity attachMentEntity = proReport.getAttachMentEntity();
        if(attachMentEntity != null){
            proSysAttachmentService.saveByVo(proReport, FileTypeEnum.S11);
        }else {
            List<SysAttachment> sysAttachmentList = proReport.getFiles();
            proSysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proReport.getProModelId(), FileTypeEnum.S11, FileStepEnum.S1103);
        }

        ProModel pm = this.get(proReport.getProModelId());
        if (!Const.YES.equals(pm.getState())) {
            // actTaskService.runNextProcess(this.get(proReport.getProModelId()));
            try {
                auditWithGateWay(pm, gnodeId);
            } catch (GroupErrorException e) {
                js.put("msg", e.getCode());
                js.put("ret", 0);
                return js;
            }
            // actTaskService.runNextProcessByGnodeId(this.get(proReport.getProModelId()),gnodeId);
        }
        js.put("msg", "提交成功");
        js.put("ret", 1);
        return js;
    }

    public List<ProModel> getListByGroupId(String groupId) {
        return dao.getListByGroupId(groupId);
    }

    public String dealPageApplay(FormPageType fpageType, Model model, HttpServletRequest request,HttpServletResponse response, ProModel proModel, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            if ((FormTheme.F_MR).equals(formTheme)) {
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, proSysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else if ((FormTheme.F_MD).equals(formTheme)) {
                if (FlowProjectType.PMT_XM.equals(actYw.getFptype())) {
                    // 参数实现已经移动至实现类FppMd
                    fpage.getParam().init(model, request, response, new Object[]{});
                    fpage.getParam().initSysAttachment(model, request, response,
                            new Object[]{proModel, proSysAttachmentService, proModelMdService});
                    return FormPage.getAbsUrl(actYw, fpageType, null);
                } else {
                    fpage.getParam().init(model, request, response, new Object[]{});
                    fpage.getParam().initSysAttachment(model, request, response,
                            new Object[]{proModel, proSysAttachmentService});
                    return FormPage.getAbsUrl(actYw, fpageType, null);
                }
            } else if ((FormTheme.F_COM).equals(formTheme)) {
                // 参数实现已经移动至实现类FppCom
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, proSysAttachmentService});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            } else {
                logger.error("当前流程主题未定义！");
            }
        } else {
            logger.error("流程主题不存在！");
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    //通过groupId和proInsId得到ProModelNodeVo对象（时间轴相关数据）
    public List<ProModelNodeVo> getProModelNodeVoByGroupIdAndPromodel(String yearId, String ppId, String groupId, String proModelId) {
        ProModel proModel = get(proModelId);
        ActYw actYw = actYwService.get(proModel.getActYwId());
        if(StringUtil.isEmpty( proModel.getProcInsId())){
            return null;
        }
        List<ActYwGnode> newGview = proActYwGnodeService.queryGnodeByAuditList(yearId, ppId, groupId, proModelId, proModel.getProcInsId());
        //List<ProModelNodeVo> proModelNodeVoList=actYwGnodeService.getProModelNodeVoByGroupIdAndProInsId();
        List<ProModelNodeVo> proModelNodeVoList = Lists.newArrayList();
        for (int i = 0; i < newGview.size(); i++) {
            ProModelNodeVo proModelNodeVo = new ProModelNodeVo();
            ActYwGnode gnodeView = newGview.get(i);
            if(gnodeView!=null){
                if(gnodeView.getActYwGtime()!=null&&gnodeView.getActYwGtime().getBeginDate()!=null){
                    proModelNodeVo.setBeginDate(gnodeView.getActYwGtime().getBeginDate());
                    proModelNodeVo.setEndDate(gnodeView.getActYwGtime().getEndDate());
                }
                proModelNodeVo.setId(gnodeView.getId());
                proModelNodeVo.setName(gnodeView.getName());
            }
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            ActYwGnode curGnode = new ActYwGnode();
            if (StringUtil.isNotEmpty(proModel.getEndGnodeId())) {
                actYwAuditInfo.setGnodeId(proModel.getEndGnodeId());
                curGnode = actYwGnodeService.get(proModel.getEndGnodeId());
            } else {
                actYwAuditInfo.setGnodeId(gnodeView.getId());
                curGnode = gnodeView;
            }
            actYwAuditInfo.setPromodelId(proModelId);
            String res = getStateByAuditInfo(curGnode, actYwAuditInfoService.findList(actYwAuditInfo));
            if (StringUtil.isNotEmpty(res)) {
                proModelNodeVo.setResult(gnodeView.getName() + res);
            }
            proModelNodeVoList.add(proModelNodeVo);
        }
        return proModelNodeVoList;
    }

    public boolean hasNodeSubmit(String proModelId, String gnodeId) {
        if (proReportService.getByGnodeId(proModelId, gnodeId) != null) {
            return true;
        }
        if (proMidSubmitService.getByGnodeId(proModelId, gnodeId) != null) {//民大项目的判断
            return true;
        }
        if (proCloseSubmitService.getByGnodeId(proModelId, gnodeId) != null) {//民大项目的判断
            return true;
        }
        return false;
    }

    /**
     * 只查promodel表
     *
     * @param proModel
     * @return
     */
    public List<String> findListByIdsWithoutJoin(ProModel proModel) {
        return dao.findListByIdsWithoutJoin(proModel);
    }

    public List<ProModel> findImportList(ProModel proModel) {
        return dao.findImportList(proModel);
    }

    @Transactional(readOnly = false)
    public void promodelDeleteList(String[] idList) {
        for(int i=0;i<idList.length;i++){
            ProModel proModel=get(idList[i]);
            promodelDelete(proModel);
        }
    }

    @Transactional(readOnly = false)
    public void deleteProModels(List<ProModel> idList) {
       if (idList.size() > 0){
           idList.forEach(item->promodelDelete(item));
       }
    }

    @Transactional(readOnly = false)
    public void promodelDelete(ProModel proModel) {
        if (StringUtils.isNotBlank(proModel.getProcInsId())) {
            try {
                runtimeService.deleteProcessInstance(proModel.getProcInsId(), "");
            } catch (Exception e) {
                System.out.println("该项目的工作流已经走完");
            }
            try {
                historyService.deleteHistoricProcessInstance(proModel.getProcInsId());
            } catch (Exception e) {
                System.out.println("该项目没有历史记录");
            }
        }
        //删除委派和指派记录
        ActYwGassign actYwGassign=new ActYwGassign();
        actYwGassign.setPromodelId(proModel.getId());
        actYwGassignService.deleteByAssign(actYwGassign);

        //删除项目
        delete(proModel);
        //删除团队历史记录表
        dao.deleteTeamUserHisByProModelId(proModel.getId());
        //删除报告信息表数据
        dao.deleteReportByProModelId(proModel.getId());
    }

    //查询项目可以变更节点
    public List<ActYwAuditInfo> getProModelChangeGnode(ProModel proModel) {
		ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
		actYwAuditInfo.setPromodelId(proModel.getId());
		List<ActYwAuditInfo> auditList=	actYwAuditInfoService.getProModelChangeGnode(actYwAuditInfo);
		List<ActYwAuditInfo> newList=new ArrayList<ActYwAuditInfo>();
		if(proModel.getProcInsId()!=null && auditList!=null){
			ActYwGnode curr= proActTaskService.getNodeByProInsId(proModel.getProcInsId());
            if(curr==null){
                return newList;
            }
			for(int i=0;i<auditList.size();i++){
				if(curr.getId().equals(auditList.get(i).getGnodeId())){
					break;
				}
				newList.add(auditList.get(i));
			}
		}
		return newList;
	}

    //查询项目可以变更节点
    public List<ActYwAuditInfo> getProModelChangeGnode(ProvinceProModel provinceProModel) {
        ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(provinceProModel.getId());
        List<ActYwAuditInfo> auditList=	actYwAuditInfoService.getProModelChangeGnode(actYwAuditInfo);
        List<ActYwAuditInfo> newList=new ArrayList<ActYwAuditInfo>();
        if(provinceProModel.getProcInsId()!=null && auditList!=null){
            ActYwGnode curr= proActTaskService.getNodeByProInsId(provinceProModel.getProcInsId());
            if(curr==null){
                return newList;
            }
            for(int i=0;i<auditList.size();i++){
                if(curr.getId().equals(auditList.get(i).getGnodeId())){
                    break;
                }
                newList.add(auditList.get(i));
            }
        }
        return newList;
    }



    //删除项目变更之前的指派数据
    public void deleteAssignByGnode(ProModel proModel,String gnodeId) {
        ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModel.getId());
        List<ActYwAuditInfo> auditList=	actYwAuditInfoService.getProModelChangeGnode(actYwAuditInfo);
        if(proModel.getProcInsId()!=null && auditList!=null){
            boolean isDelGnode=false;
            int min=0;
            for(int i=0;i<auditList.size();i++){
                if(gnodeId.equals(auditList.get(i).getGnodeId()) && min==0){
                    isDelGnode=true;
                    min++;
                }
                //变更改变指派记录
                if(isDelGnode){
                    ActYwGnode gnode = actYwGnodeService.get(auditList.get(i).getGnodeId());
                    if (gnode.getIsAssign()) {
                        ActYwGassign actYwGassign = new ActYwGassign();
                        actYwGassign.setPromodelId(proModel.getId());
                        actYwGassign.setGnodeId(gnode.getId());
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                }
            }
        }
    }

    //将项目变更到相应的节点
    //proModelId 项目id
    // gnodeId 变更节点的id
    @Transactional(readOnly = false)
    public void promodelChangeAct(String proModelId, String gnodeId) {
        ProModel proModel = get(proModelId);
        ActYwGnode actYwGnode = actYwGnodeService.getByg(gnodeId);
        if (proModel != null && actYwGnode != null) {
            String taskId = actTaskService.getTaskidByProcInsId(proModel.getProcInsId());
            List<String> roleAll = new ArrayList<String>();
            String userGnode = "";
            String roleIds = "";
            List<Role> roleList = actYwGnode.getRoles();
            if (roleList == null) {
               return;
            }
            //多角色配置人员
            roleIds = StringUtil.listIdToStr(roleList, StringUtil.LINE_D);
            if (actYwGnode.getIsAssign() != null && actYwGnode.getIsAssign()) {
                roleAll.clear();
                roleAll.add("assignUser");
                //删除旧的指派记录
                ActYwGassign actYwGassign =new ActYwGassign();
                actYwGassign.setPromodelId(proModel.getId());
                Boolean isHasAssign=actYwGassignService.isHasAssign(actYwGassign);
                //已经被指派过
                if(isHasAssign){
                 //删除旧的指派记录
                 actYwGassignService.deleteByAssign(actYwGassign);
                }
            } else {
                //删除委派记录
                if (actYwGnode.getIsDelegate() != null && actYwGnode.getIsDelegate()) {
                    ActYwGassign actYwGassign =new ActYwGassign();
                    actYwGassign.setPromodelId(proModel.getId());
                    actYwGassign.setGnodeId(gnodeId);
                    Boolean isHasAssign=actYwGassignService.isHasAssign(actYwGassign);
                    //已经被委派过
                    if(isHasAssign){
                    //删除旧的委派记录
                    actYwGassignService.deleteByAssign(actYwGassign);
                    }
                }

                for (int i = 0; i < roleList.size(); i++) {
                    List<String> roles = new ArrayList<String>();
                    Role role = roleList.get(i);
                    // 启动节点
                    String roleName = role.getName();
                    if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                        roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
                    } else {
                        roles = userService.findListByRoleId(role.getId());
                    }
                    // 学生角色id 清除其他人id 只保留申报学生id
                    if (role.getId().equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                        roles.clear();
                        roles.add(userService.findUserById(proModel.getDeclareId()).getId());
                    }
                    roleAll.addAll(roles);
                }
                roleAll=removeDuplicate(roleAll);
            }
            if (actYwGnode != null && GnodeTaskType.GTT_NONE.getKey().equals(actYwGnode.getTaskType())) {
                userGnode = ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds;
            } else {
                userGnode = ActYwTool.FLOW_ROLE_ID_PREFIX + roleIds + "s";
            }
            ActYwGnode cgnode = proActTaskService.getNodeByProInsId(proModel.getProcInsId());
            //流程变更节点
            String res=actTaskService.rollBackFlow(taskId, userGnode, roleAll, ActYwTool.FLOW_ID_PREFIX + actYwGnode.getId());
            if("SUCCESS".equals(res)){
                //删除当前节点的指派记录aszsx
                if (StringUtil.isNotEmpty(cgnode.getId())) {
                    if(cgnode.getIsAssign()){
                        ActYwGassign actYwGassign =new ActYwGassign();
                        actYwGassign.setPromodelId(proModel.getId());
                        actYwGassign.setGnodeId(cgnode.getId());
                        actYwGassignService.deleteByAssign(actYwGassign);
                    }
                }
                //删除指派记录
                deleteAssignByGnode(proModel,gnodeId);
            }
        }
    }

    @Override
    public String viewForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ActYw actYw) {
        FormTheme formTheme = actYw.getFtheme();
        ProModel proModel1 = dao.get(proModel.getId());
        model.addAttribute("proModel", proModel1);
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            //参数实现已经移动至实现类FppMd
            fpage.getParam().init(model, request, response, new Object[]{});
            fpage.getParam().initSysAttachment(model, request, response, new Object[]{proModel, proSysAttachmentService, this});
            return FormPage.getAbsUrl(actYw, fpageType, null);
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Override
    public void audit(String gnodeId,String proModelId, Model model) {
        ProModel proModel = dao.getWith(proModelId);
        SysAttachment sa = new SysAttachment();
        sa.setUid(proModel.getId());
        List<SysAttachment> fileListMap = proSysAttachmentService.getFiles(sa);
        if (fileListMap != null) {
            model.addAttribute("sysAttachments", fileListMap);
        }
        model.addAttribute("proModel", proModel);
    }

    public String applayForm(FormPageType fpageType, Model model, HttpServletRequest request, HttpServletResponse response, ProModel proModel, ProProject proProject, ActYw actYw) {
        User user = UserUtils.getUser();
        if (!org.springframework.util.StringUtils.isEmpty(proModel.getId())) {
            proModel = dao.get(proModel.getId());
        }
        if (actYw != null && proProject != null && ProSval.PRO_TYPE_PROJECT.equals(proProject.getProType())
                && StringUtil.isNotEmpty(proProject.getType())) {
            model.addAttribute("proModelType", DictUtils.getDictLabel(proProject.getType(), "project_style", ""));
        }
		if (org.springframework.util.StringUtils.isEmpty(proModel.getLogoUrl())){
			if (proModel.getLogo() != null && StringUtil.isNotEmpty(proModel.getLogo().getFtpUrl())){
				proModel.setLogoUrl(proModel.getLogo().getUrl());
			}
		}
        proModel.setActYwId(actYw.getId());
        proModel.setCreateDate(new Date());
        model.addAttribute("proModel", proModel);
        model.addAttribute("teams", projectDeclareService.findTeams(user.getId(), ""));
        model.addAttribute("leader", user);
        model.addAttribute("cuser", user);
        model.addAttribute("sse", user);
        model.addAttribute("isSubmit", 0);
        model.addAttribute("wprefix", IWparam.getFileTplPreFix());
        model.addAttribute("wtypes", Wtype.toJson());
        FormTheme formTheme = actYw.getFtheme();
        if (formTheme != null) {
            FormPage fpage = FormPage.getByKey(formTheme, actYw.getFptype().getKey(), fpageType.getKey());
            if (FlowProjectType.PMT_XM.equals(actYw.getFptype())) {
                // 参数实现已经移动至实现类FppMd
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, proSysAttachmentService, this});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }
            else {
                fpage.getParam().init(model, request, response, new Object[]{});
                fpage.getParam().initSysAttachment(model, request, response,
                        new Object[]{proModel, proSysAttachmentService,this});
                return FormPage.getAbsUrl(actYw, fpageType, null);
            }
        } else {
            logger.error("流程主题不存在！");
        }
        return CorePages.ERROR_404.getIdxUrl();
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        dao.myDelete(id);
    }

    @Override
    public void audit(ProModel proModel, Model model, SysAttachment attachment) {}

    @Override
    public Class<ProModel> getTClass() {
        return ProModel.class;
    }

    @Override
    public void saveAddPro(ProModel proModel) {}

    @Override
    public void auditByGateWay(ProModel proModel, String gnodeId, HttpServletRequest request) {
        auditWithGateWay(proModel, gnodeId);
    }

    @Override
    public void gcontestEdit(ProModel proModel, HttpServletRequest request, Model model) {}

    @Override
    public void projectEdit(ProModel proModel, HttpServletRequest request, Model model) {}

    public Integer getByNumberAndId(String number, String id) {
        return dao.getByNumberAndId(number, id);
    }


    public Integer checkNumberByActYwId(String number, String id,String actYwId) {
        return dao.checkNumberByActYwId(number, id,actYwId);
    }

    @Transactional(readOnly = false)
    public JSONObject saveProjectEdit(ProModel proModel,HttpServletRequest reuqest) throws Exception {
        JSONObject js = new JSONObject();
        List<TeamUserHistory> stus = proModel.getStudentList();
        List<TeamUserHistory> teas = proModel.getTeacherList();
        String actywId = proModel.getActYwId();
        String teamId = proModel.getTeamId();
        String proId = proModel.getId();
        String category = proModel.getType();
        js = commonService.checkProjectOnModify(stus, teas, proId, actywId, category, teamId, proModel.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }


        saveForEdit(proModel);
        commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);

        //变更第一导师转交任务
        Team newTeam=proModel.getTeam();
        Team oldTeam=teamService.get(proModel.getTeamId());
        if(newTeam.getFirstTeacher()!=null && oldTeam.getFirstTeacher()!=null
                &&  (!newTeam.getFirstTeacher().equals(oldTeam.getFirstTeacher()))){
            TaskQuery todoTaskQuery =  taskService.createTaskQuery().taskAssignee(oldTeam.getFirstTeacher()).active();
            List<Task> todoList = todoTaskQuery.list();
            for(Task task:todoList){
                taskService.setAssignee(task.getId(),newTeam.getFirstTeacher());
            }
            ProModel newPro= get(proModel.getId());
            Team team=teamService.get(newPro.getTeamId());
            team.setFirstTeacher(newTeam.getFirstTeacher());
            teamService.updateAllInfo(team);
        }
        AttachMentEntity att=new AttachMentEntity();
        String isImport=proModel.getImpdata();
        if("1".equals(isImport)){
            if(proModel.getAttachMentEntity()!=null){
                att =proModel.getAttachMentEntity();
            }else{
                if(proModel.getProModel()!=null && proModel.getProModel().getAttachMentEntity()!=null) {
                    att = proModel.getProModel().getAttachMentEntity();
                }
            }
        }else{
            if(proModel.getAttachMentEntity()!=null){
                att =proModel.getAttachMentEntity();
            }else{
                if(proModel.getProModel()!=null && proModel.getProModel().getAttachMentEntity()!=null) {
                    att = proModel.getProModel().getAttachMentEntity();
                }else{
                    throw new RuntimeException("附件不能为空");
                }
            }
        }

        if(StringUtil.checkNotEmpty(att.getFielFtpUrl())){
            proSysAttachmentService.saveByVo(att, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        }

        //流程变更
        if (StringUtil.isNotEmpty(proModel.getToGnodeId())) {
            promodelChangeAct(proModel.getId(), proModel.getToGnodeId());
        }
        js.put("msg", "保存成功");
        return js;
    }


    @Transactional(readOnly = false)
    public JSONObject saveProModelEdit(ProModel proModel) throws Exception {
        JSONObject js = new JSONObject();
        List<TeamUserHistory> stus = proModel.getStudentList();
        List<TeamUserHistory> teas = proModel.getTeacherList();
        String actywId = proModel.getActYwId();
        String teamId = proModel.getTeamId();
        String proId = proModel.getId();
        String category = proModel.getType();
        js = commonService.checkProjectOnModify(stus, teas, proId, actywId, category, teamId, proModel.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }


        saveForEdit(proModel);
        commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);

        //变更第一导师转交任务
        Team newTeam=proModel.getTeam();
        Team oldTeam=teamService.get(proModel.getTeamId());
        if(newTeam.getFirstTeacher()!=null && oldTeam.getFirstTeacher()!=null
                &&  (!newTeam.getFirstTeacher().equals(oldTeam.getFirstTeacher()))){
            TaskQuery todoTaskQuery =  taskService.createTaskQuery().taskAssignee(oldTeam.getFirstTeacher()).active();
            List<Task> todoList = todoTaskQuery.list();
            for(Task task:todoList){
                taskService.setAssignee(task.getId(),newTeam.getFirstTeacher());
            }
            ProModel newPro= get(proModel.getId());
            Team team=teamService.get(newPro.getTeamId());
            team.setFirstTeacher(newTeam.getFirstTeacher());
            teamService.updateAllInfo(team);
        }
        AttachMentEntity att=new AttachMentEntity();
        String isImport=proModel.getImpdata();
        //单用通用项目， isImport这段代码无用
        if("1".equals(isImport)){
            if(proModel.getAttachMentEntity()!=null){
                att =proModel.getAttachMentEntity();
            }else{
                if(proModel.getProModel()!=null && proModel.getProModel().getAttachMentEntity()!=null) {
                    att = proModel.getProModel().getAttachMentEntity();
                }
            }
        }else{
            List<SysAttachment> sysAttachmentList = proModel.getFileInfo();
            if(sysAttachmentList.size() == 0){
                js.put("ret", "0");
                js.put("msg", "附件不能为空");
                return js;
            }
            proSysAttachmentService.saveBySysAttachmentVo(sysAttachmentList, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
//            if(proModel.getAttachMentEntity()!=null){
//                att =proModel.getAttachMentEntity();
//            }else{
//                if(proModel.getProModel()!=null && proModel.getProModel().getAttachMentEntity()!=null) {
//                    att = proModel.getProModel().getAttachMentEntity();
//                }else{
//                    throw new RuntimeException("附件不能为空");
//                }
//            }
        }
        //流程变更
        if (StringUtil.isNotEmpty(proModel.getToGnodeId())) {
            promodelChangeAct(proModel.getId(), proModel.getToGnodeId());
        }
        js.put("msg", "保存成功");
        return js;
    }

    @Override
    public void reportForm(Model model, HttpServletRequest request, HttpServletResponse response, String proModelId) {}

    @Transactional(readOnly = false)
    public JSONObject saveGcontestEdit(ProModel proModel, HttpServletRequest request) throws Exception {
        JSONObject js = new JSONObject();
        List<TeamUserHistory> stus = proModel.getStudentList();
        List<TeamUserHistory> teas = proModel.getTeacherList();
        String actywId = proModel.getActYwId();
        String teamId = proModel.getTeamId();
        String proId = proModel.getId();
        js = commonService.checkGcontestOnModify(stus, teas, proId, actywId, teamId, proModel.getYear());
        if ("0".equals(js.getString("ret"))) {
            return js;
        }
        saveForEdit(proModel);
        commonService.disposeTeamUserHistoryForModify(stus, teas, actywId, teamId, proId);

        SysAttachment s=new SysAttachment();
        s.setUid(proModel.getId());
        s.setType(FileTypeEnum.S11);
        s.setFileStep(FileStepEnum.S1102);
        proSysAttachmentService.deleteByCdn(s);
        proSysAttachmentService.saveByVo(proModel.getAttachMentEntity(), proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1102);
        //流程变更
        if (StringUtil.isNotEmpty(proModel.getToGnodeId())) {
            promodelChangeAct(proModel.getId(), proModel.getToGnodeId());
        }else{
            ActYw actYw=actYwService.get(actywId);
            String serviceName = FormTheme.getById(actYw.getGroup().getTheme()).getServiceName();
            IWorkFlow<?, ?, ?> workFlow = WorkFlowUtil.getWorkFlowService(serviceName);
            if (workFlow != null) {
                if(workFlow instanceof ProModelService){
                    ((ProModelService)workFlow).saveFormData(proModel, request);
                }else{
                    logger.warn("服务名指定的服务类型不匹配["+serviceName+"->ProModelService]");
                }
            }else{
                logger.warn("服务名指定的服务名["+serviceName+"]未定义");
            }
        }
        js.put("msg", "保存成功");
        return js;
    }

    @Transactional(readOnly = false)
    private void saveForEdit(ProModel proModel) throws Exception {
        ProModel old = this.get(proModel.getId());
        if (old == null) {
            return;
        }
        old.setCompetitionNumber(proModel.getCompetitionNumber());
        old.setShortName(proModel.getShortName());
        old.setPName(proModel.getPName());
        old.setProCategory(proModel.getProCategory());
        old.setLevel(proModel.getLevel());
        old.setIntroduction(proModel.getIntroduction());
        old.setFinalStatus(proModel.getFinalStatus());
        old.setSubTime(proModel.getSubTime());
        this.save(old);
        if (StringUtil.isNotEmpty(proModel.getLogoUrl())) {// logo有变动
            String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
            String dblogo = sysAttachmentService.getLogoByProModelId(proModel.getId());
            if (dblogo == null){
                proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
            }
            if (dblogo != null && !dblogo.equals(proModel.getLogoUrl())){
                proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
                proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);
            }
            /*String url = StringEscapeUtils.unescapeHtml4(proModel.getLogoUrl());
            if (old.getLogo() != null) {
                proSysAttachmentService.delFile(old.getLogo().getId(), old.getLogo().getUrl());
            } else {
                old.setLogo(new SysAttachment());
            }
            proSysAttachmentService.moveTempFile(url, proModel.getId(), FileTypeEnum.S11, FileStepEnum.S1101);*/
        }
    }
    @Override
    public void saveFormData(ProModel proModel, HttpServletRequest request) {}

    @Override
    public List<ExpProModelVo> exportData(Page<ProModel> page, ProModel proModel) {
        logger.info("开始：exportData！");
        proModel.setPage(page);
        try {
            return dao.export(proModel);
        } catch (Exception e) {
            logger.info("异常：exportData！");
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return null;
    }

    /**
     * 自定义流程导出Excel获取数据的方法.
     * @param request
     * @param response

     * @param pids
     * @param tm
     * @return
     */
    public Map<String, List<IWorkRes>> exportDataMap(HttpServletRequest request, HttpServletResponse response, List<String> pids, IWorkRes tm) {
        logger.info("开始：exportDataMap！");
        Page<ProModel> page = new Page<ProModel>(request, response);
        page.setPageSize(Page.MAX_PAGE_SIZE);
        ProModel pm =(ProModel)tm;
        pm.setIds(pids);
        return IWorkFlow.exportDataMap(exportData(page, pm));
    }

    /**
     * 按学院生成数据和附件包目录.
     */
    @Override
    public void exportData(HttpServletRequest request, HttpServletResponse response, String tempPath, ActYw actyw, ActYwGnode gnode, List<String> pids, ProModel proModel) {
        logger.info("开始：生成数据！");
        proModel.setIds(pids);
        proModel.setActYwId(actyw.getId());
        Map<String, List<IWorkRes>> map = exportDataMap(request, response, pids, proModel);
        if(map == null){
            logger.warn("exportDataMap 方法处理异常，查询结果不能为空！");
            return;
        }
        logger.info("完成：生成数据，准备生成附件汇总！");
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
            expGfile.setFileName(fileName + StringUtil.LINE_D + "项目汇总表");
            expGfile.setClazz(ExpProModelVo.class);
            expGfile.setReqParam(new ItReqParam(request));
            ExportParams eparams = new ExportParams(expGfile.getFileName(), "项目汇总", expGfile.getFileType());
            eparams.setHeight((short) 7);
            eparams.setDictHandler(new DictHandler());
            eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
            expGfile.setParam(eparams);
            IWorkFlow.expExcelByOs(expGfile, map.get(key));
        }
        logger.info("生成附件汇总完成！");
    }

    /**
     * 查询列表生成数据附件.
     */
    @Override
    public void exportDataQuery(HttpServletRequest request, HttpServletResponse response, ProModel proModel) {
        Page<ProModel> curpage = new Page<ProModel>(request, response, Page.MAX_PAGE_SIZE);
        curpage.setPageSize(Page.MAX_PAGE_SIZE);
        List<ExpProModelVo> proModels = exportData(curpage, proModel);
        ExpGnodeFile expGfile = new ExpGnodeFile();
        expGfile.setFileName("大赛申报汇总表");
        expGfile.setClazz(ExpProModelVo.class);
        ExportParams eparams = new ExportParams(expGfile.getFileName(), "大赛申报汇总", expGfile.getFileType());
        eparams.setSecondTitle("学院名称：    (盖章)");
        eparams.setDictHandler(new DictHandler());
        eparams.setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        expGfile.setParam(eparams);
        IWorkFlow.expRenderView(request, response, expGfile, proModels);
    }

    /**
     * 是否为资金分配节点
     * @param proModel
     * @param model
     */
    public void moneyList(ProModel proModel,String gnodeId, Model model) {
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModel.getId());
        List<ActYwAuditInfo> list = actYwAuditInfoService.findList(actYwAuditInfo);
        if(StringUtil.checkNotEmpty(list)){
            ActYwAuditInfo audit=list.get(list.size()-1);
            String gnodeVesion=audit.getGnodeVesion();
            String lastGnodeId=audit.getGnodeId();
            if(lastGnodeId!=null && !lastGnodeId.equals(gnodeId)){
                ActYwGnode curGnode=actYwGnodeService.getByg(gnodeId);
                ActYwGnode lastGnode=actYwGnodeService.getByg(lastGnodeId);
                //判断节点标志
                Boolean isCurMoney =false;
                Boolean isLastMoney =false;
                if(curGnode.getGforms()!=null){
                    List<ActYwGform> curlist=curGnode.getGforms();
                    for(ActYwGform actYwGform:curlist){
                        if(actYwGform.getForm().getPath().contains("money")){
                            isCurMoney=true;
                            break;
                        }
                    }
                }
                if(lastGnode.getGforms()!=null){
                    List<ActYwGform> curlist=lastGnode.getGforms();
                    for(ActYwGform actYwGform:curlist){
                        if(actYwGform.getForm().getPath().contains("money")){
                            isLastMoney=true;
                            break;
                        }
                    }
                }
                if(isCurMoney && isLastMoney){
                    actYwAuditInfo.setGnodeId(lastGnodeId);
                    if(StringUtil.isNotEmpty(gnodeVesion)){
                        actYwAuditInfo.setGnodeVesion(gnodeVesion);
                    }
                    List<ActYwAuditInfo> moneyList = actYwAuditInfoService.findList(actYwAuditInfo);
                    model.addAttribute("auditMoneyList",moneyList);
                    double avg=actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo);
                    model.addAttribute("avgName",lastGnode.getName());
                    model.addAttribute("avg",avg);
                }
            }
        }
    }

    public JSONObject getJsByProInsId(ActYwGnode curGnode,String proInsId) {
        JSONObject js =new JSONObject();
        ProModel proModel=getByProInsId(proInsId);
        if (proModel == null){
            ProvinceProModel provinceProModel = provinceProModelService.getByProInsId(proInsId);
            if (provinceProModel == null){
                logger.error("流程实例："+proInsId+"查询项目不存在");
                return null;
            }
            if (provinceProModel != null){
                proModel = provinceProModel.getProModel();
                //设置省Id做查询和审核记录
                proModel.setId(provinceProModel.getId());
            }
        }
        ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
        actYwAuditInfo.setPromodelId(proModel.getId());
        List<ActYwAuditInfo> auditList=	actYwAuditInfoService.getProModelChangeGnode(actYwAuditInfo);
        List<String> gnodeIdList=new ArrayList<String>();
        if(StringUtil.checkNotEmpty(auditList)){
            for(int i=0;i<auditList.size();i++){
                gnodeIdList.add(auditList.get(i).getGnodeId());
            }
        }
        gnodeIdList.add(curGnode.getId());
        if(StringUtil.checkNotEmpty(gnodeIdList)){
            for(int i=0;i<gnodeIdList.size();i++){
                String gnodeId=gnodeIdList.get(i);
                List<Map<String,Object>> userList=new ArrayList<Map<String,Object>>();
                ActYwGnode actYwGnode=actYwGnodeService.getByg(gnodeId);
                Boolean isCur =false;
                if(curGnode.getId().equals(gnodeId)){
                    isCur=true;
                }
                Boolean isFirst =false;
                if(actYwGnode.getGforms()!=null){
                    List<ActYwGform> curlist=actYwGnode.getGforms();
                    for(ActYwGform actYwGform:curlist){
                        if(FormClientType.FST_FRONT.getKey().equals(actYwGform.getForm().getClientType())){
                            isFirst=true;
                            break;
                        }
                    }
                }
                //判断项目所处状态
                if(isFirst && isCur){
                    String userId= proModel.getDeclareId();
                    User user=UserUtils.get(userId);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("id",user.getId());
                    map.put("name",user.getName());
                    map.put("isScored",false);
                    userList.add(map);
                }else if(isFirst && !isCur){
                    String userId= proModel.getDeclareId();
                    User user=UserUtils.get(userId);
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("id",user.getId());
                    map.put("name",user.getName());
                    map.put("isScored",true);
                    userList.add(map);
                }else if(!isFirst && !isCur){
                    ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
                    actYwAuditInfoIn.setPromodelId(proModel.getId());
                    actYwAuditInfoIn.setGnodeId(gnodeId);
                    List<ActYwAuditInfo> auditListIn=	actYwAuditInfoService.findList(actYwAuditInfoIn);
                    for(ActYwAuditInfo auditInfoIn:auditListIn){
                        User user=auditInfoIn.getCreateBy();
                        user=UserUtils.get(user.getId());
                        Map<String,Object> map=new HashMap<String,Object>();
                        map.put("id",user.getId());
                        map.put("name",user.getName());
                        map.put("isScored",true);
                        userList.add(map);
                    }
                }else if(!isFirst && isCur){
                    //不是第一个节点 流程是当前节点
                    if(actYwGnode.getIsAssign()){
                        List<Map<String, String>> list = actYwGnodeService.getAssignUsers(proModel.getId(),gnodeId);
                        if(StringUtil.checkNotEmpty(list)){
                            for(Map<String, String> mapIndex:list){
                                Map<String,Object> map=new HashMap<String,Object>();
                                map.put("id",mapIndex.get("id"));
                                map.put("name",mapIndex.get("name"));
                                map.put("isScored",false);
                                userList.add(map);
                            }
                            ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
                            actYwAuditInfoIn.setPromodelId(proModel.getId());
                            actYwAuditInfoIn.setGnodeId(gnodeId);
                            List<ActYwAuditInfo> auditListIn=	actYwAuditInfoService.findList(actYwAuditInfoIn);
                            for(ActYwAuditInfo auditInfoIn:auditListIn){
                                User user=auditInfoIn.getCreateBy();
                                user=UserUtils.get(user.getId());
                                Map<String,Object> map=new HashMap<String,Object>();
                                map.put("id",user.getId());
                                map.put("name",user.getName());
                                map.put("isScored",true);
                                for(Map<String, Object> mapIndex:userList){
                                    if(mapIndex.get("id").equals(user.getId())){
                                        userList.remove(mapIndex);
                                        break;
                                    }
                                }
                                userList.add(map);
                            }
                        }
                    }else{
                        List<ActYwGrole> groleList=actYwGnode.getGroles();
                        if(StringUtil.checkNotEmpty(groleList)){
                            for(int j=0;j<groleList.size();j++){
                                Role role=   groleList.get(j).getRole();
                                List<User> userListIn=new ArrayList<User>();
                                if(role.getName().contains(SysIds.ISCOLLEGE.getRemark())||role.getName().contains(SysIds.ISMS.getRemark())){
                                    userListIn=userService.findUserListByRoleId(role.getId(),proModel.getDeclareId());
                                }else{
                                    userListIn=userService.findUserListByRoleId(role.getId());
                                }
                                if(StringUtil.checkNotEmpty(userListIn)){
                                    for(User user:userListIn) {
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("id", user.getId());
                                        map.put("name", user.getName());
                                        map.put("isScored", false);
                                        userList.add(map);
                                    }
                                }
                            }
                        }
                        ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
                        actYwAuditInfoIn.setPromodelId(proModel.getId());
                        actYwAuditInfoIn.setGnodeId(gnodeId);
                        List<ActYwAuditInfo> auditListIn=	actYwAuditInfoService.findList(actYwAuditInfoIn);
                        for(ActYwAuditInfo auditInfoIn:auditListIn){
                            User user=auditInfoIn.getCreateBy();
                            user=UserUtils.get(user.getId());
                            Map<String,Object> map=new HashMap<String,Object>();
                            map.put("id",user.getId());
                            map.put("name",user.getName());
                            map.put("isScored",true);
                            for(Map<String, Object> mapIndex:userList){
                                if(mapIndex.get("id").equals(user.getId())){
                                    userList.remove(mapIndex);
                                    break;
                                }
                            }
                            userList.add(map);
                        }
                    }
                }
                js.put(gnodeId,userList);
                //处理完当前节点 后续节点不用查找。
                if(isCur){
                    break ;
                }
            }
        }
        return js;
    }

    public void batchChangeLevel(String[] idList, String finalStatus) {
        List<String> idsList=Arrays.asList(idList);
        dao.updateFinalStatusPl(idsList,finalStatus);
    }

    public boolean batchAudit(String[] idList, String grade,String gnodeId) {
        for(int i=0;i<idList.length;i++){
            ProModel proModel=get(idList[i]);
            proModel.setGrade(grade);
            try{
                auditWithGateWay(proModel,gnodeId);
            }catch (Exception e){
                logger.error(e);
                return false;
            }
       }
        return true;
    }

    public void batchScoreAudit(String[] idList, String score, String gnodeId) {
        for(int i=0;i<idList.length;i++){
            ProModel proModel=get(idList[i]);
            //得到当前项目上次评分时候的平均分
            ActYwAuditInfo actYwAuditInfoIn=new ActYwAuditInfo();
            actYwAuditInfoIn.setPromodelId(proModel.getId());
            ActYwAuditInfo lastActYwAuditInfoIn=actYwAuditInfoService.getLastAuditByPromodel(actYwAuditInfoIn);
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setPromodelId(proModel.getId());
            actYwAuditInfo.setGnodeId(lastActYwAuditInfoIn.getGnodeId());
            String gscore = String.valueOf(actYwAuditInfoService.getAuditAvgInfo(actYwAuditInfo));
            String grade="";
            boolean isPass=false;
            if(Integer.parseInt(gscore)>Integer.parseInt(score)){
                isPass=true;
            }
            //根据gnodeId得到下一个节点是否为网关，是否需要网关状态
            List<ActYwStatus> actYwStatusList = getActYwStatus(gnodeId);
            if (actYwStatusList != null) {//批量审核参数
                for(ActYwStatus actYwStatus:actYwStatusList){
                    if(isPass){
                        if(actYwStatus.getState().equals("通过")){
                            grade= actYwStatus.getStatus();
                            proModel.setGrade(grade);
                            try{
                                auditWithGateWay(proModel,gnodeId);
                            }catch (Exception e){
                                logger.error(e);
                            }
                        }
                    }//不通过的等待人工审核
                }
            }
        }
    }

    //根据角色值得到用户
    public List<String> getUsersByRoles(String proModelId, String roleIds) {
        ProModel proModel = get(proModelId);
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        String[] roleList = roleIds.split(StringUtil.LINE_D);
        for (int i = 0; i < roleList.length; i++) {
            List<String> roles = new ArrayList<String>();
            String roleId=roleList[i];
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName =Optional.ofNullable(role.getName()).orElseThrow(()->new GroupErrorException("角色名称异常")) ;

            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
            } else {
                roles = userService.findListByRoleId(role.getId());
            }
            if(roles==null){
                throw new GroupErrorException("该审核节点角色已经不存在");
            }
            // 学生角色id 清除其他人id 只保留申报学生id
            if (role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())
//                    ||role.getId().equals(SysIds.SYS_ROLE_TEACHER.getId())
            ) {
                roles.clear();
                roles.add(userService.findUserById(proModel.getDeclareId()).getId());
            }
            // 导师角色id 清除其他人id 只保留第一导师id
            if (
                role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())) {
                roles.clear();
                roles.add(teamService.findFistTeacherByTeamId(proModel.getTeamId()));
            }
            roleAll.addAll(roles);
        }
        return removeDuplicate(roleAll);
    }



    //根据角色列表获得用户
    public List<String> getUsersByRoleList(String proModelId, List<Role> roleList) {
        ProModel proModel = get(proModelId);
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        for (int i = 0; i < roleList.size(); i++) {
            List<String> roles = new ArrayList<String>();
            String roleId=roleList.get(i).getId();
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName = role.getName();
            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
            } else {
                roles = userService.findListByRoleId(role.getId());
            }
            // 学生角色id 清除其他人id 只保留申报学生id
            if (role.getId().equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())) {
                roles.clear();
                roles.add(userService.findUserById(proModel.getDeclareId()).getId());
            }
            // 导师角色id 清除其他人id 只保留第一导师id
            if (
                role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getTenantId())) {
                roles.clear();
                roles.add(teamService.findFistTeacherByTeamId(proModel.getTeamId()));
            }
            roleAll.addAll(roles);
        }
        return removeDuplicate(roleAll);
    }

    //根据角色列表获得用户
    public List<String> getUsersByRoleList(ProModel proModel, List<Role> roleList) {
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        for (int i = 0; i < roleList.size(); i++) {
            List<String> roles = new ArrayList<String>();
            //Role role = roleList.get(i);
            String roleId=roleList.get(i).getId();
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName = role.getName();

            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), proModel.getDeclareId());
            } else {
                roles= userService.findListByRoleId(role.getId());
            }
          // 学生角色id 清除其他人id 只保留申报学生id
            if (role.getId().equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())||role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())) {
                roles.clear();
                roles.add(userService.findUserById(proModel.getDeclareId()).getId());
            }
            // 导师角色id 清除其他人id 只保留第一导师id
            if (
                role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())) {
                roles.clear();
                roles.add(teamService.findFistTeacherByTeamId(proModel.getTeamId()));
            }
            roleAll.addAll(roles);
        }
        return removeDuplicate(roleAll);
    }

    //根据角色列表获得用户
    public List<String> getUsersByRoleList(List<Role> roleList) {
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        for (int i = 0; i < roleList.size(); i++) {
            List<String> roles = new ArrayList<String>();
            String roleId=roleList.get(i).getId();
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName = role.getName();
            roles= userService.findListByRoleId(role.getId());
            roleAll.addAll(roles);
        }
        return removeDuplicate(roleAll);
    }


	//根据角色列表获得用户
	public List<String> getProvUsersByRoleList(List<Role> roleList,String tenantId) {
		List<String> roleAll = new ArrayList<String>();
		//多角色配置人员
		for (int i = 0; i < roleList.size(); i++) {
			List<String> roles = new ArrayList<String>();
			String roleId=roleList.get(i).getId();
			Role role = systemService.getRole(roleId);
			// 启动节点
			roles= userService.findProvListByRoleId(role.getId(),tenantId);
			roleAll.addAll(roles);
		}
		return removeDuplicate(roleAll);
	}


    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    public ProModel getExcellentById(String id) {
        return dao.getExcellentById(id);
    }

    public ProModel getGcontestExcellentById(String id) {
        return dao.getGcontestExcellentById(id);
    }

    public boolean checkName(ProModel proModel) {
        /*if (proModel.getIsNewRecord()){
            String k = ProSval.ProEmskey.PROMODEL+":name:"+TenantConfig.getCacheTenant()+":"+proModel.getActYwId();
            List<String> listName = JedisUtils.listAll(k);
            if (listName.size() == 0){
                return false;
            }
            for (int i = 0;i<listName.size();i++){
            	if (proModel.getPName().equals(listName.get(i))){
					return true;
				}
			}

        }*/
        if (proModelDao.checkProName(proModel.getPName(), proModel.getId(), proModel.getActYwId()) > 0) {
            return true;
        }
        return false;
    }

    public List<ProModel> findProjects(String userId) {
        return dao.findListAllByLeader(userId);
    }

    public Page<ProModel> getAssginProList(Page<ProModel> page,ActYwEtAssignRule actYwEtAssignRule) {

        ActYw actYw=actYwService.get(actYwEtAssignRule.getActywId());
        //// TODO: 2019/4/23 0023 省中心租户id
        if(actYw.getTenantId().equals("20")){
            return getProvinceProModelPage(page, actYwEtAssignRule, actYw);
        }else {
            return getProModelPage(page, actYwEtAssignRule, actYw);
        }
    }

    public Page<ProModel> getProvAssginProList(Page<ProModel> page,ActYwEtAssignRule actYwEtAssignRule) {
        ActYw actYw=actYwService.get(actYwEtAssignRule.getActywId());
        return getProvinceProModelPage(page, actYwEtAssignRule, actYw);
    }

    private Page<ProModel> getProvinceProModelPage(Page<ProModel> page, ActYwEtAssignRule actYwEtAssignRule, ActYw actYw) {
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> gnodeIdList = new ArrayList<String>();
        gnodeIdList.add(actYwEtAssignRule.getGnodeId());
        List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actYwEtAssignRule.getActywId());
        if (StringUtil.checkEmpty(recordIds)) {
            return page;
        }

        ProModel proModel = new ProModel();
        proModel.setActYwId(actYwEtAssignRule.getActywId());
        proModel.setGnodeId(actYwEtAssignRule.getGnodeId());
        if (StringUtil.isNotEmpty(actYwEtAssignRule.getOfficeId())) {
            proModel.setOfficeId(actYwEtAssignRule.getOfficeId());
        }
        proModel.setIds(recordIds);
        proModel.setPage(page);
        proModel.setTenantId(CoreIds.NPR_SYS_TENANT.getId());
        List<ProModel> list = dao.findProvListByIdsAssign(proModel);
        //委派人跟项目关系
        Map<String, String> ass = getProvTaskAssigns(list, proModel.getGnodeId());
        for (ProModel model : list) {
            ProvinceProModel prov=provinceProModelService.getByProModelId(model.getId());
            //指派人
            if (ass != null && !ass.isEmpty()) {
                model.setTaskAssigns(ass.get(prov.getId()));
            }
            // 查询团队指导老师的名称，多个以逗号分隔
            List<Team> team = teamService.findTeamUserName(model.getTeamId());
            List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                    .collect(Collectors.toList());
            if (!names.isEmpty()) {
                model.getTeam().setuName(StringUtils.join(names, ","));
            }
            // 查询团队学生的名称，多个以逗号分隔
            List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                    .collect(Collectors.toList());
            if (StringUtil.isNotEmpty(model.getDeclareId())) {
                User declareUser = systemService.getUser(model.getDeclareId());
                if (declareUser != null) {
                    if (stunames.contains(declareUser.getName())) {
                        stunames.remove(declareUser.getName());
                    }
                }
            }
            if (!stunames.isEmpty()) {
                model.getTeam().setEntName(StringUtils.join(stunames, ","));
            }
        }
        page.setList(list);
        return page;
    }

    //判断是否为当前提交节点
    public boolean checkIsHasReport(String gnodeId, String proModelId) {
        ProModel proModel=get(proModelId);
        if(proModel==null){
            return false;
        }
        if(proModel.getState()!=null && proModel.getState().equals("1")){
            return true;
        }
        ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(proModel.getProcInsId());
        if(actYwGnode==null){
            return false;
        }
        String currGnodeId=actYwGnode.getId();
        return gnodeId.equals(currGnodeId);
    }

    private Page<ProModel> getProModelPage(Page<ProModel> page, ActYwEtAssignRule actYwEtAssignRule, ActYw actYw) {
        String key = ActYw.getPkey(actYw);
        Act act = new Act();
        act.setProcDefKey(key);  //流程标识
        List<String> gnodeIdList = new ArrayList<String>();
        gnodeIdList.add(actYwEtAssignRule.getGnodeId());
        List<String> recordIds = actTaskService.recordIdsWithoutUser(act, gnodeIdList, actYwEtAssignRule.getActywId());
        if (StringUtil.checkEmpty(recordIds)) {
            return page;
        }
        ProModel proModel = new ProModel();
        proModel.setActYwId(actYwEtAssignRule.getActywId());
        proModel.setGnodeId(actYwEtAssignRule.getGnodeId());
        if (StringUtil.isNotEmpty(actYwEtAssignRule.getOfficeId())) {
            proModel.setOfficeId(actYwEtAssignRule.getOfficeId());
        }
        proModel.setIds(recordIds);
        proModel.setPage(page);
        String tenantId = TenantConfig.getCacheTenant();
        proModel.setTenantId(tenantId);
		List<ProModel> list = null;
        if (!tenantId.equals(CoreIds.NPR_SYS_TENANT.getId())){
			list = dao.findListByIdsAssign(proModel);
		}else{
        	//要改方法,关联省的项目
			list = dao.findProvListByIdsAssign(proModel);
		}

        //证书
        Map<String, String> ass = getTaskAssigns(list, proModel.getGnodeId());
        for (ProModel model : list) {

            //指派人
            if (ass != null && !ass.isEmpty()) {
                model.setTaskAssigns(ass.get(model.getId()));
            }
            // 查询团队指导老师的名称，多个以逗号分隔
            List<Team> team = teamService.findTeamUserName(model.getTeamId());
            List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                    .collect(Collectors.toList());
            if (!names.isEmpty()) {
                model.getTeam().setuName(StringUtils.join(names, ","));
            }
            // 查询团队学生的名称，多个以逗号分隔
            List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                    .collect(Collectors.toList());
            if (StringUtil.isNotEmpty(model.getDeclareId())) {
                User declareUser = systemService.getUser(model.getDeclareId());
                if (declareUser != null) {
                    if (stunames.contains(declareUser.getName())) {
                        stunames.remove(declareUser.getName());
                    }
                }
            }
            if (!stunames.isEmpty()) {
                model.getTeam().setEntName(StringUtils.join(stunames, ","));
            }
        }
        page.setList(list);
        return page;
    }

    //根据用户得到他的审核任务
    public Page<ProModel> getFirstTeacherPromodel(Page page,ProModel proModel) {
        List<String> ids= actTaskService.todoPromodelIds(UserUtils.getUserId());
        proModel.setIds(ids);
        proModel.setPage(page);
        if (ids.isEmpty()) {
            page.setList(new ArrayList<>(0));
        }else{
            List<ProModel> list = dao.findListByIdsOfBefore(proModel);
            //证书
            Map<String, List<SysCertInsVo>> certMap = getSysCertIns(list);
            for (ProModel proModelIndex : list) {
                if (certMap != null && !certMap.isEmpty()) {
                    proModelIndex.setScis(certMap.get(proModelIndex.getId()));
                }
                // 查询团队指导老师的名称，多个以逗号分隔
                List<Team> team = teamService.findTeamUserName(proModelIndex.getTeamId());
                List<String> names = team.stream().filter(e -> "2".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if (!names.isEmpty()) {
                    proModelIndex.getTeam().setuName(StringUtils.join(names, ","));
                }
                List<String> stunames = team.stream().filter(e -> "1".equals(e.getTeamUserType())).map(e -> e.getuName())
                        .collect(Collectors.toList());
                if(StringUtil.isNotEmpty(proModelIndex.getDeclareId())){
                    User declareUser= systemService.getUser(proModelIndex.getDeclareId());
                    if(declareUser!=null){
                        if(stunames.contains(declareUser.getName())){
                            stunames.remove(declareUser.getName());
                        }
                    }
                }
                if (!stunames.isEmpty()) {
                    proModelIndex.getTeam().setEntName(StringUtils.join(stunames, ","));
                }
                // 项目结果
                if (StringUtils.isNotBlank(proModelIndex.getEndGnodeId()) && Const.YES.equals(proModelIndex.getState())) {// 流程已结束
                    getFinalResult(proModelIndex);
                }
                Map<String,String>map= ProProcessDefUtils.getActByPromodelId(proModelIndex.getId());
                proModelIndex.setAuditMap(map);
            }
            page.setList(list);
        }
        return page;
    }

    @Transactional(readOnly = false)
    public void auditDelegate(ProModel proModel, String gnodeId) {
        // 判断审核结果
        ActYwGnode actYwGnode=actYwGnodeService.get(gnodeId);
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
        actYwAuditInfo.setPromodelId(proModel.getId());
        actYwAuditInfo.setGnodeId(gnodeId);
        actYwAuditInfo.setIsDelegate(Const.YES);
        actYwAuditInfo.setScore(Double.parseDouble(proModel.getgScore()));
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
        if (lastAuditActYwAuditInfo != null) {
            actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
        }
        actYwAuditInfoService.save(actYwAuditInfo);
        ActYwGassign actYwGassign = new ActYwGassign();
        actYwGassign.setPromodelId(proModel.getId());
        actYwGassign.setGnodeId(gnodeId);
        actYwGassign.setRevUserId(UserUtils.getUser().getId());
        ActYwGassign oldActYwGassign=actYwGassignService.getByActYwGassign(actYwGassign);
        oldActYwGassign.setIsOver(Const.YES);
//        oldActYwGassign = ActYwGassign.initType(oldActYwGassign, actYwGnode);
        actYwGassignService.save(oldActYwGassign);
    }

    @Transactional(readOnly = false)
    public void saveDelegateAudit(ProModel proModel, String gnodeId) {
        ActYwGnode actYwGnode=actYwGnodeService.get(gnodeId);
        ActYwAuditInfo actYwAuditInfo=new ActYwAuditInfo();
        actYwAuditInfo.setIsDelegate(Const.YES);
        actYwAuditInfo.setGnodeId(gnodeId);
        actYwAuditInfo.setPromodelId(proModel.getId());
        actYwAuditInfo.setAuditId(UserUtils.getUserId());
        actYwAuditInfo.setScore(Double.parseDouble(proModel.getgScore()));
        actYwAuditInfo.setSuggest(proModel.getSource());
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        actYwAuditInfoService.save(actYwAuditInfo);
    }

    @Transactional(readOnly = false)
    public void deleteProcessInstanceById(String id) {
        ProModel proModel=get(id);
        runtimeService.deleteProcessInstance(proModel.getProcInsId(), "结束");
        // 标志流程结束
        proModel.setState("1");
        //团队完成记录
        teamUserHistoryService.updateFinishAsClose(id);
        //项目完成节点
        proModel.setEndGnodeId("");

//        actYwGclazzDataService.deleteWLPLByApply(id);
        super.save(proModel);
    }

    @Transactional(rollbackFor = Exception.class)
    public int bulkInsert(List<ProModel> list){
        return proModelDao.bulk(list);
    }


    public Boolean revertAfterApplyed(String startDate){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            return new Date().before(date);
        } catch (ParseException e) {
            logger.info("revertAfterApplyed :撤销申请时间转换异常");
            e.printStackTrace();
        }
        return false;
    }

    @Autowired
    ProvinceProModelService provinceProModelService;
    @Transactional(readOnly = false)
    public void sendProToProvince(String promodelId,String tenantId,String actywId,ActYw provinceActYw,String year,Date subTime) {
        ProvinceProModel provinceProModel=new ProvinceProModel();
        provinceProModel.setModelId(promodelId);
        provinceProModel.setActYwId(actywId);
        provinceProModel.setTenantId(provinceActYw.getTenantId());
        provinceProModel.setYear(year);
        actProvinceStart(provinceProModel,provinceActYw,tenantId,subTime);
    }

    public ProModel getWith(String modelId) {
        return proModelDao.getWith(modelId);
    }


    //根据角色值得到用户
    public List<String> getProvUsersByRoles(String roleIds) {
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        String[] roleList = roleIds.split(StringUtil.LINE_D);
        for (int i = 0; i < roleList.length; i++) {
            List<String> roles = new ArrayList<String>();
            String roleId=roleList[i];
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName = role.getName();
            roles = userService.findListByRoleId(role.getId());
            if(roles==null){
                throw new GroupErrorException("该审核节点角色已经不存在");
            }
            roleAll.addAll(roles);
        }
        return removeDuplicate(roleAll);
    }


    public Integer updateIsSendById(String promodelId,String code){
        return proModelDao.updateIsSendById(promodelId,code);
    }

    /**
     * 撤回
     * @param proModel
     */
    @Transactional(rollbackFor = Exception.class)
    public void revertBySelf(ProModel proModel){
        if (proModel.getSubStatus() != null && StringUtils.equals(CoreSval.Const.NO,proModel.getSubStatus())){
            return;
        }
    	try{
			ActYw actYw = actYwService.get(proModel.getActYwId());
			if (actYw == null) {
				throw new GroupErrorException("找不到流程配置(actYw)");
			}
			//打回将该任务挂起
			runtimeService.suspendProcessInstanceById(proModel.getProcInsId());
			updateSubStatus(proModel.getId(), Const.NO);
			teamUserHistoryDao.updateFinishAsSave(proModel.getId());
			proModel.setSubStatus(CoreSval.Const.NO);
			super.save(proModel);
		}catch (Exception e){
    		logger.error("项目撤销异常",e);
    		throw new ApplyException(ApiProModel.PRO_REVERT_ERROR.getMsg());
		}

    }

    /**
     * 按Id查找项目
     * @param proModelId ID
     * @return ProModel
     */
    public ProModel getProModelId(String proModelId){
        if (StringUtils.isBlank(proModelId)){
            return null;
        }
        return proModelDao.getProModelId(proModelId);
    }

    public Integer  saveTopicProModel(ProModel proModel){
        return proModelDao.insertOfTopic(proModel);
    }

    public void getRoleResult(ProModel proModel){
        String roleFlag = null;
        List<Role> roleList = UserUtils.getUser().getRoleList();
		List<Role> checkRole = new ArrayList<>();
		if (StringUtil.checkNotEmpty(roleList)){
            Role roleAdmin = coreService.getByRtype(CoreSval.Rtype.ADMIN_SN.getKey());
            if (roleAdmin != null){
                checkRole = roleList.stream().filter(item -> item.getId().equals(roleAdmin.getId())).collect(Collectors.toList());
                if (checkRole.size() == 0){
                    Role roleSuper = coreService.getByRtype(CoreSval.Rtype.SUPER_SC.getKey());
                    if (roleSuper != null){
                        checkRole = roleList.stream().filter(item -> item.getId().equals(roleSuper.getId())).collect(Collectors.toList());
                        if (checkRole.size() == 0){
                            Role roleSysSC = coreService.getByRtype(CoreSval.Rtype.ADMIN_SYS_SC.getKey());
                            if (roleSysSC != null){
                                checkRole = roleList.stream().filter(item -> item.getId().equals(roleSysSC.getId())).collect(Collectors.toList());
                                if (checkRole.size() == 0){
                                    Role roleYw = coreService.getByRtype(CoreSval.Rtype.ADMIN_YW_SC.getKey());
                                    if (roleYw != null){
                                        checkRole = roleList.stream().filter(item -> item.getId().equals(roleYw.getId())).collect(Collectors.toList());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
		if (checkRole.size() > 0){
		    //管理员
            roleFlag = "0";
        }else{
            Role roleMinister = coreService.getByRtype(CoreSval.Rtype.MINISTER.getKey());
            if((roleMinister != null) && StringUtil.isNotEmpty(roleMinister.getId())){
                checkRole = roleList.stream().filter(item -> item.getId().equals(roleMinister.getId())).collect(Collectors.toList());
            }

            if (checkRole.size() > 0){
                roleFlag = "1";
                proModel.setOfficeId(UserUtils.getUser().getOfficeId());
            }else{
                roleFlag = "2";
                proModel.setProfessor(UserUtils.getUserId());
            }
        }
        proModel.setRoleFlag(roleFlag);

    }

    public void updateByProId(String id,Integer ranking){
        dao.updateByProId(id,ranking);
    }
    public List<ProRole> findRolesByIds(ProRole entity){
        return dao.findRolesByIds(entity);
    }

    public List<String> getIdsByRanking(Integer ranking,String id){
        return getIdsByRanking(ranking,id, null);
    }

    public List<String> getIdsByRanking(Integer ranking,String id,String actYwId){
        return dao.getIdsByRanking(ranking,id,actYwId);
    }


}