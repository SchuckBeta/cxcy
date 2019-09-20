package com.oseasy.pro.modules.promodel.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.vo.ProjectExpVo;
import com.oseasy.pro.modules.promodel.entity.GContestUndergo;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;

/**
 * 学生信息表DAO接口
 * @author zy
 * @version 2017-03-27
 */
@MyBatisDao
public interface ProStudentExpansionDao extends CrudDao<StudentExpansion> {
    public List<ProjectExpVo> findProjectByStudentId(String id);
    public List<GContestUndergo> findGContestByStudentId(String userId);//根据userid获取大赛经历
}