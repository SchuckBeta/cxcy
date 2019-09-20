/**
 * .
 */

package com.oseasy.pro.modules.promodel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.service.CoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.entity.Act;
import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwAuditInfo;
import com.oseasy.act.modules.actyw.entity.ActYwGassign;
import com.oseasy.act.modules.actyw.entity.ActYwGnode;
import com.oseasy.act.modules.actyw.entity.ActYwGstatus;
import com.oseasy.act.modules.actyw.entity.ActYwStatus;
import com.oseasy.act.modules.actyw.exception.GroupErrorException;
import com.oseasy.act.modules.actyw.service.ActYwAuditInfoService;
import com.oseasy.act.modules.actyw.service.ActYwGassignService;
import com.oseasy.act.modules.actyw.service.ActYwGnodeService;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.service.ActYwStatusService;
import com.oseasy.act.modules.actyw.tool.apply.IActYw;
import com.oseasy.act.modules.actyw.tool.apply.IApply;
import com.oseasy.act.modules.actyw.tool.apply.IAstatus;
import com.oseasy.act.modules.actyw.tool.process.ActYwTool;
import com.oseasy.act.modules.actyw.tool.process.vo.FormClientType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeTaskType;
import com.oseasy.act.modules.actyw.tool.process.vo.GnodeType;
import com.oseasy.act.modules.actyw.tool.process.vo.RegType;
import com.oseasy.act.modules.actyw.tool.process.vo.StenType;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.sys.common.config.SysIds;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class ProActService {
    private static final String SCORE_100 = "100";
    @Autowired
    private ActYwService actYwService;
    @Autowired
    private ActYwGnodeService actYwGnodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActYwStatusService actYwStatusService;
    @Autowired
    private ActYwGassignService actYwGassignService;
    @Autowired
    private ActYwAuditInfoService actYwAuditInfoService;
    @Autowired
    private OaNotifyService oaNotifyService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ActTaskService actTaskService;
    @Autowired
    private ProActTaskService proActTaskService;
    @Autowired
    CoreService coreService;
    /**
     * 设置项目流程.
     * 1、如果actYwId == null,则设置当前默认的流程.
     * 2、如果actYwId != null,则设置流程.
     */
    public IApply initActYw(IApply apply) {
        if((apply.iactYw().group().flowType() != null) && StringUtil.isNotEmpty(apply.iactYw().config().ptype())){
            apply.iactYw(actYwService.findCurrsByflowTypeAndPType(apply.iactYw()));
        }else{
            apply.iactYw(actYwService.get(apply.iactYw().id()));
        }
        return apply;
    }

    /**
     * 判断下一个节点是否为网关节点.
     * @param gnodeId
     * @return boolean
     */
    public boolean getNextIsGate(String gnodeId) {
        Boolean res = false;
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (actYwGnode != null) {
            // 如果节点为子流程
            if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
                // 节点之间有连接线
                // 根据节点得到下一个节点
                List<ActYwGnode> gnodes = actYwGnodeService.getNextNextNode(actYwGnode);
                // 判断下一个节点是否为网关
                if (StringUtil.checkNotEmpty(gnodes)) {
                    if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(gnodes.get(0).getType())) {
                        res = isGateByList(gnodes);
                    } else if (GnodeType.GT_PROCESS_END.getId().equals(gnodes.get(0).getType())) {
                        ActYwGnode subActYwGnode = actYwGnodeService.get(gnodes.get(0).getParentId());
                        if (subActYwGnode != null) {
                            // 判断list是否为网关。
                            res = isGateByList(actYwGnodeService.getNextNextNode(subActYwGnode));
                        }
                    }
                }
            } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
                // 如果节点为根流程
                res = isGateByList(actYwGnodeService.getNextNextNode(actYwGnode));
            }
        }
        return res;
    }

    public boolean isGateByList(List<ActYwGnode> gnodes) {
        boolean res = false;
        if (StringUtil.checkNotEmpty(gnodes)) {
            if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(gnodes.get(0).getType())
                    || GnodeType.GT_ROOT_GATEWAY.getId().equals(gnodes.get(0).getType())) {
                res = true;
            }
        }
        return res;
    }

    /**
     * 根据节点ID获取节点.
     * @param gnodeId
     * @return ActYwGnode
     */
    public ActYwGnode getByg(String gnodeId) {
        return actYwGnodeService.getByg(gnodeId);
    }

    /**
     * 根据当前结点获取网关节点的下一个节点.
     * @param id
     * @return ActYwGnode
     */
    public ActYwGnode getNextNextGate(String id) {
        return getBygGclazz(getNextGate(id));
    }

    /**
     * 网关流程根据当前节点得到网关节点.
     * @param gnodeId
     * @return ActYwGnode
     */
    public ActYwGnode getNextGate(String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                boolean res = isGateByList(actYwGnodeList);
                if (res) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    return gateGnode;
                    // 网关下的分支线
                }
                if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    } else {
                        ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                        if (subActYwGnode != null) {
                            List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                            // 判断list是否为网关。
                            res = isGateByList(subActYwGnodeList);
                            if (res) {
                                return subActYwGnodeList.get(0);
                            }
                        }
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            boolean res = isGateByList(actYwGnodeList);
            if (res) {
                return actYwGnodeList.get(0);
            }
        }
        return null;
    }

    /**
     * 根据当前结点获取节点的下一个节点.
     * @param id
     * @return ActYwGnode
     */
    public ActYwGnode getNextNextGnode(String id) {
        return getBygGclazz(getNextGnode(id));
    }

    /**
     * 根据结点获取节点的ActYwGclazz.

     * @return ActYwGnode
     */
    public ActYwGnode getBygGclazz(ActYwGnode nextGnode) {
        return actYwGnodeService.getBygGclazz(nextGnode.getId());
    }

   /**
    * 得到下一个节点通过gnodeId 无网关.
    * @param gnodeId
    * @return ActYwGnode
    */
    public ActYwGnode getNextGnode(String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        List<ActYwGnode> nextActYwGnodes = actYwGnodeService.getNextNextNode(actYwGnode);
        ActYwGnode noGate = null;
        if (nextActYwGnodes != null && nextActYwGnodes.size() > 0) {
            noGate = nextActYwGnodes.get(0);
            if (GnodeType.GT_PROCESS_END.getId().equals(noGate.getType())) {
                // 得到父节点
                ActYwGnode subActYwGnode = actYwGnodeService.get(noGate.getParentId());
                if (subActYwGnode != null) {
                    // 得到父节点下一个节点
                    List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                    if (subActYwGnodeList != null && subActYwGnodeList.size() > 0) {
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        if (GnodeType.GT_ROOT_END.getId().equals(gateGnode.getType())) {
                            return gateGnode;
                        } else if (GnodeType.GT_PROCESS.getId().equals(gateGnode.getType())) {
                            // 得到子流程中第一个任务节点userTask
                            ActYwGnode toSubNextGnode = getFirstGnode(gateGnode);
                            toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                            if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                                return toSubNextGnode;
                            }
                        } else {
                            gateGnode = actYwGnodeService.getByg(gateGnode.getId());
                            return gateGnode;
                        }
                    }
                }
            } else if (GnodeType.GT_PROCESS.getId().equals(noGate.getType())) {
                // 得到子流程中第一个任务节点userTask
                ActYwGnode toSubNextGnode = getFirstGnode(noGate);
                toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                    return toSubNextGnode;
                }
            }
            noGate = actYwGnodeService.getByg(noGate.getId());
        }
        return noGate;
    }

    // 网关得到下一个子流程节点的第一个节点
    public ActYwGnode getFirstGnode(ActYwGnode toNextGnode) {
        List<ActYwGnode> childFirstGnode = actYwGnodeService.getChildFlowsSecondNode(toNextGnode);
        ActYwGnode toSubNextGnode = null;
        if (childFirstGnode != null) {
            toSubNextGnode = childFirstGnode.get(0);
        }
        return toSubNextGnode;
    }

    /**
     * 获得gnodeId网关节点的状态.
     * @param id 节点ID
     * @return List
     */
    public List<IAstatus> getActYwStatus(String id) {
        List<IAstatus> actYwStatusList = null;
        ActYwGnode actYwGnode = actYwGnodeService.get(id);
        if (actYwGnode != null) {
            // 节点之间有连接线
            if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
                List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
                // 根据实例id得到下一个节点
                // 判断下一个节点是否为网关
                if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                    if (GnodeType.GT_ROOT_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                        actYwStatusList = getActYwStatusList(actYwGnodeList.get(0));
                    }
                }
            } else {
                // 节点之间有连接线 //根据实例id得到下一个节点
                List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
                // 判断下一个节点是否为网关
                if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                    if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                        actYwStatusList = getActYwStatusList(actYwGnodeList.get(0));
                        // actYwStatusList =
                        // actYwStatusService.getAllStateByGnodeId(actYwGnodeList.get(0).getId());
                    } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                        ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                        if (subActYwGnode != null) {
                            List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                            if (subActYwGnodeList != null && subActYwGnodeList.size() > 0) {
                                if (GnodeType.GT_ROOT_GATEWAY.getId().equals(subActYwGnodeList.get(0).getType())) {
                                    actYwStatusList = getActYwStatusList(subActYwGnodeList.get(0));
                                    // actYwStatusList =
                                    // actYwStatusService.getAllStateByGnodeId(subActYwGnodeList.get(0).getId());
                                }
                            }
                        }
                    }
                }

            }

        }
        return actYwStatusList;
    }

    /**
     * 根据网关节点得到状态.
     * @param gnode 节点
     * @return
     */
    public List<IAstatus> getActYwStatusList(ActYwGnode gnode) {
        List<IAstatus> actYwStatusList = new ArrayList<IAstatus>();
        List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNode(gnode);
        if (StringUtil.checkNotEmpty(actYwGnodeList)) {
            for (int i = 0; i < actYwGnodeList.size(); i++) {
                actYwStatusList.addAll(actYwStatusService.getAllStateByGnodeId(actYwGnodeList.get(i).getId()));
            }
        }
        return actYwStatusList;
    }

    /**
     * 根据值传递grade实际走那个节点.
     * @param grade 结果值
     * @param actYwStatuss List 状态
     * @return ActYwStatus
     */
    public IAstatus getGateActYwStatusByGrade(String grade, List<IAstatus> actYwStatuss) {
        IAstatus actYwStatusNext = null;
        for (IAstatus actYwStatus : actYwStatuss) {
            if ((grade).equals(actYwStatus.getIstatus())) {
                actYwStatusNext = actYwStatus;
                break;
            }
        }
        return actYwStatusNext;
    }

    /**
     * 获取审核结果均值.
     * @param infoSerch 审核信息
     * @return int
     */
    public Double getAuditAvgInfo(ActYwAuditInfo infoSerch) {
        return (double) actYwAuditInfoService.getAuditAvgInfo(infoSerch);
    }

    /**
     * 网关流程根据当前节点和网关状态获得下个节点.
     * @param grade 审核结果
     * @param gnodeId 节点
     * @return ActYwGnode
     */
    public ActYwGnode getNextGnodeByGrade(String grade, String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        // 网关后的线gnode
        ActYwGnode actYwGnodeNextLine = null;
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    // 网关下的分支线
                    List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                    actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
                } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    // 直接结束节点
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    }
                    ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                    if (subActYwGnode != null) {
                        List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        // 网关下的分支线
                        List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                        actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
                        // actYwGnodeNext=getNextGnodeByGate(subActYwGnodeList,proModel);
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            ActYwGnode gateGnode = actYwGnodeList.get(0);
            // 网关下的分支线
            List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
            actYwGnodeNextLine = getNextGnodeByValue(gatelineList, grade);
            // actYwGnodeNext=getNextGnodeByGate(actYwGnodeList,proModel);
        }
        if (actYwGnodeNextLine != null) {
            ActYwGnode nextGnode = getNextGnodeByline(actYwGnodeNextLine);
            return nextGnode;
        }
        return null;
    }

    /**
     * 根据值传递判断出走的网关那个分支.
     * @param gnodeGates 网关节点列表
     * @param grade 结果
     * @return
     */
    public ActYwGnode getNextGnodeByValue(List<ActYwGnode> gnodeGates, String grade) {
        ActYwGnode actYwGnodeNext = null;
        for (ActYwGnode gateNextLineGnode : gnodeGates) {
            ActYwGnode gateNextGnode = actYwGnodeService.getByg(gateNextLineGnode.getId());
            List<ActYwGstatus> actywGstatuss = gateNextGnode.getGstatuss();
            if (actywGstatuss != null && actywGstatuss.size() > 0) {
                for (int i = 0; i < actywGstatuss.size(); i++) {
                    ActYwGstatus actYwGstatus = gateNextGnode.getGstatuss().get(i);
                    ActYwStatus actYwStatus = actYwGstatus.getStatus();
                    if (grade.equals(actYwStatus.getStatus())) {
                        actYwGnodeNext = gateNextGnode;
                        break;
                    }
                }
            }
            if (actYwGnodeNext != null) {
                break;
            }
        }
        return actYwGnodeNext;
    }

   /**
    * 网关流程根据当前节点和网关状态获得下个节点的角色.
    * @param apply
    * @param gnodeId
    * @return
    */
    public ActYwGnode getNextGnode(IApply apply, String gnodeId) {
        ActYwGnode actYwGnode = actYwGnodeService.get(gnodeId);
        // 网关后的线gnode
        ActYwGnode actYwGnodeNextLine = null;
        if (actYwGnode == null) {
            return null;
        }
        // 子流程中节点
        if (GnodeType.GT_PROCESS_TASK.getId().equals(actYwGnode.getType())) {
            // 节点之间有连接线 //根据实例id得到下一个节点
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            // 判断下一个节点是否为网关
            if (actYwGnodeList != null && actYwGnodeList.size() > 0) {
                if (GnodeType.GT_PROCESS_GATEWAY.getId().equals(actYwGnodeList.get(0).getType())) {
                    ActYwGnode gateGnode = actYwGnodeList.get(0);
                    // 网关下的分支线
                    List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                    actYwGnodeNextLine = getNextGnodeByGate(gatelineList, apply);
                } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeList.get(0).getType())) {
                    // 直接结束节点
                    StenType stenType = StenType.getByKey(actYwGnodeList.get(0).getNode().getNodeKey());
                    if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                        return actYwGnodeList.get(0);
                    }
                    ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeList.get(0).getParentId());
                    if (subActYwGnode != null) {
                        List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                        ActYwGnode gateGnode = subActYwGnodeList.get(0);
                        // 网关下的分支线
                        List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
                        actYwGnodeNextLine = getNextGnodeByGate(gatelineList, apply);
                    }
                }
            }
        } else if (GnodeType.GT_ROOT_TASK.getId().equals(actYwGnode.getType())) {
            // 如果节点为根流程
            List<ActYwGnode> actYwGnodeList = actYwGnodeService.getNextNextNode(actYwGnode);
            ActYwGnode gateGnode = actYwGnodeList.get(0);
            // 网关下的分支线
            List<ActYwGnode> gatelineList = actYwGnodeService.getNextNode(gateGnode);
            actYwGnodeNextLine = getNextGnodeByGate(gatelineList, apply);
            // actYwGnodeNext=getNextGnodeByGate(actYwGnodeList,proModel);
        }
        if (actYwGnodeNextLine != null) {
            ActYwGnode nextGnode = getNextGnodeByline(actYwGnodeNextLine);
            return nextGnode;
        }
        return null;
    }

    /**
     * 根据线得到下一个节点.
     * @param gnodeNextLine 线节点
     * @return ActYwGnode
     */
    public ActYwGnode getNextGnodeByline(ActYwGnode gnodeNextLine) {
        List<ActYwGnode> actYwGnodeNextGnodeList = actYwGnodeService.getNextNode(gnodeNextLine);
        if (actYwGnodeNextGnodeList == null) {
            return null;
        }
        // 通过线得到下一个节点
        ActYwGnode actYwGnodeNext = actYwGnodeService.getByg(actYwGnodeNextGnodeList.get(0).getId());
        // 下一个节点为子流程
        if (GnodeType.GT_PROCESS.getId().equals(actYwGnodeNext.getType())) {
            // 得到子流程中第一个任务节点userTask
            ActYwGnode toSubNextGnode = getFirstGnode(actYwGnodeNext);
            toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
            if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                return toSubNextGnode;
            }
            // 下一个节点为子节点结束节点
        } else if (GnodeType.GT_PROCESS_END.getId().equals(actYwGnodeNext.getType())) {
            // 直接结束节点
            StenType stenType = StenType.getByKey(actYwGnodeNext.getNode().getNodeKey());
            if ((StenType.ST_END_EVENT_TERMINATE).equals(stenType)) {
                return actYwGnodeNext;
            }
            // 当前节点父节点
            ActYwGnode subActYwGnode = actYwGnodeService.get(actYwGnodeNext.getParentId());
            if (subActYwGnode != null) {
                List<ActYwGnode> subActYwGnodeList = actYwGnodeService.getNextNextNode(subActYwGnode);
                ActYwGnode gateGnode = subActYwGnodeList.get(0);
                if (GnodeType.GT_ROOT_END.getId().equals(gateGnode.getType())) {
                    return actYwGnodeNext;
                } else if (GnodeType.GT_PROCESS.getId().equals(gateGnode.getType())) {
                    // 得到子流程中第一个任务节点userTask
                    ActYwGnode toSubNextGnode = getFirstGnode(gateGnode);
                    toSubNextGnode = actYwGnodeService.getByg(toSubNextGnode.getId());
                    if (toSubNextGnode.getGroles() != null && toSubNextGnode.getGroles().get(0) != null) {
                        return toSubNextGnode;
                    }
                } else {
                    gateGnode = actYwGnodeService.getByg(gateGnode.getId());
                    return gateGnode;
                }
            }
        } else if (GnodeType.GT_ROOT_END.getId().equals(actYwGnodeNext.getType())) {
            return actYwGnodeNext;
        } else {
            // 下一个节点为userTask
            if (actYwGnodeNext.getGroles() != null && actYwGnodeNext.getGroles().get(0) != null) {
                return actYwGnodeNext;
            }
        }
        return null;
    }

    /**
     * 根据判断条件和填入的条件判断出走的网关那个分支.
     * @param actYwGnodeGate 网关节点
     * @param apply 申请
     * @return ActYwGnode
     */
    public ActYwGnode getNextGnodeByGate(List<ActYwGnode> actYwGnodeGate, IApply apply) {
        ActYwGnode actYwGnodeNext = null;
        for (ActYwGnode gateNextLineGnode : actYwGnodeGate) {
            ActYwGnode gateNextGnode = actYwGnodeService.getByg(gateNextLineGnode.getId());
            List<ActYwGstatus> actywGstatuss = gateNextGnode.getGstatuss();
            if (actywGstatuss != null && actywGstatuss.size() > 0) {
                for (int i = 0; i < actywGstatuss.size(); i++) {
                    ActYwGstatus actYwGstatus = gateNextGnode.getGstatuss().get(i);
                    ActYwStatus actYwStatus = actYwGstatus.getStatus();
                    // 打分
                    if ((RegType.RT_GE.getId()).equals(actYwStatus.getRegType())) {
                        String alias = actYwStatus.getAlias();
                        String startNum = alias.substring(0, StringUtil.lastIndexOf(alias, StringUtil.LINE_M));
                        String endNum = alias.substring(StringUtil.lastIndexOf(alias, StringUtil.LINE_M) + 1);
                        int sNum = Integer.parseInt(startNum);
                        int eNum = Integer.parseInt(endNum);
                        if (apply.iasup().iscore() != null) {
                            // 分数为100
                            if (endNum.equals(apply.iasup().iscore()) && SCORE_100.equals(endNum)) {
                                actYwGnodeNext = gateNextGnode;
                                break;
                            }
                            if (eNum > apply.iasup().iscore() && apply.iasup().iscore() >= sNum) {
                                actYwGnodeNext = gateNextGnode;
                                break;
                            }
                        }
                    } else {
                        if (apply.iasup().igrade() != null) {
                            if ((apply.iasup().igrade()).equals(actYwStatus.getStatus())) {
                                actYwGnodeNext = gateNextGnode;
                                break;
                            }
                        }
                    }
                }
            }
            if (actYwGnodeNext != null) {
                break;
            }
        }
        return actYwGnodeNext;
    }

   /**
    * 判断审核结果走那个节点状态.
    * @param apply
    * @param actYwStatuss List
    * @return
    */
    public IAstatus getGateActYwStatus(IApply apply, List<IAstatus> actYwStatuss) {
        IAstatus actYwStatusNext = null;
        for (IAstatus actYwStatus : actYwStatuss) {
            // 判断网关类型为评分还是其他审核 2为评分类型
            if ((RegType.RT_GE.getId()).equals(actYwStatus.getIregType())) {
                String alias = actYwStatus.getIalias();
                String startNum = alias.substring(0, StringUtil.lastIndexOf(alias, "-"));
                String endNum = alias.substring(StringUtil.lastIndexOf(alias, "-") + 1);
                Double sNum = Double.parseDouble(startNum);
                Double eNum = Double.parseDouble(endNum);
                if (apply.iasup().iscore() != null) {
                    // 分数为100
                    if (endNum.equals(apply.iasup().iscore()) && SCORE_100.equals(endNum)) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                    if (eNum > apply.iasup().iscore() && apply.iasup().iscore() >= sNum) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                }
            } else {
                if (apply.iasup().igrade() != null) {
                    if ((apply.iasup().igrade()).equals(actYwStatus.getIstatus())) {
                        actYwStatusNext = actYwStatus;
                        break;
                    }
                }
            }
        }
        return actYwStatusNext;
    }

    /**
     * 下一个节点需要的审核人变量设置.
     * @param gnode 节点
     * @param nextGnodeRoleId 下一个节点角色ID
     * @param vars 变量
     * @param apply
     * @return Map
     */
    public Map<String, Object> addRoleIn(ActYwGnode gnode, String nextGnodeRoleId, Map<String, Object> vars, IApply apply) {
        if (StringUtil.isNotEmpty(nextGnodeRoleId)) {
            List<String> roles = new ArrayList<String>();
            if (gnode.getIsAssign() != null && gnode.getIsAssign()) {
                roles.clear();
                roles.add("assignUser");
                //删除旧的指派记录
                ActYwGassign actYwGassign = new ActYwGassign();
                actYwGassign.setPromodelId(apply.getIid());
                Boolean isHasAssign= actYwGassignService.isHasAssign(actYwGassign);
                //已经被指派过
                if(isHasAssign){
                    //删除旧的指派记录
                    actYwGassignService.deleteByAssign(actYwGassign);
                }
            } else {
                roles = getUsersByRoles(apply, nextGnodeRoleId);
            }
            if (gnode != null && GnodeTaskType.GTT_NONE.getKey().equals(gnode.getTaskType())) {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId, roles);
            } else {
                vars.put(ActYwTool.FLOW_ROLE_ID_PREFIX + nextGnodeRoleId + "s", roles);
            }
        }
        return vars;
    }

    /**
     * 根据角色值得到用户.
     * @param apply 申请
     * @param roleIds 角色Ids
     * @return List
     */
    public List<String> getUsersByRoles(IApply apply, String roleIds) {
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        String[] roleList = roleIds.split(StringUtil.LINE_D);
        for (int i = 0; i < roleList.length; i++) {
            List<String> roles = new ArrayList<String>();
            String roleId=roleList[i];
//            if(SysIds.EXPERT_COLLEGE_EXPERT.getId().equals(roleId)
//                ||SysIds.EXPERT_SCHOOL_EXPERT.getId().equals(roleId)
//                ||SysIds.EXPERT_OUTSCHOOL_EXPERT.getId().equals(roleId)){
//                roleId=SysIds.EXPERT_ROLE.getId();
//            }
            Role role = systemService.getRole(roleId);
            // 启动节点
            String roleName = role.getName();
            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), apply.iauserId());
            } else {
                roles = userService.findListByRoleId(role.getId());
            }
            if(roles==null){
                throw new GroupErrorException("该审核节点角色已经不存在");
            }
            // 学生角色id 清除其他人id 只保留申报学生id
            if (role.getId().equals(coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId())||role.getId().equals(coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId())) {
                roles.clear();
                roles.add(userService.findUserById(apply.iauserId()).getId());
            }
            roleAll.addAll(roles);
        }
        return StringUtil.removeDup(roleAll);
    }

    /**
     * 根据用户获取角色.
     * @param uid 用户ID
     * @param roleList 角色列表
     * @return List
     */
    public List<String> getUsersByRoleList(String uid, List<Role> roleList) {
        List<String> roleAll = new ArrayList<String>();
        //多角色配置人员
        for (int i = 0; i < roleList.size(); i++) {
            List<String> roles = new ArrayList<String>();
            Role role = roleList.get(i);
            // 启动节点
            String roleName = role.getName();
            if (roleName.contains(SysIds.ISCOLLEGE.getRemark()) || roleName.contains(SysIds.ISMS.getRemark())) {
                roles = userService.findListByRoleIdAndOffice(role.getId(), uid);
            } else {
                roles = userService.findListByRoleId(role.getId());
            }
            // 学生角色id 清除其他人id 只保留申报学生id
            if ((coreService.getByRtype(CoreSval.Rtype.STUDENT.getKey()).getId()).equals(role.getId()) || (coreService.getByRtype(CoreSval.Rtype.TEACHER.getKey()).getId()).equals(role.getId())) {
                roles.clear();
                roles.add(userService.findUserById(uid).getId());
            }
            roleAll.addAll(roles);
        }
        return StringUtil.removeDup(roleAll);
    }


