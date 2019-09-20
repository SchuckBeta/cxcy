<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
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
		<span>${pwEnterDetail.pename} 入驻申报</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/pw/pwEnterDetail/list?petype=${pwEnterDetail.petype}">${pwEnterDetail.pename} 入驻申报列表</a></li>
			<shiro:hasPermission name="pw:pwEnterDetail:edit"><li><a href="${ctx}/pw/pwEnterDetail/form?petype=${pwEnterDetail.petype}">${pwEnterDetail.pename} 入驻申报添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="pwEnterDetail" action="${ctx}/pw/pwEnterDetail/list?petype=${pwEnterDetail.petype}" method="post" class="breadcrumb form-search">
			<input id="petype" name="petype" type="hidden" value="${pwEnterDetail.petype}"/>
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<ul class="ul-form">
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li><label>入驻编号：</label>
					<form:input path="pwEnter.no" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>学号：</label>
					<form:input path="pwEnter.applicant.no" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>所属学院：</label>
					<form:input path="pwEnter.applicant.office.name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>关键字：</label>
					<form:input path="pwEnter.keys" htmlEscape="false" maxlength="255" class="input-medium" placeholder="区分大小写"/>
					<%-- <input name="pwEnter.keys" value="${pwEnterDetail.pwEnter.keys}" class="input-medium" placeholder="区分大小写"> --%>
				</li>
				<li><label>入驻类型</label>
					<form:select id="type" path="type" class="input-medium">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('pw_enter_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-thead-bg">
			<thead>
				<tr>
					<th>入驻编号</th>
					<th>负责人</th>
					<th>所属学院</th>
					<th>学号</th>
					<th>状态</th>
					<th>名称(团队/项目/企业)</th>
					<%-- <c:if test="${empty pwEnterDetail.petype}"><th>类型</th></c:if> --%>
					<th>最后更新时间</th>
					<th>备注</th>
					<shiro:hasPermission name="pw:pwEnterDetail:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="cpwEnterDetail">
				<tr>
					<td><a href="${ctx}/pw/pwEnterDetail/form?id=${cpwEnterDetail.id}">
						${cpwEnterDetail.pwEnter.no}
					</a></td>
					<td>
						${cpwEnterDetail.pwEnter.applicant.name}
					</td>
					<td>
						${cpwEnterDetail.pwEnter.applicant.office.name}
					</td>
					<td>
						${cpwEnterDetail.pwEnter.applicant.no}
					</td>
					<td>
						${fns:getDictLabel(cpwEnterDetail.status, 'pw_enter_shstatus', '')}
					</td>
					<td>
						<fmt:formatDate value="${cpwEnterDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${cpwEnterDetail.remarks}
					</td>
					<shiro:hasPermission name="pw:pwEnterDetail:edit"><td>
						<c:if test="${cpwEnterDetail.pwEnter.status eq 0}">
		    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnter/form?id=${cpwEnterDetail.pwEnter.id}">审核</a>
						</c:if><c:if test="${cpwEnterDetail.pwEnter.status eq 1}">
		    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnter/form?id=${cpwEnterDetail.pwEnter.id}">续期</a>
		    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnter/form?id=${cpwEnterDetail.pwEnter.id}">退孵</a>
						</c:if><c:if test="${cpwEnterDetail.pwEnter.status eq 2}">
		    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnter/form?id=${cpwEnterDetail.pwEnter.id}">重新入驻</a>
						</c:if>
	    				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnterDetail/form?id=${cpwEnterDetail.id}">修改</a>
						<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/pw/pwEnterDetail/delete?id=${cpwEnterDetail.id}" onclick="return confirmx('确认要删除该入驻申报详情吗？', this.href)">删除</a>
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