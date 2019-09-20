<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改立项审核结果</title>
	    <script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/gcProject/GC_check_new.css">
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxCommon}/common-js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctxJs}/gcProject/project_check.js"></script>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">

	<link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=1111">
	<script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>
<style>
	input[readonly]{
		background-color: #eee;
		opacity: 1;
	}
</style>
<script type="text/javascript">
	$(document).ready(function() {


	});

	function changeSet() {
		var url='/a/state/changeSchoolSet';
		$.ajax({
			type: 'post',
			url: url,
			datatype: "json",
			data: {"number" : $("#number").val(), "roleId" : $("#roleId").val()},
			success: function (data) {
				var st = "success";
				if (data.ret == "1") {
					alert("打回成功");
				}else{
					alert(data.msg);
				}
			}
		});

	}
</script>
<body>
<div class="mybreadcrumbs"><span>修改立项审核结果</span></div>
<form:form id="inputForm" modelAttribute="projectDeclare" action="${ctx}/state/changeSchoolSet" method="post" >

	<div class="container-fluid content-wrap">
		项目编号：<input type="text" name="number"id="number"/>
		审核节点角色ID：<input type="text" name="roleId" id="roleId" value="431e3fc9adb248279221c0ab5b3b717f"/>
		（默认为学院秘书）
		<input class="btn btn-primary btn-small" onclick="changeSet()" value="打回修改级别"/>
	</div>
</form:form>
</body>

</html>
