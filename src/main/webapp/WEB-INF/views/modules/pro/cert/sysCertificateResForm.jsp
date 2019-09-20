<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书资源管理</title>
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
		<span>系统证书资源</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificateRes/">系统证书资源列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificateRes/form?id=${sysCertificateRes.id}">系统证书资源<shiro:hasPermission name="sys:sysCertificateRes:edit">${not empty sysCertificateRes.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysCertificateRes:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysCertificateRes" action="${ctx}/sys/sysCertificateRes/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">资源类型：</label>
				<div class="controls">
					<form:select path="type" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${wresFtype}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">资源名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">宽度：</label>
				<div class="controls">
					<form:input path="width" htmlEscape="false" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">高度：</label>
				<div class="controls">
					<form:input path="height" htmlEscape="false" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">X坐标：</label>
				<div class="controls">
					<form:input path="xlt" htmlEscape="false" maxlength="11" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Y坐标：</label>
				<div class="controls">
					<form:input path="ylt" htmlEscape="false" maxlength="11" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">透明度：</label>
				<div class="controls">
					<form:input path="opacity" htmlEscape="false" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">角度：</label>
				<div class="controls">
					<form:input path="rate" htmlEscape="false" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否平铺：</label>
				<div class="controls">
					<form:select path="hasLoop" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否显示：</label>
				<div class="controls">
					<form:select path="isShow" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysCertificateRes:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>