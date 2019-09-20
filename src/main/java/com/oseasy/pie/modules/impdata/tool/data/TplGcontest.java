/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.common.utils.poi.MergedResult;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.pie.modules.impdata.service.ImpInfoService;
import com.oseasy.pie.modules.impdata.service.ProModelGcontestErrorService;
import com.oseasy.pie.modules.impdata.tool.IitTplData;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelCardNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelElement;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelEnter;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelGtgroups;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelGttype;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelHasFile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelLeaderName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelOut;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectIntroduction;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectYear;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamEducation;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamEmail;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamMobile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamProfes;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamPm;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通用大赛数据模板.
 * @author chenhao
 *
 */
public class TplGcontest implements IitTplData{
    public final static Logger logger = Logger.getLogger(TplGcontest.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static ImpInfoService impInfoService = SpringContextHolder.getBean(ImpInfoService.class);
    private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
    private static ImpInfoErrmsgService impInfoErrmsgService = SpringContextHolder.getBean(ImpInfoErrmsgService.class);
    private static ProModelGcontestErrorService proModelGcontestErrorService = SpringContextHolder.getBean(ProModelGcontestErrorService.class);

    @Override
    public int headRow() {
        return ImpDataService.descHeadRow;
    }

    @Override
    public int dataStartRow() {
        return ImpDataService.descHeadRow + 2;
    }

    @Override
    public void checkTpl(ItOper impVo, XSSFSheet datasheet, HttpServletRequest request) {
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
     * 处理国家互联网+大赛附件数据和附件存储.
     */
    @Override
    public void impData(ActYw ay, MultipartFile imgFile, HttpServletRequest request, ItOper impVo) {
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
            checkTpl(impVo, sheet, request);// 检查模板版本
            ii.setTotal((sheet.getLastRowNum() - ImpDataService.descHeadRow) + "");
            impInfoService.save(ii);// 插入导入信息
            ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        impDataFile(sheet, ii, ay, impVo);
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

    @Override
    public void impDataFile(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) throws Exception{
        ItCparamPm param = new ItCparamPm(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), ay);
        Office office = null;
        Office profes = null;
        XSSFRow rowData;
        ImpInfoErrmsg iie;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        int megRows = 1;//合并的行数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = dataStartRow(); i <= sheet.getLastRowNum(); ) {
            MergedResult mr = ExcelUtils.isMergedRegion(sheet, i, 0);//判断是否合并行
            if (mr.isMerged()) {
                megRows = mr.getEndRow() - mr.getStartRow() + 1;
            } else {
                megRows = 1;
            }
            param.setTag(0);// 有几个错误字段
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
            for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }
                param.setIdx(j);
                param.setVal(val);
                param.setRows(ImpDataService.descHeadRow);

                param = new ItCkProModelProjectName(proModelService).validate(param, phe, validinfo);
                param = new ItCkProModelProjectYear().validate(param, phe, validinfo);
                param = new ItCkProModelElement("项目阶段", "stage", 50, null).validate(param, phe, validinfo);
                param = new ItCkProModelGttype().validate(param, phe, validinfo);
                param = new ItCkProModelGtgroups().validate(param, phe, validinfo);
                param = new ItCkProModelProjectIntroduction().validate(param, phe, validinfo);
                param = new ItCkProModelHasFile().validate(param, phe, validinfo);
                param = new ItCkProModelLeaderName().validate(param, phe, validinfo);
                param = new ItCkProModelTeamNo(userService).validate(param, phe, validinfo);
                param = new ItCkProModelTeamProfes(null).validate(param, phe, validinfo);
                param = new ItCkProModelTeamMobile(userService).validate(param, phe, validinfo);
                param = new ItCkProModelTeamEmail().validate(param, phe, validinfo);
                param = new ItCkProModelEnter(DateUtil.FMT_YYYY).validate(param, phe, validinfo);
                param = new ItCkProModelOut(DateUtil.FMT_YYYY).validate(param, phe, validinfo);
                param = new ItCkProModelTeamEducation().validate(param, phe, validinfo);
                param = new ItCkProModelCardNo().validate(param, phe, validinfo);
            }
            //处理合并行数据end

//            //处理组成员数据
//            for (int k = i; k < i + megRows; k++) {
//                PmgMemsError pme = new PmgMemsError();
//                PmgMemsError vpme = new PmgMemsError();// 用于保存处理之后的信息，以免再次查找数据库.
//                pme.setSort(k + "");
//                pme.setPmgeId(phe.getId());
//                pme.setImpId(ii.getId());
//                pme.setId(IdGen.uuid());
//                rowData = sheet.getRow(k);
//                /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
//                int svalidcell = 0;
//                for (int j = 16; j <= 24; j++) {
//                    if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
//                        svalidcell++;
//                        break;
//                    }
//                }
//                if (svalidcell == 0) {
//                    continue;
//                }
//                for (int j = 16; j <= 24; j++) {
//                    String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
//                    if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setName(val);
//                        vpme.setName(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (StringUtil.isEmpty(val)) {
//                            tag++;
//                            iie.setErrmsg("必填信息");
//                        } else if (val.length() > 100) {
//                            tag++;
//                            iie.setErrmsg("最多100个字符");
//                            pme.setName(null);
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("学号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setNo(val);
//                        vpme.setNo(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (StringUtil.isEmpty(val)) {
//                            tag++;
//                            iie.setErrmsg("必填信息");
//                        } else if (val.length() > 100) {
//                            tag++;
//                            iie.setErrmsg("最多100个字符");
//                            pme.setNo(null);
//                        } else {
//                            User u = userService.getByNo(val);
//                            if (u != null && !UserUtils.checkHasRole(u, RoleBizTypeEnum.XS)) {
//                                tag++;
//                                iie.setErrmsg("找到该学号人员，但不是学生");
//                            } else if (u != null && pme.getName() != null && !pme.getName().equals(u.getName())) {
//                                tag++;
//                                iie.setErrmsg("学号和姓名不一致");
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("专业".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        profes = null;
//                        pme.setProfes(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (StringUtil.isEmpty(val)) {
//                            tag++;
//                            iie.setErrmsg("必填信息");
//                        } else if ((profes = OfficeUtils.getProfessionalByName(val)) == null) {
//                            tag++;
//                            iie.setErrmsg("专业不存在");
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        } else {
//                            if (profes != null) {
//                                vpme.setProfes(profes.getId());
//                            }
//                        }
//                    } else if ("入学年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setEnter(val);
//                        vpme.setEnter(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!ImpDataService.checkYear(val)) {
//                                tag++;
//                                iie.setErrmsg("入学年份格式不正确");
//                            } else if (val.length() > 4) {
//                                tag++;
//                                iie.setErrmsg("最多4个字符");
//                                pme.setEnter(null);
//                            }
//                        }
//
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("毕业年份".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setOut(val);
//                        vpme.setOut(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!ImpDataService.checkYear(val)) {
//                                tag++;
//                                iie.setErrmsg("毕业年份格式不正确");
//                            } else if (val.length() > 4) {
//                                tag++;
//                                iie.setErrmsg("最多4个字符");
//                                pme.setOut(null);
//                            } else if (!ImpDataService.checkOutYear(phe.getEnter(), phe.getOut())) {
//                                tag++;
//                                iie.setErrmsg("毕业年份需大于入学年份");
//                            }
//                        }
//
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("学历".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        Dict d = null;
//                        pme.setXueli(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if ((d = DictUtils.getDictByLabel("enducation_level", val)) == null) {
//                                tag++;
//                                iie.setErrmsg("学历不存在");
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        } else {
//                            if (d != null) {
//                                vpme.setXueli(d.getValue());
//                            }
//                        }
//                    } else if ("身份证号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setIdnum(val);
//                        vpme.setIdnum(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!Pattern.matches(RegexUtils.idNumRegex, val)) {
//                                tag++;
//                                iie.setErrmsg("身份证号码格式不正确");
//                            } else if (val.length() > 32) {
//                                tag++;
//                                iie.setErrmsg("最多32个字符");
//                                pme.setIdnum(null);
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("手机号码".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setMobile(val);
//                        vpme.setMobile(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
//                                tag++;
//                                iie.setErrmsg("手机号码格式不正确");
//                            } else {
//                                User u = new User();
//                                u.setMobile(val);
//                                User temu = userService.getByMobile(u);
//                                if (temu != null && pme.getNo() != null && !pme.getNo().equals(temu.getNo())) {
//                                    tag++;
//                                    iie.setErrmsg("手机号码已存在");
//                                }
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setEmail(val);
//                        vpme.setEmail(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!Pattern.matches(RegexUtils.emailRegex, val)) {
//                                tag++;
//                                iie.setErrmsg("邮箱格式不正确");
//                            } else if (val.length() > 128) {
//                                tag++;
//                                iie.setErrmsg("最多128个字符");
//                                pme.setEmail(null);
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    }
//                }
//                phe.getPmes().add(pme);
//                validinfo.getPmes().add(vpme);
//            }
//            //处理组成员数据end
//
//            //处理导师数据
//            for (int k = i; k < i + megRows; k++) {
//                PmgTeasError pme = new PmgTeasError();
//                PmgTeasError vpme = new PmgTeasError();// 用于保存处理之后的信息，以免再次查找数据库.
//                pme.setSort(k + "");
//                pme.setPmgeId(phe.getId());
//                pme.setImpId(ii.getId());
//                pme.setId(IdGen.uuid());
//                rowData = sheet.getRow(k);
//                /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
//                int svalidcell = 0;
//                for (int j = 25; j <= 30; j++) {
//                    if (!StringUtil.isEmpty(ExcelUtils.getStringByCell(rowData.getCell(j), sheet))) {
//                        svalidcell++;
//                        break;
//                    }
//                }
//                if (svalidcell == 0) {
//                    continue;
//                }
//                for (int j = 25; j <= 30; j++) {
//                    String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
//                    if ("姓名".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setName(val);
//                        vpme.setName(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (StringUtil.isEmpty(val)) {
//                            tag++;
//                            iie.setErrmsg("必填信息");
//                        } else if (val.length() > 100) {
//                            tag++;
//                            iie.setErrmsg("最多100个字符");
//                            pme.setName(null);
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("工号".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setNo(val);
//                        vpme.setNo(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (StringUtil.isEmpty(val)) {
//                            tag++;
//                            iie.setErrmsg("必填信息");
//                        } else if (val.length() > 100) {
//                            tag++;
//                            iie.setErrmsg("最多100个字符");
//                            pme.setNo(null);
//                        } else {
//                            User u = userService.getByNo(val);
//                            if (u != null && !UserUtils.checkHasRole(u, RoleBizTypeEnum.DS)) {
//                                tag++;
//                                iie.setErrmsg("找到该工号人员，但不是导师");
//                            } else if (u != null && pme.getName() != null && !pme.getName().equals(u.getName())) {
//                                tag++;
//                                iie.setErrmsg("工号和姓名不一致");
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("学院".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        office = null;
//                        pme.setOffice(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if ((office = OfficeUtils.getOfficeByName(val)) == null) {
//                                tag++;
//                                iie.setErrmsg("学院不存在");
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        } else {
//                            if (office != null) {
//                                vpme.setOffice(office.getId());
//                            }
//                        }
//                    } else if ("手机".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setMobile(val);
//                        vpme.setMobile(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!Pattern.matches(RegexUtils.mobileRegex, val)) {
//                                tag++;
//                                iie.setErrmsg("手机号码格式不正确");
//                            } else {
//                                User u = new User();
//                                u.setMobile(val);
//                                User temu = userService.getByMobile(u);
//                                if (temu != null && pme.getNo() != null && !pme.getNo().equals(temu.getNo())) {
//                                    tag++;
//                                    iie.setErrmsg("手机号码已存在");
//                                }
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("邮箱".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setEmail(val);
//                        vpme.setEmail(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (!Pattern.matches(RegexUtils.emailRegex, val)) {
//                                tag++;
//                                iie.setErrmsg("邮箱格式不正确");
//                            } else if (val.length() > 128) {
//                                tag++;
//                                iie.setErrmsg("最多128个字符");
//                                pme.setEmail(null);
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    } else if ("职称".equals(ExcelUtils.getStringByCell(sheet.getRow(ImpDataController.descHeadRow + 1).getCell(j), sheet))) {
//                        pme.setZhicheng(val);
//                        vpme.setZhicheng(val);
//                        iie = new ImpInfoErrmsg();
//                        iie.setImpId(ii.getId());
//                        iie.setDataId(phe.getId());
//                        iie.setDataSubId(pme.getId());
//                        iie.setColname(j + "");
//                        if (!StringUtil.isEmpty(val)) {
//                            if (val.length() > 100) {
//                                tag++;
//                                iie.setErrmsg("最多100个字符");
//                                pme.setZhicheng(null);
//                            }
//                        }
//                        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//                            impInfoErrmsgService.save(iie);
//                        }
//                    }
//                }
//                phe.getPtes().add(pme);
//                validinfo.getPtes().add(vpme);
//            }
            //处理导师数据end
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
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

    @Override
    public void expDataError(String id, ActYw ay, HttpServletRequest request, ItOper impVo) {
        // TODO Auto-generated method stub

    }

}
