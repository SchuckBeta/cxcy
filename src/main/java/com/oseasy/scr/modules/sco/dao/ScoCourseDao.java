package com.oseasy.scr.modules.sco.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.sco.entity.ScoCourse;
import com.oseasy.scr.modules.sco.vo.ScoCourseVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学分课程DAO接口.
 * @author 张正
 * @version 2017-07-13
 */
@MyBatisDao
public interface ScoCourseDao extends CrudDao<ScoCourse> {
    @Override
    @FindListByTenant
    public List<ScoCourse> findList(ScoCourse entity);
    @Override
    @InsertByTenant
    public int insert(ScoCourse entity);
    //根据课程名或者课程代码查询学分课程
    public List<ScoCourse> findListByNameOrCode(@Param("keyword") String keyword);

    public List<ScoCourse> findCourseList(ScoCourse scoCourse);
    
    public int checkName(@Param("id") String id,@Param("name")String name);
	public int checkCode(@Param("id") String id,@Param("code") String code);
    @FindListByTenant
	List<ScoCourseVo> findscoCourseVoList(ScoCourseVo scoCourseVo);
}