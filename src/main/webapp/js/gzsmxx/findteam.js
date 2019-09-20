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

$(function(){
	if($("#id").val()!=""){
		snumber = $(".studenttb tbody tr").size();
	}
	// fileDownUp("gcontest","delFile");

	$('input[name="guochuang"]').change(function(){
		var val = $(this).val();
		$(this).parent().parent().find('select').css('visibility',function(){
			return val == "2" ? 'visible' : 'hidden'
		})
	})
});

function findTeamPerson(){
	if(!$("[id='proModel.teamId']").val()){
		$(".studenttb tbody").html("");
		$(".teachertb tbody").html("");
		var $proModelTeamEle = $("[id='proModel.teamId']");
		var $errorEle = $proModelTeamEle.parent().find('span.error');
		$errorEle.hide().find('a').text('').attr('href', '')
		return;
	}
	$.ajax({
		type: "GET",
		url: "/f/promodel/proModel/findTeamPerson",
		data: "id="+$("[id='proModel.teamId']").val()
		+"&actywId="+$("[id='actYwId']").val()
		,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (data) {
			var ret=data.ret;

			if(ret=="1"){
				var map=data.map;
				$(".studenttb tbody").html("");
				$(".teachertb tbody").html("");
				if(map){
					var shtml="";
					var scount=1;
					var thtml="";
					var tcount=1;
					$.each(map,function(i,v){
						if(v.user_type=='1'){
							shtml=shtml+"<tr>"

								+"<td>"+scount+"<input type='hidden' name='proModel.studentList["+(scount-1)+"].userId' value='"+ v.userId +"' /></td>"
								+"<td>"+(v.name||"")+"</td>"
								+"<td>"+(v.no||"")+"</td>"
								+"<td>"+(v.mobile||"")+"</td>"
								+"<td>"+(v.org_name||"")+"</td>"
								+"</tr>";
							scount++;
						}
						if(v.user_type=='2'){
							thtml=thtml+"<tr>"
								+"<td>"+tcount+"<input type='hidden' name='proModel.teacherList[" + (tcount - 1) + "].userId' value='" + v.userId + "' /></td>"
								+"<td>"+(v.name||"")+"</td>"
								+"<td>"+(v.no||"")+"</td>"
								+"<td>"+(v.teacherType||"")+"</td>"
								+"<td>"+(v.postTitle||"")+"</td>"
								+"<td>"+(v.education||"")+"</td>"
								+"<td>"+(v.mobile||"")+"</td>"
								+"<td>"+(v.email||"")+"</td>"
							+"</tr>";
							tcount++;
						}
					});
					$(".studenttb tbody").html(shtml);
					$(".teachertb tbody").html(thtml);
					var $proModelTeamEle = $("[id='proModel.teamId']");
					var $errorEle = $proModelTeamEle.parent().find('span.error');
					$errorEle.hide().find('a').text('').attr('href', '')
				}
			}else{
				var teamId=data.teamId;
				var type=$("[id='proModel.type']").val();
				var proType=$("[id='proModel.proCategory']").val();
				var $proModelTeamEle = $("[id='proModel.teamId']");
				var $errorEle = $proModelTeamEle.parent().find('span.error');
				var $error = $('<span for="proModel.teamId" class="error"><a href="' + ctxFront + '/team/indexMyTeamList?opType=2&id='+teamId+'&proType='+proType+'&type='+type+'">'+data.msg+'</a></span>');
				if($errorEle.size() > 0){
					$errorEle.show().find('a').attr('href', '/f/team/indexMyTeamList?opType=2&id='+teamId+'&proType='+proType+'&type='+type).text(data.msg)
				}else{
					$proModelTeamEle.parent().append($error)
				}

					// .html('<a href="' + ctxFront + '/team/indexMyTeamList?teamId='+teamId+'&proType='+proType+'>'+data.msg+'</a>')
				// $error.insertAfter($("[id='proModel.teamId']"));
				// $("[id='proModel.teamId']").next().show().append('<a href="' + ctxFront + '/team/indexMyTeamList?teamId='+teamId+'&proType='+proType+'>'+data.msg+'</a>');
				// $(".studenttb tbody").html('');
				// $(".teachertb tbody").html('');

				// showModalMessage(0, data.msg);
			}

		},
		error: function (msg) {
			showModalMessage(0, msg);
		}
	});
}

$(function () {
	// findTeamPerson()
})


