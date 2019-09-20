<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>监听管理</title>
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
		<span>监听</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/actyw/actYwClazz/">监听列表</a></li>
			<li class="active"><a href="${ctx}/actyw/actYwClazz/form?id=${actYwClazz.id}">监听<shiro:hasPermission name="actyw:actYwClazz:edit">${not empty actYwClazz.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="actyw:actYwClazz:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="actYwClazz" action="${ctx}/actyw/actYwClazz/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">表单组：</label>
				<div class="controls">
					<form:select id="theme" path="theme" class="input-xlarge required">
						<form:option value="" label="--请选择--"/>
						<c:forEach var="curFormTheme" items="${formThemes}">
							<c:if test="${actYwClazz.theme eq curFormTheme.id}">
								<option value="${curFormTheme.id}" selected="selected">${curFormTheme.name }</option>
							</c:if>
							<c:if test="${actYwClazz.theme ne curFormTheme.id}">
								<option value="${curFormTheme.id}" >${curFormTheme.name }</option>
							</c:if>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">类型：</label>
				<div class="controls">
					<form:select id="type" path="type" class="input-xlarge required">
						<form:option value="" label="--请选择--"/>
						<c:forEach var="curListener" items="${ctlisteners}">
							<c:if test="${actYwClazz.type eq curListener.key}">
								<option value="${curListener.key}" selected="selected">${curListener.name }</option>
							</c:if>
							<c:if test="${actYwClazz.type ne curListener.key}">
								<option value="${curListener.key}" >${curListener.name }</option>
							</c:if>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">别名：</label>
				<div class="controls">
					<form:input path="alias" htmlEscape="false" maxlength="200" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="actyw:actYwClazz:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>