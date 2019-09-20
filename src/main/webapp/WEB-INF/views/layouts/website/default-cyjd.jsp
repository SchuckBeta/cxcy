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
    <link rel="stylesheet" href="${ctxCss}/common/common.bundle.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/jquery-ui.css?v=1"/>
    <%--<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>--%>
    <!--头部导航公用样式-->
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>--%>
    <link rel="stylesheet" href="${ctxCss}/common/headerFooter.bundle.css">

    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/commonCyjd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q11">

    <!--focus样式表-->


    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <!--文本溢出-->
    <script src="${ctxJs}/common.js?v=1" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/frontCommon.js?v=21111" type="text/javascript"></script>
    <script>
        var ftpHttp = '${fns:ftpHttpUrl()}';
        var webMaxUpFileSize = "${fns:getMaxUpFileSize()}";
        var $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <style>
        .white-space-pre{
            white-space: pre;
            white-space: pre-line;
            word-wrap: break-word;
        }
    </style>
    <sitemesh:head/>
</head>

<body>
<%@ include file="headerNew.jsp" %>
<div id="content">
    <sitemesh:body/>
</div>
<%@ include file="footer.jsp" %>
<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
</body>
</html>