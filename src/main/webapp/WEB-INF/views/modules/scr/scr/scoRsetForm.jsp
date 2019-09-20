<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>设置学分规则</title>
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
            <span>设置学分规则</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="scoRset" action="${ctx}/scr/scoRset/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	系统自动保留小数点 1：是自动，0不是自动：</label>
				<div class="controls">
					<form:input path="isKeepNpoint" htmlEscape="false" maxlength="2" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	小数点位数：</label>
				<div class="controls">
					<form:input path="keepNpoint" htmlEscape="false" maxlength="64" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否最后一位四舍五入 1：是自动，0不是自动：</label>
				<div class="controls">
					<form:select path="isRprd" class=" ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否低于X分（0否1是）：</label>
				<div class="controls">
					<form:select path="isSnum" class=" ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	低于x分：</label>
				<div class="controls">
					<form:input path="snumMin" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	按照x分：</label>
				<div class="controls">
					<form:input path="snumVal" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否认定总分不超过X分（0否1是）：</label>
				<div class="controls">
					<form:select path="isSnumlimit" class=" ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	认定总分：</label>
				<div class="controls">
					<form:input path="snumlimit" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	备注：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="scr:scoRset:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>