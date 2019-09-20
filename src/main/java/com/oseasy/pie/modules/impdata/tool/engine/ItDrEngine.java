/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.engine;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.thread.ThreadPoolUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.exception.ImpDataException;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitParam;
import com.oseasy.pie.modules.impdata.tool.IitTpl;
import com.oseasy.pie.modules.impdata.tool.param.ItParamDrCard;
import com.oseasy.pie.modules.impdata.tool.param.ItSupparam;
import com.oseasy.pie.modules.impdata.tool.service.IitAbsService;
import com.oseasy.pie.modules.impdata.tool.service.ItDrService;
import com.oseasy.pie.modules.impdata.tool.tpl.IitAbsTpl;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXsheet;
import com.oseasy.pie.modules.impdata.tool.tpl.ItTplXworkbook;
import com.oseasy.util.common.utils.FileUtil;

/**
 * 通用导入模板引擎.
 * @author chenhao
 *
 */
public class ItDrEngine extends IitAbsEngine<ItDrService>{
    public final static Logger logger = Logger.getLogger(ItDrEngine.class);

    public ItDrEngine() {
        super();
    }

    public ItDrEngine(ItDrService service) {
        super(service);
    }

    @Override
    public void checkTpl(IitTpl<?> tpl, HttpServletRequest request) throws ImpDataException{
        service().checkTpl(tpl, request);
    }

    /**
     * 参数为ItSupparam时，处理操作方式.
     * @param param 原始参数
     * @param mpFiles 附件
     * @param request 请求
     * @param curparam 当前支持参数
     */
    private void dealFile(IitParam<?> param, List<MultipartFile> mpFiles, HttpServletRequest request, ItSupparam curparam) {
        if((curparam == null) || (curparam.getOper() == null)){
            return;
        }

        for (MultipartFile mpFile : mpFiles) {
            impData(curparam, mpFile, request);
        }
    }

    @Override
    public void impDatas(IitParam<?> param, List<MultipartFile> mpFiles, HttpServletRequest request) {
        if (param instanceof ItParamDrCard) {
            dealFile(param, mpFiles, request, (ItParamDrCard)param);
        }else{
            logger.warn("参数类型未定义！");
        }
    }

    @Override
    public void impData(IitParam<?> param, MultipartFile mpFile, HttpServletRequest request) {
        if (!(param instanceof ItParamDrCard)) {
            logger.warn("参数类型未定义！");
            return;
        }

        ItParamDrCard curparam = (ItParamDrCard) param;
        // 检查模板和导入信息初始化
        if((curparam.getTpl() == null)){
            return;
        }

        if(!FileUtil.checkFileType(mpFile, curparam.getTpl().getFtype())){
            return;
        }

        ImpInfo ii = new ImpInfo();
        ii.setImpTpye(curparam.getImpType());
        ii.setTotal("0");
        ii.setFail("0");
        ii.setSuccess("0");
        ii.setIsComplete(Const.NO);
        ii.setFilename(mpFile.getOriginalFilename());
        IitAbsService.impservice.save(ii);// 插入导入信息

        XSSFWorkbook wb = null;
        InputStream is = null;
        try {
            is = mpFile.getInputStream();
            wb = new XSSFWorkbook(is);

            IitAbsTpl itTpl = (IitAbsTpl) curparam.getTpl();
            itTpl.setRootPath(SpringContextHolder.getWebPath(request, ItDrService.TPL_EXCEL_DR_ROOT));
            itTpl = service().getTpl(itTpl, null);
            if(itTpl instanceof ItTplXsheet){
                ItTplXsheet curitTpl = (ItTplXsheet)itTpl;
                curitTpl.setSheet(wb.getSheetAt(0));// 获取第一个sheet表
                ii.setTotal((curitTpl.getFile().getLastRowNum() - ImpDataService.descHeadRow) + "");
                curparam.setTpl(curitTpl);
            }else if(itTpl instanceof ItTplXworkbook){
                ItTplXworkbook curitTpl = (ItTplXworkbook)curparam.getTpl();
                curitTpl.setWorkbook(wb);
                ii.setTotal((curitTpl.getFile().getSheetAt(0).getLastRowNum() - ImpDataService.descHeadRow) + "");
                curparam.setTpl(curitTpl);
            }

            checkTpl(curparam.getTpl(), request);// 检查模板版本
            IitAbsService.impservice.save(ii);// 插入导入信息
            curparam.setIi(ii);
            ThreadPoolUtils.fixedThreadPool.execute(new Thread() {
                @Override
                public void run() {
                    try {
                        service().impData(curparam, mpFile, request);
                    } catch (Exception e) {
                        ii.setIsComplete(Const.YES);
                        IitAbsService.impservice.save(ii);
                        CacheUtils.remove(CacheUtils.IMPDATA_CACHE, ii.getId());
                        logger.error("门禁卡信息导入出错,模板检查异常", e);
                    }
                }
            });
        } catch (POIXMLException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("请选择正确的文件");
            IitAbsService.impservice.save(ii);
            logger.error("导入出错", e);
        } catch (ImpDataException e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg(e.getMessage());
            IitAbsService.impservice.save(ii);
            logger.error("导入出错", e);
        } catch (Exception e) {
            ii.setIsComplete(Const.YES);
            ii.setErrmsg("导入出错");
            IitAbsService.impservice.save(ii);
            logger.error("导入出错", e);
        } finally {
            curparam.setIi(ii);
            try {
                is.close();
                //wb.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

    }

    @Override
    public void run(IitParam<?> param, List<MultipartFile> mpFiles, HttpServletRequest request) {
        if (param instanceof ItParamDrCard) {
            impDatas(param, mpFiles, request);
        }else{
            logger.warn("参数类型未定义！");
        }
    }
}
