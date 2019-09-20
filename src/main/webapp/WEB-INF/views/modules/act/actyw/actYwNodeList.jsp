<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>流程节点管理</title>
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
		<span>流程节点</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/actyw/actYwNode/">流程节点列表</a></li>
			<shiro:hasPermission name="actyw:actYwNode:edit"><li><a href="${ctx}/actyw/actYwNode/form">流程节点添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="actYwNode" action="${ctx}/actyw/actYwNode/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>名称</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>模块</label>
					<form:select path="type" class="input-small">
						<form:option value="" label="--请选择--"/>
						<form:options items="${nodeEtypes}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>表单:</label>
					<form:select path="isForm" class="input-small">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>可见:</label>
					<form:select path="isVisible" class="input-small">
                                                <form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>节点名称</th>
					<th>业务模块</th>
					<th>是否有表单</th>
					<th>默认图标</th>
					<th>流程标识</th>
					<th>最后更新时间</th>
					<th>备注</th>
					<shiro:hasPermission name="actyw:actYwNode:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="actYwNode">
				<tr>
					<td style="text-align: left;"><a href="${ctx}/actyw/actYwNode/form?id=${actYwNode.id}">
						<img src="${fns:ftpImgUrl(actYwNode.iconUrl)}" width="20" height="20"/>${actYwNode.name}
					</a></td>
					<td>
					   <c:forEach items="${nodeEtypes }" var="nodeEtype">
						   <c:if test="${nodeEtype.id eq actYwNode.type}">
	                                                ${nodeEtype.name}
						   </c:if>
					   </c:forEach>
					</td>
					<td>${fns:getDictLabelByBoolean(actYwNode.isForm, 'true_false', '')}</td>
                                        <td>
                                            ${actYwNode.iconUrl}
                                        </td>
                                        <td>
                                            ${actYwNode.nodeType}/${actYwNode.nodeKey}
                                        </td>
					<td>
						<fmt:formatDate value="${actYwNode.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${actYwNode.remarks}
					</td>
					<shiro:hasPermission name="actyw:actYwNode:edit"><td>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwNode/form?id=${actYwNode.id}">修改</a>
						<%-- <a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwNode/delete?id=${actYwNode.id}" onclick="return confirmx('确认要删除该流程节点吗？', this.href)">删除</a> --%>
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