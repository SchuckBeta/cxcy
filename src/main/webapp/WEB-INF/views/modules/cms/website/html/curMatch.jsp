<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>我的大赛</title>
    <meta name="decorator" content="site-decorator"/>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
    <style>
        .match-panel-content .info-content {
            margin-bottom: 20px;
        }

        .match-panel-content .base-info {
            padding-bottom: 20px;
            padding-left: 15px;
            padding-right: 15px;
            margin-bottom: 20px;
            border-bottom: 1px dashed #ddd;
            overflow: hidden;
        }

        .match-panel-content .match-cover {
            float: left;
            position: relative;
            width: 240px;
            height: 150px;
            border: 1px solid #ddd;
            box-sizing: border-box;
            border-radius: 3px;
            overflow: hidden;
        }

        .match-panel-content .match-cover .tip {
            display: none;
            position: absolute;
            left: 0;
            bottom: 0;
            width: 100%;
            height: 28px;
            line-height: 28px;
            color: #fff;
            background-color: rgba(0, 0, 0, .5);
            text-align: center;
        }

        .match-panel-content .match-no-cover:hover .tip {
            display: block;
        }

        .match-panel-content .info-list {
            margin-left: 260px;
            min-height: 150px;
            padding-top: 18px;
            box-sizing: border-box;
            overflow: hidden;
        }

        .match-panel-content .info-list > span {
            display: block;
            float: left;
            text-align: left;
            width: 50%;
            padding-right: 10px;
            margin-bottom: 10px;
            box-sizing: border-box;
        }

        .person-name {
            color: #e9442d;
        }

        .detail-panel {
            margin-bottom: 15px;
        }

        .person-name:hover {
            color: #e9442d;
        }

        .detail-panel .detail-panel-header {
            height: 30px;
            line-height: 30px;
            margin: 0;
            padding-left: 15px;
            background-color: #e7e7e7;
        }

        .detail-panel .detail-panel-header.active {
            background-color: #ffcc99;
        }

        .detail-panel .detail-panel-header.active a {
            color: #e9442d;
            cursor: default;
        }

        .detail-panel .detail-panel-header > a {
            display: block;
            color: #333;
            text-decoration: none;
        }

        .primary-color {
            color: #e9442d;
        }

        .detail-panel .detail-panel-box .inner {
            padding: 15px;
            border: solid #ddd;
            border-width: 0 1px 1px 1px;
            border-radius: 0 3px;
        }

        .detail-panel .detail-panel-box {
            height: 0;
            overflow: hidden;
        }

        .detail-panel .detail-panel-box.active {
            height: auto;
        }

        .detail-panel-box .table {
            margin-bottom: 0;
            text-align: center;
        }

        .table > thead > tr > th {
            text-align: center;
        }

        .table-theme-default > thead > tr {
            background-color: #b3a9a8;
        }

        .fj-list {
            padding: 0;
            margin: 0;
            list-style: none;
        }

        .fj-list > li {
            border-radius: 3px;
            padding: 0 15px;
        }

        .fj-list > li + li {
            margin-top: 10px;
        }

        .fj-list > li > a {
            display: block;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            line-height: 30px;
        }

        .fj-list > li:hover {
            background-color: #D3D3D3;
        }

        .fj-list .icon-type {
            display: inline-block;
            width: 24px;
            height: 24px;
            margin-right: 6px;
            vertical-align: middle;
            overflow: hidden;
        }
        .match-list{
        }

        .match-list>li>a{
            font-size: 16px;
            color: #333;
        }
        .match-list>li>a:hover{
            color: #e9442d;
        }
        .tabs-match{
            position: relative;
            margin-bottom: 20px;
        }
        .tabs-match .more-box{
            position: absolute;
            right:0;
            top:17px;
            z-index: 100;
        }
        .tabs-match .btn-more{
            display: block;
            padding: 0 16px;
            line-height: 30px;
            color: #e9442d;
            text-decoration: none;
            border: 1px solid transparent;
        }
        .tabs-match .more-box:hover .btn-more{
            border: 1px solid #ddd;
            border-bottom: 1px solid #fff;
        }
        .tabs-match .more-box:hover .more-drop-down{
            display: block;
        }
        .tabs-match .more-box .more-drop-down{
            display: none;
            position: absolute;
            right: 0;
            top: 100%;
            width: 350px;
            border: solid #ddd;
            border-width: 0 1px 1px;
        }
        .more-box .more-drop-down .inner{
            padding-top: 10px;
            padding-bottom: 10px;
            overflow: hidden;
        }
        .more-box .more-drop-down a{
            float: left;
            display: inline-block;
            width: 50%;
            padding: 4px 12px;
            color: #333;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }
        .more-box .more-drop-down a:hover{
            color: #e9442d;
        }
        .match-title{
            color: #333;
            margin: 0;
            font-size: 20px;
            font-weight: bold;
            padding: 13px 0;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }
    </style>
    <style>
        .process-content{
            padding: 20px 0;
            margin-bottom: 60px;
        }
        .process-time-line {
            position: relative;
        }
        .process-time-line:before{
            content: '';
            position: absolute;
            left: 0;
            top: 15px;
            width: 100%;
            height: 1px;
            background-color: #ddd;
        }

        .process-time-line .ptl-item {
            position: relative;
            padding-top: 15px;
            padding-left: 30px;
            padding-right: 30px;
            min-height: 30px;
            min-width: 110px;
            float: left;
        }
        .process-time-line .ptl-item:last-child{
            margin-right: 0;
        }

        .process-time-line .ptl-item .plt-time {
            display: block;
            position: absolute;
            left: 50%;
            top: 0;
            height: 30px;
            margin-left: -50px;
            padding: 0 12px;
            line-height: 30px;
            background-color: #ddd;
            border-radius: 3px;
            white-space: nowrap;
            box-sizing: border-box;
        }

        .process-time-line .ptl-item .plt-inner {
            position: relative;
            margin-top: 25px;
            padding: 0 10px;
            max-width: 380px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }

        .process-time-line .ptl-item.plt-item-big .plt-inner{
            max-width: 600px;
            min-width: 576px;
        }
        .process-time-line .ptl-item .arrow {
            position: absolute;
            left: 50%;
            bottom: 100%;
            margin-left: -3px;
            border-left: 6px solid transparent;
            border-right: 6px solid transparent;
            border-bottom: 6px solid #c3c3c3;
        }

        .plt-text {
            padding: 8px 0;
            margin-bottom: 0;
        }

        .process-time-line .plt-inner-header {
            height: 30px;
            margin: 0 -10px;
            line-height: 30px;
            font-size: 16px;
            padding: 0 10px;
            background-color: #ddd;
        }

        .process-time-line .plt-inner-body {
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .process-time-line .plt-inner-body > p {
            line-height: 24px;
            margin-bottom: 0;
        }

        .process-time-line .plt-inner-body .table{
            margin-bottom: 0;
            font-size: 12px;
        }
        .table>tbody>tr>td{
            text-align: center;
        }
        .process-time-line .plt-inner-body > p>span{
            display: inline-block;
            width: 50%;
            vertical-align: top;
        }
        .process-time-line .suggestion{
            width: 358px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
        }
        .process-time-line .plt-item-big .suggestion{
            display: inline-block;
            max-width: 300px;
            vertical-align: middle;
        }
        .process-time-line .ptl-item-complete .plt-time{
            background-color: #e9442d;
            color: #fff;
        }
        .process-time-line .ptl-item-complete .plt-inner{
            border-color: #e9442d;
        }
        .process-time-line .ptl-item-complete .plt-inner-header{
            background-color: #e9442d;
            color: #fff;
        }
        .process-time-line .ptl-item-complete .arrow{
            border-bottom: 6px solid #e9442d;
        }
        .process-time-line .ptl-item-complete:before{
            content: '';
            position: absolute;
            left: 0;
            top: 15px;
            width: 100%;
            height: 1px;
            background-color: #e9442d;
        }
        .edit-bar {
            margin-bottom: 15px;
        }

        .edit-bar-left {
            position: relative;
        }

        .edit-bar-left > span {
            position: relative;
            padding: 0 15px 0 0;
            color: #e9432d;
            font-size: 16px;
            font-weight: 700;
            line-height: 2.5;
            background: #fff;
            z-index: 1;
        }

        .edit-bar-left .line {
            display: block;
            position: absolute;
            left: 0;
            top: 50%;
            right: 0;
            border-top: 1px solid rgb(243, 213, 175);
        }

        .edit-bar-sm .edit-bar-left > span {
            color: #000;
            font-size: 16px;
            font-weight: normal;
        }
    </style>
</head>
<body>
<div id="curMatch" class="container container-fluid-oe">
    <div class="tabs-match">
        <h3 class="match-title">我的大赛名称开始</h3>
        <div class="more-box">
            <a class="btn-more" href="javascript:void(0);">查看更多</a>
            <div class="more-drop-down">
                <div class="inner">
                    <a href="javascript:void (0);">我的大赛名二开始</a>
                    <a href="javascript:void (0);">我的大赛名二开始</a>
                    <a href="javascript:void (0);">我的大赛名二开始</a>
                </div>
            </div>
        </div>
    </div>
    <div class="match-panel-content">
        <div class="info-content">
            <div class="base-info">
                <div class="match-cover match-no-cover">
                    <%--没有大赛展示图片显示默认图片，链接前往编辑展示页面, 有图片，链接到优秀项目或者大赛展示页面--%>
                    <a href="#">
                        <img class="img-responsive" src="${ctxImg}/video-default.jpg">
                        <span class="tip">编辑大赛展示页面</span>
                    </a>
                    <%--<a href="http://localhost:8081/f/frontExcellentView-e462cebeb77746f080b6e1e053ed15d1">--%>
                    <%--<img class="img-responsive" src="${ctxImg}/video-default.jpg">--%>
                    <%--</a>--%>
                </div>
                <div class="info-list">
                    <span>编<span style="visibility: hidden">编号</span>号：20171115091810000098</span>
                    <span>荣获奖项：大赛类型</span>
                    <span>大赛类别：互联网+商务服务</span>
                    <span>负&nbsp;责&nbsp;人&nbsp;：
                    <a class="person-name"
                       href="http://localhost:8081/f/sys/frontStudentExpansion/form?id=83a2aa31ced043a28fcacf42a98e47b7">沈淑仪</a></span>
                    <span>荣获奖项：</span>
                    <span>当前赛况：<span class="primary-color">待学院管理员审核</span></span>
                    <span>参赛组别：成长组</span>
                    <span>当前赛制：校级初赛</span>
                </div>
            </div>
            <div class="detail-info">
                <div class="detail-panel intro-box">
                    <p class="detail-panel-header active"><a href="javascript:void(0);">成员列表</a></p>
                    <div class="detail-panel-box active">
                        <div class="inner">
                            <table class="table table-bordered table-condensed table-hover table-theme-default">
                                <thead>
                                <tr>
                                    <th>姓名</th>
                                    <th>学号</th>
                                    <th>学院</th>
                                    <th>专业</th>
                                    <th>技术领域</th>
                                    <th>联系电话</th>
                                    <th>在读学位</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>火明明</td>
                                    <td>201414012574</td>
                                    <td>材料科学与工程学院</td>
                                    <td></td>
                                    <td></td>
                                    <td>13290928701</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>张麒文</td>
                                    <td>2015210346</td>
                                    <td>机械与运载工程学院</td>
                                    <td></td>
                                    <td></td>
                                    <td>15927080023</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>李亚妮</td>
                                    <td>524565</td>
                                    <td>工商管理学院</td>
                                    <td></td>
                                    <td>云计算,大数据</td>
                                    <td>13026163326</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>王江英</td>
                                    <td>201414016907</td>
                                    <td>材料科学与工程学院</td>
                                    <td></td>
                                    <td></td>
                                    <td>15790927796</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>沈淑娴</td>
                                    <td>20131401067</td>
                                    <td>材料科学与工程学院</td>
                                    <td></td>
                                    <td>人工智能</td>
                                    <td>18696128279</td>
                                    <td>学士</td>
                                </tr>
                                <tr>
                                    <td>文东</td>
                                    <td>201314015207</td>
                                    <td>材料科学与工程学院</td>
                                    <td></td>
                                    <td>物联网</td>
                                    <td>18390920120</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>蔡明俐</td>
                                    <td>201314064807</td>
                                    <td>材料科学与工程学院</td>
                                    <td></td>
                                    <td>VR/AR</td>
                                    <td>18390995704</td>
                                    <td></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="detail-panel intro-box">
                    <p class="detail-panel-header"><a href="javascript:void(0);">项目介绍</a></p>
                    <div class="detail-panel-box">
                        <div class="inner">
                            王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青王青青
                        </div>
                    </div>
                </div>
                <div class="detail-panel intro-box">
                    <p class="detail-panel-header"><a href="javascript:void(0);">附件</a></p>
                    <div class="detail-panel-box">
                        <div class="inner">
                            <ul class="fj-list">
                                <li><a href="javascript:void(0) ;"><span class="icon-type"><img class="img-responsive"
                                                                                                src="${ctxImg}/filetype/image.png"> </span>王清腾</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>大赛进程</span>
                <i class="line"></i>
            </div>
        </div>
        <div class="process-content">
            <div class="process-time-line clearfix">
                <div class="ptl-item ptl-item-complete">
                    <span class="plt-time">2017-11-15</span>
                    <div class="plt-inner">
                        <span class="arrow"></span>
                        <p class="plt-text text-center">提交报名表</p>
                    </div>
                </div>
                <div class="ptl-item ptl-item-complete">
                    <span class="plt-time">2017-11-30</span>
                    <div class="plt-inner">
                        <span class="arrow"></span>
                        <h4 class="plt-inner-header">院级审核（材料科学与工程学院）</h4>
                        <div class="plt-inner-body">
                            <p><span>学院评分：<span class="primary-color">100</span></span><span>学院排名：<span class="primary-color">1</span></span></p>
                            <p>学院参赛项目组数：0</p>
                            <p class="suggestion">建议及意见：很好很好很好很好很好很好很好</p>
                        </div>
                    </div>
                </div>
                <div class="ptl-item plt-item-big">
                    <span class="plt-time">2017-11-30</span>
                    <div class="plt-inner">
                        <span class="arrow"></span>
                        <h4 class="plt-inner-header">校级审核（湖南大学）</h4>
                        <div class="plt-inner-body">
                            <p><span>荣获奖项：<span class="primary-color">大学生创业优秀奖</span></span><span>荣获奖金：<span class="primary-color">1000000</span></span></p>
                            <p><span>学院参赛项目组数：0</span></p>
                            <table class="table table-hover table-condensed table-bordered table-theme-default">
                                <thead>
                                <tr>
                                    <th>评分内容</th>
                                    <th width="44">得分</th>
                                    <th>建议及意见</th>
                                    <th width="64">当前排名</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>学院管理员审核</td>
                                    <td>100</td>
                                    <td><span class="suggestion">王清腾王清腾王清腾王清腾</span></td>
                                    <td>1</td>
                                </tr>
                                <tr>
                                    <td>学院管理员12审核审核</td>
                                    <td>100</td>
                                    <td><span class="suggestion">王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾王清腾</span></td>
                                    <td>1</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).on('click', '.match-panel-content .detail-panel-header a', function (e) {
        if ($(this).parent().hasClass('active')) {
            return
        }
        $(this).parents('.detail-panel').siblings().find('.detail-panel-header, .detail-panel-box').removeClass('active');
        $(this).parent().addClass('active').next().addClass('active')
    })

</script>
</body>
</html>
