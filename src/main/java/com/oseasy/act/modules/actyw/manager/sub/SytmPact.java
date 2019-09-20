


package com.oseasy.act.modules.actyw.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.dao.*;
import com.oseasy.act.modules.actyw.entity.*;
import com.oseasy.act.modules.actyw.service.ActYwGroupRelationService;
import com.oseasy.act.modules.actyw.service.ActYwPscrelService;
import com.oseasy.act.modules.actyw.tool.process.vo.*;
import com.oseasy.act.modules.actyw.vo.ActYwGnodev;
import com.oseasy.act.modules.pro.dao.ProProjectDao;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.DictDao;
import com.oseasy.com.pcore.modules.sys.dao.MenuDao;
import com.oseasy.com.pcore.modules.sys.dao.RoleDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.vo.SysNoType;
import com.oseasy.com.pcore.modules.sys.vo.SysNodeTool;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.manager.sub.SupSytm;
import com.oseasy.com.pcore.modules.syt.manager.sub.SytmvTenant;
import com.oseasy.com.pcore.modules.syt.vo.SytRole;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 流程推送.
 */
public class SytmPact extends SupSytm<SytmvPact> {
    public final static Logger logger = Logger.getLogger(SytmPact.class);
    private static ActYwDao actYwDao = SpringContextHolder.getBean(ActYwDao.class);
    private static ActYwGroupDao actYwGroupDao = SpringContextHolder.getBean(ActYwGroupDao.class);
    private static ActYwGnodeDao actYwGnodeDao = SpringContextHolder.getBean(ActYwGnodeDao.class);
    private static ActYwGthemeDao actYwGthemeDao = SpringContextHolder.getBean(ActYwGthemeDao.class);
    private static ActYwGtimeDao actYwGtimeDao = SpringContextHolder.getBean(ActYwGtimeDao.class);
    private static ActYwGroleDao actYwGroleDao = SpringContextHolder.getBean(ActYwGroleDao.class);
    private static ActYwGstatusDao actYwGstatusDao = SpringContextHolder.getBean(ActYwGstatusDao.class);
    private static ActYwGformDao actYwGformDao = SpringContextHolder.getBean(ActYwGformDao.class);
    private static ActYwGassignDao actYwGassignDao = SpringContextHolder.getBean(ActYwGassignDao.class);
    private static ActYwYearDao actYwYearDao = SpringContextHolder.getBean(ActYwYearDao.class);
    private static ProProjectDao proProjectDao = SpringContextHolder.getBean(ProProjectDao.class);
    private static ActYwEtAssignRuleDao actYwEtAssignRuleDao = SpringContextHolder.getBean(ActYwEtAssignRuleDao.class);
    private static ActYwEtAuditNumDao actYwEtAuditNumDao = SpringContextHolder.getBean(ActYwEtAuditNumDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);

    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static DictService dictService = SpringContextHolder.getBean(DictService.class);
    private static ActYwPscrelService actYwPscrelService = SpringContextHolder.getBean(ActYwPscrelService.class);

    public SytmPact(SytmvPact sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "流程";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setMsg(name() + "不能为Null！");
        }
        if(StringUtil.isEmpty(this.sytmvo.getId())){
            this.status.setMsg(name() + "ID不能为空！");
        }

        if((this.sytmvo.getSytmvTenant() == null)){
            this.status.setMsg(name() + "sytmvTenant 不能为空！");
        }

        if(StringUtil.isEmpty(this.sytmvo.getSytmvTenant().getType())){
            this.status.setMsg(name() + "租户类型不能为空！");
        }

        if((this.sytmvo.getSytmvTenant().getSysTenant() == null)){
            this.status.setMsg(name() + "目标租户不能为空！");
        }

        if((this.sytmvo.getSytmvTenant().getSysTenantTpl() == null)){
            this.status.setMsg(name() + "租户模板不能为空！");
        }

