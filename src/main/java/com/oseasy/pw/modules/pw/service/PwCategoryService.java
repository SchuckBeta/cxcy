package com.oseasy.pw.modules.pw.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.service.TreeService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.pw.modules.pw.dao.PwCategoryDao;
import com.oseasy.pw.modules.pw.entity.PwCategory;
import com.oseasy.pw.modules.pw.entity.PwFassets;
import com.oseasy.pw.modules.pw.entity.PwFassetsnoRule;
import com.oseasy.pw.modules.pw.exception.CategoryException;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 资源类别Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class PwCategoryService extends TreeService<PwCategoryDao, PwCategory> {

    @Autowired
    private PwCategoryDao pwCategoryDao;

    @Autowired
    private PwFassetsService pwFassetsService;

    @Autowired
    private PwFassetsnoRuleService pwFassetsnoRuleService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    public PwCategory get(String id) {
        return super.get(id);
    }

    public List<PwCategory> findList(PwCategory pwCategory) {
        if (StringUtil.isNotBlank(pwCategory.getParentIds())) {
            pwCategory.setParentIds("," + pwCategory.getParentIds() + ",");
        }
        return super.findList(pwCategory);
    }

    @Transactional(readOnly = false)
    public void save(PwCategory pwCategory) {
        PwCategory c = new PwCategory();
        c.setName(pwCategory.getName());
        c.setParent(new PwCategory(pwCategory.getParentId()));
        List<PwCategory> list = super.findList(c);
        if (list != null && !list.isEmpty()) {
            for (PwCategory category : list) {
                if (!category.getId().equals(pwCategory.getId()) && category.getName().equals(pwCategory.getName())) {
                    logger.warn("同级下名称不能重复");
                    throw new CategoryException("同级下名称不能重复");

                }
            }
        }
        if (!pwCategory.getIsNewRecord()) {
            PwCategory newPwCategory = get(pwCategory.getId());
            //修改了级别
            String newParentIds = get(pwCategory.getParentId()).getParentIds() + pwCategory.getParentId();
            if (newParentIds.split(",").length != newPwCategory.getParentIds().split(",").length) {
                PwCategory category = new PwCategory();
                if (CoreIds.NCE_SYS_TREE_ROOT.getId().equals(newPwCategory.getParentId())) {//一级
                    category.setParent(new PwCategory(newPwCategory.getId()));
                } else {//二级
                    category.setId(newPwCategory.getId());
                }
                PwFassets pwFassets = new PwFassets();
                pwFassets.setPwCategory(category);
                if (!pwFassetsService.findList(pwFassets).isEmpty()) {
                    logger.warn("指定的资产类型正在使用，不能修改级别");
                    throw new CategoryException("指定的资产类型正在使用，不能修改级别");
                }
            }
        }
        super.save(pwCategory);
        this.ruleSave(pwCategory);
    }

    @Transactional(readOnly = false)
    public void delete(PwCategory pwCategory) {
        /**
         * 删除规则：系统存在此种类型(或子类型)的固定资产，则资产类型不能删除
         */
        PwCategory newCategory = this.get(pwCategory.getId());
        if (newCategory == null) {
            logger.warn("指定的资产类型不存在");
            throw new CategoryException("指定的资产类型不存在");
        }
        PwFassets pwFassets = new PwFassets();
        List<String> fcids = new ArrayList<>();
        fcids.add(pwCategory.getId());
        if (CoreIds.NCE_SYS_TREE_ROOT.getId().equals(newCategory.getParent().getId())) {//大类（办公设备、试验器材等）
            PwCategory p = new PwCategory(pwCategory.getId());
            PwCategory category = new PwCategory();
            category.setParent(p);
            pwFassets.setPwCategory(category);
            List<PwCategory> list = this.findList(category);
            if (!list.isEmpty()) {
                for (PwCategory c : list) {
                    fcids.add(c.getId());
                }
            }
        } else {
            pwFassets.setPwCategory(new PwCategory(pwCategory.getId()));//小类（电脑、桌子等）
        }
        if (!pwFassetsService.findList(pwFassets).isEmpty()) {
            logger.warn("指定的资产类型正在使用，无法删除");
            throw new CategoryException("指定的资产类型正在使用，无法删除");
        }
        //删除编号规则
        pwFassetsnoRuleService.deleteByFcids(fcids);
        super.delete(pwCategory);
    }

    /**
     * 查询指定父id的直接子资产类别
     *
     * @param parentIds
     * @return
     */
    public List<PwCategory> findListByParentIds(List<String> parentIds) {
        return pwCategoryDao.findListByParentIds(parentIds);
    }

    /**
     * 保存编号规则
     *
     * @param pwCategory
     */
    @Transactional(readOnly = false)
    public void ruleSave(PwCategory pwCategory) {
        if (StringUtils.isBlank(pwCategory.getId())) {
            logger.warn("编号规则未指定资产类型");
            throw new CategoryException("编号规则未指定资产类型");
        }
        PwFassetsnoRule pwFassetsnoRule = pwCategory.getPwFassetsnoRule();
        if (pwFassetsnoRule == null) {
            logger.warn("未设置编号规则");
            throw new CategoryException("未设置编号规则");
        }
        pwFassetsnoRule.setFcid(pwCategory.getId());
        if (StringUtils.isBlank(pwFassetsnoRule.getId())) {
            pwFassetsnoRule.setId(IdGen.uuid());
            pwFassetsnoRule.setIsNewRecord(true);
            pwFassetsnoRule.setVersion(0);
        } else {
            pwFassetsnoRule.setIsNewRecord(false);
        }
        pwFassetsnoRuleService.save(pwFassetsnoRule);
    }
}