package com.oseasy.com.pcore.modules.syt.service;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.common.config.Sval;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.syt.dao.SysTenantDao;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.util.common.utils.exception.ExceptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by PW on 2019/4/19.
 */
@Service
@Transactional(readOnly = true)
public class SysTenantService extends CrudService<SysTenantDao, SysTenant> {

    @Override
    public SysTenant get(String id) {
        return super.get(id);
    }
    @Transactional(readOnly = false)
    public void deletePL(List<SysTenant> sysTenantList){
        dao.deletePL(sysTenantList);
    }

    public SysTenant findSysTenant(SysTenant sysTenant){
        return dao.findSysTenant(sysTenant);
    }

    public SysTenant findByTenantId(String tenantId){
        return dao.getByTenant(tenantId);
    }

    public String getTypeByTenantId(String tenantId){
        return dao.getTypeByTenantId(tenantId);
    }


    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<SysTenant> findListTpl(SysTenant entity) {
        return dao.findListTpl(entity);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public Page<SysTenant> findPageTpl(Page<SysTenant> page, SysTenant entity) {
        entity.setPage(page);
        page.setList(dao.findListTpl(entity));
        return page;
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<SysTenant> findListNtTpl(SysTenant entity) {
        return dao.findListNtTpl(entity);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public Page<SysTenant> findPageNtTpl(Page<SysTenant> page, SysTenant entity) {
        entity.setPage(page);
        page.setList(dao.findListNtTpl(entity));
        return page;
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<SysTenant> findListNt(SysTenant entity) {
        return dao.findListNt(entity);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public Page<SysTenant> findPageNt(Page<SysTenant> page, SysTenant entity) {
        entity.setPage(page);
        page.setList(dao.findListNt(entity));
        return page;
    }


    public String getDomainByName(String domainName){
        return dao.getDomainByName(domainName);
    }

    public SysTenant getByLoginName(String loginName){
        return getByLoginName(loginName);
    }

    public SysTenant getByLoginName(String loginName, String id){
        try{
            return dao.getByLoginName(loginName,  id);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 查询学校租户
     * @param sysTenant SysTenant
     * @return ApiResult
     */
    public ApiResult schools(SysTenant sysTenant) {
        try{
            List<SysTenant> list = schoolsTenant(sysTenant);

            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR);
        }
    }

    /**
     * 查询学校租户
     * @param sysTenant SysTenant
     * @return ApiResult
     */
    public List<SysTenant> schoolsTenant(SysTenant sysTenant) {
        //todo 根据登录的角色，过滤查询的租户id
        List<SysTenant> list = null;
        User curuser = UserUtils.getUser();
        String curpn = CoreSval.getTenantCurrpn();
        sysTenant.setStatus(CoreSval.Const.YES);
        if((Sval.EmPn.NCENTER.getPrefix()).equals(curpn)){
            sysTenant.setFilterIds(CoreIds.filterChangeTenantByNc(curuser));
            if(User.isSuper(curuser)){
                list = findListNtTpl(sysTenant);
            }else{
                sysTenant.setIsTpl(CoreSval.Const.NO);
                list = findListNtTpl(sysTenant);
            }
        }

        if((Sval.EmPn.NPROVINCE.getPrefix()).equals(curpn)){
            sysTenant.setFilterIds(CoreIds.filterTenantByNsAdmin());
            sysTenant.setStatus(CoreSval.Const.YES);
            list = findListNtTpl(sysTenant);
        }

        if(list == null){
            list = Lists.newArrayList();
        }
        return list;
    }


    /**
     * 查询学校租户
     * @param sysTenant SysTenant
     * @return ApiResult
     */
    public List<SysTenant> schoolss(SysTenant sysTenant) {
        sysTenant.setStatus(CoreSval.Const.YES);
        sysTenant.setIsTpl(CoreSval.Const.NO);
        sysTenant.setType(Sval.EmPn.NSCHOOL.getPrefix());
        List<SysTenant> list = findListNtTpl(sysTenant);
        if(list == null){
            list = Lists.newArrayList();
        }
        return list;
    }
}
