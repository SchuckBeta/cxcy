<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/silibs/corelib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="sites"/>
    <style>

    </style>
</head>
<body>
<c:forEach var="i" items="1,2,3,4,5,6">
    <div style="background-color:#333; display: inline-block; width:100px; height: 100px; line-height: 100px; text-align: center;">
        <a href="${ct}/si" style="color:#fff;">站点模板${i}</a>
    </div>
</c:forEach>
</body>
</html>