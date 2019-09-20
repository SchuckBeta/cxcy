<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html style="overflow-x:auto;overflow-y:auto;">
<head>
    <meta charset="UTF-8">
	<script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <title><sitemesh:title/></title>
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <sitemesh:head/>
</head>
<body>
<%@ include file="headercontent.jsp" %>
<sitemesh:body/>
</body>
</html>