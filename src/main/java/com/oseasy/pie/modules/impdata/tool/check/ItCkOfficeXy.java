/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.OfficeUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.OfficeError;
import com.oseasy.pie.modules.impdata.enums.OffiGrade;
import com.oseasy.pie.modules.impdata.enums.OffiType;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.pie.modules.impdata.tool.param.ItRpOffice;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 检验学院名称.
 * @author chenhao
 *
 */
public class ItCkOfficeXy implements IitCheck<ItCparamPm>{
    public static Logger logger = Logger.getLogger(ItCkOfficeXy.class);

    private Office school;//学校
    private XSSFRow rowData;
    private OfficeService officeService;

    public ItCkOfficeXy(OfficeService officeService, Office school, XSSFRow rowData) {
        super();
        this.school = school;
        this.rowData = rowData;
        this.officeService = officeService;
    }

    @Override
    public String key() {
        return "学院名称";
    }

    @Override
    public boolean validateKey(ItCparamPm param) {
        String keyName = ExcelUtils.getStringByCell(param.getXs().getRow(ImpDataService.descHeadRow).getCell(param.getIdx()), param.getXs());
        if ((key()).equals(keyName)){
            return true;
        }
        return false;
    }

    @Override
    public ItCparamPm validate(ItCparamPm param, IitCheckEetyExt pe, IitCheckEetyExt pev) {
        if(!(param.check() && validateKey(param) && (this.school != null))){
            return param;
        }

        ItRpOffice rparam;
        if(param.getRparam() == null){
            param.setRparam(new ItRpOffice());
        }
        rparam = (ItRpOffice) param.getRparam();
        rparam.setSchool(school);

        ImpInfo ii = param.getInfo();
        ImpInfoErrmsg iie = param.getIe();

        OfficeError phe = (OfficeError)pe;
        OfficeError validinfo = (OfficeError)pev;

        //String prostr = ExcelUtils.getStringByCell(rowData.getCell(param.getIdx() + 1), param.getXs());
        Office curOffice = OfficeUtils.getOfficeByName(param.getVal());
        phe.setOffice(param.getVal());
        validinfo.setOffice(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        if (StringUtil.isEmpty(param.getVal())) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("必填信息");
        } else if (this.school == null) {
            param.setTag(param.getTag() + 1);
            iie.setErrmsg("学校不存在");
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            Office org = new Office();
            org.setType(OffiType.OT_4.getId());
            org.setGrade(OffiGrade.OG_2.getId());
            org.setParent(this.school);
            org.setName(param.getVal());
            org.setRemarks(param.getVal());
            if(curOffice == null){
                officeService.save(org);
                rparam.setOffice(org);
            }else{
                rparam.setOffice(curOffice);
            }
        }
        param.setRparam(rparam);
        return param;
    }

    public Office getSchool() {
        return school;
    }

    public void setSchool(Office school) {
        this.school = school;
    }
}
