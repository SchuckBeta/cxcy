package com.oseasy.scr.modules.scr.manager;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.modules.syt.manager.ISytFacade;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmTenant;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;
import com.oseasy.scr.modules.scr.manager.sub.SytmScr;
import com.oseasy.scr.modules.scr.manager.sub.SytmvScr;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytFacadeScr extends ISytFacade {
    public SytFacadeScr(String id) {
        super(id);
    }

    @Override
    public boolean initNsc(){
        SytmvTenant sytmvTenant2 = new SytmvTenant(this.getId());
        sytmvTenant2.setTname(Sval.EmPn.NSCHOOL.getRemark());
        sytmvTenant2.setType(Sval.EmPn.NSCHOOL.getPrefix());
        SytmTenant sytmTenant2 = initSytmTenant(sytmvTenant2);
        SytmScr sytmScr = initSytmScr(sytmTenant2.sytmvo);
        return true;
    }

    public static SytmScr initSytmScr(SytmvTenant sytmvTenant) {
        return (SytmScr) ISytFacade.println(new SytmScr(new SytmvScr(sytmvTenant)));
    }

    public static SytmTenant initSytmTenant(SytmvTenant sytmvTenant) {
        return (SytmTenant) ISytFacade.println(new SytmTenant(sytmvTenant));
    }
}
