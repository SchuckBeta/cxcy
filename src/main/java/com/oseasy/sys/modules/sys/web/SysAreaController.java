/**
 *
 */
package com.oseasy.sys.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.Area;
import com.oseasy.com.pcore.modules.sys.service.AreaService;
import com.oseasy.sys.common.config.SysSval;

/**
 * 区域Controller

 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class SysAreaController extends BaseController {
	@Autowired
	private AreaService areaService;

    @RequestMapping(value ="listpage")
    @ResponseBody
    public ApiResult listpage() {
        try {
            List<Area> list = buildTree(areaService.findAll());
            return ApiResult.success(list);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR, ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    public List<Area> buildTree(List<Area> listArea){
        List<Area> list = Lists.newArrayList();
        for(Area area : listArea){

            if("0".equals(area.getParent().getId())){
                area.setLevel(SysSval.XS);
                list.add(area);
            }
            for(Area childArea : listArea){
                if(childArea.getParent().getId().equals(area.getId())){
                    if(area.getChildren() == null){
                        area.setChildren(new ArrayList<Area>());
                    }
                    childArea.setLevel(String.valueOf(Integer.valueOf(area.getLevel())+1));
                    area.getChildren().add(childArea);
                }
            }
        }
        return list;
    }
}
