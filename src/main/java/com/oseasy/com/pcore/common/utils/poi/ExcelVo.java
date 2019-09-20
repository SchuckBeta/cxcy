package com.oseasy.com.pcore.common.utils.poi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.util.common.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/6/15 0015.
 */
public class ExcelVo {
    public final static String LV_GJJ_FILE = "F:\\tt\\附件1 西藏大学2019年拟立国家级大学生创新创业训练计划项目信息表.xlsx";
    private final static String LV_GJJ = "国家级";

    public final static String LV_XJJ_FILE = "F:\\tt\\附件3 西藏大学2019年拟立校级大学生创新创业训练计划项目信息表.xlsx";
    private final static String LV_XJ = "校级";

    public final static String LV_ZZQ_FILE = "F:\\tt\\附件2 西藏大学2019年拟立自治区级大学生创新创业训练计划项目信息表.xlsx";
    private final static String LV_ZZQ = "自治区级";

    public static void main(String[] args) {
        readVos();
    }

    public static Map<String, List<ExcelVo>> readVos() {
        ImportParams iparams = new ImportParams();
        iparams.setTitleRows(2);
        iparams.setHeadRows(2);
        Map<String, List<ExcelVo>> rvomaps = Maps.newHashMap();
        try {
            rvomaps.put(LV_GJJ, filter(ExcelUploadUtil.importExcel(LV_XJJ_FILE, iparams.getTitleRows(), iparams.getHeadRows(), ExcelVo.class)));
            rvomaps.put(LV_XJ, filter(ExcelUploadUtil.importExcel(LV_GJJ_FILE, iparams.getTitleRows(), iparams.getHeadRows(), ExcelVo.class)));
            rvomaps.put(LV_ZZQ, filter(ExcelUploadUtil.importExcel(LV_ZZQ_FILE, iparams.getTitleRows(), iparams.getHeadRows(), ExcelVo.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ExcelVo rv:rvomaps.get(LV_GJJ)) {
            System.out.println("-->pname=" + rv.getPname() + "-->pno=" + rv.getPno());
        }

        for (ExcelVo rv:rvomaps.get(LV_XJ)) {
            System.out.println("-->pname=" + rv.getPname() + "-->pno=" + rv.getPno());
        }

        for (ExcelVo rv:rvomaps.get(LV_ZZQ)) {
            System.out.println("-->pname=" + rv.getPname() + "-->pno=" + rv.getPno());
        }

        return rvomaps;
    }

    /**
     * 过来空数据行.
     * @param rvs
     * @return
     */
    public static List<ExcelVo> filter(List<ExcelVo> rvs){
        System.out.println(rvs.size());
        List<ExcelVo> rvos = Lists.newArrayList();
        for (ExcelVo rvo: rvs) {
            if(StringUtil.isEmpty(rvo.getPno()) || StringUtil.isEmpty(rvo.getPname())){
                continue;
            }
            rvos.add(rvo);
        }
        System.out.println(rvos.size());
        return rvos;
    }


    @Excel(name = "项目编号", orderNum = "2", isImportField = "true_st")
    protected String pno;

    @Excel(name = "项目名称", orderNum = "3" , isImportField = "true_st")
    protected String pname;

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
