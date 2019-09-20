<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>团队人员变更表</title>
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
            <span>团队人员变更表</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="teamUserChange" action="${ctx}/team/teamUserChange/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	用户ID：</label>
				<div class="controls">
					<sys:treeselect id="userId" name="userId" value="${teamUserChange.userId}" labelName="" labelValue="${teamUserChange.}"
						title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	操作1：添加人员 2：删除人员：</label>
				<div class="controls">
					<form:input path="operType" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	团队ID：</label>
				<div class="controls">
					<form:input path="teamId" htmlEscape="false" maxlength="64" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	操作人id：</label>
				<div class="controls">
					<form:input path="operUserId" htmlEscape="false" maxlength="64" class=" "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="team:teamUserChange:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>