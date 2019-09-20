package com.oseasy.pro.modules.cert.utils;

import com.oseasy.com.pcore.common.config.CoreSval.Prop;

public class SysCertParam {
	public static final String Email_isSend = Prop.getEmailConfig("email.isSend");
	public static final String TemplatePath = "/templates/modules/email/cert.ftl";
    public static final String FTP_FOLDER = "syscert";
}
