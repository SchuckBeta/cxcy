package com.oseasy.pie.modules.impdata.service;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import com.oseasy.com.fileserver.common.vsftp.core.Vsftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.cache.Cache;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.common.vsftp.vo.VsFile;
import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.vsftp.service.FtpService;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.expdata.entity.ExpInfo;
import com.oseasy.pie.modules.expdata.service.ExpInfoService;
import com.oseasy.pie.modules.impdata.dao.ImpInfoDao;
import com.oseasy.pro.modules.cert.service.ProSysAttachmentService;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.project.vo.ProjectNodeVo;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.ExpGnodeFile;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.proprojectmd.service.ProModelMdService;
import com.oseasy.pro.modules.workflow.IWorkFlow;
import com.oseasy.pro.modules.workflow.IWorkRes;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.pro.modules.workflow.handler.DataExpPmTlxyVoHandler;
import com.oseasy.pro.modules.workflow.handler.DataExpVoHandler;
import com.oseasy.pro.modules.workflow.service.ProDeclareService;
import com.oseasy.pro.modules.workflow.service.ProModelGzsmxxService;
import com.oseasy.pro.modules.workflow.service.ProModelMdGcService;
import com.oseasy.pro.modules.workflow.service.ProModelTlxyService;
import com.oseasy.pro.modules.workflow.utils.WorkFlowUtil;
import com.oseasy.pro.modules.workflow.vo.ExpProModelMdVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelTlxyVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.FileVo;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import net.sf.json.JSONObject;

@Service
@Transactional(readOnly = true)
public class ImpExpService {
    /**
     * .
     */
    private static final String EXP_PROJECTMD_TEMPLATE_XLSX = "exp_projectmd_template.xlsx";
    public static Logger logger = Logger.getLogger(ImpExpService.class);
    public final static int close_sheet0_head = 2;
    public final static String[] close_sheet0_foot = new String[] {
            "经手人：                       联系电话：                   学院负责人（签名）：                 " };
    public final static int mid_sheet0_head = 2;
    public final static String[] mid_sheet0_foot = new String[] {
            "经手人：                       联系电话：                   " };
    public final static int approval_sheet0_head = 2;
    public final static int approval_sheet1_head = 2;
    public final static String[] approval_sheet0_foot = new String[] {
            "联系人签字：                       联系电话：                   负责人签字：                   ",
            "注： 1.“A”为学生自主选题，来源于自己对课题长期积累与兴趣；“B”为学生来源于教师科研项目选题；“C”为学生承担社会、企业委托项目选题。",
            "2.“来源项目名称”和“来源项目类别”栏限“B”和“C”的项目填写，“来源项目类别”栏填写“863项目”、“973项目”、“国家自然科学基金项目”、“省级自然科学基金项目”、“教师横向科研项目”、“企业委托项目”、“社会委托项目”以及其他项目标识。",
            "3. 项目其他成员信息格式：姓名1（学号1）、姓名2（学号2）、…", "4. 申报级别按“国家级（含省级备选项目）”、“校级”填写。" };
    public final static String[] approval_sheet1_foot = new String[] {
            "联系人签字：                       联系电话：                   负责人签字：                   ",
            "注： 1.“A”为学生自主选题，来源于自己对课题长期积累与兴趣；“B”为学生来源于教师科研项目选题；“C”为学生承担社会、企业委托项目选题。",
            "2.“来源项目名称”和“来源项目类别”栏限“B”和“C”的项目填写，“来源项目类别”栏填写“863项目”、“973项目”、“国家自然科学基金项目”、“省级自然科学基金项目”、“教师横向科研项目”、“企业委托项目”、“社会委托项目”以及其他项目标识。",
            "3. 项目其他成员信息格式：姓名1（学号1）、姓名2（学号2）、…", "4. 申报级别按“国家级（含省级备选项目）”、“校级”填写。", "5.申报类型：创新训练项目、创业训练项目、创业实践项目。",
            "6.“是否已申请入孵”为创业项目填写，团队已提交“中南民族大学大学生创业孵化示范基地入驻申请”请填是，否则填否。" };

    private static final List<String> tableTitle = new ArrayList<>();
    static {
        tableTitle.add("项目编号");
        tableTitle.add("项目名称");
        tableTitle.add("项目类别");
        tableTitle.add("负责人");
        tableTitle.add("学号");
        tableTitle.add("联系方式");
        tableTitle.add("邮箱");
        tableTitle.add("专业");
        tableTitle.add("项目结果");
        tableTitle.add("团队成员和学号");
        tableTitle.add("指导老师和工号");
        tableTitle.add("项目简介");

    }

    @Autowired
    private ImpInfoDao impInfoDao;
    @Autowired
    private ProModelMdService proModelMdService;
    @Autowired
    private ProSysAttachmentService proSysAttachmentService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private ExpInfoService expInfoService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private ProModelGzsmxxService proModelGzsmxxService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private ProModelMdGcService proModelMdGcService;
    @Autowired
    private ProModelTlxyService proModelTlxyService;
//    @Autowired
    private ProDeclareService proDeclareService;

    public void expAll(HttpServletRequest request, HttpServletResponse response) {
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            // String actywId=request.getParameter("actywId");
            // excel模板路径
            String fileName = "项目信息.xlsx";
            String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + EXP_PROJECTMD_TEMPLATE_XLSX);
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            List<String> pids = proModelMdService.getAllPromodelMd();
            List<Map<String, String>> prodata = impInfoDao.getProjectMdData(pids);

            XSSFCellStyle rowStyle = wb.createCellStyle();
            rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            XSSFDataFormat format = wb.createDataFormat();
            rowStyle.setDataFormat(format.getFormat("@"));

            XSSFSheet sheet0 = wb.getSheetAt(0);

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(c0.getStringCellValue());

            int row0 = 3;
            for (Map<String, String> map : prodata) {
                XSSFRow row = sheet0.createRow(row0);
                row0++;
                row.createCell(0).setCellValue(row0 - 3 + "");
                row.createCell(1).setCellValue(map.get("oname"));
                row.createCell(2).setCellValue(map.get("p_number"));
                row.createCell(3).setCellValue(map.get("p_name"));
                row.createCell(4).setCellValue(map.get("pro_category"));
                row.createCell(5).setCellValue(map.get("level"));
                row.createCell(6).setCellValue(map.get("leader_name"));
                row.createCell(7).setCellValue(map.get("no"));
                row.createCell(8).setCellValue(map.get("mobile"));
                int nums = 1;
                if (StringUtil.isNotEmpty(map.get("members"))) {
                    nums = nums + map.get("members").split("、").length;
                }
                row.createCell(9).setCellValue(nums + "");
                row.createCell(10).setCellValue(map.get("members"));
                String[] teas = null;
                if (StringUtil.isNotEmpty(map.get("teachers"))) {
                    teas = map.get("teachers").split(",");
                }
                row.createCell(11).setCellValue(teas != null ? teas[0] : "");
                row.createCell(12).setCellValue(teas != null ? teas[1] : "");
                row.createCell(13).setCellValue(teas != null ? teas[2] : "");
                row.createCell(14).setCellValue(map.get("s3l"));
                row.createCell(15).setCellValue(map.get("introduction"));

                // 设置样式
                for (int m = 0; m <= 15; m++) {
                    row.getCell(m).setCellStyle(rowStyle);
                }
            }
            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 民大自定义导出数据包括.
     *
     * @param request
     * @param response
     */
    public void expQueryNoFile(HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查询过滤数据.
         */
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        String queryStr = request.getParameter("proModel.queryStr");
        String proCategory = request.getParameter("proModel.proCategory");
        ItOper impVo = new ItOper(request);
        ActYw actYw = actYwService.get(impVo.getActywId());
        JSONObject js = new JSONObject();
        if ((actYw == null) || (actYw.getFtheme() == null)) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "业务ID参数必填！");
            return;
        }
        ProModelMd proModel = new ProModelMd();
        if (StringUtils.isNotEmpty(impVo.getActywId())) {
            proModel.setActYwId(impVo.getActywId());
            proModel.setProModel(new ProModel());
        }
        if (StringUtils.isNotEmpty(proCategory)) {
            proModel.getProModel().setProCategory(proCategory);
        }
        if (StringUtils.isNotEmpty(queryStr)) {
            proModel.getProModel().setQueryStr(queryStr);
        }
        Page<ProModelMd> page = new Page<ProModelMd>();
        page.setPageSize(Page.MAX_PAGE_SIZE);
        List<ExpProModelMdVo> list = proModelMdService.exportMdQuery(page, proModel);
        if (StringUtil.checkEmpty(list)) {
            list = Lists.newArrayList();
            logger.info("当前角色在当前流程节点没有审核或查询数据");
        }

        /**
         * 生成附件.
         */
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码

        String rootpath = request.getSession().getServletContext().getRealPath("/");
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            // String actywId=request.getParameter("actywId");
            // excel模板路径
            String fileName = "大学生创新创业训练计划项目信息表.xlsx";
            String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), "ISO-8859-1") + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            File fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + "/md/" + EXP_PROJECTMD_TEMPLATE_XLSX);
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFCellStyle rowStyle = wb.createCellStyle();
            rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            XSSFDataFormat format = wb.createDataFormat();
            rowStyle.setDataFormat(format.getFormat("@"));

            XSSFSheet sheet0 = wb.getSheetAt(0);

