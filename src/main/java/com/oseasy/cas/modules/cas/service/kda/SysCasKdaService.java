/**
 * .
 */

package com.oseasy.cas.modules.cas.service.kda;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.modules.cas.dao.kda.DBCasKdaDao;
import com.oseasy.cas.modules.cas.dao.kda.SysCasKdaDao;
import com.oseasy.cas.modules.cas.entity.SysCasKda;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.service.ISysCasServive;
import com.oseasy.cas.modules.cas.service.SysCasUserService;
import com.oseasy.cas.modules.cas.vo.CasType;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class SysCasKdaService extends CrudService<SysCasKdaDao, SysCasKda> implements ISysCasServive<SysCasKda>{
    @Autowired
    SysCasUserService sysCasUserService;

    /**
     * 删除数据
     * @param entity
     */
    @Transactional(readOnly = false)
    public void delete(SysCasKda entity) {
        dao.delete(entity);
    }

    /**
     * 批量更新Enable数据
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public void updateByPlEnable(List<String> ids, Boolean enable){
        dao.updateByPlEnable(ids, enable);
    }

    /**
     * 批量更新DelFlag数据
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public void updateByPlDelFlag(List<String> ids, String delFlag){
        dao.updateByPlDelFlag(ids, delFlag);
    }

    @Override
    public List<SysCasKda> findList(boolean openFilter) {
        return DBCasKdaDao.findList(openFilter);
    }

    @Override
    @Transactional(readOnly = false)
    public int updatePLPropEnable(List<String> ids, String status) {
        return DBCasKdaDao.updatePLPropEnable(ids, status);
    }

    @Override
    @Transactional(readOnly = false)
    public int updatePLPropTime(List<SysCasKda> entitys) {
        return DBCasKdaDao.updatePLPropTime(entitys);
    }

    @Override
    public String getCasType() {
        return CasType.KUANGDA.getKey();
    }

    @Transactional(readOnly = false)
    public void casJob(List<SysCasUser> sysCasUsers){
        List<SysCasKda> SysCasKdas = findList(true);
        if(StringUtil.checkEmpty(SysCasKdas)){
            return;
        }

        List<SysCasKda> disenableSysCasKdas = Lists.newArrayList();
        List<SysCasKda> addtimeSysCasKdas = Lists.newArrayList();
        List<SysCasUser> jobSysCasUsers = Lists.newArrayList();
        for (SysCasKda SysCasKda : SysCasKdas) {
            if(SysCasKda.getTime() >= CasSval.getCasMaxTime()){
                disenableSysCasKdas.add(SysCasKda);
                continue;
            }

            Boolean hasDeal = false;
            for (SysCasUser sysCasUser : sysCasUsers) {
                if((SysCasKda.getRuid()).equals(sysCasUser.getRuid())){
                    hasDeal = true;
                    break;
                }
            }

            if(hasDeal){
                disenableSysCasKdas.add(SysCasKda);
            }else{
                SysCasUser curSysCasUser = SysCasKda.newSysCasUser(SysCasKda);
                if(curSysCasUser.getIsDeal()){
                    jobSysCasUsers.add(curSysCasUser);
                }else{
                    SysCasKda.setTime(SysCasKda.getTime() + 1);
                    addtimeSysCasKdas.add(SysCasKda);
                }
            }
        }

        /**
         * 已处理或已存在的用户数据，标记为已处理.
         */
        if(StringUtil.checkNotEmpty(disenableSysCasKdas)){
            updatePLPropEnable(StringUtil.sqlInByListIdss(disenableSysCasKdas), Const.YES);
        }

        /**
         * 处理失败的用户数据，处理次数+1.
         */
        if(StringUtil.checkNotEmpty(addtimeSysCasKdas)){
            updatePLPropTime(addtimeSysCasKdas);
        }

        /**
         * 处理成功的用户数据，保存到SysCasUser表.
         */
        if(StringUtil.checkNotEmpty(jobSysCasUsers)){
            sysCasUserService.savePL(jobSysCasUsers);
        }
    }
}