        return super.before() && StringUtil.isNotEmpty(this.sytmvo.getId());
    }

    @Override
    public boolean run() {
        if(!before()){
            return false;
        }

        System.out.println(name() + "处理中...");
        System.out.println(name() + "ActYwGroup处理中...");
        SytmvTenant sytmvTenant = this.sytmvo.getSytmvTenant();
        SysTenant sysTenant = sytmvTenant.getSysTenantTpl();

        ActYwGroup pactYwGroup = new ActYwGroup();
        pactYwGroup.setId(this.sytmvo.getId());
        pactYwGroup.setTenantId(sytmvTenant.getTenantTplId());
        ActYwGroup actYwGroup = actYwGroupDao.getByTenant(pactYwGroup);

        if(this.sytmvo.isReset()){}

        ActYwGroup cur = new ActYwGroup();
        BeanUtils.copyProperties(actYwGroup, cur);
        cur.setId(IdGen.uuid());
        cur.setSort(1);
        actYwGroup.setTemp(true);
        cur.setNtype(CoreSval.Const.YES);
        cur.setIsPush(true);
        cur.setKeyss(SysNodeTool.genByKeyss(SysNoType.NO_FLOW));
        cur.setVersion(ActYwGroup.GROUP_VERSION);
        cur.setAuthor(CoreIds.NCE_SYS_USER_SUPER.getId() + StringUtil.LINE_M + CoreIds.NCE_SYS_USER_SUPER.getRemark());
        cur.setStatus(ActYwGroup.GROUP_DEPLOY_0);
        cur.setTenantId(sytmvTenant.getTenantId());
        cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setCreateDate(DateUtil.newDate());
        actYwGroupDao.insert(cur);

        this.sytmvo.setGroup(cur);
        this.sytmvo.setGroupTpl(actYwGroup);

        System.out.println(name() + "ActYwGroup处理完成...");
        System.out.println(name() + "ActYwGnode处理中...");

        ActYwGnode pactYwGnode = new ActYwGnode();
        pactYwGnode.setTenantId(sysTenant.getId());
        pactYwGnode.setGroup(new ActYwGroup(this.sytmvo.getGroupTpl().getId()));
        List<ActYwGnode> actYwGnodes = actYwGnodeDao.findListBytGroup(pactYwGnode);

        List<ActYwGnode> curNms = Lists.newArrayList();
        List<ActYwGnodev> curGnodevs = Lists.newArrayList();
        sortList(sytmvTenant.getTenantId(), this.sytmvo.getGroup(), curNms, curGnodevs, ActYwGnodev.gen(actYwGnodes), CoreIds.NCE_SYS_TREE_ROOT.getId());
        for (ActYwGnode curg: updateGnodeParent(curNms, curGnodevs)) {
            actYwGnodeDao.insert(curg);
            this.sytmvo.getGnodes().add(curg);
        }
//        this.sytmvo.setGroup(updateGroup(cur, curGnodevs));
//        actYwGroupDao.update(this.sytmvo.getGroup());

        System.out.println(name() + "ActYwGnode处理完成...");
        System.out.println(name() + "ActYwGtheme处理中...");

        /**
         * 检查是否已经初始化主题数据.
         */
        ActYwGtheme pckactYwGtheme = new ActYwGtheme();
        pckactYwGtheme.setTenantId(sytmvTenant.getTenantId());
        List<ActYwGtheme> ckactYwGthemes = actYwGthemeDao.findList(pckactYwGtheme);
        if(StringUtil.checkEmpty(ckactYwGthemes)){
            ActYwGtheme pactYwGtheme = new ActYwGtheme();
            pactYwGtheme.setTenantId(sysTenant.getId());
            List<ActYwGtheme> actYwGthemes = actYwGthemeDao.findList(pactYwGtheme);

            for (ActYwGtheme curg: actYwGthemes) {
                curg.setId(IdGen.uuid());
                curg.setTenantId(sytmvTenant.getTenantId());
                curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                curg.setCreateDate(DateUtil.newDate());
                actYwGthemeDao.insert(curg);
                this.sytmvo.getGthemes().add(curg);
            }
        }else{
            this.sytmvo.setGthemes(ckactYwGthemes);
        }

        System.out.println(name() + "ActYwGtheme处理完成...");
        System.out.println(name() + "ActYw处理中...");
        ActYw pactYw = new ActYw();
        pactYw.setTenantId(sysTenant.getId());
        pactYw.setGroup(new ActYwGroup(this.sytmvo.getGroupTpl().getId()));
        List<ActYw> actYws = actYwDao.findList(pactYw);

        ActYwGtime pactYwGtime = new ActYwGtime();
        pactYwGtime.setTenantId(sysTenant.getId());
        pactYwGtime.setGrounpId(this.sytmvo.getGroupTpl().getId());
        List<ActYwGtime> actYwGtimes = actYwGtimeDao.findList(pactYwGtime);

        for (ActYw curg: actYws) {
            ActYwYear pactYwYear = new ActYwYear();
            pactYwYear.setTenantId(sysTenant.getId());
            pactYwYear.setActywId(curg.getId());
            List<ActYwYear> actYwYears = actYwYearDao.findList(pactYwYear);
            /**
             * 如果没有获取到年份数据，不初始化项目流程.N
             */
            if((curg.getProProject() == null) || StringUtil.checkEmpty(actYwYears)){
                continue;
            }

            System.out.println(name() + "ProProject处理中...");
            ProProject curp = curg.getProProject();
            curp.setId(IdGen.uuid());
            curp  = updateProProject(curg, curp, sytmvTenant.getTenantId());
            curp.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curp.setCreateDate(DateUtil.newDate());
            proProjectDao.insert(curp);
            System.out.println(name() + "ProProject处理完成...");

            System.out.println(name() + "Dict处理中...");
//            List<Dict> cdicts = updateProDict(curp, sytmvTenant.getTenantId());
//            cdicts = Lists.newArrayList();
//            if(StringUtil.checkNotEmpty(cdicts)){
//                for (Dict curdict:cdicts) {
//                    curdict.setTenantIds(Lists.newArrayList());
//                    curdict.getTenantIds().add(sytmvTenant.getTenantId());
//                    dictService.ajaxSaveDictToAllTenant(curdict);
//                }
//            }
            System.out.println(name() + "Dict处理完成...");


            curg.setId(IdGen.uuid());
            curg.setTenantId(sytmvTenant.getTenantId());
            curg.setIsDeploy(false);
            curg.setIsPreRelease(false);
            curg.setIsShowAxis(false);
            curg.setGroup(this.sytmvo.getGroup());
            curg.setGroupId(this.sytmvo.getGroup().getId());
            curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curg.setProProject(curp);
            curg.setRelId(curp.getId());
            curg.setKeyType(FormTheme.getById(this.sytmvo.getGroup().getTheme()).getKey());
            curg.setCreateDate(DateUtil.newDate());
            actYwDao.insert(curg);
            this.sytmvo.setActYw(curg);

            System.out.println(name() + "ActYwYear处理中...");
            List<ActYwYear> years = Lists.newArrayList();
            for (ActYwYear cury: actYwYears) {
                cury.setRid(cury.getId());
                cury.setId(IdGen.uuid());
                cury.setActywId(this.sytmvo.getActYw().getId());
                cury.setTenantId(sytmvTenant.getTenantId());
                cury.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                cury.setCreateDate(DateUtil.newDate());
                actYwYearDao.insert(cury);
                years.add(cury);

                System.out.println(name() + "ActYwGtime处理中...");
                for (ActYwGtime curgt: actYwGtimes) {
                    if(!(curgt.getYearId()).equals(cury.getRid())){
                        continue;
                    }
                    curgt.setId(IdGen.uuid());
                    curgt.setYearId(cury.getId());
                    curgt.setProjectId(curp.getId());
                    curgt.setTenantId(sytmvTenant.getTenantId());
                    curgt.setGrounpId(this.sytmvo.getGroup().getId());
                    curgt.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
                    curgt.setCreateDate(DateUtil.newDate());
                    curgt  = updateGtime(curgt, curGnodevs);
                    actYwGtimeDao.insert(curgt);
                    this.sytmvo.getGtimes().add(curgt);
                }
                System.out.println(name() + "ActYwGtime处理完成...");
            }
            this.sytmvo.getActYw().setYears(years);
            System.out.println(name() + "ActYwYear处理完成...");
        }

        System.out.println(name() + "ActYw处理完成...");
        System.out.println(name() + "ActYwGstatus处理中...");
        ActYwGstatus pactYwGstatus = new ActYwGstatus();
        pactYwGstatus.setTenantId(sysTenant.getId());
        pactYwGstatus.setGroup(new ActYwGroup(this.sytmvo.getGroupTpl().getId()));
        List<ActYwGstatus> actYwGstatuss = actYwGstatusDao.findList(pactYwGstatus);

        for (ActYwGstatus curg: actYwGstatuss) {
            if((curg.getStatus() == null) || StringUtil.isEmpty(curg.getStatus().getId())){
                continue;
            }
            curg.setId(IdGen.uuid());
            curg.setTenantId(sytmvTenant.getTenantId());
            curg.setGroup(new ActYwGroup(this.sytmvo.getGroup().getId()));
            curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curg.setCreateDate(DateUtil.newDate());
            curg  = updateGstatus(curg, curGnodevs);
            actYwGstatusDao.insert(curg);
            this.sytmvo.getGstatuss().add(curg);
        }

        System.out.println(name() + "ActYwGstatus处理完成...");
        System.out.println(name() + "ActYwGrole处理中...");
        ActYwGrole pactYwGrole = new ActYwGrole();
        pactYwGrole.setTenantId(sysTenant.getId());
        pactYwGrole.setGroup(new ActYwGroup(this.sytmvo.getGroupTpl().getId()));
        List<ActYwGrole> actYwGroles = actYwGroleDao.findList(pactYwGrole);

        /**
         * 查找校模板角色（模板租户）.
         */
        Role nscprole = new Role();
        nscprole.setTenantId(CoreIds.NSC_SYS_TENANT_TPL.getId());
        List<Role> nsctplRoles = roleDao.findRoles(nscprole);
        /**
         * 查找校租户角色（当前租户）.
         */
        Role scprole = new Role();
        scprole.setTenantId(this.sytmvo.getTargetId());
        List<Role> targetRoles = roleDao.findListByRinit(scprole);
        /**
         * 设计推送流程选择角色时只能选rinit=true的角色.
         * 查找原始租户角色（模板租户）.
         */
        Role ptrole = new Role();
        ptrole.setTenantId(sysTenant.getId());
        List<Role> troles = roleDao.findListByRinit(ptrole);

        List<SytRole> sytRoles = SytRole.genSytRole(sytmvTenant.getType(), targetRoles, troles, nsctplRoles);
        for (ActYwGrole curg: actYwGroles) {
            if((curg.getRole() == null) || StringUtil.isEmpty(curg.getRole().getId())){
                continue;
            }
            curg.setId(IdGen.uuid());
            boolean cflag = true;
            for (SytRole curRole: sytRoles) {
                if((curg.getRole().getId()).equals(curRole.getNceRid()) || (curg.getRole().getId()).equals(curRole.getNtplRid())){
                    curg.setRole(curRole.getTrole());
                    cflag = false;
                    break;
                }
            }
            if(cflag){
                logger.info("当前ActYwGrole对应的角色不存在,ActYwGrole.role.id = "+curg.getRole().getId());
                continue;
            }
            curg.setTenantId(sytmvTenant.getTenantId());
            curg.setGroup(new ActYwGroup(this.sytmvo.getGroup().getId()));
            curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curg.setCreateDate(DateUtil.newDate());
            curg  = updateGrole(this.sytmvo.getGroup(), curg, curGnodevs, sytRoles);
            actYwGroleDao.insert(curg);
            this.sytmvo.getGroles().add(curg);
        }
        this.sytmvo.setGroup(updateGroup(this.sytmvo.getGroup(), curGnodevs));
        actYwGroupDao.update(this.sytmvo.getGroup());


        System.out.println(name() + "ActYwGrole处理完成...");
        System.out.println(name() + "ActYwGform处理中...");
        ActYwGform pactYwGform = new ActYwGform();
        pactYwGform.setTenantId(sysTenant.getId());
        pactYwGform.setGroup(new ActYwGroup(this.sytmvo.getGroupTpl().getId()));
        List<ActYwGform> actYwGforms = actYwGformDao.findList(pactYwGform);

        for (ActYwGform curg: actYwGforms) {
            if((curg.getForm() == null) || StringUtil.isEmpty(curg.getForm().getId())){
                continue;
            }
            curg.setId(IdGen.uuid());
            curg.setTenantId(sytmvTenant.getTenantId());
            curg.setGroup(new ActYwGroup(this.sytmvo.getGroup().getId()));
            curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curg.setCreateDate(DateUtil.newDate());
            curg  = updateGform(curg, curGnodevs);
            actYwGformDao.insert(curg);
            this.sytmvo.getGforms().add(curg);
        }

        System.out.println(name() + "ActYwGform处理完成...");
        System.out.println(name() + "ActYwGgassign处理中...");
        ActYwGassign pactYwGassign = new ActYwGassign();
        pactYwGassign.setTenantId(sysTenant.getId());
        List<ActYwGassign> actYwGassigns = actYwGassignDao.findListByTenant(pactYwGassign);

        for (ActYwGassign curg: actYwGassigns) {
            curg.setId(IdGen.uuid());
            curg.setTenantId(sytmvTenant.getTenantId());
            curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            curg.setCreateDate(DateUtil.newDate());
            curg  = updateGassign(curg, curGnodevs);
            actYwGassignDao.insert(curg);
            this.sytmvo.getGassigns().add(curg);
        }

        System.out.println(name() + "ActYwGgassign处理完成...");
        System.out.println(name() + "ActYwEtAssignRule处理中...");
//        ActYwEtAssignRule pactYwEtAssignRule = new ActYwEtAssignRule();
//        pactYwEtAssignRule.setTenantId(sysTenant.getId());
//        List<ActYwEtAssignRule> actYwEtAssignRules = actYwEtAssignRuleDao.findList(pactYwEtAssignRule);
//
//        for (ActYwEtAssignRule curg: actYwEtAssignRules) {
//            curg.setId(IdGen.uuid());
//            curg.setTenantId(sytmvTenant.getTenantId());
//        curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
//        curg.setCreateDate(DateUtil.newDate());
//            actYwEtAssignRuleDao.insert(curg);
//            this.sytmvo.getEtAssignRules().add(curg);
//        }

        System.out.println(name() + "ActYwEtAssignRule处理完成...");
        System.out.println(name() + "ActYwEtAuditNum处理中...");
//        ActYwEtAuditNum pactYwEtAuditNum = new ActYwEtAuditNum();
//        pactYwEtAuditNum.setTenantId(sysTenant.getId());
//        List<ActYwEtAuditNum> actYwEtAuditNums = actYwEtAuditNumDao.findList(pactYwEtAuditNum);
//
//        for (ActYwEtAuditNum curg: actYwEtAuditNums) {
//            curg.setId(IdGen.uuid());
//            curg.setTenantId(sytmvTenant.getTenantId());
//        curg.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
//        curg.setCreateDate(DateUtil.newDate());
//            actYwEtAuditNumDao.insert(curg);
//            this.sytmvo.getEtAuditNums().add(curg);
//        }

        System.out.println(name() + "ActYwEtAuditNum处理完成...");

        System.out.println(name() + "ActYwPscrelService处理中...");
        ActYwPscrel actYwPscrel = actYwPscrelService.get(this.sytmvo.getScid());
        actYwPscrel.setSchoolActywId(this.sytmvo.getActYw().getId());
        actYwPscrel.setIspushed(CoreSval.Const.YES);
        actYwPscrelService.save(actYwPscrel);
        System.out.println(name() + "ActYwPscrelService处理完成...");
        System.out.println(name() + "处理完成...");

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }


    public static ActYwGnode genActYwGnode(String tid, ActYwGroup group, ActYwGnodev gv, ActYwGnodev parent) {
        ActYwGnode cur = new ActYwGnode();
        BeanUtils.copyProperties(gv, cur);
        cur.setId(IdGen.uuid());
        gv.setNid(cur.getId());
        cur.setRid(gv.getId());
        cur.setParent(parent);
        cur.setGroup(group);
        cur.setTenantId(tid);
        cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setCreateDate(DateUtil.newDate());
        cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setUpdateDate(DateUtil.newDate());
        return cur;
    }

    public static void sortList(String tid, ActYwGroup group, List<ActYwGnode> nlist, List<ActYwGnodev> list, List<ActYwGnodev> sourcelist, String parentId) {
        for (int i=0; i<sourcelist.size(); i++) {
            ActYwGnodev e = sourcelist.get(i);

            if((e.getParent() != null)){
                System.out.println(e.getParent().getId() + " = " + parentId);
            }

            if ((e.getParent() != null) && (e.getParent().getId() != null) && (e.getParent().getId().equals(parentId))) {
                list.add(e);
                nlist.add(genActYwGnode(tid, group, e, ActYwGnodev.gen(e.getParent())));

                // 判断是否还有子节点, 有则继续获取子节点
                for (int j=0; j<sourcelist.size(); j++) {
                    ActYwGnodev child = sourcelist.get(j);
                    if ((child.getParent() != null) && (child.getParent().getId() != null) && (child.getParent().getId().equals(e.getId()))) {
                        sortList(tid, group, nlist, list, sourcelist, e.getId());
                        break;
                    }
                }
            }
        }
    }

    public static List<ActYwGnode> updateGnodeParent(List<ActYwGnode> ms, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv: mvs) {
            for (ActYwGnode m : ms) {
                if((m.getParentIds()).contains(StringUtil.DOTH + mv.getId() + StringUtil.DOTH)){
                    m.setParentIds(m.getParentIds().replace((StringUtil.DOTH + mv.getId() + StringUtil.DOTH), (StringUtil.DOTH + mv.getNid() + StringUtil.DOTH)));
                }

                if(m.getParent() == null){
                    continue;
                }

                if((m.getParent().getId()).equals(mv.getId())){
                    m.getParent().setId(mv.getNid());
                }

                if(StringUtil.isEmpty(m.getPreId())){
                    continue;
                }

                if((m.getPreId()).equals(mv.getId())){
                    m.setPreId(mv.getNid());
                }
            }
        }
        return ms;
    }

    public static ActYwGroup updateGroup(ActYwGroup group, List<ActYwGnodev> mvs) {
        String html = group.getUiHtml();
        String json = group.getUiJson();
        for (ActYwGnodev mv : mvs) {
            html = html.replaceAll(StringUtil.SHUANGYH + mv.getId() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + mv.getNid() + StringUtil.SHUANGYH);
            json = json.replaceAll(StringUtil.SHUANGYH + mv.getId() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + mv.getNid() + StringUtil.SHUANGYH);
        }

//        html = html.replaceAll("\\(校\\)", StringUtil.EMPTY);
//        json = json.replaceAll("\\(校\\)", StringUtil.EMPTY);
//        html = html.replaceAll("\\(省/校\\)", StringUtil.EMPTY);
//        json = json.replaceAll("\\(省/校\\)", StringUtil.EMPTY);
        group.setUiHtml(html);
        group.setUiJson(json);
        return group;
    }

    public static ActYwGtime updateGtime(ActYwGtime g, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv : mvs) {
            if((mv.getId()).equals(g.getGnodeId()) || ((g.getGnode() != null) && (mv.getId()).equals(g.getGnode().getId()))){
                g.getGnode().setId(mv.getNid());
                g.setGnodeId(mv.getNid());
                break;
            }
        }
        return g;
    }

    public static ActYwGstatus updateGstatus(ActYwGstatus g, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv : mvs) {
            if((g.getGnode() != null) && ((mv.getId()).equals(g.getGnode().getId()))){
                g.getGnode().setId(mv.getNid());
                break;
            }
        }
        return g;
    }

    public static ActYwGrole updateGrole(ActYwGroup group, ActYwGrole g, List<ActYwGnodev> mvs, List<SytRole> sytRoles) {
        for (ActYwGnodev mv : mvs) {
            if((g.getGnode() != null) && ((mv.getId()).equals(g.getGnode().getId()))){
                g.getGnode().setId(mv.getNid());
            }
        }

        if((g.getRole() == null) || StringUtil.isEmpty(g.getRole().getId()) || StringUtil.isEmpty(g.getRole().getRid())){
            return g;
        }

        if(StringUtil.isEmpty(group.getUiHtml()) || StringUtil.isEmpty(group.getUiJson())){
            return g;
        }


        group.setUiHtml(group.getUiHtml().replaceAll(StringUtil.SHUANGYH + g.getRole().getRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + g.getRole().getId() + StringUtil.SHUANGYH));
        group.setUiJson(group.getUiJson().replaceAll(StringUtil.SHUANGYH + g.getRole().getRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + g.getRole().getId() + StringUtil.SHUANGYH));


        /**
         * 处理固定的角色ID.
         * 运营中心角色转换为校对应的角色.
         */
        if(StringUtil.checkNotEmpty(sytRoles)){
            for(SytRole tr :sytRoles){
                if((tr.getNceRid()).equals(g.getRole().getId())){
                    group.setUiHtml(group.getUiHtml().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    group.setUiJson(group.getUiJson().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    break;
                }
                if((tr.getNtplRid()).equals(g.getRole().getId())){
                    group.setUiHtml(group.getUiHtml().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    group.setUiJson(group.getUiJson().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    break;
                }
                if((tr.getNscRid()).equals(g.getRole().getId())){
                    group.setUiHtml(group.getUiHtml().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    group.setUiJson(group.getUiJson().replaceAll(StringUtil.SHUANGYH + tr.getNceRid() + StringUtil.SHUANGYH, StringUtil.SHUANGYH + tr.getNscRid() + StringUtil.SHUANGYH));
                    break;
                }
            }
        }
        return g;
    }

    public static ActYwGform updateGform(ActYwGform g, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv : mvs) {
            if((g.getGnode() != null) && ((mv.getId()).equals(g.getGnode().getId()))){
                g.getGnode().setId(mv.getNid());
                break;
            }
        }
        return g;
    }

    public static ActYwGassign updateGassign(ActYwGassign g, List<ActYwGnodev> mvs) {
        for (ActYwGnodev mv : mvs) {
            if((mv.getId()).equals(g.getGnodeId())){
                g.setGnodeId(mv.getNid());
                break;
            }
        }
        return g;
    }

    /**
     * 更新流程配置.
     * @param ay 项目流程
     * @param g 流程配置
     * @param tid 租户ID
     * @return ProProject
     */
    public static ProProject updateProProject(ActYw ay, ProProject g, String tid) {
        if((g == null) || StringUtil.isEmpty(g.getMenuRid())){
            return g;
        }

        Menu pmenu = new Menu();
        pmenu.setRid(g.getMenuRid());
        pmenu.setTenantId(tid);
        Menu m = menuDao.getByRid(pmenu);
        if(m != null){
            g.setMenuRid(m.getId());
            /**
             * 如果流程取消发布，需要删除菜单.
             */
            if (!ay.getIsDeploy()) {
                m.setTenantId(tid);
                //menuDao.deleteWLByPidTenant(m);
            }
        }
        return g;
    }

    /**
     * 处理项目大赛流程需要的字典.
     * @param g ProProject
     * @param tid String
     * @return List
     */
    public static List<Dict> updateProDict(ProProject g, String tid) {
        List<Dict> dicts = Lists.newArrayList();
        if((g == null)){
            return dicts;
        }

        if(StringUtil.isNotEmpty(g.getProType())){
            dicts.addAll(DictUtils.getDictList(FlowProjectType.KEY_ACT_PROJECT_TYPE));
        }

        if(StringUtil.isNotEmpty(g.getType())){
            if((FlowProjectType.PMT_XM.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_XM.getType().getKey()));
            }else if((FlowProjectType.PMT_DASAI.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_DASAI.getType().getKey()));
            }else if((FlowProjectType.PMT_SCORE.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_SCORE.getType().getKey()));
            }
        }

        if(StringUtil.isNotEmpty(g.getProCategory())){
            if((FlowProjectType.PMT_XM.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_XM.getCategory().getKey()));
            }else if((FlowProjectType.PMT_DASAI.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_DASAI.getCategory().getKey()));
            }else if((FlowProjectType.PMT_SCORE.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_SCORE.getCategory().getKey()));
            }
        }

        if(StringUtil.isNotEmpty(g.getLevel())){
            if((FlowProjectType.PMT_XM.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_XM.getLevel().getKey()));
            }else if((FlowProjectType.PMT_DASAI.key()).equals(g.getProType())){
                dicts.addAll(DictUtils.getDictList(FlowProjectType.PMT_DASAI.getLevel().getKey()));
            }
        }

        for (Dict cur:dicts) {
            cur.setTenantId(tid);
            cur.setIsPush(true);
        }
        return dicts;
    }


}
