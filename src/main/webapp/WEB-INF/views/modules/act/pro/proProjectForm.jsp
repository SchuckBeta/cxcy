<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>创建项目管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css">
	<style type="text/css">
		.controls select{
			height: 30px;
		}
	ul li {
		list-style-type: none;
	}

	.form-actions {
		border-top: none !important;
	}
	.icon-remove-sign{
		color:red;!important;
	}

	#xm {
		width: 284px;
	}

	.Wdate {
		width: 270px
	}

	#upload {
		margin-left: 20px
	}

	#cke_content {
		width: 69% !important;
	}

	.control-group {
		border-bottom: none !important;
	}

	.tt {
		width: 284px !important;
		max-width: none !important;
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

	<script type="text/javascript">
		$(document).ready(function() {
			var pass = false;
			$("#projectName").blur(function(){
				var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
				var username=$("#projectName").val();
				var reg = /\s/;
				if(username==" ") {
					$("#yazheng").text("*大赛名不能为空!").css('color','red');
					pass = false;
				}
				else if( /^\d.*$/.test( username ) ){
					$("#yazheng").text("*大赛名不能以数字开头!").css('color','red');
					pass = false;
				}
				else if(username.length<1 || username.length>18 ){
					$("#yazheng").text("*合法长度为1-18个字符!").css('color','red');
					pass = false;
				}else{
					$.ajax({
						url:"/a/proproject/proProject/validateName",
						data:{'name':username},
						type:"post",
						success:function(data){
							if(data==1){
								$("#yazheng").text("*大赛名已存在!").css('color','red');
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
					loading('正在提交，请稍等...');
					form.submit();
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
</head>
<body>
	<div class="mybreadcrumbs">
		<span>创建项目</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/proproject/proProject/">创建项目列表</a></li>
			<li class="active"><a href="${ctx}/proproject/proProject/form?id=${proProject.id}">创建项目<shiro:hasPermission name="proproject:proProject:edit">${not empty proProject.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="proproject:proProject:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="proProject" action="${ctx}/proproject/proProject/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>

			<div class="control-group">
				<label class="control-label"><span class="help-inline">
					<font color="red">*&nbsp;</font> </span>项目名称:</label>
				<div class="controls">
					<form:input path="projectName" htmlEscape="false" maxlength="64" class="input-xlarge "/>
					<span id="yazheng"></span>
				</div>
			</div>
		<div class="control-group">
			<label class="control-label"><span class="help-inline">
				<font color="red">*&nbsp;</font> </span>项目标识:</label>
			<div class="controls">
				<form:input path="projectMark" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				<span id="yazheng"></span>
			</div>
		</div>
			<div class="control-group">
				<label class="control-label">内容：</label>
				<div class="controls">
					<form:textarea path="content" htmlEscape="false" rows="4" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">发布状态：</label>
				<div class="controls">
					<form:select path="state" class="input-medium" maxlength="2" >
						<form:option value="" label="请选择" />
						<form:option value="1" label="发布" />
						<form:option value="0" label="不发布" />
					</form:select>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="proproject:proProject:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>