<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/backtable.jsp" %>
<html>
<head>
	<title>导师画像</title>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
	<link rel="stylesheet" href="${ctxCss}/tutor.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/tablepage.css" />
   <link rel="stylesheet" href="${ctxCss}/font-awesome.min.css">
	<script src="${ctxJs}/goback.js" type="text/javascript" charset="UTF-8"></script>
    <script src="${ctxJs}/scxiangmu.js"></script>
    <script type="text/javascript">
		$(document).ready(function() {
			$("#ps").val($("#pageSize").val());

			//增加学院下拉框change事件
			$("#collegeId").change(function(){
				var parentId=$(this).val();
				//根据当前学院id 更改
				$("#professionalSelect").empty();
				$("#professionalSelect").append('<option value="">所有专业</option>');
				$.ajax({
					type: "post",
					url: "/a/sys/office/findProfessionals",
					data: {"parentId":parentId},
					async : true,
					success: function (data) {
						$.each(data,function(i,val){
							if(val.id=="${studentExpansion.user.professional}"){
								$("#professionalSelect").append('<option selected="selected" value="'+val.id+'">'+val.name+'</option>')
							}else{
								$("#professionalSelect").append('<option value="'+val.id+'">'+val.name+'</option>')
							}

						})
					}
				});

			})
			$("#collegeId").trigger('change');
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
	<div class="container teacher" >
		<div class="row"><!-- 面包屑 开始 -->
			<div class="col-md-12" style="padding:0px;">
				<div class="mybreadcrumbs" style="margin: 0px 0px 15px 0px;"><span>导师画像</span></div>
			</div>
		</div><!-- 面包屑 结束 -->
		<div class="row"><!-- 表单+按钮 开始 -->
			<div class="row">
				<form:form id="searchForm" modelAttribute="backTeacherExpansion" action="${ctx}/analysis/teacherAnalysis/toPage" method="post" class="form-inline" style="border:none;">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

					<div class="col-md-3" style="padding-left: 0px;">
						<label class="control-label"><span>*</span>学院：</label>
						<form:select path="user.office.id" class="form-control" style="max-width:70%;" id="collegeId">
							<form:option value="" label="所有学院"/>
							<form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>

					<div class="col-md-3">
						<label class="control-label"><span>*</span>专业：</label>
						<form:select path="user.professional" class="form-control" style="max-width:70%;" id="professionalSelect">
							<form:option value="" label="所有专业"/>
						</form:select>
					</div>
					<div class="col-xs-4" style="max-width:26%;"><!-- 关键词 开始 -->
						<div class="form-group">
						    <label class="control-label"><span>*</span>关键词：</label>
						    <form:input path="user.name" class="form-control" style="width:65%;height:33px;" />
						</div>
					</div><!-- 关键词 结束 -->
					<!-- <a href="#" class="btn">查询</a> -->
					<input id="btnSubmit" class="btn btn-primary pull-right" type="submit" value="查询" style="background-color: #e9442d;
							border-color: #e9442d;"/>
					<%--<a href="#" class="btn">导出导师画像</a>--%>
				</form:form>
			</div>
		</div><!-- 表单+按钮 结束 -->

		<c:forEach items="${page.list}" var="teacherExpansion">
		<div class="information" ><!-- 导师 资料 开始 -->
			<div class="row">
				<div class="col-md-7"><!-- 左侧文字 开始 -->
					<div class="teacher-data">
						<ul>
							<li><b>年龄：</b>
								<c:if test="${teacherExpansion.user.birthday!=null && teacherExpansion.user.birthday!=''}">
										${fns:getAge(teacherExpansion.user.birthday)}
								</c:if>
							</li>
							<li><b>性别：</b>
							<c:choose>
								<c:when test="${teacherExpansion.user.sex!=null && teacherExpansion.user.sex!='0'}">
									男
								</c:when>
								<c:otherwise>
									女
								</c:otherwise>
							</c:choose>
							</li>
							<li><b>学院：</b>
							<c:if test="${teacherExpansion.user!=null&&teacherExpansion.user.office!=null }">
								${teacherExpansion.user.office.name}
							</c:if>
							</li>
						</ul>
						<div style="clear: both;">
							<ul>
								<li><b>专业：</b>

								<c:if test="${teacherExpansion.user!=null&&teacherExpansion.user.professional!=null &&teacherExpansion.user.professional!=''}">
									${fns:getProfessional(teacherExpansion.user.professional)}
								</c:if>
								</li>
								<li><b>学历：</b>${fns:getDictLabel(teacherExpansion.user.education, "enducation_level", "")}</li>
								<li><b>职称：</b>
								<c:set var="curTechnicalTitle" value="${fns:getDictLabel(teacherExpansion.technicalTitle, 'postTitle_type', '') }"></c:set>
								${(empty curTechnicalTitle) ? teacherExpansion.technicalTitle : curTechnicalTitle}
								</li>
							</ul>
							<p style="clear: both;"><b>主要经历：</b>${teacherExpansion.mainExp}</p>
							<div class="row tea-row">
								<div class="col-md-2 tea-information-th"><b>指导项目经历：</b></div>
								<div class="col-md-10 tea-information-exp">
									<ul>
										<li>15年/基于互联网的教学反馈系统设计与实现<span class="pull-right">A级</span></li>
										<li>16年/基于百度地图的武汉老地名查询系统的开发<span class="pull-right">B级</span></li>
									</ul>
								</div>
							</div>
							<div class="row tea-row">
								<div class="col-md-2 tea-information-th"><b>学术科研成果：</b></div>
								<div class="col-md-10 tea-information-exp">
									<ul>
										<li>15年/基于互联网的教学反馈系统设计与实现<span class="pull-right">A级</span></li>
										<li>16年/基于百度地图的武汉老地名查询系统的开发<span class="pull-right">B级</span></li>
									</ul>
								</div>
							</div>
							<div class="row tea-row">
								<div class="col-md-2 tea-information-th"><b>主要研究方向：</b></div>
								<div class="col-md-10 tea-information-exp">
									<ul>
										<li>15年/基于互联网的教学反馈系统设计与实现<span class="pull-right">A级</span></li>
										<li>16年/基于百度地图的武汉老地名查询系统的开发<span class="pull-right">B级</span></li>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>

				<!-- 左侧文字 结束 -->
				<div class="col-md-5">
					<div class="row">
						<div class="col-md-5">
							<h3 style="text-align:right;font-weight:bold;">${teacherExpansion.user.name}</h3>
						</div>
						<div class="col-md-7">
							<c:choose>
								<c:when test="${teacherExpansion.user.photo!=null && teacherExpansion.user.photo!='' }">
									<img style="border-radius:100px;width:200px;margin-top:15px;" src="${fns:ftpImgUrl(teacherExpansion.user.photo)}">
								</c:when>
								<c:otherwise>
									<img style="border-radius:100px;width:200px;margin-top:15px;" src="${ctxImages}/daos.png">
								</c:otherwise>
							</c:choose>


							<div class="tea-label">
								<ul style="margin-left:-40px;">
									<c:if test="${teacherExpansion.user.domainlt!=null && teacherExpansion.user.domainlt!=''}">
									${fns:getDomainlt(teacherExpansion.user.domainlt,'li')}
									</c:if>

									<!-- <li>大数据分析</li>
									<li>大数据分析</li>
									<li>大数据分析</li>
									<li>大数据分析</li>
									<li>大数据分析</li>
									<li>大数据分析</li> -->
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div><!-- 导师 资料 结束 -->
		</c:forEach>
		${page.footer}
	</div>

</body>

</html>