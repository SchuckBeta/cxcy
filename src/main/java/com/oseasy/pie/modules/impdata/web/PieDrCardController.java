package com.oseasy.pie.modules.impdata.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pie.modules.expdata.service.DrCardErrorService;
import com.oseasy.pie.modules.impdata.vo.DrCardError;
import com.oseasy.pie.modules.impdata.vo.UserSave;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 门禁卡Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCard")
public class PieDrCardController extends BaseController {
	@Autowired
	private DrCardErrorService drCardErrorService;

    /**
     * 保存临时卡用户.
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSaveUser", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxSaveUser(@RequestBody UserSave user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        ApiTstatus<String> rstatus = new ApiTstatus<String>(false, "保存失败", user.getId());
        if(StringUtil.isEmpty(user.getOffice())){
            rstatus.setMsg("用户所属学院不能为空！");
            rstatus.setStatus(false);
            return rstatus;
        }
        if(StringUtil.isEmpty(user.getProfessional())){
            rstatus.setMsg("用户所属专业不能为空！");
            rstatus.setStatus(false);
            return rstatus;
        }
        if(StringUtil.isEmpty(user.getName())){
            rstatus.setMsg("用户名不能为空！");
            rstatus.setStatus(false);
            return rstatus;
        }
        if(StringUtil.isEmpty(user.getMobile())){
            rstatus.setMsg("用户手机号不能为空！");
            rstatus.setStatus(false);
            return rstatus;
        }

        DrCardError cardError = UserSave.convertToDrCardError(user);
        if (cardError != null) {
            User curUser = drCardErrorService.saveUser(cardError);
            if(curUser == null){
                return rstatus;
            }
            rstatus.setDatas(curUser.getId());
            rstatus.setMsg("保存成功！");
            rstatus.setStatus(true);
            return rstatus;
        }
        return rstatus;
    }

}