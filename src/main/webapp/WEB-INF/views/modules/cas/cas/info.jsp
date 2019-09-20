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

	<%
	//    cas-client-3.2.1版本集成
		String uid = request.getRemoteUser();
	    String cn = "";
		String user_name = "";
	    Principal principal = request.getUserPrincipal();
	    		if(principal!=null && principal instanceof AttributePrincipal){
	    			AttributePrincipal aPrincipal = (AttributePrincipal)principal;
	    //获取用户信息中公开的Attributes部分
	    			Map<String, Object> map = aPrincipal.getAttributes();
	    			/*Iterator<String> it = map.keySet().iterator();
	    			while (it.hasNext()) {
	    				String k = it.next();
	    				response.getWriter().printf("%s:%s\r\n", k, map.get(k));
	    			}*/
					cn = (String)map.get("cn");
	                user_name = (String)map.get("user_name");
	    		}
	%>
</head>
<body>
	<div style="text-align: center; padding: 100px; line-height: 50px;">
		Hello,${ruser.casUser.ruid }! Welcome ${ruser.casUser.rcname } &nbsp;
			用户的uid: ${ruser.casUser.ruid } <br/>
			界面上操作的姓名cn: ${ruser.casUser.rcname } <br/>
			默认传递的user_name: ${ruser.casUser.rname }<br/>

			可以在更新前和更新后进行对比。<br/>
	</div>
</body>
</html>