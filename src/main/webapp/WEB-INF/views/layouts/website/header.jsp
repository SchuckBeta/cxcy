<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.oseasy.com.pcore.modules.sys.utils.UserUtils" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<% request.setAttribute("user", UserUtils.getUser()); %>



<!--顶部header公用部分-->
<div class="header">
	<div class="mid">
		<!--logo图-->
		<div class="logo-box">
			<img src="${ctxImg}/main-brandx76.png" width="76" height="76" />
			<img src="${ctxImg}/s-brandx159.png" width="159" height="76" />
		</div>
		<!--导航部分-->
		<ul style="margin-bottom: 0px;" id="header_info">
			<a href="#" class="indexPage">
				<li class="shouye"><a href="/f"><span>首页</span></a>
					<div class="secondlever"></div></li>
			</a>
			<a href="#">
				<li><a href="${ctxFront}/page-innovation"><span>双创项目</span></a>
					<div class="secondlever">
						<p>
							<a href="${ctxFront}/html-2017gcsb" style="margin-left: -43px;">●2017年国创项目申报通知</a> <a
								href="${ctxFront}/project/projectDeclare/form">●国创项目申报</a> <a
								href="${ctxFront}/page-projectshow">●优秀项目展示</a> <a
								href="${ctxFront}/project/projectDeclare/curProject">●我的项目</a>
						</p>
					</div></li>
			</a>
			<a href="#">
				<li><a href="${ctxFront}/page-competition"><span>双创大赛</span></a>
					<div class="secondlever">
						<p>
							<a href="${ctxFront}/html-sctzsj" style="margin-left: 255px">●第三届互联网+大赛报名通知</a>
							<a href="${ctxFront}/gcontest/gContest/form">●互联网+大赛报名</a> <a
								href="${ctxFront}/html-dsrd">●大赛获奖作品展示</a> <a
								href="${ctxFront}/gcontest/gContest/list">●我的大赛</a>
						</p>
					</div></li>
			</a>
			<a href="#">
				<li><a href="${ctxFront}/page-achievements"><span>科研成果</span></a>
					<div class="secondlever">
						<p>
							<a href="${ctxFront}/html-kycg" style="margin-left: 8px">●优秀科研成果展示</a>
						</p>
					</div></li>
			</a>
			<a href="#">
				<li><a href="${ctxFront}/page-teacherGrace"><span>导师风采</span></a>
					<div class="secondlever">
						<p>
							<a href="${ctxFront}/html-xyds" style="margin-left: 287px">●校内导师</a> <a
								href="${ctxFront}/html-teacherGrace">●企业导师</a>
						</p>
					</div></li>
			</a>
			<a href="#">
				<li><a href="${ctxFront}/page-SCschool"><span>人才库</span></a>
					<div class="secondlever">
						<p>
							<a href="${ctxFront}/sys/frontStudentExpansion" style="margin-left: 622px">●学生库</a>
							<a href="${ctxFront}/sys/frontTeacherExpansion">●导师资源</a> <a
								href="${ctxFront}/team/indexMyTeamList">●团队建设</a>
							<!-- <a href="${ctxFront}/sys/frontStudentExpansion/findUserInfoById">●个人信息</a>
								<a href="${ctxFront}/sys/user/indexMyNoticeList">●我的消息</a> -->
						</p>
					</div></li>
			</a>
			<a href="#">
				<li class="scxy"><a href="${ctxFront}/page-SCschool"><span>双创学院</span></a>
					<div class="secondlever"></div></li>
			</a>
		</ul>

		<!--用户名-->
		<c:if test="${loginPage==null}">
			<div class="user">
				<form id="userForm" class="form-signin" action="${ctxFront}/logout" method="post">
					<%--<div class="enter" style="position: relative;top:30px;">--%>
					<c:if test="${user.id!=null}">
						<span onclick="javascrip:$('#userForm').submit();"><i class="icon-signout"></i>退出</span>
						<a href="${ctxFront}/sys/user/indexMyNoticeList"><i class="icon-bell"></i>消息</a>
						<a href="${ctxFront}/sys/frontStudentExpansion/findUserInfoById"><i class="icon-user"></i>${user.name}</a>
						<%-- <c:if test="${user.userType=='1'}">
						<small>学生用户：</small>
						</c:if>
						<c:if test="${user.userType=='2'}">
						<small>导师用户：</small>
						</c:if> --%>

					</c:if>
					<%--</div>--%>
					<%--<div class="login" style="height: 55px;">--%>

					<c:if test="${user.id==null}">
						<a href="${ctxFront}/toLogin">登录</a>
						<a href="${fns:getSysAdminIp()}">管理员登录</a>
						<a href="${ctxFront}/toRegister">注册</a>
					</c:if>
					<%--</div>--%>
					<input type="hidden" name="fromFront" value="1" />
				</form>
			</div>
		</c:if>
	</div>
	<div class="bg-color" style="height: 50px; width: 100%; background: rgba(232, 54, 44, 1); z-index: 999; visibility: hidden"></div>
</div>


<div id="dialog-message" title="信息" style="display: none;">
	<p id="dialog-content"></p>
</div>
<!-- <style>
#header_info .indexPage:hover .bg-color{
	visibility: hidden;
}
</style> -->
<script>
	$(function() {
		$("#header_info").hover(function() {
			$(".bg-color").css("visibility", "visible");
		}, function() {
			$(".bg-color").css("visibility", "hidden");
		});
	});
</script>