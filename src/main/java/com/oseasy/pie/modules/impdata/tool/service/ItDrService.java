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
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.dr.modules.dr.service.DrCardService;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.expdata.service.DrCardErrorService;
import com.oseasy.pie.modules.iep.tool.IeIdxVos;
import com.oseasy.pie.modules.iep.tool.idx.IdxDr;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitService;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pie.modules.impdata.tool.check.ItCkDrCardExpire;
import com.oseasy.pie.modules.impdata.tool.check.ItCkDrCardNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkDrCardRepeat;
import com.oseasy.pie.modules.impdata.tool.check.ItCkDrEleDate;
import com.oseasy.pie.modules.impdata.tool.check.ItCkDrElement;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserClzss;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserOfficeXy;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserOfficeZy;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserProfessional;
import com.oseasy.pie.modules.impdata.tool.check.ItCkUserSex;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamDr;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.pie.modules.impdata.tool.param.ItParamDrCard;
import com.oseasy.pie.modules.impdata.tool.param.ItSupparam;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXsheet;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXworkbook;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.FileUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡批量导入.
 * @author chenhao
 *
 */
//@Service("itDrService")
public class ItDrService extends IitAbsService implements IitService<ItSupparam> {
    public final static Logger logger = Logger.getLogger(ItDrService.class);
    public final static String TPL_EXCEL_DR_ROOT = File.separator + "static" + File.separator + "excel" + File.separator + "dr" + File.separator;

    @Autowired
    private DrCardErrorService drCardErrorService;
    @Autowired
    private DrCardService drCardService;


    @Override
    public void initItIdxVos() {
        idxVos.add(new ItIdxVo(0, "login_name"));
        idxVos.add(new ItIdxVo(1, "name"));
        idxVos.add(new ItIdxVo(2, "no"));
        idxVos.add(new ItIdxVo(3, "mobile"));
        idxVos.add(new ItIdxVo(4, "email"));
        idxVos.add(new ItIdxVo(5, "remarks"));
        idxVos.add(new ItIdxVo(6, "birthday"));
        idxVos.add(new ItIdxVo(7, "id_type"));
        idxVos.add(new ItIdxVo(8, "id_no"));
        idxVos.add(new ItIdxVo(9, "tmp_sex"));
        idxVos.add(new ItIdxVo(10, "domain"));
        idxVos.add(new ItIdxVo(11, "degree"));
        idxVos.add(new ItIdxVo(12, "education"));
        idxVos.add(new ItIdxVo(13, "tmp_office"));
        idxVos.add(new ItIdxVo(14, "professional"));
        idxVos.add(new ItIdxVo(15, "t_class"));
        idxVos.add(new ItIdxVo(16, "country"));
        idxVos.add(new ItIdxVo(17, "area"));
        idxVos.add(new ItIdxVo(18, "national"));
        idxVos.add(new ItIdxVo(19, "political"));
        idxVos.add(new ItIdxVo(20, "projectExperience"));
        idxVos.add(new ItIdxVo(21, "contestExperience"));
        idxVos.add(new ItIdxVo(22, "award"));
        idxVos.add(new ItIdxVo(23, "enterDate"));
        idxVos.add(new ItIdxVo(24, "temporary_date"));
        idxVos.add(new ItIdxVo(25, "graduation"));
        idxVos.add(new ItIdxVo(26, "address"));
        idxVos.add(new ItIdxVo(27, "instudy"));
        idxVos.add(new ItIdxVo(28, "curr_state"));
        idxVos.add(new ItIdxVo(29, "tmp_no"));
        idxVos.add(new ItIdxVo(30, "expiry"));
    }

    public IitAbsTpl getTpl(IitAbsTpl tpl, String key) {
       return getTpl(tpl, key, null, "门禁卡模板");
    }

