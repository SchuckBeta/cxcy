/**
 *
 */
package com.oseasy.pro.common.service;

import com.oseasy.act.modules.actyw.dao.ActYwDao;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwYear;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.act.modules.actyw.service.ActYwYearService;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowPcategoryType;
import com.oseasy.act.modules.pro.entity.ProProject;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.jobs.pro.ActYwUtils;
import com.oseasy.pro.modules.gcontest.dao.GContestDao;
import com.oseasy.pro.modules.gcontest.entity.GContest;
import com.oseasy.pro.modules.gcontest.enums.GContestStatusEnum;
import com.oseasy.pro.modules.project.dao.ProjectDeclareDao;
import com.oseasy.pro.modules.project.entity.ProjectDeclare;
import com.oseasy.pro.modules.project.enums.ProjectStatusEnum;
import com.oseasy.pro.modules.promodel.dao.ProModelDao;
import com.oseasy.pro.modules.promodel.entity.ProModel;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.entity.StudentExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.ConfRange;
import com.oseasy.sys.modules.sys.vo.PersonNumConf;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.dao.TeamUserHistoryDao;
import com.oseasy.sys.modules.team.dao.TeamUserRelationDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamUserHistory;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamUserChangeService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service公共处理逻辑
 *
 * @version 2014-05-16
 */

@Service
@Transactional(readOnly = true)
public class CommonService {
    public static final String OPEN_TIME_LIMIT = CoreSval.getConfig("openTimeLimit");
    private final static String Home = "/f";
    private final static String ProjectList = "/f/project/projectDeclare/list";
    private final static String GcontestList = "/f/gcontest/gContest/list";
    private final static String Jsbtns = "btns";
    private final static String DcUrl = "/f/project/projectDeclare";
    private final static String ProUrl = "/f/promodel/proModel";
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TeamUserHistoryDao teamUserHistoryDao;
    @Autowired
    private TeamUserRelationDao teamUserRelationDao;
    @Autowired
    private TeamUserChangeService teamUserChangeService;
    @Autowired
    private ActYwDao actYwDao;
    @Autowired
    private ProjectDeclareDao projectDeclareDao;
    @Autowired
    private GContestDao gContestDao;
    @Autowired
    private ProModelDao proModelDao;
    @Autowired
    private BackTeacherExpansionDao backTeacherExpansionDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private ActYwYearService actYwYearService;
    @Autowired
    private ActYwService actYwService;

    private boolean canApply(String actywId, String year) {
        if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(year)) {
            return false;
        }
//        ActYwYear ayy = actYwYearService.getByActywIdAndYear(actywId, year);
        ActYwYear ayy = Optional.ofNullable(ActYwUtils.findActYwYear(actywId, TenantConfig.getCacheTenant())).orElse(actYwYearService.getByActywIdAndYear(actywId, year));
        if (ayy == null) {
            return false;
        }
        if ("N".equals(OPEN_TIME_LIMIT)) {
            return true;
        }
        Date now = new Date();
        if (ayy.getNodeStartDate() != null && ayy.getNodeEndDate() != null
                && now.after(ayy.getNodeStartDate()) && now.before(ayy.getNodeEndDate())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否可申报
     * @param actywId  actywId
     * @param year  year
     * @param ayy ActYwYear
     * @return boolean
     */
    private boolean canApply(String actywId, String year,ActYwYear ayy) {
        if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(year)) {
            return false;
        }
        if (ayy == null) {
            return false;
        }
        if ("N".equals(OPEN_TIME_LIMIT)) {
            return true;
        }
        Date now = new Date();
        if (ayy.getNodeStartDate() != null && ayy.getNodeEndDate() != null
                && now.after(ayy.getNodeStartDate()) && now.before(ayy.getNodeEndDate())) {
            return true;
        } else {
            return false;
        }
    }

