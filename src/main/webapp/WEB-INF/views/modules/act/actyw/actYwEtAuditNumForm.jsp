<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>指派专家组的项目</title>
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
            <span>指派专家组的项目</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="actYwEtAuditNum" action="${ctx}/actyw/actYwEtAuditNum/save" method="post" class="form-horizontal">
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
				<label class="control-label">	专家名字list 例如 1|3|5 编号为1,3,5的专家为一组：</label>
				<div class="controls">
					<form:input path="experts" htmlEscape="false" maxlength="1024" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	proId为指派的项目名称：</label>
				<div class="controls">
					<form:input path="proid" htmlEscape="false" maxlength="64" class=" "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="actyw:actYwEtAuditNum:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>