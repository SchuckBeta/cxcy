package com.oseasy.pw.modules.pw.dao;

import java.util.List;
import java.util.Set;

import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.com.pcore.common.persistence.annotation.PageNcount;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.vo.PwEnterInfo;
import com.oseasy.pw.modules.pw.vo.PwEnterRoomVo;

/**
 * 入驻申报DAO接口.
 *
 * @author chenh
 * @version 2017-11-26
 */
@MyBatisDao
public interface PwEnterDao extends CrudDao<PwEnter> {
	public List<PwEnterRoomVo> getPwRoomInfo(@Param("enids")Set<String> enids);
	public PwEnterInfo getMaxTeamEndDate(String uid);
	public PwEnterInfo getMaxProEndDate(String uid);
	public PwEnterInfo getMaxComEndDate(String uid);
	public Long getCount(PwEnter p);
	public Long getCountBGSH(PwEnter p);
    public Long getCountRstatus(PwEnter p);

	public void deleteWL(PwEnter pwEnter);

	public void updateStatus(PwEnter pwEnter);
	public void updateIsShow(PwEnter pwEnter);

	void updatePwEnterType(PwEnter pwEnter);


    /**
     * 批量状态删除.
     * @param entity
     */
    public void deletePL(@Param("ids") List<String> ids);

    /**
     * 查询列表支持关键之查询.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findListKeys(PwEnter pwEnter);

    @FindListByTenant
    public List<PwEnter> findListRoomKeys(PwEnter pwEnter);
    @FindListByTenant
    public List<PwEnter> findListRoomYTFKeys(PwEnter pwEnter);
    @PageNcount
    public List<PwEnter> findListRoomYGHKeys_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-团队.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findAllListTeamByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListTeamByg_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-项目.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findAllListProjectByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListProjectByg_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-企业.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findAllListCompanyByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListCompanyByg_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-房间.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findAllListRoomByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListRoomByg_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-申报.
     * @param pwEnter id必填
     * @return List
     */
    @FindListByTenant
    public List<PwEnter> findAllListApplyByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListApplyByg_NoCount(PwEnter pwEnter);


    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListApplyRsByg_NoCount(PwEnter pwEnter);
    /**
     * 关联查询所有-最后的申请记录.
     * @param pwEnter
     * @return
     */
    @FindListByTenant
    public List<PwEnter> findAllListAppRecordRmaxByg(PwEnter pwEnter);
    @PageNcount
    @FindListByTenant
    public List<PwEnter> findAllListApplyRmaxByg_NoCount(PwEnter pwEnter);

    public PwEnter getByParentId(String parentId);
    @FindListByTenant
	List<PwEnter> findFrontList(PwEnter pwEnter);

	void updateApplicant(@Param("id")String id, @Param("fzr")String fzr);
}