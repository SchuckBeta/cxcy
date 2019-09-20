<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>专家指派规则</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
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
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>专家指派规则</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="actYwEtAssignRule" action="${ctx}/actyw/actYwEtAssignRule/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>项目类别编号：</label>
				<div class="controls">
					<form:input path="actywId" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>审核节点编号：</label>
				<div class="controls">
					<form:input path="gnodeId" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>设定审核人数 -1为无限 默认为-1：</label>
				<div class="controls">
					<form:input path="auditUserNum" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>审核方式 0为自动 1为手动 默认为0：</label>
				<div class="controls">
					<form:input path="auditType" htmlEscape="false" maxlength="2" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>审核角色 0为自动 1为院级 默认为0：</label>
				<div class="controls">
					<form:input path="auditRole" htmlEscape="false" maxlength="2" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>设定每个项目最多审核人数 -1为无限 默认为-1：</label>
				<div class="controls">
					<form:input path="auditMax" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>是否为持续审核 0为否 1为是 默认为0：</label>
				<div class="controls">
					<form:input path="isAuto" htmlEscape="false" maxlength="2" class=" required"/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="actyw:actYwEtAssignRule:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>