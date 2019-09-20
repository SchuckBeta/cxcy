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
	<c:if test="${empty msg}">
	Hello,[${ruser.casUser.uid}]! Welcome ${ruser.casUser.rcname} &nbsp; <a href="${ctx }/cas/caslogout">登出</a>
	</c:if>
	<c:if test="${not empty msg}">
	出错啦！原因：${msg}
	</c:if>
	</div>
</body>
</html>