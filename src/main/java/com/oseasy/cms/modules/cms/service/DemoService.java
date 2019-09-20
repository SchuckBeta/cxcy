package com.oseasy.cms.modules.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;

/**
 * Created by victor on 2017/2/23.
 */
@Service
public class DemoService {

    @Autowired
    UserDao dao;

    public void find(User user) {
        Long lo =dao.findAllCount(user);
        System.out.println("l::::::::"+lo);
    }

}
