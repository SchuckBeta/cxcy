<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <%
		session.invalidate();
		String casLogoutURL = "http://authserver.uta.edu.cn/authserver/logout";
		String redirectURL= casLogoutURL+"?service="+URLEncoder.encode("http://127.0.0.1:8093/f/cas/caslogin");
		response.sendRedirect(redirectURL);
	%>
</head>
<body>
	<div style="text-align: center; padding: 100px; line-height: 50px;">
		Logout Success!!!
	</div>
</body>
</html>