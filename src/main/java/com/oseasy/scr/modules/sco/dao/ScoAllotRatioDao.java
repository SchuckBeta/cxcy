package com.oseasy.scr.modules.sco.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.scr.modules.sco.entity.ScoAllotRatio;
import com.oseasy.scr.modules.sco.vo.ScoRatioVo;

/**
 * 学分分配比例DAO接口.
 * @author 9527
 * @version 2017-07-18
 */
@MyBatisDao
public interface ScoAllotRatioDao extends CrudDao<ScoAllotRatio> {
	public List<ScoAllotRatio> findAll(String confId);
	public ScoRatioVo findRatio(ScoRatioVo scoRatioVo);
	public int checkNumber(@Param("id") String id,@Param("number") String number,@Param("confid") String confid);
	public void delByFid(String fid);
}