    //不可申报时获取需要提示的年份及申报时间
    public ActYwYear getMsgActYwYear(String actywId) {
        ActYwYear ret = null;
        List<ActYwYear> list = actYwYearService.findListByActywId(actywId);
        if (list == null || list.size() == 0) {
            return ret;
        }
        Date now = new Date();
        ActYwYear ayy = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            ayy = list.get(i);
            if (ayy.getNodeStartDate() == null || ayy.getNodeEndDate() == null) {
                continue;
            }
            if (now.before(ayy.getNodeStartDate())) {
                ret = ayy;
            } else if (now.after(ayy.getNodeEndDate())) {
                if (ret == null) {
                    ret = ayy;
                }
                break;
            } else {
                break;
            }
        }
        return ret;
    }

    //获取当前可申报的年份及申报时间
    public ActYwYear getApplyActYwYear(String actywId) {
        List<ActYwYear> list = actYwYearService.findListByActywId(actywId);
        if (list == null || list.size() == 0) {
            return null;
        }
        Date now = new Date();
        for (ActYwYear ayy : list) {
            if (ayy.getNodeStartDate() != null && ayy.getNodeEndDate() != null
                    && now.after(ayy.getNodeStartDate()) && now.before(ayy.getNodeEndDate())) {
                return ayy;
            }
        }
        return null;
    }

    //获取当前可申报的年份
    public String getApplyYear(String actywId) {
        List<ActYwYear> list = actYwYearService.findListByActywId(actywId);
        if (list == null || list.size() == 0) {
            return null;
        }
        Date now = new Date();
        for (ActYwYear ayy : list) {
            if (ayy.getNodeStartDate() != null && ayy.getNodeEndDate() != null
                    && now.after(ayy.getNodeStartDate()) && now.before(ayy.getNodeEndDate())) {
                return ayy.getYear();
            }
        }
        return null;
    }

    //true 表示有变更
    public boolean checkTeamHaveChange(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String teamId) {
        Team team = teamDao.get(teamId);
        if (team == null) {
            return false;
        }
        String fzr = null;
        for (TeamUserHistory t : stus) {
            if (Const.NO.equals(t.getUserzz())) {
                fzr = t.getUserId();
                break;
            }
        }
        if (!team.getSponsor().equals(fzr)) {
            return true;
        }
        List<TeamUserRelation> old = teamUserRelationDao.getByTeamId(teamId);
        if (old == null || old.size() == 0) {
            return true;
        }
        int stnum = stus.size();
        int teanum = 0;
        if (teas != null) {
            teanum = teas.size();
        }
        if (old.size() != stnum + teanum) {
            return true;
        }
        Map<String, String> map = new HashMap<String, String>();
        for (TeamUserRelation t : old) {
            map.put(t.getUser().getId(), t.getUser().getId());
        }
        for (TeamUserHistory t : stus) {
            if (map.get(t.getUserId()) == null) {
                return true;
            }
        }
        if (teas != null && teas.size() > 0) {
            for (TeamUserHistory t : teas) {
                if (map.get(t.getUserId()) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = false)
    public void disposeTeamUserHistoryForModify(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String actywId, String teamId, String proId) {
        if (checkTeamHaveChange(stus, teas, teamId)) {
            Date now = new Date();
            User cuser = UserUtils.getUser();
            if (stus == null || stus.size() == 0) {
                throw new RuntimeException("TeamUserHistory不能为空");
            }
            if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(teamId) || StringUtil.isEmpty(proId)) {
                throw new RuntimeException("参数不能为空");
            }
            ActYw actYw = actYwDao.get(actywId);
            if (actYw == null) {
                throw new RuntimeException("找不到ActYw");
            }
            ProProject proProject = actYw.getProProject();
            if (proProject == null) {
                throw new RuntimeException("找不到ProProject");
            }
            if (StringUtil.isEmpty(proProject.getProType()) || StringUtil.isEmpty(proProject.getType())) {
                throw new RuntimeException("ProProject的类型不能为空");
            }

            String proType = proProject.getProType();
            String proSubType = proProject.getType();
            String finish = null;
            ProjectDeclare p = projectDeclareDao.get(proId);
            GContest g = gContestDao.get(proId);
            ProModel pm = proModelDao.get(proId);
            String year = null;
            String dutyId = null;
            if (p != null) {
                year = p.getYear();
                dutyId=p.getLeader();
                if (Const.NO.equals(p.getStatus())) {
                    finish = TeamUserHistory.finish_val2;
                } else if (ProjectStatusEnum.S8.getValue().equals(p.getStatus()) || ProjectStatusEnum.S9.getValue().equals(p.getStatus())) {
                    finish = TeamUserHistory.finish_val1;
                } else {
                    finish = TeamUserHistory.finish_val0;
                }
            } else if (g != null) {
                year = g.getYear();
                dutyId=g.getDeclareId();
                if (Const.NO.equals(g.getAuditState())) {
                    finish = TeamUserHistory.finish_val2;
                } else if (GContestStatusEnum.S7.getValue().equals(g.getAuditState())) {
                    finish = TeamUserHistory.finish_val1;
                } else {
                    finish = TeamUserHistory.finish_val0;
                }
            } else if (pm != null) {
                year = pm.getYear();
                dutyId=pm.getDeclareId();
                if (Const.YES.equals(pm.getState())) {
                    finish = TeamUserHistory.finish_val1;
                } else if (StringUtil.isNotEmpty(pm.getProcInsId())) {
                    finish = TeamUserHistory.finish_val0;
                } else {
                    finish = TeamUserHistory.finish_val2;
                }
            }

            if (TeamUserHistory.finish_val1.equals(finish)) {
                //结束的,只需修改his表信息
                String fzr = null;
                for (TeamUserHistory t : stus) {
                    if ("0".equals(t.getUserzz())) {
                        fzr = t.getUserId();
                        break;
                    }
                }
                teamUserChangeService.changeBySave(stus,teas,teamId,proId,dutyId);
                if (ProSval.PRO_TYPE_PROJECT.equals(proType)) {
                    if ("1".equals(proSubType)) {//大创
                        projectDeclareDao.modifyLeaderAndTeam(fzr, null, proId);
                    } else {
                        proModelDao.modifyLeaderAndTeam(fzr, null, proId);
                    }
                } else if (ProSval.PRO_TYPE_GCONTEST.equals(proType)) {
                    if ("1".equals(proSubType)) {//互联网+
                        gContestDao.modifyLeaderAndTeam(fzr, null, proId);
                    } else {
                        proModelDao.modifyLeaderAndTeam(fzr, null, proId);
                    }
                }
                for (TeamUserHistory tuh : stus) {
                    tuh.setId(IdGen.uuid());
                    tuh.setUser(new User(tuh.getUserId()));
                    tuh.setTeamId(teamId);
                    tuh.setCreateBy(cuser);
                    tuh.setUpdateBy(cuser);
                    tuh.setCreateDate(now);
                    tuh.setUpdateDate(now);
                    tuh.setProId(proId);
                    tuh.setProType(proType);
                    tuh.setProSubType(proSubType);
                    tuh.setUserType("1");
                    tuh.setFinish(finish);
                    tuh.setDelFlag(Const.NO);
                    tuh.setWeightVal(tuh.getWeightVal());
                    tuh.setYear(year);
                }
                if (teas != null && teas.size() > 0) {
                    for (TeamUserHistory tuh : teas) {
                        tuh.setId(IdGen.uuid());
                        tuh.setUser(new User(tuh.getUserId()));
                        tuh.setTeamId(teamId);
                        tuh.setCreateBy(cuser);
                        tuh.setUpdateBy(cuser);
                        tuh.setCreateDate(now);
                        tuh.setUpdateDate(now);
                        tuh.setProId(proId);
                        tuh.setProType(proType);
                        tuh.setProSubType(proSubType);
                        tuh.setUserType("2");
                        tuh.setFinish(finish);
                        tuh.setDelFlag(Const.NO);
                        tuh.setYear(year);
                    }
                    stus.addAll(teas);
                }
                teamUserHistoryDao.deleteByProId(proId);
                teamUserHistoryDao.insertAll(stus);
            } else if (TeamUserHistory.finish_val0.equals(finish) || TeamUserHistory.finish_val2.equals(finish)) {
                //未结束的
                Integer c = teamUserHistoryDao.getDoingCountByTeamId(teamId);
                if (c != null && c >= 2) {
                    //有两个以上正在进行的项目、大赛,需要新建一个team
                    String fzr = null;
                    for (TeamUserHistory t : stus) {
                        if ("0".equals(t.getUserzz())) {
                            fzr = t.getUserId();
                            break;
                        }
                    }
                    Team t = teamDao.get(teamId);
                    t.setId(IdGen.uuid());
                    t.setSponsor(fzr);
                    t.setMemberNum(stus.size());
                    t.setUpdateBy(cuser);
                    t.setCreateBy(cuser);
                    t.setCreateDate(now);
                    t.setUpdateDate(now);
                    getTeamTeacherNum(t, teas);//算出导师人数
                    teamDao.insert(t);
                    //正在进行的 创建了一个新团队 需要保存变更后的团队id
                    teamUserChangeService.changeBySaveInState(stus,teas,teamId,t.getId(),proId,dutyId);
                    for (TeamUserHistory tuh : stus) {
                        tuh.setId(IdGen.uuid());
                        tuh.setUser(new User(tuh.getUserId()));
                        tuh.setTeamId(t.getId());
                        tuh.setCreateBy(cuser);
                        tuh.setUpdateBy(cuser);
                        tuh.setCreateDate(now);
                        tuh.setUpdateDate(now);
                        tuh.setProId(proId);
                        tuh.setProType(proType);
                        tuh.setProSubType(proSubType);
                        tuh.setUserType("1");
                        tuh.setFinish(finish);
                        tuh.setDelFlag(Const.NO);
                        tuh.setWeightVal(tuh.getWeightVal());
                        tuh.setYear(year);
                    }
                    if (teas != null && teas.size() > 0) {
                        for (TeamUserHistory tuh : teas) {
                            tuh.setId(IdGen.uuid());
                            tuh.setUser(new User(tuh.getUserId()));
                            tuh.setTeamId(t.getId());
                            tuh.setCreateBy(cuser);
                            tuh.setUpdateBy(cuser);
                            tuh.setCreateDate(now);
                            tuh.setUpdateDate(now);
                            tuh.setProId(proId);
                            tuh.setProType(proType);
                            tuh.setProSubType(proSubType);
                            tuh.setUserType("2");
                            tuh.setFinish(finish);
                            tuh.setDelFlag(Const.NO);
                            tuh.setYear(year);
                        }
                        stus.addAll(teas);
                    }
                    if (ProSval.PRO_TYPE_PROJECT.equals(proType)) {
                        if ("1".equals(proSubType)) {//大创
                            projectDeclareDao.modifyLeaderAndTeam(fzr, t.getId(), proId);
                        } else {
                            proModelDao.modifyLeaderAndTeam(fzr, t.getId(), proId);
                        }
                    } else if (ProSval.PRO_TYPE_GCONTEST.equals(proType)) {
                        if ("1".equals(proSubType)) {//互联网+
                            gContestDao.modifyLeaderAndTeam(fzr, t.getId(), proId);
                        } else {
                            proModelDao.modifyLeaderAndTeam(fzr, t.getId(), proId);
                        }
                    }
                    teamUserRelationDao.insertAll(stus);
                    teamUserHistoryDao.deleteByProId(proId);
                    teamUserHistoryDao.insertAll(stus);
                } else {//直接同步team信息
                    String fzr = null;
                    for (TeamUserHistory t : stus) {
                        if ("0".equals(t.getUserzz())) {
                            fzr = t.getUserId();
                            break;
                        }
                    }
                    if (ProSval.PRO_TYPE_PROJECT.equals(proType)) {
                        if ("1".equals(proSubType)) {//大创
                            projectDeclareDao.modifyLeaderAndTeam(fzr, null, proId);
                        } else {
                            proModelDao.modifyLeaderAndTeam(fzr, null, proId);
                        }
                    } else if (ProSval.PRO_TYPE_GCONTEST.equals(proType)) {
                        if ("1".equals(proSubType)) {//互联网+
                            gContestDao.modifyLeaderAndTeam(fzr, null, proId);
                        } else {
                            proModelDao.modifyLeaderAndTeam(fzr, null, proId);
                        }
                    }
                    teamUserChangeService.changeBySave(stus,teas,teamId,proId,dutyId);
                    Team t = teamDao.get(teamId);
                    t.setSponsor(fzr);
                    t.setMemberNum(stus.size());
                    getTeamTeacherNum(t, teas);//算出导师人数
                    teamDao.updateAllInfo(t);
                    for (TeamUserHistory tuh : stus) {
                        tuh.setId(IdGen.uuid());
                        tuh.setUser(new User(tuh.getUserId()));
                        tuh.setTeamId(teamId);
                        tuh.setCreateBy(cuser);
                        tuh.setUpdateBy(cuser);
                        tuh.setCreateDate(now);
                        tuh.setUpdateDate(now);
                        tuh.setProId(proId);
                        tuh.setProType(proType);
                        tuh.setProSubType(proSubType);
                        tuh.setUserType("1");
                        tuh.setFinish(finish);
                        tuh.setDelFlag(Const.NO);
                        tuh.setWeightVal(tuh.getWeightVal());
                        tuh.setYear(year);
                    }
                    if (teas != null && teas.size() > 0) {
                        for (TeamUserHistory tuh : teas) {
                            tuh.setId(IdGen.uuid());
                            tuh.setUser(new User(tuh.getUserId()));
                            tuh.setTeamId(teamId);
                            tuh.setCreateBy(cuser);
                            tuh.setUpdateBy(cuser);
                            tuh.setCreateDate(now);
                            tuh.setUpdateDate(now);
                            tuh.setProId(proId);
                            tuh.setProType(proType);
                            tuh.setProSubType(proSubType);
                            tuh.setUserType("2");
                            tuh.setFinish(finish);
                            tuh.setDelFlag(Const.NO);
                            tuh.setYear(year);
                        }
                        stus.addAll(teas);
                    }
                    teamUserRelationDao.deleteByTeamId(teamId);
                    teamUserRelationDao.insertAll(stus);
                    teamUserHistoryDao.deleteByProId(proId);
                    teamUserHistoryDao.insertAll(stus);
                }

            }
        }
    }

    public JSONObject checkProjectTeamForModify(List<TeamUserHistory> tuhs_stu, List<TeamUserHistory> teas, String proid, String actywId, String lowType, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (StringUtil.isEmpty(lowType)) {
            js.put("msg", "请选择项目类别");
            return js;
        }
        if (tuhs_stu == null || tuhs_stu.size() == 0) {
            js.put("msg", "请添加团队成员");
            return js;
        }
        if (StringUtil.isEmpty(year)) {
            js.put("msg", "无项目申报年份,请联系管理员");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwDao.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }
        boolean haveFzr = false;
        for (TeamUserHistory t : tuhs_stu) {
            if ("0".equals(t.getUserzz())) {
                haveFzr = true;
                break;
            }
        }
        if (!haveFzr) {
            js.put("msg", "请选择负责人");
            return js;
        }
        String subType = actYw.getProProject().getType();//项目分类
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            Team teamNums = new Team();
            teamNums.setMemberNum(tuhs_stu.size());
            getTeamTeacherNum(teamNums, teas);//算出导师人数
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, proid, proType) > 0) {
                    js.put("msg", "有团队成员已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, proid, proType, subType) > 0) {
                    js.put("msg", "有团队成员已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, proid, proType, subType, year) > 0) {
                    js.put("msg", "有团队成员已申报该类型的项目");
                    return js;
                }
            }
            if ("1".equals(scv.getTeamConf().getIntramuralValiaOnOff())) {
                ConfRange cr = scv.getTeamConf().getIntramuralValia();
                int min = Integer.valueOf(cr.getMin());
                int max = Integer.valueOf(cr.getMax());
                if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                    if (min == max) {
                        js.put("msg", "校内导师人数为" + min + "人!");
                    } else {
                        js.put("msg", "校内导师人数为" + min + "-" + max + "人!");
                    }
                    return js;
                }
            }
            String maxms = scv.getTeamConf().getMaxMembers();
            if (StringUtil.isNotEmpty(maxms) && teamNums.getMemberNum() > Integer.valueOf(maxms)) {
                js.put("msg", "团队成员人数不能超过" + maxms + "人!");
                return js;
            }
            PersonNumConf pnc = SysConfigUtil.getProPersonNumConf(scv, subType, lowType);
            if (pnc != null) {
                if ("1".equals(pnc.getTeamNumOnOff())) {//团队人数范围
                    ConfRange cr = pnc.getTeamNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getMemberNum() < min || teamNums.getMemberNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getSchoolTeacherNumOnOff())) {//校园导师人数范围
                    ConfRange cr = pnc.getSchoolTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getEnTeacherNumOnOff())) {//企业导师人数范围
                    ConfRange cr = pnc.getEnTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getEnterpriseTeacherNum() < min || teamNums.getEnterpriseTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
            }
        }
        js.put("ret", 1);
        return js;
    }

    public JSONObject checkGcontestTeamForModify(List<TeamUserHistory> tuhs_stu, List<TeamUserHistory> teas, String proid, String actywId, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_GCONTEST;//双创大赛
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (tuhs_stu == null || tuhs_stu.size() == 0) {
            js.put("msg", "请添加团队成员");
            return js;
        }
        if (StringUtil.isEmpty(year)) {
            js.put("msg", "无大赛申报年份,请联系管理员");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无大赛申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwDao.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "大赛申报配置信息有误,请联系管理员");
            return js;
        }
        boolean haveFzr = false;
        for (TeamUserHistory t : tuhs_stu) {
            if ("0".equals(t.getUserzz())) {
                haveFzr = true;
                break;
            }
        }
        if (!haveFzr) {
            js.put("msg", "请选择负责人");
            return js;
        }
        String subType = actYw.getProProject().getType();//大赛分类
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            Team teamNums = new Team();
            teamNums.setMemberNum(tuhs_stu.size());
            getTeamTeacherNum(teamNums, teas);//算出导师人数
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, proid, proType) > 0) {
                    js.put("msg", "有团队成员已参加项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的大赛,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, proid, proType, subType) > 0) {
                    js.put("msg", "有团队成员已申报其他类型的大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getaOnOff())) {//同一个大赛周期内，是否允许同一个学生在同一类项目中申报多个大赛,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, proid, proType, subType, year) > 0) {
                    js.put("msg", "有团队成员已申报该类型的大赛");
                    return js;
                }
            }
            PersonNumConf pnc = SysConfigUtil.getGconPersonNumConf(scv, subType);
            if (pnc != null) {
                if ("1".equals(pnc.getTeamNumOnOff())) {//团队人数范围
                    ConfRange cr = pnc.getTeamNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getMemberNum() < min || teamNums.getMemberNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队成员人数为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队成员人数为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getSchoolTeacherNumOnOff())) {//校园导师人数范围
                    ConfRange cr = pnc.getSchoolTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队校园导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队校园导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getEnTeacherNumOnOff())) {//企业导师人数范围
                    ConfRange cr = pnc.getEnTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getEnterpriseTeacherNum() < min || teamNums.getEnterpriseTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛企业导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛企业导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
            }
        }
        js.put("ret", 1);
        return js;
    }

    private void getTeamTeacherNum(Team teamNums, List<TeamUserHistory> teas) {
        int s = 0;
        int e = 0;
        if (teas != null && teas.size() > 0) {
            for (TeamUserHistory t : teas) {
                BackTeacherExpansion b = backTeacherExpansionDao.getByUserId(t.getUserId());
                if (b != null) {
                    if ((TeacherType.TY_XY.getKey()).equals(b.getTeachertype())) {
                        s++;
                    }
                    if ((TeacherType.TY_QY.getKey()).equals(b.getTeachertype())) {
                        e++;
                    }
                }
            }
        }
        teamNums.setSchoolTeacherNum(s);
        teamNums.setEnterpriseTeacherNum(e);
    }

    public JSONObject checkProjectTeam(String proid, String actywId, String lowType, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        JSONObject js = new JSONObject();
        js.put("ret", 0);
//        if (StringUtil.isEmpty(lowType)) {
//            js.put("msg", "请选择项目类别");
//            return js;
//        }
        if (StringUtil.isEmpty(teamid)) {
            js.put("msg", "请选择团队");
            return js;
        }
        if (StringUtil.isEmpty(year)) {
            js.put("msg", "不在申报有效时间内，无法申报");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//项目分类
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = teamUserHistoryDao.getTeamUserHistoryFromTUR(teamid, "1");
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, proid, proType) > 0) {
                    js.put("msg", "有团队成员已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, proid, proType, subType) > 0) {
                    js.put("msg", "有团队成员已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, proid, proType, subType, year) > 0) {
                    js.put("msg", "有团队成员已申报该类型的项目");
                    return js;
                }
            }
            PersonNumConf pnc = SysConfigUtil.getProPersonNumConf(scv, subType, lowType);
            if (pnc != null) {
                Team teamNums = teamDao.findTeamJoinInNums(teamid);
                if ("1".equals(pnc.getTeamNumOnOff())) {//团队人数范围
                    ConfRange cr = pnc.getTeamNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getMemberNum() < min || teamNums.getMemberNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队成员人数为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getSchoolTeacherNumOnOff())) {//校园导师人数范围
                    ConfRange cr = pnc.getSchoolTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目团队校园导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getEnTeacherNumOnOff())) {//企业导师人数范围
                    ConfRange cr = pnc.getEnTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getEnterpriseTeacherNum() < min || teamNums.getEnterpriseTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(lowType, FlowPcategoryType.PCT_XM.getKey(), "") + "项目企业导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
            }
        }
        js.put("ret", 1);
        return js;
    }

    public JSONObject checkGcontestTeam(String proid, String actywId, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_GCONTEST;//双创大赛
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无大赛申报配置信息,请联系管理员");
            return js;
        }
        if (StringUtil.isEmpty(year)) {
            js.put("msg", "不在申报有效时间内，无法申报");
            return js;
        }
        ActYw actYw = actYwDao.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "大赛申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//项目分类
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = teamUserHistoryDao.getTeamUserHistoryFromTUR(teamid, "1");
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, proid, proType) > 0) {
                    js.put("msg", "有团队成员已参加项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的大赛,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, proid, proType, subType) > 0) {
                    js.put("msg", "有团队成员已申报其他类型的大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getaOnOff())) {//同一个大赛周期内，是否允许同一个学生在同一类项目中申报多个大赛,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, proid, proType, subType, year) > 0) {
                    js.put("msg", "有团队成员已申报该类型的大赛");
                    return js;
                }
            }
            PersonNumConf pnc = SysConfigUtil.getGconPersonNumConf(scv, subType);
            if (pnc != null) {
                Team teamNums = teamDao.findTeamJoinInNums(teamid);
                if ("1".equals(pnc.getTeamNumOnOff())) {//团队人数范围
                    ConfRange cr = pnc.getTeamNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getMemberNum() < min || teamNums.getMemberNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队成员人数为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队成员人数为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getSchoolTeacherNumOnOff())) {//校园导师人数范围
                    ConfRange cr = pnc.getSchoolTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getSchoolTeacherNum() < min || teamNums.getSchoolTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队校园导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛团队校园导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
                if ("1".equals(pnc.getEnTeacherNumOnOff())) {//企业导师人数范围
                    ConfRange cr = pnc.getEnTeacherNum();
                    int min = Integer.valueOf(cr.getMin());
                    int max = Integer.valueOf(cr.getMax());
                    if (teamNums.getEnterpriseTeacherNum() < min || teamNums.getEnterpriseTeacherNum() > max) {
                        if (min == max) {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛企业导师为" + min + "人");
                        } else {
                            js.put("msg", DictUtils.getDictLabel(subType, "competition_type", "") + "大赛企业导师为" + min + "~" + max + "人");
                        }
                        return js;
                    }
                }
            }
        }
        js.put("ret", 1);
        return js;
    }

    private void addBtnArrs(JSONObject js, String btnname, String btnurl) {
        if (js == null) {
            return;
        }
        if (js.get(Jsbtns) == null) {
            js.put(Jsbtns, new JSONArray());
        }
        JSONArray btns = js.getJSONArray(Jsbtns);
        JSONObject btn = new JSONObject();
        btn.put("name", btnname);
        btn.put("url", btnurl);
        btns.add(btn);
    }

    /**
     * 广铁项目分步骤保存，第一步保存校验
     *
     * @param actywId
     * @return
     */
    public JSONObject onProjectSaveStep1(String actywId, String proid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            js.put("ret", 1);
            return js;
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师不能申报项目");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(user)) {
            js.put("msg", "个人信息未完善,立即完善个人信息？");
            js.put("ret", 2);
            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
            return js;
        }
        StudentExpansion stu = SysUserUtils.getStudentByUserId(user.getId());
        if (stu == null) {
            js.put("msg", "未找到学生信息");
            return js;
        }
        if ("2".equals(stu.getCurrState())) {
            js.put("msg", "您已经毕业,不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }

        String subType = actYw.getProProject().getType();//项目分类
        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "保存失败，距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = new ArrayList<TeamUserHistory>();
            TeamUserHistory tem = new TeamUserHistory();
            tem.setUserId(user.getId());
            tuhs_stu.add(tem);
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, null, proType) > 0) {
                    js.put("msg", "你已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, null, proType, subType) > 0) {
                    js.put("msg", "你已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, null, proType, subType, year) > 0) {
                    js.put("msg", "你已申报该类型的项目");
                    js.put("ret", 2);
                    if ("1".equals(subType)) {
                        addBtnArrs(js, "查看", DcUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    } else {// 自定义项目查看
                        addBtnArrs(js, "查看", ProUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    }
                    return js;
                }
            }

        }
        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (!"0".equals(pd.getStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            }
        }
        js.put("ret", 1);
        return js;
    }

    /**
     * 广铁项目分步骤保存，第二步保存校验
     *
     * @param actywId
     * @return
     */
    public JSONObject onProjectSaveStep2(String proid, String actywId, String lowType, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            js.put("ret", 1);
            return js;
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师不能申报项目");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(user)) {
            js.put("msg", "个人信息未完善,立即完善个人信息？");
            js.put("ret", 2);
            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
            return js;
        }
        StudentExpansion stu = SysUserUtils.getStudentByUserId(user.getId());
        if (stu == null) {
            js.put("msg", "未找到学生信息");
            return js;
        }
        if ("2".equals(stu.getCurrState())) {
            js.put("msg", "您已经毕业,不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }

        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (!"0".equals(pd.getStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            }
        }
        String subType = actYw.getProProject().getType();//项目分类

        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "保存失败，距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = new ArrayList<TeamUserHistory>();
            TeamUserHistory tem = new TeamUserHistory();
            tem.setUserId(user.getId());
            tuhs_stu.add(tem);
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, null, proType) > 0) {
                    js.put("msg", "你已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, null, proType, subType) > 0) {
                    js.put("msg", "你已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, null, proType, subType, year) > 0) {
                    js.put("msg", "你已申报该类型的项目");
                    js.put("ret", 2);
                    if ("1".equals(subType)) {
                        addBtnArrs(js, "查看", DcUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    } else {// 自定义项目查看
                        addBtnArrs(js, "查看", ProUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    }
                    return js;
                }
            }

        }

        //团队校验
        js = checkProjectTeam(proid, actywId, lowType, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }

        js.put("ret", 1);
        return js;
    }

    /**
     * 广铁项目分步骤保存，第三步提交校验
     *
     * @param actywId
     * @return
     */
    public JSONObject onProjectSubmitStep3(String proid, String actywId, String lowType, String teamid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            js.put("ret", 1);
            return js;
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师不能申报项目");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(user)) {
            js.put("msg", "个人信息未完善,立即完善个人信息？");
            js.put("ret", 2);
            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
            return js;
        }
        StudentExpansion stu = SysUserUtils.getStudentByUserId(user.getId());
        if (stu == null) {
            js.put("msg", "未找到学生信息");
            return js;
        }
        if ("2".equals(stu.getCurrState())) {
            js.put("msg", "您已经毕业,不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "提交失败，项目已被删除");
                    return js;
                }
                if (!"0".equals(pd.getStatus())) {
                    js.put("msg", "提交失败，项目已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "提交失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "提交失败，项目已被提交");
                    return js;
                }
            }
        }
        String subType = actYw.getProProject().getType();//项目分类

        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "提交失败，距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = new ArrayList<TeamUserHistory>();
            TeamUserHistory tem = new TeamUserHistory();
            tem.setUserId(user.getId());
            tuhs_stu.add(tem);
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, null, proType) > 0) {
                    js.put("msg", "你已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, null, proType, subType) > 0) {
                    js.put("msg", "你已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, null, proType, subType, year) > 0) {
                    js.put("msg", "你已申报该类型的项目");
                    js.put("ret", 2);
                    if ("1".equals(subType)) {
                        addBtnArrs(js, "查看", DcUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    } else {// 自定义项目查看
                        addBtnArrs(js, "查看", ProUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    }
                    return js;
                }
            }

        }

        //团队校验
        js = checkProjectTeam(proid, actywId, lowType, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "提交失败，" + js.getString("msg"));
            return js;
        }

        js.put("ret", 1);
        return js;
    }

    public JSONObject onProjectApply(String actywId, String pid, String year) {
        String proType = ProSval.PRO_TYPE_PROJECT;//双创项目
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            js.put("ret", 1);
            return js;
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师不能申报项目");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(user)) {
            js.put("msg", "个人信息未完善,立即完善个人信息？");
            js.put("ret", 2);
            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
            return js;
        }
        StudentExpansion stu = SysUserUtils.getStudentByUserId(user.getId());
        if (stu == null) {
            js.put("msg", "未找到学生信息");
            return js;
        }
        if ("2".equals(stu.getCurrState())) {
            js.put("msg", "您已经毕业,不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无项目申报配置信息,请联系管理员");
            return js;
        }
         ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "项目申报配置信息有误,请联系管理员");
            return js;
        }
        ActYwYear ayy = Optional.ofNullable(ActYwUtils.findActYwYear(actywId,TenantConfig.getCacheTenant())).orElse(this.getMsgActYwYear(actywId)) ;
        String subType = actYw.getProProject().getType();//项目分类
        if (!canApply(actywId, year, ayy)) {//不在申报期内

            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }

        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = new ArrayList<TeamUserHistory>();
            TeamUserHistory tem = new TeamUserHistory();
            tem.setUserId(user.getId());
            tuhs_stu.add(tem);
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, null, proType) > 0) {
                    js.put("msg", "你已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, null, proType, subType) > 0) {
                    js.put("msg", "你已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getProConf().getaOnOff())) {//同一个项目周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, null, proType, subType, year) > 0) {
                    js.put("msg", "你已申报该类型的项目");
                    js.put("ret", 2);
                    if ("1".equals(subType)) {
                        addBtnArrs(js, "查看", DcUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    } else {// 自定义项目查看
                        addBtnArrs(js, "查看", ProUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    }
                    return js;
                }
            }

        }


        if (StringUtil.isEmpty(pid)) {
            /**
             * 是否保存项目.
             */
            boolean hasSavePro = false;
            //项目或大赛Id
            String proid = teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "2");//未提交的

            if (StringUtil.isNotEmpty(proid)) {
                ProModel proModel=proModelDao.get(proid);
                if(proModel!=null ){
                    if(proModel.getDeclareId().equals(user.getId())){
                        hasSavePro = true;
                    }
                }else {
                    hasSavePro = true;
                }
            }
            if (!hasSavePro) {
                proid = proModelDao.getUnSubProIdByCdn(user.getId(), proType, subType);//未提交的
                if (StringUtil.isNotEmpty(proid)) {
                    hasSavePro = true;
                }
            }
            if (hasSavePro) {
                js.put("msg", "你有未提交的该类型项目");
                js.put("ret", 2);
                if ("1".equals(subType)) {

                    addBtnArrs(js, "继续完善表单", DcUrl + "/form?id=" + proid);
                } else {// 自定义项目修改
                    ProModel promodel=proModelDao.get(proid);
                    if(promodel!=null && promodel.getDeclareId().equals(user.getId())){
                        addBtnArrs(js, "继续完善表单", ProUrl + "/form?id=" + proid);
                    }else{
                        addBtnArrs(js, "返回列表", ProjectList);
                    }
                }
                return js;
            }

        }
        js.put("ret", 1);
        return js;
    }


    public JSONObject onGcontestApply(String proid, String actywId, String teamid, String year) {
        JSONObject js = onGcontestApply(actywId, proid, year);
        //团队校验
        js = checkGcontestTeam(proid, actywId, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "提交失败，" + js.getString("msg"));
            return js;
        }

        js.put("ret", 1);
        return js;
    }

    public JSONObject onGcontestApply(String actywId, String pid, String year) {
        String proType = ProSval.PRO_TYPE_GCONTEST;//双创大赛
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            js.put("ret", 1);
            return js;
        }
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "导师不能申报大赛");
            return js;
        }
        if (SysUserUtils.checkInfoPerfect(user)) {
            js.put("msg", "个人信息未完善,立即完善个人信息？");
            js.put("ret", 2);
            addBtnArrs(js, "确定", "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1");
            return js;
        }
        StudentExpansion stu = SysUserUtils.getStudentByUserId(user.getId());
        if (stu == null) {
            js.put("msg", "未找到学生信息");
            return js;
        }
        if ("2".equals(stu.getCurrState())) {
            js.put("msg", "您已经毕业,不能申报大赛");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "无大赛申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "大赛申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//大赛分类

        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "competition_type", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "本届" + DictUtils.getDictLabel(subType, "competition_type", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
            }
        }
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        if (scv != null) {
            List<TeamUserHistory> tuhs_stu = new ArrayList<TeamUserHistory>();
            TeamUserHistory tem = new TeamUserHistory();
            tem.setUserId(user.getId());
            tuhs_stu.add(tem);
            if ("0".equals(scv.getApplyConf().getaOnOff())) {//是否允许同一个学生用不同项目既申报项目又参加大赛，1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn1(tuhs_stu, null, proType) > 0) {
                    js.put("msg", "你已参加大赛");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getbOnOff())) {//是否允许同一个学生申报多个不同类型的项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn2(tuhs_stu, null, proType, subType) > 0) {
                    js.put("msg", "你已申报其他类型的项目");
                    return js;
                }
            }
            if ("0".equals(scv.getApplyConf().getGconConf().getaOnOff())) {//同一个大赛周期内，是否允许同一个学生在同一类项目中申报多个项目,1-允许，0-不允许
                if (teamUserHistoryDao.countByCdn3(tuhs_stu, null, proType, subType, year) > 0) {
                    js.put("msg", "你已申报该类型的项目");
                    js.put("ret", 2);
                    //互联网+
                    if ("1".equals(subType)) {
                        addBtnArrs(js, "查看", "/f/gcontest/gContest/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    } else {//自定义大赛项目查看
                        addBtnArrs(js, "查看", ProUrl + "/viewForm?id=" + teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "0"));
                    }
                    return js;
                }
            }

        }

        if (StringUtil.isEmpty(pid)) {
            String proid = teamUserHistoryDao.getProIdByCdn(user.getId(), proType, subType, "2");//未提交的
            if (StringUtil.isNotEmpty(proid)) {
                js.put("msg", "你有未提交的该类型大赛");
                js.put("ret", 2);
                if ("1".equals(subType)) {
                    addBtnArrs(js, "继续完善表单", "/f/gcontest/gContest/form?id=" + proid);
                } else {
                    addBtnArrs(js, "继续完善表单", ProUrl + "/form?id=" + proid);
                }
                return js;
            }
        }
        js.put("ret", 1);
        return js;
    }

    /**
     * 保存项目或大赛时处理TeamUserHistory表信息
     *
     * @param stus
     * @param teas
     * @param actywId
     * @param teamId
     * @param proId
     */
    @Transactional(readOnly = false)
    public void disposeTeamUserHistoryOnSave(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String actywId, String teamId, String proId, String year) {
        disposeTeamUserHistory(stus, teas, actywId, teamId, proId, TeamUserHistory.finish_val2, year);
    }

    /**
     * 提交项目或大赛时处理TeamUserHistory表信息
     *
     * @param stus
     * @param teas
     * @param actywId
     * @param teamId
     * @param proId
     */
    @Transactional(readOnly = false)
    public void disposeTeamUserHistoryOnSubmit(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String actywId, String teamId, String proId, String year) {
        disposeTeamUserHistory(stus, teas, actywId, teamId, proId, TeamUserHistory.finish_val0, year);
    }

    /**
     * 保存项目或大赛时处理TeamUserHistory表信息
     *
     * @param tuhs    带有学分配比
     * @param actywId
     * @param teamId
     * @param proId
     */
    @Transactional(readOnly = false)
    public void disposeTeamUserHistoryOnSave(List<TeamUserHistory> tuhs, String actywId, String teamId, String proId, String year) {
        disposeTeamUserHistory(tuhs, actywId, teamId, proId, TeamUserHistory.finish_val2, year);
    }

    /**
     * 提交项目或大赛时处理TeamUserHistory表信息
     *
     * @param tuhs    带有学分配比
     * @param actywId
     * @param teamId
     * @param proId
     */
    @Transactional(readOnly = false)
    public void disposeTeamUserHistoryOnSubmit(List<TeamUserHistory> tuhs, String actywId, String teamId, String proId, String year) {
        //// TODO: 2018/11/27 0027  判断是否有入驻提交 有入驻提交新生成团队
        disposeTeamUserHistory(tuhs, actywId, teamId, proId, TeamUserHistory.finish_val0, year);
    }

    private void disposeTeamUserHistory(List<TeamUserHistory> tuhs, String actywId, String teamId, String proId, String finish, String year) {
        Date now = new Date();
        User cuser = UserUtils.getUser();
        if (tuhs == null || tuhs.size() == 0) {
            throw new RuntimeException("TeamUserHistory不能为空");
        }
        if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(teamId) || StringUtil.isEmpty(proId)) {
            throw new RuntimeException("参数不能为空");
        }
        ActYw actYw = actYwDao.get(actywId);
        if (actYw == null) {
            throw new RuntimeException("找不到ActYw");
        }
        ProProject proProject = actYw.getProProject();
        if (proProject == null) {
            throw new RuntimeException("找不到ProProject");
        }
        if (StringUtil.isEmpty(proProject.getProType()) || StringUtil.isEmpty(proProject.getType())) {
            throw new RuntimeException("ProProject的类型不能为空");
        }
        String proType = proProject.getProType();
        String proSubType = proProject.getType();
        for (TeamUserHistory tuh : tuhs) {
            tuh.setId(IdGen.uuid());
            tuh.setUser(new User(tuh.getUserId()));
            tuh.setTeamId(teamId);
            tuh.setCreateBy(cuser);
            tuh.setUpdateBy(cuser);
            tuh.setCreateDate(now);
            tuh.setUpdateDate(now);
            tuh.setProId(proId);
            tuh.setProType(proType);
            tuh.setProSubType(proSubType);
            tuh.setUserType("1");
            tuh.setFinish(finish);
            tuh.setDelFlag("0");
            tuh.setWeightVal(tuh.getWeightVal());
            tuh.setYear(year);
        }
        List<TeamUserHistory> tuhs_teas = teamUserHistoryDao.getTeamUserHistoryFromTUR(teamId, "2");
        if (tuhs_teas != null && tuhs_teas.size() > 0) {
            for (TeamUserHistory tuh : tuhs_teas) {
                tuh.setId(IdGen.uuid());
                tuh.setUser(new User(tuh.getUserId()));
                tuh.setTeamId(teamId);
                tuh.setCreateBy(cuser);
                tuh.setUpdateBy(cuser);
                tuh.setCreateDate(now);
                tuh.setUpdateDate(now);
                tuh.setProId(proId);
                tuh.setProType(proType);
                tuh.setProSubType(proSubType);
                tuh.setUserType("2");
                tuh.setFinish(finish);
                tuh.setDelFlag("0");
                tuh.setYear(year);
            }
            ;
            tuhs.addAll(tuhs_teas);
        }
        teamUserHistoryDao.deleteByProId(proId);
        teamUserHistoryDao.insertAll(tuhs);
    }

    private void disposeTeamUserHistory(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String actywId, String teamId, String proId, String finish, String year) {
        Date now = new Date();
        User cuser = UserUtils.getUser();
        if (stus == null || stus.size() == 0) {
            throw new RuntimeException("TeamUserHistory不能为空");
        }
        if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(teamId) || StringUtil.isEmpty(proId)) {
            throw new RuntimeException("参数不能为空");
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null) {
            throw new RuntimeException("找不到ActYw");
        }
        ProProject proProject = actYw.getProProject();
        if (proProject == null) {
            throw new RuntimeException("找不到ProProject");
        }
        if (StringUtil.isEmpty(proProject.getProType()) || StringUtil.isEmpty(proProject.getType())) {
            throw new RuntimeException("ProProject的类型不能为空");
        }
        String proType = proProject.getProType();
        String proSubType = proProject.getType();
        for (TeamUserHistory tuh : stus) {
            tuh.setId(IdGen.uuid());
            tuh.setUser(new User(tuh.getUserId()));
            tuh.setTeamId(teamId);
            tuh.setCreateBy(cuser);
            tuh.setUpdateBy(cuser);
            tuh.setCreateDate(now);
            tuh.setUpdateDate(now);
            tuh.setProId(proId);
            tuh.setProType(proType);
            tuh.setProSubType(proSubType);
            tuh.setUserType("1");
            tuh.setFinish(finish);
            tuh.setDelFlag("0");
            tuh.setWeightVal(tuh.getWeightVal());
            tuh.setYear(year);
        }
        if (teas != null && teas.size() > 0) {
            for (TeamUserHistory tuh : teas) {
                tuh.setId(IdGen.uuid());
                tuh.setUser(new User(tuh.getUserId()));
                tuh.setTeamId(teamId);
                tuh.setCreateBy(cuser);
                tuh.setUpdateBy(cuser);
                tuh.setCreateDate(now);
                tuh.setUpdateDate(now);
                tuh.setProId(proId);
                tuh.setProType(proType);
                tuh.setProSubType(proSubType);
                tuh.setUserType("2");
                tuh.setFinish(finish);
                tuh.setDelFlag("0");
                tuh.setYear(year);
            }
            ;
            stus.addAll(teas);
        }
        teamUserHistoryDao.deleteByProId(proId);
        teamUserHistoryDao.insertAll(stus);
    }

    @Transactional(readOnly = false)
    public void copyTeamUserHistoryFromTUR(String actywId, String teamId, String proId, String finish, String year) {
        Date now = new Date();
        User cuser = UserUtils.getUser();
        if (StringUtil.isEmpty(actywId) || StringUtil.isEmpty(teamId) || StringUtil.isEmpty(proId)) {
            throw new RuntimeException("参数不能为空");
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null) {
            throw new RuntimeException("找不到ActYw");
        }
        ProProject proProject = actYw.getProProject();
        if (proProject == null) {
            throw new RuntimeException("找不到ProProject");
        }
        if (StringUtil.isEmpty(proProject.getProType()) || StringUtil.isEmpty(proProject.getType())) {
            throw new RuntimeException("ProProject的类型不能为空");
        }
        String proType = proProject.getProType();
        String proSubType = proProject.getType();
        List<TeamUserRelation> turs = teamUserRelationDao.getByTeamId(teamId);
        List<TeamUserHistory> tuhs = new ArrayList<TeamUserHistory>();
        for (TeamUserRelation tur : turs) {
            TeamUserHistory tuh = new TeamUserHistory();
            tuh.setId(IdGen.uuid());
            tuh.setUser(tur.getUser());
            tuh.setTeamId(teamId);
            tuh.setCreateBy(cuser);
            tuh.setUpdateBy(cuser);
            tuh.setCreateDate(now);
            tuh.setUpdateDate(now);
            tuh.setProId(proId);
            tuh.setProType(proType);
            tuh.setProSubType(proSubType);
            tuh.setUserType(tur.getUserType());
            tuh.setFinish(finish);
            tuh.setDelFlag(Const.NO);
            tuh.setWeightVal(tuh.getWeightVal());
            tuh.setYear(year);
            tuhs.add(tuh);
        }
        if (tuhs.size() > 0) {
            teamUserHistoryDao.deleteByProId(proId);
            teamUserHistoryDao.insertAll(tuhs);
        }
    }

    public JSONObject checkProjectOnModify(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String proid, String actywId, String lowType, String teamid, String year) {
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "保存失败，无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "保存失败，未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "保存失败，项目申报配置信息有误,请联系管理员");
            return js;
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
            }
        }
        //团队校验
        js = checkProjectTeamForModify(stus, teas, proid, actywId, lowType, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }


        js.put("ret", 1);
        return js;
    }

    public JSONObject checkGcontestOnModify(List<TeamUserHistory> stus, List<TeamUserHistory> teas, String proid, String actywId, String teamid, String year) {
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "保存失败，无大赛申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwDao.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "保存失败，未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "保存失败，大赛申报配置信息有误,请联系管理员");
            return js;
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            GContest gc = gContestDao.get(proid);
            if (gc != null) {
                if ("1".equals(gc.getDelFlag())) {
                    js.put("msg", "保存失败，大赛已被删除");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，大赛已被删除");
                    return js;
                }
            }
        }
        //团队校验
        js = checkGcontestTeamForModify(stus, teas, proid, actywId, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }


        js.put("ret", 1);
        return js;
    }

    /**
     * @param proid   项目id
     * @param actywId
     * @param lowType 项目类别
     * @param teamid  团队id
     * @return JSONObject {ret:1-成功\0-失败，msg:成功或失败信息}
     */
    public JSONObject checkProjectApplyOnSave(String proid, String actywId, String lowType, String teamid, String year) {
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "保存失败，导师不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "保存失败，无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "保存失败，未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "保存失败，项目申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//项目分类
        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "保存失败,距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "保存失败，本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (!"0".equals(pd.getStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "保存失败，项目已被提交");
                    return js;
                }
            }
        }
        //团队校验
        js = checkProjectTeam(proid, actywId, lowType, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }


        js.put("ret", 1);
        return js;
    }

    /**
     * @param proid   项目id
     * @param actywId
     * @param lowType 项目类别
     * @param teamid  团队id
     * @return JSONObject {ret:1-成功\0-失败，msg:成功或失败信息}
     */
    public JSONObject checkProjectApplyOnSubmit(String proid, String actywId, String lowType, String teamid, String year) {
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "提交失败，导师不能申报项目");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "提交失败，无项目申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "提交失败，未找到项目申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "提交失败，项目申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//项目分类
        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "提交失败,距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "project_style", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "提交失败，本届" + DictUtils.getDictLabel(subType, "project_style", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入项目列表", ProjectList);
                    return js;
                }
            }
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            ProjectDeclare pd = projectDeclareDao.get(proid);
            if (pd != null) {
                if ("1".equals(pd.getDelFlag())) {
                    js.put("msg", "提交失败，项目已被删除");
                    return js;
                }
                if (!"0".equals(pd.getStatus())) {
                    js.put("msg", "提交失败，项目已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "提交失败，项目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "提交失败，项目已被提交");
                    return js;
                }
            }
        }
        //团队校验
        js = checkProjectTeam(proid, actywId, lowType, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "提交失败，" + js.getString("msg"));
            return js;
        }

        js.put("ret", 1);
        return js;
    }

    /**
     * @param proid   大赛id
     * @param actywId
     * @param teamid  团队id
     * @return JSONObject {ret:1-成功\0-失败，msg:成功或失败信息}
     */
    public JSONObject checkGcontestApplyOnSave(String proid, String actywId, String teamid, String year) {
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "保存失败，导师不能申报大赛");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "保存失败，无大赛申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "保存失败，未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "保存失败，大赛申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//大赛分类
        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "保存失败,距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "competition_type", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "保存失败，本届" + DictUtils.getDictLabel(subType, "competition_type", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
            }
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            GContest gc = gContestDao.get(proid);
            if (gc != null) {
                if ("1".equals(gc.getDelFlag())) {
                    js.put("msg", "保存失败，大赛已被删除");
                    return js;
                }
                if (!"0".equals(gc.getAuditState())) {
                    js.put("msg", "保存失败，大赛已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，大赛目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "保存失败，大赛已被提交");
                    return js;
                }
            }
        }
        //团队校验
        js = checkGcontestTeam(proid, actywId, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }

        js.put("ret", 1);
        return js;
    }

    public JSONObject checkGcontestApplyOnSubmit(String proid, String actywId, String teamid, String year) {
        Date now = new Date();
        JSONObject js = new JSONObject();
        js.put("ret", 0);
        User user = UserUtils.getUser();
        if (SysUserUtils.checkHasRole(user, RoleBizTypeEnum.DS)) {
            js.put("msg", "保存失败，导师不能申报大赛");
            return js;
        }
        if (StringUtil.isEmpty(actywId)) {
            js.put("msg", "保存失败，无大赛申报配置信息,请联系管理员");
            return js;
        }
        ActYw actYw = actYwService.get(actywId);
        if (actYw == null || !actYw.getIsDeploy() || "1".equals(actYw.getDelFlag())) {
            js.put("msg", "保存失败，未找到大赛申报配置信息,请联系管理员");
            return js;
        }
        if (actYw.getProProject() == null || StringUtil.isEmpty(actYw.getProProject().getType()) || "1".equals(actYw.getProProject().getDelFlag())) {
            js.put("msg", "保存失败，大赛申报配置信息有误,请联系管理员");
            return js;
        }
        String subType = actYw.getProProject().getType();//大赛分类
        if (!canApply(actywId, year)) {//不在申报期内
            ActYwYear ayy = this.getMsgActYwYear(actywId);
            if (ayy != null) {
                Date start = ayy.getNodeStartDate();
                Date end = ayy.getNodeEndDate();
                if (start != null && start.after(now)) {
                    js.put("msg", "保存失败,距离第" + ayy.getYear()
                            + "届" + DictUtils.getDictLabel(subType, "competition_type", "")
                            + "申报还有" + DateUtil.formatDateTime2(start.getTime() - now.getTime()));
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
                if (end != null && end.before(now)) {
                    js.put("msg", "保存失败，本届" + DictUtils.getDictLabel(subType, "competition_type", "") + "申报截止");
                    js.put("ret", 2);
                    addBtnArrs(js, "返回首页", Home);
                    addBtnArrs(js, "进入大赛列表", GcontestList);
                    return js;
                }
            }
        }
        if (!StringUtil.isEmpty(proid)) {//修改
            GContest gc = gContestDao.get(proid);
            if (gc != null) {
                if ("1".equals(gc.getDelFlag())) {
                    js.put("msg", "保存失败，大赛已被删除");
                    return js;
                }
                if (!"0".equals(gc.getAuditState())) {
                    js.put("msg", "保存失败，大赛已被提交");
                    return js;
                }
            } else {
                ProModel pm = proModelDao.get(proid);
                if (pm == null || "1".equals(pm.getDelFlag())) {
                    js.put("msg", "保存失败，大赛目已被删除");
                    return js;
                }
                if (Const.YES.equals(pm.getSubStatus())) {
                    js.put("msg", "保存失败，大赛已被提交");
                    return js;
                }
            }
        }
        //团队校验
        js = checkGcontestTeam(proid, actywId, teamid, year);
        if ("0".equals(js.getString("ret"))) {
            js.put("msg", "保存失败，" + js.getString("msg"));
            return js;
        }


        js.put("ret", 1);
        return js;
    }


}
