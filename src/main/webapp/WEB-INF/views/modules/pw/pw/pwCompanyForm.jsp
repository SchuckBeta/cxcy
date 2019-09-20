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
                    }else if(element.attr('name') === 'regMoney'){
                        error.appendTo(element.parent());
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
            <span>入驻企业</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="content_panel">
        <ul class="nav nav-tabs">
            <li><a href="${ctx}/pw/pwCompany/">入驻企业列表</a></li>
            <li class="active">
                <a href="${ctx}/pw/pwCompany/form?id=${pwCompany.id}">入驻企业
                    <shiro:hasPermission name="pw:pwCompany:edit">
                        ${not empty pwCompany.id?'修改':'添加'}</shiro:hasPermission>
                    <shiro:lacksPermission name="pw:pwCompany:edit">查看</shiro:lacksPermission></a></li>
        </ul>
        <br/>
        <form:form id="inputForm" modelAttribute="pwCompany" action="${ctx}/pw/pwCompany/save" method="post"
                   class="form-horizontal clearfix form-search-block">
            <form:hidden path="id"/>
            <sys:message content="${message}"/>
            <div class="control-group">
                <label class="control-label"><i>*</i>执照编号：</label>
                <div class="controls">
                    <form:input path="no" htmlEscape="false" maxlength="64" cssClass="required letterNumber"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>名称：</label>
                <div class="controls">
                    <form:input path="name" htmlEscape="false" maxlength="255" cssClass="required"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>手机：</label>
                <div class="controls">
                    <form:input path="mobile" htmlEscape="false" maxlength="20" cssClass="phone_number required"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">电话：</label>
                <div class="controls">
                    <form:input path="phone" htmlEscape="false" maxlength="200" cssClass="phone_number"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>注册资金：</label>
                <div class="controls">
                    <form:input path="regMoney" htmlEscape="false" maxlength="7" min="0"
                                class="input-mini number required"/> 万元
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>资金来源：</label>
                <div class="controls controls-checkbox">
                    <form:checkboxes path="regMtypes" items="${fns:getDictList('pw_reg_mtype')}" itemLabel="label"
                                     itemValue="value" htmlEscape="false" class="required"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>法人：</label>
                <div class="controls">
                    <form:input path="regPerson" htmlEscape="false" cssClass="required"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>地址：</label>
                <div class="controls">
                    <form:input path="address" htmlEscape="false" maxlength="50" minlength="2"
                                class="input-xxlarge required"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">备注：</label>
                <div class="controls">
                    <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>
                </div>
            </div>
            <div class="form-actions">
                <shiro:hasPermission name="pw:pwCompany:edit"><input id="btnSubmit" class="btn btn-primary"
                                                                     type="submit"
                                                                     value="保 存"/>&nbsp;</shiro:hasPermission>
                <input id="btnCancel" class="btn btn-default" type="button" value="返 回" onclick="history.go(-1)"/>
            </div>
        </form:form>
    </div>
    <div id="dialog-message" title="信息">
        <p id="dialog-content"></p>
    </div>
</div>

</body>
</html>