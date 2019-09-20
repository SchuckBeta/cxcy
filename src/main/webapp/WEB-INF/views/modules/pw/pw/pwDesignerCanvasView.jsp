<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
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
		<span>基地楼层设计预览</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/pw/pwDesignerCanvas/index">基地全局图预览</a></li>
			<li class="active"><a href="${ctx}/pw/pwDesignerCanvas/view?id=${pwDesignerCanvas.id}">基地楼层设计预览</a></li>
		</ul><br/>
		<form:form id="searchForm" modelAttribute="pwDesignerCanvas" action="${ctx}/pw/pwDesignerCanvas/view" method="post" class="horizontal clearfix form-search-block">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            <div class="search-btn-box">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
            </div>
        </form:form>
		<sys:message content="${message}"/>
		<div style="width:100%; height: 800px; background-color: #333; display: block; padding: 1px;">
			<div style="background-image: url('http://127.0.0.1:8093/images/bigMatchIconbg.jpg'); background-repeat: no-repeat; width:100%; height: 100%; display: block;background-size: cover; ">
			</div>
		</div>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>