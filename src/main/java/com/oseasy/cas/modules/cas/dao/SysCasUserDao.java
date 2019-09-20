package com.oseasy.cas.modules.cas.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.cas.modules.cas.entity.SysCasUser;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;


/**
 * 授权信息记录DAO接口
 *
 * @author 授权信息记录
 * @version 2017-04-26
 */
@MyBatisDao
public interface SysCasUserDao extends CrudDao<SysCasUser> {
    /**
     * 获取单条数据
     * @param id
     * @return
     */
    public SysCasUser get(String id);

    /**
     * 获取单条数据
     * @param entity
     * @return
     */
    public SysCasUser get(SysCasUser entity);

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<SysCasUser>());
     * @param entity
     * @return
     */
    public List<SysCasUser> findList(SysCasUser entity);

    /**
     * 查询所有数据列表
     * @param entity
     * @return
     */
    public List<SysCasUser> findAllList(SysCasUser entity);

    /**
     * 查询所有数据列表
     * @see public List<SysCasUser> findAllList(SysCasUser entity)
     * @return
     */
//    @Deprecated
    public List<SysCasUser> findAllList();

    /**
     * 插入数据
     * @param entity
     * @return
     */
    public int insert(SysCasUser entity);

    /**
     * 更新数据
     * @param entity
     * @return
     */
    public int update(SysCasUser entity);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param id
     * @see public int delete(SysCasUser entity)
     * @return
     */
    @Deprecated
    public int delete(String id);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     * @param entity
     * @return
     */
    public int delete(SysCasUser entity);

    /**
     * 根据Uid批量删除数据
     * @param uid
     * @return
     */
    public void deletePlwlByUid(@Param("uid") String uid);

    /**
     * 批量修改CAS状态.
     * @param entity
     */
    public void updateALLEnable(SysCasUser entity);

    /**
     * 批量保存CAS状态.
     * @param sysCasUsers
     */
    public void savePL(@Param("entitys") List<SysCasUser> entitys);
}