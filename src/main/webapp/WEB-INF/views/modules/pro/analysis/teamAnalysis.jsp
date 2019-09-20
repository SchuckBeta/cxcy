<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台</title>
    <%--<%@include file="/WEB-INF/views/include/backCommon.jsp" %>--%>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/echarts.min.js"></script>
    <script src="${ctxJs}/analysis/teamAnalysis.js"></script>
    <script src="${ctxJs}/echart/theme.default.js"></script>
    <style type="text/css">
        .team-teachers {
            width: 80%;
            margin: 0 auto;
        }

        .teacher-pro-chart {
            height: 400px;
        }

        .team-chart-box {
            width: 80%;
            margin: 0 auto;
        }

        .team-chart {
            height: 400px;
        }

        .team-per-chart-box {
            width: 400px;
            float: left;
        }

        .team-member-chart-box {
            margin-left: 400px;
        }

        .team-member-per-chart, .team-per-chart {
            height: 400px;
        }

        .teamPieItem {
            display: inline-block;
            width: 12px;
            height: 12px;
            margin-right: 4px;
            border-radius: 50%;
            overflow: hidden;
            vertical-align: middle;
        }

        .select-year-label{
            display: inline-block;
            line-height: 30px;
            padding: 0;
            margin: 0;
            vertical-align: top;
        }
        .select-year-label+select{
            margin: 0;
        }

        .echarts-wrap{
            border: 1px solid #ccc;
            padding: 0 15px;
            border-top: 0;
        }

        .echarts-wrap .row-header-fluid{
            padding:5px 15px;
            margin: 0 -15px;
            border: solid #ccc;
            border-width: 1px 0;
        }
        .echarts-wrap .row-header-fluid .title{
            line-height: 30px;
            margin: 0;
        }
        .teacher-chart-module{
            padding: 60px 0;
        }
        .team-chart-module{
            padding: 60px 0;
        }
        .team-statistics-module{
            padding: 60px 0;
        }
        .team-chart-title{
            padding-left: 30px;
            line-height: 30px;
            margin: 0;
        }
        .split-line{
            display: block;
            margin: 30px -15px;
            border-top: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div class="container-fluid" style="padding-bottom: 60px;">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>团队数据分析</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <div class="echarts-wrap">
        <div class="row-fluid row-header-fluid">
            <div class="span2">
                <p class="title">各学院导师统计</p>
            </div>
            <div class="span7">
                <div class="radio-wrap text-center">
                    <label class="radio inline">
                        <input type="radio" name="projectDsTp" checked value="1">项目导师趋势
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="projectDsTp" value="0">按项目类型统计
                    </label>
                </div>
            </div>
            <%--<div class="span3">--%>
                <%--<div class="text-right">--%>
                    <%--<label class="select-year-label">统计年份：</label>--%>
                    <%--<select class="input-small">--%>
                        <%--<option value="">全部</option>--%>
						<%--<c:forEach items="${years}" var="op">--%>
							<%--<option value="${op}">${op}年</option>--%>
						<%--</c:forEach>--%>
                    <%--</select>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
        <div class="teacher-chart-module">
            <div class="team-teachers">
                <div id="teacherProChart" class="teacher-pro-chart"></div>
            </div>
            <div class="team-teachers hide">
                <div id="teacherProTypeChart" class="teacher-pro-chart"></div>
            </div>
        </div>
        <div class="row-fluid row-header-fluid">
            <span class="span12 title">双创团队数及人数发展趋势</span>
        </div>
        <div class="team-chart-module">
            <div class="team-chart-box">
                <p class="team-chart-title">双创项目团队发展趋势</p>
                <div id="teamChart" class="team-chart"></div>
            </div>
            <span class="split-line"></span>
            <div class="team-chart-box">
                <p class="team-chart-title">双创人数发展趋势</p>
                <div id="teamMemberChart" class="team-chart"></div>
            </div>
        </div>
        <div class="row-fluid row-header-fluid">
            <div class="span2">
                <p class="title">团队统计</p>
            </div>
            <div class="span7">
                <div class="text-center">
                    <label class="radio inline">
                        <input type="radio" onchange="teamAnalysisChange()" name="teamAnalysisType"  checked value="1">团队数统计
                    </label>
                    <label class="radio inline">
                        <input type="radio" onchange="teamAnalysisChange()" name="teamAnalysisType" value="0">团队人数统计
                    </label>
                </div>
            </div>
            <%--<div class="span3">--%>
                <%--<div class="text-right">--%>
                    <%--<label class="select-year-label">统计年份：</label>--%>
                    <%--<select class="input-small" id="teamNumYear" onchange="teamAnalysisChange()">--%>
                        <%--<option value="">全部</option>--%>
						<%--<c:forEach items="${years}" var="op">--%>
							<%--<option value="${op}">${op}年</option>--%>
						<%--</c:forEach>--%>
                    <%--</select>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
        <div class="team-statistics-module">
            <div class="row-fluid">
                <div class="team-per-chart-box">
                    <div id="teamPercentageChart" class="team-per-chart"></div>
                </div>
                <div class="team-member-chart-box">
                    <div id="teamMemberPercentageChart" class="team-member-per-chart"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    $(function () {
        //项目指导趋势
        var $projectDsTps = $('input[name="projectDsTp"]');
        var teacherChartModule = $('.teacher-chart-module');
        var teacherProCanvas = document.getElementById('teacherProChart');
        var teacherProChart = echarts.init(teacherProCanvas,'macarons');
        //项目类型
        var teacherProTypeCanvas = document.getElementById('teacherProTypeChart');
        var teacherProTypeChart;


        //项目趋势chart options 折线图
        function projectTrendsOption(legend, xAxisData, seriesData) {
            return {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: legend ,
                    left: 'center',
//                    orient: 'vertical',
                    y: 'bottom'
                },
                grid: [{
                    y: 20,
                    x: 'center',
                    width: '90%',
                    height: '80%'
                }],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData
            }
        }

        //设置项目趋势chart options折线图
        function setProjectTrendsChart(url, params,chart) {
            $.ajax({
                type : "POST",
                url : url,
                data : {"year":''},
                success : function(data) {
                    var dataList=data;
                    var legendData = new Array();
                    var categorydata=dataList[0].categories;
                    for(var i=0;i<dataList.length;i++) {
                        legendData[i] = dataList[i].name;
                    }
                    var options = projectTrendsOption(legendData,categorydata,data);
                    chart.setOption(options)
                }
            });
        }

        setProjectTrendsChart("/a/analysis/teamAnalysis/getTeamTeacherByYear",'',teacherProChart);







        //项目类型统计
        function projectTypeOption(legend, rotate, xAxisData, seriesData) {
            return {
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: legend
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: [{
                    y: 30,
                    x:'center',
                    width: '90%',
                    height: '50%'
                }],
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: rotate || 50
                    },
                    data: xAxisData
                },
                yAxis: [
                    {
                        type: 'value',
                        name: '人'
                    }
                ],
                series:seriesData
            };
        }
        //设置项目类型导师 option 柱状图
        function setProjectTypeOption(url, params,chart) {
            $.ajax({
                type : "POST",
                url : url,
                data : {"year":''},
                success : function(data) {
                    var dataList=data.seriesData;
                    var legendData = new Array();
                    var  categorydata=data.xAxisData;
                    for(var i=0;i<dataList.length;i++) {
                        legendData[i] = dataList[i].name;
                    }
                    var options = projectTypeOption(legendData,'',categorydata,dataList);
                    chart.setOption(options)
                }
            });
        }

        $projectDsTps.on('click', function (e) {
            var index = $projectDsTps.index($(this));
            var isLoad = $(this).attr('data-chart-load');
            teacherChartModule.children().eq(index).removeClass('hide').siblings().addClass('hide');
            if(index > 0 && !isLoad){
                $(this).attr('data-chart-load', 'load');
                teacherProTypeChart = echarts.init(teacherProTypeCanvas, 'macarons');
                setProjectTypeOption("/a/analysis/teamAnalysis/getTeamTeacherByCollege",'',teacherProTypeChart);
            }
        });

        //项目团队发张
        var teamDevelopCanvas = document.getElementById('teamChart');
        var teamDevelopChart = echarts.init(teamDevelopCanvas,'macarons');

        

        //设置项目团队发展趋势 chart option 折线图

        function setProjectDevelopChart(url, params,chart) {

            $.ajax({
                type : "POST",
                url : url,
                data : {"year":''},
                success : function(data) {
                    var dataList=data;
                    var legendData = new Array();
                    var categorydata=dataList[0].categories;
                    for(var i=0;i<dataList.length;i++) {
                        legendData[i] = dataList[i].name;
                    }
                    var options = projectTeamDevelopOption('项目团队发展趋势',legendData,categorydata,data,'');
                    chart.setOption(options)
                }
            });

            /*var options = projectTeamDevelopOption('项目团队发展趋势');
            teamDevelopChart.setOption(options)*/
        }
        setProjectDevelopChart('/a/analysis/teamAnalysis/getTeamNumByYear','',teamDevelopChart);
    })

</script>

</body>
</html>