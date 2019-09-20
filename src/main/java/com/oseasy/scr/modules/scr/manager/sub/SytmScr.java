package com.oseasy.scr.modules.scr.manager.sub;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytm;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;
import com.oseasy.scr.modules.scr.dao.ScoRsetDao;
import com.oseasy.scr.modules.scr.dao.ScoRuleDao;
import com.oseasy.scr.modules.scr.entity.ScoRset;
import com.oseasy.scr.modules.scr.entity.ScoRule;
import com.oseasy.scr.modules.scr.entity.ScoRuleDetailMould;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmScr extends SupSytm<SytmvScr> {
    private final static Logger logger = LoggerFactory.getLogger(SytmScr.class);
    private static ScoRuleDao scoRuleDao = SpringContextHolder.getBean(ScoRuleDao.class);
    private static ScoRsetDao scoRsetDao = SpringContextHolder.getBean(ScoRsetDao.class);
    public SytmScr(SytmvScr sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "学分";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }

        if((this.sytmvo.getSytmvTenant() == null)){
            this.status.setMsg(name() + this.sytmvo.getSytmvTenant().getName() + "不能为空！");
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
        SytmvTenant sytmvTenant = this.sytmvo.getSytmvTenant();
        //初始化创新创业学分
        ScoRule scoRuleRoot = new ScoRule(CoreIds.NCE_SYS_TREE_ROOT.getId()+sytmvTenant.getTenantId(),new ScoRule(CoreIds.NCE_SYS_TREE_PROOT.getId())
        ,CoreIds.NCE_SYS_TREE_PROOT.getId()+StringUtil.DOTH,"创新创业学分",CoreIds.NCE_SYS_TREE_PROOT.getId(),CoreIds.NCE_SYS_TREE_PROOT.getId(),sytmvTenant.getTenantId(),"0");
        //初始化技能学分
        ScoRule scoRule = new ScoRule(CoreSval.getScoreSkillId()+sytmvTenant.getTenantId(),new ScoRule(scoRuleRoot.getId())
                ,scoRuleRoot.getParentIds()+scoRuleRoot.getId()+StringUtil.DOTH,"技能学分",CoreIds.NPR_SYS_TENANT_TPL.getId(),CoreIds.NCE_SYS_TREE_ROOT.getId(),sytmvTenant.getTenantId(),"0");
        //初始化技能学分模板数据
        ScoRuleDetailMould scoRuleDetailMould = new ScoRuleDetailMould(IdGen.uuid(),scoRule.getId(),0.00,0.00,0.00,0,"0","0","0","0"
                ,"0.500","0.500","100.000","1","1","2",sytmvTenant.getTenantId(),"0");
        //初始化学分规则全局规则
        ScoRset scoRset = new ScoRset(IdGen.uuid(),"1","2","0","0",sytmvTenant.getTenantId());
        //清除数据
        scoRuleDao.deleteByTenant(new ScoRule(sytmvTenant.getTenantId(),"1"));
        scoRuleDao.deleteScoRuleDetailMouldByScoRule(new ScoRuleDetailMould(sytmvTenant.getTenantId(),"1"));
        scoRsetDao.deleteByTenant(new ScoRset(sytmvTenant.getTenantId(),"1"));
        //插入数据
        scoRuleDao.insert(scoRuleRoot);
        scoRuleDao.insert(scoRule);
        scoRuleDao.insertScoRuleDetailMould(scoRuleDetailMould);
        scoRsetDao.insert(scoRset);

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
