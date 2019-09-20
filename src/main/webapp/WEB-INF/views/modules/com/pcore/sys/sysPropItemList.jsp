<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统功能配置项管理</title>
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
		<span>系统功能配置项</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysPropItem/">系统功能配置项列表</a></li>
			<shiro:hasPermission name="sys:sysPropItem:edit"><li><a href="${ctx}/sys/sysPropItem/form">系统功能配置项添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysPropItem" action="${ctx}/sys/sysPropItem/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>系统配置ID：</label>
					<form:select path="prop.id" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>标题：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>系统配置ID</th>
					<th>标题</th>
					<th>开关</th>
					<shiro:hasPermission name="sys:sysPropItem:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysPropItem">
				<tr>
					<td><a href="${ctx}/sys/sysPropItem/form?id=${sysPropItem.id}">
						${fns:getDictLabel(sysPropItem.prop.id, '', '')}
					</a></td>
					<td>
						${sysPropItem.name}
					</td>
					<td>
						${fns:getDictLabel(sysPropItem.isOpen, '', '')}
					</td>
					<shiro:hasPermission name="sys:sysPropItem:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysPropItem/form?id=${sysPropItem.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysPropItem/delete?id=${sysPropItem.id}" onclick="return confirmx('确认要删除该系统功能配置项吗？', this.href)">删除</a>
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