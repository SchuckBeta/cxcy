/**
 * .
 */

package com.oseasy.dr.modules.dr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.dr.modules.dr.vo.DrCardRecordShowVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordWarnVo;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;
import com.oseasy.pro.modules.workflow.enums.ExpType;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class DrExpService {
    public final static Logger logger = Logger.getLogger(DrExpService.class);
    @Autowired
    PwEnterService pwEnterService;
    @Autowired
    DrCardRecordService drCardRecordService;
    @Autowired
    DrInoutRecordService drInoutRecordService;

    /**
     * 模板文件根目录.
     */
    private static final String EXP_ROOT_PATH = ExpType.TPL_ROOT_EXCEL + ExpType.DrCard.getTplpext();
    private static final String EXP_TPL_CARDRECORD = EXP_ROOT_PATH + "exp_dr_cardRecord.xlsx";
    private static final String EXP_FILE_CARDRECORD = "孵化基地刷卡记录详情表.xlsx";
    private static final String EXP_TPL_INOUTRECORD = EXP_ROOT_PATH + "exp_dr_inoutRecord.xlsx";
    private static final String EXP_FILE_INOUTRECORD = "孵化基地出入记录详情表.xlsx";
    private static final String EXP_TPL_WARNRECORD = EXP_ROOT_PATH + "exp_dr_warnRecord.xlsx";
    private static final String EXP_FILE_WARNRECORD = "孵化基地预警记录表.xlsx";

    /**
     * 获取模板路径.
     * @param request 请求
     * @param tplPath 模板文件
     * @return String
     */
    private String getPath(HttpServletRequest request, String tplPath) {
        return request.getSession().getServletContext().getRealPath("/") + tplPath;
    }

    /**
     * 初始化导出Excel的请求头.
     * @param response 响应
     * @param fileName 导出文件名
     * @throws UnsupportedEncodingException
     */
    private void initExcelHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String headStr = "attachment; filename=\"" + new String(fileName.getBytes(), StringUtil.ISO8859_1) + "\"";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", headStr);
    }

    /**
     * 导出刷卡记录.
     * @param request
     * @param response
     */
    public Boolean expAllByCardRecord(DrCardRecordShowVo vo, HttpServletRequest request, HttpServletResponse response) {
        Boolean isTrue = true;
        FileInputStream fs = null;
        OutputStream out = null;
        File file = null;
        try {
            // excel模板路径
            initExcelHeader(response, EXP_FILE_CARDRECORD);
            file = new File(getPath(request, EXP_TPL_CARDRECORD));
            fs = new FileInputStream(file);
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

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(c0.getStringCellValue());
            int row0 = 3;//数据从第3行开始

            Page<DrCardRecordShowVo> page;
            if(vo.getIsExportAll()){
                Page<DrCardRecordShowVo> curpage = new Page<DrCardRecordShowVo>(request, response);
                curpage.setPageSize(Page.MAX_PAGE_SIZE);
                page = drCardRecordService.findPage(curpage, vo);
            }else{
                page = new Page<DrCardRecordShowVo>();
                page.setList(drCardRecordService.findRecordList(vo));
            }

            if(StringUtil.checkNotEmpty(page.getList())){
                List<DrCardRecordShowVo> drCards = page.getList();

                for (DrCardRecordShowVo card : drCards) {
                    XSSFRow row = sheet0.createRow(row0);
                    row0++;
                    //row.createCell(0).setCellValue(row0 - 3 + "");
                    row.createCell(0).setCellValue(card.getCardNo());
                    row.createCell(1).setCellValue(card.getUname());
                    row.createCell(2).setCellValue(card.getUno());
                    row.createCell(3).setCellValue(card.getUmobile());
                    row.createCell(4).setCellValue(card.getOffice());

                    if(StringUtil.isNotEmpty(card.getSpace())){
                        row.createCell(5).setCellValue(card.getSpace());
                    }else if(StringUtil.isNotEmpty(card.getPsname())){
                        row.createCell(5).setCellValue(card.getPsname());
                    }else{
                        row.createCell(5).setCellValue("");
                    }

                    if((card.getCerspace() != null) && (card.getCerspace().getErspace() != null) && StringUtil.isNotEmpty(card.getCerspace().getErspace().getName())){
                        row.createCell(6).setCellValue(card.getCerspace().getErspace().getName());
                    }else if(StringUtil.isNotEmpty(card.getName())){
                        row.createCell(6).setCellValue(card.getName());
                    }else{
                        row.createCell(6).setCellValue("");
                    }
                    row.createCell(7).setCellValue(card.getPcTimeStr());

                    // 设置样式
                    for (int m = 0; m <= 7; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
            }

            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
            isTrue = false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(e);
                isTrue = false;
            }
        }
        return isTrue;
    }

    /**
     * 导出出入记录.
     * @param request
     * @param response
     */
    public Boolean expAllByInoutRecord(DrInoutRecordVo vo, HttpServletRequest request, HttpServletResponse response) {
        Boolean isTrue = true;
        FileInputStream fs = null;
        OutputStream out = null;
        File file = null;
        try {
            // excel模板路径
            initExcelHeader(response, EXP_FILE_INOUTRECORD);
            file = new File(getPath(request, EXP_TPL_INOUTRECORD));
            fs = new FileInputStream(file);
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

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(c0.getStringCellValue());
            int row0 = 3;//数据从第3行开始

            Page<DrInoutRecordVo> page;
            if(vo.getIsExportAll()){
                Page<DrInoutRecordVo> curpage = new Page<DrInoutRecordVo>(request, response);
                curpage.setPageSize(Page.MAX_PAGE_SIZE);
                page = drInoutRecordService.findPage(curpage, vo);
            }else{
                page = new Page<DrInoutRecordVo>();
                page.setList(drInoutRecordService.findListVo(vo));
            }

            if(StringUtil.checkNotEmpty(page.getList())){
                List<DrInoutRecordVo> drCards = page.getList();

                for (DrInoutRecordVo card : drCards) {
                    XSSFRow row = sheet0.createRow(row0);
                    row0++;
                    //row.createCell(0).setCellValue(row0 - 3 + "");
                    row.createCell(0).setCellValue(card.getCardNo());
                    row.createCell(1).setCellValue(card.getUname());
                    row.createCell(2).setCellValue(card.getUno());
                    row.createCell(3).setCellValue(card.getOffice());

                    if(StringUtil.isNotEmpty(card.getPrname())){
                        row.createCell(4).setCellValue(card.getPrname());
                    }else if(StringUtil.isNotEmpty(card.getPsname())){
                        row.createCell(4).setCellValue(card.getPsname());
                    }else{
                        row.createCell(4).setCellValue("");
                    }

                    if(card.getEnterTime() != null){
                        row.createCell(5).setCellValue(card.getEnterTimeStr());
                    }else{
                        row.createCell(5).setCellValue("");
                    }
                    if(card.getExitTime() != null){
                        row.createCell(6).setCellValue(card.getExitTimeStr());
                    }else{
                        row.createCell(6).setCellValue("");
                    }

                    /**
                     * V1版本Excel模板.
                     */
//                    row.createCell(7).setCellValue(card.getTimestr());
//
//                    // 设置样式
//                    for (int m = 0; m <= 7; m++) {
//                        row.getCell(m).setCellStyle(rowStyle);
//                    }

                    /**
                     * 当前版本Excel模板（去掉时长列）.
                     */
                    // 设置样式
                    for (int m = 0; m <= 6; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
            }

            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
            isTrue = false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(e);
                isTrue = false;
            }
        }
        return isTrue;
    }

    /**
     * 导出预警记录.
     * @param request
     * @param response
     */
    public Boolean expAllByWarnRecord(DrCardRecordWarnVo vo, HttpServletRequest request, HttpServletResponse response) {
        Boolean isTrue = true;
        FileInputStream fs = null;
        OutputStream out = null;
        File file = null;
        try {
            // excel模板路径
            initExcelHeader(response, EXP_FILE_WARNRECORD);
            file = new File(getPath(request, EXP_TPL_WARNRECORD));
            fs = new FileInputStream(file);
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

            XSSFCell c0 = sheet0.getRow(0).getCell(0);
            c0.setCellValue(c0.getStringCellValue());
            int row0 = 3;//数据从第3行开始

//            Page<DrCardRecordWarnVo> page = drCardRecordService.findWarnPage(new Page<DrCardRecordWarnVo>(request, response), vo);
            Page<DrCardRecordWarnVo> page;
            if(vo.getIsExportAll()){
                Page<DrCardRecordWarnVo> curpage = new Page<DrCardRecordWarnVo>(request, response);
                curpage.setPageSize(Page.MAX_PAGE_SIZE);
                page = drCardRecordService.findWarnPage(curpage, vo);
            }else{
                page = new Page<DrCardRecordWarnVo>();
                Page<DrCardRecordWarnVo> curpage = new Page<DrCardRecordWarnVo>(request, response);
                curpage.setPageSize(Page.MAX_PAGE_SIZE);
                page.setList(drCardRecordService.findWarnPage(curpage, vo).getList());
            }

            if(StringUtil.checkNotEmpty(page.getList())){
                List<DrCardRecordWarnVo> drCards = page.getList();

                for (DrCardRecordWarnVo card : drCards) {
                    XSSFRow row = sheet0.createRow(row0);
                    row0++;
                    //row.createCell(0).setCellValue(row0 - 3 + "");
                    row.createCell(0).setCellValue(card.getCardNo());
                    row.createCell(1).setCellValue(card.getUname());
                    row.createCell(2).setCellValue(card.getUno());
                    row.createCell(3).setCellValue(card.getUmobile());

                    if(StringUtil.isNotEmpty(card.getOffice())){
                        row.createCell(4).setCellValue(card.getOffice());
                    }else{
                        row.createCell(4).setCellValue("");
                    }
                    /**
                     * V1版本Excel模板.
                     */
//                    if(StringUtil.isNotEmpty(card.getRspaceName())){
//                        row.createCell(5).setCellValue(card.getRspaceName());
//                    }else if(StringUtil.isNotEmpty(card.getPsname())){
//                        row.createCell(5).setCellValue(card.getPsname());
//                    }else if(StringUtil.isNotEmpty(card.getPrname())){
//                        row.createCell(5).setCellValue(card.getPrname());
//                    }

                    /**
                     * 当前版本Excel模板（场地改为预警规则）.
                     */
                    row.createCell(5).setCellValue(card.getGname());

                    if(StringUtil.isNotEmpty(card.getWarnCurr()) && StringUtil.isNotEmpty(card.getWarnOver())){
                        row.createCell(6).setCellValue(card.getWarnCurr() + "," + card.getWarnOver());
                    }else if(StringUtil.isNotEmpty(card.getWarnCurr())){
                        row.createCell(6).setCellValue(card.getWarnCurr());
                    }else if(StringUtil.isNotEmpty(card.getWarnCurr())){
                        row.createCell(6).setCellValue(card.getWarnOver());
                    }else if(StringUtil.isNotEmpty(card.getWarnName())){
                        row.createCell(6).setCellValue(card.getWarnName());
                    }

                    // 设置样式
                    for (int m = 0; m <= 6; m++) {
                        row.getCell(m).setCellStyle(rowStyle);
                    }
                }
            }

            out = response.getOutputStream();
            wb.write(out);
        } catch (Exception e) {
            logger.error(e);
            isTrue = false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (fs != null)
                    fs.close();
            } catch (IOException e) {
                logger.error(e);
                isTrue = false;
            }
        }
//        if(isTrue){
//            FileUtil.downFile(new File(file.getPath()), request, response);
//            FileUtil.deleteFile(file.getPath());
//        }
        return isTrue;
    }
}