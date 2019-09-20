package com.oseasy.dr.modules.dr.web;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oseasy.com.common.config.ApiTstatus;
import com.oseasy.com.pcore.modules.sys.vo.CorePropType;

@RestController
@RequestMapping(value = "${adminPath}/sys/")
public class SysRestResource {
    /****************************************************************************************************************
     * 新修改的接口.
     ***************************************************************************************************************/
    /**
     * 获取门禁特权类型.
     * @return ActYwRstatus
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxSysPropTypes", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public ApiTstatus<String> ajaxSysPropTypes() {
        return new ApiTstatus<String>(true, "查询成功！", (Arrays.asList(CorePropType.values())).toString());
    }
}
