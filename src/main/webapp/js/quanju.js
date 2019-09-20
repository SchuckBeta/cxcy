$(document).ready(function() {
	pa.getData();
});
var pa = {
	getData : function() {
		// 向后台发异步请求，获取数据
		$.ajax({
			type : "POST",
			url : "/a/analysis/globalAnalysis/getData",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : {},
			cache : false,
			traditional : true,
			success : function(data) {
				pa.renderData1(data.data1);
				pa.renderData2(data.data2);
				pa.renderData3(data.data3);
			},
			error : function(XMLHttpResponse) {
			}
		});
	},
	renderData1 : function(data1) {
		if (data1) {
			this.chart1 = echarts.init($("#MchartOne")[0]);
			var option = {
				title : {
					text : '双创学生比例',
					subtext : '',
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					orient : 'vertical',
					x : '75%',
					y : '7%',
					data : data1.ja1
				},
				series : [ {
					name : '访问来源',
					type : 'pie',
					radius : '55%',
					center : [ '50%', '50%' ],
					data : data1.ja2,
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
					}
				} ],
				color : [ '#f6b89d', '#a6c3e6' ]
			};
			pa.chart1.setOption(option);
		}
	},
	renderData2 : function(data2) {
		if (data2) {
			this.chart2 = echarts.init($("#MchartTwo")[0]);
			var option = {
				title : {
					text : '导师分布比例',
					subtext : '',
					x : 'center'
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					orient : 'vertical',
					x : '75%',
					y : '7%',
					data : data2.ja1
				},
				series : [ {
					name : '访问来源',
					type : 'pie',
					radius : '55%',
					center : [ '50%', '50%' ],
					data : data2.ja2,
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
					}
				} ],
				color : [ '#fff2cc', '#a9d18e' ]
			};
			pa.chart2.setOption(option);
		}
	},
	renderData3 : function(data3) {
		this.chart3 = echarts.init($("#MchartThree")[0]);
		if (data3) {
			var option = {
				color : [ '#ba8cdc', '#ed7d31', '#a5a5a5' ],
				title : {
					text : '双创项目师生比例',
					subtext : ''
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : data3.ja1
				},

				calculable : true,
				xAxis : [ {
					type : 'category',
					data : data3.ja2
				} ],
				yAxis : [ {
					type : 'value'
				} ],
				series : data3.ja3
			};
			pa.chart3.setOption(option);
		}
	}
}