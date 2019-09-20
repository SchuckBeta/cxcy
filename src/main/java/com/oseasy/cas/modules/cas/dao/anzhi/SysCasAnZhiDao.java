package com.oseasy.cas.modules.cas.dao.anzhi;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.cas.modules.cas.entity.SysCasAnZhi;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 授权信息记录DAO接口
 *
 * @author 授权信息记录
 * @version 2017-04-26
 */
@MyBatisDao
public interface SysCasAnZhiDao extends CrudDao<SysCasAnZhi> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public SysCasAnZhi get(String id);

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public SysCasAnZhi get(SysCasAnZhi entity);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<SysCasAnZhi>());
     * @param entity
     * @return
     */
    public List<SysCasAnZhi> findList(SysCasAnZhi entity);

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<SysCasAnZhi> findAllList(SysCasAnZhi entity);

    /**
     * 查询所有数据列表
     * @see public List<SysCasAnZhi> findAllList(SysCasAnZhi entity)
     * @return
     */
    @Deprecated
    public List<SysCasAnZhi> findAllList();

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(SysCasAnZhi entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    public int update(SysCasAnZhi entity);

    /**
     * 批量更新Enable数据
     * @param entity
     * @return
     */
    public void updateByPlEnable(@Param("ids") List<String> ids, @Param("enable") Boolean enable);

    /**
     * 批量更新DelFlag数据
     * @param entity
     * @return
     */
    public void updateByPlDelFlag(@Param("ids") List<String> ids, @Param("delFlag") String delFlag);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param id
     * @see public int delete(SysCasAnZhi entity)
     * @return
     */
    @Deprecated
    public int delete(String id);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param entity
     * @return
     */
    public int delete(SysCasAnZhi entity);
}