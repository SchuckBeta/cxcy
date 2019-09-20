var tendency = [
    {
        name: '互联网+大赛',
        data: [30, 26, 300, 500],
        categories: ['2014年', '2015年', '2016年', '2017年']
    },
    {
        name: '创青春大赛',
        data: [0, 55, 156, 268],
        categories: ['2014年', '2015年', '2016年', '2017年']
    },
    {
        name: '蓝桥杯大赛',
        data: [10, 15, 56, 123],
        categories: ['2014年', '2015年', '2016年', '2017年']
    },
    {
        name: '挑战杯大赛',
        data: [8, 15, 96, 298],
        categories: ['2014年', '2015年', '2016年', '2017年']
    }
];
var seminaryMatchData = [
    {
        name: '校级初赛',
        data: [5, 20, 10, 30, 10, 20, 10, 30],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院']
    },
    {
        name: '省市复赛',
        data: [1, 10, 3, 15, 8, 18, 18, 15],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院']
    },
    {
        name: '全国决赛',
        data: [0, 8, 1, 5, 16, 8, 2, 3, 0],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院']
    },
    {
        name: '得奖总数',
        data: [0, 1, 1, 3, 1, 1, 0, 0],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院']
    }

];
var stu = [
    {
        name: '在校参赛人数',
        categories: ['互联网+大赛', '创青春大赛', '蓝桥杯大赛', '挑战杯大赛'],
        data: [845, 378, 432, 269]
    },
    {
        name: '毕业参赛人数',
        categories: ['互联网+大赛', '创青春大赛', '蓝桥杯大赛', '挑战杯大赛'],
        data: [264, 152, 264, 163]
    },
    {
        name: '休学参赛人数',
        categories: ['互联网+大赛', '创青春大赛', '蓝桥杯大赛', '挑战杯大赛'],
        data: [2450, 547, 826, 486]
    }
];


