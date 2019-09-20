package com.oseasy.act.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public class ActPnprovince implements IActPn {
    private static ActPnprovince actPnprovince = new ActPnprovince();
    public static ActPnprovince init(){
        return actPnprovince;
    }

    @Override
    public String pn() {
        return Sval.EmPn.NPROVINCE.getPrefix();
    }

    @Override
    public String pt(Sval.EmPt pt) {
        return pt.key();
    }

    @Override
    public String groupList(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwGroupList";
    }

    @Override
    public String groupForm(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwNprovinceGroupForm";
    }

    @Override
    public String actywList(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwNprovinceList";
    }

    @Override
    public String actywForm(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwNprovinceForm";
    }

    @Override
    public String actywProp(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwNprovincePropForm";
    }
}
