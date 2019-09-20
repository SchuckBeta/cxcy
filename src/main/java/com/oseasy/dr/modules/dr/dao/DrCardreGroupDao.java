package com.oseasy.dr.modules.dr.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;

/**
 * 卡记录规则组DAO接口.
 *
 * @author chenh
 * @version 2018-05-16
 */
@MyBatisDao
public interface DrCardreGroupDao extends CrudDao<DrCardreGroup> {
    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public DrCardreGroup getByg(String id);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     *
     * @param entity
     * @return
     */
    public List<DrCardreGroup> findListByg(DrCardreGroup entity);

    /**
     * 查询所有数据列表
     *
     * @param entity
     * @return
     */
    public List<DrCardreGroup> findAllListByg();

    /**
     * 更新显示状态.
     *
     * @param entity
     */
    public void updateIsShow(DrCardreGroup drCardreGroup);

    /**
     * 物理删除.
     *
     * @param entity
     */
    public void deleteWL(DrCardreGroup drCardreGroup);
}