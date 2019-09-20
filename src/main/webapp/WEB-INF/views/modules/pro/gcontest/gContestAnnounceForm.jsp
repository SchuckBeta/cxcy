 <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>大赛通告表管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/backtable.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css">
	
<style type="text/css">
ul li {
	list-style-type: none;
}

.form-actions {
	border-top: none !important;
}
.icon-remove-sign{
	color:red;!important;
}

#xm {
	width: 284px;
}

.Wdate {
	width: 270px
}

#upload {
	margin-left: 20px
}

#cke_content {
	width: 69% !important;
}

.control-group {
	border-bottom: none !important;
}

.tt {
	width: 284px !important;
	max-width: none !important;
	height:30px;
}
.biaoti{
	position:relative;
	height:41px;
}

.biaoti .file-title{
	position:absolute;
	left:0;
	top:0px;
	z-index:999;
	background:#fff;
}
.biaoti .yw-line{
	position:absolute;
	height:1px;
	background:#f3d5af;
	left:0px;
	top:10px;
	width:80%;
	
}
#btnSubmit{
  background:#e9442d!important;
}
</style>
<script type="text/javascript">
	$(document).ready(
		function() {
			//$("#name").focus();

			var operationType=$("#operationType").val();
			if(operationType!=null && operationType!=""){
				$("input").attr({readonly:"true"});
				$("#btnSubmit").hide();
				$(".content_panel span").hide();
				return false;
			}
			$("#content").blur(function(){
			//	var content = CKEDITOR.instances.getData();

			})
			var pass = false;
			$("#gContestName").blur(function(){
				var pattern = new RegExp("[~'!@#$%^&*()-+_=:]");
				var username=$("#gContestName").val();
				var reg = /\s/;
				if(username==" ") {
					$("#yazheng").text("*大赛名不能为空!").css('color','red');
					pass = false;
				}
				else if( /^\d.*$/.test( username ) ){
					$("#yazheng").text("*大赛名不能以数字开头!").css('color','red');
					pass = false;
				}
				else if(username.length<1 || username.length>18 ){
					$("#yazheng").text("*合法长度为1-18个字符!").css('color','red');
					pass = false;
				}else{
					$.ajax({
						url:"/a/gcontest/gContestAnnounce/validateName",
						data:{'name':username},
						type:"post",
						success:function(data){
							if(data==1){
								$("#yazheng").text("*大赛名已存在!").css('color','red');
								pass = false;
							}else{
								$("#yazheng").text("");
								pass = true;
							}
						}
					});
				 }
			});
			$("#inputForm").validate({
				submitHandler : function(form) {
					form.submit();
					$("#btnSubmit").attr("disabled", true);
				},
				errorContainer : "#Box",
				errorPlacement : function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")
							|| element.is(":radio")
							|| element.parent().is(".input-append")) {
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
</script>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>大赛通告表管理</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/gcontest/gContestAnnounce/">大赛列表</a></li>
			<li class="active">
				<c:choose>
					<c:when test="${operationType!=null}">
					<a href="${ctx}/gcontest/gContestAnnounce/form?id=${gContestAnnounce.id}&operationType=1">查看大赛
					</a>
					</c:when>
					<c:otherwise>
						<a href="${ctx}/gcontest/gContestAnnounce/form?id=${gContestAnnounce.id}">
						<shiro:hasPermission name="gcontest:gContestAnnounce:edit">${not empty gContestAnnounce.id?'修改':'创建'}</shiro:hasPermission>大赛
						</a>
					</c:otherwise>
				</c:choose>

			</li>
		</ul>
		<br />
		<form:form id="inputForm" modelAttribute="gContestAnnounce"
			action="${ctx}/gcontest/gContestAnnounce/save" method="post"
			class="form-horizontal">
			<form:hidden path="id" />
			<input type="hidden" value="${operationType }" id="operationType"/>
			<sys:message content="${message}" />
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>大赛名称：</label>
				<div class="controls">
					<form:input path="gName" id="gContestName" htmlEscape="false" maxlength="64"
						class="input-xlarge" />
					<span id="yazheng"></span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>大赛类型：</label>
				<div class="controls">
					<form:select path="type" class=" required tt">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('gContestAnnounce_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>竞赛级别：</label>
				<div class="controls">
					<form:select path="contestLevel" class=" required tt">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('0000000141')}"
									  itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>报名开始结束时间：</label>
				<div class="controls">
					<input name="applyStart" id="applyStart" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.applyStart}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({maxDate:'#F{$dp.$D(\'applyEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					<span>至</span> <input name="applyEnd" id="applyEnd" type="text"
						readonly="readonly" maxlength="20"
						class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.applyEnd}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'applyStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>院级赛事开始结束时间：</label>
				<div class="controls">
					<input name="collegeStart" id="collegeStart" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.collegeStart}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'applyEnd\')}',maxDate:'#F{$dp.$D(\'collegeEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					<span>至</span> <input name="collegeEnd" id="collegeEnd"
						type="text" readonly="readonly" maxlength="20"
						class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.collegeEnd}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'collegeStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>校赛开始 结束时间：</label>
				<div class="controls">
					<input name="schoolStart" id="schoolStart" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.schoolStart}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'collegeEnd\')}',maxDate:'#F{$dp.$D(\'schoolEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					<span>至</span> <input name="schoolEnd" id="schoolEnd" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.schoolEnd}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'schoolStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>省赛开始结束时间：</label>
				<div class="controls">
					<input name="provinceStart" id="provinceStart" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.provinceStart}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'schoolEnd\')}',maxDate:'#F{$dp.$D(\'provinceEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					<span>至</span> <input name="provinceEnd" id="provinceEnd"
						type="text" readonly="readonly" maxlength="20"
						class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.provinceEnd}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'provinceStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span class="help-inline"><font
						color="red">*&nbsp;</font> </span>国赛的开始结束时间：</label>
				<div class="controls">
					<input name="countryStart" id="countryStart" type="text"
						readonly="readonly" maxlength="20" class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.countryStart}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'provinceEnd\')}',maxDate:'#F{$dp.$D(\'countryEnd\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					<span>至</span> <input name="countryEnd" id="countryEnd"
						type="text" readonly="readonly" maxlength="20"
						class="input-medium Wdate required"
						value="<fmt:formatDate value="${gContestAnnounce.countryEnd}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({minDate:'#F{$dp.$D(\'countryStart\')}',dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">大赛说明：</label>
				<div class="controls">
					<form:textarea path="content" htmlEscape="false" rows="4"
						class="input-xxlarge " />
					<sys:ckeditor replace="content" uploadPath="/oa/content" />
				</div>
			</div>
			
			<div class="clear:both"></div>
			<div class="other" id="fujian" style="margin-left: 11%;margin-top:40px">
				<div class="biaoti">
					<span class="char2 file-title" style="width: 70px;">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件:</span>
					<p class="yw-line"></p>
					<c:if test="${operationType==null}">
					<div style="width: 102px; height: 30px; border-radius: 2px; position:absolute;right:165px;bottom:37px; background-color: #e9442d; float: right ;">
						<span style="font-size:12px;color:#cecece;position: absolute;right: 100px;width: 160px;top: 12px;">
							(注：文件大小不超过100m)</span>
						<a class="upload" id="upload" style="color: #fff; line-height: 30px">上传附件</a>
					</div>
					</c:if>
					<input type="file" style="display: none" id="fileToUpload" name="fileName" />
				</div>
				<ul id="file">
					<c:forEach items="${sysAttachments}" var="item" varStatus="status">
						<li>
							<p>
								<img src="${ctxImg}/filetype/${item.suffix }.png">
								<a href="javascript:void(0)" onclick="downfile('${item.url}','${item.name}');return false">
													${item.name}</a>
								<span class="del" onclick="delBackFile(this,'${item.id}','${item.url}');">
											<i class='icon-remove-sign'></i>
								</span>

							</p>
						</li>
					</c:forEach>
				</ul>
			</div>

			<div class="form-actions" style="margin-left: 20%;margin-top:30px;">
				<shiro:hasPermission name="gcontest:gContestAnnounce:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="保 存" />&nbsp;
				</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回"
					onclick="history.go(-1)" />
			</div>
		</form:form>
	</div>
	<!-- 引用ajaxfileupload.js -->
	<script src="${ctxCommon}/common-js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${ctxJs}/gcontest/gcontestAnnounce.js"></script>
	<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
	<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="${ctxJs}/common.js"></script>
	<script src="${ctxJs}/fileUpLoad.js" type="text/javascript"></script>
</body>
</html>