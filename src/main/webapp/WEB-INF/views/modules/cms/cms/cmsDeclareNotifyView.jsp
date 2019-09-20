<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <link href="${ctxCss}/video-js.css" rel="stylesheet">
    <!--首页样式表-->
    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css"/>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>
    <!--公用重置样式文件-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q111">
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <style type="text/css">


        .dsrdinfo {
            margin-top: 60px;
        }

        .dsrdinfo h3 {
            text-align: center;
        }

        .dsrdinfo h6 {
            text-align: center;
            margin-top: 34px;
            color: #BCBCBC;
        }

        .more h3 {
            margin-top: 112px;
            border-bottom: 3px solid red;
            width: 100px;
            padding-bottom: 10px;
            float: left;
        }

        .newinfo {
            margin-top: 30px;
        }

        .newinfo ul li {
            padding-top: 20px;
        }

        .newinfo ul li span {
            margin-left: 195px;
        }


    </style>
</head>

<body>
<div class="container">
    <div class="dsrdinfo">
        <%--<div class="edit-bar clearfix" style="margin-top:0;">--%>
            <%--<div class="edit-bar-left">--%>
                <%--<div class="mybreadcrumbs" style="margin:0 0 20px 9px;">--%>
                    <%--<i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;通知--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
        <ol class="breadcrumb" style="margin-top: 0">
            <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
            <li class="active">通知</li>
        </ol>
        <h3>${cmsDeclareNotify.title }</h3>
        <h6>发布时间:<fmt:formatDate value="${cmsDeclareNotify.releaseDate}" pattern="yyyy-MM-dd"/>
            浏览量:${cmsDeclareNotify.views }</h6>
    </div>
    <div style="clear: both;"></div>
    ${cmsDeclareNotify.content }
    <div class="clearfix"></div>
</div>
</body>
</html>