<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>proProjectMd管理</title>
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
			$("#ps").val($("#pageSize").val());
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
		<span>proProjectMd</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/proprojectmd/proModelMd/">proProjectMd列表</a></li>
			<shiro:hasPermission name="proprojectmd:proModelMd:edit"><li><a href="${ctx}/proprojectmd/proModelMd/form">proProjectMd添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="proModelMd" action="${ctx}/proprojectmd/proModelMd/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>更新时间</th>
					<shiro:hasPermission name="proprojectmd:proModelMd:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="proModelMd">
				<tr>
					<td><a href="${ctx}/proprojectmd/proModelMd/form?id=${proModelMd.id}">
						<fmt:formatDate value="${proModelMd.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</a></td>
					<shiro:hasPermission name="proprojectmd:proModelMd:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/proprojectmd/proModelMd/form?id=${proModelMd.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/proprojectmd/proModelMd/delete?id=${proModelMd.id}" onclick="return confirmx('确认要删除该proProjectMd吗？', this.href)">删除</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page.footer}
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>