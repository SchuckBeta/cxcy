package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwFloorDesigner;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 楼层设计DAO接口.
 * @author 章传胜
 * @version 2017-11-28
 */
@MyBatisDao
public interface PwFloorDesignerDao extends CrudDao<PwFloorDesigner> {
    /**
     * 根据楼层ID获取单条数据
     * @param floorid
     * @return PwFloorDesigner
     */
    public PwFloorDesigner getByFloorid(@Param("floorid") String floorid);

    public int insertAll(@Param("list") List<PwFloorDesigner> list);
    public void deleteAll(@Param("ids") List<String> ids);
}