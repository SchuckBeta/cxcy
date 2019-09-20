package com.oseasy.scr.modules.sco.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.scr.modules.sco.entity.ScoAffirmConf;

/**
 * 学分认定配置DAO接口.
 * 
 * @author 9527
 * @version 2017-07-18
 */
@MyBatisDao
public interface ScoAffirmConfDao extends CrudDao<ScoAffirmConf> {
	public List<Dict> getDictForScoAffirm(@Param(value = "ids") Set<String> ids,@Param(value = "type") String type);
	public int check(@Param(value = "id") String id, @Param(value = "type") String type, @Param(value = "item") String item,
			@Param(value = "category") String category,@Param(value="subdivision")  String subdivision);
	public List<ScoAffirmConf> findAll();
	public void updateProc(@Param(value = "id") String id,@Param(value = "proc") String proc);
	public List<Map<String,String>> getSetData(@Param(value = "item") String item,@Param(value = "category")String category);
	public List<Map<String,String>> getTypeSetData(@Param(value = "item") String item);
	List<ScoAffirmConf> getByItem(String item);
}