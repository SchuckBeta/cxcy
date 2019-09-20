<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统功能管理</title>
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
		<span>系统功能</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysProp/">系统功能列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysProp/form?id=${sysProp.id}">系统功能<shiro:hasPermission name="sys:sysProp:edit">${not empty sysProp.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysProp:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysProp" action="${ctx}/sys/sysProp/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">配置类型：</label>
				<div class="controls">
					<form:select id="type" path="type" class="required">
                        <form:option value="" label="--请选择--"/>
                        <c:forEach var="curPtype" items="${sysPropTypes}">
                            <c:if test="${curPtype.key ne '0'}">
                                <c:if test="${sysProp.type eq curPtype.key}">
                                    <option value="${curPtype.key}" selected="selected">${curPtype.name }</option>
                                </c:if>
                                <c:if test="${sysProp.type ne curPtype.key}">
                                    <option value="${curPtype.key}">${curPtype.name }</option>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">标题：</label>
				<div class="controls">
					<form:input path="title" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">面包屑：</label>
				<div class="controls">
					<form:input path="micaPanic" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">开关：</label>
				<div class="controls">
					<form:select path="onOff" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysProp:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>