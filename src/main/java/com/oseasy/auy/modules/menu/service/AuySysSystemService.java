/**
 *
 */
package com.oseasy.auy.modules.menu.service;

import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.act.modules.act.service.ActTaskService;
import com.oseasy.com.pcore.modules.sys.entity.Menu;
import com.oseasy.pro.common.config.ProSval;
import com.oseasy.pro.modules.promodel.utils.GcontestUtils;
import com.oseasy.pro.modules.promodel.utils.ProjectUtils;
import com.oseasy.pw.modules.pw.service.PwApplyRecordService;
import com.oseasy.pw.modules.pw.service.PwAppointmentService;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterBgremarks;
import com.oseasy.scr.modules.sco.service.ScoApplyService;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
@Transactional(readOnly = true)
public class AuySysSystemService {
    @Autowired
    private TeamService teamService;
    @Autowired
    private ScoApplyService scoApplyService;
    @Autowired
    private PwEnterService pwEnterService;
    @Autowired
    private PwApplyRecordService pwApplyRecordService;
    @Autowired
    private PwAppointmentService pwAppointmentService;
    @Autowired
    private AuyActTaskService auyActTaskService;

    /**
     * 根据菜单获取
     */
    public Long menuTodoCount(Menu menu, ActTaskService actTaskService) {
        long count = 0;
        String hreff = menu.getHref();
        if (StringUtil.isEmpty(hreff)) {
            return count;
        }
        String url = hreff.split("\\?")[0];
        if (StringUtils.isNotBlank(hreff) && hreff.contains("?actywId=") && hreff.contains("&gnodeId=")) {
            int index1 = hreff.indexOf("?actywId=");
            int index2 = hreff.indexOf("&gnodeId=");
            String actywId = hreff.substring(index1 + 9, index2);
            String gnodeId = hreff.substring(index2 + 9);
//            count = proActTaskService.todoCount(actywId, gnodeId);
            count = auyActTaskService.todoCount(actywId, gnodeId);
        } else if (StringUtil.endsWith(url, "setAuditList")) {
            count = ProjectUtils.getAuditListCount();
        } else if (StringUtil.endsWith(url, "middleAuditList")) {
            count = ProjectUtils.getMidCount();
        } else if (StringUtil.endsWith(url, "closeAuditList")) {
            count = ProjectUtils.closeAuditCount();
        } else if (StringUtil.endsWith(url, "closeReplyingList")) {
            count = ProjectUtils.closeReplyingCount();
        } else if (StringUtil.endsWith(url, "assessList")) {
            count = ProjectUtils.assessCount();
        } else if (StringUtil.endsWith(url, "collegeExportScore")) {
            count = GcontestUtils.collegeExportCount();
        } else if (StringUtil.endsWith(url, "schoolActAuditList")) {
            count = GcontestUtils.schoolActAuditList();
        } else if (StringUtil.endsWith(url, "schoolEndAuditList")) {
            count = GcontestUtils.schoolEndAuditList();
        } else if (StringUtil.endsWith(url, "team")) {// 人才库团队审核
            count = teamService.getTeamCountToAudit();
        } else if (StringUtil.endsWith(url, "sco/scoreGrade/courseList")) {// 学分认定审核
            count = scoApplyService.getCountToAudit();
        } else if (StringUtil.endsWith(url, "pw/pwEnter/list")) {// 入驻审核
            count = pwEnterService.getCountToAudit();
        } else if (StringUtil.endsWith(url, "pw/pwEnter/listBGSH")) {// 入驻变更审核
            count = pwEnterService.getCountToBGSH();
        } else if (StringUtil.endsWith(url, "pw/pwEnter/listFPCD")) {// 场地分配
            count = pwEnterService.getCountToFPCD();
        } else if (StringUtil.endsWith(url, "pw/pwEnter/listXQRZCompany")) {// 入驻续期管理
            count = pwApplyRecordService.findCountByType(PwEnterBgremarks.R3.getKey());
        } else if (StringUtil.endsWith(url, "pw/pwEnter/listQXRZCompany")) {// 入驻退孵管理
            count = pwApplyRecordService.findCountByType(PwEnterBgremarks.R4.getKey());
        } else if (StringUtil.endsWith(url, "pw/pwAppointment/review")) {// 预约审核
            count = pwAppointmentService.getCountToAudit();
        } else if (StringUtil.contains(url, "taskAssignList")) {// 指派
            int index1 = hreff.indexOf("?actywId=");
            String actywId = hreff.substring(index1 + 9);
            count = actTaskService.recordIdsAllAssign(actywId);
        } else if (StringUtil.contains(url, "TaskList")) {// 指派
            //任务分配只允许管理员查看
            if (UserUtils.isAdminOrSuperAdmin(UserUtils.getUser())){
                if (StringUtil.contains(url, "xm")) {
                    count = actTaskService.getByProTypeToAssignCount(ProSval.PRO_TYPE_PROJECT);
                } else {
                    count = actTaskService.getByProTypeToAssignCount(ProSval.PRO_TYPE_GCONTEST);
                }
            }
        }
        return count;
    }
}
