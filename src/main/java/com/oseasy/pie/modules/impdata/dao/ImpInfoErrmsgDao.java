package com.oseasy.pie.modules.impdata.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pie.modules.impdata.entity.ImpInfoErrmsg;

/**
 * 导入数据错误信息表DAO接口
 * @author 9527
 * @version 2017-05-16
 */
@MyBatisDao
public interface ImpInfoErrmsgDao extends CrudDao<ImpInfoErrmsg> {
	public List<Map<String,String>> getListByImpIdAndSheet(@Param("impid")String impid,@Param("sheet")String sheet);
	public List<Map<String,String>> getListByImpId(String impid);
	public void deleteByImpId(String impid);
	public void deleteWLByImpId(ImpInfoErrmsg impErrmsg);
}