package com.oseasy.act.modules.pro.dao;

import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 创建项目DAO接口.
 * @author zhangyao
 * @version 2017-06-15
 */
@MyBatisDao
public interface ProProjectDao extends CrudDao<ProProject> {
    ProProject getProProjectByName(String name);

    ProProject getProProjectByMark(String mark);


    ProProject getWithId(String relId);

    /**
     * 根据类型获取已使用的字典值
     * @param key String
     * @return List
     */
    List<String> getByProType(String key);
}