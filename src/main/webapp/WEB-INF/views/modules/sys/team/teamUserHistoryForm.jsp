<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>团队历史纪录管理</title>
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
		<span>团队历史纪录</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/team/teamUserHistory/">团队历史纪录列表</a></li>
			<li class="active"><a href="${ctx}/team/teamUserHistory/form?id=${teamUserHistory.id}">团队历史纪录<shiro:hasPermission name="team:teamUserHistory:edit">${not empty teamUserHistory.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="team:teamUserHistory:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="teamUserHistory" action="${ctx}/team/teamUserHistory/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">成员类型：1、学生，2、导师：</label>
				<div class="controls">
					<form:select path="userType" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">用户ID：</label>
				<div class="controls">
					<sys:treeselect id="user" name="user.id" value="${teamUserHistory.user.id}" labelName="user.name" labelValue="${teamUserHistory.user.name}"
						title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">团队ID：</label>
				<div class="controls">
					<form:input path="teamId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">状态：</label>
				<div class="controls">
					<form:input path="state" htmlEscape="false" maxlength="1" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">根据学分配比 由leader分配。：</label>
				<div class="controls">
					<form:input path="weightVal" htmlEscape="false" maxlength="11" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">remarks：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">项目/大赛ID：</label>
				<div class="controls">
					<form:input path="proId" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">双创项目、双创大赛、科研：</label>
				<div class="controls">
					<form:input path="proType" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">【大创 中创 小创 互联网+ 蓝桥杯  数据字典】：</label>
				<div class="controls">
					<form:input path="proSubType" htmlEscape="false" maxlength="64" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">所作的项目或者大赛是否结束，0-未结束，1-结束：</label>
				<div class="controls">
					<form:input path="finish" htmlEscape="false" maxlength="1" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="team:teamUserHistory:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>