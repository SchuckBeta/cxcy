/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.data;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.common.utils.poi.MergedResult;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pie.modules.iep.vo.TplVtype;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpInfoErrmsgService;
import com.oseasy.pie.modules.impdata.service.ImpInfoService;
import com.oseasy.pie.modules.impdata.service.ProModelGcontestErrorService;
import com.oseasy.pie.modules.impdata.tool.IitTplData;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgProName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromElement;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromEnter;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromHasFile;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromLeaderName;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromLeaderNo;
import com.oseasy.pie.modules.impdata.tool.check.ItCkXgPromOut;
import com.oseasy.pie.modules.impdata.tool.check.ItCparamPm;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.service.ProModelService;
import com.oseasy.pro.modules.promodel.tool.oper.ItOper;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 国家大赛导入模板.
 * @author chenhao
 *
 */
public class TplGcontestGJ implements IitTplData{
    public final static Logger logger = Logger.getLogger(TplGcontestGJ.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static ImpInfoService impInfoService = SpringContextHolder.getBean(ImpInfoService.class);
    private static ProModelService proModelService = SpringContextHolder.getBean(ProModelService.class);
    private static ImpInfoErrmsgService impInfoErrmsgService = SpringContextHolder.getBean(ImpInfoErrmsgService.class);
    private static ProModelGcontestErrorService proModelGcontestErrorService = SpringContextHolder.getBean(ProModelGcontestErrorService.class);

    @Override
    public int headRow() {
        return 1;
    }

    @Override
    public int dataStartRow() {
        return headRow() + 1;
    }

    @Override
    public void checkTpl(ItOper impVo, XSSFSheet datasheet, HttpServletRequest request) {

    }

    /**
     * 处理国家互联网+大赛附件数据和附件存储.
     */
    @Override
    public void impData(ActYw ay, MultipartFile imgFile, HttpServletRequest request, ItOper impVo) {

    }

    @Override
    public void impDataFile(XSSFSheet sheet, ImpInfo ii, ActYw ay, ItOper impVo) throws Exception{
        ItCparamPm param = new ItCparamPm(sheet, impInfoErrmsgService, ii, new ImpInfoErrmsg(), ay);
        XSSFRow rowData;
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
            ProModel pd = null;
            param.setTags(0);// 有几个错误字段
            ProModelGcontestError phe = new ProModelGcontestError();
            ProModelGcontestError validinfo = new ProModelGcontestError();// 用于保存处理之后的信息，以免再次查找数据库.
            phe.setImpId(ii.getId());
            phe.setId(IdGen.uuid());
            rowData = sheet.getRow(i);
            if(rowData == null){
                continue;
            }
            /* 判断这一行数据是不是都是空，文件中是删除数据未删除行的那种 */
            int validcell = 0;
            for (int j = 0; j < sheet.getRow(headRow()).getLastCellNum(); j++) {
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
            for (int j = 0; j < sheet.getRow(headRow()).getLastCellNum(); j++) {
                param.setTag(0);// 有几个错误字段
                String val = ExcelUtils.getStringByCell(rowData.getCell(j), sheet);
                if (val != null) {// 去掉所有空格
                    val = StringUtil.trim(val);
                }
                param.setIdx(j);
                param.setVal(val);
                param.setRows(headRow());
                pd = new ItCkXgProName(proModelService).validate(pd, param, phe, validinfo);
                if(StringUtil.isNotEmpty(param.getVal())){
                    param = new ItCkXgPromLeaderName(pd).validate(param, phe, validinfo);
                    param = new ItCkXgPromLeaderNo(userService).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("负责人手机号", "mobile", TplVtype.MOBILE).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("负责人邮箱", "email", TplVtype.EMAIL).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("学历层次", "xueli").validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("所属领域", "domain").validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("高校", "lschool").validate(param, phe, validinfo);

                    param = new ItCkXgPromEnter(DateUtil.FMT_YYYY).validate(param, phe, validinfo);
                    param = new ItCkXgPromOut(DateUtil.FMT_YYYY).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("专业名称", "profes").validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("项目阶段", "stage", 50, null).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("参赛类别", "type", 64, null).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("参赛组别", "groups", 64, null).validate(param, phe, validinfo);
                    param = new ItCkXgPromElement("项目简介", "introduction", 10000, null).validate(param, phe, validinfo);

                    if(impVo.getHasFile()){
                        param = new ItCkXgPromHasFile().validate(param, phe, validinfo);
                    }
                }
            }

            if (param.getTags() != 0) {// 有错误字段,记录错误信息
                fail++;
                proModelGcontestErrorService.insert(phe);
            } else {// 无错误字段，保存信息
                try {
                    proModelGcontestErrorService.updateProModelGcontest(phe, pd, ay, impVo);
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
