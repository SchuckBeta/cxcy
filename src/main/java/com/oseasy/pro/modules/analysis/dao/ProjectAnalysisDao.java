package com.oseasy.pro.modules.analysis.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface ProjectAnalysisDao {
	public List<Map<String,Object>> getData1();
	public List<Map<String,Object>> getData2();
	public List<Map<String,Object>> getData3();

	List<Map<String,Object>> findAllPojectByYear(String year);

	List<String> getYears();

	List<String> getProjecTypes();

	List<Map<String,Object>> findAllPojectByType(Map<String, String> map);

	List<Map<String,Object>> findPojectByTypeAndCategory(Map<String, String> map);

	List<String> getColleges();

	List<Map<String,Object>> findProjectApplyByType(Map<String, String> map);

	List<Map<String,Object>> findProjectLiByType(Map<String, String> map);

	List<Map<String,Object>> findProjectApplyByTypeNum(Map<String, String> map);

	List<Map<String,Object>> findProjectLiByTypeNum(Map<String, String> map);
}
