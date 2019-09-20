package com.oseasy.pro.modules.proprojectmd.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.proprojectmd.entity.ProModelMd;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.vo.ExpProModelMdVo;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

/**
 * proProjectMdDAO接口.
 * @author zy
 * @version 2017-09-18
 */
@MyBatisDao
public interface ProModelMdDao extends WorkFlowDao<ProModelMd, ExpProModelVo> {

	List<ProModelMd> getByDeclareId(@Param("declareId") String declareId, @Param("actywId") String actywId);
	public void updatePnum(@Param("pnum") String pnum, @Param("id") String id);
	public void updateApprovalResult(@Param("result") String result, @Param("pid") String promodelid);
	public void updateMidResult(@Param("result") String result, @Param("pid") String promodelid);
	public void updateCloseResult(@Param("result") String result, @Param("pid") String promodelid);
	public int checkMdProNumber(@Param("pnum") String pnum, @Param("pid") String pid, @Param("type") String type);
	@FindListByTenant
	public int checkMdProName(@Param("pname") String pname, @Param("pid") String pid, @Param("type") String type);
	ProModelMd getByProModelId(String proModelId);

	List<String> getBySetNoPassList();

	List<String> getByMidNoPassList();

	List<String> getByCloseNoPassList();

	List<ProModelMd> getListByModelIds(@Param("ids") List<String> ids);

	List<String> getAllPromodelMd();

	List<ProModelMd> getListByModel(Map<String, Object> map);

	int getListByModelCount(Map<String, Object> proModel);

    /**
     * 民大查询导出.
     * @param entity
     * @return
     */
    List<ExpProModelMdVo> exportMdQuery(ProModelMd entity);
}