<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>学分规则</title>
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
            <span>学分规则</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="scoRule" action="${ctx}/scr/scoRule/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>父级编号：</label>
				<div class="controls">

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>所有父级编号：</label>
				<div class="controls">
					<form:input path="parentIds" htmlEscape="false" maxlength="2000" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="100" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	级别：1：类型 ;2：类别（级别）;3、子类别（子级别）：</label>
				<div class="controls">
					<form:input path="type" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	认定形式：1：个人 2：团队：</label>
				<div class="controls">
					<form:input path="ptype" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>是否设置配比：0：否 1：是：</label>
				<div class="controls">
					<form:input path="isPb" htmlEscape="false" maxlength="1" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>是否取最高级别(标准是否互斥，需要排除参与类型）：0：否 1：是：</label>
				<div class="controls">
					<form:input path="isMaxLevel" htmlEscape="false" maxlength="1" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否显示：0、不显示；1、显示：</label>
				<div class="controls">
					<form:input path="isShow" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	备注：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="scr:scoRule:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>