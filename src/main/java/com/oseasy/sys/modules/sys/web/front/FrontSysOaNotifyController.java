/**
 *
 */
package com.oseasy.sys.modules.sys.web.front;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.sys.modules.sys.service.SysOaNotifyService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeamConf;

/**
 * 通知通告Controller
 *
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${frontPath}/oa/oaNotify")
public class FrontSysOaNotifyController extends BaseController {
    @Autowired
    private SysOaNotifyService sysOaNotifyService;

    @RequestMapping(value="unFootReadOaNotify")
    @ResponseBody
    public Map<String ,Object> unFootReadOaNotify(OaNotify oaNotify) {
        Map<String ,Object> map =new HashMap<>();

        map.put("list", sysOaNotifyService.unRead(oaNotify));
        SysConfigVo scv = SysConfigUtil.getSysConfigVo();
        //判断团队邀请是否需要审核
        if (scv != null) {
            TeamConf tc = scv.getTeamConf();
            if (tc != null) {
                //邀请加入团队限制 1-有限制，0-无限制
                map.put("inOnOff",tc.getInvitationOnOff());
            }
        }

        return map;
    }
}