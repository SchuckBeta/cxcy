package com.oseasy.dr.modules.dr.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrInoutRecord;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DrInoutRecordService;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;
import com.oseasy.util.common.utils.DateUtil;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.exception.ExceptionUtil;

/**
 * 门禁卡出入记录Controller.
 * @author 奔波儿灞
 * @version 2018-04-08
 */
@Controller
public class DrInoutRecordController extends BaseController {

	@Autowired
	private DrInoutRecordService drInoutRecordService;

	@ModelAttribute
	public DrInoutRecord get(@RequestParam(required=false) String id) {
		DrInoutRecord entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drInoutRecordService.get(id);
		}
		if (entity == null){
			entity = new DrInoutRecord();
		}
		return entity;
	}

	@RequiresPermissions("dr:drInoutRecord:view")
	@RequestMapping(value = "${adminPath}/dr/drInoutRecord/list")
	public String list(DrInoutRecordVo vo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrInoutRecordVo> page = drInoutRecordService.findPage(new Page<DrInoutRecordVo>(request, response), vo);
		model.addAttribute("page", page);
		if(StringUtil.isEmpty(vo.getQryType())){
			vo.setQryType("1");
		}
		model.addAttribute("vo", vo);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardInoutRecordList";
	}

    @ResponseBody
    @RequestMapping(value = "${adminPath}/dr/drInoutRecord/ajaxUpdate")
    public ApiTstatus<String> ajaxUpdate() {
        try {
            Date yesDay=DateUtil.addDays(new Date(), 0);
            DrUtils.disposeDrCardRecord(DateUtil.formatDate(yesDay, "yyyy-MM-dd"));
            return new ApiTstatus<String>(true, "更新成功！");
        } catch (Exception e) {
            logger.error("处理出入记录出错:"+ExceptionUtil.getStackTrace(e));
            return new ApiTstatus<String>(false, "更新失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "${adminPath}/dr/drInoutRecord/ajaxUpdateByGid/{gid}")
    public ApiTstatus<String> ajaxUpdateByGid(@PathVariable("gid") String gid) {
        try {
            Date yesDay=DateUtil.addDays(new Date(), 0);
            DrUtils.disposeDrCardRecord(DateUtil.formatDate(yesDay, "yyyy-MM-dd"));
            return new ApiTstatus<String>(true, "更新成功！");
        } catch (Exception e) {
            logger.error("处理出入记录出错:"+ExceptionUtil.getStackTrace(e));
            return new ApiTstatus<String>(false, "更新失败！");
        }
    }
}