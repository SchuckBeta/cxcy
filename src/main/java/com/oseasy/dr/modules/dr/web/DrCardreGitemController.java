package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.mapper.JsonMapper;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.utils.IdGen;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.dr.common.config.DrSval;
import com.oseasy.dr.common.config.DrSval.DrEmskey;
import com.oseasy.dr.modules.dr.entity.DrCardreGitem;
import com.oseasy.dr.modules.dr.entity.DrCardreGroup;
import com.oseasy.dr.modules.dr.service.DrCardreGitemService;
import com.oseasy.dr.modules.dr.service.DrCardreGroupService;
import com.oseasy.dr.modules.dr.service.DrPwEnterService;
import com.oseasy.dr.modules.dr.vo.PwSpace;
import com.oseasy.dr.modules.dr.vo.PwSpaceGitem;
import com.oseasy.util.common.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 卡记录规则明细Controller.
 * @author chenh
 * @version 2018-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/dr/drCardreGitem")
public class DrCardreGitemController extends BaseController {

	@Autowired
	private DrCardreGitemService drCardreGitemService;
	@Autowired
	private DrCardreGroupService drCardreGroupService;
	@Autowired
	private DrPwEnterService drPwEnterService;

	@ModelAttribute
	public DrCardreGitem get(@RequestParam(required=false) String id) {
		DrCardreGitem entity = null;
		if (StringUtil.isNotBlank(id)){
			entity = drCardreGitemService.get(id);
		}
		if (entity == null){
			entity = new DrCardreGitem();
		}
		return entity;
	}

	@RequiresPermissions("dr:drCardreGitem:view")
	@RequestMapping(value = {"list", ""})
	public String list(DrCardreGitem drCardreGitem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DrCardreGitem> page = drCardreGitemService.findPage(new Page<DrCardreGitem>(request, response), drCardreGitem);
		model.addAttribute("page", page);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGitemList";
	}

	@RequiresPermissions("dr:drCardreGitem:view")
	@RequestMapping(value = "form")
	public String form(DrCardreGitem drCardreGitem, Model model) {
		model.addAttribute("drCardreGitem", drCardreGitem);
		return DrSval.path.vms(DrEmskey.DR.k()) + "drCardreGitemForm";
	}

