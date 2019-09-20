<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书执行记录管理</title>
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
		<span>系统证书执行记录</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificateIssued/">系统证书执行记录列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificateIssued/form?id=${sysCertificateIssued.id}">系统证书执行记录<shiro:hasPermission name="sys:sysCertificateIssued:edit">${not empty sysCertificateIssued.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysCertificateIssued:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysCertificateIssued" action="${ctx}/sys/sysCertificateIssued/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">证书：</label>
				<div class="controls">
					<sys:treeselect id="sysCertRel" name="sysCertRel.id" value="${sysCertificateIssued.sysCertRel.id}" labelName="sysCertRel.sysCert.name" labelValue="${sysCertificateIssued.sysCertRel.sysCert.name}"
						title="证书" url="/sys/sysCertificate/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">业务：</label>
				<div class="controls">
					<sys:treeselect id="actYw" name="actYw.id" value="${sysCertificateIssued.actYw.id}" labelName="actYw.proProject.projectName" labelValue="${sysCertificateIssued.actYw.proProject.projectName}"
						title="项目流程" url="/actyw/actYw/tree" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">授予状态：</label>
				<div class="controls">
					<form:select path="type" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('sys_certificate_isstype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执行原因：</label>
				<div class="controls">
					<form:input path="reason" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执行时间：</label>
				<div class="controls">
					<input name="issuedDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${sysCertificateIssued.issuedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">执行人：</label>
				<div class="controls">
					<sys:treeselect id="user" name="issuedBy.id" value="${sysCertificateIssued.issuedBy.id}" labelName="issuedBy.name" labelValue="${sysCertificateIssued.issuedBy.name}"
					title="执行用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">被执行人：</label>
				<div class="controls">
					<sys:treeselect id="user2" name="acceptBy.id" value="${sysCertificateIssued.acceptBy.id}" labelName="acceptBy.name" labelValue="${sysCertificateIssued.issuedBy.name}"
					title="被执行用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysCertificateIssued:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>