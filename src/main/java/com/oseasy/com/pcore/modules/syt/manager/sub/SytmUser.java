package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.vo.Suser;
import com.oseasy.com.pcore.modules.syt.vo.SytRole;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 生成顶级机构下的用户
 */
public class SytmUser extends SupSytm<SytmvUser> {
    private final static Logger logger = LoggerFactory.getLogger(SytmRole.class);
    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);

    public SytmUser(SytmvUser sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "用户";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if((this.sytmvo.getSytmvOffice() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvOffice().getName() + "不能为空！");
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

        User user = new User();
        user.setOffice(sytmvOffice.getOfficeTpl());
        SytmvTenant.setTenant(sytmvOffice, user);
        List<User> tops = userDao.findListByOfftop(user);

        if(this.sytmvo.isReset()){
            User puser = new User();
            puser.setOffice(sytmvOffice.getOffice());
            puser.setTenantId(sytmvOffice.getOffice().getTenantId());
            userDao.deleteWLByOfftop(puser);
//            userDao.deleteUserRoleWLByUser();
        }

        for (User sysUser:tops) {
            sysUser.setTenantId(sytmvOffice.getOffice().getTenantId());
            Suser suser = new Suser(Suser.genProp(sysUser));
            User cur = new User();
            BeanUtils.copyProperties(sysUser, cur);
            cur.setId(IdGen.uuid());
            if(!Suser.updateProp(suser, cur)){
                cur.setName(sytmvOffice.getName()+"_"+cur.getName());
            }
            cur.setRegval(null);
            cur.setNo(cur.getNo() + StringUtil.genNumStr(5));
//            cur.setLoginName(cur.getLoginName() + StringUtil.genNumStr(5));
            cur.setCompany(sytmvOffice.getOffice());
            cur.setOffice(sytmvOffice.getOffice());
            cur.setTenantId(sytmvOffice.getOffice().getTenantId());
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setUpdateDate(DateUtil.newDate());
            userDao.insert(cur);
            this.sytmvo.getUsers().add(cur);

            cur = SytRole.genUserRoles(sysUser, this.sytmvo.getRoles(), cur);
            if(StringUtil.checkNotEmpty(cur.getRoleList())){
                userDao.insertUserRole(cur);

                if(UserEntity.checkRole(cur, Arrays.asList(new String[]{CoreSval.Rtype.STUDENT.getKey()}))){
                    userDao.insertUserStu(cur);
                }else if(UserEntity.checkRole(cur, Arrays.asList(new String[]{CoreSval.Rtype.TEACHER.getKey(), CoreSval.Rtype.EXPORT.getKey()}))){
                    userDao.insertUserTea(cur);
                }
            }
            this.sytmvo.getUsers().add(cur);
        }

        if(StringUtil.checkNotEmpty(this.sytmvo.getUsers())){
//            userDao.insertPL(this.sytmvo.getUsers());
//            userDao.insertPLUserRole(this.sytmvo.getUsers());
        }

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
