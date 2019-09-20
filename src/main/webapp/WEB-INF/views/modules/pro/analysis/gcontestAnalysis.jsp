<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>双创大赛分析</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
    <script src="${ctxJs}/echarts.min.js"></script>
    <script src="${ctxJs}/scdasai.js"></script>
    <style type="text/css">
        .mybreadcrumbs {
            margin: 20px 1.5em;
            margin-left: 27px;
            border-bottom: 3px solid #f4e6d4;
        }

        .mybreadcrumbs span {
            position: relative;
            top: 9px;
            font-size: 16px;
            font-weight: bold;
            color: #e9432d;
            display: inline-block;
            background-color: #FFF;
            padding-right: 10px;
        }

        .view-content {
            /*padding: 0 27px;*/
        }

        .container-fluid {
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .title-bar {
            line-height: 3;
            font-family: "Microsoft YaHei";
        }

        .mt-inline {
            display: inline-block;
        }

        .mt-inline:first-child {
            margin-right: 30px;
        }
    </style>
</head>
<body style="padding-bottom: 60px;">
<%--<div class="mybreadcrumbs"><span>双创大赛分析</span></div>--%>
<div class="container-fluid">
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <div class="view-content">
        <div class="container-fluid">
            <div class="row title-bar">
                <div class="col-md-12">双创大赛申报年增长趋势分析</div>
            </div>
            <div class="row" style="border: solid #ccc;border-width: 1px 0">
                <div class="col-md-12">
                    <div id="tendencyChart" style="width:100%; height:450px; padding-top:30px;"></div>
                </div>
            </div>
            <div class="row title-bar">
                <div class="col-md-6">
                    大赛统计分析
                </div>
                <div class="col-md-6 text-right">
                    <div class="mt-inline">
                        选择统计的大赛:
                        <select id="matchTypes">
                            <option value="">全部</option>
                            <c:forEach items="${fns:getDictList('competition_type')}" var="op">
                                <option value="${op.value}">${op.label}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mt-inline">
                        统计年份：
                        <select id="seminaryMatchYear">
                            <option value="">全部</option>
                            <c:forEach items="${years}" var="op">
                                <option value="${op}">${op}年</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="row" style="border: solid #ccc;border-width: 1px 0">
                <div>
                    <div id="seminaryMatch" style="width: 100%;height: 500px;padding: 20px 0 30px"></div>
                </div>
                <p class="text-center seminary-chart-no-data hide" style="height: 500px; line-height: 200px;">
                    没有数据，请重新选择</p>
            </div>
            <div class="row title-bar">
                <div class="col-md-6">
                    双创学生参赛统计
                </div>
                <div class="col-md-6 text-right">
                    统计年份：
                    <select id="matchYear">
                        <option value="">全部</option>
                        <c:forEach items="${years}" var="op">
                            <option value="${op}">${op}年</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row" style="border-top: 1px solid #ccc;">
                <div class="col-md-12">
                    <div>
                        <div id="stuChart" style="width:100%; height:500px; padding:20px 0 30px;"></div>
                    </div>
                    <p class="text-center stu-chart-no-data hide" style="height: 500px; line-height: 200px;">
                        没有数据，请重新选择</p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script src="${ctxJs}/goback.js" type="text/javascript" charset="UTF-8"></script>
</html>