package com.oseasy.pro.modules.workflow.dao;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelMdGc;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * proProjectMdDAO接口.
 * @author zy
 * @version 2017-09-18
 */
@MyBatisDao
public interface ProModelGzsmxxDao extends WorkFlowDao<ProModelGzsmxx, ExpProModelVo> {

	@Override
	ProModelGzsmxx getByProModelId(String proModelId);

	List<ProModelGzsmxx> findListByIds(ProModelGzsmxx proModelGzsmxx);

	List<ExpProModelVo> export(ProModelGzsmxx proModelGzsmxx);
}