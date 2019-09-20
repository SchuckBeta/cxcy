<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <!--公用重置样式文件-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <style>
        .concat-info{
            margin-bottom: 30px;
        }
        .concat-info>p{
            margin-bottom: 5px;
        }
        .concat-info>p>span{
            display: inline-block;
            width: 64px;
            margin-right: 8px;
            text-align: justify;
        }
        .container-padding{
            padding-top:60px;
            padding-bottom: 60px;
        }
        .letter-title{
            text-align: center;
        }
        .letter-content .title{
            display: block;
            font-size: 16px;
            margin-bottom: 15px;
        }
        .letter-content{
            margin-bottom: 50px;
        }
    </style>
</head>
<body>
<div class="container container-padding">
    <h3 class="letter-title">开创啦</h3>
    <div class="letter-content">
        <span class="title">各创新创业项目团队：</span>
        <p style="text-indent: 28px">为了及时了解入驻工训各创新创业项目研究进展情况，决定近期对各项目进行中期检查。现将有关事项通知如下：</p>
        <p>一、请各团队在3月24日前向指导老师提交《湖南大学入驻工训中心创新创业项目中期检查表》（附件1），重点项目还需要提交《项目阶段性研究报告》（附件2），指导教师检查签字后交创新训练办（工训204办公室）。</p>
        <p>二、由于部分项目人员有变更，请各团队将新增加或退出的学生在检查表“备注”栏中备注。</p>
        <p class="text-right" style="font-size: 16px">请各团队根据通知要求，按时完成入驻工训中心创新创业项目中期检查工作。</p>

    </div>
    <div class="concat-info">
        <p><span>联系人：</span>余老师  </p>
        <p><span>电  话：</span>88664093</p>
        <p><span>邮  箱：</span>30469879@qq.com(备注好项目名称)</p>
    </div>
    <ul class="adjuncts">
        <li><span>附件一：</span><a href="${ctxFront }/page-download-noticedoc?type=1">湖南大学入驻工训中心创新创业项目中期检查表.doc</a></li>
        <li><span>附件二：</span><a href="${ctxFront }/page-download-noticedoc?type=2">重点“项目阶段性研究报告”书写格式（见群通知）</a></li>
    </ul>
    <p class="text-right">现代工程训练中心</p>
    <p class="text-right">2017年3月16日</p>
</div>

<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>
</body>
</html>
