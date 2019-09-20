/**
 * .
 */

package com.oseasy.pie.modules.impdata.tool.check;

import com.oseasy.com.pcore.common.utils.poi.ExcelUtils;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;
import com.oseasy.pie.modules.impdata.entity.StudentError;
import com.oseasy.pie.modules.impdata.service.ImpDataService;
import com.oseasy.pie.modules.impdata.tool.IitCheck;
import com.oseasy.pie.modules.impdata.tool.IitCheckEetyExt;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.*;
import java.util.Date;

/**
 * 学生导入.
 * @author chenhao
 */
public class ItCkCUenterdate implements IitCheck<ItCparamUser>{
    public final static Logger logger = Logger.getLogger(ItCkCUenterdate.class);

    public ItCkCUenterdate() {
        super();
    }

    @Override
    public String key() {
        return "入学年份";
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

        phe.setEnterdate(param.getVal());
        validinfo.setEnterdate(param.getVal());
        iie = new ImpInfoErrmsg();
        iie.setImpId(ii.getId());
        iie.setDataId(phe.getId());
        iie.setColname(param.getIdx() + "");

        Date curDate = null;
        if (StringUtil.isNotEmpty(param.getVal())) {
            try {
                //根据输入截取前4位
                String value=param.getVal().substring(0,4);
                curDate = DateUtil.parseDate(value, DateUtil.FMT_YYYY);
            } catch (ParseException e) {
                param.setTag(param.getTag() + 1);
                iie.setErrmsg("日期格式不正确");
            }
        }

        if (StringUtil.isNotEmpty(iie.getErrmsg())) {
            param.getIes().save(iie);
        }else{
            param.getStudent().setEnterdate(curDate);
        }
        return param;
    }
    public static void main(String ars[]){
        List<String> st=new ArrayList<String>();
        st.add("222233");
        st.add("vvvv2123123dafdsf");

        st.add("aaa233333");
        Collections.sort(st);
        System.out.print("st:"+st);
//        String age="1111年";
//        System.out.print(age.substring(0,4));
    }
}