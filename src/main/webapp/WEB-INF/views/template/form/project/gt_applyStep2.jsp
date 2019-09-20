<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>

<div class="container container-ct">
    <div class="main-wrap">
        <ol class="breadcrumb">
            <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
            <li><a href="${ctxFront}//page-innovation">双创项目</a></li>
            <li class="active">申报</li>
        </ol>
        <div class="row-step-cyjd" style="height: 30px; margin-bottom: 15px;">
            <div class="step-indicator" style="margin-right: -20px;">
                <a class="step">第一步（填写个人基本信息）</a> 
                <a class="step completed">第二步（填写项目基本信息）</a>
                <a class="step">第三步（提交项目申报附件）</a>
            </div>
        </div>
        <div class="row-apply" style="margin-top:40px;">
            <div class="form-horizontal">
                <div class="row row-user-info" style="margin-bottom:0;">
                    <div  class="col-xs-3">
                        <label class="application-one">项目编号：</label>
                        <p class="application-p">${proModel.competitionNumber}</p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">填报日期：</label>
                        <p class="application-p"><fmt:formatDate value='${proModel.createDate}' pattern='yyyy-MM-dd'/></p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">申报人：</label>
                        <p class="application-p">${leader.name}</p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">学号：</label>
                        <p class="application-p">${leader.no}</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row-apply">
            <h4 class="titlebar">${proModelType}申报</h4>
            <form:form id="form1" modelAttribute="proModel" action="#" method="post" class="form-horizontal" cssstyle="margin-left:20px;"
               	enctype="multipart/form-data">
            	<form:hidden path="id"/>
        		<form:hidden path="actYwId"/>
        		<form:hidden path="year"/>
        		<div class="form-group" style="position:relative;left:45px;">
                    <label class="control-label col-xs-1" style="width: 150px;"><i>*</i>项目名称：</label>
                    <div class="col-xs-4">
                        <input type="text" class="form-control required fill" oldv="${proModel.pName}"
                               maxlength="128" name="pName" value="${proModel.pName}"
                               placeholder="最多128个字符">
                    </div>
                </div>
        		<div class="form-group" style="position:relative;left:45px;margin-top:30px;">
                    <label class="control-label col-xs-1" style="width: 150px;"><i>*</i>项目类别：</label>
                    <div class="col-xs-3">
                        <form:select  path="proCategory"
                                             class="form-control required fill" oldv="${proModel.proCategory}" onchange="findTeamPerson();">
                                    <form:option value="" label="--请选择--"/>
                                    <form:options items="${fns:getProCategoryByActywId(proModel.actYwId)}" itemValue="value" itemLabel="label"
                                                  htmlEscape="false"/></form:select>
                    </div>
                </div>
				<div class="form-group" style="position:relative;left:45px;margin-top:30px;">
                    <label class="control-label col-xs-1" style="width: 150px;"><i>*</i>项目简介：</label>
                    <div class="col-xs-9">
                        <textarea class="form-control required fill" oldv="${proModel.introduction }" name="introduction" 
                                  rows="3"
                                  maxlength="512" placeholder="最多512个字符">${proModel.introduction }</textarea>
                    </div>
                </div>
                <div class="form-group" style="position:relative;left:45px;margin-top:30px;">
                    <label class="control-label col-xs-1" style="width: 150px;"><i>*</i>选择团队：</label>
                    <div class="col-xs-3">
                        <form:select required="required" onchange="findTeamPerson();"
                                         path="teamId"
                                         class="input-medium form-control fill" oldv="${proModel.teamId }">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name"
                                              htmlEscape="false"/></form:select>
                    </div>
                   	<c:if test="${fn:length(teams)==0}">
                    	<label class="control-label col-xs-2 build-team">没有可用团队请<a href="${ctxFront}/team/indexMyTeamList">创建团队</a></label>
                    </c:if>
                </div>
                <div class="tab-pane">
                    <div class="form-group" style="position:relative;left:45px;margin-top:36px;">
                        <label class="control-label col-xs-1" style="width: 150px;">学生团队：</label>
                        <div class="col-xs-9">
                            <table class="studenttb table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
		                         	<th>序号</th>
				                    <th>姓名</th>
				                    <th>工号</th>
				                    <th>学院</th>
				                    <th>专业</th>
				                    <th>手机号</th>
				                    <th>现状</th>
				                    <th>当前在研</th>
				                    <th>技术领域</th>
			                    </tr>
                                </thead>
                                <tbody>
                                <c:if test="${teamStu!=null&&teamStu.size() > 0}">
			                        <c:forEach items="${teamStu}" var="item" varStatus="status">
			                            <tr>
			                                <td>${status.index+1 }<input type="hidden" name="studentList[${status.index}].userId"
			                                           value="${item.userId}"></td>
					                        <td>${item.name }</td>
					                        <td>${item.no }</td>
					                        <td>${item.org_name }</td>
					                        <td>${item.professional}</td>
					                        <td>
					                        	${item.mobile }
					                        </td>
					                        <td>${fns:getDictLabel(item.currState, 'current_sate', '')}</td>
					                        <td>${item.curJoin }</td>
					                        <td>${item.domain }</td>
			                            </tr>
			                        </c:forEach>
			                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group" style="position:relative;left:45px;">
                        <label class="control-label col-xs-1" style="width: 150px;">指导教师：</label>
                        <div class="col-xs-9">
                            <table class="teachertb table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
			                        <th>序号</th>
				                    <th>姓名</th>
				                    <th>工号</th>
				                    <th>单位(学院或企业、机构)</th>
				                    <th>导师来源</th>
				                    <th>当前指导</th>
				                    <th>技术领域</th>
			                    </tr>
                                </thead>
                                <tbody>
                                <c:if test="${teamTea!=null&&teamTea.size() > 0}">
			                        <c:forEach items="${teamTea}" var="item" varStatus="status">
			                            <tr>
			                                <td>${status.index+1}<input type="hidden" name="teacherList[${status.index}].userId"
			                                           value="${item.userId}"></td>
			                                <td>${item.name }</td>
					                        <td>${item.no }</td>
					                        <td>${item.org_name }</td>
					                        <td>${item.teacherType}</td>
					                        <td>${item.curJoin }</td>
					                        <td>${item.domain }</td>
			                            </tr>
			                        </c:forEach>
			                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="form-horizontal form-enter-apply" style="margin:20px 0 30px -2px;">
                <div class="form-group">
                    <label class="control-label col-xs-2">项目logo：</label>
                    <div class="col-xs-6" style="height: 104px;" >
                        <div id="ground-img">
                            <div >
                                <img style="width:115px;height:115px;" id="logoImg" paramurl="${proModel.logo.url }" class="backimg" src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}">
                            </div>
                            <c:if test="${empty proModel.procInsId}">
                            <div style="width:115px;height:115px;position: relative;top:-115px;" class="shade-img">
                                <span></span>
                            </div>
                            <div style="width:115px;height:115px;position:relative;top:-224px;" class="shade-word" id="uploadlogo">更换项目logo</div>
	                            <div style="position:relative;top:-353px;left:107px;" class="delete-image" onclick="delLogo()">
	                                <img src="${ctxImages}/remove-accessory.png">
	                            </div>
                            </c:if>
                            <%--<div class="loadding"><img src="${ctxImages}/loading.gif"></div>--%>
                        </div>

                        <div class="help-inline" style="position:relative;left:130px;top:-53px;">
                            <span>建议背景图片大小：270 × 200（像素）</span>
                        </div>

                    </div>
                </div>
            </div>
            <input id="logoSysAttId" type="hidden" value="${proModel.logo.id}">
            <sys:frontTestCut width="200" height="200" btnid="uploadlogo" imgId="logoImg" column="logoUrl" filepath="projectlogo"
                      className="modal-avatar" toTemp="true"></sys:frontTestCut>
	        </form:form>
	        <div class="form-actions-cyjd text-center">
                <a id="prevbtn" class="btn btn-primary" href="javascript:void(0)" onclick="prevStep()" style="margin-right: 10px;">上一步</a>
                <a id="savebtn" class="btn btn-primary" href="javascript:void(0)" onclick="saveStep2UnCheck()" style="margin-right: 10px;">保存</a>
	            <button id="nextbtn" type="button" class="btn btn-primary btn-save" onclick="saveStep2(this)">下一步</button>
	        </div>
	        
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">
var validate1=$("#form1").validate({
    errorPlacement: function (error, element) {
          error.insertAfter(element);
    }
});
$(function(){
    $('.img-see').click(function(){
        $('.img-up').css('display','block');
    });

    $('.img-dele').click(function(){
        $(this).parent().parent().parent().css('display','none');
    });

});
function findTeamPerson() {
	var teamId = $("#teamId").val();
	if (!teamId) {
		$(".studenttb thead").nextAll().remove();
		$(".teachertb thead").nextAll().remove();
		return false;
	}

	var type = $("#proCategory").val();
	if (type == "") {
		dialogCyjd.createDialog(0, "请先选择项目类别", {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: '确定',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        });
		$("#teamId").val("");
		$(".studenttb thead").nextAll().remove();
		$(".teachertb thead").nextAll().remove();
		return false;
	}

	$.ajax({
				type : "GET",
				url : "/f/project/projectDeclare/findTeamPerson",
				data : "id=" + teamId,
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
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
										+"<td>"+scount+"<input type='hidden' name='studentList["+(scount-1)+"].userId' value='"+ v.userId +"' /></td>"
										+"<td>"+(v.name||"")+"</td>"
										+"<td>"+(v.no||"")+"</td>"
										+"<td>"+(v.org_name||"")+"</td>"
										+"<td>"+(v.professional||"")+"</td>"
										+"<td>"+(v.mobile||"")+"</td>"
										+"<td>"+(v.currState||"")+"</td>"
										+"<td>"+(v.curJoin||"")+"</td>"
										+"<td>"+(v.domain||"")+"</td>"
							

									   +"</tr>";
								scount++;
							}
							if(v.user_type=='2'){
								thtml=thtml+"<tr>"
								+"<td>"+tcount+"<input type='hidden' name='teacherList["+(tcount-1)+"].userId' value='"+ v.userId +"' /></td>"
																+"							<td>"+(v.name||"")+"</td>"
																+"							<td>"+(v.no||"")+"</td>"
																+"							<td>"+(v.org_name||"")+"</td>"
																+"							<td>"+(v.teacherType||"")+"</td>"
																+"							<td>"+(v.curJoin||"")+"</td>"
																+"							<td>"+(v.domain||"")+"</td>"
																+"						</tr>";
								tcount++;
							}
						});
						$(".studenttb").append(shtml);
						$(".teachertb").append(thtml);
						snumber = scount-1 ;
						checkProjectTeam();
					}
	            },
				error : function(msg) {
					dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
		                dialogClass: 'dialog-cyjd-container none-close',
		                buttons: [{
		                    text: '确定',
		                    'class': 'btn btn-sm btn-primary',
		                    click: function () {
		                        $(this).dialog('close');
		                        top.location = top.location;
		                    }
		                }]
		            });
				}
			});
}
function checkProjectTeam(){
	if(!$("[id='teamId']").val()){
		return;
	}
	$.ajax({
		type:'post',
		url:'/f/project/projectDeclare/checkProjectTeam',
		data: {proid:$("[id='id']").val(),
			actywId:$("[id='actYwId']").val(),
			lowType:$("[id='proCategory']").val(),
			teamid:$("[id='teamId']").val()},
		success:function(data){
			if(checkIsToLogin(data)){
				dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
	                dialogClass: 'dialog-cyjd-container none-close',
	                buttons: [{
	                    text: '确定',
	                    'class': 'btn btn-sm btn-primary',
	                    click: function () {
	                        $(this).dialog('close');
	                        top.location = top.location;
	                    }
	                }]
	            });
			}else{
				if(data.ret=='0'){
					dialogCyjd.createDialog(0, data.msg, {
		                dialogClass: 'dialog-cyjd-container none-close',
		                buttons: [{
		                    text: '确定',
		                    'class': 'btn btn-sm btn-primary',
		                    click: function () {
		                        $(this).dialog('close');
		                    }
		                }]
		            });
				}
			}
		}
	});
}
function delLogo() {
	var url=$("#logoImg").attr("paramurl");
	if(url==null||url==""){
		return;
	}
	dialogCyjd.createDialog(0, "确定删除?", {
        dialogClass: 'dialog-cyjd-container none-close',
        buttons: [{
            text: '确定',
            'class': 'btn btn-sm btn-primary',
            click: function () {

    			$(this).dialog("close");
    			//向服务器发出请求，删除文件
    			var url = $("#logoImg").attr("paramurl");
    			var id = $("#logoSysAttId").val();
    			if (!id) {
    				id = "";
    			}
    			$.ajax({
    				type : 'post',
    				url : $frontOrAdmin+'/ftp/ueditorUpload/delFile',
    				data : {
    					url : url,
    					id : id
    				},
    				success : function(data) {
    					$("#logoImg").attr("paramurl","");
    					$("#logoSysAttId").val("");
    					$("#logoImg").attr("src","/images/upload-default-image1.png");
    				}
    			});
            }
        },{
            text: '取消',
            'class': 'btn btn-sm btn-primary',
            click: function () {
                $(this).dialog('close');
            }
        }]
    });
}
function saveStep2(obj){
	if(validate1.form()){
		var onclickFn=$(obj).attr("onclick");
		$(obj).removeAttr("onclick");
		$("#form1").attr("action","/f/proprojectgt/saveStep2");
		$(obj).prop('disabled', true);
		$("#form1").ajaxSubmit(function (data) {
			if(checkIsToLogin(data)){
				dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
	                dialogClass: 'dialog-cyjd-container none-close',
	                buttons: [{
	                    text: '确定',
	                    'class': 'btn btn-sm btn-primary',
	                    click: function () {
	                        $(this).dialog('close');
	                        top.location = top.location;
	                    }
	                }]
	            });
			}else{
				if(data.ret==1){
					top.location="${ctxFront}/proprojectgt/applyStep3?id="+data.id;
				}else{
					$(obj).attr("onclick",onclickFn);
					dialogCyjd.createDialog(0, data.msg, {
		                dialogClass: 'dialog-cyjd-container none-close',
		                buttons: [{
		                    text: '确定',
		                    'class': 'btn btn-sm btn-primary',
		                    click: function () {
		                        $(this).dialog('close');
		                    }
		                }]
		            });
				}
			}
			$(obj).prop('disabled', false);
		});
	}
}
function prevStep(){
	if(checkIsFill()){
		saveStep2UnCheck("1");
	}else{
		var id=$("#id").val();
		toPrevStep(id);
	}
}
function toPrevStep(id){
	top.location="${ctxFront}/proprojectgt/applyStep1?actywId="+$("#actYwId").val()+(id==""?"":"&id="+id);
}
function saveStep2UnCheck(type){
	var preFn=$("#prevbtn").attr("onclick");
	$("#prevbtn").removeAttr("onclick");
	var saveFn=$("#savebtn").attr("onclick");
	$("#savebtn").removeAttr("onclick");
	var nextFn=$("#nextbtn").attr("onclick");
	$("#nextbtn").removeAttr("onclick");
	$("#form1").attr("action","/f/proprojectgt/saveStep2Uncheck");
	$("#form1").ajaxSubmit(function (data) {
		if(checkIsToLogin(data)){
			dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        $(this).dialog('close');
                        top.location = top.location;
                    }
                }]
            });
		}else{
			$("#prevbtn").attr("onclick",preFn);
			$("#savebtn").attr("onclick",saveFn);
			$("#nextbtn").attr("onclick",nextFn);
			if(data.ret==1){
				if($("#id").val()==""){
					$("#id").val(data.id);
				}
				$(".fill").each(function(i,v){
					$(v).attr("oldv",$(v).val());
				});
				if(typeof($("[name='logoUrl']").val())!="undefined"&&$("[name='logoUrl']").val()!=""){
					$("[name='logoUrl']").val("");
				}
				if(type=="1"){
					toPrevStep(data.id);
				}else{
					dialogCyjd.createDialog(1, data.msg, {
		                dialogClass: 'dialog-cyjd-container none-close',
		                buttons: [{
		                    text: '确定',
		                    'class': 'btn btn-sm btn-primary',
		                    click: function () {
		                        $(this).dialog('close');
		                    }
		                }]
		            });
				}
				
			}else{
				dialogCyjd.createDialog(0, data.msg, {
	                dialogClass: 'dialog-cyjd-container none-close',
	                buttons: [{
	                    text: '确定',
	                    'class': 'btn btn-sm btn-primary',
	                    click: function () {
	                        $(this).dialog('close');
	                    }
	                }]
	            });
			}
		}
	});
}
function checkIsFill(){
	var tag=false;
	$(".fill").each(function(i,v){
		if($(v).val()!=""&&$(v).attr("oldv")!=$(v).val()){
			tag=true;
			return false;
		}
	});
	if(typeof($("[name='logoUrl']").val())!="undefined"&&$("[name='logoUrl']").val()!=""){
		tag= true;
	}
	return tag;
}
</script>
</body>
</html>