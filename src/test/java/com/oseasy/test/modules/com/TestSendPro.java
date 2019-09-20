package com.oseasy.test.modules.com;


import com.oseasy.com.pcore.common.config.CoreSval;
import com.oseasy.com.pcore.modules.sys.dao.OfficeDao;
import com.oseasy.com.pcore.modules.sys.entity.Office;
import com.oseasy.com.rediserver.common.utils.JedisUtils;
import com.oseasy.test.common.BaseTest;
import com.oseasy.util.common.utils.StringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.oseasy.com.pcore.modules.sys.utils.CoreUtils.CACHE_OFFICE_LIST;

/**
 * Created by Administrator on 2019/4/17 0017.
 */
public class TestSendPro extends BaseTest {

    String tenant= "tenant";

}
