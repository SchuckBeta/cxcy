<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书管理</title>
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
		<span>系统证书</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sys/sysCertificate/">系统证书列表</a></li>
			<shiro:hasPermission name="sys:sysCertificate:edit"><li><a href="${ctx}/sys/sysCertificate/form">系统证书添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="sysCertificate" action="${ctx}/sys/sysCertificate/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>证书名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>证书类型：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('sys_certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>所属机构：</label>
					<sys:treeselect id="office" name="office.id" value="${sysCertificate.office.id}" labelName="office.name" labelValue="${sysCertificate.office.name}"
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>证书编号</th>
					<th>证书名称</th>
					<th>证书类型</th>
					<th>所属机构</th>
					<th>状态</th>
					<th>最后更新时间</th>
					<th>说明</th>
					<shiro:hasPermission name="sys:sysCertificate:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="sysCertificate">
				<tr>
					<td><a href="${ctx}/sys/sysCertificate/form?id=${sysCertificate.id}">
						${sysCertificate.no}
					</a></td>
					<td>
						${sysCertificate.name}
					</td>
					<td>
						${fns:getDictLabel(sysCertificate.type, 'sys_certificate_type', '-')}
					</td>
					<td>
						${sysCertificate.office.name}
					</td>
					<td>
						<c:if test="${sysCertificate.hasUse }">使用中</c:if>
						<c:if test="${!sysCertificate.hasUse }">-</c:if>
					</td>
					<td>
						<fmt:formatDate value="${sysCertificate.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${sysCertificate.remarks}
					</td>
					<shiro:hasPermission name="sys:sysCertificate:edit"><td>
						<c:if test="${!sysCertificate.hasUse }"><a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificate/ajaxUse?id=${sysCertificate.id}&hasUse=true">切换</a></c:if>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificate/form?id=${sysCertificate.id}">修改</a>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificate/design?id=${sysCertificate.id}">设计</a>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificate/view?id=${sysCertificate.id}">查看资源</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/sys/sysCertificate/delete?id=${sysCertificate.id}" onclick="return confirmx('确认要删除该系统证书吗？', this.href)">删除</a>
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