<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>全局概览</title>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script>
	<script src="${ctxJs}/echarts.min.js"></script>
	<style type="text/css">
		.mybreadcrumbs {margin: 20px 1.5em;margin-left: 27px;border-bottom: 3px solid #f4e6d4;}
		.mybreadcrumbs span {position: relative;top: 9px;font-size: 16px;font-weight:bold;color: #e9432d;display: inline-block;background-color: #FFF;padding-right:10px;}
		.view-content{
			width: auto;
			padding-left: 27px;
			padding-right: 27px;
		}
		.print-bar{
			margin: 15px 0;
			padding-left: 27px;
			padding-right: 27px;
			text-align: right;
		}
		.print-bar>a{
			color: #e9432d;
			text-decoration: none;
			font-family:"Microsoft YaHei";
		}
		.title-bar{
			line-height: 3;
			font-family:"Microsoft YaHei";
		}
		.col-border-right{
			border-right: 1px solid #ccc
		}
		@media print {
			.view-content{
				/*padding-top: 50px;*/
			}
			.mybreadcrumbs{
				display: none;
			}
			.print-bar{
				display: none;
			}
			.text-right{
				/*text-align: left;*/
			}
			.col-border-right{
				/*border-right: 0;*/
			}
			.print-md-6{
				width:50%;
				float: left;
				/*width: 100%;*/
			}
			.print-height{
				padding-top:50px;
				padding-bottom: 50px;
			}
		}
	</style>
	<script>
		var chart1 ;
		var chart2 ;
		var chart3 ;
		var chart4 ;
		var chart5 ;
		var chart6 ;
		var chart7 ;
		$(document).ready(function() {
			chart1 = echarts.init($("#MchartOne")[0]);
			chart2 = echarts.init($("#MchartTwo")[0]);
			var legendData1=["互联网+大赛","创青春","挑战杯","蓝桥杯","i创杯","其他"];
			var colorArray=["#4E81BB","#BD4F4C","#99B958","#7E629F","#4AA9C3","#F39344"];
			var option1 =getPieOption('双创大赛参赛分布',legendData1,"人",colorArray);
			chart1.setOption(option1);
			setChart("","/a/analysis/globalAnalysis/getContestTypeData",chart1);

			var legendData2=["国创项目","创业项目","创新项目"];
			var option2 =getPieOption('双创项目申报分布',legendData2,"个",colorArray);
			chart2.setOption(option2);
		     setChart("","/a/analysis/globalAnalysis/getProjectTypeData",chart2);

			//第一个下拉框change事件
			$("#select1").change(function(){
				var year=$(this).val();
				setChart(year,"/a/analysis/globalAnalysis/getContestTypeData",chart1);
				setChart(year,"/a/analysis/globalAnalysis/getProjectTypeData",chart2);
			})

			//初始化双创学生比例图
			chart3 = echarts.init($("#MchartThree")[0]);
			var legendData3=["在校学生","毕业学生","休学学生"];
			var colorArray3=["#E56C0A","#30859D","#99B958"];
			var option3 =getPieOption('双创学生比例',legendData3,"人",colorArray3);
			chart3.setOption(option3);
			setChart("","/a/analysis/globalAnalysis/getProjectStudentData",chart3);

			//初始化双创项目学生统计
			chart4 = echarts.init($("#MchartFour")[0]);
			var option4 =getBarOption("双创项目学生统计","人数",colorArray3);
			chart4.setOption(option4);
			setBarChart("","/a/analysis/globalAnalysis/getStuDtn",chart4,30);

			//第二个下拉框change事件
			$("#select2").change(function(){
				var year=$(this).val();
				setChart(year,"/a/analysis/globalAnalysis/getProjectStudentData",chart3);
				setBarChart(year,"/a/analysis/globalAnalysis/getStuDtn",chart4,30);
			})

			//初始化导师分布比例
			chart5 = echarts.init($("#MchartFive")[0]);
			var legendData5=["校园导师","企业导师"];
			var colorArray5=["#548FD5","#76933C"];
			var option5 =getPieOption('导师分布比例',legendData5,"人",colorArray5);
			chart5.setOption(option5);
			setChart("","/a/analysis/globalAnalysis/getProjectTerData",chart5);

			//初始化 人气导师前5名
			chart6 = echarts.init($("#MchartSix")[0]);
			var option6 =getBarOption("人气导师前五名","",colorArray5);
			chart6.setOption(option6);
			setBarChart("","/a/analysis/globalAnalysis/getTeacherDtn",chart6,30);

			//第三个下拉框change事件
			$("#select3").change(function(){
				var year=$(this).val();
				setChart(year,"/a/analysis/globalAnalysis/getProjectTerData",chart5);
				setBarChart(year,"/a/analysis/globalAnalysis/getTeacherDtn",chart6,20);
			})

			//初始化 最热门技术领域前十名
			chart7 = echarts.init($("#MchartSeven")[0]);
			var colorArray7=["#4E81BD","#C1504C","#9BBB58"];
			var option7 =getBarOption("","",colorArray7,20);
			chart7.setOption(option7);
			setBarChart("","/a/analysis/globalAnalysis/getDomainDtn",chart7,15);

			//第四个下拉框change事件
			$("#select4").change(function(){
				var year=$(this).val();
				setBarChart(year,"/a/analysis/globalAnalysis/getDomainDtn",chart7,15);
			})




		});

		 function setChart(year,url,chart){
			$.ajax({
				type : "POST",
				url : url,
				data : {"year":year},
				success : function(data) {
					chart.setOption({
						series : [ {
							data : data
						} ]
					})
				}
			});
		}

		function getPieOption(text,legendData,unit,colorArray){
			var option = {
				width:'auto',
				title : {
					text : text,
					subtext : '',
					x : 'center'
				},
				tooltip : {
					//show:false,
					trigger : 'item',
					formatter: function(params){
						var percent=Math.ceil(params.percent);
						return  params.name+"("+ params.data.value+ (unit+"):")+ percent+"%";
					}
				},
				legend : {
					x : 'center',
					y : 'bottom',
					data : legendData
				},
				color : colorArray,
				series : [ {
					name: text,
					type : 'pie',
					radius : '55%',
					center : [ '50%', '50%' ],
					data : [],
					label : {
						normal : {
							textStyle : {
								color : '#000'
							}
						}
					},
					itemStyle : {
						emphasis : {
							shadowBlur : 10,
							shadowOffsetX : 0,
							shadowColor : 'rgba(0, 0, 0, 0.2)'
						},
						normal:{
							label:{
								show: true,
								formatter: function(params){
									var percent=Math.ceil(params.percent);
									return  params.name+"("+ params.data.value+ (unit+"):")+ percent+"%";
								}

							},
							labelLine :{show:true}
						}
					}
				} ]
			};
			return option;
		}

		function getBarOption(text,unit,colorArray,rotate){
			var option = {
				title : {
					text : text,
					subtext : '',
					x : 'center'
				},
				legend : {
					x : 'center',
					y : 'bottom'
				},
				color : colorArray,
				xAxis : [
					{
						type : 'category',
						axisLabel : {
							interval : 0,
							rotate : rotate
						}
					}
				],
				yAxis : [
					{
						type : 'value',
						name : unit
					}
				]
			};
			return option;
		}

		function setBarChart(year,url,chart,barWidth){

			$.ajax({
				type : "POST",
				url : url,
				data : {"year":year},
				success : function(data) {
					var dataList=data;
					var legendData = new Array();
					var seriesDate = new Array();
					for(var i=0;i<dataList.length;i++){
						legendData[i] = dataList[i].name;
						seriesDate[i]={
							name: dataList[i].name,
							type : 'bar',
							barWidth: barWidth,
							data : dataList[i].value,
							itemStyle : {
								normal:{
									label:{
										show: true,
										position: 'outside'
									},
									labelLine :{show:true}
								}
							}
						};
					}
					chart.setOption({
						legend : {data:legendData},
						xAxis : [{data : dataList[0].categories}],
						series :seriesDate
					})
				}
			});




		}

	</script>
</head>
<body>
<div class="mybreadcrumbs"><span>全局概览</span></div>
<div class="print-bar"><a id="printBtn" href="javascript:void (0);">打印报表</a></div>
<div id="viewPrint" class="view-content">
	<div class="container-fluid" style="border: 1px solid #ccc;border-radius: 5px">
		<div class="row title-bar">
			<div class="col-md-6 print-md-6">
				双创大赛及项目统计
			</div>
			<div class="col-md-6 text-right print-md-6">
				统计年份：
				<select id="select1" >
					<option value="">全部</option>
					<option value="2017">2017年</option>
					<option value="2016">2016年</option>
					<option value="2015">2015年</option>
					<option value="2014">2014年</option>
				</select>
			</div>
		</div>
		<div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
			<div class="col-md-6 col-border-right print-md-6">
				<div id="MchartOne" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
			<div class="col-md-6 print-md-6">
				<div id="MchartTwo" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
		</div>

		<div class="row title-bar">
			<div class="col-md-6 print-md-6">
				双创学生统计
			</div>
			<div class="col-md-6 text-right print-md-6">
				统计年份：
				<select id="select2">
					<option value="">全部</option>
					<option value="2017">2017年</option>
					<option value="2016">2016年</option>
					<option value="2015">2015年</option>
					<option value="2014">2014年</option>
				</select>
			</div>
		</div>
		<div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
			<div class="col-md-6 col-border-right print-md-6">
				<div id="MchartThree" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
			<div class="col-md-6 print-md-6">
				<div id="MchartFour" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
		</div>

		<div class="row title-bar">
			<div class="col-md-6 print-md-6">
				双创导师统计
			</div>
			<div class="col-md-6 text-right print-md-6">
				统计年份：
				<select id="select3">
					<option value="">全部</option>
					<option value="2017">2017年</option>
					<option value="2016">2016年</option>
					<option value="2015">2015年</option>
					<option value="2014">2014年</option>
				</select>
			</div>
		</div>
		<div class="row print-height" style="border:solid #ccc;border-width: 1px 0;">
			<div class="col-md-6 col-border-right print-md-6">
				<div id="MchartFive" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
			<div class="col-md-6 print-md-6">
				<div id="MchartSix" style="width: 100%;height:330px;padding:30px 0 15px;"></div>
			</div>
		</div>


		<div class="row title-bar">
			<div class="col-md-6 print-md-6">
				最热门技术前十名
			</div>
			<div class="col-md-6 text-right print-md-6">
				统计年份：
				<select id="select4">
					<option value="">全部</option>
					<option value="2017">2017年</option>
					<option value="2016">2016年</option>
					<option value="2015">2015年</option>
					<option value="2014">2014年</option>
				</select>
			</div>
		</div>
		<div class="row" style="border:solid #ccc;border-width: 1px 0 0;">
			<div class="col-md-12">
				<div id="MchartSeven" style="width: 100%;height:360px;padding:30px 0 15px;"></div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		var $printBtn = $('#printBtn');
//		window.onresize = chart7.resize;
//		chart7.resize({
//			width:800
//		});


		$printBtn.on('click',function(e){
			window.print();
		})
	})
</script>
</body>


</html>