package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwFassets;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 固定资产DAO接口.
 *
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwFassetsDao extends CrudDao<PwFassets> {

    /**
     * 获取已分配资产.
     *
     * @param pwFassets
     * @return List
     */
    public List<PwFassets> findListByRoom(PwFassets pwFassets);


    /**
     * 获取未分配资产.
     *
     * @param pwFassets
     * @return List
     */
    public List<PwFassets> findListByNoRoom(PwFassets pwFassets);

    /**
     * 批量更新数据.
     *
     * @param entitys
     * @return
     */
    public int updateByPL(@Param("entitys") List<PwFassets> entitys);

    /**
     * 根据Id查询数据.
     *
     * @param entitys
     * @return
     */
    public List<PwFassets> findListByIds(@Param("ids") List<String> ids);

    /**
     * 批量更新指定的roomId的记录为闲置
     *
     * @param roomIds
     * @return
     */
    int updateByRoomIds(@Param("roomIds") List<String> roomIds);

    /**
     * 批量添加资产
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<PwFassets> list);
}