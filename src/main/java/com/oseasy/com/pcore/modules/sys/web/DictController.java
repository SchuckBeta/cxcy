/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.common.config.CoreSval.CoreEmskey;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.util.common.utils.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典Controller

 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/dict")
public class DictController extends BaseController {
	@Autowired
	private DictService dictService;

	@Autowired
	private ActYwService actYwService;

	@ModelAttribute
	public Dict get(@RequestParam(required=false) String id) {
		if (StringUtil.isNotBlank(id)) {
			return dictService.get(id);
		}else{
			return new Dict();
		}
	}
	/*修改字典*/

	@RequestMapping(value = "edtDict")
	@ResponseBody
	public JSONObject edtDict(HttpServletRequest request) {
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		String sort=request.getParameter("sort");
		return dictService.edtDict(id,name,sort);
	}

	//批量修改
	@RequestMapping(value = "plEditDict" ,method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult plEditDict(@RequestBody List<Dict> dictList) {
		for(Dict dict:dictList){
			if("10008".equals(dict.getId())){
				dict.setId("116");
			}
			if("10009".equals(dict.getId())){
				dict.setId("115");
			}
			if("10010".equals(dict.getId())){
				dict.setId("117");
			}
			dictService.edtBackDoorDict(dict);
		}
		return ApiResult.success();
	}

	/*修改字典类型*/

	@RequestMapping(value = "edtDictType")
	@ResponseBody
	public JSONObject edtDictType(HttpServletRequest request) {
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		return dictService.edtDictType(id,name);
	}

	/*获取字典分页数据*/

	@RequestMapping(value = "getDictPagePlus")
	@ResponseBody
	public Page<Map<String,String>> getDictPagePlus(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("typeid", request.getParameter("typeid"));
		param.put("name", request.getParameter("name"));
		Page<Map<String,String>> page = dictService.getDictPagePlus(new Page<Map<String,String>>(request, response), param);
		return page;
	}
	/*获取全部字典类型*/

	@RequestMapping(value = "getDictTypeListPlus")
	@ResponseBody
	public List<Map<String, String>> getDictTypeListPlus() {
		return dictService.getDictTypeListPlus();
	}


	@RequestMapping(value = "modifyOldData")
	@ResponseBody
	public String modifyOldData() {
		dictService.modifyOldData();
		return "1";
	}

	@RequestMapping(value = {"listPlus"})
	public String listPlus() {
		return CoreSval.path.vms(CoreEmskey.SYS.k()) + "dictListPlus";
	}

	@RequestMapping(value="getDictList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Dict> getDictList(String type){
		return DictUtils.getDictList(type);
	}

	@RequestMapping(value="ajaxSaveDictToAllTenant", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ApiResult ajaxSaveDictToAllTenant(@RequestBody Dict dict){
		if(StringUtil.checkNotEmpty(dict.getTenantIds())){
			for(String tenantId : dict.getTenantIds()){
				Dict query = new Dict();
				query.setTenantId(tenantId);
				query.setType(dict.getType());
				Dict dictParent = dictService.findDictByValue(query);
				Dict dictTemp = new Dict(dict.getValue(), dict.getLabel(), dict.getType(), dict.getDescription(), dictParent.getId(), dict.getSort(),dict.getIsSys(), dict.getTenantIds());
				dictTemp.setTenantId(tenantId);
				dictService.save(dictTemp);
			}
			return ApiResult.success();
		}else{
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR));
		}
	}

	/**
	 大赛/项目类别接口
	 */
	@RequestMapping(value="getProjectCategory")
	@ResponseBody
	public ApiResult getProjectCategory(String actywId){
		try{
			ActYw actYw = actYwService.get(actywId);
			List<Dict> dists = Lists.newArrayList();
			if(actYw != null){
				List<Dict> dictList = Lists.newArrayList();
				if(actYw.getProProject().getProType().contains("1,")){
					dictList = DictUtils.getDictList("project_type");
				}else if(actYw.getProProject().getProType().contains("7,")){
					dictList = DictUtils.getDictList("competition_net_type");
				}
				String[] categoryList = actYw.getProProject().getProCategory().split(",");
				for(Dict dict : dictList){
					for(String category : categoryList){
						if(dict.getValue().equals(category)){
							dists.add(dict);
						}
					}
				}
			}
			return ApiResult.success(dists);
		}catch (Exception e){
			logger.error(e.getMessage());
			return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
		}
	}

}
