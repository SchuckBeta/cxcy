<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<title>流程节点管理</title>
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
	<link rel="stylesheet" type="text/css" href="${ctxCss}/course/webuploadLesson.css">
	<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
	<script type="text/javascript" src="${ctxJs}/actyw/uploadCover.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					var iconUrl = $("#iconUrl").val();
					if(iconUrl == ""){
						alertx("请上传图标");
					}else{
						loading('正在提交，请稍等...');
						form.submit();
					}
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
		<span>流程节点</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/actyw/actYwNode/">流程节点列表</a></li>
			<li class="active"><a href="${ctx}/actyw/actYwNode/form?id=${actYwNode.id}">流程节点<shiro:hasPermission name="actyw:actYwNode:edit">${not empty actYwNode.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="actyw:actYwNode:edit">查看</shiro:lacksPermission></a></li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="actYwNode" action="${ctx}/actyw/actYwNode/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">节点名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">业务模块：</label>
				<div class="controls">
					<form:select path="type" class="input-xlarge required">
						<form:option value="" label="--请选择--"/>
						<form:options items="${nodeEtypes}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
                        <div class="control-group">
                                <label class="control-label">是否有表单:</label>
                                <div class="controls">
                                        <form:select id="isForm" path="isForm" class="input-xlarge required">
                                                <form:option value="" label="--请选择--"/>
                                                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                        <span class="help-inline"><font color="red">*</font> </span>
                                </div>
                        </div>
                        <div class="control-group">
                                <label class="control-label">是否可见:</label>
                                <div class="controls">
                                        <form:select id="isVisible" path="isForm" class="input-xlarge required">
                                                <form:option value="" label="--请选择--"/>
                                                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                        <span class="help-inline"><font color="red">*</font> </span>
                                </div>
                        </div>
                        <div class="control-group">
                                <label class="control-label">流程标识类型：</label>
                                <div class="controls">
                                         <form:select id="nodeType" path="nodeType" class="input-xlarge required">
                                           <form:option value="" label="--请选择--"/>
                                           <option value="node" <c:if test="${actYwNode.nodeType eq 'node'}"> selected='selected' </c:if> >节点</option>
                                           <option value="edge" <c:if test="${actYwNode.nodeType eq 'edge'}"> selected='selected' </c:if> >连接</option>
                                        </form:select>
                                </div>
                        </div>
                        <div class="control-group">
                                <label class="control-label">流程标识Key：</label>
                                <div class="controls">
                                        <form:input path="nodeKey" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
                                        <span class="help-inline"><font color="red">*</font> </span>
                                </div>
                        </div>
	                <div class="control-group">
			   <label class="control-label"><font color="red">*</font> 图标:</label>
			   <form:hidden path="iconUrl"  />
			   <div class="controls">
				   <ul id="uploadList" style="width: 150px;">
					   <c:if test="${ not empty  actYwNode.iconUrl}">
						   <li class="file-item thumbnail">
							   <img  src="${fns:ftpImgUrl(actYwNode.iconUrl)}"  heigth="100" width="100"/>
						   </li>
					   </c:if>
				   </ul>
				   <div id="filePicker">上传图标</div>
			   </div>
		        </div>
                        <div class="control-group">
                                <label class="control-label">默认图标：</label>
                                <div class="controls">
                                        <form:input path="nodeIcon" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
                                </div>
                        </div>
                        <div class="control-group">
                                <label class="control-label">UI可执行的操作：</label>
                                <div class="controls">
                                        <form:input path="operate" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
                                </div>
                        </div>
                        <div class="control-group">
                                <label class="control-label">允许执行的操作：</label>
                                <div class="controls">
                                        <form:input path="nodeRoles" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
                                </div>
                        </div>
			<div class="control-group">
				<label class="control-label">Xml结构:</label>
				<div class="controls">
					<form:textarea path="nodeXml" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">UI结构:</label>
				<div class="controls">
					<form:textarea path="nodeJson" htmlEscape="false" rows="4" maxlength="2000" class="input-xxlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">备注:</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
				</div>
			</div>
			<%-- <script type="text/javascript">
				$(function(){
					changeFormNode();
				});

				function changeFormNode(){
					var isForm = $("#isForm");
					var formIdDiv = $("#formIdDiv");

					if(($(isForm).val() == '1')?false:true){
						$(formIdDiv).hide();
						$(formIdDiv).find("select").removeClass("required");
					}

					$(isForm).change(function(){
						var isFormVal = ($(this).val() == '1')?true:false;
						if(isFormVal){
							$(formIdDiv).show();
							$(formIdDiv).find("select").addClass("required");
						}else{
							$(formIdDiv).hide();
							$(formIdDiv).find("select").removeClass("required");
						}
					});
				}
			</script>

			<div class="control-group">
				<label class="control-label">关联角色:</label>
				<div class="controls">
					<form:select id="isGroup" path="isGroup" class="input-xlarge required">
						<form:option value="" label="--请选择--"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
			</div>
			<div id="flowGroupIdDiv" class="control-group">
				<label class="control-label">默认角色:</label>
				<div class="controls">
					<form:select path="flowGroup" class="input-xlarge required">
						<form:option value="" label="所有角色"/>
						<c:forEach items="${roleList}" var="role">
							<form:option value="${role.id}" label="${role.name}"/>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<script type="text/javascript">
				$(function(){
					changeFlowGroupNode();
				});

				function changeFlowGroupNode(){
					var isGroup = $("#isGroup");
					var flowGroupIdDiv = $("#flowGroupIdDiv");

					if(($(isGroup).val() == '1')?false:true){
						$(flowGroupIdDiv).hide();
						$(flowGroupIdDiv).find("select").removeClass("required");
					}

					$(isGroup).change(function(){
						var isGroupVal = ($(this).val() == '1')?true:false;
						if(isGroupVal){
							$(flowGroupIdDiv).show();
							$(flowGroupIdDiv).find("select").addClass("required");
						}else{
							$(flowGroupIdDiv).hide();
							$(flowGroupIdDiv).find("select").removeClass("required");
						}
					});
				}
			</script> --%>
			<div class="form-actions">
				<shiro:hasPermission name="actyw:actYwNode:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
</body>
</html>