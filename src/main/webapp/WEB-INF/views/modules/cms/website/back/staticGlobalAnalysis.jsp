<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>全局概览</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
    <script src="${ctxJs}/echarts.min.js"></script>
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
            width: auto;
            padding-left: 27px;
            padding-right: 27px;
        }

        .print-bar {
            margin: 15px 0;
            padding-left: 27px;
            padding-right: 27px;
            text-align: right;
        }

        .print-bar > a {
            color: #e9432d;
            text-decoration: none;
            font-family: "Microsoft YaHei";
        }

        .title-bar {
            line-height: 3;
            font-family: "Microsoft YaHei";
        }

        .col-border-right {
            border-right: 1px solid #ccc
        }

        @media print {
            .view-content {
                /*padding-top: 50px;*/
            }

            .mybreadcrumbs {
                display: none;
            }

            .print-bar {
                display: none;
            }

            .text-right {
                /*text-align: left;*/
            }

            .col-border-right {
                /*border-right: 0;*/
            }

            .print-md-6 {
                width: 50%;
                float: left;
                /*width: 100%;*/
            }

            .print-height {
                padding-top: 50px;
                padding-bottom: 50px;
            }
        }
    </style>
</head>
<body style="padding-bottom: 60px;">
<div class="mybreadcrumbs"><span>全局概览</span></div>
<div class="print-bar"><a id="printBtn" href="javascript:void (0);">打印报表</a></div>
<div id="viewPrint" class="view-content">
    <div class="container-fluid" style="border: 1px solid #ccc;border-radius: 5px">
        <div class="row title-bar">
            <div class="col-md-6">
                双创大赛及项目统计
            </div>
            <%--<div class="col-md-6 text-right">
                统计年份：
                <select id="select1" >
                    <option value="">全部</option>
                    <option value="2017">2017年</option>
                    <option value="2016">2016年</option>
                    <option value="2015">2015年</option>
                    <option value="2014">2014年</option>
                </select>
            </div>--%>
        </div>
        <div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
            <div class="col-md-6 col-border-right">
                <div id="MchartOne" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
            <div class="col-md-6">
                <div id="MchartTwo" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
        </div>

        <div class="row title-bar">
            <div class="col-xs-6">
                双创学生统计
            </div>
            <div class="col-xs-6 text-right">
                统计年份：
                <select id="select2">
                    <option value="">全部</option>
                    <option value="2017">2017年</option>
                    <option value="2016">2016年</option>
                    <option value="2015">2015年</option>
                </select>
            </div>
        </div>
        <div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
            <div class="col-md-6 col-border-right">
                <div id="MchartThree" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
            <div class="col-md-6">
                <div id="MchartFour" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
        </div>

        <div class="row title-bar">
            <div class="col-md-6">
                双创导师统计
            </div>
            <%--<div class="col-md-6 text-right">
                统计年份：
                <select id="select3">
                    <option value="">全部</option>
                    <option value="2017">2017年</option>
                    <option value="2016">2016年</option>
                    <option value="2015">2015年</option>
                    <option value="2014">2014年</option>
                </select>
            </div>--%>
        </div>
        <div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
            <div class="col-md-6 col-border-right">
                <div id="MchartFive" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
            <div class="col-md-6">
                <div id="MchartSix" class="print-echart" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
            </div>
        </div>


        <div class="row title-bar">
            <div class="col-xs-6">
                热门技术
            </div>
            <div class="col-xs-6 text-right">
                统计年份：
                <select id="select4">
                    <option value="">全部</option>
                    <option value="2017">2017年</option>
                    <option value="2016">2016年</option>
                    <option value="2015">2015年</option>
                </select>
            </div>
        </div>
        <div class="row" style="border:solid #ccc;border-width: 1px 0 0;">
            <div class="col-md-12">
                <div id="MchartSeven" class="print-echart" style="width: 100%;height:360px;padding:30px 0 15px;"></div>
            </div>
        </div>
    </div>
