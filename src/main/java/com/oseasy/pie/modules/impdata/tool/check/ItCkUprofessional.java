/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生导入.
 * @author chenhao
 */
public class ItCkUprofessional implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkUprofessional.class);
    private Office office;
    private Office professional;

    public ItCkUprofessional(Office office) {
        super();
        this.office = office;
    }

    @Override
    public String key() {
        return "专业";
    }

    @Override
    public boolean validateKey(ItCparamUser param) {
        if ((key()).equals(ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs()))){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamUser validate(ItCparamUser param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param))){
            return param;
        }

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();
        StudentError phe = (StudentError)pe;
        StudentError validinfo = (StudentError)pev;

        phe.setProfessional(param.getVal());
        validinfo.setProfessional(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (StringUtil.isNotEmpty(param.getVal())) {
            if(office == null){
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("学院不能为空");
            }else if ((this.professional = OfficeUtils.getProfessionalByName(office.getName(), param.getVal())) == null) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("专业不存在");
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            if (professional != null){
                param.getUser().setProfessional(professional.getId());
            }
        }
        return param;
    }

    public Office getProfessional() {
        return professional;
    }

    public void setProfessional(Office professional) {
        this.professional = professional;
    }
}