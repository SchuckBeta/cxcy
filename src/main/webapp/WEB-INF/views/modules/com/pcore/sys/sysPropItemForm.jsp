<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统功能配置项管理</title>
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
		<span>系统功能配置项</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysPropItem/">系统功能配置项列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysPropItem/form?id=${sysPropItem.id}">系统功能配置项<shiro:hasPermission name="sys:sysPropItem:edit">${not empty sysPropItem.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysPropItem:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysPropItem" action="${ctx}/sys/sysPropItem/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<form:hidden path="isOpen"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">系统配置：</label>
				<div class="controls">
					<form:select id="propId" path="prop.id" class="required">
                        <form:option value="" label="--请选择--"/>
                        <c:forEach var="curProp" items="${sysPropTypes}">
                            <c:if test="${sysProp.type eq curProp.key}">
                                <option value="${curProp.key}" selected="selected">${curProp.name }</option>
                            </c:if>
                            <c:if test="${sysProp.type ne curProp.key}">
                                <option value="${curProp.key}">${curProp.name }</option>
                            </c:if>
                        </c:forEach>
                    </form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">标题：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">参数JSON数组：</label>
				<div class="controls">
					<form:textarea path="params" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">参数JSON默认值：</label>
				<div class="controls">
					<form:textarea path="pfomat" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">介绍信息：</label>
				<div class="controls">
					<form:textarea path="instruction" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysPropItem:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>