<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>创建项目管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<style>
		.table-thead-bg thead tr{
			background-color: #f4e6d4;
		}
		.table th{
			background: none;
		}
		#searchForm{
			height: auto;
			padding: 15px 0 0;
			overflow: hidden;
		}
		.ul-form input[type="text"]{
			height: 20px;
		}
		.ul-form select{
			height: 30px;
			width: 174px;
			max-width: 174px;
		}
		.form-search .ul-form li{
			margin-bottom: 15px;
		}
		.form-search .ul-form li.btns{
			float: right;
		}
		.table{
			margin-bottom: 20px;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {

		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>创建项目</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/proproject/proProject/">创建项目列表</a></li>
			<shiro:hasPermission name="proproject:proProject:edit"><li><a href="${ctx}/proproject/proProject/form">创建项目添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="proProject" action="${ctx}/proproject/proProject/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table  table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>项目名称</th>
					<th>项目标识</th>
					<th>更改时间</th>
					<th>状态</th>
					<shiro:hasPermission name="proproject:proProject:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="proProject">
				<tr>
					<td class="first-line">${proProject.projectName}
						<input  type="hidden" value="${proProject.id }"/></td>
					<td class="first-line">${proProject.projectMark}</td>
					<td>
						<a href="${ctx}/proproject/proProject/form?id=${proProject.id}">
						<fmt:formatDate value="${proProject.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</a>
					</td>
					<td>
						<c:choose>
							<c:when test="${proProject.state=='1'}">
								发布
							</c:when>
							<c:otherwise>
								未发布
							</c:otherwise>
						</c:choose>
					</td>
					<shiro:hasPermission name="proproject:proProject:edit"><td>
	    				<a href="${ctx}/proproject/proProject/form?id=${proProject.id}">修改</a>
						<a href="${ctx}/proproject/proProject/delete?id=${proProject.id}" onclick="return confirmx('确认要删除该创建项目吗？', this.href)">删除</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>