<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>学分申请记录</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
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
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>学分申请记录</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="scoRapplyRecord" action="${ctx}/scr/scoRapplyRecord/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>申请ID：</label>
				<div class="controls">
					<form:input path="appId" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>申请人ID：</label>
				<div class="controls">
					<form:input path="uid" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>状态:0、待审核 1、通过;2、未通过：</label>
				<div class="controls">
					<form:input path="status" htmlEscape="false" maxlength="1" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	备注信息：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="scr:scoRapplyRecord:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>