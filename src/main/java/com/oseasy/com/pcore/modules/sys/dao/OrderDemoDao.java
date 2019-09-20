package com.oseasy.com.pcore.modules.sys.dao;


import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.OrderDemo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@MyBatisDao
public interface OrderDemoDao {

    void insert(OrderDemo order);

    List<OrderDemo> queryById2(@Param("orderIdList") List<Long> orderIdList);
}
