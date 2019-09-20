package com.oseasy.test.datasource;

import com.oseasy.com.pcore.common.aop.datasource.DataStype;
import com.oseasy.com.pcore.common.aop.datasource.DynamicDataSource;
import com.oseasy.com.pcore.modules.sys.entity.OrderDemo;
import com.oseasy.com.pcore.modules.sys.service.OrderDemoService;
import com.oseasy.test.common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class DatabaseTest extends BaseTest {

    @Autowired
    private OrderDemoService orderDemoService;

    @Test
    public void test1() {
        System.err.println("---------------------------------------------");
//        OrderDemo orderDemo =new OrderDemo();
//        long orderId = System.nanoTime();
//        orderDemo.setOrderId(orderId);
//        long userId = System.nanoTime();
//        orderDemo.setUserId(userId);
//        System.err.println("写入的参数："+orderDemo.toString());
//        orderDemoService.insert(orderDemo);
//        String dataSourceName = DataStype.SLAVE.getName();
//        DynamicDataSource.setDataSource(dataSourceName);
        List<OrderDemo>  list = orderDemoService.queryById2(Arrays.asList(2l,1l));
        list.forEach(n -> System.err.println("slave  查询的参数："+n.toString()));

        list = orderDemoService.queryById(Arrays.asList(2l,1l));
        list.forEach(n -> System.err.println("master 查询的参数："+n.toString()));

    }

    @Test
    public void test3() {
        String dataSourceName = DataStype.SLAVE.getName();
        DynamicDataSource.setDataSource(dataSourceName);
        System.err.println("当前数据库：" + DynamicDataSource.getDataSource()+ "数据库");
        OrderDemo orderDemo =new OrderDemo();
        long orderId = System.nanoTime();
        orderDemo.setOrderId(orderId);
        long userId = System.nanoTime();
        orderDemo.setUserId(userId);
        orderDemoService.insert(orderDemo);

    }


    @Test
    public void test2() {
          System.err.println("测试.............");
    }


}
