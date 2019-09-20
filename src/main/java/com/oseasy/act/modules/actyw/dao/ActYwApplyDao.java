package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.act.modules.actyw.vo.ActYwApplyVo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 流程申请DAO接口.
 * @author zy
 * @version 2017-12-05
 */
@MyBatisDao
public interface ActYwApplyDao extends CrudDao<ActYwApply> {

	void updateProcInsId(ActYwApplyVo actYwApply);
}