//    @Transactional(readOnly = false)
//    public ActYwGclazzData setListenGrade(ActYwGnode actYwGnode, String proId, String grade, String type) {
//        ActYwGclazzData pgclazzData = new ActYwGclazzData();
//        pgclazzData.setGclazz(actYwGnode.getGclazzs().get(0));
//        pgclazzData.setType(ClazzThemeListener.CMR_A.getKey());
//        pgclazzData.setApplyId(proId);
//        pgclazzData.setDatas(grade);
//        return actYwGclazzDataService.putData(proId, type, pgclazzData);
//    }

    /**
     * 对流程审核人发送提醒信息.
     * @param gnode 审核节点
     * @param vars 流程变量
     * @param nextGnodeRoleId 节点角色
     * @param apply 申请
     */
    @Transactional(readOnly = false)
    public void sendMsgToNextGnodeUser(ActYwGnode gnode, Map<String, Object> vars, String nextGnodeRoleId, IApply apply) {
        List<String> roles = new ArrayList<String>();
        if (gnode.getIsAssign() != null && gnode.getIsAssign()) {
            Role role = systemService.getRole(CoreIds.NSC_SYS_ROLE_ADMIN.getId());
            roles = userService.findListByRoleId(role.getId());
            sendOaNotifyByTypeAndUser(UserUtils.getUser(), roles, "项目指派", apply.getIname() + "项目请指派专家审核。", OaNotify.Type_Enum.TYPE18.getValue(), apply.getIid());
        } else {
            roles = getUsersByRoles(apply, nextGnodeRoleId);
            if (StringUtil.checkNotEmpty(roles)) {
                if (gnode.getGforms() != null && gnode.getGforms().get(0) != null && gnode.getGforms().get(0).getForm() != null && gnode.getGforms().get(0).getForm().getClientType() != null && FormClientType.FST_FRONT.getKey().equals(gnode.getGforms().get(0).getForm().getClientType())) {
                    sendOaNotifyByTypeAndUser(UserUtils.getUser(), roles, "任务审核", apply.getIname() + "项目需要你去继续申报", OaNotify.Type_Enum.TYPE18.getValue(), apply.getIid());
                } else {
                    sendOaNotifyByTypeAndUser(UserUtils.getUser(), roles, "任务审核", apply.getIname() + "项目需要你去审核", OaNotify.Type_Enum.TYPE14.getValue(), apply.getIid());
                }
            }
        }
    }

    /**
     * .
     * @param apply
     * @param actYwGnode
     * @param gnodeVesion
     */
    @Transactional(readOnly = false)
    public void saveActYwAuditInfo(IApply apply, ActYwGnode actYwGnode, String gnodeVesion) {
        // 判断审核结果
        if (apply.iasup().igrade() != null || apply.iasup().iscore() != null) {
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
            actYwAuditInfo.setPromodelId(apply.getIid());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            actYwAuditInfo.setGnodeVesion(gnodeVesion);
            if (StringUtils.isNotBlank(apply.iasup().igrade())) {
                actYwAuditInfo.setGrade(apply.iasup().igrade());
            } else {
                actYwAuditInfo.setScore(apply.iasup().iscore());
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfo.setSuggest(apply.iasup().iremarks());
            ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
            if (lastAuditActYwAuditInfo != null) {
                actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
            }
            actYwAuditInfoService.save(actYwAuditInfo);
        }
    }

    @Transactional(readOnly = false)
    public void saveActYwAuditInfo(IApply apply, ActYwGnode actYwGnode) {
        // 判断审核结果
        if (apply.iasup().igrade() != null || apply.iasup().iscore() != null) {
            ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
            actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
            actYwAuditInfo.setPromodelId(apply.getIid());
            actYwAuditInfo.setGnodeId(actYwGnode.getId());
            if (StringUtils.isNotBlank(apply.iasup().igrade())) {
                actYwAuditInfo.setGrade(apply.iasup().igrade());
            } else {
                actYwAuditInfo.setScore(Double.parseDouble(apply.iasup().igrade()));
            }
            actYwAuditInfo.setAuditName(actYwGnode.getName());
            actYwAuditInfo.setSuggest(apply.iasup().iremarks());
            ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
            if (lastAuditActYwAuditInfo != null) {
                actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
            }
            actYwAuditInfoService.save(actYwAuditInfo);
        }
    }

    /**
     * .
     * @param apply
     * @param actYwGnode
     * @param gnodeVesion
     */
    @Transactional(readOnly = false)
    public void saveFrontActYwAuditInfo(IApply apply, ActYwGnode actYwGnode, String gnodeVesion) {
        // 判断审核结果
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
        actYwAuditInfo.setPromodelId(apply.getIid());
        actYwAuditInfo.setGnodeId(actYwGnode.getId());
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        actYwAuditInfo.setGnodeVesion(gnodeVesion);
        ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
        if (lastAuditActYwAuditInfo != null) {
            actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
        }
        actYwAuditInfoService.save(actYwAuditInfo);
    }

    @Transactional(readOnly = false)
    public void saveFrontActYwAuditInfo(IApply apply, ActYwGnode actYwGnode) {
        // 判断审核结果
        ActYwAuditInfo actYwAuditInfo = new ActYwAuditInfo();
        actYwAuditInfo.setAuditId(UserUtils.getUser().getId());
        actYwAuditInfo.setPromodelId(apply.getIid());
        actYwAuditInfo.setGnodeId(actYwGnode.getId());
        actYwAuditInfo.setAuditName(actYwGnode.getName());
        ActYwAuditInfo lastAuditActYwAuditInfo = actYwAuditInfoService.getLastAudit(actYwAuditInfo);
        if (lastAuditActYwAuditInfo != null) {
            actYwAuditInfoService.updateIsBack(lastAuditActYwAuditInfo.getId());
        }
        actYwAuditInfoService.save(actYwAuditInfo);
    }

    /**
     * 得到流程中 当前角色的所有审核list.
     * @param gnodeId 流程节点
     * @param groupId 流程
     * @return List
     */
    public List<ActYwGnode> getSubGnodeList(String gnodeId, String groupId) {
        return proActTaskService.getSubGnodeList(gnodeId, groupId);
    }

    /**
     * 审核列表记录的ids.
     * @param act
     * @param gnodeIds 节点ids
     * @return
     */
    public List<String> recordIds(Act act, List<String> gnodeIds, String actYwId) {
        return actTaskService.recordIds(act, gnodeIds, actYwId);
    }

    public ActYwAuditInfo getGnodeByNextGnode(ActYwAuditInfo actYwAuditInfoIn) {
        return actYwAuditInfoService.getGnodeByNextGnode(actYwAuditInfoIn);
    }

    public ActYwAuditInfo getLastAudit(ActYwAuditInfo actYwAuditInfoIn) {
        return actYwAuditInfoService.getLastAudit(actYwAuditInfoIn);
    }

    /**
     * 流程审核发送消息通知.
     * @param auser 审核人
     * @param users
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @param sid 通知业务申请ID
     * @return
     */
    @Transactional(readOnly = false)
    public int sendOaNotifyByTypeAndUser(User auser, List<String> users, String title, String content, String type, String appId) {
        return oaNotifyService.sendOaNotifyByTypeAndUser(auser, users, title, content, type, appId);
    }

    /**
     * 流程审核发送消息通知.
     * @param auser 审核人
     * @param recUser 接收人
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @param sid 通知业务申请ID
     * @return
     * @return
     */
    @Transactional(readOnly = false)
    public int sendOaNotifyByType(User auser, User recUser, String title, String content, String type, String appId) {
        return oaNotifyService.sendOaNotifyByType(auser, recUser, title, content, type, appId);
    }

    public ActYwGnode getStartNextGnode(IActYw actYw) {
        return actTaskService.getStartNextGnode(ActYw.getPkey(actYw));
    }
}
