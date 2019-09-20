<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统功能管理</title>
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
		<span>系统功能</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysProp/">系统功能列表</a></li>
			<shiro:hasPermission name="sys:sysProp:edit"><li><a href="${ctx}/sys/sysProp/form">系统功能添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysProp" action="${ctx}/sys/sysProp/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>配置类型：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${sysPropTypes}" itemLabel="name" itemValue="key" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>标题：</label>
					<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>开关：</label>
					<form:select path="onOff" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>配置类型</th>
					<th>标题</th>
					<th>开关</th>
					<shiro:hasPermission name="sys:sysProp:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysProp">
				<tr>
					<td><a href="${ctx}/sys/sysProp/form?id=${sysProp.id}">
						<c:forEach var="item" items="${sysPropTypes}">
	                        <c:if test="${sysProp.type eq item.key}">${item.name }</c:if>
	                    </c:forEach>
					</a></td>
					<td>
						${sysProp.title}
					</td>
					<td>
						${fns:getDictLabel(sysProp.onOff, 'yes_no', '')}
					</td>
					<shiro:hasPermission name="sys:sysProp:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysProp/form?id=${sysProp.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysProp/delete?id=${sysProp.id}" onclick="return confirmx('确认要删除该系统功能吗？', this.href)">删除</a>
	    				<shiro:hasPermission name="sys:sysPropItem:edit">
	    					<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysPropItem/form?prop.id=${sysProp.id}">添加配置</a>
	    					<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysProp/listForm?id=${sysProp.id}">修改配置</a>
						</shiro:hasPermission>
    					<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysProp/setProp?id=${sysProp.id}">配置</a>
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