<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
    <link rel="stylesheet" href="${ctxOther}/icofont/iconfont.css">
    <link rel="stylesheet" href="${ctxOther}/pages/jquery.page.css">
    <link rel="shortcut icon" href="${ctxImages}/bitbug_favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/projectShow.css">
    <script type="text/javascript" src="${ctxJs}/actyw/vue.js"></script>
    <title>${frontTitle}</title>
    <style>
        .main {
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .main .content {
            width: 1000px;
            margin: 50px auto;
            overflow: hidden;
        }

        .content .top_link .ft12 {
            font-size: 12px;
        }

        .content .search_input {
            margin-top: 10px;
        }

        .content .search_input .input {
            float: right;
            border: 1px solid #D9D9D9;
            padding: 8px 35px 8px 8px;
            font-size: 14px;
            font-family: 'Microsoft yahei';
            position: relative;
            margin-right: 20px;
        }

        .content .search_input input {
            border: none;
            display: block;
            width: 100%;
            border-right: 1px solid #D9D9D9;
        }

        .content .search_input .ico_search {
            position: absolute;
            right: 9px;
            top: 11px;
            color: red;
            cursor: pointer;
        }

        .content .tab_clum {
            overflow: hidden;
            margin-top: 50px;
        }

        .content .tab_clum .tab {
            text-align: center;
            width: 625px;
            margin: auto;
        }

        .content .tab_clum .tab li {
            float: left;
            padding-bottom: 15px;
            border-bottom: 3px solid transparent;
        }

        .content .tab_clum .tab li a {
            display: block;
            width: 155px;
            border-right: 1px solid #A9A9A9;
            font-size: 13px;
            color: #000;
        }

        .content .tab_clum .tab li:last-child a {
            border: none;
        }

        .content .tab_clum .tab li a:hover {
            color: #e92f27;
        }

        .content .tab_clum .tab li:hover {
            border-bottom: 3px solid #e92f27;
        }

        .content .tab_clum .tab_items {
            margin-top: 30px;
        }

        .content .tab_clum .tab_items ul li {
            float: left;
            width: 240px;
            padding: 10px;
            margin: 0 2px;
            margin-bottom: 20px;
            box-sizing: border-box;
            border: 1px solid #D3D3D3;
            border-radius: 3px;
        }

        .content .tab_clum .tab_items ul li a .project_img img {
            height: 149px;
            width: 100%
        }

        .content .tab_clum .tab_items ul li .project_info {
            padding-bottom: 3px;
            border-bottom: 1px solid #D9D9D9;
            margin-bottom: 10px;
        }

        .content .tab_clum .tab_items ul li .project_info .project_title {
            height: 36px;
            margin: 17px -2px;
            line-height: 36px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .content .tab_clum .tab_items ul li .project_info .project_title span {
            font-size: 18px;
            color: #707070;
        }

        .content .tab_clum .tab_items ul li .project_info .project_bright {
            margin: 17px 0;
        }

        .content .tab_clum .tab_items ul li .project_info .project_bright span {
            padding: 2px;
            border: 1px solid rgb(247, 169, 157);
            border-radius: 4px;
            margin-right: 10px;
            color: #707070;
        }

        .content .tab_clum .tab_items ul li .project_info .project_description p {
            color: #707070;
            margin: 5px 0;
        }

        .content .tab_clum .tab_items ul li .project_time p {
            float: left;
            color: #707070;
            margin-right: 5px;
            height: 20px;
        }

        #tab_chang .tab_2 {
            display: none;
        }

        #tab_chang .tab_3 {
            display: none;
        }

        #tab_chang .tab_4 {
            display: none;
        }

        #page {
            float: left;
        }

        #select_page {
            float: right;
            line-height: 35px;
        }

        #select_page .select_item {
            float: left;
            font-size: 12px;
            color: #666;
            margin-right: 16px;
        }
    </style>
