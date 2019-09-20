var teamMemberChart;
var teamPercentageChart;

var teamMemberPercentageChart ;
$(function () {	
	//人数发展趋势
	teamMemberChart = echarts.init(document.getElementById('teamMemberChart'),'macarons');
//团队统计
	teamPercentageChart = echarts.init(document.getElementById('teamPercentageChart'),'macarons');

	teamMemberPercentageChart = echarts.init(document.getElementById('teamMemberPercentageChart'),'macarons');
	renderTeamYearData();
	teamAnalysisChange();
});
//团队统计折线图
function teamMemberPerOption(color,rotate, xAxisData, seriesData) {
    return {
        tooltip: {
            trigger: 'axis'
        },
//        color: color ||  ['#8064a2', '#4f81bd', '#c0504d', '#9bbb59'],
        grid: {
          height: '55%'
        },
        xAxis: {
            type: 'category',
            axisLabel: {
                interval: 0,
                rotate: rotate || 30
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
    };
}
//团队统计饼图 option
function teamPercentOption(color, legend, seriesData) {
    return {
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                var name = params.name;
                return '<span class="teamPieItem" style="background-color: ' + params.color + '"></span>' + name.substring(0, name.indexOf('(')) + params.value
            }
        },
        selectedMode: 'single',
        legend: [{
            x: 'center',
            y: 'bottom',
            orient: 'vertical',
            data: legend
        }],
//        color: color || ['#8064a2', '#4f81bd', '#c0504d', '#9bbb59'],
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
    }
}
function teamAnalysisChange(){
	if("1"==$("input[name='teamAnalysisType']:checked").val()){
		renderTeamData("/a/analysis/teamAnalysis/getTeamNum");
	}else{
		renderTeamData("/a/analysis/teamAnalysis/getTeamMemsNum");
	}
}
function renderTeamData(url){
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        data: {year:$("#teamNumYear").val()},
        cache: false,
        traditional: true,
        success: function (data) {
        	if(!data){
        		teamPercentageChart.clear();
        		teamMemberPercentageChart.clear();
        	}else{
	        	teamPercentageChart.setOption(teamPercentOption(null,data.pieLegend,data.pieData));
	        	teamMemberPercentageChart.setOption(teamMemberPerOption(null,null,data.barXdata,data.barSdata));
        	}
        },
        error: function (XMLHttpResponse) {
        }
    });
}


//项目团队发展hhe人数趋势
function projectTeamDevelopOption(title, legend, xAxisData, seriesData, colors) {
    return {

        tooltip: {
            trigger: 'axis'
        },
        legend: {

            x: 'center',
            y: 'bottom',
//            orient: 'vertical',
            data: legend
        },
        grid: [{
            top: 30,

            bottom: 50,
            x:'center',
            width: '90%'
        }],
//        color: colors || ['#b5cb90', '#f79747', '#c0504d', '#8064a3'],
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
function renderTeamYearData(){
    $.ajax({
        type: "POST",
        url: "/a/analysis/teamAnalysis/getTeamYearMemsNum",
        dataType: "json",
        data: {},
        cache: false,
        traditional: true,
        success: function (data) {
        	if(!data){
        		teamMemberChart.clear();
        	}else{
        		teamMemberChart.setOption(projectTeamDevelopOption('人数发展趋势',data.legend,data.xData,data.sData,null));
        	}
        },
        error: function (XMLHttpResponse) {
        }
    });
}