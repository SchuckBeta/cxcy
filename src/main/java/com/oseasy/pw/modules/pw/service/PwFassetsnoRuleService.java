package com.oseasy.pw.modules.pw.service;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pw.modules.pw.dao.PwFassetsnoRuleDao;
import com.oseasy.pw.modules.pw.entity.PwFassetsnoRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 固定资产编号规则Service.
 *
 * @author pw
 * @version 2017-12-05
 */
@Service
@Transactional(readOnly = true)
public class PwFassetsnoRuleService extends CrudService<PwFassetsnoRuleDao, PwFassetsnoRule> {

    @Autowired
    private PwFassetsnoRuleDao pwFassetsnoRuleDao;

    public PwFassetsnoRule get(String id) {
        return super.get(id);
    }

    public List<PwFassetsnoRule> findList(PwFassetsnoRule pwFassetsnoRule) {
        return super.findList(pwFassetsnoRule);
    }

    public Page<PwFassetsnoRule> findPage(Page<PwFassetsnoRule> page, PwFassetsnoRule pwFassetsnoRule) {
        return super.findPage(page, pwFassetsnoRule);
    }

    @Transactional(readOnly = false)
    public void save(PwFassetsnoRule pwFassetsnoRule) {
        super.save(pwFassetsnoRule);
    }

    @Transactional(readOnly = false)
    public void delete(PwFassetsnoRule pwFassetsnoRule) {
        super.delete(pwFassetsnoRule);
    }

    @Transactional(readOnly = false)
    public void deleteByFcids(List<String> fcids) {
        pwFassetsnoRuleDao.deleteByFcids(fcids);
    }

}