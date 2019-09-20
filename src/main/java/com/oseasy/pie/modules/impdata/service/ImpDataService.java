package com.oseasy.pie.modules.impdata.service;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.act.modules.actyw.service.ActYwGtimeService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.fileserver.common.vsftp.VsftpUtils;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.common.utils.poi.MergedResult;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.com.pcore.modules.sys.utils.RegexUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.iep.tool.check.ItCkGgjLevel;
import com.oseasy.pie.modules.iep.tool.check.ItCkGgjProName;
import com.oseasy.pie.modules.iep.tool.check.ItCkGgjtype;
import com.oseasy.pie.modules.iep.tool.check.ItCparamGgj;
import com.oseasy.pie.modules.impdata.dao.ImpInfoDao;
import com.oseasy.pie.modules.impdata.entity.BackUserError;
import com.oseasy.pie.modules.impdata.entity.GcontestError;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.OfficeError;
import com.oseasy.pie.modules.impdata.entity.PmgMemsError;
import com.oseasy.pie.modules.impdata.entity.PmgTeasError;
import com.oseasy.pie.modules.impdata.entity.ProMdApprovalError;
import com.oseasy.pie.modules.impdata.entity.ProMdCloseError;
import com.oseasy.pie.modules.impdata.entity.ProMdMidError;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGJError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.entity.ProjectError;
import com.oseasy.pie.modules.impdata.entity.ProjectHsError;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.entity.TeacherError;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.tool.IitCheckService;
import com.oseasy.pie.modules.impdata.tool.check.ItCkArea;
import com.oseasy.pie.modules.impdata.tool.check.ItCkCUenterdate;
import com.oseasy.pie.modules.impdata.tool.check.ItCkCUgraduation;
import com.oseasy.pie.modules.impdata.tool.check.ItCkOfficeName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkOfficeRemark;
import com.oseasy.pie.modules.impdata.tool.check.ItCkOfficeUcode;
import com.oseasy.pie.modules.impdata.tool.check.ItCkOfficeXy;
import com.oseasy.pie.modules.impdata.tool.check.ItCkOfficeZy;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectFinanceGrant;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectIntroduction;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectLeaderName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectLeaderNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectNumber;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectTeacherName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectTeamMembers;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectTotalGrant;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectType;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectUniversityGrant;
import com.oseasy.pie.modules.impdata.tool.check.ItCkPDProjectYear;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUaddress;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUaward;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUbirthday;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUcontestExperience;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUcountry;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUcurrState;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUdegree;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUdomain;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUeducation;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUemail;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUenterdate;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUgraduation;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUidNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUidType;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUinstudy;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUloginName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUmobile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUname;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUnational;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUno;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUoffice;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUpolitical;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUprofessional;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUprojectExperience;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUremarks;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUsex;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUtclass;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUtemporaryDate;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamPm;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamUser;
import com.oseasy.pie.modules.impdata.tool.param.ItRpOffice;
import com.oseasy.pie.modules.impdata.vo.GgjError;
import com.oseasy.pro.modules.project.service.ProjectDeclareService;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProActTaskService;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.GgjBusInfo;
import com.oseasy.pro.modules.promodel.vo.GgjStudent;
import com.oseasy.pro.modules.promodel.vo.GgjTeacher;
import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.common.config.SysSval;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.EuserType;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import com.oseasy.util.common.utils.rsa.MD5Util;

import net.sf.json.JSONObject;

/**
 * 导入数据信息表Service
 *
 * @author 9527
 * @version 2017-05-16
 */
@Service
public class ImpDataService extends CrudService<ImpInfoDao, ImpInfo> implements IitCheckService{
    public static final String YRAR = "yyyy";// 学生信息导入
    public static final String impProjectHs = "9";// 大创项目到中期检查节点的信息导入
    public static final String tempProModelFilePath = Global.REMOTEPATH + "/temp/tempProModelGcontestFiles/";
    public static final String proModelFilePath = Global.REMOTEPATH + "/proModelGcontestFiles/";
    @Autowired
    private ProModelErrorService proModelErrorService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private ProMdCloseErrorService proMdCloseErrorService;
    @Autowired
    private ProMdMidErrorService proMdMidErrorService;
    @Autowired
    private ProMdApprovalErrorService proMdApprovalErrorService;
    @Autowired
    private StudentErrorService studentErrorService;
    @Autowired
    private ConStuErrorService conStuErrorService;
    @Autowired
    private TeacherErrorService teacherErrorService;
    @Autowired
    private BackUserErrorService backUserErrorService;
    @Autowired
    private OfficeErrorService officeErrorService;
    @Autowired
    private ImpInfoService impInfoService;
    @Autowired
    private ProjectDeclareService projectDeclareService;
    @Autowired
    private ProjectErrorService projectErrorService;
    @Autowired
    private GcontestErrorService gcontestErrorService;
    @Autowired
    private ProjectHsErrorService projectHsErrorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProModelDao proModelDao;
    @Autowired
    private ProModelService proModelService;
    @Autowired
    private BackTeacherExpansionDao backTeacherExpansionDao;
    @Autowired
    private ImpInfoErrmsgService impInfoErrmsgService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    private ActYwGtimeService actYwGtimeService;
    @Autowired
    private ProModelGcontestErrorService proModelGcontestErrorService;
    @Autowired
    private CoreService coreService;

    public static int descHeadRow=3;//Excel文件说明部分行数

    public final static Logger logger = Logger.getLogger(ImpDataService.class);

    /*************************************************************************
     * 导入模板功能数据库操作.
     *************************************************************************/
    public Page<Map<String, String>> getList(Page<Map<String, String>> page, Map<String, Object> param) {
        if (page.getPageNo() <= 0) {
            page.setPageNo(1);
        }
        int count = dao.getListCount(param);
        param.put("offset", (page.getPageNo() - 1) * page.getPageSize());
        param.put("pageSize", page.getPageSize());
        List<Map<String, String>> list = null;
        if (count > 0) {
            list = dao.getList(param);
        }
        page.setCount(count);
        page.setList(list);
        page.initialize();
        return page;
    }

    public ImpInfo get(String id) {
        return super.get(id);
    }

    public List<ImpInfo> findList(ImpInfo impInfo) {
        return super.findList(impInfo);
    }

    public Page<ImpInfo> findPage(Page<ImpInfo> page, ImpInfo impInfo) {
        return super.findPage(page, impInfo);
    }

    @Transactional(readOnly = false)
    public void save(ImpInfo impInfo) {
        super.save(impInfo);
    }

    @Transactional(readOnly = false)
    public void delete(ImpInfo impInfo) {
        super.delete(impInfo);
    }