</div>
<script>

    $(function () {
        var MchartOne = document.getElementById('MchartOne');
        var MchartOnePie = echarts.init(MchartOne, 'macarons');
        MchartOnePie.setOption({
            width: 'auto',
            title: {
                text: '双创大赛参赛分布',
                subtext: '',
                x: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            selectedMode: 'single',
            legend: [{
                x: 'center',
                y: 'bottom',
                data: ['互联网+大赛', '创青春', '蓝桥杯']
            }],
            grid: [{
                x: '0'
            }],
            series: {
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
                    name: '互联网+大赛',
                    value: 100
                }, {
                    name: '创青春',
                    value: 500
                }, {
                    name: '蓝桥杯',
                    value: 100
                }]
            }
        });

        var MchartTwo = document.getElementById('MchartTwo');
        var MchartTwoPie = echarts.init(MchartTwo, 'macarons');
        MchartTwoPie.setOption({
            width: 'auto',
            title: {
                text: '双创项目分布',
                subtext: '',
                x: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            selectedMode: 'single',
            legend: [{
                x: 'center',
                y: 'bottom',
                data: ['创业项目', '大创项目', '国创项目', '其它']
            }],
            grid: [{
                x: '0'
            }],
            series: {
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
                    name: '创业项目',
                    value: 100
                }, {
                    name: '大创项目',
                    value: 500
                }, {
                    name: '国创项目',
                    value: 100
                }, {
                    name: '其它',
                    value: 100
                }]
            }
        });


        var MchartThree = document.getElementById('MchartThree');
        var MchartThreePie = echarts.init(MchartThree, 'macarons');
        var MchartFour = document.getElementById('MchartFour');
        var MchartFourBar = echarts.init(MchartFour, 'macarons');
        var $select2 = $('#select2');
        $select2.on('change', function (e) {
            var val = $(this).val();
            pieThree(val);
            barFour(val)
        });

        function pieThree(val) {
            var legendData = ['在校学生', '毕业学生', '休学学生'];
            var seriesData = [];
            legendData.forEach(function (item, i) {
                seriesData.push({
                    name: item,
                    value: Math.floor((i + 1) * Math.random() * 5000)
                })
            });
            if (val) {
                seriesData.map(function (item, i) {
                    var itemVal = item.value - Math.floor(val * Math.random());
                    return item.value = itemVal < 0 ? 0 : itemVal
                })
            }
            MchartThreePie.setOption({
                width: 'auto',
                title: {
                    text: '双创学生比例',
                    subtext: '',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                selectedMode: 'single',
                legend: [{
                    x: 'center',
                    y: 'bottom',
                    data: legendData
                }],
                grid: [{
                    x: '0'
                }],
                series: {
                    type: 'pie',
                    center: ['50%', 130],
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

        function barFour(val) {
            var legendData = ['在校学生', '毕业学生', '休学学生'];
            var seriesData = [];
            var xAxisData = ['创业项目', '大创项目', '蓝桥杯', '互联网+大赛'];
            legendData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
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

            MchartFourBar.setOption({
                title: {
                    text: '双创项目学生统计',
                    subtext: '',
                    x: 'center'
                },
                legend: [{
                    x: 'center',
                    y: 'bottom',
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    height: '55%'
                },
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

        $select2.trigger('change')


        var MchartFive = document.getElementById('MchartFive')
        var MchartFivePie = echarts.init(MchartFive, 'macarons');
        var MchartSix = document.getElementById('MchartSix');
        var MchartFiveBar = echarts.init(MchartSix, 'macarons');

        function pieFive() {
            var legendData = ['校园导师', '企业导师'];
            var seriesData = [];
            legendData.forEach(function (item, i) {
                seriesData.push({
                    name: item,
                    value: Math.floor((i + 1) * Math.random() * 5000)
                })
            });
            MchartFivePie.setOption({
                width: 'auto',
                title: {
                    text: '双创学生比例',
                    subtext: '',
                    x: 'center'
                },
                tooltip: {
                    trigger: 'item'
                },
                selectedMode: 'single',
                legend: [{
                    x: 'center',
                    y: 'bottom',
                    data: legendData
                }],
                grid: [{
                    x: '0'
                }],
                series: {
                    type: 'pie',
                    center: ['50%', 130],
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

        pieFive()

        function barSix() {
            var legendData = ['校园导师', '企业导师'];
            var seriesData = [];
            var xAxisData = ['学生点赞数', '导师点赞数', '导师评论', '学生评论'];
            legendData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
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

            MchartFiveBar.setOption({
                title: {
                    text: '人气导师前五名',
                    subtext: '',
                    x: 'center'
                },
                legend: [{
                    x: 'center',
                    y: 'bottom',
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    height: '55%'
                },
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

        barSix()


        var MchartSeven = document.getElementById('MchartSeven');
        var MchartSevenBar = echarts.init(MchartSeven, 'macarons');
        var $select4 = $('#select4');
        $select4.on('change', function (e) {
            barSeven()
        });

        function barSeven() {
            var legendData = ['大创项目', '创新型实验项目', '创业型孵化项目', '	互联网+大赛', '挑战杯', '创青春'];
            var seriesData = [];
            var xAxisData = ['云计算', '大数据', '移动互联网', '智能制造', 'VR/AR'];
            var staticData = [{
                name: '大创项目',
                value: [78, 96, 137, 34, 23]
            }, {
                name: '创新型实验项目',
                value: [36, 47, 75, 12, 24]
            }, {
                name: '创业型孵化项目',
                value: [16, 19, 36, 27, 9]
            }, {
                name: '互联网+大赛',
                value: [97, 105, 186, 52, 31]
            }, {
                name: '挑战杯',
                value: [53, 69, 92, 47, 34]
            }, {
                name: '创青春',
                value: [47, 35, 84, 32, 17]
            }]
            staticData.forEach(function (item, i) {
                seriesData.push({
                    type: 'bar',
                    barWidth: 15,
                    name: item.name,
                    data: item.value
                })
            });

            MchartSevenBar.setOption({
                title: {
                    text: '双创项目和双创大赛涉及到的热门技术领域分析',
                    subtext: '',
                    x: 'center'
                },
                legend: [{
                    y: 'bottom',
                    data: legendData
                }],
                tooltip: {
                    trigger: 'axis'
                },
                grid: {
                    height: '55%'
                },
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

        $select4.trigger('change')


        var $printBtn = $('#printBtn');
        var $printIframe;
        var $printView = $('#viewPrint');
        var chartPic = [];
        var iframeHtml;
        var chartMap = [MchartOnePie, MchartTwoPie, MchartThreePie, MchartFourBar, MchartFivePie, MchartFiveBar, MchartSevenBar];

        $printBtn.on('click', function (e) {
            clearCharIframe();
            createIframe();
            loadLink($printIframe[0].contentWindow.document);
            buildEchartPic();
            buildIframeHtml($printIframe[0].contentWindow.document);
            setTimeout(function () {
                $printIframe[0].contentWindow.print()
            }, 0);
        });

        //生成打印的html
        function buildIframeHtml(iframe) {
            var printParents;
            iframeHtml = $printView.clone();
            printParents = iframeHtml.find('.print-echart');
            printParents.children().detach();
            printParents.each(function (i, item) {
                $(item).append($('<img class="img-responsive" style="margin: 0 auto;" src="' + chartPic[i] + '">'))
            });

            $(iframe).find('body').css('backgroundColor', '#fff').append(iframeHtml);
            $('body').find('select').each(function (i, item) {
                $(iframe).find('body').find('select').eq(i).val($(item).val())
            })
        }

        //创建一个iframe
        function createIframe() {
            var $iframe = $("<iframe  />");
            $iframe.css({position: "absolute", width: "0", height: "0", left: "-600px", top: "-600px"});
            $iframe.attr('id', 'chartIframe');
            $iframe.appendTo("body");
            $printIframe = $iframe;
        }

        //清除iframe
        function clearCharIframe() {
            var $chartIframe = $('#chartIframe')
            $chartIframe.size() && $chartIframe.detach();
        }

        //加载head
        function loadLink(doc) {
            var $headChildren = $('head').children().clone();
            $(doc).find('head').append($headChildren)
        }

        //生产所有echart图片
        function buildEchartPic() {
            chartPic = [];
            $('.print-echart').each(function (i, item) {
                var chartObj = chartMap[i];
                var url = chartObj.getDataURL('png');
                chartPic.push(url);
            })
        }
    })
</script>
</body>


</html>