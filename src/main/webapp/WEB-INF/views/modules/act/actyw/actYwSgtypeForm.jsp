<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>状态条件管理</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				}
			});
		});
	</script>
</head>
<body>
<div class="container-fluid">
	<%--<div class="edit-bar clearfix">--%>
		<%--<div class="edit-bar-left">--%>
			<%--<span>状态条件</span>--%>
			<%--<i class="line weight-line"></i>--%>
		<%--</div>--%>
	<%--</div>--%>
	<%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
	<form:form id="inputForm" modelAttribute="actYwSgtype" action="${ctx}/actyw/actYwSgtype/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<input type="hidden" id="secondName" name="secondName"value="${secondName}"/>
		<div class="control-group">
			<label class="control-label">名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="64"/>
			</div>
		</div>

		<div class="form-actions">
			<button type="submit" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
		</div>
	</form:form>
</div>
</body>
</html>