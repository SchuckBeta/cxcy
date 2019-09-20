package com.oseasy.act.modules.actyw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwGroup;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 自定义流程DAO接口.
 * @author chenhao
 * @version 2017-05-23
 */
@MyBatisDao
public interface ActYwGroupDao extends CrudDao<ActYwGroup> {
    /**
     * 根据ID获取流程，不考虑删除状态.
     * @param group 唯一标识
     * @return ActYwGroup
     */
    @FindListByTenant
    public ActYwGroup getByTenant(ActYwGroup group);

    @Override
    @FindListByTenant
    public List<ActYwGroup> findList(ActYwGroup actYwGroup);

    /**
     * 根据流程keyss 获取对象.
     * @param keyss 流程标识
     * @return ActYwGroup
     */
    public List<ActYwGroup> getByKeyss(@Param("keyss")String keyss);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return ActYwGroup
     */
    @FindListByTenant
    public List<ActYwGroup> findListByCount(ActYwGroup entity);

    /**
     * 查询记录数.
     * @param entity
     * @return Long
     */
    public Long findCount(ActYwGroup entity);

    @InsertByTenant
    @Override
    int insert(ActYwGroup entity);

    List<ActYwGroup> findModelList(ActYwGroup pactYwGroup);
}