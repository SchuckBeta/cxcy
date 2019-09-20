package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.act.modules.actyw.entity.ActYwNode;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 项目流程节点DAO接口
 * @author chenhao
 * @version 2017-05-23
 */
@MyBatisDao
public interface ActYwNodeDao extends CrudDao<ActYwNode> {

  List<ActYwNode> findListByTypeNoZero(ActYwNode actYwNode);

}