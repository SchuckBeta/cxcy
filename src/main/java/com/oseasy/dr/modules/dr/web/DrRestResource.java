package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.dr.modules.dr.vo.DrAuth;

@RestController
@RequestMapping(value = "${adminPath}/dr/")
public class DrRestResource {
    /****************************************************************************************************************
     * 新修改的接口.
     ***************************************************************************************************************/
    /**
     * 获取门禁特权类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxDrAuths", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxDrAuths() {
        return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(DrAuth.values())).toString());
    }
}
