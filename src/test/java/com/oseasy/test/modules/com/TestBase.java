package com.oseasy.test.modules.com;

import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.UserService;

import com.oseasy.test.common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2019/4/17 0017.
 */
public class TestBase  extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Test
    public void test1(){
        System.out.println("检查单元测试是否正常....请勿删除");
        User user = new User();
        user.setTenantId("100");
        userService.getByMobile(user);
        System.out.println("检查单元测试是否正常....请勿删除");
    }
}