//            XSSFCell c0 = sheet0.getRow(0).getCell(0);
//            c0.setCellValue(c0.getStringCellValue());

            int row0 = 1;
            for (ExpProModelMdVo pm : list) {
                XSSFRow row = sheet0.createRow(row0);
                row0++;
                row.createCell(0).setCellValue(pm.getYear());
                row.createCell(1).setCellValue(pm.getOfficeAname());
                row.createCell(2).setCellValue(pm.getOfficeNo());
                row.createCell(3).setCellValue(pm.getOfficeName());
                row.createCell(4).setCellValue(pm.getPno());
                row.createCell(5).setCellValue(pm.getPname());
                row.createCell(6).setCellValue(pm.getProCategoryName());
                if (StringUtil.checkNotEmpty(pm.getTeamLeader())) {
                    row.createCell(7).setCellValue(pm.getTeamLeaderName());
                    row.createCell(8).setCellValue(pm.getTeamLeaderNo());
                    row.createCell(9).setCellValue(pm.getTeamLeaderMobile());
                } else {
                    row.createCell(7).setCellValue("");
                    row.createCell(8).setCellValue("");
                    row.createCell(9).setCellValue("");
                }
                row.createCell(10).setCellValue(pm.getTeamOtherss());
                if (StringUtil.checkNotEmpty(pm.getTeacherVos())) {
                    row.createCell(11).setCellValue(pm.getTeacherNames());
                    row.createCell(12).setCellValue(pm.getTeacherNos());
                    row.createCell(13).setCellValue(pm.getTeacherZcs());
                } else {
                    row.createCell(11).setCellValue("");
                    row.createCell(12).setCellValue("");
                    row.createCell(13).setCellValue("");
                }
                row.createCell(14).setCellValue("");
                row.createCell(15).setCellValue("");
                row.createCell(16).setCellValue("");
                row.createCell(17).setCellValue(pm.getCourse());
                row.createCell(18).setCellValue(pm.getIntroduction());
                row.createCell(19).setCellValue(pm.getSjNo());
                row.createCell(20).setCellValue(pm.getXxNo());
                // 设置样式
                for (int m = 0; m <= 20; m++) {
                    row.getCell(m).setCellStyle(rowStyle);
                }
            }
            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 民大自定义导出数据包括.
     *
     * @param request
     * @param response
     */
    @Transactional(readOnly = false)
    public JSONObject expQuery(HttpServletRequest request, HttpServletResponse response) {
        JSONObject js = new JSONObject();
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String year = request.getParameter("year");
        String queryStr = request.getParameter("proModel.queryStr");
        String proCategory = request.getParameter("proModel.proCategory");
        ItOper impVo = new ItOper(request);

        ProModelMd proModel = new ProModelMd();
        if (StringUtils.isNotEmpty(impVo.getActywId())) {
            proModel.setActYwId(impVo.getActywId());
            proModel.setProModel(new ProModel());
        }
        if (StringUtils.isNotEmpty(proCategory)) {
            proModel.getProModel().setProCategory(proCategory);
        }
        if (StringUtils.isNotEmpty(queryStr)) {
            proModel.getProModel().setQueryStr(queryStr);
        }
        Page<ProModelMd> page = new Page<ProModelMd>();
        page.setPageSize(Page.MAX_PAGE_SIZE);
        List<ExpProModelMdVo> list = proModelMdService.exportMdQuery(page, proModel);
        if (StringUtil.checkEmpty(list)) {
            list = Lists.newArrayList();
            logger.info("当前角色在当前流程节点没有审核或查询数据");
        }

        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        List<String> pids = Lists.newArrayList();
        for (ExpProModelMdVo epmMdVo : list) {
            pids.add(epmMdVo.getId());
        }
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(ExpType.MdExpapproval.getValue());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(ExpType.MdExpapproval.getValue());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载申报附件
        List<String> fileSteps = new ArrayList<String>();
        fileSteps.add(FileStepEnum.S2000.getValue());
        List<VsFile> vsFiles = proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps, tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
            String key = IdGen.uuid();
          /*  for (int i = 1; i < res.size(); i++) {
                //todo
                VsftpUtils.returnResource(res.get(i));
            }*/
            disQueryThread(res.get(0), tempPath, rootpath, year, pids, vsFiles, ei.getId(), key, impVo);
        } else {
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String key = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    disQueryThread(res.get(i), tempPath, rootpath, year, pids, tempList, ei.getId(), key, impVo);
                } else {
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), key, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));
                }
            }
        }

        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    public void expClose(HttpServletRequest request, HttpServletResponse response) {
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        try {
            List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取结项需要审核的pro_model
            // id
            if (pids != null && pids.size() > 0) {
                List<String> fileSteps = new ArrayList<String>();
                fileSteps.add(FileStepEnum.S2300.getValue());
                FileVo fileVo = proSysAttachmentService.downloads(FileTypeEnum.S10, pids, fileSteps, tempPath);// 下载学生结项上传的文件,并创建目录结构
                if ((fileVo.getStatus()).equals(FileVo.SUCCESS)) {
                    List<Map<String, String>> data = impInfoDao.getCloseData(pids);// 获取需要审核的项目信息
                    if (data != null && data.size() > 0) {
                        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                        for (Map<String, String> datam : data) {
                            String offname = datam.get("oname");
                            List<Map<String, String>> olist = map.get(offname);
                            if (olist == null) {
                                olist = new ArrayList<Map<String, String>>();
                                map.put(offname, olist);
                            }
                            olist.add(datam);
                        }
                        for (String key : map.keySet()) {
                            // 按学院名称生成项目审核信息
                            expCloseFileByOffice("", rootpath,
                                    tempPath + File.separator + FileStepEnum.S2300.getName() + File.separator + key,
                                    key, map.get(key));
                        }
                    }
                } else {
                    logger.error("下载项目结项报告失败");
                }
            }
            // 对生成的文件压缩，下载
            createZip("", FileStepEnum.S2300.getName(), tempPath + File.separator + FileStepEnum.S2300.getName(),
                    response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    // 导出学院结项汇总表
    private void expCloseFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list) {
        expCloseFileByOffice(year, rootpath, filepath, oname, list, null);
    }

    // 导出学院结项汇总表
    private void expCloseFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list, ItOper impVo) {
        File dirFile = new File(filepath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(filepath + File.separator + "大学生创新训练计划项目结题验收汇总表_" + oname + ".xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = null;
            if ((impVo == null) || impVo.getReqParam().getIsMdOld()) {
                fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpclose.getTplname());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname + "           年     月     日");
                int row0 = close_sheet0_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    row = sheet0.createRow(row0);
                    rowinx = row0 - close_sheet0_head - 1;
                    row0++;
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("p_number"));
                    row.createCell(2).setCellValue(map.get("p_name"));
                    row.createCell(3).setCellValue(map.get("leader_name"));
                    row.createCell(4).setCellValue(map.get("no"));
                    row.createCell(5).setCellValue(map.get("mobile"));
                    row.createCell(6).setCellValue(map.get("members"));
                    String[] teas = null;
                    if (StringUtil.isNotEmpty(map.get("teachers"))) {
                        teas = map.get("teachers").split(",");
                    }
                    row.createCell(7).setCellValue(teas != null ? teas[0] : "");
                    row.createCell(8).setCellValue(teas != null ? teas[1] : "");
                    row.createCell(9).setCellValue(map.get("level"));
                    row.createCell(10).setCellValue(map.get("pro_category"));
                    row.createCell(11).setCellValue(map.get("result"));
                    row.createCell(12).setCellValue("");
                    row.createCell(13).setCellValue("");
                    row.createCell(14).setCellValue(map.get("reimbursement_amount"));
                    row.createCell(15).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(16).setCellValue(gnodeid);
                    // 设置样式
                    for (int m = 0; m <= 16; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 11);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < close_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(close_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);
                wb.write(fos);
            } else {
                fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpclose.getTplPath());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname + "           年     月     日");
                int row0 = close_sheet0_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    row = sheet0.createRow(row0);
                    rowinx = row0 - close_sheet0_head - 1;
                    row0++;
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("p_number"));
                    row.createCell(2).setCellValue(map.get("p_name"));
                    row.createCell(3).setCellValue(map.get("leader_name"));
                    row.createCell(4).setCellValue(map.get("no"));
                    row.createCell(5).setCellValue(map.get("mobile"));
                    row.createCell(6).setCellValue(map.get("members"));
                    String[] teas = null;
                    if (StringUtil.isNotEmpty(map.get("teachers"))) {
                        teas = map.get("teachers").split(",");
                    }
                    row.createCell(7).setCellValue(teas != null ? teas[0] : "");
                    row.createCell(8).setCellValue(teas != null ? teas[1] : "");
                    row.createCell(9).setCellValue(map.get("level"));
                    row.createCell(10).setCellValue(map.get("pro_category"));
                    row.createCell(11).setCellValue(map.get("result"));
                    row.createCell(12).setCellValue("");
                    row.createCell(13).setCellValue("");
                    row.createCell(14).setCellValue(map.get("reimbursement_amount"));
                    row.createCell(15).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(16).setCellValue(gnodeid);
                    // 设置样式
                    for (int m = 0; m <= 16; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 11);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < close_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(close_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);
                wb.write(fos);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e) + e.getMessage());
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e) + e.getMessage());
            }
        }
    }

    public void expMid(HttpServletRequest request, HttpServletResponse response) {
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        try {
            List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取中期检查需要审核的pro_model
            // id
            if (pids != null && pids.size() > 0) {
                List<String> fileSteps = new ArrayList<String>();
                fileSteps.add(FileStepEnum.S2200.getValue());
                FileVo fileVo = proSysAttachmentService.downloads(FileTypeEnum.S10, pids, fileSteps, tempPath);// 下载学生中期检查上传的文件,并创建目录结构
                if ((fileVo.getStatus()).equals(FileVo.SUCCESS)) {
                    List<Map<String, String>> data = impInfoDao.getMidData(pids);// 获取需要审核的项目信息
                    if (data != null && data.size() > 0) {
                        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                        for (Map<String, String> datam : data) {
                            String offname = datam.get("oname");
                            List<Map<String, String>> olist = map.get(offname);
                            if (olist == null) {
                                olist = new ArrayList<Map<String, String>>();
                                map.put(offname, olist);
                            }
                            olist.add(datam);
                        }
                        for (String key : map.keySet()) {
                            // 按学院名称生成项目审核信息
                            expMidFileByOffice("", rootpath,
                                    tempPath + File.separator + FileStepEnum.S2200.getName() + File.separator + key,
                                    key, map.get(key));
                        }
                    }
                } else {
                    logger.error("下载项目中期报告失败");
                }
            }
            // 对生成的文件压缩，下载
            createZip("", FileStepEnum.S2200.getName(), tempPath + File.separator + FileStepEnum.S2200.getName(),
                    response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    // 导出学院中期汇总表
    private void expMidFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list) {
        expMidFileByOffice(year, rootpath, filepath, oname, list, null);
    }

    // 导出学院中期汇总表
    private void expMidFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list, ItOper impVo) {
        File dirFile = new File(filepath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(filepath + File.separator + "大学生创新创业训练计划检查汇总表_" + oname + ".xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = null;
            if ((impVo == null) || impVo.getReqParam().getIsMdOld()) {
                fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpmid.getTplname());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname + "           年     月     日");
                int row0 = mid_sheet0_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    row = sheet0.createRow(row0);
                    rowinx = row0 - mid_sheet0_head - 1;
                    row0++;
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("p_number"));
                    row.createCell(2).setCellValue(map.get("p_name"));
                    row.createCell(3).setCellValue(map.get("leader_name"));
                    row.createCell(4).setCellValue(map.get("no"));
                    row.createCell(5).setCellValue(map.get("mobile"));
                    row.createCell(6).setCellValue(map.get("teachers"));
                    row.createCell(7).setCellValue(map.get("pro_category"));
                    row.createCell(8).setCellValue(map.get("level"));
                    row.createCell(9).setCellValue("");
                    row.createCell(10).setCellValue(map.get("stage_result"));
                    row.createCell(11).setCellValue(map.get("reimbursement_amount"));
                    row.createCell(12).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(13).setCellValue(gnodeid);
                    // 设置样式
                    for (int m = 0; m <= 13; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 11);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < mid_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(mid_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);
                wb.write(fos);
            } else {
                fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpmid.getTplPath());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname + "           年     月     日");
                int row0 = mid_sheet0_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    row = sheet0.createRow(row0);
                    rowinx = row0 - mid_sheet0_head - 1;
                    row0++;
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("p_number"));
                    row.createCell(2).setCellValue(map.get("p_name"));
                    row.createCell(3).setCellValue(map.get("leader_name"));
                    row.createCell(4).setCellValue(map.get("no"));
                    row.createCell(5).setCellValue(map.get("mobile"));
                    row.createCell(6).setCellValue(map.get("teachers"));
                    row.createCell(7).setCellValue(map.get("pro_category"));
                    row.createCell(8).setCellValue(map.get("level"));
                    row.createCell(9).setCellValue("");
                    row.createCell(10).setCellValue(map.get("stage_result"));
                    row.createCell(11).setCellValue(map.get("reimbursement_amount"));
                    row.createCell(12).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(13).setCellValue(gnodeid);
                    // 设置样式
                    for (int m = 0; m <= 13; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 11);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < mid_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(mid_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);
                wb.write(fos);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }

    }

    private boolean checkIsComplete(String expInfoId) {
        ExpInfo expInfo = (ExpInfo) CacheUtils.get(CacheUtils.EXP_INFO_CACHE + expInfoId);
        if (expInfo == null) {
            return true;
        }
        Cache<String, Object> cache = (Cache<String, Object>) CacheUtils
                .getCache(CacheUtils.EXP_STATUS_CACHE + expInfoId);
        if (cache != null) {
            int tag = 0;
            for (String s : cache.keys()) {
                if ("1".equals((String) cache.get(s))) {
                    tag++;
                }
            }
            if (expInfo.getTotalThread().equals(tag + "")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public void expCloseAfterThread(String tempPath, String year, List<String> pids, String rootpath,
            String expInfoId) {
        expCloseAfterThread(tempPath, year, pids, rootpath, expInfoId, null);
    }

    @Transactional(readOnly = false)
    public void expCloseAfterThread(String tempPath, String year, List<String> pids, String rootpath, String expInfoId,
            ItOper impVo) {
        try {
            List<Map<String, String>> data = impInfoDao.getCloseData(pids);// 获取需要审核的项目信息
            if (data != null && data.size() > 0) {
                Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                for (Map<String, String> datam : data) {
                    String offname = datam.get("oname");
                    List<Map<String, String>> olist = map.get(offname);
                    if (olist == null) {
                        olist = new ArrayList<Map<String, String>>();
                        map.put(offname, olist);
                    }
                    olist.add(datam);
                }
                for (String key : map.keySet()) {
                    // 按学院名称生成项目审核信息
                    expCloseFileByOffice(year, rootpath,
                            tempPath + File.separator + FileStepEnum.S2300.getName() + File.separator + key, key,
                            map.get(key), impVo);
                }
            }
            List<Map<String, String>> prodata = impInfoDao.getProjectMdData(pids);
            // 导出全部项目信息
            expProject(year, rootpath, tempPath + File.separator + FileStepEnum.S2300.getName(), prodata, impVo,
                    ExpType.MdExpclose.getTplTotalPath());            // 对生成的文件压缩
            File file = createZip(year, FileStepEnum.S2300.getName(),
                    tempPath + File.separator + FileStepEnum.S2300.getName(), tempPath);
            String remotePath = Global.REMOTEPATH+ ExpType.MdExpclose.getValue();
            VsftpUtils.uploadFile(remotePath, expInfoId + ".zip", file);
            SysAttachment sa = new SysAttachment();
            sa.setSize(file.length() + "");
            sa.setType(FileTypeEnum.S10);
            sa.setFileStep(FileStepEnum.S2310);
            sa.setName(FileStepEnum.S2300.getName() + ".zip");
            sa.setSuffix(".zip");
            sa.setUid(expInfoId);
            sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + "/" + expInfoId + ".zip");
            proSysAttachmentService.save(sa);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    @Transactional(readOnly = false)
    public void expMidAfterThread(String tempPath, String year, List<String> pids, String rootpath, String expInfoId) {
        expMidAfterThread(tempPath, year, pids, rootpath, expInfoId, null);
    }

    @Transactional(readOnly = false)
    public void expMidAfterThread(String tempPath, String year, List<String> pids, String rootpath, String expInfoId,
            ItOper impVo) {
        try {
            List<Map<String, String>> data = impInfoDao.getMidData(pids);// 获取需要审核的项目信息
            if (data != null && data.size() > 0) {
                Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                for (Map<String, String> datam : data) {
                    String offname = datam.get("oname");
                    List<Map<String, String>> olist = map.get(offname);
                    if (olist == null) {
                        olist = new ArrayList<Map<String, String>>();
                        map.put(offname, olist);
                    }
                    olist.add(datam);
                }
                for (String key : map.keySet()) {
                    // 按学院名称生成项目审核信息
                    expMidFileByOffice(year, rootpath,
                            tempPath + File.separator + FileStepEnum.S2200.getName() + File.separator + key, key,
                            map.get(key), impVo);
                }
            }
            List<Map<String, String>> prodata = impInfoDao.getProjectMdData(pids);
            // 导出全部项目信息
            expProject(year, rootpath, tempPath + File.separator + FileStepEnum.S2200.getName(), prodata, impVo,
                    ExpType.MdExpmid.getTplTotalPath());            // 对生成的文件压缩
            File file = createZip(year, FileStepEnum.S2200.getName(),
                    tempPath + File.separator + FileStepEnum.S2200.getName(), tempPath);
            String remotePath = Global.REMOTEPATH + ExpType.MdExpmid.getValue();
            VsftpUtils.uploadFile(remotePath, expInfoId + ".zip", file);
            SysAttachment sa = new SysAttachment();
            sa.setSize(file.length() + "");
            sa.setType(FileTypeEnum.S10);
            sa.setFileStep(FileStepEnum.S2210);
            sa.setName(FileStepEnum.S2200.getName() + ".zip");
            sa.setSuffix(".zip");
            sa.setUid(expInfoId);
            sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + "/" + expInfoId + ".zip");
            proSysAttachmentService.save(sa);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    @Transactional(readOnly = false)
    public void expApprovalAfterThread(String tempPath, String year, List<String> pids, String rootpath,
            String expInfoId) {
        expApprovalAfterThread(tempPath, year, pids, rootpath, expInfoId, null);
    }

    @Transactional(readOnly = false)
    public void expApprovalAfterThread(String tempPath, String year, List<String> pids, String rootpath,
            String expInfoId, ItOper impVo) {
        try {
            List<Map<String, String>> data = impInfoDao.getApprovalData(pids);// 获取需要审核的项目信息
            if (data != null && data.size() > 0) {
                Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                for (Map<String, String> datam : data) {
                    String offname = datam.get("oname");
                    List<Map<String, String>> olist = map.get(offname);
                    if (olist == null) {
                        olist = new ArrayList<Map<String, String>>();
                        map.put(offname, olist);
                    }
                    olist.add(datam);
                }
                for (String key : map.keySet()) {
                    // 按学院名称生成项目审核信息
                    expApprovalFileByOffice(year, rootpath,
                            tempPath + File.separator + FileStepEnum.S2000.getName() + File.separator + key, key,
                            map.get(key), impVo);
                }
            }
            List<Map<String, String>> prodata = impInfoDao.getProjectMdData(pids);
            // 导出全部项目信息
            expProject(year, rootpath, tempPath + File.separator + FileStepEnum.S2000.getName(), prodata, impVo,
                    ExpType.MdExpapproval.getTplTotalPath());
            // 对生成的文件压缩
            File file = createZip(year, FileStepEnum.S2000.getName(),
                    tempPath + File.separator + FileStepEnum.S2000.getName(), tempPath);
            String remotePath = Global.REMOTEPATH+ ExpType.MdExpapproval.getValue();
            VsftpUtils.uploadFile(remotePath, expInfoId + ".zip", file);
            SysAttachment sa = new SysAttachment();
            sa.setSize(file.length() + "");
            sa.setType(FileTypeEnum.S10);
            sa.setFileStep(FileStepEnum.S2010);
            sa.setName(FileStepEnum.S2000.getName() + ".zip");
            sa.setSuffix(".zip");
            sa.setUid(expInfoId);
            sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + "/" + expInfoId + ".zip");
            proSysAttachmentService.save(sa);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    @Transactional(readOnly = false)
    public void expQueryAfterThread(String tempPath, String year, List<String> pids, String rootpath,
            String expInfoId, ItOper impVo) {
        try {
            List<Map<String, String>> data = impInfoDao.getApprovalData(pids);// 获取需要审核的项目信息
            if (data != null && data.size() > 0) {
                Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                for (Map<String, String> datam : data) {
                    String offname = datam.get("oname");
                    List<Map<String, String>> olist = map.get(offname);
                    if (olist == null) {
                        olist = new ArrayList<Map<String, String>>();
                        map.put(offname, olist);
                    }
                    olist.add(datam);
                }
                for (String key : map.keySet()) {
                    File dirFile = new File(tempPath + File.separator + FileStepEnum.S2000.getName() + File.separator + key);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }
                }
            }

            ProModelMd proModel = new ProModelMd();
            if (StringUtils.isNotEmpty(impVo.getActywId())) {
                proModel.setActYwId(impVo.getActywId());
                proModel.setProModel(new ProModel());
            }
            if (StringUtil.checkNotEmpty(pids)) {
                proModel.getProModel().setIds(pids);
            }
            Page<ProModelMd> page = new Page<ProModelMd>();
            page.setPageSize(Page.MAX_PAGE_SIZE);
            // 导出全部项目信息
            expProjectQuery(year, rootpath, tempPath + File.separator + FileStepEnum.S2000.getName(), proModelMdService.exportMdQuery(page, proModel), impVo, ExpType.MdExpapproval.getTplTotalPath());
            // 对生成的文件压缩
            String filename = "项目汇总信息";
            File file = createZip(year, filename, tempPath + File.separator + FileStepEnum.S2000.getName(), tempPath);
            String remotePath = Global.REMOTEPATH+ ExpType.MdExpapproval.getValue();
            VsftpUtils.uploadFile(remotePath, expInfoId + ".zip", file);
            SysAttachment sa = new SysAttachment();
            sa.setSize(file.length() + "");
            sa.setType(FileTypeEnum.S10);
            sa.setFileStep(FileStepEnum.S2010);
            sa.setName(filename + ".zip");
            sa.setSuffix(".zip");
            sa.setUid(expInfoId);
            sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + "/" + expInfoId + ".zip");
            proSysAttachmentService.save(sa);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    @Transactional(readOnly = false)
    public JSONObject expClosePlus(HttpServletRequest request, HttpServletResponse response) {
        return expClosePlus(request, response, null);
    }

    @Transactional(readOnly = false)
    public JSONObject expClosePlus(HttpServletRequest request, HttpServletResponse response, ItOper impVo) {
        JSONObject js = new JSONObject();
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String year = "";
        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取需要审核的pro_model
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(ExpType.MdExpclose.getValue());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(ExpType.MdExpclose.getValue());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载中期附件
        List<String> fileSteps = new ArrayList<String>();
        fileSteps.add(FileStepEnum.S2300.getValue());
        List<VsFile> vsFiles = proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps, tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
            String key = IdGen.uuid();
           /* for (int i = 1; i < res.size(); i++) {// 反还不用的链接
                VsftpUtils.returnResource(res.get(i));
            }*/
            disCloseThread(res.get(0), tempPath, rootpath, year, pids, vsFiles, ei.getId(), key, impVo);
        } else {
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String key = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    disCloseThread(res.get(i), tempPath, rootpath, year, pids, tempList, ei.getId(), key, impVo);
                } else {
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), key, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));// 返还链接
                }
            }
        }

        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    @Transactional(readOnly = false)
    public JSONObject expMidPlus(HttpServletRequest request, HttpServletResponse response) {
        return expMidPlus(request, response, null);
    }

    @Transactional(readOnly = false)
    public JSONObject expMidPlus(HttpServletRequest request, HttpServletResponse response, ItOper impVo) {
        JSONObject js = new JSONObject();
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String year = "";
        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取立项需要审核的pro_model
        // id
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(ExpType.MdExpmid.getValue());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(ExpType.MdExpmid.getValue());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载中期附件
        List<String> fileSteps = new ArrayList<String>();
        fileSteps.add(FileStepEnum.S2200.getValue());
        List<VsFile> vsFiles = proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps, tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
            String key = IdGen.uuid();
            /*for (int i = 1; i < res.size(); i++) {
                VsftpUtils.returnResource(res.get(i));
            }*/
            disMidThread(res.get(0), tempPath, rootpath, year, pids, vsFiles, ei.getId(), key, impVo);
        } else {
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String key = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    disMidThread(res.get(i), tempPath, rootpath, year, pids, tempList, ei.getId(), key, impVo);
                } else {
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), key, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));
                }
            }
        }

        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    @Transactional(readOnly = false)
    public JSONObject expApprovalPlus(HttpServletRequest request, HttpServletResponse response) {
        return expApprovalPlus(request, response, null);
    }

    @Transactional(readOnly = false)
    public JSONObject expApprovalPlus(HttpServletRequest request, HttpServletResponse response, ItOper impVo) {
        JSONObject js = new JSONObject();
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String year = "";
        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取立项需要审核的pro_model
        // id
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(ExpType.MdExpapproval.getValue());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(ExpType.MdExpapproval.getValue());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载申报附件
        List<String> fileSteps = new ArrayList<String>();
        fileSteps.add(FileStepEnum.S2000.getValue());
        List<VsFile> vsFiles = proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps, tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
            String key = IdGen.uuid();
            /*for (int i = 1; i < res.size(); i++) {
                VsftpUtils.returnResource(res.get(i));
            }*/
            disAppThread(res.get(0), tempPath, rootpath, year, pids, vsFiles, ei.getId(), key, impVo);
        } else {
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String key = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    disAppThread(res.get(i), tempPath, rootpath, year, pids, tempList, ei.getId(), key, impVo);
                } else {
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), key, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));
                }
            }
        }

        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    private List<Vsftp> getFtpRes() {
        List<Vsftp> list = new ArrayList<Vsftp>();
        int max = 0;// 最多拿几个
        int numIdle = VsftpUtils.getNumIdle();
        if (numIdle == 0) {
            return list;
        }
        if (numIdle == 1) {
            max = numIdle;
        }
        if (numIdle > 1) {
            max = numIdle * 4 / 5;
        }
        for (int i = 0; i < max; i++) {
            Vsftp vs;
            try {
                vs = VsftpUtils.getResource();
                if (vs == null) {
                    return list;
                }
            } catch (Exception e) {
                logger.error("获取ftp链接失败", e);
                return list;
            }
            list.add(vs);
        }
        return list;
    }

    private void disAppThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key) {
        disAppThread(vs, tempPath, rootpath, year, pids, vsFiles, expInfoId, key, null);
    }

    private void disQueryThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key, ItOper impVo) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    if (checkIsComplete(expInfoId)) {
                        expQueryAfterThread(tempPath, year, pids, rootpath, expInfoId, impVo);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }
    private void disAppThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key, ItOper impVo) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    if (checkIsComplete(expInfoId)) {
                        expApprovalAfterThread(tempPath, year, pids, rootpath, expInfoId, impVo);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }

    private void disMidThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key) {
        disMidThread(vs, tempPath, rootpath, year, pids, vsFiles, expInfoId, key);
    }

    private void disMidThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key, ItOper impVo) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    if (checkIsComplete(expInfoId)) {
                        expMidAfterThread(tempPath, year, pids, rootpath, expInfoId, impVo);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }

    private void disCloseThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key) {
        disCloseThread(vs, tempPath, rootpath, year, pids, vsFiles, expInfoId, key, null);
    }

    private void disCloseThread(Vsftp vs, String tempPath, String rootpath, String year, List<String> pids,
            List<VsFile> vsFiles, String expInfoId, String key, ItOper impVo) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    if (checkIsComplete(expInfoId)) {
                        expCloseAfterThread(tempPath, year, pids, rootpath, expInfoId, impVo);
                    }
                } catch (Exception e) {
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }

    public void expApproval(HttpServletRequest request, HttpServletResponse response) {
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

        String year = "";
        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        try {
            List<String> pids = actTaskService.getAllTodoId(actywId, gnodeId);// 获取立项需要审核的pro_model
            // id
            if (pids != null && pids.size() > 0) {
                List<String> fileSteps = new ArrayList<String>();
                fileSteps.add(FileStepEnum.S2000.getValue());
                FileVo fileVo = proSysAttachmentService.downloads(FileTypeEnum.S10, pids, fileSteps, tempPath);// 下载学生申报时上传的文件,并创建目录结构
                if ((fileVo.getStatus()).equals(FileVo.SUCCESS)) {
                    List<Map<String, String>> data = impInfoDao.getApprovalData(pids);// 获取需要审核的项目信息
                    if (data != null && data.size() > 0) {
                        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                        for (Map<String, String> datam : data) {
                            String offname = datam.get("oname");
                            List<Map<String, String>> olist = map.get(offname);
                            if (olist == null) {
                                olist = new ArrayList<Map<String, String>>();
                                map.put(offname, olist);
                            }
                            olist.add(datam);
                        }
                        for (String key : map.keySet()) {
                            // 按学院名称生成项目审核信息
                            expApprovalFileByOffice(year, rootpath,
                                    tempPath + File.separator + FileStepEnum.S2000.getName() + File.separator + key,
                                    key, map.get(key));
                        }
                    }
                    List<Map<String, String>> prodata = impInfoDao.getProjectMdData(pids);
                    // 导出全部项目信息
                    expProject(year, rootpath, tempPath + File.separator + FileStepEnum.S2000.getName(), prodata);
                } else {
                    logger.error("下载项目申请报告失败");
                }
            }
            // 对生成的文件压缩，下载
            createZip(year, FileStepEnum.S2000.getName(), tempPath + File.separator + FileStepEnum.S2000.getName(),
                    response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

    // 导出项目表
    private void expProject(String year, String rootpath, String filepath, List<Map<String, String>> list) {
        expProject(year, rootpath, filepath, list, null, null);
    }

    // 导出项目表
    private void expProject(String year, String rootpath, String filepath, List<Map<String, String>> list, ItOper impVo,
            String tplPath) {
        File dir = new File(filepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(filepath + File.separator + "项目信息.xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = null;
            // 导出全部项目信息
            if ((impVo == null) || StringUtil.isEmpty(tplPath) || impVo.getReqParam().getIsMdOld()) {
                fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + EXP_PROJECTMD_TEMPLATE_XLSX);
            } else {
                fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + tplPath);
            }

            fs = new FileInputStream(fi);
            XSSFWorkbook wb = new XSSFWorkbook(fs);

            XSSFCellStyle rowStyle = wb.createCellStyle();
            rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            XSSFDataFormat format = wb.createDataFormat();
            rowStyle.setDataFormat(format.getFormat("@"));

            XSSFSheet sheet0 = wb.getSheetAt(0);

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(year + c0.getStringCellValue());

            int row0 = 3;
            for (Map<String, String> map : list) {
                XSSFRow row = sheet0.createRow(row0);
                row0++;
                row.createCell(0).setCellValue(row0 - 3 + "");
                row.createCell(1).setCellValue(map.get("oname"));
                row.createCell(2).setCellValue(map.get("p_number"));
                row.createCell(3).setCellValue(map.get("p_name"));
                row.createCell(4).setCellValue(map.get("pro_category"));
                row.createCell(5).setCellValue(map.get("level"));
                row.createCell(6).setCellValue(map.get("leader_name"));
                row.createCell(7).setCellValue(map.get("no"));
                row.createCell(8).setCellValue(map.get("mobile"));
                int nums = 1;
                String members = map.get("members");
                if (StringUtil.isNotEmpty(members)) {
                    nums = nums + (members).split("、").length;
                }
                row.createCell(9).setCellValue(nums + "");
                row.createCell(10).setCellValue(members);
                String[] teas = null;
                if (StringUtil.isNotEmpty(map.get("teachers"))) {
                    teas = map.get("teachers").split(",");
                }
                row.createCell(11).setCellValue(teas != null ? teas[0] : "");
                row.createCell(12).setCellValue(teas != null ? teas[1] : "");
                row.createCell(13).setCellValue(teas != null ? teas[2] : "");
                row.createCell(14).setCellValue(map.get("s3l"));
                row.createCell(15).setCellValue(map.get("introduction"));

                // 设置样式
                for (int m = 0; m <= 15; m++) {
                    row.getCell(m).setCellStyle(rowStyle);
                }
            }
            wb.write(fos);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }
    }
    // 导出项目表
    private void expProjectQuery(String year, String rootpath, String filepath, List<ExpProModelMdVo> list, ItOper impVo,
            String tplPath) {
        File dir = new File(filepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(filepath + File.separator + "项目汇总信息.xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = null;
            // 导出全部项目信息
            if ((impVo == null) || StringUtil.isEmpty(tplPath) || impVo.getReqParam().getIsMdOld()) {
                fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + EXP_PROJECTMD_TEMPLATE_XLSX);
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                wb.write(fos);
            } else {
                fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + tplPath);
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);

                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue((StringUtil.isEmpty(year)?"":year) + c0.getStringCellValue());

                int row0 = 1;
                for (ExpProModelMdVo pm : list) {
                    XSSFRow row = sheet0.createRow(row0);
                    row0++;
                    row.createCell(0).setCellValue(pm.getYear());
//                    row.createCell(1).setCellValue(pm.getOfficeArea());
                    row.createCell(1).setCellValue(pm.getOfficeAname());
                    row.createCell(2).setCellValue(pm.getOfficeNo());
                    row.createCell(3).setCellValue(pm.getScName());
//                    row.createCell(3).setCellValue(pm.getOfficeName());
                    row.createCell(4).setCellValue(pm.getPno());
                    row.createCell(5).setCellValue(pm.getPname());
                    row.createCell(6).setCellValue(pm.getProCategoryName());
                    if (StringUtil.checkNotEmpty(pm.getTeamLeader())) {
                        row.createCell(7).setCellValue(pm.getTeamLeaderName());
                        row.createCell(8).setCellValue(pm.getTeamLeaderNo());
                        row.createCell(9).setCellValue(pm.getOfficeName());
                        row.createCell(10).setCellValue(pm.getTeamLeaderMobile());
                    } else {
                        row.createCell(7).setCellValue("");
                        row.createCell(8).setCellValue("");
                        row.createCell(9).setCellValue("");
                        row.createCell(10).setCellValue("");
                    }
                    row.createCell(11).setCellValue(pm.getTeamOtherss());
                    if (StringUtil.checkNotEmpty(pm.getTeacherVos())) {
                        row.createCell(12).setCellValue(pm.getTeacherNames());
                        row.createCell(13).setCellValue(pm.getTeacherNos());
                        row.createCell(14).setCellValue(pm.getTeacherZcs());
                    } else {
                        row.createCell(12).setCellValue("");
                        row.createCell(13).setCellValue("");
                        row.createCell(14).setCellValue("");
                    }
                    row.createCell(15).setCellValue("");
                    row.createCell(16).setCellValue("");
                    row.createCell(17).setCellValue("");
                    row.createCell(18).setCellValue(pm.getCourse());
                    row.createCell(19).setCellValue(pm.getIntroduction());
                    row.createCell(20).setCellValue(pm.getSjNo());
                    row.createCell(21).setCellValue(pm.getXxNo());

                    // 设置样式
                    for (int m = 0; m <= 21; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                wb.write(fos);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }
    }

    // 导出学院申报汇总表
    private void expApprovalFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list) {
        expApprovalFileByOffice(year, rootpath, filepath, oname, list, null);
    }

    // 导出学院申报汇总表
    private void expApprovalFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list, ItOper impVo) {
        File dirFile = new File(filepath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(filepath + File.separator + "大学生创新创业训练计划申报汇总表_" + oname + ".xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = null;
            if ((impVo == null) || impVo.getReqParam().getIsMdOld()) {
                fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpapproval.getTplname());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname);
                XSSFSheet sheet1 = wb.getSheetAt(1);
                XSSFCell c1 = sheet1.getRow(0).getCell(0);
                c1.setCellValue(year + c1.getStringCellValue());
                sheet1.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname);
                int row0 = approval_sheet0_head + 2;
                int row1 = approval_sheet1_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    if ("0000000197".equals(map.get("app_level")) || "0000000198".equals(map.get("app_level"))) {// 国家级、省级
                        row = sheet0.createRow(row0);
                        rowinx = row0 - approval_sheet0_head - 1;
                        row0++;
                    } else if ("0000000199".equals(map.get("app_level"))) {// 校级
                        row = sheet1.createRow(row1);
                        rowinx = row1 - approval_sheet1_head - 1;
                        row1++;
                    } else {
                        row = sheet1.createRow(row1);
                        rowinx = row1 - approval_sheet1_head - 1;
                        row1++;
                    }
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("pro_category"));
                    row.createCell(2).setCellValue(map.get("level"));
                    row.createCell(3).setCellValue(map.get("p_number"));
                    row.createCell(4).setCellValue(map.get("p_name"));
                    row.createCell(5).setCellValue(map.get("leader_name"));
                    row.createCell(6).setCellValue(map.get("no"));
                    row.createCell(7).setCellValue(map.get("mobile"));
                    row.createCell(8).setCellValue("A".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(9).setCellValue("B".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(10).setCellValue("C".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(11).setCellValue(map.get("source_project_name"));
                    row.createCell(12).setCellValue(map.get("source_project_type"));
                    String[] teas = null;
                    if (StringUtil.isNotEmpty(map.get("teachers"))) {
                        teas = map.get("teachers").split(",");
                    }
                    row.createCell(13).setCellValue(teas != null ? teas[0] : "");
                    row.createCell(14).setCellValue(teas != null ? teas[1] : "");
                    row.createCell(15).setCellValue(teas != null ? teas[2] : "");
                    row.createCell(16).setCellValue(teas != null ? teas[3] : "");
                    row.createCell(17).setCellValue(map.get(""));
                    String[] stus = null;
                    if (StringUtil.isNotEmpty(map.get("members"))) {
                        stus = map.get("members").split(",");
                    }
                    row.createCell(18).setCellValue(stus != null ? stus[0] : "");
                    row.createCell(19).setCellValue(stus != null ? stus[1] : "");
                    row.createCell(20).setCellValue(stus != null ? stus[2] : "");
                    row.createCell(21).setCellValue(map.get(""));
                    row.createCell(22).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(23).setCellValue(gnodeid);
                    // 设置样式
                    for (int m = 0; m <= 23; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 14);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < approval_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(approval_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);

                for (int k = 0; k < approval_sheet1_foot.length; k++) {
                    sheet1.createRow(row1 + k).createCell(0).setCellValue(approval_sheet1_foot[k]);
                }
                sheet1.getRow(row1).getCell(0).setCellStyle(cellStyle);

                wb.write(fos);
            } else {
                fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpapproval.getTplPath());
                fs = new FileInputStream(fi);
                XSSFWorkbook wb = new XSSFWorkbook(fs);

                XSSFCellStyle rowStyle = wb.createCellStyle();
                rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
                rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
                rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
                rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
                XSSFDataFormat format = wb.createDataFormat();
                rowStyle.setDataFormat(format.getFormat("@"));

                XSSFSheet sheet0 = wb.getSheetAt(0);
                XSSFCell c0 = sheet0.getRow(0).getCell(0);
                c0.setCellValue(year + c0.getStringCellValue());
                sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname);
                XSSFSheet sheet1 = wb.getSheetAt(1);
                XSSFCell c1 = sheet1.getRow(0).getCell(0);
                c1.setCellValue(year + c1.getStringCellValue());
                sheet1.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname);
                int row0 = approval_sheet0_head + 2;
                int row1 = approval_sheet1_head + 2;
                for (Map<String, String> map : list) {
                    XSSFRow row = null;
                    int rowinx = 0;
                    if ("0000000197".equals(map.get("app_level")) || "0000000198".equals(map.get("app_level"))) {// 国家级、省级
                        row = sheet0.createRow(row0);
                        rowinx = row0 - approval_sheet0_head - 1;
                        row0++;
                    } else if ("0000000199".equals(map.get("app_level"))) {// 校级
                        row = sheet1.createRow(row1);
                        rowinx = row1 - approval_sheet1_head - 1;
                        row1++;
                    } else {
                        row = sheet1.createRow(row1);
                        rowinx = row1 - approval_sheet1_head - 1;
                        row1++;
                    }
                    row.createCell(0).setCellValue(rowinx + "");
                    row.createCell(1).setCellValue(map.get("pro_category"));
                    row.createCell(2).setCellValue(map.get("level"));
                    row.createCell(3).setCellValue(map.get("p_number"));
                    row.createCell(4).setCellValue(map.get("p_name"));
                    row.createCell(5).setCellValue(map.get("leader_name"));
                    row.createCell(6).setCellValue(map.get("no"));
                    row.createCell(7).setCellValue(map.get("mobile"));
                    row.createCell(8).setCellValue("A".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(9).setCellValue("B".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(10).setCellValue("C".equals(map.get("pro_source")) ? "√" : "");
                    row.createCell(11).setCellValue(map.get("source_project_name"));
                    row.createCell(12).setCellValue(map.get("source_project_type"));
                    String[] teas = null;
                    if (StringUtil.isNotEmpty(map.get("teachers"))) {
                        teas = map.get("teachers").split(",");
                    }
                    row.createCell(13).setCellValue(teas != null ? teas[0] : "");
                    row.createCell(14).setCellValue(teas != null ? teas[1] : "");
                    row.createCell(15).setCellValue(teas != null ? teas[2] : "");
                    row.createCell(16).setCellValue(teas != null ? teas[3] : "");
                    row.createCell(17).setCellValue(map.get(""));
                    String[] stus = null;
                    if (StringUtil.isNotEmpty(map.get("members"))) {
                        stus = map.get("members").split(",");
                    }
                    row.createCell(18).setCellValue(stus != null ? stus[0] : "");
                    row.createCell(19).setCellValue(stus != null ? stus[1] : "");
                    row.createCell(20).setCellValue(stus != null ? stus[2] : "");
                    row.createCell(21).setCellValue(map.get(""));
                    row.createCell(22).setCellValue(map.get("id"));
                    String gnodeid = "";
                    ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                    if (actYwGnode != null) {
                        gnodeid = actYwGnode.getId();
                    }
                    row.createCell(23).setCellValue(gnodeid);
                    row.createCell(24);
                    row.createCell(25);
                    row.createCell(26);
                    // 设置样式
                    for (int m = 0; m <= 26; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
                // 尾部
                XSSFCellStyle cellStyle = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
                font.setFontName("仿宋_GB2312");
                font.setFontHeightInPoints((short) 14);// 设置字体大小
                cellStyle.setFont(font);

                for (int k = 0; k < approval_sheet0_foot.length; k++) {
                    sheet0.createRow(row0 + k).createCell(0).setCellValue(approval_sheet0_foot[k]);
                }
                sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);

                for (int k = 0; k < approval_sheet1_foot.length; k++) {
                    sheet1.createRow(row1 + k).createCell(0).setCellValue(approval_sheet1_foot[k]);
                }
                sheet1.getRow(row1).getCell(0).setCellStyle(cellStyle);

                wb.write(fos);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fs != null)
                    fs.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }

    }

    private void createZip(String year, String filename, String sourcePath, HttpServletResponse response) {
        OutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            String headStr = "attachment; filename=\"" + new String((year + filename + ".zip").getBytes(), "ISO-8859-1")
                    + "\"";
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            fos = response.getOutputStream();
            zos = new ZipOutputStream(fos);
            zos.setEncoding("utf-8"); // 解决linxu乱码
            writeZip(new File(sourcePath), "", zos);
        } catch (Exception e) {
            logger.error("失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                logger.error("创建ZIP文件失败", e);
            }
        }
    }

    private File createZip(String year, String filename, String sourcePath, String tempPath) {
        System.out.println(sourcePath);
        OutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File zipfile = new File(tempPath + File.separator + year + filename + ".zip");
            System.out.println(zipfile.getAbsolutePath());
            zipfile.deleteOnExit();
            fos = new FileOutputStream(zipfile);
            zos = new ZipOutputStream(fos);
            zos.setEncoding("utf-8"); // 解决linxu乱码
            writeZip(new File(sourcePath), "", zos);
            return zipfile;
        } catch (Exception e) {
            logger.error("失败", e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                logger.error("创建ZIP文件失败", e);
            }
        }
        return null;
    }

    private void writeZip(File file, String parentPath, ZipOutputStream zos) throws IOException {
        if (file.exists()) {
            ZipEntry ze = null;
            if (file.isDirectory()) {// 处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        writeZip(f, parentPath, zos);
                    }
                } else { // 空目录则创建当前目录
                    try {
                        ze = new ZipEntry(parentPath);
                        ze.setUnixMode(755);// 解决linux乱码 文件设置644 目录设置755
                        zos.putNextEntry(ze);

                        zos.flush();
                    } finally {
                        if (zos != null) {
                            zos.closeEntry();
                        }
                    }
                }
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    ze = new ZipEntry(parentPath + file.getName());
                    ze.setUnixMode(644);// 解决linux乱码 文件设置644 目录设置755
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                    if (zos != null) {
                        zos.closeEntry();
                    }
                }
            }
        }
    }

    // 自定义导出学院申报汇总表
    private void expGnodeFileByOffice(String year, String rootpath, String filepath, String oname,
            List<Map<String, String>> list) {
        File dirFile = new File(filepath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(filepath + File.separator + "大学生创新创业训练计划申报汇总表_" + oname + ".xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + "exp_gnode_template.xlsx");
            fs = new FileInputStream(fi);
            XSSFWorkbook wb = new XSSFWorkbook(fs);

            XSSFCellStyle rowStyle = wb.createCellStyle();
            rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            XSSFDataFormat format = wb.createDataFormat();
            rowStyle.setDataFormat(format.getFormat("@"));

            XSSFSheet sheet0 = wb.getSheetAt(0);
            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(year + c0.getStringCellValue());
            sheet0.getRow(1).getCell(0).setCellValue("学院名称(盖章)：" + oname);
            int row0 = approval_sheet0_head + 2;
            int row1 = approval_sheet1_head + 2;
            for (Map<String, String> map : list) {
                XSSFRow row = null;
                int rowinx = 0;
                row = sheet0.createRow(row1);
                rowinx = row1 - approval_sheet1_head - 1;
                row1++;

                row.createCell(0).setCellValue(rowinx + "");
                row.createCell(1).setCellValue(map.get("pro_category"));
                row.createCell(2).setCellValue(map.get("oname"));
                row.createCell(3).setCellValue(map.get("p_number"));
                row.createCell(4).setCellValue(map.get("p_name"));
                row.createCell(5).setCellValue(map.get("leader_name"));
                row.createCell(6).setCellValue(map.get("no"));
                row.createCell(7).setCellValue(map.get("mobile"));
                // row.createCell(8).setCellValue("A".equals(map.get("pro_source"))
                // ? "√" : "");
                // row.createCell(9).setCellValue("B".equals(map.get("pro_source"))
                // ? "√" : "");
                // row.createCell(10).setCellValue("C".equals(map.get("pro_source"))
                // ? "√" : "");
                // row.createCell(11).setCellValue(map.get("source_project_name"));
                // row.createCell(12).setCellValue(map.get("source_project_type"));
                String[] teas = null;
                if (StringUtil.isNotEmpty(map.get("teachers"))) {
                    teas = map.get("teachers").split(",");
                }
                row.createCell(8).setCellValue(teas != null ? teas[0] : "");
                row.createCell(9).setCellValue(teas != null ? teas[1] : "");
                row.createCell(10).setCellValue(teas != null ? teas[2] : "");
                row.createCell(11).setCellValue(teas != null ? teas[3] : "");

                String[] stus = null;
                if (StringUtil.isNotEmpty(map.get("members"))) {
                    stus = map.get("members").split(",");
                }
                row.createCell(12).setCellValue(stus != null ? stus[0] : "");
                row.createCell(13).setCellValue(stus != null ? stus[1] : "");
                row.createCell(14).setCellValue(stus != null ? stus[2] : "");

                // row.createCell(15).setCellValue(map.get("id"));
                // String gnodeid = "";
                // ActYwGnode actYwGnode =
                // actTaskService.getNodeByProInsId(map.get("proc_ins_id"));
                // if (actYwGnode != null) {
                // gnodeid = actYwGnode.getId();
                // }
                // row.createCell(16).setCellValue(gnodeid);
                // 设置样式
                for (int m = 0; m <= 14; m++) {
                    row.getCell(m).setCellStyle(rowStyle);
                }
            }
            // 尾部
            XSSFCellStyle cellStyle = wb.createCellStyle();
            XSSFFont font = wb.createFont();
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            font.setFontName("仿宋_GB2312");
            font.setFontHeightInPoints((short) 14);// 设置字体大小
            cellStyle.setFont(font);

            // for (int k = 0; k < approval_sheet0_foot.length; k++) {
            // sheet0.createRow(row0 +
            // k).createCell(0).setCellValue(approval_sheet0_foot[k]);
            // }
            sheet0.getRow(row0).getCell(0).setCellStyle(cellStyle);

            wb.write(fos);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fs != null)
                    fs.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }

    }

    private XSSFWorkbook WriteExcel(List<ProModel> list, String titleName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("sheet-1");
        sheet1.setDefaultColumnWidth(20);
        XSSFRow title = sheet1.createRow(0);
        Font ztFont = workbook.createFont();
        ztFont.setFontName("黑体");
        XSSFCellStyle alignStyle = workbook.createCellStyle();
        alignStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        alignStyle.setFont(ztFont);
        XSSFCell cellTitile = title.createCell(0);
        cellTitile.setCellValue(titleName);
        cellTitile.setCellStyle(alignStyle);

        // 合并第1行中的12列
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 11); // 参数都是从O开始
        sheet1.addMergedRegion(region);
        XSSFRow cetitle = sheet1.createRow(1);
        for (int i = 0; i < tableTitle.size(); i++) {
            XSSFCell cell = cetitle.createCell(i);
            cell.setCellValue(tableTitle.get(i));
            cell.setCellStyle(alignStyle);
        }
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                User user = UserUtils.get(list.get(i).getDeclareId());
                XSSFRow row = sheet1.createRow(i + 2);

                XSSFCell cell0 = row.createCell(0);
                cell0.setCellValue(list.get(i).getCompetitionNumber());

                XSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(list.get(i).getpName());

                XSSFCell cell2 = row.createCell(2);
                cell2.setCellValue(DictUtils.getDictLabel(list.get(i).getProCategory(), FlowPcategoryType.PCT_XM.getKey(), ""));

                XSSFCell cell3 = row.createCell(3);
                XSSFCell cell4 = row.createCell(4);
                XSSFCell cell5 = row.createCell(5);
                XSSFCell cell6 = row.createCell(6);
                if (user == null) {
                    cell3.setCellValue("");
                    cell4.setCellValue("");
                    cell5.setCellValue("");
                    cell6.setCellValue("");
                } else {
                    cell3.setCellValue(StringUtil.isNotEmpty(user.getName()) ? user.getName() : "");
                    cell4.setCellValue(StringUtil.isNotEmpty(user.getNo()) ? user.getNo() : "");
                    cell5.setCellValue(StringUtil.isNotEmpty(user.getMobile()) ? user.getMobile() : "");
                    cell6.setCellValue(StringUtil.isNotEmpty(user.getEmail()) ? user.getEmail() : "");
                }

                if (list.get(i).getDeuser() != null && list.get(i).getDeuser().getSubject() != null) {
                    XSSFCell cell7 = row.createCell(7);
                    cell7.setCellValue(list.get(i).getDeuser().getSubject().getName());
                }

                XSSFCell cell8 = row.createCell(8);
                cell8.setCellValue(list.get(i).getFinalResult());

                List<Map<String, String>> students = projectDeclareService
                        .findTeamStudentFromTUH(list.get(i).getTeamId(), list.get(i).getId());
                List<String> partner = students.stream().filter(e -> !user.getNo().equals(e.get("no")))
                        .map(e -> e.get("name") + e.get("no")).collect(Collectors.toList());

                if (!partner.isEmpty()) {
                    XSSFCell cell9 = row.createCell(9);
                    cell9.setCellValue(StringUtils.join(partner, ","));
                }

                List<Map<String, String>> teachersList = projectDeclareService
                        .findTeamTeacherFromTUH(list.get(i).getTeamId(), list.get(i).getId());
                List<String> teachers = teachersList.stream().map(e -> e.get("name") + e.get("no"))
                        .collect(Collectors.toList());
                if (!teachers.isEmpty()) {
                    XSSFCell cell10 = row.createCell(10);
                    cell10.setCellValue(StringUtils.join(teachers, ","));
                }

                XSSFCell cell11 = row.createCell(11);
                cell11.setCellValue(list.get(i).getIntroduction());

            }
        }
        return workbook;
    }

    /**************************************************************************************
     * 自定义导出数据包括（自定义大赛\自定义项目）.
     *
     * @param actywId
     * @param request
     * @param response
     */
    public JSONObject expData(String actywId, ItReqParam param, HttpServletRequest request,
            HttpServletResponse response) {
        /**
         * 查询过滤数据.
         */
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        ActYw actYw = actYwService.get(actywId);
        JSONObject js = new JSONObject();
        if ((actYw == null) || (actYw.getFtheme() == null)) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "业务ID参数必填！");
            return js;
        }
        List<String> pids = null;
        if ((FormTheme.F_TLXY).equals(actYw.getFtheme())) {
            List<ProModelTlxy> list = proModelTlxyService.findListByQuery(request, actYw, year, officeId,
                    param.getGnodeId());
            if (StringUtil.checkNotEmpty(list)) {
                pids = list.stream().map(e -> e.getProModel().getId()).collect(Collectors.toList());
            } else {
                pids = Lists.newArrayList();
                logger.info("当前角色在当前流程节点没有审核或查询数据");
            }
        } else {
            List<ProModel> list = proModelService.findListByQuery(request, actYw, year, officeId, param.getGnodeId());
            if (StringUtil.checkNotEmpty(list)) {
                pids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
            } else {
                pids = Lists.newArrayList();
                logger.info("当前角色在当前流程节点没有审核或查询数据");
            }
        }

        /**
         * 生成附件.
         */
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            logger.info("没有需要导出的数据");
            return js;
        }

        /**
         * 生成附件.
         */
        ActYwGnode actYwGnode = actYwGnodeService.get(param.getGnodeId());
        if (genExpData(param, request, response, actYw, pids, actYwGnode)) {
            // genEfile(response, gnodeId, actYw, list);
            js.put(CoreJkey.JK_RET, 1);
            js.put(CoreJkey.JK_MSG, "导出成功！");
            return js;
        }
        js.put(CoreJkey.JK_RET, 0);
        js.put(CoreJkey.JK_MSG, "导出生成文件失败！");
        return js;
    }

    /**
     * 导出数据生成Excel.
     *
     * @param param
     * @param request
     * @param response
     * @param actYw
     * @param pids
     * @param actYwGnode
     */
    public boolean genExpData(ItReqParam param, HttpServletRequest request, HttpServletResponse response, ActYw actYw,
            List<String> pids, ActYwGnode actYwGnode) {
        FormTheme ftheme = actYw.getFtheme();
        ExpGnodeFile expGfile = new ExpGnodeFile(param);
        if (expGfile.getParam() == null) {
            expGfile.setParam(new ExportParams());
        }
        List<? extends IWorkRes> wres = Lists.newArrayList();
        if ((FormTheme.F_COM).equals(ftheme) || (FormTheme.F_MD_COM).equals(ftheme)) {
            expGfile.setClazz(ExpProModelVo.class);
            Page<ProModel> curpage = new Page<ProModel>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            wres = proModelService.exportData(curpage, new ProModel(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        } else if ((FormTheme.F_GZSMXX).equals(ftheme)) {
            Page<ProModelGzsmxx> curpage = new Page<ProModelGzsmxx>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            expGfile.setClazz(ExpProModelVo.class);
            wres = proModelGzsmxxService.exportData(curpage, new ProModelGzsmxx(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        } else if ((FormTheme.F_MD).equals(ftheme)) {
            Page<ProModelMd> curpage = new Page<ProModelMd>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            expGfile.setClazz(ExpProModelVo.class);
            wres = proModelMdService.exportData(curpage, new ProModelMd(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        } else if ((FormTheme.F_MD_GC).equals(ftheme)) {
            Page<ProModelMdGc> curpage = new Page<ProModelMdGc>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            expGfile.setClazz(ExpProModelVo.class);
            wres = proModelMdGcService.exportData(curpage, new ProModelMdGc(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        } else if ((FormTheme.F_TLXY).equals(ftheme)) {
            Page<ProModelTlxy> curpage = new Page<ProModelTlxy>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            expGfile.setClazz(ExpProModelTlxyVo.class);
            wres = proModelTlxyService.exportData(curpage, new ProModelTlxy(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpPmTlxyVoHandler(expGfile.getReqParam()));
        } else {
            Page<ProModel> curpage = new Page<ProModel>(request, response);
            curpage.setPageSize(Page.MAX_PAGE_SIZE);
            expGfile.setClazz(ExpProModelVo.class);
            wres = proModelService.exportData(curpage, new ProModel(pids, actYw.getId()));
            expGfile.getParam().setDataHandler(new DataExpVoHandler(expGfile.getReqParam()));
        }
        IWorkFlow.expRenderFile(request, response, wres, actYw, actYwGnode, expGfile);
        return true;
    }

    /**
     * 自定义导出数据和附件包括（自定义大赛\自定义项目\铜陵学院项目\互联网+大赛）.
     *
     * @param request
     * @param response
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject expDataByGnode(ItReqParam param, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查询过滤数据.
         */
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        ActYw actYw = actYwService.get(param.getActywId());
        ActYwGnode actYwGnode = actYwGnodeService.get(param.getGnodeId());
        List<String> pids = null;
        if ((FormTheme.F_TLXY).equals(actYw.getFtheme())) {
            List<ProModelTlxy> list = proModelTlxyService.findListByQuery(request, actYw, year, officeId,
                    param.getGnodeId());
            if (StringUtil.checkNotEmpty(list)) {
                pids = list.stream().map(e -> e.getProModel().getId()).collect(Collectors.toList());
            } else {
                pids = Lists.newArrayList();
                logger.info("当前角色在当前流程节点没有审核或查询数据");
            }
        } else {
            List<ProModel> list = proModelService.findListByQuery(request, actYw, year, officeId, param.getGnodeId());
            if (StringUtil.checkNotEmpty(list)) {
                pids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
            } else {
                pids = Lists.newArrayList();
                logger.info("当前角色在当前流程节点没有审核或查询数据");
            }
        }

        /**
         * 生成附件.
         */
        JSONObject js = new JSONObject();
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            logger.info("没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            logger.info("无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(param.getGnodeId());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(param.getGnodeId());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载民大申报附件
        // List<VsFile> vsFiles =
        // proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps,
        // tempPath);
        // 自定义附件
        // List<VsFile> vsFiles =
        // proSysAttachmentService.getVsGnodeFiles(FileTypeEnum.S11, pids,
        // actYwGnode.getName(), tempPath);
        List<VsFile> vsFiles = proSysAttachmentService.getVsGnodeFilesByImpProModel(FileTypeEnum.S11, pids,
                proModelService.findList(new ProModel(pids)), actYwGnode.getName(), tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            logger.info("文件数小于ftp链接数的直接用一个线程处理(1)");
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
           /* for (int i = 1; i < res.size(); i++) {
                VsftpUtils.returnResource(res.get(i));
            }*/
            disAutoThread(request, response, actYw, param.getGnodeId(), res.get(0), tempPath, year, pids, vsFiles,
                    ei.getId(), IdGen.uuid());
        } else {
            logger.info("文件数大于ftp链接数的直接用[" + res.size() + "]线程处理(" + res.size() + ")");
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String curkey = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    logger.info("附件列表存在数据,size=[" + tempList.size() + "],准备启动多线程处理");
                    disAutoThread(request, response, actYw, param.getGnodeId(), res.get(i), tempPath, year, pids,
                            tempList, ei.getId(), curkey);
                } else {
                    logger.info("附件列表为空,处理缓存信息，同时释放Ftp资源！");
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), curkey, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));
                }
            }
        }

        js.put(CoreJkey.JK_MSG, "导出成功");
        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    /**
     * 处理导出数据和附件，生成学院附件汇总表.
     *
     * @param request
     * @param response
     * @param actYw
     * @param gnodeId
     * @param tempPath
     * @param year
     * @param pids
     * @param expInfoId
     */
    @Transactional(readOnly = false)
    public void expGnodeAfterThread(HttpServletRequest request, HttpServletResponse response, ActYw actYw,
            String gnodeId, String tempPath, String year, List<String> pids, String expInfoId) {
        try {
            logger.error("开始：处理导出数据和附件，生成学院附件汇总表！");
            if ((actYw == null) || StringUtil.isEmpty(actYw.getId()) || StringUtil.isEmpty(gnodeId)) {
                logger.error("必填参数：yw.id或gnodeId为空！");
                return;
            }

            ActYwGnode gnode = actYwGnodeService.get(gnodeId);

            if (WorkFlowUtil.genZipByService(request, response, actYw, tempPath, pids, gnode)) {
                logger.info("对生成的文件压缩！");
                // 对生成的文件压缩
                File file = createZip(year, gnode.getName(), tempPath + File.separator + gnode.getName(), tempPath);
                String remotePath = Global.REMOTEPATH+ gnodeId;
                VsftpUtils.uploadFile(remotePath, expInfoId + StringUtil.DOT + FileUtil.SUFFIX_ZIP, file);
                SysAttachment sa = new SysAttachment();
                sa.setSize(file.length() + "");
                sa.setType(FileTypeEnum.S11);
                sa.setName(gnode.getName() + StringUtil.DOT + FileUtil.SUFFIX_ZIP);
                sa.setSuffix(StringUtil.DOT + FileUtil.SUFFIX_ZIP);
                sa.setUid(expInfoId);
                sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + StringUtil.LINE + expInfoId + StringUtil.DOT + FileUtil.SUFFIX_ZIP);
                proSysAttachmentService.save(sa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("异常：处理导出数据和附件，生成学院附件汇总表！" + e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            logger.error("完成：ExpInfo.id = " + ei.getId());
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            logger.error("移除缓存，开始。标识：" + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            logger.error("移除缓存，结束。标识：" + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                logger.info("删除临时目录，开始");
                FileUtil.deleteFileOrDir(f);
                logger.info("删除临时目录，结束");
            }
            logger.info("附件压缩处理结束");
        }
    }

    /**
     * 多线程处理附件: 创建多线程处理附件及目录结构.
     *
     * @param request
     * @param response
     * @param actYw
     * @param gnodeId
     * @param vs
     * @param tempPath
     * @param year
     * @param pids
     * @param vsFiles
     * @param expInfoId
     * @param key
     */
    private void disAutoThread(HttpServletRequest request, HttpServletResponse response, ActYw actYw, String gnodeId,
            Vsftp vs, String tempPath, String year, List<String> pids, List<VsFile> vsFiles, String expInfoId,
            String key) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    logger.info("创建线程，下载附件,标识：" + expInfoId + "-" + key);
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    logger.info("附件下载完成,正在检查线程是否完成，并处理压缩文件！");
                    if (checkIsComplete(expInfoId)) {
                        logger.info("线程检查结果为：完成,开始处理附件合并生成压缩包操作！");
                        expGnodeAfterThread(request, response, actYw, gnodeId, tempPath, year, pids, expInfoId);
                    }
                } catch (Exception e) {
                    logger.info("异常：下载压缩附件不正确！");
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }

    /**************************************************************************************
     * 大创导出数据包括.
     *
     * @param actywId
     * @param request
     * @param response
     */
    public JSONObject expPdData(String actywId, ItReqParam param, HttpServletRequest request,
            HttpServletResponse response) {
        /**
         * 查询过滤数据.
         */
        String year = request.getParameter("year");
        String officeId = request.getParameter("officeId");
        ActYw actYw = actYwService.get(actywId);
        JSONObject js = new JSONObject();
        if ((actYw == null) || (actYw.getFtheme() == null)) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "业务ID参数必填！");
            return js;
        }
        List<ProjectDeclare> list = projectDeclareService.findListByQuery(new ProjectDeclare());
        // List<ProModel> list2 = proModelService.findListByQuery(request,
        // actYw, year, officeId, param.getGnodeId());
        List<String> pids = null;
        if (StringUtil.checkNotEmpty(list)) {
            pids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        } else {
            pids = Lists.newArrayList();
        }

        /**
         * 生成附件.
         */
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            logger.info("没有需要导出的数据");
            return js;
        }

        /**
         * 生成附件.
         */
        ActYwGnode actYwGnode = ProjectNodeVo.gnodeFiles();
        if (genExpPdData(param, request, response, actYw, pids, actYwGnode)) {
            js.put(CoreJkey.JK_RET, 1);
            js.put(CoreJkey.JK_MSG, "导出成功！");
            return js;
        }
        js.put(CoreJkey.JK_RET, 0);
        js.put(CoreJkey.JK_MSG, "导出生成文件失败！");
        return js;
    }

    /**
     * 大创导出数据生成Excel.
     *
     * @param param
     * @param request
     * @param response
     * @param actYw
     * @param pids
     * @param actYwGnode
     */
    public boolean genExpPdData(ItReqParam param, HttpServletRequest request, HttpServletResponse response, ActYw actYw,
            List<String> pids, ActYwGnode actYwGnode) {
        ExpGnodeFile expGfile = new ExpGnodeFile(param);
        List<? extends IWorkRes> wres = Lists.newArrayList();

        expGfile.setClazz(ExpProModelTlxyVo.class);
        Page<ProjectDeclare> curpage = new Page<ProjectDeclare>(request, response);
        curpage.setPageSize(Page.MAX_PAGE_SIZE);
        ProjectDeclare projectDeclare = new ProjectDeclare();
        projectDeclare.setIds(pids);
        projectDeclare.setActywId(actYw.getId());
        wres = proDeclareService.exportData(curpage, projectDeclare);

        expGfile.setClazz(ExpProModelTlxyVo.class);
        if ((expGfile != null) && (expGfile.getParam() == null)) {
            expGfile.setParam(new ExportParams());
        }
        expGfile.getParam().setDataHandler(new DataExpPmTlxyVoHandler(expGfile.getReqParam()));
        IWorkFlow.expRenderFile(request, response, wres, actYw, actYwGnode, expGfile);
        return true;
    }

    /**
     * 大创导出数据和附件.
     *
     * @param request
     * @param response
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject expPdDataByGnode(ItReqParam param, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 查询过滤数据.
         */
        String year = request.getParameter("year");
        year = DateUtil.getYear();
        String officeId = request.getParameter("officeId");
        ActYw actYw = actYwService.get(param.getActywId());
        ActYwGnode actYwGnode = ProjectNodeVo.gnodeFiles();
        param.setGnodeId(actYwGnode.getId());
        List<ProjectDeclare> list = projectDeclareService.findListByQuery(new ProjectDeclare());
        // List<ProModel> list2 = proModelService.findListByQuery(request,
        // actYw, year, officeId, param.getGnodeId());
        List<String> pids = null;
        if (StringUtil.checkNotEmpty(list)) {
            pids = list.stream().map(e -> e.getId()).collect(Collectors.toList());
        } else {
            pids = Lists.newArrayList();
            logger.info("当前角色在当前流程节点没有审核或查询数据");
        }

        /**
         * 生成附件.
         */
        JSONObject js = new JSONObject();
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        if (pids == null || pids.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "没有需要导出的数据");
            logger.info("没有需要导出的数据");
            return js;
        }
        List<Vsftp> res = getFtpRes();
        if (res.size() == 0) {
            js.put(CoreJkey.JK_RET, 0);
            js.put(CoreJkey.JK_MSG, "无FTP链接，请稍后再试");
            logger.info("无FTP链接，请稍后再试");
            return js;
        }
        // 删除存在的该类型的导出数据
        expInfoService.deleteByType(param.getGnodeId());
        ExpInfo ei = new ExpInfo();
        ei.setExpType(param.getGnodeId());
        ei.setTotal(pids.size() + "");
        ei.setSuccess("0");
        ei.setStartDate(new Date());
        ei.setIsComplete("0");
        expInfoService.save(ei);
        // 下载民大申报附件
        // List<VsFile> vsFiles =
        // proSysAttachmentService.getVsFiles(FileTypeEnum.S10, pids, fileSteps,
        // tempPath);
        // 自定义附件
        List<VsFile> vsFiles = proSysAttachmentService.getVsGnodeFilesByImpProjectDeclare(FileTypeEnum.S0, pids, list,
                actYwGnode.getName(), tempPath);
        if (vsFiles == null || vsFiles.size() == 0 || vsFiles.size() < res.size()) {// 文件数小于ftp链接数的直接用一个链接完成
            logger.info("文件数小于ftp链接数的直接用一个线程处理(1)");
            ei.setTotalThread("1");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            // 根据ftp连接数分线程执行
//            for (int i = 1; i < res.size(); i++) {
//                VsftpUtils.returnResource(res.get(i));
//            }
            disAutoThreadPd(request, response, actYw, param.getGnodeId(), res.get(0), tempPath, year, pids, vsFiles,
                    ei.getId(), IdGen.uuid());
        } else {
            logger.info("文件数大于ftp链接数的直接用[" + res.size() + "]线程处理(" + res.size() + ")");
            ei.setTotalThread(res.size() + "");
            CacheUtils.put(CacheUtils.EXP_INFO_CACHE + ei.getId(), ei);// 初始信息
            int cc = vsFiles.size() / res.size();
            // 根据ftp连接数分线程执行
            for (int i = 0; i < res.size(); i++) {
                List<VsFile> tempList = null;
                if (i == res.size() - 1) {// 最后一个链接处理剩余全部文件
                    tempList = vsFiles.subList(i * cc, vsFiles.size());
                } else {
                    tempList = vsFiles.subList(i * cc, ((i + 1) * cc));
                }
                String curkey = IdGen.uuid();
                if (tempList != null && tempList.size() > 0) {
                    logger.info("附件列表存在数据,size=[" + tempList.size() + "],准备启动多线程处理");
                    disAutoThreadPd(request, response, actYw, param.getGnodeId(), res.get(i), tempPath, year, pids,
                            tempList, ei.getId(), curkey);
                } else {
                    logger.info("附件列表为空,处理缓存信息，同时释放Ftp资源！");
                    CacheUtils.put(CacheUtils.EXP_STATUS_CACHE + ei.getId(), curkey, "1");// 该线程执行完毕
//                    VsftpUtils.returnResource(res.get(i));
                }
            }
        }

        js.put(CoreJkey.JK_MSG, "导出成功");
        js.put(CoreJkey.JK_RET, 1);
        return js;
    }

    /**
     * 大创处理导出数据和附件，生成学院附件汇总表.
     *
     * @param request
     * @param response
     * @param actYw
     * @param gnodeId
     * @param tempPath
     * @param year
     * @param pids
     * @param expInfoId
     */
    @Transactional(readOnly = false)
    public void expPdGnodeAfterThread(HttpServletRequest request, HttpServletResponse response, ActYw actYw,
            String gnodeId, String tempPath, String year, List<String> pids, String expInfoId) {
        try {
            logger.error("开始：处理导出数据和附件，生成学院附件汇总表！");
            if ((actYw == null) || StringUtil.isEmpty(actYw.getId())) {
                logger.error("必填参数：yw.id或gnodeId为空！");
                return;
            }

            ActYwGnode gnode = ProjectNodeVo.gnodeFiles();
            logger.info("大创项目" + gnode.getName() + "节点，生成附件失败或为空！");

            String fileName = gnode.getName();
            logger.info("对生成的文件压缩！");
            // 对生成的文件压缩
            File file = createZip(year, fileName, tempPath + File.separator + fileName, tempPath);
            String remotePath = Global.REMOTEPATH+ gnode.getId();
            VsftpUtils.uploadFile(remotePath, expInfoId + StringUtil.DOT + FileUtil.SUFFIX_ZIP, file);
            SysAttachment sa = new SysAttachment();
            sa.setSize(file.length() + "");
            sa.setType(FileTypeEnum.S0);
            sa.setName(fileName + StringUtil.DOT + FileUtil.SUFFIX_ZIP);
            sa.setSuffix(StringUtil.DOT + FileUtil.SUFFIX_ZIP);
            sa.setUid(expInfoId);
            sa.setUrl("/"+ TenantConfig.getCacheTenant()+remotePath + StringUtil.LINE + expInfoId + StringUtil.DOT + FileUtil.SUFFIX_ZIP);
            proSysAttachmentService.save(sa);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("异常：处理导出数据和附件，生成学院附件汇总表！" + e.getMessage());
        } finally {
            ExpInfo ei = expInfoService.get(expInfoId);
            logger.error("完成：ExpInfo.id = " + ei.getId());
            ei.setEndDate(new Date());
            ei.setIsComplete("1");
            ei.setSuccess(ExpInfoService.getSucNum(expInfoId) + "");
            expInfoService.save(ei);
            logger.error("移除缓存，开始。标识：" + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_NUM_CACHE + expInfoId);
            CacheUtils.remove(CacheUtils.EXP_INFO_CACHE + expInfoId);
            CacheUtils.removeAll(CacheUtils.EXP_STATUS_CACHE + expInfoId);
            logger.error("移除缓存，结束。标识：" + expInfoId);
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                logger.info("删除临时目录，开始");
                FileUtil.deleteFileOrDir(f);
                logger.info("删除临时目录，结束");
            }
            logger.info("附件压缩处理结束");
        }
    }

    /**
     * 多线程处理附件: 创建多线程处理附件及目录结构.
     *
     * @param request
     * @param response
     * @param actYw
     * @param gnodeId
     * @param vs
     * @param tempPath
     * @param year
     * @param pids
     * @param vsFiles
     * @param expInfoId
     * @param key
     */
    private void disAutoThreadPd(HttpServletRequest request, HttpServletResponse response, ActYw actYw, String gnodeId,
            Vsftp vs, String tempPath, String year, List<String> pids, List<VsFile> vsFiles, String expInfoId,
            String key) {
        ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
            @Override
            public void run() {
                try {
                    logger.info("创建线程，下载附件,标识：" + expInfoId + "-" + key);
                    VsftpUtils.downFilesPlus(vs, vsFiles, expInfoId, key);
                    logger.info("附件下载完成,正在检查线程是否完成，并处理压缩文件！");
                    if (checkIsComplete(expInfoId)) {
                        logger.info("线程检查结果为：完成,开始处理附件合并生成压缩包操作！");
                        expPdGnodeAfterThread(request, response, actYw, gnodeId, tempPath, year, pids, expInfoId);
                    }
                } catch (Exception e) {
                    logger.info("异常：下载压缩附件不正确！");
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }
        });
    }

    /**************************************************************************************
     * 未使用方法
     **************************************************************************************/
    // 根据gnode导出项目表
    private void expGnodeProject(String year, String rootpath, String filepath, List<Map<String, String>> list) {
        File dir = new File(filepath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(filepath + File.separator + "项目信息.xlsx");
        FileOutputStream fos = null;
        FileInputStream fs = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + "exp_promodelgnode_template.xlsx");
            fs = new FileInputStream(fi);
            XSSFWorkbook wb = new XSSFWorkbook(fs);

            XSSFCellStyle rowStyle = wb.createCellStyle();
            rowStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
            rowStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
            rowStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
            rowStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
            XSSFDataFormat format = wb.createDataFormat();
            rowStyle.setDataFormat(format.getFormat("@"));

            XSSFSheet sheet0 = wb.getSheetAt(0);

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(year + c0.getStringCellValue());

            int row0 = 3;
            for (Map<String, String> map : list) {
                XSSFRow row = sheet0.createRow(row0);
                row0++;
                row.createCell(0).setCellValue(row0 - 3 + "");
                row.createCell(1).setCellValue(map.get("oname"));
                row.createCell(2).setCellValue(map.get("p_number"));
                row.createCell(3).setCellValue(map.get("p_name"));
                row.createCell(4).setCellValue(map.get("pro_category"));
                row.createCell(5).setCellValue(map.get("level"));
                row.createCell(6).setCellValue(map.get("leader_name"));
                row.createCell(7).setCellValue(map.get("no"));
                int nums = 1;
                if (StringUtil.isNotEmpty(map.get("members"))) {
                    nums = nums + map.get("members").split("、").length;
                }
                row.createCell(8).setCellValue(nums + "");
                row.createCell(9).setCellValue(map.get("members"));
                String[] teas = null;
                if (StringUtil.isNotEmpty(map.get("teachers"))) {
                    teas = map.get("teachers").split(",");
                }
                row.createCell(10).setCellValue(teas != null ? teas[0] : "");
                row.createCell(11).setCellValue(teas != null ? teas[1] : "");
                row.createCell(12).setCellValue(teas != null ? teas[2] : "");
                row.createCell(13).setCellValue(map.get("s3l"));
                row.createCell(14).setCellValue(map.get("introduction"));

                // 设置样式
                for (int m = 0; m <= 14; m++) {
                    row.getCell(m).setCellStyle(rowStyle);
                }
            }
            wb.write(fos);
        } catch (Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(ExceptionUtil.getStackTrace(e));
            }
        }

    }

    public void expGnode(HttpServletRequest request, HttpServletResponse response) {
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));

        String year = "";
        String gnodeId = request.getParameter("gnodeId");
        String actywId = request.getParameter("actywId");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        String tempPath = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        File tempPathDir = new File(tempPath + File.separator);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        try {
            ActYw actYw = actYwService.get(actywId);
            String prockey = ActYw.getPkey(actYw);
            Act act = new Act();
            act.setProcDefKey(prockey); // 流程标识
            ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
            List<ActYwGnode> actYwGnodes = proActTaskService.getSubGnodeList(gnodeId, actYw.getGroupId());
            List<String> gnodeIdList = actYwGnodes.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<String> pids = actTaskService.recordIds(act, gnodeIdList, actywId);
            if (pids != null && pids.size() > 0) {
                FileVo fileVo = proSysAttachmentService.downGnodeloads(FileTypeEnum.S11, pids, actYwGnode.getName(),
                        tempPath);// 下载学生申报时上传的文件,并创建目录结构
                if ((fileVo.getStatus()).equals(FileVo.SUCCESS)) {
                    // List<Map<String, String>> data =
                    // impInfoDao.getApprovalData(pids);// 获取需要审核的项目信息
                    List<Map<String, String>> data = impInfoDao.getGnodeData(pids);// 获取需要审核的项目信息
                    if (data != null && data.size() > 0) {
                        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();// 将需要审核的项目信息按学院名称分类，学院名称-数据
                        for (Map<String, String> datam : data) {
                            String offname = datam.get("oname");
                            List<Map<String, String>> olist = map.get(offname);
                            if (olist == null) {
                                olist = new ArrayList<Map<String, String>>();
                                map.put(offname, olist);
                            }
                            olist.add(datam);
                        }
                        for (String key : map.keySet()) {
                            // 按学院名称生成项目审核信息
                            // expApprovalFileByOffice(year, rootpath,
                            // tempPath + File.separator + actYwGnode.getName()
                            // + File.separator + key,
                            // key, map.get(key));
                            expGnodeFileByOffice(year, rootpath,
                                    tempPath + File.separator + actYwGnode.getName() + File.separator + key, key,
                                    map.get(key));
                        }
                    }
                    // List<Map<String, String>> prodata =
                    // impInfoDao.getProjectMdData(pids);
                    List<Map<String, String>> prodata = impInfoDao.getProModelGnodeData(pids);
                    // 导出全部项目信息
                    expProject(year, rootpath, tempPath + File.separator + actYwGnode.getName(), prodata);
                } else {
                    logger.error("下载项目申请报告失败");
                }
            }
            // 对生成的文件压缩，下载
            createZip(year, actYwGnode.getName(), tempPath + File.separator + actYwGnode.getName(), response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            // 删除临时文件目录
            File f = new File(tempPath);
            if (f.exists()) {
                FileUtil.deleteFileOrDir(f);
            }
        }
    }

}