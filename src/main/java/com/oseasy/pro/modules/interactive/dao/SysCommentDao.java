package com.oseasy.pro.modules.interactive.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.interactive.entity.SysComment;

/**
 * 评论表DAO接口.
 * @author 9527
 * @version 2017-06-30
 */
@MyBatisDao
public interface SysCommentDao extends CrudDao<SysComment> {
	public void insertBatch(List<SysComment> list);
	public void updateLikes(@Param("param") Map<String,Integer> param);
	public void get(@Param("param") Map<String,Integer> param);
	public int 	getMyComments(@Param("uid") String uid,@Param("fid") String fid);
	public List<Map<String,String>> getPageList(Map<String,Object> param);
}