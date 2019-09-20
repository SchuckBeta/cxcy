/**
 * .
 */

package com.oseasy.pie.modules.impdata.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oseasy.pie.modules.exp.ExpRule;
import com.oseasy.pie.modules.exp.imp.PwEnterExpCompanyVo;
import com.oseasy.pie.modules.exp.imp.PwEnterExpRuleCreate;
import com.oseasy.pie.modules.exp.imp.PwEnterExpTeamVo;
import com.oseasy.pie.modules.exp.imp.PwEnterRparam;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.util.common.utils.StringUtil;

/**
 * .
 * @author chenhao
 */
@Service
@Transactional(readOnly = true)
public class PwEnterExpService {
    public static Logger logger = Logger.getLogger(PwEnterExpService.class);

    /**
     * 查询导出团队.
     * @param request
     * @param response
     */
    public void expTeam(PwEnter pwEnter, List<PwEnter> pwEnters, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(pwEnter.getType())){
            logger.error("入驻类型不能为空！");
        }
        PwEnterExpRuleCreate exprule = new PwEnterExpRuleCreate();
        ExpRule<PwEnterRparam> rule = new ExpRule<PwEnterRparam>(new PwEnterRparam(pwEnter.getType()));
        rule.setRequest(request);
        rule.setResponse(response);
        rule.setClazz(PwEnterExpTeamVo.class);
        rule.setDatas(PwEnterExpTeamVo.converts(pwEnters));
        exprule.run(rule);
    }
    /**
     * 查询导出企业.
     * @param request
     * @param response
     */
    public void expCompany(PwEnter pwEnter, List<PwEnter> pwEnters, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(pwEnter.getType())){
            logger.error("入驻类型不能为空！");
        }
        PwEnterExpRuleCreate exprule = new PwEnterExpRuleCreate();
        ExpRule<PwEnterRparam> rule = new ExpRule<PwEnterRparam>(new PwEnterRparam(pwEnter.getType()));
        rule.setRequest(request);
        rule.setResponse(response);
        rule.setClazz(PwEnterExpCompanyVo.class);
        rule.setDatas(PwEnterExpCompanyVo.converts(pwEnters));
        exprule.run(rule);
    }
}
