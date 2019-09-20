package com.oseasy.com.fileserver.common.vsftp.core;

import com.oseasy.com.fileserver.common.vsftp.config.Global;
import com.oseasy.com.pcore.modules.sys.vo.TenantConfig;
import com.oseasy.util.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InitializationFtpPath {

  private static Logger logger = LoggerFactory.getLogger( InitializationFtpPath.class);

  public  void initTenantFtpPath(){
    String tenantId = TenantConfig.getCacheTenant();
    if (StringUtil.isNotEmpty(tenantId)) {
      Global.REMOTEPATH = "/"+tenantId+Global.REMOTEPATH  ;
    }
    logger.info("当前上传的路径是：%s",Global.REMOTEPATH);
  }

}
