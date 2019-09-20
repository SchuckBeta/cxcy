/**
 *
 */
package com.oseasy.sys.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotifySent;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.sys.modules.sys.service.SysOaNotifyService;
import com.oseasy.sys.modules.sys.utils.SysConfigUtil;
import com.oseasy.sys.modules.sys.vo.SysConfigVo;
import com.oseasy.sys.modules.sys.vo.TeamConf;

/**
 * 通知通告Controller
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaNotify")
public class SysOaNotifyController extends BaseController {
    @Autowired
    SysOaNotifyService  sysOaNotifyService;

    @RequestMapping(value="unReadOaNotify")
    @ResponseBody
    public List<OaNotifySent> unReadOaNotify(OaNotify oaNotify) {
        return sysOaNotifyService.unRead(oaNotify);
    }

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