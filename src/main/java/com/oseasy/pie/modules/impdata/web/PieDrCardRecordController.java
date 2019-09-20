package com.oseasy.pie.modules.impdata.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DrExpService;
import com.oseasy.dr.modules.dr.vo.DrCardRecordShowVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordWarnVo;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;
import com.oseasy.pie.modules.expdata.service.ExpInfoService;

/**
 * 门禁卡记录Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardRecord")
public class PieDrCardRecordController extends BaseController {
    @Autowired
    DrExpService drExpService;
    @Autowired
    ExpInfoService expInfoService;

    /**
     * 导出刷卡记录.
     * @param uuid
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "ajaxExportCardRecord")
    public void ajaxExportCardRecord(String uuid, HttpServletRequest request, HttpServletResponse response) {
        try{
            DrCardRecordShowVo vo = (DrCardRecordShowVo) DrUtils.getIdrVo(uuid);
            if(vo == null){
                throw new Exception("无法获取查询条件！");
            }
            drExpService.expAllByCardRecord(vo, request, response);
        }catch (Exception e) {
            logger.error("导出附件异常！");
        }finally {
            DrUtils.removeIdrVo(DrUtils.CACHE_DR_EXPORT, uuid);
        }
    }

    /**
     * 导出刷卡记录.
     * @param uuid
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "ajaxExportInoutRecord")
    public void ajaxExportInoutRecord(String uuid, HttpServletRequest request, HttpServletResponse response) {
        try{
            DrInoutRecordVo vo = (DrInoutRecordVo) DrUtils.getIdrVo(uuid);
            if(vo == null){
                throw new Exception("无法获取查询条件！");
            }
            drExpService.expAllByInoutRecord(vo, request, response);
        }catch (Exception e) {
            logger.error("导出附件异常！");
        }finally {
            DrUtils.removeIdrVo(DrUtils.CACHE_DR_EXPORT, uuid);
        }
    }

    /**
     * 导出预警记录.
     * @param uuid
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "ajaxExportWarnRecord")
    public void ajaxExportWarnRecord(String uuid, HttpServletRequest request, HttpServletResponse response) {
        try{
            DrCardRecordWarnVo vo = (DrCardRecordWarnVo) DrUtils.getIdrVo(uuid);
            if(vo == null){
                throw new Exception("无法获取查询条件！");
            }
            drExpService.expAllByWarnRecord(vo, request, response);
        }catch (Exception e) {
            logger.error("导出附件异常！");
        }finally {
            DrUtils.removeIdrVo(DrUtils.CACHE_DR_EXPORT, uuid);
        }
    }
}