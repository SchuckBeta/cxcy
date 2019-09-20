<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<html>
<head>
	<title>${frontTitle}</title>
	<meta name="decorator" content="site-decorator" />
	<link rel="stylesheet" type="text/css" href="${ctxCss}/Contestlist.css">
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/Gctable.css">
	<link href="${ctxCommon}/common-css/website-pagenation.css" rel="stylesheet"/>
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
	<style type="text/css">
	th {
	    text-align: center;
	}
	</style>
</head>
<body>
	<%-- <form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/" method="post" class="breadcrumb form-search" style="margin-top: 50px;"> --%>
	<form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<form id="form" action="##" method="get" >
            <div class="content">
                <div class="_btn"><a href="${ctx}/gcontest/gContest/viewList">当前大赛</a><a href="${ctx}/gcontest/gContest/">我的大赛列表</a></div>
                <div class="head" >我的大赛列表</div>
				<div class="copt-table-list">
                	<table  style="width: 100%;"  cellspacing="0" cellpadding="0" class="my-copt-table">
                    <thead>
                       <tr>
							<th>序号</th>
							<th width="180px">大赛编号</th>
							<th>大赛项目名称</th>
							<th width="65px">大赛类型</th>
							<th>负责人</th>
							<th>组成员</th>
							<th>指导老师</th>
							<th width="65px">参赛组别</th>
							<th width="65px">当前赛制</th>
							<th width="105px">当前赛况</th>
							<th>荣获奖项</th>
							<th>操作</th>
						</tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${page.list}" var="gContest" varStatus="state">
							<tr>
								<td>${state.count}</td>
								<td>${gContest.competitionNumber}</td>
								<td>${gContest.pName}</td>
								<td>
								${fns:getDictLabel(gContest.type, "competition_net_type", "")}
								</td>
								<td>${gContest.declareName}</td>
								<td>${gContest.snames }</td>
								<td>${gContest.tnames }</td>
								<td>${fns:getDictLabel(gContest.level, "gcontest_level", "")}</td>
								<td>${gContest.currentSystem}</td>
								<td>${gContest.auditState}</td>
								<td>
								<c:if test="${gContest.schoolendResult!=null}">
									${fns:getDictLabel(gContest.schoolendResult, "competition_college_prise", "")}
								</c:if>
								</td>
								<td class="border-right">
									<a class="buttom btn"
									href="${ctx}/gcontest/gContest/viewForm?id=${gContest.id}" >查看</a>
									<c:if test="${gContest.create_by==user.id}">
										<c:if test="${gContest.auditState=='未提交'}">
											<a class="buttom btn"
											href="${ctx}/gcontest/gContest/form?id=${gContest.id}" >编辑</a>
											<a class="buttom btn"
											href="${ctx}/gcontest/gContest/delete?id=${gContest.id}"
											onclick="return confirm('确认要删除该大赛申报吗？', this.href)">删除</a>
										</c:if>
									</c:if>
									<c:choose>
										<c:when test="${gContest.auditCode!=null &&gContest.auditCode >0&&gContest.auditCode<7}">
											<a class="buttom btn" href="${ctx}/act/task/processMap?proInsId=${gContest.proc_ins_id}" target="_blank">跟踪</a>
										</c:when>
										<c:otherwise>
											<c:if test="${not empty gContest.proc_ins_id}">
												<a class="buttom btn" href="${ctx}/act/task/processMapByType?proInsId=${gContest.proc_ins_id}&type=ds&status=${gContest.auditCode}" target="_blank">跟踪</a>
											</c:if>
										</c:otherwise>
									</c:choose>

								</td>
							</tr>
						</c:forEach>
                    </tbody>
                </table>
					${page.footer}
				</div>
        </form>
</body>
</html>