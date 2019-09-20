/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.oseasy.pro.modules.dasai.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.dasai.entity.OaDasai;

/**
 * 大赛测试DAO接口
 * @author zhangzheng
 * @version 2017-02-24
 */
@MyBatisDao
public interface OaDasaiDao extends CrudDao<OaDasai> {

    public int updateStateAndScore(OaDasai oaDasai);
	
}