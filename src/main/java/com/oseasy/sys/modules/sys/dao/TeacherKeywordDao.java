package com.oseasy.sys.modules.sys.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.modules.sys.entity.TeacherKeyword;

import java.util.List;

/**
 * teacherKeywordDAO接口.
 * @author zy
 * @version 2017-07-03
 */
@MyBatisDao
public interface TeacherKeywordDao extends CrudDao<TeacherKeyword> {
    public void delByTeacherid(String teacherId);

    public List<TeacherKeyword> findByTeacherid(String teacherId);
    public List<String> findStringByTeacherid(String teacherId);
}