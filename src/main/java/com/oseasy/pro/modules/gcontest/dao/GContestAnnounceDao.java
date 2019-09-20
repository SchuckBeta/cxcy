package com.oseasy.pro.modules.gcontest.dao;

import java.util.List;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.gcontest.entity.GContestAnnounce;

/**
 * 大赛通告表DAO接口
 * @author zdk
 * @version 2017-03-29
 */
@MyBatisDao
public interface GContestAnnounceDao extends CrudDao<GContestAnnounce> {
	public GContestAnnounce getGContestByName(String name);
	

	List<GContestAnnounce> getGContestAnnounce(GContestAnnounce gContestAnnounce);
}