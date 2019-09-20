package com.oseasy.pro.modules.workflow.dao;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.entity.ProModelGzsmxx;
import com.oseasy.pro.modules.workflow.entity.ProModelTestgx;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * proProjectMdDAO接口.
 * @author zy
 * @version 2017-09-18
 */
@MyBatisDao
public interface ProModelTestgxDao extends WorkFlowDao<ProModelTestgx, ExpProModelVo> {

	List<ProModelGzsmxx> getByDeclareId(@Param("declareId") String declareId, @Param("actywId") String actywId);

	public int checkMdProNumber(@Param("pnum") String pnum, @Param("pid") String pid, @Param("type") String type);
//	public int checkMdProName(@Param("pname") String pname, @Param("pid") String pid, @Param("type") String type);
	ProModelTestgx getByProModelId(String proModelId);

	List<String> getBySetNoPassList();

	List<String> getByMidNoPassList();

	List<String> getByCloseNoPassList();

	List<ProModelTestgx> getListByModelIds(@Param("ids") List<String> ids);

	List<String> getAllProModelTestgx();

	List<ProModelTestgx> getListByModel(Map<String, Object> actywId);

	int getListByModelCount(Map<String, Object> proModel);

	List<ProModelTestgx> findListByIds(ProModelTestgx proModelTestgx);

	List<ExpProModelVo> export(ProModelTestgx proModelTestgx);
}