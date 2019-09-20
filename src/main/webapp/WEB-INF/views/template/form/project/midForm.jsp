<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中期检查报告</title>
	<meta name="decorator" content="site-decorator" />
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css" />
	<script type="text/javascript" src="${ctxCommon}/common-js/datepicker/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/projectForm.css" />
	<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<%--	<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>--%>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<style>
		.error-tips{
			padding-left:130px;
		}
		.error-tips span{
			display:inline-block;
			width:100px;
		}
		.error-tips .end-errorTips{
			margin-left: 20px;
		}
		label.error {
			background-position: 0 3px;
		}
		.prj-division{
			background-position: 0 3px;
			position: relative;
			left:-90px;
		}
		.str-tips,.end-tips{
			position: absolute;
			top:58px;
		}
		.str-tips label.error,.end-tips label.error{
			margin-left: 0px;
		}
		.str-tips{
			left: 43px;
		}
		.end-tips{
			left: 174px;
		}
		.prj-division-ch{
			left:-65px;
		}
		.programme{
			margin-left: -10px;
		}
		.pro-textarea{
			width: 100%;
			margin:0;
		}
		.footer-btn-wrap{
			text-align: center;
		}
	</style>

	<script type="text/javascript">

		var validate;
		var ratio = "";
		$(document).ready(function() {
			ratio = "${ratio}";
			if(ratio==""){
				$("#ratioSpan").remove();
				$('th.credit-ratio,td.credit-ratio').remove();
			}
			validate=$("#inputForm").validate({
				rules:{
					taskBeginDate:{
						required:true
					},
					taskEndDate:{
						required:true
					}
				},
				errorPlacement: function(error, element) {
				    if(element.attr("id") == "taskBeginDate"){
						$(".start-errorTips").html(error);
					}else if(element.attr("id") == "taskEndDate"){
						$(".end-errorTips").html(error);
					}else if(element.attr("name").indexOf("proSituationList")>=0){
						element.siblings('.prj-division').html(error)
					}else if(element.attr("name").indexOf("startDate")>=0){
						element.siblings('.str-tips').html(error);
					}else if(element.attr("name").indexOf("endDate")>=0){
						element.siblings('.end-tips').html(error);
					}else if(element.attr("name").indexOf("proProgresseList")>=0){
						element.siblings('.prj-division-ch').html(error);
					}else if(element.attr("name")=="programme"){
						element.siblings('.programme').html(error);
					}
					else {
						error.insertAfter(element);
					}
				}
			});
		});


		function submit(){
			//学分配比校验
		/*	if(!checkRatio()){
				showModalMessage(0, "学分配比不符合规则！",{
					"确定":function() {
						$( this ).dialog( "close" );
						$("input[name='teamUserRelationList[0].weightVal']" ).focus();
					}
				});
				return false;
			}*/
			if(validate.form()){
				var me = $("#submitBtn");
				if(me.data("data-clicked") === 1) {
					return;
				}
				$("#inputForm").submit();
				me.data("data-clicked", 1);
			}
		}

		//学分配比校验
		function checkRatio(){
			var result = true ;
			if(ratio!=""){
				var creditArr = [];
				$('.credit-ratio input.form-control').each(function(i,item){
					creditArr.push($(item).val());
				});

				creditArr.sort(function (a,b) {
					return b -a;
				});
				var ratioArr  = ratio.split(':').sort(function(a,b){
					return b - a;
				});

				if(creditArr.join(':') !== ratioArr.join(':')){
					result=false;
				}
			}
			return result;
		}


	</script>
</head>
<body>
	<form action="${ctxFront}/project/proMid/submitMid" id="inputForm" method="post">
	<div class="top-title">
		<h3>${proModel.name}</h3>
		<h4>中期检查报告</h4>
		<div class="top-bread">
			<div class="top-prj-num"><span>项目编号:</span>${proModel.number}</div>
			<div class="top-prj-num"><span>创建时间:</span><fmt:formatDate value="${proModel.createDate}" /></div>
			<a href="javascript:;" class="btn btn-sm btn-primary" onclick="history.go(-1);">返回</a>
		</div>
	</div>


		<div class="outer-wrap">
			<div class="container-fluid">
                <input type="hidden" id="projectId" name="projectId" value="${proModel.id}">
                <div class="row content-wrap">
                    <section class="row">
                        <div class="form-horizontal" novalidate>
                            <c:set var="leader" value="${fns:getUserById(proModel.leader)}" />
                            <div class="form-group col-sm-6 col-md-6 col-lg-6">
                                <label class="col-xs-3 ">项目负责人：</label>
                                <p class="col-xs-9">
                                    ${leader.name}
                                </p>
                            </div>
                            <div class="form-group col-sm-2 col-md-2 col-lg-2"></div>
                            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                                <label class="col-xs-5">专业年级：</label>
                                <p  class="col-xs-7">${fns:getOffice(leader.professional).name}<fmt:formatDate value="${student.enterDate}" pattern="yyyy"/></p>
                            </div>
                        </div>



                    </section>



                    <section class="row">
                        <div class="prj_common_info" style="height: 40px;line-height: 40px;">
                            <h4 class="sub-file" style="margin-top: 25px;">附件 </h4><span class="yw-line yw-line-fj"></span>
                            <div  class="upload-file upload-file-sm" id="upload">上传附件</div>
                            <input type="file"  style="display: none" id="fileToUpload" name="fileName"/>
                        </div>
                        <div class="textarea-wrap">
                            <sys:frontFileUpload fileitems="${fileListMap}" gnodeId="${gnodeId}" filepath="proMidForm" btnid="upload"></sys:frontFileUpload>
                        </div>
                    </section>



                    <div class="footer-btn-wrap">
                        <%--<a href="javascript:;" class="btn btn-sm btn-primary btn-save" onclick="submit()">保存</a>--%>
                        <a href="javascript:;" class="btn btn-sm btn-primary" onclick="submit()" id="submitBtn">提交</a>
                    </div>
                </div>
			</div>
		</div>
	</form>
<%--	<script src="${ctxJs}/gcProject/fileUpLoad.js"></script>--%>
</body>
</html>