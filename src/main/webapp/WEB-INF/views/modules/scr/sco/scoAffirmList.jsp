<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>创新、创业、素质学分认定表管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
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
		<span>创新、创业、素质学分认定表</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/sco/scoAffirm/">创新、创业、素质学分认定表列表</a></li>
			<shiro:hasPermission name="sco:scoAffirm:edit"><li><a href="${ctx}/sco/scoAffirm/form">创新、创业、素质学分认定表添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="scoAffirm" action="${ctx}/sco/scoAffirm/" method="post" class="breadcrumb form-search">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li><label>学分类型：1、创新学分/2、创业学分/3、素质学分：</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
				<li><label>项目/大赛ID：</label>
					<form:input path="proId" htmlEscape="false" maxlength="64" class="input-medium"/>
				</li>
				<li><label>项目类型/大赛类型：具体值可查看文档：</label>
					<form:input path="proType" htmlEscape="false" maxlength="1" class="input-medium"/>
				</li>
				<li><label>项目级别/大赛级别：具体值可查看文档：</label>
					<form:input path="proLevelType" htmlEscape="false" maxlength="1" class="input-medium"/>
				</li>
				<li><label>只有项目有类别，大赛无类别：具体值可查看文档：</label>
					<form:input path="proPtype" htmlEscape="false" maxlength="1" class="input-medium"/>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-bordered table-condensed">
			<thead>
				<tr>
					<th>流程实例ID</th>
					<th>学分类型：1、创新学分/2、创业学分/3、素质学分</th>
					<th>项目/大赛ID</th>
					<th>项目类型/大赛类型：具体值可查看文档</th>
					<th>项目级别/大赛级别：具体值可查看文档</th>
					<th>只有项目有类别，大赛无类别：具体值可查看文档</th>
					<th>项目/大赛结果：具体值可查看文档</th>
					<th>大赛有得分，项目无得分</th>
					<th>认定学分</th>
					<th>更新时间</th>
					<th>备注信息</th>
					<shiro:hasPermission name="sco:scoAffirm:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="scoAffirm">
				<tr>
					<td><a href="${ctx}/sco/scoAffirm/form?id=${scoAffirm.id}">
						${scoAffirm.procInsId}
					</a></td>
					<td>
						${fns:getDictLabel(scoAffirm.type, '', '')}
					</td>
					<td>
						${scoAffirm.proId}
					</td>
					<td>
						${scoAffirm.proType}
					</td>
					<td>
						${scoAffirm.proLevelType}
					</td>
					<td>
						${scoAffirm.proPtype}
					</td>
					<td>
						${scoAffirm.proResult}
					</td>
					<td>
						${scoAffirm.proScore}
					</td>
					<td>
						${scoAffirm.scoreVal}
					</td>
					<td>
						<fmt:formatDate value="${scoAffirm.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${scoAffirm.remarks}
					</td>
					<shiro:hasPermission name="sco:scoAffirm:edit"><td>
	    				<a href="${ctx}/sco/scoAffirm/form?id=${scoAffirm.id}">修改</a>
						<a href="${ctx}/sco/scoAffirm/delete?id=${scoAffirm.id}" onclick="return confirmx('确认要删除该创新、创业、素质学分认定表吗？', this.href)">删除</a>
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