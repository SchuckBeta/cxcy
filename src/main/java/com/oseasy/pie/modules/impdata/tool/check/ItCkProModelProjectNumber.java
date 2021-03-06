/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;

/**
 * 检验项目名称列.
 * @author chenhao
 *
 */
public class ItCkProModelProjectNumber implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkProModelProjectNumber.class);

    private ProModelDao proModelDao;

    public ItCkProModelProjectNumber(ProModelDao proModelDao) {
        super();
        this.proModelDao = proModelDao;
    }

    @Override
    public String key() {
        return "项目编号";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (param.getActyw() != null) && (this.proModelDao != null))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setNumber(param.getVal());
            validinfo.setNumber(param.getVal());

//          iie = new ImpInfoErrmsg();
//          iie.setImpId(ii.getId());
//          iie.setDataId(phe.getId());
//          iie.setColname(param.getIdx() + "");
//          if (StringUtil.isEmpty(param.getVal())) {
//              param.setTag(param.getTag() + 1);
//              iie.setErrmsg("必填信息");
//          } else if (param.getVal().length() > 64) {
//              param.setTag(param.getTag() + 1);
//              iie.setErrmsg("最多64个字符");
//              phe.setNumber(null);
//          } else {
//              ProModel proModel = new ProModel();
//              proModel.setCompetitionNumber(param.getVal());
//              proModel.setProType(param.getActyw().getProProject().getProType());
//              proModel.setType(param.getActyw().getProProject().getType());
//              Integer cc = proModelDao.findCountByNum(proModel);
//              if (cc != null && cc > 0) {
//                  param.setTag(param.getTag() + 1);
//                  iie.setErrmsg("该项目编号已经存在");
//              }
    //
//          }
//          if (StringUtil.isNotEmpty(iie.getErrmsg())) {
//              param.getIes().save(iie);
//          }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;
            //TODO CHENHAO
        }
        return param;
    }
}
