/**
 *
 */
package com.oseasy.com.pcore.modules.gen.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.gen.entity.GenScheme;

/**
 * 生成方案DAO接口


 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {


    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    public List<GenScheme> findClass(GenScheme entity);
}
