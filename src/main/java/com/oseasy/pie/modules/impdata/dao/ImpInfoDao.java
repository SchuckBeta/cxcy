package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ImpInfo;

/**
 * 导入数据信息表DAO接口
 * @author 9527
 * @version 2017-05-16
 */
@MyBatisDao
public interface ImpInfoDao extends CrudDao<ImpInfo> {
	public List<ImpInfo> findListByIep(ImpInfo vo);

	public List<ImpInfo> getDrCardList(ImpInfo vo);
	public List<ImpInfo> getProModelList(ImpInfo vo);
	public List<Map<String,String>> getMdList(Map<String,Object> param);
	public int getMdListCount(Map<String,Object> param);
	public List<Map<String,String>> getList(Map<String,Object> param);
	public int getListCount(Map<String,Object> param);
	public List<Map<String,String>> getApprovalData(@Param("pids")List<String> pids);
	public List<Map<String,String>> getMidData(@Param("pids")List<String> pids);
	public List<Map<String,String>> getCloseData(@Param("pids")List<String> pids);
	public List<Map<String,String>> getProjectMdData(@Param("pids")List<String> pids);


	List<Map<String,String>> getProModelGnodeData(@Param("pids")List<String> pids);
	List<Map<String,String>> getGnodeData(@Param("pids")List<String> pids);
}