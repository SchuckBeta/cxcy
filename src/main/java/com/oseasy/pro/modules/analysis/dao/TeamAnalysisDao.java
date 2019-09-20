package com.oseasy.pro.modules.analysis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface TeamAnalysisDao {

	List<Map<String,Object>> findTeamTeacherByYear(String year);

	List<String> getYears() ;

	List<String> getTypes();

	List<String> getColleges();

	List<Map<String,Object>> findTeamTeacherByCollege(Map<String, String> year);

	List<Map<String,Object>> findTeamNumByYear(String param);

	public List<Map<String,Object>> getTeamNumFromProject(@Param("year") String year);
	public List<Map<String,Object>> getTeamNumFromGcontest(@Param("year") String year);
	public List<Map<String,Object>> getTeamNumFromModel(@Param("year") String year);
	public List<Map<String,Object>> getTeamMemsFromProject(@Param("year") String year);
	public List<Map<String,Object>> getTeamMemsFromGcontest(@Param("year") String year);
	public List<Map<String,Object>> getTeamMemsFromModel(@Param("year") String year);
	public List<Map<String,Object>> getTeamYearMemsFromProject();
	public List<Map<String,Object>> getTeamYearMemsFromGcontest();
	public List<Map<String,Object>> getTeamYearMemsFromModel();
}
