package com.oseasy.com.pcore.modules.sys.service;

import com.oseasy.com.pcore.common.aop.datasource.DataSource;
import com.oseasy.com.pcore.common.aop.datasource.DataStype;
import com.oseasy.com.pcore.common.aop.datasource.DynamicDataSource;
import com.oseasy.com.pcore.modules.sys.dao.OrderDemoDao;
import com.oseasy.com.pcore.modules.sys.entity.OrderDemo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional(readOnly = true)
public class OrderDemoService {

    @Autowired
    OrderDemoDao dao;

    @Transactional(readOnly = false)
    public  void insert(OrderDemo order){
        dao.insert(order);
    }

    @DataSource("slaveDataSource")
    public List<OrderDemo> queryById2(@Param("orderIdList") List<Long> orderIdList){
        List<OrderDemo> orderDemos = dao.queryById2(orderIdList);

        return orderDemos;
    }


    public List<OrderDemo> queryById(@Param("orderIdList") List<Long> orderIdList){
        List<OrderDemo> orderDemos = dao.queryById2(orderIdList);
        return orderDemos;
    }

}
