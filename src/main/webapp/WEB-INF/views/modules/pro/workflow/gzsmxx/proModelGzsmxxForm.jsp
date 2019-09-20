<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>proProjectMd管理</title>
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
		<span>proProjectMd</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/proprojectmd/proModelMd/">proProjectMd列表</a></li>
			<li class="active"><a href="${ctx}/proprojectmd/proModelMd/form?id=${proModelMd.id}">proProjectMd<shiro:hasPermission name="proprojectmd:proModelMd:edit">${not empty proModelMd.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="proprojectmd:proModelMd:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="proModelMd" action="${ctx}/proprojectmd/proModelMd/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">model_id：</label>
				<div class="controls">
					<form:input path="modelId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数字 申请金额 例如：1000000：</label>
				<div class="controls">
					<form:input path="appAmount" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">subject：：</label>
				<div class="controls">
					<form:input path="subject" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">属性：国家级（含省级备选项目）、&ldquo;校级：</label>
				<div class="controls">
					<form:input path="appLevel" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目来源：            A为学生自主选题，来源于自己对课题长期积累与兴趣；            B为学生来源于教师科研项目选题；            C为学生承担社会、企业委托项目选题。：</label>
				<div class="controls">
					<form:input path="proSource" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">来源项目名称：限B和C的项目：</label>
				<div class="controls">
					<form:input path="sourceProjectName" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">来源项目类别：            写&ldquo;863项目&rdquo;            、&ldquo;973项目&rdquo;、            &ldquo;国家自然科学基金项目&rdquo;、            &ldquo;省级自然科学基金项目&rdquo;、            &ldquo;教师横向科研项目&rdquo;、            &ldquo;企业委托项目&rdquo;、            &ldquo;社会委托项目&rdquo;            以及其他项目标识。：</label>
				<div class="controls">
					<form:input path="sourceProjectType" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">是否入孵：</label>
				<div class="controls">
					<form:input path="iisIncubation" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">阶段成果：中期：</label>
				<div class="controls">
					<form:input path="stageResult" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">结项成果：结项：</label>
				<div class="controls">
					<form:input path="result" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">申报开始时间：</label>
				<div class="controls">
					<input name="appBeginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${proModelMd.appBeginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">申报结束时间：</label>
				<div class="controls">
					<input name="appEndDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${proModelMd.appEndDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">数字  报销金额 例如：1000000：</label>
				<div class="controls">
					<form:input path="reimbursementAmount" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="proprojectmd:proModelMd:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>