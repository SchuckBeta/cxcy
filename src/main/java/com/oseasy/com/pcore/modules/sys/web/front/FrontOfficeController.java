package com.oseasy.com.pcore.modules.sys.web.front;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.pcore.modules.sys.service.OfficeService;
import com.oseasy.com.pcore.modules.sys.utils.CoreUtils;
import com.oseasy.com.pcore.modules.sys.utils.OfficeTree;
import com.oseasy.util.common.utils.StringUtil;

@Controller
@RequestMapping(value = "${frontPath}/sys/office")
public class FrontOfficeController  extends BaseController {
	@Autowired
	private OfficeService officeService;


	/**
	 * 获取机构JSON数据。
	 * @param extId 排除的ID
	 * @param type	类型（1：公司；2：部门/小组/其它：3：用户）
	 * @param grade 显示级别
	 * @param response
	 * @return
	 */
	//@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, @RequestParam(required=false) Boolean isAll, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Office> list = officeService.findListFront(isAll);
		for (int i=0; i<list.size(); i++) {
			Office e = list.get(i);
			if ((StringUtil.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
					&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
					&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				map.put("grade", e.getGrade());
				if (type != null && "3".equals(type)) {
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	//@ResponseBody
	@RequestMapping(value = "professionList")
	public String professionList(String officeIds, HttpServletResponse response,Model model) {
		List<Office> professionList = new ArrayList<Office>();
		StringBuffer officeSbuff = new StringBuffer();
		if (StringUtil.isNotBlank(officeIds)) {
			String[] officeIdArrays = officeIds.split(",");
			for (String officeId : officeIdArrays) {
				officeId = officeId.trim();
				officeSbuff.append("'");
				officeSbuff.append(officeId);
				officeSbuff.append("'");
				officeSbuff.append(",");
			}
			//officeIds = officeSbuff.substring(0, officeSbuff.lastIndexOf("',")).substring(1);
			officeIds = officeSbuff.substring(0, officeSbuff.lastIndexOf(","));
		}

		professionList = officeService.findProfessionByParentIds(officeIds);
		logger.info("professionList.size:"+professionList.size());
		model.addAttribute("professionList", professionList);
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "userListTreePublish";
	}

	@RequestMapping(value = "findProfessionals")
	@ResponseBody
	public List<Office> findProfessionals(String parentId) {
		return officeService.findProfessionals(parentId);
	}

	@RequestMapping(value="getOrganizationList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Office> getOrganizationList(){
		return CoreUtils.getOfficeList();
	}

	@RequestMapping(value="getOfficeList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ApiResult getOfficeList(){
		try {
			return ApiResult.success(CoreUtils.getOfficeList());
		}catch (Exception e){
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR) + ":" + e.getMessage());
		}
	}

	@RequestMapping(value="getOfficeTree", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Office> getOfficeTree(){
		return OfficeTree.buildTree(CoreUtils.getOfficeList());
	}

}