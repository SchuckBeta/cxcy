var ratio = "";
var snumber = 0;
$(function(){
	GCSB.initGCSB();
	var v = nowstr();
	$(".Wdate").each(function(){
		!this.value && (this.value = v);
	});
	if($("#projectDeclare\\.id").val()!=""){
		snumber = $(".studenttb tbody tr").size();
		console.log("snumber",snumber);
		var type =  $("#projectDeclare\\.type").val();
		console.log("type",type);
		changeRatio(type);
	}

});

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

//根据项目类型和团队学生人数查找配比规则
function changeRatio(type){
	$.ajax({
		type:'post',
		url:'/f/project/projectDeclare/findRatio',
		data: {type:type,
			   snumber:snumber
			  },
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

function checkOnInit(){
	if($("#usertype").val()=='2'){
		dialogCyjd.createDialog(0, "导师不能申报项目");
		$(".btngroup").hide();
		return;
	}
	var user_name=$.trim($("#user_name").val());
	var user_officename=$.trim($("input[name='user_officename']").val());
	var user_no=$.trim($("input[name='user_no']").val());
	var user_professional=$.trim($("input[name='user_professional']").val());
	var user_mobile=$.trim($("input[name='user_mobile']").val());
	var user_email=$.trim($("input[name='user_email']").val());

	var currState=$.trim($("input[name='currState']").val());
	
	if(currState == 2){

		dialogCyjd.createDialog(0, "您已经毕业，不能申报项目", {
			dialogClass: 'dialog-cyjd-container none-close',
			buttons: [{
				text: "确定",
				'class': 'btn btn-sm btn-primary',
				click: function () {
					$(this).dialog('close')
					top.location = "/f";
				}
			}]
		});
        $(".btngroup").hide();
		return;
	}

	
	if($("select[id='projectDeclare.teamId']").find("option").length<=1){
		dialogCyjd.createDialog(0, "无可用团队信息，立即建设团队？", {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: "确定",
                'class': 'btn btn-sm btn-primary',
                click: function () {
                	$(this).dialog('close')
                	top.location = "/f/team/indexMyTeamList?custRedict=1";
                }
            },{
                text: '取消',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        });
		return;
	}
}
var GCSB = {
	plansIndex:0,
	initGCSB : function(){
		if($("#pageType").val()=="edit"){
			onProjectApplyInit($("[id='projectDeclare.actywId']").val(),$("[id='projectDeclare.id']").val(),checkOnInit);
		}
		$.each($("#resultTypeStr").val().split(","),function(i,item){
			$("input[name='projectDeclare.resultType'][ value='"+item+"']").attr("checked",true);
		});
		$.each($("#developmentStr").val().split(","),function(i,item){
			$("input[name='projectDeclare.development'][ value='"+item+"']").attr("checked",true);
		});
		GCSB.addEventToBtn();
		GCSB.printOut();
		GCSB.plansIndex=$(".task tbody tr").size()-1;
		
		$("[name='projectDeclare.development']").on("click",function(){
			if(this.checked){
				$(".content .iteminfor p.nocheckerror").css("display","none");
			}
		});
		$("[name='projectDeclare.resultType']").on("click",function(){
			if(this.checked){
				$(".content .exceptresult p.nocheckerror").css("display","none");
			}
		});
	},
	tabletrminus : function(){
		$(".minus").unbind();
		$(".minus").click(function(){
			if($(".task tbody tr").size()>1) {
				var rex="\\[(.+?)\\]";
				$(this).parent().parent().remove();//删除当前行
				GCSB.plansIndex--;//索引减一
				//重置序号
				$(".task tbody tr").find("td:first").each(function (i, v) {
					$(this).html(i + 1);
				});
				var index=0;
				$(".task tbody tr").each(function (i, v) {
					$(this).find("[name]").each(function (i2, v2) {
						var name=$(this).attr("name");
						var indx=name.match(rex)[1];
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
			GCSB.plansIndex++;
			var lastdate = $(this).closest("table").find('tr:last-child .timeerrorbox input:last-child').val();
			if(!lastdate) {
				lastdate = $('#plan-end-date').val() || nowstr();
			}
			var html='<tr>'
+'							<td>'
+(GCSB.plansIndex+1)
+'								</td>'
+'								<td>'
+'									<textarea required name="plans['+GCSB.plansIndex+'].content" class="form-control" maxlength="2000" rows="3"></textarea>'
+'								</td>'
+'								<td>'
+'									<textarea required name="plans['+GCSB.plansIndex+'].description" class="form-control" maxlength="2000" rows="3"></textarea>'
+'								</td>'
+'								<td style="vertical-align: middle">'+
									'<div class="time-input-inline">'
+'									<input required id="plan-start-date-' + GCSB.plansIndex + '" class="Wdate form-control" style="width: 100px;" type="text" name="plans['+GCSB.plansIndex+'].startDate" onfocus="WdatePicker(getStartDatepicker(' + GCSB.plansIndex + '))" value="' + lastdate + '"/>'
+'									<span>至</span>'
+'									<input required id="plan-end-date-' + GCSB.plansIndex + '" class="Wdate form-control" type="text"  style="width: 100px;" name="plans['+GCSB.plansIndex+'].endDate"  onfocus="WdatePicker(getEndDatepicker(' + GCSB.plansIndex + '))" value="' + lastdate + '"/>'
+'								</div></td>'
+'								<td>'
+'									<input  type="text" class="number required form-control" maxlength="20" name="plans['+GCSB.plansIndex+'].cost"/>'
+'								</td>'
+'								<td>'
+'									<textarea required class="form-control" rows="3" maxlength="2000" name="plans['+GCSB.plansIndex+'].quality"></textarea>'
+'								</td>'
+'								<td>'
+'									<a class="minus"></a>'
+'									<a class="plus"></a>'
+'								</td>'
+'							</tr>';
			$(".task tbody").append(html);
			GCSB.addEventToBtn();
			return false;
		})
	},
	addEventToBtn:function(){
		GCSB.tabletrminus();
		GCSB.tabletrplus();
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
	save:function(obj){
		//学分配比的校验
		var ischeck=true;
		ischeck=checkMenuByNum(ischeck);
		if(!ischeck){
			return false;
		}
		var onclickFn=$(obj).attr("onclick");
		// if(!$.trim($("[name='projectDeclare.name']").val())){
		// 	showModalMessage(0, "请填写项目名称")
		// 	return;
		// }
		$(obj).removeAttr("onclick");
		$("#form1").attr("action","save");
		$("label.error").attr("style","display:none;");
		$("p.nocheckerror").attr("style","display:none;");

		$(obj).prop('disabled', true);

		$("#form1").ajaxSubmit(function (data) {
			if(GCSB.checkIsToLogin(data)){
				dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
					dialogClass: 'dialog-cyjd-container none-close',
					buttons: [{
						text: '确定',
						'class': 'btn btn-primary',
						'click': function () {
							location.href = 'list'
						}
					}]
				})
			}else{
				$(obj).attr("onclick",onclickFn);
				if(data.ret==1){
					$("[id='projectDeclare.id']").val(data.id);
					dialogCyjd.createDialog(1, data.msg, {
						dialogClass: 'dialog-cyjd-container none-close',
						buttons: [{
							text: '进入项目列表',
							'class': 'btn btn-sm btn-primary',
							'click': function () {
								top.location = "list";
							}
						}, {
							text: '返回',
							'class': 'btn btn-sm btn-default',
							'click': function () {
								top.location = "list";
							}
						}]
					})
				}else{
					dialogCyjd.createDialog(0, data.msg)
				}
			}
			$(obj).prop('disabled', false);
		});
	},
	subBtn:function(obj){
		var onclickFn=$(obj).attr("onclick");
		if(mycheck()){
			//学分配比的校验
			var ischeck=true;
			ischeck=checkMenuByNum(ischeck);
			if(!ischeck){
				return false;
			}

			showAgreeMessage(function(yes){
				if(!yes) { return; }
				$(obj).removeAttr("onclick");
				$("#form1").attr("action","submit");
				$(obj).prop('disabled', true);
				$("#form1").ajaxSubmit(function (data) {
					if(GCSB.checkIsToLogin(data)){
						dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
							dialogClass: 'dialog-cyjd-container none-close',
							buttons: [{
								text: '确定',
								'class': 'btn btn-primary',
								'click': function () {
									location.href = 'list'
								}
							}]
						})
					}else{
						$(obj).attr("onclick",onclickFn);
						if(data.ret==1){
							$(obj).prev().remove();
							$(obj).remove();

							dialogCyjd.createDialog(1, data.msg, {
								dialogClass: 'dialog-cyjd-container none-close',
								buttons: [{
									text: '跟踪当前项目',
									'class': 'btn btn-sm btn-primary',
									'click': function () {
										top.location = "curProject";
									}
								}, {
									text: '返回',
									'class': 'btn btn-sm btn-default',
									'click': function () {
										top.location = "list";
									}
								}]
							})
						}else{
							dialogCyjd.createDialog(0, data.msg)
						}
					}
					$(obj).prop('disabled', false);
				});
			});
		}else{
			return false;
		}
	},
	findTeamPerson:function(){
		var teamId =$("#projectDeclare\\.teamId").val();
		if(!teamId){
			$(".studenttb thead").nextAll().remove();
			$(".teachertb thead").nextAll().remove();
			$("#ratio").text("");
			return false;
		}

		var type= $("#projectDeclare\\.type").val();
		if(type==""){
			dialogCyjd.createDialog(0, '请先选择项目类别')
			$("#projectDeclare\\.teamId").val("");
			return false;
		}


		$.ajax({
		            type: "GET",
		            url: "findTeamPerson",
		            data: "id="+$("[id='projectDeclare.teamId']").val(),
		            contentType: "application/json; charset=utf-8",
		            dataType: "json",
		            success: function (data) {
						$(".studenttb thead").nextAll().remove();
						$(".teachertb thead").nextAll().remove();
		                if(data){
							var shtml="";
							var scount=1;
							var thtml="";
							var tcount=1;
							$.each(data,function(i,v){
								if(v.user_type=='1'){
									shtml=shtml+"<tr>"
											+"<td>"+scount+"<input type='hidden' name='teamUserRelationList["+(scount-1)+"].userId' value='"+ v.userId +"' /></td>"
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
												shtml=shtml+"<td class='credit-ratio'><input class='form-control input-sm' name='teamUserRelationList["+(scount-1)+"].weightVal' value=''/> </td>"
												//添加表头
												if($("#studentTr th.credit-ratio").size() < 1){
													$("#studentTr").append("<th class='credit-ratio'>学分配比</th>")
												};

											}
										},
										error: function (msg) {
										}
									});

										   +"</tr>";
									scount++;
								}
								if(v.user_type=='2'){
									thtml=thtml+"<tr>"
																		+"<td>"+tcount+"</td>"
																	+"							<td>"+(v.name||"")+"</td>"
																	+"							<td>"+(v.no||"")+"</td>"
																	+"							<td>"+(v.teacherType||"")+"</td>"
																	+"							<td>"+(v.org_name||"")+"</td>"
																	+"							<td>"+(v.technical_title||"")+"</td>"
																	+"							<td>"+(v.domain||"")+"</td>"
																	+"							<td>"+(v.mobile||"")+"</td>"
																	+"							<td>"+(v.email||"")+"</td>"
																	+"						</tr>";
									tcount++;
								}
							});
							$(".studenttb").append(shtml);
							$(".teachertb").append(thtml);
							snumber = scount-1 ;
							changeRatio(type);
							checkProjectTeam();
						}
		            },
		            error: function (msg) {
						dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
							dialogClass: 'dialog-cyjd-container none-close',
							buttons: [{
								text: '确定',
								'class': 'btn btn-primary',
								'click': function () {
									top.location = top.location;
								}
							}]
						})
		            }
		        });
	},
}
function checkProjectTeam(){
	if(!$("[id='projectDeclare.teamId']").val()){
		return;
	}
	$.ajax({
		type:'post',
		url:'/f/project/projectDeclare/checkProjectTeam',
		data: {proid:$("[id='projectDeclare.id']").val(),
			actywId:$("[id='projectDeclare.actywId']").val(),
			lowType:$("[id='projectDeclare.type']").val(),
			teamid:$("[id='projectDeclare.teamId']").val()},
		success:function(data){
			if (GCSB.checkIsToLogin(data)) {
				dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
					dialogClass: 'dialog-cyjd-container none-close',
					buttons: [{
						text: '确定',
						'class': 'btn btn-primary',
						'click': function () {
							top.location = top.location;
						}
					}]
				})

			} else {
				if (data.ret == '0') {
					dialogCyjd.createDialog(0, data.msg)

				}
			}
		}
	});
}
function getStartDatepicker(row) {
	if(typeof  row === "number") {
		return {minDate:"#F{$dp.$D('plan-start-date" + (row > 0 ? "-" + (row - 1) : "") + "')}",ychanged: onStartDateChange,Mchanged: onStartDateChange,dchanged: onStartDateChange};
	} else {
		return {minDate:'%y-%M-%d',ychanged: onStartDateChange,Mchanged: onStartDateChange,dchanged: onStartDateChange};
	}
}
function getEndDatepicker(row){
	var str;
	if(typeof row === "number") {
		str = "-" + row;
	} else {
		str = "";
	}
	return {minDate:"#F{$dp.$D('plan-start-date" + str + "')}"};
}

