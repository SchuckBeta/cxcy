<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>系统证书管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">
    <script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/uploadCutImage.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
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
	</script>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>系统证书</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/sysCertificate/">系统证书列表</a></li>
			<li class="active"><a href="${ctx}/sys/sysCertificate/form?id=${sysCertificate.id}">系统证书<shiro:hasPermission name="sys:sysCertificate:edit">${not empty sysCertificate.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysCertificate:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="sysCertificate" action="${ctx}/sys/sysCertificate/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">证书名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">证书编号：</label>
				<div class="controls">
					<form:input path="no" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">证书类型：</label>
				<div class="controls">
					<form:select path="type" class="input-xlarge ">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('sys_certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<%-- <div class="control-group">
				<label class="control-label">证书附件：</label>
				<div class="controls">
					<img id="logoText1" src="${empty proProject.imgUrl ? '/images/upload.png' : fns:ftpImgUrl(site.logo)}" style="display:block;max-width: 300px;">
	                <button type="button" id="filePickerLesson1" class="btn">上传</button>
	                <span class="help-inline">建议Logo大小：300 × 600（像素）</span>
 	            </div>
			</div>
			<div class="control-group">
				<label class="control-label">水印附件：</label>
				<div class="controls">
                    <img id="logoText2" src="${empty proProject.imgUrl ? '/images/upload.png' : fns:ftpImgUrl(site.logo)}" style="display:block;max-width: 50px;">
	                <button type="button" id="filePickerLesson2" class="btn">上传</button>
	                <span class="help-inline">建议Logo大小：50 × 100（像素）</span>
	            </div>
			</div> --%>
			<div class="control-group">
				<label class="control-label">所属机构：</label>
				<div class="controls">
					<sys:treeselect id="office" name="office.id" value="${sysCertificate.office.id}" labelName="office.name" labelValue="${sysCertificate.office.name}"
						title="部门" url="/sys/office/treeData" isAll="true" cssClass="" allowClear="true" notAllowSelectParent="false"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">说明：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="sys:sysCertificate:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
		<sys:upLoadCutImage width="300" height="600" btnid="filePickerLesson1" column="sysCertificate.zsfile" imgId="logoText1" filepath="sysCertificate/10000" className="modal-avatar hide" modalId="modalAvatar1" aspectRatio="1/2"></sys:upLoadCutImage>
		<sys:upLoadCutImage width="50" height="100" btnid="filePickerLesson2" column="sysCertificate.waterfile" imgId="logoText2" filepath="sysCertificate/10001" className="modal-avatar hide" modalId="modalAvatar2" aspectRatio="1/2"></sys:upLoadCutImage>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>