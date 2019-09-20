<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>留言</title>
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
            <span>留言</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="cmsGuestbook" action="${ctx}/cms/cmsGuestbook/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	留言分类（咨询，问题反馈）：</label>
				<div class="controls">
					<form:select path="type" class=" ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	留言内容：</label>
				<div class="controls">
					<form:textarea path="content" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	姓名：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="100" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	邮箱：</label>
				<div class="controls">
					<form:input path="email" htmlEscape="false" maxlength="100" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	手机号：</label>
				<div class="controls">
					<form:input path="phone" htmlEscape="false" maxlength="100" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	QQ：</label>
				<div class="controls">
					<form:input path="qq" htmlEscape="false" maxlength="50" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	审核状态（待审核，审核通过，审核不通过）：</label>
				<div class="controls">
					<form:textarea path="auditstatus" htmlEscape="false" rows="4" maxlength="1" class="input-xxlarge "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否推荐（0否1是）：</label>
				<div class="controls">
					<form:radiobuttons path="isRecommend" items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	回复人：</label>
				<div class="controls">
					<sys:treeselect id="reUser" name="reUser.id" value="${cmsGuestbook.reUser.id}" labelName="reUser.name" labelValue="${cmsGuestbook.reUser.name}"
						title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	回复时间：</label>
				<div class="controls">
					<input name="reDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						value="<fmt:formatDate value="${cmsGuestbook.reDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	回复内容：</label>
				<div class="controls">
					<form:input path="reContent" htmlEscape="false" maxlength="64" class=" "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="cms:cmsGuestbook:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>