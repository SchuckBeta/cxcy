package com.oseasy.cms.modules.cms.utils;

import java.util.List;

import com.oseasy.com.fileserver.common.utils.FtpUtil;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FreemarkerGetFtpUrl implements TemplateMethodModelEx {

	@Override
	public Object exec(List args) throws TemplateModelException {
		String permissionsStr = args.get(0).toString();
		return FtpUtil.ftpImgUrl(permissionsStr);
	}
}