</head>
<body>
<div id="projectShowPages" class="container-fluid container-fluid-oe">
    <ol class="breadcrumb">
        <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/page-innovation">国创项目</a></li>
        <li class="active">优秀项目展示</li>
    </ol>
    <div class="tab-container">
        <div class="tab-header">
            <ul class="nav-inline clearfix" role="tablist">
                <li role="presentation" class="active">
                    <a href="#projectFilter1" aria-controls="projectFilter1" role="tab" data-toggle="tab">全部项目</a>
                </li>
                <li role="presentation">
                    <a href="#projectFilter2" aria-controls="projectFilter2" role="tab" data-toggle="tab">学院项目</a>
                </li>
            </ul>
            <div class="clearfix">
                <div class="form-search-box">
                    <div class="input-group">
                        <input type="text" placeholder="关键字输入" class="form-control">
                    <span class="input-group-addon" id="basic-addon2">
                        <i class="iconfont ico_search icon-sousuo-sousuo"
                           style="display: inline-block;height: 13px;font-size: 14px"></i>
                    </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="projectFilter1">
                <div class="row">
                    <div class="col-md-3" v-for="item in list">
                        <div class="project-item">
                            <a class="project-pic" href="{{item.link}}">
                                <img class="img-responsive" v-bind:src="item.thumbnailUrl" alt="{{item.proName}}">
                            </a>
                            <h4 class="pro-title"><a title="{{item.title}}" href="{{item.link}}">{{item.title}}</a></h4>
                            <div class="pro-info">
                                <p>项目来源： {{item.proName}}</p>
                                <p style="display: none">荣获奖项: 优秀项目</p>
                                <p>项目负责人： {{item.chargeName}}</p>
                                <p>学院： {{item.schoolName}}</p>
                            </div>
                            <div class="pro-time-look-wx">
                                <span class="time"><i class="iconfont ico icon-rili"></i>{{item.date}}</span>
                                <span class="look"><i class="iconfont ico icon-yanjing1"></i>{{item.look}}</span>
                                <span class="wx"><i class="iconfont ico icon-tuxing1"></i>{{item.wxGood}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="projectFilter2">
                <div class="row">
                    <div class="col-md-3" v-for="item in list2">
                        <div class="project-item">
                            <a class="project-pic" href="{{item.link}}">
                                <img class="img-responsive" v-bind:src="item.thumbnailUrl" alt="{{item.proName}}">
                            </a>
                            <h4 class="pro-title"><a title="{{item.title}}" href="{{item.link}}">{{item.title}}</a></h4>
                            <%--<div class="pro-tags">--%>
                                <%--<span v-for="tag in item.tags">{{tag}}</span>--%>
                            <%--</div>--%>
                            <div class="pro-info">
                                <p>项目来源： {{item.proName}}</p>
                                <p style="display: none">荣获奖项: 优秀项目</p>
                                <p>项目负责人： {{item.chargeName}}</p>
                                <p>学院： {{item.schoolName}}</p>
                            </div>
                            <div class="pro-time-look-wx">
                                <span class="time"><i class="iconfont ico icon-rili"></i>{{item.date}}</span>
                                <span class="look"><i class="iconfont ico icon-yanjing1"></i>{{item.look}}</span>
                                <span class="wx"><i class="iconfont ico icon-tuxing1"></i>{{item.wxGood}}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="change_page clearfix">
        <div id="page"></div>
        <div id="select_page">
            <div class="select_item info">
                当前页数<span>1</span>页
            </div>
            <div class="select_item count">
                总共<span>20</span>页
            </div>
            <div class="select_item sum">
                总记录<span>20</span>
            </div>
        </div>
    </div>
</div>
<div class="main">
    <div class="content">
        <div class="tab_clum">
            <div class="tab_items" id="tab_chang" style="display: none">
                <div class="tab_1 tab_select">
                    <ul class="clearfix">
                        <li><a href="${ctxFront}/page-projectshowa01">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item1.jpg" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>GPS精确定位仪器</span>
                                </div>
                                <div class="project_bright">
                                    <span>工程测量</span><span>航空摄影测量</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李凯迪</p>
                                    <p>学院: 电气院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshowa27">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item27.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>支持隐私保护的安全数据库设计与实现</span>
                                </div>
                                <div class="project_bright">
                                    <span>数据安全</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 黄子巍</p>
                                    <p>学院: 信科院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshowa38">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item38.jpg" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 18px;">
                                    <span>基于搭载无线电中继系统UVA的紧急救灾相关研究</span>
                                </div>
                                <div class="project_bright">
                                    <span>无线电中继</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:创新创业项目</p>
                                    <p style="display: none">荣获奖项: 一等奖</p>
                                    <p>项目负责人:田丰</p>
                                    <p>学院: 物电院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshowa55">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item55.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>可折叠太阳能板汽车（SE-CAR）</span>
                                </div>
                                <div class="project_bright">
                                    <span>环保</span><span>太阳能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 刘曲仪</p>
                                    <p>学院: 机械院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <div class="clearfix"></div>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item57.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>电瓷显微结构调控及其对材料性能的优化研究</span>
                                </div>
                                <div class="project_bright">
                                    <span>电瓷显微结构分析</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 沈淑娴</p>
                                    <p>学院: 材料院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-itemb8.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>基于虚拟现实技术的旅游电商创新应用及社区运营模式研究</span>
                                </div>
                                <div class="project_bright">
                                    <span>虚拟现实</span><span>旅游</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 二等奖</p>
                                    <p>项目负责人: 陈昌祺</p>
                                    <p>学院: 工管院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-03-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item2.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>一种新型的超市储物柜系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>ARM微控制器</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:创新创业项目</p>
                                    <p style="display: none">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 林锦杰</p>
                                    <p>学院: 电气院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshowa03">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item3.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>野外求生多功能净水杯</span>
                                </div>
                                <div class="project_bright">
                                    <span>环保</span><span>净水器</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 优秀奖</p>
                                    <p>项目负责人: 张令</p>
                                    <p>学院: 土木院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item07.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span> 基于监测心率评估个体能量代谢状况的可穿戴设备及其服务链 </span>
                                </div>
                                <div class="project_bright">
                                    <span>数据融合算法</span><span>数模混合信号</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 王皓冉</p>
                                    <p>学院: 生物院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item17.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>湖南大学口袋猫团队“学研商”模式创新创业计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>STC-B学习板</span><span>PAG学习板</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 陈李培</p>
                                    <p>学院: 信科院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item19.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 1;!important">
											<span> 室内智能安防服务机器人
											</span>
                                </div>
                                <div class="project_bright">
                                    <span>安防监控</span><span>云转储</span><span>数据采集</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 优秀科研成果（校级）</p>
                                    <p style="display: none;">荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 赵国春</p>
                                    <p>学院: 电气院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshowa21">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item21.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>可重构全向智能移动平台—X平台</span>
                                </div>
                                <div class="project_bright">
                                    <span>电气</span><span> 数据通讯 </span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 易宗耀</p>
                                    <p>学院: 机械院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item22.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>面向家庭用户的创意3D打印灯具</span>
                                </div>
                                <div class="project_bright">
                                    <span>节能环保</span><span> 3D </span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 胡哲</p>
                                    <p>学院: 建筑院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item46.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>核酸适配体对膀胱癌样本如尿液等早期监测及相关数据库的建立</span>
                                </div>
                                <div class="project_bright">
                                    <span>医疗</span><span> 膀胱癌 </span><span>肿瘤</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:创新创业项目</p>
                                    <p style="display:none">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 李志雄</p>
                                    <p>学院: 生物院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item47.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>化工仪表自动化仿真实训装置创新设计与制作</span>
                                </div>
                                <div class="project_bright">
                                    <span>仿真</span><span> 化工仪表 </span><span>自动化</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 宋均</p>
                                    <p>学院: 化工院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item53.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>可重构共享移动交通系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>只能交通</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 杨程</p>
                                    <p>学院: 机械院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-item56.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>橙子类水果包装机的研发与推广</span>
                                </div>
                                <div class="project_bright">
                                    <span>农业</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 罗荣华</p>
                                    <p>学院: 机械院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-itemb1.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>岳麓书院IP开发与AR众创空间</span>
                                </div>
                                <div class="project_bright">
                                    <span>AR</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 蒋文妮</p>
                                    <p>学院: 新影院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-itemb4.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>基于网络大数据的网贷借款人信用风险评价关键技术、方法与模型研究</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span> 金融 </span><span>风险评估</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none;">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 陈颖盈</p>
                                    <p>学院: 金统院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/project-ths/project-itemb15.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>知涯职业生涯规划平台</span>
                                </div>
                                <div class="project_bright">
                                    <span>大学生</span><span> 职业规划 </span><span>服务</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p style="display: none">荣获奖项: 三等奖</p>
                                    <p>项目负责人: 闵跃龙</p>
                                    <p>学院: 工管院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                    </ul>
                </div>
                <div class="tab_2 tab_select">
                    <ul class="clearfix">
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>蓝牙遥控插座</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 杨丽华</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm07_07.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 18px;">
                                    <span>物联网远程打印机ReRouter</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 一等奖</p>
                                    <p>项目负责人:张镭</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>课程交换网站平台</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>监控</span><span>物联网</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 彭宇</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>


                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm08.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>hadoop</span><span>监控</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>

                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会说话的一片云</span>
                                </div>
                                <div class="project_bright">
                                    <span>云计算</span><span>linux</span><span>openstack</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（省赛）</p>
                                    <p>荣获奖项: 二等奖</p>
                                    <p>项目负责人: 薛晓丹</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-03-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm_03.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李思缔</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm10.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会跳舞的LED</span>
                                </div>
                                <div class="project_bright">
                                    <span>LED</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀奖</p>
                                    <p>项目负责人: 马晓媟</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm05.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>大数据互联环境监控系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span> 机器学习 </span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 陈思思</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm01.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span> 蓝牙遥控插座 </span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 龙思瑶</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: XXX</p>
                                    <p>学院: xxx</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm03.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 1;!important">
											<span> 遥控机器人/<span style="font-size: 13px;">

													圣诞节表白神器，一 举拿下女神的心</span>
											</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 优秀科研成果（校级）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李长江</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>

                    </ul>
                </div>
                <div class="tab_3 tab_select">
                    <ul class="clearfix">
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>课程交换网站平台</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>监控</span><span>物联网</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 彭宇</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm07_07.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 18px;">
                                    <span>物联网远程打印机ReRouter</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 一等奖</p>
                                    <p>项目负责人:张镭</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>蓝牙遥控插座</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 杨丽华</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm08.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>hadoop</span><span>监控</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <div class="clearfix"></div>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm_03.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李思缔</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会说话的一片云</span>
                                </div>
                                <div class="project_bright">
                                    <span>云计算</span><span>linux</span><span>openstack</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（省赛）</p>
                                    <p>荣获奖项: 二等奖</p>
                                    <p>项目负责人: 薛晓丹</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-03-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>

                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm10.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会跳舞的LED</span>
                                </div>
                                <div class="project_bright">
                                    <span>LED</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀奖</p>
                                    <p>项目负责人: 马晓媟</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm05.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>大数据互联环境监控系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span> 机器学习 </span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 陈思思</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm01.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span> 蓝牙遥控插座 </span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 龙思瑶</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: XXX</p>
                                    <p>学院: xxx</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm03.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 1;!important">
                <span> 遥控机器人/<span style="font-size: 13px;">

                圣诞节表白神器，一 举拿下女神的心</span>
                </span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 优秀科研成果（校级）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李长江</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                    </ul>
                </div>
                <div class="tab_4 tab_select">
                    <ul class="clearfix">
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm03.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 1;!important">
                                    <span> GPS精确定位仪器</span>
                                    </span>
                                </div>
                                <div class="project_bright" style="display: none;">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 优秀科研成果（校级）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李凯迪</p>
                                    <p>学院: 电气院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>

                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm07_07.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title" style="line-height: 18px;">
                                    <span>物联网远程打印机ReRouter</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 一等奖</p>
                                    <p>项目负责人:张镭</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>蓝牙遥控插座</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 杨丽华</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm08.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>hadoop</span><span>监控</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源:互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <div class="clearfix"></div>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>宝贝养成计划</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 万抒豪</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm_03.png" alt=""/>
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span>机器学习</span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 李思缔</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm04.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>课程交换网站平台</span>
                                </div>
                                <div class="project_bright">
                                    <span>大数据</span><span>监控</span><span>物联网</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: 彭宇</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>

                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm10.jpg">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会跳舞的LED</span>
                                </div>
                                <div class="project_bright">
                                    <span>LED</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀奖</p>
                                    <p>项目负责人: 马晓媟</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm05.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>大数据互联环境监控系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>机器人</span><span> 机器学习 </span><span>人工智能</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 陈思思</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>会说话的一片云</span>
                                </div>
                                <div class="project_bright">
                                    <span>云计算</span><span>linux</span><span>openstack</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（省赛）</p>
                                    <p>荣获奖项: 二等奖</p>
                                    <p>项目负责人: 薛晓丹</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-03-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm02.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span>腕带识别系统</span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 创新创业项目</p>
                                    <p>荣获奖项: 优秀项目</p>
                                    <p>项目负责人: XXX</p>
                                    <p>学院: xxx</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2015-05-5</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                        <li><a href="${ctxFront}/page-projectshow3">
                            <div class="project_img">
                                <img src="${ctxImg}/yxxm01.png">
                            </div>
                            <div class="project_info">
                                <div class="project_title">
                                    <span> 蓝牙遥控插座 </span>
                                </div>
                                <div class="project_bright">
                                    <span>手势识别</span><span>可穿戴上设备</span>
                                </div>
                                <div class="project_description">
                                    <p>项目来源: 互联网+大赛（校赛）</p>
                                    <p>荣获奖项: 三等奖</p>
                                    <p>项目负责人: 龙思瑶</p>
                                    <p>学院: 计算机学院</p>
                                </div>
                            </div>
                            <div class="project_time clearfix">
                                <p>
                                    <i class="iconfont ico icon-rili"></i> <span>2016-3-25</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-yanjing1"></i> <span>140</span>
                                </p>
                                <p>
                                    <i class="iconfont ico icon-tuxing1"></i> <span>5</span>
                                </p>
                            </div>
                        </a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${ctxCommon}/common-js/jquery.min.js"></script>
<script src="${ctxOther}/pages/jquery.page.js"></script>

<script>
    var projectShow = new Vue({
        el: '#projectShowPages',
        name: 'ProjectShow',
        data: function () {
            return {
                list: [],
                list2: []
            }
        }
    })
</script>
<script>
    $("#page").Page({
        totalPages: 10,//分页总数
        liNums: 7,//分页的数字按钮数(建议取奇数)
        activeClass: 'activP', //active 类样式定义
        callBack: function (page) {
            //console.log(page)
        }
    });
    $(document).ready(
            function () {
                $('#TAB>li').on('click', function () {
                    if ($(this).find('>a').hasClass('disabled')) {
                        return false
                    }
                    $('#tab_chang .tab_select').eq($(this).index())
                            .show().siblings().hide();
                })
                var obj = []
                var obj2 = []
                $('#tab_chang').find('.tab_1').find('li').each(function (i) {
                    obj.push({
                        thumbnailUrl: $(this).find('.project_img>img').attr('src'),
                        link: $(this).find('>a').attr('href'),
                        title: $.trim($(this).find('.project_title').text()),
                        tags: [],
                        proName: '创新创业项目',
                        chargeName: $(this).find('.project_description p').eq(2).text().substr(5),
                        schoolName: $(this).find('.project_description p').eq(3).text().substr(2),
                        date: '2016-3-25',
                        look: '140',
                        wxGood: '5'
                    });
                    $(this).find('project_bright').each(function () {
                        obj[i]['tags'].push($(this).text())
                    })
                });
                $('#tab_chang').find('.tab_2').find('li').each(function (i) {
                    obj2.push({
                        thumbnailUrl: $(this).find('.project_img>img').attr('src'),
                        link: $(this).find('>a').attr('href'),
                        title: $.trim($(this).find('.project_title').text()),
                        tags: [],
                        proName: '创新创业项目',
                        chargeName: $(this).find('.project_description p').eq(2).text().substr(5),
                        schoolName: $(this).find('.project_description p').eq(3).text().substr(2),
                        date: '2016-3-25',
                        look: '140',
                        wxGood: '5'
                    });
                    $(this).find('project_bright').each(function () {
                        obj2[i]['tags'].push($(this).text())
                    })
                });
                projectShow.$data.list = obj
                projectShow.$data.list2 = obj2
            });
</script>
</body>
</html>