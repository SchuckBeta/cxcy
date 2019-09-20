package com.oseasy.dr.modules.dr.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCard;
import com.oseasy.dr.modules.dr.entity.DrCardRecord;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.manager.DrUtils;
import com.oseasy.dr.modules.dr.service.DRDeviceService;
import com.oseasy.dr.modules.dr.service.DrCardRecordService;
import com.oseasy.dr.modules.dr.service.DrCardreGroupService;
import com.oseasy.dr.modules.dr.vo.DrCardRecordParam;
import com.oseasy.dr.modules.dr.vo.DrCardRecordShowVo;
import com.oseasy.dr.modules.dr.vo.DrCardRecordWarnVo;
import com.oseasy.dr.modules.dr.vo.DrCardType;
import com.oseasy.dr.modules.dr.vo.DrCstatus;
import com.oseasy.dr.modules.dr.vo.DrInoutRecordVo;
import com.oseasy.dr.modules.dr.vo.GitemEstatus;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 门禁卡记录Controller.
 * @author chenh
 * @version 2018-03-30
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardRecord")
public class DrCardRecordController extends BaseController {
	@Autowired
	private DrCardRecordService drCardRecordService;
    @Autowired
    DRDeviceService deviceService;
    @Autowired
    DrCardreGroupService drCardreGroupService;

