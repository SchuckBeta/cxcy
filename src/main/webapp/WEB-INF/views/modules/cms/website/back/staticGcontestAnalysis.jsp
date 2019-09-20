<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>双创大赛分析</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
    <script src="${ctxJs}/echarts.min.js"></script>
    <%--<script src="${ctxJs}/scdasai.js"></script>--%>
    <script src="${ctxJs}/echart/theme.default.js"></script>
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
            padding: 0 27px;
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
<div class="mybreadcrumbs"><span>双创大赛分析</span></div>
<div class="view-content">
    <div class="container-fluid">
        <div class="row title-bar">
            <div class="col-md-12">双创大赛趋势分析</div>
        </div>
        <div class="row" style="border: solid #ccc;border-width: 1px 0">
            <div class="col-md-12">
                <div id="tendencyChart" style="width:100%; height:450px;"></div>
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
                        <option value="2017">创青春</option>
                        <option value="2016">挑战杯</option>
                        <option value="2015">蓝桥杯</option>
                        <option value="2014">新大赛</option>
                    </select>
                </div>
                <div class="mt-inline">
                    统计年份：
                    <select id="seminaryMatchYear">
                        <option value="">全部</option>
                        <option value="2017">2017年</option>
                        <option value="2016">2016年</option>
                        <option value="2015">2015年</option>
                        <option value="2014">2014年</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row" style="border: solid #ccc;border-width: 1px 0">
            <div class="col-md-6">
                <div id="seminaryMatch" style="width: 100%;height: 500px;"></div>
            </div>
            <div class="col-md-6">
                <div id="seminaryMatch2" style="width: 100%;height: 500px;"></div>
            </div>
        </div>
        <div class="row title-bar">
            <div class="col-md-6">
                双创学生参赛统计
            </div>
            <div class="col-md-6 text-right">
                统计年份：
                <select id="matchYear">
                    <option value="">全部</option>
                    <option value="2017">2017年</option>
                    <option value="2016">2016年</option>
                    <option value="2015">2015年</option>
                    <option value="2014">2014年</option>
                </select>
            </div>
        </div>
        <div class="row" style="border-top: 1px solid #ccc;">
            <div class="col-md-12">
                <div id="stuChartBar" style="width:100%; height:500px;"></div>
            </div>
        </div>
    </div>
