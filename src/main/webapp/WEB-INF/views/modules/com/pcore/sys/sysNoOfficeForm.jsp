<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统机构编号管理</title>
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
		<span>系统机构编号</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysNoOffice/">系统机构编号列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysNoOffice/form?id=${sysNoOffice.id}">系统机构编号<shiro:hasPermission name="sys:sysNoOffice:edit">${not empty sysNoOffice.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysNoOffice:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysNoOffice" action="${ctx}/sys/sysNoOffice/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">机构ID：</label>
				<div class="controls">
					<form:input path="sysNo.id" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">机构ID：</label>
				<div class="controls">
					<sys:treeselect id="office" name="office.id" value="${sysNoOffice.office.id}" labelName="office.name" labelValue="${sysNoOffice.office.name}"
						title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">编号最大值：</label>
				<div class="controls">
					<form:input path="maxVal" htmlEscape="false" maxlength="11" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">create_by：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysNoOffice:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>