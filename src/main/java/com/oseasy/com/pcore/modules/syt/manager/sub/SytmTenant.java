package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.dao.SysTenantDao;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2019/4/19 0019.
 */
public class SytmTenant extends SupSytm<SytmvTenant> {
    private final static Logger logger = LoggerFactory.getLogger(SytmTenant.class);
    private static SysTenantDao sysTenantDao = SpringContextHolder.getBean(SysTenantDao.class);

    public SytmTenant() {
    }

    public SytmTenant(SytmvTenant sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "租户";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setStatus(false);
            this.status.setMsg(name() + "不能为Null！");
            logger.error(this.status.getMsg());
            return false;
        }

        if(StringUtil.isEmpty(this.sytmvo.getTenantId())){
            this.status.setStatus(false);
            this.status.setMsg(name() + "标识不能为空！");
            logger.error(this.status.getMsg());
            return false;
        }

        this.sytmvo.setName(name());
        return true;
    }

    @Override
    public boolean run() {
        if(!before()){
            this.status.setStatus(false);
            this.status.setMsg(name() + "参数校验失败！");
            logger.error(this.status.getMsg());
            return false;
        }
        System.out.println(name() + "处理中...");

        String tplId = null;
        if((Sval.EmPn.NCENTER.getPrefix()).equals(this.sytmvo.getType())){
            tplId = CoreIds.NCE_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(this.sytmvo.getType())){
            tplId = CoreIds.NPR_SYS_TENANT_TPL.getId();
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(this.sytmvo.getType())){
            tplId = CoreIds.NSC_SYS_TENANT_TPL.getId();
        }else{
            this.status.setStatus(false);
            this.status.setMsg(name() + "找不到租户模板！");
            logger.error(this.status.getMsg());
            return false;
        }
        this.sytmvo.setSysTenantTpl(sysTenantDao.get(tplId));

        /**
         * 如果租户不存在，使用模仁的模板租户.
         */
        SysTenant sysTenant = getTenant();
        if(sysTenant == null){
            sysTenant = this.sytmvo.getSysTenantTpl();
        }

        if(sysTenant == null){
            this.status.setStatus(false);
            this.status.setMsg(name() + "模板数据不存在 id = " + sysTenant.getId());
            logger.error(this.status.getMsg());
            return false;
        }

        if(this.sytmvo.isReset()){
            //TODO CHENHAO
        }

        if((CoreIds.NCE_SYS_TENANT_TPL.getId()).equals(sysTenant.getId())
            || (CoreIds.NPR_SYS_TENANT_TPL.getId()).equals(sysTenant.getId())
            || (CoreIds.NSC_SYS_TENANT_TPL.getId()).equals(sysTenant.getId())){
            SysTenant cur = new SysTenant();
            BeanUtils.copyProperties(sysTenant, cur);
            cur.setId(IdGen.uuid());
            cur.setSchoolName(this.sytmvo.getTname());
            cur.setTenantId(this.sytmvo.getTenantId());
            cur.setIsTpl(CoreSval.Const.NO);
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            sysTenantDao.insert(cur);
            sysTenant = cur;
        }else{
            sysTenant.setIsTpl(CoreSval.Const.NO);
            sysTenant.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            sysTenant.setUpdateDate(DateUtil.newDate());
            sysTenantDao.update(sysTenant);
        }

        this.sytmvo.setSysTenant(sysTenant);

        System.out.println(name() + "处理完成...");

        after();
        return true;
    }

    @Override
    public boolean pushTpl() {
        if(!before()){
            this.status.setStatus(false);
            this.status.setMsg(name() + "推送参数校验失败！");
            logger.error(this.status.getMsg());
            return false;
        }
        System.out.println(name() + "处理中...");

        /**
         * 如果租户不存在，使用模仁的模板租户.
         */
        SysTenant sysTenant = sysTenantDao.getByTenant(this.sytmvo.getTenantId());
        if(sysTenant == null){
            this.status.setStatus(false);
            this.status.setMsg(name() + "租户获取失败！");
            logger.error(this.status.getMsg());
            return false;
        }
        this.sytmvo.setSysTenant(sysTenant);

        /**
         * 获取模板租户.
         */
        SysTenant target = sysTenantDao.getByTenant(this.sytmvo.getTenantTplId());
        if(target == null){
            this.status.setStatus(false);
            this.status.setMsg(name() + "模板租户不存在！");
            logger.error(this.status.getMsg());
            return false;
        }
        this.sytmvo.setSysTenantTpl(target);

        System.out.println(name() + "处理完成...");

        after();
        return super.pushTpl();
    }

    /**
     * 获取当前租户.
     * @return SysTenant
     */
    private SysTenant getTenant() {
        String tenantId = this.sytmvo.getTenantId();
        /**
         * 根据租户标识检查是否存在这个租户。
         * 如果存在，返回当前租户.
         */
        SysTenant curSysTenant = sysTenantDao.getByTenant(tenantId);
        if(curSysTenant != null){
            this.sytmvo.setId(curSysTenant.getId());
            return curSysTenant;
        }

        /**
         * 租户不存在，根据id属性查询租户是否存在。
         * 如果id不存在，使用默认的租户ID创建租户.
         * 如果id存在，则为修改操作.
         */
        String id = this.sytmvo.getId();
        if(StringUtil.isNotEmpty(id)){
            curSysTenant = sysTenantDao.get(id);
        }

//        /**
//         * 如果租户不存在，使用模仁的模板租户.
//         */
//        if(curSysTenant == null){
//            if((Sval.EmPn.NCENTER.getKey()).equals(this.sytmvo.getType())){
//                id = CoreIds.NCE_SYS_TENANT_TPL.getId();
//            }else if((Sval.EmPn.NPROVINCE.getKey()).equals(this.sytmvo.getType())){
//                id = CoreIds.NPR_SYS_TENANT_TPL.getId();
//            }else if((Sval.EmPn.NSCHOOL.getKey()).equals(this.sytmvo.getType())){
//                id = CoreIds.NSC_SYS_TENANT_TPL.getId();
//            }
//            curSysTenant = sysTenantDao.get(id);
//        }
        return curSysTenant;
    }

    @Override
    public boolean after() {
        return true;
    }
}
