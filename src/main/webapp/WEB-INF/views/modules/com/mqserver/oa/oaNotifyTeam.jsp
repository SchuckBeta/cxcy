<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团建通知</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			var teamId="${oaNotify.content}";
			//执行同意团建
			$("#btn1").click(function(){
				$("#sms").val("0");  //同意加入
				$("#inputForm").submit();
			})
			$("#btn2").click(function(){
				$("#sms").val("3");  //不同意加入
				$("#inputForm").submit();
			})
			$("#btn3").click(function(){
				$("#sms").val("4");  //忽略
				$("#inputForm").submit();
			})
		});


	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaNotify/">通知列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaNotify/form?id=${oaNotify.id}">通知<shiro:hasPermission name="oa:oaNotify:edit">${oaNotify.status eq '1' ? '查看' : not empty oaNotify.id ? '修改' : '添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaNotify:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="teamUserRelation" action="${ctx}/team/teamUserRelation/changeState" method="post" class="form-horizontal">

		<input type="hidden" id="teamId" name="teamId" value="${oaNotify.content}"/>
		<input type="hidden" id="state" name="state" value=""/>
		<sys:message content="${message}"/>

		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
			${oaNotify.title}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">团队组建人：</label>
			<div class="controls">
					${team.createBy.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目名称：</label>
			<div class="controls">
		           ${team.projectName}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目简介：</label>
			<div class="controls">
					${team.projectIntroduction}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">成员人数：</label>
			<div class="controls">
				${team.memberNum}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">导师人数：</label>
			<div class="controls">
				${team.masterNum}
			</div>
		</div>



		<div class="form-actions">
			<c:if test="${state==2}">
				<input id="btn1" class="btn btn-primary" type="button" value="同 意"/>&nbsp;
				<input id="btn2" class="btn btn-primary" type="button" value="不同意"/>&nbsp;
				<input id="btn3" class="btn btn-primary" type="button" value="忽 略"/>&nbsp;
			</c:if>
			<c:if test="${state==0}">
					您已经同意加入
			</c:if>
			<c:if test="${state==3}">
				    您已经不同意加入
			</c:if>
			<c:if test="${state==4}">
				您已经忽略
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>