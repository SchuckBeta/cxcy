package com.oseasy.cas.modules.cas.service;

import java.util.Date;
import java.util.List;

import com.oseasy.com.pcore.common.config.CoreSval;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.modules.cas.dao.SysCasUserDao;
import com.oseasy.cas.modules.cas.entity.SysCasAnZhi;
import com.oseasy.cas.modules.cas.entity.SysCasKda;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.entity.SysUser;
import com.oseasy.cas.modules.cas.service.anzhi.SysCasAnzhiService;
import com.oseasy.cas.modules.cas.service.kda.SysCasKdaService;
import com.oseasy.cas.modules.cas.vo.CasType;
import com.oseasy.cas.modules.cas.vo.CheckRet;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.sys.modules.sys.vo.Utype;
import com.oseasy.util.common.utils.StringUtil;

@Service
@Transactional(readOnly = true)
public class SysCasUserService extends CrudService<SysCasUserDao, SysCasUser> {
	public static Logger logger = Logger.getLogger(SysCasUserService.class);
	@Autowired
	private CoreService coreService;
    @Autowired
    private SysUserService sysUserService;
	@Autowired
	private SysCasKdaService entitykdaService;
	@Autowired
    private SysCasAnzhiService entityAnzhiService;

    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public SysCasUser get(String id) {
        return super.get(id);
    }

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public SysCasUser get(SysCasUser entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     * @param entity
     * @return
     */
    public List<SysCasUser> findList(SysCasUser entity) {
        return dao.findList(entity);
    }

    /**
     * 查询分页数据
     * @param page 分页对象
     * @param entity
     * @return
     */
    public Page<SysCasUser> findPage(Page<SysCasUser> page, SysCasUser entity) {
        entity.setPage(page);
        page.setList(dao.findList(entity));
        return page;
    }

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<SysCasUser> findAllList(){
        return dao.findAllList();
    }

    /**
     * 保存数据（插入）
     * @param entity
     */
    @Transactional(readOnly = false)
    public void insert(SysCasUser entity) {
        dao.insert(entity);
    }

    /**
     * 保存数据（更新）
     * @param entity
     */
    @Transactional(readOnly = false)
    public void update(SysCasUser entity) {
        dao.update(entity);
    }

    /**
     * 保存数据（插入或更新）
     * @param entity
     */
    @Transactional(readOnly = false)
    public void save(SysCasUser entity) {
        if ((entity.getIsNewRecord())) {
            if (StringUtil.isEmpty(entity.getId())) {
                entity.setTime(1);
            }
            if (entity.getLastLoginDate() == null) {
                entity.setLastLoginDate(new Date());
            }
            if (entity.getEnable() == null) {
                entity.setEnable(true);
            }
        }
        if(entity.getType() == null){
            entity.setType(CasSval.getCasTypes().get(0));
        }
        super.save(entity);
    }

    /**
     * 删除数据
     * @param entity
     */
    @Transactional(readOnly = false)
    public void delete(SysCasUser entity) {
        dao.delete(entity);
    }

    /**
     * 根据Uid批量删除数据
     * @param entity
     */
    @Transactional(readOnly = false)
    public void deletePlwlByUid(String uid) {
        dao.deletePlwlByUid(uid);
    }

    /**
     * 批量修改CAS状态.
     * @param entity
     */
    @Transactional(readOnly = false)
    public void updateALLEnable(SysCasUser entity) {
        if((entity.getType() == null)){
            entity.setType(CasSval.getCasTypes().get(0));
        }
        dao.updateALLEnable(entity);
    }

    /**
     * 批量保存.
     * @param entitys
     */
    @Transactional(readOnly = false)
    public void savePL(List<SysCasUser> entitys) {
        dao.savePL(entitys);
    }

    /**
     * 检查用户是否存在
     * @param entity
     * @return
     */
    public SysCasUser checkSysCaseUser(SysCasUser entity) {
        if(entity == null){
            entity = new SysCasUser();
        }

        if(StringUtil.isEmpty(entity.getRuid())){
            entity.setCheckRet(CheckRet.FALSE.getKey());
            return entity;
        }

        String curUtype = entity.getRutype();
        /**
         * 检查SysCasUser是否存在用户.
         */
        SysCasUser pentity = new SysCasUser();
        pentity.setRuid(entity.getRuid());
        List<SysCasUser> entitys = dao.findList(pentity);
        Boolean hasCasUser = (StringUtil.checkNotEmpty(entitys) && (entitys.size() > 0));
        if(hasCasUser){
            entity = entitys.get(0);
            entity.setCheckRet(CheckRet.TRUE.getKey());
            if(StringUtil.isEmpty(entity.getRutype())){
                entity.setRutype(curUtype);
            }else{
                curUtype = entity.getRutype();
            }
        }else{
            entity.setCheckRet(CheckRet.FALSE.getKey());
            entity.setEnable(true);
        }

        /**
         * 检查SysUser是否存在用户.
         */
        User puentity = new User();
        puentity.setNo(pentity.getRuid());
        puentity.setLoginName(pentity.getRuid());
        Boolean hasUser = coreService.checkUserByNoAndLoginName(puentity);
        if(hasUser){
            entity.setCheckRetUser(CheckRet.TRUE.getKey());
            SysUser curUser = sysUserService.getUserByNoOrLoginName(puentity);
            entity.setUid(curUser.getUser().getId());
            if(StringUtil.isNotEmpty(curUser.getUser().getUserType())){
                entity.setRutype(curUser.getUser().getUserType());
            }

            /**
             * 检查Role是否存在.
             */
            Boolean hasRole = false;
            Boolean hasST = false;
            if((Utype.STUDENT.getKey()).equals(entity.getRutype())){
                hasST = coreService.checkStudentByUid(entity.getUid());
                hasRole = coreService.checkRoleByUid(entity.getUid(), coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId());
            }else if((Utype.TEACHER.getKey()).equals(entity.getRutype())){
                hasST = coreService.checkTeacherByUid(entity.getUid());
                hasRole = coreService.checkRoleByUid(entity.getUid(), coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId());
            }

            if(hasRole){
                entity.setCheckRetRole(CheckRet.TRUE.getKey());
            }else{
                entity.setCheckRetRole(CheckRet.FALSE.getKey());
            }

            if(hasST){
                entity.setCheckRetST(CheckRet.TRUE.getKey());
            }else{
                entity.setCheckRetST(CheckRet.FALSE.getKey());
            }
        }else{
            entity.setCheckRetUser(CheckRet.FALSE.getKey());
        }

        if(StringUtil.isEmpty(entity.getRutype()) && StringUtil.isNotEmpty(curUtype)){
            entity.setRutype(curUtype);
        }

        if(StringUtil.isEmpty(entity.getRutype())){
            entity.setCheckUtype(true);
        }else{
            entity.setCheckUtype(false);
        }
        return entity;
    }

    @Transactional(readOnly = false)
    public void casJobs() {
        List<ISysCasServive> casServices = Lists.newArrayList();
        casServices.add(entitykdaService);
        casServices.add(entityAnzhiService);
        List<String> casTypes = CasSval.getCasTypes();
        for (String key : casTypes) {
            for(ISysCasServive casService: casServices){
                if((casService.getCasType()).equals(key)){
                    casService.casJob(findAllList());
                }
            }
        }
    }

    @Transactional(readOnly = false)
    public void casDeletes(SysCasUser casUser) {
        List<String> casTypes = CasSval.getCasTypes();
        for (String casType : casTypes) {
            if((CasType.ANZHI.getKey()).equals(casType)){
                entityAnzhiService.delete(new SysCasAnZhi(casUser));
            }else if((CasType.KUANGDA.getKey()).equals(casType)){
                entitykdaService.delete(new SysCasKda(casUser));
            }
        }
    }
}