    /*************************************************************************
     * 处理导入模板功能.
     *************************************************************************/
    private void checkTemplate(XSSFSheet datasheet, HttpServletRequest request) throws ImpDataException,Exception {
        String rootpath = request.getSession().getServletContext().getRealPath(StringUtil.LINE);
        String sheetname = datasheet.getSheetName();
        String[] ss = sheetname.split("-");
        if (ss.length != 2) {
            throw new ImpDataException("模板错误,请下载最新的模板");
        }
        FileInputStream fs = null;
        try {
            String filename = "";
            if ("学生".equals(ss[0])) {
                filename = ExpType.Stu.getTplname();
            } else if ("全国学生".equals(ss[0])) {
                filename = ExpType.ConStu.getTplname();
            } else if ("导师".equals(ss[0])) {
                filename = ExpType.Tea.getTplname();
            } else if ("后台用户".equals(ss[0])) {
                filename = ExpType.BackUser.getTplname();
            } else if ("机构".equals(ss[0])) {
                filename = ExpType.Org.getTplname();
            } else if ("国创项目".equals(ss[0])) {
                filename = ExpType.DcProjectClose.getTplname();
            } else if ("项目信息".equals(ss[0])) {
                filename = ExpType.PmProjectHsmid.getTplname();
            } else if ("互联网+大赛信息".equals(ss[0])) {
                filename = ExpType.HlwGcontest.getTplname();
            } else {
                throw new ImpDataException("模板错误,请下载最新的模板");
            }
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + filename);
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().split("-")[1].equals(ss[1])) {
                throw new ImpDataException("模板错误,请下载最新的模板");
            }
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet)
                        .equals(ExcelUtils.getStringByCell(datasheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    throw new ImpDataException("模板错误,请下载最新的模板");
                }
            }
        } catch (Exception e) {
            throw new ImpDataException("模板错误,请下载最新的模板");
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void checkMdTemplate(XSSFWorkbook swb, HttpServletRequest request, ItOper impVo)
            throws ImpDataException, IOException {
        String rootpath = request.getSession().getServletContext().getRealPath(StringUtil.LINE);
        if (StringUtil.isNotEmpty(impVo.getReqParam().getType())) {
            if ((ExpType.MdExpapproval.getIdx()).equals(impVo.getReqParam().getType())) {
                FileInputStream fs = null;
                try {
                    File fi = null;
                    if((impVo == null) || impVo.getReqParam().getIsMdOld()){
                        fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpapproval.getTplname());
                    }else{
                        fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpapproval.getTplPath());
                    }
                    fs = new FileInputStream(fi);
                    // 读取了模板内所有sheet内容
                    XSSFWorkbook wb = new XSSFWorkbook(fs);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFSheet datasheet = swb.getSheetAt(0);
                    XSSFSheet sheet1 = wb.getSheetAt(1);
                    XSSFSheet datasheet1 = swb.getSheetAt(1);
                    XSSFCell sheetXSSFCell = null;
                    XSSFCell datasheetXSSFCell = null;
                    try {
                        for (int j = 0; j < sheet.getRow(ImpExpService.approval_sheet0_head).getLastCellNum(); j++) {
                            sheetXSSFCell = sheet.getRow(ImpExpService.approval_sheet0_head).getCell(j);
                            datasheetXSSFCell = datasheet.getRow(2).getCell(j);
                            if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                                continue;
                            }
                            if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet))) {
                                throw new ImpDataException("请选择正确的文件");
                            }
                        }
                    } catch (Exception e) {
                        throw new ImpDataException("请选择正确的文件"+ e.getMessage());
                    }
                    for (int j = 0; j < sheet1.getRow(ImpExpService.approval_sheet1_head).getLastCellNum(); j++) {
                        sheetXSSFCell = sheet1.getRow(ImpExpService.approval_sheet1_head).getCell(j);
                        datasheetXSSFCell = datasheet1.getRow(2).getCell(j);
                        if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                            continue;
                        }
                        if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet1).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet1))) {
                            throw new ImpDataException("请选择正确的文件");
                        }
                    }
                } catch (Exception e) {
                    throw new ImpDataException("请选择正确的文件");
                } finally {
                    try {
                        if (fs != null)
                            fs.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            } else if ((ExpType.MdExpmid.getIdx()).equals(impVo.getReqParam().getType())) {
                FileInputStream fs = null;
                try {
                    File fi = null;
                    if(impVo.getReqParam().getIsMdOld()){
                        fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpmid.getTplname());
                    }else{
                        fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpmid.getTplPath());
                    }
                    fs = new FileInputStream(fi);
                    // 读取了模板内所有sheet内容
                    XSSFWorkbook wb = new XSSFWorkbook(fs);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFSheet datasheet = swb.getSheetAt(0);
                    XSSFCell sheetXSSFCell = null;
                    XSSFCell datasheetXSSFCell = null;
                    for (int j = 0; j < sheet.getRow(ImpExpService.mid_sheet0_head).getLastCellNum(); j++) {
                        sheetXSSFCell = sheet.getRow(ImpExpService.mid_sheet0_head).getCell(j);
                        datasheetXSSFCell = datasheet.getRow(2).getCell(j);
                        if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                            continue;
                        }

                        if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet))) {
                            throw new ImpDataException("请选择正确的文件");
                        }
                    }
                } catch (Exception e) {
                    throw new ImpDataException("请选择正确的文件");
                } finally {
                    try {
                        if (fs != null)
                            fs.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            } else if ((ExpType.MdExpclose.getIdx()).equals(impVo.getReqParam().getType())) {
                FileInputStream fs = null;
                try {
                    File fi = null;
                    if(impVo.getReqParam().getIsMdOld()){
                        fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + ExpType.MdExpclose.getTplname());
                    }else{
                        fi = new File(rootpath + ExpType.TPL_ROOT_EXCEL + ExpType.MdExpclose.getTplPath());
                    }

                    fs = new FileInputStream(fi);
                    // 读取了模板内所有sheet内容
                    XSSFWorkbook wb = new XSSFWorkbook(fs);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFSheet datasheet = swb.getSheetAt(0);
                    XSSFCell sheetXSSFCell = null;
                    XSSFCell datasheetXSSFCell = null;
                    for (int j = 0; j < sheet.getRow(ImpExpService.close_sheet0_head).getLastCellNum(); j++) {
                        sheetXSSFCell = sheet.getRow(ImpExpService.close_sheet0_head).getCell(j);
                        datasheetXSSFCell = datasheet.getRow(2).getCell(j);
                        if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                            continue;
                        }

                        if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet))) {
                            throw new ImpDataException("请选择正确的文件");
                        }
                    }
                } catch (Exception e) {
                    throw new ImpDataException("请选择正确的文件");
                } finally {
                    try {
                        if (fs != null)
                            fs.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }else{
            if(StringUtil.isNotEmpty(impVo.getActywId()) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId())){
                ActYwGtime gtime = actYwGtimeService.getTimeByYnodeId(impVo.getActywId(), impVo.getReqParam().getGnodeId());
                if((gtime == null) || (!gtime.getHasTpl()) || StringUtil.isEmpty(gtime.getExcelTplPath())){
                    logger.error("请配置Excel导出模板！");
                    return;
                }

                FileInputStream fs = null;
                try {
                    File fi = new File(rootpath + File.separator + gtime.getExcelTplPath());
                    fs = new FileInputStream(fi);
                    // 读取了模板内所有sheet内容
                    XSSFWorkbook wb = new XSSFWorkbook(fs);
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFSheet datasheet = swb.getSheetAt(0);
                    XSSFCell sheetXSSFCell = null;
                    XSSFCell datasheetXSSFCell = null;
                    try {
                        for (int j = 0; j < sheet.getRow(ImpExpService.approval_sheet0_head).getLastCellNum(); j++) {
                            sheetXSSFCell = sheet.getRow(ImpExpService.approval_sheet0_head).getCell(j);
                            datasheetXSSFCell = datasheet.getRow(2).getCell(j);
                            if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                                continue;
                            }

                            if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet))) {
                                throw new ImpDataException("请选择正确的文件");
                            }
                        }
                    } catch (Exception e) {
                        throw new ImpDataException("请选择正确的文件");
                    }
                    XSSFSheet sheet1 = wb.getSheetAt(1);
                    XSSFSheet datasheet1 = swb.getSheetAt(1);
                    for (int j = 0; j < sheet1.getRow(ImpExpService.approval_sheet1_head).getLastCellNum(); j++) {
                        sheetXSSFCell = sheet1.getRow(ImpExpService.approval_sheet1_head).getCell(j);
                        datasheetXSSFCell = datasheet1.getRow(2).getCell(j);
                        if((sheetXSSFCell == null) && (datasheetXSSFCell == null)){
                            continue;
                        }

                        if (!ExcelUtils.getStringByCell(sheetXSSFCell, sheet1).equals(ExcelUtils.getStringByCell(datasheetXSSFCell, sheet1))) {
                            throw new ImpDataException("请选择正确的文件");
                        }
                    }
                } catch (Exception e) {
                    throw new ImpDataException("请选择正确的文件");
                } finally {
                    try {
                        if (fs != null)
                            fs.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
    }

    private void checkProModelTemplate(XSSFSheet datasheet, HttpServletRequest request) throws Exception {
        String rootpath = request.getSession().getServletContext().getRealPath(StringUtil.LINE);
        String sheetname = datasheet.getSheetName();
        FileInputStream fs = null;
        try {
            String filename = "promodel_data_template.xlsx";
            File fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + filename);
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equals(sheetname)) {
                throw new ImpDataException("模板错误,请下载最新的模板");
            }
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet)
                        .equals(ExcelUtils.getStringByCell(datasheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    throw new ImpDataException("模板错误,请下载最新的模板");
                }
            }
        } catch (Exception e) {
            throw new ImpDataException("模板错误,请下载最新的模板");
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void checkProModelGcontestTemplate(ItOper impVo, XSSFSheet datasheet, HttpServletRequest request) throws Exception {
        String rootpath = request.getSession().getServletContext().getRealPath(StringUtil.LINE);
        String sheetname = datasheet.getSheetName();
        FileInputStream fs = null;
        try {
            File fi = null;
            String type = impVo.getReqParam().getTplType();
            String filename = null;
            // excel模板路径
            // excel模板路径
            filename = "promodel_gcontest_data_template.xlsx";
            fi = new File(rootpath + ExpType.TPL_ROOT_STATICEXCELTEMPLATE + filename);
            fs = new FileInputStream(fi);
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            if (!sheet.getSheetName().equals(sheetname)) {
                throw new ImpDataException("模板错误,请下载最新的模板");
            }
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet)
                        .equals(ExcelUtils.getStringByCell(datasheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    throw new ImpDataException("模板错误,请下载最新的模板");
                }
            }
        } catch (Exception e) {
            throw new ImpDataException("模板错误,请下载最新的模板");
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 导入民大项目.
     * @param type
     * @param imgFiles
     * @param request
     */
//    public void importMdData(String actywId, String gnodeId, String type, List<MultipartFile> imgFiles, HttpServletRequest request) {
    public void importMdData(List<MultipartFile> imgFiles, HttpServletRequest request, ItOper impVo) {
        for (MultipartFile imgFile : imgFiles) {
            importMdData(imgFile, request, impVo);
        }
    }

    /**
     * 导入自定义项目.
     * @param ay
     * @param imgFiles
     * @param request
     */
    public void importFlowData(ActYw ay, List<MultipartFile> imgFiles, HttpServletRequest request, ItOper impVo) throws Exception  {
        for (MultipartFile imgFile : imgFiles) {
            //先处理项目附件
            if(impVo.getIsImpFileData()){
                uploadZip(ay, imgFile);
            }
            //处理项目信息
            if(impVo.getIsImpFirstData() || impVo.getIsImpHisData()){
                if((FlowProjectType.PMT_DASAI).equals(ay.getFptype())){
                    importProModelGcontestData(ay, imgFile, request, impVo);
                }else if((FlowProjectType.PMT_XM).equals(ay.getFptype())){
                    importProModelData(ay, imgFile, request, impVo);
                }else{
                    importProModelData(ay, imgFile, request, impVo);
                }
            }
        }
    }

    /**
     * 处理大赛附件数据和附件存储.
     * @param ay
     * @param imgFile
     * @param request
     */
    public void importProModelGcontestData(ActYw ay, MultipartFile imgFile, HttpServletRequest request, ItOper impVo) {
        if(!FileUtil.checkFileType(imgFile, FileUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX)){
            return;
        }

        InputStream is = null;
        ImpInfo ii = ImpInfo.genImpInfo(ay, imgFile.getOriginalFilename(), ExpType.PmGcontest.getIdx());
        impInfoService.save(ii);// 插入导入信息
        try {
            is = imgFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
            checkProModelGcontestTemplate(impVo, sheet, request);// 检查模板版本
            ii.setTotal((sheet.getLastRowNum() - ImpDataService.descHeadRow) + "");
            impInfoService.save(ii);// 插入导入信息
            ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        importProModelGcontest(sheet, ii, ay, impVo);
                    } catch (Exception e) {
                        ii.setIsComplete(Const.YES);
                        impInfoService.save(ii);
                        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                        logger.error(ay.getProProject().getProjectName() + "信息导入出错", e);
                    }
                }
            });
        } catch (POIXMLException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("请选择正确的文件");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (ImpDataException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg(e.getMessage());
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (Exception e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("导入出错");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void importProModelData(ActYw ay, MultipartFile imgFile, HttpServletRequest request, ItOper impVo) {
        if(!FileUtil.checkFileType(imgFile, FileUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX)){
            return;
        }

        InputStream is = null;
        ImpInfo ii = ImpInfo.genImpInfo(ay, imgFile.getOriginalFilename(), ExpType.PmProject.getIdx());
        impInfoService.save(ii);// 插入导入信息
        try {
            is = imgFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
            checkProModelTemplate(sheet, request);// 检查模板版本
            ii.setTotal((sheet.getLastRowNum() - ImpDataService.descHeadRow) + "");
            impInfoService.save(ii);// 插入导入信息
            ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        importProModel(sheet, ii, ay, impVo);
                    } catch (Exception e) {
                        ii.setIsComplete(Const.YES);
                        impInfoService.save(ii);
                        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                        logger.error(ay.getProProject().getProjectName() + "信息导入出错", e);
                    }
                }
            });
        } catch (POIXMLException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("请选择正确的文件");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (ImpDataException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg(e.getMessage());
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (Exception e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("导入出错");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void importMdData(MultipartFile imgFile, HttpServletRequest request, ItOper impVo) {
        InputStream is = null;
        ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), impVo.getReqParam().getType());
        ii.setActywid(impVo.getActywId());
        impInfoService.save(ii);// 插入导入信息
        try {
            is = imgFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(is);
            if (StringUtil.isNotEmpty(impVo.getReqParam().getType())) {// 民大立项
                impVo.getReqParam().setIsMdOld(true);
                checkMdTemplate(wb, request, impVo);// 检查模板版本
                if ((ExpType.MdExpapproval.getIdx()).equals(impVo.getReqParam().getType())) {// 民大立项
                    XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
                    XSSFSheet sheet2 = wb.getSheetAt(1); // 获取第一个sheet表
                    JSONObject js = new JSONObject();
                    int t0 = ExcelUtils.getTotalRow(sheet, "联系人签字：");
                    int t1 = ExcelUtils.getTotalRow(sheet2, "联系人签字：");
                    js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                    js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                    js.put("title1", sheet2.getRow(0).getCell(0).getStringCellValue());
                    js.put("tel1", sheet2.getRow(t1).getCell(0).getStringCellValue());
                    js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                    ii.setTotal((t0 + t1 - 8) + "");
                    ii.setMsg(js.toString());
                    impInfoService.save(ii);// 插入导入信息
                    ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                        @Override
                        public void run() {
                            try {
                                importMdApproval(sheet, sheet2, ii, impVo);
                            } catch (Exception e) {
                                ii.setIsComplete(Const.YES);
                                impInfoService.save(ii);
                                CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                                logger.error(FileStepEnum.S2000.getName() + "信息导入出错", e);
                            }
                        }
                    });
                } else if ((ExpType.MdExpmid.getIdx()).equals(impVo.getReqParam().getType())) {// 民大中期
                    XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
                    JSONObject js = new JSONObject();
                    int t0 = ExcelUtils.getTotalRow(sheet, "经手人：");
                    js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                    js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                    js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                    ii.setTotal((t0 - 4) + "");
                    ii.setMsg(js.toString());
                    impInfoService.save(ii);// 插入导入信息
                    ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                        @Override
                        public void run() {
                            try {
                                importMdMid(sheet, ii, impVo);
                            } catch (Exception e) {
                                ii.setIsComplete(Const.YES);
                                impInfoService.save(ii);
                                CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                                logger.error(FileStepEnum.S2200.getName() + "信息导入出错", e);
                            }
                        }
                    });
                } else if ((ExpType.MdExpclose.getIdx()).equals(impVo.getReqParam().getType())) {// 民大结项
                    XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
                    JSONObject js = new JSONObject();
                    int t0 = ExcelUtils.getTotalRow(sheet, "经手人：");
                    js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                    js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                    js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                    ii.setTotal((t0 - 4) + "");
                    ii.setMsg(js.toString());
                    impInfoService.save(ii);// 插入导入信息
                    ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                        @Override
                        public void run() {
                            try {
                                importMdClose(sheet, ii, impVo);
                            } catch (Exception e) {
                                ii.setIsComplete(Const.YES);
                                impInfoService.save(ii);
                                CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                                logger.error(FileStepEnum.S2300.getName() + "信息导入出错", e);
                            }
                        }
                    });
                }
            }else{
                impVo.getReqParam().setIsMdOld(false);
                checkMdTemplate(wb, request, impVo);// 检查模板版本
                if(StringUtil.isNotEmpty(impVo.getActywId()) && StringUtil.isNotEmpty(impVo.getReqParam().getGnodeId())){
                    ActYwGtime gtime = actYwGtimeService.getTimeByYnodeId(impVo.getActywId(), impVo.getReqParam().getGnodeId());

                    if((gtime == null) || (!gtime.getHasTpl()) || StringUtil.isEmpty(gtime.getExcelTplPath())){
                        logger.error("请配置Excel导出模板！");
                        return;
                    }

                    String excelTplPath = gtime.getExcelTplPath();
                    XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
                    XSSFSheet sheet2 = wb.getSheetAt(1); // 获取第一个sheet表
                    JSONObject js = new JSONObject();
                    if((excelTplPath).contains(ExpType.MdExpapproval.getKey())){
                        int t0 = ExcelUtils.getTotalRow(sheet, "联系人签字：");
                        int t1 = ExcelUtils.getTotalRow(sheet2, "联系人签字：");
                        js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                        js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                        js.put("title1", sheet2.getRow(0).getCell(0).getStringCellValue());
                        js.put("tel1", sheet2.getRow(t1).getCell(0).getStringCellValue());
                        js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                        ii.setTotal((t0 + t1 - 8) + "");
                        //importMdApproval(sheet, sheet2, ii, impVo);
                    }else if ((excelTplPath).contains(ExpType.MdExpmid.getKey())) {// 民大中期
                        int t0 = ExcelUtils.getTotalRow(sheet, "经手人：");
                        js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                        js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                        js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                        ii.setTotal((t0 - 4) + "");
                        //importMdMid(sheet, ii, impVo);
                    }else if ((excelTplPath).contains(ExpType.MdExpclose.getKey())) {// 民大结项
                        int t0 = ExcelUtils.getTotalRow(sheet, "经手人：");
                        js.put("title0", sheet.getRow(0).getCell(0).getStringCellValue());
                        js.put("tel0", sheet.getRow(t0).getCell(0).getStringCellValue());
                        js.put("oname", sheet.getRow(1).getCell(0).getStringCellValue());
                        ii.setTotal((t0 - 4) + "");
                        //importMdClose(sheet, ii, impVo);
                    }
                    ii.setMsg(js.toString());
                    impInfoService.save(ii);// 插入导入信息

                    ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                        @Override
                        public void run() {
                            try {
                                if((excelTplPath).contains(ExpType.MdExpapproval.getKey())){
                                    importMdApproval(sheet, sheet2, ii, impVo);
                                }else if ((excelTplPath).contains(ExpType.MdExpmid.getKey())) {// 民大中期
                                    importMdMid(sheet, ii, impVo);
                                }else if ((excelTplPath).contains(ExpType.MdExpclose.getKey())) {// 民大结项
                                    importMdClose(sheet, ii, impVo);
                                }
                            } catch (Exception e) {
                                ii.setIsComplete(Const.YES);
                                impInfoService.save(ii);
                                CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                                logger.error(FileStepEnum.S2300.getName() + "信息导入出错", e);
                            }
                        }
                    });
                }
            }
        } catch (POIXMLException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("请选择正确的文件");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (ImpDataException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg(e.getMessage());
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } catch (Exception e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("导入出错");
            impInfoService.save(ii);
            logger.error("导入出错", e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void importProModelGcontest(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) throws Exception {
        Office office = null;
        Office profes = null;
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        int megRows = 1;//合并的行数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 2; i <= sheet.getLastRowNum(); ) {
            MergedResult mr = ExcelUtils.isMergedRegion(sheet, i, 0);//判断是否合并行
            if (mr.isMerged()) {
                megRows = mr.getEndRow() - mr.getStartRow() + 1;
            } else {
                megRows = 1;
            }
            int tag = 0;// 有几个错误字段
            ProModelGcontestError phe = new ProModelGcontestError();
            ProModelGcontestError validinfo = new ProModelGcontestError();// 用于保存处理之后的信息，以免再次查找数据库.
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
            /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                i = i + megRows;
                continue;
            }
            /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */

            //处理合并行数据
            for (int j = 0; j < 16; j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if ("项目名称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setName(val);
                    validinfo.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setName(null);
                    } else if (proModelService.existProName(ay, val)) {
                        tag++;
                        iie.setErrmsg("项目名称已存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                        validinfo.setValidName(false);
                    } else {
                        validinfo.setValidName(true);
                    }
                } else if ("项目年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setYear(val);
                    validinfo.setYear(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (!checkYear(val)) {
                        tag++;
                        iie.setErrmsg("项目年份格式不正确");
                    } else if (val.length() > 4) {
                        tag++;
                        iie.setErrmsg("最多4个字符");
                        phe.setYear(null);
                    }

                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目阶段".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setStage(val);
                    validinfo.setStage(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 50) {
                            tag++;
                            iie.setErrmsg("最多50个字符");
                            phe.setStage(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("参赛类别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setType(null);
                    } else if ((d = DictUtils.getDictByLabel("competition_net_type", val)) == null) {
                        tag++;
                        iie.setErrmsg("参赛类别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setType(d.getValue());
                    }
                } else if ("参赛组别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setGroups(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setGroups(null);
                    } else if ((d = DictUtils.getDictByLabel("gcontest_level", val)) == null) {
                        tag++;
                        iie.setErrmsg("参赛组别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setGroups(d.getValue());
                    }
                } else if ("项目简介".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setIntroduction(val);
                    validinfo.setIntroduction(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 5000) {
                            tag++;
                            iie.setErrmsg("最多5000个字符(2500个汉字)");
                            phe.setIntroduction(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("是否有附件".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setHasfile(val);
                    validinfo.setHasfile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 1) {
                        tag++;
                        iie.setErrmsg("最多1个字符");
                        phe.setHasfile(null);
                    } else if (!Const.YES_ZH.equals(val) && !Const.NO_ZH.equals(val)) {
                        tag++;
                        iie.setErrmsg("填写错误");
                    } else if (!checkValidFile(validinfo, ay)) {
                        tag++;
                        iie.setErrmsg("未找到上传的附件");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setLeader(val);
                    validinfo.setLeader(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setLeader(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("学号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setNo(val);
                    validinfo.setNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setNo(null);
                    } else {
                        User u = userService.getByNo(val);
                        if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                            tag++;
                            iie.setErrmsg("找到该学号人员，但不是学生");
                        } else if (u != null && phe.getLeader() != null && !phe.getLeader().equals(u.getName())) {
                            tag++;
                            iie.setErrmsg("负责人学号和姓名不一致");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("专业".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    profes = null;
                    phe.setProfes(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((profes = OfficeUtils.getProfessionalByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("专业不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (profes != null) {
                            validinfo.setProfes(profes.getId());
                        }
                    }
                } else if ("入学年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setEnter(val);
                    validinfo.setEnter(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!checkYear(val)) {
                            tag++;
                            iie.setErrmsg("入学年份格式不正确");
                        } else if (val.length() > 4) {
                            tag++;
                            iie.setErrmsg("最多4个字符");
                            phe.setEnter(null);
                        }
                    }

                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("毕业年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setOut(val);
                    validinfo.setOut(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!checkYear(val)) {
                            tag++;
                            iie.setErrmsg("毕业年份格式不正确");
                        } else if (val.length() > 4) {
                            tag++;
                            iie.setErrmsg("最多4个字符");
                            phe.setOut(null);
                        } else if (!checkOutYear(phe.getEnter(), phe.getOut())) {
                            tag++;
                            iie.setErrmsg("毕业年份需大于入学年份");
                        }
                    }

                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("学历".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setXueli(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if ((d = DictUtils.getDictByLabel("enducation_level", val)) == null) {
                            tag++;
                            iie.setErrmsg("学历不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null) {
                            validinfo.setXueli(d.getValue());
                        }
                    }
                } else if ("身份证号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setIdnum(val);
                    validinfo.setIdnum(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.idNumRegex, val)) {
                            tag++;
                            iie.setErrmsg("身份证号码格式不正确");
                        } else if (val.length() > 32) {
                            tag++;
                            iie.setErrmsg("最多32个字符");
                            phe.setIdnum(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("手机号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setMobile(val);
                    validinfo.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号码格式不正确");
                        } else {
                            User u = new User();
                            u.setMobile(val);
                            User temu = userService.getByMobile(u);
                            if (temu != null && phe.getNo() != null && !phe.getNo().equals(temu.getNo())) {
                                tag++;
                                iie.setErrmsg("手机号码已存在");
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                    phe.setEmail(val);
                    validinfo.setEmail(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                            tag++;
                            iie.setErrmsg("邮箱格式不正确");
                        } else if (val.length() > 128) {
                            tag++;
                            iie.setErrmsg("最多128个字符");
                            phe.setEmail(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                }

            }
            //处理合并行数据end

            //处理组成员数据
            for (int k = i; k < i + megRows; k++) {
                PmgMemsError pme = new PmgMemsError();
                PmgMemsError vpme = new PmgMemsError();// 用于保存处理之后的信息，以免再次查找数据库.
                pme.setSort(k + "");
                pme.setPmgeId(phe.getId());
                pme.setImpId(ii.getId());
                pme.setId(IdGen.uuid());
                rowData = sheet.getRow(k);
				/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
                int svalidcell = 0;
                for (int j = 16; j <= 24; j++) {
                    if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                        svalidcell++;
                        break;
                    }
                }
                if (svalidcell == 0) {
                    continue;
                }
                for (int j = 16; j <= 24; j++) {
                    String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                    if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setName(val);
                        vpme.setName(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (StringUtil.isEmpty(val)) {
                            tag++;
                            iie.setErrmsg("必填信息");
                        } else if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            pme.setName(null);
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("学号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setNo(val);
                        vpme.setNo(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (StringUtil.isEmpty(val)) {
                            tag++;
                            iie.setErrmsg("必填信息");
                        } else if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            pme.setNo(null);
                        } else {
                            User u = userService.getByNo(val);
                            if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                                tag++;
                                iie.setErrmsg("找到该学号人员，但不是学生");
                            } else if (u != null && pme.getName() != null && !pme.getName().equals(u.getName())) {
                                tag++;
                                iie.setErrmsg("学号和姓名不一致");
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("专业".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        profes = null;
                        pme.setProfes(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (StringUtil.isEmpty(val)) {
                            tag++;
                            iie.setErrmsg("必填信息");
                        } else if ((profes = OfficeUtils.getProfessionalByName(val)) == null) {
                            tag++;
                            iie.setErrmsg("专业不存在");
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        } else {
                            if (profes != null) {
                                vpme.setProfes(profes.getId());
                            }
                        }
                    } else if ("入学年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setEnter(val);
                        vpme.setEnter(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!checkYear(val)) {
                                tag++;
                                iie.setErrmsg("入学年份格式不正确");
                            } else if (val.length() > 4) {
                                tag++;
                                iie.setErrmsg("最多4个字符");
                                pme.setEnter(null);
                            }
                        }

                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("毕业年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setOut(val);
                        vpme.setOut(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!checkYear(val)) {
                                tag++;
                                iie.setErrmsg("毕业年份格式不正确");
                            } else if (val.length() > 4) {
                                tag++;
                                iie.setErrmsg("最多4个字符");
                                pme.setOut(null);
                            } else if (!checkOutYear(phe.getEnter(), phe.getOut())) {
                                tag++;
                                iie.setErrmsg("毕业年份需大于入学年份");
                            }
                        }

                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("学历".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        Dict d = null;
                        pme.setXueli(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if ((d = DictUtils.getDictByLabel("enducation_level", val)) == null) {
                                tag++;
                                iie.setErrmsg("学历不存在");
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        } else {
                            if (d != null) {
                                vpme.setXueli(d.getValue());
                            }
                        }
                    } else if ("身份证号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setIdnum(val);
                        vpme.setIdnum(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!Pattern.matches(RegexUtils.idNumRegex, val)) {
                                tag++;
                                iie.setErrmsg("身份证号码格式不正确");
                            } else if (val.length() > 32) {
                                tag++;
                                iie.setErrmsg("最多32个字符");
                                pme.setIdnum(null);
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("手机号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setMobile(val);
                        vpme.setMobile(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                                tag++;
                                iie.setErrmsg("手机号码格式不正确");
                            } else {
                                User u = new User();
                                u.setMobile(val);
                                User temu = userService.getByMobile(u);
                                if (temu != null && pme.getNo() != null && !pme.getNo().equals(temu.getNo())) {
                                    tag++;
                                    iie.setErrmsg("手机号码已存在");
                                }
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setEmail(val);
                        vpme.setEmail(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                                tag++;
                                iie.setErrmsg("邮箱格式不正确");
                            } else if (val.length() > 128) {
                                tag++;
                                iie.setErrmsg("最多128个字符");
                                pme.setEmail(null);
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    }
                }
                phe.getPmes().add(pme);
                validinfo.getPmes().add(vpme);
            }
            //处理组成员数据end

            //处理导师数据
            for (int k = i; k < i + megRows; k++) {
                PmgTeasError pme = new PmgTeasError();
                PmgTeasError vpme = new PmgTeasError();// 用于保存处理之后的信息，以免再次查找数据库.
                pme.setSort(k + "");
                pme.setPmgeId(phe.getId());
                pme.setImpId(ii.getId());
                pme.setId(IdGen.uuid());
                rowData = sheet.getRow(k);
				/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
                int svalidcell = 0;
                for (int j = 25; j <= 30; j++) {
                    if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                        svalidcell++;
                        break;
                    }
                }
                if (svalidcell == 0) {
                    continue;
                }
                for (int j = 25; j <= 30; j++) {
                    String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                    if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setName(val);
                        vpme.setName(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (StringUtil.isEmpty(val)) {
                            tag++;
                            iie.setErrmsg("必填信息");
                        } else if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            pme.setName(null);
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("工号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setNo(val);
                        vpme.setNo(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (StringUtil.isEmpty(val)) {
                            tag++;
                            iie.setErrmsg("必填信息");
                        } else if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            pme.setNo(null);
                        } else {
                            User u = userService.getByNo(val);
                            if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)) {
                                tag++;
                                iie.setErrmsg("找到该工号人员，但不是导师");
                            } else if (u != null && pme.getName() != null && !pme.getName().equals(u.getName())) {
                                tag++;
                                iie.setErrmsg("工号和姓名不一致");
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("学院".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        office = null;
                        pme.setOffice(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if ((office = OfficeUtils.getOfficeByName(val)) == null) {
                                tag++;
                                iie.setErrmsg("学院不存在");
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        } else {
                            if (office != null) {
                                vpme.setOffice(office.getId());
                            }
                        }
                    } else if ("手机".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setMobile(val);
                        vpme.setMobile(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                                tag++;
                                iie.setErrmsg("手机号码格式不正确");
                            } else {
                                User u = new User();
                                u.setMobile(val);
                                User temu = userService.getByMobile(u);
                                if (temu != null && pme.getNo() != null && !pme.getNo().equals(temu.getNo())) {
                                    tag++;
                                    iie.setErrmsg("手机号码已存在");
                                }
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setEmail(val);
                        vpme.setEmail(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                                tag++;
                                iie.setErrmsg("邮箱格式不正确");
                            } else if (val.length() > 128) {
                                tag++;
                                iie.setErrmsg("最多128个字符");
                                pme.setEmail(null);
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    } else if ("职称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow + 1).getCell(j), sheet))) {
                        pme.setZhicheng(val);
                        vpme.setZhicheng(val);
                        iie = new ImpInfoErrmsg();
                        iie.setImpId(ii.getId());
                        iie.setDataId(phe.getId());
                        iie.setDataSubId(pme.getId());
                        iie.setColname(j + "");
                        if (!StringUtil.isEmpty(val)) {
                            if (val.length() > 100) {
                                tag++;
                                iie.setErrmsg("最多100个字符");
                                pme.setZhicheng(null);
                            }
                        }
                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                            impInfoErrmsgService.save(iie);
                        }
                    }
                }
                phe.getPtes().add(pme);
                validinfo.getPtes().add(vpme);
            }
            //处理导师数据end
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proModelGcontestErrorService.insert(phe);
            } else {// 无错误字段，保存信息
                try {
                    proModelGcontestErrorService.saveProModelGcontest(validinfo, ay, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存大赛信息出错", e);
                    fail++;
                    proModelGcontestErrorService.insert(phe);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
            i = i + megRows;
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importProModel(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) {
        Office office = null;
        Office profes = null;
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            int tag = 0;// 有几个错误字段
            ProModelError phe = new ProModelError();
            ProModelError validinfo = new ProModelError();// 用于保存处理之后的信息，以免再次查找数据库.
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = val.replaceAll(" ", "");
                }
                if ("学院".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    office = null;
                    phe.setOffice(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((office = OfficeUtils.getOfficeByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("学院不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setOffice(office.getId());
                    }
                } else if ("项目名称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setName(val);
                    validinfo.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setName(null);
                    } else if (proModelService.existProName(ay, val)) {
                        tag++;
                        iie.setErrmsg("项目名称已存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目编号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setNumber(val);
//                    validinfo.setNumber(val);
//                    iie = new ImpInfoErrmsg();
//                    iie.setImpId(ii.getId());
//                    iie.setDataId(phe.getId());
//                    iie.setColname(j + "");
//                    if (StringUtil.isEmpty(val)) {
//                        tag++;
//                        iie.setErrmsg("必填信息");
//                    } else if (val.length() > 64) {
//                        tag++;
//                        iie.setErrmsg("最多64个字符");
//                        phe.setNumber(null);
//                    } else {
//                        ProModel proModel = new ProModel();
//                        proModel.setCompetitionNumber(val);
//                        proModel.setProType(ay.getProProject().getProType());
//                        proModel.setType(ay.getProProject().getType());
//                        Integer cc = proModelDao.findCountByNum(proModel);
//                        if (cc != null && cc > 0) {
//                            tag++;
//                            iie.setErrmsg("该项目编号已经存在");
//                        }
//
//                    }
//                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                        impInfoErrmsgService.save(iie);
//                    }
                } else if ("项目类型".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setType(null);
                    } else if ((d = DictUtils.getDictByLabel(FlowPcategoryType.PCT_XM.getKey(), val)) == null) {
                        tag++;
                        iie.setErrmsg("项目类型不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setType(d.getValue());
                    }
                } else if ("负责人姓名"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setLeader(val);
                    validinfo.setLeader(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setLeader(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("学号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setNo(val);
                    validinfo.setNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setNo(null);
                    } else {
                        User u = userService.getByNo(val);
                        if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                            tag++;
                            iie.setErrmsg("找到该学号人员，但不是学生");
                        } else if (u != null && phe.getLeader() != null && !phe.getLeader().equals(u.getName())) {
                            tag++;
                            iie.setErrmsg("负责人学号和姓名不一致");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("联系电话"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setMobile(val);
                    validinfo.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号格式不正确");
                        } else {
                            User u = new User();
                            u.setMobile(val);
                            User temu = userService.getByMobile(u);
                            if (temu != null && phe.getNo() != null && !phe.getNo().equals(temu.getNo())) {
                                tag++;
                                iie.setErrmsg("手机号已存在");
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("电子邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setEmail(val);
                    validinfo.setEmail(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                            tag++;
                            iie.setErrmsg("邮箱格式不正确");
                        } else if (val.length() > 128) {
                            tag++;
                            iie.setErrmsg("最多128个字符");
                            phe.setEmail(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("专业"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    profes = null;
                    phe.setProfes(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (office != null
                                && (profes = OfficeUtils.getProfessionalByName(office.getName(), val)) == null) {
                            tag++;
                            iie.setErrmsg("专业不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (profes != null)
                            validinfo.setProfes(profes.getId());
                    }
                } else if ("团队成员及学号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setMembers(val);
                    validinfo.setMembers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 256) {
                            tag++;
                            iie.setErrmsg("最多256个字符");
                            phe.setMembers(null);
                        } else {
                            String s = checkMembers(userService, val, phe.getNo());
                            if (s != null) {
                                tag++;
                                iie.setErrmsg(s);
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("指导教师姓名"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeachers(val);
                    validinfo.setTeachers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isNotEmpty(val)) {
                        if (val.length() > 128) {
                            tag++;
                            iie.setErrmsg("最多128个字符");
                            phe.setTeachers(null);
                        } else {
                            String temval = ExcelUtils.getStringByCell(rowData.getCell(j + 1), sheet);
                            if (temval != null) {// 去掉所有空格
                                temval = temval.replaceAll(" ", "");
                            }
                            String s = ItCkProModelTeacherName.checkTeaName(val, temval);
                            if (s != null) {
                                tag++;
                                iie.setErrmsg(s);
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("指导教师工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeaNo(val);
                    validinfo.setTeaNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isNotEmpty(val)) {
                        if (val.length() > 128) {
                            tag++;
                            iie.setErrmsg("最多128个字符");
                            phe.setTeaNo(null);
                        } else {
                            String s = ItCkProModelTeacherNo.checkTeaNo(userService, phe.getTeachers(), val);
                            if (s != null) {
                                tag++;
                                iie.setErrmsg(s);
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("职称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeaTitle(val);
                    validinfo.setTeaTitle(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            phe.setTeaTitle(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目年份"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setYear(val);
                    validinfo.setYear(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!checkYear(val)) {
                            tag++;
                            iie.setErrmsg("项目年份格式不正确");
                        } else if (val.length() > 4) {
                            tag++;
                            iie.setErrmsg("最多4个字符");
                            phe.setYear(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目结果".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setResult(val);
                    validinfo.setResult(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 64) {
                            tag++;
                            iie.setErrmsg("最多64个字符");
                            phe.setResult(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                }

            }
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proModelErrorService.insert(phe);
            } else {// 无错误字段，保存信息
                try {
                    proModelErrorService.saveProject(validinfo, ay, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存项目信息出错", e);
                    fail++;
                    proModelErrorService.insert(phe);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importMdClose(XSSFSheet sheet0, ImpInfo ii) {
        importMdClose(sheet0, ii, null);
    }
    private void importMdClose(XSSFSheet sheet0, ImpInfo ii, ItOper impVo) {
        XSSFRow rowData;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        String sheetIndx0 = "0";
        for (int i = 4; i < ExcelUtils.getTotalRow(sheet0, "经手人："); i++) {
            int tag = 0;// 有几个错误字段
            ProMdCloseError se = new ProMdCloseError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet0.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet0.getRow(2).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet0))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            String pid = ExcelUtils.getStringByCell(rowData.getCell(15), sheet0);
            ImpInfoErrmsg iie3 = new ImpInfoErrmsg();
            iie3.setSheetIndx(sheetIndx0);
            iie3.setImpId(ii.getId());
            iie3.setDataId(se.getId());
            iie3.setColname("1");
            ProModel pm = proModelDao.get(pid);
            if (StringUtil.isEmpty(pid) || pm == null) {
                tag++;
                iie3.setErrmsg("文档结构已被修改，无法匹配");
            }
            if (StringUtil.isNotEmpty(iie3.getErrmsg())) {
                impInfoErrmsgService.save(iie3);
            }

            String result = ExcelUtils.getStringByCell(rowData.getCell(12), sheet0);
            ImpInfoErrmsg iie2 = new ImpInfoErrmsg();
            iie2.setSheetIndx(sheetIndx0);
            iie2.setImpId(ii.getId());
            iie2.setDataId(se.getId());
            iie2.setColname("12");
            if (StringUtil.isEmpty(result)) {
                tag++;
                iie2.setErrmsg("请填写学院审查意见");
            } else if (getResultCode(result) == null) {
                tag++;
                iie2.setErrmsg("未知的学院审查意见");
                result = null;
            }
            if (StringUtil.isNotEmpty(iie2.getErrmsg())) {
                impInfoErrmsgService.save(iie2);
            } else {
                result = getResultCode(result);
            }
            String exc = ExcelUtils.getStringByCell(rowData.getCell(13), sheet0);
            ImpInfoErrmsg iie1 = new ImpInfoErrmsg();
            iie1.setSheetIndx(sheetIndx0);
            iie1.setImpId(ii.getId());
            iie1.setDataId(se.getId());
            iie1.setColname("13");
            if (!StringUtil.isEmpty(exc)) {
                if (getYorNCode(exc) == null) {
                    tag++;
                    iie1.setErrmsg("未知的输入");
                    result = null;
                }
            }
            if (StringUtil.isNotEmpty(iie1.getErrmsg())) {
                impInfoErrmsgService.save(iie1);
            } else {
                exc = getYorNCode(exc);
            }

            String gnode = ExcelUtils.getStringByCell(rowData.getCell(16), sheet0);
            ImpInfoErrmsg iie4 = new ImpInfoErrmsg();
            iie4.setSheetIndx(sheetIndx0);
            iie4.setImpId(ii.getId());
            iie4.setDataId(se.getId());
            iie4.setColname("1");
            if (StringUtil.isEmpty(gnode)) {
                tag++;
                iie4.setErrmsg("文档结构已被修改，无法匹配");
            } else if (pm != null && !checkGnode(gnode, pm.getProcInsId())) {
                tag++;
                iie4.setErrmsg("该项目已被导入过");
            }
            if (StringUtil.isNotEmpty(iie4.getErrmsg())) {
                impInfoErrmsgService.save(iie4);
            }
            se.setPNumber(ExcelUtils.getStringByCell(rowData.getCell(1), sheet0));
            se.setPName(ExcelUtils.getStringByCell(rowData.getCell(2), sheet0));
            se.setLeaderName(ExcelUtils.getStringByCell(rowData.getCell(3), sheet0));
            se.setNo(ExcelUtils.getStringByCell(rowData.getCell(4), sheet0));
            se.setMobile(ExcelUtils.getStringByCell(rowData.getCell(5), sheet0));
            se.setMembers(ExcelUtils.getStringByCell(rowData.getCell(6), sheet0));
            se.setTeaName(ExcelUtils.getStringByCell(rowData.getCell(7), sheet0));
            se.setTeaNo(ExcelUtils.getStringByCell(rowData.getCell(8), sheet0));
            se.setLevel(ExcelUtils.getStringByCell(rowData.getCell(9), sheet0));
            se.setProCategory(ExcelUtils.getStringByCell(rowData.getCell(10), sheet0));
            se.setResult(ExcelUtils.getStringByCell(rowData.getCell(11), sheet0));
            se.setAuditResult(ExcelUtils.getStringByCell(rowData.getCell(12), sheet0));
            se.setExcellent(ExcelUtils.getStringByCell(rowData.getCell(13), sheet0));
            se.setReimbursementAmount(ExcelUtils.getStringByCell(rowData.getCell(14), sheet0));
            se.setProModelMdId(ExcelUtils.getStringByCell(rowData.getCell(15), sheet0));
            se.setGnodeid(ExcelUtils.getStringByCell(rowData.getCell(16), sheet0));
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proMdCloseErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    proMdCloseErrorService.saveProMdClose(exc, result, pm, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存民大项目结项审核信息出错", e);
                    fail++;
                    proMdCloseErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importMdMid(XSSFSheet sheet0, ImpInfo ii) {
        importMdMid(sheet0, ii, null);
    }

    private void importMdMid(XSSFSheet sheet0, ImpInfo ii, ItOper impVo) {
        XSSFRow rowData;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        String sheetIndx0 = "0";
        for (int i = 4; i < ExcelUtils.getTotalRow(sheet0, "经手人："); i++) {
            int tag = 0;// 有几个错误字段
            ProMdMidError se = new ProMdMidError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet0.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet0.getRow(2).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet0))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            String pid = ExcelUtils.getStringByCell(rowData.getCell(12), sheet0);
            ImpInfoErrmsg iie3 = new ImpInfoErrmsg();
            iie3.setSheetIndx(sheetIndx0);
            iie3.setImpId(ii.getId());
            iie3.setDataId(se.getId());
            iie3.setColname("1");
            ProModel pm = proModelDao.get(pid);
            if (StringUtil.isEmpty(pid) || pm == null) {
                tag++;
                iie3.setErrmsg("文档结构已被修改，无法匹配");
            }
            if (StringUtil.isNotEmpty(iie3.getErrmsg())) {
                impInfoErrmsgService.save(iie3);
            }

            String result = ExcelUtils.getStringByCell(rowData.getCell(9), sheet0);
            ImpInfoErrmsg iie2 = new ImpInfoErrmsg();
            iie2.setSheetIndx(sheetIndx0);
            iie2.setImpId(ii.getId());
            iie2.setDataId(se.getId());
            iie2.setColname("9");
            if (StringUtil.isEmpty(result)) {
                tag++;
                iie2.setErrmsg("请填写学院审查意见");
            } else if (getMidResultCode(result) == null) {
                tag++;
                iie2.setErrmsg("未知的学院审查意见");
                result = null;
            }
            if (StringUtil.isNotEmpty(iie2.getErrmsg())) {
                impInfoErrmsgService.save(iie2);
            } else {
                result = getMidResultCode(result);
            }

            String gnode = ExcelUtils.getStringByCell(rowData.getCell(13), sheet0);
            ImpInfoErrmsg iie4 = new ImpInfoErrmsg();
            iie4.setSheetIndx(sheetIndx0);
            iie4.setImpId(ii.getId());
            iie4.setDataId(se.getId());
            iie4.setColname("1");
            if (StringUtil.isEmpty(gnode)) {
                tag++;
                iie4.setErrmsg("文档结构已被修改，无法匹配");
            } else if (pm != null && !checkGnode(gnode, pm.getProcInsId())) {
                tag++;
                iie4.setErrmsg("该项目已被导入过");
            }
            if (StringUtil.isNotEmpty(iie4.getErrmsg())) {
                impInfoErrmsgService.save(iie4);
            }
            se.setPNumber(ExcelUtils.getStringByCell(rowData.getCell(1), sheet0));
            se.setPName(ExcelUtils.getStringByCell(rowData.getCell(2), sheet0));
            se.setLeaderName(ExcelUtils.getStringByCell(rowData.getCell(3), sheet0));
            se.setNo(ExcelUtils.getStringByCell(rowData.getCell(4), sheet0));
            se.setMobile(ExcelUtils.getStringByCell(rowData.getCell(5), sheet0));
            se.setTeachers(ExcelUtils.getStringByCell(rowData.getCell(6), sheet0));
            se.setProCategory(ExcelUtils.getStringByCell(rowData.getCell(7), sheet0));
            se.setLevel(ExcelUtils.getStringByCell(rowData.getCell(8), sheet0));
            se.setResult(ExcelUtils.getStringByCell(rowData.getCell(9), sheet0));
            se.setStageResult(ExcelUtils.getStringByCell(rowData.getCell(10), sheet0));
            se.setReimbursementAmount(ExcelUtils.getStringByCell(rowData.getCell(11), sheet0));
            se.setProModelMdId(ExcelUtils.getStringByCell(rowData.getCell(12), sheet0));
            se.setGnodeid(ExcelUtils.getStringByCell(rowData.getCell(13), sheet0));
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proMdMidErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    proMdMidErrorService.saveProMdMid(result, pm, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存民大项目中期检查信息出错", e);
                    fail++;
                    proMdMidErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importMdApproval(XSSFSheet sheet0, XSSFSheet sheet1, ImpInfo ii) {
        importMdApproval(sheet0, sheet1, ii, null);
    }
    private void importMdApproval(XSSFSheet sheet0, XSSFSheet sheet1, ImpInfo ii, ItOper impVo) {
        XSSFRow rowData;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        String sheetIndx0 = "0";
        for (int i = 4; i < ExcelUtils.getTotalRow(sheet0, "联系人签字："); i++) {
            int tag = 0;// 有几个错误字段
            ProMdApprovalError se = new ProMdApprovalError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet0.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet0.getRow(2).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet0))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            String pid = ExcelUtils.getStringByCell(rowData.getCell(22), sheet0);
            ImpInfoErrmsg iie3 = new ImpInfoErrmsg();
            iie3.setSheetIndx(sheetIndx0);
            iie3.setImpId(ii.getId());
            iie3.setDataId(se.getId());
            iie3.setColname("4");
            ProModel pm = proModelDao.get(pid);
            if (StringUtil.isEmpty(pid) || pm == null) {
                tag++;
                iie3.setErrmsg("文档结构已被修改，无法匹配");
            }
            if (StringUtil.isNotEmpty(iie3.getErrmsg())) {
                impInfoErrmsgService.save(iie3);
            }

            String pnumber = ExcelUtils.getStringByCell(rowData.getCell(3), sheet0);
//            ImpInfoErrmsg iie1 = new ImpInfoErrmsg();
//            iie1.setSheetIndx(sheetIndx0);
//            iie1.setImpId(ii.getId());
//            iie1.setDataId(se.getId());
//            iie1.setColname("3");
//            if (StringUtil.isEmpty(pnumber)) {
//                tag++;
//                iie1.setErrmsg("请填写项目编号");
//            } else if (pnumber.length() > 64) {
//                tag++;
//                iie1.setErrmsg("最多64个字符");
//                pnumber = null;
//            } else if (proMdApprovalErrorService.checkMdProNumber(pnumber, pid, pm)) {
//                tag++;
//                iie1.setErrmsg("该项目编号已存在");
//            }
//            if (StringUtil.isNotEmpty(iie1.getErrmsg())) {
//                impInfoErrmsgService.save(iie1);
//            }
            String result = ExcelUtils.getStringByCell(rowData.getCell(21), sheet0);
            ImpInfoErrmsg iie2 = new ImpInfoErrmsg();
            iie2.setSheetIndx(sheetIndx0);
            iie2.setImpId(ii.getId());
            iie2.setDataId(se.getId());
            iie2.setColname("21");
            if (StringUtil.isEmpty(result)) {
                tag++;
                iie2.setErrmsg("请填写评审结果");
            } else if (getResultCode(result) == null) {
                tag++;
                iie2.setErrmsg("未知的评审结果");
                result = null;
            }
            if (StringUtil.isNotEmpty(iie2.getErrmsg())) {
                impInfoErrmsgService.save(iie2);
            } else {
                result = getResultCode(result);
            }

            String gnode = ExcelUtils.getStringByCell(rowData.getCell(23), sheet0);
            ImpInfoErrmsg iie4 = new ImpInfoErrmsg();
            iie4.setSheetIndx(sheetIndx0);
            iie4.setImpId(ii.getId());
            iie4.setDataId(se.getId());
            iie4.setColname("4");
            if (StringUtil.isEmpty(gnode)) {
                tag++;
                iie4.setErrmsg("文档结构已被修改，无法匹配");
            } else if (pm != null && !checkGnode(gnode, pm.getProcInsId())) {
                tag++;
                iie4.setErrmsg("该项目已被导入过");
            }
            if (StringUtil.isNotEmpty(iie4.getErrmsg())) {
                impInfoErrmsgService.save(iie4);
            }
            se.setProCategory(ExcelUtils.getStringByCell(rowData.getCell(1), sheet0));
            se.setLevel(ExcelUtils.getStringByCell(rowData.getCell(2), sheet0));
            se.setPNumber(ExcelUtils.getStringByCell(rowData.getCell(3), sheet0));
            se.setPName(ExcelUtils.getStringByCell(rowData.getCell(4), sheet0));
            se.setLeaderName(ExcelUtils.getStringByCell(rowData.getCell(5), sheet0));
            se.setNo(ExcelUtils.getStringByCell(rowData.getCell(6), sheet0));
            se.setMobile(ExcelUtils.getStringByCell(rowData.getCell(7), sheet0));
            se.setProSource(getAbc(rowData, sheet0));
            se.setSourceProjectName(ExcelUtils.getStringByCell(rowData.getCell(11), sheet0));
            se.setSourceProjectType(ExcelUtils.getStringByCell(rowData.getCell(12), sheet0));
            se.setTeachers1(ExcelUtils.getStringByCell(rowData.getCell(13), sheet0));
            se.setTeachers2(ExcelUtils.getStringByCell(rowData.getCell(14), sheet0));
            se.setTeachers3(ExcelUtils.getStringByCell(rowData.getCell(15), sheet0));
            se.setTeachers4(ExcelUtils.getStringByCell(rowData.getCell(16), sheet0));
            se.setRufu(ExcelUtils.getStringByCell(rowData.getCell(17), sheet0));
            se.setMembers1(ExcelUtils.getStringByCell(rowData.getCell(18), sheet0));
            se.setMembers2(ExcelUtils.getStringByCell(rowData.getCell(19), sheet0));
            se.setMembers3(ExcelUtils.getStringByCell(rowData.getCell(20), sheet0));
            se.setResult(ExcelUtils.getStringByCell(rowData.getCell(21), sheet0));
            se.setProModelMdId(ExcelUtils.getStringByCell(rowData.getCell(22), sheet0));
            se.setGnodeid(ExcelUtils.getStringByCell(rowData.getCell(23), sheet0));
            se.setSheetIndex(sheetIndx0);

            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proMdApprovalErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    proMdApprovalErrorService.saveProMdApproval(pnumber, result, pm, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存民大项目立项信息出错", e);
                    fail++;
                    proMdApprovalErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        String sheetIndx1 = "1";
        for (int i = 4; i < ExcelUtils.getTotalRow(sheet1, "联系人签字："); i++) {
            int tag = 0;// 有几个错误字段
            ProMdApprovalError se = new ProMdApprovalError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet1.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet1.getRow(2).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet1))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            String pid = ExcelUtils.getStringByCell(rowData.getCell(22), sheet1);
            ImpInfoErrmsg iie3 = new ImpInfoErrmsg();
            iie3.setSheetIndx(sheetIndx1);
            iie3.setImpId(ii.getId());
            iie3.setDataId(se.getId());
            iie3.setColname("4");
            ProModel pm = proModelDao.get(pid);
            if (StringUtil.isEmpty(pid) || pm == null) {
                tag++;
                iie3.setErrmsg("文档结构已被修改，无法匹配");
            }
            if (StringUtil.isNotEmpty(iie3.getErrmsg())) {
                impInfoErrmsgService.save(iie3);
            }

            String pnumber = ExcelUtils.getStringByCell(rowData.getCell(3), sheet1);
//            ImpInfoErrmsg iie1 = new ImpInfoErrmsg();
//            iie1.setSheetIndx(sheetIndx1);
//            iie1.setImpId(ii.getId());
//            iie1.setDataId(se.getId());
//            iie1.setColname("3");
//            if (StringUtil.isEmpty(pnumber)) {
//                tag++;
//                iie1.setErrmsg("请填写项目编号");
//            } else if (pnumber.length() > 64) {
//                tag++;
//                iie1.setErrmsg("最多64个字符");
//                pnumber = null;
//            } else if (proMdApprovalErrorService.checkMdProNumber(pnumber, pid, pm)) {
//                tag++;
//                iie1.setErrmsg("该项目编号已存在");
//            }
//            if (StringUtil.isNotEmpty(iie1.getErrmsg())) {
//                impInfoErrmsgService.save(iie1);
//            }
            String result = ExcelUtils.getStringByCell(rowData.getCell(21), sheet1);
            ImpInfoErrmsg iie2 = new ImpInfoErrmsg();
            iie2.setSheetIndx(sheetIndx1);
            iie2.setImpId(ii.getId());
            iie2.setDataId(se.getId());
            iie2.setColname("21");
            if (StringUtil.isEmpty(result)) {
                tag++;
                iie2.setErrmsg("请填写评审结果");
            } else if (getResultCode(result) == null) {
                tag++;
                iie2.setErrmsg("未知的评审结果");
                result = null;
            }
            if (StringUtil.isNotEmpty(iie2.getErrmsg())) {
                impInfoErrmsgService.save(iie2);
            } else {
                result = getResultCode(result);
            }

            String gnode = ExcelUtils.getStringByCell(rowData.getCell(23), sheet1);
            ImpInfoErrmsg iie4 = new ImpInfoErrmsg();
            iie4.setSheetIndx(sheetIndx1);
            iie4.setImpId(ii.getId());
            iie4.setDataId(se.getId());
            iie4.setColname("4");
            if (StringUtil.isEmpty(gnode)) {
                tag++;
                iie4.setErrmsg("文档结构已被修改，无法匹配");
            } else if (pm != null && !checkGnode(gnode, pm.getProcInsId())) {
                tag++;
                iie4.setErrmsg("该项目已被导入过");
            }
            if (StringUtil.isNotEmpty(iie4.getErrmsg())) {
                impInfoErrmsgService.save(iie4);
            }
            se.setProCategory(ExcelUtils.getStringByCell(rowData.getCell(1), sheet1));
            se.setLevel(ExcelUtils.getStringByCell(rowData.getCell(2), sheet1));
            se.setPNumber(ExcelUtils.getStringByCell(rowData.getCell(3), sheet1));
            se.setPName(ExcelUtils.getStringByCell(rowData.getCell(4), sheet1));
            se.setLeaderName(ExcelUtils.getStringByCell(rowData.getCell(5), sheet1));
            se.setNo(ExcelUtils.getStringByCell(rowData.getCell(6), sheet1));
            se.setMobile(ExcelUtils.getStringByCell(rowData.getCell(7), sheet1));
            se.setProSource(getAbc(rowData, sheet1));
            se.setSourceProjectName(ExcelUtils.getStringByCell(rowData.getCell(11), sheet1));
            se.setSourceProjectType(ExcelUtils.getStringByCell(rowData.getCell(12), sheet1));
            se.setTeachers1(ExcelUtils.getStringByCell(rowData.getCell(13), sheet1));
            se.setTeachers2(ExcelUtils.getStringByCell(rowData.getCell(14), sheet1));
            se.setTeachers3(ExcelUtils.getStringByCell(rowData.getCell(15), sheet1));
            se.setTeachers4(ExcelUtils.getStringByCell(rowData.getCell(16), sheet1));
            se.setRufu(ExcelUtils.getStringByCell(rowData.getCell(17), sheet1));
            se.setMembers1(ExcelUtils.getStringByCell(rowData.getCell(18), sheet1));
            se.setMembers2(ExcelUtils.getStringByCell(rowData.getCell(19), sheet1));
            se.setMembers3(ExcelUtils.getStringByCell(rowData.getCell(20), sheet1));
            se.setResult(ExcelUtils.getStringByCell(rowData.getCell(21), sheet1));
            se.setProModelMdId(ExcelUtils.getStringByCell(rowData.getCell(22), sheet1));
            se.setGnodeid(ExcelUtils.getStringByCell(rowData.getCell(23), sheet1));
            se.setSheetIndex(sheetIndx1);

            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                proMdApprovalErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    proMdApprovalErrorService.saveProMdApproval(pnumber, result, pm, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存民大项目立项信息出错", e);
                    fail++;
                    proMdApprovalErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    public static void main(String[] args) {
        try {
            showExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  void showExcel() throws Exception {
        XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\项目详情信息汇总表（1-20）.xlsx")));
        XSSFSheet sheet=null;
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
             sheet=workbook.getSheetAt(0);
             for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {//获取每行
                 XSSFRow row=sheet.getRow(j);
                for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {//获取每个单元格
                    System.out.print(row.getCell(k)+"\t");
                }
                System.out.println("---Sheet表"+i+"处理完毕---");
            }
        }
    }

    public void importData(MultipartFile imgFile, HttpServletRequest request) throws Exception {
        InputStream is = null;
        try {
            is = imgFile.getInputStream();
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0); // 获取第一个sheet表
            String sheetname = sheet.getSheetName();
            checkTemplate(sheet, request);// 检查模板版本
            String sname = sheetname.split("-")[0];
            if ("学生".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.Stu.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importStudent(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CoreUtils.USER_CACHE);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("学生信息导入出错", e);
                        }
                    }
                });
            } else if ("全国学生".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.ConStu.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importConStu(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CoreUtils.USER_CACHE);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("学生信息导入出错", e);
                        }
                    }
                });
            }else if ("导师".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.Tea.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importTeacher(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CoreUtils.USER_CACHE);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("导师信息导入出错", e);
                        }
                    }
                });
            } else if ("后台用户".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.BackUser.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importBackUser(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CoreUtils.USER_CACHE);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("后台用户信息导入出错", e);
                        }
                    }
                });
            } else if ("机构".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.Org.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importOrg(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("机构信息导入出错", e);
                        }
                    }
                });
            } else if ("国创项目".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.DcProjectClose.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importProject(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("国创项目信息导入出错", e);
                        }
                    }
                });
            } else if ("项目信息".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.PmProjectHsmid.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {

                            importHsProject(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("项目信息导入出错", e);
                        }
                    }
                });
            } else if ("互联网+大赛信息".equals(sname)) {
                ImpInfo ii = ImpInfo.genImpInfo(imgFile.getOriginalFilename(), ExpType.HlwGcontest.getIdx(), ((sheet.getLastRowNum() - ImpDataService.descHeadRow) + ""));
                impInfoService.save(ii);// 插入导入信息
                ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                    @Override
                    public void run() {
                        try {
                            importGcontest(sheet, ii);
                        } catch (Exception e) {
                            ii.setIsComplete(Const.YES);
                            impInfoService.save(ii);
                            CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                            logger.error("互联网+大赛信息导入出错", e);
                        }
                    }
                });
            }
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Transactional(readOnly = false)
    public void importNewData(ItCparamGgj param, ActYw ay, XSSFWorkbook wb, ImpInfo ii, ItOper iepVo) {
        XSSFSheet sheet=null;
        int fail = 0;// 失败数
        int success = 0;// 成功数

        List<ProModelGJError> phes = null;
        List<ProModelGJError> vals = Lists.newArrayList();
        System.out.println("wb.getNumberOfSheets() = "+wb.getNumberOfSheets());
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            phes = Lists.newArrayList();
            param.setTag(0);// 有几个错误字段
            param.setTags(0);
            GgjError ggjError = new GgjError();
            ProModelGJError phe = ggjError.getErr();
            ProModelGJError validinfo = ggjError.getVerr();
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            phe.setIsNewRecord(true);

            // TODO: 2019/1/25 0025  陈浩 模板校验
            //获取每个Sheet表
            sheet=wb.getSheetAt(i);
            param.setIdxSheet(phe.getId());
            int rowNum = sheet.getLastRowNum();
            int proRow = 4;
            for(int j = 0;j < proRow ; j++){
                XSSFRow rowData = sheet.getRow(j);
                if(rowData == null){
                    break;
                }

                String val = ExcelUtils.getStringByCell(rowData.getCell(0), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }

                //做判断 根据首行一个点判断当前人的职责
                if("项目名称".equals(val)){
                    param.setIdxRow(j+1);
                    XSSFRow nextRdata = sheet.getRow(j+1);
                    String name = ExcelUtils.getStringByCell(nextRdata.getCell(0), sheet);
                    phe.setName(name);
                    phe.setPstype(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    phe.setLevel(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    phe.setType(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    phe.setStage(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    phe.setPdomain(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));

                    validinfo.setName(phe.getName());
                    validinfo.setPstype(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    validinfo.setLevel(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    validinfo.setType(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    validinfo.setStage(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    validinfo.setPdomain(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    XSSFRow nnextRdata = sheet.getRow(j + 3);
                    phe.setIntroduction(ExcelUtils.getStringByCell(nnextRdata.getCell(0), sheet));
                    validinfo.setIntroduction(ExcelUtils.getStringByCell(nnextRdata.getCell(0), sheet));

                    param.setTag(0);
                    param.setVal(phe.getName());
                    param.setColName("项目名称");
                    param.setIdx(0);
                    param = new ItCkGgjProName(proModelService).validate(param, phe, validinfo);
                    fail = dearError(param, phes, fail, phe);

                    param.setTag(0);
                    param.setVal(phe.getLevel());
                    param.setColName("组别");
                    param.setIdx(2);
                    param = new ItCkGgjLevel().validate(param, phe, validinfo);
                    fail = dearError(param, phes, fail, phe);

                    param.setTag(0);
                    param.setVal(phe.getType());
                    param.setColName("类别");
                    param.setIdx(3);
                    param = new ItCkGgjtype().validate(param, phe, validinfo);
                    fail = dearError(param, phes, fail, phe);
                    ggjError.setErr(phe);
                    ggjError.setVerr(validinfo);
                }else{
                    break;
                }
            }

            for(int j = proRow; j < rowNum; j=j+2){
                XSSFRow rowData = sheet.getRow(j);
                if(rowData == null){
                    break;
                }

                String val = ExcelUtils.getStringByCell(rowData.getCell(0), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }

                //做判断 根据首行一个点判断当前人的职责
                if("项目负责人信息".equals(val)){
                    int curRow = j+1;
                    XSSFRow nextRdata = sheet.getRow(curRow);
                    phe.setLsrow(curRow);
                    phe.setLeader(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    phe.setProvince(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    phe.setUniversityName(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    phe.setProfes(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    phe.setMobile(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    phe.setNo(phe.getMobile());
                    phe.setEmail(ExcelUtils.getStringByCell(nextRdata.getCell(6), sheet));
                    phe.setLyear(ExcelUtils.getStringByCell(nextRdata.getCell(7), sheet));
                    phe.setGyear(ExcelUtils.getStringByCell(nextRdata.getCell(8), sheet));
                    phe.setSchool(phe.getUniversityName());
                    validinfo.setLeader(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    validinfo.setProvince(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    validinfo.setUniversityName(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    validinfo.setProfes(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    validinfo.setMobile(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    validinfo.setNo(validinfo.getMobile());
                    validinfo.setEmail(ExcelUtils.getStringByCell(nextRdata.getCell(6), sheet));
                    validinfo.setLyear(ExcelUtils.getStringByCell(nextRdata.getCell(7), sheet));
                    validinfo.setGyear(ExcelUtils.getStringByCell(nextRdata.getCell(8), sheet));
                    validinfo.setSchool(validinfo.getUniversityName());
//                    param.setTag(0);
//                    param.setVal(phe.getNo());
//                    param.setColName("学号");
//                    param = new ItCkGgjTeamNo(userService).validate(param, phe, validinfo);
//                    fail = dearError(param, fail, phe);
                    ggjError.setErr(phe);
                    ggjError.setVerr(validinfo);
                }else if("团队成员信息".equals(val)){
                    int curRow = j+1;
                    XSSFRow nextRdata = sheet.getRow(curRow);
                    GgjStudent cstu = new GgjStudent();
                    cstu.setCname("团队成员信息");
                    cstu.setSrow(curRow);
                    cstu.setName(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    cstu.setArea(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    cstu.setSchool(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    cstu.setProfessional(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    cstu.setMobile(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    cstu.setNo(cstu.getMobile());
                    cstu.setEmail(ExcelUtils.getStringByCell(nextRdata.getCell(6), sheet));
                    cstu.setYear(ExcelUtils.getStringByCell(nextRdata.getCell(7), sheet));
                    cstu.setGyear(ExcelUtils.getStringByCell(nextRdata.getCell(8), sheet));

//                    param.setTag(0);
//                    param.setVal(cstu.getNo());
//                    param.setColName("学号");
//                    param = new ItCkGgjTeamNo(userService).validate(param, phe, validinfo);
//                    fail = dearError(param, fail, phe);
                    ggjError.getStuErrs().add(cstu);
                }else if("指导教师信息".equals(val)) {
                    int curRow = j+1;
                    XSSFRow nextRdata = sheet.getRow(curRow);
                    GgjTeacher ctea = new GgjTeacher();
                    ctea.setCname("指导教师信息");
                    ctea.setSrow(curRow);
                    ctea.setName(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    ctea.setArea(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    ctea.setSchool(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    ctea.setSubject(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    ctea.setTechnicalTitle(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    ctea.setMobile(ExcelUtils.getStringByCell(nextRdata.getCell(6), sheet));
                    ctea.setNo(ctea.getMobile());
                    ctea.setEmail(ExcelUtils.getStringByCell(nextRdata.getCell(7), sheet));

//                    param.setTag(0);
//                    param.setVal(ctea.getNo());
//                    param.setColName("指导教师工号");
//                    param = new ItCkGgjTeacherNo(userService).validate(param, phe, validinfo);
//                    fail = dearError(param, fail, phe);
                    ggjError.getTeaErrs().add(ctea);
                }else if("工商信息".equals(val)) {
                    int curRow = j+1;
                    XSSFRow nextRdata = sheet.getRow(curRow);
                    GgjBusInfo cinfo = new GgjBusInfo();
                    cinfo.setCname("工商信息");
                    cinfo.setSrow(curRow);
                    cinfo.setCyname(ExcelUtils.getStringByCell(nextRdata.getCell(1), sheet));
                    cinfo.setArea(ExcelUtils.getStringByCell(nextRdata.getCell(2), sheet));
                    cinfo.setNo(ExcelUtils.getStringByCell(nextRdata.getCell(3), sheet));
                    cinfo.setName(ExcelUtils.getStringByCell(nextRdata.getCell(4), sheet));
                    cinfo.setMoney(ExcelUtils.getStringByCell(nextRdata.getCell(5), sheet));
                    cinfo.setRegTime(ExcelUtils.getStringByCell(nextRdata.getCell(6), sheet));

//                    param.setTag(0);
//                    param.setVal(ctea.getNo());
//                    param.setColName("指导教师工号");
//                    param = new ItCkGgjTeacherNo(userService).validate(param, phe, validinfo);
//                    fail = dearError(param, fail, phe);
                    ggjError.getInfoErrs().add(cinfo);
                }else{
                    break;
                }

                if(StringUtil.isNotEmpty(phe.getId())){
                    /**
                     * 判断项目出错，更新其它信息到错误信息表.
                     */
                    for (ProModelGJError curphe : phes) {
                        if((curphe.getId()).equals(phe.getId())){
                            phes.remove(curphe);
                            phes.add(phe);
                            break;
                        }
                    }
                }
            }

            if(StringUtil.checkNotEmpty(ggjError.getStuErrs())){
                phe.setMembers(GgjStudent.toMembers(ggjError.getStuErrs()));
                validinfo.setMembers(GgjStudent.toMembers(ggjError.getStuErrs()));
                ggjError.setErr(phe);
                ggjError.setVerr(validinfo);
            }
            if(StringUtil.checkNotEmpty(ggjError.getTeaErrs())){
                phe.setTeachers(GgjTeacher.toTeachers(ggjError.getTeaErrs()));
                validinfo.setTeachers(GgjTeacher.toTeachers(ggjError.getTeaErrs()));
                ggjError.setErr(phe);
                ggjError.setVerr(validinfo);
            }
            if(StringUtil.checkNotEmpty(ggjError.getInfoErrs())){
                phe.setBusinfos(GgjBusInfo.toBusInfos(ggjError.getInfoErrs()));
                validinfo.setBusinfos(GgjBusInfo.toBusInfos(ggjError.getInfoErrs()));
                ggjError.setErr(phe);
                ggjError.setVerr(validinfo);
            }
            if ((param.getTags() != 0)) {// 有错误字段,记录错误信息
                fail++;
                List<ProModelGcontestError> pges = Lists.newArrayList();
                if (StringUtil.checkNotEmpty(phes)) {// 有错误字段,记录错误信息
                    for (ProModelGJError cphe : phes) {
                        pges.add(ProModelGJError.toGcontestError(cphe));
                    }
                    proModelGcontestErrorService.updatePL(pges);
                }
//                proModelErrorService.updatePL(phes);
            } else {// 无错误字段，保存信息
                try {
                    proModelErrorService.saveProject(validinfo, ay, iepVo, false);
                    success++;
                } catch (Exception e) {
                    logger.error("保存项目信息出错", e);
                    fail++;
                    proModelErrorService.insert(phe);
                }
            }

            //记录在缓存中 页面会刷新缓存值
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private int dearError(ItCparamGgj param, List<ProModelGJError> phes, int fail, ProModelGJError phe) {
        if (param.getTag() != 0) {// 有错误字段,记录错误信息
            param.setTags(param.getTags() + param.getTag());
//            fail++;
//            proModelErrorService.insert(phe);
            ProModelGcontestError curPmgError = ProModelGJError.toGcontestError(phe);
            if(curPmgError.getIsNewRecord()){
                proModelGcontestErrorService.insert(curPmgError);
                phe.setIsNewRecord(false);
                phes.add(phe);
            }

        }
        return fail;
    }

    private void importStudent(XSSFSheet sheet, ImpInfo ii) {
        ItCparamUser param = new ItCparamUser(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), new User());
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        Dict d = null;
        Office office = null;// 学院
        Office professional = null;// 专业
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判
        // 断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            param.setStudent(new StudentExpansion());
            param.setTag(0);// 有几个错误字段
            StudentError validinfo = new StudentError();// 用于保存处理之后的信息，以免再次查找数据库.
            StudentError se = new StudentError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet.getRow(i);

            /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }

			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }
                param.setIdx(j);
                param.setVal(val);
                param.setRows(ImpDataService.descHeadRow);

                param = new ItCkUloginName().validate(param, se, validinfo);
                param = new ItCkUname().validate(param, se, validinfo);
                param = new ItCkUno().validate(param, se, validinfo);
                param = new ItCkUmobile().validate(param, se, validinfo);
                param = new ItCkUemail().validate(param, se, validinfo);
                param = new ItCkUremarks().validate(param, se, validinfo);
                param = new ItCkUbirthday().validate(param, se, validinfo);
                param = new ItCkUidType().validate(param, se, validinfo);
                param = new ItCkUidNo().validate(param, se, validinfo);
                param = new ItCkUsex().validate(param, se, validinfo);
                param = new ItCkUdomain().validate(param, se, validinfo);
                param = new ItCkUdegree().validate(param, se, validinfo);
                param = new ItCkUeducation().validate(param, se, validinfo);
                ItCkUoffice itCkUoffice =  new ItCkUoffice();
                param = itCkUoffice.validate(param, se, validinfo);
                if(itCkUoffice.getOffice() != null){
                    office = itCkUoffice.getOffice();
                }

                ItCkUprofessional itCkUprofessional =  new ItCkUprofessional(office);
                param = itCkUprofessional.validate(param, se, validinfo);
                if(itCkUprofessional.getProfessional() != null){
                    professional = itCkUprofessional.getProfessional();
                }
                param = new ItCkUtclass().validate(param, se, validinfo);
                param = new ItCkUcountry().validate(param, se, validinfo);
                param = new ItCkUnational().validate(param, se, validinfo);
                param = new ItCkUpolitical().validate(param, se, validinfo);
                param = new ItCkUprojectExperience().validate(param, se, validinfo);
                param = new ItCkUcontestExperience().validate(param, se, validinfo);
                param = new ItCkUaward().validate(param, se, validinfo);
                param = new ItCkUenterdate().validate(param, se, validinfo);
                param = new ItCkUtemporaryDate().validate(param, se, validinfo);
                param = new ItCkUgraduation().validate(param, se, validinfo);
                param = new ItCkUaddress().validate(param, se, validinfo);
                param = new ItCkUinstudy().validate(param, se, validinfo);
                param = new ItCkUcurrState().validate(param, se, validinfo);
            }
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
                fail++;
                studentErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    param.getUser().setUserType(EuserType.UT_C_STUDENT.getType());
                    param.getStudent().setUser(param.getUser());
                    studentErrorService.saveStudent(param.getStudent());
                    success++;
                } catch (Exception e) {
                    logger.error("保存学生信息出错", e);
                    fail++;
                    studentErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importConStu(XSSFSheet sheet, ImpInfo ii) {
           ItCparamUser param = new ItCparamUser(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), new User());
           XSSFRow rowData;
           ImpInfoErrmsg iie;
           Dict d = null;
           Office office = null;// 学院
           Office professional = null;// 专业
           int fail = 0;// 失败数
           int success = 0;// 成功数
           // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
           for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
               param.setStudent(new StudentExpansion());
               param.setTag(0);// 有几个错误字段
               StudentError validinfo = new StudentError();// 用于保存处理之后的信息，以免再次查找数据库.
               StudentError se = new StudentError();
               se.setImpId(ii.getId());
               se.setId(IdGen.uuid());
               rowData = sheet.getRow(i);

               /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
               int validcell = 0;
               for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                   if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                       validcell++;
                       break;
                   }
               }
               if (validcell == 0) {
                   continue;
               }

   			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
               for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                   String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                   if (val != null) {// 去掉所有空格
                       val = StringUtil.trim(val);
                   }
                   param.setIdx(j);
                   param.setVal(val);
                   param.setRows(ImpDataService.descHeadRow);
                   param = new ItCkUname().validate(param, se, validinfo);
                   param = new ItCkUno().validate(param, se, validinfo);
                   param = new ItCkUmobile().validate(param, se, validinfo);
                   param = new ItCkUemail().validate(param, se, validinfo);
                   param = new ItCkUremarks().validate(param, se, validinfo);

                   ItCkUoffice itCkUoffice =  new ItCkUoffice();
                   param = itCkUoffice.validate(param, se, validinfo);
                   if(itCkUoffice.getOffice() != null){
                       office = itCkUoffice.getOffice();
                   }
                   ItCkUprofessional itCkUprofessional =  new ItCkUprofessional(office);
                   param = itCkUprofessional.validate(param, se, validinfo);
                   if(itCkUprofessional.getProfessional() != null){
                       professional = itCkUprofessional.getProfessional();
                   }
                   param = new ItCkCUenterdate().validate(param, se, validinfo);
                   param = new ItCkCUgraduation().validate(param, se, validinfo);

               }
               if (param.getTag() != 0) {// 有错误字段,记录错误信息
                   fail++;
                   studentErrorService.insert(se);
               } else {// 无错误字段，保存信息
                   try {
                       param.getUser().setUserType(EuserType.UT_C_STUDENT.getType());
                       param.getStudent().setUser(param.getUser());
                       studentErrorService.saveStudent(param.getStudent());
                       success++;
                   } catch (Exception e) {
                       logger.error("保存学生信息出错", e);
                       fail++;
                       studentErrorService.insert(se);
                   }
               }
               ii.setFail(fail + "");
               ii.setSuccess(success + "");
               ii.setTotal((fail + success) + "");
               CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
           }
           ii.setIsComplete(Const.YES);
           impInfoService.save(ii);
           CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
       }

    private void importTeacher(XSSFSheet sheet, ImpInfo ii) {
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        Dict d = null;
        Office office = null;// 学院
        Office professional = null;// 专业
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            BackTeacherExpansion tc = new BackTeacherExpansion();
            User user = new User();
            int tag = 0;// 有几个错误字段
            TeacherError te = new TeacherError();
            te.setImpId(ii.getId());
            te.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if ("用户名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setLoginName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
//                        if (UserUtils.getByLoginNameOrNo(val) != null) {
//                            tag++;
//                            iie.setErrmsg("用户名已存在");
//                        } else
                        if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            te.setLoginName(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setLoginName(val);
                    }
                } else if ("导师来源".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setTeachertype(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((d = DictUtils.getDictByLabel(SysSval.DICT_MASTER_TYPE, val)) == null) {
                        tag++;
                        iie.setErrmsg("导师来源不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            tc.setTeachertype(d.getValue());
                    }
                } else if ("工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        te.setNo(null);
                    }
//                    else if (UserUtils.isExistNo(val)) {
//                        tag++;
//                        iie.setErrmsg("工号已存在");
//                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setNo(val);
                    }
                } else if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        te.setName(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setName(val);
                    }
                } else if ("性别"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setSex(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && (d = DictUtils.getDictByLabel(SysSval.DICT_SEX, val)) == null) {
                        tag++;
                        iie.setErrmsg("性别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            user.setSex(d.getValue());
                    }
                } else if ("手机号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号格式不正确");
                        } else if (UserUtils.isExistMobile(val)) {
                            tag++;
                            iie.setErrmsg("手机号已存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setMobile(val);
                    }
                } else if ("邮箱"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setEmail(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                            tag++;
                            iie.setErrmsg("邮箱格式不正确");
                        } else if (val.length() > 200) {
                            tag++;
                            iie.setErrmsg("最多200个字符");
                            te.setEmail(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setEmail(val);
                    }
                } else if ("备注".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setRemarks(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 255) {
                        tag++;
                        iie.setErrmsg("最多255个字符");
                        te.setRemarks(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setRemarks(val);
                    }
                } else if ("出生年月"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setBirthday(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        try {
                            user.setBirthday(DateUtil.parseDate(val, DateUtil.FMT_YYYYMMDD_ZG));
                        } catch (ParseException e) {
                            tag++;
                            iie.setErrmsg("日期格式不正确");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("证件类别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setIdType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if ((d = DictUtils.getDictByLabel(SysSval.DICT_ID_TYPE, val)) == null) {
                            tag++;
                            iie.setErrmsg("证件类别不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            user.setIdType(d.getValue());
                    }
                } else if ("证件号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setIdNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 32) {
                            tag++;
                            iie.setErrmsg("最多32个字符");
                            te.setIdNo(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setIdNumber(val);
                    }
                } else if ("擅长技术领域"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setDomain(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    List<String> list = new ArrayList<String>();
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 1024) {
                            tag++;
                            iie.setErrmsg("擅长技术领域内容过长");
                            te.setDomain(null);
                        } else {
                            String[] vs = val.split(",");
                            for (String v : vs) {
                                if ((d = DictUtils.getDictByLabel(SysSval.DICT_TECHNOLOGY_FIELD, v)) == null) {
                                    tag++;
                                    iie.setErrmsg("擅长技术领域不存在");
                                    break;
                                } else {
                                    list.add(d.getValue());
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setDomain(StringUtil.join(list.toArray(), ","));
                    }
                } else if ("学历类别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setEducationType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if ((d = DictUtils.getDictByLabel(SysSval.DICT_ENDUCATION_TYPE, val)) == null) {
                            tag++;
                            iie.setErrmsg("学历类别不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            tc.setEducationType(d.getValue());
                    }
                } else if ("学位"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setDegree(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if ((d = DictUtils.getDictByLabel(SysSval.DICT_DEGREE_TYPE, val)) == null) {
                            tag++;
                            iie.setErrmsg("学位不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            user.setDegree(d.getValue());
                    }
                } else if ("学历".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setEducation(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if ((d = DictUtils.getDictByLabel(SysSval.DICT_ENDUCATION_LEVEL, val)) == null) {
                            tag++;
                            iie.setErrmsg("学历不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            user.setEducation(d.getValue());
                    }
                } else if ("学院"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    office = null;
                    te.setOffice(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((office = OfficeUtils.getOfficeByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("学院不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setOffice(office);
                        //user.setProfessional(office.getId());
                    }
                } else if ("专业".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    professional = null;
                    te.setProfessional(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!StringUtil.isEmpty(val) && office != null && (professional = OfficeUtils.getProfessionalByName(office.getName(), val)) == null) {
                            tag++;
                            iie.setErrmsg("专业不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (professional != null)
                            user.setProfessional(professional.getId());
                    }
                } else if ("民族"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setNational(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        te.setNational(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setNational(val);
                    }
                } else if ("政治面貌".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setPolitical(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        te.setPolitical(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setPolitical(val);
                    }
                } else if ("国家/地区"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setArea(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 20) {
                        tag++;
                        iie.setErrmsg("最多20个字符");
                        te.setArea(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setArea(val);
                    }
                } else if ("学科门类".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    d = null;
                    te.setDiscipline(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && (d = DictUtils.getDictByLabel(SysSval.DICT_PROFESSIONAL_TYPE, val)) == null) {
                        tag++;
                        iie.setErrmsg("学科门类不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            tc.setDiscipline(Integer.parseInt(d.getValue()));
                    }
                } else if ("行业"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setIndustry(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 32) {
                        tag++;
                        iie.setErrmsg("最多32个字符");
                        te.setIndustry(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setIndustry(val);
                    }
                } else if ("职称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setTechnicalTitle(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 20) {
                        tag++;
                        iie.setErrmsg("最多20个字符");
                        te.setTechnicalTitle(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setTechnicalTitle(val);
                    }
                } else if ("服务意向"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setServiceIntention(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    List<String> list = new ArrayList<String>();
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 1024) {
                            tag++;
                            iie.setErrmsg("服务意向内容过长");
                            te.setServiceIntention(null);
                        } else {
                            String[] vs = val.split(",");
                            for (String v : vs) {
                                if ((d = DictUtils.getDictByLabel(SysSval.DICT_MASTER_HELP, v)) == null) {
                                    tag++;
                                    iie.setErrmsg("服务意向不存在");
                                    break;
                                } else {
                                    list.add(d.getValue());
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setServiceIntention(StringUtil.join(list.toArray(), ","));
                    }
                } else if ("工作单位".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setWorkUnit(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        te.setWorkUnit(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setWorkUnit(val);
                    }
                } else if ("联系地址"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setAddress(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        te.setAddress(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setAddress(val);
                    }
                } else if ("开户银行".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setFirstBank(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        te.setFirstBank(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        tc.setFirstBank(val);
                    }
                } else if ("银行账号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    te.setBankAccount(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(te.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 16) {
                            tag++;
                            iie.setErrmsg("最多16个字符");
                            te.setBankAccount(null);
                        } else {
                            try {
                                Long.valueOf(val);
                            } catch (Exception e) {
                                tag++;
                                iie.setErrmsg("只能包含数字");
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (!StringUtil.isEmpty(val))
                            tc.setBankAccount(val);
                    }
                }
            }
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                teacherErrorService.insert(te);
            } else {// 无错误字段，保存信息
                try {
                    user.setUserType(EuserType.UT_C_TEACHER.getType());
                    tc.setUser(user);
                    teacherErrorService.saveTeacher(tc);
                    success++;
                } catch (Exception e) {
                    logger.error("保存导师信息出错", e);
                    fail++;
                    teacherErrorService.insert(te);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importBackUser(XSSFSheet sheet, ImpInfo ii) {
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        Dict d = null;
        Office office = null;// 学院
        Role role = null;// 角色
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            User user = new User();
            int tag = 0;// 有几个错误字段
            BackUserError se = new BackUserError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if ("用户名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setLoginName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (UserUtils.getByLoginNameOrNo(val) != null) {
                        tag++;
                        iie.setErrmsg("用户名已存在");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        se.setLoginName(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setLoginName(val);
                    }
                } else if ("用户类型".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
					/*d = null;
					se.setUserType(val);
					iie = new ImpInfoErrmsg();
					iie.setImpId(ii.getId());
					iie.setDataId(se.getId());
					iie.setColname(j + "");
					if (StringUtil.isEmpty(val)) {
						tag++;
						iie.setErrmsg("必填信息");
					} else if ((d = DictUtils.getDictByLabel("sys_user_type", val)) == null || "1".equals(d.getValue())
							|| "2".equals(d.getValue())) {
						tag++;
						iie.setErrmsg("用户类型不存在");
					}
					if (StringUtil.isNotEmpty(iie.getErrmsg())) {
						impInfoErrmsgService.save(iie);
					} else {
						if (d != null)
							user.setUserType(d.getValue());
					}*/
                } else if ("角色"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    boolean validRole = true;//角色信息是否有效
                    se.setRoles(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    List<Role> list = new ArrayList<Role>();
                    if (StringUtil.isEmpty(val)) {
                        validRole = false;
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 1024) {
                        validRole = false;
                        tag++;
                        iie.setErrmsg("角色内容过长");
                        se.setRoles(null);
                    } else {
                        for (String v : getSetFromStr(val)) {
                            role = getRoleByName(v);
                            if (role == null) {
                                validRole = false;
                                tag++;
                                iie.setErrmsg("角色不存在");
                                break;
                            } else if ((role.getId()).equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId()) || (role.getId()).equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())) {
                                validRole = false;
                                tag++;
                                iie.setErrmsg("后台用户角色不包含（学生、导师）");
                                break;
                            } else {
                                list.add(role);
                            }
                        }
                    }
                    if (validRole) {
                        String rolecheck = SysUserUtils.checkRoleList(list);
                        if (rolecheck != null) {
                            tag++;
                            iie.setErrmsg(rolecheck);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setRoleList(list);
                    }
                } else if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        se.setName(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setName(val);
                    }
                } else if ("工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        se.setNo(null);
                    } else if (UserUtils.isExistNo(val)) {
                        tag++;
                        iie.setErrmsg("工号已存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setNo(val);
                    }
                } else if ("手机号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
//                    if (StringUtil.isEmpty(val)) {
//                        tag++;
//                        iie.setErrmsg("必填信息");
//                    } else
                    if (StringUtil.isNotEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号格式不正确");
                        } else if (UserUtils.isExistMobile(val)) {
                            tag++;
                            iie.setErrmsg("手机号已存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setMobile(val);
                    }
                } else if ("邮箱"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setEmail(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                            tag++;
                            iie.setErrmsg("邮箱格式不正确");
                        } else if (val.length() > 200) {
                            tag++;
                            iie.setErrmsg("最多200个字符");
                            se.setEmail(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setEmail(val);
                    }
                } else if ("备注".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setRemarks(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val) && val.length() > 255) {
                        tag++;
                        iie.setErrmsg("最多255个字符");
                        se.setRemarks(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setRemarks(val);
                    }
                } else if ("擅长技术领域"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    se.setDomain(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    List<String> list = new ArrayList<String>();
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 1024) {
                            tag++;
                            iie.setErrmsg("擅长技术领域内容过长");
                            se.setDomain(null);
                        } else {
                            String[] vs = val.split(",");
                            for (String v : vs) {
                                if ((d = DictUtils.getDictByLabel(SysSval.DICT_TECHNOLOGY_FIELD, v)) == null) {
                                    tag++;
                                    iie.setErrmsg("擅长技术领域不存在");
                                    break;
                                } else {
                                    list.add(d.getValue());
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setDomain(StringUtil.join(list.toArray(), ","));
                    }
                } else if ("学校/学院"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    office = null;
                    se.setOffice(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(se.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((office = OfficeUtils.getOrgByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("学校/学院不存在");
                    } /*else {
						if ("3".equals(user.getUserType()) || "4".equals(user.getUserType())) {
							if (!"2".equals(office.getGrade())) {
								tag++;
								iie.setErrmsg("只能填学院");
							}
						} else if ("5".equals(user.getUserType()) || "6".equals(user.getUserType())
								|| "7".equals(user.getUserType())) {
							if (!"1".equals(office.getGrade())) {
								tag++;
								iie.setErrmsg("只能填学校");
							}
						}
					}*/
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        user.setOffice(office);
                        user.setProfessional(office.getId());
                    }
                }
            }
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                backUserErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    backUserErrorService.saveBackUser(user);
                    success++;
                } catch (Exception e) {
                    logger.error("保存后台用户信息出错", e);
                    fail++;
                    backUserErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importOrg(XSSFSheet sheet, ImpInfo ii) {
        importOrg(sheet, ii, null, null);
    }
    private void importOrg(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) {
        ItCparamPm param = new ItCparamPm(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), ay);
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        Office school = OfficeUtils.getSchool();// 学校
        ItRpOffice rparam = null;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            param.setTag(0);// 有几个错误字段
            OfficeError validinfo = new OfficeError();// 用于保存处理之后的信息，以免再次查找数据库.
            OfficeError se = new OfficeError();
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet.getRow(i);

			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }
                param.setIdx(j);
                param.setVal(val);
                param.setRows(ImpDataService.descHeadRow);

                param = new ItCkOfficeXy(officeService, school, rowData).validate(param, se, validinfo);
                param = new ItCkOfficeZy().validate(param, se, validinfo);
                param = new ItCkOfficeRemark().validate(param, se, validinfo);
            }
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
                fail++;
                officeErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    rparam = (ItRpOffice) param.getRparam();
                    officeService.save(rparam.getCur());
                    success++;
                } catch (Exception e) {
                    logger.error("保存机构信息出错", e);
                    fail++;
                    officeErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importProject(XSSFSheet sheet, ImpInfo ii) {
        importProject(sheet, ii, null, null);
    }
    private void importProject(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) {
        ItCparamPm param = new ItCparamPm(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), ay);
        XSSFRow rowData;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            List<User> leader = null;
            ProjectError se = new ProjectError();
            ProjectError validinfo = new ProjectError();// 用于保存处理之后的信息，以免再次查找数据库.
            param.setTag(0);// 有几个错误字段
            se.setImpId(ii.getId());
            se.setId(IdGen.uuid());
            rowData = sheet.getRow(i);

			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }

            if (validcell == 0) {
                continue;
            }

			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }
                param.setIdx(j);
                param.setVal(val);
                param.setRows(ImpDataService.descHeadRow);
                param = new ItCkPDProjectYear().validate(param, se, validinfo);
                param = new ItCkArea().validate(param, se, validinfo);
                param = new ItCkOfficeUcode().validate(param, se, validinfo);
                param = new ItCkOfficeName().validate(param, se, validinfo);
                param = new ItCkPDProjectNumber(projectDeclareService).validate(param, se, validinfo);
                ItCkPDProjectName itCkPDProjectName =  new ItCkPDProjectName(userService, rowData, projectDeclareService, studentErrorService);
                param = itCkPDProjectName.validate(param, se, validinfo);
                if(StringUtil.checkNotEmpty(itCkPDProjectName.getLeader())){
                    leader = itCkPDProjectName.getLeader();
                }
                param = new ItCkPDProjectType().validate(param, se, validinfo);
                param = new ItCkPDProjectLeaderName().validate(param, se, validinfo);
                param = new ItCkPDProjectLeaderNo(leader).validate(param, se, validinfo);
                param = new ItCkPDProjectTeamMembers(userService, studentErrorService).validate(param, se, validinfo);
                param = new ItCkPDProjectTeacherName(userService, teacherErrorService).validate(param, se, validinfo);
                param = new ItCkPDProjectFinanceGrant().validate(param, se, validinfo);
                param = new ItCkPDProjectUniversityGrant().validate(param, se, validinfo);
                param = new ItCkPDProjectTotalGrant().validate(param, se, validinfo);
                param = new ItCkPDProjectIntroduction().validate(param, se, validinfo);
            }
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
                fail++;
                projectErrorService.insert(se);
            } else {// 无错误字段，保存信息
                try {
                    projectErrorService.saveProject(validinfo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存项目信息出错", e);
                    fail++;
                    projectErrorService.insert(se);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importGcontest(XSSFSheet sheet, ImpInfo ii) {
        Office office = null;
        Office profes = null;
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            int tag = 0;// 有几个错误字段
            GcontestError phe = new GcontestError();
            GcontestError validinfo = new GcontestError();// 用于保存处理之后的信息，以免再次查找数据库.
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = val.replaceAll(" ", "");
                }
                if ("参赛项目名称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setName(val);
                    validinfo.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setName(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("大赛类别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setType(null);
                    } else if ((d = DictUtils.getDictByLabel("competition_net_type", val)) == null) {
                        tag++;
                        iie.setErrmsg("大赛类别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setType(d.getValue());
                    }
                } else if ("参赛组别"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setGroups(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setGroups(null);
                    } else if ((d = DictUtils.getDictByLabel("gcontest_level", val)) == null) {
                        tag++;
                        iie.setErrmsg("参赛组别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setGroups(d.getValue());
                    }
                } else if ("申报人/学号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setLeader(val);
                    validinfo.setLeader(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setLeader(null);
                    } else if (val.split(StringUtil.LINE).length != 2) {
                        tag++;
                        iie.setErrmsg("格式有误");
                    } else {
                        User u = userService.getByNo(val.split(StringUtil.LINE)[1]);
                        if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                            tag++;
                            iie.setErrmsg("找到该学号人员，但不是学生");
                        } else if (u != null && !val.split(StringUtil.LINE)[0].equals(u.getName())) {
                            tag++;
                            iie.setErrmsg("申报人学号和姓名不一致");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("所属学院".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    office = null;
                    phe.setOffice(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((office = OfficeUtils.getOfficeByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("学院不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setOffice(office.getId());
                    }
                } else if ("所属专业"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    profes = null;
                    phe.setProfes(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (office != null
                                && (profes = OfficeUtils.getProfessionalByName(office.getName(), val)) == null) {
                            tag++;
                            iie.setErrmsg("专业不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (profes != null) {
                            validinfo.setProfes(profes.getId());
                        }
                    }
                } else if ("申报人手机"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setMobile(val);
                    validinfo.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号格式不正确");
                        } else {
                            User u = new User();
                            u.setMobile(val);
                            User temu = userService.getByMobile(u);
                            if (temu != null && phe.getLeader() != null && phe.getLeader().split(StringUtil.LINE).length == 2
                                    && !phe.getLeader().split(StringUtil.LINE)[1].equals(temu.getNo())) {
                                tag++;
                                iie.setErrmsg("手机号已存在");
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("校内导师/工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setSteachers(val);
                    validinfo.setSteachers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 256) {
                        tag++;
                        iie.setErrmsg("最多256个字符");
                        phe.setSteachers(null);
                    } else {
                        String s = checkSTeachers(val);
                        if (s != null) {
                            tag++;
                            iie.setErrmsg(s);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("企业导师/工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setEteachers(val);
                    validinfo.setEteachers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 256) {
                            tag++;
                            iie.setErrmsg("最多256个字符");
                            phe.setEteachers(null);
                        } else {
                            String s = checkETeachers(val, getMapFromStr(phe.getSteachers()));
                            if (s != null) {
                                tag++;
                                iie.setErrmsg(s);
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("备注".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setRemarks(val);
                    validinfo.setRemarks(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 1024) {
                            tag++;
                            iie.setErrmsg("最多1024个字符");
                            phe.setRemarks(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("荣获奖项"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setHuojiang(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 64) {
                            tag++;
                            iie.setErrmsg("最多64个字符");
                            phe.setHuojiang(null);
                        } else if ((d = DictUtils.getDictByLabel("competition_college_prise", val)) == null) {
                            tag++;
                            iie.setErrmsg("荣获奖项不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (d != null)
                            validinfo.setHuojiang(d.getValue());
                    }
                }
            }
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                gcontestErrorService.insert(phe);
            } else {// 无错误字段，保存信息
                try {
                    gcontestErrorService.saveGcontest(validinfo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存项目信息出错", e);
                    fail++;
                    gcontestErrorService.insert(phe);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    private void importHsProject(XSSFSheet sheet, ImpInfo ii) {
        Office office = null;
        Office profes = null;
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            int tag = 0;// 有几个错误字段
            ProjectHsError phe = new ProjectHsError();
            ProjectHsError validinfo = new ProjectHsError();// 用于保存处理之后的信息，以免再次查找数据库.
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
                    validcell++;
                    break;
                }
            }
            if (validcell == 0) {
                continue;
            }
			/* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种end */
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = val.replaceAll(" ", "");
                }
                if ("学院".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    office = null;
                    phe.setOffice(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if ((office = OfficeUtils.getOfficeByName(val)) == null) {
                        tag++;
                        iie.setErrmsg("学院不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setOffice(office.getId());
                    }
                } else if ("项目名称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setName(val);
                    validinfo.setName(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setName(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目编号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setNumber(val);
//                    validinfo.setNumber(val);
//                    iie = new ImpInfoErrmsg();
//                    iie.setImpId(ii.getId());
//                    iie.setDataId(phe.getId());
//                    iie.setColname(j + "");
//                    if (StringUtil.isEmpty(val)) {
//                        tag++;
//                        iie.setErrmsg("必填信息");
//                    } else if (val.length() > 64) {
//                        tag++;
//                        iie.setErrmsg("最多64个字符");
//                        phe.setNumber(null);
//                    } else {
//                        List<ProjectDeclare> plist = projectDeclareService.getProjectByCdn(val, null, null);
//                        if (plist != null && !plist.isEmpty()) {
//                            tag++;
//                            iie.setErrmsg("该项目编号已经存在");
//                        }
//
//                    }
//                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                        impInfoErrmsgService.save(iie);
//                    }
                } else if ("项目类型".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setType(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 64) {
                        tag++;
                        iie.setErrmsg("最多64个字符");
                        phe.setType(null);
                    } else if ((d = DictUtils.getDictByLabel(FlowPcategoryType.PCT_XM.getKey(), val)) == null) {
                        tag++;
                        iie.setErrmsg("项目类型不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setType(d.getValue());
                    }
                } else if ("负责人姓名"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setLeader(val);
                    validinfo.setLeader(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setLeader(null);
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("学号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setNo(val);
                    validinfo.setNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setNo(null);
                    } else {
                        User u = userService.getByNo(val);
                        if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                            tag++;
                            iie.setErrmsg("找到该学号人员，但不是学生");
                        } else if (u != null && phe.getLeader() != null && !phe.getLeader().equals(u.getName())) {
                            tag++;
                            iie.setErrmsg("负责人学号和姓名不一致");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("联系电话"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setMobile(val);
                    validinfo.setMobile(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
                            tag++;
                            iie.setErrmsg("手机号格式不正确");
                        } else {
                            User u = new User();
                            u.setMobile(val);
                            User temu = userService.getByMobile(u);
                            if (temu != null && phe.getNo() != null && !phe.getNo().equals(temu.getNo())) {
                                tag++;
                                iie.setErrmsg("手机号已存在");
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("电子邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setEmail(val);
                    validinfo.setEmail(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!Pattern.matches(RegexUtils.emailRegex, val)) {
                            tag++;
                            iie.setErrmsg("邮箱格式不正确");
                        } else if (val.length() > 128) {
                            tag++;
                            iie.setErrmsg("最多128个字符");
                            phe.setEmail(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("专业".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    profes = null;
                    phe.setProfes(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (office != null
                                && (profes = OfficeUtils.getProfessionalByName(office.getName(), val)) == null) {
                            tag++;
                            iie.setErrmsg("专业不存在");
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        if (profes != null) {
                            validinfo.setProfes(profes.getId());
                        }
                    }
                } else if ("年级".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setGrade(val);
                    validinfo.setGrade(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (!checkYear(val)) {
                            tag++;
                            iie.setErrmsg("年级填写有误");
                        } else if (val.length() > 12) {
                            tag++;
                            iie.setErrmsg("最多12个字符");
                            phe.setGrade(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("团队成员及学号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setMembers(val);
                    validinfo.setMembers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 256) {
                            tag++;
                            iie.setErrmsg("最多256个字符");
                            phe.setMembers(null);
                        } else {
                            String s = checkMembers(userService, val, phe.getNo());
                            if (s != null) {
                                tag++;
                                iie.setErrmsg(s);
                            }
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("第一指导教师姓名"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeachers(val);
                    validinfo.setTeachers(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setTeachers(null);
                    } else {
                        String temval = ExcelUtils.getStringByCell(rowData.getCell(j + 1), sheet);
                        if (temval != null) {// 去掉所有空格
                            temval = temval.replaceAll(" ", "");
                        }
                        String s = ItCkProModelTeacherName.checkTeaName(val, temval);
                        if (s != null) {
                            tag++;
                            iie.setErrmsg(s);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("指导教师工号"
                        .equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeaNo(val);
                    validinfo.setTeaNo(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 128) {
                        tag++;
                        iie.setErrmsg("最多128个字符");
                        phe.setTeaNo(null);
                    } else {
                        String s = ItCkProModelTeacherNo.checkTeaNo(userService, phe.getTeachers(), val);
                        if (s != null) {
                            tag++;
                            iie.setErrmsg(s);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("职称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setTeaTitle(val);
                    validinfo.setTeaTitle(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 100) {
                            tag++;
                            iie.setErrmsg("最多100个字符");
                            phe.setTeaTitle(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("级别".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    Dict d = null;
                    phe.setLevel(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (val.length() > 100) {
                        tag++;
                        iie.setErrmsg("最多100个字符");
                        phe.setLevel(null);
                    } else if ((d = DictUtils.getDictByLabel("project_degree", val)) == null) {
                        tag++;
                        iie.setErrmsg("项目级别不存在");
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    } else {
                        validinfo.setLevel(d.getValue());
                    }
                } else if ("备注".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setRemarks(val);
                    validinfo.setRemarks(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (!StringUtil.isEmpty(val)) {
                        if (val.length() > 1024) {
                            tag++;
                            iie.setErrmsg("最多1024个字符");
                            phe.setRemarks(null);
                        }
                    }
                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                } else if ("项目年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet))) {
                    phe.setYear(val);
                    validinfo.setYear(val);
                    iie = new ImpInfoErrmsg();
                    iie.setImpId(ii.getId());
                    iie.setDataId(phe.getId());
                    iie.setColname(j + "");
                    if (StringUtil.isEmpty(val)) {
                        tag++;
                        iie.setErrmsg("必填信息");
                    } else if (!checkYear(val)) {
                        tag++;
                        iie.setErrmsg("项目年份格式不正确");
                    } else if (val.length() > 4) {
                        tag++;
                        iie.setErrmsg("最多4个字符");
                        phe.setYear(null);
                    }

                    if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                        impInfoErrmsgService.save(iie);
                    }
                }
            }
            if (tag != 0) {// 有错误字段,记录错误信息
                fail++;
                projectHsErrorService.insert(phe);
            } else {// 无错误字段，保存信息
                try {
                    projectHsErrorService.saveProject(validinfo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存项目信息出错", e);
                    fail++;
                    projectHsErrorService.insert(phe);
                }
            }
            ii.setFail(fail + "");
            ii.setSuccess(success + "");
            ii.setTotal((fail + success) + "");
            CacheUtils.put(CacheUtils.IMPDATA_CACHE, ii.getId(), ii);
        }
        ii.setIsComplete(Const.YES);
        impInfoService.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    /*******************************************************************************************
     * Excel处理列数据转换.
     *******************************************************************************************/
    //解析  企业导师/工号
    private Map<String, String> getMapFromStr(String steas) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isNotEmpty(steas)) {
            String[] list = steas.split("、");
            if (list != null && list.length > 0) {
                for (int i = 0; i < list.length; i++) {
                    String[] tea = list[i].split(StringUtil.LINE);
                    if (tea.length == 2) {
                        map.put(tea[1], tea[1]);
                    }
                }
            }
        }
        return map;
    }

    // 解析名称学号
    public static List<String[]> getMembersData(String members) {
        String regxpForTag = "\\d+";
        Pattern patternForTag = Pattern.compile(regxpForTag, Pattern.CASE_INSENSITIVE);
        if (StringUtil.isNotEmpty(members)) {
            List<String[]> list = new ArrayList<String[]>();
            for (String mem : members.split(",")) {
                if (StringUtil.isNotEmpty(mem)) {
                    String[] ss = new String[2];
                    Matcher matcherForTag = patternForTag.matcher(mem);
                    if (matcherForTag.find()) {
                        ss[1] = matcherForTag.group(0);
                        ss[0] = mem.substring(0, matcherForTag.start());
                        list.add(ss);
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }
    // 解析名称学号
    public static List<String[]> getMembersData(String members, String prefix, String postfix) {
        String regxpForTag = "\\d+";
        Pattern patternForTag = Pattern.compile(regxpForTag, Pattern.CASE_INSENSITIVE);
        if (StringUtil.isNotEmpty(members)) {
            List<String[]> list = new ArrayList<String[]>();
            for (String mem : members.split(postfix)) {
                if (StringUtil.isNotEmpty(mem)) {
                    String[] ss = new String[2];
                    int idx = (mem).indexOf(prefix);
                    if (idx != -1) {
                        ss[0] = mem.substring(0, idx);
                        ss[1] = mem.substring(idx + 1);
                        list.add(ss);
                    }
//                    Matcher matcherForTag = patternForTag.matcher(mem);
//                    if (matcherForTag.find()) {
//                        ss[1] = matcherForTag.group(0);
//                        ss[0] = mem.substring(0, matcherForTag.start());
//                        list.add(ss);
//                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }
    // 解析名称学号
    public static List<String[]> getMembersData(String members, ItReqParam reqParam) {
        if (StringUtil.isNotEmpty(members)) {
            String[] splits = null;
            if(reqParam.getSpre().getIsTwo()){
                splits = SpiltPref.getTwo(reqParam.getSpre());
            }

            List<String[]> list = new ArrayList<String[]>();
            for (String mem : members.split(reqParam.getPostfix())) {
                if (StringUtil.isEmpty(mem)) {
                    continue;
                }

                if (((splits != null) && (splits.length == 2))) {
                    String[] ss = new String[2];
                    int idx = (mem).indexOf(splits[0]);
                    if (idx != -1) {
                        ss[0] = mem.substring(0, idx);
                        ss[1] = mem.substring(idx + 1, (mem).indexOf(splits[1]));
                        list.add(ss);
                    }
                }else{
                    String[] ss = new String[2];
                    int idx = (mem).indexOf(reqParam.getPrefix());
                    if (idx != -1) {
                        ss[0] = mem.substring(0, idx);
                        ss[1] = mem.substring(idx + 1);
                        list.add(ss);
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    private String getMidResultCode(String s) {
        if ("通过中检".equals(s)) {
            return "1";
        } else if ("项目终止".equals(s)) {
            return "0";
        } else {
            return null;
        }
    }

    private String getYorNCode(String s) {
        if ("是".equals(s)) {
            return "1";
        } else if ("否".equals(s)) {
            return "0";
        } else {
            return null;
        }
    }

    private String getResultCode(String s) {
        if ("通过".equals(s)) {
            return "1";
        } else if ("不通过".equals(s)) {
            return "0";
        } else {
            return null;
        }
    }

    // false 已被导入过
    private boolean checkGnode(String gnode, String proc) {
        ActYwGnode actYwGnode = proActTaskService.getNodeByProInsId(proc);
        if ((actYwGnode != null) && (!actYwGnode.isSuspended()) && (actYwGnode.getId().equals(gnode))) {
            return true;
        }
        return false;
    }

    private String getAbc(XSSFRow rowData, XSSFSheet sheet0) {
        if ("√".equals(ExcelUtils.getStringByCell(rowData.getCell(8), sheet0))) {
            return "A";
        } else if ("√".equals(ExcelUtils.getStringByCell(rowData.getCell(9), sheet0))) {
            return "B";
        } else if ("√".equals(ExcelUtils.getStringByCell(rowData.getCell(10), sheet0))) {
            return "C";
        } else {
            return "";
        }
    }

    private Role getRoleByName(String name) {
        List<Role> list = UserUtils.getRoleList();
        if (!list.isEmpty()) {
            for (Role r : list) {
                if (r.getName().equals(name)) {
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * 处理角色，用户导入专用.
     * @param val
     * @return
     */
    private Set<String> getSetFromStr(String val) {
        Set<String> set = new HashSet<String>();
        for (String s : val.split(",")) {
            set.add(s);
        }
        return set;
    }

    /*******************************************************************************************
     * Excel处理公共数据校验.
     *******************************************************************************************/
    /**
     * 校验入学年份.
     * @param enter
     * @param out
     * @return boolean
     */
    public static boolean checkOutYear(String enter, String out) {
        if (StringUtil.isEmpty(enter) || StringUtil.isEmpty(out)) {
            return true;
        }
        if (!checkYear(enter) || !checkYear(out)) {
            return true;
        }
        if (Integer.parseInt(enter) >= Integer.parseInt(out)) {
            return false;
        }
        return true;
    }

    public static boolean checkYear(String year) {
        try {
            if (YRAR.length() != year.length()) {
                return false;
            }
            new SimpleDateFormat(YRAR).parse(year);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String checkSTeachers(String teachers) {
        StringBuffer sb = new StringBuffer();
        String[] list = teachers.split("、");
        if (list != null && list.length > 0) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < list.length; i++) {
                String[] tea = list[i].split(StringUtil.LINE);
                if (tea.length != 2) {
                    return "格式有误";
                } else if (StringUtil.isEmpty(tea[0])) {
                    return "请填写校内导师名称";
                } else if (tea[0].length() > 100) {
                    return "校内导师名称最多100个字符";
                } else if (StringUtil.isEmpty(tea[1])) {
                    return "请填写校内导师工号";
                } else if (tea[1].length() > 100) {
                    return "校内导师工号最多100个字符";
                } else if (map.get(tea[1]) != null) {
                    return tea[1] + "校内导师工号重复";
                } else {
                    User u = userService.getByNo(tea[1]);
                    if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)) {
                        sb.append(tea[0]).append(tea[1]).append("找到该工号人员，但不是校内导师;");
                    }
                    if (u != null && SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)
                            && backTeacherExpansionDao.findTeacherByUserIdAndType(u.getId(), "1") == null) {
                        sb.append(tea[0]).append(tea[1]).append("找到该工号人员，但不是校内导师;");
                    } else if (u != null && !tea[0].equals(u.getName())) {
                        sb.append(tea[0]).append(tea[1]).append("姓名工号不一致;");
                    } else {
                        map.put(tea[1], tea[1]);
                    }
                }
            }
        }
        if (StringUtil.isNotEmpty(sb)) {
            return sb.toString();
        }
        return null;
    }

    private String checkETeachers(String teachers, Map<String, String> steas) {
        StringBuffer sb = new StringBuffer();
        String[] list = teachers.split("、");
        if (list != null && list.length > 0) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < list.length; i++) {
                String[] tea = list[i].split(StringUtil.LINE);
                if (tea.length != 2) {
                    return "格式有误";
                } else if (StringUtil.isEmpty(tea[0])) {
                    return "请填写企业导师名称";
                } else if (tea[0].length() > 100) {
                    return "企业导师名称最多100个字符";
                } else if (StringUtil.isEmpty(tea[1])) {
                    return "请填写企业导师工号";
                } else if (tea[1].length() > 100) {
                    return "企业导师工号最多100个字符";
                } else if (map.get(tea[1]) != null) {
                    return tea[1] + "企业导师工号重复";
                } else if (steas.get(tea[1]) != null) {
                    return tea[1] + "企业导师工号和校内导师工号重复";
                } else {
                    User u = userService.getByNo(tea[1]);
                    if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)) {
                        sb.append(tea[0]).append(tea[1]).append("找到该工号人员，但不是企业导师;");
                    }
                    if (u != null && SysUserUtils.checkHasRole(u, RoleBizTypeEnum.DS)
                            && backTeacherExpansionDao.findTeacherByUserIdAndType(u.getId(), "2") == null) {
                        sb.append(tea[0]).append(tea[1]).append("找到该工号人员，但不是企业导师;");
                    } else if (u != null && !tea[0].equals(u.getName())) {
                        sb.append(tea[0]).append(tea[1]).append("姓名工号不一致;");
                    } else {
                        map.put(tea[1], tea[1]);
                    }
                }
            }
        }
        if (StringUtil.isNotEmpty(sb)) {
            return sb.toString();
        }
        return null;
    }

    public static String checkMembers(UserService userService, String members, String leaderno) {
        StringBuffer sb = new StringBuffer();
        List<String[]> list = getMembersData(members, StringUtil.LINE, StringUtil.DOTH);
        if (list != null && list.size() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            for (String[] mem : list) {
                if (StringUtil.isEmpty(mem[0])) {
                    return "请填写成员名称";
                } else if (mem[0].length() > 100) {
                    return "成员名称最多100个字符";
                } else if (StringUtil.isEmpty(mem[1])) {
                    return "请填写成员学号";
                } else if (mem[1].length() > 100) {
                    return "成员学号最多100个字符";
                } else if (mem[1].equals(leaderno)) {
                    return "成员学号和负责人学号重复";
                } else {
                    User u = userService.getByNo(mem[1]);
                    if (u != null && !SysUserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
                        sb.append(mem[0]).append(mem[1]).append("找到该学号人员，但不是学生;");
                    } else if (u != null && !mem[0].equals(u.getName())) {
                        sb.append(mem[0]).append(mem[1]).append("姓名学号不一致;");
                    } else if (map.get(mem[1]) != null) {
                        sb.append(mem[1]).append("学号重复;");
                    } else {
                        map.put(mem[1], mem[1]);
                    }
                }
            }
            if (StringUtil.isNotEmpty(sb)) {
                return sb.toString();
            }
        }
        return null;
    }

    /**
     * 校验附件是否存在.
     * @param phe
     * @param ay
     * @return boolean
     * @throws Exception
     */
    public static boolean checkValidFile(ProModelGcontestError phe, ActYw ay) throws Exception {
        if (!phe.isValidName()) {
            return true;
        }
        if (Const.NO_ZH.equals(phe.getHasfile())) {
            return true;
        }
        String filepath = "/"+ TenantConfig.getCacheTenant()+tempProModelFilePath + ay.getProProject().getProType() + ay.getProProject().getType() + StringUtil.LINE + MD5Util.string2MD5(phe.getName());
        int c = VsftpUtils.getFileCount(filepath);
        if (c > 0) {
            return true;
        } else {
            return false;
        }
    }
    /*******************************************************************************************
     * Excel处理、数据解析
     *******************************************************************************************/


    /*******************************************************************************************
     * 公共工具类方法，处理附件上传
     *******************************************************************************************/
    /**
     * 导入附件时，上传附件到Ftp服务器.
     * @param ay
     * @param imgFile
     * @throws Exception
     */
    public static void uploadZip(ActYw ay, MultipartFile imgFile) throws Exception {
        //保存zip文件到本地
        if (!FileUtil.checkFileType(imgFile, FileUtil.DOT + FileUtil.SUFFIX_ZIP)) {
            return;
        }
        String filedir = File.separator + FileUtil.TempFileDir + File.separator + IdGen.uuid();// 生成的文件所在目录
        try {
            File tempPathDir = new File(filedir + File.separator);
            if (!tempPathDir.exists()) {
                tempPathDir.mkdirs();
            }
            String fname = imgFile.getOriginalFilename().toLowerCase();
            File newFile = new File(filedir + File.separator + fname);
            InputStream is = null;
            OutputStream os = null;
            try {
                is = imgFile.getInputStream();
                os = new FileOutputStream(newFile);
                byte buffer[] = new byte[1024];
                int cnt = 0;
                while ((cnt = is.read(buffer)) > 0) {
                    os.write(buffer, 0, cnt);
                }
            } finally {
                //关闭输入输出流
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            //解压缩zip文件
            String unzipdir = filedir + File.separator + IdGen.uuid();// 生成的文件所在目录
            FileUtil.unZipFiles(filedir + File.separator + fname, unzipdir);
            //将解压缩的文件按项目名称放入文件夹
            moveFileToDir(unzipdir);
            //上传到ftp
            uploadZipToFtp(ay, unzipdir);
        } finally {
            //FileUtil.deleteDirectory(filedir);
        }
    }

    //将解压缩的文件按项目名称放入文件夹
    private static void moveFileToDir(String filespath) throws Exception {
        File dir = new File(filespath);
        File[] fs = dir.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }
        for (File f : fs) {
            if (f.isDirectory()) {
                File[] sfs = f.listFiles();
                if (sfs == null || sfs.length == 0) {
                    continue;
                }

                for (File sff : sfs) {
                    if (sff.isDirectory()) {
                        continue;
                    }

                    String fname = sff.getName();
                    String proname = null;
                    try {
                        /**
                         * 判断文件名是否含有两个下划线分隔，如果有则为项目名。
                         */
                        int first = (fname).indexOf(StringUtil.LINE_D);
                        int last = (fname).indexOf(StringUtil.LINE_D, (first + 1));
                        if((first != -1) && (last != -1) && (first != last)){
                            proname = (fname).substring(first + 1, last);
                        }else{
                            proname = null;
                        }
//                        proname = fname.substring(fname.indexOf("_") + 1, fname.lastIndexOf("_"));
                    } catch (Exception e) {
                        logger.error(ExceptionUtil.getStackTrace(e)+" 文件名不能为空且文件名必须包含两个_！当前文件["+fname+"]");
                        proname = null;
                    }
                    if (proname == null) {
                        continue;
                    }
                    File tempPathDir = new File(filespath + StringUtil.LINE + proname + StringUtil.LINE);
                    if (!tempPathDir.exists()) {
                        tempPathDir.mkdirs();
                    }
                    sff.renameTo(new File(filespath + StringUtil.LINE + proname + StringUtil.LINE + fname));
                }
                continue;
            }
            String fname = f.getName();
            String proname = null;
            try {
                  /**
                  * 判断文件名是否含有两个下划线分隔，如果有则为项目名。
                  */
                 int first = (fname).indexOf(StringUtil.LINE_D);
                 int last = (fname).indexOf(StringUtil.LINE_D, (first + 1));
                 if((first != -1) && (last != -1) && (first != last)){
                     proname = (fname).substring(first + 1, last);
                 }else{
                     proname = null;
                 }
            } catch (Exception e) {
                logger.error(ExceptionUtil.getStackTrace(e) +" 文件名不能为空且文件名必须包含两个_！当前文件["+fname+"]");
                proname = null;
            }
            if (proname == null) {
                continue;
            }
            File tempPathDir = new File(filespath + StringUtil.LINE + proname + StringUtil.LINE);
            if (!tempPathDir.exists()) {
                tempPathDir.mkdirs();
            }
            f.renameTo(new File(filespath + StringUtil.LINE + proname + StringUtil.LINE + fname));
        }
    }

    //上传到ftp
    private static void uploadZipToFtp(ActYw ay, String filespath) throws Exception { ProProject pp = ay.getProProject();
        if (pp == null || StringUtil.isEmpty(pp.getProType()) || StringUtil.isEmpty(pp.getType())) {
            throw new ImpDataException("大赛配置信息错误");
        }
        File dir = new File(filespath);
        File[] fs = dir.listFiles();
        if (fs == null || fs.length == 0) {
            return;
        }
        String ftpfilepath = "/"+ TenantConfig.getCacheTenant()+tempProModelFilePath + pp.getProType() + pp.getType();
        File tempPathDir = new File(ftpfilepath + StringUtil.LINE);
        if (!tempPathDir.exists()) {
            tempPathDir.mkdirs();
        }
        for (File f : fs) {
            if (!f.isDirectory()) {
                continue;
            }
            File[] sfs = f.listFiles();
            if (sfs == null || sfs.length == 0) {
                continue;
            }
            String fmd5 = MD5Util.string2MD5(f.getName());
            for (File sf : sfs) {
                if (!sf.isDirectory()) {
                    String sname = sf.getName().substring(0, sf.getName().lastIndexOf("."));
                    String suffix = sf.getName().substring(sf.getName().lastIndexOf(".") + 1).toLowerCase();
                    String tempname = MD5Util.string2MD5(sname);
                    String sfmd5 = tempname + "." + suffix;
                    File stempPathDir = new File(ftpfilepath + StringUtil.LINE + fmd5 + StringUtil.LINE);
                    if (!stempPathDir.exists()) {
                        stempPathDir.mkdirs();
                    }
                    if (VsftpUtils.isFileExist(ftpfilepath + StringUtil.LINE + fmd5 + StringUtil.LINE + sfmd5)) {
                        VsftpUtils.removeFile(ftpfilepath + StringUtil.LINE + fmd5, sfmd5);
                    }
                    System.out.println("上传文件路径："+ftpfilepath + StringUtil.LINE + fmd5);
                    ftpfilepath = tempProModelFilePath + pp.getProType() + pp.getType();
                    VsftpUtils.uploadFile(ftpfilepath + StringUtil.LINE + fmd5, sfmd5, sf);
                    CacheUtils.put(CacheUtils.GcontestImpFile_CACHE + fmd5 + tempname, sf.getName());
                }
            }
        }
    }
}
