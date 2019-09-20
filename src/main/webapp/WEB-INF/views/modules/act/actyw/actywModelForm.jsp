<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新建模型 - 模型管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
					setTimeout(function(){location='${ctx}/act/model/'}, 1000);
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
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
	</script>
	<style>
		html,body{
			font-size: 14px;
			font-family: "微软雅黑";
		}
		.process-wrap{
			margin: 27px 27px 0 27px;
		}
		.mybreadcrumbs{
			height: 41px;
			position: relative;
		}
		.mybreadcrumbs span{
			padding-right:20px;
			font-size: 16px;
			line-height: 41px;
			background: #fff;
		}
		.mybreadcrumbs .yw-line{
			position: absolute;
			height: 3px;
			width: 100%;
			background:orange;
			left: 0px;
			top:20px;
			z-index: -10;
		}
		.table-nowrap td{
			max-width: 110px !important;
		}
		.breadcrumb{
			background-color: #fff;
		}
		.my-primary-btn{
			background: #e9432d;
			color:#fff;
		}
		.btn-primary:hover{
			background: #e9432d;
			color:#fff;
		}
		.form-actions{
			background: #fff;
			border:none;
		}
		.control-group{
			border-bottom: none;
		}
	</style>
</head>
<body>
<div class="process-wrap">
	<div class="mybreadcrumbs">
		<span>新建模型</span>
		<p class="yw-line"></p>
	</div>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/model/">模型管理</a></li>
		<li><a href="${ctx}/act/model/create">新建模型</a></li>
		<li class="active"><a href="${ctx}/actyw/model/form">新建模型2</a></li>
	</ul><br/>
	<sys:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="rtModel" action="${ctx}/actyw/model/save" target="_blank" method="post" class="form-horizontal">
		<div class="control-group">
			<label class="control-label">流程分类：</label>
			<div class="controls">
				<select id="category" name="category" class="required input-medium">
					<c:forEach items="${fns:getDictList('act_category')}" var="dict">
						<option value="${dict.value}">${dict.label}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模块名称：</label>
			<div class="controls">
				<input id="name" name="name" type="text" class="required" />
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模块标识：</label>
			<div class="controls">
				<input id="key" name="key" type="text" class="required" />
				<span class="help-inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模块描述：</label>
			<div class="controls">
				<textarea id="description" name="description" class="required"></textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">JSONXML：</label>
			<div class="controls">
				<textarea id="jsonXml" name="jsonXml" class="required"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary my-primary-btn" type="submit" value="提 交"/>
			<input id="btnCancel" class="btn btn-primary my-primary-btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
	</div>
</body>
</html>
