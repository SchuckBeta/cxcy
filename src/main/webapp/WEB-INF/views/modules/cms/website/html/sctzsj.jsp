<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <title>${frontTitle}</title>

    <!--头部导航公用样式-->
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sctzsj.css"/>


</head>

<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0;">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li class="active">双创大赛</li>
    </ol>

    <div class="title_info" style="margin: 0">
        <h3>教育部关于举办第四届中国“互联网+” </h3>
        <h4>大学生创新创业大赛的通知 </h4>
        <p>发布时间：2018-03-09 <span style="margin: 0 15px">来源：中华人民共和国教育部</span> 浏览量：150</p>
    </div>
    <div class="china">
        <img src="${ctxImg}/fourNoticeMatch.jpg"/></div>
</div>
</body>

</html>