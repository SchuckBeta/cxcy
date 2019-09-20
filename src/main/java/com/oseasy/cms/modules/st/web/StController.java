/**
 *
 */
package com.oseasy.cms.modules.st.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.cms.modules.cms.entity.*;
import com.oseasy.cms.modules.cms.enums.CategoryModel;
import com.oseasy.cms.modules.cms.enums.CmsIndexManager;
import com.oseasy.cms.modules.cms.service.*;
import com.oseasy.cms.modules.cms.utils.CmsUtils;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.mqserver.common.utils.sms.SMSUtilAlidayu;
import com.oseasy.com.pcore.common.config.CoreIds;
import com.oseasy.com.pcore.common.config.CorePages;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.security.shiro.session.SessionDAO;
import com.oseasy.com.pcore.common.servlet.ValidateCodeServlet;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.com.pcore.modules.syt.entity.SysTenant;
import com.oseasy.com.pcore.modules.syt.service.SysTenantService;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网站Controller
 */
@Controller
@RequestMapping(value = "/st")
public class StController extends BaseController{
	@RequestMapping(value = "")
	public String home() {
		System.out.println(CmsSval.path.vss(CmsSval.StEmskey.ST.k()) + "stIndex");
		return CmsSval.path.vss(CmsSval.StEmskey.ST.k()) + "stIndex";
	}

	@RequestMapping(value = "{app}/indexs")
	public String app(@PathVariable String app) {
		System.out.println(CmsSval.path.vss(CmsSval.StEmskey.ST.k(), app) + app + "Index");
		return CmsSval.path.vss(CmsSval.StEmskey.ST.k(), app) + app + "Index";
	}

	@RequestMapping(value = "{app}/index")
	public String app(@PathVariable String app, @RequestParam(value = "id", required = true) String id) {
		System.out.println(CmsSval.path.vss(CmsSval.StEmskey.ST.k(), app) + app + "Index?id="+id);
		return CmsSval.path.vss(CmsSval.StEmskey.ST.k(), app) + app + "Index?id="+id;
	}
}