function onStartDateChange(dp){
	var v = dp.cal.getNewDateStr();
	var enddate = $(this).siblings('input')[0];
	if(v > enddate.value) {
		enddate.value = v;
	}
	var tr=$(this).parent().parent();
	var nextStart=tr.next().find(".Wdate")[0];
	if(nextStart&& v>nextStart.value){
		tr.nextAll().find(".Wdate").val(v);
	}
	enddate.focus();
}

function nowstr(){
	var d = new Date;
	var parts = [{
		method: 'getFullYear'
	}, {
		method: 'getMonth',
		delta: 1
	}, {
		method: 'getDate'
	}];
	return parts.map(function(o){
		var v = d[o.method]();
		if(typeof o.delta === "number") {
			v += o.delta;
		}
		if(v < 10) {
			v = '0' + v;
		}
		return v;
	}).join('-');
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
					dialogCyjd.createDialog(0, "学分配比不符合规则！",{
						buttons: [{
							'text': '确定',
							'class':'btn btn-primary',
							'click': function () {
								$( this ).dialog( "close" );
								$("input[name='teamUserRelationList[0].weightVal']" ).focus();
							}
						}]
					});
				}
			}
		},
		error: function (msg) {
			dialogCyjd.createDialog(1, msg.status)
			// showModalMessage(0, msg);
		}
	});
	return ischeck;
}
function showAgreeMessage(callback){
	var buf = ['<div><div class="agree-title">项目团队成员承诺</div>'];
	buf.push('<div class="agree-content">本人保证以上填报内容的真实性。如果获得立项，本人（以及项目组成员）将严肃、认真地实施项目计划，严格执行审批的项目经费预算并按要求及时报送有关材料。');
	buf.push('</div><div class="agree-actions"><label><input type="radio" name="accept-protocol" value="1" checked/> 同意</label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label><input type="radio" name="accept-protocol" value="0" /> 不同意</label></div>');
	buf.push('</div>');
	var dlg = $( buf.join('') );
	dlg.appendTo(document.body);
	dlg.dialog({
		modal: true,
		dialogClass: "agree-box",
		buttons: [{
			text: "继续提交",
			'class': 'btn btn-primary',
			disabled: false,
			click: function () {
				callback && callback(true);
				$( this ).dialog( "close" );
			}
		}, {
			text: "返回",
			'class': 'btn btn-default',
			click: function () {
				callback && callback(false);
				$( this ).dialog( "close" );
			}
		}],
		open: function(){
			var dlg = $(this);
			dlg.find('.agree-actions :radio').change(function(e){
				var v = parseInt(this.value);
				$(this).closest(".ui-dialog").find("button:contains('继续提交')").prop('disabled',v == 0);
			});
		}
	});
}