package com.oseasy.pro.modules.gcontest.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.gcontest.entity.GcontestHots;

/**
 * 大赛热点DAO接口.
 * @author 9527
 * @version 2017-07-12
 */
@MyBatisDao
public interface GcontestHotsDao extends CrudDao<GcontestHots> {
	@Override
	@FindListByTenant
	public List<GcontestHots> findList(GcontestHots entity);
	@Override
	@InsertByTenant
	public int insert(GcontestHots entity);
	public GcontestHots getTop();
	public List<Map<String,Object>> getMore(@Param(value="id")String id,@Param(value="keys") List<String> keys);
	public void updateViews(@Param("param") Map<String,Integer> param);
}