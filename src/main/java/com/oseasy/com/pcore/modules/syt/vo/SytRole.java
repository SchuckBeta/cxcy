package com.oseasy.com.pcore.modules.syt.vo;

/**
 * Created by Administrator on 2019/5/30 0030.
 */

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.json.JsonAliUtils;
import com.oseasy.util.common.utils.json.JsonNetUtils;
import com.oseasy.util.common.utils.json.JsonOrgUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.List;

/**
 * 角色关系实体。
 */
public class SytRole {
    private String type;//租户类型
    private String rtype;//角色类型
    private String nceRid;//运营角色
    private String nscRid;//校角色
    private String nprRid;//省角色
    private String ntplRid;//模板角色
    private Role trole;//角色

        public static List<SytRole> genSytRole(String type, List<Role> targetRoles, List<Role> srcRoles, List<Role> nsctplRoles){
        List<SytRole> sytRoles = Lists.newArrayList();
        for (Role curRole: nsctplRoles) {
            SytRole sytRole = new SytRole();
            sytRole.setType(type);
            sytRole.setNtplRid(curRole.getId());

            for (Role cur: targetRoles) {
                if(StringUtil.isEmpty(cur.getRid())){
                    continue;
                }

                if((cur.getRid()).equals(curRole.getId()) || (curRole.getRtype()).equals(cur.getRtype())){
                    sytRole.setRtype(cur.getRtype());
                    sytRole.setNscRid(cur.getId());
                    sytRole.setTrole(cur);
                    break;
                }
            }

            for (Role cur: srcRoles) {
                if(StringUtil.isEmpty(cur.getRid())){
                    continue;
                }

                if((cur.getRid()).equals(curRole.getId()) || (curRole.getRtype()).equals(cur.getRtype())){
                    sytRole.setRtype(cur.getRtype());
                    sytRole.setNceRid(cur.getId());
                    break;
                }
            }
            sytRoles.add(sytRole);
        }
        return sytRoles;
    }

    public Role getTrole() {
        return trole;
    }

    public void setTrole(Role trole) {
        this.trole = trole;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getNtplRid() {
        return ntplRid;
    }

    public void setNtplRid(String ntplRid) {
        this.ntplRid = ntplRid;
    }

    public String getNceRid() {
        return nceRid;
    }

    public void setNceRid(String nceRid) {
        this.nceRid = nceRid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNscRid() {
        return nscRid;
    }

    public void setNscRid(String nscRid) {
        this.nscRid = nscRid;
    }

    public String getNprRid() {
        return nprRid;
    }

    public void setNprRid(String nprRid) {
        this.nprRid = nprRid;
    }

    /**
     * 根据用户和模板角色列表获取给当前用户赋角色
     * @return User
     */
    public static User genUserRoles(User sysUser, List<Role> roles, User cur) {
        if(StringUtil.checkNotEmpty(sysUser.getRoleList()) && StringUtil.checkNotEmpty(roles)){
            List<Role> croles = Lists.newArrayList();
            for (Role curor:sysUser.getRoleList()) {
                /**
                 * 添加校管理员角色.
                 */
                for (Role curnr: roles) {
                    if((croles).contains(curnr)){
                        continue;
                    }

                    if((curor.getId()).equals(curnr.getRid())){
                        croles.add(curnr);
                    }
                }
            }
            cur.setRoleList(croles);
        }
        return cur;
    }
}
