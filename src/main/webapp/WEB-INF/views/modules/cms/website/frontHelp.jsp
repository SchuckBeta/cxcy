<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>${frontTitle}</title>
	<!--引用装饰器-->
	<meta name="decorator" content="site-decorator"/>
</head>
<body>
<div class="container">
	<h4 class="text-center" style="margin-top: 60px;">项目申报帮助</h4>
	<h3 class="text-center" style="margin-top: 20px;margin-bottom: 20px; font-size: 20px;">初次登录平台的学生，请用各自的学号+默认密码（123456）登录，登录后请立即修改密码并完善个人信息！</h3>
	<h5 class="text-right">下载文档：<a href="javascript:location.href='/downHelp?fileName='+encodeURI(encodeURI('大创项目申报操作手册.docx'));">大创项目申报操作手册.docx</a></h5>
	<div style="width: 722px; margin: 0 auto 60px;">
		<img class="img-responsive" src="${ctxImages}/applyProjectFlow.png">
	</div>

</div>
</body>
</html>