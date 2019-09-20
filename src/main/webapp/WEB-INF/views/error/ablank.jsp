<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${msg}</title>
    <meta name="decorator" content="backPath_header"/>
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
                ${msg}
                <a style="display: block;text-align: right;font-size: 14px;line-height: 48px;color: #959595;font-weight: normal" href="javascript:void (0);" onclick="history.go(-1);">
                    返回&gt;&gt;
                </a>
            </p>
        </div>
    </div>
</div>

</body>
</html>
