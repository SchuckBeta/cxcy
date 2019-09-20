


var annals = [
    {
        name: '国创项目',
        data: [30, 60, 80, 100],
        categories: ['2014年', '2015年', '2016年', '2017年']
    },
    {
        name: '开放实验项目',
        data: [50, 60, 80, 120],
        categories: ['2014年', '2015年', '2016年', '2017年']
    },
    {
        name: '创新性实验项目',
        data: [30, 80, 120, 200],
        categories: ['2014年', '2015年', '2016年', '2017年']
    }
];

var proportions = [
    {
        name: '国创项目',
        value: 356,
        types: {
            title: '国创项目类型分布',
            data: [{
                name: '创新训练项目',
                value: 185
            }, {
                name: '创业训练项目',
                value: 100
            }, {
                name: '创业实践项目',
                value: 95
            }]
        }
    }, {
        name: '开放实验项目',
        value: 245,
        types: {
            title: '开放实验项目类型分布',
            data: [{
                name: '开放创新项目',
                value: 185
            }, {
                name: '开放创业项目',
                value: 95
            }, {
                name: '开放实践项目',
                value: 34
            }]
        }
    },
    {
        name: '创新性实验项目',
        value: 156,
        types: {
            title: '创新性实验项目类型分布',
            data: [{
                name: '创新项目',
                value: 185
            }, {
                name: '创业项目',
                value: 95
            }, {
                name: '实践项目',
                value: 65
            }]
        }
    }
];

var approvals1 = [
    {
        name: '国创项目申报数',
        data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    },
    {
        name: '开放实验项目申报数',
        data: [10, 19, 10, 8, 5, 5, 10, 6, 7, 15, 10, 5, 8, 10, 10, 15, 10, 8, 1, 6],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    },
    {
        name: '创新性实验项目申报数',
        data: [10, 20, 10, 10, 10, 20, 10, 13, 6, 20, 10, 5, 10, 10, 10, 12, 10, 6, 8, 15],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    },
    {
        name: '国创项目立项数',
        data: [10, 6, 10, 25, 10, 20, 10, 11, 18, 20, 10, 5, 12, 10, 10, 3, 1, 12, 2, 10],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    },
    {
        name: '开放实验项目立项数',
        data: [10, 3, 10, 8, 10, 6, 10, 30, 12, 20, 10, 5, 3, 10, 10, 2, 1, 1, 1, 13],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    },
    {
        name: '创新性实验项目立项数',
        data: [10, 8, 10, 16, 10, 20, 10, 12, 21, 20, 10, 5, 14, 10, 8, 1, 1, 2, 0, 8],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    }
];

var approvals2 = [
    {
        name: '申报项目',
        data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    }, {
        name: '立项成功数',
        data: [10, 6, 10, 25, 10, 20, 10, 11, 18, 20, 10, 5, 12, 10, 10, 3, 1, 12, 2, 10],
        categories: ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院']
    }
];

var funds = [
    {
        name: '国创项目',
        categories: [],
        data: [520]
    }, {
        name: '开放实验项目',
        categories: [],
        data: [3980]
    }, {
        name: '创新性实验项目',
        categories: [],
        data: [1500]
    }
];

var fundsAnalyze = [
    {
        name: 'A级-国家级项目',
        data: [100, 300, 800],
        categories: ['创新训练项目', '创新训练项目', '创业实践项目']
    }, {
        name: 'B级-校级项目',
        data: [200, 400, 600],
        categories: ['创新训练项目', '创新训练项目', '创业实践项目']
    }, {
        name: 'C级-学院级项目',
        data: [400, 800, 1000],
        categories: ['创新训练项目', '创新训练项目', '创业实践项目']
    }
];

