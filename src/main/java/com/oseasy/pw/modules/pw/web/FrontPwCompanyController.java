package com.oseasy.pw.modules.pw.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.pw.modules.pw.entity.PwCompany;
import com.oseasy.pw.modules.pw.service.PwCompanyService;

/**
 * 入驻企业Controller.
 * @author chenh
 * @version 2017-11-26
 */
@Controller
@RequestMapping(value = "${frontPath}/pw/pwCompany")
public class FrontPwCompanyController extends BaseController {

	@Autowired
	private PwCompanyService pwCompanyService;

  /**
   * 获取入驻企业.
   * @param uid 创建人ID
   * @return ActYwRstatus
   */
  @ResponseBody
  @RequestMapping(value = "ajaxPwCompany/{uid}")
  public ApiTstatus<List<PwCompany>> ajaxPwCompany(@PathVariable(value = "uid") String uid) {
    PwCompany pwCompany = new PwCompany();
    pwCompany.setCreateBy(new User(uid));
    List<PwCompany> pwCompanys = pwCompanyService.findList(pwCompany);
    if((pwCompanys != null) && (pwCompanys.size() > 0)){
      return new ApiTstatus<List<PwCompany>>(true, "查询成功！", pwCompanys);
    }else{
      return new ApiTstatus<List<PwCompany>>(true, "查询结果为空！");
    }
  }
}