	@ModelAttribute
	public DrCardRecord get(@RequestParam(required=false) String id) {
		DrCardRecord entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardRecordService.get(id);
		}
		if (entity == null){
			entity = new DrCardRecord();
		}
		return entity;
	}

    @RequiresPermissions("dr:drCardRecord:view")
    @RequestMapping(value = {"list", ""})
    public String list(DrCardRecordShowVo vo, HttpServletRequest request, HttpServletResponse response, Model model) {
        if((vo.getCard() == null)){
            vo.setCard(new DrCard());
        }
        if(StringUtil.isEmpty(vo.getCard().getCardType())){
            vo.getCard().setCardType(DrCardType.NO_TEMP.getKey());
        }
        Page<DrCardRecordShowVo> page = drCardRecordService.findPage(new Page<DrCardRecordShowVo>(request, response), vo);
        model.addAttribute("page", page);
		model.addAttribute("vo", vo);
        model.addAttribute(DrCardType.DR_CTYPES, DrCardType.getAll());
        model.addAttribute(GitemEstatus.GE_STATUSS, GitemEstatus.getAll());
        return DrSval.path.vms(DrEmskey.DR.k()) + "drCardRecordList";
    }

	@RequiresPermissions("dr:drCardRecord:view")
	@RequestMapping(value = {"warnList"})
	public String warnList(DrCardRecordWarnVo drCardRecordWarnVo, HttpServletRequest request, HttpServletResponse response, Model model) {
	    Page<DrCardRecordWarnVo> page = drCardRecordService.findWarnPage(new Page<DrCardRecordWarnVo>(request, response), drCardRecordWarnVo);
		if(StringUtil.checkNotEmpty(page.getList())){
			List<DrCardRecordWarnVo> drCards = page.getList();

			for (DrCardRecordWarnVo card : drCards) {
				//card.setLastEnterDate(drCardRecordService.getLastEnterTime(card.getId()));

				card.setEnterAllTime(drCardRecordService.getEnterAllTime(card.getId()));
				if(card.getUid()== null){
					continue;
				}
				//card.setPwEnterInfo(pwEnterService.getPwEnterInfo(card.getUid()));
			}
		}
        model.addAttribute(DrCardreGroup.DR_CREGROUP, drCardreGroupService.findList(new DrCardreGroup()));
        model.addAttribute(DrCstatus.DR_CSTATUSS, DrCstatus.getAll());
        model.addAttribute("drCardRecordWarnVo", drCardRecordWarnVo);
		model.addAttribute("page", page);
	    return DrSval.path.vms(DrEmskey.DR.k()) + "drCardRecordWarnList";
	}

    @RequiresPermissions("dr:drCardRecord:view")
    @RequestMapping(value = {"countList", ""})
    public String countList(DrCardRecord drCardRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<DrCardRecord> page = drCardRecordService.findPage(new Page<DrCardRecord>(request, response), drCardRecord);
        model.addAttribute("page", page);
        return DrSval.path.vms(DrEmskey.DR.k()) + "drCardRecordCountList";
    }

	@RequiresPermissions("dr:drCardRecord:view")
	@RequestMapping(value = "form")
	public String form(DrCardRecord drCardRecord, Model model) {
		model.addAttribute("drCardRecord", drCardRecord);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardRecordForm";
	}

	@RequiresPermissions("dr:drCardRecord:edit")
	@RequestMapping(value = "save")
	public String save(DrCardRecord drCardRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardRecord)){
			return form(drCardRecord, model);
		}
		drCardRecordService.save(drCardRecord);
		addMessage(redirectAttributes, "保存门禁卡记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardRecord/?repage";
	}

	@RequiresPermissions("dr:drCardRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCardRecord drCardRecord, RedirectAttributes redirectAttributes) {
		drCardRecordService.delete(drCardRecord);
		addMessage(redirectAttributes, "删除门禁卡记录成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardRecord/?repage";
	}

	/**
	 * 刷卡记录.
	 * @param jo
	 * @param request
	 * @param response
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value = "/ajaxCacheCardRecord", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxCacheCardRecord(@RequestBody JSONObject jo, HttpServletRequest request, HttpServletResponse response) {
        DrCardRecordShowVo vo = (DrCardRecordShowVo) JsonMapper.fromJsonString(jo.toString(), DrCardRecordShowVo.class);
        String uuid = IdGen.uuid();
        DrUtils.putIdrVo(uuid, vo);
        return new ApiTstatus<String>(true, "处理完成", "/dr/drCardRecord/ajaxExportCardRecord?uuid=" + uuid);
    }

    /**
     * 出入记录.
     * @param jo
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCacheInoutRecord", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxCacheInoutRecord(@RequestBody JSONObject jo, HttpServletRequest request, HttpServletResponse response) {
        DrInoutRecordVo vo = (DrInoutRecordVo) JsonMapper.fromJsonString(jo.toString(), DrInoutRecordVo.class);
        String uuid = IdGen.uuid();
        DrUtils.putIdrVo(uuid, vo);
        return new ApiTstatus<String>(true, "处理完成", "/dr/drCardRecord/ajaxExportInoutRecord?uuid=" + uuid);
    }

    /**
     * 预警记录.
     * @param jo
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxCacheWarnRecord", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxCacheWarnRecord(@RequestBody JSONObject jo, HttpServletRequest request, HttpServletResponse response) {
        DrCardRecordWarnVo vo = (DrCardRecordWarnVo) JsonMapper.fromJsonString(jo.toString(), DrCardRecordWarnVo.class);
        String uuid = IdGen.uuid();
        DrUtils.putIdrVo(uuid, vo);
        return new ApiTstatus<String>(true, "处理完成", "/dr/drCardRecord/ajaxExportWarnRecord?uuid=" + uuid);
    }

    @ResponseBody
    @RequestMapping(value = "ajaxUpdate")
    public ApiTstatus<String> ajaxUpdate() {
        if(deviceService.getNewRecords()){
            return new ApiTstatus<String>(true, "更新成功！");
        }else{
            return new ApiTstatus<String>(false, "更新失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxSynch", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxSynch(@RequestBody JSONObject record) {
        DrCardRecordParam drCrParam = (DrCardRecordParam) JsonMapper.fromJsonString(record.toString(), DrCardRecordParam.class);
        if(drCardRecordService.ajaxSynch(drCrParam)){
            return new ApiTstatus<String>(true, "更新成功！");
        }else{
            return new ApiTstatus<String>(false, "更新失败！");
        }
    }
}