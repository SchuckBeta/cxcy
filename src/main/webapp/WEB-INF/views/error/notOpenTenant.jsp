<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="${ctxCommon}/common-css/common.css"/>
    <link rel="stylesheet" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <!--名师讲堂样式文件-->
    <link rel="stylesheet" href="${ctxCss}/msStyle.css"/>
    <link rel="stylesheet" href="${ctxOther}/icofont/iconfont.css">
    <link rel="stylesheet" href="${ctxOther}/pages/jquery.page.css">

    <title>暂未开通</title>
    <style>
        .jurisdiction-box{
            position: absolute;
            top:50%;
            left: 50%;
            margin-left: -200px;
            margin-top: -200px;
            width: 400px;
            height: 400px;
            background: url('/images/jurisdiction.png') no-repeat center;
        }
    </style>
</head>

<body>
    <div class="container" style="height: 100%;position: relative;min-height: 600px;">
        <div class="jurisdiction-box">
            <div class="text-center" style="padding-top: 190px">
                <p class="text-right" style="display: inline-block;font-size: 24px;font-weight: bold;color: #b5b5b5;">
                    暂未开通，请联系管理员
                </p>
            </div>
        </div>
    </div>
</body>

</html>