</div>
<script>


    $(function () {
        var tendencyChartEle = document.getElementById('tendencyChart');
        var tendencyChart = echarts.init(tendencyChartEle, 'macarons');

        function getTotal(arr) {
            return arr.reduce(function (total, next) {
                return total+next;
            }, 0)
        }

        tendencyChart.setOption({
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['互联网+大赛','挑战杯','创青春','其它'],
                right: 0,
                orient: 'vertical',
                y: 'center'
            },
            grid: [{
                x: 'center',
                y: 'center',
                width: '75%',
                height: '70%'
            }],
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: ['2015年', '2016年', '2017年']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                type: 'line',
                name: '互联网+大赛',
                data: [117,375,751]
            }, {
                type: 'line',
                name: '挑战杯',
                data: [254,435,679]
            }, {
                type: 'line',
                name: '创青春',
                data: [153,208,270]
            }, {
                type: 'line',
                name: '其它',
                data: [270,273,289]
            }]
        });


        var seminaryMatchChartEle1 = document.getElementById('seminaryMatch');
        var seminaryMatchChart = echarts.init(seminaryMatchChartEle1, 'macarons');
        var seminaryMatchChartEle2 = document.getElementById('seminaryMatch2');
        var seminaryMatchChart2 = echarts.init(seminaryMatchChartEle2, 'macarons');
        var $seminaryMatchYear = $('#seminaryMatchYear');
        var $matchTypes = $('#matchTypes');
        $seminaryMatchYear.on('change', function (e) {
            pieSeminaryMatchChart();
            barSeminaryMatchChart()
        });
        $matchTypes.on('change', function (e) {
            pieSeminaryMatchChart();
            barSeminaryMatchChart()
        });

        function pieSeminaryMatchChart() {
            var legendData = ['互联网+大赛', '挑战杯', '创青春', '其它'];
            var seriesData = [];
//            legendData.forEach(function (item, i) {
//                seriesData.push({
//                    name: item,
//                    value: Math.floor((i + 1) * Math.random() * 5000)
//                })
//            });


            seriesData = [{
                name: '互联网+大赛',
                value: getTotal([112,105,32,34,26,74,69,87,97,42,48,25,0])
            },{
                name: '挑战杯',
                value: getTotal([75,86,64,35,31,34,49,72,47,46,37,39,64])
            },{
                name: '创青春',
                value:getTotal([42,23,27,16,12,15,27,31,24,23,13,17,0])
            },{
                name: '其它',
                value: getTotal([18,16,13,26,10,28,29,17,23,27,34,14,34])
            }]
            seminaryMatchChart.setOption({
                width: 'auto',
                title: {
                    y: 30,
                    x: 'center',
                    text: '双创大赛类型分析'
                },
                tooltip: {
                    trigger: 'item'
                },
                selectedMode: 'single',
                legend: [{
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                grid: [{
                    x: 'center',
                    y: 'center'
                }],
                series: {
                    type: 'pie',
                    center: ['50%', '50%'],
                    radius: [0, '50%'],
                    label: {
                        normal: {
                            position: 'inner',
                            formatter: '{d}%'
                        }
                    },
                    data: seriesData
                }
            })
        }

        function barSeminaryMatchChart() {
            var legendData = ['互联网+大赛', '挑战杯', '创青春', '其它'];
            var seriesData = [];
            var xAxisData = ['会计学院', '工商管理学院', '财税与公共管理学院', '经济学院', '法学院', '数学与计算机学院', '电气工程学院', '建筑工程学院', '机械工程学院', '文学与艺术传媒学院', '金融学院', '外国语学院', '团委'];
            var staticData = [{
                name: '互联网+大赛',
                value: [112,105,32,34,26,74,69,87,97,42,48,25,0]
            },{
                name: '挑战杯',
                value: [75,86,64,35,31,34,49,72,47,46,37,39,64]
            },{
                name: '创青春',
                value: [42,23,27,16,12,15,27,31,24,23,13,17,0]
            },{
                name: '其它',
                value: [18,16,13,26,10,28,29,17,23,27,34,14,34]
            }]

            staticData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    name: item.name,
                    stack: '学院',
                    data: item.value
                })
            });


            seminaryMatchChart2.setOption({
                title: {
                    y: 30,
                    x: 'center',
                    text: '双创大赛各学院占比分析'

                },
                legend: [{
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    y: 'center',
                    x: 'center',
                    width: '75%',
                    height: '55%'
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

        $seminaryMatchYear.trigger('change');

        var stuChartBarEle = document.getElementById('stuChartBar');
        var stuChartBarChart = echarts.init(stuChartBarEle, 'macarons');
        var $matchYear = $('#matchYear');


        $matchYear.on('change', function (e) {
            pieBarStuChart()
        });


        function barStuChartBarChart() {
            var legendData = ['在校参赛人数', '毕业参赛人数', '休学参赛人数'];
            var seriesData = [];
            var xAxisData = ['互联网+大赛', '挑战杯', '创青春', '其它'];
            var staticData = [{
                name: '互联网+大赛',
                value: [2253,256,13]
            },{
                name: '挑战杯',
                value: [2037,210,0]
            },{
                name: '创青春',
                value: [816,134,0]
            },{
                name: '其它',
                value: [1156,55,17]
            }]
            staticData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    name: item.name,
                    data: item.value
                })
            });

            stuChartBarChart.setOption({
                title: {
                    y: 30,
                    x: 'center',
                    text: '双创大赛学生分布统计分析'

                },
                legend: [{
                    x: 'center',
                    bottom: 30,
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    y: 'center',
                    x: 'center',
                    width: '75%',
                    height: '55%'
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

        function pieBarStuChart() {
            var legendData = ['在校参赛人数', '毕业参赛人数', '休学参赛人数'];
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
                tooltip: {
                    trigger: 'item'
                },
                data: [{
                    name: '在校参赛人数',
                    value: getTotal([2253,2037,816,1156])
                },{
                    name: '毕业参赛人数',
                    value: getTotal([256,210,134,55])
                },{
                    name: '休学参赛人数',
                    value: getTotal([13,0,0,17])
                }]
            };
            var xAxisData = ['互联网+大赛', '挑战杯', '创青春', '其它'];
            var staticData = [{
                name: '在校参赛人数',
                value: [2253,2037,816,1156]
            },{
                name: '毕业参赛人数',
                value: [256,210,134,55]
            },{
                name: '休学参赛人数',
                value: [13,0,0,17]
            }]
            staticData.forEach(function (item, i) {
//                pieData.push({
//                    name: item,
//                    value: Math.floor((i + 1) * Math.random() * 5000)
//                });
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    name: item.name,
                    data: item.value
                })
            });
//            pie['data'] = pieData;
            seriesData.push(pie);
            stuChartBarChart.setOption({
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

        $matchYear.trigger('change')
    })

</script>
</body>


</html>