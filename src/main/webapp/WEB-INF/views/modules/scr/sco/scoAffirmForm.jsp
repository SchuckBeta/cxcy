<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>创新、创业、素质学分认定表管理</title>
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
		<span>创新、创业、素质学分认定表</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sco/scoAffirm/">创新、创业、素质学分认定表列表</a></li>
			<li class="active"><a href="${ctx}/sco/scoAffirm/form?id=${scoAffirm.id}">创新、创业、素质学分认定表<shiro:hasPermission name="sco:scoAffirm:edit">${not empty scoAffirm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sco:scoAffirm:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="scoAffirm" action="${ctx}/sco/scoAffirm/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">流程实例ID：</label>
				<div class="controls">
					<form:input path="procInsId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">学分类型：1、创新学分/2、创业学分/3、素质学分：</label>
				<div class="controls">
					<form:select path="type" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目/大赛ID：</label>
				<div class="controls">
					<form:input path="proId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目类型/大赛类型：具体值可查看文档：</label>
				<div class="controls">
					<form:input path="proType" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目级别/大赛级别：具体值可查看文档：</label>
				<div class="controls">
					<form:input path="proLevelType" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">只有项目有类别，大赛无类别：具体值可查看文档：</label>
				<div class="controls">
					<form:input path="proPtype" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目/大赛结果：具体值可查看文档：</label>
				<div class="controls">
					<form:input path="proResult" htmlEscape="false" maxlength="2" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">大赛有得分，项目无得分：</label>
				<div class="controls">
					<form:input path="proScore" htmlEscape="false" maxlength="2" class="input-xlarge  digits"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">认定学分：</label>
				<div class="controls">
					<form:input path="scoreVal" htmlEscape="false" class="input-xlarge  number"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">备注信息：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sco:scoAffirm:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>