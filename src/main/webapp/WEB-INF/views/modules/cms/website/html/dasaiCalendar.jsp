<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/calender.css">
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
    <style>
        .time-line {
            position: relative;
            width: 600px;
            margin: 40px auto;
        }

        .time-line::before {
            content: '';
            position: absolute;
            top: 0;
            left: 50%;
            height: 100%;
            width: 2px;
            background: #ccc;
            margin-left: 3px;
        }

        .time-line .time-item {
            position: relative;
            width: 124px;
            height: 150px;
            margin: 0 auto 35px;
            z-index: 100;
        }

        .time-line .time-item-bg {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
            background: url('/images/half_circle.png') no-repeat left top;
            z-index: -1;
        }

        .time-line .time-item.last .time-inner {
            top: 26px;
        }

        .time-line .time-item.active .time-item-bg {
            background-image: url('/images/hover_half_circle.png');
        }

        .time-line .time-item.last .time-item-bg {
            transform: rotateX(180deg);
        }

        .time-line .time-inner {
            position: relative;
            width: 124px;
            height: 124px;
            border-radius: 62px;
            background-color: #c3c3c3;
            transition: width .15s linear;
            overflow: hidden;
        }

        .time-line .time-item.active .time-inner {
            background-color: #e9422f;
        }

        .time-line .time-inner .time-inner-header {
            float: left;
            width: 124px;
        }

        .time-line .time-inner .time-inner-header .cell {
            display: table-cell;
            height: 124px;
            width: 1%;
            text-align: center;
            font-size: 12px;
            color: #fff;
            vertical-align: middle;
        }

        .time-line .time-inner .time-inner-content {
            margin-left: 124px;
            height: 124px;
        }

        .time-line .time-item.active.hasContent:hover .time-inner {
            width: 300px;
        }

        .time-line .time-item-intro {
            position: relative;
        }

        .time-line .intro-dot {
            position: absolute;
            left: 50%;
            top: 3px;
            width: 12px;
            height: 12px;
            margin-left: -2px;
            border: 1px solid #fcc177;
            border-radius: 50%;
        }

        .time-line .intro-dot > span {
            display: block;
            width: 6px;
            height: 6px;
            background-color: #e9422f;
            margin: 2px auto 0;
            border-radius: 50%;
        }

        .time-line .intro-date {
            text-align: right;
            width: 290px;
        }

        .time-line .intro-inner {
            float: right;
            width: 280px;
            border: 1px solid #e9422d;
            border-radius: 3px;
            margin-top: -4px;
        }

        .time-line .intro-header {
            position: relative;
            padding: 3px 8px;
            background-color: #e9422d;
            color: #fff;
        }

        .time-line .intro-content {
            padding: 3px 8px;
            font-size: 12px;
            line-height: 1.42857143;
        }

        .time-line .intro-inner .title {
            margin: 0;
            line-height: 20px;
            height: auto;
        }

        .time-line .intro-inner .arrow {
            position: absolute;
            left: -7px;
            top: 6px;
            border-top: 6px solid transparent;
            border-bottom: 6px solid transparent;
            border-right: 6px solid #e9422d;
        }

        .time-line-box {
            position: relative;
            padding-bottom: 30px;
            z-index: 100;
        }

        .time-line-box:last-child {
            padding-bottom: 0;
        }

        .time-line .time-line-box-arrow {
            position: absolute;
            left: 50%;
            bottom: 100%;
            margin-left: -4px;
            border-right: 8px solid transparent;
            border-left: 8px solid transparent;
            border-top: 12px solid #ccc;
        }

        .time-line-box.completed .time-line-box-arrow {
            border-top: 12px solid #e9422d;
        }

        .time-line-box.completed:before {
            content: '';
            position: absolute;
            top: 0;
            left: 50%;
            height: 100%;
            width: 2px;
            background: #e9422d;
            margin-left: 3px;
        }

        .time-line-box.completed.gray:before {
            display: none;
        }

        .btn-milestone {
            text-decoration: none;
            color: #fff;
            background: -webkit-linear-gradient(#fd9a69, #fb4f2e);
            background: -o-linear-gradient(#fd9a69, #fb4f2e);
            background: -moz-linear-gradient(#fd9a69, #fb4f2e);
            background: linear-gradient(#fd9a69, #fb4f2e);
            border-radius: 5px;
        }

        .btn-milestone:hover, .btn-milestone:active {
            color: #fff;
            background: -webkit-linear-gradient(#fd9a69, #fb4f2e);
            background: -o-linear-gradient(#fd9a69, #fb4f2e);
            background: -moz-linear-gradient(#fd9a69, #fb4f2e);
            background: linear-gradient(#fd9a69, #fb4f2e);
        }

        .time-line .time-inner .time-inner-content .cell {
            display: table-cell;
            width: 1%;
            height: 124px;
            text-align: center;
            vertical-align: middle;
        }

        .time-line .time-inner .time-inner-content .approval {
            color: #fff;
        }

        .time-line-container {
            margin-bottom: 40px;
            /*border: 1px solid #ececec;*/
        }

        .time-line-title {
            margin: 0;
            line-height: 50px;
            text-align: center;
        }

        .project-info > span {
            font-size: 12px;
            margin-right: 10px;
            line-height: 30px;
        }

        .time-line-header {
            padding: 6px 10px;
            background-color: #f4e6d4;
        }

        .btn-primary {
            color: #fff;
            background-color: #e9432d;
            border-color: #e53018;
        }

        .btn-primary:focus,
        .btn-primary.focus {
            color: #fff;
            background-color: #cd2b16;
            border-color: #71180c;
        }

        .btn-primary:hover {
            color: #fff;
            background-color: #cd2b16;
            border-color: #ad2412;
        }

        .btn-primary.active.focus, .btn-primary.active:focus, .btn-primary.active:hover, .btn-primary:active.focus, .btn-primary:active:focus, .btn-primary:active:hover, .open > .dropdown-toggle.btn-primary.focus, .open > .dropdown-toggle.btn-primary:focus, .open > .dropdown-toggle.btn-primary:hover {
            color: #fff;
            background-color: #cd2b16;
            border-color: #ad2412;
        }

        .btn-pro-list {
            color: #4b4b4b;
            background-color: #bebebe;
        }

        .btn-pro-list:hover, .btn-pro-list:focus {
            color: #4b4b4b;
            background-color: #bebebe;
        }

        .time-actions {
            margin: 40px 0 20px;
        }

        .categories-column {
            margin-bottom: 20px;
        }

        .categories-column .categories span {
            display: inline-block;
            width: 93px;
            padding: 0 4px;
            margin-bottom: 8px;
            height: 20px;
            line-height: 20px;
            white-space: nowrap;
            overflow: hidden;
            vertical-align: top;
            text-overflow: ellipsis;
        }

        .categories-column .category-label {
            width: 75px;
            line-height: 20px;
            float: left;
        }

        .categories-column .categories {
            margin-left: 75px;
        }

        .categories-column .categories a {
            color: #333;
            text-decoration: none;
        }

        .categories-column .categories a.router-link-active {
            color: #e9422f;
        }

        .no-time-line, .loading-data {
            margin: 60px auto;
            text-align: center;
        }

        .time-sidebar {
            float: left;
            width: 48px;
        }

        .time-content {
            margin-left: 48px;
            border: solid #ddd;
            border-width: 0 1px 1px;
            /*padding-left: 10px;*/
            /*border-left: 1px dashed #ccc;*/
        }

        .time-name-nav {
            margin: 49px 0 0;
            padding: 0;
            list-style: none;
        }

        .time-name-nav > li {
            margin-bottom: 10px;
        }

        .time-name-nav > li > a {
            display: block;
            line-height: 20px;
            padding: 10px 8px;
            color: #333;
            text-decoration: none;
            border-radius: 5px 0 0 5px;
            border: solid #ddd;
            border-width: 1px 0 1px 1px;
            text-align: center;
        }

        .time-name-nav > li > a > span {
            display: block;
            height: 100px;
            overflow: hidden;
            word-wrap: break-word;
        }

        .time-name-nav > li.active > a, .time-name-nav > li:hover a {
            color: #fff;
            background-color: #e9422f;
            border-bottom: 1px solid #fff;
        }

        .time-line-header {
            height: 42px;
        }

        .nav-tabs > li.router-link-active > a, .nav-tabs > li.router-link-active > a:focus, .nav-tabs > li.router-link-active > a:hover {
            color: #555;
            cursor: default;
            background-color: #fff;
            border: 1px solid #ddd;
            border-bottom-color: transparent;
        }

        .nav-wrap {
            position: relative;
            margin-left: 48px;
        }

        .nav-wrap .nav {
            margin-right: 64px;
        }

        .nav-wrap .dropdown {
            position: absolute;
            right: 0;
            top: 0;
            height: 42px;
            width: 64px;
            border-bottom: 1px solid #ddd;
        }

        .list-wrap {
            position: relative;
            margin: 0 20px 0;
            border: 1px solid #cecece;
        }

        .list-wrap + div {
            margin-top: -1px;
        }

        .list-wrap .month-label {
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 120px;
        }

        .list-wrap .match-info {
            margin-left: 120px;
        }

        .list-wrap .evt-list-span {
            left: 120px;
            right: 0;
            top: 0;
            bottom: 0;
            margin-right: 0;
            border: none;
            z-index: 1;
        }

        .list-wrap:hover .month-label {
            position: absolute;
        }

        .list-wrap .match-info .table {
            margin-bottom: 0;
        }

        .list-wrap .match-info {
            padding: 15px;
            min-height: 120px;
        }

        .list-wrap .bottom-arrow {
            bottom: 0;
            top: auto;
        }

        .mon-wrap {
            top: auto;
            margin-top: auto;
        }

        .list-wrap:hover {
            z-index: 10;
            border-color: #e9422f;
        }

        .time-list-content {
            padding: 40px 0;
        }
    </style>
</head>
<body>

<div id="appCalendar" class="container container-fluid-oe">
    <div class="nav-wrap">
        <ul class="nav nav-tabs">
            <li>
                <a href="#">蓝桥杯</a>
            </li>
            <li class="active">
                <a href="#">蓝桥杯</a>
            </li>
        </ul>
        <div class="dropdown">
            <%--<a href="#">更多</a>--%>
            <%--<ul>--%>
            <%--<li><a href="#"></a></li>--%>
            <%--</ul>--%>
        </div>
    </div>
    <div class="time-line-container">
        <div class="time-sidebar">
            <ul class="time-name-nav">
                <li class="active"><a href="javascript:void (0)"><span>Pro1501</span></a></li>
                <li><a href="javascript:void (0)"><span>王清腾的项目2王清腾的项目2王清腾的项目2</span></a></li>
            </ul>
        </div>
        <div v-show="loadedData" style="display: none" class="time-content">
            <h4 class="time-line-title">{{project_code.name}}</h4>
            <div class="time-line-header clearfix">
                <div class="pull-right"><a
                        href="/227f6f786a5e4bf6a7343fa9d94d874a?actYwId=2d7850ca88324274b0da3c18cb292f96&amp;type=1"
                        class="btn btn-primary btn-sm router-link-active">当前大赛
                </a> <a href="${ctxFront}/project/projectDeclare/list" class="btn btn-pro-list btn-sm">大赛列表</a></div>
                <div class="project-info">
                    <span>项目编号：{{project_code.code}}</span>
                </div>
            </div>
            <div class="time-list-content">
                <div class="list-wrap" v-for="(item, index) in list">
                    <div class="month-label month-label-three">
                        <div class="en-month">{{item.Date | formatterMonth}}</div>
                        <div class="mon-wrap">
                            <div class="date"><strong>{{item.Date.substring(8)}}</strong></div>
                            <div class="events">{{item.SpeedOfProgress}}</div>
                        </div>
                    </div>
                    <div class="evt-list-span evt-list-span-three">
                        <span class="left-arrow"></span>
                        <span class="right-arrow"></span>
                        <span class="top-arrow"></span>
                        <span class="bottom-arrow"></span>
                    </div>
                    <div class="match-info">
                        <div v-if="item.type == '0'">
                            <p class="txt-list">
                                <img v-type-img="{link: item.link}">
                                <a href="javascript:void(0)" @click="downfile(item.url, item.link); return false">{{item.link}}</a>
                            </p>
                        </div>
                        <div v-if="item.type == '1'">
                            <table class="table table-hover table-bordered table-condensed">
                                <caption class="prj-title-desc"><p>所在组别：<span>{{item.group}}</span></p>
                                    <p>学院：<span>{{item.School}}</span></p>
                                    <p>参赛项目组数：<span>{{item.Number_of_entries}}</span></p></caption>
                                <thead>
                                <tr>
                                    <th>学院评分</th>
                                    <th>建议及意见</th>
                                    <th>学院排名</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(tr, index) in item.list">
                                    <template v-if="tr.College_score">
                                        <td>{{tr.College_score}}</td>
                                    </template>
                                    <template>
                                        <td><span style="opacity: 0">0</span></td>
                                    </template>
                                    <td>{{tr.Proposal}}</td>
                                    <td>{{tr.ranking}}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div v-if="item.type == '2'">
                            <table class="table table-hover table-condensed table-bordered">
                                <caption class="prj-title-desc"><p>所在组别：<span>{{item.group}}</span></p>
                                    <p>学院：<span>{{item.School}}</span></p>
                                    <p>参赛项目组数：<span>{{item.Number_of_entries}}</span></p></caption>
                                <thead>
                                <tr>
                                    <th>评分内容</th>
                                    <th>得分</th>
                                    <th>建议及意见</th>
                                    <th>当前排名</th>
                                    <th>荣获奖项</th>
                                    <th>荣获奖金</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="(tr, index) in item.list">
                                    <template v-if="tr.Review_the_content">
                                        <td>{{tr.Review_the_content}}</td>
                                    </template>
                                    <template>
                                        <td><span style="opacity: 0">0</span></td>
                                    </template>
                                    <td>{{tr.getScore}}</td>
                                    <td>{{tr.advice}}</td>
                                    <td>{{tr.Current_rank}}</td>
                                    <template v-if="index === 0">
                                        <td :rowspan="item.list.length">{{item.Awards}}</td>
                                        <td :rowspan="item.list.length">{{item.bonus}}</td>
                                    </template>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="!loadedData" class="loading-data">
            数据加载中...
        </div>
    </div>
</div>

<script type="text/javascript">

    var matchCalendar = new Vue({
        el: '#appCalendar',
        data: function () {
            return {
                loadedData: false,
                project_code: {},
                list: []
            }
        },
        directives: {
            'type-img': {
                inserted: function (element, binding, cnode) {
                    var link = binding.link;
                    if (!link) {
                        $(element).remove();
                        return false;
                    }
                    var index = link.lastIndexOf('.');
                    var Txtype = link.substr(index + 1, link.length), icon_type = '';
                    switch (Txtype) {
                        case 'docx' :
                            icon_type = 'word';
                            break;
                        case 'doc'  :
                            icon_type = 'word';
                            break;
                        case 'pptx' :
                            icon_type = 'ppt';
                            break;
                        case 'ppt'  :
                            icon_type = 'ppt';
                            break;
                        case 'xlsx' :
                            icon_type = 'excel';
                            break;
                        case 'xls'  :
                            icon_type = 'excel';
                            break;
                        case 'txt'    :
                            icon_type = 'txt';
                            break;
                        case 'rar'    :
                            icon_type = 'rar';
                            break;
                        case 'zip'    :
                            icon_type = 'ZIP';
                            break;
                        case 'project'    :
                            icon_type = 'project';
                            break;
                        default     :
                            icon_type = 'image';
                            break;
                    }
                    element.src = '/images/' + icon_type + '.png'
                }
            }
        },
        filters: {
            formatterMonth: function (date) {
                var d = new Date(date);
                var monthArr = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
                return monthArr[d.getMonth()].substring(0, 3);
            }
        },
        methods: {
            getCalendarList: function () {
                var xhr = $.get('/f/gcontest/gContest/getGcontestTimeIndexData');
                var self = this;
                xhr.success(function (data) {
                    self.project_code = data.project_code;
                    self.list = data.table_first.list;
                    self.loadedData = true;
                })
            },
            downfile: function (url, link) {
                location.href = "/ftp/loadUrl?url=" + url + "&fileName=" + encodeURI(encodeURI(link));
            }
        },
        beforeMounted: function () {

        },
        mounted: function () {
            this.getCalendarList()
        }
    })


</script>

</body>
</html>
