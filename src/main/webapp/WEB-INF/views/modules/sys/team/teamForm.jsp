<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团队管理</title>
	<meta name="decorator" content="default"/>
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
	<ul class="nav nav-tabs">
		<li><a href="<% request.getContextPath();%>/f/team/">团队列表</a></li>
		<li class="active"><a href="<% request.getContextPath();%>/f/team/form?id=${team.id}">团队<shiro:hasPermission name="team:team:edit">${not empty team.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="team:team:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="team" action="${ctx}/team/team/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">团队名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
<%-- 		<div class="control-group">
			<label class="control-label">团队负责人：</label>
			<div class="controls">
				<form:input path="sponsor" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">团队状态(0.建设中,1建设完毕，2解散）：</label>
			<div class="controls">
				<form:select id="selstate" path="state" class="input-xlarge ">
					<form:option value="" label="--请选择--"/>
					<form:options items="${fns:getDictList('teamstate_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队信息简介：</label>
			<div class="controls">
				<form:textarea path="summary" htmlEscape="false" rows="4" maxlength="64" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目名称：</label>
			<div class="controls">
				<form:input path="projectName" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目简介：</label>
			<div class="controls">
				<form:textarea path="projectIntroduction" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业导师人数：</label>
			<div class="controls">
				<form:input path="enterpriseTeacherNum" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">校园导师人数：</label>
			<div class="controls">
				<form:input path="schoolTeacherNum" htmlEscape="false" maxlength="11" class="input-xlarge required digits"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">成员人数：</label>
			<div class="controls">
				<form:input path="memberNum" htmlEscape="false" maxlength="11" class="input-xlarge  digits"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">成员要求：</label>
			<div class="controls">
				<form:textarea path="membership" htmlEscape="false" rows="4" maxlength="64" class="input-xxlarge "/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">所属学院：</label>
			<div class="controls">
				<form:input path="localCollege" htmlEscape="false" maxlength="64" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">组员：</label>
			<div class="controls">
				<form:textarea path="memberNames" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">校园导师：</label>
			<div class="controls">
				<form:textarea path="schTeacherNames" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">企业导师：</label>
			<div class="controls">
				<form:textarea path="entTeacherNames" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="team:team:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	<script type="text/javascript">
	 if("${team.state}"!=null){
		  $("#selstate").val("${team.state}");
	 }
	 
	</script>
</body>
</html>