var chartsDefaultOption = {
    lineChartOption: function (title, unit, color, rotate) {
        return {
            title: {
                text: title
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                x:'right',
                y:'center',
                data: [],
                orient:'vertical'
            },
            grid:[{
                left:'10%',
                width: '70%',
                containLabel: true
            }],
//            color: color,
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                name: unit,
                type: 'value'
            },
            series: []
        }
    },
    barChartOption: function (titles, unit, color, rotate) {
        return {
            title: [{
                text: titles[0],
                x: '20%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }, {
                text: titles[1],
                x: '65%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }],
            legend: {
                x: 'center',
                y: 'bottom',
                data: []
            },
            tooltip: {},
            grid: [{
                top: 50,
                width: '60%',
                left: '40%',
                containLabel: true
            }],
            color: color,
            xAxis: {
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: rotate
                },
                data: []
            },
            yAxis: {
                type: 'value',
                name: unit
            },
            series: []
        };
    },
    matchChartOption: function (titles, unit, color, rotate) {
        return {
            title: [{
                text: titles[0] + '赛制入围分布',
                x: '20%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }, {
                text: titles[1],
                x: '65%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }],
            tooltip: {},
            legend: {
                x: 'center',
                y: 'bottom',
                data: []
            },
            color: color,
            grid: [{
                top: 40,
                width: '60%',
                height: '70%',
                left: '40%',
                containLabel: true
            }],
            xAxis: {
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: rotate,
                    textStyle: {
                        fontSize: 12
                    }
                },
                data: []
            },
            yAxis: {
                type: 'value',
                name: unit
            }
            ,
            series: []
        }
    }
};

var chartsOption = {
    tendency: function (data) {
        var res = {
            legend: {
                data: []
            },
            xAxis: {
                data: []
            },
            series: []
        };
        if (data.length) {
            res.xAxis.data = data[0].categories;
        }
        $.each(data, function (i, item) {
            res.legend.data.push(item.name);
            res.series.push({
                name: item.name,
                type: 'line',
                data: item.data
            })
        });
        return res;
    },
    stuChart: function (data, width) {
        var res = {
            title: [{
                text: '双创大赛参赛学生分布统计',
                x: '20%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }, {
                text: '双创各类大赛学生分布统计',
                x: '65%',
                textAlign: 'center',
                textStyle:{
                    fontSize:16,
                    color:'#666',
                    fontWeight:'normal'
                }
            }],
            legend: {
                x: 'center',
                y: 'bottom',
                data: []
            },
            tooltip: {},
            grid: [{
                top: 50,
                width: '60%',
                left: '40%',
                containLabel: true
            }],
            color: ['#9bbb59', '#4f81bd', '#c0504d'],
            yAxis: {
                type: 'value',
                name: '人'
            },
            series: []
        }


        res.legend.data = data.length > 0 ? data[0].categories : [];


        var pie = {
            type: 'pie',
            radius: [0, '36%'],
            center: ['20%', '30%'],
            data: [],
            label: {
                normal: {
                    position: 'inner'
                }
            }
        };

        if(data.length > 0){
            $.each(data, function (i, item) {
                var name = item.name;
                var count = 0;
                res.legend.data.push(name);
                res.series.push({
                    name: item.name,
                    type: 'bar',
                    barWidth: width,
                    data: item.data,
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'outside'
                            },
                            labelLine: {show: true}
                        }
                    }
                });
                $.each(item.data, function (i2, val) {
                    count += parseInt(val);
                });
                pie.data.push({
                    name: item.name,
                    value: count,
                    tooltip: {
                        formatter: function (params) {
                            var percent = Math.ceil(params.percent);
                            return params.name + '(' + params.data.value + '):' + percent + "%";
                        }
                    }
                })
            });
            pie.label.normal.formatter = '{d}%';
            res.series.push(pie);
        }

        return res;
    },
    seminaryMatchChart: function (data, width, titles) {
        var res = {
            title: [{
                text: titles[0] + '赛制入围分布'
            }, {
                text: titles[1]
            }],
            legend: {
                data: []
            },
            xAxis: {
                data: []
            },
            series: []
        };
        var pie = {
            type: 'pie',
            radius: [0, '36%'],
            center: ['20%', '30%'],
            data: [],
            label: {
                normal: {
                    position: 'inner'
                }
            }
        };
        if (data.length) {
            res.xAxis.data = data[0].categories;
        }
        $.each(data, function (i, item) {
            var name = item.name;
            var count = 0;
            res.legend.data.push(name);
            res.series.push({
                name: item.name,
                type: 'bar',
                barWidth: width,
                stack: '总人数',
                data: item.data
            });
            $.each(item.data, function (i2, val) {
                count += parseInt(val);
            });

            pie.data.push({
                name: item.name,
                value: count,
                tooltip: {
                    formatter: function (params) {
                        var percent = Math.ceil(params.percent);
                        return params.name + '(' + params.data.value + '):' + percent + "%";
                    }
                }
            });
        });
        res.legend.formatter = function (name, b, c, d) {
            return name
        };

        pie.label.normal.formatter = '{d}%';
        res.series.push(pie);
        return res;
    }
};
/*大赛统计分析*/
function renderData2(seminaryMatchChart,typeTitle,pyear,ptype){
	$.ajax({
        type: "POST",
        url: "/a/analysis/gcontestAnalysis/getGcontestOfficeNum",
        dataType: "json",
        data: {year:pyear,type:ptype},
        cache: false,
        traditional: true,
        success: function (data) {
            if(data.length > 0){
                seminaryMatchChart.setOption(chartsOption.seminaryMatchChart(data, 10, [typeTitle, '各院系参赛对比分析']));
                $('.seminary-chart-no-data').addClass('hide').prev().removeClass('hide')
            }else{
                $('.seminary-chart-no-data').removeClass('hide').prev().addClass('hide')
            }

        },
        error: function (XMLHttpResponse) {
        }
    });
}
/*双创项目分布统计*/
function renderData3(stuChart,pyear){
	$.ajax({
        type: "POST",
        url: "/a/analysis/gcontestAnalysis/getGcontestMemNum",
        dataType: "json",
        data: {year:pyear},
        cache: false,
        traditional: true,
        success: function (data) {
			 if(data.length > 0){
                stuChart.setOption(chartsOption.stuChart(data, 10), false);
                $('.stu-chart-no-data').addClass('hide').prev().removeClass('hide')
            }else{
                $('.stu-chart-no-data').removeClass('hide').prev().addClass('hide')
            }        },
        error: function (XMLHttpResponse) {
        }
    });
}
$(function () {
    /*双创项目申报年增长趋势分析*/
    var tendencyCanvas = document.getElementById('tendencyChart');
    var tendencyChart = echarts.init(tendencyCanvas);

    tendencyChart.setOption(chartsDefaultOption.lineChartOption('', '', ['#4f81bd', '#c15552', '#9bbb59', '#8064a2']));

    $.ajax({
        type: "POST",
        url: "/a/analysis/gcontestAnalysis/getGcontestNum",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: {},
        cache: false,
        traditional: true,
        success: function (data) {
            tendencyChart.setOption(chartsOption.tendency(data));
        },
        error: function (XMLHttpResponse) {
        }
    });

    /*大赛统计分析*/
    var seminaryMatchCanvas = document.getElementById('seminaryMatch');
    var seminaryMatchChart = echarts.init(seminaryMatchCanvas);
    seminaryMatchChart.setOption(chartsDefaultOption.matchChartOption(['全部', '各院系参赛对比分析'], '', ['#a7cf8d', '#eb7c30', '#a3a3a3', '#e7ad00'], 30));
    renderData2(seminaryMatchChart, '全部', '', '');

    var $seminaryEle = $('#seminaryMatchYear');
    var $matchTypesEle = $('#matchTypes');
    var seminaryYear = $seminaryEle.val();
    var matchTypeVal = $matchTypesEle.val();


    $matchTypesEle.on('change',function(e){
        var val = $(this).val();
        if (matchTypeVal == val) {
            return false
        } else {
            matchTypeVal = val;
        }
        renderData2(seminaryMatchChart, $matchTypesEle.find('option:selected').text(), seminaryYear, val);
    });
    $seminaryEle.on('change',function(e){
        var val = $(this).val();
        if (seminaryYear == val) {
            return false
        } else {
            seminaryYear = val;
        }
        renderData2(seminaryMatchChart, $matchTypesEle.find('option:selected').text(), val, matchTypeVal);
    });

    /*双创项目分布统计*/
    var stuCanvas = document.getElementById('stuChart');
    var stuChart = echarts.init(stuCanvas);

    stuChart.setOption(chartsDefaultOption.barChartOption(['双创大赛参赛学生分布统计','双创各类大赛学生分布统计'], '人', ['#9bbb59', '#4f81bd', '#c0504d']), false)


    renderData3(stuChart, '');

    var $matchYear = $('#matchYear');
    var matchYearVal = $matchYear.val();


    $matchYear.on('change', function () {
        var val = $(this).val();
        // if (matchYearVal == val) {
        //     return false
        // } else {
        //     matchYearVal = val;
        // }
        renderData3(stuChart, val);
        // console.log(stuChart.getOption())
    });


})