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
		if(snumber == null || snumber == undefined ){
			snumber=0;
		}
		changeRatio();
	}

	DSSB.initDSSB();
	try{
		fileDownUp("gcontest","delFile");
	}catch (e){
		console.log(e)
	}
	// $('#myradiobox :radio').change(function () {
	// 	if(!this.checked) { return; }
	// 	if(this.value =='1'){
	// 		$("select[name='pId']").val("");
	// 	}
	// 	$(this).siblings('select').css('visibility', this.value === "2" ? 'visible' : 'hidden');
	//
	// })
	$('input[name="guochuang"]').change(function(){
		var val = $(this).val();
		$(this).parent().parent().find('select').css('visibility',function(){
			return val == "2" ? 'visible' : 'hidden'
		})
	})
});
function checkOnInit(){
	var user_name=$.trim($("input[name='shenbaoren']").val());
	var user_officename=$.trim($("#company").val());
	var user_no=$.trim($("#zhuanye").val());
	var user_professional=$.trim($("input[name='zynj']").val());
	var user_mobile=$.trim($("#mobile").val());
	var user_email=$.trim($("#email").val());
	if(!user_name||!user_officename||!user_no||!user_mobile||!user_email){
		showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
			确定: function() {
				top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
			},
			取消: function() {
				$( this ).dialog( "close" );
			}
		});
		return;
	}
	if(!mobileRegExp.test(user_mobile)){
		showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
			确定: function() {
				top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
			},
			取消: function() {
				$( this ).dialog( "close" );
			}
		});
		return;
	}
	if(!emailRegExp.test(user_email)){
		showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
			确定: function() {
				top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
			},
			取消: function() {
				$( this ).dialog( "close" );
			}
		});
		return;
	}
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
var DSSB = {
	plansIndex:0,
	initDSSB : function(){
		if($("#pageType").val()=="edit"){
			onGcontestApplyInit($("[id='actywId']").val(),$("[id='id']").val(),checkOnInit);
		}
		// DSSB.addEventToBtn();
		DSSB.printOut();
		DSSB.plansIndex=$(".task tbody tr").size()-1;
		$("#pId").on('change', function() { 	
			var options=$("#pId option:selected");
		    var opt=options.text(); 
		    $("#projectName").val(opt);
		});
	},
	tabletrminus : function(){
		$(".minus").unbind();
		$(".minus").click(function(){
			if($(".task tbody tr").size()>1) {
				var rex="\\[(.+?)\\]";
				$(this).parent().parent().remove();//删除当前行
				DSSB.plansIndex--;//索引减一
				//重置序号
				$(".task tbody tr").find("td:first").each(function (i, v) {
					$(this).html(i + 1);
				});
				var index=0;
				$(".task tbody tr").each(function (i, v) {
					$(this).find("[name]").each(function (i2, v2) {
						var name=$(this).attr("name");
						var indx=name.match(rex)[1];;
						$(this).attr("name",name.replace(indx,index));
					});
					index++;
				});
			}
			return false;
		});
	},
	tabletrplus : function(){
		$(".plus").unbind();
		$(".plus").click(function(){
			DSSB.plansIndex++;
			var html='<tr>'
				+'<td>'
				+(DSSB.plansIndex+1)
				+'</td>'
				+'<td>'
				+'	<textarea name="plans['+DSSB.plansIndex+'].content"></textarea>'
				+'</td>'
				+'<td>'
				+'	<textarea name="plans['+DSSB.plansIndex+'].description"></textarea>'
				+'</td>'
				+'<td class="timeerrorbox">'
				+'	<input class="Wdate" type="text" name="plans['+DSSB.plansIndex+'].startDate" onClick="WdatePicker()"/>'
				+'	<span>至</span>'
				+'	<input class="Wdate" type="text" name="plans['+DSSB.plansIndex+'].endDate"  onClick="WdatePicker()"/>'
				+'</td>'
				+'<td>'
				+'	<textarea name="plans['+DSSB.plansIndex+'].cost"></textarea>'
				+'</td>'
				+'<td>'
				+'	<textarea name="plans['+DSSB.plansIndex+'].quality"></textarea>'
				+'</td>'
				+'<td>'
				+'	<a class="minus"></a>'
				+'	<a class="plus"></a>'
				+'</td>'
			+'</tr>';
			$(".task tbody").append(html);
			DSSB.addEventToBtn();
			return false;
		})
	},
	addEventToBtn:function(){
		DSSB.tabletrminus();
		DSSB.tabletrplus();
	},
	printOut:function(){
		$("#print").click(function(){
			document.body.innerHTML=document.getElementById('wholebox').innerHTML;
			window.print();
		})
	},

	checkIsToLogin:function(data){
		try{
			if(data.indexOf("id=\"imFrontLoginPage\"") != -1){
				return true;
			}
		}catch(e){
			return false;
		}
	},
	save:function(){
		var ischeck=true;
		ischeck=checkMenuByNum(ischeck);
		if(!ischeck){
			return false;
		}
		/*//学分配比的校验
		if(!checkRatio()){
			showModalMessage(0, "学分配比不符合规则！",{
				"确定":function() {
					$( this ).dialog( "close" );
					$("input[name='teamUserHistoryList[0].weightVal']" ).focus();
				}
			});
			return false;
		}*/

		if(validate1.form()) {
			$("#competitionfm").attr("action", "/f/gcontest/gContest/save");

			$("#competitionfm").ajaxSubmit(function (data) {
				if (data.ret == 1) {
					showModalMessage(1, data.msg, {
						确定: function() {

							location.href = 'list'
						}
					});
				} else {
					showModalMessage(0, data.msg);
				}
			});
		}
	},
	submit:function(){
		//学分配比的校验
		var ischeck=true;
		ischeck=checkMenuByNum(ischeck);
		if(!ischeck){
			return false;
		}
		$("#competitionfm").attr("action","/f/gcontest/gContest/submit");
		if(validate1.form()){
			$('#btnSubmit').hide().prop('disabled', true).prev().hide().prop('disabled', true).end().next().hide().find('input[name="file"]').prop('disabled', true);
			$("#competitionfm").ajaxSubmit(function (data) {
				if(DSSB.checkIsToLogin(data)){
					$('#btnSubmit').show().prop('disabled', false).prev().show().prop('disabled', false).end().next().show().find('input[name="file"]').prop('disabled', false);
					showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
						确定: function() {
							location.href = 'list'
						}
					});
				}else{
					if(data.ret==1){
						showModalMessage(1, data.msg, {
							跟踪当前项目:function() {
								top.location = "viewList";
							},
							返回: function() {
								$( this ).dialog( "close" );
								location.href = 'list'
							}
						});
					}else{
						showModalMessage(0, data.msg);
						$('#btnSubmit').show().prop('disabled', false).prev().show().prop('disabled', false).end().next().show().find('input[name="file"]').prop('disabled', false);
					}
				}
			});
		}	
		
	/*	if(validate1.form()){
			$("#competitionfm").attr("action","/f/gcontest/gContest/submit");
			$("#competitionfm").submit();
		}else{
			return false;
		}*/
	},
	findTeamPerson:function(){

		var actywId=$.trim($("#actywId").val());

		$.ajax({
			type: "GET",
			url: "findTeamPerson",
			data: "id="+$("[id='teamId']").val()+"&actywId="+actywId,
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data) {
				$(".studenttb tbody").html("");
				$(".teachertb tbody").html("");
				if(data){
					var shtml="";
					var scount=1;
					var thtml="";
					var tcount=1;
					$.each(data,function(i,v){
						if(v.user_type=='1'){
							shtml=shtml+"<tr>"
								+"<td>"+scount+"<input type='hidden' name='teamUserHistoryList["+(scount-1)+"].userId' value='"+ v.userId +"' /></td>"
								+"<td>"+(v.name||"")+"</td>"
								+"<td>"+(v.no||"")+"</td>"
								+"<td>"+(v.org_name||"")+"</td>"
								+"<td>"+(v.professional||"")+"</td>"
								+"<td>"+(v.domain||"")+"</td>"
								+"<td>"+(v.mobile||"")+"</td>"
								+"<td>"+(v.instudy||"")+"</td>"

							$.ajax({
								type: "GET",
								url: "/f/authorize/checkMenuByNum",
								data: "num=5",
								async:false,
								contentType: "application/json; charset=utf-8",
								dataType: "json",
								success: function (data) {
									if(data){
										shtml=shtml+"<td class='credit-ratio'><input class='form-control input-sm' name='teamUserHistoryList["+(scount-1)+"].weightVal' value=''/> </td>"
										//添加表头
										if($("#studentTr th.credit-ratio").size() < 1){
											$("#studentTr").append("<th class='credit-ratio'>学分配比</th>")
										};
										
									}
								},
								error: function (msg) {
									showModalMessage(0, msg);
								}
							});


								+"</tr>";
							scount++;
						}
						if(v.user_type=='2'){
							thtml=thtml+"<tr>"
								+"<td>"+tcount+"</td>"
								+"<td>"+(v.name||"")+"</td>"
								+"<td>"+(v.no||"")+"</td>"
								+"<td>"+(v.org_name||"")+"</td>"
								+"<td>"+(v.technical_title||"")+"</td>"
								+"<td>"+(v.domain||"")+"</td>"
								+"<td>"+(v.mobile||"")+"</td>"
								+"<td>"+(v.email||"")+"</td>"
							+"</tr>";
							tcount++;
						}
					});
					

					snumber = scount-1 ;
					changeRatio();
					checkGcontestTeam();
					$(".studenttb tbody").html(shtml);
					$(".teachertb tbody").html(thtml);
				}
			},
			error: function (msg) {
				showModalMessage(0, msg);
			}
		});
	}
}

function checkMenuByNum(ischeck){
	$.ajax({
		type: "GET",
		url: "/f/authorize/checkMenuByNum",
		data: "num=5",
		async:false,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function (data) {
			if(data){
				if(!checkRatio()){
					ischeck=false;
					showModalMessage(0, "学分配比不符合规则！",{
						"确定":function() {
							$( this ).dialog( "close" );
							$("input[name='teamUserRelationList[0].weightVal']" ).focus();
						}
					});

				}
			}
		},
		error: function (msg) {
			showModalMessage(0, msg);
		}
	});
	return ischeck;
}
function checkGcontestTeam(){
	if(!$("[id='teamId']").val()){
		return;
	}
	$.ajax({
		type:'post',
		url:'/f/gcontest/gContest/checkGcontestTeam',
		data: {proid:$("[id='id']").val(),
			actywId:$("[id='actywId']").val(),
			teamid:$("[id='teamId']").val()},
		success:function(data){
			if(DSSB.checkIsToLogin(data)){
				showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
					确定: function() {
						top.location = top.location;
					}
				});
			}else{
				if(data.ret=='0'){
					showModalMessage(0, data.msg, {
						确定: function() {
							$( this ).dialog( "close" );
						}
					});
				}
			}
		}
	});
}
