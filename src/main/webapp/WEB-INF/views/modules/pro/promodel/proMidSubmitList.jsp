<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>proMidSubmit管理</title>
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
		<span>proMidSubmit</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/promodel/proMidSubmit/">proMidSubmit列表</a></li>
			<shiro:hasPermission name="promodel:proMidSubmit:edit"><li><a href="${ctx}/promodel/proMidSubmit/form">proMidSubmit添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="proMidSubmit" action="${ctx}/promodel/proMidSubmit/" method="post" class="breadcrumb form-search">
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
					<th>update_date</th>
					<shiro:hasPermission name="promodel:proMidSubmit:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="proMidSubmit">
				<tr>
					<td><a href="${ctx}/promodel/proMidSubmit/form?id=${proMidSubmit.id}">
						<fmt:formatDate value="${proMidSubmit.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</a></td>
					<shiro:hasPermission name="promodel:proMidSubmit:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/promodel/proMidSubmit/form?id=${proMidSubmit.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/promodel/proMidSubmit/delete?id=${proMidSubmit.id}" onclick="return confirmx('确认要删除该proMidSubmit吗？', this.href)">删除</a>
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