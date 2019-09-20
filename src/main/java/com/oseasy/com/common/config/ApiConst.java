package com.oseasy.com.common.config;

/**
 * 内容管理--错误信息枚举类
 *
 * Created by liangjie on 2018/8/30.
 */
public enum ApiConst {


    TOKEN_FAILED(1001,"token失效"),
    PARAM_ERROR(1002,"参数有误"),
    INNER_ERROR(1003,"内部错误"),
    NULL_ERROR(1004,"数据为空"),
    MORE_ERROR(1005,"重复数据"),
    CATEGORYMORE_ERROR(1006,"栏目标识已存在"),
    SYSTEM_ERROR(1007,"标识已存在"),
    USER_ERROR(1008,"用户信息不全"),
    CERTUSED_ERROR(1010,"已用于学分认定申请"),
    MEMBERMORE_ERROR(1011,"组成员不能再申请同样的标准"),
    RAPPLYMORE_ERROR(1012,"根据学分规则，标准取最高级别值的只能申请一次,已存在该申请"),
    LESSUSER_ERROR(1009,"实际专家数少于所需专家数"),
    ASSIGN_ERROR(1013,"专家人数不足，建议手动分配"),
    CMS_CATEGORY_NULL_ERROR(1016,"栏目数据为空"),
    CMS_ARTICLE_NULL_ERROR(1017,"文章数据为空"),
    DETAILSCOREFULL_ERROR(1014,"该标准学分已满"),
    GATELIST_ERROR(1018,"网关条件为空"),
    MODEL_ERROR(1020,"模板流程已经被关联，请关联其他流程"),
    DETAILCERTMORE_ERROR(1015,"已申请过相同的认定学分"),
    OTHER_ERROR(10000, "其它错误");

    public static Integer STATUS_SUCCESS = 1;
    public static Integer STATUS_FAIL = 0;
    public static Integer CODE_REQUEST_SUCCESS = 1000;
    public static Integer CODE_TOKEN_FAILED_CODE = 1001;
    public static Integer CODE_PARAM_ERROR_CODE = 1002;
    public static Integer CODE_INNER_ERROR = 1003;
    public static Integer CODE_NULL_ERROR = 1004;
    public static Integer CODE_MORE_ERROR = 1005;
    public static Integer CODE_CATEGORYMORE_ERROR = 1006;
    public static Integer CODE_SYSTEM_ERROR = 1007;
    public static Integer CODE_USER_ERROR = 1008;
    public static Integer CODE_LESSUSER_ERROR = 1009;
    public static Integer CODE_ASSIGN_ERROR = 1013;
    public static Integer CODE_GATELIST_ERROR = 1018;
    public static Integer CODE_MODEL_ERROR = 1020;
    public static Integer CODE_OTHER_ERROR = 10000;
    private Integer code;
    private String msg;

    public static String STATUS_SUCCESS_STR = "1";
    public static String STATUS_FAIL_STR = "0";

    public static String AUDITSTATUS_TODO_STR = "0";
    public static String AUDITSTATUS_SUCCESS_STR = "1";
    public static String AUDITSTATUS_FAIL_STR = "2";

    ApiConst(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public java.lang.Integer getCode() {
        return code;
    }

    public void setCode(java.lang.Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public static String getErrMsg(Integer code) {
        for(ApiConst cmsApiConst : ApiConst.values()){
            if(code.equals(cmsApiConst.getCode())){
                return cmsApiConst.getMsg();
            }
        }
        return INNER_ERROR.getMsg();
    }

}
