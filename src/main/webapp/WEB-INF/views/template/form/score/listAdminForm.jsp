<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<html>
<head>
	<title>参赛项目查询</title>
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
	<style>
		.tabel_head{
			position: relative;
		}
		.tabel_head .tlt{
			position: absolute;
			left: 10px;
			top:6px;
			color:white;
		}
	</style>
</head>
<body>
	<div class="mybreadcrumbs"><span>参赛项目查询</span></div>
	<div class="content_panel">
	<form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/gContestChangeList" method="post" class="breadcrumb form-search clearfix">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="input-item"><label>大赛类型</label>
		<form:select path="type" class="input-xlarge required">
			<form:option value="" label="所有大赛类型"/>
			<form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select></div>
		<%-- 学院:
		<sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="${role.office.name}"
		title="学院" url="/sys/office/treeData" />--%>
			<div class="input-item"><label class="char6">参赛项目名称</label><form:input path="pName" /></div>
			<div class="pull-right">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</div>
	</form:form>
	<table id="contentTable" class="table  table-bordered table-condensed self_table">
		<thead>
			<tr>
				<th width="160px">大赛编号</th>
				<th>参赛项目名称</th>
				<th>学院</th>
				<th>申报人</th>
				<th>组成员</th>
				<th>组别</th>
				<th>大赛类型</th>
				<th>融资情况</th>
				<th>指导老师</th>
				<th>网评分</th>
				<th>路演分</th>
				<th>评级</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gContest">
			<tr>
				<td>${gContest.competitionNumber}</td>
				<td>${gContest.pName}</td>
				<td>
				   ${gContest.collegeName}
				</td>
				<td>
				   ${gContest.declareName}
				</td>
				<td>
				   ${gContest.snames}
				</td>
				<td>
					${fns:getDictLabel(gContest.level, "gcontest_level", "")}
				</td>
				<td>
					${fns:getDictLabel(gContest.type, "competition_net_type", "")}
				</td>
				<td>
				  ${fns:getDictLabel(gContest.financingStat, "financing_stat", "")}
				</td>
				<td>
				   ${gContest.tnames}
				</td>
				<td>
				   ${gContest.schoolExportScore}
				</td>
				<td>
				   ${gContest.schoolluyanScore}
				</td>
				<td>
					<c:if test="${gContest.auditCode =='7'}">	
				  	${fns:getDictLabel(gContest.schoolendResult, "competition_college_prise", "")}
					</c:if>
				</td>
				<td>
					<c:choose>
						<c:when test="${gContest.auditCode!=null && gContest.auditCode!='7' && gContest.auditCode!='8'&& gContest.auditCode!='9'}">
						<a  target="_blank" href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">${gContest.auditState}</a>
					
						</c:when>
						<c:otherwise>
							${gContest.auditState}
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<a class="check_btn btn-pray" 
					href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
					查看
					</a>
					<c:if test="${college == null}">
					<a class="check_btn btn-pray"
					href="${ctx}/gcontest/gContest/changeGcontest?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
					变更
					</a>
					</c:if>
					<%-- <a class="check_btn btn-pray" target="_blank"
					 href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">查看</a> --%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	${page.footer}
		</div>
</body>
</html>