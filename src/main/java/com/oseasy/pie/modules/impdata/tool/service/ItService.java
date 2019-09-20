/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowProjectType;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.common.config.PieSval;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.iep.tool.IeIdxVos;
import com.oseasy.pie.modules.iep.tool.idx.IdxPm;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.service.ProModelErrorService;
import com.oseasy.pie.modules.impdata.tool.IitDownTpl;
import com.oseasy.pie.modules.impdata.tool.IitService;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelEleDate;
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
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherNameReq;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherNoReq;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeacherTitle;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamEmail;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamMembers;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamMobile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkProModelTeamProfes;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamPm;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProModelGcontest;
import com.oseasy.pie.modules.impdata.tool.down.ItDnProModelPro;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.pie.modules.impdata.tool.param.ItParam;
import com.oseasy.pie.modules.impdata.tool.param.ItSupparam;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXsheet;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXworkbook;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.pro.modules.workflow.entity.ProModelTlxy;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 通用流程导入.
 * @author chenhao
 *
 */
@Transactional(readOnly = true)
public class ItService extends IitAbsService implements IitService<ItSupparam> {
    public final static Logger logger = Logger.getLogger(ItService.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static ProModelDao proModelDao = SpringContextHolder.getBean(ProModelDao.class);
    private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
    private static ProModelErrorService proModelErrorService = SpringContextHolder.getBean(ProModelErrorService.class);

    @Override
    public void initItIdxVos() {
        idxVos.add(new ItIdxVo(0, "office"));
        idxVos.add(new ItIdxVo(1, "name"));
        idxVos.add(new ItIdxVo(2, "number"));
        idxVos.add(new ItIdxVo(3, "type"));
        idxVos.add(new ItIdxVo(4, "leader"));
        idxVos.add(new ItIdxVo(5, "no"));
        idxVos.add(new ItIdxVo(6, "team_id"));
        idxVos.add(new ItIdxVo(7, "team_name"));
        idxVos.add(new ItIdxVo(8, "mobile"));
        idxVos.add(new ItIdxVo(9, "email"));
        idxVos.add(new ItIdxVo(10, "profes"));
        idxVos.add(new ItIdxVo(11, "members"));
        idxVos.add(new ItIdxVo(12, "teachers"));
        idxVos.add(new ItIdxVo(13, "tea_no"));
        idxVos.add(new ItIdxVo(14, "tea_title"));
        idxVos.add(new ItIdxVo(15, "year"));
        idxVos.add(new ItIdxVo(16, "result"));
        idxVos.add(new ItIdxVo(17, "hasfile"));
        idxVos.add(new ItIdxVo(18, "introduction"));
        idxVos.add(new ItIdxVo(19, "level"));
        idxVos.add(new ItIdxVo(20, "short_name"));
        idxVos.add(new ItIdxVo(21, "stage"));
        idxVos.add(new ItIdxVo(22, "source"));

        idxVos.add(new ItIdxVo(23, "resultType"));
        idxVos.add(new ItIdxVo(24, "resultContent"));
        idxVos.add(new ItIdxVo(25, "budgetDollar"));
        idxVos.add(new ItIdxVo(26, "budget"));
        idxVos.add(new ItIdxVo(27, "innovation"));
        idxVos.add(new ItIdxVo(28, "planStep"));
        idxVos.add(new ItIdxVo(29, "planContent"));
        idxVos.add(new ItIdxVo(30, "planStartDate"));
        idxVos.add(new ItIdxVo(31, "planEndDate"));
    }

    @Override
    public void uploadFtp(ItSupparam param, MultipartFile mpFile) {
        //上传附件
        try {
            ItParam curparam = (ItParam) param;
            ImpDataService.uploadZip(curparam.getActYw(), mpFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("resource")
    @Override
    public void downTpl(ActYw actyw, HttpServletResponse response, String rootPath, String key) {
        IitAbsTpl absTpl = IitTpl.getTplByActYw(actyw, rootPath, key);
        if(absTpl == null){
            return;
        }

        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= response.getOutputStream();
            fs = PieExcelUtils.setExcelHeader(response, absTpl);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);

            FlowProjectType fpType =  actyw.getFptype();
            if((fpType == null)){
                logger.warn("模板类型未定义，请检查参数！");
                return;
            }

            // excel模板路径
            IitDownTpl downTpl = null;
            if((FlowProjectType.PMT_XM).equals(fpType)){
                downTpl = new ItDnProModelPro(actyw);
                downTpl.setBody(wb, sheet);
            }else if((FlowProjectType.PMT_DASAI).equals(fpType)){
                downTpl = new ItDnProModelGcontest(actyw);
                downTpl.setBody(wb, sheet);
            }else{
                logger.info("当前下载的模板类型未定义！");
            }
            out = response.getOutputStream();
            new XSSFWorkbook(fs).write(out);
        } catch (Exception e) {
            logger.error("当前模板文件不存在：["+absTpl.getRootPath() +"/"+ absTpl.getTplName()+"]" + e);
        } finally {
            try {
                if (out!=null)out.close();
                if (fs!=null)fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    @Override
    public IitAbsTpl getTpl(IitAbsTpl tpl, String key, String tplName, String fileName) {
        if(StringUtil.isEmpty(tpl.getRootPath()) || StringUtil.isEmpty(tplName)){
            logger.warn("模板根路径RootPath或tplName不能为空！");
            return tpl;
        }

        if(StringUtil.isNotEmpty(key)){
            tplName += StringUtil.LINE_D + key;
        }

        IitAbsTpl absTpl = null;
        if((tpl instanceof ItTplXsheet)){
            absTpl = new ItTplXsheet();
            tplName += StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX;
            if(StringUtil.isNotEmpty(fileName)){
                absTpl.setFname(fileName + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX);
            }else{
                absTpl.setFname("项目" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX);
            }
        }else if((tpl instanceof ItTplXsheet)){
            absTpl = new ItTplXworkbook();
            tplName += StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS;
            if(StringUtil.isNotEmpty(fileName)){
                absTpl.setFname(fileName + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS);
            }else{
                absTpl.setFname("项目" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS);
            }
        }else{
            logger.warn("模板类型不匹配");
            return null;
        }
        absTpl.setTplName(tplName);
        absTpl.setRootPath(tpl.getRootPath());
        return absTpl;
    }

    @Override
    public void checkTpl(IitTpl<?> tpl, HttpServletRequest request) {
        if((FileUtil.SUFFIX_EXCEL_XLSX).equals(tpl.getFileType())){
            ItTplXsheet xsheetTpl = (ItTplXsheet) tpl;
            if((xsheetTpl == null) || (xsheetTpl.getFile() == null)){
                return;
            }

            String sheetname = xsheetTpl.getFile().getSheetName();
            FileInputStream fs = null;
            try {
                File fi = new File(xsheetTpl.getRootPath() + StringUtil.LINE + xsheetTpl.getTplName());
                fs = new FileInputStream(fi);
                // 读取了模板内所有sheet内容
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                XSSFSheet sheet = wb.getSheetAt(0);
                if (!sheet.getSheetName().equals(sheetname)) {
                    throw new ImpDataException("模板错误,请下载最新的模板,原因：SheetName不一致("+sheet.getSheetName()+")不等于("+sheetname+")！");
                }
                String sheetColName = "";
                String sheetplColName = "";
                for (int j = 0; j < sheet.getRow(ImpDataService.descHeadRow).getLastCellNum(); j++) {
                    sheetColName = ExcelUtils.getStringByCell(sheet.getRow(ImpDataService.descHeadRow).getCell(j), sheet);
                    sheetplColName = ExcelUtils.getStringByCell(xsheetTpl.getFile().getRow(ImpDataService.descHeadRow).getCell(j), sheet);
                    if (!(sheetColName).equals(sheetplColName)) {
                        throw new ImpDataException("模板错误,请下载最新的模板,原因：列头不一致("+sheetColName+")不等于("+sheetplColName+")！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fs != null)
                        fs.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }else if((FileUtil.SUFFIX_DOC).equals(tpl.getFileType()) || (FileUtil.SUFFIX_DOCX).equals(tpl.getFileType())){
            //ItTplXworkbook xworkbook = (ItTplXworkbook) param.getTpl();
            //TODO
        }else{
            logger.warn("模板文件类型未定义！");
        }
    }

    /**
     * 单个文件处理.
     */
    @Override
    @Transactional(readOnly = false)
    public void impData(ItSupparam param, MultipartFile mpFile, HttpServletRequest request) throws Exception{
        if(param.getTpl() == null){
            return;
        }

        ItParam curparam = (ItParam) param;
        if((XSSFWorkbookType.XLSX.getExtension()).equals(curparam.getTpl().getFileType())){
            ItTplXsheet xsheetTpl = (ItTplXsheet) curparam.getTpl();
            if((xsheetTpl == null) || (xsheetTpl.getFile() == null)){
                return;
            }
            importProModel(xsheetTpl.getFile(), curparam.getIi(), curparam.getActYw(), curparam.getOper());
        }else if((XSSFWorkbookType.XLSM.getExtension()).equals(curparam.getTpl().getFileType())){
            ItTplXworkbook xsheetTpl = (ItTplXworkbook) curparam.getTpl();
            if((xsheetTpl == null) || (xsheetTpl.getFile() == null)){
                return;
            }
            //TODO CHENHAO
            //importProModel(xsheetTpl.getFile(), curparam.getIi(), curparam.getActYw(), curparam.getOper());
        }else{
            logger.warn("模板后缀不匹配！");
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
    @Transactional(readOnly = false)
    private void importProModel(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) throws Exception {
        ItCparamPm param = new ItCparamPm(sheet, impemsgService, ii, new ImpInfoErrmsg(), ay);
        XSSFRow rowData;
        Office office = null;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            ProModelError validinfo = new ProModelError();// 用于保存处理之后的信息，以免再次查找数据库.
            ProModelError phe = new ProModelError();
            param.setTag(0);// 有几个错误字段
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
                new ItCkProModelTeacherTitle(userService).validate(param, phe, validinfo);
                new ItCkProModelProjectYear().validate(param, phe, validinfo);
                new ItCkProModelProjectResult().validate(param, phe, validinfo);
                new ItCkProModelHasFile().validate(param, phe, validinfo);
                new ItCkProModelProjectIntroduction().validate(param, phe, validinfo);
                if((FormTheme.F_TLXY).equals(ay.getFtheme())){
                    new ItCkProModelTeacherNameReq(rowData).validate(param, phe, validinfo);
                    new ItCkProModelTeacherNoReq(userService).validate(param, phe, validinfo);
                }else{
                    new ItCkProModelTeacherName(rowData).validate(param, phe, validinfo);
                    new ItCkProModelTeacherNo(userService).validate(param, phe, validinfo);
                }
                new ItCkProModelElement("团队ID", "teamId").validate(param, phe, validinfo);
                new ItCkProModelElement("团队名称", "teamName").validate(param, phe, validinfo);
                new ItCkProModelElement("级别/组别", "level").validate(param, phe, validinfo);
                new ItCkProModelElement("项目简称", "introduction").validate(param, phe, validinfo);
                new ItCkProModelElement("项目阶段", "stage").validate(param, phe, validinfo);
                new ItCkProModelElement("项目来源", "projectSource").validate(param, phe, validinfo);

                if((FormTheme.F_TLXY).equals(ay.getFtheme())){
                    if(phe.getPmTlxy() == null){
                        phe.setPmTlxy(new ProModelTlxy());
                    }
                    if(validinfo.getPmTlxy() == null){
                        validinfo.setPmTlxy(new ProModelTlxy());
                    }
                    new ItCkProModelElement("项目来源", validinfo.getPmTlxy(), "source").validate(param, phe, validinfo);
                    new ItCkProModelElement("成果形式", validinfo.getPmTlxy(), "resultType").validate(param, phe, validinfo);
                    new ItCkProModelElement("成果说明", validinfo.getPmTlxy(), "resultContent", 7000, 0).validate(param, phe, validinfo);
                    new ItCkProModelElement("项目经费预算", validinfo.getPmTlxy(), "budgetDollar").validate(param, phe, validinfo);
                    new ItCkProModelElement("经费预算明细", validinfo.getPmTlxy(), "budget", 7000, 0).validate(param, phe, validinfo);
                    new ItCkProModelElement("前期调研准备", validinfo.getPmTlxy(), "innovation", 7000, 0).validate(param, phe, validinfo);
                    new ItCkProModelElement("保障措施", validinfo.getPmTlxy(), "planStep", 7000, 0).validate(param, phe, validinfo);
                    new ItCkProModelElement("实施预案", validinfo.getPmTlxy(), "planContent", 7000, 0).validate(param, phe, validinfo);
                    new ItCkProModelEleDate("项目预案时间起始", validinfo.getPmTlxy(), "planStartDate", DateUtil.FMT_YYYYMMDD_HHmmss_ZG).validate(param, phe, validinfo);
                    new ItCkProModelEleDate("项目预案时间截止", validinfo.getPmTlxy(), "planEndDate", DateUtil.FMT_YYYYMMDD_HHmmss_ZG).validate(param, phe, validinfo);
                }
            }
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
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
        impservice.save(ii);
        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
    }

    @Override
    public ApiTstatus<?> deleteImpInfo(ImpInfo impInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void expErrorData(ImpInfo impInfo, String tplName, String fileName, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(impInfo.getId()) || StringUtil.isEmpty(fileName)){
            logger.error("导入信息ID或fileName不能为空！");
        }

        List<ItIdxVo> idxVos = new IdxPm().initIdxVos();
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            ItTplXsheet xsheet = new ItTplXsheet();
            xsheet.setRootPath(SpringContextHolder.getWebPath(request, PieSval.ROOT_IMP));
            fs = PieExcelUtils.setExcelHeader(response, getTpl(xsheet, null, tplName, fileName));
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            CreationHelper factory = wb.getCreationHelper();
            ClientAnchor anchor = factory.createClientAnchor();
            Drawing drawing = sheet.createDrawingPatriarch();
            List<Map<String, String>> list= proModelErrorService.getListByImpId(impInfo.getId());
            setExcelHead(sheet);
            if (!list.isEmpty()) {
                initItIdxVos();
                Map<String, Integer> rowIndex=new HashMap<String, Integer>();
                // 在相应的单元格进行赋值
                for(int i=0;i<list.size();i++) {
                    Map<String, String> map=list.get(i);
                    XSSFRow row = sheet.createRow(i + 1 + getExcelHeadRow());
                    rowIndex.put(map.get("id"), i + 1 + getExcelHeadRow());
                    for(String key : map.keySet()) {
//                        Integer cindex = super.getIdx(key);
                        Integer cindex = IeIdxVos.getIdx(idxVos, key);
                        if (cindex != null) {
                            XSSFCell cell = row.createCell(cindex);
                            Object obj = map.get(key);
                            if(obj instanceof Date || (obj) instanceof Timestamp){
                                DateFormat fmt = new SimpleDateFormat(DateUtil.FMT_YYYYMMDD_HHmmss_ZG);
                                cell.setCellValue(fmt.format(obj));
                            }else{
                                cell.setCellValue(map.get(key));
                            }
                        }
                    }
                }
                List<Map<String, String>> errlist= impemsgService.getListByImpId(impInfo.getId());
                if (!errlist.isEmpty()) {
                    for(Map<String, String> errmap:errlist) {
                        Comment comment0 = drawing.createCellComment(anchor);
                        RichTextString str0 = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
                        XSSFFont commentFormatter = wb.createFont();
                        str0.applyFont(commentFormatter);
                        comment0.setString(str0);
                        if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
                        }else{
                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment0);
                        }
                    }
                }
            }
            out = response.getOutputStream();
            wb.write(out);
        }   catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (out!=null)out.close();
                if (fs!=null)fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }

    }

    @Override
    public void getExpErrorDataFile(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setExcelHead(XSSFSheet sheet) {
        // TODO Auto-generated method stub
    }

    @Override
    public int getExcelHeadRow() {
        return 3;
    }
}
