package com.oseasy.pro.modules.project.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.project.entity.ProSituation;

import java.util.List;

/**
 * 国创项目完成情况表单DAO接口
 * @author 9527
 * @version 2017-03-29
 * @Deprecated
 */
@MyBatisDao
public interface ProSituationDao extends CrudDao<ProSituation> {

    public void deleteByMidId(String fid);

    public List<ProSituation> getByFid(String fid);

}