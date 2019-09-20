package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;

import org.apache.ibatis.annotations.Param;

/**
 * 入驻申报详情DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwEnterDetailDao extends CrudDao<PwEnterDetail> {

	public void deleteByEid(String eid);

	/**
	* 获取团队数据.
	* @param id 主键
	* @return PwEnterDetail
	*/
	public PwEnterDetail getByTeam(String id);

	/**
	* 获取企业数据.
	* @param id 主键
	* @return PwEnterDetail
	*/
	public PwEnterDetail getByQy(String id);

	/**
	* 获取项目数据.
	* @param id 主键
	* @return PwEnterDetail
	*/
	public PwEnterDetail getByXm(String id);

	/**
	* 获取所有类型数据.
	* @param id 主键
	* @return PwEnterDetail
	*/
	public PwEnterDetail getByAll(String id);


	/**
	* 查找团队入驻.
	* @param pwEnterDetail 实体
	* @return List 列表
	*/
	public List<PwEnterDetail> findListByTeam(PwEnterDetail pwEnterDetail);

	/**
	 * 查找企业入驻.
	* @param pwEnterDetail 实体
	* @return List 列表
	 */
	public List<PwEnterDetail> findListByQy(PwEnterDetail pwEnterDetail);

	/**
	 * 查找项目入驻.
	 * @param pwEnterDetail 实体
	* @return List 列表
	 */
	public List<PwEnterDetail> findListByXm(PwEnterDetail pwEnterDetail);

	/**
	 * 查找所有入驻.
	 * @param pwEnterDetail 实体
	 * @return List 列表
	 */
	public List<PwEnterDetail> findListByAll(PwEnterDetail pwEnterDetail);

	/**
	* 物理删除数据.
	* @param perDetail 实体
	*/
	public void deleteWL(PwEnterDetail perDetail);

	PwEnterDetail getByPwEnterIdAndType(@Param("eid")String eid, @Param("type")String type);

	List<PwEnterDetail> getByPwEnterXmIdAndType(@Param("eid")String id,@Param("type") String type);

	void saveAll(List<PwEnterDetail> detailList);

	List<PwEnterDetail> checkPwEnterTeam(@Param("eid")String eid,@Param("rid")String rId,@Param("type") String type);

	void deleteByEidAndType(@Param("eid")String eid, @Param("type")String type);

	int checkPwEnterByTeamId(String teamId);
}