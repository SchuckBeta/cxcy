package com.oseasy.cms.modules.cms.manager;

import com.oseasy.cms.modules.cms.manager.sub.SytmSite;
import com.oseasy.cms.modules.cms.manager.sub.SytmvSite;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.syt.manager.ISytFacade;
import com.oseasy.com.pcore.modules.syt.manager.sub.*;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytFacadeCms extends ISytFacade {
    public SytFacadeCms(String id) {
        super(id);
    }

    @Override
    public boolean initNsc(){
        SytmvTenant sytmvTenant2 = new SytmvTenant(this.getId());
        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getPrefix());
        SytmTenant sytmTenant2 = initSytmTenant(sytmvTenant2);
        SytmSite sytmSite = initSytmSite(sytmTenant2.sytmvo);
        return true;
    }

    public static SytmSite initSytmSite(SytmvTenant sytmvTenant) {
        return (SytmSite) ISytFacade.println(new SytmSite(new SytmvSite(sytmvTenant)));
    }

    public static SytmTenant initSytmTenant(SytmvTenant sytmvTenant) {
        return (SytmTenant) ISytFacade.println(new SytmTenant(sytmvTenant));
    }
}
