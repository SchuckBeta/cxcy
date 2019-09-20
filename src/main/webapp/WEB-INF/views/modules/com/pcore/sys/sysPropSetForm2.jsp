<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统功能管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
</head>
<body>
	<div class="container-fluid">
	<hr>
	${sysProp}
	<hr>
		<div class="edit-bar clearfix">
			<div class="edit-bar-left">
				<span>${sysProp.title }</span>
				<i class="line weight-line"></i>
			</div>
		</div>

		<c:if test="${not empty sysProp.items }">
			<c:forEach var="pitem" items="${sysProp.items }">
				<form class="form-horizontal form-horizontal-rule" style="padding-left: 90px;">
					<div class="control-group warning-box">
						<div class="controls-checkbox warning-content">
							<div class="control-group warning-grounp-width">
								<div class="controls warning-controls">
									<input type="hidden" name="items.id" value="${pitem.id }">
									<label class="checkbox inline warning-align">
										<input class="warning-align" type="checkbox">

									</label>
									<input type="text" maxlength="4" class="required input-mini number digits" value="${pitem.params[0].value }"/>
									<label class="checkbox inline warning-align" style="margin-left:-20px;">天，系统预警</label>
								</div>
							</div>

						</div>
						<span class="warning-end"><i>*</i>${pitem.name }</span>
					</div>
					<div class="form-actions" style="padding-left: 390px;margin-left: -90px;">
						<input class="btn btn-primary" type="submit" value="保 存"/>
					</div>
				</form>
			</c:forEach>
		</c:if>



		<form class="form-horizontal form-horizontal-rule" style="padding-left: 90px;">
			<div class="control-group warning-box">
				<div class="controls-checkbox warning-content">
					<div class="control-group warning-grounp-width">
						<div class="controls warning-controls">
							<input type="hidden" value="">
							<label class="checkbox inline warning-align">
								<input class="warning-align" type="checkbox">
								距离入驻有效期还剩
							</label>
							<input type="text" maxlength="4" class="required input-mini number digits"/>
							<label class="checkbox inline warning-align" style="margin-left:-20px;">天，系统预警</label>
						</div>
					</div>

				</div>
				<span class="warning-end"><i>*</i>入驻到期预警</span>
			</div>

			<div class="control-group warning-box">
				<div class="controls-checkbox warning-content">
					<div class="control-group warning-grounp-width">
						<div class="controls warning-controls">
							<input type="hidden" value="">
							<label class="checkbox inline warning-align">
								<input class="warning-align" type="checkbox">
								连续
							</label>
							<input type="text" maxlength="4" class="required input-mini number digits"/>
							<label class="checkbox inline warning-align" style="margin-left:-20px;">天未进入基地，系统预警</label>
						</div>
					</div>

				</div>
				<span class="warning-end"><i>*</i>未进入基地预警</span>
			</div>

			<div class="control-group warning-box">
				<div class="controls-checkbox warning-content">
					<div class="control-group warning-grounp-width">
						<div class="controls warning-controls">
							<input type="hidden" value="">
							<label class="checkbox inline warning-align">
								<input class="warning-align" type="checkbox">
								已进入基地，截止当天
							</label>
							<input type="text" maxlength="4" class="required input-mini number digits"/>
							<label class="checkbox inline warning-align" style="margin-left:-20px;">未出基地，系统预警</label>
						</div>
					</div>

				</div>
				<span class="warning-end"><i>*</i>未出基地预警</span>
			</div>


			<div class="form-actions" style="padding-left: 390px;margin-left: -90px;">
				<input class="btn btn-primary" type="submit" value="保 存"/>
			</div>

		</form>
	</div>
</body>
</html>