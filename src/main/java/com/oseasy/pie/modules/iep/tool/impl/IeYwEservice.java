/**
 * .
 */

package com.oseasy.pie.modules.iep.tool.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsEser;
import com.oseasy.pie.modules.iep.tool.IeAbsYw;
import com.oseasy.pie.modules.iep.tool.check.ItCparamGgj;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.pie.modules.iep.vo.TplStep;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ProModelErrorService;
import com.oseasy.pie.modules.impdata.tool.IitTplData;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelElement;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelHasFile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelLeaderName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelLeaderOffice;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectIntroduction;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectNumber;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectResult;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectType;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelProjectYear;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherTitle;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamEmail;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamMembers;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamMobile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamProfes;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamPm;
import com.oseasy.pie.modules.impdata.tool.data.TplCkCol;
import com.oseasy.pie.modules.impdata.tool.data.TplCkRows;
import com.oseasy.pie.modules.impdata.tool.data.TplGcontestGJ;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通用流程导入.
 * @author chenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class IeYwEservice extends IeAbsEser {
    public final static Logger logger = Logger.getLogger(IeYwEservice.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static ProModelDao proModelDao = SpringContextHolder.getBean(ProModelDao.class);
    private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
    private static ProModelErrorService proModelErrorService = SpringContextHolder.getBean(ProModelErrorService.class);
    private static ImpDataService impDataService = SpringContextHolder.getBean(ImpDataService.class);

    @Override
    public void checkTpl(IepTpl iepTpl, IeAbsYw yw) throws ImpDataException{
        if((TplType.GJ.getKey()).equals(iepTpl.getType())){
            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = (XSSFWorkbook)yw.getTpl().getWb();
                XSSFSheet sheet = null;
                List<TplCkRows> tckRows = Lists.newArrayList();
                if((TplStep.TS1.getKey() == iepTpl.getParent().getStep())){
                    sheet = wb.getSheetAt(0);

                    TplCkRows crow0 = new TplCkRows(0);
                    crow0.getCols().add(new TplCkCol(0, "项目名称"));
                    tckRows.add(crow0);

                    TplCkRows crow4 = new TplCkRows(4);
                    crow4.getCols().add(new TplCkCol(0, "项目负责人信息"));
                    tckRows.add(crow4);
                    IitTplData.genCheckExcept(sheet, tckRows);
                }else if((TplStep.TS2.getKey() == iepTpl.getParent().getStep())){
                    sheet = wb.getSheetAt(0);

                    TplCkRows crow = new TplCkRows(new TplGcontestGJ().headRow());
                    crow.getCols().add(new TplCkCol(0, "项目名称"));
                    crow.getCols().add(new TplCkCol(12, "项目简介"));
                    tckRows.add(crow);
                    IitTplData.genCheckExcept(sheet, tckRows);
                }else if((TplStep.TS3.getKey() == iepTpl.getParent().getStep())){
                    sheet = wb.getSheetAt(0);

                    TplCkRows crow = new TplCkRows(new TplGcontestGJ().headRow());
                    crow.getCols().add(new TplCkCol(0, "项目名称"));
                    crow.getCols().add(new TplCkCol(12, "项目简介"));
                    crow.getCols().add(new TplCkCol(13, "是否有附件"));
                    tckRows.add(crow);
                    IitTplData.genCheckExcept(sheet, tckRows);
                }


//                if((TplStep.TS1.getKey() == iepTpl.getParent().getStep())){
//                    sheet = wb.getSheetAt(0);
//                    if ((sheet == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    int headRow = 0;
//                    if ((sheet.getRow(headRow) == null) || (sheet.getRow(headRow).getCell(0) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR0Col = ExcelUtils.getStringByCell(sheet.getRow(headRow).getCell(0), sheet);
//                    if (!(sheeR0Col).equals("项目名称")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+headRow+")行列头("+sheeR0Col+")不等于(项目名称)！");
//                    }
//
//                    int headRowA = 4;
//                    if ((sheet.getRow(headRowA) == null) || (sheet.getRow(headRowA).getCell(0) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR4Col = ExcelUtils.getStringByCell(sheet.getRow(headRowA).getCell(0), sheet);
//                    if (!(sheeR4Col).equals("项目负责人信息")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+headRowA+")行列头("+sheeR4Col+")不等于(项目负责人信息)！");
//                    }
//                }else if((TplStep.TS2.getKey() == iepTpl.getParent().getStep())){
//                    sheet = wb.getSheetAt(0);
//                    if ((sheet == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//
//                    TplGcontestGJ tpl = new TplGcontestGJ();
//                    if ((sheet.getRow(tpl.headRow()) == null) || (sheet.getRow(tpl.headRow()).getCell(0) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR3C0 = ExcelUtils.getStringByCell(sheet.getRow(tpl.headRow()).getCell(0), sheet);
//                    if (!(sheeR3C0).equals("项目名称")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+tpl.headRow()+")行列头("+sheeR3C0+")不等于(项目名称)！");
//                    }
//
//                    if ((sheet.getRow(tpl.headRow()) == null) || (sheet.getRow(tpl.headRow()).getCell(12) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR3C12 = ExcelUtils.getStringByCell(sheet.getRow(tpl.headRow()).getCell(12), sheet);
//                    if (!(sheeR3C12).equals("项目简介")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+tpl.headRow()+")行列头("+sheeR3C12+")不等于(项目简介)！");
//                    }
//                }else if((TplStep.TS3.getKey() == iepTpl.getParent().getStep())){
//                    sheet = wb.getSheetAt(0);
//                    if ((sheet == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//
//                    TplGcontestGJ tpl = new TplGcontestGJ();
//                    if ((sheet.getRow(tpl.headRow()) == null) || (sheet.getRow(tpl.headRow()).getCell(0) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR3C0 = ExcelUtils.getStringByCell(sheet.getRow(tpl.headRow()).getCell(0), sheet);
//                    if (!(sheeR3C0).equals("项目名称")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+tpl.headRow()+")行列头("+sheeR3C0+")不等于(项目名称)！");
//                    }
//
//                    if ((sheet.getRow(tpl.headRow()) == null) || (sheet.getRow(tpl.headRow()).getCell(12) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致！");
//                    }
//                    String sheeR3C12 = ExcelUtils.getStringByCell(sheet.getRow(tpl.headRow()).getCell(12), sheet);
//                    if (!(sheeR3C12).equals("项目简介")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+tpl.headRow()+")行列头("+sheeR3C12+")不等于(项目简介)！");
//                    }
//
//                    if ((sheet.getRow(tpl.headRow()) == null) || (sheet.getRow(tpl.headRow()).getCell(13) == null)) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：模板不一致(是否有附件)列为空！");
//                    }
//                    String sheeR3C13 = ExcelUtils.getStringByCell(sheet.getRow(tpl.headRow()).getCell(13), sheet);
//                    if (!(sheeR3C13).equals("是否有附件")) {
//                        throw new POIXMLException("模板错误,请下载最新的模板,原因：第("+tpl.headRow()+")行列头("+sheeR3C13+")不等于(是否有附件)！");
//                    }
//                }
            }
        }
    }

    /**
     * 处理Sheet数据导入,及数据校验.
     * @param sheet Excel表
     * @param ii 导入信息
     * @param ay 业务
     * @param impVo 导入操作
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false)
    public void impData(IepTpl iepTpl, IeAbsYw yw, MultipartFile mpFile) throws Exception {
        if((TplType.MR.getKey()).equals(iepTpl.getType())){
            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){

                XSSFSheet sheet = null;
                if (yw.getTpl().getCur() instanceof XSSFSheet) {
                    sheet = (XSSFSheet) yw.getTpl().getCur();
                }else{
                    return;
                }

                ActYw ay = (ActYw)yw.getIeYw();
                ItCparamPm param = new ItCparamPm(sheet, impemsgService, yw.getRpparam().getIi(), new ImpInfoErrmsg(), ay);
                XSSFRow rowData;
                Office office = null;
                int fail = 0;// 失败数
                int success = 0;// 成功数
                // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
                for (int i = iepTpl.getDstartRow() + 1; i < sheet.getLastRowNum() + 1; i++) {
                    ProModelError validinfo = new ProModelError();// 用于保存处理之后的信息，以免再次查找数据库.
                    ProModelError phe = new ProModelError();
                    param.setTag(0);// 有几个错误字段
                    phe.setImpId(yw.getRpparam().getIi().getId());
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
                            val = StringUtil.trim(val);
                        }
                        param.setIdx(j);
                        param.setVal(val);
                        param.setRows(ImpDataService.descHeadRow);
                        new ItCkProModelProjectName(proModelService).validate(param, phe, validinfo);
                        new ItCkProModelProjectNumber(proModelDao).validate(param, phe, validinfo);
                        new ItCkProModelProjectType().validate(param, phe, validinfo);
                        new ItCkProModelLeaderName().validate(param, phe, validinfo);
                        new ItCkProModelLeaderOffice().validate(param, phe, validinfo);

                        new ItCkProModelTeamNo(userService).validate(param, phe, validinfo);
                        new ItCkProModelTeamMobile(userService).validate(param, phe, validinfo);
                        new ItCkProModelTeamEmail().validate(param, phe, validinfo);
                        new ItCkProModelTeamProfes(phe.getCurOffice()).validate(param, phe, validinfo);
                        new ItCkProModelTeamMembers(userService).validate(param, phe, validinfo);
                        new ItCkProModelTeacherName(rowData).validate(param, phe, validinfo);
                        new ItCkProModelTeacherNo(userService).validate(param, phe, validinfo);
                        new ItCkProModelTeacherTitle(userService).validate(param, phe, validinfo);
                        new ItCkProModelProjectYear().validate(param, phe, validinfo);
                        new ItCkProModelProjectResult().validate(param, phe, validinfo);
                        new ItCkProModelHasFile().validate(param, phe, validinfo);
                        new ItCkProModelProjectIntroduction().validate(param, phe, validinfo);

                        new ItCkProModelElement("团队ID", "teamId").validate(param, phe, validinfo);
                        new ItCkProModelElement("团队名称", "teamName").validate(param, phe, validinfo);
                        new ItCkProModelElement("级别/组别", "level").validate(param, phe, validinfo);
                        new ItCkProModelElement("项目简称", "introduction").validate(param, phe, validinfo);
                        new ItCkProModelElement("项目阶段", "stage").validate(param, phe, validinfo);
                        new ItCkProModelElement("项目来源", "projectSource").validate(param, phe, validinfo);
                    }
                    if (param.getTag() != 0) {// 有错误字段,记录错误信息
                        fail++;
                        proModelErrorService.insert(phe);
                    } else {// 无错误字段，保存信息
                        try {
                            //TODO CHENHAO
                            proModelErrorService.saveProject(validinfo, ay, new ItOper(yw.getRequest()));
                            success++;
                        } catch (Exception e) {
                            logger.error("保存项目信息出错", e);
                            fail++;
                            proModelErrorService.insert(phe);
                        }
                    }
                    yw.getRpparam().getIi().setFail(fail + "");
                    yw.getRpparam().getIi().setSuccess(success + "");
                    yw.getRpparam().getIi().setTotal((fail + success) + "");
                    CacheUtils.put(CacheUtils.IMPDATA_CACHE, yw.getRpparam().getIi().getId(), yw.getRpparam().getIi());
                }
                yw.getRpparam().getIi().setIsComplete(Const.YES);
                impservice.save(yw.getRpparam().getIi());
                CacheUtils.remove(CacheUtils.IMPDATA_CACHE, yw.getRpparam().getIi().getId());
            }else if((TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
            }else{
                logger.warn("模板后缀不匹配！");
            }
        }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFSheet sheet = null;
                if (yw.getTpl().getCur() instanceof XSSFSheet) {
                    sheet = (XSSFSheet) yw.getTpl().getCur();
                }else{
                    return;
                }

                ActYw ay = (ActYw)yw.getIeYw();
                ItCparamGgj param = new ItCparamGgj(sheet, impemsgService, yw.getRpparam().getIi(), new ImpInfoErrmsg(), ay);
                ItOper iepVo = new ItOper(yw.getRequest());
                iepVo.setIsImpFirstData(true);
                if((TplStep.TS1.getKey() == iepTpl.getParent().getStep())){
                    iepVo.setIsFirstTask(true);
                    iepVo.setIsImpFileData(false);
                    iepVo.setHasFile(iepTpl.getHasFile());
                    impDataService.importNewData(param, (ActYw)yw.getIeYw(), (XSSFWorkbook)yw.getTpl().getWb(), yw.getRpparam().getIi(), iepVo);
                    entityService.updateSteps(iepTpl.getParent());
                }else if((TplStep.TS2.getKey() == iepTpl.getParent().getStep())){
                    iepVo.setIsFirstTask(false);
                    iepVo.setIsImpFileData(true);
                    iepVo.setHasFile(iepTpl.getHasFile());
                    TplGcontestGJ itTplData = new TplGcontestGJ();
                    itTplData.impDataFile(sheet, yw.getRpparam().getIi(), (ActYw)yw.getIeYw(), iepVo);
                    entityService.updateSteps(iepTpl.getParent());
                }else if((TplStep.TS3.getKey() == iepTpl.getParent().getStep())){
                    iepVo.setIsFirstTask(false);
                    iepVo.setIsImpFileData(true);
                    iepVo.setHasFile(iepTpl.getHasFile());
                    TplGcontestGJ itTplData = new TplGcontestGJ();
                    itTplData.impDataFile(sheet, yw.getRpparam().getIi(), (ActYw)yw.getIeYw(), iepVo);
                    entityService.updateSteps(iepTpl.getParent());
                }else if((TplStep.TS4.getKey() == iepTpl.getParent().getStep())){
                }
            }else{
                logger.warn("模板后缀不匹配！");
            }
        }
    }
}
