package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.act.modules.actyw.entity.ActYwGroupRelation;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点状态中间表DAO接口.
 * @author zy
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwGroupRelationDao extends CrudDao<ActYwGroupRelation> {

  /**
   * 物理删除.
   * @param actYwGroupRelation
   */
  public void deleteWL(ActYwGroupRelation actYwGroupRelation);

  public void saveAll(List<ActYwGroupRelation> actYwGroupRelationList);
  /**
   * 批量保存.
   * @param list 列表
   * @return
   */
  public int savePl(@Param("list") List<ActYwGroupRelation> list);

  public String getModelActYwGroupByProv(@Param("groupId")String groupId);

  public ActYwGroup getNscActYwGroupByProv(ActYwGroupRelation entity);

  public List<ActYwGroup> findListHasGrel(ActYwGroupRelation entity);

  public List<ActYw> findListHasYrel(ActYwGroupRelation entity);


}