var chartsDefaultOption = {
    annals: {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: [],
            right: 0,
            orient: 'vertical',
            y: 'center'
        },
        grid: [{
            top: 10,
            left: 30,
            width: '75%',
            height: '80%',
            containLabel: true
        }],
        color: ['#4f81bd', '#c15552', '#9bbb59'],
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: []
    },
    bars: function (title, unit, colors, rotate) {
        return {
            title: {
                text: title,
                subtext: '',
                x: 'center'
            },
            legend: {
                x: 'center',
                y: 'bottom',
                data: []
            },
            tooltip: {
                // trigger: 'axis'
            },
            grid: [{
                top: 40,
                containLabel: true
            }],
            color: colors,
            xAxis: {
                type: 'category',
                axisLabel: {
                    interval: 0,
                    rotate: rotate
                },
                data: []
            },
            yAxis: [
                {
                    type: 'value',
                    name: unit
                }
            ],
            series: []
        };
    },
    proportions: function (titles, color) {
        return {
            title: [{
                text: titles[0],
                x: '20%',
                textAlign: 'center',
                textStyle: {
                    fontSize: 16,
                    color: '#666',
                    fontWeight: 'normal'
                }
            }, {
                text: titles[1],
                x: '80%',
                textAlign: 'center',
                textStyle: {
                    fontSize: 16,
                    color: '#666',
                    fontWeight: 'normal'
                }
            }],
            selectedMode: 'single',
            legend: [{
                left: '5%',
                bottom: '0',
                // orient: 'vertical',
                data: []
            }, {
                left: '65%',
                bottom: '0',
                // orient: 'vertical',
                data: []
            }],
            color: color,
            series: []
        }
    }
};

var chartsOption = {
    annals: function (data) {
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
    funds: function (data, width) {
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
        });
        return res;
    },
    approval1: function (data, width) {
        var res = {
            legend: {
                x: 'center',
                y: 'bottom',
                data: [],
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: [{
                top: 10,
                height: '60%',
                containLabel: true
            }],
            xAxis: {
                data: [],
                textStyle: {
                    fontSize: 12
                }
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
                type: 'bar',
                barWidth: width,
                data: item.data
            });
            if (i == 0 || i == 3) {
                res.series[i]['stack'] = '国创项目'
            } else if (i == 1 || i == 4) {
                res.series[i]['stack'] = '开放实验项目'
            } else {
                res.series[i]['stack'] = '创新性实验项目'
            }
        });
        return res;
    },
    approval2: function (data, width) {
        var res = {
            legend: {
                x: 'center',
                y: 'bottom',
                data: [],
            },
            tooltip: {
                trigger: 'axis'
            },
            grid: [{
                top: 10,
                height: '60%',
                containLabel: true
            }],
            xAxis: {
                data: [],
                textStyle: {
                    fontSize: 12
                }
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
                type: 'bar',
                stack: '立项',
                barWidth: width,
                data: item.data
            });
        });
        return res;
    },
    proportions: function (data) {
        var res = {
            title: [{}, {
                text: ''
            }],
            legend: [{
                data: []
            }, {
                data: []
            }],
            series: [{
                type: 'pie',
                id: '1',
                radius: [0, '60%'],
                center: ['20%', '50%'],
                data: [],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{d}%'
                    }
                }
            }, {
                type: 'pie',
                id: '2',
                radius: [0, '60%'],
                center: ['80%', '50%'],
                data: [],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{d}%'
                    }
                }
            }]
        };
        $.each(data, function (i, item) {
            var types = item.types;
            res.legend[0].data.push(item.name);
            res.series[0].data.push({
                name: item.name,
                value: item.value,
                selected: i == 0 || false
            });
            if (i == 0) {
                $.each(types.data, function (j, typeObj) {
                    res.legend[1].data.push(typeObj.name);
                    res.series[1].data.push({
                        name: typeObj.name,
                        value: typeObj.value
                    });
                });
                res.title[1].text = types.title
            }
        });
        return res
    }
};

function setProjectChart(year,url,chart){
    $.ajax({
        type : "POST",
        url : url,
        data : {"year":year},
        success : function(data) {
            chart.setOption(chartsDefaultOption.annals);
            chart.setOption(chartsOption.annals(data));
        }
    });
}

