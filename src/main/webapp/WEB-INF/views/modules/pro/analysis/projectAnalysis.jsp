<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>双创项目统计</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
    <script src="${ctxJs}/echarts.min.js"></script>
    <script src="${ctxJs}/scxiangmu.js"></script>
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

        .title-bar .radio {
            height: 20px;
            line-height: 20px;
            margin-top: 11px;
            margin-bottom: 11px;
        }
    </style>
</head>
<body style="padding-bottom: 60px;">
<%--<div class="mybreadcrumbs"><span>双创项目分析</span></div>--%>
<div class="container-fluid">
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <div class="view-content">
        <div class="container-fluid">
            <div class="row title-bar">
                <div class="col-md-12">
                    双创项目申报年增长趋势
                </div>
            </div>
            <div class="row" style="border-bottom: 1px solid #ccc">
                <div class="col-md-12">
                    <div id="annalsChart" style="width: 100%;height:400px;padding:30px 0 40px;"></div>
                </div>
            </div>
            <div class="row title-bar">
                <div class="col-md-6 print-md-6">
                    双创项目分布统计
                </div>
                <div class="col-md-6 text-right">
                    统计年份：
                    <select id="proScatterYear">
                        <option value="">全部</option>
                        <c:forEach items="${years}" var="year">
                            <option value="${year}">${year}年</option>
                        </c:forEach>
                        <%--<option value="2017">2017年</option>
                        <option value="2016">2016年</option>
                        <option value="2015">2015年</option>
                        <option value="2014">2014年</option>--%>
                    </select>
                </div>
            </div>
            <div class="row" style="border: solid #ccc;border-width: 1px 0">
                <div class="col-md-6">
                    <div id="proportionsChart" style="width: 100%; height:500px; padding:30px 0 40px;"></div>
                </div>
                <div class="col-md-6">
                    <div id="distributionSubChart" style="width: 100%; height:500px; padding:30px 0 40px;"></div>
                </div>
            </div>
            <div class="row title-bar">
                <div class="col-sm-6 col-md-3">
                    各学院项目立项统计
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="radio">
                        <label>
                            <input type="radio" name="optionsRadios" id="optionsRadios1" value="1" checked>双创项目立项统计
                        </label>
                    </div>
                </div>
                <div class="col-sm-6 col-md-3">
                    <div class="radio">
                        <label>
                            <input type="radio" name="optionsRadios" id="optionsRadios2" value="2">双创项目类型统计
                        </label>
                    </div>
                </div>
                <div class="col-md-3 text-right">
                    统计年份：
                    <select id="approvalYear">
                        <option value="">全部</option>
                        <c:forEach items="${years}" var="year">
                            <option value="${year}">${year}年</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row approval-charts" style="border: solid #ccc;border-width: 1px 0">
                <div class="col-md-12">
                    <div id="approvalsSubChart" style="width:100%; height:500px; padding:30px 0 40px;"></div>
                </div>
                <div class="col-md-12 hide">
                    <div id="approvalsChart" style="width:100%; height:500px; padding:30px 0 40px;"></div>
                </div>
            </div>
            <%--<div class="row title-bar">
                <div class="col-md-6">
                    项目经费统计分析
                </div>
                <div class="col-md-6 text-right">
                    统计年份：
                    <select id="proFundsYear">
                        <option value="">全部</option>
                        <option value="2017">2017年</option>
                        <option value="2016">2016年</option>
                        <option value="2015">2015年</option>
                        <option value="2014">2014年</option>
                    </select>
                </div>
            </div>--%>
            <%--<div class="row" style="border-top: 1px solid #ccc;">
                <div class="col-md-6">
                    <div id="fundsChart" style="width:100%;height: 400px;padding: 30px 0 40px"></div>
                </div>
                <div class="col-md-6">
                    <div id="fundsAnalyzeChart" style="width:100%;height: 400px;padding: 30px 0 40px"></div>
                </div>
            </div>--%>
        </div>
    </div>
</div>
</body>

<script src="${ctxJs}/goback.js" type="text/javascript" charset="UTF-8"></script>
</html>