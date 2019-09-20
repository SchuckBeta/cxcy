package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwForm;
import com.oseasy.act.modules.actyw.entity.ActYwFormVo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

/**
 * 项目流程表单DAO接口.
 * @author chenhao
 * @version 2017-05-23
 */
@MyBatisDao
public interface ActYwFormDao extends CrudDao<ActYwForm> {

  List<ActYwForm> findListByInStyle(ActYwFormVo actYwVo);

  ActYwForm getActYwForm(@Param("id")String id, @Param("tenantId")String tenantId);
}