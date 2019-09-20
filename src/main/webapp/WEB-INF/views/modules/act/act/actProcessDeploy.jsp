<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部署流程 - 流程管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
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
			background:#f4e6d4;
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
			border-top:none;
		}
		.process-wrap>.nav-tabs>li>a{
			color: #646464;
			border:1px solid #ddd;
		}
		.process-wrap>.nav-tabs>li>a:hover{
			background-color: #f4e6d4;
		}
		.process-wrap>.nav-tabs>.active>a{
			color:#646464;
			background-color: #f4e6d4;
			font-weight: bold;
		}
	</style>
</head>
<body>
<div class="process-wrap">
	<div class="mybreadcrumbs">
		<span>部署流程</span>
		<p class="yw-line"></p>
	</div>
	<ul class="nav nav-tabs" style="margin-bottom: 0px;">
		<li><a href="${ctx}/act/process/">流程管理</a></li>
		<li class="active"><a href="${ctx}/act/process/deploy/">部署流程</a></li>
		<li><a href="${ctx}/act/process/running/">运行中的流程</a></li>
	</ul>
	<div style="border:1px solid #ccc;border-top: none;padding-top: 20px;min-height: 700px;">
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/act/process/deploy" method="post" enctype="multipart/form-data" class="form-horizontal">
		<div class="control-group" style="border: none;">
			<label class="control-label">流程分类：</label>
			<div class="controls">
				<select id="category" name="category" class="required input-medium">
					<c:forEach items="${fns:getDictList('act_category')}" var="dict">
						<option value="${dict.value}">${dict.label}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="control-group" style="border: none;">
			<label class="control-label">流程文件：</label>
			<div class="controls">
				<input type="file" id="file" name="file" class="required"/>
				<span class="help-inline">支持文件格式：zip、bar、bpmn、bpmn20.xml</span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary my-primary-btn" type="submit" value="提 交"/>
			<input id="btnCancel" class="btn btn-primary my-primary-btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form>
	</div>
	</div>
</body>
</html>
