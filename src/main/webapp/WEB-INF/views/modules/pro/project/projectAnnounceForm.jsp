<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目通告管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<script type="text/javascript" src="${ctxJs}/common.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css">
	<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<style type="text/css">
		ul li{
			list-style-type:none;
		}
		.form-actions{
		border-top:none;}

	</style>

	<script type="text/javascript">
		$(document).ready(function() {
			var operationType = $("#operationType").val();
			if(operationType!=null && operationType!=""){
				$("input").attr({readonly:"true"});
				$("#btnSubmit").hide();
				$(".content_panel span").hide();
				return false;
			}
			var pass = true;
			$("#projectName").blur(function(){
				var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
				var username=$("#projectName").val();
				var reg = /\s/;
				if(username==" ") {
					$("#yazheng").text("*项目名不能为空!").css('color','red');
					pass = false;
				}
				else if( /^\d.*$/.test( username ) ){
					$("#yazheng").text("*项目名不能以数字开头!").css('color','red');
					pass = false;
				}
				else if(username.length<1 || username.length>18 ){
					$("#yazheng").text("*合法长度为1-18个字符!").css('color','red');
					pass = false;
				}else{
					var proAnnId=$("#id").val();
					$.ajax({
						url:"/a/project/projectAnnounce/validateName",
						data:{'name':username,
								'id':proAnnId},
						type:"post",
						success:function(data){
							if(data==1){
								$("#yazheng").text("*项目名已存在!").css('color','red');
								pass = false;
							}else{
								$("#yazheng").text("");
								pass = true;
							}
						}
					});
				 }
			});
			$("#inputForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					if(pass){
						form.submit();
						$("#btnSubmit").attr("disabled",true);
					}else{
						alert("信息输入有误！");
						//showModalMessage(0, "信息输入有误！");
						//window.location.reload();
					}

				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
	<style>
	#xm{
	   width:284px;
	}
	.Wdate{
	  width:270px
	}
	#upload{
	margin-left:20px
	}
	#cke_content{
	width:69%!important;
	}
	.control-group{
	border-bottom:none!important;
	}
	.icon-remove-sign{
	color:red;!important;
}
	.tt{
	  width:284px!important;
	  max-width:none!important;
	  height:30px;
	}
	.biaoti{
	position:relative;
	height:41px;
}

.biaoti .file-title{
	position:absolute;
	left:0;
	top:0px;
	z-index:999;
	background:#fff;
}
.biaoti .yw-line{
	position:absolute;
	height:1px;
	background:#f3d5af;
	left:0px;
	top:10px;
	width:80%;

}
#btnSubmit{
  background:#e9442d!important;
}
	</style>
