<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<br/><br/>
		<c:if test="${not empty msg}">
			出错啦！原因：${msg}
		</c:if>
		<br/>
		Hello, Welcome ${ruser.casUser.rcname}.
		<hr/>
		<c:if test="${ruser.retype eq '100'}">
			<a href="${ctx }${root }${ruser.returl}">跳转首页</a>
		</c:if>
		<c:if test="${ruser.retype eq '110'}">
			<a href="${ctx }${ruser.returl}">跳转登录页</a>
		</c:if>
		<c:if test="${ruser.retype eq '120'}">
			<a href="${ctx }${ruser.returl}">跳转注册页</a>
		</c:if>
		<c:if test="${ruser.retype eq '170'}">
			<a href="${ctx }${root }${ruser.returl}">选择用户类型页</a>
		</c:if>
	</div>
</body>
</html>