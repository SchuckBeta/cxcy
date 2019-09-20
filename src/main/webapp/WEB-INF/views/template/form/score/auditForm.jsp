<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<html>
<head>
	<title>立项审核</title>
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="${ctxCss}/gcProject/GC_check_new.css">
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxCommon}/common-js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctxJs}/gcProject/project_check.js"></script>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
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
	function submit(){
		if(validate.form()){
			$("#inputForm").submit();
		}
	}
</script>
<body>

<form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelAudit" method="post" >
	<form:hidden path="id"/>
	<form:hidden path="act.taskId"/>
	<form:hidden path="act.taskName"/>
	<form:hidden path="act.taskDefKey"/>
	<form:hidden path="act.procInsId"/>
	<form:hidden path="act.procDefId"/>
	<div class="container-fluid content-wrap">
        <div class="row info-panel">
            <div class="panel-body">
                <div class="col-md-6 col-lg-6 col-sm-6 w142">
                    <p><strong>申报人：</strong>${proModel.declareId}</p>
                </div>
            </div>
        </div>
        <div class="row info-panel">
            <h3><a href="javascript:;" class="panel-toggle"><i class="icon-double-angle-up" aria-hidden="true"></i></a><span>项目基本信息</span></h3>
            <div class="panel-body">
                <div class="col-md-4 col-lg-4">
                    <p><strong>参赛项目名称：</strong>${proModel.pName}</p>
                </div>
            </div>
        </div>

		<section class="row">
			<div class="prj_common_info">
				<h3>审核建议及意见</h3><span class="yw-line"></span>
			</div>
			<div style="padding:10px" id="lastCheckToggle_wrap">

				<div class="form-horizontal row" novalidate>
					<div class="form-group col-sm-6 col-md-6 col-lg-6">
						<label class="col-xs-4 ">审核结果为：</label>
						<form:select path="grade" class="input-xlarge required">
                            <form:option value="0" label="不合格"/>
							<form:option value="1" label="合格"/>
						</form:select>
					</div>
				</div>
				<div class="form-horizontal row" novalidate>
					<div class="form-group col-sm-12 col-md-12 col-lg-12">
						<label class="col-xs-2 ">建议及意见：</label>
						<textarea  name="source" maxlength="300" class="col-xs-10" style="margin-left: -4px;"  placeholder="请给予您的意见和建议" ></textarea>
					</div>
				</div>

				<div class="row" style="text-align: center;">
					<a href="javascript:;" class="btn btn-sm btn-primary btn-submit" role="btn" onclick="submit();">提交</a>
					<a href="javascript:;" class="btn btn-sm btn-primary" role="btn" onclick="history.go(-1)">返回</a>
				</div>

			</div>
		</section>
	</div>
</form:form>
</body>

</html>
