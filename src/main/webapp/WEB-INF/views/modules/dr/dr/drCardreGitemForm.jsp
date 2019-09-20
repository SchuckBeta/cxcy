<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>卡记录规则明细</title>
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
            <span>卡记录规则明细</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="drCardreGitem" action="${ctx}/dr/drCardreGitem/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>预警规则：</label>
				<div class="controls">
					<%-- <form:checkboxes path="group.id" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/> --%>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>预警场地ID：</label>
				<div class="controls">
					<%-- <form:checkboxes path="erspace.id" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/> --%>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="dr:drCardreGitem:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>