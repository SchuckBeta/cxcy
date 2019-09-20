package com.oseasy.pw.modules.pw.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.TreeDao;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwRoom;
import com.oseasy.pw.modules.pw.entity.PwSpace;

/**
 * 设施DAO接口.
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwSpaceDao extends TreeDao<PwSpace> {

    /**
     * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
     * @param entity
     * @return
     */
    @FindListByTenant
    public List<PwSpace> findListRooms(PwSpace entity);

    @Override
    @FindListByTenant
    public List<PwSpace> findList(PwSpace entity) ;

    @Override
    @InsertByTenant
    public  int insert(PwSpace entity);
}