<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
	<title>项目周报</title>
	<meta name="decorator" content="cyjd-site-default"/>
	<%--<link rel="stylesheet" type="text/css" href="${ctxCss}/projectForm1.css"/>--%>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
	<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
	<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
	<script src="${ctxStatic}/fullcalendar/moment.min.js"></script>
</head>
<body>
<div class="container container-ct">
	<h3 class="text-center" style="margin-bottom: 1em; margin-top: 0">${projectDeclare.name}</h3>
	<div class="weekly-wrap">
		<div class="form-horizontal">
			<div class="row">
				<div class="col-xs-6">
					项目编号：${projectDeclare.number}
				</div>
				<div class="col-xs-6 text-right">
					创建时间：<fmt:formatDate value="${projectDeclare.createDate}"/>
				</div>
			</div>
			<h4 class="titlebar titlebar-custom">项目周报</h4>
			<div class="section-user">
				<div class="row">
					<div class="col-xs-5 col-xs-offset-1 control-group-user">
						<span class="control-span-user text-right">汇报人：</span>
						<div class="controls-user">
							<p class="controls-user-static">${cuser.name}</p>
						</div>
					</div>
					<div class="col-xs-5 control-group-user">
						<span class="control-span-user text-right">职责：</span>
						<div class="controls-user">
							<p class="controls-user-static">${duty}</p>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-5 col-xs-offset-1 control-group-user">
						<span class="control-span-user text-right">项目组成员：</span>
						<div class="controls-user">
							<p class="controls-user-static">${teamList}</p>
						</div>
					</div>
					<div class="col-xs-5 control-group-user">
						<span class="control-span-user text-right">项目导师：</span>
						<div class="controls-user">
							<p class="controls-user-static">${teacher}</p>
						</div>
					</div>
				</div>
			</div>
			<div class="edit-bar edit-bar-sm clearfix">
				<div class="edit-bar-left">
					<span>上周任务小结</span>
					<i class="line"></i>
				</div>
				<div class="edit-bar-right">
					<p class="time-section gray-color">时间：<span><fmt:formatDate
							value="${vo.lastpw.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
							value="${vo.lastpw.endDate}" pattern="yyyy-MM-dd"/></span></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">上周任务计划：</label>
				<div class="col-xs-10">
					<p class="form-control-static">
						<c:if test="${empty vo.lastpw.plan}"><span class="gray-color">上周无任务计划</span></c:if>
						${vo.lastpw.plan}
					</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">完成情况：</label>
				<div class="col-xs-10">
					<p class="form-control-static">${vo.lastpw.achieved}</p>
				</div>
			</div>
			<div class="form-group" style="margin-bottom: 30px;">
				<label class="control-label col-xs-2">存在问题：</label>
				<div class="col-xs-10">
					<p class="form-control-static">${vo.lastpw.problem}</p>
				</div>
			</div>
			<div class="edit-bar edit-bar-sm clearfix">
				<div class="edit-bar-left">
					<span>本周任务计划</span>
					<i class="line"></i>
				</div>
				<div class="edit-bar-right">
					<p class="time-section gray-color">时间：<span><fmt:formatDate
							value="${vo.projectWeekly.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
							value="${vo.projectWeekly.endDate}" pattern="yyyy-MM-dd"/></span></p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">计划内容：</label>
				<div class="col-xs-10">
					<p class="form-control-static">${vo.lastpw.plan}</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2">附件：</label>
				<div class="col-xs-10">
					<sys:frontFileUpload className="accessories-h34" fileitems="${vo.fileInfo}"
										 filepath="weekly" readonly="true"></sys:frontFileUpload>
					<c:if test="${empty vo.fileInfo}"><p class="form-control-static gray-color">无附件</p></c:if>
				</div>
			</div>
			<div class="edit-bar edit-bar-sm clearfix">
				<div class="edit-bar-left">
					<span>导师意见及建议</span>
					<i class="line"></i>
				</div>
				<div class="edit-bar-right">
					<p class="time-section gray-color">时间：<span><fmt:formatDate
							value="${vo.projectWeekly.suggestDate}" pattern="yyyy-MM-dd"/></span></p>
				</div>
			</div>

			<div class="form-group">
				<label class="control-label col-xs-2">意见及建议：</label>
				<div class="col-xs-10">
					<p class="form-control-static">${vo.projectWeekly.suggest}</p>
				</div>
			</div>
		</div>
	</div>
	<div class="text-center">
		<button type="button" class="btn btn-default" onclick="history.go(-1);">返回</button>
	</div>
</div>
</body>
</html>