package com.oseasy.pie.modules.impdata.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.common.persistence.DataEntity;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.pie.modules.impdata.service.PwEnterExpService;
import com.oseasy.pw.modules.pw.entity.PwEnter;
import com.oseasy.pw.modules.pw.service.PwEnterService;
import com.oseasy.pw.modules.pw.vo.PwEnterStatus;
import com.oseasy.pw.modules.pw.vo.PwEnterType;
import com.oseasy.pw.modules.pw.web.PwEnterController;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 入驻申报Controller.
 *
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${adminPath}/pw/pwEnter")
public class PiePwEnterController extends BaseController {
	public static final String pwEnterEdit="pw:pwEnter:edit";
	@Autowired
	private PwEnterService pwEnterService;
	@Autowired
	private PwEnterExpService pwEnterExpService;


    @RequestMapping(value = "ajaxCompanyExp")
    public void ajaxCompanyExp(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            PwEnterController.queryList(pwEnter, pag);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.START_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            pwEnter.setType(PwEnterType.PET_QY.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByQuery());
            pwEnterExpService.expCompany(pwEnter, pwEnterService.findQueryListByGroup(pwEnter), request, response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @RequestMapping(value = "ajaxTeamExp")
    public void ajaxTeamExp(PwEnter pwEnter, HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            Page<PwEnter> pag = new Page<PwEnter>(request, response);
            PwEnterController.queryList(pwEnter, pag);
            if (StringUtil.isEmpty(pag.getOrderBy())) {
                pag.setOrderBy(PwEnter.START_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + DataEntity.CREATE_DATE + StringUtil.KGE + Page.ORDER_DESC + StringUtil.DOTH + StringUtil.KGE + PwEnter.TYPE);
                pag.setOrderByType(Page.ORDER_DESC);
            }
            pwEnter.setType(PwEnterType.PET_TEAM.getKey());
            pwEnter.setPstatus(PwEnterStatus.getKeyByQuery());
            pwEnterExpService.expTeam(pwEnter, pwEnterService.findQueryListByGroup(pwEnter), request, response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}