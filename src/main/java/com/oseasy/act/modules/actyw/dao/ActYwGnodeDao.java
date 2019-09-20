package com.oseasy.act.modules.actyw.dao;

import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.persistence.annotation.InsertByTenant;
import org.apache.ibatis.annotations.Param;

import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.com.pcore.common.persistence.CrudDao;
import com.oseasy.com.pcore.common.persistence.annotation.MyBatisDao;

/**
 * 流程节点DAO接口.
 * @author chenh
 * @version 2018-01-15
 */
@MyBatisDao
public interface ActYwGnodeDao extends CrudDao<ActYwGnode> {
	public List<Map<String,String>> getAssignUserNames(@Param("pids") List<String> pids,@Param("gnodeid") String gnodeid);
	public List<ActYwGnode> getAssignNodes(String actywid);
	public List<ActYwGnode> findGnodesByDelegate(String actywid);
  /**
   * 物理删除.
   * @param entity
   */
  public void deleteWL(ActYwGnode actYwGnode);

  public List<ActYwGnode> getALLByGroup(ActYwGnode actYwGnode);

  /**
   * 根据group批量物理删除.
   * @param entity
   */
  public void deletePlwlByGroup(@Param("groupId") String groupId);
  public int savePl(@Param("list") List<ActYwGnode> list);

  public List<ActYwGnode> getALLVersionByGroup(@Param("version") String version, @Param("groupId") String groupId);
  public int savePlVersion(@Param("version") String version, @Param("list") List<ActYwGnode> list);
  public void deleteVersionPlwlByGroup(@Param("version") String version, @Param("groupId") String groupId);

  /**
   * 根据流程查询流程节点.
   * @param gnode
   * @return List
   */
  public List<ActYwGnode> findListByGroup(ActYwGnode gnode);
  @InsertByTenant
  public List<ActYwGnode> findListBytGroup(ActYwGnode gnode);



    /**
     * 根据IDS查询流程节点.
     * @param ids 节点ID
     * @return List
     */
    public List<ActYwGnode> findListByInIds(@Param("ids") List<String> ids);

    /**
     * 根据流程查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.
     * @param gnode
     * @return List
     */
    public List<ActYwGnode> findListBygGroup(ActYwGnode gnode);
    /**
     * 根据父节点模糊查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.
     * @param actYwGnode 父节点IDS
     * @return List
     */
    public List<ActYwGnode> findListBygParentIdsLike(ActYwGnode actYwGnode);
    /**
     * 根据IDS查询流程节点 GROUP BY ID （关联表单、角色、用户、状态）.
     * @param ids 节点ID
     * @return List
     */
    public List<ActYwGnode> findListBygInIds(@Param("ids") List<String> ids);
    public List<ActYwGnode> findListByg(ActYwGnode gnode);
    public ActYwGnode getByg(@Param("id") String id);

    /**
     * 关联查询所有-表单（非委派）.
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListFormByg(ActYwGnode gnode);
    /**
     * 关联查询所有-表单（委派）.
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListDformByg(ActYwGnode gnode);
    /**
     * 关联查询所有-状态.
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListStatusByg(ActYwGnode gnode);
    /**
     * 关联查询所有-角色(非委派).
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListRoleByg(ActYwGnode gnode);
    /**
     * 关联查询所有-角色(委派).
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListDroleByg(ActYwGnode gnode);
    /**
     * 关联查询所有-用户.
     * @param gnode groupId必填
     * @return List
     */
    public List<ActYwGnode> findAllListUserByg(ActYwGnode gnode);
    /**
     * 关联查询所有-时间.
     * @param gnode groupId和actYwGtime.projectId必填
     * @return List
     */
    public List<ActYwGnode> findAllListTimeByg(ActYwGnode gnode);
    /**
     * 关联查询所有-监听.
     * @param gnode groupId和listener.projectId必填
     * @return List
     */
    public List<ActYwGnode> findAllListClazzByg(ActYwGnode gnode);

    /**
     * 根据流程ID获取根开始节点.
     * @param groupId 流程ID
     * @return ActYwGnode
     */
    public ActYwGnode getStart(@Param("groupId") String groupId);
    public ActYwGnode getStartByg(@Param("groupId") String groupId);
    public List<ActYwGnode> findListByPre(@Param("preId") String preId);

    public ActYwGnode getFirstTask(@Param("ywid") String ywid, @Param("groupId") String groupId, @Param("type") String type);
    public ActYwGnode getLastTask(@Param("ywid") String ywid, @Param("groupId") String groupId, @Param("type") String type);

    /**
     * 根据流程ID获取根结束节点.
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getEndByRoot(@Param("groupId") String groupId);
    public List<ActYwGnode> getEndNameByRoot(@Param("groupId") String groupId);
    public List<ActYwGnode> getEndBygRoot(@Param("groupId") String groupId);

    /**
     * 根据流程ID获取子流程结束节点.
     * @param groupId 流程ID
     * @return List
     */
    public List<ActYwGnode> getEndBySub(@Param("groupId") String groupId, @Param("nodeIds") List<String> nodeIds);

    /**
     * 根据流程ID和前线节点获取结束节点或子流程结束（事件结束）.
     * @param groupId 流程ID
     * @param preIds 前线节点ID
     * @param nodeIds 节点ID
     * @return List
     */
    public List<ActYwGnode> getEnds(@Param("groupId") String groupId, @Param("preIds") List<String> preIds, @Param("nodeIds") List<String> nodeIds);
    /**
     * 根据流程ID和前业务节点获取结束节点或子流程结束（事件结束）.
     * @param groupId 流程ID
     * @param preIds 前业务节点ID
     * @param nodeIds 节点ID
     * @return List
     */
    public List<ActYwGnode> getEndsByPre(@Param("groupId") String groupId, @Param("preIds") List<String> preIds, @Param("nodeIds") List<String> nodeIds);
    /**
     * 根据流程ID和前前业务节点获取结束节点或子流程结束（事件结束）.
     * @param groupId 流程ID
     * @param preIds 前前业务节点ID
     * @param nodeIds 节点ID
     * @return List
     */
    public List<ActYwGnode> getEndsByPpre(@Param("groupId") String groupId, @Param("preIds") List<String> preIds, @Param("nodeIds") List<String> nodeIds);

    /**根据当前获取下个节点*/
   public List<ActYwGnode> getNextNode(ActYwGnode fnode);
   public List<ActYwGnode> getBygNextNode(ActYwGnode fnode);

   /**根据当前获取下下个节点*/
   public List<ActYwGnode> getNextNextNode(ActYwGnode fnode);

    /**
     * 查找符合生成菜单和栏目的的节点.
     * @param gnode 节点（group.id）
     * @return List
     */
    public List<ActYwGnode> findListBygMenu(ActYwGnode gnode);

    /**
     * 查找当前子流程节点的所有子节点.
     * group.id和parent.id必填
     * type可选（GnodeType）
     * @param gnode 节点
     * @return List
     */
    public List<ActYwGnode> findListByGparent(ActYwGnode gnode);

    public List<ActYwGnode> getChildFlowAllNode(ActYwGnode fnode);

  List<ActYwGnode> getAuditNodes(@Param("actywId")String actywid);

  List<Map<String,String>> getAssignUsers(@Param("promodelId")String promodelId, @Param("gnodeId")String gnodeId);

  List<ActYwGnode> getDelegateNode(@Param("actywId")String actywId);

  @InsertByTenant
  @Override
  int insert(ActYwGnode actYwGnode);
}