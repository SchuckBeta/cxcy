package com.oseasy.com.rediserver.redis.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oseasy.com.pcore.modules.authorize.web.AuthorizeController;
import com.oseasy.com.pcore.common.utils.license.LicenseCacheUtils;
import com.oseasy.com.rediserver.common.config.RedisrSval;
import com.oseasy.com.rediserver.common.config.RedisrSval.RedisrEmskey;

/**
 * Created by victor on 2017/4/13.
 */
@Controller
@RequestMapping(value = "/a/get")
public class TestehCacheController {
    private static Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @RequestMapping(value = "put")
    public String accredit(Model model) {
        logger.info("------------------------------put");
        String temp1 = (String) LicenseCacheUtils.get("echcahe");
        if (temp1 != null || !"".equals(temp1)) {
            LicenseCacheUtils.remove("echcahe");
        }
        LicenseCacheUtils.put("echcahe", Math.random() + "aa");
        return RedisrSval.path.vms(RedisrEmskey.REDIS.k()) + "/test1";
    }

    @RequestMapping(value = "get")
    public String accredita(Model model) {
        logger.info("------------------------------get");
        String temp = (String) LicenseCacheUtils.get("echcahe");
        logger.info("echche 值:" + temp);
        model.addAttribute("temp", temp);
        return RedisrSval.path.vms(RedisrEmskey.REDIS.k()) + "/test1";
    }
}
