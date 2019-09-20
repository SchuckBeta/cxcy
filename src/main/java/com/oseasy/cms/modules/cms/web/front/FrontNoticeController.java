package com.oseasy.cms.modules.cms.web.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oseasy.cms.common.config.CmsSval;
import com.oseasy.cms.common.config.CmsSval.CmsEmskey;
import com.oseasy.com.common.config.ApiConst;
import com.oseasy.com.common.config.ApiResult;
import com.oseasy.com.fileserver.common.utils.FtpUtil;
import com.oseasy.com.mqserver.modules.oa.entity.OaNotify;
import com.oseasy.com.mqserver.modules.oa.service.OaNotifyService;
import com.oseasy.com.mqserver.modules.oa.utils.OaUtils;
import com.oseasy.com.pcore.common.persistence.Page;
import com.oseasy.com.pcore.common.web.BaseController;
import com.oseasy.util.common.utils.StringUtil;
/**
 * Created by zhangzheng on 2017/6/21.
 */
@Controller
@RequestMapping(value = "${frontPath}/frontNotice")
public class FrontNoticeController extends BaseController {
    @Autowired
    private OaNotifyService oaNotifyService;


    @RequestMapping(value="noticeView")
    public String noticeView(String id, Model model) {
        //根据id得到notice对象
        if (StringUtil.isNotBlank(id)) {
            OaNotify  oaNotify = oaNotifyService.get(id);
            if(oaNotify==null){
                return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/noticeView";
            }
            String title = oaNotify.getTitle();
            oaNotify.setContent(OaUtils.convertFront(oaNotify.getContent()));
            String content=StringUtil.unescapeHtml3(oaNotify.getContent());
            content = content.replaceAll(FtpUtil.FTP_MARKER,FtpUtil.FTP_HTTPURL);
            model.addAttribute("title",title);
            model.addAttribute("content",content);
            model.addAttribute("oaNotify", oaNotify);
        }

        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/noticeView";
    }

    @RequestMapping(value="noticeList")
    public String noticeList( HttpServletRequest request, HttpServletResponse response,Model model) {
        //查询通知列表
//        List<OaNotify> list = oaNotifyService.loginList(100);
//        model.addAttribute("list",list);
        Page<OaNotify> pageForSearch =new Page<OaNotify>(request, response);
        Page<OaNotify> page =  oaNotifyService.findLoginPage(pageForSearch,new OaNotify());
        model.addAttribute("page",page);
        return CmsSval.path.vms(CmsEmskey.WEBSITE.k()) + "pages/noticeList";
    }


    @RequestMapping(value="getNoticeList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ApiResult getNoticeList(HttpServletRequest request, HttpServletResponse response,Model model){
        try {
            Page<OaNotify> pageForSearch =new Page<OaNotify>(request, response);
            Page<OaNotify> page =  oaNotifyService.findLoginPage(pageForSearch,new OaNotify());
            return ApiResult.success(page);
        }catch (Exception e){
            return ApiResult.failed(ApiConst.CODE_INNER_ERROR,ApiConst.getErrMsg(ApiConst.CODE_INNER_ERROR)+":"+e.getMessage());
        }
    }

    //前台首页我的通知跑马灯
    @RequestMapping(value ="frontList")
    @ResponseBody
    public List<Map<String,String>> frontList() {
        List<OaNotify> list = oaNotifyService.loginList(5);
        List<Map<String,String>> rList = new ArrayList<Map<String,String>>();
        for (int i = 0; i < list.size(); i++) {
            if (StringUtil.isNotBlank(list.get(i).getContent())&&StringUtil.isNotBlank(list.get(i).getTitle())) {
                Map<String,String> psm=new HashMap<String,String>();
//                String notifys = list.get(i).getTitle()+":"+StringUtil.replaceEscapeHtml(list.get(i).getContent());
                String notifys = list.get(i).getTitle();
                psm.put("title",  list.get(i).getTitle());
                String  titleName=StringUtil.abbr(notifys,85);
                psm.put("titleName",titleName);
                psm.put("id",list.get(i).getId());
                rList.add(psm);
            }
        }
        return rList;
    }
}
