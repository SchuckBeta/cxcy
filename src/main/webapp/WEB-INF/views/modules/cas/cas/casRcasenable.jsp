<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.security.Principal" %>
<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%@ page import="java.util.Iterator" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>
	<div style="text-align: center; padding: 100px; line-height: 50px;">
		<br/>
		<br/>
		Hello, Welcome ${ruser.casUser.rcname}, 您的账号被禁止CAS访问了，请前往登录页登录！
		<br/>
		<br/>
		<br/>
		<br/>
	</div>
</body>
</html>