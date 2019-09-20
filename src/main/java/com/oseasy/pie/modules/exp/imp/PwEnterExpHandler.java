/**
 * .
 */

package com.oseasy.pie.modules.exp.imp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.HSSFColor;

import com.oseasy.pie.modules.exp.AbsExpHandler;
import com.oseasy.pie.modules.exp.ExpRule;
import com.oseasy.pro.common.utils.EasyPoiUtil;
import com.oseasy.pro.modules.workflow.handler.DictHandler;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.util.common.utils.Encodes;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;

/**
 * .
 * @author chenhao
 */
public class PwEnterExpHandler extends AbsExpHandler {
    public final static Logger logger = Logger.getLogger(PwEnterExpHandler.class);

    @Override
    public void handle(ExpRule prule) {
        if(prule == null){
            return;
        }
        ExpRule<PwEnterRparam> rule;
        try {
            rule = (ExpRule<PwEnterRparam>) prule;
        } catch (Exception e) {
            return;
        }

        if((rule.getClazz() == null) || (rule.getRparam() == null)){
            logger.warn("导出模板或模板类不能为空！");
            return;
        }

        if(StringUtil.checkEmpty(rule.getDatas())){
            logger.warn("数据为空！");
            return;
        }

        PwEnterType peType = PwEnterType.getByKey(rule.getRparam().getType());
        if(peType == null){
            logger.warn("rule.type不能为空！");
            return;
        }

        if(!this.doPreHandle(rule)){
            return;
        }

        rule.setFileName("入驻"+peType.getName());
        String titleName = new String (rule.getFileName()) + "信息表";
        rule.setFileName(rule.getFileName() + StringUtil.LINE_D + "信息");
        if(StringUtil.isEmpty(rule.getSheetName())){
            rule.setSheetName(peType.getName() + "信息-v1");
        }

        ExportParams eparams = null;
        if(rule.getParam() != null){
            eparams = rule.getParam();
            eparams.setTitle(rule.getFileName());
            eparams.setSecondTitle(titleName);
            eparams.setSheetName(rule.getSheetName());
            eparams.setCreateHeadRows(true);
            eparams.setHeaderColor(HSSFColor.SKY_BLUE.index);
        }else{
            eparams = new ExportParams(rule.getFileName(), titleName, rule.getSheetName());
        }
        eparams.setDictHandler(new DictHandler());
        rule.setParam(eparams);
        render(rule);
        //this.doBeforeHandle(rule);
    }

    /**
     * 列表导出Excel方法.
     */
    public void render(ExpRule rule) {
        if((rule == null) || (rule.getRequest() == null) || (rule.getResponse() == null) || (rule.getClazz() == null)|| (StringUtil.isEmpty(rule.getFileName()))){
            logger.error("导出模板参数未定义");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        if((rule.getParam() == null)){
            return;
        }
        ExportParams params = rule.getParam();
        if(StringUtil.isEmpty(params.getSecondTitle())){
            logger.warn("Excel 的当前子标题为空！");
        }
        System.setProperty("sun.jnu.encoding", "utf-8");// 设置文件的编码
        logger.info("系统编码是：：：" + System.getProperty("file.encoding"));
        params.setStyle(EasyPoiUtil.class);
        params.setType(ExcelType.XSSF);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.DATA_LIST, rule.getDatas());
        map.put(NormalExcelConstants.CLASS, rule.getClazz());
        map.put(NormalExcelConstants.FILE_NAME, rule.getFileName());
        rule.getResponse().setContentType("application/octet-stream; charset=utf-8");
        rule.getResponse().setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(rule.getFileName()));
        PoiBaseView.render(map, rule.getRequest(), rule.getResponse(), NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
}
