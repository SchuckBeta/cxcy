var ratio = "";
var snumber = 0;
//学分配比校验
function checkRatio(){
	var result = true ;
	if(ratio!=""){
		var creditArr = [];
		$('.credit-ratio input.form-control').each(function(i,item){
			creditArr.push($(item).val());
		});

		creditArr.sort(function (a,b) {
			return b -a;
		});
		var ratioArr  = ratio.split(':').sort(function(a,b){
			return b - a;
		});

		if(creditArr.join(':') !== ratioArr.join(':')){
			result=false;
		}
	}
	return result;
}
//根据项团队学生人数查找配比规则
function changeRatio(){
	$.ajax({
		type:'post',
		url:'/f/gcontest/gContest/findRatio',
		data: {snumber:snumber},
		success:function(data){
			if(data!=""){
				$("#ratio").text("学分配比规则："+data);
			}else{
				$('th.credit-ratio,td.credit-ratio').remove();
				$("#ratio").text("");
			}
			ratio = data;
		}
	});
}

$(function(){
	if($("#id").val()!=""){
		snumber = $(".studenttb tbody tr").size();
		//changeRatio();
	}
	// fileDownUp("gcontest","delFile");

	$('input[name="guochuang"]').change(function(){
		var val = $(this).val();
		$(this).parent().parent().find('select').css('visibility',function(){
			return val == "2" ? 'visible' : 'hidden'
		})
	})
});
function checkOnInit(){
	if($("select[id='teamId']").find("option").length<=1){
		showModalMessage(0, "无可用团队信息，立即建设团队？", {
			确定: function() {
				top.location = "/f/team/indexMyTeamList";
			},
			取消: function() {
				$( this ).dialog( "close" );
			}
		});
		return;
	}
}

function findTeamPerson(){
	var teamId = $('#teamId').val();


	if(!teamId){
		$(".studenttb tbody").html("");
		$(".teachertb tbody").html("");
		return false;
	}

	$.ajax({
		type: "GET",
		url: '/f/promodel/proModel/findTeamPerson?id='+ teamId	+"&actywId="+$("[id='actYwId']").val(),
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (data) {
			if(data){
				var shtml="";
				var scount=1;
				var thtml="";
				var tcount=1;
				var list = data.map;
				$.each(list,function(i,v){
					if(v.user_type=='1'){
						shtml=shtml+"<tr>"
							+"<td>"+scount+"</td>"
							+"<td>"+(v.name||"")+"</td>"
							+"<td>"+(v.no||"")+"</td>"
							+"<td>"+(v.org_name||"")+"</td>"
							+"<td>"+(v.professional||"")+"</td>"
							+"<td>"+(v.domain||"")+"</td>"
							+"<td>"+(v.mobile||"")+"</td>"
							+"<td>"+(v.instudy||"")+"</td>"
							+"<input type='hidden' name='teamUserHistoryList["+(scount-1)+"].userId' value='"+ v.userId +"' />"
							// +"<td class='credit-ratio'><input class='form-control input-sm' name='teamUserHistoryList["+(scount-1)+"].weightVal' value='"+ (v.weightVal||"")  + "'/> </td>"
						+"</tr>";
						scount++;
					}
					if(v.user_type=='2'){
						thtml=thtml+"<tr>"
							+"<td>"+tcount+"</td>"
							+"<td>"+(v.name||"")+"</td>"
							+"<td>"+(v.org_name||"")+"</td>"
							+"<td>"+(v.technical_title||"")+"</td>"
							+"<td>"+(v.domain||"")+"</td>"
							+"<td>"+(v.mobile||"")+"</td>"
							+"<td>"+(v.email||"")+"</td>"
						+"</tr>";
						tcount++;
					}
				});

				//添加表头
				// if($("#studentTr th.credit-ratio").size() < 1){
				// 	$("#studentTr").append("<th class='credit-ratio'>学分配比</th>")
				// };
				snumber = scount-1 ;
				// changeRatio(type);

				$(".studenttb tbody").html(shtml);
				$(".teachertb tbody").html(thtml);
			}
		},
		error: function (msg) {
			showModalMessage(0, '请求失败');
		}
	});
}


