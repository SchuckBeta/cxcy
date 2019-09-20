/**
 * .
 */

package com.oseasy.pie.modules.iep.service;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.pie.common.utils.poi.PieExcelUtils;
import com.oseasy.pie.modules.iep.entity.IepTpl;
import com.oseasy.pie.modules.iep.tool.IeAbsYw;
import com.oseasy.pie.modules.iep.tool.IeIdxVos;
import com.oseasy.pie.modules.iep.tool.idx.IdxPmGcontest;
import com.oseasy.pie.modules.iep.tool.impl.CellCment;
import com.oseasy.pie.modules.iep.tool.impl.IeDmap;
import com.oseasy.pie.modules.iep.tool.impl.IeRpm;
import com.oseasy.pie.modules.iep.tool.impl.IeRpmFlow;
import com.oseasy.pie.modules.iep.tool.impl.IeTpl;
import com.oseasy.pie.modules.iep.tool.impl.IeYwEngine;
import com.oseasy.pie.modules.iep.tool.impl.IeYws;
import com.oseasy.pie.modules.iep.vo.TplFType;
import com.oseasy.pie.modules.iep.vo.TplOperType;
import com.oseasy.pie.modules.iep.vo.TplStep;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpInfoService;
import com.oseasy.pie.modules.impdata.tool.engine.ItIdxVo;
import com.oseasy.pro.modules.promodel.vo.GgjBusInfo;
import com.oseasy.pro.modules.promodel.vo.GgjStudent;
import com.oseasy.pro.modules.promodel.vo.GgjTeacher;
import com.oseasy.pro.modules.promodel.vo.TplType;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Service
public class IepService {
    public final static Logger logger = Logger.getLogger(IepService.class);
    @Autowired
    private IepTplService iepTplService;
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ImpInfoService impInfoService;

    /**
     * 根据模板ID和类型获取模板.
     * @param iepTpl 模板
     * @param totype 模板类型
     * @return IepTpl
     */
    public IepTpl getTpl(IepTpl iepTpl, String... totype) {
        if(!(totype).equals(iepTpl.getOperType())){
            IepTpl curiepTpl = null;
            List<IepTpl> entitys = iepTplService.findTreeById(iepTpl);

            if(StringUtil.checkEmpty(entitys)){
                return null;
            }

            for (IepTpl cur : entitys) {
                for (String curot : totype) {
                    if((curot).equals(cur.getOperType())){
                        curiepTpl = cur;
                        break;
                    }
                }

                if(curiepTpl != null){
                    iepTpl = curiepTpl;
                    break;
                }
            }
        }
        return iepTpl;
    }