    @Override
    public IitAbsTpl getTpl(IitAbsTpl tpl, String key, String tplName, String fileName) {
        if(StringUtil.isEmpty(tpl.getRootPath())){
            logger.warn("模板根路径不能为空！RootPath");
            return tpl;
        }

        if(StringUtil.isEmpty(tplName)){
            tplName = "imp_dr_card";
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
                absTpl.setFname("门禁卡模板" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLSX);
            }
        }else if((tpl instanceof ItTplXsheet)){
            absTpl = new ItTplXworkbook();
            tplName += StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS;
            if(StringUtil.isNotEmpty(fileName)){
                absTpl.setFname(fileName + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS);
            }else{
                absTpl.setFname("门禁卡模板" + StringUtil.DOT + FileUtil.SUFFIX_EXCEL_XLS);
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
    public int getExcelHeadRow() {
        return 3;
    }

    @Override
    public void downTpl(ActYw actyw, HttpServletResponse response, String rootPath, String key) {
        if(StringUtil.isEmpty(rootPath)){
            return;
        }
        ItTplXsheet xsheetTpl = new ItTplXsheet();
        xsheetTpl.setRootPath(rootPath);
        IitAbsTpl absTpl = getTpl(xsheetTpl, key);
        logger.info("当前下载的模板路径为：" + absTpl.getRootPath() +"/"+ absTpl.getTplName());
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= response.getOutputStream();
            fs = PieExcelUtils.setExcelHeader(response, absTpl);
            // excel模板路径
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
    public ApiTstatus<?> deleteImpInfo(ImpInfo impInfo) {
        ApiTstatus<?> actYwRstatus = new ApiTstatus(false, "删除失败");
        impservice.delete(impInfo);
        impemsgService.deleteWLByImpId(new ImpInfoErrmsg(null, impInfo.getId()));
        drCardErrorService.deleteWLByImpId(new DrCardError(null, impInfo.getId()));
        actYwRstatus.setStatus(true);
        actYwRstatus.setMsg("删除成功");
        return actYwRstatus;
    }

    @Override
    public void getExpErrorDataFile(ImpInfo impInfo, HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setExcelHead(XSSFSheet sheet) {
        sheet.getRow(0).getCell(0).setCellValue(new XSSFRichTextString("填写数据说明：红色名称为必填信息。日期格式举例2017-05-19；擅长技术领域为多选信息，若有多个则用英文输入法逗号分隔;"
                + "\r\n擅长技术领域可选值有:云计算,大数据,移动互联网,物联网,智慧城市,VR/AR,人工智能,"
                + "\r\n有效期格式：2018-01-01"));
    }

    @Override
    public void expErrorData(ImpInfo impInfo, String tplName, String fileName, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(impInfo.getId())){
            logger.error("导入信息ID不能为空！");
        }
        List<ItIdxVo> idxVos = new IdxDr().initIdxVos();
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            ItTplXsheet xsheet = new ItTplXsheet();
            xsheet.setRootPath(SpringContextHolder.getWebPath(request, ItDrService.TPL_EXCEL_DR_ROOT));
            fs = PieExcelUtils.setExcelHeader(response, getTpl(xsheet, null, tplName, (StringUtil.isEmpty(fileName)?"门禁卡错误数据":fileName)));
            // 读取了模板内所有sheet内容
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            CreationHelper factory = wb.getCreationHelper();
            ClientAnchor anchor = factory.createClientAnchor();
            Drawing drawing = sheet.createDrawingPatriarch();
            List<Map<String, String>> list= drCardErrorService.findListByImpId(impInfo.getId());
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
                            System.out.println(obj.toString());
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
    public void checkTpl(IitTpl<?> tpl, HttpServletRequest request) throws ImpDataException{
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
                    throw new ImpDataException("模板错误,请下载最新的模板");
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
        }else{
            logger.warn("模板文件类型未定义！");
        }
    }

    @Override
    public void impData(ItSupparam param, MultipartFile mpFile, HttpServletRequest request) throws Exception {
        if(param.getTpl() == null){
            return;
        }

        ItParamDrCard curparam = (ItParamDrCard) param;
        if((XSSFWorkbookType.XLSX.getExtension()).equals(curparam.getTpl().getFileType())){
            ItTplXsheet xsheetTpl = (ItTplXsheet) curparam.getTpl();
            if((xsheetTpl == null) || (xsheetTpl.getFile() == null)){
                return;
            }
            importData(xsheetTpl.getFile(), curparam.getIi(), curparam.getOper());
        }else{
            logger.warn("模板后缀不匹配！");
        }
    }

    @Override
    public void uploadFtp(ItSupparam param, MultipartFile mpFile) {
        // TODO 无附件处理
    }

    /**
     * 处理Sheet数据导入,及数据校验.
     * @param sheet Excel表
     * @param ii 导入信息
     * @param ay 业务
     * @param impVo 导入操作
     * @throws Exception
     */
    private void importData(XSSFSheet sheet, ImpInfo ii, ItOper impVo) throws Exception {
        ItCparamDr param = new ItCparamDr(sheet, impemsgService, ii, new ImpInfoErrmsg());
        XSSFRow rowData;
        int fail = 0;// 失败数
        int success = 0;// 成功数
        // 转换、校验所有字段并塞入要用到的各种对象。最后根据校验的结果判断要保存什么对象
        for (int i = ImpDataService.descHeadRow + 1; i < sheet.getLastRowNum() + 1; i++) {
            DrCardError validinfo = new DrCardError();// 用于保存处理之后的信息，以免再次查找数据库.
            DrCardError phe = new DrCardError();
            param.setTag(0);// 有几个错误字段
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            phe.setIsNewRecord(true);
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
                String val = StringUtil.trim(ExcelUtils.getStringByCell(rowData.getCell(j), sheet));
                param.setIdx(j);
                param.setVal(val);
                param.setRows(ImpDataService.descHeadRow);
                new ItCkDrElement("用户名", "loginName").validate(param, phe, validinfo);
                new ItCkUserName().validate(param, phe, validinfo);//姓名
                new ItCkUserNo().validate(param, phe, validinfo);//学号
                new ItCkDrElement("手机号", "tmpTel").validate(param, phe, validinfo);
                new ItCkDrElement("邮箱", "email").validate(param, phe, validinfo);
                new ItCkDrElement("备注", "remarks").validate(param, phe, validinfo);
                new ItCkDrElement("出生年月", "birthday").validate(param, phe, validinfo);
                new ItCkDrElement("证件类别", "idType").validate(param, phe, validinfo);
                new ItCkDrElement("证件号", "idNo").validate(param, phe, validinfo);
                new ItCkUserSex().validate(param, phe, validinfo);
                new ItCkDrElement("擅长技术领域", "domain").validate(param, phe, validinfo);
                new ItCkDrElement("学位", "degree").validate(param, phe, validinfo);
                new ItCkDrElement("学历", "education").validate(param, phe, validinfo);
                new ItCkUserOfficeXy().validate(param, phe, validinfo);
                new ItCkUserOfficeZy().validate(param, phe, validinfo);
                new ItCkDrElement("班级", "tClass").validate(param, phe, validinfo);
                new ItCkUserProfessional().validate(param, phe, validinfo);
                new ItCkUserClzss().validate(param, phe, validinfo);
                new ItCkDrElement("国家", "country").validate(param, phe, validinfo);
                new ItCkDrElement("地区", "area").validate(param, phe, validinfo);
                new ItCkDrElement("民族", "national").validate(param, phe, validinfo);
                new ItCkDrElement("政治面貌", "political").validate(param, phe, validinfo);
                new ItCkDrElement("项目经历", "projectExperience").validate(param, phe, validinfo);
                new ItCkDrElement("大赛经历", "contestExperience").validate(param, phe, validinfo);
                new ItCkDrElement("获奖作品", "award").validate(param, phe, validinfo);
                new ItCkDrElement("入学时间", "enterDate").validate(param, phe, validinfo);
                new ItCkDrElement("休学时间", "temporaryDate").validate(param, phe, validinfo);
                new ItCkDrElement("毕业时间", "graduation").validate(param, phe, validinfo);
                new ItCkDrElement("联系地址", "address").validate(param, phe, validinfo);
                new ItCkDrElement("在读学位", "instudy").validate(param, phe, validinfo);
                new ItCkDrElement("现状", "currState").validate(param, phe, validinfo);
                new ItCkDrCardNo(drCardService).validate(param, phe, validinfo);
                new ItCkDrCardExpire().validate(param, phe, validinfo);
                new ItCkDrCardRepeat(drCardService).validate(param, phe, validinfo);
            }
            if (param.getTag() != 0) {// 有错误字段,记录错误信息
                fail++;
                drCardErrorService.save(phe);
            } else {// 无错误字段，保存信息
                try {
                    drCardErrorService.saveCardAndUser(validinfo, impVo);
                    success++;
                } catch (Exception e) {
                    logger.error("保存卡片信息出错", e);
                    fail++;
                    drCardErrorService.save(phe);
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
}
