<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="decorator" content="cyjd-site-default"/>
	<title>${fns:getConfig('frontTitle')}</title>
</head>
<body>
	<%--<div class="mybreadcrumbs">--%>
		<%--<span>预约详情</span>--%>
	<%--</div>--%>
	<div class="container container-ct">
		<ol class="breadcrumb" style="margin-top: 0">
		  	<li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
		  	<li class="active">创业基地</li>
			<li><a href="${ctxFront}/pw/pwAppointment/myList">预约查询</a></li>
			<li class="active">预约详情</li>
	  	</ol>
	    <div class="row-apply">
			<h4 class="titlebar">预约详情</h4>
			<div class="form-horizontal form-horizontal-apply">
				<div class="row row-user-info">
					<div class="col-xs-4">
						<label class="label-static">申请人：</label>
						<p class="form-control-static">${pwAppointment.user.name}</p>
					</div>
					<div class="col-xs-4">
						<label class="label-static">房间名：</label>
						<p class="form-control-static">${pwAppointment.pwRoom.name}</p>
					</div>
					<div class="col-xs-4">
						<label class="label-static">房间类型：</label>
						<p class="form-control-static">${fns:getDictLabel(pwAppointment.pwRoom.type,"pw_room_type" , "")}</p>
					</div>
				</div>
				<div class="row row-user-info">
					<div class="col-xs-4">
						<label class="label-static">可容纳人数：</label>
						<p class="form-control-static">${pwAppointment.pwRoom.num}</p>
					</div>

					<div class="col-xs-4">
						<label class="label-static">参与人数：</label>
						<p class="form-control-static">${pwAppointment.personNum}</p>
					</div>
					<div class="col-xs-4">
						<label class="label-static">预约状态：</label>
						<p class="form-control-static">${fns:getDictLabel(pwAppointment.status,"pw_appointment_status" , "")}</p>
					</div>
				</div>
				<div class="row row-user-info">
					<div class="col-xs-4">
						<label class="label-static">预约日期：</label>
						<p class="form-control-static"><fmt:formatDate value="${pwAppointment.startDate}" pattern="yyyy-MM-dd"/></p>
					</div>
					<div class="col-xs-4">
						<label class="label-static">预约时间段：</label>
						<p class="form-control-static"><fmt:formatDate value="${pwAppointment.startDate}" pattern="HH:mm"/>-<fmt:formatDate
												value="${pwAppointment.endDate}" pattern="HH:mm"/></p>
					</div>

				</div>
				<div class="row row-user-info">
					<div class="col-xs-10">
						<label class="label-static">用途：</label>
						<p class="form-control-static">${pwAppointment.subject}</p>
					</div>
				</div>
			</div>
		</div>
		<div class="text-center" style="padding: 30px; text-align: center">
			<input class="btn btn-small btn-default" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>



	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>


	<%--<form class="form-horizontal">--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">申请人：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${pwAppointment.user.name}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">房间名：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${pwAppointment.pwRoom.name}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">房间类型：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${fns:getDictLabel(pwAppointment.pwRoom.type,"pw_room_type" , "")}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">可容纳人数：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${pwAppointment.pwRoom.num}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">用途：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${pwAppointment.subject}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">参与人数：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${pwAppointment.personNum}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">预约日期：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static"><fmt:formatDate value="${pwAppointment.startDate}" pattern="yyyy-MM-dd"/></p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">预约时间段：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static"><fmt:formatDate value="${pwAppointment.startDate}" pattern="HH:mm"/>-<fmt:formatDate--%>
						<%--value="${pwAppointment.endDate}" pattern="HH:mm"/></p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-group">--%>
			<%--<label class="col-sm-2 control-label">预约状态：</label>--%>
			<%--<div class="col-sm-10">--%>
				<%--<p class="form-control-static">${fns:getDictLabel(pwAppointment.status,"pw_appointment_status" , "")}</p>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="form-actions-cyjd text-center">--%>
			<%--<input class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>--%>
		<%--</div>--%>
	<%--</form>--%>
	<%--<div id="dialog-message" title="信息">--%>
		<%--<p id="dialog-content"></p>--%>
	<%--</div>--%>

	<script>

		$(function(){
//			$('.container').click(function(){
//					alert($(window).height());
//					alert($('.header').height());
//					alert($('#content').height());
//					alert($('.footerBox').height());
				var minHeight = $(window).height()-$('.header').height()-$('.footerBox').height();
//				alert(minHeight);
				var middleHeight = $('#content').height();
//				alert(middleHeight);
				if(middleHeight < minHeight){
					$('#content').height(minHeight);
				}
//				alert($('#content').height());

//			});
		});




	</script>





</body>
</html>