    /**
     * 下载模板.
     * @param iepTpl 模板
     * @param response 响应
     */
    public void downTpl(IepTpl iepTpl, IeAbsYw yw){
        iepTpl = getTpl(iepTpl, TplOperType.DOWNTPL.getKey());
        IeTpl absTpl = new IeTpl(iepTpl, SpringContextHolder.getWebPath(yw.getRequest()));
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= yw.getResponse().getOutputStream();
            fs = PieExcelUtils.setExcelHeader(yw.getResponse(), absTpl);
            if(StringUtil.isEmpty(iepTpl.getFtype())){
                logger.warn("模板类型未定义，请检查参数！");
                if (out!=null)out.close();
                if (fs!=null)fs.close();
                return;
            }
            out = yw.getResponse().getOutputStream();
            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                wb.write(out);
                wb.close();
            }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
                //TODO
            }else {
                logger.error("当前模板文件类型未定义");
            }
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

    @SuppressWarnings("unchecked")
    public ApiTstatus<?> uploadTpl(HttpServletRequest request, HttpServletResponse response) {
        String iepId = request.getParameter(IepTpl.IEPTPL_ID);
        if(StringUtil.isEmpty(iepId)){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        IepTpl iepTpl= iepTplService.get(iepId);
        iepTpl = getTpl(iepTpl, TplOperType.IMP.getKey());
        IeAbsYw yw = new IeYws(iepTpl, request, response);

        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        List<MultipartFile> imgFiles = null;
        ApiTstatus<Object> rstatus = IeAbsYw.checkUpload(iepTpl, yw);
        if(!rstatus.getStatus()){
            rstatus.setDatas(null);
            return rstatus;
        }else{
            imgFiles = (List<MultipartFile>)rstatus.getDatas();
        }

        try {
            IeYwEngine engine = new IeYwEngine();
            if((TplType.MR.getKey()).equals(iepTpl.getType())){
                rstatus = IeRpmFlow.checkIeYw(iepTpl, yw.getRpparam(), actYwService);
                if(!rstatus.getStatus()){
                    rstatus.setDatas(null);
                    return rstatus;
                }

                yw.setIeYw((ActYw)rstatus.getDatas());
                engine.run(iepTpl, yw, imgFiles);
            }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
                rstatus = IeRpmFlow.checkIeYw(iepTpl, yw.getRpparam(), actYwService);
                if(!rstatus.getStatus()){
                    rstatus.setDatas(null);
                    return rstatus;
                }

                yw.setIeYw((ActYw)rstatus.getDatas());
                engine.run(iepTpl, yw, imgFiles);
            }
        }catch(ImpDataException w) {
            logger.error("导入出错", w);
            return new ApiTstatus<Object>(false, w.getMessage());
        }catch (POIXMLException e) {
            logger.error("导入出错", e);
            return new ApiTstatus<Object>(false, "请选择正确的文件");
        }catch (Exception e) {
            logger.error("导入出错", e);
            return new ApiTstatus<Object>(false, "导入出错");
        }
        return rstatus;
    }

    public void exportTpl(IepTpl iepTpl, IeAbsYw yw) {
        iepTpl = getTpl(iepTpl, TplOperType.EXP.getKey());
        IeTpl absTpl = new IeTpl(iepTpl, SpringContextHolder.getWebPath(yw.getRequest()));
        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= yw.getResponse().getOutputStream();
            fs = PieExcelUtils.setExcelHeader(yw.getResponse(), absTpl);
            if(StringUtil.isEmpty(iepTpl.getFtype())){
                logger.warn("模板类型未定义，请检查参数！");
                return;
            }
            out = yw.getResponse().getOutputStream();
            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                wb.write(out);
                wb.close();
            }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
                //TODO
            }else {
                logger.error("当前模板文件类型未定义");
            }
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

    public void downSdata(String id, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(id)){
            logger.info("参数未定义");
            return;
        }
        ImpInfo impInfo = impInfoService.get(id);
        if((impInfo == null) || StringUtil.isEmpty(impInfo.getImpTpye())){
            logger.info("参数未定义");
            return;
        }

        IepTpl iepTpl= iepTplService.get(impInfo.getImpTpye());
        iepTpl = getTpl(iepTpl, TplOperType.IMP.getKey(), TplOperType.EXP.getKey(), TplOperType.DOWNTPL.getKey());
        IeAbsYw yw = new IeYws(iepTpl, request, response);
        iepTpl.setError(true);//获取错误模板
        IeTpl absTpl = new IeTpl(iepTpl, SpringContextHolder.getWebPath(request));

        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            logger.info("参数未定义");
            return;
        }

        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= yw.getResponse().getOutputStream();
            fs = PieExcelUtils.setExcelHeader(yw.getResponse(), absTpl);
            if(StringUtil.isEmpty(iepTpl.getFtype())){
                logger.warn("模板类型未定义，请检查参数！");
                return;
            }

            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
                    if((TplType.MR.getKey()).equals(iepTpl.getType())){
                        //
                    }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
                        if((TplStep.TS1.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            CellCment ccment = new CellCment(wb);
                            IeDmap edmap = impInfoService.downSdataByIep(impInfoService.get(id));
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                // 标题字体样式
                                XSSFFont titleFont = wb.createFont();
                                // 内容字体样式
                                XSSFFont contFont = wb.createFont();
                                // 标题样式
                                XSSFCellStyle titleStyle = wb.createCellStyle();
                                // 内容样式
                                XSSFCellStyle contentStyle = wb.createCellStyle();
                              for(int i=0;i< edmap.getEdinfos().size();i++) {
                                  int headRow = 0;
                                  Map<String, String> map = edmap.getEdinfos().get(i);
                                  XSSFSheet csheet = null;
                                  if(i == 0){
                                      csheet = wb.getSheetAt(0);
                                  }
                                  if(csheet == null){
                                      csheet = wb.createSheet("项目"+i);
                                  }
                                  ccment.setDrawing(csheet.createDrawingPatriarch());
                                  ccment.setEdmap(edmap);
                                  ccment.setSheetIndex(map.get(CoreJkey.JK_ID));
                                  setColWith(csheet);
                                  titleStyle = setTitleStyle(titleFont, titleStyle);
                                  contentStyle = setContStyle(contFont, contentStyle);

                                  /**
                                   * 项目标题.
                                   */
                                  headRow = gjSproHrow(headRow, csheet, titleStyle);
                                  /**
                                   * 项目行数据.
                                   */
                                  headRow = gjSproRdata(headRow, csheet, contentStyle, map, ccment);
                                  /**
                                   * 项目介绍标题.
                                   */
                                  headRow = gjSproIntroHrow(headRow, csheet, titleStyle, contentStyle, map);
                                  /**
                                   * 项目介绍数据.
                                   */
                                  headRow = gjSproIntroRdata(headRow, csheet, titleStyle, contentStyle, map);
                                  /**
                                   * Leader标题.
                                   */
                                  headRow = gjSproLHrow(headRow, csheet, titleStyle);
                                  /**
                                   * Leader数据.
                                   */
                                  headRow = gjSproLRata(headRow, csheet, contentStyle, map, ccment);
                                  /**
                                   * 成员数据.
                                   */
                                  headRow = gjSproMenberRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                                  /**
                                   * 导师数据.
                                   */
                                  headRow = gjSproTeacherRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                                  /**
                                   * 商业信息数据.
                                   */
                                  headRow = gjSproBusinfoRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                              }
                            }
                        }else if((TplStep.TS2.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            int headRow = 3;
                            XSSFSheet sheet = wb.getSheetAt(0);
                            CreationHelper factory = wb.getCreationHelper();
                            ClientAnchor anchor = factory.createClientAnchor();
                            Drawing drawing = sheet.createDrawingPatriarch();
                            IeDmap edmap = impInfoService.downSdataByIep(impInfoService.get(id));
    //                        setExcelHead(sheet);
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                Map<String, Integer> rowIndex = Maps.newHashMap();
                                /**
                                 * 处理响应单元格进行赋值.
                                 */
                                for(int i=0;i< edmap.getEdinfos().size();i++) {
                                    Map<String, String> map = edmap.getEdinfos().get(i);
                                    XSSFRow row = sheet.createRow(i + 1 + headRow);
                                    rowIndex.put(map.get("id"), i + 1 + headRow);
                                    for(String key : map.keySet()) {
                                        Integer cindex = IeIdxVos.getIdx(idxVos, key);
//                                        Integer cindex = IeIdxVos.getIdx(idxVos, key, Arrays.asList(new String[]{"13"}));
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
                                /**
                                 * 处理错误信息.
                                 */
                                if (StringUtil.checkNotEmpty(edmap.getEinfos())) {
                                    for(Map<String, String> errmap : edmap.getEinfos()) {
                                        Comment comment = getConment(wb, factory, anchor, drawing, errmap);
                                        if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }else{
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }
                                    }
                                }
                            }
                        }else if((TplStep.TS3.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            int headRow = 3;
                            XSSFSheet sheet = wb.getSheetAt(0);
                            CreationHelper factory = wb.getCreationHelper();
                            ClientAnchor anchor = factory.createClientAnchor();
                            Drawing drawing = sheet.createDrawingPatriarch();
                            IeDmap edmap = impInfoService.downSdataByIep(impInfoService.get(id));
                            //                        setExcelHead(sheet);
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                Map<String, Integer> rowIndex = Maps.newHashMap();
                                /**
                                 * 处理响应单元格进行赋值.
                                 */
                                for(int i=0;i< edmap.getEdinfos().size();i++) {
                                    Map<String, String> map = edmap.getEdinfos().get(i);
                                    XSSFRow row = sheet.createRow(i + 1 + headRow);
                                    rowIndex.put(map.get("id"), i + 1 + headRow);
                                    for(String key : map.keySet()) {
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
                                /**
                                 * 处理错误信息.
                                 */
                                if (StringUtil.checkNotEmpty(edmap.getEinfos())) {
                                    for(Map<String, String> errmap : edmap.getEinfos()) {
                                        Comment comment = getConment(wb, factory, anchor, drawing, errmap);
                                        if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }else{
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//                    expInfoService.deleteByIep(expInfoService.get(id));
                }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
                    //TODO
                }

                wb.write(out);
                wb.close();
            }else if((TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
                    //impInfoService.deleteByIep(impInfoService.get(id));
                }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//                    expInfoService.deleteByIep(expInfoService.get(id));
                }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
                    //TODO
                }

                wb.write(out);
                wb.close();
            }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
                //TODO
            }else {
                logger.error("当前模板文件类型未定义");
            }
        } catch (Exception e) {
            logger.error("当前模板文件不存在：["+absTpl.getRootPath() + absTpl.getTplName()+"]" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out!=null)out.close();
                if (fs!=null)fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public void downEdata(String id, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(id)){
            logger.info("参数未定义");
            return;
        }
        ImpInfo impInfo = impInfoService.get(id);
        if((impInfo == null) || StringUtil.isEmpty(impInfo.getImpTpye())){
            logger.info("参数未定义");
            return;
        }

        IepTpl iepTpl= iepTplService.get(impInfo.getImpTpye());
        iepTpl = getTpl(iepTpl, TplOperType.IMP.getKey(), TplOperType.EXP.getKey(), TplOperType.DOWNTPL.getKey());
        IeAbsYw yw = new IeYws(iepTpl, request, response);
        iepTpl.setError(true);//获取错误模板
        IeTpl absTpl = new IeTpl(iepTpl, SpringContextHolder.getWebPath(request));

        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            logger.info("参数未定义");
            return;
        }

        FileInputStream fs = null;
        OutputStream out = null;
        try {
            out= yw.getResponse().getOutputStream();
            fs = PieExcelUtils.setExcelHeader(yw.getResponse(), absTpl);
            if(StringUtil.isEmpty(iepTpl.getFtype())){
                logger.warn("模板类型未定义，请检查参数！");
                return;
            }

            if((TplFType.EXCEL_XLS.getKey()).equals(iepTpl.getFtype()) || (TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
                    if((TplType.MR.getKey()).equals(iepTpl.getType())){
                        //
                    }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
                        if((TplStep.TS1.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            CellCment ccment = new CellCment(wb);
                            IeDmap edmap = impInfoService.downEdataByIep(impInfoService.get(id));
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                // 标题字体样式
                                XSSFFont titleFont = wb.createFont();
                                // 内容字体样式
                                XSSFFont contFont = wb.createFont();
                                // 标题样式
                                XSSFCellStyle titleStyle = wb.createCellStyle();
                                // 内容样式
                                XSSFCellStyle contentStyle = wb.createCellStyle();
                                for(int i=0;i< edmap.getEdinfos().size();i++) {
                                    int headRow = 0;
                                    Map<String, String> map = edmap.getEdinfos().get(i);
                                    XSSFSheet csheet = null;
                                    if(i == 0){
                                        csheet = wb.getSheetAt(0);
                                    }
                                    if(csheet == null){
                                        csheet = wb.createSheet("项目"+i);
                                    }
                                    ccment.setDrawing(csheet.createDrawingPatriarch());
                                    ccment.setEdmap(edmap);
                                    ccment.setSheetIndex(map.get(CoreJkey.JK_ID));
                                    setColWith(csheet);
                                    titleStyle = setTitleStyle(titleFont, titleStyle);
                                    contentStyle = setContStyle(contFont, contentStyle);

                                    headRow = gjSproHrow(headRow, csheet, titleStyle);
                                    headRow = gjSproRdata(headRow, csheet, contentStyle, map, ccment);
                                    headRow = gjSproIntroHrow(headRow, csheet, titleStyle, contentStyle, map);
                                    headRow = gjSproIntroRdata(headRow, csheet, titleStyle, contentStyle, map);
                                    headRow = gjSproLHrow(headRow, csheet, titleStyle);
                                    headRow = gjSproLRata(headRow, csheet, contentStyle, map, ccment);
                                    headRow = gjSproMenberRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                                    headRow = gjSproTeacherRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                                    headRow = gjSproBusinfoRata(headRow, csheet, titleStyle, contentStyle, map, ccment);
                                }
                            }
                        }else if((TplStep.TS2.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            int headRow = 1;
                            XSSFSheet sheet = wb.getSheetAt(0);
                            CreationHelper factory = wb.getCreationHelper();
                            ClientAnchor anchor = factory.createClientAnchor();
                            Drawing drawing = sheet.createDrawingPatriarch();
                            IeDmap edmap = impInfoService.downEdataByIep(impInfoService.get(id));
                            //                        setExcelHead(sheet);
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                Map<String, Integer> rowIndex = Maps.newHashMap();
                                /**
                                 * 处理响应单元格进行赋值.
                                 */
                                for(int i=0;i< edmap.getEdinfos().size();i++) {
                                    Map<String, String> map = edmap.getEdinfos().get(i);
                                    XSSFRow row = sheet.createRow(i + 1 + headRow);
                                    rowIndex.put(map.get("id"), i + 1 + headRow);
                                    for(String key : map.keySet()) {
                                        Integer cindex = IeIdxVos.getIdx(idxVos, key);
//                                        Integer cindex = IeIdxVos.getIdx(idxVos, key, Arrays.asList(new String[]{"13"}));
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
                                /**
                                 * 处理错误信息.
                                 */
                                if (StringUtil.checkNotEmpty(edmap.getEinfos())) {
                                    for(Map<String, String> errmap : edmap.getEinfos()) {
                                        Comment comment = getConment(wb, factory, anchor, drawing, errmap);
                                        if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }else{
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }
                                    }
                                }
                            }
                        }else if((TplStep.TS3.getKey() == iepTpl.getParent().getStep())){
                            List<ItIdxVo> idxVos = new IdxPmGcontest().initIdxVos();
                            int headRow = 1;
                            XSSFSheet sheet = wb.getSheetAt(0);
                            CreationHelper factory = wb.getCreationHelper();
                            ClientAnchor anchor = factory.createClientAnchor();
                            Drawing drawing = sheet.createDrawingPatriarch();
                            IeDmap edmap = impInfoService.downEdataByIep(impInfoService.get(id));
                            //                        setExcelHead(sheet);
                            if (StringUtil.checkNotEmpty(edmap.getEdinfos())) {
                                Map<String, Integer> rowIndex = Maps.newHashMap();
                                /**
                                 * 处理响应单元格进行赋值.
                                 */
                                for(int i=0;i< edmap.getEdinfos().size();i++) {
                                    Map<String, String> map = edmap.getEdinfos().get(i);
                                    XSSFRow row = sheet.createRow(i + 1 + headRow);
                                    rowIndex.put(map.get("id"), i + 1 + headRow);
                                    for(String key : map.keySet()) {
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
                                /**
                                 * 处理错误信息.
                                 */
                                if (StringUtil.checkNotEmpty(edmap.getEinfos())) {
                                    for(Map<String, String> errmap : edmap.getEinfos()) {
                                        Comment comment = getConment(wb, factory, anchor, drawing, errmap);
                                        if (sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME)))==null) {
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).createCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }else{
                                            sheet.getRow(rowIndex.get(errmap.get(ImpInfoErrmsg.DATA_ID))).getCell(Integer.parseInt(errmap.get(ImpInfoErrmsg.COL_NAME))).setCellComment(comment);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//                    expInfoService.deleteByIep(expInfoService.get(id));
                }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
                    //TODO
                }

                wb.write(out);
                wb.close();
            }else if((TplFType.EXCEL_XLSX.getKey()).equals(iepTpl.getFtype())){
                XSSFWorkbook wb = new XSSFWorkbook(fs);
                if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
                    //impInfoService.deleteByIep(impInfoService.get(id));
                }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//                    expInfoService.deleteByIep(expInfoService.get(id));
                }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
                    //TODO
                }

                wb.write(out);
                wb.close();
            }else if((TplFType.WORD.getKey()).equals(iepTpl.getFtype())){
                //TODO
            }else {
                logger.error("当前模板文件类型未定义");
            }
        } catch (Exception e) {
            logger.error("当前模板文件不存在：["+absTpl.getRootPath() + absTpl.getTplName()+"]" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out!=null)out.close();
                if (fs!=null)fs.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    /**
     * 设置错误信息.
     */
    private void setCellConment(CellCment ccment, XSSFCell cell, int rowIndex, int colIndex) {
        if ((ccment.getEdmap() != null) && StringUtil.checkEmpty(ccment.getEdmap().getEinfos())) {
            return;
        }
        String curSheet;
        int curRow, curCol;
        for(Map<String, String> errmap : ccment.getEdmap().getEinfos()) {
            curSheet = errmap.get(ImpInfoErrmsg.IDX_SHEET);
            curRow = Integer.parseInt(errmap.get(ImpInfoErrmsg.IDX_ROW));
            curCol = Integer.parseInt(errmap.get(ImpInfoErrmsg.IDX_COL));
            if(((ccment.getSheetIndex()).equals(curSheet)) && (rowIndex == curRow) && (colIndex == curCol)){
                cell.setCellComment(getConment(ccment.getWb(), ccment.getFactory(), ccment.getAnchor(), ccment.getDrawing(), errmap));
            }
        }
    }

    private Comment getConment(XSSFWorkbook wb, CreationHelper factory, ClientAnchor anchor, Drawing drawing,
            Map<String, String> errmap) {
            Comment comment = drawing.createCellComment(anchor);
           RichTextString str = factory.createRichTextString(errmap.get(ImpInfoErrmsg.ERROR_MSG));
           XSSFFont commentFormatter = wb.createFont();
           str.applyFont(commentFormatter);
           comment.setString(str);
           return comment;
    }

    /**
     * 设置标题字体样式.
     * @param font 字体
     * @param style 样式
     */
    private XSSFCellStyle setTitleStyle(XSSFFont font, XSSFCellStyle style) {
        // 加粗
        font.setBold(true);
        font.setColor(new XSSFColor(new Color(255, 0, 0)));
        // 字体名称
//        font.setFontName("宋体");
        // 字体大小
//        font.setFontHeight(16);
//        style.setFont(font);
        style.setFont(font);
        // 竖向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 横向居中
        style.setAlignment(HorizontalAlignment.LEFT);
        // 边框
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
        return style;
    }
    /**
     * 设置内容字体样式.
     * @param font 字体
     * @param style 样式
     */
    private XSSFCellStyle setContStyle(XSSFFont font, XSSFCellStyle style) {
        font.setBold(false);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private int gjSproBusinfoRata(int startRow, XSSFSheet csheet, XSSFCellStyle tstyle, XSSFCellStyle cstyle, Map<String, String> map, CellCment ccment) {
        String businfos = map.get(GgjBusInfo.BUSINFOS);
          if(StringUtil.isNotEmpty(businfos)){
              boolean isFirst = true;
              List<GgjBusInfo> ggjs = GgjBusInfo.mtoGgjBusInfo(businfos);
              for (GgjBusInfo ggj : ggjs) {
                  if(isFirst){
                      startRow = gjSproBusInfoHrow(startRow, csheet, tstyle);
                      isFirst = false;
                  }else{
                      startRow = gjSproBusInfoHrow(startRow + 1, csheet, tstyle);
                  }
                  startRow = gjSproBusInfoRata(startRow + 1, csheet, cstyle, ggj, ccment);
              }
          }
          return startRow + 1;
    }

    private int gjSproTeacherRata(int startRow, XSSFSheet csheet, XSSFCellStyle tstyle, XSSFCellStyle cstyle, Map<String, String> map, CellCment ccment) {
        String teachers = map.get(GgjTeacher.TEACHERS);
        if(StringUtil.isNotEmpty(teachers)){
            boolean isFirst = true;
            List<GgjTeacher> ggjs = GgjTeacher.mtoGgjTeachers(teachers);
            for (GgjTeacher ggj : ggjs) {
                if(isFirst){
                    startRow = gjSproTeaHrow(startRow, csheet, tstyle);
                    isFirst = false;
                }else{
                    startRow = gjSproTeaHrow(startRow + 1, csheet, tstyle);
                }
                startRow = gjSproTeaRata(startRow + 1, csheet, cstyle, ggj, ccment);
            }
        }
        return startRow + 1;
    }

    private int gjSproMenberRata(int startRow, XSSFSheet csheet, XSSFCellStyle tstyle, XSSFCellStyle cstyle, Map<String, String> map, CellCment ccment) {
          String members = map.get(GgjStudent.MEMBERS);
          if(StringUtil.isNotEmpty(members)){
              boolean isFirst = true;
              List<GgjStudent> ggjs = GgjStudent.mtoGgjStudents(members);
              for (GgjStudent ggj : ggjs) {
                  if(isFirst){
                      startRow = gjSproStuHrow(startRow, csheet, tstyle);
                      isFirst = false;
                  }else{
                      startRow = gjSproStuHrow(startRow + 1, csheet, tstyle);
                  }
                  startRow = gjSproStuRata(startRow + 1, csheet, cstyle, ggj, ccment);
              }
          }
          return startRow + 1;
    }

    private int gjSproBusInfoHrow(int startRow, XSSFSheet csheet, XSSFCellStyle style) {
        XSSFRow crow5 = csheet.createRow(startRow);
        crow5.setHeight((short) 540);
        XSSFCell ccell50 = crow5.createCell(0);
        ccell50.setCellValue("工商信息");
        ccell50.setCellStyle(style);
        XSSFCell ccell51 = crow5.createCell(1);
        ccell51.setCellValue("公司名称");
        ccell51.setCellStyle(style);
        XSSFCell ccell52 = crow5.createCell(2);
        ccell52.setCellValue("注册所在地");
        ccell52.setCellStyle(style);
        XSSFCell ccell53 = crow5.createCell(3);
        ccell53.setCellValue("统一社会信用代码");
        ccell53.setCellStyle(style);
        XSSFCell ccell54 = crow5.createCell(4);
        ccell54.setCellValue("法人代表");
        ccell54.setCellStyle(style);
        XSSFCell ccell55 = crow5.createCell(5);
        ccell55.setCellValue("注册资金");
        ccell55.setCellStyle(style);
        XSSFCell ccell56 = crow5.createCell(6);
        ccell56.setCellValue("注册时间");
        ccell56.setCellStyle(style);
        return startRow;
    }

    private int gjSproBusInfoRata(int startRow, XSSFSheet csheet, XSSFCellStyle style, GgjBusInfo ggj, CellCment ccment) {
        XSSFRow crow6 = csheet.createRow(startRow);
        crow6.setHeight((short) 270);
        XSSFCell ccell60 = crow6.createCell(0);
        ccell60.setCellValue("");
        ccell60.setCellStyle(style);
        XSSFCell ccell61 = crow6.createCell(1);
        ccell61.setCellValue(ggj.getCyname());
        ccell61.setCellStyle(style);
        setCellConment(ccment, ccell61, startRow, 1);
        XSSFCell ccell62 = crow6.createCell(2);
        ccell62.setCellValue(ggj.getArea());
        ccell62.setCellStyle(style);
        XSSFCell ccell63 = crow6.createCell(3);
        ccell63.setCellValue(ggj.getNo());
        ccell63.setCellStyle(style);
        XSSFCell ccell64 = crow6.createCell(4);
        ccell64.setCellValue(ggj.getName());
        ccell64.setCellStyle(style);
        XSSFCell ccell65 = crow6.createCell(5);
        ccell65.setCellValue(ggj.getMoney());
        ccell65.setCellStyle(style);
        XSSFCell ccell66 = crow6.createCell(6);
        ccell66.setCellValue(ggj.getRegTime());
        ccell66.setCellStyle(style);
        return startRow;
    }
    private int gjSproTeaHrow(int startRow, XSSFSheet csheet, XSSFCellStyle style) {
        XSSFRow crow5 = csheet.createRow(startRow);
        crow5.setHeight((short) 540);
          XSSFCell ccell50 = crow5.createCell(0);
          ccell50.setCellValue("指导教师信息");
          ccell50.setCellStyle(style);
          XSSFCell ccell51 = crow5.createCell(1);
          ccell51.setCellValue("姓名");
          ccell51.setCellStyle(style);
          XSSFCell ccell52 = crow5.createCell(2);
          ccell52.setCellValue("省市");
          ccell52.setCellStyle(style);
          XSSFCell ccell53 = crow5.createCell(3);
          ccell53.setCellValue("高校");
          ccell53.setCellStyle(style);
          XSSFCell ccell54 = crow5.createCell(4);
          ccell54.setCellValue("部门");
          ccell54.setCellStyle(style);
          XSSFCell ccell55 = crow5.createCell(5);
          ccell55.setCellValue("职称");
          ccell55.setCellStyle(style);
          XSSFCell ccell56 = crow5.createCell(6);
          ccell56.setCellValue("手机号");
          ccell56.setCellStyle(style);
          XSSFCell ccell57 = crow5.createCell(7);
          ccell57.setCellValue("邮箱");
          ccell57.setCellStyle(style);
          return startRow;
    }

    private int gjSproTeaRata(int startRow, XSSFSheet csheet, XSSFCellStyle style, GgjTeacher ggj, CellCment ccment) {
        XSSFRow crow6 = csheet.createRow(startRow);
        crow6.setHeight((short) 270);
        XSSFCell ccell60 = crow6.createCell(0);
        ccell60.setCellValue("");
        ccell60.setCellStyle(style);
        XSSFCell ccell61 = crow6.createCell(1);
        ccell61.setCellValue(ggj.getName());
        ccell61.setCellStyle(style);
        setCellConment(ccment, ccell61, startRow, 1);
        XSSFCell ccell62 = crow6.createCell(2);
        ccell62.setCellValue(ggj.getArea());
        ccell62.setCellStyle(style);
        XSSFCell ccell63 = crow6.createCell(3);
        ccell63.setCellValue(ggj.getSchool());
        ccell63.setCellStyle(style);
        XSSFCell ccell64 = crow6.createCell(4);
        ccell64.setCellValue(ggj.getSubject());
        ccell64.setCellStyle(style);
        XSSFCell ccell65 = crow6.createCell(5);
        ccell65.setCellValue(ggj.getTechnicalTitle());
        ccell65.setCellStyle(style);
        XSSFCell ccell66 = crow6.createCell(6);
        ccell66.setCellValue(ggj.getMobile());
        ccell66.setCellStyle(style);
        XSSFCell ccell67 = crow6.createCell(7);
        ccell67.setCellValue(ggj.getEmail());
        ccell67.setCellStyle(style);
        return startRow;
    }
    private int gjSproStuHrow(int startRow, XSSFSheet csheet, XSSFCellStyle style) {
        XSSFRow crow5 = csheet.createRow(startRow);
        crow5.setHeight((short) 540);
        XSSFCell ccell50 = crow5.createCell(0);
        ccell50.setCellValue("团队成员信息");
        ccell50.setCellStyle(style);
        XSSFCell ccell51 = crow5.createCell(1);
        ccell51.setCellValue("姓名");
        ccell51.setCellStyle(style);
        XSSFCell ccell52 = crow5.createCell(2);
        ccell52.setCellValue("省市");
        ccell52.setCellStyle(style);
        XSSFCell ccell53 = crow5.createCell(3);
        ccell53.setCellValue("高校");
        ccell53.setCellStyle(style);
        XSSFCell ccell54 = crow5.createCell(4);
        ccell54.setCellValue("专业");
        ccell54.setCellStyle(style);
        XSSFCell ccell55 = crow5.createCell(5);
        ccell55.setCellValue("手机号");
        ccell55.setCellStyle(style);
        XSSFCell ccell56 = crow5.createCell(6);
        ccell56.setCellValue("邮箱");
        ccell56.setCellStyle(style);
        XSSFCell ccell57 = crow5.createCell(7);
        ccell57.setCellValue("入学年份");
        ccell57.setCellStyle(style);
        XSSFCell ccell58 = crow5.createCell(8);
        ccell58.setCellValue("毕业年份");
        ccell58.setCellStyle(style);
        return startRow;
    }

    private int gjSproStuRata(int startRow, XSSFSheet csheet, XSSFCellStyle style, GgjStudent ggj, CellCment ccment) {
        XSSFRow crow6 = csheet.createRow(startRow);
        crow6.setHeight((short) 540);
        XSSFCell ccell60 = crow6.createCell(0);
        ccell60.setCellValue("");
        ccell60.setCellStyle(style);
        XSSFCell ccell61 = crow6.createCell(1);
        ccell61.setCellValue(ggj.getName());
        ccell61.setCellStyle(style);
        setCellConment(ccment, ccell61, startRow, 1);
        XSSFCell ccell62 = crow6.createCell(2);
        ccell62.setCellValue(ggj.getArea());
        ccell62.setCellStyle(style);
        XSSFCell ccell63 = crow6.createCell(3);
        ccell63.setCellValue(ggj.getSchool());
        ccell63.setCellStyle(style);
        XSSFCell ccell64 = crow6.createCell(4);
        ccell64.setCellValue(ggj.getProfessional());
        ccell64.setCellStyle(style);
        XSSFCell ccell65 = crow6.createCell(5);
        ccell65.setCellValue(ggj.getMobile());
        ccell65.setCellStyle(style);
        XSSFCell ccell66 = crow6.createCell(6);
        ccell66.setCellValue(ggj.getEmail());
        ccell66.setCellStyle(style);
        XSSFCell ccell67 = crow6.createCell(7);
        ccell67.setCellValue(ggj.getYear());
        ccell67.setCellStyle(style);
        XSSFCell ccell68 = crow6.createCell(8);
        ccell68.setCellValue(ggj.getGyear());
        ccell68.setCellStyle(style);
        return startRow;
    }

    private int gjSproLRata(int startRow, XSSFSheet csheet, XSSFCellStyle style, Map<String, String> map, CellCment ccment) {
        XSSFRow crow = csheet.createRow(startRow);
        crow.setHeight((short) 540);
          XSSFCell ccell0 = crow.createCell(0);
          ccell0.setCellValue("");
          ccell0.setCellStyle(style);
          XSSFCell ccell1 = crow.createCell(1);
          ccell1.setCellValue(map.get("leader"));
          ccell1.setCellStyle(style);
          setCellConment(ccment, ccell1, startRow, 1);
          XSSFCell ccell2 = crow.createCell(2);
          ccell2.setCellValue(map.get("province"));
          ccell2.setCellStyle(style);
          XSSFCell ccell3 = crow.createCell(3);
          ccell3.setCellValue(map.get("lschool"));
          ccell3.setCellStyle(style);
          XSSFCell ccell4 = crow.createCell(4);
          ccell4.setCellValue(map.get("profes"));
          ccell4.setCellStyle(style);
          XSSFCell ccell5 = crow.createCell(5);
          ccell5.setCellValue(map.get("mobile"));
          ccell5.setCellStyle(style);
          XSSFCell ccell6 = crow.createCell(6);
          ccell6.setCellValue(map.get("email"));
          ccell6.setCellStyle(style);
          XSSFCell ccell7 = crow.createCell(7);
          ccell7.setCellValue(map.get("enter"));
          ccell7.setCellStyle(style);
          XSSFCell ccell8 = crow.createCell(8);
          ccell8.setCellValue(map.get("outy"));
          ccell8.setCellStyle(style);
          return startRow + 1;
    }

    private int gjSproLHrow(int startRow, XSSFSheet csheet, XSSFCellStyle style) {
        XSSFRow crow = csheet.createRow(startRow);
        crow.setHeight((short) 270);
        XSSFCell ccell0 = crow.createCell(0);
        ccell0.setCellValue("项目负责人信息");
        ccell0.setCellStyle(style);
        XSSFCell ccell1 = crow.createCell(1);
        ccell1.setCellValue("姓名");
        ccell1.setCellStyle(style);
        XSSFCell ccell2 = crow.createCell(2);
        ccell2.setCellValue("省市");
        ccell2.setCellStyle(style);
        XSSFCell ccell3 = crow.createCell(3);
        ccell3.setCellValue("高校");
        ccell3.setCellStyle(style);
        XSSFCell ccell4 = crow.createCell(4);
        ccell4.setCellValue("专业");
        ccell4.setCellStyle(style);
        XSSFCell ccell5 = crow.createCell(5);
        ccell5.setCellValue("手机号");
        ccell5.setCellStyle(style);
        XSSFCell ccell6 = crow.createCell(6);
        ccell6.setCellValue("邮箱");
        ccell6.setCellStyle(style);
        XSSFCell ccell7 = crow.createCell(7);
        ccell7.setCellValue("入学年份");
        ccell7.setCellStyle(style);
        XSSFCell ccell8 = crow.createCell(8);
        ccell8.setCellValue("毕业年份");
        ccell8.setCellStyle(style);
        return startRow + 1;
    }

    private int gjSproIntroHrow(int startRow, XSSFSheet csheet, XSSFCellStyle tstyle, XSSFCellStyle cstyle, Map<String, String> map) {
          XSSFRow crow = csheet.createRow(startRow);
          crow.setHeight((short) 270);
          XSSFCell ccell = crow.createCell(0);
          ccell.setCellValue("项目概述");
          ccell.setCellStyle(tstyle);
          return startRow + 1;
    }
    private int gjSproIntroRdata(int startRow, XSSFSheet csheet, XSSFCellStyle tstyle, XSSFCellStyle cstyle, Map<String, String> map) {
        XSSFRow crow = csheet.createRow(startRow);
        crow.setHeight((short) 2430);
        XSSFCell ccell = crow.createCell(0);
        ccell.setCellValue(map.get("introduction"));
        ccell.setCellStyle(cstyle);
        return startRow + 1;
    }

    private int gjSproRdata(int startRow, XSSFSheet csheet, XSSFCellStyle style, Map<String, String> map, CellCment ccment) {
        XSSFRow crow = csheet.createRow(startRow);
        crow.setHeight((short) 810);
          XSSFCell ccell0 = crow.createCell(0);
          ccell0.setCellValue(map.get("name"));
          ccell0.setCellStyle(style);
          setCellConment(ccment, ccell0, startRow, 0);
          XSSFCell ccell1 = crow.createCell(1);
          ccell1.setCellValue(map.get("track"));
          ccell1.setCellStyle(style);
          XSSFCell ccell2 = crow.createCell(2);
          ccell2.setCellValue(map.get("groups"));
          ccell2.setCellStyle(style);
          setCellConment(ccment, ccell2, startRow, 2);
          XSSFCell ccell3 = crow.createCell(3);
          ccell3.setCellValue(map.get("type"));
          ccell3.setCellStyle(style);
          setCellConment(ccment, ccell3, startRow, 3);
          XSSFCell ccell4 = crow.createCell(4);
          ccell4.setCellValue(map.get("stage"));
          ccell4.setCellStyle(style);
          XSSFCell ccell5 = crow.createCell(5);
          ccell5.setCellValue(map.get("domain"));
          ccell5.setCellStyle(style);
          return startRow + 1;
    }

    private int gjSproHrow(int startRow, XSSFSheet csheet, XSSFCellStyle style) {
        XSSFRow crow = csheet.createRow(startRow);
        crow.setHeight((short) 270);
          XSSFCell ccell0 = crow.createCell(0);
          ccell0.setCellValue("项目名称");
          ccell0.setCellStyle(style);
          XSSFCell ccell1 = crow.createCell(1);
          ccell1.setCellValue("赛道");
          ccell1.setCellStyle(style);
          XSSFCell ccell2 = crow.createCell(2);
          ccell2.setCellValue("组别");
          ccell2.setCellStyle(style);
          XSSFCell ccell3 = crow.createCell(3);
          ccell3.setCellValue("类别");
          ccell3.setCellStyle(style);
          XSSFCell ccell4 = crow.createCell(4);
          ccell4.setCellValue("项目进展");
          ccell4.setCellStyle(style);
          XSSFCell ccell5 = crow.createCell(5);
          ccell5.setCellValue("所属领域");
          ccell5.setCellStyle(style);
          return startRow + 1;
    }

    /**
     * 设置列宽.
     * @param csheet XSSFSheet
     */
    private void setColWith(XSSFSheet csheet) {
        csheet.setColumnWidth(0, 13853);
          csheet.setColumnWidth(1, 9309);
          csheet.setColumnWidth(2, 5132);
          csheet.setColumnWidth(3, 2320);
          csheet.setColumnWidth(4, 4848);
          csheet.setColumnWidth(5, 4315);
          csheet.setColumnWidth(6, 5900);
          csheet.setColumnWidth(7, 2320);
          csheet.setColumnWidth(8, 2320);
    }

    /**
     * 获取上传导出状态信息.
     * @param entity
     * @param yw 业务参数实体
     * @return Rtstatus
     */
    public ApiTstatus<?> findListByIep(IeAbsYw yw) {
        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        IepTpl iepTpl = iepTplService.get(yw.getRpparam().getIepId());
        if(iepTpl == null){
            return new ApiTstatus<Object>(false, "模板数据未定义(ID = "+yw.getRpparam().getIepId()+")");
        }

        List<IepTpl> iepTpls = iepTplService.findTreeById(iepTpl);
        if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
            ImpInfo entity = new ImpInfo();
            entity.setIepType(iepTpl.getType());
            if(StringUtil.checkNotEmpty(iepTpls)){
                iepTpls.add(iepTpl);
            }else{
                iepTpls = Lists.newArrayList();
                iepTpls.add(iepTpl);
            }
            entity.setIepTypes(StringUtil.sqlInByListIdss(iepTpls));
            return new ApiTstatus<Object>(true, "成功", impInfoService.findListByIep(entity));
        }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//            ExpInfo entity = new ExpInfo();
            return new ApiTstatus<Object>(true, TplOperType.EXP.getName() + "成功");
        }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
            //TODO
            return new ApiTstatus<Object>(true, TplOperType.EXP.getName() + "成功");
        }
        return new ApiTstatus<Object>(false, "模板操作类型未定义");
    }

    /**
     * 删除记录.
     * @param id 记录ID
     * @param request 请求
     * @param response 响应
     * @return Rtstatus
     */
    public ApiTstatus<?> delRecord(String id, HttpServletRequest request, HttpServletResponse response) {
        String iepId = request.getParameter(IepTpl.IEPTPL_ID);
        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(iepId)){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        IepTpl iepTpl= iepTplService.get(iepId);
        IeAbsYw yw = new IeYws(iepTpl, request, response);

        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        if(iepTpl == null){
            return new ApiTstatus<Object>(false, "模板数据未定义(ID = "+yw.getRpparam().getIepId()+")");
        }

        if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
            impInfoService.deleteByIep(impInfoService.get(id));
        }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//            expInfoService.deleteByIep(expInfoService.get(id));
        }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
            //TODO
        }

        String url = null;
        if((TplType.MR.getKey()).equals(iepTpl.getType())){
            IeRpmFlow param = (IeRpmFlow)yw.getRpparam();
            url = "/iep/iepTpl/stepList?repage&id=" +iepTpl.getId() + IeRpmFlow.genParam(param);
        }else if((TplType.GJ.getKey()).equals(iepTpl.getType())){
            IeRpmFlow param = (IeRpmFlow)yw.getRpparam();
            url = "/iep/iepTpl/stepList?repage&id=" +iepTpl.getId() + IeRpmFlow.genParam(param);
        }else{
            IeRpm param = (IeRpm)yw.getRpparam();
            url = "/iep/iepTpl/stepList?repage&id=" +iepTpl.getId() + IeRpm.genParam(param);
        }

        return new ApiTstatus<Object>(true, "删除成功", CoreSval.REDIRECT + CoreSval.getAdminPath() + url);
    }

    /**
     * 获取单条上传导出状态信息(iepId和operType必填).
     * @param entity
     * @return Rtstatus
     */
    public ApiTstatus<Object> getByIep(List<ImpInfo> infos, HttpServletRequest request, HttpServletResponse response) {
        String iepId = request.getParameter(IepTpl.IEPTPL_ID);
        if(StringUtil.checkEmpty(infos) || StringUtil.isEmpty(iepId)){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        IepTpl iepTpl= iepTplService.get(iepId);
        IeAbsYw yw = new IeYws(iepTpl, request, response);

        if((yw.getRpparam() == null) || StringUtil.isEmpty(yw.getRpparam().getIepId())){
            return new ApiTstatus<Object>(false, "参数未定义");
        }

        if(iepTpl == null){
            return new ApiTstatus<Object>(false, "模板数据未定义(ID = "+yw.getRpparam().getIepId()+")");
        }

        Map<String, ImpInfo> rmap = Maps.newHashMap();
        if((TplOperType.IMP.getKey()).equals(yw.getRpparam().getOperType())){
            for(ImpInfo info : infos){
                rmap.put(info.getId(), impInfoService.getImpInfo(info.getId()));
            }
            return new ApiTstatus<Object>(true, TplOperType.IMP.getName() + "成功", rmap);
        }else if((TplOperType.EXP.getKey()).equals(yw.getRpparam().getOperType())){
//            for(ExpInfo info : infos){
//                rmap.put(info.getId(), expInfoService.getImpInfo(info.getId()));
//            }
            return new ApiTstatus<Object>(true, TplOperType.EXP.getName() + "成功", rmap);
        }else if((TplOperType.DOWNTPL.getKey()).equals(yw.getRpparam().getOperType())){
            //TODO
            return new ApiTstatus<Object>(true, TplOperType.EXP.getName() + "成功", rmap);
        }
        return new ApiTstatus<Object>(false, "模板操作类型未定义");
    }

    /**
     * 更新Step属性.
     * @param entity IepTpl
     */
    public boolean updateSteps(IepTpl entity) {
        return iepTplService.updateSteps(entity);
    }
}
