/**
 * 
 */
package com.oseasy.com.mqserver.common.utils;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.oseasy.com.pcore.common.config.CoreSval.Prop;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 发送电子邮件
 */
public class SendMailUtil {

	private static final String from = Prop.getEmailConfig("email.fromAddr");
	private static final String fromName = Prop.getEmailConfig("email.fromName");
	private static final String charSet = "utf-8";
	private static final String username = Prop.getEmailConfig("email.account");
	private static final String password = Prop.getEmailConfig("email.passw");
	private static final String host = Prop.getEmailConfig("email.host");
	private static final String smtpPort = Prop.getEmailConfig("email.port");


	/**
	 * 发送模板邮件
	 * 
	 * @param toMailAddr
	 *            收信人地址
	 * @param subject
	 *            email主题
	 * @param templatePath
	 *            模板地址
	 * @param map
	 *            模板map
	 * @throws Exception 
	 */
	public static void sendFtlMail(String toMailAddr, String subject,
			String templateUrl, Map<String, Object> map,List<EmailAttachment> eas) throws Exception {
		Template template = null;
		Configuration freeMarkerConfig = null;
		HtmlEmail hemail = new HtmlEmail();
		Resource resource = new ClassPathResource(templateUrl);
		String filepath=resource.getURL().getPath();
		String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
		String templatePath = filepath.substring(0, filepath.lastIndexOf("/") + 1);
		hemail.setHostName(host);
		hemail.setSmtpPort(Integer.parseInt(smtpPort));
		hemail.setCharset(charSet);
		hemail.addTo(toMailAddr);
		hemail.setFrom(from, fromName);
		hemail.setAuthentication(username, password);
		hemail.setSubject(subject);
		freeMarkerConfig = new Configuration();
		freeMarkerConfig.setDirectoryForTemplateLoading(new File(templatePath));
		// 获取模板
		template = freeMarkerConfig.getTemplate(filename,new Locale("Zh_cn"), "UTF-8");
		// 模板内容转换为string
		String htmlText = FreeMarkerTemplateUtils
				.processTemplateIntoString(template, map);
		hemail.setMsg(htmlText);
		
		///创建邮件附件可多个   
		if(eas!=null&&eas.size()>0){
			for(EmailAttachment attachment:eas){
				hemail.attach(attachment);
			}
		}
		
		hemail.send();
	}

}