package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwFassetsnoRule;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 固定资产编号规则DAO接口.
 *
 * @author pw
 * @version 2017-12-05
 */
@MyBatisDao
public interface PwFassetsnoRuleDao extends CrudDao<PwFassetsnoRule> {

    int deleteByFcids(@Param("fcids")List<String> fcids);

}