</head>
<body>
<div class="mybreadcrumbs"><span>项目通告管理</span></div>
<div class="content_panel">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/project/projectAnnounce/">项目列表</a></li>
		<li class="active">
			<c:choose>
				<c:when test="${operationType!=null}">
				<a href="${ctx}/project/projectAnnounce/form?id=${projectAnnounce.id}&operationType=1">查看项目
				</a>
				</c:when>
				<c:otherwise>
					<a href="${ctx}/project/projectAnnounce/form?id=${projectAnnounce.id}">
					<shiro:hasPermission name="gcontest:gContestAnnounce:edit">
					${not empty projectAnnounce.id?'修改':'创建'}
					</shiro:hasPermission>
						项目
					</a>
				</c:otherwise>
			</c:choose>

			<%--<a href="${ctx}/project/projectAnnounce/form?id=${projectAnnounce.id}">
			项目
			<shiro:hasPermission name="project:projectAnnounce:edit">
			${not empty projectAnnounce.id?'修改':'添加'}</shiro:hasPermission>
			<shiro:lacksPermission name="project:projectAnnounce:edit">查看</shiro:lacksPermission>
			</a>--%>
		</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="projectAnnounce" action="${ctx}/project/projectAnnounce/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input type="hidden" id="operationType" value="${operationType}"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>项目名称:</label>
			<div class="controls">
				<form:input path="name" id="projectName" htmlEscape="false" maxlength="64" class="input-xlarge required" />
				<span id="yazheng"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>项目类型:</label>
			<div class="controls">
				<form:select path="proType" class="tt required">
					<form:option value="" label="请选择" />
					<form:options items="${fns:getDictList('project_style')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>项目申报有效期:</label>
			<div class="controls">
				<input name="beginDate" id="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.beginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span>至</span>
				<input name="endDate" id="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'#F{$dp.$D(\'pIniStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>立项审核有效期:</label>
			<div class="controls">
				<input name="pIniStart" id="pIniStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.pIniStart}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'endDate\')}',maxDate:'#F{$dp.$D(\'pIniEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span>至</span>
				<input name="pIniEnd" id="pIniEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.pIniEnd}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'pIniStart\')}',maxDate:'#F{$dp.$D(\'midStartDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>中期检查时间:</label>
			<div class="controls">
				<input name="midStartDate" id="midStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.midStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'pIniEnd\')}',maxDate:'#F{$dp.$D(\'midEndDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span>至</span>
				<input name="midEndDate" id="midEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.midEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'midStartDate\')}',maxDate:'#F{$dp.$D(\'finalStartDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label"><span class="help-inline"><font
					color="red">*&nbsp;</font> </span>结项审核时间:</label>
			<div class="controls">
				<input name="finalStartDate" id="finalStartDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.finalStartDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'midEndDate\')}',maxDate:'#F{$dp.$D(\'finalEndDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				<span>至</span>
				<input name="finalEndDate" id="finalEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${projectAnnounce.finalEndDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({minDate:'#F{$dp.$D(\'finalStartDate\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目说明:</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" class="input-xxlarge "/>
				<sys:ckeditor replace="content" uploadPath="/project/content" />
			</div>
		</div>
		<div class="other" id="fujian" style="margin-left: 11%;margin-top:40px">
			<div class="biaoti" >
				<span class="char2 file-title" style="width: 70px;">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件</span>
				<p class="yw-line"></p>
				<c:if test="${operationType==null}">
				<div style="width: 102px; height: 30px; border-radius: 2px; position:absolute;right:165px;bottom:37px; background-color: #e9442d; float: right ;">
					<span style="font-size:12px;color:#cecece;position: absolute;right: 100px;width: 160px;top: 12px;">(注：文件大小不超过100m)</span><a class="upload" id="upload" style="color: #fff; line-height: 30px">上传附件</a>
				</div>
				<input type="file"  style="display: none" id="fileToUpload" name="fileName"/>
				</c:if>
			</div>
			<ul id="file">
			<c:forEach items="${sysAttachments}" var="sysAttachment">
			<ul>
				<li>
					<p><img src="${ctxImg}/filetype/${sysAttachment.suffix }.png">
						<a  href="javascript:void(0)"
						   onclick="downfile('${sysAttachment.url}','${sysAttachment.name}');return false">
					${sysAttachment.name}</a>
					<span class="del" onclick="delBackFile(this,'${item.id}','${item.arrUrl}');">
								<i class='icon-remove-sign'></i>
					</span>

					<input name="url" value="${sysAttachment.url}" id="url" type="hidden"/>
					</p>
				</li>
			</ul>
			</c:forEach>
			</ul>
		</div>

		<div class="form-actions" style="margin-left:20%;margin-top:30px;">
			<shiro:hasPermission name="project:projectAnnounce:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
	<!-- 引用ajaxfileupload.js -->
	<script src="${ctxCommon}/common-js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${ctxJs}/gcontest/projectAnnouce.js"></script>
	<script src="${ctxJs}/fileUpLoad.js" type="text/javascript"></script>
	<script src="${ctxJs}/common.js" type="text/javascript"></script>
	<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>

</body>
</html>