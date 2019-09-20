<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统全局编号管理</title>
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
		<span>系统全局编号</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysNo/">系统全局编号列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysNo/form?id=${sysNo.id}">系统全局编号<shiro:hasPermission name="sys:sysNo:edit">${not empty sysNo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysNo:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysNo" action="${ctx}/sys/sysNo/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">编号惟一标识：</label>
				<div class="controls">
					<form:input path="keyss" htmlEscape="false" maxlength="11" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">处理类：</label>
				<div class="controls">
					<form:input path="clazz" htmlEscape="false" maxlength="11" class="input-xlarge"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">前缀：</label>
				<div class="controls">
					<form:input path="prefix" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">全局编号最大值：</label>
				<div class="controls">
					${sysNo.sysmaxVal }
					<form:hidden path="sysmaxVal" ></form:hidden>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">后缀：</label>
				<div class="controls">
					<form:input path="postfix" htmlEscape="false" maxlength="64" class="input-xlarge"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">格式：</label>
				<div class="controls">
					<c:if test="${not empty sysNo.id}">
						<input name="format"  class="input-xlarge" value="${sysNo.format}"/>
					</c:if>
					<c:if test="${empty sysNo.id}">
						<input name="format"  class="input-xlarge" value="yyyyMMddHHmmss{{0}}"/>
					</c:if>
					<span class="help-inline">默认：前缀+yyyyMMddHHmmss+{{0}}+后缀</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">编号最大值位数：</label>
				<div class="controls">
					<c:if test="${not empty sysNo.id}">
						<input name="maxByte"  class="input-xlarge" value="${sysNo.maxByte}"/>
					</c:if>
					<c:if test="${empty sysNo.id}">
						<input name="maxByte"  class="input-xlarge" value="5"/>
					</c:if>
					<span class="help-inline">默认：最大值补全为0后的位数，默认为5。注意：当format为空时，建议设置稍大一点，避免溢出</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">编号功能说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysNo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>