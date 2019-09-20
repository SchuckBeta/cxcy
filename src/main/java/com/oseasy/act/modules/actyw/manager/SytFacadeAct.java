package com.oseasy.act.modules.actyw.manager;

import com.oseasy.act.modules.actyw.manager.sub.SytmAct;
import com.oseasy.act.modules.actyw.manager.sub.SytmPact;
import com.oseasy.act.modules.actyw.manager.sub.SytmvAct;
import com.oseasy.act.modules.actyw.manager.sub.SytmvPact;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.manager.ISytFacade;
import com.oseasy.com.pcore.modules.syt.manager.SytFacadeCore;
import com.oseasy.com.pcore.modules.syt.manager.sub.*;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytFacadeAct extends ISytFacade {
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
    public SytFacadeAct(String id) {
        super(id);
    }

    @Override
    public boolean pushNsc(String targetId, String groupId, String scid) {
        SytmvTenant sytmvTenant2 = new SytmvTenant(targetId);
        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getPrefix());
        sytmvTenant2.setTenantTplId(this.getId());
        SytmTenant sytmTenant2 = SytFacadeCore.initSytmTenantPush(sytmvTenant2);

        SytmPact sytmPact = initSytmPact(sytmvTenant2, targetId, groupId, scid);
        return super.pushNsc(targetId, groupId, scid);
    }

    @Override
    public boolean initNsc(){
        Office office = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
        SytmvTenant sytmvTenant2 = new SytmvTenant(getId());
        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getPrefix());
        SytmTenant sytmTenant2 = SytFacadeCore.initSytmTenant(sytmvTenant2);

        SytmAct sytmAct2Ds = initSytmAct(sytmvTenant2, "0edf360eb32143a2aee9f666ca31ccdd");
        SytmAct sytmAct2Xm = initSytmAct(sytmvTenant2, "30e4148fd9894142ba8cd2ff64334823");
//        SytmAct sytmAct2Xf = this.initSytmAct(sytmvTenant2, "7e9dd5ec40e54d1692c68c1a3d0136f4");
//        SytmAct sytmAct2Xm = this.initSytmAct(sytmvTenant2, "7e9dd5ec40e54d1692c68c1a3d0136f4");
        return true;
    }

    @Override
    public boolean resetTNsc(){

        return super.resetTNsc();
    }

    public static SytmAct initSytmAct(SytmvTenant sytmvTenant, String groupId) {
        return (SytmAct) ISytFacade.println(new SytmAct(new SytmvAct(groupId, sytmvTenant)));
    }

    public static SytmPact initSytmPact(SytmvTenant sytmvTenant, String targetId, String groupId, String scid) {
        return (SytmPact) ISytFacade.println(new SytmPact(new SytmvPact(targetId, groupId, scid, sytmvTenant)));
    }


}
