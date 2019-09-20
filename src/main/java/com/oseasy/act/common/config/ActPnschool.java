package com.oseasy.act.common.config;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.common.utils.ICorePn;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;

/**
 * Created by Administrator on 2019/4/18 0018.
 */
public class ActPnschool implements IActPn {
    private static ActPnschool actPnschool = new ActPnschool();
    public static ActPnschool init(){
        return actPnschool;
    }

    @Override
    public String pn() {
        return Sval.EmPn.NSCHOOL.getPrefix();
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
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwGroupForm";
    }

    @Override
    public String actywList(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwList";
    }

    @Override
    public String actywForm(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwForm";
    }

    @Override
    public String actywProp(Sval.EmPt pt) {
        return ActSval.path.vms(ActSval.ActEmskey.ACTYW.k()) + "actYwPropForm";
    }
}
