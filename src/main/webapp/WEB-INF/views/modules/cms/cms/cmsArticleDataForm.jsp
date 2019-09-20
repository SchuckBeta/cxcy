<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>文章详情表</title>
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
            <span>文章详情表</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="cmsArticleData" action="${ctx}/cms/cmsArticleData/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	内容id：</label>
				<div class="controls">
					<form:input path="contentId" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	文章内容：</label>
				<div class="controls">
					<form:textarea path="content" htmlEscape="false" rows="4" class="input-xxlarge "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	文章来源：</label>
				<div class="controls">
					<form:input path="copyfrom" htmlEscape="false" maxlength="255" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否展示文章来源：</label>
				<div class="controls">
					<form:input path="isshowcopyfrom" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	相关文章：</label>
				<div class="controls">
					<form:input path="relation" htmlEscape="false" maxlength="255" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否允许评论（0否1是或者空）：</label>
				<div class="controls">
					<form:input path="allowComment" htmlEscape="false" maxlength="1" class=" "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="cms:cmsArticleData:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>