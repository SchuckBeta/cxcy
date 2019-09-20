<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
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
		<span>耗材购买记录</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/pw/pwCosMaterialPrecords/">耗材购买记录列表</a></li>
			<li class="active"><a href="${ctx}/pw/pwCosMaterialPrecords/form?id=${pwCosMaterialPrecords.id}">耗材购买记录<shiro:hasPermission name="pw:pwCosMaterialPrecords:edit">${not empty pwCosMaterialPrecords.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="pw:pwCosMaterialPrecords:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="pwCosMaterialPrecords" action="${ctx}/pw/pwCosMaterialPrecords/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">耗材编号：</label>
				<div class="controls">
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">购买人姓名：</label>
				<div class="controls">
					<form:input path="prname" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">电话：</label>
				<div class="controls">
					<form:input path="phone" htmlEscape="false" maxlength="200" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">手机：</label>
				<div class="controls">
					<form:input path="mobile" htmlEscape="false" maxlength="200" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">购买日期：</label>
				<div class="controls">
					<input name="time" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${pwCosMaterialPrecords.time}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">购买数量：</label>
				<div class="controls">
					<form:input path="num" htmlEscape="false" maxlength="11" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">单价：</label>
				<div class="controls">
					<form:input path="price" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">总价：</label>
				<div class="controls">
					<form:input path="totalPrice" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">备注：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="pw:pwCosMaterialPrecords:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>