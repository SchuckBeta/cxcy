<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>流程管理</title>
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
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input style="position: absolute; top: 75%;left: 55%;" id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
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
		.table .m-table-th th{
			text-align: center;
		}
		.table .m-table-td{
			text-align: center;
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
		.nav-tabs>li{ margin-right:5px;}
	</style>
</head>
<body>
	<div class="process-wrap">
		<div class="edit-bar clearfix">
			<div class="edit-bar-left">
				<span>流程管理</span>
				<i class="line weight-line"></i>
			</div>
		</div>
		<ul class="nav nav-tabs" style="margin-bottom:0px;">
			<li class="active"><a href="${ctx}/act/process/">流程管理</a></li>
			<li><a href="${ctx}/act/process/deploy/">部署流程</a></li>
			<li><a href="${ctx}/act/process/running/">运行中的流程</a></li>
		</ul>
		<form id="searchForm" action="${ctx}/act/process/" method="post" class="breadcrumb form-search m-form-search" >
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<select id="category" name="category" class="input-medium">
				<option value="">全部分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
				</c:forEach>
			</select>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" style="top:15px;right: 15px;"/>
		</form>
		<sys:message content="${message}"/>
		<table class="table  table-bordered table-condensed table-nowrap table-hover">
			<thead>
				<tr class="m-table-th">
					<th>流程分类</th>
					<th>流程名称</th>
					<th style="width:300px;">流程标识/流程ID</th>
					<!-- <th>流程标识</th> -->
					<th>流程版本</th>
					<th>部署时间</th>
					<th>流程XML</th>
					<th>流程图片</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="object">
					<c:set var="process" value="${object[0]}" />
					<c:set var="deployment" value="${object[1]}" />
					<tr>
						<td class="m-table-td"><a href="javascript:updateCategory('${process.id}', '${process.category}')" title="设置分类">${fns:getDictLabel(process.category,'act_category','无分类')}</a></td>
						<td class="m-table-td">${process.name}</td>
						<td title="${process.key}">${process.id}</td>
						<%-- <td class="m-table-td">${process.key}</td> --%>
						<td class="m-table-td"><b title='流程版本号'>V: ${process.version}</b></td>
						<td class="m-table-td"><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=xml">${process.resourceName}</a></td>
						<td><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=image">${process.diagramResourceName}</a></td>
						<td class="m-table-td" style="padding: 10px 0px;">
							<c:if test="${process.suspended}">
								<a href="${ctx}/act/process/update/active?procDefId=${process.id}" class="btn" onclick="return confirmx('确认要激活吗？', this.href)">激活</a>
							</c:if>
							<c:if test="${!process.suspended}">
								<a href="${ctx}/act/process/update/suspend?procDefId=${process.id}" class="btn" onclick="return confirmx('确认挂起除吗？', this.href)">挂起</a>
							</c:if>
							<a href='${ctx}/act/process/delete?deploymentId=${process.deploymentId}' class="btn" onclick="return confirmx('确认要删除该流程吗？', this.href)">删除</a><br>
							<a style="padding:3px 18px; margin-top: 5px;" href='${ctx}/act/process/convert/toModel?procDefId=${process.id}' class="btn" onclick="return confirmx('确认要转换为模型吗？', this.href)">转换为模型</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		${page.footer}
	</div>
</body>
</html>
