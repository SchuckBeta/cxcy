/**
 * .
 */

package com.oseasy.pro.modules.workflow.handler;

import java.util.Map;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

import com.oseasy.pro.modules.promodel.vo.ItReqParam;
import com.oseasy.pro.modules.workflow.impl.SpiltPref;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;
import com.oseasy.util.common.utils.StringUtil;

import cn.afterturn.easypoi.handler.inter.IExcelDataHandler;

/**
 * ExpProModelVo数据处理类.
 * @author chenhao
 *
 */
public class DataExpVoHandler implements IExcelDataHandler<ExpProModelVo>{
    private ItReqParam reqParam;

    public DataExpVoHandler() {
        super();
    }

    public DataExpVoHandler(ItReqParam reqParam) {
        super();
        this.reqParam = reqParam;
    }

    @Override
    public Object exportHandler(ExpProModelVo obj, String name, Object value) {
        if((reqParam == null) || (value == null)){
            return value;
        }

        if(StringUtil.isNotEmpty((String) value) && StringUtil.isNotEmpty(reqParam.getPrefix()) && StringUtil.isNotEmpty(reqParam.getPostfix())){
            if(reqParam.getSpre().getIsTwo()){
                String[] splits = SpiltPref.getTwo(reqParam.getSpre());
                value = ((String) value).replaceAll(StringUtil.PREF, splits[0]);
                value = ((String) value).replaceAll(StringUtil.POST, splits[1]+reqParam.getPostfix());
                value += splits[1];
            }else{
                value = ((String) value).replaceAll(StringUtil.PREF, reqParam.getPrefix());
                value = ((String) value).replaceAll(StringUtil.POST, reqParam.getPostfix());
            }
            return value;
        }
        return value;
    }

    @Override
    public String[] getNeedHandlerFields() {
        return new String[]{
                "负责人姓名", "团队成员及学号", "指导教师姓名", "指导教师工号", "职称"    //铜陵学院列
                , "项目负责人", "项目其他成员信息", "指导教师信息"    //通用大赛列
        };
    }

    @Override
    public Object importHandler(ExpProModelVo obj, String name, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setNeedHandlerFields(String[] fields) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        // TODO Auto-generated method stub
    }

    @Override
    public Hyperlink getHyperlink(CreationHelper creationHelper, ExpProModelVo obj, String name, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    public ItReqParam getReqParam() {
        return reqParam;
    }

    public void setReqParam(ItReqParam reqParam) {
        this.reqParam = reqParam;
    }
}
