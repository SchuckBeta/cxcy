package com.oseasy.com.pcore.modules.syt.manager.sub;

import com.google.common.collect.Lists;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.utils.SpringContextHolder;
import com.oseasy.com.pcore.modules.sys.dao.DictDao;
import com.oseasy.com.pcore.modules.sys.dao.SysConfigDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.SysConfig;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.vo.Dictv;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/4/19 0019.
 * 初始化系统配置.
 */
public class SytmConfig extends SupSytm<SytmvConfig> {
    private final static Logger logger = LoggerFactory.getLogger(SytmConfig.class);
    private static SysConfigDao sysConfigDao = SpringContextHolder.getBean(SysConfigDao.class);
    public SytmConfig(SytmvConfig sytmvo) {
        super(sytmvo);
    }

    @Override
    public String name() {
        return "系统配置";
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
        SysConfig psConfig = new SysConfig();
        SytmvTenant.setTenant(sytmvTenant, psConfig);
        List<SysConfig> sysConfigs = sysConfigDao.findListByTenant(psConfig);
        if(this.sytmvo.isReset()){
            SysConfig psysConfig = new SysConfig();
            psysConfig.setTenantId(sytmvTenant.getTenantId());
            sysConfigDao.delete(psysConfig);
        }

        for (SysConfig sysConfig: sysConfigs) {
            SysConfig cur = new SysConfig();
            BeanUtils.copyProperties(sysConfig, cur);
            cur.setId(IdGen.uuid());
            cur.setTenantId(sytmvTenant.getTenantId());
            cur.setCreateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setCreateDate(DateUtil.newDate());
            cur.setUpdateBy(new User(CoreIds.NCE_SYS_USER_SUPER.getId()));
            cur.setUpdateDate(DateUtil.newDate());
            sysConfigDao.insert(cur);
            this.sytmvo.getSysConfigs().add(cur);
        }

        after();
        return true;
    }

    @Override
    public boolean after() {
        return true;
    }
}
