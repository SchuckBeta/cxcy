package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
//import com.oseasy.annotation.Cacheable;



/**
 * 项目流程关联DAO接口.
 * @author chenhao
 * @version 2017-05-23
 */
@MyBatisDao
public interface ActYwDao extends CrudDao<ActYw> {
  /**
   * 根据流程keyss 获取对象.
   * @param keyss 流程标识
   * @return List
   */
  public List<ActYw> getByKeyss(@Param("keyss")String keyss);

  /**
   * 根据条件查询已部署的流程.
   * @param actYw 项目流程
   * @return List
   */
  public List<ActYw> findListByDeploy(ActYw actYw);

  /**
   * 根据条件查询项目流程.
   * @param actYw 项目流程
   * @return List
   */
  @FindListByTenant
  public List<ActYw> findListByYear(ActYw actYw);

  /**
   * 根据条件查询流程.
   * @param flowType 流程类型
   * @return List
   */
  public List<ActYw> findCurrsByflowType(@Param("flowType") String flowType);

  /**
   * 根据流程类型和项目类型条件查询流程.
   * @param flowType 流程类型
   * @param ptype 项目类型
   * @return List
   */
  public List<ActYw> findCurrsByflowTypeAndPtype(@Param("flowType") String flowType, @Param("ptype") String ptype);
  /**
   * 根据条件查询当前流程.
   * @param groupId 流程ID
   * @return List
   */
  public List<ActYw> findCurrsByGroup(@Param("groupId") String groupId);

  /**
   * 批量更新显示到时间轴状态.
   * @param entitys 修改记录（id不能为空）
   * @param isShowAxis 修改值
   */
  public void updateIsShowAxisPL(@Param("entitys") List<ActYw> entitys, Boolean isShowAxis);

  List<ActYw> findActYwListByGroupId(@Param("groupId") String groupId);

  List<ActYw> findActYwListByProProject(@Param("proType") String proType,@Param("type") String type,@Param("tenantId") String tenantId);

  List<ActYw> findActYwListByRelIdAndState(@Param("proType") String proType,@Param("type") String type,@Param("tenantId") String tenantId);

  List<ActYw> findAllActYwListByGroupId(@Param("groupId")String groupId);

    /**
     * 批量更新ActYw的isCurr属性.
     * @param actYws 项目流程
     * @param no
     */
    public void updateIsCurr(@Param("entitys") List<ActYw> actYws, @Param("isCurr") String isCurr);

    @FindListByTenant
  List<ActYw> findActYwListByProType(@Param("proType")String proType);



  @InsertByTenant
  @Override
   int insert(ActYw entity);

	@Override
	@FindListByTenant
	List<ActYw> findList(ActYw entity);


  ActYw getById(String activitiId);

  List<ActYw> getByGroupId(ActYw entity);

  List<ActYw> getActywByModel(String proType);

  ActYw getSchoolActYwByActYwId(@Param("provinceActywId")String provinceActywId,@Param("tenantId")String tenantId);

  void insertNc(ActYw actYw);
}