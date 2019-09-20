package com.oseasy.cms.modules.cms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.com.pcore.modules.sys.entity.User;
import com.oseasy.com.pcore.modules.sys.utils.UserUtils;
import com.oseasy.pro.modules.interactive.entity.SysLikes;
import com.oseasy.pro.modules.interactive.service.SysLikesService;
import com.oseasy.pro.modules.interactive.service.SysViewsService;
import com.oseasy.util.common.utils.StringUtil;

/**
 * 学生信息表Controller
 *
 * @author zy
 * @version 2017-03-27
 */
@Controller
@RequestMapping(value = "${frontPath}/sys/frontStudentExpansion")
public class FrontCmsStudentExpansionController extends BaseController {
    @Autowired
    SysViewsService sysViewsService;
    @Autowired
    SysLikesService sysLikesService;

    /**检查当前用户对foreignId是否点过赞
     * @param userId
     * @return
     */
    @RequestMapping(value = "checkedIsLiked", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult checkedIsLiked(String userId) {
        User user=UserUtils.getUser();
        if (StringUtil.isEmpty(user.getId())) {
            return ApiResult.success(true);
        }
        if (user.getId().equals(userId)) {
            return ApiResult.success(true);
        }
        SysLikes sc=new SysLikes();
        sc.setUserId(user.getId());
        sc.setForeignId(userId);
        return ApiResult.success(sysLikesService.getExistsLike(sc)>0);
    }
}