/**
 *
 */
package com.oseasy.com.pcore.modules.sys.web.front;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.entity.ActYw;
import com.oseasy.act.modules.actyw.service.ActYwService;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Dict;
import com.oseasy.com.pcore.modules.sys.service.DictService;
import com.oseasy.com.pcore.modules.sys.utils.DictUtils;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 字典Controller

 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/dict")
public class FrontDictController extends BaseController {
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

	@RequestMapping(value="getDictList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Dict> getDictList(String type){
		return DictUtils.getDictList(type);
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
