package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.UserEntity;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
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
 * 生成子级机构下的用户
 */
public class SytmUserOsub extends SupSytm<SytmvUserOsub> {
    private final static Logger logger = LoggerFactory.getLogger(SytmRole.class);
    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

    public SytmUserOsub(SytmvUserOsub sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "子机构用户";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if((this.sytmvo.getSytmvOfficeSub() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvOfficeSub().getName() + "不能为空！");
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
        SytmvOfficeSub sytmvOfficeSub = this.sytmvo.getSytmvOfficeSub();
        SytmvOffice sytmvOffice = sytmvOfficeSub.getSytmvOffice();

        User user = new User();
        user.setOffice(sytmvOffice.getOfficeTpl());
        SytmvTenant.setTenant(sytmvOffice, user);
        List<User> subs = userDao.findListByOffsub(user);

        if(this.sytmvo.isReset()){
            User puser = new User();
            puser.setOffice(sytmvOffice.getOffice());
            puser.setTenantId(sytmvOffice.getOffice().getTenantId());
            userDao.deleteWLByOffsub(puser);
        }
        Office office = new Office(sytmvOffice.getOffice());
        List<Office> curOffices = officeDao.findByParentIds(office);
        for (Office curOffice:curOffices) {
            if(!(curOffice.getParentId()).equals(sytmvOffice.getOffice().getId())){
                continue;
            }

            for (User sysUser:subs) {
                if(StringUtil.isEmpty(sysUser.getOfficeId()) || (sytmvOffice.getOfficeTpl().getId()).equals(sysUser.getOfficeId())){
                    continue;
                }

                sysUser.setTenantId(sytmvOffice.getOffice().getTenantId());
                Suser suser = new Suser(Suser.genProp(sysUser));
                User cur = new User();
                System.out.println(sysUser.getId());
                System.out.println(sysUser.toString());
                BeanUtils.copyProperties(sysUser, cur);
                cur.setId(IdGen.uuid());
                Suser.updateProp(suser, cur);
                cur.setNo(cur.getNo() + StringUtil.genNumStr(5));
                cur.setLoginName(cur.getLoginName() + StringUtil.genNumStr(5));
                cur.setCompany(curOffice);
                cur.setOffice(curOffice);
                cur.setRegval(null);
                cur.setTenantId(sytmvOffice.getOffice().getTenantId());
                cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                cur.setCreateDate(DateUtil.newDate());
                cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                cur.setUpdateDate(DateUtil.newDate());
                userDao.insert(cur);

                this.sytmvo.getUsers().add(cur);
            }
        }

        for (User cur:this.sytmvo.getUsers()) {
            cur = SytRole.genUserRoles(cur, this.sytmvo.getRoles(), cur);
            if(StringUtil.checkNotEmpty(cur.getRoleList())){
                if(UserEntity.checkRole(cur, Arrays.asList(new String[]{CoreSval.Rtype.STUDENT.getKey()}))){
                    userDao.insertUserStu(cur);
                }else if(UserEntity.checkRole(cur, Arrays.asList(new String[]{CoreSval.Rtype.TEACHER.getKey(), CoreSval.Rtype.EXPORT.getKey()}))){
                    userDao.insertUserTea(cur);
                }

                userDao.insertUserRole(cur);
            }
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
