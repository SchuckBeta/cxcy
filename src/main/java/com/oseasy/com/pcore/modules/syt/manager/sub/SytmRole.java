package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.authorize.enums.TenantMenu;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.Menuv;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.PinyinUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmRole extends SupSytm<SytmvRole> {
    private final static Logger logger = LoggerFactory.getLogger(SytmRole.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    public SytmRole(SytmvRole sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "角色";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
            logger.error(this.status.getMsg());
        }

        if((this.sytmvo.getSytmvOffice() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvOffice().getName() + "不能为空！");
            logger.error(this.status.getMsg());
        }

        this.sytmvo.setName(name());
        return true;
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }

        System.out.println(name() + "处理中...");
        SytmvOffice sytmvOffice = this.sytmvo.getSytmvOffice();

        Role role = new Role(sytmvOffice.getOfficeTpl());
        SytmvTenant.setTenant(sytmvOffice, role);
        List<Role> roles = roleDao.findByOffice(role);

        if(this.sytmvo.isReset()){
            //TODO CHENHAO 删除角色菜单
            //roleDao.deleteRoleMenu(new Role(sytmvOffice.getOffice()));
            roleDao.deleteWLByOffice(new Role(sytmvOffice.getOffice()));
        }

        for (Role sysRole:roles) {
            Role cur = new Role();
            BeanUtils.copyProperties(sysRole, cur);
            cur.setId(IdGen.uuid());
            cur.setRid(sysRole.getId());
            cur.setOffice(sytmvOffice.getOffice());
            cur.setTenantId(sytmvOffice.getOffice().getTenantId());
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setUpdateDate(DateUtil.newDate());
            roleDao.insert(cur);

//            if(StringUtil.checkNotEmpty(this.sytmvo.getMenus())){
//                if((CoreSval.Rtype.ADMIN_SN.getKey()).equals(cur.getRtype())){
//                    cur.getMenuList().addAll(this.sytmvo.getMenus());
//                    continue;
//                }
//                cur.getMenuList().addAll(Menuv.filters(TenantMenu.filters(cur.getRtype()), this.sytmvo.getMenus()));
//            }
//            if(StringUtil.checkNotEmpty(cur.getMenuList())){
//                roleDao.insertRoleMenu(cur);
//            }
            this.sytmvo.getRoles().add(cur);
        }

//        if(StringUtil.checkNotEmpty(this.sytmvo.getRoles())){
//            roleDao.insertPL(this.sytmvo.getRoles());
//        }

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }

//    Srole srole = null;
//    if(StringUtil.isNotEmpty(sysRole.getRegval())){
//        srole = new Srole(genProp(sysRole));
//    }
//
//    /**
//     * 生成属性值.
//     * 格式：%s系统超级管理员#PREFsuperadmin%s#PREFadmin123
//     * @param user User 正则参数{名称}
//     * @return String 格式：{名称|登录名|密码}
//     */
//    public static String[] genProp(Srole role){
//        return StringUtil.genRegex(role.getRegval(), new String[]{role.getName(), PinyinUtil.firstChar(role.getName())});
//    }
//
//
//    public static boolean updateProp(Suser suser, User user){
//        if(suser != null){
//            user.setName(suser.getName());
//            user.setLoginName(suser.getEname());
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 格式：{名称|登录名|密码}
//     */
//    class Srole{
//        private String name;
//        private String ename;
//        private String psw;
//
//        public Srole(String[] params) {
//            if(params != null){
//                this.name = params[0];
//                this.ename = params[1];
//                this.psw = params[2];
//            }
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getEname() {
//            return ename;
//        }
//
//        public void setEname(String ename) {
//            this.ename = ename;
//        }
//
//        public String getPsw() {
//            return psw;
//        }
//
//        public void setPsw(String psw) {
//            this.psw = psw;
//        }
//    }
}
