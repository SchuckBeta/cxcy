package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwCategory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源类别DAO接口.
 *
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwCategoryDao extends TreeDao<PwCategory> {

    /**
     * 查询指定父id的直接子资产类别
     *
     * @param parentIds
     * @return
     */
    List<PwCategory> findListByParentIds(@Param("parentIds") List<String> parentIds);

}