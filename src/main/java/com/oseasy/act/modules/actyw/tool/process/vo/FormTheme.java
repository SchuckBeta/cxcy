package com.oseasy.act.modules.actyw.tool.process.vo;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 表单表单组类型.
 * @author chenhao
 *
 */
public enum FormTheme {
    F_MR(0, false, FlowType.FWT_ALL.getKey(), "", "默认", "默认", "旧的表单模板", ""),
    F_COM(30, true, FlowType.FWT_XM.getKey(), "common_", "通用项目", "通用项目", "根据产品通用业务制定的表单组", "proModelService"),
    F_GZSMXX(40, false, FlowType.FWT_DASAI.getKey(), "gzsmxx_", "桂子山梦想秀大赛", "桂子山梦想秀大赛", "桂子山梦想秀表单组", "proModelGzsmxxService"),
    F_MD(10, true, FlowType.FWT_XM.getKey(), "md_", "民大2018年项目", "民大2018", "根据民大业务制定的表单组", "proModelMdService"),
    F_MD_XM(90, true, FlowType.FWT_XM.getKey(), "mdXm_", "民大2019年项目", "民大2019", "根据民大业务制定的表单组", "proModelMdService"),
    F_MD_COM(50, true, FlowType.FWT_DASAI.getKey(), "md_", "互联网+大赛", "互联网+大赛", "根据民大业务制定的表单组(含评分)", "proModelService"),
    F_MD_GC(60, true, FlowType.FWT_DASAI.getKey(), "mdGc_", "通用大赛", "通用大赛", "根据民大业务制定的表单组(不含评分)", "proModelMdGcService"),
    F_TLXY(70, false, FlowType.FWT_XM.getKey(), "tlxy_", "铜陵学院项目", "铜陵学院项目", "根据铜陵学院业务制定的表单组", "proModelTlxyService"),
    F_HSXM(100, true, FlowType.FWT_XM.getKey(), "hsxm_", "华师大创项目", "华师大创项目", "根据华师大创业务制定的表单组", "proModelHsxmService"),
    F_NP_AIM(110, true, FlowType.FWT_XM.getKey(), "npaim_", "省国创项目", "省国创项目", "根据国家大创项目创业务制定的表单组", "provinceProModelService"),
    F_NP_GC(120, true, FlowType.FWT_DASAI.getKey(), "npGc_", "省互联网+大赛", "省互联网+大赛", "根据国家互联网+大赛创业务制定的表单组", "provinceProModelService"),
//    F_NP_GC(120, true, FlowType.FWT_DASAI.getKey(), "npGc_", "省互联网+大赛", "省互联网+大赛", "根据国家互联网+大赛创业务制定的表单组", "provinceProModelGcService"),
    F_TEST(80, false, FlowType.FWT_ALL.getKey(), "testgx_", "测试模板", "测试模板", "根据测试模板业务制定的表单组", "proModelService"),
    F_GT(20, false, FlowType.FWT_XM.getKey(), "gt_", "广铁项目", "广铁项目", "根据广铁业务制定的表单组", "proModelGtService"),
    F_SCORE(200, true, FlowType.FWT_SCORE.getKey(), "scr_", "学分认定", "学分认定", "根据学分认定业务制定的表单组", "scrService")
    ;
    public static final String FLOW_THEMES = "formThemes";

    private FormTheme(Integer id, Boolean enable, String ftype, String key, String sname, String name, String remark, String serviceName) {
        this.id = id;
        this.ftype = ftype;
        this.key = key;
        this.enable = enable;
        this.sname = sname;
        this.name = name;
        this.remark = remark;
        this.serviceName = serviceName;
    }

    private Integer id;
    private String key;
    private String ftype;
    private Boolean enable;
    private String sname;
    private String name;
    private String remark;
    private String serviceName;

    /**
     * 获取主题 .
     * @return List
     */
    public static List<FormTheme> getAll(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<FormTheme> enty = Lists.newArrayList();
        FormTheme[] entitys = FormTheme.values();
        for (FormTheme entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                enty.add(entity);
            }
        }
        return enty;
    }
    public static List<FormTheme> getAll() {
        return getAll(true);
    }

    /**
     * 获取Ids .
     * @return List
     */
    public static List<Integer> getAllIds(Boolean enable) {
        if(enable == null){
            enable = true;
        }

        List<Integer> ids = Lists.newArrayList();
        FormTheme[] entitys = FormTheme.values();
        for (FormTheme entity : entitys) {
            if ((entity.getId() != null) && (entity.getEnable())) {
                ids.add(entity.getId());
            }
        }
        return ids;
    }
    public static List<Integer> getAllIds() {
        return getAllIds(true);
    }

    /**
     * 根据id获取FormTheme .
     * @author chenhao
     * @param id id惟一标识
     * @return FormTheme
     */
    public static FormTheme getById(Integer id) {
        if (id == null) {
            return null;
        }

        FormTheme[] entitys = FormTheme.values();
        for (FormTheme entity : entitys) {
            if ((entity.getId()).equals(id)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 根据key获取FormTheme .
     * @author chenhao
     * @param key key惟一标识
     * @return FormTheme
     */
    public static FormTheme getByKey(String key) {
        FormTheme[] entitys = FormTheme.values();
        for (FormTheme entity : entitys) {
            if ((key).equals(entity.getKey())) {
                return entity;
            }
        }
        return null;
    }

    /**
     * 枚举转字符串.
     */
    public static String listToStr(List<FormTheme> pestatus) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < pestatus.size(); i++) {
            FormTheme estatus = pestatus.get(i);
            if(i == 0){
                buffer.append(estatus.getId());
            }else{
                buffer.append(StringUtil.DOTH);
                buffer.append(estatus.getId());
            }
        }
        return buffer.toString();
    }

    /**
     * 显示所有流程设计的功能.
     * @return List
     */
    public static List<FormTheme> showWps() {
        List<FormTheme> es = Lists.newArrayList();
        es.add(FormTheme.F_TLXY);
        return es;
    }
    public static String showWp() {
        return FormTheme.listToStr(FormTheme.showWps()) + StringUtil.DOTH;
    }

    public Integer getId() {
        return id;
    }

    public String getFtype() {
        return ftype;
    }

    public String getKey() {
        return key;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getSname() {
        return sname;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"key\":\""+ this.key+ "\",\"ftype\":\""+ this.ftype +  "\",\"enable\":\""+ this.enable + "\",\"sname\":\""+ this.sname + "\",\"name\":\""+ this.name + "\",\"remark\":\"" + this.remark + "\"}";
    }
}
