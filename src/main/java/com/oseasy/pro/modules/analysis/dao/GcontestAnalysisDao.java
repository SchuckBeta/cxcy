package com.oseasy.pro.modules.analysis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface GcontestAnalysisDao {
	public List<Map<String,Object>> getData1();
	public List<Map<String,Object>> getData2();
	public List<Map<String,Object>> getData3();
	public List<Map<String,Object>> getGcontestNum();
	public List<Map<String,Object>> getGcontestNumFromModel();
	public List<Map<String,Object>> getGcontestOfficeNum(@Param("type") String type,@Param("year") String year);
	public List<Map<String,Object>> getGcontestOfficeNumFromModel(@Param("type") String type,@Param("year") String year);
	public List<Map<String,Object>> getGcontestMemNum(@Param("year") String year);
	public List<Map<String,Object>> getGcontestMemNumFromModel(@Param("year") String year);
}
