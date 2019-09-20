package com.oseasy.com.pcore.modules.syt.vo;

import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.PinyinUtil;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 格式：{名称|登录名|密码}
 */
public class Suser{
    private String name;
    private String ename;
    private String psw;

    public Suser(String[] params) {
        if((params != null) && (params.length == 3)){
            this.name = params[0];
            this.ename = params[1];
            this.psw = params[2];
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }


    /**
     * 生成属性值.
     * 格式：%s系统超级管理员#PREFsuperadmin%s#PREFadmin123
     * @param user User 正则参数{名称}
     * @return String 格式：{名称|登录名|密码}
     */
    public static String[] genProp(User user){
        if(StringUtil.isEmpty(user.getRegval())){
            return null;
        }
        return StringUtil.genRegex(user.getRegval(), new String[]{(user.getName()).replaceAll("模板", ""), user.getTenantId(), StringUtil.EMPTY});
    }

    public static boolean updateProp(Suser suser, User user){
        if(suser != null){
            user.setName(suser.getName());
            user.setLoginName(suser.getEname());
            return true;
        }
        return false;
    }
}