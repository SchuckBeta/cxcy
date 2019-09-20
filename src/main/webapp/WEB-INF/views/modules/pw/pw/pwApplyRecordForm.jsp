<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>pwApplyRecord</title>
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
            <span>pwApplyRecord</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="pwApplyRecord" action="${ctx}/pw/pwApplyRecord/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>申报单编号【pw_enter-&gt;id】：</label>
				<div class="controls">
					<form:input path="eid" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	申请类型:1、入驻团队申请;2、入驻企业申请; 	3、续期申请;4、退孵申请;【5~8 变更】5、场地变更  6、企业变更 7、项目变更 8、团队变更      9管理员审核 10:管理员变更：</label>
				<div class="controls">
					<form:input path="type" htmlEscape="false" maxlength="2" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	审核状态：0、待审核；1、审核通过；2、审核不通过：</label>
				<div class="controls">
					<form:input path="status" htmlEscape="false" maxlength="2" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	期限,单位：年：</label>
				<div class="controls">
					<form:input path="term" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	变更说明   废弃：</label>
				<div class="controls">
					<form:input path="bgremarks" htmlEscape="false" maxlength="500" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	审核意见：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="pw:pwApplyRecord:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>