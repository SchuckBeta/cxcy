package com.oseasy.pro.modules.excellent.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.cms.modules.cms.vo.ExcellentGcontestVo;
import com.oseasy.cms.modules.cms.vo.ExcellentProjectVo;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.excellent.entity.ExcellentShow;

/**
 * 优秀展示DAO接口.
 * @author 9527
 * @version 2017-06-23
 */
@MyBatisDao
public interface ExcellentShowDao extends CrudDao<ExcellentShow> {
	public List<ExcellentGcontestVo> findGcontestList(ExcellentGcontestVo vo);
	public List<ExcellentProjectVo> findProjectList(ExcellentProjectVo vo);
	public void deleteAll(@Param("ids")String[] ids,@Param("uid")String uid);
	public void unrelease(@Param("ids")String[] ids,@Param("uid")String uid);
	public void resall(@Param("fids")String[] fids,@Param("uid")String uid);
	public List<Map<String,String>> findProjectForIndex();
	public List<Map<String,String>> findGcontestForIndex();
	public Map<String,String> getProjectInfoFromProModel(String projectId);
	public List<Map<String,String>> getProjectTeacherInfoFromProModel(String projectId);
	public Map<String,String> getProjectInfo(String projectId);
	public List<Map<String,String>> getProjectTeacherInfo(String projectId);
	public Map<String,String> getGcontestInfoFromProModel(String gcontestId);
	public List<Map<String,String>> getGcontestTeacherInfoFromProModel(String gcontestId);
	public Map<String,String> getGcontestInfo(String gcontestId);
	public List<Map<String,String>> getGcontestTeacherInfo(String gcontestId);
	public ExcellentShow getByForid(String id);
	public void updateComments(@Param("param") Map<String,Integer> param);
	public void updateViews(@Param("param") Map<String,Integer> param);
	public void updateLikes(@Param("param") Map<String,Integer> param);

	List<Map<String,String>> findAllProjectShow(Map<String, Object> param);
	int findAllProjectShowCount(Map<String, Object> param);
}