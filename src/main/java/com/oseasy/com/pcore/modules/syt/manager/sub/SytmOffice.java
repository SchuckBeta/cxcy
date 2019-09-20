package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化顶级机构.
 */
public class SytmOffice extends SupSytm<SytmvOffice> {
    private final static Logger logger = LoggerFactory.getLogger(SytmOffice.class);
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

    public SytmOffice(SytmvOffice sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "顶级机构";
    }

    @Override
    public boolean before() {
        if(!super.before()){
            this.status.setStatus(false);
            this.status.setMsg(name() + "不能为Null！");
            logger.error(this.status.getMsg());
        }

        if(StringUtil.isEmpty(this.sytmvo.getId())){
            this.status.setStatus(false);
            this.status.setMsg(name() + "ID不能为空！");
            logger.error(this.status.getMsg());
        }

        if(this.sytmvo.getSytmvTenant() == null){
            this.status.setStatus(false);
            this.status.setMsg(name() + this.sytmvo.getSytmvTenant().getName() + "必填！");
            logger.error(this.status.getMsg());
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
        SysTenant sysTenant = sytmvTenant.getSysTenant();

        Office sysOffice = null;
        if((Sval.EmPn.NCENTER.getPrefix()).equals(sytmvTenant.getType())){
            sysOffice = officeDao.get(CoreIds.NCE_SYS_OFFICE_TOP.getId());
        }else if((Sval.EmPn.NPROVINCE.getPrefix()).equals(sytmvTenant.getType())){
            sysOffice = officeDao.get(CoreIds.NPR_SYS_OFFICE_TOP.getId());
        }else if((Sval.EmPn.NSCHOOL.getPrefix()).equals(sytmvTenant.getType())){
            sysOffice = officeDao.get(CoreIds.NSC_SYS_OFFICE_TOP.getId());
        }

        if(this.sytmvo.isReset()){
            Office office = new Office(sysOffice.getParent());
            office.setTenantId(sysTenant.getTenantId());
            officeDao.deleteWLByParentIds(office);
        }

        /**
         * 设置默认机构为1.
         */
//        Office root = new Office();
//        BeanUtils.copyProperties(officeDao.get(CoreIds.NCE_SYS_TREE_ROOT.getId()), root);
//        root.setId(IdGen.uuid());
//        if ((this.sytmvo.getPoffice() != null)) {
//            root.setParentIds("0,");
//        }
//        root.setTenantId(sytmvTenant.getTenantId());
//        root.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
//        root.setCreateDate(DateUtil.newDate());
//        officeDao.insert(root);
//        this.sytmvo.setPoffice(root);


        Office root = officeDao.get(CoreIds.NCE_SYS_TREE_ROOT.getId());
        this.sytmvo.setPoffice(root.getParent());
        Office cur = new Office();
        BeanUtils.copyProperties(sysOffice, cur);
        cur.setId(IdGen.uuid());
        if ((this.sytmvo.getPoffice() != null)) {
            cur.setParentIds(null);
            cur.setParent(this.sytmvo.getPoffice());
            cur.setParentIds("0,");
            cur.setCode(StringUtil.genNumStr(4));
        }else{
            cur.setParentIds("0,");
            cur.setCode(StringUtil.genNumStr(4));
        }
        cur.setRid(sysOffice.getId());
        cur.setName(sysTenant.getSchoolName());
        cur.setTenantId(sytmvTenant.getTenantId());
        cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
        cur.setCreateDate(DateUtil.newDate());
        officeDao.insert(cur);

        this.sytmvo.setOffice(cur);
        this.sytmvo.setOfficeTpl(sysOffice);

        System.out.println(name() + "处理完成...");

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
