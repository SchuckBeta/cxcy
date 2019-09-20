<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <meta charset="utf-8"/>
    <!--头部导航公用样式-->
    <!--公用重置样式文件-->
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/html/gxxmsb.css"/>

</head>
<body>
<div class="container container-ct">
    <div class="title_info text-center" style="margin: 0">
        <h3>关于组织申报2017年度 </h3>
        <h4>大学生创新创业训练计划项目的通知 </h4>
        <p>发布时间：2016-12-16 <!-- 来源：中南民族大学 --> 浏览量：150</p>
    </div>
    <div class="china">
        <img class="img-responsive" style="margin: 0 auto" src="${ctxImg}/gcxmtz.jpg">
    </div>
    <div id="title">
        <h3>相关推荐</h3>
    </div>
    <div id="info">
        <ul>
            <li><img src="${ctxImg}/dsrd.jpg"></li>
            <li><img src="${ctxImg}/dsrd01.jpg"></li>
            <li><img src="${ctxImg}/dsrdo2.jpg"></li>
            <li><img src="${ctxImg}/dsrd03.jpg"></li>
        </ul>
    </div>
</div>
<%--<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>--%>
</body>

</html>