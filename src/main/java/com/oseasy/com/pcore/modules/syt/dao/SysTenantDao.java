package com.oseasy.com.pcore.modules.syt.dao;



import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户表Dao接口
 * Created by PW on 2019/3/21.
 */
@MyBatisDao
public interface SysTenantDao extends CrudDao<SysTenant> {
    public SysTenant findSysTenant(SysTenant sysTenant);
    public void deletePL(@Param("ids") List<SysTenant> sysTenantList);

    /**
     * 根据租户获取单条数据
     * @param id
     * @return
     */
    public SysTenant getByTenant(String id);

    /**
     * 根据域名获取租户数据
     * @param domainName
     * @return
     */
    public SysTenant getByDomainName(String domainName);

    /**
     * 根据登录名和User.id获取单条数据
     * @param loginName 登录名
     * @param id 用户ID
     * @return SysTenant
     */
    public SysTenant getByLoginName(@Param("loginName") String loginName, @Param("id") String id);

    int insert(SysTenant entity);

    String getTypeByTenantId(String schoolTenantId);

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    @FindListByTenant
    public List<SysTenant> findListTpl(SysTenant entity);

    /**
     * 查询列表数据（无租户、有模板)
     * @param entity
     * @return
     */
    public List<SysTenant> findListNtTpl(SysTenant entity);

    /**
     * 查询列表数据（无租户、根据条件模板)
     * @param entity
     * @return
     */
    public List<SysTenant> findListNt(SysTenant entity);

    String getDomainByName(String domainName);
}
