package com.oseasy.act.modules.actyw.dao;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwGroupRelation;
import com.oseasy.act.modules.actyw.entity.ActYwPscrel;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点状态中间表DAO接口.
 * @author zy
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwPscrelDao extends CrudDao<ActYwPscrel> {

  /**
   * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
   * @param entity
   * @return
   */
  public List<ActYwPscrel> findListByActywId(ActYwPscrel entity);

  /**
   * 物理删除.
   * @param actYwPscrel
   */
  public void deleteWL(ActYwPscrel actYwPscrel);

  public void saveAll(List<ActYwPscrel> actYwPscrelList);
  /**
   * 批量保存.
   * @param list 列表
   * @return
   */
  public int savePl(@Param("list") List<ActYwPscrel> list);

  List<ActYwPscrel> getSchoolByActYw(String actYwId);

  /**
   * 获取关联省流程
   * @param schoolActYwId
   * @return
   */
  String findProvActYwId(@Param("schoolActYwId")String schoolActYwId,@Param("tenantId")String tenantId);

}