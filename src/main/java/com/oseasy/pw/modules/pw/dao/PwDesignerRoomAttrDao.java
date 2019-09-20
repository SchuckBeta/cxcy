package com.oseasy.pw.modules.pw.dao;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pw.modules.pw.entity.PwDesignerRoomAttr;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间属性表DAO接口.
 * @author zy
 * @version 2017-12-18
 */
@MyBatisDao
public interface PwDesignerRoomAttrDao extends CrudDao<PwDesignerRoomAttr> {

	void deleteAllByRoomId(@Param("ids") List<String> ids);

	List<PwDesignerRoomAttr> findListByRid(@Param("rid") String rid);
}