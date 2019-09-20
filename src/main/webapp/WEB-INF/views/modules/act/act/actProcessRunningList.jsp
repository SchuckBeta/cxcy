<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>运行中的流程</title>
	<%@include file="/WEB-INF/views/include/backCommon.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			$("#ps").val($("#pageSize").val());
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/process/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在设置，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="procDefId" value="" />
			<select id="categoryBoxCategory" name="category">
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary"  style="position: absolute; top: 75%;left: 55%;" type="submit" value="   保    存   "/>　　
		</form>
	</script>
	<style>
		html,body{
			font-size: 14px;
			font-family: "微软雅黑";
		}
		.process-wrap{
			margin: 27px 27px 0 27px;
		}

		.table-nowrap td{
			max-width: 110px !important;
		}
		.breadcrumb{
			background-color: #fff;
		}
		.form-search{
			position: relative;
		}
		.btn-primary{
			background: #e9432d;
			position: absolute;
			right:0;
			top:8px;
			color:#fff;
		}
		.btn-primary:hover{
			background: #e9432d;
			color:#fff;
		}
		table tr th{
			color:#656565;
			background: #f4e6d4 !important;
		}
		table td .btn{
			padding:3px 11px ;
			background: #e5e5e5;
			color:#666;
		}
		table td .btn:hover{
			background:#e9432d ;
			color:#fff;
		}

		.m-form-search{
			border-left: 1px solid #ddd; border-right: 1px solid #ddd;  margin-bottom: 0px; padding: 15px 15px;  margin-top: 0px; border-radius: 0px;
		}
	</style>
</head>
<body>
<div class="process-wrap" style="margin-bottom: 60px;">
	<div class="edit-bar clearfix">
		<div class="edit-bar-left">
			<span>运行中的流程</span>
			<i class="line weight-line"></i>
		</div>
	</div>
	<ul class="nav nav-tabs" style="margin-bottom: 0">
		<li><a href="${ctx}/act/process/">流程管理</a></li>
		<li><a href="${ctx}/act/process/deploy/">部署流程</a></li>
		<li class="active"><a href="${ctx}/act/process/running/">运行中的流程</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/process/running/" method="post" class="breadcrumb form-search m-form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>流程实例ID：</label><input type="text" id="procInsId" name="procInsId" value="${procInsId}" class="input-medium"/>
		<label>流程定义Key：</label><input type="text" id="procDefKey" name="procDefKey" value="${procDefKey}" class="input-medium"/>
		&nbsp;<input id="btnSubmit" style="margin-right: 15px;" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<sys:message content="${message}"/>
	<table class="table  table-bordered table-condensed table-nowrap">
		<thead>
			<tr>
				<th width="400px">执行ID</th>
				<th width="400px">流程实例ID</th>
				<th width="400px">流程定义ID</th>
				<th width="50px">当前环节</th>
				<th width="50px">是否挂起</th>
				<th width="50px">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>
					<td>${procIns.id}</td>
					<td>${procIns.processInstanceId}</td>
					<td>${procIns.processDefinitionId}</td>
					<td>${procIns.activityId}</td>
					<td>${procIns.suspended}</td>
					<td>
						<shiro:hasPermission name="act:process:edit">
							<a href="${ctx}/act/process/deleteProcIns?procInsId=${procIns.processInstanceId}&reason=" onclick="return promptx('删除流程','删除原因',this.href);" class="btn">删除流程</a>
						</shiro:hasPermission>&nbsp;
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	${page.footer}
	</div>
</body>
</html>
