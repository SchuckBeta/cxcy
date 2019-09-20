package com.oseasy.scr.modules.sco.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.sco.entity.ScoScore;

/**
 * 学分汇总DAO接口.
 * @author chenh
 * @version 2017-07-18
 */
@MyBatisDao
public interface ScoScoreDao extends CrudDao<ScoScore> {

  /**
   * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<ScoScore>());
   * @param entity
   * @return
   */
  public List<ScoScore> findListGbyUser(ScoScore entity);

  //addBy zhangzheng 根据项目id删除对应的分数
  public int deleteProject(String proId);
}