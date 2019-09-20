<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
	<script type="text/javascript">
        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
	<%--<div class="edit-bar clearfix">--%>
		<%--<div class="edit-bar-left">--%>
			<%--<span>场地管理</span>--%>
			<%--<i class="line weight-line"></i>--%>
		<%--</div>--%>
	<%--</div>--%>
	<%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/pw/pwSpace/">场地列表</a></li>
		<li class="active"><a
				href="${ctx}/pw/pwSpace/form?id=${pwSpace.id}&parent.id=${pwSpaceparent.id}">学院<shiro:hasPermission
				name="pw:pwSpace:edit">${not empty pwSpace.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
				name="pw:pwSpace:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<sys:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="pwSpace" action="${ctx}/pw/pwSpace/save" method="post"
			   class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="parent.id" value="${pwSpace.parent.id}"/>
		<form:hidden path="type" value="0"/>
		<input type="hidden" id="secondName" name="secondName"value="${secondName}"/>

		<div class="control-group">
			<label class="control-label"><i>*</i>学院名称：</label>
			<div class="controls">
				<form:input path="name" id="school" htmlEscape="false" maxlength="20" class="required"
							value=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" class="input-xxlarge" maxlength="200"
							   rows="4"></form:textarea>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="pw:pwSpace:edit">
				<input class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form></div>
<div id="dialog-message" title="信息">
	<p id="dialog-content"></p>
</div>
</body>
</html>