<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书资源关联管理</title>
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
		<span>系统证书资源关联</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificateRel/">系统证书资源关联列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificateRel/form?id=${sysCertificateRel.id}">系统证书资源关联<shiro:hasPermission name="sys:sysCertificateRel:edit">${not empty sysCertificateRel.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysCertificateRel:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysCertificateRel" action="${ctx}/sys/sysCertificateRel/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">证书：</label>
				<div class="controls">
					<sys:treeselect id="sysCert" name="sysCert.id" value="${sysCertificateRel.sysCert.id}" labelName="sysCert.name" labelValue="${sysCertificateRel.sysCert.name}"
						title="证书" url="/sys/sysCertificate/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">证书资源：</label>
				<div class="controls">
					<sys:treeselect id="sysCertRes" name="sysCertRes.id" value="${sysCertificateRel.sysCertRes.id}" labelName="sysCertRes.name" labelValue="${sysCertificateRel.sysCertRes.name}"
						title="证书资源" url="/sys/sysCertificateRes/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">类型：</label>
				<div class="controls">
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wrelType}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">资源类型:</label>
				<div class="controls">
					<form:select path="resType" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wresType}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">资源类（res_type=5是使用）：</label>
				<div class="controls">
					<form:input path="resClazz" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">资源类属性（res_type=5是使用）：</label>
				<div class="controls">
					<form:input path="resClazzProp" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysCertificateRel:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>