	@RequiresPermissions("dr:drCardreGitem:edit")
	@RequestMapping(value = "save")
	public String save(DrCardreGitem drCardreGitem, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, drCardreGitem)){
			return form(drCardreGitem, model);
		}
		drCardreGitemService.save(drCardreGitem);
		addMessage(redirectAttributes, "保存卡记录规则明细成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGitem/?repage";
	}

	@RequiresPermissions("dr:drCardreGitem:edit")
	@RequestMapping(value = "delete")
	public String delete(DrCardreGitem drCardreGitem, RedirectAttributes redirectAttributes) {
		drCardreGitemService.delete(drCardreGitem);
		addMessage(redirectAttributes, "删除卡记录规则明细成功");
		return CoreSval.REDIRECT+CoreSval.getAdminPath()+"/dr/drCardreGitem/?repage";
	}

    /**
     * 获取规则组明细信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGitemsByGid/{gid}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxGitemsByGid(@PathVariable("gid") String gid, HttpServletRequest request, HttpServletResponse response) {
        DrCardreGroup group = new DrCardreGroup(gid);
        group.setDrCreGitems(drCardreGitemService.findList(new DrCardreGitem(group)));
        return new ApiTstatus<DrCardreGroup>(true, "查询成功", group);
    }

    /**
     * 获取规则组明细信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxGitemsSelByGid/{gid}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<PwSpaceGitem>> ajaxGitemsByGid2(@PathVariable("gid") String gid, HttpServletRequest request, HttpServletResponse response) {
        return new ApiTstatus<List<PwSpaceGitem>>(true, "查询成功", drPwEnterService.getPwSpaceGitem(gid));
    }


	@ResponseBody
	@RequestMapping(value = "/ajaxPwSpaceList", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public ApiTstatus<List<PwSpace>> ajaxPwSpaceList(HttpServletRequest request, HttpServletResponse response) {
//        DrCardreGitem pdGitem = new DrCardreGitem(new DrCardreGroup(gid));
//        return new ActYwRstatus<List<DrCardreGitem>>(true, "查询成功", drCardreGitemService.findList(pdGitem));
		return new ApiTstatus<List<PwSpace>>(true, "查询成功",   drPwEnterService.getPwSpaceList());
	}

    /**
     * 保存修改规则组明细信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSavePl", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<DrCardreGroup> ajaxSavePl(@RequestBody JSONObject group, HttpServletRequest request, HttpServletResponse response) {
        DrCardreGroup drCardreGroup = (DrCardreGroup) JsonMapper.fromJsonString(group.toString(), DrCardreGroup.class);
        drCardreGitemService.deleteWLPLByGid(drCardreGroup.getId());
        if((drCardreGroup == null) || StringUtil.isEmpty(drCardreGroup.getId())){
            return new ApiTstatus<DrCardreGroup>(false, "规则参数不能为空（ID）");
        }
        if(StringUtil.checkEmpty(drCardreGroup.getDrCreGitems())){
            return new ApiTstatus<DrCardreGroup>(false, "至少要有一个授权");
        }
        List<DrCardreGitem> errGitems = Lists.newArrayList();
        for (DrCardreGitem gitem : drCardreGroup.getDrCreGitems()) {
            if(StringUtil.isEmpty(gitem.getId())){
                errGitems.add(gitem);
                gitem.setId(IdGen.uuid());
            }
        }

        if(StringUtil.checkNotEmpty(errGitems)){
            drCardreGroup.setDrCreGitems(errGitems);
            drCardreGitemService.savePl(drCardreGroup.getDrCreGitems());
            return new ApiTstatus<DrCardreGroup>(true, "规则组详情ID不能为空，后台自动生成了ID！", drCardreGroup);
        }

        drCardreGitemService.savePl(drCardreGroup.getDrCreGitems());
        return new ApiTstatus<DrCardreGroup>(true, "保存完成", drCardreGroupService.getByg(drCardreGroup.getId()));
    }

    /**
     * 根据IDs删除规则组明细信息.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDelete", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public ApiTstatus<List<String>> ajaxDelete(String ids, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtil.isEmpty(ids)){
            return new ApiTstatus<List<String>>(false, "规则参数不能为空（IDS）");
        }
        List<String> idList = Arrays.asList(StringUtil.split(ids));
        drCardreGitemService.deleteWLPLByIds(idList);
        return new ApiTstatus<List<String>>(true, "保存完成", idList);
    }


    public static void main(String[] args) {
        //String json = "{\"id\":\"1\",\"drCreGitems\":[{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"9e6d49911def47d6b6a043869b851b30\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"83e3be036cf045efac93b58299556fba\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"0433048aad924d9a89873f9621049164\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"b121cee7a75740668e1e4b2ba3ceaaaa\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"f13f0d371e82426bacbf13041e377ca8\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"0b7689b6cd4f4237aef1aa198658f2c8\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"f13f0d371e82426bacbf13041e377ca8\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"ac459d2fc3b840c18e1b68c7c65b34ab\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"e8f99e27976a4b5b885ca4a1496bfe53\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"e8f99e27976a4b5b885ca4a1496bfe53\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"eb994c919121475a83a9674eadf7803a\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"eb994c919121475a83a9674eadf7803a\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"fe131add868b44f4a0739c96c1f5f627\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"fe131add868b44f4a0739c96c1f5f627\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"2b8c1216191c4d88acf5e6ddbefc0395\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"cbf5be794d4848ff81dc92fa0871670e\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"3703b955f466489787784713d3565c68\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"26aaa0d3994f45dd93c7c119b7ed75cd\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"e0cdf9c4e6a4414480f158fcdfbe7c51\"}},{\"group\":{\"id\":\"1\"},\"erspace\":{\"id\":\"3813493b2ab34e81ae634dc811d4a8cb\"}}]}";
        String json2 = "{\"id\":\"0\",\"drCreGtimes\":[{\"beginDate\":\"2018-05-15\",\"endDate\":\"2018-05-18\"}]}";
        DrCardreGroup ss =  (DrCardreGroup) JsonMapper.fromJsonString(json2, DrCardreGroup.class);
        System.out.println(ss);
    }
}