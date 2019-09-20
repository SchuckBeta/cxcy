package com.oseasy.sys.modules.sys.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.sys.modules.sys.dao.SysNumberRuleDao;
import com.oseasy.sys.modules.sys.entity.SysNumberRule;
import com.oseasy.sys.modules.sys.entity.SysNumberRuleDetail;
import com.oseasy.sys.modules.sys.utils.SysEnumUtils;
import com.oseasy.util.common.utils.exception.RunException;

/**
 * 编号规则管理Service.
 *
 * @author 李志超
 * @version 2018-05-17
 */
@Service
@Transactional(readOnly = false)
public class SysNumberRuleService extends CrudService<SysNumberRuleDao, SysNumberRule> {
    @Autowired
    private SysNumberRuleDetailService sysNumberRuleDetailService;


    public SysNumberRule get(String id) {
        return super.get(id);
    }

    public List<SysNumberRule> findList(SysNumberRule sysNumberRule) {
        return dao.findList(sysNumberRule);
    }

    public Page<SysNumberRule> findPage(Page<SysNumberRule> page, SysNumberRule sysNumberRule) {
        return super.findPage(page, sysNumberRule);
    }

    @Transactional(readOnly = false)
    public void save(SysNumberRule sysNumberRule) {
        try {
            if (StringUtils.isEmpty(sysNumberRule.getId())) {//判断主键为空时进行保存操作
                if (!StringUtils.isEmpty(dao.getRuleByAppType(sysNumberRule.getAppType(), sysNumberRule.getId()))) {//做应用类型唯一性校验
                    throw new RunException("该应用编号规则已存在, 不可重复添加");
                }
                super.save(sysNumberRule);
            } else { //判断主键非空时为编辑操作，对规则明细表中的内容进行删除操作
                sysNumberRuleDetailService.deleteByRuleId(sysNumberRule.getId());
            }

            List<SysNumberRuleDetail> sysNumberRuleDetailList = sysNumberRule.getSysNumberRuleDetailList();
            //对明细列表进行顺序排序
            Collections.sort(sysNumberRuleDetailList, new Comparator<SysNumberRuleDetail>() {
                public int compare(SysNumberRuleDetail detail1, SysNumberRuleDetail detail2) {
                    return detail1.getSort() - detail1.getSort();
                }
            });
            //设置规则主表中的规则正则
            sysNumberRule.setRule(getRegRuleText(sysNumberRuleDetailList, sysNumberRule.getId()));

            super.save(sysNumberRule);
            sysNumberRuleDetailService.batchSave(sysNumberRuleDetailList);
        } catch (RunException e) {
            throw new RunException(e.getMsg());
        }
    }

    @Transactional(readOnly = false)
    public void update(SysNumberRule sysNumberRule) {
        super.save(sysNumberRule);
    }

    /**
     * 生成编号规则正则
     *
     * @param sysNumberRuleDetailList 规则明细列表
     * @param fk                      规则主表id
     * @return
     */
    private String getRegRuleText(List<SysNumberRuleDetail> sysNumberRuleDetailList, String fk) {

        StringBuffer rule = new StringBuffer();

        for (SysNumberRuleDetail detail : sysNumberRuleDetailList) {

            //通过规则类型获取该规则正则，并进行拼接
            rule.append(SysEnumUtils.RuleType.getRuleText(detail));

            //设置规则明细中的主表id及明细表主键
            detail.setProNumberRuleId(fk);
            detail.setId(IdGen.uuid());
        }
        return rule.toString();
    }

    @Transactional(readOnly = false)
    public void delete(SysNumberRule sysNumberRule) {
        try {
            sysNumberRuleDetailService.deleteByRuleId(sysNumberRule.getId());
            dao.deleteWL(sysNumberRule);
        } catch (Exception e) {
            throw new RuntimeException("删除失败,请联系管理员");
        }
    }

    @Transactional(readOnly = false)
    public void deleteWL(SysNumberRule sysNumberRule) {
        dao.deleteWL(sysNumberRule);
    }

    @Transactional(readOnly = true)
    public SysNumberRule getRuleByAppType(String appType, String id) {
        return dao.getRuleByAppType(appType, id);
    }
    @Transactional(readOnly = true)
    public SysNumberRule getRuleByAppType(String appType) {
        if(StringUtils.isEmpty(appType)){
            return null;
        }
        return dao.getRuleByAppType(appType, null);
    }
    //根据规则id获得具体规则明细
    public List<SysNumberRuleDetail> findSysNumberRuleDetailList(String id) {
        return sysNumberRuleDetailService.findSysNumberRuleDetailList(id);
    }
}