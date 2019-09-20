<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${frontTitle}</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet"/>
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
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
	<%--<ul class="nav nav-tabs">
		<li><a href="${ctx}/team/">团队信息列表</a></li>
		<li class="active"><a href="${ctx}/team/form?id=${team.id}">组建团队</a></li>
	</ul><br/>--%>
	<form:form id="inputForm" modelAttribute="team" action="${ctx}/team/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		

		<div class="control-group">
			<label class="control-label">项目名称：</label>
			<div class="controls">
				<form:input path="projectName" htmlEscape="false" maxlength="200" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目简介：</label>
			<div class="controls">
				<form:input path="projectIntroduction" htmlEscape="false" maxlength="1024" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">成员人数：</label>
			<div class="controls">
				<form:input path="memberNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">导师人数：</label>
			<div class="controls">
				<form:input path="masterNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">选择导师:</label>
			<div class="controls">
				<form:checkboxes path="masterList" items="${masterList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				<span class="help-inline"></span>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">选择学生:</label>
			<div class="controls">
				<form:checkboxes path="studentList" items="${studentList}" itemLabel="name" itemValue="id" htmlEscape="false" />
				<span class="help-inline"></span>
			</div>
		</div>





		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="发布团队"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>