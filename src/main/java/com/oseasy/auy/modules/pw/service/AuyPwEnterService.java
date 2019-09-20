package com.oseasy.auy.modules.pw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.entity.ActYwApply;
import com.oseasy.act.modules.actyw.tool.process.vo.FlowType;
import com.oseasy.auy.modules.act.service.AuyActYwApplyService;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.fileserver.modules.attachment.enums.FileStepEnum;
import com.oseasy.com.fileserver.modules.attachment.enums.FileTypeEnum;
import com.oseasy.com.fileserver.modules.attachment.vo.SysAttachmentVo;
import com.oseasy.com.pcore.common.config.CoreSval.Const;
import com.oseasy.com.pcore.common.service.CrudService;
import com.oseasy.pro.modules.promodel.tool.process.vo.FlowYwId;
import com.oseasy.pw.modules.pw.dao.PwEnterDao;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.entity.PwEnterDetail;
import com.oseasy.pw.modules.pw.exception.EnterException;
import com.oseasy.pw.modules.pw.exception.NoTeamException;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterVo;
import com.oseasy.sys.modules.team.entity.Team;
import com.oseasy.sys.modules.team.service.TeamService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报Service.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Service
@Transactional(readOnly = true)
public class AuyPwEnterService extends CrudService<PwEnterDao, PwEnter> {
    @Autowired
    PwEnterService pwEnterService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private AuyActYwApplyService auyActYwApplyService;

    /*******************************************************************************
     ***已处理的方法******************************************************************
     ******************************************************************************/
    @Transactional(readOnly = false)
    public ActYwApply saveApply(PwEnter pwEnter) {
        ActYwApply actYwApply = new ActYwApply();
        actYwApply.setActYw(new ActYw(FlowYwId.FY_ENTER.getId()));
        actYwApply.setType(FlowType.FWT_ENTER.getKey());
        actYwApply.setRelId(pwEnter.getId());
        auyActYwApplyService.saveApplyAndSubmit(actYwApply);
        return actYwApply;
    }


    /**
     * 入驻申报.
     *
     * @param pwEnter
     * @param isSaveRel
     * @return
     * @throws NoTeamException
     * @throws EnterException
     */
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> saveEnterApply(PwEnter pwEnter, Boolean isSaveRel)
            throws NoTeamException, EnterException {
        if (isSaveRel == null) {
            isSaveRel = false;
        }
        Boolean isTrue = false;
        ActYwApply actYwApply = null;

        if (isSaveRel) {
            pwEnter.setIsTemp(Const.NO);
            save(pwEnter);
            actYwApply = saveApply(pwEnter);
            if ((actYwApply == null)) {
                throw new EnterException("保存失败(申报信息保存失败)！");
            }
        }

        if (isTrue) {
            return new ApiTstatus<PwEnter>(true, "保存成功！", pwEnter);
        } else {
            throw new EnterException("保存失败！");
        }
    }


    /**
     * 入驻申报.
     *
     * @param peVo
     * @return
     * @throws EnterException
     */
    @Transactional(readOnly = false)
    public ApiTstatus<PwEnter> saveEnterApply(PwEnterVo peVo) throws NoTeamException, EnterException {
        if (StringUtil.isEmpty(peVo.getEid())) {
            return new ApiTstatus<PwEnter>(false, "入驻申请不能为空");
        }

        if ((!peVo.getHasCompany()) && StringUtil.isEmpty(peVo.getTeamId())) {
            return new ApiTstatus<PwEnter>(false, "入驻团队不能为空");
        }

        PwEnter pwEnter = pwEnterService.getByGroup(new PwEnter(peVo.getEid()));
        if (pwEnter == null) {
            return new ApiTstatus<PwEnter>(false, "入驻记录不存在！");
        }

        List<String> delIds = Lists.newArrayList();
        if (peVo.getHasCompany()) {
            if ((peVo.getPwCompany() != null)) {
                List<SysAttachmentVo> list = peVo.getCfiles();
                if (list != null && list.size() > 0) {
                    pwEnterService.saveSysAttachment(peVo.getEid(), FileTypeEnum.S_PW_ENTER.getValue(),
                            FileStepEnum.S_ENTER_COMPANY.getValue(), list);
                }
            } else {
                return new ApiTstatus<PwEnter>(false, "入驻企业参数不能为空");
            }
        } else {
            if ((pwEnter.getEcompany() != null) && StringUtil.isNotEmpty(pwEnter.getEcompany().getId())) {
                delIds.add(pwEnter.getEcompany().getId());
            }
            pwEnter.setEcompany(null);
        }

        if (peVo.getHasProject()) {
            List<SysAttachmentVo> list = peVo.getPfiles();
            // 提交时同时保存项目所带数据
            if (peVo.getIsSave()) {
                if (peVo.getProjectFiles() != null && peVo.getProjectFiles().size() > 0) {
                    list.addAll(peVo.getProjectFiles());
                }
            }
            if (list != null && list.size() > 0) {
                pwEnterService.saveSysAttachment(peVo.getEid(), FileTypeEnum.S_PW_ENTER.getValue(),
                        FileStepEnum.S_ENTER_PROJECT.getValue(), list);
            }
        } else {
        }

        if (peVo.getHasTeam()) {
            if (StringUtil.isNotEmpty(peVo.getTeamId())) {
                /**
                 * 校验团队是否处于正常状态.
                 */
                Team team = teamService.getByUsable(peVo.getTeamId());
                if ((team == null)) {
                    return new ApiTstatus<PwEnter>(false, "入驻团队被删除或非建设完毕状态");
                }
            } else {
                return new ApiTstatus<PwEnter>(false, "入驻团队不能为空");
            }

            if ((pwEnter.getEteam() == null)) {
                pwEnter.setEteam(new PwEnterDetail(new Team(peVo.getTeamId())));
                pwEnter.getEteam().setRemarks(peVo.getTeamRemarks());
            } else {
                pwEnter.getEteam().setRemarks(peVo.getTeamRemarks());
                pwEnter.getEteam().setIsNewRecord(false);
            }
            List<SysAttachmentVo> list = peVo.getTfiles();
            if (list != null && list.size() > 0) {
                pwEnterService.saveSysAttachment(peVo.getEid(), FileTypeEnum.S_PW_ENTER.getValue(),
                        FileStepEnum.S_ENTER_TEAM.getValue(), list);
            }
        } else {
            if ((pwEnter.getEteam() != null) && StringUtil.isNotEmpty(pwEnter.getEteam().getId())) {
                delIds.add(pwEnter.getEteam().getId());
            }
            pwEnter.setEteam(null);
        }

        /**
         * 更新选中.
         */
        if (!((peVo.getCursel() != null) && (peVo.getCursel()).equals(pwEnter.getCursel()))) {
            pwEnter.setCursel(peVo.getCursel());
            save(pwEnter);
        }
        return saveEnterApply(pwEnter, peVo.getIsSave());
    }
}