$(function () {

    /*双创项目申报年增长趋势分析*/
    var annalsCanvas = document.getElementById('annalsChart');
    var annalsChart = echarts.init(annalsCanvas);
    setProjectChart("","/a/analysis/projectAnalysis/getPojectByYear",annalsChart,30);

    /*双创项目分布统计*/
    var proportionsCanvas = document.getElementById('proportionsChart');
    var proportionsChart = echarts.init(proportionsCanvas);
    var distributionSubCanvas = document.getElementById('distributionSubChart');
    var distributionSubChart = echarts.init(distributionSubCanvas);
    var $proScatterYear = $('#proScatterYear');


    // 项目分布默认饼图option
    function proDisOption(title, legend, seriesData, color){
        return {
            title: [{
                text:title,
                left: 'center',
                textStyle: {
                    fontSize: 16,
                    color: '#666',
                    fontWeight: 'normal'
                }
            }],
            selectedMode: 'single',
            legend: [{
                left: 'center',
                bottom: '0',
                // orient: 'vertical',
                data: legend || ['国创项目', '开放实验项目', '创新性实验项目']
            }],
            color: color || ['#4bacc6', '#f79646', '#8064a2', '#4f81bd', '#c0504d', '#9bbb59'],
            series: [{
                type: 'pie',
                id: '1',
                radius: [0, '60%'],
                center: ['50%', '50%'],
                data: seriesData || [{
                    name: '国创项目',
                    value: '365'
                },{
                    name: '开放实验项目',
                    value: '365'
                },{
                    name: '创新性实验项目',
                    value: '365'
                }],
                label: {
                    normal: {
                        position: 'inner',
                        formatter: '{d}%'
                    }
                }
            }]
        }
    }

    //设置项目饼图
    function setProjectDistribution(url, params, chart){
        // $.ajax()
        var proScaYear = $proScatterYear.val();
        $.ajax({
           type : "POST",
           url : url,
           data : {"year":proScaYear},
           success : function(data) {
               //alert("data:"+data);
               var dataList=data;
                var legendData = new Array();
                var colorArray = new Array();
                for(var i=0;i<dataList.length;i++) {
                    legendData[i] = dataList[i].name;
                    // colorArray[i] = bg3;
                }
               // alert("legendData:"+legendData);
               // alert("colorArray:"+colorArray);
               chart.setOption(proDisOption('双创项目分布比例',legendData,data,''));
           }
       });

    }
    //设置项目子类饼图
    function setProjectDistributionSub(url, label, chart){
        // $.ajax()
        var proScaYear = $proScatterYear.val();
        var params="year="+proScaYear;
        if(label){
            params=params+"&label="+label;
        }
        $.ajax({
          type : "POST",
          url : url,
          data : params,
          success : function(data) {
              var dataList=data;
               var legendData = new Array();
               var colorArray = new Array();
               for(var i=0;i<dataList.length;i++) {
                   legendData[i] = dataList[i].name;
                   // colorArray[i] = bg3();
               }
                if(label){
                     //params={"year":proScaYear,"label":label};
                    chart.setOption(proDisOption(label+'类型分布',legendData,data,''));
                }else{
                    chart.setOption(proDisOption('双创项目类型分布',legendData,data,''));
                }
          }
      });
    }

    setProjectDistribution('/a/analysis/projectAnalysis/getProjectByType', '', proportionsChart);
    //创新性实验项目类型分布
    setProjectDistributionSub('/a/analysis/projectAnalysis/getProjectByTypeAndCategory', '',distributionSubChart);

    //点击这个获取选中的信息
    proportionsChart.on('pieselectchanged', function (params) {
        //参数里面包含选中的mode信息
        var label=params.name;
        setProjectDistributionSub('/a/analysis/projectAnalysis/getProjectByTypeAndCategory',label,distributionSubChart);
    });
    function bg3(){
   		var r=Math.floor(Math.random()*256);
   		var g=Math.floor(Math.random()*256);
   		var b=Math.floor(Math.random()*256);
   		return "rgb("+r+','+g+','+b+")";//所有方法的拼接都可以用ES6新特性`其他字符串{$变量名}`替换
   	}
    $proScatterYear.on('change', function () {
        setProjectDistribution('/a/analysis/projectAnalysis/getProjectByType', '', proportionsChart);
            //创新性实验项目类型分布
        setProjectDistributionSub('/a/analysis/projectAnalysis/getProjectByTypeAndCategory', '',distributionSubChart);
    });

    /*双创项目立项统计*/
    var approvalsCanvas = document.getElementById('approvalsChart');
    var approvalsChart;
    var approvalsSubCanvas = document.getElementById('approvalsSubChart');
    var approvalsSubChart = echarts.init(approvalsSubCanvas);


    var $approvalYearEle = $('#approvalYear');
    // setProjectApplyChart('/a/analysis/projectAnalysis/getProjectApplyByType','', approvalsChart);

    setProjectApplyLiChart('/a/analysis/projectAnalysis/getProjectApplyLiByType','', approvalsSubChart)


    var $optionsRadios = $('input[name="optionsRadios"]');

    $approvalYearEle.on('change',function(){
        var $checkedRadio = $('input[name="optionsRadios"]:checked');
        setProjectApplyLiChart('/a/analysis/projectAnalysis/getProjectApplyLiByType','', approvalsSubChart);
        if(approvalsChart){
            setProjectApplyChart('/a/analysis/projectAnalysis/getProjectApplyByType','', approvalsChart);
        }
    });


    $optionsRadios.on('change', function () {
        var index = $optionsRadios.index($(this));
        $('.approval-charts').children().eq(index).removeClass('hide').siblings().addClass('hide');
        if(!$(this).attr('data-chart') && index > 0){
            $(this).attr('data-chart', 'shown');
            setTimeout(function () {
                approvalsChart = echarts.init(approvalsCanvas);
                setProjectApplyChart('/a/analysis/projectAnalysis/getProjectApplyByType','', approvalsChart);
            }, 10)
        }
    });

    //双创项目立项统计option
       /*@params
       title: 标题，
       unit： 单位，
       colors: 线条颜色
        */
    function projectApplyOption(legend, xAxisData,seriesData, title,unit, colors){
       return {
           title: {
               text: title,
               subtext: '',
               x: 'center'
           },
           legend: {
               x: 'center',
               y: 'bottom',
               data: legend || ['国创项目申报数','国创项目立项数','开放实验项目申报数','开放实验项目立项数']
           },
           tooltip: {
               trigger: 'axis'
           },
           grid: [{
               height: '56%'
           }],
           color: colors || ['#c0504d', '#8064a2', '#f79646', '#f2dcdb', '#e3deeb', '#feeada'],
           xAxis: {
               type: 'category',
               axisLabel: {
                   interval: 0,
                   rotate: 30
               },
               data: xAxisData || ['机械与运载工程学院', '材料科学与工程学院', '建筑学院', '土木工程学院', '化学化工学院', '数学与计量经济学院', '经济与贸易学院', '工商管理学院', '马克思主义学院', '教育科学研究院', '中国语言文学学院', '外国语与国际教育学院', '汽车车身先进设计制造国家重点实验室', '经济管理研究中心', '电子与信息工程学院', '信息工程与工程学院', '环境工程与工程学院', '设计艺术学院', '生物学院', '物理与微电子科学学院'],
           },
           yAxis: [
               {
                   type: 'value',
                   name: unit
               }
           ],
           series: seriesData || [{
               name: '国创项目申报数',
               type: 'bar',
               barWidth: 8,
               data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
               stack: '申报'
           },{
              name: '国创项目立项数',
              type: 'bar',
              barWidth: 8,
              data: [16, 18, 17, 26, 20, 22, 10, 30, 22, 20, 10, 5, 8, 10, 10, 18, 13, 12, 5, 1],
              stack: '立项'
              },{
               name: '开放实验项目申报数',
               type: 'bar',
               barWidth: 8,
               data: [10, 19, 10, 8, 5, 5, 10, 6, 7, 15, 10, 5, 8, 10, 10, 15, 10, 8, 1, 6],
               stack: '申报'
           },{
              name: '开放实验项目立项数',
              type: 'bar',
              barWidth: 8,
              data: [10, 19, 10, 8, 5, 5, 10, 6, 7, 15, 10, 5, 8, 10, 10, 15, 10, 8, 1, 6],
              stack: '立项'
          }]
       };
    }

    function setProjectApplyChart(url, label, chart){
       var approvalYear = $approvalYearEle.val();
       var params="year="+approvalYear;
       if(label){
          params=params+"&label="+label;
       }
       $.ajax({
           type : "POST",
           url : url,
           data : params,
           success : function(data) {
               var dataList=data.seriesData;
               var legendData = new Array();
               var xAxisData = data.xAxisData;
               for(var i=0;i<dataList.length;i++) {
                   legendData[i] = dataList[i].name;
               }

               var options =  projectApplyOption(legendData,xAxisData , dataList, '', '', ['#c0504d', '#8064a2', '#f79646', '#f2dcdb', '#e3deeb', '#feeada'])
               chart.setOption(options);
           }
       });
    }

    function setProjectApplyLiChart(url, label, chart){
        var approvalYear = $approvalYearEle.val();
        var params="year="+approvalYear;
        if(label){
             params=params+"&label="+label;
        }
        $.ajax({
              type : "POST",
              url : url,
              data : params,
              success : function(data) {
                  var dataList=data.seriesData;
                  var legendData = new Array();
                  var xAxisData = data.xAxisData;
                  for(var i=0;i<dataList.length;i++) {
                      legendData[i] = dataList[i].name;
                  }

                  var options =  projectApplyOption(legendData,xAxisData , dataList, '', '', ['#c0504d', '#8064a2', '#f79646', '#f2dcdb', '#e3deeb', '#feeada'])
                  chart.setOption(options);
              }
        });
    }

   /* setProjectApplyChart('','', approvalsSubChart)*/
    //双创项目子类统计option

    // var $optionsRadios = $('input[name="optionsRadios"]');
    // var $approvalYearEle = $('#approvalYear');
    // var approvalYear = $approvalYearEle.val();
    // $optionsRadios.on('change', function () {
    //     if ($(this).prop('checked')) {
    //         var val = $(this).val();
    //         if (val == 1) {
    //             approvalsChart.setOption(chartsOption.approval1(approvals1, 8));
    //         } else {
    //             var appr2option = $.extend(true, {}, chartsDefaultOption.bars('', '', approvalColors, 30), chartsOption.approval2(approvals2, 8))
    //             approvalsChart.setOption(appr2option, true);
    //         }
    //
    //     }
    // });
    // $approvalYearEle.on('change',function(){
    //     var val = $(this).val();
    //     var ratio = 1;
    //     var res;
    //     var checkedRatioValue = $('input[name="optionsRadios"]:checked').val();
    //     if (approvalYear == val) {
    //         return false;
    //     } else {
    //         approvalYear = val;
    //     }
    //     if (val == 2017) {
    //         ratio = 0.85;
    //     } else if (val == 2014) {
    //         ratio = 0.25;
    //     } else if (val == 2015) {
    //         ratio = 0.5
    //     } else if (val == 2016) {
    //         ratio = 0.75
    //     } else {
    //         ratio = 1
    //     }
    //     if(checkedRatioValue == 1){
    //         res = yearFundsData(ratio, approvals1);
    //         approvalsChart.setOption(chartsOption.approval1(res, 8));
    //     }else{
    //         res = yearFundsData(ratio, approvals2);
    //         approvalsChart.setOption(chartsOption.approval2(res, 8));
    //     }
    // });
    //
    //
    // /*项目经费统计分析*/
    // var fundsCanvas = document.getElementById('fundsChart');
    // var fundsAnalyzeCanvas = document.getElementById('fundsAnalyzeChart');
    // var fundsChart = echarts.init(fundsCanvas);
    // var fundsAnalyzeChart = echarts.init(fundsAnalyzeCanvas);
    //
    // fundsChart.setOption(chartsDefaultOption.bars('', '人民币(万)', ['#e06b0a', '#33889f', '#77933c'], 0));
    // fundsChart.setOption(chartsOption.funds(funds, 50));
    // fundsAnalyzeChart.setOption(chartsDefaultOption.bars('', '人民币(万)', ['#4f81bd', '#c0504d', '#9bbb59'], 0));
    // fundsAnalyzeChart.setOption(chartsOption.funds(fundsAnalyze, 30));
    //
    //
    // var $proFundsYear = $('#proFundsYear');
    // var proFundsYearOldVal = $proFundsYear.val();
    //
    // $proFundsYear.on('change', function () {
    //     var val = $(this).val();
    //     var ratio = 1;
    //     var res1;
    //     var res2;
    //     if (proFundsYearOldVal == val) {
    //         return false;
    //     } else {
    //         proFundsYearOldVal = val;
    //     }
    //     if (val == 2017) {
    //         ratio = 0.85;
    //     } else if (val == 2014) {
    //         ratio = 0.25;
    //     } else if (val == 2015) {
    //         ratio = 0.5
    //     } else if (val == 2016) {
    //         ratio = 0.75
    //     } else {
    //         ratio = 1
    //     }
    //     res1 = yearFundsData(ratio, funds);
    //     res2 = yearFundsData(ratio, fundsAnalyze);
    //     fundsChart.setOption(chartsOption.funds(res1, 50));
    //     fundsAnalyzeChart.setOption(chartsOption.funds(res2, 30));
    // });
    //
    // function yearFundsData(ratio, data) {
    //     var res = [];
    //     $.each(data, function (i, item) {
    //         res.push({
    //             name: item.name,
    //             categories: item.categories,
    //             data: []
    //         });
    //         $.each(item.data, function (j, val) {
    //             res[i].data.push(Math.ceil(val * ratio))
    //         })
    //     });
    //     return res;
    // }
    //
    // function yearProporData(ratio, data) {
    //     var res = [];
    //
    //     $.each(data, function (i, item) {
    //         var childData = [];
    //         res.push({
    //             name: item.name,
    //             value: item.value * ratio + Math.random()*100,
    //             types: {}
    //         });
    //         $.each(item.types.data, function (j, typeObj) {
    //             childData.push({
    //                 name: typeObj.name,
    //                 value: typeObj.value * ratio + Math.random()*100
    //             });
    //         })
    //         res[i]['types']['title'] = item.types.title;
    //         res[i]['types']['data'] = childData
    //     })
    //     return res;
    // }
});