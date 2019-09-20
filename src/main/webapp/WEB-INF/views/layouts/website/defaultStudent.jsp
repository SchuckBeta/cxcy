<%@page import="com.oseasy.com.pcore.modules.sys.entity.User" %>
<%@page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%request.setAttribute("user", UserUtils.getUser());%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <title><sitemesh:title/></title>
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css"  href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/dialog/alert-dialog.css">
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/pagination.css">


    <!--无用css-->
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>--%>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/commonStudent.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css"/>
    <!--focus样式表-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/focustyle.css"/>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>



    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxJs}/template.js" type="text/javascript"></script>
    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <script src="${ctxCommon}/common-js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
    <!--文本溢出-->
    <script type="text/javascript" src="${ctxJs}/team/mock-min.js"></script>
    <script src="${ctxStatic}/common/initiate.js" type="text/javascript"></script>
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script>
        var ftpHttp= '${ftpHttp}';
        var webMaxUpFileSize = "${fns:getMaxUpFileSize()}";
    </script>
    <sitemesh:head/>
</head>

<body>
<%@ include file="headerNew.jsp" %>
<div id="content">
    <sitemesh:body/>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>