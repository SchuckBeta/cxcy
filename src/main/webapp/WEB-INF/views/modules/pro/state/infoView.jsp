<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>查看审核记录</title>
	    <script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/gcProject/GC_check_new.css">
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxCommon}/common-js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctxJs}/gcProject/project_check.js"></script>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">

	<link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=1111">
	<script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>

<body>
<div class="mybreadcrumbs"><span>查看审核记录</span></div>
<form:form id="inputForm" modelAttribute="projectDeclare" action="${ctx}/state/collegeSetSave" method="post" >
	<form:hidden path="id"/>
	<form:hidden path="act.taskId"/>
	<form:hidden path="act.taskName"/>
	<form:hidden path="act.taskDefKey"/>
	<form:hidden path="act.procInsId"/>
	<form:hidden path="act.procDefId"/>
	<div class="container-fluid content-wrap">
		<!--项目基本信息 -->
		<c:import url="${ctx}/projectBase/baseInfo" >
			<c:param name="id" value="${projectDeclare.id}" />
		</c:import>

			<section class="row">
                    <div class="prj_common_info">
                        <h3>审核记录</h3><span class="yw-line"></span>
                        <a href="javascript:;" id="checkRecord" data-flag="true"><span class="icon-double-angle-up"></span></a>
                    </div>
                    <div style="padding:10px" class="toggle_wrap" id="checkRecord_wrap">
                        <table class="table table-hover table-bordered">
                            <thead class="prj_table_head">
                            <th colspan="4" style="text-align: center;">立项审核记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>审核人</th>
                            <th>项目评级</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
							<c:forEach items="${infos1}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${fns:getDictLabel(info.grade, "project_degree", info.grade)}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								    </td>
								</tr>
							</c:forEach>
                            </tbody>

						<c:if test="${show2==1}">
							<thead class="th_align-center">
							<th colspan="4" style="text-align: center;">中期检查评分记录</th>
							</thead>
							<thead class="th_align-center">
							<th>评分专家</th>
							<th>评分</th>
							<th>建议及意见</th>
							<th>审核时间</th>
							</thead>
							<tbody>
							<c:forEach items="${infos2}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${info.score}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach >
							<c:if test="${showMiddleScore==1}">
								<tr>
									<td class="prj_range" align="center">平均分</td>
									<td colspan="3" class="result_score">
										<c:if test="${projectDeclare.midScore>0}">
											${projectDeclare.midScore}
										</c:if>
									</td>
								</tr>
							</c:if>
							</tbody>
						</c:if>

						<c:if test="${show3==1}">
							<thead class="th_align-center">
							<th colspan="4" style="text-align: center;">中期检查结果记录</th>
							</thead>
							<thead class="th_align-center">
							<th>审核人</th>
							<th>中期结果</th>
							<th>建议及意见</th>
							<th>审核时间</th>
							</thead>
							<c:forEach items="${infos3}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${fns:getDictLabel(info.grade, "project_result", info.grade)}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach>
						</c:if>

						<c:if test="${show4==1}">
							<thead class="prj_table_head">
							<th colspan="4" style="text-align: center;">结项评分记录</th>
							</thead>
							<thead class="th_align-center">
							<th>评分专家</th>
							<th>评分</th>
							<th>建议及意见</th>
							<th>审核时间</th>
							</thead>
							<tbody>
							<c:forEach items="${infos4}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${info.score}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach >
							<c:if test="${showFinalScore==1}">
								<tr>
									<td class="prj_range">平均分</td>
									<td colspan="3" class="result_score">
										<c:if test="${projectDeclare.finalScore>0}">
											${projectDeclare.finalScore}
										</c:if>
									</td>
								</tr>
							</c:if>
							</tbody>
						</c:if>

						<c:if test="${show5==1}">
							<thead class="prj_table_head">
							<th colspan="4" style="text-align: center;">答辩分记录</th>
							</thead>
							<thead class="th_align-center">
							<th>审核人</th>
							<th>评分</th>
							<th>建议及意见</th>
							<th>审核时间</th>
							</thead>
							<tbody>
							<c:forEach items="${infos5}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${info.score}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach >
							</tbody>
						</c:if>

						<c:if test="${show6==1}">
							<thead class="prj_table_head">

							<th colspan="4" style="text-align: center;">结果评定记录</th>
							</thead>
							<thead class="th_align-center">
							<th>审核人</th>
							<th>结果</th>
							<th>建议及意见</th>
							<th>审核时间</th>
							</thead>
							<tbody>
							<c:forEach items="${infos6}" var="info">
								<tr>
									<td align="center">${fns:getUserById(info.createBy.id).name}</td>
									<td align="center">${fns:getDictLabel(info.grade, "project_result", info.grade)}</td>
									<td>${info.suggest}</td>
									<td align="center">
										<fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</c:if>

                        </table>
                    </div>
                </section>

		<section class="row">

				<div class="row" style="text-align: center;">
					<c:if test="${showMiddleReport==1}">
						<a href="${ctx}/project/proMid/view?id=${proMidId}" target="_blank" class="btn btn-sm btn-primary" role="btn" >中期检查报告</a>
					</c:if>
					<c:if test="${showCloseReport==1}">
						<a href="${ctx}/project/projectClose/view?id=${proCloseId}" target="_blank" class="btn btn-sm btn-primary" role="btn" >结项报告</a>
					</c:if>
					<a href="javascript:;" class="btn btn-sm btn-primary" role="btn" onclick="history.go(-1)">返回</a>
				</div>

		</section>
	</div>
</form:form>
</body>

</html>
