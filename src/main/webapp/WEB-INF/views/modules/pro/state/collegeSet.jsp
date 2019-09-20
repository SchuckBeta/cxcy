<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>院级立项审核</title>
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
<script type="text/javascript">
	var validate;
	$(document).ready(function() {
		validate=$("#inputForm").validate({
			rules:{
				comment:{
					maxlength:300
				}
			},
			messages:{
				comment:{
					maxlength:"最多输入{0}个字"
				}
			},
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
	function submitSet(){
		if(validate.form()){
			$("#btnSumbit").prop('disabled', true);
			$("#inputForm").submit();
		}
	}
</script>
<body>
<div class="mybreadcrumbs"><span>立项审核</span></div>
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
				<h3>审核建议及意见</h3><span class="yw-line"></span>
			</div>
			<div style="padding:10px" id="lastCheckToggle_wrap">
				<c:if test="${standardList!=null&&fn:length(standardList)>0}">
					<div class="toggle_wrap" style="padding-left: 0" id="standardToggle_wrap">
					<table class="table  table-hover table-condensed table-bordered">
						<thead>
						<tr>
							<th width="100">检查要点</th>
							<th >审核元素</th>
							<th width="40">分值</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${standardList}" var="item" varStatus="status">
							<tr>
								<td>
										${item.checkPoint}
									<input type="hidden" name="auditStandardDetailInsList[${status.index}].checkPoint" value="${item.checkPoint}"/>
									<input type="hidden" name="auditStandardDetailInsList[${status.index}].sort" value="${item.sort}"/>
								</td>
								<td>
										${item.checkElement}
									<input type="hidden" name="auditStandardDetailInsList[${status.index}].checkElement" value="${item.checkElement}"/>
								</td>
								<td>
										${item.viewScore}
									<input type="hidden" name="auditStandardDetailInsList[${status.index}].viewScore" value="${item.viewScore}"/>
								</td>
							</tr>
						</c:forEach>

						</tbody>
					</table>
				</div>
				</c:if>

				<div class="form-horizontal row" novalidate>
					<div class="form-group col-sm-6 col-md-6 col-lg-6">
						<label class="col-xs-4 " style="width: 150px">评定项目为：</label>
						<form:select path="level" class="input-xlarge required">
							<form:option value="" label="请选择"/>
							<form:option value="4" label="C级"/>
							<form:option value="2" label="提交为校级项目"/>
							<form:option value="5" label="不合格"/>
						</form:select>
					</div>
				</div>


				<div class="form-horizontal row" novalidate>
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<label class="col-xs-2 " style="width: 150px">建议及意见：</label>
						<textarea  name="comment" maxlength="300" class="col-xs-10"   placeholder="请给予您的意见和建议" ></textarea>
					</div>
				</div>

				<div class="row" style="text-align: center; margin-bottom: 60px;">
					<button type="button" class="btn btn-sm btn-primary" id="btnSumbit" onclick="submitSet();" style="margin-right: 50px;">提交</button>
					<%--<a href="javascript:;" class="btn btn-sm btn-primary btn-submit" role="btn" onclick="submit();">提交</a>--%>
					<a href="javascript:;" class="btn btn-sm btn-primary" role="btn" onclick="history.go(-1)">返回</a>
				</div>

			</div>
		</section>
	</div>
</form:form>
</body>

</html>
