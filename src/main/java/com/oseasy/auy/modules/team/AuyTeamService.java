package com.oseasy.auy.modules.team;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.common.utils.sms.SMSUtilAlidayu;
import com.oseasy.com.mqserver.common.utils.sms.impl.SendParam;
import com.oseasy.com.mqserver.modules.oa.dao.OaNotifyDao;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifyRecord;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.vo.OaNotifySendType;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CoreJkey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.modules.sys.dao.UserDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.entity.Role;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.service.SystemService;
import com.oseasy.com.pcore.modules.sys.service.UserService;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.rediserver.common.utils.CacheUtils;
import com.oseasy.pw.modules.pw.service.PwEnterDetailService;
import com.oseasy.sys.common.utils.SysUserUtils;
import com.oseasy.sys.modules.seq.service.SequenceService;
import com.oseasy.sys.modules.sys.dao.BackTeacherExpansionDao;
import com.oseasy.sys.modules.sys.entity.BackTeacherExpansion;
import com.oseasy.sys.modules.sys.enums.RoleBizTypeEnum;
import com.oseasy.sys.modules.sys.service.SysConfigService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeacherType;
import com.oseasy.sys.modules.sys.vo.TeamConf;
import com.oseasy.sys.modules.team.dao.TeamDao;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.entity.TeamDetails;
import com.oseasy.sys.modules.team.entity.TeamUserRelation;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.sys.modules.team.service.TeamUserHistoryService;
import com.oseasy.sys.modules.team.service.TeamUserRelationService;
import com.oseasy.util.common.utils.Msg;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.RunException;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 团队管理Service
 *
 * @author 刘波
 * @version 2017-03-30
 */
@Service
@Transactional(readOnly = true)
public class AuyTeamService extends CrudService<TeamDao, Team> {
    @Autowired
    TeamUserHistoryService teamUserHistoryService;
    @Autowired
    PwEnterDetailService pwEnterDetailService;
    @Autowired
    UserService userService;
    @Autowired
    OfficeService officeService;
    @Autowired
    TeamService teamService;
    public Page<Team> findPage(Page<Team> page, Team team) {
        page = super.findPage(page, team);
        if (page != null && page.getList() != null && page.getList().size() > 0) {
            List<Team> teamList = page.getList();
            /*查询当前登录人(学生)在每一个团队中是否已经加入，控制申请加入按钮*/
            List<Map<String, Object>> rl = dao.checkIsJoinInTeams(teamList, UserUtils.getUser().getId());//加入了团队的
            Map<String, Object> cm = new HashMap<String, Object>();
            if (rl != null) {
                for (Map<String, Object> m : rl) {
                    Object tid = m.get("team_id");
                    Object cc = m.get("cc");
                    if (tid != null && cc != null) {
                        cm.put(tid.toString(), cc);
                    }
                }
            }
            List<Map<String, Object>> rll = dao.checkIsJoinInTUR(teamList, UserUtils.getUser().getId());//加入团队或者发申请和被邀请
            Map<String, Object> cml = new HashMap<String, Object>();
            if (rll != null) {
                for (Map<String, Object> m : rll) {
                    Object tid = m.get("team_id");
                    Object cc = m.get("cc");
                    if (tid != null && cc != null) {
                        cml.put(tid.toString(), cc);
                    }
                }
            }
            for (Team tt : teamList) {
                if (cm.get(tt.getId()) != null) {
                    tt.setCheckJoin(true);
                } else {
                    tt.setCheckJoin(false);
                }
                if (cml.get(tt.getId()) != null) {
                    tt.setCheckJoinTUR(true);
                } else {
                    tt.setCheckJoinTUR(false);
                }
                //查询大赛和项目状态
                int tuhNum=teamUserHistoryService.getHisByTeamId(tt.getId());
                //查询基地入驻状态
                int pwNum=pwEnterDetailService.checkPwEnterByTeamId(tt.getId());

                if(tuhNum == 0 && pwNum==0){
                    tt.setCheckIsHus(false);
                }
                if (StringUtil.isNotBlank(tt.getSponsor())) {
                    User usertmp = userService.findUserById(tt.getSponsor());
                    if (usertmp != null) {
                        tt.setSponsorId(tt.getSponsor());
                        tt.setSponsor(usertmp.getName());
                    }
                }

                if (StringUtil.isNotBlank(tt.getLocalCollege())) {
                    Office officetmp = officeService.get(tt.getLocalCollege());
                    if (officetmp != null) {
                        tt.setLocalCollege(officetmp.getName());
                    }
                }

                String teamId = tt.getId();// 获取teamId
                List<Team> teamUserName = teamService.findTeamUserName(teamId);
                int qyCount = 0;
                int xyCount = 0;
                int xsCount = 0;
                StringBuffer xsbuffer = new StringBuffer();
                StringBuffer qybuffer = new StringBuffer();
                StringBuffer xybuffer = new StringBuffer();
                if (teamUserName != null) {
                    for (Team teamTm : teamUserName) {
                        if (teamTm != null) {
                            if ("1".equals(teamTm.getTeamUserType())) {// 代表类型是学生
                                xsCount++;
                                xsbuffer.append(teamTm.getuName() + "/");
                            } else if ("2".equals(teamTm.getTeamUserType())) {// 否则类型就是导师
                                if ("2".equals(teamTm.getTeacherType())) {// 判断是否是企业导师
                                    qyCount++;
                                    qybuffer.append(teamTm.getuName() + "/");
                                } else if ("1".equals(teamTm.getTeacherType())) {// 否则就是校园导师
                                    xyCount++;
                                    xybuffer.append(teamTm.getuName() + "/");
                                }
                            }
                        }
                    }
                    if (xsbuffer.length() > 0) {
                        tt.setUserName(xsbuffer.substring(0, xsbuffer.lastIndexOf("/")));
                    }
                    tt.setUserCount(xsCount);
                    if (qybuffer.length() > 0) {
                        tt.setEntName(qybuffer.substring(0, qybuffer.lastIndexOf("/")));
                    }
                    tt.setEnterpriseNum(qyCount);
                    if (xybuffer.length() > 0) {
                        tt.setSchName(xybuffer.substring(0, xybuffer.lastIndexOf("/")));
                    }
                    tt.setSchoolNum(xyCount);
                }
            }
            page.setList(teamList);
        }
        return page;
    }
}