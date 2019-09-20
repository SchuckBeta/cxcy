<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>后台</title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <script src="${ctxJs}/echarts.min.js"></script>
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

        .select-year-label {
            display: inline-block;
            line-height: 30px;
            padding: 0;
            margin: 0;
            vertical-align: top;
        }

        .select-year-label + select {
            margin: 0;
        }

        .echarts-wrap {
            border: 1px solid #ccc;
            padding: 0 15px;
            border-top: 0;
        }

        .echarts-wrap .row-header-fluid {
            padding: 5px 15px;
            margin: 0 -15px;
            border: solid #ccc;
            border-width: 1px 0;
        }

        .echarts-wrap .row-header-fluid .title {
            line-height: 30px;
            margin: 0;
        }

        .teacher-chart-module {
            padding: 60px 0;
        }

        .team-chart-module {
            padding: 60px 0;
        }

        .team-statistics-module {
            padding: 60px 0;
        }

        .team-chart-title {
            padding-left: 30px;
            line-height: 30px;
            margin: 0;
        }

        .split-line {
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
            <span>团队统计</span>
            <i class="line weight-line"></i>
        </div>
    </div>
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
            <div class="span7">
                <div class="text-center">
                    <label class="radio inline">
                        <input type="radio" name="teamAnalysisType" checked value="1">团队数统计
                    </label>
                    <label class="radio inline">
                        <input type="radio" name="teamAnalysisType" value="0">团队人数统计
                    </label>
                </div>
            </div>
        </div>
        <div class="team-statistics-module">
            <div class="row-fluid">
                <div class="team-per-chart-box" style="width: 100%">
                    <div id="teamPercentageChart" style="width: 100%" class="team-per-chart"></div>
                </div>
                <div class="team-member-chart-box hide" style="width: 100%">
                    <div id="teamMemberPercentageChart" class="team-member-per-chart"></div>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    $(function () {
        var $projectDsTps = $('input[name="projectDsTp"]');
        var teacherProChartEle = document.getElementById('teacherProChart');
        var teacherProTypeChartEle = document.getElementById('teacherProTypeChart');
        var $teacherChartModule = $('.teacher-chart-module');
        var teacherProChart, teacherProTypeChart;
        $projectDsTps.on('change', function () {
            var teacherCharts = $teacherChartModule.children();
            var index = $projectDsTps.index($(this));
            var val = $(this).val();
            teacherCharts.eq(index).removeClass('hide').siblings().addClass('hide');
            setTimeout(function () {
                if (val == 1) {
                    if (!teacherProChart) {
                        teacherProChart = echarts.init(teacherProChartEle, 'macarons');
                    }
                    autoTeacherProChart()
                } else {
                    if (!teacherProTypeChart) {
                        teacherProTypeChart = echarts.init(teacherProTypeChartEle, 'macarons');
                    }
                    autoTeacherProTypeChart()
                }
            }, 0)
        });

        function autoTeacherProChart() {
            teacherProChart.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 'center',
                    bottom: 30,
                    data: ['创业项目', '大创项目', '蓝桥杯', '互联网+大赛']
                },
                grid: [{
                    x: 'center',
                    y: 30,
                    width: '90%',
                    height: '60%'
                }],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['2014年', '2015年', '2016年', '2017年']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type: 'line',
                    name: '创业项目',
                    data: [30, 60, 80, 100]
                }, {
                    type: 'line',
                    name: '大创项目',
                    data: [50, 60, 80, 120]
                }, {
                    type: 'line',
                    name: '蓝桥杯',
                    data: [30, 80, 120, 200]
                }, {
                    type: 'line',
                    name: '互联网+大赛',
                    data: [30, 80, 120, 120]
                }]
            });
        }

        function autoTeacherProTypeChart() {
            var legendData = ['国创项目申报数', '开放实验项目申报数', '创新性实验项目申报数', '国创项目立项数'];
            var seriesData = [];
            var xAxisData = ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院'];
            legendData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    name: item,
                    stack: '项目',
                    data: (function (xAxisData) {
                        var list = [];
                        xAxisData.forEach(function (item, i) {
                            list.push(Math.floor(Math.random() * 500))
                        });
                        return list
                    })(xAxisData)
                })
            });
            teacherProTypeChart.setOption({
                legend: [{
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    x: 'center',
                    y: 30,
                    width: '90%',
                    height: '60%'
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: 30
                    },
                    data: xAxisData
                },
                yAxis: [
                    {
                        type: 'value',
                        name: ''
                    }
                ],
                series: seriesData
            })
        }


        $('input[name="projectDsTp"]:checked').trigger('change');


        var teamChartEle = document.getElementById('teamChart');
        var teamChart = echarts.init(teamChartEle, 'macarons');
        var teamMemberChartEle = document.getElementById('teamMemberChart');
        var teamMemberChart = echarts.init(teamMemberChartEle, 'macarons');

        function autoTeamChart() {
            teamChart.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 'center',
                    bottom: 30,
                    data: ['创业项目', '大创项目', '蓝桥杯', '互联网+大赛']
                },
                grid: [{
                    x: 'center',
                    y: 30,
                    width: '90%',
                    height: '60%'
                }],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['2014年', '2015年', '2016年', '2017年']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type: 'line',
                    name: '创业项目',
                    data: [30, 60, 80, 100]
                }, {
                    type: 'line',
                    name: '大创项目',
                    data: [50, 60, 80, 120]
                }, {
                    type: 'line',
                    name: '蓝桥杯',
                    data: [30, 80, 120, 200]
                }, {
                    type: 'line',
                    name: '互联网+大赛',
                    data: [30, 80, 120, 120]
                }]
            });
        }

        function autoTeamMemberChart() {
            teamMemberChart.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 'center',
                    bottom: 30,
                    data: ['互联网+大赛', '大赛2.0', '大创项目']
                },
                grid: [{
                    x: 'center',
                    y: 30,
                    width: '90%',
                    height: '60%'
                }],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: ['2014年', '2015年', '2016年', '2017年']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    type: 'line',
                    name: '互联网+大赛',
                    data: [30, 60, 80, 100]
                }, {
                    type: 'line',
                    name: '大赛2.0',
                    data: [50, 60, 80, 120]
                }, {
                    type: 'line',
                    name: '大创项目',
                    data: [30, 80, 120, 200]
                }]
            });
        }
        autoTeamChart();
        autoTeamMemberChart();

        var $teamAnalysisTypes = $('input[name="teamAnalysisType"]');
        var teamPercentageChart, teamMemberPercentageChart;
        var teamPercentageChartEle = document.getElementById('teamPercentageChart');
        var teamMemberPercentageChartEle = document.getElementById('teamMemberPercentageChart');
        var $teamCharts = $('.team-statistics-module').children();

        $teamAnalysisTypes.on('change', function () {
            var $teamChartBoxs = $teamCharts.children();
            var index = $teamAnalysisTypes.index($(this));
            var val = $(this).val();
            $teamChartBoxs.eq(index).removeClass('hide').siblings().addClass('hide');
            setTimeout(function () {
                if (val == 1) {
                    if (!teamMemberPercentageChart) {
                        teamPercentageChart = echarts.init(teamPercentageChartEle, 'macarons');
                    }
                    autoTeamPercentageChart()
                } else {
                    if (!teamMemberPercentageChart) {
                        teamMemberPercentageChart = echarts.init(teamMemberPercentageChartEle, 'macarons');
                    }
                    autoTeamMemberPercentageChart()
                }
            }, 0)
        });
        
        function autoTeamPercentageChart() {
            var legendData = ['大创项目团队', '互联网+大赛团队', '大赛2.0团队'];
            var seriesData = [];
            var pieData = [];
            var pie = {
                type: 'pie',
                center: ['20%', '50%'],
                radius: [0, '50%'],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{d}%'
                    }
                },
                data: []
            };
            var xAxisData = ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院'];

            legendData.forEach(function (item, i) {
                pieData.push({
                    name: item,
                    value: Math.floor((i + 1) * Math.random() * 5000)
                });
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    stack: '学院',
                    name: item,
                    data: (function (xAxisData) {
                        var list = [];
                        xAxisData.forEach(function (item, i) {
                            list.push(Math.floor(Math.random() * 500))
                        });
                        return list
                    })(xAxisData)
                })
            });
            pie['data'] = pieData;
            seriesData.push(pie);
            teamPercentageChart.setOption({
                title: [{
                    show: false,
                    y: 30,
                    x: 'center',
                    text: '双创入围赛制分布'
                }, {
                    y: 30,
                    x: 'center',
                    text: '双创大赛各类学生分布统计'

                }],
                legend: [{
                    show: false,
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }, {
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: [{
                    y: 'center',
                    x: '40%',
                    right: 30,
                    height: '55%'
                }],
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: 30
                    },
                    data: xAxisData
                },
                yAxis: [
                    {
                        type: 'value',
                        name: ''
                    }
                ],
                series: seriesData
            })

        }

        function autoTeamMemberPercentageChart() {
            var legendData = ['大创项目人数', '互联网+大赛人数', '大赛2.0人数'];
            var seriesData = [];
            var pieData = [];
            var pie = {
                type: 'pie',
                center: ['20%', '50%'],
                radius: [0, '50%'],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{d}%'
                    }
                },
                data: []
            };
            var xAxisData = ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院'];

            legendData.forEach(function (item, i) {
                pieData.push({
                    name: item,
                    value: Math.floor((i + 1) * Math.random() * 5000)
                });
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    stack: '学院',
                    name: item,
                    data: (function (xAxisData) {
                        var list = [];
                        xAxisData.forEach(function (item, i) {
                            list.push(Math.floor(Math.random() * 500))
                        });
                        return list
                    })(xAxisData)
                })
            });
            pie['data'] = pieData;
            seriesData.push(pie);
            teamMemberPercentageChart.setOption({
                title: [{
                    show: false,
                    y: 30,
                    x: 'center',
                    text: '双创入围赛制分布'
                }, {
                    y: 30,
                    x: 'center',
                    text: '双创大赛各类学生分布统计'

                }],
                legend: [{
                    show: false,
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }, {
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: [{
                    y: 'center',
                    x: '40%',
                    right: 30,
                    height: '55%'
                }],
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        interval: 0,
                        rotate: 30
                    },
                    data: xAxisData
                },
                yAxis: [
                    {
                        type: 'value',
                        name: ''
                    }
                ],
                series: seriesData
            })

        }

        $('input[name="teamAnalysisType"]:checked').trigger('change');

    })

</script>

</body>
</html>