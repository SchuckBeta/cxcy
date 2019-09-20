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
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <style type="text/css">

        ul li {
            list-style: none;
        }

        .scinfonavigation ul li {
            float: left;
            padding-left: 5px;
        }

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

        .dsrdinfo h5 {
            text-align: center;
            margin-top: 94px;
            color: #BCBCBC;
        }

        .m {
            width: 696px;
            height: 322px;
            margin: auto;
            margin-top: 80px;
        }

        .video_info {
            float: left;
            margin-top: 60px;
        }

        .video_info .class {
            font-size: 15px;
        }

        .video_info .teach_info {
            margin-top: 70px;
        }

        .teach_info span {
            font-size: 18px;
            font-weight: bold;
        }

        .video_info h4 {
            font-weight: bold;
        }

        .gj_info {
            margin-top: 96px;
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

        .gj_info p {
            color: #BCBCBC;
            padding-top: 28px;
            text-indent: 2em;
        }

        .dsimg {
            width: 521px;
            height: 363px;
            margin: 89px auto;
        }

        #content .container, .dsrdinfo h5, .gj_info, .gj_info p {
            color: #666;
        }


    </style>
</head>

<body>
<div class="container" style="margin-top: 0px;">
    <!-- <div class="scinfo">
        <div class="scinfonavigation">
            <ul>
                <li><img src="${ctxImg}/bc_home.png"></li>
                <li>首页></li>
                <li>大赛热点</li>
            </ul>
        </div>
    </div> -->
    <div class="dsrdinfo">
        <h3>“翱翔系列小卫星”夺冠第二届中国“互联网+”大学生创新创业大赛</h3>
        <h6>发布时间:2016-12-16 来源:全国大学生创业服务端 浏览量:150</h6>
        <h5>“翱翔系列小卫星”项目以530分获得全国冠军！2016年10月14日晚,经过2小时的精彩对决,第二届中国"互联网+"
            大学生创新创业全国总决赛在华中科技大学光谷体育诞生:</h5>
        <div class="m">
            <!--播放器-->
            <video style="margin:auto; " id="my-video" class="video-js" controls preload="auto" width="514" height="360"
                   poster="MY_VIDEO_POSTER.jpg" data-setup="{}">
                <source src="${ctxImg}/temple.mp4" type="video/ogg" autostart="false" loop="false">
                <p class="vjs-no-js">
                    当前浏览器不支持 video直接播放，请使用高版本的浏览器查看。
                    <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
                </p>
            </video>
        </div>
    </div>
    <div style="clear: both;"></div>
    <div class="gj_info">
        <p>冠军项目是什么样的高科技？</p>
        <p>目前以立方星为代表的低成本商业航天正成为世界航天市场的创新创业热点。获得冠军的“翱翔系列微小卫星”项目,由西北工业大学在校研究生组成的创业团队创立,民享sad
            艾斯德斯大多撒大所大所大所大所大所大所大所大所多撒大所大所多所大所大所</p>
        <p>西北工业大学“翱翔系列微小卫星”项目团队成员代表、指导教师接受现场采访</p>
        <p>‘翱翔系列微小卫星’项目以530分获得全国总冠军！2016年10月14日晚，经过2个小时的精彩对决，第二届中国“互联网+”大学</p>
        <p>“翱翔系列小卫星”夺冠第二届中国“互联网+”大学生创新创业大赛</p>
        <div class="dsimg">
            <img src="${ctxImg}/dsrd_02.jpg">
        </div>
        <p style="text-align: center; margin-top: -100px;">西北工业大学“翱翔系列微小卫星”项目团队成员代表、指导教师接受现场采访</p>

    </div>

    <div class="more">
        <h3>相关推荐</h3>
    </div>

    <div class="clearfix"></div>
    <div class="newinfo">
        <ul>
            <!--<li>
                <div class="newone fl">
                    李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示
                </div>
                <div class="newtwo fr">
                    2016年1月12日
                </div>
            </li>-->
            <li>李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示<span>2016年1月12日</span></li>
            <li>李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示<span>2016年1月12日</span></li>
            <li>李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示<span>2016年1月12日</span></li>
            <li>李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示<span>2016年1月12日</span></li>
        </ul>
    </div>
</div>

<script src="http://vjs.zencdn.net/5.18.4/video.min.js"></script>
<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>
<script type="text/javascript" src="${ctxJs}/scroll.js"></script>
<script>
    var myPlayer = videojs('my-video');
    videojs("my-video").ready(function () {
        var myPlayer = this;
        myPlayer.play();
    });
</script>
</body>

</html>