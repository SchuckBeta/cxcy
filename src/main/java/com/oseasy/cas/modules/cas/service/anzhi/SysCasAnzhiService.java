/**
 * .
 */

package com.oseasy.cas.modules.cas.service.anzhi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.cas.common.config.CasSval;
import com.oseasy.cas.modules.cas.dao.anzhi.DBCasAnZhiDao;
import com.oseasy.cas.modules.cas.dao.anzhi.SysCasAnZhiDao;
import com.oseasy.cas.modules.cas.dao.kda.DBCasKdaDao;
import com.oseasy.cas.modules.cas.entity.SysCasAnZhi;
import com.oseasy.cas.modules.cas.entity.SysCasKda;
import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.cas.modules.cas.service.ISysCas;
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
public class SysCasAnzhiService extends CrudService<SysCasAnZhiDao, SysCasAnZhi> implements ISysCasServive<SysCasAnZhi>{
    @Autowired
    SysCasUserService sysCasUserService;

    /**
     * 删除数据
     * @param entity
     */
    @Transactional(readOnly = false)
    public void delete(SysCasAnZhi entity) {
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
    public List<SysCasAnZhi> findList(boolean openFilter) {
        return DBCasAnZhiDao.findList(openFilter);
    }

    @Override
    @Transactional(readOnly = false)
    public int updatePLPropEnable(List<String> ids, String status) {
        return DBCasAnZhiDao.updatePLPropEnable(ids, status);
    }

    @Override
    @Transactional(readOnly = false)
    public int updatePLPropTime(List<SysCasAnZhi> entitys) {
        return DBCasAnZhiDao.updatePLPropTime(entitys);
    }

    @Override
    public String getCasType() {
        return CasType.ANZHI.getKey();
    }

    @Transactional(readOnly = false)
    public synchronized void casJob(List<SysCasUser> sysCasUsers){
        List<SysCasAnZhi> sysCasAnZhis = DBCasAnZhiDao.findList(true);
        if(StringUtil.checkEmpty(sysCasAnZhis)){
            return;
        }

        List<SysCasAnZhi> disenableSysCasAnZhis = Lists.newArrayList();
        List<SysCasAnZhi> addtimeSysCasAnZhis = Lists.newArrayList();
        List<SysCasUser> jobSysCasUsers = Lists.newArrayList();
        for (SysCasAnZhi sysCasAnZhi : sysCasAnZhis) {
            if(sysCasAnZhi.getTime() >= CasSval.getCasMaxTime()){
                disenableSysCasAnZhis.add(sysCasAnZhi);
                continue;
            }

            Boolean hasDeal = false;
            for (SysCasUser sysCasUser : sysCasUsers) {
                if((sysCasAnZhi.getRuid()).equals(sysCasUser.getRuid())){
                    hasDeal = true;
                    break;
                }
            }

            if(hasDeal){
                disenableSysCasAnZhis.add(sysCasAnZhi);
            }else{
                SysCasUser curSysCasUser = SysCasAnZhi.newSysCasUser(sysCasAnZhi);
                if(curSysCasUser.getIsDeal()){
                    jobSysCasUsers.add(curSysCasUser);
                }else{
                    sysCasAnZhi.setTime(sysCasAnZhi.getTime() + 1);
                    addtimeSysCasAnZhis.add(sysCasAnZhi);
                }
            }
        }

        /**
         * 已处理或已存在的用户数据，标记为已处理.
         */
        if(StringUtil.checkNotEmpty(disenableSysCasAnZhis)){
            updatePLPropEnable(StringUtil.sqlInByListIdss(disenableSysCasAnZhis), Const.YES);
        }

        /**
         * 处理失败的用户数据，处理次数+1.
         */
        if(StringUtil.checkNotEmpty(addtimeSysCasAnZhis)){
            updatePLPropTime(addtimeSysCasAnZhis);
        }

        /**
         * 处理成功的用户数据，保存到SysCasUser表.
         */
        if(StringUtil.checkNotEmpty(jobSysCasUsers)){
            sysCasUserService.savePL(jobSysCasUsers);
        }
    }
}
