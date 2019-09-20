package com.oseasy.scr.modules.sco.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.sco.entity.ScoAffirmSkill;

/**
 * 技能学分认定DAO接口.
 * @author chenhao
 * @version 2017-07-18
 */
@MyBatisDao
public interface ScoAffirmSkillDao extends CrudDao<ScoAffirmSkill> {

}