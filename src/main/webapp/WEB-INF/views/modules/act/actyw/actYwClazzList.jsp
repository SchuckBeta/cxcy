<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>监听管理</title>
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
		<span>监听</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/actyw/actYwClazz/">监听列表</a></li>
			<shiro:hasPermission name="actyw:actYwClazz:edit"><li><a href="${ctx}/actyw/actYwClazz/form">监听添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwClazz" action="${ctx}/actyw/actYwClazz/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>表单组</label>
					<form:select id="theme" path="theme" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${formThemes}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>类名：</label>
					<form:input path="clazz" htmlEscape="false" maxlength="64" class="input-medium"/>
				</li>
				<li><label>别名：</label>
					<form:input path="alias" htmlEscape="false" maxlength="200" class="input-medium"/>
				</li>
				<li><label>说明：</label>
					<form:input path="remarks" htmlEscape="false" maxlength="200" class="input-medium"/>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>表单组</th>
					<th>类包</th>
					<th>类名</th>
					<th>别名</th>
					<th>说明</th>
					<shiro:hasPermission name="actyw:actYwClazz:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwClazz">
				<tr>
					<td><a href="${ctx}/actyw/actYwClazz/form?id=${actYwClazz.id}">
						<c:forEach var="item" items="${formThemes}">
							<c:if test="${actYwForm.theme eq item.id}">${item.name }</c:if>
						</c:forEach>
					</a></td>
					<td>
						${actYwClazz.packag}
					</td>
					<td>
						${actYwClazz.clazz}
					</td>
					<td>
						${actYwClazz.alias}
					</td>
					<td>
						${actYwClazz.remarks}
					</td>
					<shiro:hasPermission name="actyw:actYwClazz:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwClazz/form?id=${actYwClazz.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwClazz/delete?id=${actYwClazz.id}" onclick="return confirmx('确认要删除该监听吗？', this.href)">删除</a>
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