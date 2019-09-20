package com.oseasy.pro.modules.project.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.sys.common.utils.Tree;

import java.util.List;
import java.util.Map;


@MyBatisDao
public interface AppTypeDao extends CrudDao<Tree> {

    @FindListByTenant
    List<Tree> getAppTypeList(Map<String, Object> params);
}