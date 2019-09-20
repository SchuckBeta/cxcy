package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwGtime;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 时间和组件关联关系DAO接口.
 * @author zy
 * @version 2017-06-27
 */
@MyBatisDao
public interface ActYwGtimeDao extends CrudDao<ActYwGtime> {
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	@InsertByTenant
	public int insert(ActYwGtime entity);

	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	@FindListByTenant
	public List<ActYwGtime> findList(ActYwGtime entity);

	public void deleteByGroupId(ActYwGtime actYwGtime);

	ActYwGtime getTimeByGnodeId(ActYwGtime actYwGtime);

	ActYwGtime getTimeByYnodeId(@Param("ywId") String ywId, @Param("gnodeId") String gnodeId);

	/**
	   * 批量保存.
	   * @param list
	   * @return
	   */
	public int savePl(@Param("list") List<ActYwGtime> list);

	/**
	* 根据groupId批量物理删除.
	* @param groupId 流程ID
	*/
	public void deletePlwlByGroup(@Param("groupId") String groupId);
	/**
	* 根据groupId和gnodeId批量物理删除.
	* @param groupId 流程ID
	* @param gnodeId 节点ID
	*/
  	public void deletePlwl(@Param("groupId") String groupId, @Param("gnodeId") String gnodeId);

	void deleteYearPlwl(@Param("projectId")String projectId,@Param("yearId")String yearId);

	List<String> getProByProjectIdAndYear(@Param("projectId")String projectId, @Param("year")String year);

	List<ActYwGtime> checkTimeByActYw(@Param("actYwId")String actYwId);

	int checkTimeIndex(@Param("actywId")String actYwId);
}