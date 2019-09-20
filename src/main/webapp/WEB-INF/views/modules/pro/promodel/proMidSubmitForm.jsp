<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>proMidSubmit管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
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
	<div class="mybreadcrumbs">
		<span>proMidSubmit</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/promodel/proMidSubmit/">proMidSubmit列表</a></li>
			<li class="active"><a href="${ctx}/promodel/proMidSubmit/form?id=${proMidSubmit.id}">proMidSubmit<shiro:hasPermission name="promodel:proMidSubmit:edit">${not empty proMidSubmit.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="promodel:proMidSubmit:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="proMidSubmit" action="${ctx}/promodel/proMidSubmit/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">自定义表id：</label>
				<div class="controls">
					<form:input path="promodelId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">gnode表id：</label>
				<div class="controls">
					<form:input path="gnodeId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">保留字段：</label>
				<div class="controls">
					<form:input path="state" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="promodel:proMidSubmit:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>