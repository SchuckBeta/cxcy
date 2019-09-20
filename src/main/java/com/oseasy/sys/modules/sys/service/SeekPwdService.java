package com.oseasy.sys.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;

@Service
public class SeekPwdService {
	@Autowired
	private UserDao userDao;
	public User findUserByPhone(String phoneMailNumber) {
		User user = new User();
		user.setMobile(phoneMailNumber);
		user.setDelFlag("0");
		return userDao.getByMobile(user);
	}
	public void resetPassWord(String password,String phone) {
		User user = new User();
		user.setMobile(phone);
	    password = CoreUtils.entryptPassword(password);
		user.setPassword(password);
		userDao.updateUserByPhone(user);
	}
}
