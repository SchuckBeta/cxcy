<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台</title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <script src="${ctxJs}/echarts.min.js"></script>
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
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>项目分析</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="echarts-wrap">
        <div class="row-fluid row-header-fluid">
            <div class="span2">
                <p class="title">各学院导师统计</p>
            </div>
            <div class="span8">
                <div class="radio-wrap text-center">
                    <label class="radio inline">
                        <input type="radio" checked value="1">项目指导趋势
                    </label>
                    <label class="radio inline">
                        <input type="radio" value="0">按项目类型统计
                    </label>
                </div>
            </div>
            <div class="span2">
                <div class="text-right">
                    <label class="select-year-label">统计年份：</label>
                    <select class="input-small">
                        <option>-全部-</option>
                        <option selected>2017年</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="teacher-chart-module">
            <div class="team-teachers">
                <div id="teacherProChart" class="teacher-pro-chart"></div>
            </div>
            <div class="team-teachers">
                <div id="teacherProTypeChart" class="teacher-pro-chart"></div>
            </div>
        </div>
        <div class="row-fluid row-header-fluid">
            <span class="span12 title">团队数及人数发展趋势</span>
        </div>
        <div class="team-chart-module">
            <div class="team-chart-box">
                <p class="team-chart-title">项目团队发展趋势</p>
                <div id="teamChart" class="team-chart"></div>
            </div>
            <span class="split-line"></span>
            <div class="team-chart-box">
                <p class="team-chart-title">人数发展趋势</p>
                <div id="teamMemberChart" class="team-chart"></div>
            </div>
        </div>
        <div class="row-fluid row-header-fluid">
            <div class="span2">
                <p class="title">团队统计</p>
            </div>
            <div class="span8">
                <div class="text-center">
                    <label class="radio inline">
                        <input type="radio" checked value="1">团队数统计
                    </label>
                    <label class="radio inline">
                        <input type="radio" value="0">团队人数统计
                    </label>
                </div>
            </div>
            <div class="span2">
                <div class="text-right">
                    <label class="select-year-label">统计年份：</label>
                    <select class="input-small">
                        <option>-全部-</option>
                        <option selected>2017年</option>
                    </select>
                </div>
            </div>
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
        var teacherProCanvas = document.getElementById('teacherProChart');
        var teacherProChart = echarts.init(teacherProCanvas);

        //项目趋势chart options 折线图
        function projectTrendsOption(legend, xAxisData, seriesData, colors) {
            return {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: legend || ['大创项目导师发展趋势', '开放实验项目导师发展趋势', '创新型实验项目导师发展趋势'],
                    left: 'center',
//                    orient: 'vertical',
                    y: 'bottom'
                },
                grid: [{
                    y: 30,
                    x: 'center',
                    width: '80%',
                    height: '80%'
                }],
                color: colors || ['#9bbb59', '#4bacc6', '#f79646'],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData || ['2014年', '2015年', '2016年', '2017年']
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData || [{
                    name: '大创项目导师发展趋势',
                    type: 'line',
                    data: [12, 10, 22, 27, 44]
                }, {
                    name: '开放实验项目导师发展趋势',
                    type: 'line',
                    data: [12, 3, 10, 27, 44]
                }, {
                    name: '创新型实验项目导师发展趋势',
                    type: 'line',
                    data: [12, 18, 29, 30, 44]
                }]
            }
        }

        //设置项目趋势chart options折线图
        function setProjectTrendsChart(url, params) {
            var options = projectTrendsOption();
            teacherProChart.setOption(options)
        }

        setProjectTrendsChart();

        //项目类型
        var teacherProTypeCanvas = document.getElementById('teacherProTypeChart');
        var teacherProTypeChart = echarts.init(teacherProTypeCanvas);


        //项目类型统计
        function projectTypeOption(legend, rotate, colors, xAxisData, seriesData) {
            return {
                legend: {
                    x: 'center',
                    y: 'bottom',
                    data: legend || ['大创项目导师人数（总人数）', '开放实验项目导师人数（总人数）', '创新型实验项目导师人数（总人数）']
                },
                tooltip: {
                    trigger: 'axis'
                },
                grid: [{
                    height: '50%'
                }],
                color: colors || ['#9bbb59', '#4bacc6', '#f79646'],
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: rotate || 30
                    },
                    data: xAxisData || ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
                },
                yAxis: [
                    {
                        type: 'value',
                        name: '人'
                    }
                ],
                series: [
                    {
                        name: '大创项目导师人数（总人数）',
                        type: 'bar',
                        barWidth: 30,
                        data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
                        stack: '导师人数'
                    }, {
                        name: '开放实验项目导师人数（总人数）',
                        type: 'bar',
                        barWidth: 30,
                        data: [10, 6, 10, 25, 10, 20, 10, 11, 18, 20, 10, 5, 12, 10, 10, 3, 1, 12, 2, 10],
                        stack: '导师人数'
                    },
                    {
                        name: '创新型实验项目导师人数（总人数）',
                        type: 'bar',
                        barWidth: 30,
                        data: [10, 20, 10, 10, 10, 20, 10, 13, 6, 20, 10, 5, 10, 10, 10, 12, 10, 6, 8, 15],
                        stack: '导师人数'
                    }
                ]
            };
        }

        //设置项目类型导师 option 柱状图
        function setProjectTypeOption(url, params) {
            var options = projectTypeOption();
            teacherProTypeChart.setOption(options)
        }

        setProjectTypeOption();

        //项目团队发张
        var teamDevelopCanvas = document.getElementById('teamChart');
        var teamDevelopChart = echarts.init(teamDevelopCanvas);

        //人数发展趋势
        var teamMemberCanvas = document.getElementById('teamMemberChart');
        var teamMemberChart = echarts.init(teamMemberCanvas);

        //项目团队发展hhe人数趋势
        function projectTeamDevelopOption(title, legend, xAxisData, seriesData, colors) {
            return {
                title: {
//                    text: title,
//                    left: 0
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 'right',
                    y: 'center',
                    orient: 'vertical',
                    data: legend || ['大创项目团队', '开放实验项目团队', '创新型实验项目团队', '互联网+大赛人数']
                },
                grid: [{
                    top: 30,
                    width: '80%'
                }],
                color: colors || ['#b5cb90', '#f79747', '#c0504d', '#8064a3'],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: xAxisData || ['2014年', '2015年', '2016年', '2017年', '2018年']
                },
                yAxis: {
                    type: 'value'
                },
                series: seriesData || [{
                    name: '大创项目团队',
                    type: 'line',
                    data: [12, 10, 22, 27, 44]
                }, {
                    name: '开放实验项目团队',
                    type: 'line',
                    data: [12, 3, 10, 27, 44]
                }, {
                    name: '创新型实验项目团队',
                    type: 'line',
                    data: [12, 18, 29, 30, 44]
                }, {
                    name: '互联网+大赛人数',
                    type: 'line',
                    data: [12, 50, 29, 30, 44]
                }]
            }
        }

        //设置项目团队发展趋势 chart option 折线图
        function setProjectDevelopChart(url, params) {
            var options = projectTeamDevelopOption('项目团队发展趋势');
            teamDevelopChart.setOption(options)
        }

        //设置项目人数发展趋势
        function setProjectMemberChart(url, params) {
            var options = projectTeamDevelopOption('人数发展趋势');
            teamMemberChart.setOption(options)
        }

        setProjectDevelopChart();
        setProjectMemberChart();

        //团队统计
        var teamPercentageCanvas = document.getElementById('teamPercentageChart');
        var teamPercentageChart = echarts.init(teamPercentageCanvas);

        var teamMemberPercentageCanvas = document.getElementById('teamMemberPercentageChart');
        var teamMemberPercentageChart = echarts.init(teamMemberPercentageCanvas);

        //团队统计饼图 option
        function teamPercentOption(color, legend, seriesData) {
            return {
                tooltip: {
                    trigger: 'item',
                    formatter: function (params) {
                        var name = params.name;
                        return '<span class="teamPieItem" style="background-color: ' + params.color + '"></span>' + name.substring(0, name.indexOf('(')) + params.value + '人'
                    }
                },
                selectedMode: 'single',
                legend: [{
                    x: 'center',
                    y: 'bottom',
                    orient: 'vertical',
                    data: legend || ['大创项目团队数(356)', '开放项目实验项目团队数(245)', '开放项目实验项目团队数(76)', '互联网+大赛团队数(500)']
                }],
                color: color || ['#8064a2', '#4f81bd', '#c0504d', '#9bbb59'],
                grid: [{
                    x: '0'
                }],
                series: seriesData || {
                    type: 'pie',
                    center: ['50%', 130],
                    radius: [0, '50%'],
                    label: {
                        normal: {
                            position: 'inner',
                            formatter: '{d}%'
                        }
                    },
                    data: [{
                        name: '大创项目团队数(356)',
                        value: 356
                    }, {
                        name: '开放项目实验项目团队数(245)',
                        value: 245
                    }, {
                        name: '开放项目实验项目团队数(76)',
                        value: 76
                    }, {
                        name: '互联网+大赛团队数(500)',
                        value: 500
                    }]
                }
            }
        }

        //设置团队饼图option
        function setTeamPercentChart(url, params) {
            var option = teamPercentOption();
            teamPercentageChart.setOption(option)
        }

        setTeamPercentChart();

        //团队统计折线图
        function teamMemberPerOption(color,rotate, xAxisData, seriesData) {
            return {
                tooltip: {
                    trigger: 'axis'
                },
                color: color ||  ['#8064a2', '#4f81bd', '#c0504d', '#9bbb59'],
                grid: {
                  height: '55%'
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: rotate || 30
                    },
                    data: xAxisData || ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
                },
                yAxis: [
                    {
                        type: 'value',
                        name: '人'
                    }
                ],
                series: seriesData || [
                    {
                        name: '大创项目团队数',
                        type: 'bar',
                        barWidth: 15,
                        data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
                        stack: '团队人数占比'
                    }, {
                        name: '开放实验项目团队数',
                        type: 'bar',
                        barWidth: 15,
                        data: [10, 6, 10, 25, 10, 20, 10, 11, 18, 20, 10, 5, 12, 10, 10, 3, 1, 12, 2, 10],
                        stack: '团队人数占比'
                    },
                    {
                        name: '创新型实验项目团队数',
                        type: 'bar',
                        barWidth: 15,
                        data: [10, 20, 10, 10, 10, 20, 10, 13, 6, 20, 10, 5, 10, 10, 10, 12, 10, 6, 8, 15],
                        stack: '团队人数占比'
                    },
                    {
                        name: '互联网+大赛团队数',
                        type: 'bar',
                        barWidth: 15,
                        data: [10, 20, 10, 10, 10, 20, 10, 13, 6, 40, 10, 5, 10, 10, 10, 12, 10, 6, 8, 15],
                        stack: '团队人数占比'
                    }
                ]
            };
        }

        //团队统计折线图
        function teamMemberPerChart(url, params) {
            var option = teamMemberPerOption();
            teamMemberPercentageChart.setOption(option)
        }
        teamMemberPerChart()


    })

</script>

</body>
</html>