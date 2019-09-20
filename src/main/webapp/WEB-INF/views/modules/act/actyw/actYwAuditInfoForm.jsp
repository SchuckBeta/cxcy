<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>自定义审核信息管理</title>
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
		<span>自定义审核信息</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/promodel/actYwAuditInfo/">自定义审核信息列表</a></li>
			<li class="active"><a href="${ctx}/promodel/actYwAuditInfo/form?id=${actYwAuditInfo.id}">自定义审核信息<shiro:hasPermission name="promodel:actYwAuditInfo:edit">${not empty actYwAuditInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="promodel:actYwAuditInfo:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="actYwAuditInfo" action="${ctx}/promodel/actYwAuditInfo/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">自定义大赛或项目id：</label>
				<div class="controls">
					<form:input path="promodelId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">审核人id：</label>
				<div class="controls">
					<form:input path="auditId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">审核级别：1代表院级 2代表校级：</label>
				<div class="controls">
					<form:input path="auditLevel" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">评审名称，如评分、答辩等：</label>
				<div class="controls">
					<form:input path="auditName" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">审核节点id：</label>
				<div class="controls">
					<form:input path="gnodeId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">意见：</label>
				<div class="controls">
					<form:input path="suggest" htmlEscape="false" maxlength="512" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">分数：</label>
				<div class="controls">
					<form:input path="score" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">审核结果 0：不合格1：合格：</label>
				<div class="controls">
					<form:input path="grade" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">流程实例id：</label>
				<div class="controls">
					<form:input path="procInsId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="promodel:actYwAuditInfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>