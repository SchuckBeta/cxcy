<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>区域管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="cmsIndexRegion" action="${ctx}/cms/cmsIndexRegion/save" method="post"
               class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <div class="control-group">
            <label class="control-label">上级栏目:</label>
            <div class="controls">
                <sys:treeselect id="category" name="category.id"
                                value="${cmsIndexRegion.category.id}" labelName="category.name"
                                labelValue="${cmsIndexRegion.category.name}" title="栏目"
                                url="/cms/category/treeData" extId="${category.id}"
                                cssClass="required" allowClear="true" notAllowSelectParent="true"
                                cssStyle="width: 175px;"/>
            </div>
        </div>
        <%-- <div class="control-group">
            <label class="control-label"><span class="help-inline"><font color="red">*&nbsp;</font> </span>区域编号：</label>
            <div class="controls">
                <form:input path="regionId" htmlEscape="false" maxlength="64" class="input-xlarge required"/>
            </div>
        </div> --%>
        <div class="control-group">
            <label class="control-label"><i>*</i>区域名：</label>
            <div class="controls">
                <form:input path="regionName" htmlEscape="false" maxlength="64" class="required"/>
            </div>
        </div>
        <%-- <div class="control-group">
            <label class="control-label"><span class="help-inline"><font color="red">*&nbsp;</font> </span>区域模式：</label>
            <div class="controls">
                <form:select path="regionModel" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('region_model')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><span class="help-inline"><font color="red">*&nbsp;</font> </span>区域类型：</label>
            <div class="controls">
                <form:select path="regionType" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('regiontype_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div> --%>
        <div class="control-group">
            <label class="control-label"><i>*</i>区域状态：</label>
            <div class="controls">
                <form:select path="regionState" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('regionstate_flag')}" itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i>*</i>区域排序：</label>
            <div class="controls">
                <form:input path="regionSort" htmlEscape="false" maxlength="64" class="required"/>
            </div>
        </div>
        <div class="form-actions">
            <shiro:hasPermission name="cms:cmsIndexRegion:edit">
                <button class="btn btn-primary" type="submit">保存</button>
            </shiro:hasPermission>
            <button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>
</body>
</html>