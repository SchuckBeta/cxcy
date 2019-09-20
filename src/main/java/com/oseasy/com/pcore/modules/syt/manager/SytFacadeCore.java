package com.oseasy.com.pcore.modules.syt.manager;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.syt.manager.sub.*;
import com.oseasy.util.common.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytFacadeCore extends ISytFacade {
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

    public SytFacadeCore(String id) {
        super(id);
    }

    public static final String IN_INTERCEPTOR = "20000";
    Boolean success = true;

    @Override
    public boolean initNce() {
        super.initNce();
        SytmvTenant sytmvTenant0 = new SytmvTenant(SytFacadeCore.IN_INTERCEPTOR + 0);
        sytmvTenant0.setTname(Sval.EmPn.NCENTER.getRemark());
        sytmvTenant0.setType(Sval.EmPn.NCENTER.getPrefix());
        SytmTenant sytmTenant0 = initSytmTenant(sytmvTenant0);
        SytmOffice sytmOffice0 = initSytmOffice(sytmTenant0.sytmvo, CoreIds.NCE_SYS_OFFICE_TOP.getId());
        SytmOfficeSub sytmOfficeSub0 = initSytmOfficeSub(sytmOffice0.sytmvo);
        SytmRole sytmRole0 = initSytmRole(sytmOffice0.sytmvo);
        SytmUser sytmUser0 = initSytmUser(sytmOffice0.sytmvo);
        SytmUserOsub sytmUserOsub0 = initSytmUserOsub(sytmOfficeSub0.sytmvo);
//        SytmDict sytmDict0 = this.initSytmDict(sytmTenant0.sytmvo);
//        SytmConfig sytmConfig0 = this.initSytmConfig(sytmTenant0.sytmvo);
        return super.initNce();
    }

    @Override
    public boolean initNpr() {
        super.initNpr();
        Office office = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
        SytmvTenant sytmvTenant1 = new SytmvTenant(SytFacadeCore.IN_INTERCEPTOR + 1);
        sytmvTenant1.setTname(Sval.EmPn.NPROVINCE.getRemark());
        sytmvTenant1.setType(Sval.EmPn.NPROVINCE.getPrefix());
        SytmTenant sytmTenant1 = initSytmTenant(sytmvTenant1);
        SytmOffice sytmOffice1 = initSytmOffice(sytmTenant1.sytmvo, CoreIds.NPR_SYS_OFFICE_TOP.getId(), office);
        SytmOfficeSub sytmOfficeSub1 = initSytmOfficeSub(sytmOffice1.sytmvo);
        SytmRole sytmRole1 = initSytmRole(sytmOffice1.sytmvo);
        SytmUser sytmUser1 = initSytmUser(sytmOffice1.sytmvo);
        SytmUserOsub sytmUserOsub1 = initSytmUserOsub(sytmOfficeSub1.sytmvo);
//        SytmDict sytmDict1 = this.initSytmDict(sytmTenant1.sytmvo);
//        SytmConfig sytmConfig1 = this.initSytmConfig(sytmTenant1.sytmvo);
        return true;
    }

    @Override
    public boolean initNsc(){
        Office office = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
        SytmvTenant sytmvTenant2 = new SytmvTenant(this.getId());
        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getPrefix());
        SytmTenant sytmTenant2 = initSytmTenant(sytmvTenant2);
        SytmOffice sytmOffice2 = initSytmOffice(sytmTenant2.sytmvo, CoreIds.NCE_SYS_TREE_ROOT.getId(), office);
        SytmOfficeSub sytmOfficeSub2 = initSytmOfficeSub(sytmOffice2.sytmvo);
        SytmRole sytmRole2 = initSytmRole(sytmOffice2.sytmvo);
        SytmMenu sytmMenu2 = initSytmMenu(sytmTenant2.sytmvo, sytmRole2.sytmvo);
        SytmUser sytmUser2 = initSytmUser(sytmOffice2.sytmvo, sytmRole2.sytmvo);
        SytmUserOsub sytmUserOsub2 = initSytmUserOsub(sytmOfficeSub2.sytmvo, sytmRole2.sytmvo);
        SytmDict sytmDict = initSytmDict(sytmTenant2.sytmvo);
        SytmConfig sytmConfig = initSytmConfig(sytmTenant2.sytmvo);

        /**
         *清除开户缓存.
         */
        CoreUtils.removeOpenAll();
        return true;
    }
    public static SytmConfig initSytmConfig(SytmvTenant sytmvTenant) {
        return (SytmConfig) ISytFacade.println(new SytmConfig(new SytmvConfig(sytmvTenant)));
    }

    public static SytmDict initSytmDict(SytmvTenant sytmvTenant) {
        return (SytmDict) ISytFacade.println(new SytmDict(new SytmvDict(sytmvTenant)));
    }

    public static SytmMenu initSytmMenu(SytmvTenant sytmvTenant, SytmvRole sytmvRole) {
        return (SytmMenu) ISytFacade.println(new SytmMenu(new SytmvMenu(sytmvTenant, sytmvRole)));
    }

    public static SytmUserOsub initSytmUserOsub(SytmvOfficeSub sytmvOfficeSub) {
        return initSytmUserOsub(sytmvOfficeSub, null);
    }
    public static SytmUserOsub initSytmUserOsub(SytmvOfficeSub sytmvOfficeSub, SytmvRole sytmvRole) {
        return (SytmUserOsub) ISytFacade.println(new SytmUserOsub(new SytmvUserOsub(sytmvOfficeSub, (sytmvRole == null) ? null : sytmvRole.getRoles())));
    }

    public static SytmUser initSytmUser(SytmvOffice sytmvOffice) {
        return initSytmUser(sytmvOffice, null);
    }
    public static SytmUser initSytmUser(SytmvOffice sytmvOffice, SytmvRole sytmvRole) {
        return (SytmUser) ISytFacade.println(new SytmUser(new SytmvUser(sytmvOffice, (sytmvRole == null) ? null : sytmvRole.getRoles())));
    }

    public static SytmRole initSytmRole(SytmvOffice sytmvOffice) {
        return (SytmRole) ISytFacade.println(new SytmRole(new SytmvRole(sytmvOffice)));
    }

    public static SytmOfficeSub initSytmOfficeSub(SytmvOffice sytmvOffice) {
        return (SytmOfficeSub) ISytFacade.println(new SytmOfficeSub(new SytmvOfficeSub(sytmvOffice)));
    }

    public static SytmOffice initSytmOffice(SytmvTenant sytmvTenant, String id) {
        return initSytmOffice(sytmvTenant, id, null);
    }
    public static SytmOffice initSytmOffice(SytmvTenant sytmvTenant, String id, Office poffice) {
        return (SytmOffice) ISytFacade.println(new SytmOffice(new SytmvOffice(sytmvTenant, id, poffice)));
    }

    public static SytmTenant initSytmTenant(SytmvTenant sytmvTenant) {
        return (SytmTenant) ISytFacade.println(new SytmTenant(sytmvTenant));
    }

    public static SytmTenant initSytmTenantPush(SytmvTenant sytmvTenant) {
        return (SytmTenant) ISytFacade.printlnPush(new SytmTenant(sytmvTenant));
    }


}
