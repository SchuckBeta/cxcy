package com.oseasy.pro.modules.workflow.enums;

import com.alibaba.fastjson.JSONObject;

public enum ExpType {
    MdExpapproval("mdExpapproval", "6", "approval", "民大申报信息导出", "/md/", "exp_approval_template.xlsx", "exp_projectmd_template.xlsx", "", "", "立项信息导入.xlsx"),//民大项目立项信息导入
    MdExpmid("mdExpmid", "7", "mid", "民大中期检查导出", "/md/", "exp_mid_template.xlsx", "exp_projectmd_template.xlsx", "", "", "中期检查信息导入.xlsx"),//民大项目中期检查信息导入
    MdExpclose("mdExpclose", "8", "close", "民大结项信息导出", "/md/", "exp_close_template.xlsx", "exp_projectmd_template.xlsx", "", "", "结项审核信息导入.xlsx"),//民大项目结项信息导入

    DcProjectClose("dcProjectClose", "5", "dcclose", "大创项目到结项节点的信息导出", "", "project_data_template.xlsx", "", "", "", "项目信息导入.xlsx"),// 大创项目到结项节点的信息导入
    HlwGcontest("hlwGcontest", "10", "hlw", "互联网+大赛信息导出", "", "gcontest_data_template.xlsx", "", "", "", "互联网+大赛信息导入.xlsx"),// 互联网+大赛的信息导入

    PmProjectHsmid("pmProjectHsmid", "9", "hsmid", "大创华师项目到中期检查节点的信息导出", "", "project_hs_data_template.xlsx", "", "", "", "项目信息导入.xlsx"),// 大创华师项目到中期检查节点的信息导入
    PmProject("pmProject", "11", "projectHs", "自定义项目信息导出", "", "", "", "", "", ""),// 自定义项目导入
    PmGcontest("pmGcontest", "12", "gcontest", "自定义大赛信息导出", "", "", "", "", "", ""),// 自定义大赛导入
    PmGcontestGJ("pmGcontestGJ", "14", "gcontestGJ", "国家互联网+大赛信息导出", "", "", "", "", "", ""),// 国家互联网+大赛导入

    DrCard("drCard", "23", "close", "门禁卡信息导出", "/dr/", "", "", "", "", ""),//门禁卡导入

    ConStu("conStu", "13", "conStu", "学生信息导出", "/user/", "constu_data_template.xlsx", "", "", "", "学生信息导入.xlsx"),// 新学生信息导入

    Stu("stu", "1", "stu", "学生信息导出", "/user/", "student_data_template.xlsx", "", "", "", "学生信息导入.xlsx"),// 学生信息导入
    Tea("tea", "2", "tea", "导师信息导出", "/user/", "teacher_data_template.xlsx", "", "", "", "导师信息导入.xlsx"),// 导师信息导入
    BackUser("backUser", "3", "backUser", "后台用户信息导出", "/user/", "backuser_data_template.xlsx", "", "", "", "后台用户信息导入.xlsx"),// 后台用户信息导入
    Org("org", "4", "org", "机构信息导出", "/org/", "org_data_template.xlsx", "", "", "", "机构信息导入.xlsx")// 机构信息导入
    ;

    public static final String TPL_ROOT_STATICEXCELTEMPLATE = "/static/excel-template/";
    public static final String TPL_ROOT_EXCEL = "/WEB-INF/views/template/imp/";

    private String value;
    private String idx;//类型标识（有业务逻辑处理，禁止删除）
    private String key;//字符业务标识（有业务逻辑处理，禁止删除）
    private String name;
    private String tplpext;//模板拓展路径（有业务逻辑处理，禁止删除）
    private String tplname;//模板名（有业务逻辑处理，禁止删除）
    private String tplTotalName;//学院汇总模板名（有业务逻辑处理，禁止删除）
    private String impfname;//导入文件名
    private String expfname;//导出文件名
    private String downfname;//下载模板文件名

    private ExpType(String value, String idx, String key, String name, String tplpext, String tplname, String tplTotalName, String impfname, String expfname, String downfname) {
        this.value = value;
        this.idx = idx;
        this.key = key;
        this.name = name;
        this.tplpext = tplpext;
        this.expfname = expfname;
        this.tplname = tplname;
        this.tplTotalName = tplTotalName;
        this.impfname = impfname;
        this.downfname = downfname;
    }

    public String getValue() {
        return value;
    }

    public String getTplpext() {
        return tplpext;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getIdx() {
        return idx;
    }

    public String getImpfname() {
        return impfname;
    }

    public String getExpfname() {
        return expfname;
    }

    public String getTplname() {
        return tplname;
    }

    public String getTplPath() {
        return this.tplpext + this.tplname;
    }
    public String getTplTotalPath() {
        return this.tplpext + this.tplTotalName;
    }

    public String getDownfname() {
        return downfname;
    }

    public static Integer getValueByName(String name) {
        if (name != null) {
            for (ExpType e : ExpType.values()) {
                if (e.name.equals(name)) {
                    return Integer.parseInt(e.value);
                }
            }
        }
        return null;
    }

    public static ExpType getByKey(String key) {
        if (key != null) {
            for (ExpType e : ExpType.values()) {
                if (e.key.equals(key)) {
                    return e;
                }
            }
        }
        return null;
    }

    public static ExpType getByIdx(String idx) {
        if (idx != null) {
            for (ExpType e : ExpType.values()) {
                if (e.idx.equals(idx)) {
                    return e;
                }
            }
        }
        return null;
    }

    public String getTplTotalName() {
        return tplTotalName;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("key", this.key);
        object.put("name", this.name);
        object.put("idx", this.idx);
        object.put("tplpext", this.tplpext);
        object.put("expfname", this.expfname);
        object.put("tplname", this.tplname);
        object.put("tplTotalName", this.tplTotalName);
        object.put("value", this.value);
        return object.toString();
    }
}
