package com.oseasy.pro.modules.promodel.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.fileserver.modules.attachment.entity.SysAttachment;
import com.oseasy.com.pcore.common.persistence.annotation.FindListByTenant;
import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import com.oseasy.pro.modules.promodel.entity.ProRole;
import org.apache.ibatis.annotations.Param;

import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.pro.modules.promodel.vo.GcontestExpData;
import com.oseasy.pro.modules.promodel.vo.GcontestStuExpData;
import com.oseasy.pro.modules.promodel.vo.GcontestTeaExpData;
import com.oseasy.pro.modules.workflow.WorkFlowDao;
import com.oseasy.pro.modules.workflow.vo.ExpProModelVo;

/**
 * proModelDAO接口.
 *
 * @author zy
 * @version 2017-07-13
 */
@MyBatisDao
public interface ProModelDao extends WorkFlowDao<ProModel, ExpProModelVo> {
    @FindListByTenant
	public Integer getByNumberAndId(@Param("number")String number,@Param("id")String id);

    @FindListByTenant
	Integer checkNumberByActYwId(@Param("number")String number,@Param("id")String id ,@Param("actYwId")String actYwId);
    @FindListByTenant
    public Integer getCountByName(@Param("protype")String protype,@Param("type")String type,@Param("name")String name);

    public void updateSubStatus(@Param("pid")String pid,@Param("subStatus")String subStatus);

    @FindListByTenant
    int checkProName(@Param("pname") String pname, @Param("pid") String pid, @Param("actywId") String actywId);
    @FindListByTenant
    public List<String> getProcinsidByActywid(String actywid);

	public void deleteReportByActywid(String actywid);
	public void deleteTeamUserHisByActywid(String actywid);
	public void deleteByActywid(String actywid);
    @FindListByTenant
	public List<GcontestExpData> getGcontestExpData(ProModel param);
    @FindListByTenant
	public List<GcontestStuExpData> getGcontestStuExpData(ProModel param);
    @FindListByTenant
	public List<GcontestTeaExpData> getGcontestTeaExpData(ProModel param);
    @FindListByTenant
	String getUnSubProIdByCdn(@Param("uid") String uid, @Param("protype") String protype, @Param("subtype") String subtype);

    public void modifyLeaderAndTeam(@Param("uid") String uid, @Param("tid") String tid, @Param("pid") String pid);

    public void myDelete(String id);


    public ProModel getByProInsId(String proInsId);

//    Page<ProModel> getPromodelList(ProModel proModel);

    public void updateResult(@Param("result") String result, @Param("pid") String promodelid);

    int getProModelAuditListCount(ProModel proModel);


    public ProModel getProScoreConfigure(String proId);

    public ProModel get(String id);
    /**
     * 根据leaderId获取我的项目.
     *
     * @param leaderId 团队负责人
     * @return List
     */
//    public List<ProModel> findListByLeader(@Param("uid") String uid);

    /**
     * 根据leaderId获取我的项目和大赛.
     *
     * @param leaderId 团队负责人
     * @return List
     */
    @FindListByTenant
    public List<ProModel> findListAllByLeader(@Param("uid") String uid);

    void updateState(@Param("state") String state, @Param("id") String id);
    @FindListByTenant
    List<ProModel> findListByIds(ProModel proModel);

	@FindListByTenant
    List<ProModel> findListByIdsOfSt(ProModel proModel);

	@FindListByTenant
    List<ProModel> findListByIdsOfBefore(ProModel proModel);

	/**
	 * 按专家查询
	 * @param proModel
	 * @return
	 */
	@FindListByTenant
    List<ProModel> findListByIdsOfProfessor(ProModel proModel);

    @FindListByTenant
    List<ProModel> findListByIdsUnAudit(ProModel proModel);
    @FindListByTenant
    List<ProModel> findIsOverHaveData(@Param("actYwId") String actYwId, @Param("state") String state);
    @FindListByTenant
    List<ProModel> findIsHaveData(@Param("actYwId") String actYwId);
    @FindListByTenant
    List<ProModel> getListByGroupId(@Param("groupId") String groupId);
    @FindListByTenant
    List<String> findListByIdsWithoutJoin(ProModel proModel);
    @FindListByTenant
    List<ProModel> findImportList(ProModel proModel);

    @FindListByTenant
    List<ProRole> findRolesByIds(ProRole entity);

	void deleteTeamUserHisByProModelId(@Param("id") String id);

 	void deleteReportByProModelId(@Param("id") String id);

    List<ExpProModelVo> export(ProModel proModel);

	void updateFinalStatus(@Param("id") String id, @Param("finalStatus")String finalStatus);

	void updateFinalStatusPl(@Param("idsList")List<String> idsList, @Param("finalStatus")String finalStatus);

	ProModel getExcellentById(String id);

	ProModel getGcontestExcellentById(String id);

//    @FindListByTenant
	List<ProModel> findListByIdsAssign(ProModel proModel);


	List<ProModel> findListByIdsAssignToClear(ProModel proModel);

	List<ProModel> findListByIdsAssignOfSchool(ProModel proModel);

	void updateLogo(ProModel old);
    /**
     * 根据项目名和类型获取项目（第一个）.
     * @param pm 项目
     * @return ProModel
     */
    @FindListByTenant
    public ProModel getByName(ProModel pm);

    @Override
    @InsertByTenant
   	public int insert(ProModel entity);


    int insertOfTopic(ProModel entity);

	/**
	 * 批量插入
	 * @param list
	 * @return int
	 */
	@InsertByTenant
	int bulk(List<ProModel> list);

    ProModel getWith(String modelId);

	/**
	 * 修改推送状态
	 * @param id proModelId
	 * @param isSend 推送状态
	 * @return Integer
	 */
	Integer updateIsSendById(@Param("id")String id,@Param("isSend")String isSend);

    List<ProModel> findProvListByIdsAssign(ProModel proModel);

    List<ProModel> findProvListByIdsAssignOfId(ProModel proModel);


    ProModel getProModelId(@Param("id")String id);

    void updateByProId(@Param("id")String id,@Param("ranking")Integer ranking);

	/**
	 * 查找名次相同的大赛
	 * @param id
	 * @param ranking
	 * @return
	 */
    @FindListByTenant
    List<String> getIdsByRanking(@Param("ranking")Integer ranking,@Param("id")String id,@Param("actYwId")String actYwId);


}