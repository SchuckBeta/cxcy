/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.ProModelError;
import com.oseasy.pie.modules.impdata.entity.ProModelGcontestError;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验项目负责人姓名列.
 * @author chenhao
 *
 */
public class ItCkXgPromLeaderName implements IitCheck<ItCparamPm>{
    public final static Logger logger = Logger.getLogger(ItCkXgPromLeaderName.class);
    private User user;

    public ItCkXgPromLeaderName(ProModel pm) {
        super();
        if(pm != null){
            this.user = pm.getDeuser();
        }
    }

    @Override
    public String key() {
        return "负责人姓名";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        if ((key()).contains(ExcelUtils.getStringByCell(param.getXs().getRow(param.getRows()).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        if((pe instanceof ProModelError) && (pev instanceof ProModelError)){
            ProModelError phe = (ProModelError)pe;
            ProModelError validinfo = (ProModelError)pev;

            phe.setLeader(param.getVal());
            validinfo.setLeader(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("必填信息");
            } else if ((param.getVal()).length() > 100) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("最多100个字符");
                phe.setLeader(null);
            }

            if ((user != null)) {
                if (!(user.getName()).equals(param.getVal())) {
                    param.setTag(param.getTag() + 1);
                    param.setTags(param.getTags() + 1);
                    iie.setErrmsg("项目申报姓名不符");
                    phe.setLeader(param.getVal());
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else if((pe instanceof ProModelGcontestError) && (pev instanceof ProModelGcontestError)){
            ProModelGcontestError phe = (ProModelGcontestError)pe;
            ProModelGcontestError validinfo = (ProModelGcontestError)pev;

            phe.setLeader(param.getVal());
            validinfo.setLeader(param.getVal());
            iie = new ImpInfoErrmsg();
            iie.setImpId(ii.getId());
            iie.setDataId(phe.getId());
            iie.setColname(param.getIdx() + "");
            if (StringUtil.isEmpty(param.getVal())) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("必填信息");
            } else if ((param.getVal()).length() > 100) {
                param.setTag(param.getTag() + 1);
                param.setTags(param.getTags() + 1);
                iie.setErrmsg("最多100个字符");
                phe.setLeader(null);
            }

            if ((user != null)) {
                if (!(user.getName()).equals(param.getVal())) {
                    param.setTag(param.getTag() + 1);
                    param.setTags(param.getTags() + 1);
                    iie.setErrmsg("项目申报姓名不符");
                    phe.setLeader(param.getVal());
                }
            }
            if (StringUtil.isNotEmpty(iie.getErrmsg())) {
                param.getIes().save(iie);
            }
        }else{
            logger.warn("PE 或 PEV 未定义！");
        }
        return param;
    }
}
